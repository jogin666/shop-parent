package com.zy.shop.conpon.application.facade.service;

import com.zy.shop.common.aspect.RequestLogger;
import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopCouponRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopCouponResponse;
import com.zy.shop.common.util.ResultBuilder;
import com.zy.shop.conpon.application.service.ICouponService;
import com.zy.shop.facade.ShopCouponService;
import com.zy.shop.pojo.ShopCoupon;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.zy.shop.common.enums.ShopCouponStatusEnum.*;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.REQUEST_PARAM_EMPTY;

/**
 * @Author: Jong
 * @Date: 2020/12/5 16:13
 */
@Slf4j
@Component
@Service(interfaceClass = ShopCouponService.class)
public class CouponService implements ShopCouponService {

    @Autowired
    private ICouponService couponService;

    @Override
    @RequestLogger(description = "商品优惠卷查询 - by couponId")
    public BaseShopResponse<ShopCouponResponse> findOneById(BaseShopRequest<Long> request) {
        if (request == null || request.getData() == null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        Long couponId = request.getData();
        if (StringUtils.hasLength(String.valueOf(couponId))) {
            String message = ResultBuilder.conditionEmpty("couponId");
            return BaseShopResponse.fail(message);
        }
        ShopCouponResponse response = null;
        log.info("商品优惠卷查询：{}", couponId);
        ShopCoupon tradeCoupon = couponService.findOneById(couponId);
        if (tradeCoupon != null) {
            response = ShopCouponResponse.builder()
                    .couponId(tradeCoupon.getCouponId())
                    .couponMoney(tradeCoupon.getCouponMoney())
                    .status(tradeCoupon.getStatus())
                    .userId(tradeCoupon.getUserId())
                    .useTime(tradeCoupon.getUseTime())
                    .build();
        }
        log.info("商品优惠卷查询 - by couponId 查询结果：{}", response);
        return BaseShopResponse.success(response);
    }

    @Override
    @RequestLogger(description = "更新商品优惠卷状态")
    public BaseShopResponse<ResultEntity> updateCouponStatus(BaseShopRequest<ShopCouponRequest> request) {
        if (Objects.requireNonNull(request).getData() != null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        Long couponId = request.getData().getCouponId();
        if (StringUtils.hasLength(String.valueOf(couponId))) {
            String message = ResultBuilder.conditionEmpty("couponId");
            return BaseShopResponse.fail(message);
        }
        try {
            ShopCouponRequest couponRequest = request.getData();
            ShopCoupon tradeCoupon = new ShopCoupon();
            tradeCoupon.setCouponId(couponId);
            tradeCoupon.setStatus(couponRequest.getStatus());
            tradeCoupon.setUserId(couponRequest.getUserId());
            tradeCoupon.setUseTime(couponRequest.getUseTime());
            tradeCoupon.setCreateTime(couponRequest.getCreateTime());
            log.info("更新优惠卷状态入参：{}", tradeCoupon);
            couponService.updateCouponStatus(tradeCoupon);
            return BaseShopResponse.success(new ResultEntity(SHOP_COUPON_UPDATE_SUCCESS.getCode(), SHOP_COUPON_UPDATE_SUCCESS.getDesc()));
        } catch (Exception e) {
            log.error("更新商品优惠卷状态报错：{}", e.getMessage(), e);
            return BaseShopResponse.fail(SHOP_COUPON_UPDATE_FAIL.toString());
        }
    }
}
