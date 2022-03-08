package com.Xinh.QQbot.BiliBiliUpInfo.Bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by 76705
 * Description :
 *      封装B站up主的信息
 */

@Data
public class UpInfo implements Serializable {

    private static final long serialVersionUID = -3182719789536809029L;

    //up主uid
    private long uid;
    //up主名字
    private String upname;
    //up主最新动态更新时间
    private long updatetime;
    //up主直播状态
    private Integer livestate;

}
