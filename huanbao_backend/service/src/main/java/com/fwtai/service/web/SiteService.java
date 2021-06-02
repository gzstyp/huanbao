package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.SiteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 位置地点(区域)业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-13 13:13:55
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class SiteService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SiteDao siteDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_lat = "lat";
        final String p_lng = "lng";
        final String p_name = "name";
        final String p_address = "address";
        final String validate = ToolClient.validateField(formData,p_lat,p_lng,p_name,p_address);
        if(validate != null)return validate;
        if(formData.getString("lat").length() > 20){
            return ToolClient.createJsonFail("纬度内容字数太长");
        }
        if(formData.getString("lng").length() > 20){
            return ToolClient.createJsonFail("经度内容字数太长");
        }
        if(formData.getString("name").length() > 128){
            return ToolClient.createJsonFail("位置地点名称字数太长");
        }
        if(formData.getString("address").length() > 128){
            return ToolClient.createJsonFail("位置地址字数太长");
        }
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(siteDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_lat = "lat";
        final String p_lng = "lng";
        final String p_name = "name";
        final String p_address = "address";
        final String validate = ToolClient.validateField(formData,p_lat,p_lng,p_name,p_kid,p_address);
        if(validate != null)return validate;
        if(formData.getString("lat").length() > 20){
            return ToolClient.createJsonFail("纬度字数太长");
        }
        if(formData.getString("lng").length() > 20){
            return ToolClient.createJsonFail("经度字数太长");
        }
        if(formData.getString("name").length() > 128){
            return ToolClient.createJsonFail("位置地点名称字数太长");
        }
        if(formData.getString("address").length() > 128){
            return ToolClient.createJsonFail("位置地址字数太长");
        }
        final String exist_key = siteDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(siteDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(siteDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = siteDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(siteDao.delById(kid));
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
        return ToolClient.executeRows(siteDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
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
            final HashMap<String,Object> map = siteDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }
}