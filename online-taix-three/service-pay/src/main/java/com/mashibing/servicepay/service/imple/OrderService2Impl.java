package com.mashibing.servicepay.service.imple;

import com.mashibing.servicepay.dao.TblOrderEvent2Dao;
import com.mashibing.servicepay.dao.entity.TblOrderEvent2;
import com.mashibing.servicepay.service.OrderService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 49178
 * @create 2022/3/25
 */
@Service
public class OrderService2Impl implements OrderService2 {
    @Autowired
    TblOrderEvent2Dao orderEvent2Dao;
    @Override
    public void insert(TblOrderEvent2 orderEvent) {
        orderEvent2Dao.insert(orderEvent);
    }
}
