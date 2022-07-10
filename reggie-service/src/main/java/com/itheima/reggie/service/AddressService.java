package com.itheima.reggie.service;

import com.itheima.reggie.domain.Address;

import java.util.List;

public interface AddressService {

    //查询地址信息列表
    List<Address> addressList();

    //新增地址
    void save(Address address);

    //地址回显
    Address findById(Long id);

    //修改地址
    void update(Address address);

    //设置默认地址
    void setDefault(Long id);

    //查询用户的默认收货地址（购物车结算时使用）
    Address findDefault();

    //地址的批量逻辑删除
    void deleteBatchIds(List<Long> ids);
}
