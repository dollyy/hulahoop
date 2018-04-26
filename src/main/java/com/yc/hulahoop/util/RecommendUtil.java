package com.yc.hulahoop.util;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.pojo.StrategyItem;
import com.yc.hulahoop.pojo.UserBehaviour;
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

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RecommendUtil {

    //混合推荐
    public static StringBuilder recommend(Integer userId) {
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
        return recommends;
    }

    //1.基于用户的协同过滤
    private static List<RecommendedItem> userCFRecommend(Integer userId) {
        DataModel model;
        try {
            //根据文件建立数据模型
            File file = new File("D:/behaviours.csv");
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

        int i, j;   //循环中用到的参数
        int strategyCount = DBUtil.queryStrategyCount();    //攻略总数
        int userCount = DBUtil.queryUserCount();        //用户总数
        if (strategyCount == 0 || userCount == 0) {   //没有攻略/用户 无法推荐
            return -1;
        }

        //1.建立物品特征矩阵, 行:攻略总数, 列:攻略特征总数
        double[][] itemProfile = new double[strategyCount][Const.ITEM_FEATURE];
        List<StrategyItem> strategyItems = DBUtil.queryStrategyItem();
        if (strategyItems.size() == 0) {  //没有攻略信息
            return -1;
        }
        //为物品特征矩阵赋值
        for (i = 0; i < strategyItems.size(); i++) {
            itemProfile[i][0] = strategyItems.get(i).getAreaHd();
            itemProfile[i][1] = strategyItems.get(i).getAreaHn();
            itemProfile[i][2] = strategyItems.get(i).getAreaHz();
            itemProfile[i][3] = strategyItems.get(i).getAreaHb();
            itemProfile[i][4] = strategyItems.get(i).getAreaXb();
            itemProfile[i][5] = strategyItems.get(i).getAreaXn();
            itemProfile[i][6] = strategyItems.get(i).getAreaDb();
            itemProfile[i][7] = strategyItems.get(i).getAreaOther();
        }

        //2.建立用户评分矩阵, 行:用户总数, 列:攻略总数
        double[][] userScore = new double[userCount][strategyCount];
        //为用户评分矩阵赋值
        List<UserBehaviourVo> records;
        for (i = 0; i < userCount; i++) {
            //初始化矩阵, 赋值0
            for (j = 0; j < strategyCount; j++) {
                userScore[i][j] = 0;
            }
            records = DBUtil.queryBehaviourVoByUserId(i + 1);   //根据用户id查找用户行为记录
            if (records.size() == 0) {  //如果没有记录就进行下一次循环
                continue;
            }
            //为有评分的攻略赋值
            for (UserBehaviourVo record : records) {
                userScore[i][record.getOrder() - 1] = record.getPreference().doubleValue();
            }
        }

        //3.用户评分矩阵 和 物品特征矩阵 相乘获得 用户特征矩阵, 行:用户总数, 列:攻略特征总数
        RealMatrix scoreMatrix = new Array2DRowRealMatrix(userScore);       //用户评分矩阵
        RealMatrix itemMatrix = new Array2DRowRealMatrix(itemProfile);             //物品特征矩阵
        double[][] userProfile = scoreMatrix.multiply(itemMatrix).getData();

        //4.用户特征矩阵 和 物品特征矩阵 计算cos求得 用户对物品的喜好程度
        /*
         * U1=(a1, a2, a3)    I1=(b1, b2, b3)
         * cos<U1, I1> = (a1b1 + a2b2 + a3b3) / Math.sqrt(a1*a1 + a2*a2 + a3*a3) * Math.sqrt(b1*b1 + b2*b2 + b3*b3)
         */
        double numerator;               //分子, 用户Ui和攻略Ii的乘积
        double denominator1;            //分母, 用户Ui的长度
        double denominator2;            //分母, 攻略Ii的长度
        double[][] userItem = new double[userCount][strategyCount];   //用户对物品的喜好程度
        for (i = 0; i < userProfile.length; i++) {
            numerator = 0;
            denominator1 = 0;
            denominator2 = 0;
            for (j = 0; j < userProfile[i].length; j++) {
                numerator += userProfile[i][j] * itemProfile[i][j];
                denominator1 += userProfile[i][j] * userProfile[i][j];
                denominator2 += itemProfile[i][j] * itemProfile[i][j];
                if (denominator1 == 0 || denominator2 == 0 || numerator == 0) {
                    userItem[i][j] = 0;
                } else {
                    userItem[i][j] = numerator / denominator1 * denominator2;
                }
            }
        }

        //5.将已有的行为置0
        for (i = 0; i < userItem.length; i++) {
            //根据用户id查找用户行为记录
            records = DBUtil.queryBehaviourVoByUserId(i + 1);
            if (records.size() == 0) {
                continue;
            }
            //将已有的行为置0
            for (UserBehaviourVo record : records) {
                userItem[i][record.getOrder() - 1] = 0;
            }
        }

        //6.获取每个用户喜欢程度最高的物品的order
        List<Integer> itemId = new ArrayList<>();
        double max;
        int index;
        boolean flag;
        for (i = 0; i < userItem.length; i++) {
            flag = false;
            max = userItem[i][0];
            index = 0;
            for (j = 0; j < userItem[i].length; j++) {
                if (userItem[i][j] == 0) {
                    continue;
                }
                flag = true;
                if (userItem[i][j] > max) {
                    max = userItem[i][j];
                    index = j;
                }
            }
            itemId.add(flag ? index : -1);
        }

        //7.根据order获得物品的id
        List<Integer> strategyId=DBUtil.queryStrategy();
        int userItemId=itemId.get(userId-1);
        return userItemId == -1 ? -1 : strategyId.get(userItemId);
    }

    //2.2基于物品的协同过滤
    private static List<RecommendedItem> itemCFRecommend(Integer userId) {
        DataModel model;
        try {
            //根据文件建立数据模型
            model = new FileDataModel(new File("D:/behaviours.csv"));
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

}