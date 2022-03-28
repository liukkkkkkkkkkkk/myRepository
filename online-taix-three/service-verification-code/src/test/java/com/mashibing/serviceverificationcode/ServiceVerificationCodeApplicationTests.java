package com.mashibing.serviceverificationcode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceVerificationCodeApplicationTests {

    @Test
    void contextLoads() {
        for (int i = 0; i <10000 ; i++) {
            String substring = (Math.random() + "").substring(2, 8);
            System.out.println(substring);
        }
    }

}
