package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Strategy;
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

    Strategy detail(Integer strategyId);

    int deleteByUserIdAndStrategyId(@Param("strategyId") Integer strategyId, @Param("userId") Integer userId);

    List<Strategy> searchByUsername(String username);

    List<Strategy> searchByStrategyName(String strategyName);

    List<Strategy> queryUserStrategy(int userId);

    List<Strategy> queryCollectionList(@Param("userId") int userId, @Param("cityId") Integer cityId);
}