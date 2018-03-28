package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.CityMapper;
import com.yc.hulahoop.dao.CollectionMapper;
import com.yc.hulahoop.dao.CommentMapper;
import com.yc.hulahoop.dao.StrategyMapper;
import com.yc.hulahoop.pojo.City;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.service.StrategyService;
import com.yc.hulahoop.util.PropertiesUtil;
import com.yc.hulahoop.vo.CollectionVo;
import com.yc.hulahoop.vo.CommentVo;
import com.yc.hulahoop.vo.StrategyVo;
import com.yc.hulahoop.vo.UserStrategyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("strategyService")
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    StrategyMapper strategyMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    CityMapper cityMapper;

    @Autowired
    CommentMapper commentMapper;

    @Override
    public ServerResponse queryStrategyList(int pageNum, int pageSize, Integer cityId, String duration) {
        Map<String, Object> result = Maps.newHashMap();
        //获取所有的攻略
        //1.startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //2.填充自己的sql查询逻辑
        List<StrategyVo> strategyList = strategyMapper.list(cityId, duration);
        //3.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        result.put("strategies", pageInfo);

        //获取所有的cities
        List<City> cities = cityMapper.selectCities();
        result.put("cities", cities);

        //获取所有的时长
        List<String> durations = strategyMapper.selectAllDurations();
        result.put("durations", durations);
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse updateStrategyList(int pageNum, Integer cityId, String duration) {
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        //2.填充自己的sql查询逻辑
        List<StrategyVo> strategyList = strategyMapper.list(cityId, duration);
        //3.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer strategyId) {
        Map<String, Object> result = Maps.newHashMap();
        if (strategyId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //获取该攻略的详细信息
        StrategyVo strategyVo = strategyMapper.detail(strategyId);
        if (strategyVo == null) {
            return ServerResponse.createByErrorMessage("攻略不存在");
        }
        //strategy
        String content = strategyVo.getContent();
        String[] item;
        List<String> catalog = Lists.newArrayList();
        List<String> strategyContent = Lists.newArrayList();
        if (content.contains("#-")) {
            String[] contents = content.split("#-");
            for (String str : contents) {
                item = str.split("@#");
                catalog.add(item[0]);
                strategyContent.add(item[1]);
            }
        } else {
            item = content.split("@#");
            catalog.add(item[0]);
            strategyContent.add(item[1]);
        }
        catalog.add("评论");
        result.put("strategy", strategyVo);
        result.put("catalog", catalog);
        result.put("content", strategyContent);
        //comment
        List<CommentVo> commentVos = commentMapper.listByStrategy(strategyVo.getStrategyId());
        for (CommentVo commentVo : commentVos) {
            commentVo.setCommentCount(commentMapper.queryCommentCount(commentVo.getStrategyId(), commentVo.getLevel() + ".%"));
        }
        result.put("comments", commentVos);
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse add(Strategy strategy) {
        if (strategy == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //为main_img赋值
        String content = strategy.getContent();
        if (StringUtils.isNotBlank(content)) {
            int startLen = content.indexOf(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            if (startLen == -1) {
                strategy.setMainImg(PropertiesUtil.getProperty("ftp.strategy.default"));
            } else {
                int endLen = content.indexOf(" ", startLen) - 1;
                strategy.setMainImg(content.substring(startLen, endLen));
            }
        }
        //向数据库新增攻略信息
        int count = strategyMapper.insert(strategy);
        //新增成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("新增成功");
        }
        //新增失败
        return ServerResponse.createByErrorMessage("新增失败");
    }

    @Override
    public ServerResponse delete(Integer userId, String strategyId) {
        if (StringUtils.isBlank(strategyId)) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
        //删除1/多个攻略
        String[] strategyIds = strategyId.split(",");
        List<Integer> strategyList = Lists.newArrayList();
        for (String str : strategyIds) {
            strategyList.add(Integer.parseInt(str));
        }
        int count;
        if (userId == Const.ADMIN_ID) {
            count=strategyMapper.deleteByAdmin(strategyList);
        } else {
            //删除时添加user_id防止横向越权
            count = strategyMapper.deleteByUserIdAndStrategyId(strategyList, userId);
        }
        //删除成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        //删除失败
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @Override
    public ServerResponse update(Strategy strategy) {
        //禁止修改的字段
        strategy.setForNum(null);
        strategy.setCollectNum(null);
        strategy.setCreateTime(null);
        //为main_img赋值
        String content = strategy.getContent();
        if (StringUtils.isNotBlank(content)) {
            int startLen = content.indexOf(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            int endLen = content.indexOf(" ", startLen) - 1;
            strategy.setMainImg(content.substring(startLen, endLen));
        }
        //防止横向越权
        int count = strategyMapper.updateByUserIdAndStrategyId(strategy);
        //更新成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        //更新失败
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse updateForOrCollect(Strategy strategy) {
        int count = strategyMapper.updateForOrCollect(strategy);
        //更新成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        //更新失败
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse search(String type, String val, int pageNum) {
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        List<StrategyVo> strategyList;
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
    public ServerResponse queryUserStrategy(Integer userId) {
        if (userId == null) {
            return ServerResponse.createByErrorMessage(Const.ILLEGAL_PARAMETER);
        }
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
    public ServerResponse queryUserCollection(int userId, int pageNum, int pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        //处理排序
        if (Const.StrategyOrderBy.STRATEGY_ORDER.contains(orderBy)) {
            String[] orders = orderBy.split("\\|");
            PageHelper.orderBy(orders[0] + " " + orders[1]);
        }
        List<CollectionVo> collectionVoList = collectionMapper.queryCollectionList(userId);
        PageInfo pageInfo = new PageInfo(collectionVoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
