package com.mashibing.apipassenger.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashibing.apipassenger.dao.CommonGrayDao;
import com.mashibing.apipassenger.entity.CommonGrayRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author 49178
 * @create 2022/3/7
 */
@RestController
@RequestMapping("/testCall")
public class TestCallServiceSmsController {
    @Value("${server.port}")
    private int port;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonGrayDao commonGrayDao;
    @RequestMapping("/test1")
    public String test1(){ //实际项目中进行灰动发布，可以通过token取到用户的信息，进行灰度
        String result = restTemplate.getForObject("http://service-sms/send/test", String.class);
        return result;
    }


    @RequestMapping("/test2")
    public Object test2(){//mybatis-plus测试
        return commonGrayDao.selectById(1);
    }
    @RequestMapping("/test3")
    public Object test3(){//mybatis-plus 使用Page对象进行分页 测试
        Page<CommonGrayRule> page = new Page<>(1, 1);
        QueryWrapper<CommonGrayRule> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",111);
        ////        1.创建一个page对象 封装分页数据
        //分页查询的方法 selectPage()
        Page<CommonGrayRule> result = commonGrayDao.selectPage(page, wrapper);
        System.out.println("total:"+result.getTotal());
        /**
         *     3.从Page对象中获取查询到的数据
         * getRecords() 获取查询结果
         * getTotal() 总条数
         * */
        return commonGrayDao.selectPage(page,wrapper);
    }
}
