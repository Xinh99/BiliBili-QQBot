package com.Xinh.QQbot.BiliBiliMsg.Service.Impl;

import cn.hll520.linling.biliClient.model.LiveRoom;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.BiliBiliMsg.Bean.LiveRoomMsg;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.ILiveRoomMsgService;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.IPictureService;
import com.Xinh.QQbot.Utils.Builder.LiveRoomBuilder;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by 76705
 * Description :
 *      提供B站直播间信息的封装服务
 */

@Service
public class LiveRoomMsgService implements ILiveRoomMsgService {

    @Autowired
    private IPictureService pictureService;

    @Override
    public Result getLiveRoomMsg(User user) {
        String name;
        String title;
        String url;
        String pic;

        try {
            //1.获取用户名及其直播间
            name = user.getName();
            LiveRoom liveRoom = user.getLive_room();

            //2.获取直播间标题,链接以及直播间封面url
            title = liveRoom.getTitle();
            url = liveRoom.getUrl();
            String picUrl = liveRoom.getCover();

            //3.获取直播间封面
            Result picResult = pictureService.getPicture(picUrl);
            if(!picResult.isSuccess())
                return Result.fail(ResultCode.LIVEROOMMSG_PACK_FAIL,picResult);

            pic = (String) picResult.getObject();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("直播间信息数据异常");
            return Result.fail(ResultCode.LIVEROOMMSG_PACK_FAIL);
        }

        //4...
        LiveRoomMsg liveRoomMsg = new LiveRoomBuilder().buildLiveRoomMsg()
                .buildUpName(name)
                .buildLiveRoomTitle(title)
                .buildLiveRoomUrl(url)
                .buildLiveRoomPic(pic)
                .build();

        return Result.success(liveRoomMsg);
    }
}
