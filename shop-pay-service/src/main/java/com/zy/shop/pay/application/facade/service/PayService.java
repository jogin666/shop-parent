package com.zy.shop.pay.application.facade.service;

import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopPayRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.facade.ShopPayService;
import com.zy.shop.pay.application.service.IPayService;
import com.zy.shop.pojo.ShopPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.shop.common.enums.ShopPaidStatusEnum.SHOP_PAY_STATUS_CANNOT_CREATE;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.*;

/**
 * @author: jogin
 * @date: 2020/12/6 16:03
 */

@Slf4j
@Component
@Service(interfaceClass = ShopPayService.class)
public class PayService implements ShopPayService {

    @Autowired
    private IPayService payService;

    @Override
    public BaseShopResponse<ResultEntity> createPayment(BaseShopRequest<ShopPayRequest> request) {
        if (request == null || request.getData() == null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        ShopPayRequest data = request.getData();
        if (data.getStatus()==null || data.getOrderId()==null
                ||data.getPayMoney()==null || data.getPayId()==null){
            log.info("执行扣减商品库存,请求参数有误：{}", request);
            return BaseShopResponse.fail(REQUEST_PARAM_ERROR.toString());
        }
        ShopPay shopPay = newShopPay(request.getData());
        try{
            if (payService.createPayment(shopPay)){
                return BaseShopResponse.success(new ResultEntity(REQUEST_RESULT_SUCCESS.getCode(),REQUEST_RESULT_SUCCESS.getDes()));
            }
        }catch (Exception e){
            log.error("创建支付订单报错：{}",e.getMessage(),e);
            return BaseShopResponse.fail(new ResultEntity(SHOP_PAY_STATUS_CANNOT_CREATE.getCode(),SHOP_PAY_STATUS_CANNOT_CREATE.getDesc()));
        }
        return BaseShopResponse.fail(new ResultEntity(REQUEST_RESULT_FAIL.getCode(),REQUEST_RESULT_FAIL.getDes()));
    }

    @Override
    public BaseShopResponse<ResultEntity> callbackPayment(BaseShopRequest<ShopPayRequest> request) {
        ResultEntity result = new ResultEntity();
        if (request == null || request.getData() == null) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        ShopPayRequest data = request.getData();
        if (data.getStatus()==null || data.getOrderId()==null
                ||data.getPayMoney()==null || data.getPayId()==null){
            log.info("执行扣减商品库存,请求参数有误：{}", request);
            return BaseShopResponse.fail(REQUEST_PARAM_ERROR.toString());
        }
        try{
            ShopPay shopPay = newShopPay(request.getData());
            payService.callbackPayment(shopPay);
            result.setMessage("支付对调成功");
            return BaseShopResponse.success(result);
        }catch (Exception e){
            log.error("支付回调报错：{}",e.getMessage(),e);
            result.setMessage("支付回调报错");
            return BaseShopResponse.fail(result);
        }
    }

    private ShopPay newShopPay(ShopPayRequest request){
        ShopPay shopPay = new ShopPay();
        shopPay.setStatus(request.getStatus());
        shopPay.setOrderId(request.getOrderId());
        shopPay.setPayMoney(request.getPayMoney());
        shopPay.setPayId(request.getPayId());
        return shopPay;
    }
}
