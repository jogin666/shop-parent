package com.zy.shop.user.application.facade.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopUserUseMoneyLogRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopUserResponse;
import com.zy.shop.common.util.ResultBuilder;
import com.zy.shop.facade.ShopUserService;
import com.zy.shop.pojo.ShopUser;
import com.zy.shop.pojo.ShopUserUseMoneyLog;
import com.zy.shop.user.application.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import static com.zy.shop.common.enums.ShopUserMoneyStatusEnum.SHOP_USER_MONEY_STATUS_REDUCE_FAIL;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.*;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.REQUEST_RESULT_SUCCESS;

/**
 * @author: jogin
 * @date: 2020/12/6 15:28
 */
@Slf4j
@Component
@Service(interfaceClass = ShopUserService.class)
public class UserService implements ShopUserService {

    @Autowired
    private IUserService userService;

    @Override
    public BaseShopResponse<ShopUserResponse> findOneById(BaseShopRequest<Long> request) {
        if (request == null || StringUtils.isEmpty(request.getData())) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        Long goodsId = request.getData();
        if (StringUtils.hasLength(String.valueOf(goodsId))) {
            String message = ResultBuilder.conditionEmpty("userId");
            return BaseShopResponse.fail(message);
        }
        ShopUserResponse response = null;
        ShopUser shopUser = userService.findOneById(goodsId);
        if (shopUser!=null){
            response = newShopUserResponse(shopUser);
        }
        return BaseShopResponse.success(response);
    }

    @Override
    public BaseShopResponse<ResultEntity> updateMoneyPaid(BaseShopRequest<ShopUserUseMoneyLogRequest> request) {
        if (request == null || request.getData() == null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        ShopUserUseMoneyLogRequest data = request.getData();
        if (data.getUserId() == null || data.getOrderId() == null
                || data.getMoney() == null || data.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            return BaseShopResponse.fail(REQUEST_PARAM_ERROR.toString());
        }
        try{
            if (userService.updateMoneyPaid(newTradeUserMoneyLog(data))){
                return BaseShopResponse.success(new ResultEntity(REQUEST_RESULT_SUCCESS.getCode(), REQUEST_RESULT_SUCCESS.getDes()));
            }
        }catch (Exception e){
            log.error("更新用户余额报错：{}",e.getMessage(),e);
            return BaseShopResponse.fail(SHOP_USER_MONEY_STATUS_REDUCE_FAIL.toString());
        }
        return  null;
    }

    private ShopUserResponse newShopUserResponse(ShopUser shopUser){
        return  ShopUserResponse.builder()
                .userId(shopUser.getUserId())
                .telPhone(shopUser.getTelPhone())
                .money(shopUser.getMoney())
                .name(shopUser.getName())
                .password(shopUser.getPassword())
                .account(shopUser.getAccount())
                .address(shopUser.getAddress())
                .age(shopUser.getAge())
                .build();
    }

    private ShopUserUseMoneyLog newTradeUserMoneyLog(ShopUserUseMoneyLogRequest userMoneyLogRequest){
        ShopUserUseMoneyLog userMoneyLog = new ShopUserUseMoneyLog();
        userMoneyLog.setCreateTime(userMoneyLogRequest.getCreateTime());
        userMoneyLog.setType(userMoneyLogRequest.getType());
        userMoneyLog.setOrderId(userMoneyLogRequest.getOrderId());
        userMoneyLog.setMoney(userMoneyLogRequest.getMoney());
        userMoneyLog.setUserId(userMoneyLogRequest.getUserId());
        return userMoneyLog;
    }
}

