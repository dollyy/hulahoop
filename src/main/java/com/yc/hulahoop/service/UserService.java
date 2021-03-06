package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;

public interface UserService {

    ServerResponse<String> verify(String val, String type);

    ServerResponse<String> register(User user);

    ServerResponse login(String type, String val, String password);

    ServerResponse resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse updatePassword(String email, String password, String token);

    ServerResponse queryUserInformation(Integer userId);

    ServerResponse updateUserInformation(Integer userId, String username, String bio, String gender, String city);

    ServerResponse updateEmail(Integer userId, String email);

    ServerResponse updateAvatar(User user);

    ServerResponse adminLogin(String username, String password);

}