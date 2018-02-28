package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Strategy;

public interface StrategyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Strategy record);

    int insertSelective(Strategy record);

    Strategy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Strategy record);

    int updateByPrimaryKey(Strategy record);
}