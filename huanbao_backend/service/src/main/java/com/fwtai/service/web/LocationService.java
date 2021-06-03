package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.core.AreaDao;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.DeviceNoiseDao;
import com.fwtai.web.LocationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 检测点信息业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-10 19:00:46
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class LocationService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private LocationDao locationDao;

    @Resource
    private DeviceNoiseDao deviceNoiseDao;

    @Resource
    private AreaDao areaDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_site_id = "site_id";
        final String p_device_id = "device_id";
        final String p_name = "name";
        final String p_longs = "longs";
        final String p_lat = "lat";
        final String p_county_id = "county_id";
        final String p_county_name = "county_name";
        final String p_province_id = "province_id";
        final String p_province_name = "province_name";
        final String p_city_id = "city_id";
        final String p_city_name = "city_name";
        final String p_top = "top";
        final String p_left = "left";
        final String validate = ToolClient.validateField(formData,p_site_id,p_device_id,p_name,p_longs,p_lat,p_county_id,p_county_name,p_province_id,p_province_name,p_city_id,p_city_name);
        if(validate != null)return validate;
        final String top = formData.getString(p_top);
        final String left = formData.getString(p_left);
        if(top != null){
            try {
                formData.put("top",Integer.parseInt(top));
            } catch (Exception e) {
                return ToolClient.createJsonFail("像素只能是数字");
            }
        }
        if(left != null){
            try {
                formData.put("left",Integer.parseInt(left));
            } catch (Exception e) {
                return ToolClient.createJsonFail("像素只能是数字");
            }
        }
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(locationDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_device_id = "device_id";
        final String p_site_id = "site_id";
        final String p_name = "name";
        final String p_longs = "longs";
        final String p_lat = "lat";
        final String p_county_id = "county_id";
        final String p_county_name = "county_name";
        final String p_top = "top";
        final String p_left = "left";
        final String validate = ToolClient.validateField(formData,p_site_id,p_device_id,p_name,p_longs,p_lat,p_county_id,p_county_name,p_kid);
        if(validate != null)return validate;
        final String top = formData.getString(p_top);
        final String left = formData.getString(p_left);
        if(top != null){
            try {
                formData.put("top",Integer.parseInt(top));
            } catch (Exception e) {
                return ToolClient.createJsonFail("像素只能是数字");
            }
        }
        if(left != null){
            try {
                formData.put("left",Integer.parseInt(left));
            } catch (Exception e) {
                return ToolClient.createJsonFail("像素只能是数字");
            }
        }
        final String exist_key = locationDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(locationDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(locationDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = locationDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(locationDao.delById(kid));
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
        return ToolClient.executeRows(locationDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
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
            final HashMap<String,Object> map = locationDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }

    /**查询位置地点名称*/
    public String getSiteList(final String value){
        return ToolClient.queryJson(locationDao.getSiteList(value));
    }

    /**获取设备标识名称*/
    public String getDeviceList(final String value){
        return ToolClient.queryJson(deviceNoiseDao.getDeviceList(value));
    }

    public String getAreaTree(Long pid){
        if(pid == null){
            pid = 0L;
        }
        return ToolClient.queryJson(areaDao.getAreaTree(pid));
    }
}