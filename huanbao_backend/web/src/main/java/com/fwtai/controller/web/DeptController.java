package com.fwtai.controller.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.web.DeptService;
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
 * 组织部门控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-17 15:44:15
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/department")
public class DeptController{

    @Resource
	private DeptService deptService;

    /**添加*/
    @PreAuthorize("hasAuthority('department_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('department_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('department_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.queryById(new PageFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('department_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.delById(new PageFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('department_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.delByKeys(new PageFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('department_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(deptService.listData(new PageFormData(request)),response);
    }

    /**通过父级id获取数据构建树形*/
    @PreAuthorize("hasAuthority('department_btn_getListTree')")
    @GetMapping("/getListTree")
    public void getListTree(final String kid,final HttpServletResponse response){
        ToolClient.responseJson(deptService.getListTree(kid),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }
}