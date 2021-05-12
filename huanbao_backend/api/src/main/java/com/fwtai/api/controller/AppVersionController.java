package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * app版本更新
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-21 10:11
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
//@Api(tags = "app版本更新")
@RequestMapping(ConfigFile.api_v10 + "appVersion")
public class AppVersionController{

    //@ApiOperation(value = "获取最新的版本号", notes = "app端通过比较本地的版本号和返回的版本,若是返回的版本号大于本地的版本号,说明有新版本,下载地址的是url,描述信息是description")
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @GetMapping("/getVersion")
    public void getVersion(final HttpServletResponse response){}
}