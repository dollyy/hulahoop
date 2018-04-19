package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.StrategyItem;

import java.util.List;

public interface StrategyItemService {

    ServerResponse<List<StrategyItem>> queryStrategyItem();

}
