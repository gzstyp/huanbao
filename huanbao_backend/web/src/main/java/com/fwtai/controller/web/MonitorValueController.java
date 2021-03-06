package com.fwtai.controller.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.web.MonitorValueService;
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
 * 监测点数据控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-12 11:55:05
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/monitorValue")
public class MonitorValueController{

    @Resource
	private MonitorValueService monitorvalueService;

    /**添加*/
    @PreAuthorize("hasAuthority('monitorValue_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('monitorValue_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('monitorValue_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.queryById(new PageFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('monitorValue_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.delById(new PageFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('monitorValue_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.delByKeys(new PageFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('monitorValue_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.listData(new PageFormData(request)),response);
    }

    @GetMapping("/getListMap")
    public void getListMap(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getListMap(new PageFormData(request)),response);
    }

    //获取设备的联机状态
    @GetMapping("/getDeviceStatus")
    public void getDeviceStatus(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getDeviceStatus(new PageFormData(request)),response);
    }

    /**获取监测点过去10小时的噪音数据*/
    @GetMapping("/getAgo10Hour")
    public void getAgo10Hour(final String kid,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getAgo10Hour(kid),response);
    }

    /**查看24小时实时监测数据,仅能查询2天的数据*/
    @GetMapping("/getStatistics")
    public void getStatistics(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getStatistics(new PageFormData(request)),response);
    }

    /** 最近7天平均监测数据统计 */
    @GetMapping("/getLately7Day")
    public void getLately7Day(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getLately7Day(new PageFormData(request)),response);
    }

    /** 统计噪凌晨到现在(24小时内)音超过60 */
    @GetMapping("/getTotal60")
    public void getTotal60(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getTotal60(new PageFormData(request)),response);
    }

    /** 最近1年平均监测数据统计 */
    @GetMapping("/getLately365Day")
    public void getLately365Day(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getLately365Day(new PageFormData(request)),response);
    }

    /** 获取监测点所有的设备 */
    @GetMapping("/getSiteDevice")
    public void getSiteDevice(final String kid,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getSiteDevice(kid),response);
    }

    /** 首页设备信息实时刷新,每分钟刷新一次 */
    @GetMapping("/getDeviceValue")
    public void getDeviceValue(final String kid,final HttpServletResponse response){
        ToolClient.responseJson(monitorvalueService.getDeviceValue(kid),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.accessDenied(),response);
    }
}