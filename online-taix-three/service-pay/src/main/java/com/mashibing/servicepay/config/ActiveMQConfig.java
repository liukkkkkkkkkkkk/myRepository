package com.mashibing.servicepay.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJcaListenerContainerFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;


/**
 * @author 49178
 * @create 2022/3/25
 */
@Configuration
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String url;

    /**
     * mq连接工厂
     * @param redeliveryPolicy
     * @return
     */
    @Bean
    public ActiveMQConnectionFactory connectionFactory(RedeliveryPolicy redeliveryPolicy){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return connectionFactory;
    }

    /**
     * 重发消息配置
     * @return
     */
    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        return new RedeliveryPolicy();
    }


    /**
     * 设置消息队列 确认机制
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory){
        DefaultJmsListenerContainerFactory containerFactory =new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        // 1: 自动确认（默认），2： 客户端手动确认，3：自动批量确认，4 事务提交并确认。
        containerFactory.setSessionAcknowledgeMode(2);  //客户端不确认收到消息，服务端会重新发送,默认重试6次，失败会将消息丢到死信队列
        return containerFactory;
    }
}
