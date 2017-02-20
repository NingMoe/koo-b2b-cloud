package com.koolearn.cloud.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/** JQuery创建Cookie  $.cookie('userid',0,{expires:new Date()-1000, domain:'7k7k.com', path:'/'});
 *        获取Cookie  $.cookie(‘cookieName’); 
 *        删除Cookie  $.cookie(‘cookieName’,null);
 *        删除一个带有效路径的cookie  $.cookie(‘cookieName’,null,{path:’/'});
 * 1)cookie其实是由name,value, expires,path,domain等属性组成
 * 2)当没有指明cookie时间时，所创建的cookie有效期默认到用户浏览器关闭止，故被称为会话cookie
 * 3)expires：7  当指明时间时，故称为持久cookie，并且有效时间为天
 * 4)如果不设置有效路径，在默认情况下，只能在cookie设置当前页面读取该cookie，cookie的路径用于设置能够读取cookie的顶级目录
 * @author gehaisong 
 *
 */
public class CookieUtil {
public static String domainName=null;
public static String path="/";//设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie的路径及其子路径可以访问
public static int maxAge = 3600 * 24;//记录一天
public static final String JSESSIONID_COOKIE_NAME="JSESSIONID";
public static String cookieName="koolearn_netalliance_cookie";
public static String kwCookieName="koolearn_netalliance_baidu_kwid";


/**
 * * @Description: TODO(获取session id) 
   *  @param request
   *  @return
   *  @throws Exception    
   * @return Cookie    
   * @author: 葛海松
   * @time:    2015年3月16日 下午2:26:36 
   * @throws
 */
public static Cookie getSessionCookie(HttpServletRequest request) throws Exception{
	return readCookie(request, JSESSIONID_COOKIE_NAME);
}

/**
 * * @Description: TODO(创建cookie) 
   *  @param response
   *  @param cookieNameCurr  cookieName
   *  @param cookieValue
   *  @throws Exception    
   * @return void    
   * @author: 葛海松
   * @time:    2015年3月14日 下午12:13:23 
   * @throws
 */
	public static void writeCookie(HttpServletResponse response,String cookieNameCurr,String cookieValue)throws Exception{
		if (StringUtils.isNotBlank(cookieValue)) {
			if(StringUtils.isBlank(cookieNameCurr))
				cookieNameCurr=cookieName;
			Cookie cookie = new Cookie(cookieNameCurr, cookieValue);
			cookie.setMaxAge(maxAge);
			cookie.setPath(path);
           	if (domainName!=null && !"".equals(domainName))
           		cookie.setDomain(domainName);
			response.addCookie(cookie);
		}
	}
	/**
	 * * @Description: TODO(读取指定的Cookie  path $.cookie('examIdArr',examIdArr,{path:'/'});) 
	   *  @param request
	   *  @param cookieNameCurr
	   *  @return
	   *  @throws Exception    
	   * @return Cookie    
	   * @author: 葛海松
	   * @time:    2015年3月14日 下午12:20:59 
	   * @throws
	 */
	public static Cookie readCookie(HttpServletRequest request,String cookieNameCurr)throws Exception{
		if(StringUtils.isBlank(cookieNameCurr))
			cookieNameCurr=cookieName;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equals(cookieNameCurr)) {
					return c;
				}
			}
		}
		return null;
	}
	/**
	 * * @Description: TODO(删除Cookie  path $.cookie('examIdArr',examIdArr,{path:'/'});
	 *                       如果写cookie的时候设置了path与domain,
	 *                       则清除cookie时也需要设置相同的path,domain,  如果没有设置domain, 即取当前的location.host) 
	   *  @param request
	   *  @param response
	   *  @param cookie
	   *  @param cookieName
	   *  @throws Exception    
	   * @return void    
	   * @author: 葛海松
	   * @time:    2015年3月14日 下午12:27:32 
	   * @throws
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
			Cookie cookie,String cookieName)throws Exception{
		String d=getDomainName( request);
		if(StringUtils.isNotBlank(cookieName)){
		   cookie=readCookie(request,cookieName);
		}
		if(cookie!=null){
			 cookie.setPath(path);
	         cookie.setValue("");
	         cookie.setMaxAge(0);//立刻删除
	         if (domainName!=null && !"".equals(domainName))cookie.setDomain(domainName);
	         response.addCookie(cookie);
		}
	}
	/**
	 * * @Description: TODO(描述这个方法的作用) 
	   *  @param request
	   *  @return    
	   * @return String    
	   * @author: 葛海松
	   * @time:    2015年3月14日 下午12:29:47 
	   * @throws
	 */
	public static String getDomainName(HttpServletRequest request) {
	        String domainName = request.getServerName();
	        if (domainName != null) {
	            int pos = domainName.indexOf(".");
	            if (pos > 0) {
	                domainName = domainName.substring(pos);
	                return domainName;
	            }
	        }
	        return null;
	    }
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	public static void setCookieName(String cookieName) {
		CookieUtil.cookieName = cookieName;
	}
	public static void setDomainName(String domainName) {
		CookieUtil.domainName = domainName;
	}
	public static void setMaxAge(int maxAge) {
		CookieUtil.maxAge = maxAge;
	}
	public static void setPath(String path) {
		CookieUtil.path = path;
	} 	
	/**
     * 获取访问者IP
     * 
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * 
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     * 
     * @param request
     * @return
     */
    public static String getIpFromRequest(HttpServletRequest request) throws Exception{
        String ip = request.getHeader("X-Real-IP");//X-Real-IP:一般只记录真实发出请求的客户端IP
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        //X-Forwarded-For是用于记录代理信息的，每经过一级代理(匿名代理除外)，代理服务器都会把这次请求的来源IP追加在X-Forwarded-For中
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
        	  if (StringUtils.isBlank(ip)|| "unknown".equalsIgnoreCase(ip)) {
                  ip = request.getHeader("Proxy-Client-IP");
              }
              if (StringUtils.isBlank(ip)|| "unknown".equalsIgnoreCase(ip)) {
                  ip = request.getHeader("WL-Proxy-Client-IP");
              }
              if (StringUtils.isBlank(ip)|| "unknown".equalsIgnoreCase(ip)) {
                  ip = request.getRemoteAddr();
              }
              String[] ips = ip.split(",");
              return ips[0].trim();
        }
    }
	 
}
