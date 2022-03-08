package com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces;

import cn.hll520.linling.biliClient.model.dynamic.Picture;
import com.Xinh.QQbot.Utils.Result.Result;

import java.util.List;

/**
 * Create by 76705
 * Description :
 */

public interface IPictureService {

    Result downloadPicture(String url);

    Result getPicture(String url);

    Result getPicture(List<Picture> urls);



}
