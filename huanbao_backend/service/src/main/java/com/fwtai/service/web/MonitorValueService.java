package com.fwtai.service.web;

import com.fwtai.bean.ImagePng;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolImage;
import com.fwtai.tool.ToolString;
import com.fwtai.web.MonitorValueDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 监测点数据业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-12 11:55:05
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class MonitorValueService{

    @Value("${upload_dir_window}")
    private String dir_window;

    @Value("${upload_dir_linux}")
    private String dir_linux;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MonitorValueDao monitorvalueDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_device_flag = "device_flag";
        final String p_location_id = "location_id";
        final String validate = ToolClient.validateField(formData,p_device_flag,p_location_id);
        if(validate != null)return validate;
        if(formData.getString("device_flag").length() > 64){
            return ToolClient.createJsonFail("设备标识过多");
        }
        if(formData.getString("location_id").length() > 32){
            return ToolClient.createJsonFail("数据有误");
        }
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(monitorvalueDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_device_flag = "device_flag";
        final String p_location_id = "location_id";
        final String validate = ToolClient.validateField(formData,p_device_flag,p_location_id,p_kid);
        if(validate != null)return validate;
        if(formData.getString("device_flag").length() > 64){
            return ToolClient.createJsonFail("设备标识过多");
        }
        if(formData.getString("location_id").length() > 32){
            return ToolClient.createJsonFail("监测点位置有误");
        }
        final String exist_key = monitorvalueDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(monitorvalueDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(monitorvalueDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = monitorvalueDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(monitorvalueDao.delById(kid));
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
        return ToolClient.executeRows(monitorvalueDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    public String listData(PageFormData formData){
        final String p_iColumns = "iColumns";
        final String validate = ToolClient.validateField(formData,p_iColumns);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(formData,p_iColumns);
        if(fieldInteger != null)return fieldInteger;
        final String date_start = formData.getString("date_start");
        final String date_end = formData.getString("date_end");
        if(date_start != null){
            final boolean b = ToolString.checkDateTime(date_start);
            if(!b){
                return ToolClient.createJsonFail("开始日期格式有误");
            }
        }
        if(date_end != null){
            final boolean b = ToolString.checkDateTime(date_end);
            if(!b){
                return ToolClient.createJsonFail("截至日期格式有误");
            }
        }
        try {
            formData = ToolClient.dataTableMysql(formData);
            if(formData == null)return ToolClient.jsonValidateField();
            final HashMap<String,Object> map = monitorvalueDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }

    public String getListMap(final PageFormData formData){
        final String siteIds = formData.getString("siteIds");
        final String countyIds = formData.getString("countyIds");
        if(siteIds != null){
            final ArrayList<String> isds = ToolString.keysToList(siteIds);
            if(isds != null && isds.size() > 0){
                formData.put("siteIds",isds);
            }
        }
        if(countyIds != null){
            final ArrayList<String> isds = ToolString.keysToList(countyIds);
            if(isds != null && isds.size() > 0){
                formData.put("countyIds",isds);
            }
        }
        final String min = formData.getString("min");
        final String max = formData.getString("max");
        formData.remove("min");
        formData.remove("max");
        if(min != null){
            if(max != null){
                final List<HashMap<String,Object>> list = monitorvalueDao.queryMapGreaterLess();
                final HashMap<String,Object> map = new HashMap<>();
                for(int i = 0; i < list.size(); i++){
                    final HashMap<String,Object> object = list.get(i);
                    final String ky = (String)object.get("ky");
                    final Object ve = object.get("ve");
                    switch (ky){
                        case "overproof":
                            map.put("overproof",ve);
                            break;
                        case "severityover":
                            map.put("severityover",ve);
                            break;
                        default:
                            break;
                    }
                }
                formData.put("min",map.get("overproof"));
                formData.put("max",map.get("severityover"));
            }
            if(max == null){
                final HashMap<String,Object> map = monitorvalueDao.queryMapGreaterValue();
                formData.put("min",map.getOrDefault("ve",65));
            }
        }
        final List<HashMap<String,Object>> list = monitorvalueDao.getListTable(formData);
        final String baseDir = ToolString.isLinuxOS() ? dir_linux : dir_window;
        list.forEach(map->{
            final String value = (String)map.get("count");
            final String siteName = (String)map.get("siteName");
            final String icon = ToolString.getIdsChar32();
            //final ImagePng png = ToolImage.getPng(baseDir,icon,siteName+"("+value+")");
            final ImagePng png = ToolImage.getPng(baseDir,icon,value);
            if(png != null){
                map.put("icon",png.getIcon()+".png");
                map.put("width",png.getWidth());
            }
        });
        return ToolClient.queryJson(list);
    }

    //获取设备的联机状态
    public String getDeviceStatus(final PageFormData formData){
        final String siteIds = formData.getString("siteIds");
        final String countyIds = formData.getString("countyIds");
        if(siteIds != null){
            final ArrayList<String> isds = ToolString.keysToList(siteIds);
            if(isds != null && isds.size() > 0){
                formData.put("siteIds",isds);
            }
        }
        if(countyIds != null){
            final ArrayList<String> isds = ToolString.keysToList(countyIds);
            if(isds != null && isds.size() > 0){
                formData.put("countyIds",isds);
            }
        }
        return ToolClient.queryJson(monitorvalueDao.getDeviceStatus(formData));
    }

    public String getRefreshValue(){
        final Integer result = monitorvalueDao.getRefreshValue();
        return ToolClient.queryJson(result);
    }

    public String getAgo10Hour(final String kid){
        if(kid == null || kid.length() <= 0){
            return ToolClient.createJsonFail("请选择要查看的位置点");
        }
        return ToolClient.queryJson(monitorvalueDao.getAgo10Hour(kid));
    }

    public String getStatistics(final PageFormData formData){
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(formData,p_kid);
        final String kid = formData.getString(p_kid);
        if(validate != null)return validate;
        if(kid == null || kid.length() <= 0){
            return ToolClient.createJsonFail("请选择要查看的位置点");
        }
        final String date_start = formData.getString("date_start");
        final String date_end = formData.getString("date_end");
        if(date_start != null){
            final boolean b = ToolString.checkDateTime(date_start);
            if(!b){
                return ToolClient.createJsonFail("开始日期格式有误");
            }
        }
        if(date_end != null){
            final boolean b = ToolString.checkDateTime(date_end);
            if(!b){
                return ToolClient.createJsonFail("截至日期格式有误");
            }
        }
        final List<HashMap<String,Object>> list = monitorvalueDao.get0toNow(formData);//获取展示凌晨到现在的24小时实时监测数据
        if(list == null || list.size() <= 0){
            return ToolClient.queryEmpty();
        }
        return ToolClient.queryJson(list);
    }

    //统计噪凌晨到现在(24小时内)音超过60
    public String getTotal60(final PageFormData formData){
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(formData,p_kid);
        final String kid = formData.getString(p_kid);
        if(validate != null)return validate;
        if(kid == null || kid.length() <= 0){
            return ToolClient.createJsonFail("请选择要查看的位置点");
        }
        final List<HashMap<String,Object>> list = monitorvalueDao.getTotal60(kid);//统计图统计噪音超过60
        if(list == null || list.size() <= 0){
            return ToolClient.queryEmpty();
        }
        return ToolClient.queryJson(list);
    }

    //最近7天
    public String getLately7Day(final PageFormData formData){
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(formData,p_kid);
        final String kid = formData.getString(p_kid);
        if(validate != null)return validate;
        if(kid == null || kid.length() <= 0){
            return ToolClient.createJsonFail("请选择要查看的位置点");
        }
        final List<HashMap<String,Object>> list = monitorvalueDao.getLately7Day(formData);//最近7天
        if(list == null || list.size() <= 0){
            return ToolClient.queryEmpty();
        }
        return ToolClient.queryJson(list);
    }

    //最近1年
    public String getLately365Day(final PageFormData formData){
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(formData,p_kid);
        final String kid = formData.getString(p_kid);
        if(validate != null)return validate;
        if(kid == null || kid.length() <= 0){
            return ToolClient.createJsonFail("请选择要查看的位置点");
        }
        final String date_start = formData.getString("date_start");
        final String date_end = formData.getString("date_end");
        if(date_start != null){
            final boolean b = ToolString.checkDate(date_start);
            if(!b){
                return ToolClient.createJsonFail("开始日期格式有误");
            }
        }
        if(date_end != null){
            final boolean b = ToolString.checkDate(date_end);
            if(!b){
                return ToolClient.createJsonFail("截至日期格式有误");
            }
        }
        final List<HashMap<String,Object>> list = monitorvalueDao.getLately365Day(formData);//获取展示凌晨到现在的24小时实时监测数据
        if(list == null || list.size() <= 0){
            return ToolClient.queryEmpty();
        }
        return ToolClient.queryJson(list);
    }
}