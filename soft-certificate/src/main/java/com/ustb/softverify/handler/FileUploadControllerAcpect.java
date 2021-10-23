package com.ustb.softverify.handler;

import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.mapper.UploadInfoDAO;
import com.ustb.softverify.service.Impl.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.aspectj.lang.JoinPoint;
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
@Component
public class FileUploadControllerAcpect {

    private Logger logger = LoggerFactory.getLogger(FileUploadControllerAcpect.class);

    @Autowired
    private ChainService chainService;

    @Autowired
    private UploadInfoDAO uploadInfoDAO;

    @Value("${chainobj.address}")
    private String chainAddresses;

    /**
     * 定义切入点，切入点为com.ustb.softverify.controller下的所有函数
     */
    @Pointcut("execution(public * com.ustb.softverify.controller.UploadController.*(..))")
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
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL:{}; HTTP_METHOD:{}; IP:{}; CLASS_METHOD:{}; ARGS:{}" , request.getRequestURL().toString(),
                request.getMethod(),request.getRemoteAddr(),joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));

        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            if (argItem instanceof UserUploadInfoVo) {
                UserUploadInfoVo userUploadInfoVo = (UserUploadInfoVo) argItem;
                //将目标用户的发送记录上链存证
                String txid = upChain(userUploadInfoVo);
                uploadInfoDAO.insertTxid(userUploadInfoVo.getGovUserId(),txid);
                logger.info("govUserId:{}; txid:{}",userUploadInfoVo.getGovUserId(),txid);
            }
        }
    }

    /**
     * 用户信息上链处理
     * @param userUploadInfoVo 用户信息对象
     * @return 交易id
     */
    private String upChain(UserUploadInfoVo userUploadInfoVo) {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("userInfo",userUploadInfoVo);
        String txid = null;
        try {
            txid = chainService.send2Obj(chainAddresses, 0, attributes);
            return txid;
        } catch (ShellChainException | SQLException | ClassNotFoundException e) {
            throw new CertificateUpChainException();
        }
    }
}