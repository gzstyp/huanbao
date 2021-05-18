package com.fwtai.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 参数管理访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class ParamsDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("params.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("params.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("params.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("params.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("params.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("params.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"params.listData","params.listTotal");
    }
}