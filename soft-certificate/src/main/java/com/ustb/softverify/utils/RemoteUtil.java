package com.ustb.softverify.utils;

import ch.ethz.ssh2.*;
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
    //#数据服务器的ip地址
    private static String dataServerIp = "123.56.246.148";
    // #数据服务器的用户名
    private static String dataServerUsername = "root";
    //#数据服务器的密码
    private static String dataServerPassword = "chainNode202";
    //#数据服务器的目的文件夹
    private static String dataServerDestDir = "/root/TmpSoftware/" +new SimpleDateFormat("yyyy").format(new Date());
    private static String localDir = "D:\\file\\";

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
            System.out.println(dataServerDestDir);
            System.out.println(pid);
            //session.execCommand("cd "+dataServerDestDir+" ; mkdir 10092 ; cd 10092 ; mkdir input ;mkdir output ");//分号隔开执行多条指令
            session.execCommand("cd "+dataServerDestDir+" ; mkdir -p " +pid+ "; cd " +pid+"; mkdir -p original ;mkdir -p archive ");//分号隔开执行多条指令

//            }
            //获得退出状态
            System.out.println("session，ExitCode: " + session.getExitStatus());//返回0 表示命令执行成功；返回1 表示执行失败
            session.close();
//            //远程文件复制到本地成功
//            SCPClient client = new SCPClient(conn);
//            client.put(localDir+"10092.zip", dataServerDestDir+"10092/input");//成功，把本地的10092.zip上传到/10092/input/目录中
////            client.get(dataServerDestDir + "10092/input/10092.zip", localDir); //成功，远程scp到本地

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
