package com.yc.hulahoop.controller;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.pojo.StrategyItem;
import com.yc.hulahoop.pojo.UserBehaviour;
import com.yc.hulahoop.service.StrategyItemService;
import com.yc.hulahoop.service.StrategyService;
import com.yc.hulahoop.service.UserBehaviourService;
import com.yc.hulahoop.service.UserService;
import com.yc.hulahoop.util.DBUtil;
import com.yc.hulahoop.vo.UserBehaviourVo;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
public class RecommendController {

    //1.基于用户的协同过滤
    private static List<RecommendedItem> userCFRecommend(Integer userId) {
        DataModel model;
        try {
            //根据文件建立数据模型
            File file=new File("D:/Peach/20170710s/apache-tomcat-7.0.42/bin/behaviours.csv");
            model = new FileDataModel(file);
            //计算用户相似度，使用基于皮尔逊相关系数计算相似度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            //选择邻居用户，使用NearestNUserNeighborhood选择临近的USER_NUM个用户
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(Const.USER_NUM, similarity, model);
            //确定相邻用户后，一个普通的user-based推荐器被构建
            //使用协同过滤算法中基于用户的推荐
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            //向userId推荐howMany个item
            return recommender.recommend(userId, Const.HOW_MANY);
        } catch (TasteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //2.1基于内容的推荐 -> 获取用户和物品相似度最高的物品id传入基于物品的协同过滤中
    private static int contentBased(Integer userId) {

        int i, j, x, y, k;    //循环中用到的参数
        int m = DBUtil.queryStrategyCount();    //攻略总数
        int n = DBUtil.queryUserCount();        //用户总数
        if (m == 0 || n == 0) {   //没有攻略/用户 无法推荐
            return -1;
        }

        //1.item profile
        //物品特征矩阵, 行:攻略总数   列:每个攻略所有的特征总数
        double[][] item = new double[m][Const.ITEM_FEATURE];
        List<StrategyItem> strategyItems = DBUtil.queryStrategyItem();
        if (strategyItems.size() == 0) {  //没有攻略信息
            return -1;
        }
        for (i = 0; i < strategyItems.size(); i++) {
            item[i][0] = strategyItems.get(i).getAreaHd();
            item[i][1] = strategyItems.get(i).getAreaHn();
            item[i][2] = strategyItems.get(i).getAreaHz();
            item[i][3] = strategyItems.get(i).getAreaHb();
            item[i][4] = strategyItems.get(i).getAreaXb();
            item[i][5] = strategyItems.get(i).getAreaXn();
            item[i][6] = strategyItems.get(i).getAreaDb();
            item[i][7] = strategyItems.get(i).getAreaOther();
        }

        //2.根据用户攻略评分为用户评分矩阵赋值
        //用户评分矩阵, 行:用户总数   列:攻略总数
        double[][] userScore = new double[n][m];

        //获取用户攻略评分,并为用户评分矩阵赋值 9 18
        List<UserBehaviourVo> records;
        for (i = 0; i < n; i++) {
            //初始化矩阵
            for (j = 0; j < m; j++) {
                userScore[i][j] = 0;
            }
            records = DBUtil.queryBehaviourVoByUserId(i + 1);
            if (records == null) {
                continue;
            }
            //为有评分的攻略赋值
            for (UserBehaviourVo record : records) {
                userScore[i][record.getOrder() - 1] = record.getPreference().doubleValue();
            }
        }
        RealMatrix scoreMatrix = new Array2DRowRealMatrix(userScore);    //用户评分矩阵
        RealMatrix itemMatrix = new Array2DRowRealMatrix(item);        //攻略特征矩阵
        //用户评分矩阵和攻略特征矩阵相乘获得用户对item的各个特征的评分，行：用户总数，列：item特征总数
        double[][] userItem = scoreMatrix.multiply(itemMatrix).getData();
/*        for(double[] d1 :userItem){
            for(double d2 : d1){
                System.out.print(d2+"       ");
            }
            System.out.println();
        }*/

        //3.n:Const.ITEM_FEATURE, m:Const.ITEM_FEATURE
        double numerator;               //用户Ui对攻略Ii的喜好程度
        double denominator1 = 0;        //用户Ui的长度
        double denominator2 = 0;        //攻略Ii的长度
        List<Double> userFeature;       //用户Ui对攻略特征的喜好程度
        List<Double> itemProfile;       //攻略Ii的特征分布
        List<UserBehaviour> recommends;        //用户Ui和所有攻略的相似度
        UserBehaviour recommend;    //推荐对象
        List<UserBehaviourVo> userRecord;
        boolean recordFlag;
        List<Integer> userItemFirst = new ArrayList<>();
        for (i = 0; i < userItem.length; i++) {
            recommends = new ArrayList<>();
            userFeature = new ArrayList<>();
            //获取用户Ui对攻略特征的喜好程度
            for (j = 0; j < userItem[i].length; j++) {
                userFeature.add(userItem[i][j]);
            }
            for (x = 0; x < item.length; x++) {
                //如果是用户评过分的攻略则不进行推荐,进行下一次循环
                recordFlag = false;
                userRecord = DBUtil.queryBehaviourVoByUserId(i + 1);
                for (UserBehaviourVo record : userRecord) {
                    if (record.getOrder() == x + 1) {
                        recordFlag = true;
                        break;
                    }
                }
                if (recordFlag) {
                    continue;
                }
                numerator = 0;
                itemProfile = new ArrayList<>();
                //获取攻略Ii的特征分布
                for (y = 0; y < item[i].length; y++) {
                    itemProfile.add(item[x][y]);
                }
                for (k = 0; k < userFeature.size(); k++) {
                    denominator1 = 0;
                    denominator2 = 0;
                    for (double feature : userFeature) {
                        denominator1 += feature * feature;
                    }
                    denominator1 = Math.sqrt(denominator1);
                    for (double profile : itemProfile) {
                        denominator2 += profile * profile;
                    }
                    denominator2 = Math.sqrt(denominator2);
                    numerator += userFeature.get(k) * itemProfile.get(k);
                }
                if(denominator1 == 0 || denominator2 == 0){
                    continue;
                }
                double score = numerator / denominator1 * denominator2;
                recommend = new UserBehaviour();
                recommend.setId(x + 1);
                recommend.setPreference(new BigDecimal(score));
                recommends.add(recommend);
            }
            //根据score排序
            Collections.sort(recommends);
            if(recommends.size() > 0){
                userItemFirst.add(recommends.get(0).getId());
            }
        }

        return userItemFirst.get(userId - 1);
    }

    //2.2基于物品的协同过滤
    private static List<RecommendedItem> itemCFRecommend(Integer userId) {
        DataModel model;
        try {
            //根据文件建立数据模型
            model = new FileDataModel(new File("D:/Peach/20170710s/apache-tomcat-7.0.42/bin/behaviours.csv"));
            //计算用户相似度，使用基于皮尔逊相关系数计算相似度
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
            //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于物品的协同过滤推荐
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
            //向userId推荐howMany个item
            Integer itemId = contentBased(userId);
            if (itemId == -1) {   //没有攻略不进行推荐
                return null;
            }
            return recommender.recommendedBecause(userId, itemId, Const.HOW_MANY);
        } catch (TasteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StringBuilder recommend(Integer userId) {
        System.out.println("userId----->"+userId);
        List<UserBehaviour> existBehaviour = DBUtil.queryBehaviourByUserId(userId);
        if (existBehaviour.size() == 0) {
            System.out.println("==========no record, nothing to recommend");
            return new StringBuilder("");
        }

        //1.基于用户的协同过滤
        List<RecommendedItem> userBasedRecommend = userCFRecommend(userId);

        //2.基于物品的协同过滤
        List<RecommendedItem> itemBasedRecommend = itemCFRecommend(userId);

        //3.混合两个推荐列表
        List<RecommendedItem> recommendedItems = new ArrayList<>();
        if ((userBasedRecommend != null)) {
            recommendedItems.addAll(userBasedRecommend);
        }
        if ((itemBasedRecommend != null)) {
            recommendedItems.addAll(itemBasedRecommend);
        }
        if (recommendedItems.size() == 0) {
            System.out.println("==========nothing to recommend");
            return new StringBuilder("");
        }

        //将已经有过评分的物品从list中移除
        //用户所有有记录行为的strategy_id
        List<Integer> strategyIdList = DBUtil.queryStrategyIdByUserId(userId);
        if (strategyIdList.size() > 0) {
            Iterator<RecommendedItem> iterator = recommendedItems.iterator();
            RecommendedItem recommendedItem;
            while (iterator.hasNext()) {
                recommendedItem = iterator.next();
                for (Integer integer : strategyIdList) {
                    if (integer == recommendedItem.getItemID()) {
                        iterator.remove();
                    }
                }
            }
        }

        //没有推荐列表
        if (recommendedItems.size() == 0) {
            return new StringBuilder("");
        }

        //将list排序
        //Collections.sort(recommendedItems);
        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println("===========" + recommendedItem.getItemID() + "," + recommendedItem.getValue());
        }
        //将strategy_id组合起来存进数据库
        int recommendSize = recommendedItems.size();
        int recommendLength = recommendSize > 10 ? Const.HOW_MANY : recommendSize;    //推荐列表不足10个则有多少推荐多少
        StringBuilder recommends = new StringBuilder("");
        for (int i = 0; i < recommendLength; i++) {
            recommends.append(recommendedItems.get(i).getItemID()).append(",");
        }
        if (recommends.length() > 0) {
            recommends.deleteCharAt(recommends.length() - 1);
        }
        System.out.println("recommendList->" + recommends);
        return recommends;
    }

    public static void main(String[] args){
        recommend(4);
    }
}
