package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 菜品分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<Dish> findByPage(Integer pageNum, Integer pageSize, String name) {
        // 1.查询菜品分页数据
        // (1)查询条件封装
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(name), Dish::getName, name);
        // (2)分页条件封装
        Page<Dish> page = new Page<>(pageNum, pageSize);
        // (3)执行mapper查询
        page = dishMapper.selectPage(page, wrapper);
        // 2.遍历每一个菜品对象
        List<Dish> dishList = page.getRecords();
        if (CollectionUtil.isNotEmpty(dishList)) {
            for (Dish dish : dishList) {
                // 3.查询分类对象
                Category category = categoryMapper.selectById(dish.getCategoryId());
                dish.setCategoryName(category.getName());
                // 4.查询口味列表
                // (1)封装口味的查询条件
                LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
                dishFlavorWrapper.eq(DishFlavor::getDishId, dish.getId());
                // (2)查询list
                List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(dishFlavorWrapper);
                // (3)封装到菜品对象中
                dish.setFlavors(dishFlavorList);
            }
        }
        return page; // 菜品（分类、口味）
    }

    /**
     * 回显菜品分类
     * @param dish
     */
    @Override
    public void save(Dish dish) {
        // 1.添加菜品的主干信息
        log.info("菜品保存之前：{}",dish.getId());
        dishMapper.insert(dish);
        log.info("菜品保存之后：{}",dish.getId());

        // 2.根据添加后的菜品id，添加菜品的口味dis_flavor
        List<DishFlavor> flavorList = dish.getFlavors();
        if (CollectionUtil.isNotEmpty(flavorList)) {
            for (DishFlavor dishFlavor : flavorList) {
                // 为口味指定所属的菜品id
                dishFlavor.setDishId(dish.getId());
                // 保存口味
                dishFlavorMapper.insert(dishFlavor);
            }
        }
    }
}
