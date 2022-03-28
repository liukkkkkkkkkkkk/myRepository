package com.mashibing.servicepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJms
@EnableScheduling
@SpringBootApplication
public class ServicePayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePayApplication.class, args);
    }

}
