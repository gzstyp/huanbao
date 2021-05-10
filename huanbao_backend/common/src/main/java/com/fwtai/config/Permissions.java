package com.fwtai.config;

import java.util.List;

/**
 * 保存高并发环境下的用户请求xxx/listData的url角色权限
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020年11月28日 22:11:31
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class Permissions{

    private final static ThreadLocal<List<String>> localUrl = new ThreadLocal<List<String>>();

    //在拦截器获取并保存请求的url,在需要service或dao层或其他地方调用解析即可
    public static List<String> get(){
        return localUrl.get();
    }

    public static void set(final List<String> value){
        localUrl.set(value);
    }

    public static void remove(){
        localUrl.remove();
    }
}