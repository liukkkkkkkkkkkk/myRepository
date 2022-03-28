package com.mashibing.apipassenger.service.impl;

import com.mashibing.apipassenger.service.ServiceSmsRestTemplateService;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicesms.SmsTemplateDto;
import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 49178
 * @create 2022/2/18  调用三方平台发短信服务给手机号发送验证码
 */
@Service
public class ServiceSmsRestTemplateServiceImpl implements ServiceSmsRestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseResult sendSMS(String phoneNumber, String code) {
        String http ="http://";
        String serviceName ="service-sms";
        String uri = "/send/sms-template";
        String url = http + serviceName + uri;
        SmsSendRequest smsSendRequest = new SmsSendRequest();
        String [] phoneNumbers =new String[]{phoneNumber};
        smsSendRequest.setReceivers(phoneNumbers);

        List<SmsTemplateDto> data =new ArrayList<>();
        SmsTemplateDto dto = new SmsTemplateDto();
        dto.setId("SMS_144145499");
        int templateSize =1;
        HashMap<String,Object> templateMap =new HashMap<>(1);
        templateMap.put("code",code);
        dto.setTemplateMap(templateMap);
        data.add(dto);
        smsSendRequest.setData(data);
        return restTemplate.postForEntity(url,smsSendRequest,ResponseResult.class).getBody();
    }
}
