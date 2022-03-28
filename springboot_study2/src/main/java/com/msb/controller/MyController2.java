package com.msb.controller;

import com.msb.entity.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
public class MyController2 {

    @Value("${server.port}")
    private String port ;
    @RequestMapping("/abcd")
    public String getHello(String name,Integer age){
        System.out.println("port:"+port);
        System.out.println("name:"+name+" age:"+age);
        System.out.println("abcd");
        return "hello";
    }
    @RequestMapping("/hello2")
    public String getHello2(Model model){
        System.out.println("hello2");
        model.addAttribute("msg","来自model里的消息");
        return "hello2";  //根据视图解析器找页面
    }





}
