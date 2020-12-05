package com.zy.shop.conpon.application.service.impl;

import com.zy.shop.conpon.application.mapper.ShopCouponMapper;
import com.zy.shop.conpon.application.service.ICouponService;
import com.zy.shop.pojo.ShopCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: jogin
 * @date: 2020/12/5 16:04
 */

@Service
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private ShopCouponMapper couponMapper;

    /**
     * 查询商品优惠卷
     *
     * @param couponId 优惠卷 Id
     * @return 优惠卷
     */
    @Override
    public ShopCoupon findOneById(long couponId) {
        return couponMapper.findOneById(couponId);
    }

    /**
     * 更新优惠卷状态
     *
     * @param tradeCoupon 优惠卷
     * @return 更新数量
     */
    @Override
    @Transactional
    public int updateCouponStatus(ShopCoupon tradeCoupon) {
        return couponMapper.updateCoupon(tradeCoupon);
    }
}
