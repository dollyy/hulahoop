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

    List<CollectionVo> queryCollectionList(@Param("userId") int userId, @Param("cityId") Integer cityId);
}