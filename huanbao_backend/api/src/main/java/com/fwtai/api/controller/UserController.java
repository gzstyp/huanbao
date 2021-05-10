package com.fwtai.api.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.service.core.UserService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * app端用户中心,list或查询详细信息时，不需要token的,而添加、编辑、删除时需要登录
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-24 12:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Api(tags = "账号|用户中心")
@RestController
@RequestMapping(ConfigFile.api_v10 + "user")
public class UserController{

    @Resource
    private UserService userService;

    /*更新token,小程序|移动端可能用不到这个功能*/
    @PostMapping("/refreshToken")
    public void refreshToken(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        final String access_token = formData.getString("accessToken");
        try {
            final String userId = ToolJWT.extractUserId(access_token);
            final HashMap<String,String> result = userService.refreshToken(userId);
            ToolClient.responseJson(ToolClient.queryJson(result),response);
        } catch (final JwtException exception){
            ToolClient.responseJson(ToolClient.tokenInvalid(),response);
        }
    }

    //放行对外提供注册接口url
    @ApiOperation(value = "用户注册", notes = "对外提供注册接口")
    @PostMapping(value = "/register")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "登录账号(用户名|手机号)", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "password", value = "账号密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "affirm", value = "确认密码", dataType = "String", paramType = "query", required = true)
    })
    public void register(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("不携带token使用post访问register成功"),response);
    }
}