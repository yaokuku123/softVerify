package com.ustb.softverify.handler;

import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.mapper.UploadInfoDAO;
import com.ustb.softverify.service.Impl.ChainService;
import com.ustb.softverify.utils.EnvUtils;
import com.ustb.softverify.utils.FileUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

@Aspect
//@Component
public class TimeAcpect {

    private Logger logger = LoggerFactory.getLogger(TimeAcpect.class);

    private long startTime;
    /**
     * 定义切入点，切入点为com.ustb.softverify.controller下的所有函数
     */
    @Pointcut("execution(public * com.ustb.softverify.controller.*.*(..))")
    public void executeService() {
    }

    /**
     * 前置通知：在连接点之前执行的通知
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("executeService()")
    public void doBefore(JoinPoint joinPoint) {
        this.startTime = System.currentTimeMillis();
    }

    @After("executeService()")
    public void doAfter(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        long endTime = System.currentTimeMillis();
        long executeTime = endTime - this.startTime;
        //写入csv文件
        String csvPath = EnvUtils.CSVTmp + "data.csv";
        System.out.println(executeTime);
        // url,http_method,class_method,args,time
        String data = request.getRequestURL().toString() + "," + request.getMethod() + ","
                + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "," +
                (Arrays.toString(joinPoint.getArgs())).replace(',','#') +","+ executeTime;
        FileUtil.writeToCsv(csvPath, data);
    }

}