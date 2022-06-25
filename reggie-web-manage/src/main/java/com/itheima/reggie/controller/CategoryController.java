package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 分类列表
    @GetMapping("/category/findAll")
    public ResultInfo findAll() {
        // 1.调用service查询
        List<Category> list = categoryService.findAll();
        // 2.返回resultInfo结果
        return ResultInfo.success(list);
    }

    // 新增分类
    @PostMapping("/category")
    public ResultInfo save(@RequestBody Category category) { //  1.接收参数

        // 2.调用service新增
        categoryService.save(category);

        // 3.返回resultInfo结果
        return ResultInfo.success(null);
    }

    // 修改分类
    @PutMapping("/category")
    public ResultInfo update(@RequestBody Category category) { // 1.接收参数
        // 2.调用serivce修改
        categoryService.update(category);

        // 3.返回resultInfo结果
        return ResultInfo.success(null);
    }

    // 删除分类
    @DeleteMapping("/category")
    public ResultInfo delete(Long id) { // 1.接收参数
        // 2.调用service删除
        ResultInfo resultInfo =  categoryService.delete(id);
        // 3.返回删除结果
        return resultInfo;

    }
}
