package com.mashibing.system.io.rpcdemo.rpc.services;

import java.io.Serializable;

/**
 * @author 49178
 * @create 2021/12/15
 */
public class Person implements Serializable {
    private int age ;
    private  String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
