package com.koolearn.cloud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fn on 2016/8/23.
 */
public class CheckUtil {

    private static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    private static final String REGEX_EMAIL = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile( REGEX_MOBILE );
        Matcher m = p.matcher(mobiles);
        System.out.println("tel is "  + m.matches());
        return m.matches();
    }
    public static boolean isMail(String mail){
        Pattern p = Pattern.compile( REGEX_EMAIL );
        Matcher m = p.matcher(mail);
        System.out.println( "mail is " + m.matches()  );
        return m.matches();
    }
    public static void main( String[] args ){
        isMobileNO( "134456562122" );
        isMail("sdfh@12.cn");
    }

}
