package com.itheima.reggie.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@Slf4j
public class SmsUtil {
    static final String host = "https://gyytz.market.alicloudapi.com"; //产品域名,开发者无需替换
    static final String path = "/sms/smsSend"; //接口地址，开发者无需修改
    static final String method = "POST"; //请求方式，无需修改
    static String appcode = "96c0386dabcc46fe9e379d9195992dec"; //appCode 需要替换成自己的
    static Integer minute = 5; //验证码有效时长（分钟）
    static String smsSignId = "b8051460d1d84e0784a4ff1ede561963"; //短信签名，可以修改成自己的


    public static String sendSms(String phone, String code) {
        String templateId = "02551a4313154fe4805794ca069d70bf"; //登录模板ID

        //1.headers
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        //2.querys
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:"+minute);
        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);

        //3.bodys
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            log.info(response.getStatusLine().getStatusCode()+"");
            return response.getStatusLine().getStatusCode()+""; //200
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}

