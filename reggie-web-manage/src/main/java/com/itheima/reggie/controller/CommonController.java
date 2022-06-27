package com.itheima.reggie.controller;

import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.OssTemplate;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 专门用于处理文件上传
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.oss.url}")
    private String baseName;

    @Autowired
    private OssTemplate ossTemplate;

    @PostMapping("/upload")
    public ResultInfo uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        String filePath = null;
        try {
            //调取工具类上传文件，返回上传文件的网络地址
            filePath = ossTemplate.upload(filename, inputStream);
            log.info("文件上传成功,访问地址是:{}", filePath);
        } catch (Exception e) {
            throw new CustomException("图片上传失败，请稍后再试...");
        }
        //4.返回数据
        return ResultInfo.success(filePath);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //1.通过图片url获取其对应的objectName
        String objectName = name.replace(baseName + "/", "");
        log.info(objectName);

        //2.通过objectName从服务器中下载指定的文件，返回流式文件
        InputStream inputStream = ossTemplate.download(objectName);

        //3.通过输出流将文件内容写回浏览器，然后在浏览器中进行展示
        OutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");

        //4.流拷贝inputStream拷贝到outputStream中
        //Attempted read on closed stream.httpclient的获取实体流只能使用一次，不能重复使用。
        int len = 0;
        byte[] buff = new byte[1024];

        while ((len = inputStream.read(buff)) > -1) {
            outputStream.write(buff,0, len);
        }
        outputStream.flush();

        //5.关闭输入输出流
        inputStream.close();
        outputStream.close();
    }
}