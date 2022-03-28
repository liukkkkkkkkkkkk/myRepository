package com.mashibing.cloudzuul.dao;

import com.mashibing.cloudzuul.entity.CommGrayRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 49178
 * @create 2022/3/17
 */
@Mapper
public interface CommGrayRuleCustomDao extends  CommGrayRuleDao{
    public CommGrayRule selectByUserId(String userId);
}
