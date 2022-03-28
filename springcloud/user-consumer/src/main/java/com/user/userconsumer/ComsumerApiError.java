package com.user.userconsumer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
@Component  //有问题
public class ComsumerApiError implements FallbackFactory<ConsumerApi> {
    @Override
    public ConsumerApi create(Throwable cause) {
        return new ConsumerApi() {
            @Override
            public Map getMap1(String name, int age) {
                System.out.println(cause);
                System.out.println(ToStringBuilder.reflectionToString(cause));
                return Collections.singletonMap("出问题了","出问题了") ;
            }

            @Override
            public Map getMap2(Map map) {
                return null;
            }

            @Override
            public Map getMap3(Map map) {
                return null;
            }

            @Override
            public Person getPerson(Person person) {
                return null;
            }

            @Override
            public String testHystrix() {
                System.out.println("aaaaaaaaaaaaa");
                System.out.println(cause);
                System.out.println(ToStringBuilder.reflectionToString(cause));
                return "出问题了" ;
            }

            @Override
            public String alive() {
                return null;
            }

            @Override
            public String getById(Integer id) {
                return null;
            }

            @Override
            public Map getMap4(Map map) {
                return null;
            }
        };
    }
}
