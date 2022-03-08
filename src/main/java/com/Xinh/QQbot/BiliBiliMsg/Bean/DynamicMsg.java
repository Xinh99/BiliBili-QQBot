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
import java.io.Serializable;
import java.util.List;

/**
 * Create by 76705
 * Description : 动态信息包装类
 *      对B站返回的动态信息中需要的部分进行封装
 */

@Data
public class DynamicMsg implements Serializable {

    private static final long serialVersionUID = -1653084332100439068L;

    //用户名
    private String upName;
    //动态类型
    private String type;
    //动态文字信息
    private String context;
    //动态图片信息
    private List<String> pics;

    @Override
    public String toString(){
        return upName + type + "\n"
                + context;
    }

    public MessageChain toChain(Group group){
        //构建Mirai中QQ消息的消息链
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.append(new PlainText(upName + type + "\n"))
                .append(context);

        //将图片资源上传至Mirai中
        if(pics != null){
            ExternalResource res = null;
            for(String pic : pics){
                res = ExternalResource.create(new File(Info.picturePath + pic));
                Image image = group.uploadImage(res);
                builder.append(image);
            }

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
