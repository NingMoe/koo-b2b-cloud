package com.koolearn.cloud.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;



public class WeiXinUtil {

	private static final String randomStringSource = "qwertyuioplkjhgfdsazxcvbnmZXCVBNMKLJHGFDSAQWERTYUIOP*&%$#!";
    private static Log logger = LogFactory.getLog(WeiXinUtil.class);
	public static String randomString() {
		int size = randomStringSource.length();

		int randLength = RandomUtils.nextInt(size);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < randLength - 1; i++) {
			sb.append(randomStringSource.charAt(i));
		}
		return md5(sb.toString());
	}

	public static String md5(String s) {
		s = DigestUtils.md5Hex(s);
		return s;
	}
	
	/**
	 * @Description: 加密app发往微信参数
	 * @param 发往微信参数,加密key
	 * @return String  
	 * @throws
	 * @author yangzh
	 * @date 2016-5-25
	 */
	public static String weixinSign(Map<String,String> params,String tenpaySecret){
		String signStr = createLinkString(params);
	    signStr = (signStr+"&key="+tenpaySecret);


        logger.info("添加 key后 字符串 "+signStr);

	    signStr = md5(signStr).toUpperCase();

        logger.info("MD5 后 大写 "+signStr);

		return signStr;
	}
	
	/**
	 * @Description: 加密prepay_id
	 * @param 发往微信参数,加密key
	 * @return String  
	 * @throws
	 * @author yangzh
	 * @date 2016-5-25
	 */
	public static String classSign(String prepay_id,String classKey){
		String signStr = prepay_id+classKey;
	    signStr = md5(signStr);   
		return signStr;
	}
	
	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer prestr = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (prestr.length() > 0) {
				prestr.append("&");
			}
			prestr.append(key).append("=").append(value.trim());
		}
		return prestr.toString();
	}
}
