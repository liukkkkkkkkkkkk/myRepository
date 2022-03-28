package com.mashibing.servicepay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 49178
 * @create 2022/3/25
 */
@RestController
public class MyController {
    @RequestMapping
    public String test(){
        return "ok";
    }


}
