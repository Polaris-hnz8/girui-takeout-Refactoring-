package com.itheima.reggie.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 监听器，监听session中验证码有效期
 */
@Component
@Slf4j
public class MessageListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("session创建了！监听器起作用了！");
    }

    /**
     * 监听向session中加入数据
     * @param sessionBindingEvent
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent sessionBindingEvent) {
        //1.获取当前监听的session
        HttpSession session = sessionBindingEvent.getSession();

        //2.获取监听到的session中本次存放的数据
        String key = sessionBindingEvent.getName();
        Object value = sessionBindingEvent.getValue();
        log.info("本次向session存放的数据：key：{},value：{}", key, value);

        //3.判断session中获取的数据是否为验证码（只对session中存储的验证码进行定时失效处理）
        if (key.startsWith("phone_sms")) {
//            //（1）创建定时器对象
//            Timer timer = new Timer();
//
//            //（2）创建定时器任务对象
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    session.removeAttribute(key);
//                }
//            };
//            （3）计时5分钟
//            timer.schedule(task,1000*60*5);

            //改成匿名内部类
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    String code = (String) session.getAttribute(key);
                    //先判断session是否还存在该验证码，不存在再失效
                    if (StrUtil.isNotBlank(code)) {
                        session.removeAttribute(key);
                        log.info("session中的验证码：{}失效了！", key);
                    }
                }
            //}, 1000 * 60 * 5);
            }, 1000 * 5);
        }
    }
}