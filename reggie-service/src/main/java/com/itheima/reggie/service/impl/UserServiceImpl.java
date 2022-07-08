package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.User;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultInfo login(String code, String phone) {
        // 1. 对比验证码（预留位置）

        // 2.根据手机号查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);

        // 3.新用户帮他注册
        if (user == null) {
            //没有注册进行注册
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            //非必要字段可以给默认值，也可以不处理
            userMapper.insert(user);
        } else {
            // 4.已经注册则直接进行登录操作
            if (user.getStatus() != 1) {//判断状态是否为禁用
                throw new CustomException("此用户已被禁用，请联系企业客服~~");
                //return ResultInfo.error("此用户已被禁用，请联系企业客服~~");
            }
        }

        // 5.登录成功
        return ResultInfo.success(user);
    }
}
