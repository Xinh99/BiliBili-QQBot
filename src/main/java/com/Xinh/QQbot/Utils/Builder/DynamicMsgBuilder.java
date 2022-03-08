package com.Xinh.QQbot.Utils.Builder;

import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;

import java.util.List;

/**
 * Create by 76705
 * Description :
 */

public class DynamicMsgBuilder {
    /**
     *  String upName;
     *  String type;
     *  String context;
     *  List<String> pics;
     */

    private DynamicMsg dynamicMsg;

    public DynamicMsgBuilder(){}

    public DynamicMsgBuilder buildDynamicMsg(){
        dynamicMsg = new DynamicMsg();
        return this;
    }

    public DynamicMsgBuilder buildUpName(String upName){
        dynamicMsg.setUpName(upName);
        return this;
    }

    public DynamicMsgBuilder buildType(String type){
        dynamicMsg.setType(type);
        return this;
    }

    public DynamicMsgBuilder buildContext(String context){
        dynamicMsg.setContext(context);
        return this;
    }

    public DynamicMsgBuilder buildPics(List<String> pics){
        if (!pics.isEmpty())
            dynamicMsg.setPics(pics);

        return this;
    }

    public DynamicMsg build(){
        return dynamicMsg;
    }

}
