package com.Xinh.QQbot.BiliBiliClient.Service.Impl;

import cn.hll520.linling.biliClient.BiliClient;
import cn.hll520.linling.biliClient.BiliClientFactor;
import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.BiliBiliClient.Service.Interfaces.IBiliBiliService;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Create by 76705
 * Description : 提供获取B站服务器返回信息的服务
 *
 */

@Service
public class BiliBiliService implements IBiliBiliService {

    //B站客户端
    private BiliClient client;

    //初始化B站客户端
    @PostConstruct
    public void init(){
        client = getClient();
    }

    //获取客户端
    @Override
    public BiliClient getClient() {
        return BiliClientFactor.getClient();
    }

    //根据Uid获取动态
    @Override
    public Result getDynamicByUID(long UID) {
        Dynamic dynamic;
        try {
            dynamic = client.dynamic().withHostUid(UID).list().getItems().get(0);
        } catch (Exception e) {
            System.out.println("动态获取异常");
            return Result.fail(ResultCode.DYNAMIC_GET_EXCEPTION);
        }

        return Result.success(dynamic);
    }

    //根据动态id获取动态
    @Override
    public Result getDynamicByID(long ID) {
        Dynamic dynamic;
        try {
            dynamic = client.dynamic().withDynamicId(ID).get();
        } catch (Exception e) {
            System.out.println("动态获取异常");
            return Result.fail(ResultCode.DYNAMIC_GET_EXCEPTION);
        }

        return Result.success(dynamic);
    }

    //通过Uid获取B站用户
    @Override
    public Result getUserByUID(long UID) {
        User user;
        try {
            user = client.user().withUID(UID).get();
        } catch (Exception e) {
            System.out.println("用户获取异常");
            return Result.fail(ResultCode.USER_GET_EXCEPTION);
        }

        return Result.success(user);
    }

}
