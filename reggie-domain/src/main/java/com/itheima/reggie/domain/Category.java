package com.itheima.reggie.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

//分类
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("category")//当前实体类对应的数据表名,默认值为当前类名首字母小写
public class Category implements Serializable {

    @TableId
    private Long id;//主键

    private Integer type;//类型 1 菜品分类 2 套餐分类

    private String name;//分类名称

    private Integer sort; //顺序

    // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)  //在更新时自动填充
    private Date createTime;//创建时间

    // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

    @TableField(fill = FieldFill.INSERT)  //在更新时自动填充
    private Long createUser;//创建人

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;//修改人

    //标识的逻辑删除字段
    @TableLogic(value = "1",delval = "0")
    private Integer deleted;

}