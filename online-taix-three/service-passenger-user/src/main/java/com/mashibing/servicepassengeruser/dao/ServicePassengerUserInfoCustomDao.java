package com.mashibing.servicepassengeruser.dao;

import com.mashibing.servicepassengeruser.entity.ServicePassengerUserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 49178
 * @create 2022/2/21
 */
@Mapper
public interface ServicePassengerUserInfoCustomDao extends  ServicePassengerUserInfoDao {
    /**
     * 根据手机号查询乘客信息
     * @param passengerPhone
     * @return
     */
    ServicePassengerUserInfo selectByPhoneNumber(String passengerPhone);
}
