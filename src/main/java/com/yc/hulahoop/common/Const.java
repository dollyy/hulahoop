package com.yc.hulahoop.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Yang Chen on 18-2-28.
 */
public class Const {

    public static final String USERNAME = "username";
    public static final String PHONE = "phone";
    public static final String CURRENT_USER = "currentUSer";
    public static final String STRATEGY_NAME="strategyName";

    public static final String ILLEGAL_PARAMETER = "非法参数";
    public static final String NOT_LOGIN = "用户未登录";
    public static final String NO_USER = "用户不存在";
    public static final String NO_PHONE = "手机号不存在";
    public static final String NON_ADMIN="非管理员无权操作";
    public static final int PAGE_SIZE=10;

    public interface Role {
        int ADMIN = 1;
        int USER = 0;
    }

    public interface CommentType{
        String FOR="for";
        String AGAINST="against";
    }

    public interface StrategyOrderBy{
        //List contains的时间复杂度是O(n),Set contains的时间复杂度是O(1)
        Set<String> STRATEGY_ORDER= Sets.newHashSet("create_time.desc","create_time.asc","for_num.desc","for_num.asc"
                ,"collect_num.desc","collect_num.asc","city_id.desc","city_id.asc");
    }

    public enum ResponseCode {
        SUCCESS(1, "成功"),
        ERROR(0, "失败");

        private final int code;
        private final String description;

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        ResponseCode(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

}