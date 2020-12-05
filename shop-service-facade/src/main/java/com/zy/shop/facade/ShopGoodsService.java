package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopGoodsRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopGoodsResponse;

/**
 * @author: jogin
 * @date: 2020/12/5 16:45
 */
public interface ShopGoodsService {

    /**
     * 根据 goodsId 查询 goods
     *
     * @param request
     * @return
     */
    BaseShopResponse<ShopGoodsResponse> findOneById(BaseShopRequest<Long> request);

    /**
     * 扣减 goods 库存
     *
     * @param request
     * @return
     */
    BaseShopResponse<ResultEntity> reduceGoodsNumber(BaseShopRequest<ShopGoodsRequest> request);
}
