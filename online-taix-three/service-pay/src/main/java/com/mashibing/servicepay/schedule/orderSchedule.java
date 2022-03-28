package com.mashibing.servicepay.schedule;

import com.mashibing.servicepay.dao.TblOrderEvent2Dao;
import com.mashibing.servicepay.dao.entity.TblOrderEvent2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 49178
 * @create 2022/3/25
 */
@Component
public class orderSchedule {

    @Autowired
    private TblOrderEvent2Dao event2Dao;
    /**
     每隔5秒执行一次    从本地事件表里取未处理的数据，进行处理 事件表里的数据由消息队列里取出插入
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void dealOrder(){
        List<TblOrderEvent2> list = event2Dao.selectByStatus(0);
        list.forEach(e->
        {
            System.out.println("定时任务处理数据:"+e);
            e.setStatus(2);
            event2Dao.updateByPrimaryKey(e);
        });
    }
}
