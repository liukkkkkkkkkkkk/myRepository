package com.mashibing.cloudeureka.listen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 49178
 * @create 2022/1/26
 */

 class TestA {

    public void fun(String str) {
        System.out.println(str);
    }

    public void printName() {
        System.out.println("1类名 ：" + Thread.currentThread().getStackTrace()[1].getClassName());
    }
}

/*2.新建一个ImportConfig,在类上面加上@Configuration，加上@Configuration是为了能让Spring 扫描到这个类，并且直接通过@Import引入TestA类*/
@Import({TestA.class,TestB.class})
@Configuration
 class ImportConfig {
}
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportAnnotionTest {

    @Autowired
    TestA testA;

    @Autowired
    TestB testB;

    /*TestA 是一个普通的类，现在可以被@Autowired注释然后调用，就直接说明已经被Spring 注入并管理了，普通的类都是需要先实例化*/

    @Test
    public void TestA() {
        testA.printName();
    }

    /*测试结果
TestB.class 的类上面已经有了@Configuration注解,本身就会配spring扫到并实例，@import引入带有@Configuration的配置文件，是需要先实例这个配置文件再进行相关操作*/
    @Test
    public void TestB(){
        testB.printName();
    }

}








