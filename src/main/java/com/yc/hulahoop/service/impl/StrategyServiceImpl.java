package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.CollectionMapper;
import com.yc.hulahoop.dao.StrategyMapper;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.service.StrategyService;
import com.yc.hulahoop.vo.CollectionVo;
import com.yc.hulahoop.vo.StrategyVo;
import com.yc.hulahoop.vo.UserStrategyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("strategyService")
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    StrategyMapper strategyMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Override
    public ServerResponse queryStrategyList(int pageNum, Integer cityId, String duration) {
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        //2.填充自己的sql查询逻辑
        List<Strategy> strategyList = strategyMapper.list(cityId, duration);
        //3.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer strategyId) {
        if (strategyId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //获取该攻略的详细信息
        StrategyVo strategyVo = strategyMapper.detail(strategyId);
        if (strategyVo == null) {
            return ServerResponse.createByErrorMessage("攻略不存在");
        }
        System.out.println(strategyVo.getContent());
        String[] contents=strategyVo.getContent().split(",");
        for(String str : contents){
            String[] items=str.split(":");
            System.out.println("--------"+items[0]+","+items[1]);
        }
        return ServerResponse.createBySuccessData(strategyVo);
    }

    @Override
    public ServerResponse add(Strategy strategy) {
        if (strategy == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //向数据库新增攻略信息
        int count = strategyMapper.insert(strategy);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("新增成功");
        }
        return ServerResponse.createByErrorMessage("新增失败");
    }

    @Override
    public ServerResponse delete(Integer strategyId, Integer userId) {
        if (strategyId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //删除时添加user_id防止横向越权
        int count = strategyMapper.deleteByUserIdAndStrategyId(strategyId, userId);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse update(Strategy strategy) {
        //禁止修改的字段
        strategy.setForNum(null);
        strategy.setCollectNum(null);
        strategy.setCreateTime(null);
        //防止横向越权
        int count = strategyMapper.updateByUserIdAndStrategyId(strategy);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse search(String type, String val, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Strategy> strategyList;
        //根据type选择搜索的类型
        switch (type) {
            case Const.USERNAME:    //按username来搜索
                strategyList = strategyMapper.searchByUsername("%" + val + "%");
                break;
            case Const.STRATEGY_NAME:   //按攻略名来搜索
                strategyList = strategyMapper.searchByStrategyName("%" + val + "%");
                break;
            default:    //错误的参数类型
                return ServerResponse.createByErrorMessage("参数类型错误");
        }
        if (strategyList.size() == 0) {
            return ServerResponse.createBySuccessMessage("没有匹配信息");
        }
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse queryUserStrategy(int userId) {
        List<UserStrategyVo> userStrategyVoList = strategyMapper.queryUserStrategy(userId);
        if (userStrategyVoList.size() == 0) {
            return ServerResponse.createBySuccessMessage("没有匹配信息");
        }
        //为count赋值
        int count;
        for (UserStrategyVo userStrategyVo : userStrategyVoList) {
            count = strategyMapper.countUserStrategy(userId, userStrategyVo.getCityId());
            userStrategyVo.setCount(count);
        }
        return ServerResponse.createBySuccessData(userStrategyVoList);
    }

    @Override
    public ServerResponse queryUserCollection(int userId, Integer cityId, int pageNum, int pageSize, String orderBy) {
        if (cityId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        PageHelper.startPage(pageNum, pageSize);
        //处理排序
        if (Const.StrategyOrderBy.STRATEGY_ORDER.contains(orderBy)) {
            String[] orders = orderBy.split(".");
            PageHelper.orderBy(orders[0] + " " + orders[1]);
        }
        List<CollectionVo> collectionVoList = collectionMapper.queryCollectionList(userId, cityId == 0 ? null : cityId);
        PageInfo pageInfo = new PageInfo(collectionVoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
