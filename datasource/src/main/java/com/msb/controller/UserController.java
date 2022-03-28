package com.msb.controller;

import com.msb.annotation.DataSource;
import com.msb.entity.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    JdbcTemplate jdbcTemplate;



    @GetMapping("/local")
    @DataSource(value = DataSourceType.LOCAL)
    public String insertUserLocal(){
        String sql ="insert into user (username,sex) values('老李','男')";
        jdbcTemplate.update(sql);
        return "insert success";
    }


    @GetMapping("/remote")
    @DataSource(value = DataSourceType.REMOTE)
    public String insertUserRemote(){
        String sql ="insert into user (username,sex) values('老李','男')";
        jdbcTemplate.update(sql);
        return "insert success";
    }
}