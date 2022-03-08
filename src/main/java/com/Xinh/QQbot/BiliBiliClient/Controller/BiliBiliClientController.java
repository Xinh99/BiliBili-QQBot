package com.Xinh.QQbot.BiliBiliClient.Controller;

import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.BiliBiliClient.Service.Interfaces.IBiliBiliService;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by 76705
 * Description : BiliBili客户端控制器,用于获取B站服务器返回的信息
 *
 */

@RestController
@RequestMapping("/BClient")
public class BiliBiliClientController {

    @Autowired
    private IBiliBiliService biliService;

    //根据up主UID获取该up的第一条动态动态
    @GetMapping("/dynamic/UID{UID}")
    public Result getDynamicByUID(@PathVariable(value = "UID") long UID) {
        //拿到有效动态才返回success
        Result dynamicResult = biliService.getDynamicByUID(UID);
        if(!dynamicResult.isSuccess())
            return Result.fail(ResultCode.DYNAMIC_GET_FAIL,dynamicResult);

        Dynamic dynamic = (Dynamic) dynamicResult.getObject();
        return Result.success(dynamic);
    }

    //根据B站动态ID获取动态
    @GetMapping("/dynamic/ID{ID}")
    public Result getDynamicByID(@PathVariable(value = "ID") long ID){
        Result result = biliService.getDynamicByID(ID);
        if(!result.isSuccess())
            return Result.fail(ResultCode.DYNAMIC_GET_FAIL,result);

        Dynamic dynamic = (Dynamic) result.getObject();
        return Result.success(dynamic);
    }

    //根据UID获取B站用户
    @GetMapping("/user/UID{UID}")
    public Result getUserByUID(@PathVariable(value = "UID") long UID){
        Result result = biliService.getUserByUID(UID);
        if(!result.isSuccess())
            return Result.fail(ResultCode.USER_GET_FAIL,result);

        User user = (User) result.getObject();
        return Result.success(user);
    }

}
