package com.mashibing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MyController {
    @Value("${server.port}")
    String port;

    private AtomicInteger count =new AtomicInteger();


    @Autowired
    private HealthStatusService healthStatusService;
    @GetMapping("hello")
    public String getHello() {
        return "hello~~~~~8081";
    }

    @GetMapping("health")
    public String health(@RequestParam("status")boolean status) {
        healthStatusService.setStatus(status);
        return String.format("status is %s", status);
    }
    @GetMapping("client6")
    public String getclient6() {
        return "client6~~~~~:"+port;
    }


    @GetMapping("hi")
    public String getHi() {
        return "hi~~~~~:"+port;
    }

    @GetMapping("client7")
    public Map getClient() {
        return Collections.singletonMap("1111","aaaa");
    }

    @GetMapping("client8")
    public Person getPerson(){
        return new Person("张三",20);
    }
    @GetMapping("client9")
    public Person client9(String name,int age){
        return new Person(name,age);
    }

    @GetMapping("client10")
    public Person client10(String name){
        System.out.println("from this");

        return new Person(name,11,port);
    }

    @PostMapping("client11")
    public URI postParam(@RequestBody Person person, HttpServletResponse response) throws Exception {

        URI uri = new URI("https://www.baidu.com/s?wd=" + person.getName().trim());
        response.addHeader("Location", uri.toString());
        return uri;
    }


    @GetMapping("client12")
    public String getClient12() throws InterruptedException {
        Thread.sleep(4000);
        count.getAndIncrement();
        String s ="第"+ count+"次调用，"+"from prot:"+port;
        System.out.println(s);
        return "第"+count+"次调用，"+"from prot:"+port;
    }

    @GetMapping("client13")
    public String getClient13()  { ///Hystrix熔断
       int i=0;
        int s = 3 / i;
        return "aaa";
    }

    @GetMapping("client14")
    public String client14(String name){
        System.out.println("from this");

        return "provider:"+port;
    }
}
