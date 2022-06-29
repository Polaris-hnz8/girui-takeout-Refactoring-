package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

    // 根据用户名查询（登录时进行密码校验）
    Employee findByUsername(String username);

    //添加用户
    void save(Employee employee);

    //根据id查询
    Employee findById(Long id);

    //修改员工
    void update(Employee employee);
}
