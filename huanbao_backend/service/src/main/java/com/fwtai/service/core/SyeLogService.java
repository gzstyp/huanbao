package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.core.SyeLogDao;
import com.fwtai.service.AsyncService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * sys_log业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-17 17:58:03
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class SyeLogService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SyeLogDao syelogDao;

    @Resource
    private AsyncService asyncService;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_ip = "ip";
        final String p_result = "result";
        final String p_user_name = "user_name";
        final String validate = ToolClient.validateField(formData,p_ip,p_result,p_user_name);
        if(validate != null)return validate;
        if(formData.getString("ip").length() > 16){
            return ToolClient.createJsonFail("登录ip字数太长");
        }
        if(formData.getString("user_name").length() > 64){
            return ToolClient.createJsonFail("登录账号字数太长");
        }
        final String fieldInteger = ToolClient.validateInteger(formData,p_result);
        if(fieldInteger != null)return fieldInteger;
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(syelogDao.add(formData));
    }

    public void addLoginLog(final HashMap<String,Object> params){
        asyncService.addLoginLog(params);
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_ip = "ip";
        final String p_result = "result";
        final String p_user_name = "user_name";
        final String validate = ToolClient.validateField(formData,p_ip,p_result,p_user_name,p_kid);
        if(validate != null)return validate;
        if(formData.getString("ip").length() > 16){
            return ToolClient.createJsonFail("登录ip字数太长");
        }
        if(formData.getString("user_name").length() > 64){
            return ToolClient.createJsonFail("登录账号字数太长");
        }
        final String fieldInteger = ToolClient.validateInteger(formData,p_result);
        if(fieldInteger != null)return fieldInteger;
        final String exist_key = syelogDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(syelogDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(syelogDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = syelogDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(syelogDao.delById(kid));
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
        return ToolClient.executeRows(syelogDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
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
            final HashMap<String,Object> map = syelogDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }
}