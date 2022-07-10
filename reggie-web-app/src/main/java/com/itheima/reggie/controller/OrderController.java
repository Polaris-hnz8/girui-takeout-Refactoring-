package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param order
     * @return
     */
    @PostMapping("/submit")
    public ResultInfo orderSubmit(@RequestBody Order order) {
        // 1.调用service生成订单
        orderService.save(order);

        // 2.返回成功
        return ResultInfo.success("下单成功");
    }

    /**
     * 移动蹲订单查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public ResultInfo userOrdrePage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) { // 1.接收参数

        // 2.调用service
        Page<Order> page = orderService.userOrdrePage(pageNum, pageSize);

        // 3.返回结果
        return ResultInfo.success(page);
    }
}
