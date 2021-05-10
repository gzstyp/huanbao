package com.fwtai.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 生成json的数据格式,用法: JsonData builder().successMsg();
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/12/1 13:52
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class JsonData{

    private Integer code = 199;

    private String msg = "操作失败";

    private Object data;

    private JsonData(){}

    public static JsonData builder(){
        return new JsonData();
    }

    /**code=200*/
    public JsonData success(){
        this.code = 200;
        return this;
    }

    /**code=199*/
    public JsonData failure(){
        this.code = 199;
        return this;
    }

    public JsonData failure(final Integer code){
        this.code = code;
        return this;
    }

    public JsonData msg(){
        this.msg = "操作成功";
        return this;
    }

    public JsonData failureMsg(){
        this.msg = "操作失败";
        return this;
    }

    /**可指定code*/
    public JsonData code(final Integer code){
        this.code = code;
        return this;
    }

    /**可指定msg*/
    public JsonData msg(final String msg){
        this.msg = msg;
        return this;
    }

    /**赋值给data*/
    public JsonData data(final Object data){
        this.data = data;
        return this;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public Object getData(){
        return data;
    }

    public String build(){
        return JSONObject.toJSONString(this);
    }

    /**返回 {"code":200,"msg":"操作成功"}*/
    public String successMsg(){
        return builder().code(200).msg("操作成功").build();
    }

    /**返回 {"code":200,"msg":"指定的msg"}*/
    public String successMsg(final String msg){
        return builder().code(200).msg(msg).build();
    }

    /**返回 {"code":200,"msg":"指定的msg","data":[{}]或{}}*/
    public String successMsgData(final String msg,final Object data){
        return builder().code(200).msg(msg).data(data).build();
    }

    /**返回 {"code":200,"msg":"指定的msg","data":[{}]或{}}*/
    public String successData(final Object data){
        return builder().code(200).msg("操作成功").data(data).build();
    }

    /**返回 {"code":199,"msg":"操作失败"}*/
    public String fail(){
        return builder().build();
    }

    /**返回 {"code":199,"msg":"指定的msg"}*/
    public String fail(final String msg){
        return builder().msg(msg).build();
    }
}