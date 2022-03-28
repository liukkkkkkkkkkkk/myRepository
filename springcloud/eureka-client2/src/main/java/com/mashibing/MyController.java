package com.mashibing;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
public class MyController {

    /*Consider defining a bean of type 'org.springframework.web.client.RestTemplate' in your configuration. */
    @Bean
    public RestTemplate myRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    // 开启负载均衡
    @LoadBalanced
    public RestTemplate myRestTemplate2(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.getInterceptors().add(new MyHttpInterceptor());
        return restTemplate;
    }

    @Autowired
    private DiscoveryClient client;

    @Autowired    //编译报错没关系，可以正常运行
    private EurekaClient eurekaClient;

    @Autowired
    //@Resource
    @Qualifier("myRestTemplate")
    private RestTemplate myRestTemplate;


    @Autowired
    @Qualifier("myRestTemplate2")
    private RestTemplate myRestTemplate2;


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("hello")
    public String getHello() {
        return "hello~~~~~";
    }

    @GetMapping("getInstances")
    public String getInstances() {
        List<String> services = client.getServices();
        services.forEach(e -> System.out.println(e));

        return "getInstances~~~~~";

    }

    @GetMapping("client2")
    public Object getclient2() {
        //有多少个服务提供方,provider01是服务名
        List<ServiceInstance> list = client.getInstances("provider01");
        list.forEach(e -> System.out.println(ToStringBuilder.reflectionToString(e)));
        return list;

    }

    @GetMapping("client3")
    public Object getclient3() {
        //查某个具体的服务
        // List<InstanceInfo> list = eurekaClient.getInstancesById("LAPTOP-BHT2CBOD:provider01:8081");
        // 使用服务名 ，找provider01服务下的服务列表
        List<InstanceInfo> list = eurekaClient.getInstancesByVipAddress("provider01", false);

        if (list.size() > 0) {
            InstanceInfo instanceInfo = list.get(0);
            if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP) {
                String hostName = instanceInfo.getHostName();
                int port = instanceInfo.getPort();
                String url = "http://" + hostName + ":" + port + "/hello";
                System.out.println("url:" + url);
                //采用restTemplate进行http运程调用
                String resp = myRestTemplate.getForObject(url, String.class);
                System.out.printf("resp[%s]", resp);
            }

        }
        return "client3";
    }


    @GetMapping("client4")
    public Object getclient4() {
        //Ribbon完成客户端的负载均衡，过滤掉了down的节点 loadBalancerClient调用了ribbon
        ServiceInstance instance = loadBalancerClient.choose("provider01");
        String host = instance.getHost();
        int port = instance.getPort();
        String url = "http://" + host + ":" + port + "/hello";
        System.out.println("url:" + url);
        //采用restTemplate进行http运程调用
        String resp = myRestTemplate.getForObject(url, String.class);
        System.out.printf("resp[%s]", resp);
        return resp;
    }

    @GetMapping("hi")
    public Object getclient4a() {
        //Ribbon完成客户端的负载均衡，过滤掉了down的节点 loadBalancerClient调用了ribbon
        ServiceInstance instance = loadBalancerClient.choose("provider01");
        String host = instance.getHost();
        int port = instance.getPort();
        String url = "http://" + host + ":" + port + "/hi";
        System.out.println("url:" + url);
        //采用restTemplate进行http运程调用
        String resp = myRestTemplate.getForObject(url, String.class);
        System.out.printf("resp[%s]", resp);
        return resp;
    }
    @GetMapping("client5")
    public Object getclient5() { //手动负载均衡
        List<ServiceInstance> list = discoveryClient.getInstances("provider01");
        int i = new Random().nextInt(list.size());
        ServiceInstance instance = list.get(i);
        return  "aaa";

    }


    @GetMapping("client6")
    public Object getclient6() {
        String url="http://provider01/client6";   //@loadbalanced接下来便可以使用资源地址调用服务,转化成真正的服务器地址
        String resp = myRestTemplate2.getForObject(url, String.class);

        return  resp;

    }


}
