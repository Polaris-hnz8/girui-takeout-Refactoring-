package com.itheima.reggie.common;

import com.itheima.reggie.domain.Employee;

// threadLocal 封装工具类
public class EmployeeHolder {

    private static final ThreadLocal<Employee> TL= new ThreadLocal<>();


    // 设置
    public static void set(Employee employee){
        TL.set(employee);
    }
    // 获取
    public static Employee get(){
        return TL.get();
    }

    // 删除
    public static void remove(){
        TL.remove();
    }
}