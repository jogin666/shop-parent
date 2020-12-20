package com.zy.shop.user.application.service.impl;

import com.zy.shop.common.enums.ShopUserMoneyStatusEnum;
import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.common.util.RedisLock;
import com.zy.shop.pojo.ShopUser;
import com.zy.shop.pojo.ShopUserUseMoneyLog;
import com.zy.shop.user.application.mapper.ShopUserMapper;
import com.zy.shop.user.application.mapper.ShopUserUseMoneyLogMapper;
import com.zy.shop.user.application.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.zy.shop.common.enums.ShopPaidStatusEnum.*;
import static com.zy.shop.common.enums.ShopUserMoneyStatusEnum.*;

/**
 * @Author: Jong
 * @Date: 2020/12/6 15:03
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private ShopUserMapper userMapper;
    @Autowired
    private ShopUserUseMoneyLogMapper userMoneyLogMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 查询用户
     *
     * @param userId 用户Id
     * @return 用户
     */
    @Override
    public ShopUser findOneById(Long userId) {
        return userMapper.findOneById(userId);
    }

    /**
     * 更新用户金额
     *
     * @param shopUser     用户信息
     * @param userMoneyLog 用户支付记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean updateUserMoney(ShopUser shopUser, ShopUserUseMoneyLog userMoneyLog) {
        if (userMoneyLog != null) {
            userMoneyLogMapper.updateUseMoneyLogStatus(userMoneyLog);
        }
        return userMapper.updateUserMoney(shopUser) > 0;
    }

    /**
     * 更新用户余额
     *
     * @param userMoneyLog 用户金额使用记录
     * @return 更新结果
     */
    @Override
    @Transactional
    public boolean updateMoneyPaid(ShopUserUseMoneyLog userMoneyLog) throws ShopBizException {
        final int expireTime = 5;
        // 幂等
        Integer state = (Integer) redisTemplate.opsForValue().get(userMoneyLog.getOrderId());
        if (state != null) {
            ShopUserMoneyStatusEnum[] paidStateEnums = ShopUserMoneyStatusEnum.values();
            for (ShopUserMoneyStatusEnum paidStateEnum : paidStateEnums) {
                if (paidStateEnum.getCode().intValue() == state.intValue()) {
                    throw new ShopBizException(paidStateEnum.toString());
                }
            }
        }
        try {
            redisTemplate.opsForValue().set(userMoneyLog.getOrderId(), SHOP_USER_MONEY_STATUS_UPDATING.getCode(), expireTime, TimeUnit.MINUTES);
            // 加锁
            RedisLock.lock(redisTemplate, String.valueOf(userMoneyLog.getOrderId()));
            log.info("用户支付订单 - reids 加锁成功,key：{}", userMoneyLog.getOrderId());
            ShopUser shopUser = userMapper.findOneById(userMoneyLog.getUserId());
            // 扣减余额
            if (userMoneyLog.getType().intValue() == SHOP_PAY_STATUS_UNPAID.getCode()) {
                reduceUserMoney(userMoneyLog, shopUser);
                log.info("用户支付订单金额成功，支付金额：{}", (-userMoneyLog.getMoney().intValue()));
            }
            // 回滚余额
            if (userMoneyLog.getType().intValue() == SHOP_PAY_STATUS_REFUND.getCode()) {
                rollbackUsedMoney(userMoneyLog, shopUser);
                log.info("用户订单：{} 退款成功，退款金额：{}", userMoneyLog.getUserId(), userMoneyLog.getMoney().intValue());
            }
            // 解锁
            RedisLock.unlockLua(redisTemplate, String.valueOf(userMoneyLog.getOrderId()));
        } catch (Exception e) {
            log.error("用户金额更新失败：{}", e.getMessage(), e);
            throw new ShopBizException("用户金额更新出错", e);
        }
        return true;
    }

    /**
     * 扣减用户的金额
     *
     * @param userMoneyLog 用户金额使用记录
     * @param shopUser     用户信息
     */
    private void reduceUserMoney(ShopUserUseMoneyLog userMoneyLog, ShopUser shopUser) throws ShopBizException {

        if (hasPaid(userMoneyLog.getUserId(), userMoneyLog.getOrderId())) {
            log.error("用户：{} 的支付订单：{} 已付款", userMoneyLog.getUserId(), userMoneyLog.getOrderId());
            throw new ShopBizException(SHOP_PAY_STATUS_PAID.toString());
        }
        BigDecimal money = shopUser.getMoney().subtract(userMoneyLog.getMoney());
        shopUser.setMoney(money);
        this.updateUserMoney(shopUser, null);
        userMoneyLog.setCreateTime(new Timestamp(new Date().getTime()));
        userMoneyLogMapper.saveUseMoneyLog(userMoneyLog);
        redisTemplate.opsForValue().set(userMoneyLog.getOrderId(), SHOP_PAY_STATUS_PAID.getCode());
    }

    /**
     * 回滚用户使用的金额
     *
     * @param userMoneyLog 用户支付记录
     * @param shopUser     用户信息
     */
    private void rollbackUsedMoney(ShopUserUseMoneyLog userMoneyLog, ShopUser shopUser) throws ShopBizException {

        if (!hasPaid(userMoneyLog.getUserId(), userMoneyLog.getOrderId())) {
            log.error("用户：{} 的支付订单：{} 未支付", userMoneyLog.getUserId(), userMoneyLog.getOrderId());
            throw new ShopBizException(SHOP_PAY_STATUS_ROLLBACK_FAIL2.toString());
        }
        if (hasRollbackMoney(userMoneyLog)) {
            log.error("用户：{} 的支付订单：{} 已退款", userMoneyLog.getUserId(), userMoneyLog.getOrderId());
            throw new ShopBizException(SHOP_PAY_STATUS_ROLLBACK_FAIL1.toString());
        }
        //退款
        shopUser.setMoney(shopUser.getMoney().subtract(userMoneyLog.getMoney()));
        userMapper.updateUserMoney(shopUser);
        // 记录当订单操作
        userMoneyLog.setCreateTime(new Timestamp(new Date().getTime()));
        userMoneyLogMapper.saveUseMoneyLog(userMoneyLog);
        redisTemplate.opsForValue().set(userMoneyLog.getOrderId(), SHOP_PAY_STATUS_ROLLBACK_SUCCESS.getCode());
    }

    /**
     * 查询是否支付过订单
     *
     * @param userId  用户Id
     * @param orderId 订单Id
     * @return 是否支付
     */
    private boolean hasPaid(Long userId, Long orderId) {
        // 查询订单余额使用日志
        ShopUserUseMoneyLog userUseMoneyLoge = new ShopUserUseMoneyLog();
        userUseMoneyLoge.setOrderId(orderId);
        userUseMoneyLoge.setUserId(userId);
        userUseMoneyLoge.setType(SHOP_PAY_STATUS_PAID.getCode());
        int count = userMoneyLogMapper.countUserUseMoney(userUseMoneyLoge);
        return count > 0;
    }

    /**
     * 查询是否回滚过金额
     *
     * @param userMoneyLog 用户金额历史
     * @return 是否已回滚过金额
     */
    private boolean hasRollbackMoney(ShopUserUseMoneyLog userMoneyLog) {
        userMoneyLog.setType(SHOP_USER_MONEY_STATUS_ROLLBACK_SUCCESS.getCode());
        int result = userMoneyLogMapper.countUserUseMoney(userMoneyLog);
        return result > 0;
    }
}
