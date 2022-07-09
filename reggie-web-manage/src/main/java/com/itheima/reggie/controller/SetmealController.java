package com.itheima.reggie.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/setmeal/page")
    public ResultInfo findByPage(
            String name,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // 1.调用service查询
        Page<Setmeal> page = setmealService.findByPage(pageNum, pageSize, name);

        // 2.返回结果
        return ResultInfo.success(page);
    }

    /**
     * 新增套餐
     * @param setmeal
     * @return
     */
    @PostMapping("/setmeal")
    public ResultInfo save(@RequestBody Setmeal setmeal) {
        // 1.调用service
        setmealService.save(setmeal);

        // 2.返回结果
        return ResultInfo.success("新增套餐成功");
    }

    /**
     * 套餐起售与停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/setmeal/status/{status}")
    public ResultInfo updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        // 1.调用service改变菜品状态
        setmealService.updateStatus(status,ids);

        // 2.返回成功消息
        return ResultInfo.success(null);
    }

    /**
     * 套餐批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/setmeal")
    public ResultInfo deleteBatchIds(@RequestParam List<Long> ids) {
        //1.调用serivce删除
        if (CollectionUtil.isNotEmpty(ids)) {
            setmealService.deleteBatchIds(ids);
        }

        //2.返回结果
        return ResultInfo.success(null);
    }

}
