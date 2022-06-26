package com.itheima.reggie.controller;

import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.OssTemplate;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 专门用于处理文件上传
 */
@RestController
@Slf4j
public class FileController {
    @Autowired
    private OssTemplate ossTemplate;

    @PostMapping("/common/upload")
    public ResultInfo uploadFile(MultipartFile file) throws IOException {
        String filePath = null;
        try {
            //调取工具类上传文件，返回上传文件的网络地址
            filePath = ossTemplate.upload(file.getOriginalFilename(), file.getInputStream());
            log.info("文件上传成功,访问地址是:{}", filePath);
        } catch (Exception e) {
            throw new CustomException("图片上传失败，请稍后再试...");
        }
        //4.返回数据
        return ResultInfo.success(filePath);
    }
}