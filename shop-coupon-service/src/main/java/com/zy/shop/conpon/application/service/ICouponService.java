package com.zy.shop.conpon.application.service;

import com.zy.shop.pojo.ShopCoupon;

/**
 * @author: jogin
 * @date: 2020/12/5 15:34
 */
public interface ICouponService {

    /**
     * 根据 couponId 查询 coupon
     *
     * @param couponId
     * @return
     */
    ShopCoupon findOneById(long couponId);

    /**
     * 更新 coupon
     *
     * @param tradeCoupon
     * @return
     */
    int updateCouponStatus(ShopCoupon tradeCoupon);
}
