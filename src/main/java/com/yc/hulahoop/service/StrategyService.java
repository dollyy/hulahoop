package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;
import org.springframework.web.multipart.MultipartFile;

public interface StrategyService {

    ServerResponse queryStrategyList(int pageNum, int pageSize, Integer cityId, String duration);

    ServerResponse updateStrategyList(int pageNum, int pageSize, Integer cityId, String duration);

    ServerResponse detail(Integer userId, Integer strategyId);

    ServerResponse add(Strategy strategy);

    ServerResponse delete(Integer userId, String strategyId);

    ServerResponse update(Strategy strategy);

    ServerResponse updateForOrCollect(Strategy strategy);

    ServerResponse search(String content, int pageNum, int pageSize);

    ServerResponse queryUserStrategy(Integer userId);

    ServerResponse updateForStatus(Integer userId, Integer strategyId, Integer status);

    ServerResponse updateCollectStatus(Integer userId, Integer strategyId, Integer status);

    ServerResponse queryUserCollection(int userId, int pageSize, int pageNum, String orderBy);
}
