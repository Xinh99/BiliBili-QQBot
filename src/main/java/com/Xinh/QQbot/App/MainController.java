package com.Xinh.QQbot.App;

import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;
import com.Xinh.QQbot.BiliBiliMsg.Bean.LiveRoomMsg;
import com.Xinh.QQbot.BiliBiliMsg.Controller.BiliBiliMsgController;
import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.Controller.UpInfoController;
import com.Xinh.QQbot.QQbot.Controller.QQbotController;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Static.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Create by 76705
 * Description :
 *
 * 暂定为入口吧
 *
 */

@RestController
@RequestMapping("/App")
public class MainController implements CommandLineRunner {

    @Autowired
    private BiliBiliMsgController biliMsgController;

    @Autowired
    private QQbotController qQbotController;

    @Autowired
    private UpInfoController upInfoController;

    @Override
    public void run(String... args) throws Exception {
        QQGroupMessageListener();
        BiliBiliClientListener();
    }

    public void BiliBiliClientListener(){

        //用于存放关注列表中的up主信息,用于下方遍历
        Collection<UpInfo> upInfoList;

        try {
            while (true){
                //1.获取关注列表中的所有Up主信息
                Result upInfoResult = upInfoController.getAllUpInfo();

                //列表为空则进行10s等待
                if(!upInfoResult.isSuccess()){
                    Thread.sleep(10000);
                    continue;
                }
                upInfoList = (Collection<UpInfo>) upInfoResult.getObject();


                for(UpInfo upInfo : upInfoList){
                    System.out.println(upInfo.getUpname());

                    //2.获取该up主的最新动态信息
                    Result DynamicMsgResult = biliMsgController.getNewDynamicMsgByUID(upInfo.getUid());
                    if(DynamicMsgResult.isSuccess()){
                        DynamicMsg dynamicMsg = (DynamicMsg) DynamicMsgResult.getObject();
                        qQbotController.sendMessage(dynamicMsg);
                    }

                    //3.获取该up主的直播间信息
                    Result liveRoomMsgResult = biliMsgController.getLiveRoomMsgByUID(upInfo.getUid());
                    if(liveRoomMsgResult.isSuccess()){
                        LiveRoomMsg liveRoomMsg = (LiveRoomMsg) liveRoomMsgResult.getObject();
                        qQbotController.sendMessage(liveRoomMsg);
                    }

                    //4.清理图片
                    Method.cleanPicture();


                    //5.设置每个up主信息之间的间隔,避免频繁访问B站API导致被ban
                    Thread.sleep(10000);
                }//for...

            }//while...
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//try catch


    }

    //见QQbotController
    public void QQGroupMessageListener(){
        qQbotController.groupMessageListener();
    }


}
