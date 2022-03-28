package com.mashibing.cloudzuul.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.mashibing.cloudzuul.entity.MashibingProperties;
import com.mashibing.cloudzuul.service.SentinelService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 49178
 * @create 2022/3/14
 */
@RestController
@RequestMapping("myController")
public class MyController {

    @Autowired
    private SentinelService sentinelService;


    @Autowired
    private MashibingProperties properties;

    @RequestMapping("/mycontroller/test")
    public String test(){
        return "to test controller";
    }

    @RequestMapping("/test1")
    public String test1(){
        System.out.println(properties.toString());
        return properties.toString();
    }

    @RequestMapping("/testSentinelService")
    public String testSentinelService(){
        return sentinelService.success();
    }




}
