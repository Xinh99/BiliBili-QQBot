package com.Xinh.QQbot.Utils.Builder;

import com.Xinh.QQbot.QQbot.Bean.QQbotInfo;

/**
 * Create by 76705
 * Description :
 */

public class QQbotInfoBuilder {
    /**
    * long QQNum;
    * String QQPwd;
    * long workGroup;
    * long ownerNum;
    */

    private QQbotInfo qQbotInfo;

    public QQbotInfoBuilder(){}

    public QQbotInfoBuilder buildQQbotInfo(){
        qQbotInfo = new QQbotInfo();
        return this;
    }

    public QQbotInfoBuilder buildQQNum(long QQNum){
        qQbotInfo.setQQNum(QQNum);
        return this;
    }

    public QQbotInfoBuilder buildQQPwd(String QQpwd){
        qQbotInfo.setQQPwd(QQpwd);
        return this;
    }

    public QQbotInfoBuilder buildWorkGroup(long workGroup){
        qQbotInfo.setWorkGroup(workGroup);
        return this;
    }

    public QQbotInfoBuilder buildOwner(long ownerNum){
        qQbotInfo.setOwnerNum(ownerNum);
        return this;
    }

    public QQbotInfo build(){
        return  qQbotInfo;
    }


}
