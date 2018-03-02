package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.FeedbackInfo;
import com.yc.hulahoop.pojo.User;

public interface UserService {

    ServerResponse<String> verify(String val, String type);

    ServerResponse<String> register(User user);

    ServerResponse login(String val, String password);

    ServerResponse resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse queryUserInformation(int userId);

    ServerResponse updateUserInformation(User user);

    ServerResponse feedback(FeedbackInfo feedbackInfo);

    ServerResponse reFeedback(FeedbackInfo feedbackInfo);

}