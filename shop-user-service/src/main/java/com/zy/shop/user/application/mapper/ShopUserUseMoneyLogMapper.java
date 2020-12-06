package com.zy.shop.user.application.mapper;

import com.zy.shop.pojo.ShopUserUseMoneyLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: jogin
 * @date: 2020/12/6 14:54
 */
@Mapper
public interface ShopUserUseMoneyLogMapper {

    int countUserUseMoney(ShopUserUseMoneyLog useMoneyLog);

    int saveUseMoneyLog(ShopUserUseMoneyLog useMoneyLog);

    List<ShopUserUseMoneyLog> findUserUseMoneyLog(ShopUserUseMoneyLog useMoneyLog);

}
