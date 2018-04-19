package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.StrategyItemMapper;
import com.yc.hulahoop.pojo.StrategyItem;
import com.yc.hulahoop.service.StrategyItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("strategyItemService")
public class StrategyItemServiceImpl implements StrategyItemService {

    @Autowired
    StrategyItemMapper strategyItemMapper;

    @Override
    public ServerResponse<List<StrategyItem>> queryStrategyItem() {
        return ServerResponse.createBySuccessData(strategyItemMapper.queryStrategyItem());
    }
}
