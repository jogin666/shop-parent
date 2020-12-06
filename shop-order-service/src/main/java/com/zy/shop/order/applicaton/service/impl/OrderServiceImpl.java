package com.zy.shop.order.applicaton.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zy.shop.common.dto.mq.MQMessageEntity;
import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopCouponRequest;
import com.zy.shop.common.dto.request.ShopGoodsRequest;
import com.zy.shop.common.dto.request.ShopUserUseMoneyLogRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopCouponResponse;
import com.zy.shop.common.dto.response.ShopGoodsResponse;
import com.zy.shop.common.dto.response.ShopUserResponse;
import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.common.util.IDWorker;
import com.zy.shop.facade.ShopCouponService;
import com.zy.shop.facade.ShopGoodsService;
import com.zy.shop.facade.ShopUserService;
import com.zy.shop.order.applicaton.mapper.ShopOrderMapper;
import com.zy.shop.order.applicaton.service.IOrderService;
import com.zy.shop.pojo.ShopOrder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import static com.zy.shop.common.enums.ShopCouponStatusEnum.*;
import static com.zy.shop.common.enums.ShopGoodsStatusEnum.*;
import static com.zy.shop.common.enums.ShopOrderStatusEnum.*;
import static com.zy.shop.common.enums.ShopPaidStatusEnum.SHOP_PAY_STATUS_UNPAID;
import static com.zy.shop.common.enums.ShopUserStatusEnum.*;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.REQUEST_RESULT_SUCCESS;

/**
 * @author: jogin
 * @date: 2020/12/6 16:16
 */

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private ShopOrderMapper orderMapper;
    @Value("${mq.order.topic}")
    private String topic;
    @Value("${mq.order.tag.cancel}")
    private String tag;
    @Reference
    private ShopGoodsService goodsService;
    @Reference
    private ShopUserService userService;
    @Reference
    private ShopCouponService couponService;
    @Autowired
    private IDWorker idWorker;

    private final String CKECK_ORDER_OK = "OK";
    private final String CKECK_ORDER_PRE_OK = "PRE_OK";


    @Override
    public boolean confirmOrder(ShopOrder order) throws ShopBizException {
        // 检查订单
        checkOrder(order);
        try {
            String flag = (String) redisTemplate.opsForValue().get(order.getOrderId());
            if (flag != null && CKECK_ORDER_OK.equals(flag)) {
                throw new ShopBizException(SHOP_ORDER_STATUS_CONFIRM_FAIL.toString());
            }
            if (flag != null && CKECK_ORDER_PRE_OK.equals(flag)){
                throw new ShopBizException(SHOP_ORDER_STATUS_CONFIRM_PROCESSING.toString());
            }
            redisTemplate.opsForValue().setIfAbsent(order.getOrderId(),CKECK_ORDER_PRE_OK);
            // 保存订单
            savePreOrder(order);
            //扣减商品库存
            reduceGoodsNum(order);
            // 使用优惠卷
            updateCouponStatus(order);
            // 扣减用户余额
            reduceMoneyPaid(order);
            // 更新订单状态
            updateOrderStatus(order);

            redisTemplate.opsForValue().setIfPresent(order.getOrderId(),CKECK_ORDER_PRE_OK);
        } catch (Exception e) {
            return invokeException(order);
        }
        return true;
    }

    private void updateOrderStatus(ShopOrder order) throws ShopBizException {
        order.setUpdateTime(new Timestamp(new Date().getTime()));
        order.setStatus(SHOP_ORDER_STATUS_CONFIRM.getCode());
        int row = orderMapper.updateShopOrder(order);
        if (row < 0) {
            throw new ShopBizException(SHOP_ORDER_STATUS_NOT_FOUND.toString());
        }
        log.info("成功更新订单：{} 状态为确认状态", order.getOrderId());
    }

    @Override
    public boolean saveOrder(ShopOrder order) throws ShopBizException {
        ShopOrder shopOrder = (ShopOrder) redisTemplate.opsForValue().get(order.getOrderId());
        if (shopOrder != null) {
            throw new ShopBizException(SHOP_ORDER_STATUS_NOT_FOUND.toString());
        }
        int row = orderMapper.saveOrder(order);
        redisTemplate.opsForValue().setIfAbsent(order.getOrderId(), order);
        log.info("订单：{} 保存成功", order.getOrderId());
        return row > 0;
    }

    /**
     * 检查订单
     *
     * @param order
     */
    private void checkOrder(ShopOrder order) throws ShopBizException {
        //1.校验订单是否存在
        if (order == null) {
            log.info("校验订单：{} 失败，订单不存在", order.getOrderId());
            throw new ShopBizException(SHOP_ORDER_STATUS_NOT_FOUND.toString());
        }
        //2.校验订单中的商品是否存在
        BaseShopResponse<ShopGoodsResponse> baseShopGoodsResponse = goodsService.findOneById(new BaseShopRequest<Long>(order.getGoodsId(), null));
        if (baseShopGoodsResponse == null || baseShopGoodsResponse.getData() == null) {
            log.info("校验订单：{} 失败，商品：{} 不存在", order.getOrderId(), order.getGoodsId());
            throw new ShopBizException(SHOP_GOODS_STATUS_NOT_FOUND.toString());
        }
        //3.校验下单用户是否存在
        BaseShopResponse<ShopUserResponse> baseShopUserResponse = userService.findOneById(new BaseShopRequest<Long>(order.getUserId(), null));
        if (baseShopUserResponse == null || baseShopUserResponse.getData() == null) {
            log.info("校验订单：{} 失败，用户：{} 不存在", order.getOrderId(), order.getUserId());
            throw new ShopBizException(SHOP_USER_STATUS_NO_EXIST.toString());
        }
        //4.校验商品单价是否合法
        if (order.getGoodsPrice().compareTo(baseShopGoodsResponse.getData().getGoodsPrice()) != 0) {
            log.info("校验订单：{} 失败，商品单价:{} 不准确", order.getOrderId(), order.getGoodsPrice());
            throw new ShopBizException(SHOP_GOOD_STATUS_PRICE_INVALID.toString());
        }
        //5.校验订单商品数量是否合法
        if (order.getGoodsNumber() >= baseShopGoodsResponse.getData().getNumber()) {
            log.info("校验订单：{} 失败，商品：{} 库存不足", order.getOrderId(), order.getGoodsId());
            throw new ShopBizException(SHOP_GOODS_STATUS_NUMBER_INSUFFICIENT.toString());
        }
        log.info("校验订单：{} 通过", order.getOrderId());
    }

    /**
     * 保存订单
     *
     * @param order
     * @return
     */
    private Long savePreOrder(ShopOrder order) throws ShopBizException {
        order.setOrderId(idWorker.nextId());
        // 核算订单运费
        order.setStatus(SHOP_ORDER_STATUS_NO_CONFIRM.getCode());
        // 判断用户是否使用余额
        BigDecimal moneyPaid = order.getMoneyPaid();
        if (moneyPaid != null) {
            // 订单中用户使用的余额是否合法
            int result = moneyPaid.compareTo(BigDecimal.ZERO);
            // 余额小于 0
            if (result == -1) {
                log.warn("无法生成用户订单，用户订单使用的用户余额：{} 小于 0", moneyPaid);
                throw new ShopBizException(SHOP_USER_STATUS_MONEY_PAID_LESS_ZERO.toString());
            }
            // 余额大于 0
            if (result == 1) {
                BaseShopRequest<Long> request = new BaseShopRequest<>();
                request.setData((order.getUserId()));
                BaseShopResponse<ShopUserResponse> response = userService.findOneById(request);
                if (response != null && response.getData() != null) {
                    if (moneyPaid.compareTo(response.getData().getMoney()) == 0) {
                        log.warn("无法生成用户订单，用户订单使用的用户余额：{} 非法", moneyPaid);
                        throw new ShopBizException(SHOP_USER_STATUS_MONEY_PAID_INVALID.toString());
                    }
                }
            }
        } else {
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        // 判断用户是否使用优惠卷
        Long couponId = order.getCouponId();
        if (couponId != null) {
            BaseShopRequest<Long> request = new BaseShopRequest<Long>();
            request.setData(order.getCouponId());
            BaseShopResponse<ShopCouponResponse> response = couponService.findOneById(request);
            if (response == null || response.getData() != null) {
                log.warn("无法生成用户订单，用户订单使用的优惠卷查询不到：{} ", order.getCouponId());
                throw new ShopBizException(SHOP_COUPON_NO_EXIST.toString());
            }
            if (response != null && response.getData() != null) {
                if (SHOP_COUPON_USED.getCode().intValue() == response.getData().getStatus()) {
                    log.warn("无法生成用户订单，用户订单使用的优惠卷查询已被使用过：{} ", order.getCouponId());
                    throw new ShopBizException(SHOP_COUPON_USED.toString());
                }
            }
            order.setCouponMoney(response.getData().getCouponMoney());
        } else {
            order.setCouponMoney(BigDecimal.ZERO);
        }
        // 核算订单支付金额    订单总金额-余额-优惠券金额
        BigDecimal payAmount = order.getTotalMoney().subtract(order.getMoneyPaid()).subtract(order.getCouponMoney());
        order.setTotalMoney(payAmount);
        order.setCreateTime(new Timestamp(new Date().getTime()));
        orderMapper.saveOrder(order);
        log.info("用户订单使用优惠卷：{}", order.getCouponId());
        log.info("成功保存用户订单：{}", order.getOrderId());
        return order.getOrderId();
    }

    /**
     * 计算商品运费
     *
     * @param orderAmount
     * @return
     */
    private BigDecimal calculateShippingFee(BigDecimal orderAmount) {
        if (orderAmount.compareTo(new BigDecimal(100)) == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(100);
    }

    /**
     * 扣减库存
     *
     * @param order
     */
    private boolean reduceGoodsNum(ShopOrder order) {
        ShopGoodsRequest data = new ShopGoodsRequest();
        data.setGoodsId(order.getGoodsId());
        data.setOrderId(order.getOrderId());
        data.setNumber(order.getGoodsNumber());
        BaseShopResponse<ResultEntity> response = goodsService.reduceGoodsNumber(new BaseShopRequest<ShopGoodsRequest>(data, null));
        if (response != null && response.getData() != null
                && response.getData().getCode().intValue() == REQUEST_RESULT_SUCCESS.getCode().intValue()) {
            log.info("订单：{} 扣减库存成功", order.getOrderId());
            return true;
        }
        log.info("订单：{} 扣减库存失败：{}", order.getOrderId(), response.getData());
        return false;
    }

    /**
     * 使用用户余额
     *
     * @param order
     */
    private void reduceMoneyPaid(ShopOrder order) {
        if (order.getStatus() != null && order.getTotalMoney().compareTo(BigDecimal.ZERO) == 1) {
            ShopUserUseMoneyLogRequest request = new ShopUserUseMoneyLogRequest();
            request.setOrderId(order.getOrderId());
            request.setUserId(order.getUserId());
            request.setMoney(order.getTotalMoney());
            request.setType(SHOP_PAY_STATUS_UNPAID.getCode());
            BaseShopRequest baseShopRequest = new BaseShopRequest();
            baseShopRequest.setData(request);
            BaseShopResponse<ResultEntity> response = userService.updateMoneyPaid(baseShopRequest);
            if (response != null || response.getData() != null) {
                if (response.getData().getCode().intValue() == REQUEST_RESULT_SUCCESS.getCode().intValue()) {
                    log.info("用户订单:{} 扣减用户余额：{} 成功", order.getOrderId(), order.getTotalMoney());
                }
            }
        }
    }

    /**
     * 更新商品优惠卷状态
     *
     * @param order
     */
    private void updateCouponStatus(ShopOrder order) throws ShopBizException {
        if (order.getCouponId() == null) {
            return;
        }
        BaseShopRequest<Long> request = new BaseShopRequest();
        request.setData(order.getCouponId());
        BaseShopResponse<ShopCouponResponse> response = couponService.findOneById(request);
        if (response == null || response.getData() == null) {
            log.warn("查询不到用户使用的商品优惠卷：{}", order.getCouponId());
            throw new ShopBizException(SHOP_COUPON_NO_EXIST.toString());
        }
        ShopCouponRequest couponRequest = new ShopCouponRequest();
        couponRequest.setCouponMoney(response.getData().getCouponMoney());
        couponRequest.setStatus(SHOP_COUPON_USED.getCode());
        couponRequest.setUserId(order.getUserId());
        couponRequest.setUseTime(new Timestamp(new Date().getTime()));

        //更新优惠券状态
        BaseShopRequest<ShopCouponRequest> baseRequest = new BaseShopRequest<>();
        BaseShopResponse<ResultEntity> updateCouponStatusResponse = couponService.updateCouponStatus(baseRequest);
        if (updateCouponStatusResponse != null && updateCouponStatusResponse.getData() != null
                && updateCouponStatusResponse.getData().getCode().intValue() == SHOP_COUPON_UPDATE_SUCCESS.getCode().intValue()) {
            log.info("成功更新商品优惠卷：{}", order.getCouponId());
            return;
        }
        throw new ShopBizException(SHOP_COUPON_USE_FAIL.toString());
    }

    private boolean invokeException(ShopOrder order) {
        //1.确认订单失败,发送消息
        MQMessageEntity mqEntity = new MQMessageEntity();
        mqEntity.setOrderId(order.getOrderId());
        mqEntity.setUserId(order.getUserId());
        mqEntity.setUseMoney(order.getTotalMoney());
        mqEntity.setGoodId(order.getGoodsId());
        mqEntity.setGoodNumber(order.getGoodsNumber());
        mqEntity.setCouponId(order.getCouponId());
        try {
            String body = JSON.toJSONString(mqEntity);
            Message message = new Message(topic, tag, String.valueOf(order.getOrderId()), body.getBytes());
            rocketMQTemplate.getProducer().send(message);
        } catch (Exception e) {
            log.error("发送回滚确定订单的 MQ 消息事变", e.getMessage(), e);
            return false;
        }
        return true;
    }
}
