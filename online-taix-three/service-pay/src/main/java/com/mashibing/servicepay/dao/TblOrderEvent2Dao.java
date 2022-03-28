package com.mashibing.servicepay.dao;

import com.mashibing.servicepay.dao.entity.TblOrderEvent2;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TblOrderEvent2Dao {
    int deleteByPrimaryKey(Integer id);

    int insert(TblOrderEvent2 record);

    int insertSelective(TblOrderEvent2 record);

    TblOrderEvent2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TblOrderEvent2 record);

    int updateByPrimaryKey(TblOrderEvent2 record);

    List<TblOrderEvent2> selectByStatus(int status);
}