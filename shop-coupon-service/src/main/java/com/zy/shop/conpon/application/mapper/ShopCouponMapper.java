package com.zy.shop.conpon.application.mapper;

import com.zy.shop.pojo.ShopCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:52
 */
@Mapper
public interface ShopCouponMapper {

    ShopCoupon findOneById(Long couponId);

    int updateCoupon(ShopCoupon coupon);
}
