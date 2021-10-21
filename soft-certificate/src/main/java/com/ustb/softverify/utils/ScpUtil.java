package com.ustb.softverify.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.io.File;
import java.io.IOException;

/**
 * @Author wuyongjia_1752898160@qq.com
 * Date on 2021/10/11  9:34 上午
 * 远程上传下载服务器文件
 */
public class ScpUtil {

    private static String IP = "123.56.246.148";
    private static int PORT = 22;
    private static String USER = "root";//登录用户名
    private static String PASSWORD = "chainNode202";//生成私钥的密码和登录密码，这两个共用这个密码
    private static String PRIVATEKEY = "C:\\Users\\mph\\.ssh\\id_rsa";// 本机的私钥文件

    /**
     * 使用用户名和密码来进行登录验证。如果为true则通过用户名和密码登录，false则使用rsa免密码登录
     */
    private static boolean usePassword = true;
    /**
     * ssh连接对象
     */
    private static Connection connection = new Connection(IP, PORT);

    /**
     * ssh用户登录验证，使用用户名和密码来认证
     *
     * @param user
     * @param password
     * @return
     */
    public static boolean isAuthedWithPassword(String user, String password) {
        try {
            return connection.authenticateWithPassword(user, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ssh用户登录验证，使用用户名、私钥、密码来认证 其中密码如果没有可以为null，生成私钥的时候如果没有输入密码，则密码参数为null
     *
     * @param user
     * @param privateKey
     * @param password
     * @return
     */
    public static boolean isAuthedWithPublicKey(String user, File privateKey, String password) {
        try {
            return connection.authenticateWithPublicKey(user, privateKey, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 认证
     * @return
     */
    public static boolean isAuth() {
        if (usePassword) {
            return isAuthedWithPassword(USER, PASSWORD);
        } else {
            return isAuthedWithPublicKey(USER, new File(PRIVATEKEY), PASSWORD);
        }
    }

    /**
     * 从远程服务器下载文件
     * @param remoteFile
     * @param path
     */
    public static void getFile(String remoteFile, String path) {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                System.out.println("认证成功!");
                SCPClient scpClient = connection.createSCPClient();
                scpClient.get(remoteFile, path);
            } else {
                System.out.println("认证失败!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    /**
     * 向远程服务器上传文件
     * @param localFile
     * @param remoteTargetDirectory
     */
    public static void putFile(String localFile, String remoteTargetDirectory) {
        try {
            connection.connect();
            boolean isAuthed = isAuth();
            if (isAuthed) {
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory);
            } else {
                System.out.println("认证失败!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.close();
        }
    }

    /**
     * 向远程服务器下载文件
     * @param localFile
     * @param remoteTargetDirectory
     */
    public static void downFile(String localFile, String remoteTargetDirectory) {





    }


}
