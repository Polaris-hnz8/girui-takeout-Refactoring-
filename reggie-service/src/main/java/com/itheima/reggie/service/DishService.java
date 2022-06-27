package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Employee;

public interface DishService {
    // 分页查询
    Page<Dish> findByPage(Integer pageNum, Integer pageSize, String name);

    // 新增菜品
    void save(Dish dish);

    // 菜品回显
    Dish findById(Long id);

    // 菜品修改
    void update(Dish dish);
}