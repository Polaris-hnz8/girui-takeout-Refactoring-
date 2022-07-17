# 一、环境搭建

## 1.数据导入

![image-20211031155003320](assets\image-20211031155003320.png) 

| **表名**      | **说明**         |
| ------------- | ---------------- |
| employee      | 员工表           |
| category      | 菜品和套餐分类表 |
| dish          | 菜品表           |
| setmeal       | 套餐表           |
| setmeal_dish  | 套餐菜品关系表   |
| dish_flavor   | 菜品口味关系表   |
| user          | 用户表（C端）    |
| address_book  | 地址簿表         |
| shopping_cart | 购物车表         |
| orders        | 订单表           |
| order_detail  | 订单明细表       |

## 2.模块设计

>maven分模块   手画

![image-20211123101154630](assets\image-20211123101154630.png) 

## 3.环境搭建

### （1）创建父工程

> 在idea中创建maven项目，名称`reggie-parent`，删除src目录，并在pom.xml中引入依赖

![image-20211121123134764](assets\image-20211121123134764.png)  

~~~xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.4.5</version>
    <relativePath/>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.7.1</version>
        <scope>compile</scope>
    </dependency>

    <!--数据层-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.23</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.1</version>
    </dependency>
    <!--        <dependency>-->
    <!--            <groupId>com.baomidou</groupId>-->
    <!--            <artifactId>mybatis-plus-boot-starter</artifactId>-->
    <!--            <version>3.4.2</version>-->
    <!--        </dependency>-->

    <!--阿里云-->
    <dependency>
        <groupId>com.aliyun.oss</groupId>
        <artifactId>aliyun-sdk-oss</artifactId>
        <version>3.10.2</version>
    </dependency>
    <dependency>
        <groupId>com.aliyun</groupId>
        <artifactId>aliyun-java-sdk-core</artifactId>
        <version>4.0.6</version>
    </dependency>
    <dependency>
        <groupId>com.aliyun</groupId>
        <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
        <version>1.1.0</version>
    </dependency>

    <!--工具类-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.76</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.20</version>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>5.4.3</version>
    </dependency>

    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
~~~

### （2）创建实体类模块

>创建`reggie-domain`模块，用于存放实体类

 ![image-20211123101449090](assets\image-20211123101449090.png) 

### （3）创建工具类模块

>创建`reggie-common`模块，用于存放通用的一些类，本模块需要依赖`reggie-domain`模块

 ![image-20211123101536624](assets\image-20211123101536624.png) 

### （4）创建持久层模块

>创建`reggie-mapper`模块，用于存放持久层代码，本模块需要依赖`reggie-common`模块

![image-20211123101630787](assets\image-20211123101630787.png)  

### （5）创建业务层模块

>创建`reggie-service`模块，用于存放业务层代码，本模块需要依赖`reggie-mapper`模块

![image-20211121124952313](assets\image-20211121124952313.png) 

### （6）创建表示层模块

>创建`reggie-web-manage`模块，用于存放管理后台的表示层代码，本模块需要依赖`reggie-service`模块

 ![image-20211124194142457](assets\image-20211124194142457.png) 

~~~xml
<dependencies>
    <dependency>
        <groupId>com.itheima</groupId>
        <artifactId>reggie-service</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<build>
    <finalName>reggie-web-manage</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
~~~

**① 添加配置文件**

>在`reggie-web-manage`工程的resources目录下创建`application.yml`文件,并引入配置

~~~yaml
server:
  port: 8080
spring:
  application:
    name: reggie-web-manage # 应用名称
  datasource: # 数据源配置
    druid:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://localhost:3306/reggie?useUnicode=true&characterEncoding=utf-8&useSSL=false
      username: root
      password: root
      
mybatis:
  configuration:
    map-underscore-to-camel-case: true # 驼峰命名法映射 address_book ---> AddressBook
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 日志输出
  mapper-locations: classpath:/mappers/**.xml # 指定xml位置
~~~

**② 创建启动类**

>在`reggie-web-manage`工程下创建启动类`com.itheima.reggie.WebManageApplication`

~~~java
package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication // 启动类
@MapperScan("com.itheima.reggie.mapper")// 扫描mybatis接口创建代理对象
@EnableTransactionManagement // 开启事务注解支持
@Slf4j
public class WebManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebManageApplication.class, args);
        log.info("项目启动成功");
    }
}
~~~

**③ 静态资源导入**

> 资料中已经提供好了本项目所需要的静态资源，只需要将其引入到项目的resources即可

 ![image-20211121130221207](assets\image-20211121130221207.png) 

**④ 静态资源映射**

>在Springboot项目中，<font color='#BAOC2F'>默认静态资源</font>的存放目录为：`classpath:/static/`
>
>而本项目中静态资源存放在backend目录中, 那么这个时候要想访问到静态资源，就需要设置静态资源映射。
>
>创建一个`com.itheima.reggie.config.ReggieWebMvcConfig`配置类，内容如下:

```java
package com.itheima.reggie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//web相关配置
@Configuration
public class ReggieWebMvcConfig implements WebMvcConfigurer {
    //设置静态资源映射
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //当访问请求是/backend/**时,去classpath:/backend/寻找对应资源
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
    }
}
```



### （7）访问测试

> 本地启动工程，通过下面地址进行访问测试http://localhost:8080/backend/page/login/login.html
>

![image-20211031161702593](assets\image-20211031161702593.png)



# 二、系统登录

## 1.员工登录

### （1）思路分析

#### 前端核心代码

![image-20211103190637290](assets\image-20211103190637290.png) 



![image-20211229094053987](assets\image-20211229094053987.png) 



#### 后台思路分析

>当点击登录按钮时，页面会发送请求到`/employee/login`并提交参数username和password, 
>
>后台接收请求参数，校验用户名和密码，返回登录结果
>
>通过观察结果，会发现返回的结果成会包含三部分：code操作结果代码、msg操作代码提示、data操作结果主信息
>
>~~~json
>{
>       "code":"1",
>       "msg":"登录成功",
>       "data":{
>             //返回的对象
>        }
>}
>~~~
>
>因此后台专门准备了一个封装类似对象的类ResultInfo（资料中已经提供），后台直接返回这个对象即可
>
>![image-20211121131606919](assets\image-20211121131606919.png)  

  

![image-20211229095427249](assets\image-20211229095427249.png) 



### （2）代码编写

#### Employee

>在`reggie-domain`模块下创建`com.itheima.reggie.domain.Employee` 

~~~java
package com.itheima.reggie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

// 员工
@Data
public class Employee implements Serializable {

    //状态标识常量
    public static final Integer STATUS_DISABLE = 0;
    public static final Integer STATUS_ENABLE = 1;

    private Long id;//主键

    private String username;//用户名

    private String name;//姓名

    private String password;//密码

    private String phone;//手机号

    private String sex;//性别

    private String idNumber;//身份证号

    private Integer status;//状态 0:禁用 1:正常

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间

    private Long createUser;//创建用户

    private Long updateUser;//更新用户
}
~~~

#### EmployeeController

>在`reggie-web-manage`模块下创建`com.itheima.reggie.controller.EmployeeController`

~~~java
package com.itheima.reggie.controller;

import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

// 内部员工模块
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    HttpSession session;

    @PostMapping("/employee/login")
    public ResultInfo login(@RequestBody Map<String, String> param) {
        // 1.接收请求参数
        String username = param.get("username");
        String password = param.get("password");
        // 2.调用service完成登录
        ResultInfo resultInfo = employeeService.login(username, password);
        // 3.如果登录成功，将员工信息存储session
        if (resultInfo.getCode() == 1) {
            Employee employee = (Employee) resultInfo.getData();
            session.setAttribute(Constant.SESSION_EMPLOYEE, employee);
        }
        // 4.返回结果
        return resultInfo;
    }
}

~~~

**这里需要一个常量类，从资料中导入即可**

![image-20211121132411928](assets\image-20211121132411928.png) 

#### EmployeeService

>在`reggie-service`模块下创建`com.itheima.reggie.service.EmployeeService`

~~~java
package com.itheima.reggie.service;

import com.itheima.reggie.common.ResultInfo;

public interface EmployeeService {

    // 员工登录
    ResultInfo login(String username, String password);
}

~~~

#### EmployeeServiceImpl

>在`reggie-service`模块下创建`com.itheima.reggie.service.impl.EmployeeServiceImpl`

~~~java
package com.itheima.reggie.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public ResultInfo login(String username, String password) {
        // 1.根据username调用mapper查询
        Employee employee = employeeMapper.findByUsername(username);
        if (employee == null) {
            return ResultInfo.error("员工不存在");
        }
        // 2.比对密码
        // 2-1 将前端密码进行加密
        String md5Pwd = SecureUtil.md5(password);
        // 2-2 取出数据密码，比对
        if (!StrUtil.equals(employee.getPassword(), md5Pwd)) { // 解决空指针问题
            return ResultInfo.error("密码不正确");
        }
        // 3.员工是否禁用
        if (employee.getStatus() == 0) {
            return ResultInfo.error("此员工被冻结，请联系管理员");
        }
        // 4.登录成功，返回结果
        return ResultInfo.success(employee);
    }
}

~~~

#### EmployeeMapper

>在`reggie-mapper`模块下创建`com.itheima.reggie.mapper.EmployeeMapper`

~~~java
package com.itheima.reggie.mapper;

import com.itheima.reggie.domain.Employee;

public interface EmployeeMapper {

    // 根据用户名查询
    Employee findByUsername(String username);
}

~~~

#### EmployeeMapper.xml

>在`reggie-mapper`模块下的`resource/mappers`下创建`EmployeeMapper.xml`

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itheima.reggie.mapper.EmployeeMapper">

    <select id="findByUsername" resultType="com.itheima.reggie.domain.Employee">
        select * from employee where username = #{username}
    </select>
</mapper>
~~~



### （3）debug调试

https://blog.csdn.net/yxl_1207/article/details/80973622





### （4）主页面结构

>本项目主页面主要使用iframe实现
>
>![image-20211229103534609](assets\image-20211229103534609.png) 

~~~html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>主页面模拟</title>
    </head>
    <body>
        <table width="100%">
            <tr>
                <td width="100px">
                    <ul>
                        <li><a href="http://www.itcast.cn" target="myFrame">传智</a></li>
                        <li><a href="https://www.aliyun.com" target="myFrame">阿里云</a></li>
                        <li><a href="https://www.taobao.com" target="myFrame">淘宝</a></li>
                        <li><a href="https://www.suning.com/" target="myFrame">苏宁</a></li>
                    </ul>
                </td>
                <td>
                    <iframe name="myFrame" src="http://www.itcast.cn" width="100%" height="800px">

                    </iframe>
                </td>
            </tr>
        </table>
    </body>
</html>
~~~



## 2.员工退出

### （1）思路分析

#### 前端核心代码

![image-20211103204619863](assets\image-20211103204619863.png)  

#### 后台思路分析

>当点击退出按钮时，后台清理session中的用户信息，然后返回成功标识。

![image-20211103205430849](assets\image-20211103205430849.png) 

### （2）代码编写

#### EmployeeController

~~~java
// 退出
@PostMapping("/employee/logout")
public ResultInfo logout(){
    // 1.清除session
    session.invalidate();
    // 2.返回结果
    return ResultInfo.success(null);
}
~~~



## 3.员工列表

### （1）思路分析

#### 数据模型(employee表)

<img src="assets\image-20210726234915737.png" alt="image-20210726234915737" style="zoom:80%;" /> 

#### 前端核心代码

![image-20211103230514453](assets\image-20211103230514453.png) 

![image-20211229105720997](assets\image-20211229105720997.png) 



#### 后台思路分析

>当`进入页面`或者`在搜索框输入员工姓名然后回车`的时候发起请求，查询回员工列表之后，将其渲染到页面表格中
>
>![image-20211229110108664](assets\image-20211229110108664.png) 

 

### （2）代码实现

#### EmployeeController

~~~java
// 员工列表
@GetMapping("/employee/find")
public ResultInfo findList(String name) { // 1.接收请求参数

    // 2.调用service查询员工列表
    List<Employee> list = employeeService.findList(name);
    // 3.返回结果
    return ResultInfo.success(list);
}
~~~

#### EmployeeService

~~~java
// 员工列表
List<Employee> findList(String name);
~~~

#### EmployeeServiceImpl

~~~java
@Override
public List<Employee> findList(String name) {
    // 1.调用mapper查询
    List<Employee> list = employeeMapper.findByName(name);
    // 2.返回结果
    return list;
}
~~~

#### EmployeeMapper

~~~java
// 根据姓名查询
List<Employee> findByName(String name);
~~~

#### EmployeeMapper.xml

~~~xml
<select id="findByName" resultType="com.itheima.reggie.domain.Employee">
    select * from employee
    <where>
        <if test="name!=null and name!=''">
            name like CONCAT('%',#{name},'%')
        </if>
    </where>
</select
~~~

![image-20220621115531719](assets\image-20220621115531719.png)

## 4.访问拦截

### （1）问题分析

> 目前已经完成了后台系统的员工登录功能，但是还存在一个问题，那就是用户如果不登录，直接访问系统首页，照样可以正常访问。 
>
> 这种设计明显不合理，我们希望看到的是，只有登录成功后才可以访问系统中的页面，如果没有登录, 访问系统时会自动跳转到登录页面。
>
> 这可以使用者拦截器来实现，在拦截器中判断用户是否已经登录，如果没有登录则返回提示信息，跳转到登录页面即可。

![image-20210728000838992](assets\image-20210728000838992.png) 



![image-20211229114054341](assets\image-20211229114054341.png) 

### （2）功能实现

#### 前端代码

>前端已经定义好了一个拦截器，这个拦截器会在浏览器一端拦截请求和响应，所有的请求发送都会被这个拦截器拦截并处理
>
>现在我们只关注响应的一段，里面有个判断，逻辑时当返回的code=0并且msg=NOTLOGINT时，它会清空浏览器存储的员工信息，然后跳转登录页面

![image-20211103220606416](assets\image-20211103220606416.png) 

#### 开发拦截器

>在`reggie-web-manage`模块下创建`com.itheima.reggie.interceptor.LoginCheckInterceptor`

~~~java
package com.itheima.reggie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.ResultInfo;
import com.itheima.reggie.domain.Employee;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        return true;
    }
}
~~~

#### 配置拦截器

>在`reggie-web-manage`模块下的`com.itheima.reggie.config.ReggieWebMvcConfig`中添加拦截器的配置

~~~java
@Autowired
private LoginCheckInterceptor loginCheckInterceptor;

//设置拦截器
@Override
public void addInterceptors(InterceptorRegistry registry) {
    //定义放行路径
    ArrayList<String> urls = new ArrayList<>();
    urls.add("/backend/**");//管理系统静态资源
    urls.add("/error");//错误请求
    urls.add("/employee/login");//管理系统登录请求
    urls.add("/employee/logout");//管理系统退出请求

    //配置拦截器和路径
    registry.addInterceptor(loginCheckInterceptor)
        .addPathPatterns("/**")//拦截所有
        .excludePathPatterns(urls); //放行指定路径
}
~~~










