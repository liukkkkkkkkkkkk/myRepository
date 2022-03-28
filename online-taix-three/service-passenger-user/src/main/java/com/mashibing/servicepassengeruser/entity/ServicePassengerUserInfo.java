package com.mashibing.servicepassengeruser.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**

 */
/**
 * service_passenger_user_info
 * @author 49178
 * @create 2022/2/21
 * */
@Data
public class ServicePassengerUserInfo implements Serializable {
    private static final long serialVersionUID = 549399922591364436L;
    private Long id;
    private Date registerDate;
    private String passengerPhone;

    /**
     * 乘客姓名
     */
    private String passengerName;
    /**
     * 性别。1：男，0：女
     */
    private Byte passengerGender;

    /**
     * 用户状态：1：有效，0：失效
     */
    private Byte userState;
    private Date createTime;
    private Date updateTime;


}
