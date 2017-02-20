package com.koolearn.cloud.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.koolearn.util.SystemGlobals;

public class LectureUtil {
	
    protected static Log log = LogFactory.getLog(LectureUtil.class);

    /**
     * 生成新的m3u8文件内容
     * @param httpurl
     * @return
     */
    @SuppressWarnings("static-access")
	public static String updateUrlContent(String httpurl){
    	StringBuffer sb = new StringBuffer();
    	try
    	{
    		URL url = new URL(httpurl);
    		URLConnection conn = url.openConnection();
    		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		String line = null;
    		while ( (line = reader.readLine()) != null) {
    			if(line.indexOf("method=validationFile")!=-1){
    				ProductMD5 md = new ProductMD5();
    				String str = md.md5s("uc1q2w3e4r");
    				line = line.replaceAll("method=validationFile", "method=validationFile?m="+str);
    			}
    			if(!line.startsWith("#")){
    				line = line.replaceAll(line, "http://192.168.102.21:8134/hls-vod/"+line);
    				//上线后用
    				//line = line.replaceAll(line, "http://fms.koolearn.com:8134/hls-vod/"+line);
    			}
    			sb.append(line);
    		}
    		reader.close();
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	return sb.toString();
    }
 
    

    /**
     * 将原来的getRtmpMp4PlayString 方法拆分，保证可以直接传入url地址，获取到视频播放地址。
     * @title getRtmpMp4PlayStringByUrl
     * @param request
     * @param wzzj
     * @param url
     * @return
     */
    public static String getRtmpMp4PlayStringByUrl(HttpServletRequest request, String wzzj, String url) {
//        String rtmpHost = "rtmp://192.168.100.52/vod/mp4:";
        String rtmpHost =SystemGlobals.getPreference("mp4.rtmp.host");
        if( !StringUtils.isBlank( wzzj )) {
        	if( wzzj.equals( "sd")) {	//山东
        		rtmpHost = SystemGlobals.getPreference("mp4.rtmp.host.sd");
        	} else if( wzzj.equals( "hb")) {	//河北
        		rtmpHost = SystemGlobals.getPreference("mp4.rtmp.host.hb");
        	} else if( wzzj.equals( "wh")) {	//武汉
        		rtmpHost = SystemGlobals.getPreference("mp4.rtmp.host.wh");
        	} else if( wzzj.equals( "bj")) {	//北京
        		rtmpHost = SystemGlobals.getPreference("mp4.rtmp.host.bj");
        	}
        }
        log.debug("rtmpHost=" + rtmpHost + ",url=" + url);
        
        //String host = request.getServerName();
        //if ( host.indexOf("newcourse.neibu.koolearn.com") >=0 || host.indexOf("newcourse165.koolearn.com") >=0 || host.indexOf("localhost") >=0 ) {
			/*
	        if ( url.startsWith("vod2:") ) {
	        	//反盗版，同一视频播放器
	        	rtmpHost = rtmpHost.replace(instanceName, "vod2");
	        	url = url.substring(5);
	        }
	        else if ( url.startsWith("vod1") ) {
	        	//反盗版，非同一视频播放器
	        	rtmpHost = rtmpHost.replace(instanceName, "vod1");
	        	url = url.substring(5);
	        }*/
	    //}
        
    	//取实例名称
    	String instanceName = null;
    	int pos = rtmpHost.indexOf("://");
    	if ( pos >= 0 ) {
    		pos += 3;
    	} else {
    		pos = 0;
    	}

		int start = rtmpHost.indexOf( "/", pos );
		int end = rtmpHost.indexOf("/", start+1);
		if ( end > start ) {
			instanceName = rtmpHost.substring(start+1, end);
		}
		else {
			instanceName = rtmpHost.substring(start+1);
		}
		
		if ( StringUtils.isBlank(instanceName) ) instanceName = "vod";
        
        //如果在子文件夹下，则用vod2播放；否则用vod播放
		if(StringUtils.isBlank(url)) return "";
		url = url.replace( "//", "/" );
		if ( url.endsWith("/") ) url = url.substring(0, url.length() - 1);
		if ( !url.startsWith("/") ) url = "/" + url;
		pos = url.lastIndexOf("/");
		if ( pos > 0 ) {
			String s = url + "/";
			if ( s.indexOf("/zxmkh/") == -1 && 
				s.indexOf("/st/") == -1 ) {
    			//子文件夹下的视频, 反盗版
            	rtmpHost = rtmpHost.replace(instanceName, "vod2");
			}
		}
        
        String mp4Url = rtmpHost + url;
        mp4Url = mp4Url.replace("mp4:/", "mp4:");
        
        String protocolswitch = "t";//request.getParameter("ps");
        if ( "t".equals(protocolswitch) ) {
        	//切换到rtmpt协议播放
            if ( mp4Url.startsWith("rtmp://") ) {
            	mp4Url = mp4Url.replace( "rtmp://", "rtmpt://" );
            }
            if ( mp4Url.startsWith("rtmpe://") ) {
            	mp4Url = mp4Url.replace( "rtmpe://", "rtmpte://" );
            }
        }
        
        log.debug( mp4Url );
        return mp4Url;
    }

    
    /**
     * 为了是获取的mp4文件路径一致，调用知识点试听的路径。
     * @title 产品详情页使用：获取MP4的试听路径
     * @param request
     * @param url 产品的试听地址 product.getFreeListenFile()
     * @param iconFileUrl 产品的图片地址 product.getIconFileUrl()
     * @return
     */
    public static String getRtmpMp4PlayStringForProductDetail(HttpServletRequest request, String url) {
        
        return getRtmpMp4PlayStringByUrl(request, null, url) ;
    }
    
 
    public static void main(String[] args) {
    	System.out.println( getRtmpMp4PlayStringByUrl(null, null, "b46/2012/2011cet4_zh_zj_1_3.mp4"));
	}
  
   
    
    
    
}   
