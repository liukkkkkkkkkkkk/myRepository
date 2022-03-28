package com.mashibing.servicesms.controller;

import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;
import com.mashibing.servicesms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mashibing.internalcommon.dto.ResponseResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 49178
 * @create 2022/2/18
 */
@RestController
@RequestMapping("/send")
@Slf4j
public class SendContorller {
    @Autowired
    private SmsService smsService;

    @Value("${server.port}")
    private int port;

    @PostMapping("/sms-template")
    public ResponseResult send(@RequestBody SmsSendRequest request){
        JSONObject param = JSONObject.fromObject(request);

        log.info("/send/alisms-template   request："+param.toString());
        return smsService.sendSms(request);
    }

    @GetMapping("/test")
    public String test2(){
        return "service-sms: " +port;
    }
    @GetMapping("/getToken")
    public String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String str = "port:" + port + "token:" + token;
        System.out.println(str);
        return str;
    }

}
