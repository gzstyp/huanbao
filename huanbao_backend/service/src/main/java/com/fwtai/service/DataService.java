package com.fwtai.service;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 仅适用于单表的删除、批量删除,详情
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-09-27 17:09
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public class DataService{

    @Autowired
    private DaoHandle daoHandle;

    protected final String delById(final PageFormData formData,final String sqlMapId){
        final String p_kid = "id";
        final String validateField = ToolClient.validateField(formData,p_kid);
        if(validateField != null)return validateField;
        return ToolClient.executeRows(daoHandle.execute(sqlMapId,formData.getString(p_kid)));
    }

    protected final String delByKeys(final PageFormData formData,final String sqlMapId){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final ArrayList<Object> lists = ToolString.jsonArrayToList(formData.get(p_ids));
        if(lists.size() <= 0){
            return ToolClient.createJsonFail("请选择要删除的数据");
        }
        return ToolClient.executeRows(daoHandle.execute(sqlMapId,lists),"操作成功","数据已不存在,刷新重试");
    }

    protected final String queryById(final PageFormData formData,final String sqlMapId){
        final String p_id = "id";
        final String validate = ToolClient.validateField(formData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(daoHandle.queryForHashMap(sqlMapId,formData.getString(p_id)));
    }

    protected final String filterList(final PageFormData pageFormData){
        final String p_iColumns = "iColumns";
        final String validate = ToolClient.validateField(pageFormData,p_iColumns);
        if(validate != null) return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,p_iColumns);
        if(fieldInteger != null) return fieldInteger;
        return null;
    }
}