package com.itheima.reggie.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Page<Category> findByPage(Integer pageNum, Integer pageSize, String name) {
        // 1.查询条件封装
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(name), Category::getName, name);
        wrapper.orderByAsc(Category::getSort);
        // 2.分页条件封装
        Page<Category> page = new Page<>(pageNum, pageSize);
        // 3.执行mapper查询
        page = categoryMapper.selectPage(page, wrapper);
        return page; // 菜品（分类、口味）
    }

    // 新增分类
    @Override
    public void save(Category category) {
        // 1.补齐参数
        // 1-1 id
        long id = IdUtil.getSnowflake(1, 1).nextId();
        category.setId(id);
        // 1-2 创建、更新时间
        //category.setCreateTime(new Date());
        //category.setUpdateTime(new Date());
        // 1-3 创建、更新人
        //category.setCreateUser(1L); // 暂时写死1L
        //category.setUpdateUser(1L);// 暂时写死1L

        // 2.调用mapper新增
        categoryMapper.insert(category);
        //categoryMapper.save(category);
    }

    // 修改分类
    @Override
    public void update(Category category) {
        // 1.补齐参数
        // category.setUpdateTime(new Date());
        // category.setUpdateUser(1l);// 暂时写死

        //LambdaUpdateWrapper<Category> wrapper = Wrappers.lambdaUpdate(new Category());
        // 2.调用mapper修改
        categoryMapper.update(category);
    }

    @Override
    public ResultInfo delete(Long id) {
        //1. 查看当前分类下是否有菜品,如果有,不允许删除本分类  查询dish表
        int count1 = categoryMapper.countDishByCategoryId(id);
        if (count1 > 0) {
            throw new CustomException("当前分类下存在菜品,不能删除");//抛异常
        }

        //2. 查看当前分类下是否有套餐,如果有,不允许删除本分类  查询setmeal表
        int count2 = categoryMapper.countSetmealByCategoryId(id);
        if (count2 > 0) {
            throw new CustomException("当前分类下存在套餐,不能删除");//抛异常
        }

        //3. 当前分类下什么都没有,可以删除
        categoryMapper.deleteById(id);
        return ResultInfo.success(null);
    }

    /**
     * 根据type查询分类列表
     * @param type
     * @return
     */
    @Override
    public List<Category> findByType(Integer type) {
        // 1.构建查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType, type);//type值进行对比
        // 2.调用mapper查询
        return categoryMapper.selectList(wrapper);
    }
}
