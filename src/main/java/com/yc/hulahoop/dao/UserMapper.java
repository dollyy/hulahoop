package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int verifyUsername(String username);

    int verifyEmail(String email);

    User loginByUsername(@Param("username") String username, @Param("password") String password);

    User loginByEmail(@Param("email") String email, @Param("password") String password);

    int verifyPassword(@Param("passwordOld") String passwordOld, @Param("userId") int userId);

    User adminLogin(@Param("username") String username, @Param("password") String password);

    int existAdmin();

    int updatePasswordByToken(@Param("email") String email, @Param("password") String password);

    int updateEmail(@Param("email") String email, @Param("userId") Integer userId);

    int updateRecommend(@Param("userId") Integer userId, @Param("email") StringBuilder email);

    String queryRecommendByUerId(@Param("userId") Integer userId);

}