package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.StrategyFor;
import org.apache.ibatis.annotations.Param;

public interface StrategyForMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyFor record);

    int insertSelective(StrategyFor record);

    StrategyFor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyFor record);

    int updateByPrimaryKey(StrategyFor record);

    int existRecord(@Param(value = "userId") Integer userId, @Param(value = "strategyId") Integer strategyId);

    int updateByUserIdAndStrategyId(@Param(value = "userId") Integer userId, @Param(value = "strategyId") Integer strategyId);
}