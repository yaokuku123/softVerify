package com.ustb.softverify.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author WYP
 * @date 2021-10-09 20:45
 */
@Component
public class FileTransferUtil {

    @Value("${hostname}")
    private String hostname;

    @Value("${name}")
    private String username;

    @Value("${password}")
    private String password;

    public void transfer(String localFile,String remoteTargetDirectory){
        int port = 22;
        Connection conn = new Connection(hostname,port);
        Session ssh = null;
        try {
            conn.connect();
            boolean isconn = conn.authenticateWithPassword(username, password);
            if (!isconn)
            {
                System.out.println("用户名称或者是密码不正确");
            }
            else {
                SCPClient clt = conn.createSCPClient();
                //clt.put("D:\\a.txt", "/root/data");
                clt.put(localFile, remoteTargetDirectory);
                System.out.println("传输完成");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            //连接的Session和Connection对象都需要关闭
            if(ssh!=null)
            {
                ssh.close();
            }
            if(conn!=null)
            {
                conn.close();
            }
        }
    }
}
