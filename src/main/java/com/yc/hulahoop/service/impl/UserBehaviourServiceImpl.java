package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.UserBehaviourMapper;
import com.yc.hulahoop.pojo.UserBehaviour;
import com.yc.hulahoop.service.UserBehaviourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userBehaviourService")
public class UserBehaviourServiceImpl implements UserBehaviourService {

    @Autowired
    UserBehaviourMapper userBehaviourMapper;

    @Override
    public ServerResponse<List<UserBehaviour>> queryBehaviourByUserId(Integer userId) {
        return ServerResponse.createBySuccessData(userBehaviourMapper.queryBehaviourByUserId(userId));
    }

    @Override
    public ServerResponse<List<Integer>> queryStrategyIdByUserId(Integer userId) {
        return ServerResponse.createBySuccessData(userBehaviourMapper.queryStrategyIdByUserId(userId));
    }
}
