package com.ustb.softverify.service.Impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WYP
 * @date 2021-10-23 22:12
 */
@Service
public class ShellTools {
    /**
     * 获取linux ssh连接
     *
     * @param hostname 客户端ip
     * @param user     客户端用户名
     * @param passwd   客户端密码
     * @return linux连接
     */
    public Connection getConn(String hostname, String user, String passwd) throws IOException {
        Connection conn = null;
        boolean isAuthenticated = false;
        if (null == conn) {
            conn = new Connection(hostname);
            conn.connect();
            isAuthenticated = conn.authenticateWithPassword(user, passwd);
            if (isAuthenticated == false) {
                throw new IOException("Authentication failed.");
            }
        }

        return conn;
    }

    /**
     * 客户端将文件scp到服务器
     *
     * @param conn                 linux ssh连接
     * @param remoteFile           远程文件
     * @param localTargetDirectory 本地文件夹
     */
    public void scpRemoteFile(Connection conn, String[] remoteFile,
                              String localTargetDirectory) throws IOException {
        SCPClient client = new SCPClient(conn);
        client.get(remoteFile, localTargetDirectory);

    }

    /**
     * 客户端将文件scp到服务器
     *
     * @param conn                 linux ssh连接
     * @param remoteFile           远程文件
     * @param localTargetDirectory 本地文件夹
     */
    public void scpRemoteFile(Connection conn, String remoteFile,
                              String localTargetDirectory) throws IOException {
        SCPClient client = new SCPClient(conn);
        client.get(remoteFile, localTargetDirectory);

    }

    /**
     * 将服务器中的文件下载到本地文件夹
     *
     * @param conn                  linux ssh连接
     * @param localFile             服务器文件
     * @param remoteTargetDirectory 客户端文件夹
     */
    public void scpDownloadFile(Connection conn, String localFile,
                                String remoteTargetDirectory) throws IOException {
        SCPClient client = new SCPClient(conn);
        client.put(localFile, remoteTargetDirectory);
    }

    /**
     * 将服务器中的文件批量下载到本地文件夹
     *
     * @param conn                  linux ssh连接
     * @param localFile             服务器文件
     * @param remoteTargetDirectory 客户端文件夹
     */
    public void scpDownloadFile(Connection conn, String[] localFile,
                                String remoteTargetDirectory) throws IOException {
        SCPClient client = new SCPClient(conn);
        client.put(localFile, remoteTargetDirectory);

    }

    /**
     * 关闭连接
     *
     * @param conn shell连接
     */
    public void closeConn(Connection conn) {
        conn.close();
    }


    public List<String> runCommend(Connection conn, String command) throws IOException {
        List<String> result = new ArrayList<>();
        Session session = conn.openSession();
        session.execCommand(command);
        InputStream is = new StreamGobbler(session.getStdout());//获得标准输出流
        BufferedReader brs = new BufferedReader(new InputStreamReader(is));
        for (String line = brs.readLine(); line != null; line = brs.readLine()) {
            result.add(line);
        }
        if (session != null) {
            session.close();
        }
        session.close();
        return result;
    }
}
