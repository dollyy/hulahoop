package com.yc.hulahoop.service.impl;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public ServerResponse listByLevel(Integer level) {
        if (level == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        List<CommentVo> commentList = commentMapper.listByLevel(level, level + ".%");
        //攻略没有评论
        if (commentList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //封装评论信息
        List<List<CommentVo>> comments = new ArrayList<>();
        List<CommentVo> comment = null;
        for (CommentVo vo : commentList) {
            if (!vo.getLevel().contains(".")) {
                if (comment != null) {
                    comments.add(comment);
                }
                comment = new ArrayList<>();
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
        if (comment == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //获取所有parent为0的评论的个数
        int sequence = commentMapper.querySequenceByAdd() + 1;
        comment.setSequence(sequence);
        //添加时的评论parent一定是0
        comment.setParent("0");
        //level=sequence.parent
        comment.setLevel(String.valueOf(sequence));
        //添加评论
        int count = commentMapper.insert(comment);
        //添加成功
        if (count > 0) {
            return ServerResponse.createBySuccessData(comment.getSequence());
        }
        //添加失败
        return ServerResponse.createByErrorMessage("添加评论失败");
    }

    @Override
    public ServerResponse reply(Comment comment) {
        if (comment == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //获取这条评论在同级中的sequence
        int sequence = commentMapper.querySequenceByReply(comment.getParent() + "._") + 1;
        comment.setSequence(sequence);
        //level=sequence.parent
        comment.setLevel(comment.getParent() + "." + sequence);
        //添加评论
        int count = commentMapper.insert(comment);
        //添加成功
        if (count > 0) {
            Map<String, Object> result = new HashMap<>();
            //返回这条评论的id和level
            result.put("level", comment.getLevel());
            result.put("id", comment.getId());
            return ServerResponse.createBySuccessData(result);
        }
        //添加失败
        return ServerResponse.createByErrorMessage("回复评论失败");
    }

    @Override
    public ServerResponse forOrAgainstComment(Integer userId, Integer id, String type, Integer number) {
        if (userId == null || id == null || StringUtils.isBlank(type) || number == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(userId);
        switch (type) {
            //操作 支持
            case Const.CommentType.FOR:
                comment.setForNum(number);
                break;
            //操作 反对
            case Const.CommentType.AGAINST:
                comment.setAgainstNum(number);
                break;
            default:
                return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                        Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //更新comment
        int count = commentMapper.updateByPrimaryKeySelective(comment);
        //更新成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        //更新失败
        return ServerResponse.createByErrorMessage("更新失败");
    }
}
