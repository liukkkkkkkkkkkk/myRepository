package com.mashibing.cloudzuul.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * comm_gray_rule
 * @author 
 */
@Data
public class CommGrayRule implements Serializable {
    private Integer id;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 用户id
     */
    private Integer userId;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "CommGrayRule{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", userId=" + userId +
                '}';
    }
}