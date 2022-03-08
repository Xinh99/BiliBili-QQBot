package com.Xinh.QQbot.Utils.Result;

/**
 * Create by 76705
 * Description :
 *      返回的状态码
 */

/**
 * params : Integer, String
 * */
public enum ResultCode {

    SUCCESS(200,"成功"),

    //UpInfo
    UP_NOT_FOLLOWING(20001,"没有关注该up主"),
    NO_ONE_FOLLOWING(20002,"关注列表为空"),

    //BiliBiliClient
    DYNAMIC_GET_EXCEPTION(30001,"B站动态获取异常"),
    LIVEROOM_GET_EXCEPTION(30002,"直播间获取异常"),
    USER_GET_EXCEPTION(30003,"B站用户获取异常"),

    DYNAMIC_NOT_UPDATE(30004,"该up主没有更新动态"),

    DYNAMIC_GET_FAIL(30005,"B站动态获取失败"),
    USER_GET_FAIL(30007,"B站用户获取失败"),


    //BiliBiliMessage
    URL_WRONG(40001,"url错误"),
    PICTURE_DOWNLOAD_FAIL(40002,"图片下载失败"),
    PICTURE_DONT_NEED_DELETE(40003,"图片无需删除"),
    PICTURE_DELETE_FAIL(40004,"图片删除失败"),
    PICTURE_DELETE_EXCEPTION(40005,"图片删除异常"),

    DYNAMIC_TYPE_UNKNOWN(40013,"不支持该动态类型"),
    DYNAMIC_COMMON_FAIL(40014,"普通类型动态数据异常"),
    DYNAMIC_VIDEO_FAIL(40015,"视频类型动态数据异常"),
    DYNAMIC_REPOST_FAIL(40016,"转发类型动态数据异常"),
    DYNAMICMSG_GET_FAIL(40017,"动态信息获取失败"),


    LIVEROOMMSG_GET_FAIL(40020,"直播间信息获取失败"),
    LIVEROOMMSG_PACK_FAIL(40021,"直播间信息封装失败"),
    UP_IS_NOT_LIVING(40022,"up主没有开启直播"),


    //QQbot
    IS_NOT_COMMAND(50001,"非命令格式"),

    COMMAND_UNKNOWN(50002,"主人还没教会我这个捏"),
    COMMAND_WRONG(50003,"Bot被玩壞了!\n這肯定不是我的問題!絕對不是!"),
    COMMAND_TARGET_WRONG(50004,"我超,这™发的是啥啊"),

    NOT_OWNER(50005,"抱歉,我不能听你的,你不是我的master"),

    USER_NOT_FOUND(50010,"找不到该B站用户捏"),
    USER_ADD_SUCCESS(50011,"标记->"),
    USER_ADD_FAIL(50012,"我超啊,你已经关注他了"),
    USER_REMOVE_SUCCESS(50013,"取关->"),
    USER_REMOVE_FAIL(50014,"关注列表里面也妹有他啊我寻思着"),


    ;





    private final Integer code;
    private final String message;


    ResultCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer code(){
        return this.code;
    }

    public String message(){
        return this.message;
    }


}
