package com.Xinh.QQbot.Utils.Builder;

import com.Xinh.QQbot.BiliBiliMsg.Bean.LiveRoomMsg;

/**
 * Create by 76705
 * Description :
 */

public class LiveRoomBuilder {

    /**
     *  String upName;
     *  String liveRoomTitle;
     *  String liveRoomUrl;
     *  String liveRoomPic;
     */
    private LiveRoomMsg liveRoomMsg;

    public LiveRoomBuilder(){}

    public LiveRoomBuilder buildLiveRoomMsg(){
        liveRoomMsg = new LiveRoomMsg();
        return this;
    }

    public LiveRoomBuilder buildUpName(String name){
        liveRoomMsg.setUpName(name);
        return this;
    }

    public LiveRoomBuilder buildLiveRoomTitle(String title){
        liveRoomMsg.setLiveRoomTitle(title);
        return this;
    }

    public LiveRoomBuilder buildLiveRoomUrl(String url){
        liveRoomMsg.setLiveRoomUrl(url);
        return this;
    }

    public LiveRoomBuilder buildLiveRoomPic(String pic){
        liveRoomMsg.setLiveRoomPic(pic);
        return this;
    }

    public LiveRoomMsg build(){
        return liveRoomMsg;
    }



}
