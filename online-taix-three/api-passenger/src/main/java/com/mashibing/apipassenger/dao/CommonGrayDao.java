package com.mashibing.apipassenger.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mashibing.apipassenger.entity.CommonGrayRule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 49178
 * @create 2022/3/21
 */
@Mapper
public interface CommonGrayDao extends BaseMapper<CommonGrayRule> {
}
