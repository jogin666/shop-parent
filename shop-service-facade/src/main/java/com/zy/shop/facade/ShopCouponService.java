package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopCouponRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopCouponResponse;

/**
 * @author: jogin
 * @date: 2020/12/5 16:45
 */
public interface ShopCouponService {

    /**
     * 根据 couponId 查询 coupon
     *
     * @param request
     * @return
     */
    BaseShopResponse<ShopCouponResponse> findOneById(BaseShopRequest<Long> request);

    /**
     * 更新 coupon 状态
     *
     * @param request
     * @return
     */
    BaseShopResponse<ResultEntity> updateCouponStatus(BaseShopRequest<ShopCouponRequest> request);
}
