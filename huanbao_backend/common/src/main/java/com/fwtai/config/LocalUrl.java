package com.fwtai.config;

/**
 * 保存高并发环境下的用户请求url路径
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-09 16:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class LocalUrl{

    private final static ThreadLocal<String> localUrl = new ThreadLocal<String>();

    //在拦截器获取并保存请求的url,在需要service或dao层或其他地方调用解析即可
    public static String get(){
        return localUrl.get();
    }

    public static void set(final String value){
        localUrl.set(value);
    }

    public static void remove(){
        localUrl.remove();
    }
}