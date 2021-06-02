package com.fwtai.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fwtai.bean.PageFormData;
import com.fwtai.bean.UploadFile;
import com.fwtai.bean.UploadObject;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.FlagToken;
import com.fwtai.config.LocalUrl;
import com.fwtai.config.LocalUserId;
import com.fwtai.config.Permissions;
import com.fwtai.config.RenewalToken;
import com.fwtai.exception.HandleException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端请求|服务器端响应工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月11日 19:20:50
 * @QQ号码 444141300
 * @主页 http://www.fwtai.com
*/
public final class ToolClient implements Serializable{

	private final static long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(ToolClient.class);

	/**
	 * 生成简单类型json字符串,仅用于查询返回,客户端只需判断code是否为200才操作,仅用于查询操作,除了list集合之外都可以用data.map获取数据,list的是data.listData,字符串或数字对应是obj
	 * @作者 田应平
	 * @注意 如果传递的是List则在客户端解析listData的key值,即data.listData;是map或HashMap或PageFormData解析map的key值,即data.map;否则解析obj的key值即data.obj或data.map
	 * @用法 解析后判断data.code == AppKey.code.code200 即可
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装,不可用于redis缓存value
	 * @创建时间 2017年1月11日 上午10:27:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static String queryJson(final Object object){
        if(object == null || object.toString().trim().length() <= 0){
            return queryEmpty();
        }
        if (object instanceof Exception) {
            return exceptionJson();
        }
        if(object instanceof Map<?,?>){
            final Map<?,?> map = (Map<?,?>) object;
            if(map.size() <= 0){
                queryEmpty();
            }else {
                return jsonData(ConfigFile.msg200,object);
            }
        }
        final JSONObject json = new JSONObject(3);
        if(object instanceof List<?>){
            final List<?> list = (List<?>) object;
            if(list.size() <= 0){
                return queryEmpty();
            }else {
                if (list.get(0) == null){
                    return queryEmpty();
                }else {
                    json.put(ConfigFile.code,ConfigFile.code200);
                    json.put(ConfigFile.msg,ConfigFile.msg200);
                    json.put(ConfigFile.data,object);
                    json.put(ConfigFile.record,((List<?>) object).size());
                    final String jsonObj = json.toJSONString();
                    final JSONObject j = JSONObject.parseObject(jsonObj);
                    final String listData = j.getString(ConfigFile.data);
                    if (listData.equals("[{}]")){
                        return queryEmpty();
                    }
                    return jsonObj;
                }
            }
        }
        if(String.valueOf(object).toLowerCase().equals("null") || String.valueOf(object).replaceAll("\\s*", "").length() == 0){
            return queryEmpty();
        }else {
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
            json.put(ConfigFile.data,object);
            final String jsonObj = json.toJSONString();
            final JSONObject j = JSONObject.parseObject(jsonObj);
            final String obj = j.getString(ConfigFile.data);
            if (obj.equals("{}")){
                return queryEmpty();
            }
            return jsonObj;
        }
	}

	/**
	 * 查询时得到的数据为空返回的json字符串
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月11日 下午9:40:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static String queryEmpty(){
        return createJson(ConfigFile.code201,ConfigFile.msg201);
	}

	/**
	 * 生成json字符串对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作时,判断当前的rows是否大于0来确定是否操作成功,一般在service调用,偷懒的人可以使用本方法
	 * @param rows 执行后受影响的行数
	 * @用法 解析后判断data.code == AppKey.code.code200即可操作
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:44:23
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String executeRows(final int rows){
		if(rows > 0){
            return jsonData(ConfigFile.msg200,rows);
		}else{
            return createJsonFail(ConfigFile.msg199);
		}
	}

    /**
     * 操作成功生成json字符串对象,失败信息是ConfigFile.msg199,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/1/19 11:31
    */
    public static String executeRows(final int rows,final String success){
        if(rows > 0){
            return jsonData(success,rows);
        }else{
            return createJsonFail(ConfigFile.msg199);
        }
    }

    /**
     * code=200的成功json数据格式
     * @param msg code=200时提示的信息
     * @param data key的data的数据对象
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/31 14:28
    */
    protected static String jsonData(final String msg,final Object data){
        final JSONObject json = new JSONObject(3);
        json.put(ConfigFile.code,ConfigFile.code200);
        json.put(ConfigFile.msg,msg);
        json.put(ConfigFile.data,data);
        return json.toJSONString();
    }

	/**
	 * 生成自定义的json对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
	 * @param rows 执行后受影响的行数
	 * @param success 执行成功的提示消息
	 * @param failure 执行失败的提示消息
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:50:22
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String executeRows(final int rows,final String success,final String failure){
		if(rows > 0){
            return jsonData(success,rows);
		}else{
            return createJsonFail(failure);
		}
	}

	/**
	 * 生成json格式字符串,code和msg的key是固定的,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
	 * @创建时间 2016年12月25日 18:11:16
	 * @QQ号码 444141300
	 * @param code 相关参数协议
	 * @主页 http://www.fwtai.com
	*/
	public static String createJson(final int code,final String msg){
		final JSONObject json = new JSONObject(2);
		json.put(ConfigFile.code,code);
		json.put(ConfigFile.msg,msg);
		return json.toJSONString();
	}

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
     * @创建时间 2018年7月3日 09:20:31
     * @QQ号码 444141300
     * @param map 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static String createJson(final Map<String,Object> map){
        return new JSONObject(map).toJSONString();
    }

    /**
     * 生成code为199的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public static String createJsonFail(final String msg){
        return createJson(ConfigFile.code199,msg);
    }

    /**
     * 生成code为200的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public static String createJsonSuccess(final String msg){
        return createJson(ConfigFile.code200,msg);
    }

	/**
	 * 验证密钥key的返回json格式专用,先调用方法validateKey(pageFormData)返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static String jsonValidateKey(){
        return createJson(ConfigFile.code203,ConfigFile.msg203);
	}

	/**
	 * 验证必要的参数字段是否为空的返回json格式专用,先调用方法validateField()返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static String jsonValidateField(){
        return createJson(ConfigFile.code202,ConfigFile.msg202);
	}

	/**
	 * 验证传入的值是否有值
	 * @param 
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/5/22 11:30
	*/
    public static String validateField(final String... fields){
        if(fields == null || fields.length <= 0){
            return jsonValidateField();
        }
        boolean flag = false;
        for (final String p : fields){
            if(p == null || p.length() <= 0){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    /**
     * 验证必要的字段是否为空,service层或controller层都适用,如果返回为 null 则验证成功,否则失败;适用于增、删、改、查操作!
     * @fields 需要验证的form字段
     * @用法1 final String validate = ToolClient.validateField(params,"kid");if(validate != null)return validate;
     * @用法2 final String validate = ToolClient.validateField(params,new String[]{"id"});if(validate != null)return validate;
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装,如果返回为 null 则验证成功!
     * @创建时间 2017年2月23日 下午10:10:34
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public static String validateField(final Map<String,?> params,final String... fields){
        if(params == null || params.size() <= 0) return jsonValidateField();
        for (final String value : fields){
            final Object object = params.get(value);
            if(object == null){
                logger.warn(value+"参数的值为空");
                return jsonValidateField();
            }else{
                final boolean bl = checkNull(String.valueOf(object));
                if(bl){
                    return jsonValidateField();
                }
            }
        }
        return null;
    }

    /**
     * 验证必填字段,线程安全
     * @用法1 final String validate = ToolClient.validateForm(params,"kid");if(validate != null)return validate;
     * @用法2 final String validate = ToolClient.validateForm(params,new String[]{"id"});if(validate != null)return validate;
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/2 18:31
   */
    public static String validateForm(final ConcurrentHashMap<String,String> formData,final String[] fields){
        if(ToolString.isBlank(formData) || ToolString.isBlank(fields)){
            return jsonValidateField();
        }
        boolean flag = false;
        for(final String p : fields){
            if(ToolString.isBlank(formData.get(p))){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    private static String jsonValidateInteger(){
        return createJson(ConfigFile.code199,"参数类型有误");
    }

    /**
     * 验证所输入的数据是否是Integer类型,先验证是否必填后才调用本方法,service层或controller层都适用
     * @用法1 final String fieldInteger = ToolClient.validateInteger(pageFormData,"type");if(fieldInteger != null)return fieldInteger;
     * @用法2 final String fieldInteger = ToolClient.validateInteger(pageFormData,new String[]{"category","subset","type"});if(fieldInteger != null)return fieldInteger;
     * @param
     * @作者 田应平
     * @QQ 444141300`
     * @创建时间 2020/4/2 13:04
    */
    public static String validateInteger(final Map<String,?> params,final String... fields){
        if(params == null || params.size() <= 0) throw new HandleException("参数不是有效的数字");
        for(int i = 0; i < fields.length;i++){
            try {
                final Object o = params.get(fields[i]);
                if(o != null){
                    final String value = String.valueOf(String.valueOf(o));
                    if(value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined") || value.equals("_") || value.length() <= 0)return jsonValidateInteger();
                    Integer.parseInt(value);
                }else{
                    throw new HandleException("参数不是有效的数字");
                }
            } catch (final Exception e) {
                throw new HandleException("参数不是有效的数字");
            }
        }
        return null;
    }

	/**
	 * 生成|计算总页数
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月2日 下午1:20:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static Integer totalPage(final Integer total,final Integer pageSize){
		return (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
	}

	/**
	 * 生成json对象
	 * @param map
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年7月30日 22:47:24
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String jsonObj(final Map<String, Object> map){
		return JSON.toJSONString(map);
	}

	/**
	 * 生成json数组
	 * @param listData
	 * @return
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月12日 下午9:28:55
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String jsonArray(final List<?> listData){
		return JSON.toJSONString(listData);
	}


	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装,msg 为系统统一的‘系统出现错误’
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static String exceptionJson(){
        return exceptionJson(ConfigFile.msg204);
	}

	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @param msg 自定义提示的异常信息
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static String exceptionJson(final String msg){
        return createJson(ConfigFile.code204,msg);
	}

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static void responseException(final HttpServletResponse response){
		responseJson(exceptionJson(),response);
    }

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @param msg 自定义提示的异常信息
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static void responseException(final HttpServletResponse response,final String msg){
		responseJson(exceptionJson(msg),response);
    }

	/**
	 * 未登录提示信息,json格式
	 * @作者 田应平
	 * @创建时间 2017年1月14日 上午12:46:08
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String jsonNotLogin(){
        return createJson(ConfigFile.code205,ConfigFile.msg205);
	}

    /**
     * 通用的响应json返回json对象,只能在是controller层调用
     * @param json json对象
     * @注意 不能在service层调用
     * @作者 田应平
     * @创建时间 2016年8月18日 17:53:18
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public static void responseJson(final String json){
        try {
            responseJson(json,getResponse());
        } catch (final Exception ignored) {}
    }

	/**
	 * 通用的响应json返回json对象,只能在是controller层调用
	 * @param json json对象
	 * @param response
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年8月18日 17:53:18
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static void responseJson(final String json,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
        try {
			writer = response.getWriter();
            if(json == null){
                writer.write(createJson(ConfigFile.code201,ConfigFile.msg201));
            }else{
                final String token = RenewalToken.get();
                if(token != null){
                    writer.write(JSON.parseObject(json).fluentPut("renewal","token").toJSONString());
                }else{
                    writer.write(json);
                }
            }
            writer.flush();
		}catch (final IOException e){
			logger.error("类ToolClient的方法responseJson出现异常",e.getMessage());
		}finally{
            FlagToken.remove();
            LocalUserId.remove();
            RenewalToken.remove();
            LocalUrl.remove();
            Permissions.remove();
			if(writer != null){
				writer.close();
                writer = null;
			}
		}
	}

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

	/**
	 * 响应返回客户端字符串,该obj对象字符串不是标准的json字符串!
	 * @param
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月7日 17:31:10
	*/
	public static void responseObj(final Object obj,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
            final String token = RenewalToken.get();
            if(token != null){
                writer.write(JSON.parseObject(String.valueOf(obj)).fluentPut("renewal","token").toJSONString());
            }else{
                writer.write(String.valueOf(obj));
            }
            writer.flush();
		}catch (IOException e){
			logger.error("类ToolClient的方法responseWrite出现异常",e);
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}

	/**
	 * 指定文件路径下载
	 * @param filePath 文件物理路径
	 * @param response
	 * @作者 田应平
	 * @创建时间 2015-10-17 下午6:01:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static boolean download(final String filePath,final HttpServletResponse response){
		try {
			// filePath是指欲下载的文件的全路径。
			final File file = new File(filePath);
			if(!file.exists()){
				logger.info("类ToolClient.java下的方法download():文件不存在");
				return false;
			}
			// 取得文件名。
			final String filename = file.getName();
            final int node = filename.lastIndexOf(".")+1;
            // 取得文件的后缀名。
            final String ext = filename.substring(node);
			// 以流的形式下载文件。
			final InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="+ new String((filename.substring(0,node) + ext).getBytes("utf-8"), "ISO-8859-1"));
            response.addHeader("Content-Length",String.valueOf(file.length()));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			return true;
		} catch (IOException ex){
			ex.printStackTrace();
			logger.error("类ToolClient.java下的方法download():出现异常",ex);
			return false;
		}
	}

	/**
	 * 获取项目所在的物理路径,推荐使用,但不适合jar运行的项目,仅能通过配置固定目录
	 * @param request
	 * @作者 田应平
	 * @创建时间 2017年9月25日 下午3:47:29
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String getWebRoot(final HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath(File.separator);
	}

	/**
	 * 获取访问项目的网站域名,如http://api.yinlz.com
	 * @param request
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月16日 15:18:55
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static String getDomainName(final HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName();
	}

	/**
	 * 统计处理
	 * @作者 田应平
	 * @参数 List 是数据的数据条数
	 * @参数 keyTotal是count字段或该字段别名
	 * @参数 decimalFormat是统计时的数据格式化,如0、0.0、0.00
	 * @创建时间 2016年9月12日 下午7:34:01
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static List<Map<String, Object>> statistics(final List<Map<String, Object>> list,final String keyTotal,final String decimalFormat){
		int total = 0;
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			for(final String key : map.keySet()){
				if(key.equals(keyTotal)){
					total += Integer.parseInt(map.get(key).toString());//计算总数
				}
			}
		}
		final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			final Map<String, Object> m = new HashMap<String, Object>();
			for(final String key : map.keySet()){
				if(key.equals(keyTotal)){
					float f = (float)(Integer.parseInt(map.get(key).toString()))/total * 100;//求平均数
					final DecimalFormat df = new DecimalFormat(decimalFormat);//格式化小数,如0.0或0或0.00
					m.put(key,Double.parseDouble(df.format(f)));
				}else {
					m.put(key,map.get(key));
				}
			}
			result.add(m);
		}
		return result;
	}

	/**
	 * 生成带分页的参数查询参数
	 * @param params
	 * @param pageSize 每页大小
	 * @param current 当前页
	 * @作者 田应平
	 * @返回值类型 HashMap<String,Object>
	 * @创建时间 2016年12月29日 下午10:06:03
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static HashMap<String, Object> pageParams(final HashMap<String, Object> params,final Integer pageSize,final Integer current){
		params.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		params.put(ConfigFile.pageSize,pageSize);//每页大小
		return params;
	}

    /**
     * 数据库为Mysql的datatable组装固定分页参数,section读取区间;pageSize每页大小;
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2017年1月23日 下午3:45:06
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public static PageFormData dataTableMysql(final PageFormData pageFormData) throws Exception{
        final String iDisplayLength = "iDisplayLength";
        final String iDisplayStart = "iDisplayStart";
        Integer rows = pageFormData.getInteger(iDisplayLength);
        final Integer start = pageFormData.getInteger(iDisplayStart);
        if(rows == null || start == null) return null;
        int current = (start / rows) + 1; //计算当前页
        final String sort = pageFormData.getString("sSortDir_0");//排序关键字
        final String index = pageFormData.getString("iSortCol_0");//排序的列索引
        final String column = pageFormData.getString("mDataProp_"+index);//排序的字段
        final int iColumns = pageFormData.getInteger("iColumns");
        pageFormData.remove("sColumns");
        pageFormData.remove("iColumns");
        pageFormData.remove("sSortDir_0");
        pageFormData.remove("iSortCol_0");
        pageFormData.remove(iDisplayLength);
        pageFormData.remove(iDisplayStart);
        pageFormData.remove("iSortingCols");
        for(int i = 0; i < iColumns; i++){
            pageFormData.remove("bSortable_"+i);
            pageFormData.remove("mDataProp_"+i);
        }
        if(rows < 0){
            rows = 50;
        }
        if(current <= 0){
            current = 1;
        }
        final int pageSize = rows > 100 ? ConfigFile.size_default : rows;
        pageFormData.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
        pageFormData.put(ConfigFile.pageSize,pageSize);//每页大小
        if(sort != null && column != null){
            final boolean orderKey = ToolString.checkOrderKey(sort);
            final String _column = orderKey ? ToolString.sqlInject(column) : null;
            final String _sort = orderKey ? ToolString.sqlInject(sort) : null;
            if(_column != null && _sort != null){
                pageFormData.put("column",_column);//排序字段 order by name desc
                pageFormData.put("order",_sort);//排序关键字(升序|降序)
            }
        }
        return pageFormData;
    }

    /**
     * 获取表单的请求参数,不含文件域,返回的是HashMap<String,String>
     * @param request
     * @作者:田应平
     * @创建时间 2019年11月13日 19:14:15
     * @主页 www.fwtai.com
    */
    public static HashMap<String,String> getFormParams(final HttpServletRequest request){
        final HashMap<String,String> params = new HashMap<String,String>();
        abstractMap(request,params);
        return params;
    }

    /**
     * 获取表单的请求参数,不含文件域,返回的是线程安全的ConcurrentHashMapString,String>
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/13 19:29
    */
    public static ConcurrentHashMap<String,String> getFormParam(final HttpServletRequest request){
        final ConcurrentHashMap<String,String> params = new ConcurrentHashMap<String,String>();
        abstractMap(request,params);
        return params;
    }

    protected static void abstractMap(final HttpServletRequest request,final AbstractMap<String,String> map){
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            String value = request.getParameter(key);
            if(value != null && value.length() >0){
                value = value.trim();
                if(checkNull(value))
                    continue;
                map.put(key,value);
            }
        }
    }

    /**
     * 获取表单的请求参数,不含文件域
     * @param request
     * @作者:田应平
     * @创建时间 2017年10月21日 16:03:16
     * @主页 www.fwtai.com
    */
    public static PageFormData getFormData(final HttpServletRequest request){
        return new PageFormData(request);
    }

    private static boolean checkNull(final String value){
        if(value.length() <= 0)return true;
        if(value.equals("_"))return true;
        if(value.equalsIgnoreCase("undefined"))return true;
        if(value.equalsIgnoreCase("null"))return true;
        return false;
    }

	/**
	 * 获取表单的所有请求参数
	 * @param request
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/1/8 21:25
	*/
    public static JSONObject getRequestData(final HttpServletRequest request){
        return new PageFormData().buildJSONObject(request);
    }

	/**
	 * 获取由HttpClient发送的数据的HttpServletRequest请求参数
	 * @作者 田应平
	 * @QQ 444141300
     * @param request 请求参数,默认的字符编码为"UTF-8"
	 * @创建时间 2018年7月3日 09:33:19
	*/
    public static String getHttpClientRequest(final HttpServletRequest request) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 获取由HttpClient发送的数据的HttpServletRequest请求参数
     * @作者 田应平
     * @QQ 444141300
     * @param request 请求参数
     * @param charsetName 字符编码,如 "UTF-8"
     * @创建时间 2018年7月3日 09:39:00
    */
    public static String getHttpClientRequest(final HttpServletRequest request,final String charsetName) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,charsetName);
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 返回没有操作权限的code=401,msg=没有操作权限
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/3/1 0:13
     */
    public static String notAuthorized(){
        return createJson(ConfigFile.code401,ConfigFile.msg401);
    }

    /**
     * token无效或已过期,请重新登录
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/4/19 21:00
    */
    public static String tokenInvalid(){
        return tokenInvalid(ConfigFile.TOKEN_INVALID);
    }

    public static String tokenInvalid(final String msg){
        return createJson(ConfigFile.code205,msg);
    }

    /**
     * 账号或密码错误
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:16
     */
    public static String invalidUserInfo(){
        return createJson(ConfigFile.code206,ConfigFile.msg206);
    }

    /**
     * 用于生成Easyui里的datagrid数据表格
     * @param total 总条数|总记录数
     * @param listData 数据库返回的list集合数据
     * @作者 田应平
     * @返回值类型 String
     * @创建时间 2020年3月22日 22:51:46
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
     */
    public static String dataTableOK(List<Object> listData,Object total,final Object sEcho){
        final JSONObject json = new JSONObject(7);
        if(listData == null || listData.size() == 0){
            listData = new ArrayList<Object>();
            total = 0;
            json.put(ConfigFile.code,ConfigFile.code201);
            json.put(ConfigFile.msg,ConfigFile.msg201);
        }else{
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
        }
        final String url = LocalUrl.get();
        if(url != null){
            final List<String> permissions = Permissions.get();
            if(permissions != null && permissions.size() > 0){
                json.put(ConfigFile.permissions,permissions);
            }
        }
        json.put("sEcho",sEcho);
        json.put(ConfigFile.recordsTotal,total);
        json.put(ConfigFile.recordsFiltered,total);
        json.put(ConfigFile.data,listData);
        return json.toJSONString();
    }

    public static String dataTableException(final Object sEcho){
        final JSONObject json = new JSONObject(6);
        json.put(ConfigFile.code,ConfigFile.code204);
        json.put(ConfigFile.msg,ConfigFile.msg204);
        json.put("sEcho",sEcho);
        json.put(ConfigFile.recordsTotal,0);
        json.put(ConfigFile.recordsFiltered,0);
        json.put(ConfigFile.data,new ArrayList<Object>());
        return json.toJSONString();
    }

    /**获取带分页的数据,适用于移动端或小程序,因为限制了每页大小最大为20,返回null时说明参数有误!!然后再判断某个key是否必填*/
    public static PageFormData pageParamsApi(final HttpServletRequest request){
        try {
            final PageFormData params = getFormData(request);
            final String rows = params.getString("pageSize");//每页大小
            final String current = params.getString("current");//当前页
            if(current == null || rows == null) return null;
            final String order = params.getString("order");//排序关键字
            final String column = params.getString("column");//排序字段
            int currentPage = Integer.parseInt(current);
            int pageSize = Integer.parseInt(rows);
            if(pageSize > ConfigFile.size_api_default) pageSize = 10;
            if(currentPage < 1)currentPage = 1;
            params.remove("pageSize");
            params.remove("current");
            params.put(ConfigFile.section,(currentPage - 1) * pageSize);//读取区间
            params.put(ConfigFile.pageSize,pageSize);//每页大小
            params.remove("order");
            params.remove("column");
            if(order != null && column != null){
                final boolean b = ToolString.checkOrderKey(order);
                if(!b) return null;
                final String _order = ToolString.sqlInject(order);
                final String _column = ToolString.sqlInject(column);
                if(_order != null && _column != null){
                    params.put("order",_order);//排序关键字
                    params.put("column",_column);//排序的字段 order by name desc
                }
            }
            return params;
        } catch (final Exception e){
            return null;
        }
    }

    /**返回分页数据*/
    public static String jsonPage(final List<?> listData,final Integer total){
        final JSONObject json = new JSONObject(4);
        if(listData.isEmpty() || total == 0){
            return queryEmpty();
        }else{
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
            json.put(ConfigFile.total,total);
            json.put(ConfigFile.data,listData);
            json.put(ConfigFile.record,listData.size());
            return json.toJSONString();
        }
    }

    public static String jsonPage(final Object listData,final Integer total,final List<String> permissions){
        final JSONObject json = new JSONObject(5);
        if(listData == null || total == 0){
            return queryEmpty();
        }else{
            if(permissions != null && permissions.size() > 0){
                json.put(ConfigFile.permissions,permissions);
            }
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
            json.put(ConfigFile.total,total);
            json.put(ConfigFile.data,listData);
            return json.toJSONString();
        }
    }

    /**
     * 从请求参数或者header获取token
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/23 23:26
    */
    public static String getToken(final HttpServletRequest request){
        String token = ToolString.wipeString(request.getParameter("token"));
        if(token == null || token.length() <= 0){
            token = ToolString.wipeString(request.getHeader("token"));;
        }
        return token;
    }

    /**
     * 封装文件上传,返回值实体 UploadObject,仅需判断 uploadObject.getErrorMsg();是否为空再处理业务
     * @param baseDir 该值的结尾必须要带 /
     * @param prefixType 前缀分类,如图片 /images; 或 /excel/import
     * @param limit 如果该值为null或为负数时则不限制文件数
     * @param verify 文件是否是必填项
     * @return HashMap_key,Object>,其中key可能为error,files,params，要做error判断再做页面处理,若 key 不为空时,那files则是　ArrayList_HashMap_String,String;params是PageFormData
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021年3月25日 18:18:31
    */
    public static UploadObject uploadImage(final HttpServletRequest request,final String baseDir,final String prefixType,final Integer limit,final boolean verify){
        return uploadFile(request,baseDir,prefixType,true,limit,verify);
    }

    /**
     * 封装文件上传,返回值实体 UploadObject,仅需判断 uploadObject.getErrorMsg();是否为空再处理业务
     * @param baseDir 该值的结尾必须要带 /
     * @param prefixType 前缀分类,如图片 /images; 或 /excel/import
     * @param temporary 文件是否是临时文件,如导入的文件算是临时的文件,是临时则不创建以日期的生成的目录
     * @param limit 如果该值为null或为负数时则不限制文件数
     * @param verify 文件是否是必填项
     * @return HashMap_key,Object>,其中key可能为error,files,params，要做error判断再做页面处理,若 key 不为空时,那files则是　ArrayList_HashMap_String,String;params是PageFormData
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年6月1日 21:04:37
    */
    private static UploadObject uploadFile(final HttpServletRequest request,final String baseDir,final String prefixType,final boolean temporary,final Integer limit,final boolean verify){
        final UploadObject uploadObject = new UploadObject();
        MultipartHttpServletRequest mhsr = null;
        try {
            mhsr = (MultipartHttpServletRequest) request;
        } catch (Exception e){
            if(verify){
                uploadObject.setErrorMsg("请上传文件");
                return uploadObject;
            }
        }
        final Map<String,MultipartFile> map = mhsr.getFileMap();
        if(verify){
            if(map == null || map.size() <=0){
                uploadObject.setErrorMsg("请选择上传文件");
                return uploadObject;
            }
        }
        if(limit != null && limit > 0){
            if(map.size() > limit){
                uploadObject.setErrorMsg("文件数量已超过限制");
                return uploadObject;
            }
        }
        final DiskFileItemFactory fac = new DiskFileItemFactory();
        final ServletFileUpload upload = new ServletFileUpload(fac);
        final ArrayList<UploadFile> files = new ArrayList<UploadFile>();
        try {
            upload.setHeaderEncoding("utf-8");
            mhsr.setCharacterEncoding("utf-8");
            boolean bl = false;
            for(final String key : map.keySet()){
                final MultipartFile mf = mhsr.getFile(key);
                if(mf.getSize() > 1024*1024*15L){//15mb,必须结合配置文件 spring.servlet.multipart.max.xxx一起使用
                    bl = true;
                    break;
                }
                final String originalName = mf.getOriginalFilename();
                final String extName = originalName.substring(originalName.lastIndexOf("."));
                final String fileName = ToolString.getIdsChar32() + extName;
                final String dayDir = temporary ? (prefixType + "/") : (prefixType + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/");
                final File fileDir = new File(baseDir + dayDir);
                if(!fileDir.exists()){
                    fileDir.mkdirs();
                }
                final String fullPath = (baseDir + dayDir + fileName).replaceAll("//","/");
                mf.transferTo(new File(fullPath));
                final UploadFile uploadFile = new UploadFile();
                uploadFile.setUrlFile(dayDir + fileName);// Nginx的配置windows环境的路径 root C:\\;上传的跟目录是 C:\images; Nginx的配置linux的环境路径 root /home/data/;上传的跟目录是 /home/data/images/
                uploadFile.setOriginalName(originalName);
                uploadFile.setFullPath(fullPath);
                uploadFile.setFileName(fileName);
                uploadFile.setBasePath(baseDir);
                uploadFile.setName(key);
                files.add(uploadFile);
            }
            if(bl){
                for(int i = 0; i < files.size(); i++){
                    delFileByThread(files.get(i).getFullPath());
                }
                uploadObject.setErrorMsg("操作失败,某个文件过大");
                return uploadObject;
            }
            if(files.size() > 0){
                uploadObject.setListFiles(files);
            }
            final PageFormData formData = new PageFormData(request);
            if(formData.size() > 0){
                uploadObject.setParams(formData);
            }
            return uploadObject;
        } catch (Exception e) {
            for(int i = 0; i < files.size(); i++){
                delFileByThread(files.get(i).getFullPath());
            }
            uploadObject.setErrorMsg("操作失败,处理文件失败");
            return uploadObject;
        }
    }

    /**
     * 开线程访问服务器删除图片
     * @date 2019年10月31日 16:41:40
    */
    public static void delFileByThread(final String filePath) {
        final ExecutorService threads = Executors.newCachedThreadPool();
        threads.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    final File file = new File(filePath);
                    if(file.isFile()){
                        file.delete();
                    }
                } catch (final Exception ignored){
                }finally{
                    threads.shutdown();//释放线程池
                }
            }
        });
    }

    /**
     * 把URL后跟的查询字符串转成HashMap对象
    */
    public static HashMap<String,String> parseQuery(final String urlQuery) {
        final HashMap<String,String> data = new HashMap<String,String>();
        final String[] params = urlQuery.split("&");
        for (final String param : params) {
            final String[] k = param.split("=");
            final String value = k[1];
            if(value != null && value.trim().length() > 0){
                data.put(k[0],value);
            }
        }
        return data;
    }

    /**获取访问者真实的IP地址*/
    public static String getIp(final HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() > 0){
            if(ip.contains(",")){
                String[] ips = ip.split(ip);
                if(ips.length > 0){
                    return ips[0];
                }
            }
        }
        return ip;
    }

    /**获取访问者真实的IP地址*/
    public static String getIpAddr(final HttpServletRequest request)
        throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (ipString == null || ipString.trim().length() <= 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (ipString == null || ipString.trim().length() <= 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipString == null || ipString.trim().length() <= 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }
        return ipString;
    }

    /**
     * 已实现 implements Serializable 的实体转PageFormData
     * @param cls
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/12/16 11:32
    */
    public static PageFormData beanToPageFormData(final Serializable cls){
        return JSONObject.parseObject(JSONObject.toJSONString(cls),PageFormData.class);
    }
}