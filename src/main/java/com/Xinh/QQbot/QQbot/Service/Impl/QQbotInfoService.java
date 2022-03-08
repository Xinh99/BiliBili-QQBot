package com.Xinh.QQbot.QQbot.Service.Impl;

import com.Xinh.QQbot.QQbot.Bean.QQbotInfo;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotInfoService;
import com.Xinh.QQbot.Utils.Builder.QQbotInfoBuilder;
import com.Xinh.QQbot.Utils.Result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Create by 76705
 * Description :
 *      提供获取机器信息的服务
 */

@Service
@PropertySource("classpath:properties/QQInfo.properties")
public class QQbotInfoService implements IQQbotInfoService {


    @Value("${Bot.Number}")
    private long QQNum;

    @Value("${Bot.Password}")
    private String QQPwd;

    @Value("${Bot.WorkGroup}")
    private long workGroup;

    @Value("${Bot.Owner}")
    private long ownerNum;

    private QQbotInfo qQbotInfo;
    //初始化QQ机器人信息,QQ机器人登录
    @PostConstruct
    public void init(){
        qQbotInfo = new QQbotInfoBuilder().buildQQbotInfo()
                .buildQQNum(QQNum)
                .buildQQPwd(QQPwd)
                .buildWorkGroup(workGroup)
                .buildOwner(ownerNum)
                .build();
    }

    @Override
    public Result getQQbotInfo() {
        return Result.success(qQbotInfo);
    }

}
