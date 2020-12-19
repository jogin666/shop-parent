package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopGoodsRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopGoodsResponse;

/**
 * @Author: Jong
 * @Date: 2020/12/5 16:45
 */
public interface ShopGoodsService {

    /**
     * 根据 goodsId 查询 goods
     *
     * @param request 商品查询请求
     * @return 商品信息
     */
    BaseShopResponse<ShopGoodsResponse> findOneById(BaseShopRequest<Long> request);

    /**
     * 扣减 goods 库存
     *
     * @param request 扣减库存请求
     * @return 扣除库存结果
     */
    BaseShopResponse<ResultEntity> reduceGoodsNumber(BaseShopRequest<ShopGoodsRequest> request);
}
