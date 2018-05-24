package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.UserBehaviour;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface UserBehaviourMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBehaviour record);

    int insertSelective(UserBehaviour record);

    UserBehaviour selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBehaviour record);

    int updateByPrimaryKey(UserBehaviour record);

    BigDecimal existBehaviour(@Param("userId") Integer userId, @Param("strategyId") Integer strategyId);

    int updateByUserIdAndStrategyId(@Param("userId") Integer userId, @Param("strategyId") Integer strategyId,
                                    @Param("preference") BigDecimal preference);

    List<UserBehaviour> queryBehaviourByUserId(Integer userId);

    List<Integer> queryStrategyIdByUserId(Integer userId);

    int deleteItem(@Param("strategyList") List<Integer> strategyList);
}