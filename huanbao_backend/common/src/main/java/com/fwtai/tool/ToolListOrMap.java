package com.fwtai.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * list处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/3/26 19:32
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolListOrMap{

    /**
     * 通过excel导入时下拉或数据字典
     * field,指定要统计的字段key
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 11:57
    */
    public static ArrayList<String> listObjGroupOption(final ArrayList<HashMap<String,Object>> list,final String field){
        return new ArrayList<>(listObjGetMap(list,field).keySet());
    }

    /**
     * 通过excel导入时下拉或数据字典
     * field,指定要统计的字段key
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 11:57
    */
    public static ArrayList<String> listStrGroupOption(final ArrayList<HashMap<String,String>> list,final String field){
        return new ArrayList<>(listStrGetMap(list,field).keySet());
    }

    /**
     * list通过元素的key进行分组
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 12:25
    */
    public static Map<String,List<Map<String,Object>>> listObjGetMap(final ArrayList<HashMap<String,Object>> list,final String field){
        return list.stream().collect(groupingBy(map -> String.valueOf(map.get(field))));
    }

    /**
     * 在替换下拉的key-value之前就做处理,默认是name
     * @param listNullMsg 类型匹配对应不上时的提示信息,如,全部场所类型,全部冷库类型
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 23:33
    */
    public static String existOption(final HashMap<String,Object> map,final String field,final List<HashMap<String,String>> list,final String listNullMsg){
        return existOption(map,field,list,"name",listNullMsg);
    }

    /**
     * 在替换下拉的key-value之前就做处理
     * @param listNullMsg 类型匹配对应不上时的提示信息,如,全部场所类型,全部冷库类型,
     * @用法 ToolListOrMap.existOption(map,"site_type",siteTypes,"全部场所类型");
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 23:33
    */
    public static String existOption(final HashMap<String,Object> map,final String field,final List<HashMap<String,String>> list,final String mapKey,final String listNullMsg){
        if(list == null || list.size() <=0) return listNullMsg;
        final String value = (String)map.get(field);
        boolean bl = false;
        for(int i = 0; i < list.size(); i++){
            final String temp = list.get(i).get(mapKey);
            if(temp.equals(value)){
                bl = true;
                break;
            }
        }
        if(bl){
            return null;
        }else{
            return value;
        }
    }

    /**
     * list通过元素的key进行分组
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 12:25
    */
    public static Map<String,List<Map<String,String>>> listStrGetMap(final ArrayList<HashMap<String,String>> list,final String field){
        return list.stream().collect(groupingBy(map -> String.valueOf(map.get(field))));
    }

    //导入时替换下拉或数据字段为替换主键kid
    public static String handleOptions(final HashMap<String,Object> map,final String field,final List<HashMap<String,String>> list){
        return handleOptions(map,field,list,"name","kid");
    }

    /**
     * 导入时list每个元素的替换下拉或数据字段为替换主键kid
     * @param field 指定map的key获取要匹配对比的value
     * @param text 指定list下拉的字段或的text
     * @param value 指定list下拉的字段或的value
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/26 20:05
    */
    public static String handleOptions(final HashMap<String,Object> map,final String field,final List<HashMap<String,String>> list,final String text,final String value){
        if(list != null && list.size() <= 0) return null;
        final String v = String.valueOf(map.get(field));
        for(int i = 0; i < list.size(); i++){
            final HashMap<String,String> listMap = list.get(i);
            if(listMap.get(text).equals(v)){
                map.put(field,listMap.get(value));
                return null;
            }
        }
        return v;
    }

    //处理list时每个元素的是或否两种结果
    public static void whether(final HashMap<String,Object> map,final String field){
        String value = (String)map.get(field);
        if(value != null && value.length()>0){
            value = ToolString.wipeStrBlank(value);
            if(value.contains("是")){
                map.put(field,1);
                return;
            }
        }
        map.put(field,0);
    }

    //处理导入时list每个元素的检测结果:(1未检测;2阴性;3阳性)
    public static void handleResult(final HashMap<String,Object> map,final String field){
        String value = (String)map.get(field);
        if(value != null && value.length()>0){
            value = ToolString.wipeStrBlank(value);
            switch (value){
                case "未做":
                    map.put(field,1);
                    break;
                case "阴性":
                    map.put(field,2);
                    break;
                case "阳性":
                    map.put(field,3);
                    break;
                default:
                    map.put(field,1);
                    break;
            }
        }else{
            map.put(field,1);
        }
    }

    //处理导入时list每个元素的非必填字段
    public static void checkDefault(final HashMap<String,Object> map,final String field,final Object defaultValue){
        String value = (String)map.get(field);
        if(value != null && value.length()>0){
            value = ToolString.wipeStrBlank(value);
            map.put(field,value);
        }else{
            map.put(field,defaultValue);
        }
    }

    //处理导入时list每个元素的必填字段是否为空
    public static String checkList(final ArrayList<HashMap<String,Object>> list,final String field,final String msg){
        for(int i = 0; i < list.size(); i++){
            final HashMap<String,Object> map = list.get(i);
            final String site_name = (String)map.get(field);
            if(site_name == null || site_name.length() <=0){
                return ToolClient.createJsonFail(msg);
            }
        }
        return null;
    }
}