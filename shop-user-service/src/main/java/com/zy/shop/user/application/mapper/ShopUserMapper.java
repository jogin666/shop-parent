package com.zy.shop.user.application.mapper;

import com.zy.shop.pojo.ShopUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: jogin
 * @date: 2020/12/6 14:54
 */

@Mapper
public interface ShopUserMapper {

    ShopUser findOneById(Long userId);

    int updateUserMoney(ShopUser shopUser);
}
