package com.mashibing.cloudzuul.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * @author 49178
 * @create 2022/3/23
 */
@Service
public class SentinelService {

    /**
     * 正常方法   容器必须注入 SentinelResourceAspect 切面类
     * @return
     */
    @SentinelResource(value = "SentinelService.success",blockHandler = "failMethod")
    public String success(){
        System.out.println("接口请求正常执行");
        return "successs return";
    }

    /**
     * 阻塞住时走的方法
     * @param e  必须要BlockException参数
     * @return
     */
    public String failMethod(BlockException e){
        System.out.println("请求被限流了，走的fail方法返回");
        return "请求被限流了，走的fail方法返回";
    }
}
