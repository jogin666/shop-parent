package com.zy.shop.order.applicaton.facade.service;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopOrderRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.facade.ShopOrderService;
import com.zy.shop.order.applicaton.service.IOrderService;
import com.zy.shop.pojo.ShopOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.shop.common.enums.rpc.RequestResultEnum.*;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.REQUEST_RESULT_FAIL;

/**
 * @Author: Jong
 * @Date: 2020/12/6 16:13
 */
@Slf4j
@Component
@Service(interfaceClass = ShopOrderService.class)
public class OrderService implements ShopOrderService{


    @Autowired
    private IOrderService orderService;

    @Override
    public BaseShopResponse<ResultEntity> confirmOrder(BaseShopRequest<ShopOrderRequest> request) {
        if (request == null || request.getData() == null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        ShopOrderRequest data = request.getData();
        if (data.getGoodsId() == null || data.getOrderId() == null
                || data.getGoodsNumber() == null || data.getUserId() == null) {
            log.info("执行扣减商品库存,请求参数有误：{}", request);
            return BaseShopResponse.fail(REQUEST_PARAM_ERROR.toString());
        }
        try {
            boolean result = orderService.confirmOrder(newOrderTrade(data));
            if (result){
                return BaseShopResponse.success(new ResultEntity(REQUEST_RESULT_SUCCESS.getCode(),REQUEST_RESULT_SUCCESS.getDes()));
            }
        } catch (Exception e) {
            log.error("执行确定订单失败：{}",e.getMessage(),e);
        }
        return BaseShopResponse.fail(new ResultEntity(REQUEST_RESULT_FAIL.getCode(),REQUEST_RESULT_FAIL.getDes()));
    }

    private ShopOrder newOrderTrade(ShopOrderRequest orderRequest) {
        ShopOrder order = new ShopOrder();
        order.setCreateTime(orderRequest.getCreateTime());
        order.setAddress(orderRequest.getAddress());
        order.setCouponId(orderRequest.getCouponId());
        order.setCouponMoney(orderRequest.getCouponMoney());
        order.setGoodsNumber(orderRequest.getGoodsNumber());
        order.setGoodsId(orderRequest.getGoodsId());
        order.setGoodsNumber(orderRequest.getGoodsNumber());
        order.setGoodsPrice(orderRequest.getGoodsPrice());
        order.setPaidMoney(orderRequest.getMoneyPaid());
        order.setTotalMoney(orderRequest.getTotalMoney());
        order.setOrderId(orderRequest.getOrderId());
        order.setOrderStatus(orderRequest.getStatus());
        order.setUserId(orderRequest.getUserId());
        return order;
    }
}
