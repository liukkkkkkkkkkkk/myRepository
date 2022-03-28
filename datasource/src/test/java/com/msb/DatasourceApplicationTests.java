package com.msb;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class DatasourceApplicationTests {

    @Autowired
    private DataSource dataSource;
    @Test
    void contextLoads() throws SQLException {
       /* System.out.println(dataSource.getClass());
        Connection con = dataSource.getConnection();
        System.out.println(con);
        System.out.println(con.getMetaData().getURL());
        con.close();
        */

        DruidDataSource druidDataSource = (DruidDataSource)dataSource;
        System.out.println(druidDataSource.getClass());
        System.out.println("getInitialSize:"+druidDataSource.getInitialSize());
    }

}
