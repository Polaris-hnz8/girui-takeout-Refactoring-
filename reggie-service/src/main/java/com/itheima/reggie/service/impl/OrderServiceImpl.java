package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.*;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.CartService;
import com.itheima.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void save(Order order) {
        // 1.获取用户信息
        User user = UserHolder.get();

        // 2.获取购物车信息
        List<Cart> cartList = cartService.cartList();
        if (CollectionUtil.isEmpty(cartList)) {
            throw new CustomException("购物车为空不能下单");
        }

        // 3.获取地址信息
        Address address = addressMapper.selectById(order.getAddressBookId());
        if (address == null) {
            throw new CustomException("收货地址为空不能下单");
        }

        // 4.封装订单明细并保存（遍历所有的购物车记录，将每一个购物车记录转换成一个订单详情记录）
        // （1）使用雪花算法生成订单id（为了让每个订单详情都有orderId可以赋值，这里手动生成orderId）
        long orderId = IdWorker.getId();// mp提供的工具类
        // （2）计算订单总金额
        BigDecimal amount = new BigDecimal(0);
        // （3）遍历购物车封装订单明细
        for (Cart item : cartList) {
            OrderDetail orderDetail = new OrderDetail();// 创建订单明细对象

            orderDetail.setName(item.getName()); // 购物车菜品名称
            orderDetail.setOrderId(orderId);// 订单id
            orderDetail.setDishId(item.getDishId());// 购物车菜品id
            orderDetail.setSetmealId(item.getSetmealId());// 购物车套餐id
            orderDetail.setDishFlavor(item.getDishFlavor());// 购物车口味
            orderDetail.setNumber(item.getNumber());// 购物车数量
            orderDetail.setAmount(item.getAmount());// 购物车金额
            orderDetail.setImage(item.getImage());// 购物车图片

            // 订单总金额累加： amount = amount + (cart.getAmount() * cart.getNumber())
            amount = amount.add(item.getAmount().multiply(new BigDecimal(item.getNumber())));
            // 保存订单明细
            orderDetailMapper.insert(orderDetail);
        }

        // 5.封装订单并保存（再将订单中的所有字段补全）
        order.setId(orderId); // 订单id
        order.setNumber(String.valueOf(orderId));// 订单号（id号就是订单号）
        order.setStatus(1); // 1为待付款
        order.setUserId(user.getId());// 用户id

        order.setOrderTime(new Date());// 下单时间
        order.setCheckoutTime(new Date()); // 结账时间
        order.setAmount(amount); // 订单金额

        //查询地址对象，获取收货人手机号、收货人地址、收货人姓名
        order.setUserName(user.getName()); // 用户名称
        order.setPhone(address.getPhone()); // 收货人手机号
        order.setAddress(address.getDetail()); // 收货人地址
        order.setConsignee(address.getConsignee()); // 收货人名称
        // 保存订单
        orderMapper.insert(order);

        // 6.清空购物车
        cartService.cartClean();
    }
}
