package com.Xinh.QQbot.BiliBiliUpInfo.Service.Impl;

import com.Xinh.QQbot.BiliBiliUpInfo.Bean.UpInfo;
import com.Xinh.QQbot.BiliBiliUpInfo.DAO.UpInfoMapper;
import com.Xinh.QQbot.BiliBiliUpInfo.Service.Interfaces.IUpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Create by 76705
 * Description :
 *      DAO
 */

@Service
public class UpInfoService implements IUpInfoService {

    @Autowired
    private UpInfoMapper upInfoMapper;

    //获取所有up主信息
    @Override
    public Map<Long, UpInfo> getUpInfo() {
        return upInfoMapper.getUpInfo();
    }

    //更新关注up的信息
    @Override
    public void updateUpInfo(UpInfo upInfo) {
        upInfoMapper.updateUpInfo(upInfo);
    }

    //关注新的up
    @Override
    public void insertUpInfo(UpInfo upInfo){
        upInfoMapper.insertUpInfo(upInfo);
    }

    //将up主移除关注列表
    @Override
    public void deleteUpInfo(long uid){
        upInfoMapper.deleteUpInfo(uid);
    }

}
