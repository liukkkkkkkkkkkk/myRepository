package com.mashibing.cloudzuul.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 49178
 * @create 2022/3/14
 */
@Component
@ConfigurationProperties(prefix = "com.mashibing")
@PropertySource("classpath:application.yaml")
@Data
public class MashibingProperties {
    @Value("${name}")
    private String name;

    @Value("${hobby}")
    private String hobby;

}
