package com.fwtai.service.api;

import com.fwtai.api.ApiMonitorValueDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.entity.MonitorValue;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 监测点数据业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-12 12:33:06
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiMonitorValueService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ApiMonitorValueDao apiMonitorValueDao;

    public String add(final MonitorValue monitorValue){
        final PageFormData formData = ToolClient.beanToPageFormData(monitorValue);
        final String p_device_flag = "deviceFlag";
        final String p_data_time = "dataTime";
        final String p_volume = "volume";
        final String validate = ToolClient.validateField(formData,p_device_flag,p_volume,p_data_time);
        if(validate != null)return validate;
        if(formData.getString("deviceFlag").length() > 64){
            return ToolClient.createJsonFail("设备标识过多");
        }
        final String device_flag = formData.getString(p_device_flag);
        final String locationId = apiMonitorValueDao.getLocationId(device_flag);//若系统不存在则不让数据接入
        if(locationId == null){
            return ToolClient.createJsonFail(device_flag+"设备标识不存在,拒绝接入");
        }
        formData.put("device_flag",formData.getString(p_device_flag));
        formData.put("data_time",formData.getString(p_data_time));
        formData.put("location_id",locationId);
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(apiMonitorValueDao.add(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(apiMonitorValueDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = apiMonitorValueDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(apiMonitorValueDao.delById(kid));
    }

    //适用于api接口
    public String listDataPage(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        if(formData == null) return ToolClient.jsonValidateField();
        final HashMap<String,Object> map = apiMonitorValueDao.listData(formData);
        return ToolClient.jsonPage((List<?>) map.get(ConfigFile.rows),(Integer) map.get(ConfigFile.total));
    }
}