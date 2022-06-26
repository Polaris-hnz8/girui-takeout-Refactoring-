package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 根据type查询分类列表
    @GetMapping("/category/list")
    public ResultInfo categoryList(Integer type) { // 1.接收请求参数Get请求的方法的参数可以单独接收
        // 2.调用service查询
        List<Category> list = categoryService.findByType(type);
        // 3.返回结果
        return ResultInfo.success(list);
    }

    // 分类列表
    @GetMapping("/category/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            String name) { // 1.接收参数
        // 1.调用service查询
        Page<Category> page = categoryService.findByPage(pageNum, pageSize, name);
        // 2.返回resultInfo结果
        return ResultInfo.success(page);
    }

    // 新增分类
    @PostMapping("/category")
    public ResultInfo save(@RequestBody Category category) { //  1.接收参数

        // 2.调用service新增
        categoryService.save(category);

        // 3.返回resultInfo结果
        return ResultInfo.success("新增分类成功!");
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
