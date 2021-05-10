package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.controller.core.DictionaryDao;
import com.fwtai.service.DataService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 字典表业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-07-15 14:52:53
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class DictionaryService extends DataService{

    @Resource
    private DictionaryDao dictionaryDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_NAME = "NAME";
        final String p_SORT = "SORT";
        final String validate = ToolClient.validateField(formData,p_NAME);
        if(validate != null)return validate;
        final String sort = formData.getString(p_SORT);
        if(sort != null){
            final String fieldInteger = ToolClient.validateInteger(formData,p_SORT);
            if(fieldInteger != null)return fieldInteger;
        }
        formData.put("KID",ToolString.getIdsChar32());
        return ToolClient.executeRows(dictionaryDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_KID = "KID";
        final String p_NAME = "NAME";
        final String p_SORT = "SORT";
        final String validate = ToolClient.validateField(formData,p_NAME,p_KID);
        if(validate != null)return validate;
        final String sort = formData.getString(p_SORT);
        if(sort != null){
            final String fieldInteger = ToolClient.validateInteger(formData,p_SORT);
            if(fieldInteger != null)return fieldInteger;
        }
        final String exist_key = dictionaryDao.queryExistById(formData.getString(p_KID));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(dictionaryDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(dictionaryDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = dictionaryDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(dictionaryDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        if(lists == null || lists.size() <= 0){
            return ToolClient.createJsonFail("请选择要删除的数据");
        }
        return ToolClient.executeRows(dictionaryDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    public String listData(PageFormData formData){
        final String filter = super.filterList(formData);
        if(filter != null) return filter;
        try {
            formData = ToolClient.dataTableMysql(formData);
            if(formData == null)return ToolClient.jsonValidateField();
            final HashMap<String,Object> map = dictionaryDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }

    /*根据父级字典ID查询数据字典数据*/
    public String queryDictData(final String kid){
        return ToolClient.queryJson(dictionaryDao.queryDictData(kid));
    }
}