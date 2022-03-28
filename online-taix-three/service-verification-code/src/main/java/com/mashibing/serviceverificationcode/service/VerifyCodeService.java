package com.mashibing.serviceverificationcode.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicevificationcode.VerificationCodeResponse;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 49178
 * @create 2022/2/15
 */

public interface VerifyCodeService {
    /**
     * 根据身份和手机号生成验证码
     * @param identity
     * @param phoneNumber
     * @return
     */
    public ResponseResult<VerificationCodeResponse> generate(int identity, String phoneNumber);

    /**
     * 校验身份，手机号，验证码的合法性
     * @param identity
     * @param phoneNumber
     * @param code
     * @return
     */
    public ResponseResult verify(int identity,String phoneNumber,String code);
}