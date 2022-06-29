package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.domain.SetmealDish;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 套餐分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<Setmeal> findByPage(Integer pageNum, Integer pageSize, String name) {
        //共涉及3张表：套餐表（基本信息）、分类表（分类名称）、套餐菜品中间表（套餐中的菜品）
        //1.先查套餐基本信息
        //（1）构建条件对象：模糊查询条件
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.like(StrUtil.isNotEmpty(name), Setmeal::getName, name);
        //（2）构建分页对象
        Page<Setmeal> page = new Page<>(pageNum, pageSize);
        //（3）查询
        page = setmealMapper.selectPage(page, setmealWrapper);

        // 2.获取套餐list集合（所有套餐对象）并遍历
        List<Setmeal> setmealList = page.getRecords();
        if (CollectionUtil.isNotEmpty(setmealList)) {
            //遍历集合获取每一个套餐对象
            for (Setmeal setmeal : setmealList) {
                //3.根据category_id查询分类对象
                Category category = categoryMapper.selectById(setmeal.getCategoryId());
                //将分类名称关联到套餐中
                setmeal.setCategoryName(category.getName());
                // 4.根据setmeal的id查询菜品（中间表）列表
                //（1）构建中间表条件对象
                LambdaQueryWrapper<SetmealDish> sdWrapper = new LambdaQueryWrapper<>();
                sdWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
                //（2）查询套菜菜品集合
                List<SetmealDish> setmealDishList = setmealDishMapper.selectList(sdWrapper);
                //（3）关联到套餐中
                setmeal.setSetmealDishes(setmealDishList);
            }
        }
        // 5.返回结果
        return page;
    }

    /**
     * 套餐新增
     * @param setmeal
     */
    @Override
    public void save(Setmeal setmeal) {
        // 1.先保存套餐基本信息
        setmealMapper.insert(setmeal);
        log.info("保存套餐基本信息，id：{},名称：{},价格：{}", setmeal.getId(),setmeal.getName(),setmeal.getPrice());

        // 2.取出套餐菜品列表
        List<SetmealDish> dishList = setmeal.getSetmealDishes();
        if (CollectionUtil.isNotEmpty(dishList)) {
            for (SetmealDish setmealDish : dishList) {
                // 关联套餐id
                setmealDish.setSetmealId(setmeal.getId());
                // 保存套餐菜品
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    @Override
    public void deleteBatchIds(List<Long> ids) {
        // 1.先判断套餐状态
        //（1）构建套餐条件对象
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.in(Setmeal::getId, ids);
        setmealWrapper.eq(Setmeal::getStatus, 1);
        //（2）查询套餐数量
        Integer count = setmealMapper.selectCount(setmealWrapper);
        if (count > 0) {
            throw new CustomException("删除的套餐状态必须为停售");
        }

        // 2.再删除套餐
        setmealMapper.deleteBatchIds(ids);

        // 3.最后删除套餐菜品
        //（1）构建套餐菜品条件对象
        LambdaQueryWrapper<SetmealDish> sdWrapper = new LambdaQueryWrapper<>();
        sdWrapper.in(SetmealDish::getSetmealId, ids);
        //（2）条件删除
        setmealDishMapper.delete(sdWrapper);
    }
}

