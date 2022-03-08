package com.Xinh.QQbot.Utils.Static;

import cn.hll520.linling.biliClient.model.LiveRoom;
import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.Controller.UpInfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Create by 76705
 * Description :
 *  提供一些逻辑判断,以及其他的静态方法
 */

@Component
public class Method {

    @Autowired
    private UpInfoController upInfoController;

    //图片清理 当图片数量超出预设值时进行图片清理
    public static void cleanPicture() {

        try {
            if (Info.pictureNum < Info.pictureMax)
                return;

            //拿到所有图片信息
            File file = new File(Info.picturePath);
            String[] list = file.list();
            //逐个删除
            File pic;
            for (int i = 0; i < list.length; i++) {
                pic = new File(Info.picturePath + list[i]);
                pic.delete();
            }
            Info.pictureNum = 0;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    //用于判断动态是否有更新
    public static boolean isNewDynamic(Dynamic dynamic, UpInfo upInfo) {
        long time = upInfo.getUpdatetime();
        long updateTime = dynamic.getData().getTimestamp();

        boolean flag = time < updateTime;

        if(flag)
            upInfo.setUpdatetime(updateTime);

        return flag;
    }

    /**
     * 只有从为开播状态转为开播状态 即 0->1
     * 才为有效
     */
    //对获取的直播间状态进行判断,查看是否处于正在直播状态
    public static Info.LiveStatus isLive(UpInfo upInfo, LiveRoom liveRoom) {

        //up主的直播状态
        int live = upInfo.getLivestate();
        //直播间的直播状态
        int state = liveRoom.getLiveStatus();

        //当直播间正在开启直播...
        if (state == 1) {

            //但up主信息仍然时没有直播的状态,更新数据
            if (live == 0){
                upInfo.setLivestate(state);
                return Info.LiveStatus.ON_LIVE;
            }


            //且up主信息也是正在直播时
            else
                return Info.LiveStatus.NORMAL;

        } else{ //当直播间没有开启直播...

            //但up主信息仍是直播状态 更新数据
            if(live == 1){
                upInfo.setLivestate(state);
                return Info.LiveStatus.NOT_LIVING;
            }

            //且up主信息也是没在直播时
            else
                return Info.LiveStatus.NORMAL;
        }



    }


}
