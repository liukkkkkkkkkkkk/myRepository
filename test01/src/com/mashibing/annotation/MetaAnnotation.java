package com.mashibing.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@MyAnnotaion(name = "ddd",age = 30,likes = {"book","read"})
public class MetaAnnotation {
    @MyAnnotaion
    public void test1(){

    }

}

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   //需要在什么级别保存该注释信息，描述注解的生命周期 Source < Class < Runtime 一般是运行时
@Documented //说明该注解将被包含在javadoc中
@Inherited //  说明子类可以继承父类中的该注解
@interface MyAnnotaion{
    //定义的方式看起来像是方法， 返回值类型就是参数的类型（返回值叧能是基本类型，Class,String,enum）
    // 其中的每一个方法实际上是声明了一个配置参数.实际上使用注解的时候填写参数的名称，默认的名称是value（使用时可以不写名称）
    //自定义注解里填写的所有方法都需要在使用注解时去添加值，可以给它加上默认值
    String name()default "zhangsan";
    int age() default 12;
    String[]likes() default {"a","b","c"};

}