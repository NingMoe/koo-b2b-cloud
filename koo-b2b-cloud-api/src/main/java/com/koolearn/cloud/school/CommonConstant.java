package com.koolearn.cloud.school;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

/**
 * Created by fn on 2016/9/1.
 */
public class CommonConstant {

    public static final String AUTH_KEY = "cloud_school";
    public static final String MYSQL_DATASOURCE = "cloud";
    public static final String HOSTS_SSO= PropertiesConfigUtils.getProperty("hosts.sso");
    public static final String REPOSITORY_QUSTION_ID="repository_qustion_id_";//试题前缀
    public static final String SEPERATOR_ANSWER_AND="&";

    public static final int STATUS_0 = 0;
    public static final int STATUS_1 = 1;
    public static final int STATUS_2 = 2;
    public static final int TYPE_1 = 1;//l老师  2. 学生
    public static final int TYPE_2 = 2;//l老师  2. 学生

    public static final int QUESTION_SAVETYPE_SAVE=0;
    //试题响应类型
    public static final int QUESTION_RESPONSETYPE_NORMAL=0;//正常操作
    public static final int QUESTION_SAVETYPE_UPDATE=1;
    public static final int QUESTION_SAVETYPE_SAVEAS=2;
    /**
     * 五分失效时间
     */
    public static final int CACHE_300_SECEND= 60*5;
    /**
     * 登录用户uuid
     */
    public static final String getRegistVerifyImage(String sessionId) {
        return "registImageCode_"+sessionId;//session key:注册图片验证码
    }
    /**
     * 获取登录图片验证码key
     * @param sessionId
     * @return
     */
    public static final String getLoginVerifyImage(String sessionId){
        return "loginImageCode_"+sessionId;//session key:登录图片验证码
    }
}
