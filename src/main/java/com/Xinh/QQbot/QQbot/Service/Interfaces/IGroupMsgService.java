package com.Xinh.QQbot.QQbot.Service.Interfaces;

import com.Xinh.QQbot.Utils.Result.Result;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * Create by 76705
 * Description :
 */

public interface IGroupMsgService {

    Result analyzeMessage(String message, GroupMessageEvent event);

}
