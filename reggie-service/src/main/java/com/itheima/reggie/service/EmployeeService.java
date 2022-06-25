package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface EmployeeService {

    // 员工登录
    ResultInfo login(String username, String password);

    // 分页查询
    Page<Employee> findByPage(Integer pageNum, Integer pageSize, String name);

    //添加员工
    void save(Employee employee);

    //回显员工
    Employee findById(Long id);

    //修改员工
    void update(Employee employee);
}
