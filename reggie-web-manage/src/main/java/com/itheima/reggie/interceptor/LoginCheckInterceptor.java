package com.itheima.reggie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.reggie.common.Constant;
import com.itheima.reggie.common.EmployeeHolder;
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
        //当前处于登录状态，把当前用户放到threadlocal中，方便本次访问时，serviec与mapper使用
        EmployeeHolder.set(employee);

        // 3.session有员工，放行
        return true;
    }

    //本次访问结束前，去掉本次访问存储的threalocal中
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 服务器响应前，删除线程内map集合
        EmployeeHolder.remove();
    }
}