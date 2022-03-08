package com.Xinh.QQbot.BiliBiliMsg.Service.Impl;

import cn.hll520.linling.biliClient.model.dynamic.Dynamic;
import cn.hll520.linling.biliClient.model.dynamic.DynamicDetail;
import cn.hll520.linling.biliClient.model.dynamic.Picture;
import cn.hll520.linling.biliClient.model.user.User;
import cn.hll520.linling.biliClient.model.video.Video;
import com.Xinh.QQbot.BiliBiliClient.Controller.BiliBiliClientController;
import com.Xinh.QQbot.BiliBiliMsg.Bean.DynamicMsg;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.IDynamicMsgService;
import com.Xinh.QQbot.BiliBiliMsg.Service.Interfaces.IPictureService;
import com.Xinh.QQbot.Utils.Builder.DynamicMsgBuilder;
import com.Xinh.QQbot.Utils.Result.Result;
import com.Xinh.QQbot.Utils.Result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by 76705
 * Description :
 *      提供B站动态信息封装的服务
 */

@Service
public class DynamicMsgService implements IDynamicMsgService {

    @Autowired
    private BiliBiliClientController biliClientController;

    @Autowired
    private IPictureService pictureService;

    //动态类别
    private final String COMMON = "发布动态:";
    private final String VIDEO = "发布视频:";
    private final String REPOST = "转发动态:";
    private final String DESC = "并表态:";

    //动态信息分类处理,返回
    @Override
    public Result getDynamicMsg(Dynamic dynamic) {
        Dynamic.DType type = dynamic.getType();
        switch (type) {
            case COMMON:
                return commonDynamic(dynamic);
            case VIDEO:
                return videoDynamic(dynamic);
            case REPOST:
                return repostDynamic(dynamic);
            default:
                return Result.fail(ResultCode.DYNAMIC_TYPE_UNKNOWN);
        }
    }

    //普通动态处理方式
    private Result commonDynamic(Dynamic dynamic) {

        String name;
        String context;
        List<String> pics = new ArrayList<>();
        /**
         * !!!
         * 有的普通动态无法获取用户名称
         * dynamic.getName();
         * 有的普通动态的detail竟然是空的,暂且蒙古
         * dynamic.getDetail();
         * */
        try {
            //1.拿到发布动态的用户信息
            name = dynamic.getName();

            //2.拿到B站动态信息
            DynamicDetail detail = dynamic.getDetail();

            //2.1获取动态的文本信息
            context = detail.getDescription();

            //2.2获取动态中的图片信息
            List<Picture> pictures = detail.getPictures();

            //3.如果有,就获取图片到本地
            if(!pictures.isEmpty()){
                Result pictureResult = pictureService.getPicture(pictures);
                if(!pictureResult.isSuccess())
                    return Result.fail(ResultCode.DYNAMIC_COMMON_FAIL,pictureResult);

                pics = (List<String>) pictureResult.getObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("普通类型动态数据异常");
            return Result.fail(ResultCode.DYNAMIC_COMMON_FAIL,dynamic);
        }

        //4.构造封装动态信息, Builder要对空的图片集合进行处理
        DynamicMsg dynamicMsg = new DynamicMsgBuilder().buildDynamicMsg()
                .buildUpName(name)
                .buildType(COMMON)
                .buildContext(context)
                .buildPics(pics)
                .build();

        return Result.success(dynamicMsg);
    }

    //视频动态处理方式
    private Result videoDynamic(Dynamic dynamic) {

        String name;
        String context;
        List<String> pics = new ArrayList<>();

        try {
            //1.获取视频信息
            Video video = dynamic.getVideo();

            //1.1获取用户信息
            name = video.getOwner().getName();

            //1.2获取up主所发布的视频标题,简介以及URL
            String vTitle = video.getTitle() + "\n";
            String vDesc = video.getDesc() + "\n";
            String vUrl = "www.bilibili.com/video/" + dynamic.getData().getBvid() + "\n";

            context = vTitle + vDesc + vUrl;

            //2.获取视频封面,由于只有一张图片,调用 String getPicture(String)
            Result pictureResult = pictureService.getPicture(video.getPic());
            if(!pictureResult.isSuccess())
                return Result.fail(ResultCode.DYNAMIC_VIDEO_FAIL,pictureResult);

            //2.1这里...... 可把String加到list去build Msg
            //也可以在builder进行重载buildPics(String)
            //这里采取第一种的处理方式
            String fileName = (String) pictureResult.getObject();
            pics.add(fileName);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("视频类型动态数据异常");
            return Result.fail(ResultCode.DYNAMIC_VIDEO_FAIL,dynamic);
        }

        //3 构建
        DynamicMsg dynamicMsg = new DynamicMsgBuilder().buildDynamicMsg()
                .buildUpName(name)
                .buildType(VIDEO)
                .buildContext(context)
                .buildPics(pics)
                .build();

        return Result.success(dynamicMsg);
    }

    /**
     * 转发动态的处理方式,可能会有多层的嵌套 : a -> b -> c
     * 暂时没有好的想法处理
     *
     * 暂时采用格式
     * a 转发动态 :
     * 被转发的动态内容
     * a 的转发时的表态
     *
     * 我擦 好乱的变量名
     */
    private Result repostDynamic(Dynamic dynamic) {

        String name;
        String context;
        String desc;
        List<String> pics;

        try {
            //1.拿到up主的用户名以及转发时的表态
            //name特殊处理  转发类型动态中 用户名 服务器返回数据为空
            name = getUserName(dynamic);
            desc = DESC + dynamic.getRepost().getContent();

            //2.拿到被转发的动态的ID
            Long origDyId = dynamic.getData().getOrig_dy_id();

            //2.1通过动态ID拿到被转发的动态
            Result dynamicResult = biliClientController.getDynamicByID(origDyId);
            if(!dynamicResult.isSuccess())
                return Result.fail(ResultCode.DYNAMIC_REPOST_FAIL,dynamicResult);

            Dynamic dynamicRepost = (Dynamic) dynamicResult.getObject();

            //2.2将被转发的动态信息进行封装
            Result dynamicMsgResult = getDynamicMsg(dynamicRepost);
            if(!dynamicMsgResult.isSuccess())
                return Result.fail(ResultCode.DYNAMIC_REPOST_FAIL,dynamicMsgResult);

            DynamicMsg dynamicMsg = (DynamicMsg) dynamicMsgResult.getObject();

            //2.3提取被转发的动态的信息
            context = dynamicMsg.toString();
            pics = dynamicMsg.getPics();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转发类型动态数据异常");
            return Result.fail(ResultCode.DYNAMIC_REPOST_FAIL,dynamic);
        }

        //构建 同样的,在builder处理图片集合为空的情况
        DynamicMsg dynamicMsg = new DynamicMsgBuilder().buildDynamicMsg()
                .buildUpName(name)
                .buildType(REPOST)
                .buildContext(context + "\n" + desc)
                .buildPics(pics)
                .build();

        return Result.success(dynamicMsg);
    }

    //用于应对 REPOST类型动态中用户名为空的情况
    private String getUserName(Dynamic dynamic){
        Long UID = dynamic.getUid();
        Result userResult = biliClientController.getUserByUID(UID);
        if(!userResult.isSuccess())
            return "用户名未知";

        User user = (User) userResult.getObject();
        return user.getName();
    }

}