# 一、员工管理

## 1.新增员工

### （1）思路分析

#### 前端核心代码

![image-20211103234015426](assets/image-20211103234015426.png) 



![image-20211231084757128](assets/image-20211231084757128.png) 



#### 后台思路分析

>填写列表内容，点击保存按钮，提交表单，后台补齐表中所缺字段的值，然后保存到数据库

 ![image-20211231085406859](assets/image-20211231085406859.png) 

### （2）代码实现

#### EmployeeController

~~~java
// 新增员工
@PostMapping("/employee")
public ResultInfo save(@RequestBody Employee employee) {  //  1.接收请求体参数

    // 2.调用service新增
    employeeService.save(employee);

    // 3.返回结果
    return ResultInfo.success(null);
}
~~~

#### EmployeeService

~~~java
// 新增员工
void save(Employee employee);
~~~

#### EmployeeServiceImpl

~~~java
@Override
public void save(Employee employee) {
    // 1.补齐参数
    // 1-1 id  雪花算法ID生成器（课下作业）
    Long id = IdUtil.getSnowflake(1, 1).nextId();
    employee.setId(id);
    // 1-2 密码 md5加密
    String md5Pwd = SecureUtil.md5(Constant.INIT_PASSWORD);
    employee.setPassword(md5Pwd);
    // 1-3 员工状态
    employee.setStatus(Employee.STATUS_ENABLE);
    // 创建、更新时间
    employee.setCreateTime(new Date());
    employee.setUpdateTime(new Date());
    // 创建、更新人
    employee.setCreateUser(1L);
    employee.setUpdateUser(1L);

    // 2.调用mapper新增
    employeeMapper.save(employee);
}
~~~

#### EmployeeMapper

~~~java
// 员工新增
void save(Employee employee);
~~~

#### EmployeeMapper.xml

~~~xml
<!--注意：  name在前、 username在后 -->
<insert id="save">
    insert into employee
    values(
    #{id},
    #{name},
    #{username},
    #{password},
    #{phone},
    #{sex},
    #{idNumber},
    #{status},
    #{createTime},
    #{updateTime},
    #{createUser},
    #{updateUser}
    )
</insert>
~~~

### （3）ID精度损失问题

#### 问题说明

>新增完成之后执行查询所有，打印出id，忽然发现，后台返回的id发生了精度损失的情况
>
>这个问题的原因是js在对长度较长的长整型数据进行处理时会损失精度， 从而导致提交的id和数据库中的id不一致
>
>要想解决这个问题，也很简单，只需要让js处理的ID数据类型为字符串类型即可, 这样就不会损失精度了

#### 问题修复

>具体的方式是在后台程序中添加一个数据类型转换器，然后在转换器中自定义数据转换规则，然后将对象转换器配置到spring中

**JacksonObjectMapper**

>在`reggie-common`模块下创建`com.itheima.reggie.common.JacksonObjectMapper`,自定义映射规则

```java
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public JacksonObjectMapper() {
        super();
        //收到未知属性时不报异常
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //反序列化时，属性不存在的兼容处理
        this.getDeserializationConfig().withoutFeatures(FAIL_ON_UNKNOWN_PROPERTIES);

        //自定义转换规则
        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(BigInteger.class, ToStringSerializer.instance)//将BigInteger转换为String
                .addSerializer(Long.class, ToStringSerializer.instance)//将Long转换成String
                .addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DEFAULT_DATE_FORMAT))); // 将Date转为指定格式字符串
        this.registerModule(simpleModule);
    }
}
```

**ReggieWebMvcConfig**

>在`com.itheima.reggie.config.ReggieWebMvcConfig`中添加下面代码

```java
//扩展mvc框架的消息转换器
public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    //创建消息转换器对象
    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    //设置对象转换器，底层使用Jackson将Java对象转为json
    messageConverter.setObjectMapper(new JacksonObjectMapper());
    //将上面的消息转换器对象追加到mvc框架的转换器集合中
    converters.add(0, messageConverter);
}
```



## 2.回显员工

### （1）思路分析

#### 前端核心代码

![image-20211104001254235](assets/image-20211104001254235.png)

![image-20211231091721711](assets/image-20211231091721711.png) 

#### 后台思路分析

>点击编辑按钮时，页面跳转到add.html，并在url中携带参数[员工id]
>
>进入add.html之后，发送查询请求到后台，后台根据id从数据库查询员工信息返回给前端页面

![image-20211231091943344](assets/image-20211231091943344.png) 

 

### （2）代码实现

#### EmployeeController

~~~java
// 回显员工（根据id查询）
@GetMapping("/employee/{id}")
public ResultInfo findById(@PathVariable Long id) { // 1.接收参数
    // 2.调用serivce
    Employee employee = employeeService.findById(id);
    // 3.返回结果
    return ResultInfo.success(employee);

}
~~~

#### EmployeeService

~~~java
// 根据id查询
Employee findById(Long id);
~~~

#### EmployeeServiceImpl

~~~java
@Override
public Employee findById(Long id) {
    // 直接调用mapper
    return employeeMapper.findById(id);
}
~~~

#### EmployeeMapper

~~~java
// 根据id查询
Employee findById(Long id);
~~~

#### EmployeeMapper.xml

~~~xml
<select id="findById" resultType="com.itheima.reggie.domain.Employee">
    select * from employee where id = #{id}
</select>
~~~



## 3.修改员工

### （1）思路分析

#### 前端核心代码

>本项目中新增和修改使用的是同一段前端代码，他会根据跳转到当前页面时，路径上是否含有id参数判断是否是修改

![image-20211104145159533](assets/image-20211104145159533.png) 

![image-20211231095822938](assets/image-20211231095822938.png) 



#### 后台思路分析

![image-20211231100137330](assets/image-20211231100137330.png) 

### （2）代码实现

#### EmployeeController

~~~java
// 修改员工
@PutMapping("/employee")
public ResultInfo update(@RequestBody Employee employee) { // 1.接收参数
    // 2.调用serivce修改
    employeeService.update(employee);
    // 3.返回结果
    return ResultInfo.success(null);
}
~~~

#### EmployeeService

~~~java
// 修改员工
void update(Employee employee);
~~~

#### EmployeeServiceImpl

~~~java
@Override
public void update(Employee employee) {
    // 1.补齐参数
    employee.setUpdateTime(new Date());
    employee.setUpdateUser(1L); // 暂时写死1L

    // 2.调用mapper修改
    employeeMapper.update(employee);
}
~~~

#### EmployeeMapper

~~~java
// 修改员工
void update(Employee employee);
~~~

#### EmployeeMapper.xml

~~~xml
<!--只有字符串类型，才需要判断非空串...-->
<update id="update">
    update employee
    <set>
        <if test="name!=null and name!=''">
            name=#{name},
        </if>
        <if test="username!=null and username!=''">
            username=#{username},
        </if>
        <if test="password!=null and password!=''">
            password=#{password},
        </if>
        <if test="phone!=null and phone!=''">
            phone=#{phone},
        </if>
        <if test="sex!=null and sex!=''">
            sex=#{sex},
        </if>
        <if test="idNumber!=null and idNumber!=''">
            id_number=#{idNumber},
        </if>
        <if test="status!=null">
            status=#{status},
        </if>
        <if test="updateTime!=null">
            update_time=#{updateTime},
        </if>
        <if test="updateUser!=null">
            update_user=#{updateUser},
        </if>
    </set>
    where id = #{id}
</update>
~~~



### （3）文本对比工具

https://www.jq22.com/textDifference





### （4）账号禁用/启用

>账号的禁用/启用其实就是一种修改，它修改的是status字段的值（禁用0 启用1）
>
>这个请求使用的接口跟上面的修改是同一个，所以不需要在单独开发

![image-20211104152154939](assets/image-20211104152154939.png)

 



# 二、分类管理

## 1.分类列表

### （1）思路分析

#### 数据模型

分类对应的是category表，具体表结构如下：

![image-20210801165801665](assets/image-20210801165801665.png) 

套餐名称，是唯一的，不能够重复的，所以在设计表结构时，已经针对于name字段建立了唯一索引，如下：

![image-20210801165921450](assets/image-20210801165921450.png) 

#### 前端核心代码

![image-20211104162654255](assets/image-20211104162654255.png) 

![image-20211231103901885](assets/image-20211231103901885.png) 



#### 后台思路分析

>页面进入分类列表页面后立即发送请求，将所有分类信息查询回来
>
>注意：分类信息要按照sort正（升）序排列

![image-20211231104047531](assets/image-20211231104047531.png) 

 

### （2）代码实现

#### Category

> 在`reggie-domain`模块下创建`com.itheima.reggie.domain.Category`

~~~java
package com.itheima.reggie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//分类
@Data
public class Category implements Serializable {

    private Long id;//主键

    private Integer type;//类型 1 菜品分类 2 套餐分类

    private String name;//分类名称

    private Integer sort; //顺序

    // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private Long createUser;//创建人

    private Long updateUser;//修改人

}
~~~

#### CategoryController

>在`reggie-web-manage`创建`com.itheima.reggie.controller.CategoryController`

~~~java
package com.itheima.reggie.controller;

import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 分类列表
    @GetMapping("/category/findAll")
    public ResultInfo findAll() {
        // 1.调用service查询
        List<Category> list = categoryService.findAll();
        // 2.返回resultInfo结果
        return ResultInfo.success(list);
    }
}
~~~

#### CategoryService

>在`reggie-service`模块下创建`com.itheima.reggie.service.CategoryService`

~~~java
package com.itheima.reggie.service;

import com.itheima.reggie.domain.Category;

import java.util.List;

public interface CategoryService {

    // 分类列表
    List<Category> findAll();
}

~~~

#### CategoryServiceImpl

>在`reggie-service`模块下创建`com.itheima.reggie.service.impl.CategoryServiceImpl`

~~~java
package com.itheima.reggie.service.impl;

import com.itheima.reggie.domain.Category;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}

~~~

#### CategoryMapper

>在`reggie-mapper`模块下创建`com.itheima.reggie.mapper.CategoryMapper`

~~~java
package com.itheima.reggie.mapper;

import com.itheima.reggie.domain.Category;

import java.util.List;

public interface CategoryMapper {

    // 查询所有
    List<Category> findAll();
}

~~~

#### CategoryMapper.xml

>在`reggie-mapper`模块的`resources/mappers`下创建`CategoryMapper.xml`

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itheima.reggie.mapper.CategoryMapper">

    <select id="findAll" resultType="com.itheima.reggie.domain.Category">
        select * from category order by sort asc
    </select>
</mapper>
~~~



## 2.新增分类

### （1）思路分析

#### 前端核心代码

![image-20211104171544421](assets/image-20211104171544421.png) 

![image-20211231105212106](assets/image-20211231105212106.png) 

#### 后台思路分析

>`添加菜品分类`和`添加套餐分类`使用的是同一个后台接口，只不过在传递参数的时候有所差异（type不一致）

![image-20211231105445672](assets/image-20211231105445672.png) 



 

### （2）代码实现

#### CategoryController

~~~java
// 新增分类
@PostMapping("/category")
public ResultInfo save(@RequestBody Category category) { //  1.接收参数

    // 2.调用service新增
    categoryService.save(category);

    // 3.返回resultInfo结果
    return ResultInfo.success(null);
}
~~~

#### CategoryService

~~~java
// 新增分类
void save(Category category);
~~~

#### CategoryServiceImpl

~~~java
// 新增分类
@Override
public void save(Category category) {
    // 1.补齐参数
    // 1-1 id
    long id = IdUtil.getSnowflake(1, 1).nextId();
    category.setId(id);
    // 1-2 创建、更新时间
    category.setCreateTime(new Date());
    category.setUpdateTime(new Date());
    // 1-3 创建、更新人
    category.setCreateUser(1L); // 暂时写死1L
    category.setUpdateUser(1L);// 暂时写死1L

    // 2.调用mapper新增
    categoryMapper.save(category);
}
~~~

#### CategoryMapper

~~~java
// 新增
void save(Category category);
~~~

#### CategoryMapper.xml

~~~xml
<insert id="save">
    insert into category
    values(
    #{id},
    #{type},
    #{name},
    #{sort},
    #{createTime},
    #{updateTime},
    #{createUser},
    #{updateUser}
    )
</insert>

~~~



## 3.修改分类

### （1）思路分析

#### 前端核心代码

>我们原来的数据反显，都是当打开修改框的时候，需要向后台发送一个请求，将数据查询回来
>
>目前这个功能并没有这么做，而是当点击修改按钮的时候，直接将修改按钮所在行的值赋值到修改框中得表单上

![image-20211104180215284](assets/image-20211104180215284.png) 

>当修改完毕，点击确定提交按钮的时候，会将修改之后的数据发送到后台，后台接收数据后，根据id就行修改就可以了

![image-20211104180550693](assets/image-20211104180550693.png) 



![image-20211231110841433](assets/image-20211231110841433.png) 

#### 后台思路分析

> 前端提交要修改的分类的id、name、sort，后台需要根据id更新name和sort

![image-20211231111103030](assets/image-20211231111103030.png) 

 

### （2）代码实现

#### CategoryController

~~~java
// 修改分类
@PutMapping("/category")
public ResultInfo update(@RequestBody Category category) { // 1.接收参数
    // 2.调用serivce修改
    categoryService.update(category);

    // 3.返回resultInfo结果
    return ResultInfo.success(null);
}
~~~

#### CategoryService

~~~java
// 修改分类
void update(Category category);
~~~

#### CategoryServiceImpl

~~~java
// 修改分类
@Override
public void update(Category category) {
    // 1.补齐参数
    category.setUpdateTime(new Date());
    category.setUpdateUser(1l);// 暂时写死

    // 2.调用mapper修改
    categoryMapper.update(category);
}
~~~

#### CategoryMapper

~~~java
// 修改分类
void update(Category category);
~~~

#### CategoryMapper.xml

~~~xml
<update id="update">
    update category
    <set>
        <if test="type != null">
            type = #{type},
        </if>
        <if test="name != null and name != ''">
            name = #{name},
        </if>
        <if test="sort != null">
            sort = #{sort},
        </if>
        <if test="updateTime != null">
            update_time = #{updateTime},
        </if>
        <if test="updateUser != null">
            update_user = #{updateUser},
        </if>
    </set>
    where id = #{id}
</update>
~~~



## 4.删除分类

### （1）思路分析

#### 数据模型说明

那么在这里又涉及到我们后面要用到的两张表结构 dish(菜品表) 和 setmeal(套餐表)。具体的表结构如下： 

<img src="assets/image-20210802001302912.png" alt="image-20210802001302912" style="zoom:80%;" /> 

<img src="assets/image-20210802001348928.png" alt="image-20210802001348928" style="zoom:80%;" /> 

>三张表关系如下

![image-20211104211442408](assets/image-20211104211442408.png) 

#### 前端核心代码

![image-20211104211028268](assets/image-20211104211028268.png) 



![image-20211231112346061](assets/image-20211231112346061.png) 



#### 后台思路分析

>前端点击删除按钮，向后台发送删除请求，并携带id作为请求参数；后台接收id，然后根据id进行删除;
>
>注意删除分类之前要首先判断当前分类下是否含有菜品或者套餐，如果有，则不允许删除

![image-20211104212424567](assets/image-20211104212424567.png) 

### （2）代码实现

#### CategoryController

~~~java
// 删除分类
@DeleteMapping("/category")
public ResultInfo delete(Long id) { // 1.接收参数
    // 2.调用service删除
    ResultInfo resultInfo =  categoryService.delete(id);
    // 3.返回删除结果
    return resultInfo;

}
~~~

#### CategoryService

~~~java
// 删除分类
ResultInfo delete(Long id);
~~~

#### CategoryServiceImpl

~~~java
// 删除分类
@Override
public ResultInfo delete(Long id) {
    // 1.查询菜品数量
    Integer count1 = categoryMapper.countDishByCategoryId(id);
    if (count1 > 0) {
        return ResultInfo.error("该分类下还有菜品不能删除");
    }
    // 2.查询套餐数量
    Integer count2 = categoryMapper.countSetmealByCategoryId(id);
    if (count2 > 0) {
        return ResultInfo.error("该分类下还有套餐不能删除");
    }

    // 3.删除分类
    categoryMapper.delete(id);
    return ResultInfo.success(null);
}
~~~

#### CategoryMapper

~~~java
// 查询菜品数量
Integer countDishByCategoryId(Long id);

// 查询套餐数量
Integer countSetmealByCategoryId(Long id);

// 删除
void delete(Long id);
~~~

#### CategoryMapper.xml

~~~xml
<select id="countDishByCategoryId" resultType="java.lang.Integer">
    SELECT COUNT(*) FROM dish WHERE category_id = #{id}
</select>

<select id="countSetmealByCategoryId" resultType="java.lang.Integer">
    SELECT COUNT(*) FROM setmeal WHERE category_id = #{id}
</select>

<delete id="delete">
    delete from category where id = #{id}
</delete>
~~~

## 5.全局异常处理

![image-20211231121253207](assets/image-20211231121253207.png) 

### CustomException

>在`reggie-common`模块下创建`com.itheima.reggie.common.CustomException`

~~~java
package com.itheima.reggie.common;

//业务异常类
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
~~~

### GlobalExceptionHandler

>全局异常处理类，处理的异常主要分为两类：① 预期异常，需要给客户返回明确提示   ② 非预期异常，需要给客户返回统一的模糊提示  
>
>在`reggie-web-manage`模块下创建`com.itheima.reggie.handler.GlobalExceptionHandler`

~~~java
package com.itheima.reggie.handler;

import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    // unique重复异常
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultInfo duplicateKeyExceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return ResultInfo.error("名称重复");
    }

    // 预期异常处理方法
    @ExceptionHandler(CustomException.class)
    public ResultInfo exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return ResultInfo.error(ex.getMessage());// 返回逻辑中定义的错误提示
    }

    // 非预期异常处理方法
    @ExceptionHandler(Exception.class)
    public ResultInfo exceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return ResultInfo.error("对不起,网络问题,请稍后再试");// 返回一个固定的错误提示
    }
}
~~~



>版本一：delete的service层要改成下面这个版本

~~~java
@Override
public ResultInfo delete(Long id) {
    //1. 查看当前分类下是否有菜品,如果有,不允许删除本分类  查询dish表
    int count1 = categoryMapper.countDishByCategoryId(id);
    if (count1 > 0) {
        throw new CustomException("当前分类下存在菜品,不能删除");//抛异常
    }

    //2. 查看当前分类下是否有套餐,如果有,不允许删除本分类  查询setmeal表
    int count2 = categoryMapper.countSetmealByCategoryId(id);
    if (count2 > 0) {
        throw new CustomException("当前分类下存在套餐,不能删除");//抛异常
    }

    //3. 当前分类下什么都没有,可以删除
    categoryMapper.delete(id);
    return ResultInfo.success(null);
}
~~~



> 版本二：web层和service都需要修改
>
> ```java
>     // 删除分类
>     @DeleteMapping("/category")
>     public ResultInfo delete(Long id) { // 1.接收参数
>         // 2.调用service删除
>         categoryService.delete(id);
>         // 3.返回删除结果
>         return ResultInfo.success(null);
>     }
> ```
>
> ```java
> // 删除分类
> void delete(Long id);
> 
> // 删除分类
> @Override
> public void delete(Long id) {
>     // 1.查询菜品数量
>     Integer count1 = categoryMapper.countDishByCategoryId(id);
>     if (count1 > 0) {
>         // return ResultInfo.error("该分类下还有菜品不能删除");
>         throw new CustomException("该分类下还有菜品不能删除");
>     }
>     // 2.查询套餐数量
>     Integer count2 = categoryMapper.countSetmealByCategoryId(id);
>     if (count2 > 0) {
>         //  return ResultInfo.error("该分类下还有套餐不能删除");
>         throw new CustomException("该分类下还有套餐不能删除");
>     }
>     // 3.删除分类
>     categoryMapper.delete(id);
> }
> ```
>



# 三、技术优化

## 1.MyBatisPlus

>将当前项目持久层技术由原来的的mybatis替换成mybatisplus

### （1）依赖添加MyBatisPlus

修改reggie-parent的pom.xml，添加mybatisplus依赖

~~~xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.2</version>
</dependency>
~~~

### （2）配置MyBatisPlus

修改reggie-web-manage的application.yml，替换为mybatisplus的配置

~~~yaml
server:
  port: 8080
spring:
  application:
    name: reggie # 应用名称
  datasource: # 数据源配置
    druid:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://localhost:3306/reggie?useUnicode=true&characterEncoding=utf-8&useSSL=false
      username: root
      password: root
#mybatis:
#  configuration:
#    map-underscore-to-camel-case: true # 驼峰命名法映射 address_book ---> AddressBook
#    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 日志输出
#  mapper-locations: classpath:/mappers/**.xml # 指定xml位置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:/mappers/**.xml
  global-config:
    db-config:
      id-type: ASSIGN_ID # id生成策略类型
~~~

### （3）Mapper接口继承BaseMapper

让当前的Mapper接口继承BaseMapper

![image-20211121141809337](assets/image-20211121141809337.png)  



## 2.公共字段处理

>目前每个表中都有四个字段：createUser、updateUser、createTime、updateTime
>
>如果每次都要单独设置实在是过于麻烦，本小节，我们使用mybatisplus的自动代码填充来处理公共字段

### （1）注释掉当前代码

注释掉当前代码中设置操作人和操作时间的代码

![image-20211121142328509](assets/image-20211121142328509.png)  

### （2）自动填充注解

对公共自动添加自动填充注解

![image-20211121142837984](assets/image-20211121142837984.png)  

### （3）自动填充逻辑

编写自动填充逻辑

>在`reggie-mapper`模块下创建`com.itheima.reggie.config.MyMetaObjectHandler`类，内容如下

~~~java
package com.itheima.reggie.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//自定义元数据对象处理器
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入操作，自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());
        metaObject.setValue("createUser", 1L);
        metaObject.setValue("updateUser", 1L);
    }

    //更新操作，自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());
        metaObject.setValue("updateUser", 1L);
    }
}
~~~

### （4）更新方法设置

将更新方法改成mybatisPlus提供的

![image-20220101202426687](assets/image-20220101202426687.png) 



## 3.用户信息共享

>目前的代码中是这样设置操作用户Id的`metaObject.setValue("createUser", 1L);`
>
>那么怎样才能在MyMetaObjectHandler中获取当前登录的用户的信息呢？ 
>
>![image-20220101205349264](assets/image-20220101205349264.png) 

### （1）拦截器

拦截器是Spring提供的一种技术, 他有三个方法: 

* preHandle() : 在目标方法之前执行，一般用于预处理 
* postHandle()：在目标方法执行之后执行，一般用于后处理 
* afterCompletion()：整个请求处理完毕，在视图渲染完毕时回调，一般用于资源的清理或性能的统计

![image-20220101205434323](assets/image-20220101205434323.png) 

### （2）ThreadLocal

线程局部变量，该变量对其他线程而言是隔离的；在进行对象跨层传递的时候，使用ThreadLocal可以避免多次传递，打破层次间的约束。

ThreadLocal的三个方法: 

- set(T value) ：设置当前线程绑定的变量
- get()：获取当前线程绑定的变量
- remove() ：移除当前线程绑定的变量 

![image-20220101205540976](assets/image-20220101205540976.png) 



![image-20220102120400490](assets/image-20220102120400490.png) 





### （3）实现思路

![image-20220102121744653](assets/image-20220102121744653.png) 

### （4）代码实现

#### EmployeeHolder

>在`reggie-common`下创建`com.itheima.reggie.common.EmployeeHolder`

~~~java
// threadLocal 封装工具类
public class EmployeeHolder {
    
    private static final ThreadLocal<Employee> TL= new ThreadLocal<>();
    
    
    // 设置
    public static void set(Employee employee){
        TL.set(employee);
    }
    // 获取
    public static Employee get(){
        return TL.get();
    }
    
    // 删除
    public static void remove(){
        TL.remove();
    }
}
~~~

#### LoginCheckInterceptor

~~~java
// 登录拦截器
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 登录拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取session中员工信息
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute(Constant.SESSION_EMPLOYEE);

        // 2.判断 session中没员工，拦截
        if (employee == null) {
            // 手动封装提示信息
            ResultInfo resultInfo = ResultInfo.error("NOTLOGIN");
            // 手动转json
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(resultInfo);
            // 通过response响应
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return false;
        }

        // 3.session有员工，放行
        EmployeeHolder.set(employee); // 将员工信息存储到线程内map集合
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 服务器响应前，删除线程内map集合
        EmployeeHolder.remove();
    }
}
~~~

#### MyMetaObjectHandler

```java
//自定义元数据对象处理器
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入操作，自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());

        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("createUser", employee.getId()); // 创建人
            metaObject.setValue("updateUser", employee.getId()); // 修改人
        }

    }

    //更新操作，自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());
        Employee employee = EmployeeHolder.get();
        if (employee != null) {
            metaObject.setValue("updateUser", employee.getId());  // 修改人
        }
    }
}
```









