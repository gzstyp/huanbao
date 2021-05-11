package com.fwtai.controller.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.web.LocationService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检测点信息控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-10 19:00:46
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/location")
public class LocationController{

    @Resource
	private LocationService locationService;

    /**添加*/
    @PreAuthorize("hasAuthority('location_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('location_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('location_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.queryById(new PageFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('location_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.delById(new PageFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('location_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.delByKeys(new PageFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('location_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(locationService.listData(new PageFormData(request)),response);
    }

    /**查询位置地点名称*/
    @PreAuthorize("hasAuthority('location_btn_getListSite')")
    @GetMapping("/getSiteList")
    public void getSiteList(final String value,final HttpServletResponse response){
        ToolClient.responseJson(locationService.getSiteList(value),response);
    }

    @PreAuthorize("hasAuthority('location_btn_getAreaTree')")
    @GetMapping("/getAreaTree")
    public void getAreaTree(final Long kid,final HttpServletResponse response){
        ToolClient.responseJson(locationService.getAreaTree(kid),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }
}