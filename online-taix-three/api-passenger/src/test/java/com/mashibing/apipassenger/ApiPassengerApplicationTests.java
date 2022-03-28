package com.mashibing.apipassenger;

import com.mashibing.apipassenger.dao.CommonGrayDao;
import com.mashibing.apipassenger.entity.CommonGrayRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiPassengerApplicationTests {

    @Autowired
    CommonGrayDao commonGrayDao;
    @Test
    void contextLoads() {
        CommonGrayRule commonGrayRule = commonGrayDao.selectById(1);
        System.out.println("result:"+commonGrayRule);
    }

}
