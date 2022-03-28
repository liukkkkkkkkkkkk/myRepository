package com.mashibing.cloudeureka.listen;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 49178
 * @create 2022/1/26
 */
@Component
public class TestEvent {

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event){//发邮件
        System.out.println("下线㕸："+event.getAppName()+"--"+event.getServerId());
    }
    public static void main(String[] args) {
        Timer timer = new Timer("tt");
        TimerTask timerTask =new TimerTask() {
            @Override
            public void run() {
                System.out.println("sssadf");
            }
        };
        timer.schedule(timerTask,1);
    }
}
