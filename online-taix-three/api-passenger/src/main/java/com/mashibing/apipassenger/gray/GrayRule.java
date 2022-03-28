package com.mashibing.apipassenger.gray;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 49178
 * @create 2022/3/7
 */

public class GrayRule extends AbstractLoadBalancerRule {

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(),key);
    }

    /**
     * 根据规则选出后端服务
     * @param lb
     * @param key
     * @return
     */
    public Server choose(ILoadBalancer lb,Object key){
        List<Server> servers = lb.getReachableServers();
        Server server =null;
        HashMap map  = RibbonParameters.get();
        String version = (String)map.get("version");
        for (Server ser:servers){
            Map<String, String> metadata = ((DiscoveryEnabledServer) ser).getInstanceInfo().getMetadata();
            if (StringUtils.isNotBlank(version)&& version.trim().equals(metadata.get("version"))){
                System.out.println("找到version对应的服务了");
                // 服务的meta也有了，用户的version也有了。
                server =ser;
                return server;
            }

        }
        //返回没有找到对应灰度规则的服务，这里先简化返回
        return  servers.get(0);
    }
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
