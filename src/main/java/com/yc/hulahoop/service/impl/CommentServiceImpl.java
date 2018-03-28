package com.yc.hulahoop.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.CommentMapper;
import com.yc.hulahoop.pojo.Comment;
import com.yc.hulahoop.pojo.User;
import com.yc.hulahoop.service.CommentService;
import com.yc.hulahoop.vo.CommentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public ServerResponse listByLevel(Integer level) {
        if (level == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        List<CommentVo> commentList = commentMapper.listByLevel(level+"%");
        //攻略没有评论
        if (commentList.size() == 0) {
            return ServerResponse.createBySuccessMessage("暂无信息");
        }
        //封装评论信息
        List<List<CommentVo>> comments = Lists.newArrayList();
        List<CommentVo> comment = null;
        for (CommentVo vo : commentList) {
            if (!vo.getLevel().contains(".")) {
                if (comment != null) {
                    comments.add(comment);
                }
                comment = Lists.newArrayList();
                comment.add(vo);
            } else {
                User user = commentMapper.queryRequestUser(vo.getParent());
                vo.setRequestId(user.getId());
                vo.setRequestName(user.getUsername());
                comment.add(vo);
            }
        }
        comments.add(comment);
        return ServerResponse.createBySuccessData(comments);
    }

    @Override
    public ServerResponse add(Comment comment) {
        int sequence = commentMapper.querySequenceByAdd() + 1;
        comment.setParent("0");
        comment.setSequence(sequence);
        comment.setLevel(String.valueOf(sequence));
        int count = commentMapper.insert(comment);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("添加评论成功");
        }
        return ServerResponse.createByErrorMessage("添加评论失败");
    }

    @Override
    public ServerResponse reply(Comment comment) {
        int sequence = commentMapper.querySequenceByReply(comment.getParent() + "._") + 1;
        comment.setSequence(sequence);
        comment.setLevel(comment.getParent() + "." + sequence);
        int count = commentMapper.insert(comment);
        if (count > 0) {
            Map<String, Object> result= Maps.newHashMap();
            result.put("level",comment.getLevel());
            result.put("id",comment.getId());
            return ServerResponse.createBySuccessData(result);
        }
        return ServerResponse.createByErrorMessage("回复评论失败");
    }

    @Override
    public ServerResponse forOrAgainstComment(Integer userId, Integer id, String type, Integer number) {
        if (userId == null || id == null || StringUtils.isBlank(type) || number == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userId);
        switch (type) {
            case Const.CommentType.FOR:
                comment.setForNum(number);
                break;
            case Const.CommentType.AGAINST:
                comment.setAgainstNum(number);
                break;
            default:
                return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        int count = commentMapper.updateByPrimaryKeySelective(comment);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }
}
