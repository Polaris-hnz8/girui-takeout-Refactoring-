package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;

public interface DishService {

    // 分页查询
    Page<Dish> findByPage(Integer pageNum, Integer pageSize, String name);
}