package com.msb.controller;

import com.msb.annotation.DataSource;
import com.msb.entity.DataSourceType;
import com.msb.entity.User;
import com.msb.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JdbcController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;
    @GetMapping("insertUser")
    public String insertUser(){
        String sql ="insert into user (username,sex) values('老李','男')";
        jdbcTemplate.update(sql);
        return "insert success";
    }

/*    @GetMapping("insert/{id}")
    @Transactional
    public String insertUser2(@PathVariable int id){
        User user = new User("sadfasf", "man", "1999");
        System.out.println("id:"+id);
        userMapper.insert(user);
        int j =0;
       // int x =10/j;
        return "insert success";
    }*/


    @GetMapping("insertLocal")
    @Transactional
    @DataSource(DataSourceType.LOCAL)
    public String insertUserLocal(){
        User user = new User("sadfasf", "man", "2000");
        userMapper.insert(user);
        return "insert local success";
    }




    @GetMapping("insertRemote")
    @Transactional
    @DataSource(DataSourceType.REMOTE)
   public String insertUserRemote(){
            User user = new User("sadfasf", "男", "2000");
            userMapper.insert(user);
            return "insert remote success";
            }


}
