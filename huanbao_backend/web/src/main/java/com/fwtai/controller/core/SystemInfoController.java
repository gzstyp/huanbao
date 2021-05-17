package com.fwtai.controller.core;

import com.fwtai.info.HardwareInfo;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统监控
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-05-17 10:11
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@RequestMapping("/systemInfo")
public class SystemInfoController{

    //获取系统信息
    @PreAuthorize("hasAuthority('systemInfo_btn_getData')")
    @GetMapping("/getData")
    public void getData(final HttpServletResponse response){
        final HardwareInfo info = new HardwareInfo();
        info.copyTo();
        ToolClient.responseJson(ToolClient.queryJson(info),response);
    }
}