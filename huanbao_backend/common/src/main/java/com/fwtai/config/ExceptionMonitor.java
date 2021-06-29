package com.fwtai.config;

import com.fwtai.exception.HandleException;
import com.fwtai.exception.InvalidParams;
import com.fwtai.tool.ToolClient;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;
import java.time.format.DateTimeParseException;

/**
 * 拦截异常并统一处理
 * @param
 * @作者 田应平
 * @QQ 444141300
 * @创建时间 2020/4/2 20:05
*/
@RestControllerAdvice
public class ExceptionMonitor{

    private final static Logger logger = LoggerFactory.getLogger(ExceptionMonitor.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void notSupported(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("不支持该请求方式"),response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public void missingServletRequest(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("请选择文件再操作"),response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void maxUploadSize(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("上传的文件过大"),response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(final Exception exception,final HttpServletResponse response){
        System.out.println("方法accessDeniedException:"+exception.getClass());
        ToolClient.responseJson(ToolClient.accessDenied(),response);
    }

    @ExceptionHandler({SecurityException.class})
    public void signatureException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.tokenInvalid("无效的token,请重新登录"),response);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public void expiredJwtException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.tokenInvalid(),response);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public void jwtException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.tokenInvalid(),response);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void credentialsNotFoundException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.tokenInvalid(),response);
    }

    @ExceptionHandler(NullPointerException.class)
    public void nullPointer(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("请检查是否已传入数值"),response);//注意service层的方法上是否有final关键字,否则也会这个报错,或提示系统出现错误(其实是空指针)
    }

    @ExceptionHandler(ClassCastException.class)
    public void classCast(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("类型转换异常"),response);
    }

    @ExceptionHandler(NumberFormatException.class)
    public void mumberFormat(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("数字格式异常"),response);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public void dateTimeParseException(final HttpServletResponse response){
        final String json = ToolClient.createJsonFail("无效的年月");
        ToolClient.responseJson(json,response);
    }

    //自定义异常信息,用法:throw new HandleException("参数不是有效的数字");
    @ExceptionHandler(HandleException.class)
    public void handleException(final Exception exception,final HttpServletResponse response){
        ToolClient.responseJson(exception.getMessage(),response);
    }

    //自定义异常信息,用法:throw new InvalidParams("请求参数有误");
    @ExceptionHandler(InvalidParams.class)
    public void invalidParams(final Exception exception,final HttpServletResponse response){
        ToolClient.responseJson(exception.getMessage(),response);
    }

    @ExceptionHandler({SocketTimeoutException.class,MyBatisSystemException.class,DataAccessException.class})
    public void socketTimeoutException(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson(),response);
    }

    @ExceptionHandler(PersistenceException.class)
    public void persistenceException(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        String json = ToolClient.exceptionJson();
        final String message = exception.getMessage();
        if(message.contains("java.sql.SQLException: Incorrect datetime value:")){
            json = ToolClient.createJsonFail("日期时间格式不对");
            ToolClient.responseJson(json,response);
            return;
        }
        ToolClient.responseJson(json,response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void httpMessageNotReadableException(final Exception exception,final HttpServletResponse response){
        final String message = exception.getMessage();
        if(message.contains("JSON parse error: Unexpected character")){
            ToolClient.responseJson(ToolClient.createJsonFail("JSON数据格式有误"),response);
        }else{
            ToolClient.responseJson(ToolClient.exceptionJson(),response);
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void integrityViolation(final Exception exception,final HttpServletRequest request,final HttpServletResponse response){
        final String message = exception.getMessage();
        if(message.contains("Data too long for column '")){
            final int start = message.lastIndexOf("Data too long for column '") + 26;
            final int forKey = message.lastIndexOf("' at row");
            final String column = message.substring(start,forKey);
            final String value = request.getParameter(column);
            if(value != null){
                ToolClient.responseJson(ToolClient.exceptionJson("输入的[<span style='color:#f00'>"+value+ "</span>]字符过多,减少一些试试"),response);
            }else{
                ToolClient.responseJson(ToolClient.exceptionJson("输入的字符过多,减少一些试试!"),response);
            }
        }else if(message.contains("' doesn't have a default value")){
            final int start = message.indexOf("Cause: java.sql.SQLException: Field '")+37;
            final int end = message.indexOf("' doesn't have a default value");
            final String field = message.substring(start,end);
            logger.error("字段:"+field+"未传值*******************************");//上线后是否要删除
            ToolClient.responseJson(ToolClient.exceptionJson("哥们,有个字段的值为空哦!"),response);
        }else{
            exception.printStackTrace();
            ToolClient.responseJson(ToolClient.exceptionJson("哦,抱歉,系统出现错误"),response);
        }
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public void duplicateKeyException(final Exception exception,final HttpServletResponse response){
        final String message = exception.getMessage();
        if(message.contains("Duplicate entry '")){
            final int start = message.lastIndexOf("Duplicate entry '") + 17;
            final int forKey = message.lastIndexOf("' for key '");
            final String value = message.substring(start,forKey);
            ToolClient.responseJson(ToolClient.exceptionJson("添加编辑的[<span style='color:#f00'>"+value + "</span>]已存在"),response);
        }else{
            ToolClient.responseJson(ToolClient.exceptionJson("添加编辑的数据已存在"),response);
        }
    }

    @ExceptionHandler(Exception.class)
    public void exception(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        final String message = exception.getMessage();
        System.out.println("错误信息:"+message);
        System.out.println("报错的类:"+exception.getClass());
        ToolClient.responseJson(ToolClient.exceptionJson(),response);
    }
}