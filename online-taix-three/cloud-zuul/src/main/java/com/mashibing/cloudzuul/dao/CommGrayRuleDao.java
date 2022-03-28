package com.mashibing.cloudzuul.dao;

import com.mashibing.cloudzuul.entity.CommGrayRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommGrayRuleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CommGrayRule record);

    int insertSelective(CommGrayRule record);

    CommGrayRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommGrayRule record);

    int updateByPrimaryKey(CommGrayRule record);

    List<CommGrayRule> getGrayRuleByUserId(int userId);
}