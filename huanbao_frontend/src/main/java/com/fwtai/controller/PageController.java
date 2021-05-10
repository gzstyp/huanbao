package com.fwtai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 页面跳转,方法上或类上不能还有final关键字
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-28 1:46
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Controller
public final class PageController{

    @Resource
    private HttpServletRequest request;

    @GetMapping(value = "main")
    public final ModelAndView main(){
        final ModelAndView modeView = new ModelAndView();
        modeView.addObject("login_user","admin");
        modeView.addObject("menuData",null);
        modeView.setViewName("main");
        return modeView;
    }

    @GetMapping(value = "sys_menu")
    public final String sysMenu(){
        return "sys_menu";
    }
}