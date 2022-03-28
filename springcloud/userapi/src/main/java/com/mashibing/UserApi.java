package com.mashibing;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserApi {

@RequestMapping("/alive")
    public String alive();
}
