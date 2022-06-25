package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public ResultInfo login(String username, String password) {
        // 1.根据username调用mapper查询
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee == null) {
            return ResultInfo.error("员工不存在");
        }
        // 2.比对密码
        // 2-1 将前端密码进行加密
        String md5Pwd = SecureUtil.md5(password);
        // 2-2 取出数据密码，比对
        if (!StrUtil.equals(employee.getPassword(), md5Pwd)) { // 解决空指针问题
            return ResultInfo.error("密码不正确");
        }
        // 3.员工是否禁用
        if (employee.getStatus() == 0) {
            return ResultInfo.error("此员工被冻结，请联系管理员");
        }
        // 4.登录成功，返回结果
        return ResultInfo.success(employee);
    }



    @Override
    public Page<Employee> findByPage(Integer pageNum, Integer pageSize, String name) {
        // 1.查询条件封装
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(name), Employee::getName, name);
        // 2.分页条件封装
        Page<Employee> page = new Page<>(pageNum, pageSize);
        // 3.执行mapper查询
        page = employeeMapper.selectPage(page, wrapper);
        return page; // 菜品（分类、口味）
    }

    @Override
    public void save(Employee employee) {
        // 1.补齐参数
        // 1-1 id 雪花算法ID生成器
        Long id = IdUtil.getSnowflake(1,1).nextId();
        employee.setId(id);
        // 1-2 密码 md5加密
        String md5Pwd = SecureUtil.md5(Constant.INIT_PASSWORD); //默认密码加密
        employee.setPassword(md5Pwd);
        // 1-3 员工状态
        employee.setStatus(Employee.STATUS_ENABLE);
        //创建，更新时间
        //employee.setCreateTime(new Date());
        //employee.setUpdateTime(new Date());
        //创建，更新人
        //employee.setCreateUser(1L);
        //employee.setUpdateUser(1L);
        // 2.调用mapper新增
        employeeMapper.insert(employee);
    }

    @Override
    public Employee findById(Long id) {
        // 直接调用mapper
        return employeeMapper.findById(id);
    }

    @Override
    public void update(Employee employee) {
        // 1.补齐参数
        //employee.setUpdateTime(new Date());
        //employee.setUpdateUser(1L); //暂时写死1L

        // 2.调用mapper修改
        employeeMapper.update(employee);
        //employeeMapper.update(employee);
    }
}
