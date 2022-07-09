package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Order;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     * @param pageNum
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/order/page")
    public ResultInfo findByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            Long number, String beginTime, String endTime) { // 1.接收参数
        log.info("pageNum：" + pageNum + "，pageSize：" + pageSize + "，orderId：" + number);
        log.info("beginTime：" + beginTime + "，endTime：" + endTime);

        // 2.调用service
        Page<Order> page = orderService.findByPage(pageNum, pageSize, number, beginTime, endTime);

        // 3.返回结果
        return ResultInfo.success(page);
    }
}
