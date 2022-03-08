package com.Xinh.QQbot.BiliBiliClient.Service.Interfaces;

import cn.hll520.linling.biliClient.BiliClient;
import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.Utils.Result.Result;

/**
 * Create by 76705
 * Description :
 */

public interface IBiliBiliService {

    //获取客户端
    BiliClient getClient();

    //根据Uid获取动态
    Result getDynamicByUID(long uid);

    //根据动态ID获取动态
    Result getDynamicByID(long id);

    //根据Uid获取up主
    Result getUserByUID(long uid);

}
