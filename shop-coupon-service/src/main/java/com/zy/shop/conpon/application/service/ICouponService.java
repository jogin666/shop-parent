package com.zy.shop.conpon.application.service;

import com.zy.shop.pojo.ShopCoupon;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:34
 */
public interface ICouponService {

    /**
     * 根据 couponId 查询 coupon
     *
     * @param couponId 优惠卷Id
     * @return 商品优惠卷
     */
    ShopCoupon findOneById(long couponId);

    /**
     * 更新 coupon
     *
     * @param tradeCoupon 山沟i你优惠卷
     * @return 更新数量
     */
    int updateCouponStatus(ShopCoupon tradeCoupon);
}
