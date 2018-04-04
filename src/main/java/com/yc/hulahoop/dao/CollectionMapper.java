package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Collection;
import com.yc.hulahoop.vo.CollectionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    List<CollectionVo> queryCollectionList(int userId);

    int isCollected(@Param(value = "userId") Integer userId, @Param(value = "strategyId") Integer strategyId);

    int deleteByStrategyId(List<Integer> strategyIdList);

    int deleteByStrategyIdAndUserId(@Param(value = "userId") Integer userId, @Param(value = "strategyId") Integer strategyId);
}