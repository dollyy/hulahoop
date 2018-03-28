package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.FeedbackInfo;

public interface FeedbackService {

    ServerResponse list(Integer pageNum);

    ServerResponse add(FeedbackInfo feedbackInfo);

    ServerResponse reply(FeedbackInfo feedbackInfo);

    ServerResponse detail(String level);

}
