package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;

public interface StrategyService {

    ServerResponse queryStrategyList(int pageNum, Integer cityId, String duration);

    ServerResponse detail(Integer strategyId);

    ServerResponse add(Strategy strategy);

    ServerResponse delete(Integer strategyId, Integer userId);

    ServerResponse update(Strategy strategy);

    ServerResponse search(String type, String val, int pageNum, int pageSize);

    ServerResponse queryUserStrategy(int userId);

    ServerResponse queryUseCollection(int userId, Integer cityId,int pageNum, int pageSize, String orderBy);
}
