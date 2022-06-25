package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;

import java.util.List;

public interface CategoryService {

    // 分类列表
    List<Category> findAll();

    //新增分类
    void save(Category category);

    //修改分类
    void update(Category category);

    // 删除分类
    ResultInfo delete(Long id);
}
