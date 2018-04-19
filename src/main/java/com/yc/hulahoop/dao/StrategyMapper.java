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

    List<StrategyVo> recommendStrategies();

    List<StrategyVo> latestStrategies();

    List<StrategyVo> hottestStrategies();

    List<StrategyVo> list(@Param("cityId") Integer cityId, @Param("duration") String duration);

    StrategyVo detail(Integer strategyId);

    int deleteByUserIdAndStrategyId(@Param("userId") Integer userId, @Param("strategyList") List<Integer> strategyList);

    int deleteByAdmin(@Param("strategyIdList") List<Integer> strategyIdList);

    int updateByUserIdAndStrategyId(Strategy strategy);

    int updateForOrCollect(Strategy strategy);

    List<UserStrategyVo> queryUserStrategy(int userId);

    int countUserStrategy(@Param("userId") Integer userId, @Param("cityId") Integer cityId);

    List<String> selectAllDurations();

    List<StrategyVo> searchByAdmin(String content);

    int queryStrategyCount();
}