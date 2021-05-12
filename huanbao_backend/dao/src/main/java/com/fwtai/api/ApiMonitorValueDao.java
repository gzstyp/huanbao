package com.fwtai.api;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 监测点数据访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class ApiMonitorValueDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("api_monitor_value.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("api_monitor_value.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("api_monitor_value.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("api_monitor_value.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("api_monitor_value.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("api_monitor_value.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"api_monitor_value.listData","api_monitor_value.listTotal");
    }

    public String getSiteName(final String deviceFlag){
        return dao.queryForString("api_monitor_value.getSiteName",deviceFlag);
    }
}