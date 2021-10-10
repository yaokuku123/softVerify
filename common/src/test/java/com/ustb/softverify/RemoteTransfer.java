package com.ustb.softverify;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import org.junit.Test;

import java.io.IOException;

public class RemoteTransfer{

    @Test
    public void test(){

        String hostname = "123.56.246.148";
        int port = 22;//22 usually the default port
        String username = "root";
        String password = "chainNode202";
        //指明连接主机的IP地址
        Connection conn = new Connection(hostname,port);
        Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if (!isconn)
            {
                System.out.println("用户名称或者是密码不正确");
            }
            else {

                //将本地a.txt 传输到远程主机的/root/目录下
                SCPClient clt = conn.createSCPClient();
                clt.put("D:\\a.txt", "/root/");
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