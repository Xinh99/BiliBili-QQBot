package com.Xinh.QQbot.QQbot.Bean;

import lombok.Data;

/**
 * Create by 76705
 * Description :
 *      封装QQ机器人信息
 */

@Data
public class QQbotInfo {

    //机器人账号
    private long QQNum;
    //机器人密码
    private String QQPwd;
    //机器人工作群
    private long workGroup;
    //机器人的主人的QQ账号
    private long ownerNum;

}
