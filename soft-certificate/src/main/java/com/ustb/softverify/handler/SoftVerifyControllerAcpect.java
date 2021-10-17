package com.ustb.softverify.handler;

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
import java.util.Arrays;

@Aspect
@Component
public class SoftVerifyControllerAcpect {

    private Logger logger = LoggerFactory.getLogger(SoftVerifyControllerAcpect.class);

    /**
     * 定义切入点，切入点为com.ustb.softverify.controller下的所有函数
     */
    @Pointcut("execution(public * com.ustb.softverify.controller.SoftVerifyController.*(..))")
    public void executeService(){}

    /**
     * 前置通知：在连接点之前执行的通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("executeService()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL:{}; HTTP_METHOD:{}; IP:{}; CLASS_METHOD:{}; ARGS:{}" , request.getRequestURL().toString(),
                request.getMethod(),request.getRemoteAddr(),joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }
}
