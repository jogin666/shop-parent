package com.zy.shop.facade;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopUserUseMoneyLogRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopUserResponse;

/**
 * @Author: Jong
 * @Date: 2020/12/5 16:45
 */

public interface ShopUserService {

    /**
     * 根据 userId 查询 user
     *
     * @param request 查询用户请求
     * @return 用户信息
     */
    BaseShopResponse<ShopUserResponse> findOneById(BaseShopRequest<Long> request);

    /**
     * 更新用户使用金额的状态
     *
     * @param request  更新用户使用金额的状态请求
     * @return 结果
     */
    BaseShopResponse<ResultEntity> updateMoneyPaid(BaseShopRequest<ShopUserUseMoneyLogRequest> request);
}
