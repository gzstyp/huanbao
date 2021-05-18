package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.core.ParamsDao;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数管理业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-18 11:34:27
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ParamsService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ParamsDao paramsDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_ky = "ky";
        final String p_name = "name";
        final String p_ve = "ve";
        final String validate = ToolClient.validateField(formData,p_ky,p_name,p_ve);
        if(validate != null)return validate;
        if(formData.getString("ky").length() > 128){
            return ToolClient.createJsonFail("参数的键key字数太长");
        }
        if(formData.getString("name").length() > 128){
            return ToolClient.createJsonFail("参数的名称字数太长");
        }
        if(formData.getString("ve").length() > 128){
            return ToolClient.createJsonFail("参数的值value字数太长");
        }
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(paramsDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_ky = "ky";
        final String p_name = "name";
        final String p_ve = "ve";
        final String validate = ToolClient.validateField(formData,p_ky,p_name,p_ve,p_kid);
        if(validate != null)return validate;
        if(formData.getString("ky").length() > 128){
            return ToolClient.createJsonFail("参数的键key字数太长");
        }
        if(formData.getString("name").length() > 128){
            return ToolClient.createJsonFail("参数的名称字数太长");
        }
        if(formData.getString("ve").length() > 128){
            return ToolClient.createJsonFail("参数的值value字数太长");
        }
        final String exist_key = paramsDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(paramsDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(paramsDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = paramsDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(paramsDao.delById(kid));
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
        return ToolClient.executeRows(paramsDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    public String listData(PageFormData formData){
        final String p_iColumns = "iColumns";
        final String validate = ToolClient.validateField(formData,p_iColumns);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(formData,p_iColumns);
        if(fieldInteger != null)return fieldInteger;
        try {
            formData = ToolClient.dataTableMysql(formData);
            if(formData == null)return ToolClient.jsonValidateField();
            final HashMap<String,Object> map = paramsDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }
}