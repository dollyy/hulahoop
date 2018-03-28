package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;
import org.springframework.web.multipart.MultipartFile;

public interface StrategyService {

    ServerResponse queryStrategyList(int pageNum, int pageSize, Integer cityId, String duration);

    ServerResponse updateStrategyList(int pageNum, Integer cityId, String duration);

    ServerResponse detail(Integer strategyId);

    ServerResponse add(Strategy strategy);

    ServerResponse delete(Integer userId, String strategyId);

    ServerResponse update(Strategy strategy);

    ServerResponse updateForOrCollect(Strategy strategy);

    ServerResponse search(String type, String val, int pageNum);

    ServerResponse queryUserStrategy(Integer userId);

    ServerResponse queryUserCollection(int userId, int pageNum, int pageSize, String orderBy);
}
