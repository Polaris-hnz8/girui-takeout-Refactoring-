package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("/list")
    public ResultInfo addressList() {
        // 1.调用service查询
        List<Address> addresseList = addressService.addressList();

        // 2.返回结果
        return ResultInfo.success(addresseList);
    }

    /**
     * 新增地址
     * @param address
     * @return
     */
    @PostMapping
    public ResultInfo save(@RequestBody Address address) {
        // 1.调用service保存
        addressService.save(address);

        // 2.返回成功
        return ResultInfo.success(null);

    }
}
