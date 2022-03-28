package com.mahibing.serviceorder.service.impl;

import com.mahibing.serviceorder.dao.TblOrderEventDao;
import com.mahibing.serviceorder.entity.TblOrderEvent;
import com.mahibing.serviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 49178
 * @create 2022/3/25
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    TblOrderEventDao orderEventDao;

    @Override
    @Transactional
    /**
     进行服务内的业务逻辑处理，处理完存到事件表里，整个操作在一个本地事务中执行  事件表的消息等待后续通过MQ发送到其它服务消费
     */
    public void insert() {
        String msg = "\"goodsId\":\"1234556\",\"goodsPrice\":\"100.00\"";
        TblOrderEvent event = new TblOrderEvent();
        event.setContent(msg);
        event.setStatus(0);
        int i = orderEventDao.insertSelective(event);
        System.out.println("插入事件表成功！ " + msg);
    }
}
