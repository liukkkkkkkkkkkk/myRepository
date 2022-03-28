package com.mashibing.apipassenger.request;

//import com.online.taxi.common.validation.PhoneNumberValidation;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class ShortMsgRequest {
    /**
     * 自定义 校验手机号的注解
     */
//    @PhoneNumberValidation

    /**
     * 通过 正则校验
     */
    @Pattern(message = "手机号校验不正确",regexp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$")
    private String phoneNumber;


}