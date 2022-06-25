package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

// 内部员工模块
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    HttpSession session;

    @PostMapping("/employee/login")
    public ResultInfo login(@RequestBody Map<String, String> param) {
        // 1.接收请求参数
        String username = param.get("username");
        String password = param.get("password");
        // 2.调用service层的login方法完成登录
        ResultInfo resultInfo = employeeService.login(username, password);
        // 3.如果登录成功，将员工信息存储session
        if (resultInfo.getCode() == 1) {
            Employee employee = (Employee) resultInfo.getData();
            session.setAttribute(Constant.SESSION_EMPLOYEE, employee);
        }
        // 4.返回结果
        return resultInfo;
    }

    // 退出
    @PostMapping("/employee/logout")
    public ResultInfo logout(){
        // 1.清除session
        session.invalidate();
        // 2.返回结果
        return ResultInfo.success(null);
    }

    // 分页查找
    @GetMapping("/employee/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            String name) { // 1.接收参数

        // 2.调用service
        Page<Employee> page = employeeService.findByPage(pageNum, pageSize, name);

        // 3.返回结果
        return ResultInfo.success(page);

    }

    //新增员工
    @PostMapping("/employee")
    public ResultInfo save(@RequestBody Employee employee){
        //调用service完成功能
        employeeService.save(employee);
        //返回添加结果
        return ResultInfo.success(null);
    }

    // 回显员工（根据id查询）
    @GetMapping("/employee/{id}")
    public ResultInfo findById(@PathVariable Long id) { // 1.接收参数
        // 2.调用serivce
        Employee employee = employeeService.findById(id);
        // 3.返回结果
        return ResultInfo.success(employee);
    }

    // 修改员工
    @PutMapping("/employee")
    public ResultInfo update(@RequestBody Employee employee) { // 1.接收参数
        // 2.调用serivce修改
        employeeService.update(employee);
        // 3.返回结果
        return ResultInfo.success(null);
    }
}
