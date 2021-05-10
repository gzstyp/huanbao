package com.fwtai.exception;

/**
 * 自定义通用的异常信息
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-03-16 10:47
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class FwtaiEeception extends RuntimeException{

    private Integer code;
    private String msg;

    public FwtaiEeception(final Integer code,final String msg){
        this.code = code;
        this.msg = msg;
    }

    public FwtaiEeception(final String msg){
        this.code = 204;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public void setCode(Integer code){
        this.code = code;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
}