package com.Xinh.QQbot.BiliBiliUpInfo.Controller;

import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.user.User;
import com.Xinh.QQbot.BiliBiliClient.Controller.BiliBiliClientController;
import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.Service.Interfaces.IUpInfoService;
import com.Xinh.QQbot.Utils.Builder.UpInfoBuilder;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by 76705
 * Description : B站up主信息控制器
 *
 */

@RestController
@RequestMapping("/UpInfo")
public class UpInfoController {

    @Autowired
    private IUpInfoService upInfoService;

    @Autowired
    private BiliBiliClientController biliClientController;

    /**
     * 注意在多线程情况下map容器的读写操作
     * !!!可能出错!!!
     * */
    //存储up主信息的容器<up主的uid,up主信息>
    private ConcurrentHashMap<Long, UpInfo> upInfoMap;

    //必要的初始化 将数据库中的up信息加载到内存中,避免后续的断断续续的访问数据库get数据
    @PostConstruct
    public void init(){
        upInfoMap = new ConcurrentHashMap<>();
        Map<Long,UpInfo> upInfo = upInfoService.getUpInfo();
        upInfoMap.putAll(upInfo);
    }

    //关注某个up主,添加到map中
    @PostMapping("/post")
    public Result insertUpInfo(@RequestBody UpInfo upInfo){
        upInfoMap.put(upInfo.getUid(),upInfo);
        upInfoService.insertUpInfo(upInfo);
        return Result.success(upInfo);
    }

    //只有关注了才取关并返回up主名字
    @DeleteMapping("/delete/UID{UID}")
    public Result deleteUpInfo(@PathVariable(value = "UID") long UID){
        if(!isFollowing(UID))
            return Result.fail(ResultCode.UP_NOT_FOLLOWING);

        String name = upInfoMap.get(UID).getUpname();
        upInfoMap.remove(UID);
        upInfoService.deleteUpInfo(UID);

        return Result.success(name);
    }

    //更新up主信息(动态更新时间以及直播状态)
    @PutMapping("/update")
    public Result updateUpInfo(@RequestBody UpInfo upInfo){
        if(!isFollowing(upInfo.getUid()))
            return Result.fail(ResultCode.UP_NOT_FOLLOWING);

        upInfoMap.put(upInfo.getUid(),upInfo);
        upInfoService.updateUpInfo(upInfo);
        return Result.success();
    }

    //根据UID获取up主信息
    @GetMapping("/get/UID{UID}")
    public Result getUpInfoByUID(@PathVariable(value = "UID") long UID){
        if(!isFollowing(UID))
            return Result.fail(ResultCode.UP_NOT_FOLLOWING);

        return Result.success(upInfoMap.get(UID));
    }

    //获取所有up信息
    @GetMapping("/get/all")
    public Result getAllUpInfo(){
        if(upInfoMap.isEmpty())
            return Result.fail(ResultCode.NO_ONE_FOLLOWING);
        
        return Result.success(upInfoMap.values());
    }

    //获取不在列表中的up主信息并添加到列表中
    //现用于关注新up主
    @GetMapping("/getNew/UID{UID}")
    public Result getNewUpInfoByUID(@PathVariable(value = "UID") long UID){
        //1.获取B站用户
        Result userResult = biliClientController.getUserByUID(UID);
        if(!userResult.isSuccess())
            return Result.fail(ResultCode.USER_NOT_FOUND);

        User user = (User) userResult.getObject();

        //2.构建消息  注意 此时还有updateTime未完成
        UpInfoBuilder upInfoBuilder = new UpInfoBuilder().buildUpInfo()
                .buildUID(UID)
                .buildUpName(user.getName())
                .buildLiveState(user.getLive_room().getLiveStatus());

        //3.获取该用户的动态更新时间
        Result dynamicResult = biliClientController.getDynamicByUID(UID);
        //3.1如果该用户没有发布过动态,则设更新时间为0
        if(!dynamicResult.isSuccess()){
            upInfoBuilder.buildUpdateTime(0);
            return Result.success(upInfoBuilder.build());
        }

        Dynamic dynamic = (Dynamic) dynamicResult.getObject();

        //4.最终构建完成并返回
        upInfoBuilder.buildUpdateTime(dynamic.getData().getTimestamp());
        UpInfo upInfo = upInfoBuilder.build();
        insertUpInfo(upInfo);

        return Result.success(upInfo);
    }


    //用于判断是否已经关注该up主
    @RequestMapping("/isFollow/{UID}")
    public boolean isFollowing(@PathVariable(value = "UID") long UID){
        return upInfoMap.containsKey(UID);
    }

}
