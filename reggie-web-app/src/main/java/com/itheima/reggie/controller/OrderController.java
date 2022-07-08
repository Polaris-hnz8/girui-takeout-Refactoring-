package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
