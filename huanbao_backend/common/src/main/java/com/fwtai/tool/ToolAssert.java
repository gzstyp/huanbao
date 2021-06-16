package com.fwtai.tool;

import com.fwtai.exception.InvalidParams;

/**
 * 验证是否已输入值
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/6/16 11:22
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolAssert{

    //用法：ToolAssert.isBlank(formData.getString(p_name),"请输入区域名称");
    public static void isBlank(final String value,final String message) {
        if (value == null || value.length() <= 0){
            throw new InvalidParams(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new InvalidParams(message);
        }
    }
}