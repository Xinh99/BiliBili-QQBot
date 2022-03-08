package com.Xinh.QQbot.QQbot.Service.Impl;

import com.Xinh.QQbot.QQbot.Bean.QQbotInfo;
import com.Xinh.QQbot.QQbot.Service.Interfaces.ICommandService;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IGroupMsgService;
import com.Xinh.QQbot.QQbot.Service.Interfaces.IQQbotInfoService;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by 76705
 * Description :
 *      提供指令分析的服务
 */

@Service
public class GroupMsgService implements IGroupMsgService {

    @Autowired
    private IQQbotInfoService qbotInfoService;

    @Autowired
    private ICommandService commandService;


    /**
     * 返回值特殊处理
     * 只有不是指令时才会返回fail
     * 其余返回success + 结果代码的文本说明部分(String)
     *
     * 因为bot需要反馈指令信息
     * 所以只要是指令,不论指令是否正确 ,都返回success,让bot反馈信息到群里
     * */
    @Override
    public Result analyzeMessage(String message, GroupMessageEvent event) {

        //1.指令判断
        char c = message.charAt(0);
        switch (c) {
            //代开发
            case '#':
                return Result.success(ResultCode.COMMAND_UNKNOWN.message());

            case '/':
                // 以'/' 定为主人的指令,需要指令发送者的身份
                //简单的权限识别
                if(isCommand(message)){

                    if (isOwner(event) )
                        return ownerCommand(message,message.indexOf(' '));

                    return Result.success(ResultCode.NOT_OWNER.message());
                }

                return Result.success(ResultCode.IS_NOT_COMMAND.message());

            //!!!!仅当信息不是命令语句是才会返回fail,不让机器人回复信息!!!!
            //否则都要返回success 让机器人回复信息
            default:
                return Result.fail(ResultCode.IS_NOT_COMMAND);

        }
    }

    private Result ownerCommand(String message, int i){
        //截取命令头尾,判断命令类型并获取目标对象
        String comm = message.substring(1,i);
        long target;
        try {
            target = Long.parseLong(message.substring(i + 1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Result.success(ResultCode.COMMAND_TARGET_WRONG.message());
        }

        //直接返回下级返回结果也是可以
        switch (comm){

            case "add":
            case "添加":
                return commandService.addCommand(target);
            case "remove":
            case "移除":
                return commandService.removeCommand(target);

            default:
                return Result.success(ResultCode.COMMAND_UNKNOWN.message());

        }

    }

    //空格要在第二个字符往后才算指令 example: /add 111 /remove 111 not / add 123
    private boolean isCommand(String message){
        return message.indexOf(' ') > 1;
    }

    //判断发送者身份
    private boolean isOwner(GroupMessageEvent event) {

        Result qQbotInfoResult = qbotInfoService.getQQbotInfo();
        if (qQbotInfoResult.isSuccess()) {
            //拿到QQ机器人的主人的账号  判断发送指令者的身份
            QQbotInfo qQbotInfo = (QQbotInfo) qQbotInfoResult.getObject();
            return qQbotInfo.getOwnerNum() == event.getSender().getId();

        }
        return false;
    }
}
