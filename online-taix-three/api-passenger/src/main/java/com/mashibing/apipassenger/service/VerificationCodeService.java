package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

/**
 * @author 49178
 * @create 2022/2/18
 */
public interface VerificationCodeService {
    ResponseResult send(String phoneNumber);
}
