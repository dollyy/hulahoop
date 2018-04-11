package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.*;
import com.yc.hulahoop.pojo.City;
import com.yc.hulahoop.pojo.Collection;
import com.yc.hulahoop.pojo.Strategy;
import com.yc.hulahoop.pojo.StrategyFor;
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

    @Autowired
    StrategyForMapper strategyForMapper;

    @Override
    public ServerResponse indexInfo(Integer userId) {
        Map<String, Object> result = Maps.newHashMap();
        //推荐列表
        if (userId != 0) {
            //查询用户的推荐列表
            //List<Integer> strategyId=Lists.newHashList();
            //String recommendList = userMapper.queryRecommendList(userId);
            //if(recommendList.contains(","){
            //  String[] recommends=recommendList.split(",");
            //  strategyId.addAll(recommends);
            //  List<StrategyVo> recommendStrategies = strategyMapper.recommendStrategies(recommends);
            //  if(recommendStrategies.size() > 0){
            //      result.put("recommend", recommendStrategies);
            //  }
            //}
            List<StrategyVo> recommendStrategies = strategyMapper.recommendStrategies();
            if (recommendStrategies.size() > 0) {
                result.put("recommend", recommendStrategies);
            }
        }
        //最新
        List<StrategyVo> latestStrategies = strategyMapper.latestStrategies();
        if (latestStrategies.size() > 0) {
            result.put("latest", latestStrategies);
        }
        //最热
        List<StrategyVo> hottestStrategies = strategyMapper.hottestStrategies();
        if (hottestStrategies.size() > 0) {
            result.put("hottest", hottestStrategies);
        }
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse queryStrategyList(int pageNum, int pageSize, Integer cityId, String duration) {
        Map<String, Object> result = Maps.newHashMap();
        //1.startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //获取所有的攻略
        List<StrategyVo> strategyList = strategyMapper.list(cityId, duration);
        if (strategyList.size() > 0) {
            //2.pageHelper--end
            PageInfo pageInfo = new PageInfo(strategyList);
            result.put("strategies", pageInfo);
        }

        //获取所有的cities
        List<City> cities = cityMapper.selectCities();
        if (cities.size() > 0) {
            result.put("cities", cities);
        }

        //获取所有的时长
        List<String> durations = strategyMapper.selectAllDurations();
        if (durations.size() > 0) {
            result.put("durations", durations);
        }
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse updateStrategyList(int pageNum, int pageSize, Integer cityId, String duration) {
        //1.startPage--start
        PageHelper.startPage(pageNum, pageSize);
        cityId = cityId == 0 ? null : cityId;
        duration = "全部".equals(duration) ? null : duration;
        List<StrategyVo> strategyList = strategyMapper.list(cityId, duration);
        //没有信息
        if (strategyList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse detail(Integer userId, Integer strategyId) {
        Map<String, Object> result = Maps.newHashMap();
        if (strategyId == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //获取该攻略的详细信息
        StrategyVo strategyVo = strategyMapper.detail(strategyId);
        //攻略不存在
        if (strategyVo == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //封装strategy对象
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
        //攻略对象
        result.put("strategy", strategyVo);
        //攻略目录
        result.put("catalog", catalog);
        //攻略内容
        result.put("content", strategyContent);
        //是否收藏
        int count = collectionMapper.isCollected(userId, strategyId);
        result.put("collect", count == 1);  //收藏返回true
        //是否点赞
        count = strategyForMapper.existRecord(userId, strategyId);
        result.put("for", count == 1);  //点赞返回true
        //封装comment对象
        List<CommentVo> commentVos = commentMapper.listByStrategy(strategyVo.getStrategyId());
        for (CommentVo commentVo : commentVos) {
            commentVo.setCommentCount(commentMapper.queryCommentCount(commentVo.getStrategyId(), commentVo.getLevel() +
                    ".%"));
        }
        result.put("comments", commentVos);
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse add(Strategy strategy) {
        if (strategy == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //为main_img赋值
        String mainImg = mainImgValue(strategy.getContent());
        //内容为空非法操作
        if (StringUtils.isBlank(mainImg)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        strategy.setMainImg(mainImg);
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
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        List<Integer> strategyList = Lists.newArrayList();
        System.out.println("================" + strategyId);
        //删除一/多个攻略
        if (strategyId.contains(",")) {   //删除多个
            String[] strategyIds = strategyId.split(",");
            for (String str : strategyIds) {
                strategyList.add(Integer.parseInt(str));
            }
        } else { //删除一个
            strategyList.add(Integer.parseInt(strategyId));
        }
        if (userId == Const.ADMIN_ID) { //管理员删除
            strategyMapper.deleteByAdmin(strategyList);
        } else {    //用户删除
            //删除时添加user_id防止横向越权
            strategyMapper.deleteByUserIdAndStrategyId(userId, strategyList);
        }
        for (Integer integer : strategyList) {
            System.out.println("==========" + integer);
        }
        //删除用户收藏
        int count = collectionMapper.deleteByStrategyId(strategyList);
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
        String mainImg = mainImgValue(strategy.getContent());
        //内容为空非法操作
        if (StringUtils.isBlank(mainImg)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        strategy.setMainImg(mainImg);
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
    public ServerResponse updateForOrCollect(Integer userId, Strategy strategy, Integer status, String type) {
        if (strategy == null) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //没有指定禁止修改的字段是因为在updateForOrCollect中只允许修改for或者against
        int count = strategyMapper.updateForOrCollect(strategy);
        //
        if ("for".equals(type)) {
            count = strategyForMapper.existRecord(userId, strategy.getId());
            System.out.println("===================" + count);
            if (count > 0) {  //更新
                count = strategyForMapper.updateByUserIdAndStrategyId(userId, status, strategy.getId());
            } else {  //新增
                StrategyFor strategyFor = new StrategyFor();
                strategyFor.setUserId(userId);
                strategyFor.setStrategyId(strategy.getId());
                strategyFor.setStatus(1);
                count = strategyForMapper.insert(strategyFor);
            }
        } else if ("collect".equals(type)) {
            if (status == 0) {  //取消收藏
                count = collectionMapper.deleteByStrategyIdAndUserId(userId, strategy.getId());
            } else {  //收藏
                Collection collection = new Collection();
                collection.setUserId(userId);
                collection.setStrategyId(strategy.getId());
                count = collectionMapper.insert(collection);
            }
        }
        //更新成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        //更新失败
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse search(String content, int pageNum, int pageSize) {
        if (StringUtils.isBlank(content)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //1.startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<StrategyVo> strategyList = strategyMapper.searchByAdmin("%" + content + "%");
        //没有匹配信息
        if (strategyList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(strategyList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse queryUserStrategy(Integer userId) {
        List<UserStrategyVo> userStrategyVoList = strategyMapper.queryUserStrategy(userId);
        if (userStrategyVoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
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
    public ServerResponse queryUserCollection(int userId, int pageSize, int pageNum, String orderBy) {
        //处理排序
        if (!Const.StrategyOrderBy.STRATEGY_ORDER.contains(orderBy)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        } else {
            //1.startPage--start
            PageHelper.startPage(pageNum, pageSize);
            //需要排序
            if (!StringUtils.isBlank(orderBy)) {
                String[] orders = orderBy.split("\\|");
                PageHelper.orderBy(orders[0] + " " + orders[1]);
            }
            List<CollectionVo> collectionVoList = collectionMapper.queryCollectionList(userId);
            //没有匹配信息
            if (collectionVoList.size() == 0) {
                return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                        Const.ResponseCode.NO_INFO.getDescription());
            }
            //获取用户有没有对攻略点赞
            int count;
            for (CollectionVo collectionVo : collectionVoList) {
                count = strategyForMapper.existRecord(userId, collectionVo.getId());
                collectionVo.setForStatus(count);
            }
            //2.pageHelper--end
            PageInfo pageInfo = new PageInfo(collectionVoList);
            return ServerResponse.createBySuccessData(pageInfo);
        }
    }

    //操作攻略主图
    private String mainImgValue(String content) {
        if (StringUtils.isNotBlank(content)) {
            int startLen = content.indexOf(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            if (startLen == -1) {   //攻略默认主图
                return PropertiesUtil.getProperty("ftp.strategy.default");
            } else {
                int endLen = content.indexOf(" ", startLen) - 1;
                return content.substring(startLen, endLen);
            }
        }
        return "";
    }
}