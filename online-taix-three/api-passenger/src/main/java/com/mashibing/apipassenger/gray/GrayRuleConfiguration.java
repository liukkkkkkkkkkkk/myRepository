package com.mashibing.apipassenger.gray;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 49178
 * @create 2022/3/7
 */
//@Configuration   可以省略@Configuration   因为注解@RibbonClient已经有配置  name = "service-sms" 只对调用特定的服务进行灰度
// @RibbonClient(name = "service-sms",configuration = GrayRuleConfiguration.class)
public class GrayRuleConfiguration {
    @Bean
    public IRule ribbonRule (){
        return new GrayRule();
    }
}
