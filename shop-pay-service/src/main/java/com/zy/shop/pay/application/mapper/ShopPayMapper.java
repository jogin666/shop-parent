package com.zy.shop.pay.application.mapper;

import com.zy.shop.pojo.ShopPay;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: jogin
 * @date: 2020/12/6 14:00
 */
@Mapper
public interface ShopPayMapper {

    ShopPay findOneById(Long payId);

    int countById(Long payId);

    int saveShopPay(ShopPay pay);
}


