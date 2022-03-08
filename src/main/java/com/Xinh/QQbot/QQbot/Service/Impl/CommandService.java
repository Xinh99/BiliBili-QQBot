package com.Xinh.QQbot.QQbot.Service.Impl;

import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.Controller.UpInfoController;
import com.Xinh.QQbot.QQbot.Service.Interfaces.ICommandService;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by 76705
 * Description :
 *      完成相应的指令的执行并返回结果
 */


/**
 * return :
 *      关与返回值,见GroupMsgService
 * */

@Service
public class CommandService implements ICommandService {

    @Autowired
    private UpInfoController upInfoController;

    /**
     * 关注新的up主
     * 首先判断该up主是否已被关注
     * 再根据Uid获取B站up主用户信息,获取失败则返回空
     *
     * 取关up主也是类似操作
     * */
    @Override
    public Result addCommand(long target){
        //1.判断是否已经关注了目标up主
        if(upInfoController.isFollowing(target))
            return Result.success(ResultCode.USER_ADD_FAIL.message());

        //2.在获取新的B站用户信息时已经 添加到了列表当中 见upinfoController
        Result upInfoResult = upInfoController.getNewUpInfoByUID(target);
        if(!upInfoResult.isSuccess())
            return Result.success(ResultCode.USER_NOT_FOUND.message());

        UpInfo upInfo = (UpInfo) upInfoResult.getObject();
        return Result.success(ResultCode.USER_ADD_SUCCESS.message() + upInfo.getUpname());
    }

    //取关已有up
    @Override
    public Result removeCommand(long target){

        //除非数据库异变,否则不会异常
        Result deleteResult = upInfoController.deleteUpInfo(target);
        if(!deleteResult.isSuccess())
            return Result.success(ResultCode.USER_REMOVE_FAIL.message());

        //返回删除的目标用户名
        String name = (String) deleteResult.getObject();
        return Result.success(ResultCode.USER_REMOVE_SUCCESS.message() + name);
    }

}
