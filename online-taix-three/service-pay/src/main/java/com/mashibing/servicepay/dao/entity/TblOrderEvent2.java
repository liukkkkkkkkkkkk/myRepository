package com.mashibing.servicepay.dao.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tbl_order_event2
 * @author 
 */
@Data
public class TblOrderEvent2 implements Serializable {
    private Integer id;

    /**
     * 事件状态 0.初始态未处理   1.处理完成
     */
    private Integer status;

    /**
     * 事件环节（new,published,processed)
     */
    private String process;

    /**
     * 事件内容
     */
    private String content;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}