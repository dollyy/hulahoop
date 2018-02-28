package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.ResourceInfo;

public interface ResourceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResourceInfo record);

    int insertSelective(ResourceInfo record);

    ResourceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResourceInfo record);

    int updateByPrimaryKey(ResourceInfo record);
}