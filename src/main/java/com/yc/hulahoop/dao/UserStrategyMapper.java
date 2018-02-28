package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.UserStrategy;

public interface UserStrategyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserStrategy record);

    int insertSelective(UserStrategy record);

    UserStrategy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserStrategy record);

    int updateByPrimaryKey(UserStrategy record);
}