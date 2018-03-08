package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.HelpInfo;

import java.util.List;

public interface HelpInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HelpInfo record);

    int insertSelective(HelpInfo record);

    HelpInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HelpInfo record);

    int updateByPrimaryKey(HelpInfo record);

    List<HelpInfo> list();
}