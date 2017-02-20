package com.koolearn.cloud.teacher.listenCourse.player;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线视频播放参数
 * @author gehaisong
 *
 */
public class VideoPlayParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * accountId:0 sessionId:719C30FAA2F7C86CDFF3DA39CD4EB9A3 productId:0 url：
	 * 加密url：
	 * M0qTMgp6kY8XnshUlsIekwQWlCeblv9cLcpYkcbWmD2vmDIVlwAXlv8YlDQXIcFamR96KR96Ky8XHZlULHAa
	 * 
	 * String key = accountId + sessionId + nowTime + securityKey; key = new
	 * MD5().calcMD5(key);
	 */

	private String videoPlayUrl;// 视频播放加密url
	private String key;
	private int accountId = 0;
	private String sessionId = "719C30FAA2F7C86CDFF3DA39CD4EB9A3";
	private long nowTime = new Date().getTime();

	private int productId = 0;
	private String securityKey = "670B14728AD9902AECBA32E22FA4F6BD";
	private String videoUrl;

	/**
	 * 获取视频播放路径
	 * @return
	 */
	public String getVideoPlayUrl() {
	 //视频全路径videoUrl : rtmpt://192.168.100.52/vod2/mp4:b46/2012/2011cet4_zh_zj_1_3.mp4
        String offline = PropertiesConfigUtils.getProperty("offline");
        if("true".equals(offline)) {
            return videoUrl;
        }
		if (videoUrl != null && !"".equals(videoUrl)) {
			byte[] buffer = videoUrl.getBytes();
			ExBase64 base64 = new ExBase64();
			byte[] buffer2 = base64.encode(buffer);
			videoPlayUrl = new String(buffer2);
		}
		return videoPlayUrl;
	}
  /**
   * 获取视频播放key值
   * @return
   */
	public String getKey() {
		/** accountId:0 sessionId:719C30FAA2F7C86CDFF3DA39CD4EB9A3 productId:0 */
		 System.out.println("accountId:"+accountId+" sessionId:"+sessionId
		           +" productId:"+productId);
		key = accountId + sessionId + nowTime + securityKey;
		key = new MD5().calcMD5(key);
		return key;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public void setVideoPlayUrl(String videoPlayUrl) {
		this.videoPlayUrl = videoPlayUrl;
        getVideoPlayUrl();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getNowTime() {
		return nowTime;
	}

	public void setNowTime(long nowTime) {
		this.nowTime = nowTime;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}
