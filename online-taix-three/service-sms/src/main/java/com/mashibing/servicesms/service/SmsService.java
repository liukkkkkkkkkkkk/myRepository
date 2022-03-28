package com.mashibing.servicesms.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;

/**
 * @author 49178
 * @create 2022/2/18
 */
public interface SmsService {
    ResponseResult sendSms(SmsSendRequest request);
}
