package com.fwtai.tool;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

public final class ToolBean{

    public static <T> T getBean(final HttpServletRequest request,final Class<T> requiredType){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()).getBean(requiredType);
    }
}