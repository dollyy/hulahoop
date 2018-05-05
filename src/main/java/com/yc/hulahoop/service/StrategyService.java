package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.Strategy;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface StrategyService {

    ServerResponse indexInfo(Integer userId);

    ServerResponse queryStrategyList(int pageNum, int pageSize, Integer cityId, String duration);

    ServerResponse updateStrategyList(int pageNum, int pageSize, Integer cityId, String duration);

    ServerResponse detail(HttpServletRequest request, Integer userId, Integer strategyId);

    ServerResponse add(Strategy strategy);

    ServerResponse delete(Integer userId, String strategyId);

    ServerResponse update(Strategy strategy);

    ServerResponse updateForOrCollect(Integer userId, Strategy strategy, Integer status, String type);

    ServerResponse search(String content, int pageNum, int pageSize);

    ServerResponse queryUserStrategy(Integer userId);

    ServerResponse queryUserCollection(int userId, int pageSize, int pageNum, String orderBy);
}
