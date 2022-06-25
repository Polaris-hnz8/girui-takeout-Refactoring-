package com.itheima.reggie.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.reggie.common.EmployeeHolder;
import com.itheima.reggie.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Date;

//自定义元数据对象处理器
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入操作，自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert...]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());

        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("createUser", employee.getId()); // 创建人
            metaObject.setValue("updateUser", employee.getId()); // 修改人
        }
    }

    //更新操作，自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update...]");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", new Date());
        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("updateUser", employee.getId());  // 修改人
        }
    }
}