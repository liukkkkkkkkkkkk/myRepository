package com.mashibing;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RefreshScope
public class MyController2 {


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
    private restService restService;


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    DiscoveryClient discoveryClient;

@Value("${server.port}")
String port;

@Value("${my.name}")
String name;

    @GetMapping("client7")
    public Map getclient6() {
        String url="http://provider01/client7";   //@loadbalanced接下来便可以使用资源地址调用服务,转化成真正的服务器地址
        Map map = myRestTemplate2.getForObject(url, Map.class);
        return  map;
    }

    @GetMapping("client8")
    public Person getclient8() {
        String url="http://provider01/client8";
        Person person = myRestTemplate2.getForObject(url, Person.class);
        return  person;
    }


    @GetMapping("client9/{name}/{age}")
    public Person client9(@PathVariable("name") String name,@PathVariable("age") int age){
        String url="http://provider01/client9?name={1}&&age={2}";
        Person person = myRestTemplate2.getForObject(url, Person.class, name, age);
        return person;
    }

    @GetMapping("client10/{name}/{age}")
    public String client10(@PathVariable("name") String name,@PathVariable("age") int age){
        String url="http://provider01/client10?name={name}";
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name",name);
       // map.put("age",age);
        Person person = myRestTemplate2.getForObject(url,Person.class,map);
        return "consumer1:port:"+ port+"--->"+person;
    }

    @GetMapping("client11")
    public void client11(HttpServletResponse response) throws IOException {
        String url ="http://provider01//client11";
        Map<String, String> map = Collections.singletonMap("name", " memeda");
        URI location = myRestTemplate2.postForLocation(url, map, Person.class);
        response.sendRedirect(location.toURL().toString());
        System.out.println(location);

    }


    @GetMapping("client12")
    public String client12() {
        String url="http://provider01/client12";   //@loadbalanced接下来便可以使用资源地址调用服务,转化成真正的服务器地址
        String s = myRestTemplate2.getForObject(url, String.class);
        return  s;

    }

    @GetMapping("client13")
    public String client13() {
        String url="http://provider01/client13";   //   hystrix整合RestTemplate
        String s = myRestTemplate2.getForObject(url, String.class);
        return  s;

    }
    @GetMapping("client14")
    public String client14(String name){
        String url="http://provider01/client14";   //   hystrix整合RestTemplate
        String s = myRestTemplate2.getForObject(url, String.class);
        return  "conusmer:"+port+"--->"+s;
    }
    @GetMapping("getPerson")
    public Person getPerson() {
        Person person =restService.getPerson();
        return  person;
    }

    @GetMapping("getConfig")
    public String getConfig() {
        return  name;
    }
}
