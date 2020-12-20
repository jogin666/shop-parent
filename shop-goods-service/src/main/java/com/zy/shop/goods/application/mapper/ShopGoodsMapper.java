package com.zy.shop.goods.application.mapper;

import com.zy.shop.pojo.ShopGoods;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Jong
 * @Date: 2020/12/5 18:40
 */

@Mapper
public interface ShopGoodsMapper {

    ShopGoods findOneById(Long goodsId);

    int updateGoods(ShopGoods goods);

}
