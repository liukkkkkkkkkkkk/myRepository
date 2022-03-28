package com.mashibing.apiconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author 49178
 * @create 2022/3/8
 */
@RestController
@RequestMapping("/testCall")
public class CallSmsServiceController {

    @Autowired
    @Qualifier("getRestTemplate")
    private RestTemplate restTemplate;

    @RequestMapping("/test1")
    public String test1() {
        String result = restTemplate.getForObject("http://service-sms/send/test", String.class);
        return result;

    }
}
