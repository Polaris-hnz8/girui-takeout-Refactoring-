package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface DishService {
    // 分页查询
    Page<Dish> findByPage(Integer pageNum, Integer pageSize, String name);

    // 新增菜品
    void save(Dish dish);

    // 菜品回显
    Dish findById(Long id);

    // 菜品修改
    void update(Dish dish);

    // 菜品删除
    void deleteBatchIds(List<Long> ids);

    // 菜品起售、停售
    void updateStatus(Integer status, List<Long> ids);

    // 根据分类id查询菜品列表
    List<Dish> findListByCategoryId(Long categoryId);

}