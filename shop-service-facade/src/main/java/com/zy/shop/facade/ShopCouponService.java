package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopCouponRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopCouponResponse;

/**
 * @Author: Jong
 * @Date: 2020/12/5 16:45
 */
public interface ShopCouponService {

    /**
     * 根据 couponId 查询 coupon
     *
     * @param request 优惠卷查询请求
     * @return 优惠卷信息
     */
    BaseShopResponse<ShopCouponResponse> findOneById(BaseShopRequest<Long> request);

    /**
     * 更新 coupon 状态
     *
     * @param request 更新优惠卷状态
     * @return 更新优惠卷状态结果
     */
    BaseShopResponse<ResultEntity> upDateCouponStatus(BaseShopRequest<ShopCouponRequest> request);
}
