package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopOrderRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;

/**
 * @author: jogin
 * @date: 2020/12/5 16:45
 */
public interface ShopOrderService {

    /**
     * 确定订单
     *
     * @param request
     * @return
     */
    BaseShopResponse<ResultEntity> confirmOrder(BaseShopRequest<ShopOrderRequest> request);
}
