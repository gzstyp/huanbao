package com.fwtai.datasource;

/**
 * 写库|读库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/7 23:14
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public enum DataSourceType {

    READ("read","读库"),WRITE("write","写库");

    private String type;
    private String name;

    DataSourceType(final String type,final String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}