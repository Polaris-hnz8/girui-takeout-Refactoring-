package com.itheima.reggie.controller;

import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Address;
import com.itheima.reggie.domain.Dish;
import com.itheima.reggie.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 查询地址列表
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

    /**
     * 单个地址详情回显
     * @param id
     * @return
     */
    @GetMapping("/{id}") // 前面没有#
    public ResultInfo findById(@PathVariable Long id) {
        // 1.调用service查询
        Address address = addressService.findById(id);
        // 2.返回结果
        return ResultInfo.success(address);
    }

    /**
     * 修改地址
     * @param address
     * @return
     */
    @PutMapping
    public ResultInfo update(@RequestBody Address address) {
        // 1.调用service保存
        addressService.update(address);

        // 2.返回成功
        return ResultInfo.success("修改成功");
    }

    /**
     * 设置默认地址
     * @param param
     * @return
     */
    @PutMapping("/default")
    public ResultInfo setDefault(@RequestBody Map<String, Long> param){
        // 1.调用service更新
        Long id = param.get("id");
        addressService.setDefault(id);

        // 2.返回成功
        return ResultInfo.success(null);
    }

    /**
     * 查询用户的默认收货地址
     * @return
     */
    @GetMapping("/default")
    public ResultInfo findDefault() {
        // 1.调用service查询
        Address address = addressService.findDefault();
        if (address == null) {
            throw new CustomException("请设置默认地址后，再结算购物车");
        }

        // 2.返回成功
        return ResultInfo.success(address);
    }

    /**
     * 地址的批量逻辑删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResultInfo update(@RequestParam List<Long> ids) {
        // 1.调用service进行逻辑删除
        addressService.deleteBatchIds(ids);

        // 2.返回成功
        return ResultInfo.success("逻辑删除成功");
    }
}
