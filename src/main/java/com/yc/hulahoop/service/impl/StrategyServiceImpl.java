package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.StrategyMapper;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("strategyService")
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    StrategyMapper strategyMapper;

    @Override
    public ServerResponse queryStrategyList(int pageNum, int cityId, String duration) {
        System.out.println("service" + pageNum + "," + cityId + "," + duration);
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        //2.填充自己的sql查询逻辑
        List<Strategy> strategyList = strategyMapper.list(cityId, duration);
        //3.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
