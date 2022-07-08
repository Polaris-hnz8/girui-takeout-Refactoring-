package com.itheima.reggie.service;

import com.itheima.reggie.domain.Address;

import java.util.List;

public interface AddressService {

    //查询地址信息列表
    List<Address> addressList();

    //新增地址
    void save(Address address);
}
