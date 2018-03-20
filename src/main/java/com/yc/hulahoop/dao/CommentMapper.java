package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Comment;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.vo.CommentVo;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentVo> listByStrategyId(Integer strategyId);

    User queryRequestUser(String parent);

    int querySequenceByAdd();

    int querySequenceByReply(String parent);

}