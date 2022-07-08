package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Cart;
import com.itheima.reggie.mapper.CartMapper;
import com.itheima.reggie.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    /**
     * 向购物车中添加商品
     * @param cartParam
     * @return
     */
    @Override
    public Cart cartAdd(Cart cartParam) {
        // 1.先查询购物车是否有商品：查询条件为用户id+菜品id、用户id+套餐id
        // （1）构建条件
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, UserHolder.get().getId()); // 用户id
        cartWrapper.eq(cartParam.getDishId() != null, Cart::getDishId, cartParam.getDishId()); // 菜品id
        cartWrapper.eq(cartParam.getSetmealId() != null, Cart::getSetmealId, cartParam.getSetmealId()); // 套餐id
        // （2）查询记录
        Cart cart = cartMapper.selectOne(cartWrapper);

        // 2.购物车新增判断
        if (cart == null) {
            cartParam.setNumber(1); //新增数量为1
            cartParam.setUserId(UserHolder.get().getId()); //补齐用户id
            cartParam.setCreateTime(new Date());
            cartMapper.insert(cartParam);//没有当菜品or套餐时新增记录
            return cartParam;
        } else {
            cart.setNumber(cart.getNumber() + 1);
            cartMapper.updateById(cart);//当有菜品or套餐时数量+1
            return cart;
        }
    }

    /**
     * 查询购物车列表
     * @return
     */
    @Override
    public List<Cart> cartList() {
        // 1.构建条件
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, UserHolder.get().getId());  // 根据当前登录人id

        // 2.查询并返回
        return cartMapper.selectList(wrapper);
    }

}
