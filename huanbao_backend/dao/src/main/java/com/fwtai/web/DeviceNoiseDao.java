package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 监测设备访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class DeviceNoiseDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("device_noise.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("device_noise.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("device_noise.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("device_noise.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("device_noise.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("device_noise.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"device_noise.listData","device_noise.listTotal");
    }

    public Integer queryDeviceTotal(){
        return dao.queryForInteger("device_noise.queryDeviceTotal");
    }
}