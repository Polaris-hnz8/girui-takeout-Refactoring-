package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    //新增
    void save(Category category);

    //修改分类
    void update(Category category);

    // 查询菜品数量
    Integer countDishByCategoryId(Long id);

    // 查询套餐数量
    Integer countSetmealByCategoryId(Long id);

    // 删除
    void delete(Long id);
}
