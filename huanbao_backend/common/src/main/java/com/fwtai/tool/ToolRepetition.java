package com.fwtai.tool;

import org.apache.commons.collections4.map.LRUMap;

/**
 * 防止数据重复提交
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-07-26 9:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolRepetition{

    // 根据 LRU(Least Recently Used，最近最少使用)算法淘汰数据的 Map 集合，最大容量 100 个
    private static LRUMap<String,Object> reqCache = new LRUMap<String,Object>(100);

    /**幂等性判断,用于编辑操作,用法:if (ToolRepetition.judge(id,this.getClass())){return "执行失败";}*/
    public static boolean judge(final String key,final Object lockClass) {
        synchronized (lockClass){
            // 重复请求判断
            if (reqCache.containsKey(key)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + key);
                return true;
            }
            // 非重复请求，存储请求 ID
            reqCache.put(key,1);
        }
        return false;
    }
}