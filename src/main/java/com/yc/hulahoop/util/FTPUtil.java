package com.yc.hulahoop.util;

import com.yc.hulahoop.common.Const;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static final String FTP_IP = PropertiesUtil.getProperty("ftp.server.ip");
    private static final Integer FTP_PORT = PropertiesUtil.getIntProperty("ftp.port");
    private static final String FTP_USER = PropertiesUtil.getProperty("ftp.user");
    private static final String FTP_PASSWORD = PropertiesUtil.getProperty("ftp.password");
    private static final String FTP_STRATEGY = PropertiesUtil.getProperty("ftp.strategy");
    private static final String FTP_AVATAR = PropertiesUtil.getProperty("ftp.avatar");

    private static FTPClient ftpClient;

    //连接FTP服务器
    private static boolean connectServer(String ip, int port, String user, String pwd) {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.setDefaultPort(port);
            return ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }
        return false;
    }

    //上传图片
    public static boolean uploadFile(String name, InputStream inputStream, String imgType) throws IOException {
        logger.info("******连接FTP服务器.......");
        boolean uploaded = true;
        //连接FTP服务器
        if (connectServer(FTP_IP, FTP_PORT, FTP_USER, FTP_PASSWORD)) {
            try {
                switch (imgType){
                    //更换上传路径至攻略图片
                    case Const.STRATEGY:ftpClient.changeWorkingDirectory(FTP_STRATEGY);break;
                    //更换上传路径至头像
                    case Const.AVATAR:ftpClient.changeWorkingDirectory(FTP_AVATAR);break;
                    default:return false;
                }
                //设置1M的缓存
                ftpClient.setBufferSize(1024);
                //设置字符集为UTF-8
                ftpClient.setControlEncoding("UTF-8");
                //设置文件类型: 二进制
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                //上传文件
                ftpClient.storeFile(name, inputStream);
            } catch (IOException e) {
                uploaded = false;
                logger.error("******上传文件异常", e);
            } finally {
                ftpClient.disconnect();
            }
        }
        logger.info("******结束上传,上传结果:{}", uploaded);
        return uploaded;
    }

}