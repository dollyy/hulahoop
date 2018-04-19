package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.UserBehaviour;

import java.util.List;

public interface UserBehaviourService {

    ServerResponse<List<UserBehaviour>> queryBehaviourByUserId(Integer userId);

    ServerResponse<List<Integer>> queryStrategyIdByUserId(Integer userId);

}
