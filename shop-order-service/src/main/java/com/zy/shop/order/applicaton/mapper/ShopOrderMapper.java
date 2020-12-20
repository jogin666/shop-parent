package com.zy.shop.order.applicaton.mapper;

import com.zy.shop.pojo.ShopOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Jong
 * @Date: 2020/12/6 16:13
 */

@Mapper
public interface ShopOrderMapper {

    int saveOrder(ShopOrder order);

    int updateShopOrder(ShopOrder order);
}
