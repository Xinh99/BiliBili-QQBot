package com.Xinh.QQbot.QQbot.Service.Impl;

import com.Xinh.QQbot.QQbot.Bean.QQbotInfo;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotInfoService;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotService;
import com.Xinh.QQbot.Utils.Result.Result;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Create by 76705
 * Description :
 *     初始化机器人
 *     提供获取机器人的服务
 */

@Service
public class QQbotService implements IQQbotService {

    @Autowired
    private IQQbotInfoService qbotInfoService;

    private Bot bot;

    @PostConstruct
    public void init(){
        Result qQbotInfoResult = qbotInfoService.getQQbotInfo();
        if(qQbotInfoResult.isSuccess()){
            /**
             * 初始化QQ机器人
             * 固定格式
             * https://docs.mirai.mamoe.net/Bots.html#_1-%E5%88%9B%E5%BB%BA%E5%92%8C%E9%85%8D%E7%BD%AE-bot
             * */
            QQbotInfo qQbotInfo = (QQbotInfo) qQbotInfoResult.getObject();
            bot = BotFactory.INSTANCE.newBot(qQbotInfo.getQQNum(), qQbotInfo.getQQPwd(), new BotConfiguration(){{
                //使用指定设备
                fileBasedDeviceInfo("device.json");
            }});

            bot.login();
        }

    }

    @Override
    public Result getQQbot() {
        return Result.success(bot);
    }

}
