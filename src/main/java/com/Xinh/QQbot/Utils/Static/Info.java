package com.Xinh.QQbot.Utils.Static;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Create by 76705
 * Description :
 *  保存一些静态信息
 *
 *
 *  好像机器人信息也能放在这里的感觉.......
 */

@Component
@PropertySource("classpath:/properties/App.properties")
public class Info {

    //图片保存路径
    public static String picturePath;
    //本地图片数量
    public static int pictureNum = 0;
    //本地最大允许图片数量
    public static int pictureMax;

    //直播状态
    public enum LiveStatus{

        ON_LIVE (1),
        NORMAL (0),
        NOT_LIVING (-1);

        private int status;

        LiveStatus(int status){
            this.status = status;
        }


    }

    @Value("${Picture.Path}")
    public void setPicturePath(String path){
        picturePath = path;
    }


    @Value("${Picture.Max}")
    private void setPictureMax(int max){
        pictureMax = max;
    }

}
