package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopPayRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;

/**
 * @author: jogin
 * @date: 2020/12/5 16:45
 */
public interface ShopPayService {

    /**
     * 创建支付订单
     *
     * @param request
     * @return
     */
    BaseShopResponse<ResultEntity> createPayment(BaseShopRequest<ShopPayRequest> request);

    /**
     * 支付回调，更新支付订单状态
     *
     * @param request
     * @return
     */
    BaseShopResponse<ResultEntity> callbackPayment(BaseShopRequest<ShopPayRequest> request);
}
