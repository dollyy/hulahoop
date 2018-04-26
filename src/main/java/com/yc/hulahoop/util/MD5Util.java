package com.yc.hulahoop.util;

import java.security.MessageDigest;

public class MD5Util {

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (byte bytes : b) {
            resultSb.append(byteToHexString(bytes));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 返回大写MD5
     *
     * @param origin      需要加密的字符串
     * @param charsetName 字符集
     * @return 大写MD5加密后的密码
     */
    public static String MD5Encode(String origin, String charsetName) {
        //salt：盐值，增加MD5破解的难度
        origin = origin + PropertiesUtil.getProperty("password.salt", "");
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {    //使用默认字符集
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString.toUpperCase();
    }

}
