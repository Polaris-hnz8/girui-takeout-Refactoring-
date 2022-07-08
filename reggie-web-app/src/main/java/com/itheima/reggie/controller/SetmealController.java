package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Setmeal;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    // h5页面套餐展示
    @GetMapping("/setmeal/list")
    public ResultInfo setmealList(Long categoryId, Integer status) {
        // 1.调用service
        List<Setmeal> setmealList = setmealService.setmealList(categoryId);

        // 2.返回结果
        return ResultInfo.success(setmealList);

    }
}
