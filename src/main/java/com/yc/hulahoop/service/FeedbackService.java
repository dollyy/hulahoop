package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;

public interface FeedbackService {

    ServerResponse list(Integer pageNum);

    ServerResponse add(Integer userId, String content, Integer receiveId);

    ServerResponse reply(Integer userId, String content, Integer parent, Integer sequence,
                         Integer receiveId);

    ServerResponse detail(String level);

    ServerResponse search(String content, Integer pageNum);

    ServerResponse listByUser(Integer userId, Integer pageNum);

    ServerResponse queryNotice(Integer userId);

    ServerResponse updateFeedStatus(Integer responseId, String level);

}
