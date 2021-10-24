package com.ustb.softverify.utils;

import ch.ethz.ssh2.*;
import com.ustb.softverify.domain.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WYP
 * @date 2021-10-24 9:07
 */
public class RemoteUtil {
    public static void main(String[] args) {

        makeDir("1000");
    }

    //#开发环境
    private static String dataServerIp = "123.56.246.148";
    private static String dataServerUsername = "root";
    private static String dataServerPassword = "chainNode202";

    // 部署环境
//    private static String dataServerIp = "192.168.179.89";
//    private static String dataServerUsername = "root";
//    private static String dataServerPassword = "kingsoft";

    private static String dataServerDestDir = "/root/TmpSoftware/" +new SimpleDateFormat("yyyy").format(new Date());

    public static void makeDir(String pid){
        //文件scp到数据服务器
        Connection conn = new Connection(dataServerIp);
        SFTPv3Client sftpClient = null;
        Session session = null;
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(dataServerUsername, dataServerPassword);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
            //执行远程命令，成功
            session = conn.openSession();
            //session.execCommand("cd "+dataServerDestDir+" ; mkdir 10092 ; cd 10092 ; mkdir input ;mkdir output ");//分号隔开执行多条指令
            session.execCommand("cd "+dataServerDestDir+" ; mkdir -p " +pid+ "; cd " +pid+"; mkdir -p original ;mkdir -p archive ");//分号隔开执行多条指令

//            }
            //获得退出状态
            System.out.println("session，ExitCode: " + session.getExitStatus());//返回0 表示命令执行成功；返回1 表示执行失败
            session.close();
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

    public static void generatePdf(String pid){
        //文件scp到数据服务器
        Connection conn = new Connection(dataServerIp);
        SFTPv3Client sftpClient = null;
        Session session = null;
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(dataServerUsername, dataServerPassword);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
            //执行远程命令，成功
            session = conn.openSession();
            //session.execCommand("cd "+dataServerDestDir+" ; mkdir 10092 ; cd 10092 ; mkdir input ;mkdir output ");//分号隔开执行多条指令
            session.execCommand("cd "+dataServerDestDir+" ; mkdir -p " +pid+ "; cd " +pid+"; mkdir -p original ;mkdir -p archive ");//分号隔开执行多条指令

//            }
            //获得退出状态
            System.out.println("session，ExitCode: " + session.getExitStatus());//返回0 表示命令执行成功；返回1 表示执行失败
            session.close();
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
