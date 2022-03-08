package com.Xinh.QQbot.BiliBiliMsg.Controller;

import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.BiliBiliClient.Controller.BiliBiliClientController;
import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;
import com.Xinh.QQbot.BiliBiliMsg.Bean.LiveRoomMsg;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.IDynamicMsgService;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.ILiveRoomMsgService;
import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.Controller.UpInfoController;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import com.Xinh.QQbot.Utils.Static.Info;
import com.Xinh.QQbot.Utils.Static.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by 76705
 * Description : BiliBili信息控制器
 *      用于获取经过封装后的B站信息
 */

@RestController
@RequestMapping("/BMessage")
public class BiliBiliMsgController {

    @Autowired
    private BiliBiliClientController biliClientController;

    @Autowired
    private UpInfoController upInfoController;

    @Autowired
    private IDynamicMsgService dynamicMsgService;

    @Autowired
    private ILiveRoomMsgService liveRoomMsgService;

    //根据UID获取up主的动态信息
    @GetMapping("/dynamicMsg/UID{UID}")
    public Result getDynamicMsgByUID(@PathVariable(value = "UID") Long UID) {
        //1.获取B站动态
        Result dynamicResult = biliClientController.getDynamicByUID(UID);
        if (!dynamicResult.isSuccess())
            return Result.fail(ResultCode.DYNAMICMSG_GET_FAIL, dynamicResult);

        Dynamic dynamic = (Dynamic) dynamicResult.getObject();

        //2.将动态信息封装,返回
        Result dynamicMsgResult = dynamicMsgService.getDynamicMsg(dynamic);
        if (!dynamicMsgResult.isSuccess())
            return Result.fail(ResultCode.DYNAMICMSG_GET_FAIL, dynamicMsgResult);

        DynamicMsg dynamicMsg = (DynamicMsg) dynamicMsgResult.getObject();
        return Result.success(dynamicMsg);
    }

    //根据UID获取关注列表中up主的最新一条动态信息 若up不在关注列表,则返回fail
    @GetMapping("/newDynamicMsg/UID{UID}")
    public Result getNewDynamicMsgByUID(@PathVariable(value = "UID") long UID) {
        //1.拿到up主信息
        Result upInfoResult = upInfoController.getUpInfoByUID(UID);
        if (!upInfoResult.isSuccess())
            return Result.fail(ResultCode.DYNAMICMSG_GET_FAIL, upInfoResult);

        UpInfo upInfo = (UpInfo) upInfoResult.getObject();

        //2.获取动态信息
        Result dynamicResult = biliClientController.getDynamicByUID(UID);
        if (!dynamicResult.isSuccess())
            return Result.fail(ResultCode.DYNAMICMSG_GET_FAIL, dynamicResult);

        Dynamic dynamic = (Dynamic) dynamicResult.getObject();

        //3.判断动态是否有更新,若有更新,则更新数据
        if(!Method.isNewDynamic(dynamic, upInfo))
            return Result.fail(ResultCode.DYNAMIC_NOT_UPDATE);

        //3.1更新数据
        upInfoController.updateUpInfo(upInfo);

        //4.将该动态信息进行封装,返回
        Result dynamicMsgResult = dynamicMsgService.getDynamicMsg(dynamic);
        if (!dynamicMsgResult.isSuccess())
            return Result.fail(ResultCode.DYNAMICMSG_GET_FAIL, dynamicResult);

        DynamicMsg dynamicMsg = (DynamicMsg) dynamicMsgResult.getObject();
        return Result.success(dynamicMsg);
    }

    //根据UID获取直播间信息
    @GetMapping("/liveRoomMsg/UID{UID}")
    public Result getLiveRoomMsgByUID(@PathVariable(value = "UID") long UID) {
        //1.根据UID获取B站用户
        Result userResult = biliClientController.getUserByUID(UID);
        if (!userResult.isSuccess())
            return Result.fail(ResultCode.LIVEROOMMSG_GET_FAIL, userResult);

        User user = (User) userResult.getObject();

        //2.拿到up主信息
        Result upInfoResult = upInfoController.getUpInfoByUID(UID);
        if(!upInfoResult.isSuccess())
            return Result.fail(ResultCode.LIVEROOMMSG_GET_FAIL,upInfoResult);

        UpInfo upInfo = (UpInfo) upInfoResult.getObject();

        //3.判断用户是否处于直播状态
        Info.LiveStatus liveStatus = Method.isLive(upInfo, user.getLive_room());

        //3.1如果处理处于下播状态,则更新数据 返回fail
        if(liveStatus == Info.LiveStatus.NOT_LIVING){
            upInfoController.updateUpInfo(upInfo);
            return Result.fail(ResultCode.UP_IS_NOT_LIVING);
        }
        //3.2如果处理普通状态 即正在直播以及没在直播,则不需要更新数据 直接返回fail
        else if(liveStatus == Info.LiveStatus.NORMAL)
            return Result.fail(ResultCode.UP_IS_NOT_LIVING);

        //3.3如果处理开启直播状态,更新数据,则有效,可以返回封装的直播间信息
        upInfoController.updateUpInfo(upInfo);

        //4.将直播间信息进行封装,返回
        Result liveRoomMsgResult = liveRoomMsgService.getLiveRoomMsg(user);
        if (!liveRoomMsgResult.isSuccess())
            return Result.fail(ResultCode.LIVEROOMMSG_GET_FAIL, liveRoomMsgResult);

        LiveRoomMsg liveRoomMsg = (LiveRoomMsg) liveRoomMsgResult.getObject();
        return Result.success(liveRoomMsg);
    }

}
