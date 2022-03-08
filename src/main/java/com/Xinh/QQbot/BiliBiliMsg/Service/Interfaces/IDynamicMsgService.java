package com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces;

import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;
import com.Xinh.QQbot.Utils.Result.Result;

/**
 * Create by 76705
 * Description :
 */

public interface IDynamicMsgService {

    //将B站原始动态信息进行封装
    Result getDynamicMsg(Dynamic dynamic);

}
