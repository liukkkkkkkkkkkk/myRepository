package com.user.userconsumer;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.userapi.UserApi;
import feign.Param;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//@FeignClient(name="xxxx",url = "http://localhost:8086")
@FeignClient(name="user-provider",fallback = MyHistrix.class)
public interface ConsumerApi extends UserApi {

/*    @GetMapping("/alive")
    public String alive();*/

  @GetMapping("getMap1")
    public Map getMap1(@RequestParam String name, @RequestParam int age);

    //远程调用时传map
    @GetMapping("getMap2")
    public Map getMap2(@RequestParam  Map map);


    //post方式远程调用
    @PostMapping("getMap3")
    public Map getMap3(@RequestBody Map map);


  @PostMapping("getPerson")
  public Person getPerson( Person person);


  @GetMapping("testHystrix")
  public String testHystrix();

 /* public String myfallback(){
    System.out.println("服务走的降级的");
    return "服务降级了";
  }*/

}
