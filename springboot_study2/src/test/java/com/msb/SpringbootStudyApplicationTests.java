package com.msb;

import com.msb.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootStudyApplicationTests {

    @Autowired
    Person person;
    @Test
    void contextLoads() {

        System.out.println(person);
    }

}
