package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.common.UserHolder;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.domain.DishFlavor;
import com.itheima.reggie.mapper.AddressMapper;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.security.auth.callback.LanguageCallback;
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
        wrapper.eq(Address::getIsDeleted, 0);

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

    /**
     * 地址回显
     * @param id
     * @return
     */
    @Override
    public Address findById(Long id) {
        // 1.先查菜品基本信息
        Address address = addressMapper.selectById(id);
        return address;
    }

    /**
     * 修改地址
     * @param address
     */
    @Override
    public void update(Address address) {
        // mapper保存
        addressMapper.updateById(address);
    }

    /**
     * 设置默认地址
     * @param id
     */
    @Override
    public void setDefault(Long id) {
        // 1.先将该用户下的所有地址信息状态更改为0
        // （1）准备实体
        Address address1 = new Address();
        address1.setIsDefault(0);
        // （2）更新操作
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, UserHolder.get().getId());
        addressMapper.update(address1, wrapper);

        // 2.再将需要设为默认地址的地址状态更改为1
        // （1）准备实体
        Address address2 = new Address();
        address2.setId(id);
        address2.setIsDefault(1);
        // （2）主键更新
        addressMapper.updateById(address2);
    }

    /**
     * 查询用户的默认收货地址（购物车结算时使用）
     * @return
     */
    @Override
    public Address findDefault() {
        //1.找到当前用户默认的地址对象
        LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper.eq(Address::getUserId, UserHolder.get().getId());
        addressWrapper.eq(Address::getIsDefault, 1);
        Address address = addressMapper.selectOne(addressWrapper);

        //2.对查询结果进行判断
        if (address == null) {
            throw new CustomException("没有默认地址，请先添加地址!");
        }

        return address;
    }

    /**
     * 地址的批量逻辑删除
     * @param ids
     */
    @Override
    public void deleteBatchIds(List<Long> ids) {
        // 1.查询需要进行删除的地址
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids != null, Address::getId, ids);
        List<Address> addressList = addressMapper.selectList(queryWrapper);

        // 2.封装地址对象
        Address address = new Address();
        address.setIsDeleted(1);

        // 3.mapper进行逻辑删除
        addressMapper.update(address, queryWrapper);
    }
}
