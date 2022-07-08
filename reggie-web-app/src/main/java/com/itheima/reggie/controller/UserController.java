package com.itheima.reggie.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.SmsTemplate;
import com.itheima.reggie.domain.User;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private HttpSession session;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    /**
     * 发送短信
     * @param param
     * @return
     */
    @PostMapping("/user/sendMsg")
    public ResultInfo sendMsg(@RequestBody Map<String, String> param) {// 接收请求参数
        // 1.取出手机号，生成6位随机数存入session中
        String phone = param.get("phone");
        String code = RandomUtil.randomNumbers(6);
        log.info("手机号：{}    短信验证码：{}", phone, code);
        session.setAttribute("phone_sms:" + phone, code); // session中存储

        // 2.调用第三方接口发送
        // smsTemplate.sendSms(phone, code); // TODO 开发期不做短信发送，上线修改回来

        // 3.返回成功
        return ResultInfo.success(null);
    }

    /**
     * 登录注册
     * @param param
     * @return
     */
    @PostMapping("/user/login")
    public ResultInfo login(@RequestBody Map<String, String> param) {
        // 1.接收请求参数：手机号和验证码
        String phone = param.get("phone");
        String code = param.get("code");

        //2.从session中获取保存的信息
        // TODO 在学习redis之前 我们的验证码判断 暂时在controller中解决
        String codeFromSession = (String) session.getAttribute("phone_sms:" + phone);

        //3.在判断输入的验证码与提供的验证码是否一致（页面中提交的code与session中保存的code是否相同？）
        if (!StrUtil.equals(code, codeFromSession)) {
            throw new CustomException("无效验证码");
        }

        //4.调用service登录
        ResultInfo resultInfo = userService.login(code, phone);

        //5.通过resultInfo判断是否登录成功，如果登录成功将user放入session中进入登录状态
        if (resultInfo.getCode() == 1) {
            User user = (User) resultInfo.getData();
            session.setAttribute(Constant.SESSION_USER, user);
            //当登录完成后验证码应该立即失效
            session.removeAttribute("phone_sms:" + phone);
        }

        //6.返回用户用户结果
        return resultInfo;
    }

    /**
     * 用户退出
     * @return
     */
    @PostMapping("/user/logout")
    public ResultInfo logout(){
        session.invalidate();
        return ResultInfo.success("退出登录成功");
    }
}