package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class MonitorValueDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("monitor_value.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("monitor_value.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("monitor_value.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("monitor_value.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("monitor_value.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("monitor_value.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"monitor_value.listData","monitor_value.listTotal");
    }

    public List<HashMap<String,Object>> getListTable(final PageFormData formData){
        return dao.queryForListHashMap("monitor_value.getListTable",formData);
    }

    public List<HashMap<String,Object>> getDeviceStatus(final PageFormData formData){
        return dao.queryForListHashMap("monitor_value.getDeviceStatus",formData);
    }

    public List<HashMap<String,Object>> queryMapGreaterLess(){
        return dao.queryForListHashMap("monitor_value.queryMapGreaterLess");
    }

    public HashMap<String,Object> queryMapGreaterValue(){
        return dao.queryForHashMap("monitor_value.queryMapGreaterValue");
    }

    public Integer getRefreshValue(){
        return dao.queryForInteger("monitor_value.getRefreshValue");
    }

    public List<HashMap<String,Object>> getAgo10Hour(final String kid){
        return dao.queryForListHashMap("monitor_value.getAgo10Hour",kid);
    }

    public List<HashMap<String,Object>> get0toNow(final PageFormData formData){
        return dao.queryForListHashMap("monitor_value.get0toNow",formData);
    }

    public List<HashMap<String,Object>> getLately365Day(final PageFormData formData){
        return dao.queryForListHashMap("monitor_value.getLately365Day",formData);
    }

    public List<HashMap<String,Object>> getLately7Day(final PageFormData formData){
        return dao.queryForListHashMap("monitor_value.getLately7Day",formData);
    }

    public List<HashMap<String,Object>> getTotal60(final String kid){
        return dao.queryForListHashMap("monitor_value.getTotal60",kid);
    }
}