package com.zy.shop.user.application.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopUser;
import com.zy.shop.pojo.ShopUserUseMoneyLog;

/**
 * @author: jogin
 * @date: 2020/12/6 15:02
 */
public interface IUserService {

    ShopUser findOneById(Long userId);

    boolean updateMoneyPaid(ShopUserUseMoneyLog userMoneyLog) throws ShopBizException;
}
