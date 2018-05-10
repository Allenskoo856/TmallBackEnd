package com.mmall.aliyunoss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.mmall.util.Propertiesutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * @author : Administrator
 * @create 2018-05-10 19:43
 */
public class AliyunOssConfigre {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssConfigre.class);

    private static String endpoint = Propertiesutil.getProperty("aliyun.endpoint");
    private static String accessKeyId = Propertiesutil.getProperty("aliyun.accessKeyId");
    private static String accessKeySecret = Propertiesutil.getProperty("aliyun.accessKeySecret");
    private static String bucketName = Propertiesutil.getProperty("aliyun.bucketName");
    private static boolean SupportCname = Boolean.parseBoolean(Propertiesutil.getProperty("OSSClient.SupportCname"));

    private static ClientConfiguration conf = new ClientConfiguration();
    private static OSSClient client;

    // 开启支持CNAME选项
    static {
        conf.setMaxConnections(200);
        conf.setSocketTimeout(10000);
        conf.setMaxErrorRetry(5);
        conf.setSupportCname(SupportCname);
        // 创建OSSClient实例
        client = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
    }

    /**
     * 辅助方法、上传文件
     *
     * @param key      文件的上传的名字
     * @param fileList 文件
     * @return boolean
     * @throws FileNotFoundException
     */
    public static boolean uploadFile(String key, List<File> fileList) throws FileNotFoundException {
        boolean result = AliyunOssConfigre.uploadFile(bucketName, key, fileList);
        logger.info("开始链接FTP服务器，结束上传，上传结果{}", result);
        return result;
    }


    /**
     * 辅助方法、上传文件
     *
     * @param bucketName 阿里云oss存贮器名字
     * @param key        文件的上传的名字
     * @param fileList   文件
     * @return boolean
     * @throws FileNotFoundException
     */
    private static boolean uploadFile(String bucketName, String key, List<File> fileList) throws FileNotFoundException {
        boolean uploaded = true;
        try {
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
            }
            for (File fileItem : fileList) {
                InputStream inputStream = new FileInputStream(fileItem);
                client.putObject(bucketName, key, inputStream);
            }
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            uploaded = false;
        } finally {
            client.shutdown();
        }
        return uploaded;
    }

}
