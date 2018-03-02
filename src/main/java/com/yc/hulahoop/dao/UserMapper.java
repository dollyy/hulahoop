package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int verifyUsername(String username);

    int verifyPhone(String phone);

    User loginByUsername(@Param("username") String username, @Param("password") String password);

    User loginByPhone(@Param("phone") String phone, @Param("password") String password);

    int verifyPassword(@Param("passwordOld") String passwordOld, @Param("userId") int userId);
}