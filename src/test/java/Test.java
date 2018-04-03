import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.util.PropertiesUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static void main(String[] args) throws MessagingException {
        String phone="yangchen302@163.com";
        Pattern pattern=Pattern.compile(REGEX_EMAIL);
        Matcher matcher=pattern.matcher(phone);
        System.out.println(matcher.matches());
    }
}
