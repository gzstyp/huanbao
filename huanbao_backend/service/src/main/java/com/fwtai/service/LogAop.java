package com.fwtai.service;

import com.fwtai.config.LocalUserId;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/*

 https://blog.csdn.net/qmqm011/article/details/90172698
 https://blog.csdn.net/qq_36761831/article/details/90299680

•AspectJ 支持 5 种类型的通知注解:

–@Before: 前置通知, 在方法执行之前执行

–@After: 后置通知, 在方法执行之后执行

–@AfterRunning: 返回通知, 在方法返回结果之后执行

–@AfterThrowing: 异常通知, 在方法抛出异常之后

–@Around: 环绕通知, 围绕着方法执行

*/

/**
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-10-26 17:52
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Aspect
@Component
@Order(8)
public class LogAop{

    /*public HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }*/

    @Before("execution(* com.fwtai..*.*Service.*(..))")
    public void before(){
        final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        System.out.println(attributes);
        /*final HttpServletRequest request = getRequest();
        final String requestURI = request.getRequestURI();*/
        final String loginId = LocalUserId.get();
        System.err.println(loginId+",Before操作日志");
    }
}