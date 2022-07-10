package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;

import java.util.List;

public interface SetmealService {
    // 套餐分页查询
    Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name);

    //提供给移动端直接查询所有的套餐dish与套餐setmeal的方法
    List<Setmeal> setmealList(Long categoryId);

    // 新增套餐
    void save(Setmeal setmeal);

    //单个套餐信息回显
    Setmeal findById(Long id);

    //套餐修改
    void update(Setmeal setmeal);

    // 套餐启售与停售
    void updateStatus(Integer status, List<Long> ids);

    // 套餐删除
    void deleteBatchIds(List<Long> ids);

    // 移动端获取套餐中菜品详情
    List<SetmealDish> getDishesBySetmealId(Long id);
}
