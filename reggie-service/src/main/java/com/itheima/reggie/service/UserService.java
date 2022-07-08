package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;

//前台用户
public interface UserService {
    // 登录注册
    ResultInfo login(String code, String phone);
}
