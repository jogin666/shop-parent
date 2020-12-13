package com.zy.shop.goods.application.facade.service;

import com.zy.shop.common.aspect.RequestLogger;
import com.zy.shop.common.dto.mq.ResultEntity;
import com.zy.shop.common.dto.request.BaseShopRequest;
import com.zy.shop.common.dto.request.ShopGoodsRequest;
import com.zy.shop.common.dto.response.BaseShopResponse;
import com.zy.shop.common.dto.response.ShopGoodsResponse;
import com.zy.shop.common.util.ResultBuilder;
import com.zy.shop.facade.ShopGoodsService;
import com.zy.shop.goods.application.service.IGoodsService;
import com.zy.shop.pojo.ShopGoods;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.zy.shop.common.enums.rpc.RequestResultEnum.*;
import static com.zy.shop.common.enums.rpc.RequestResultEnum.REQUEST_RESULT_SUCCESS;

/**
 * @author: jogin
 * @date: 2020/12/6 13:04
 */
@Slf4j
@Component
@Service(interfaceClass = ShopGoodsService.class)
public class GoodsService implements ShopGoodsService {

    @Autowired
    private IGoodsService goodsService;

    @Override
    @RequestLogger(description = "查询商品 - by Id")
    public BaseShopResponse<ShopGoodsResponse> findOneById(BaseShopRequest<Long> request) {
        if (request == null || StringUtils.isEmpty(request.getData())) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        Long goodsId = request.getData();
        if (StringUtils.isEmpty(String.valueOf(goodsId))) {
            String message = ResultBuilder.conditionEmpty("goodsId");
            return BaseShopResponse.fail(message);
        }
        ShopGoodsResponse response = null;
        log.info("商品查询：{}", goodsId);
        ShopGoods goods = goodsService.findOneById(goodsId);
        if (goods != null) {
            response = ShopGoodsResponse.builder()
                    .goodsDesc(goods.getGoodsDesc())
                    .goodsId(goods.getGoodsId())
                    .name(goods.getName())
                    .number(goods.getNumber())
                    .goodsPrice(goods.getGoodsPrice())
                    .updateTime(goods.getUpdateTime())
                    .createTime(goods.getCreateTime())
                    .build();
        }
        log.info("查询商品 - by Id 查询结果：{}", response);
        return BaseShopResponse.success(response);
    }

    @Override
    @RequestLogger(description = "执行扣减商品库存")
    public BaseShopResponse<ResultEntity> reduceGoodsNumber(BaseShopRequest<ShopGoodsRequest> request) {
        if (request == null || StringUtils.isEmpty(request.getData())) {
            return BaseShopResponse.fail(REQUEST_PARAM_EMPTY.toString());
        }
        ShopGoodsRequest data = request.getData();
        if (data.getNumber() == null || data.getGoodsId() == null
                || data.getNumber() == null || data.getNumber().intValue() < 0) {
            log.info("执行扣减商品库存,请求参数有误：{}", request);
            return BaseShopResponse.fail(REQUEST_PARAM_ERROR.toString());
        }
        try{
            ShopGoods shopGoods = new ShopGoods();
            shopGoods.setGoodsId(data.getGoodsId());
            shopGoods.setNumber(data.getNumber());
            shopGoods.setName(data.getName());
            shopGoods.setGoodsPrice(data.getGoodsPrice());
            boolean result = goodsService.reduceGoodsNumber(shopGoods,data.getOrderId());
            log.info("执行扣减商品库存结果：{}", result ? "success" : "fail");
            if (result){
                return BaseShopResponse.success(new ResultEntity(REQUEST_RESULT_SUCCESS.getCode(),REQUEST_RESULT_SUCCESS.getDes()));
            }
        }catch (Exception e){
            log.error("执行扣减商品库存报错：{}",e.getMessage(),e);
            return BaseShopResponse.fail(e.getMessage());
        }
        return BaseShopResponse.fail(null);
    }
}
