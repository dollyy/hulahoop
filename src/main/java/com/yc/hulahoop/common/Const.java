package com.yc.hulahoop.common;

import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by Yang Chen on 18-2-28.
 */
public class Const {

    public static final int ADMIN_ID = 1;
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String CURRENT_USER = "currentUser";
    public static final String STRATEGY = "strategy";
    public static final String AVATAR = "avatar";
    public static final String HELPINFO = "helpInfo";
    public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final int USER_NUM = 5;            //邻居数
    public static final int ITEM_FEATURE = 8;    //物品的特征数
    public static final int HOW_MANY = 8;            //推荐的列表个数
    public static final int PAGE_SIZE = 12;
    public static final int PAGE_SIZE_ADMIN = 8;
    public static final int ADMIN_HELPINFO_PAGESIZE = 10;

    public interface Role {
        int ADMIN = 1;
        int USER = 0;
    }

    public interface UserBehaviour {
        BigDecimal BROWSE_SCORE = new BigDecimal(0.5);    //浏览一次加0.5分
        BigDecimal LIKE_SCORE = new BigDecimal(1);        //点赞加0.5分
        BigDecimal COLLECT_SCORE = new BigDecimal(1.5);   //收藏加1.5分
        BigDecimal BROWSE_TIME_SCORE = new BigDecimal(1.5);   //收藏加1.5分
        String OPERATION_ADD = "add";
        String OPERATION_SUBTRACT = "subtract";
    }

    public interface CommentType {
        String FOR = "for";
        String AGAINST = "against";
    }

    public interface StrategyOrderBy {
        //List contains的时间复杂度是O(n),Set contains的时间复杂度是O(1)
        Set<String> STRATEGY_ORDER = Sets.newHashSet("", "s.create_time|desc", "s.create_time|asc", "for_num|desc", "for_num|asc"
                , "collect_num|desc", "collect_num|asc", "city_id|desc", "city_id|asc");
    }

    public enum ResponseCode {
        SUCCESS(1, "成功"),
        ERROR(0, "失败"),
        NO_INFO(-1, "没有匹配信息"),
        NEED_LOGIN(-2, "用户未登录"),
        ILLEGAL_PARAMETER(-3, "非法参数"),
        NON_ADMIN(-4, "非管理员无权操作");

        private final Integer code;
        private final String description;

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        ResponseCode(Integer code, String description) {
            this.code = code;
            this.description = description;
        }
    }

}