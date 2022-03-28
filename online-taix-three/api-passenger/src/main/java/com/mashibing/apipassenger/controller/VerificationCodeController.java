package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.request.ShortMsgRequest;
import com.mashibing.apipassenger.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mashibing.internalcommon.dto.ResponseResult;

import javax.xml.ws.Response;

/**
 * @author 49178
 * @create 2022/2/18
 */
@RestController
@RequestMapping("/verify-code")
public class VerificationCodeController {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @PostMapping("/send")
    public ResponseResult send(@RequestBody @Validated ShortMsgRequest request){
        return  verificationCodeService.send(request.getPhoneNumber());
    }
}
