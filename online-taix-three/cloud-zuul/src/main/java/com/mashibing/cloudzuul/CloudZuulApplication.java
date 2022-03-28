package com.mashibing.cloudzuul;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
public class CloudZuulApplication {

    public static void main(String[] args) {
        init();
        SpringApplication.run(CloudZuulApplication.class, args);
    }

    //生成令牌
    private static void init() {
        //所有限流规则的集合
        List<FlowRule> rules = new ArrayList();

        //创建限流规则
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("my_resource1"); //限流要保护的东西，资源名,相当于令牌桶
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);  //按qps限流
        flowRule.setCount(2);//一秒接2个请求
        rules.add(flowRule);

        FlowRule flowRule2 = new FlowRule();
        flowRule2.setResource("SentinelService.success"); //限流要保护的东西，资源名,相当于令牌桶
        flowRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);  //按qps限流
        flowRule2.setCount(2);//
        rules.add(flowRule2);
        FlowRuleManager.loadRules(rules);
    }


    @Bean
    public SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }
}
