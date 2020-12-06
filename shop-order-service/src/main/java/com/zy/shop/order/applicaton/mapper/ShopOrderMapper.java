package com.zy.shop.order.applicaton.mapper;

import com.zy.shop.pojo.ShopOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: jogin
 * @date: 2020/12/6 16:13
 */

@Mapper
public interface ShopOrderMapper {

    int saveOrder(ShopOrder order);

    int updateShopOrder(ShopOrder order);
}
