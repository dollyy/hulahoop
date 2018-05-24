package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.StrategyItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyItem record);

    int insertSelective(StrategyItem record);

    StrategyItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyItem record);

    int updateByPrimaryKey(StrategyItem record);

    int deleteItem(@Param("strategyList") List<Integer> strategyList);

    List<StrategyItem> queryStrategyItem();

}