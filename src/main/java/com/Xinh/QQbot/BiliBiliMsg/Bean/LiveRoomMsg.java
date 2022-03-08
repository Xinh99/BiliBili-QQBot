package com.Xinh.QQbot.BiliBiliMsg.Bean;

import com.Xinh.QQbot.Utils.Static.Info;
import lombok.Data;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;

/**
 * Create by 76705
 * Description : 直播间信息包装类
 *      对B站的直播间信息进行封装
 */

@Data
public class LiveRoomMsg {

    //用户名
    private String upName;
    //直播间标题
    private String liveRoomTitle;
    //直播间连接
    private String liveRoomUrl;
    //直播间封面
    private String liveRoomPic;

    @Override
    public String toString() {
        return upName + " 正在直播\n" + liveRoomTitle + "\n"
                + liveRoomUrl + "\n";
    }

    public MessageChain toChain(Group group){
        //构建消息链
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new PlainText(upName + "正在直播:" + "\n"))
                .append(new PlainText(liveRoomTitle + "\n"))
                .append(new PlainText(liveRoomUrl + "\n"));

        //上传图片资源
        if(liveRoomPic != null){
            ExternalResource res = ExternalResource.create(new File(Info.picturePath + liveRoomPic));
            Image image = group.uploadImage(res);
            builder.append(image);

            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ExternalResource 资源关闭失败");
            }
        }

        //构建消息并返回
        MessageChain chain = builder.build();
        return chain;
    }

}
