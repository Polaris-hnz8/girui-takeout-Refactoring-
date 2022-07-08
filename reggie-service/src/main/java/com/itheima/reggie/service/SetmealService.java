package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Setmeal;

import java.util.List;

public interface SetmealService {
    // 套餐分页查询
    Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name);

    //提供给移动端直接查询所有的套餐dish与套餐setmeal的方法
    List<Setmeal> setmealList(Long categoryId);

    // 新增套餐
    void save(Setmeal setmeal);

    // 套餐删除
    void deleteBatchIds(List<Long> ids);
}
