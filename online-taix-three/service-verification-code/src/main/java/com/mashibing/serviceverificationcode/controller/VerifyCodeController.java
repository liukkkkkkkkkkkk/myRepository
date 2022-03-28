package com.mashibing.serviceverificationcode.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.service.verifycode.request.VerifyCodeRequest;
import com.mashibing.serviceverificationcode.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;

/**
 * @author 49178
 * @create 2022/2/15
 */
@RestController
@Slf4j
@RequestMapping("/verify-code")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

   @GetMapping("/generate/{identity}/{phoneNumber}")
    public ResponseResult generate(@PathVariable("identity")int identity,@PathVariable("phoneNumber")String phoneNumber){
       return  verifyCodeService.generate(identity,phoneNumber);
   }


   @PostMapping("/verify")
   public ResponseResult verify(@RequestBody VerifyCodeRequest request) {
       log.info("/verify-code/verify  request:"+ JSONObject.fromObject(request));
       //获取手机号和验证码
       String phoneNumber = request.getPhoneNumber();
       int identity = request.getIdentity();
       String code = request.getCode();
       return verifyCodeService.verify(identity,phoneNumber,code);


   }
}
