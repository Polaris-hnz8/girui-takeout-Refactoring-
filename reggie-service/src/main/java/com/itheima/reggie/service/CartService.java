package com.itheima.reggie.service;

import com.itheima.reggie.domain.Cart;

import java.util.List;

public interface CartService {
    // 添加购物车
    Cart cartAdd(Cart cartParam);

    // 查询购物车
    List<Cart> cartList();

    //清空购物车
    void cartClean();
}
