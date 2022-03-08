package com.Xinh.QQbot.BiliBiliUpInfo.DAO;

import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Create by 76705
 * Description : DAO
 */


@Mapper
@Repository
public interface UpInfoMapper {

    @MapKey("uid")
    Map<Long, UpInfo> getUpInfo();

    boolean updateUpInfo(UpInfo upInfo);

    void insertUpInfo(UpInfo upInfo);

    void deleteUpInfo(long uid);

}
