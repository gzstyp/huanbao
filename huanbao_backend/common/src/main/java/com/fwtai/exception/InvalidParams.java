package com.fwtai.exception;

/**
 * 构建无效请求参数的自定义异常,用法:throw new InvalidParams("请求参数有误");final String json = e.getMessage();
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/3/1 14:59
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class InvalidParams extends RuntimeException{

    public InvalidParams(){
        super();
    }

    public InvalidParams(final String msg){
        super(HandleException.buildJson(msg));
    }
}