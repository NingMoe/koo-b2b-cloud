package com.koolearn.cloud.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Pattern;
import javax.net.ssl.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.common.entity.RedisKey;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.sso.entity.DomainConnect;
import com.koolearn.sso.service.IUserService;
import com.koolearn.sso.util.CommonUtil;
import com.koolearn.sso.util.ConstantCommon;
import com.koolearn.sso.util.ErrCode;
import com.koolearn.sso.util.SSOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @Description:功能描述
 * @author chenlei
 * @date 2013-12-24
 * @version 1.0
 */
public class SsoUtil {
	private static Log logger = LogFactory.getLog(SsoUtil.class);
    private static final Pattern emailer = Pattern.compile("^[\\w-._]+@[\\w-]+(\\.[\\w-]+)*(\\.([a-zA-Z]){2,3})$");

    private static final Pattern mobiler = Pattern.compile("^(1[0-9][0-9])\\d{8}$");

    private static final Pattern passworder = Pattern.compile("^[^ ]{6,16}$");

    private static final Pattern md5Pass32 = Pattern.compile("^[0-9a-f]{32}$");

    //不是从QQ小钱包注册的用户将会被验证长度以及合法性?
    //注册时用户名验证
    private static final Pattern userNamePurseer = Pattern.compile("[A-Za-z\u4e00-\u9fa5][A-Za-z0-9\u4e00-\u9fa5_\\-]{1,15}");

    private static final Pattern userNameLibraryPurseer = Pattern.compile("[A-Za-z\u4e00-\u9fa5][A-Za-z0-9\u4e00-\u9fa5_\\-]{1,59}");

    private static final Pattern hexPattern = Pattern.compile("[A-Fa-f0-9]+");

    private static final Pattern userNamePurseerEnterprise = Pattern.compile("[A-Za-z\u4e00-\u9fa5][A-Za-z0-9\u4e00-\u9fa5_\\-]{1,25}");

	/**
	 * 图片验证码正确性验证
	 * @param request
	 * @return
	 */
	public static void checkEmailCode(String randomCode,String sessionId){
		String regCode = CacheTools.getCache(RedisKey.getRegistVerifyImage(sessionId), String.class);
		if(StringUtils.isBlank(randomCode)||!randomCode.equalsIgnoreCase(regCode)){
			throw new SSOException("regCode.error");
		}
	}
	
    /**
	 * 检查手机验证码是否正确
	 * @param request
	 */
	public static void checkMobileCode(String randomCode,String mobile){
		// check手机验证码
		if (StringUtils.isBlank(randomCode)) {
			throw new SSOException(ErrCode.ERROR_REGCODE_NULL);
		}
		//rstatus=1为成功，rstatus=0验证码超时 rstatus=-1验证失败
		int rstatus = verifyMobileRandomCode(randomCode, mobile,300);
		if (rstatus == ConstantCommon.MOBILE_RANDOM_VALIDATION_TIMEOUT) {
			throw new SSOException(ErrCode.ERROR_REGCODE_OUTTIME);
		} else if (rstatus == ConstantCommon.MOBILE_RANDOM_VALIDATION_FAIL) {
			throw new SSOException(ErrCode.ERROR_REGCODE_NOTEXISTS);
		}
	}
	
    /**
     * 验证手机验证码
     * @param randomCode
     * @param mobile
     * @param timeout
     * @return
     */
    public static int verifyMobileRandomCode(String randomCode,String mobile,int timeout){
        if(logger.isDebugEnabled()){
            logger.debug("verifyMobileCode, code:"+randomCode+", mobile:"+mobile+", timeout:"+timeout);
        }
        if(StringUtils.isNotBlank(randomCode) && StringUtils.isNotBlank(mobile) && CommonUtil.isMobile(mobile)){
            String regCode = CacheTools.getCache(RedisKey.mobileVerifyCode(mobile), String.class);
            if (StringUtils.isNotBlank(regCode)) {
                String[] regCodeArr = regCode.split(",");
                if(regCodeArr != null && regCodeArr.length > 1){
                    if(randomCode.equals(regCodeArr[0])){
                        long createTime = Long.valueOf(regCodeArr[1]).longValue();
                        long now = System.currentTimeMillis();
                        if(timeout<30){
                            timeout = ConstantCommon.MOBI_REGCODE_TIMEOUT;
                        }
                        if (now - createTime < timeout * 1000) {
                            return ConstantCommon.MOBILE_RANDOM_VALIDATION_SUCCESS;
                        } else {
                            return ConstantCommon.MOBILE_RANDOM_VALIDATION_TIMEOUT;
                        }
                    }else{
                        logger.error("code invalid");
                    }
                }else{
                    logger.error("regCode format invalid");
                }
            }else{
                logger.error("regCode is null");
            }

        }else{
            logger.error("param blank");
        }
        return ConstantCommon.MOBILE_RANDOM_VALIDATION_FAIL;
    }
    
    /**
     * 判断str是否是合法email
     * @title: isEmail
     * @description:
     * @param str
     * @return
     * @throws
     */
    public static boolean isEmail(String str){
        if(str == null){
            return false;
        }
        String email = str.toLowerCase();
        return emailer.matcher(email).matches();
    }

    public static boolean isMobile(String str) {
        if(str == null){
            return false;
        }
        return mobiler.matcher(str).matches();
    }

    /**
     * 从一个给定的string中随机拿出length个char拼成一个随机串
     *
     * @param genModel
     * @param length
     * @return
     */
    public static String randomString(String genModel, int length) {
        int genModelLength = genModel.length();
        if (length < 1 || genModelLength < 1 || length > genModelLength) {
            return null;
        }

        Random randGen = new Random();
        char[] numbersAndLetters = (genModel).toCharArray();

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(genModelLength)];
        }
        return new String(randBuffer);
    }

    /**
     *
     * @title: randomMobiCode
     * @description:生成随机的手机验证码
     * @return
     * @throws
     */
    public static String randomMobiCode(){
        return randomString("0123456789", 4);
    }

    /**
     *
     * @title: randomEmailCode
     * @description: 生成随机的邮箱验证码
     * @return
     * @throws
     */
    public static String randomEmailCode(){
        return randomString("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 6);
    }

    /**
     *
     * @title: randomPicCode
     * @description:图片验证码
     * @return
     * @throws
     */
    public static String randomPicCode(){
        return randomString("234567890abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ", 4);
    }

    public static String randomToken(){
        return randomString("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 16);
    }

    /**
     * @author wangying@Koolearn-inc.com
     * @title: verifyUserName
     * @description: 用户名验证 （不是从QQ小钱包注册的用户将会被验证长度以及合法性?）
     * @param str
     * @return
     * @throws
     */
    public static boolean verifyUserName(String str) {
        if(str == null){
            return false;
        }
        return userNamePurseer.matcher(str).matches();
    }

    public static boolean isUserName(String str) {
        if(str == null){
            return false;
        }
        return userNamePurseer.matcher(str).matches();
    }

    /**
     * @author wangying@Koolearn-inc.com
     * @title: verifyUserName
     * @description: 用户名验证 （不是从QQ小钱包注册的用户将会被验证长度以及合法性?）
     * @param str
     * @return
     * @throws
     */
    public static boolean verifyUserNameLibrary(String str) {
        if(str == null){
            return false;
        }
        return userNameLibraryPurseer.matcher(str).matches();
    }

    public static boolean verifyUserNameEnterprise(String userName){
        if(userName == null){
            return false;
        }
        return userNamePurseerEnterprise.matcher(userName).matches();
    }


    public static boolean isPassword(String password){
        if (password == null) {
            return false;
        }

        if (password.length() < 6 || password.length() > 16) {
            return false;
        }

        if (!passworder.matcher(password).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isMd532(String md5){
        if(StringUtils.isBlank(md5)){
            return false;
        }
        if(md5.length()!=32){
            return false;
        }
        return md5Pass32.matcher(md5).matches();
    }

    public static void main(String args[]){
        System.out.println(isMd532("123456789a123d5678c012305678901f"));
    }

    /**
     * @author wangying@Koolearn-inc.com
     * @title: getChannel
     * @description:
     * @param request
     * @return
     * @throws
     */
    public static String getChannel(HttpServletRequest request) {
        String channel = StringUtils.trim(request.getParameter("channel"));
        if (channel == null){
            //先根据refer地址判断channel
            String referer = request.getHeader("referer");
            if(StringUtils.isNotBlank(referer)){
                channel = getChannelByReferer(referer);
            }else{
                channel = "class";
            }
        }
        return channel;
    }

    private static String getChannelByReferer(String referer) {
        //暂时返回class
        return "class";
    }

    /**
     * 得到 int
     *
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) obj;
            return bigDecimal.intValue();
        }
        if (obj instanceof Double) {
            Double d = (Double) obj;
            return d.intValue();
        }

        if (obj instanceof String) {
            String str = (String) obj;
            if (StringUtils.isBlank(str)) {
                return 0;
            }
            str = str.trim();
            try {
                if (str.indexOf(".") != -1) {
                    Double d = Double.parseDouble(str);
                    return d.intValue();
                }
                return Integer.parseInt(str);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }

        if (obj instanceof Boolean) {
            boolean b = (Boolean) obj;
            if (b) {
                return 1;
            }
            return 0;
        }

        return 0;
    }

    public static List<String> strToList(String str, String delimiter) {
        if (StringUtils.isBlank(str)){
            return null;
        }
        StringTokenizer token = new StringTokenizer(str, delimiter, false);
        List<String> result = new ArrayList<String>();
        String s = null;
        while (token.hasMoreElements()) {
            s = ((String) token.nextElement()).trim();
            if (s.length() > 0) {
                result.add(s);
            }
        }
        return result;
    }


    public static boolean isHex(String value) {
        return hexPattern.matcher(value).matches();
    }


    public static JSONObject get4JsonHttps(String url, Map<String, String> params) {
        DataOutputStream os = null;
        InputStream is = null;
        try{
            StringBuilder sb = new StringBuilder();
            if(params!=null && params.size()>0) {
                sb.append(url.contains("?")?"&":"?");
                for (String key : params.keySet()) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
                sb.deleteCharAt(sb.length()-1);
            }
            String connectUrl = url+sb.toString();
            URL connect = new URL(connectUrl);
            HttpsURLConnection con = (HttpsURLConnection) connect.openConnection();
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
            };

            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);
            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            is = con.getInputStream();
            byte[] data = readData(is);
            /*if(logger.isDebugEnabled()){
                logger.debug("httpresponse:"+new String(data, "utf-8"));
            }*/
            return JSON.parseObject(new String(data, "utf-8"));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(os!=null){
                try{
                    os.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try{
                    is.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static byte[] readData(InputStream is) {
        byte[] buf = new byte[512];
        StringBuilder sb = new StringBuilder();
        int len = 0;
        while(len!=-1){
            try {
                len = is.read(buf);
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if(len>0){
                sb.append(new String(buf, 0, len));
            }
        }
        return sb.toString().getBytes();
    }

    public static UsersDTO.REG_TYPE getRegType(String type){
        UsersDTO.REG_TYPE regType = null;
        if(DomainConnect.DOMAIN_QQ.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.WEB_QQ;
        }else if(DomainConnect.DOMAIN_SINAWEIBO.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.WEB_SINAWEIBO;
        }else if(DomainConnect.DOMAIN_RENREN.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.WEB_RENREN;
        }else if(DomainConnect.DOMAIN_WEIXIN.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.DOMAIN_WEIXIN;
        }else if(DomainConnect.DOMAIN_BAIDU.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.DOMAIN_BAIDU;
        }else if(DomainConnect.DOMAIN_XDF.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.DOMAIN_XDF;
        }else if(DomainConnect.DOMAIN_ZHIUP.equalsIgnoreCase(type)){
            regType = UsersDTO.REG_TYPE.DOMAIN_ZHIUP;
        }
        return regType;
    }
    
    public static String PARAM_MOBILE_INVALID_MESSGE = "手机号非法";//ErrCode PARAM_MOBILE_INVALID = "7004"
    public static String BIZ_MSG_SEND_INTERVAL_MESSGE = "没到短信发送间隔";//ErrCode BIZ_MSG_SEND_INTERVAL = "7201";没到短信发送间隔
    public static String BIZ_MSG_SEND_FAILED_MESSGE = "短信发送失败";//ErrCode BIZ_MSG_SEND_FAILED = "7203";短信发送失败
}
