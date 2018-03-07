package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Comment;

public interface CommentService {

    ServerResponse list();

    ServerResponse add(Comment comment);

    ServerResponse reply(Comment comment);

    ServerResponse forOrAgainstComment(Integer userId, Integer id, String type, Integer number);
}
