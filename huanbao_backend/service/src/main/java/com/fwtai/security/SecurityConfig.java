package com.fwtai.security;

import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring().antMatchers(ConfigFile.IGNORE_URLS);//忽略url
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()//开启认证
            .anyRequest()//任何请求
            .authenticated()//登录认证之后才能访问,注意配置顺序,顺序很重要,它和shiro是一样的
            .and()//此时又回到上面第1行的 'authorizeRequests()'
            .formLogin()//表单登录
            .loginPage(ConfigFile.URL_LOGIN_PAGE)//指定登录页面的url[若没有额外的指定的话默认的登录接口就是它,只是页面的get请求,而登录接口是post请求]
            .permitAll()
            .and()//此时又回到上面第1行的 'authorizeRequests()'
            .logout()//配置退出登录
            .logoutUrl(ConfigFile.URL_LOGOUT)//退出登录的url,默认是get方式,若要该成post方式请用下面方式处理
            .permitAll()
            //.logoutRequestMatcher(new AntPathRequestMatcher("/exit","POST"))//此方式是POST
            .logoutSuccessHandler((request,response,authentication)->{//实现接口 org.springframework.security.web.authentication.logout.LogoutSuccessHandler 内部类
                SecurityContextHolder.clearContext();
                ToolClient.responseJson(ToolClient.createJsonSuccess("退出注销成功"),response);
            })
            .permitAll()//表示与退出相关的都统统放行
            .and()
            .cors().and().csrf().disable()
            .addFilter(new LoginAuthentication(authenticationManager()))//登录认证处理
            .addFilter(new AuthorizationFilter(authenticationManager()))//鉴权处理
            // 不需要session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()//未登录认证处理或无权限的处理
            .authenticationEntryPoint(new AuthFailureEntryPoint());
    }

    /*@Bean //暂时保留
    protected CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }*/
}