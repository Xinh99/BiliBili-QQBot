package com.Xinh.QQbot.BiliBiliMsg.Service.Impl;

import cn.hll520.linling.biliClient.model.dynamic.Picture;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.IPictureService;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import com.Xinh.QQbot.Utils.Static.Info;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by 76705
 * Description :
 *      提供将B站图片内容获取到本地的服务
 */

@Service
public class PictureService implements IPictureService {

    @Override
    public Result downloadPicture(String picUrl) {

        String fileName;
        try {
            URL url = new URL(picUrl);

            /**
             * 设定图片名以及图片保存位置 imgXX.jpg
             * 图片保存位置暂定为写死固定
             * !!!如有需要进行更改!!!
             * */
            //1.获取IO流
            DataInputStream dis = new DataInputStream(url.openStream());
            fileName = "img" + Info.pictureNum++ + ".jpg";
            OutputStream os = new FileOutputStream(Info.picturePath + fileName);

            //2.写入本地
            byte buffer[] = new byte[1024];
            int len = -1;
            while ((len = dis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            //3.关闭资源
            os.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("动态图片获取异常");
            return Result.fail(ResultCode.PICTURE_DOWNLOAD_FAIL);
        }

        return Result.success(fileName);
    }

    //一张图片的处理方式
    @Override
    public Result getPicture(String url) {
        if(url == null)
            return Result.fail(ResultCode.URL_WRONG);

        Result result = downloadPicture(url);
        if(!result.isSuccess())
            return result;

        String fileName = (String) result.getObject();
        return Result.success(fileName);
    }

    //多张图片
    /**
     * 多张图片的获取 目前只针对多张图片的普通动态 目前...
     * 采取的策略是逐张获取,无效url跳过
     * 若全部无效,才返回失败
     * return fileNames 所有图片名的集合
     * */
    @Override
    public Result getPicture(List<Picture> urls) {
        List<String>fileNames = new ArrayList<>();

        for(Picture url : urls){
            if(url == null)
                continue;

            Result result = downloadPicture(url.getImg_src());
            if(!result.isSuccess())
                continue;

            String fileName = (String) result.getObject();
            fileNames.add(fileName);
        }

        if(fileNames.isEmpty())
            return Result.fail(ResultCode.PICTURE_DOWNLOAD_FAIL);

        return Result.success(fileNames);
    }




}
