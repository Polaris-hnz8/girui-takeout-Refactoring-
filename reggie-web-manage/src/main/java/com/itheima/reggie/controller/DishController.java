package com.itheima.reggie.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 菜品分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/dish/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            String name) { // 1.接收参数

        // 2.调用service
        Page<Dish> page = dishService.findByPage(pageNum, pageSize, name);

        // 3.返回结果
        return ResultInfo.success(page);
    }

    /**
     * 新增菜品
     * @param dish
     * @return
     */
    @PostMapping("/dish")
    public ResultInfo save(@RequestBody Dish dish) { // 1.接收参数
        // 2.调用serivce保存
        dishService.save(dish);
        // 3.返回成功结果
        return ResultInfo.success(null);
    }

    /**
     * 单个菜品详情回显
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}") // 前面没有#
    public ResultInfo findById(@PathVariable Long id) {
        // 1.调用service查询
        Dish dish = dishService.findById(id);
        // 2.返回结果
        return ResultInfo.success(dish);
    }

    /**
     * 菜品修改
     * @param dish
     * @return
     */
    @PutMapping("/dish")
    public ResultInfo update(@RequestBody Dish dish) {
        // 1.调用service修改
        dishService.update(dish);
        // 2.返回成功消息
        return ResultInfo.success(null);
    }

    /**
     * 菜品删除
     * @param ids
     * @return
     */
    @DeleteMapping("/dish")
    public ResultInfo deleteBatchIds(@RequestParam List<Long> ids) {
        // 1.调用service删除
        dishService.deleteBatchIds(ids);

        //2.返回成功消息
        return ResultInfo.success(null);
    }

    /**
     * 菜品起售与停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/dish/status/{status}")
    public ResultInfo updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        // 1.调用service改变菜品状态
        dishService.updateStatus(status,ids);

        // 2.返回成功消息
        return ResultInfo.success(null);
    }

    /**
     * 1.根据分类id查询菜品列表（给新增套餐信息回显使用）
     * 2.菜品检索（提供给套餐新增、修改时使用）
     * @param categoryId
     * @param name
     * @return
     */
    @GetMapping("/dish/list")
    public ResultInfo findList(Long categoryId, String name){
        List<Dish> dishList = null;
        if (StrUtil.isEmpty(name)) {
            dishList = dishService.findListByCategoryId(categoryId);
        } else {
            dishList = dishService.findByName(name);
        }
        return ResultInfo.success(dishList);
    }
}
