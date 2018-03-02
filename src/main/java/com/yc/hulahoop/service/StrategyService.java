package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;

public interface StrategyService {

    ServerResponse queryStrategyList(int pageNum, int cityId, String duration);

}
