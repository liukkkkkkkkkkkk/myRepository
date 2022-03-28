package com.user.userconsumer;

import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MainController {
    @Autowired
    public ConsumerApi userApi;

       @GetMapping("alive")
    public String alive(){

        return userApi.alive();
    }



    @GetMapping("xxx/{id}")
    public String xxx(@PathVariable("id") Integer id ){
        return userApi.getById(id);
    }

    @GetMapping("getMap1")
    public Map getMap1( String name, int age){
        System.out.println("name:"+name+ " age:"+age);
           return userApi.getMap1(name,age);
    }




       @GetMapping("getMap2")
       public Map getMap2(@RequestParam Map map){
           System.out.println(map);
           return userApi.getMap2(map);
       }


       @GetMapping("getMap3")
    public Map getMap3(@RequestParam Map map){
           System.out.println("getMap3"+ map);
           return userApi.getMap3(map);

       }
    @GetMapping("getMap4")
    public Map getMap4(@RequestParam Map map){
        System.out.println("getMap4"+ map);
        return userApi.getMap4(map);

    }

    @GetMapping("getPerson")
    public Person getPerson(@RequestParam Map map){
        System.out.println("getPerson"+ map);
        int id = Integer.parseInt(map.get("age").toString());
        String name = (String)map.get("name");
        Person person = new Person(id, name);
        return userApi.getPerson(person);
    }

    @GetMapping("testHystrix")
    public String testHystrix(){
     return  userApi.testHystrix();
    }
}
