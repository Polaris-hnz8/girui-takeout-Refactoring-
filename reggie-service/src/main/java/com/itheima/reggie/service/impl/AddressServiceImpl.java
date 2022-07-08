package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /**
     * 查询地址信息列表
     * @return
     */
    @Override
    public List<Address> addressList() {
        // 1.构建条件
        // SELECT * FROM `address_book` WHERE user_id = 1458310743471493121
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());

        // 2.查询并返回
        return addressMapper.selectList(wrapper);
    }

    /**
     * 新增地址
     * @param address
     */
    @Override
    public void save(Address address) {
        // 1.补全信息
        address.setUserId(UserHolder.get().getId());

        // 2.mapper保存
        addressMapper.insert(address);
    }
}
