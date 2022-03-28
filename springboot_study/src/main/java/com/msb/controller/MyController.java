package com.msb.controller;

import com.msb.filter.MyHttpSessionListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MyController {
      @RequestMapping("/hello")
    public String getHello(HttpSession session){
        session.setAttribute("name","msb");
        return "来自服务器1 ";
    }


    @RequestMapping("online")
    public String online(){
        return "当前在线人数："+ MyHttpSessionListener.online +"人";
    }

}
