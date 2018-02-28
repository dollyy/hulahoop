package com.yc.hulahoop.common;

/**
 * Created by ts on 18-2-28.
 */
public class Const {

    public static final String USERNAME="username";
    public static final String PHONE="phone";
    public static final String CURRENT_USER="currentUSer";

    public static final String ILLEGAL_PARAMETER="非法参数";
    public static final String DUPLICATE_PARAMETER="参数已存在";
    public static final String NOT_LOGIN="用户未登录";
    public static final String NO_USER="用户不存在";

    public enum ResponseCode {
        SUCCESS(1,"成功"),
        ERROR(0,"失败");

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