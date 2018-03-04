package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.StrategyMapper;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.service.StrategyService;
import com.yc.hulahoop.vo.StrategyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("strategyService")
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    StrategyMapper strategyMapper;

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
        Strategy strategy = strategyMapper.detail(strategyId);
        if (strategy == null) {
            return ServerResponse.createByErrorMessage("攻略不存在");
        }
        return ServerResponse.createBySuccessData(strategy);
    }

    @Override
    public ServerResponse add(Strategy strategy) {
        if (strategy == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //为main_img赋值
        if (StringUtils.isNotBlank(strategy.getSubImg())) {
            String[] images = strategy.getSubImg().split(",");
            if (images.length > 0) {
                strategy.setMainImg(images[0]);
            }
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
        strategy.setId(null);
        strategy.setForNum(null);
        strategy.setCollectNum(null);
        strategy.setCreateTime(null);
        int count = strategyMapper.updateByPrimaryKeySelective(strategy);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse search(String type, String val, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Strategy> strategyList;
        switch (type) {
            case Const.USERNAME:
                strategyList = strategyMapper.searchByUsername("%" + val + "%");
                break;
            case Const.STRATEGY_NAME:
                strategyList = strategyMapper.searchByStrategyName("%" + val + "%");
                break;
            default:
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
        List<Strategy> strategyList = strategyMapper.queryUserStrategy(userId);
        if (strategyList.size() == 0) {
            return ServerResponse.createBySuccessMessage("没有匹配信息");
        }
        return ServerResponse.createBySuccessData(strategyList);
    }

    @Override
    public ServerResponse queryUseCollection(int userId, Integer cityId, int pageNum, int pageSize, String orderBy) {
        if (cityId == null || StringUtils.isBlank(orderBy)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        PageHelper.startPage(pageNum, pageSize);
        //处理排序
        if (Const.StrategyOrderBy.STRATEGY_ORDER.contains(orderBy)) {
            String[] orders = orderBy.split(".");
            PageHelper.orderBy(orders[0] + " " + orders[1]);
        }
        List<Strategy> strategyList = strategyMapper.queryCollectionList(userId, cityId == 0 ? null : cityId);
        PageInfo pageInfo=new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
