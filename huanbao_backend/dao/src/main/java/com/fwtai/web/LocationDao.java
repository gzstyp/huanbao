package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 检测点信息访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class LocationDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("location.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("location.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("location.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("location.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("location.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("location.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"location.listData","location.listTotal");
    }
}