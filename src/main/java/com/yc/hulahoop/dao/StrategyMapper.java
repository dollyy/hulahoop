package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.vo.StrategyVo;
import com.yc.hulahoop.vo.UserStrategyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Strategy record);

    int insertSelective(Strategy record);

    Strategy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Strategy record);

    int updateByPrimaryKey(Strategy record);

    List<Strategy> list(@Param("cityId") Integer cityId, @Param("duration") String duration);

    StrategyVo detail(Integer strategyId);

    int deleteByUserIdAndStrategyId(@Param("strategyId") Integer strategyId, @Param("userId") Integer userId);

    int updateByUserIdAndStrategyId(Strategy strategy);

    List<Strategy> searchByUsername(String username);

    List<Strategy> searchByStrategyName(String strategyName);

    List<UserStrategyVo> queryUserStrategy(int userId);

    int countUserStrategy(@Param("userId") Integer userId, @Param("cityId") Integer cityId);
}