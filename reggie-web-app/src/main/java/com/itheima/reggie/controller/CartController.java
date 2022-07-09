package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cartParam
     * @return
     */
    @PostMapping("/add")
    public ResultInfo cartAdd(@RequestBody Cart cartParam) {
        // 1.调用service添加
        Cart cart = cartService.cartAdd(cartParam);

        // 2.返回购物车数据
        return ResultInfo.success(cart);
    }

    /**
     * 修改购物车
     * @param cartParam
     * @return
     */
    @PostMapping("/sub")
    public ResultInfo cartUpdate(@RequestBody Cart cartParam) {
        // 1.调用service添加
        Cart cart = cartService.cartUpdate(cartParam);

        // 2.返回购物车数据
        return ResultInfo.success(cart);
    }

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("/list")
    public ResultInfo cartList() {
        // 1.调用service查询
        List<Cart> cartList = cartService.cartList();

        // 2.返回结果
        return ResultInfo.success(cartList);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public ResultInfo cartClean(){
        // 1.调用service删除
        cartService.cartClean();

        // 2.返回成功
        return ResultInfo.success("清空购物车成功");
    }
}
