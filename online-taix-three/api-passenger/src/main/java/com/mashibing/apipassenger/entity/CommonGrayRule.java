package com.mashibing.apipassenger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 49178
 * @create 2022/3/21
 */
@TableName("comm_gray_rule")
@Data
public class CommonGrayRule implements Serializable {

    private static final long serialVersionUID = -5743173401207530338L;

    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField("version")
    private String serviceName;
    @TableField("user_id")
    private String userId;



}
