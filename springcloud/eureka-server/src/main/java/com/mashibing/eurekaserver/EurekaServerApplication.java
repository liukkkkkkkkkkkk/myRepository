package com.mashibing.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.HashMap;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sss","abcde");
        Object sss = map.get("sss");
        sss ="abc";
        System.out.println(map.get("sss"));
    }

}
