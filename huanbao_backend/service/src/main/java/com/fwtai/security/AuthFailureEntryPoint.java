package com.fwtai.security;

import com.fwtai.config.FlagToken;
import com.fwtai.tool.ToolClient;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 未登录认证处理或鉴权失败
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/11/23 23:42
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class AuthFailureEntryPoint implements AuthenticationEntryPoint {

    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException authException) throws IOException, ServletException {
        final Integer flag = FlagToken.get();//值若为空说明是未带token且未放行的url而请求导致的
        System.out.println("flag-->"+flag);
        String json = ToolClient.accessDenied();
        final Locale locale = request.getLocale();
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        if(language.equalsIgnoreCase("zh") || country.equalsIgnoreCase("CN")){
        }else{
        }
        if(flag != null){
            switch (flag){
                case 1:
                    break;
                case 2:
                    json = ToolClient.tokenInvalid();
                    break;
                default:
                    break;
            }
        }
        System.out.println("++++++++++++++未登录认证处理或token无效++++++++++++++");
        ToolClient.responseJson(json,response);
        // todo 处理 ThreadLocal
    }
}