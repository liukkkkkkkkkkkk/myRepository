package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.request.UserAuthRequest;
import com.mashibing.apipassenger.service.AuthService;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 49178
 * @create 2022/2/18
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${server.port}")
    private int port;
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Validated UserAuthRequest userAuthRequest){
        String passengerPhone = userAuthRequest.getPassagerPhoneNumber();
        String code = userAuthRequest.getCode();
        return authService.auth(passengerPhone , code);
    }

    @GetMapping
    public String test(){
        return "test:"+port;
    }


}
