package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.Comment;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.vo.CommentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentVo> listByLevel(String level);

    List<CommentVo> listByStrategy(Integer strategyId);

    User queryRequestUser(String parent);

    int querySequenceByAdd();

    int querySequenceByReply(String parent);

    int queryCommentCount(@Param(value = "strategyId") Integer strategyId, @Param(value = "level") String level);

}