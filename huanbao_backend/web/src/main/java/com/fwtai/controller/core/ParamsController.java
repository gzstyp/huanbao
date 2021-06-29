package com.fwtai.controller.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.core.ParamsService;
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
 * 参数管理控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-18 11:34:27
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/params")
public class ParamsController{

    @Resource
	private ParamsService paramsService;

    /**添加*/
    @PreAuthorize("hasAuthority('params_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('params_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('params_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.queryById(new PageFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('params_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.delById(new PageFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('params_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.delByKeys(new PageFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('params_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(paramsService.listData(new PageFormData(request)),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.accessDenied(),response);
    }
}