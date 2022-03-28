package com.mashibing.cloudzuul.service;

import com.mashibing.cloudzuul.entity.CommGrayRule;

import java.util.List;

/**
 * @author 49178
 * @create 2022/3/5
 */
public interface CommGrayRuleService {
   public List<CommGrayRule> getGrayRuleByUserId(int userId);
}