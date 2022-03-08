package com.Xinh.QQbot.QQbot.Controller;

import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;
import com.Xinh.QQbot.BiliBiliMsg.Bean.LiveRoomMsg;
import com.Xinh.QQbot.QQbot.Bean.QQbotInfo;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IGroupMsgService;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotInfoService;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotService;
import com.Xinh.QQbot.Utils.Result.Result;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Create by 76705
 * Description : QQ机器人控制器
 */

@RestController
@RequestMapping("/QQbot")
public class QQbotController {

    @Autowired
    private IQQbotService qbotService;

    @Autowired
    private IQQbotInfoService qbotInfoService;

    @Autowired
    private IGroupMsgService groupMsgService;

    //QQ机器人
    private Bot bot;
    //机器人工作群
    private Group group;

    //初始化
    @PostConstruct
    public void init(){
        //1.获取机器人以及机器人的信息
        Result qQbotResult = qbotService.getQQbot();
        Result qQbotInfoResult = qbotInfoService.getQQbotInfo();

        if(qQbotResult.isSuccess() && qQbotInfoResult.isSuccess()){
            //2.初始化机器人以及他的工作QQ群
            bot = (Bot) qQbotResult.getObject();
            QQbotInfo qQbotInfo = (QQbotInfo) qQbotInfoResult.getObject();
            group = bot.getGroup(qQbotInfo.getWorkGroup());

        }
        
    }

    /**
     * 监听群消息,并判断该消息是否是一条指令
     * 如果是一条指令则进行后续任务
     * */
    public void groupMessageListener() {

        //创建QQ群事件监听器 监听QQ群消息
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) ->{
            //如果收到机器人工作QQ群的消息,则判断消息是否是一条指令
            if(event.getSubject() == group){
                //判断消息是否是一条指令,并根据返回结果在群里作出相应的回复
                String message = event.getMessage().contentToString();
                Result result = groupMsgService.analyzeMessage(message, event);
                //在群里进行反馈
                if(result.isSuccess()){
                    String reply = (String) result.getObject();
                    sendMessage(reply);
                }

            }///if == workgroup

        }); //bot.getEvent
    }

    //往QQ群发送信息
    public void sendMessage(Object object){
        if(object instanceof DynamicMsg)
            group.sendMessage(((DynamicMsg) object).toChain(group));
        else if(object instanceof LiveRoomMsg)
            group.sendMessage(((LiveRoomMsg) object).toChain(group));
        else if(object instanceof String)
            group.sendMessage(new PlainText((String) object));
    }

}
