package com.itheima.reggie.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

// 员工
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("employee")//当前实体类对应的数据表名,默认值为当前类名首字母小写
public class Employee implements Serializable {

    //状态标识常量
    public static final Integer STATUS_DISABLE = 0;
    public static final Integer STATUS_ENABLE = 1;

    @TableId
    private Long id;//主键

    private String username;//用户名

    private String name;//姓名

    private String password;//密码

    private String phone;//手机号

    private String sex;//性别

    private String idNumber;//身份证号

    private Integer status;//状态 0:禁用 1:正常

    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)  //在更新时自动填充
    private Date createTime;//创建时间

    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;//创建用户

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;//更新用户
}