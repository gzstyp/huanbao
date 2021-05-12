package com.fwtai.api.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.entity.MonitorValue;
import com.fwtai.entity.ReqPage;
import com.fwtai.service.api.ApiMonitorValueService;
import com.fwtai.tool.ToolClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 监测点数据控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2021-05-12 12:33:06
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "监测点数据")
@RequestMapping(ConfigFile.push_v10)
public class MonitorValueController{

    @Resource
	private ApiMonitorValueService apiMonitorValueService;

    /**添加*/
    @ApiOperation(value = "推送数据操作", notes = "推送数据,非json格式提交")
    @PostMapping("/add")
    public void add(final MonitorValue monitorValue,final HttpServletResponse response){
        ToolClient.responseJson(apiMonitorValueService.add(monitorValue),response);
    }

    /**根据主键kid查询对应的数据*/
    @ApiOperation(value = "获取详细信息", notes = "通过kid获取详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "被查看的kid", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiMonitorValueService.queryById(new PageFormData(request)),response);
    }

    /**获取分页数据,请勿删除ReqPage否则swagger不显示参数*/
    @ApiOperation(value = "获取分页数据", notes = "如需带条件搜索的自行添加对应的字段和值即可,支持多个字段和对应的值")
    @GetMapping("/listDataPage")
    public void listDataPage(final ReqPage reqPage,final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiMonitorValueService.listDataPage(request),response);
    }
}