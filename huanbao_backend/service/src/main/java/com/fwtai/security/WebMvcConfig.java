package com.fwtai.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/29 20:42
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Value("${app.origins}")
    private String origins;

    @Override
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
            //同源配置*表示任何请求,若需指定ip和端口可以改为如“localhost:82”，多个以“,”分隔;接口http://api.fwtai.com在部署时且做upstream代理,需加入跨域里,否则报Invalid CORS request
            .allowedOrigins(origins.split(","))//允许跨域访问资源的访问者的url,注意：若参数值为*时 accessControlAllowCredentials 参数必须为false
            .allowedMethods("GET","POST","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)//客户端是否携带cookie,true允许,若参数值true时 accessControlAllowOrigin的参数值不能为* 必须指定允许跨域访问资源的访问者的url
            .maxAge(7200L);//默认值是 1800 seconds (即30分钟)
    }
}