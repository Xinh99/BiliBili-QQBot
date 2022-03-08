package com.Xinh.QQbot.BiliBiliUpInfo.Service.Interfaces;

import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import org.apache.ibatis.annotations.MapKey;

import java.util.Collection;
import java.util.Map;

/**
 * Create by 76705
 * Description :
 */

public interface IUpInfoService {

    Map<Long, UpInfo> getUpInfo();

    void updateUpInfo(UpInfo upInfo);

    void insertUpInfo(UpInfo upInfo);

    void deleteUpInfo(long uid);


}
