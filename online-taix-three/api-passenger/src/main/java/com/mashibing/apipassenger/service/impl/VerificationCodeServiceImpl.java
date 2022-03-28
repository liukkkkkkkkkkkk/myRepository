package com.mashibing.apipassenger.service.impl;

import com.mashibing.apipassenger.service.ServiceSmsRestTemplateService;
import com.mashibing.apipassenger.service.VerificationCodeService;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.IdentityConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicevificationcode.VerificationCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author 49178
 * @create 2022/2/18
 */
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    RestTemplate restTemplate;


    @Autowired
    private ServiceSmsRestTemplateService serviceSmsRestTemplateService;


    @Override
    public ResponseResult send(String phoneNumber) {
        //调生成验证服务
        ResponseResult responseResult = generateCode(IdentityConstant.PASSENGER, phoneNumber);
        VerificationCodeResponse verifyResponse =null;
        if (CommonStatusEnum.FAIL.getCode()==responseResult.getCode()){
            return ResponseResult.fail("获取验证码失败！");
        }
        HashMap map =(HashMap) responseResult.getData();
        String code = String.valueOf(map.get("code")); //生成的验证码
        serviceSmsRestTemplateService.sendSMS(phoneNumber,code);
        return ResponseResult.success(String.format("验证码[%s]已发到手机上[%s]", code,phoneNumber));
    }


    //调用生成验证码的服务
    public ResponseResult generateCode(int identity, String phoneNumber) {
        String url = "http://service-verification-code/verify-code/generate/" + identity + "/" + phoneNumber;
        // ribbon负载均衡 @LoadBalanced
        ResponseResult result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(null, null), ResponseResult.class).getBody();
        return result;
    }
}
