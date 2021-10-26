package com.ustb.softverify.utils;//java在远程linux服务器中切换目录和新建目录，把本地文件上传到刚才新建的目录中
//ganymed-ssh2-build210.jar需要下载


import ch.ethz.ssh2.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    //#开发环境
    //#数据服务器的ip地址
    private static String dataServerIp = "123.56.246.148";
    // #数据服务器的用户名
    private static String dataServerUsername = "root";
    //#数据服务器的密码
    private static String dataServerPassword = "chainNode202";
    //#数据服务器的目的文件夹
    private static String dataServerDestDir = "/root/test/";
    private static String localDir = "D:\\file\\";

    public static void main(String[] args) {
        //文件scp到数据服务器
        Connection conn = new Connection(dataServerIp);
        SFTPv3Client sftpClient = null;
        Session session = null;
        System.out.println("开始scp文件");
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(dataServerUsername, dataServerPassword);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
            //执行远程命令，成功
            session = conn.openSession();
            session.execCommand("cd "+dataServerDestDir+" ; mkdir 10092 ; cd 10092 ; mkdir input ;mkdir output ");//分号隔开执行多条指令
            //显示执行命令后的信息
            InputStream stdout = new StreamGobbler(session.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    System.out.println("session，远程服务器返回信息:空");
                    break;
                }
                System.out.println("session，远程服务器返回信息:" + line);
            }
            //获得退出状态
            System.out.println("session，ExitCode: " + session.getExitStatus());//返回0 表示命令执行成功；返回1 表示执行失败
            session.close();

            //远程文件复制到本地成功
            SCPClient client = new SCPClient(conn);
            client.put(localDir+"10092.zip", dataServerDestDir+"10092/input");//成功，把本地的10092.zip上传到/10092/input/目录中
//            client.get(dataServerDestDir + "10092/input/10092.zip", localDir); //成功，远程scp到本地

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件scp到数据服务器时发生异常");
            if (conn != null) {
                conn.close();
            }
            if (sftpClient != null) {
                sftpClient.close();
            }
            if (session != null) {
                session.close();
            }

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (sftpClient != null) {
                sftpClient.close();
            }
            if (session != null) {
                session.close();
            }
        }
        System.out.println("scp文件结束");
    }//main
}