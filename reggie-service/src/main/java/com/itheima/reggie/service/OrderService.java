package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Order;

import java.util.Date;

public interface OrderService {
    //订单创建
    void save(Order order);

    //订单分页显示
    Page<Order> findByPage(Integer pageNum, Integer pageSize, Long number, String beginTime, String endTime);
}
