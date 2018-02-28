package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int verifyUsername(String username);

    int verifyPhone(String phone);

    User loginByUsername(String username, String password);

    User loginByPhone(String username, String phone);

    int verifyPassword(String passwordOld, int userId);
}