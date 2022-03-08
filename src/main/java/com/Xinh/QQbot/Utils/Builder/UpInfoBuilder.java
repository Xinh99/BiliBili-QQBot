package com.Xinh.QQbot.Utils.Builder;

import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;

/**
 * Create by 76705
 * Description :
 */


public class UpInfoBuilder {
    /**
     * long uid;
     * String upname;
     * long updatetime;
     * Integer livestate;
     */
    UpInfo upInfo;

    public UpInfoBuilder(){}

    public UpInfoBuilder buildUpInfo() {
        upInfo = new UpInfo();
        return this;
    }

    public UpInfoBuilder buildUID(long UID){
        upInfo.setUid(UID);
        return this;
    }

    public UpInfoBuilder buildUpName(String upName){
        upInfo.setUpname(upName);
        return this;
    }

    public UpInfoBuilder buildUpdateTime(long updateTime){
        upInfo.setUpdatetime(updateTime);
        return this;
    }

    public UpInfoBuilder buildLiveState(Integer liveState){
        upInfo.setLivestate(liveState);
        return this;
    }

    public UpInfo build(){
        return upInfo;
    }


}
