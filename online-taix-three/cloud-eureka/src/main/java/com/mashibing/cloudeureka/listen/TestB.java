package com.mashibing.cloudeureka.listen;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TestB {
    public void fun(String str) {
        System.out.println(str);
    }

    public void printName() {
        System.out.println("b类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
    }
}
