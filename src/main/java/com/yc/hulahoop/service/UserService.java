package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.User;

public interface UserService {

    ServerResponse<String> verify(String val, String type);

    ServerResponse<String> register(User user);

    ServerResponse login(String type, String val, String password);

    ServerResponse resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse updatePassword(String passwordNew);

    ServerResponse queryUserInformation(Integer userId);

    ServerResponse updateUserInformation(User user);

    ServerResponse adminLogin(String username, String password);

}