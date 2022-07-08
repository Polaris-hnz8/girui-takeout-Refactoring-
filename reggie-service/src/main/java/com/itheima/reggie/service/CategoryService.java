package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Employee;

import java.util.List;

public interface CategoryService {

    // 分类列表
    Page<Category> findByPage(Integer pageNum, Integer pageSize, String name);

    //新增分类
    void save(Category category);

    //修改分类
    void update(Category category);

    // 删除分类
    ResultInfo delete(Long id);

    // 根据type查询分类列表
    List<Category> findByType(Integer type);

    //提供给移动端直接查询所有的菜品dish与套餐setmeal的方法
    List<Category> findAll();
}
