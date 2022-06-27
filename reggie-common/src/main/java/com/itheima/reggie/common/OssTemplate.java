package com.itheima.reggie.common;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//阿里存储工具类
@Component
@ConfigurationProperties(prefix = "reggie.oss")//读取配置信息
@Data
public class OssTemplate {

    private String key; //访问key
    private String secret;//访问秘钥
    private String endpoint;//端点
    private String bucket;//桶名
    private String url;//访问域名

    /**
     * 文件上传
     * @param fileName
     * @param inputStream
     * @return
     */
    public String upload(String fileName, InputStream inputStream) {

        //1.创建客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, key, secret);

        //2.设置文件最终的路径和名称
        //String objectName = "images/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
        String objectName = "reggie/" + fileName;

        //3.meta设置请求头,解决访问图片地址直接下载
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));

        //4.上传
        ossClient.putObject(bucket, objectName, inputStream, meta);

        //5.关闭客户端
        ossClient.shutdown();

        return url + "/" + objectName;
    }

    /**
     * 文件下载
     * @param objectName
     * @return
     */
    public InputStream download(String objectName) throws IOException {
        //1.创建客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, key, secret);

        //2.下载
        OSSObject ossObject = ossClient.getObject(bucket, objectName);
        InputStream inputStream = ossObject.getResponse().getContent();

        //3.关闭客户端
        ossObject.close();
        ossClient.shutdown();

        //4.返回下载到的流式文件
        return inputStream;
    }

    //文件后缀处理
    private String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        return "image/jpg";
    }
}