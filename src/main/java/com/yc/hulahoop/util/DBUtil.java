package com.yc.hulahoop.util;

import com.google.common.collect.Maps;
import com.yc.hulahoop.controller.RecommendController;
import com.yc.hulahoop.pojo.StrategyItem;
import com.yc.hulahoop.pojo.UserBehaviour;
import com.yc.hulahoop.vo.UserBehaviourVo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBUtil {

    private static Connection conn;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/hulahoop", "root", "123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<UserBehaviourVo> queryBehaviourVoByUserId(Integer userId) {
        List<UserBehaviourVo> behaviours = new ArrayList<>();
        UserBehaviourVo userBehaviour;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_behaviours WHERE user_id = ? ORDER BY user_id, strategy_id");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userBehaviour = new UserBehaviourVo();
                userBehaviour.setId(rs.getInt(1));
                userBehaviour.setUserId(rs.getInt(2));
                Integer strategyId = rs.getInt(3);
                userBehaviour.setStrategyId(strategyId);
                Integer order = queryOrder(strategyId);
                userBehaviour.setOrder(order);
                userBehaviour.setPreference(rs.getBigDecimal(4));
                behaviours.add(userBehaviour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return behaviours;
    }

    private static int queryOrder(Integer id) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT id,(@rowNum:=@rowNum+1) AS rowNo FROM strategies, (SELECT (@rowNum :=0) ) b");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (id == rs.getInt(1)) {
                    return rs.getInt(2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //查询用户的行为记录
    public static List<UserBehaviour> queryBehaviourByUserId(Integer userId) {
        List<UserBehaviour> behaviours = new ArrayList<>();
        UserBehaviour userBehaviour;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_behaviours WHERE user_id = ? ORDER BY user_id, strategy_id");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userBehaviour = new UserBehaviour();
                userBehaviour.setId(rs.getInt(1));
                userBehaviour.setUserId(rs.getInt(2));
                userBehaviour.setStrategyId(rs.getInt(3));
                userBehaviour.setPreference(rs.getBigDecimal(4));
                behaviours.add(userBehaviour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return behaviours;
    }

    //查询攻略总数
    public static int queryStrategyCount() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM strategies");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //查询用户总数
    public static int queryUserCount() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //查询攻略特征
    public static List<StrategyItem> queryStrategyItem() {
        List<StrategyItem> strategyItems = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM strategy_item");
            ResultSet rs = ps.executeQuery();
            StrategyItem strategyItem;
            while (rs.next()) {
                strategyItem = new StrategyItem();
                strategyItem.setId(rs.getInt(1));
                strategyItem.setStrategyId(rs.getInt(2));
                strategyItem.setAreaHd(rs.getInt(3));
                strategyItem.setAreaHn(rs.getInt(4));
                strategyItem.setAreaHz(rs.getInt(5));
                strategyItem.setAreaHb(rs.getInt(6));
                strategyItem.setAreaXb(rs.getInt(7));
                strategyItem.setAreaXn(rs.getInt(8));
                strategyItem.setAreaDb(rs.getInt(9));
                strategyItem.setAreaOther(rs.getInt(10));
                strategyItems.add(strategyItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strategyItems;
    }

    //在记录表中根据user_id查询所有攻略
    public static List<Integer> queryStrategyIdByUserId(Integer userId) {
        List<Integer> strategyIdList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT strategy_id FROM user_behaviours WHERE user_id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                strategyIdList.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strategyIdList;
    }


    public static void main(String[] args) {
        queryBehaviourByUserId(4);
    }

}
