package com.fwtai.security;

import com.fwtai.bean.JwtUser;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.FlagToken;
import com.fwtai.config.LocalUserId;
import com.fwtai.config.RenewalToken;
import com.fwtai.exception.HandleException;
import com.fwtai.tool.ToolBean;
import com.fwtai.tool.ToolJWT;
import com.fwtai.tool.ToolString;
import io.jsonwebtoken.ExpiredJwtException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权操作
 * 验证成功当然就是进行鉴权,鉴权操作,无token时会提示'统一处理,需要认证才能访问'
 * 登录成功之后走此类进行鉴权操作
*/
public final class AuthorizationFilter extends BasicAuthenticationFilter{

    public AuthorizationFilter(final AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain) throws IOException, ServletException{
        final String uri = request.getRequestURI();
        final String refresh_token = ToolString.wipeString(request.getHeader(ConfigFile.REFRESH_TOKEN));
        final String access_token = ToolString.wipeString(request.getHeader(ConfigFile.ACCESS_TOKEN));
        final String url_refresh_token = ToolString.wipeString(request.getParameter(ConfigFile.REFRESH_TOKEN));
        final String url_access_token = ToolString.wipeString(request.getParameter(ConfigFile.ACCESS_TOKEN));
        final String refresh = refresh_token == null ? url_refresh_token : refresh_token;
        final String access = access_token == null ? url_access_token : access_token;
        if(access != null){//删除一个条件判断,if(refresh != null && access != null) 移动端token失效时重新登录即可!!!
            //判断令牌是否过期，默认是一周,比较好的解决方案是：登录成功获得token后，将token存储到数据库（redis）
            try {
                ToolJWT.parser(refresh);
            } catch (final Exception e) {
                if(e instanceof ExpiredJwtException){
                    RenewalToken.set(access);
                }
            }
            try {
                //通过令牌获取用户名称
                final String userId = ToolJWT.extractUserId(access);
                // todo 根据userId 从 redis ，获取用户 authentication 角色权限信息
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                LocalUserId.set(userId);
                //通过用户信息得到UserDetails
                final JwtUser jwtUser = ToolBean.getBean(request,UserDetailsServiceImpl.class).getUserById(userId,uri.startsWith("/") ? uri.substring(1) : uri);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtUser.getUsername(),null,jwtUser.getAuthorities()));
                super.doFilterInternal(request,response,chain);
            } catch (final Exception exception){
                final Class<? extends Exception> aClass = exception.getClass();
                System.out.println("aClass -> " + aClass);
                if(exception instanceof MyBatisSystemException){
                    throw new HandleException("系统出现错误,稍候重试");
                }else{
                    RenewalToken.remove();
                    FlagToken.set(2);
                }
                //todo 勿删,走这里因为没有角色权限信息,所以要被 AuthenticationEntryPoint 的实现类拦截下来并执行
                chain.doFilter(request,response);//报错是否因为这个???,若是不加的话，则请求没有返回值
            }
        }else{
            chain.doFilter(request,response);
        }
    }
}