package com.mashibing.servicepay.consumer;

import com.mashibing.servicepay.dao.TblOrderEvent2Dao;
import com.mashibing.servicepay.dao.entity.TblOrderEvent2;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author 49178
 * @create 2022/3/25
 *
 */
@Component
public class ConsumerQueue {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private TblOrderEvent2Dao event2Dao;
    //从消息队列里接收消息
    @JmsListener(destination="myActiveMQ",containerFactory = "jmsListenerContainerFactory")
    private void receive(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            System.out.println("消费者收到消息:"+text);
            TblOrderEvent2 event2 = (TblOrderEvent2)JSONObject.toBean(JSONObject.fromObject(text), TblOrderEvent2.class);
            event2Dao.insertSelective(event2);   //重点 通过事件表的主键id唯一性，来保证消息不会重复消费
            int i = 1 / 0;
            // 业务完成，确认消息 消费成功，从当前队列里移除 ，mq如果6次重试还没有收到消费成功的ack,从队列移除，消息放到死信队列，后期对死信队列做补偿处理
            textMessage.acknowledge();//确认消息已消费了，从当前队列里移除
        } catch (Exception e) {
            session.recover();         //无法消费，把消息恢复到消息队列里去  RedeliveryPolicy 需配置重发消息
            System.out.println("消费消息抛异常啦");
            e.printStackTrace();
            //正式项目中，log.error记录日志，不能使用  e.printStackTrace();
        }
    }

    /**
     * 默认重试6次消费失败，没给出ack,异常消息进入死信队列
     * 补偿 处理（人工，脚本）。记录到日志，redis/数据库等等。自己根据自己情况对业务异常数据做处理。
     * @param text
     */
    @JmsListener(destination = "DLQ.myActiveMQ")
    public void dealDLQQueue(String text){
        System.out.println("从死信队列里处理消息："+text);
        //如果补偿处理还异常，记录异常
    }
}
