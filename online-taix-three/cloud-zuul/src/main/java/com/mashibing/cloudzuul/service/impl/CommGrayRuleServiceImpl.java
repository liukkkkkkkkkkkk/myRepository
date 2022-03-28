package com.mashibing.cloudzuul.service.impl;

import com.mashibing.cloudzuul.service.CommGrayRuleService;
import com.mashibing.cloudzuul.dao.CommGrayRuleDao;
import com.mashibing.cloudzuul.entity.CommGrayRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 49178
 * @create 2022/3/5
 */
@Service
public class CommGrayRuleServiceImpl implements CommGrayRuleService {

    @Autowired
    private CommGrayRuleDao commGrayRuleDao;
    @Override
    public List<CommGrayRule> getGrayRuleByUserId(int userId) {
        return commGrayRuleDao.getGrayRuleByUserId(userId);
    }
}
