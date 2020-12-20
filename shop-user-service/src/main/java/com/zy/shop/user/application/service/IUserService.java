package com.zy.shop.user.application.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopUser;
import com.zy.shop.pojo.ShopUserUseMoneyLog;

/**
 * @Author: Jong
 * @Date: 2020/12/6 15:02
 */
public interface IUserService {

    ShopUser findOneById(Long userId);

    boolean updateMoneyPaid(ShopUserUseMoneyLog userMoneyLog) throws ShopBizException;

    Boolean updateUserMoney(ShopUser shopUse, ShopUserUseMoneyLog userMoneyLog);
}
