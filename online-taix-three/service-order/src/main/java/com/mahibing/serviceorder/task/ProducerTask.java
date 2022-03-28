package com.mahibing.serviceorder.task;

import com.mahibing.serviceorder.dao.TblOrderEventDao;
import com.mahibing.serviceorder.entity.TblOrderEvent;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;

/**
 * @author 49178
 * @create 2022/3/25
 */
@Component
public class ProducerTask {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private TblOrderEventDao eventDao;

    @Autowired
    private Queue queue;

    /**
     每隔5秒执行一次    从事件表里取未处理的数据，推送到MQ里
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void sendQueueTask() {
        System.out.println("执行定时任务推送.....");
        List<TblOrderEvent> tblOrderEvents = eventDao.selectByStatus(0);
        for (TblOrderEvent event : tblOrderEvents) {
            eventDao.updateStatusByPrimaryKey(event);
            String msg = JSONObject.fromObject(event).toString();
            System.out.println("msg:"+msg);
            jmsMessagingTemplate.convertAndSend(queue,msg);
            System.out.println("推送消息成功:"+event.toString());
        }
    }





}
