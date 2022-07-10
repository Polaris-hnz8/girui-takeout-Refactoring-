package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.domain.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {
    //订单创建
    void save(Order order);

    //订单分页显示
    Page<Order> findByPage(Integer pageNum, Integer pageSize, Long number, String beginTime, String endTime);

    //移动端用户订单查询
    Page<Order> userOrdrePage(Integer pageNum, Integer pageSize);
}
