package com.real.o2o.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: mabin
 * @create: 2019/5/12 11:17
 */
@Aspect
@Component
public class RequestLogAspect {

    private static Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

    @Pointcut("execution (public * com.real.o2o.controller.local..LocalAuthController.loginCheck(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        //接受到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录下请求的内容
        logger.info("URL: "+ request.getRequestURL().toString());
        logger.info("IP: "+ request.getRemoteAddr());
    }
}
