package com.mashibing.apilistenorder;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@RestController
@Slf4j
public class ApiListenOrderApplication {


    public static void main(String[] args) {
        SpringApplication.run(ApiListenOrderApplication.class, args);
    }

/*    @RequestMapping(value = "/listen/order/{driverId}",produces ="text/event-stream;charset=utf-8")
    public String listenOrder(@PathVariable int driverId){
        log.info("司机{}调用了听单的服务","["+driverId+"]");
        String orderId = String.valueOf(Math.random());
        log.info("司机听到的订单号为:{}",orderId);
        return "data:"+Math.random()+"\n\n";
    }*/

    @RequestMapping(value = "/listen/order/{driverId}",produces ="text/event-stream;charset=utf-8")
    public String driverListenOrder(@PathVariable int driverId) {
        System.out.println(String.format("司机{%s}听单", "[" + driverId + "]"));
        long orderId = new Random().nextLong();
        String json = JSONObject.fromObject(orderId).toString(); //这里字符串转成json是空的呀
        System.out.println("json：" + json);
        System.out.println("返回听到的单号：" + orderId);
//        报头"Content-Type"设置为"text/event-stream"
        //(3)输出发送的数据始终以："data: "开头,键必须为data 必须要，不然前台接收不到值（且数据一定要以两个换行符为结束）
        return "data:"+orderId+"\n\n";  //测试前端console.info(evt.data); 只接这种固定格式的字符串，json也不行

    }


}
