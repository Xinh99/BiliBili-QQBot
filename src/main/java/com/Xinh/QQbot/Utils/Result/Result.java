package com.Xinh.QQbot.Utils.Result;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by 76705
 * Description :
 *      返回结果
 */

@Data
public class Result implements Serializable {

    //状态码
    private Integer code;

    //返回信息
    private String message;

    //返回对象
    private Object object;

    Result(){}

    //呃呃呃呃
    Result(ResultCode resultCode,Object object){
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.object = object;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object object){
        Result result = new Result();
        result.object = object;
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result fail(ResultCode resultCode){
        Result result = new Result();
        result.setCode(resultCode);
        return result;
    }

    public static Result fail(ResultCode resultCode, Object object){
        Result result = new Result();
        result.setObject(object);
        result.setCode(resultCode);
        return result;
    }

    public boolean isSuccess(){
        return this.code == 200;
    }

    private void setCode(ResultCode resultCode){
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

}
