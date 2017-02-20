package com.koolearn.cloud.login.interceptor;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.sso.util.CookieUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudentInterceptor extends HandlerInterceptorAdapter {
	private static Log logger = LogFactory.getLog(StudentInterceptor.class);
	
    /**
     * 被拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isFire = false;
        String loginUrl = "/loginPage";
    	Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    	 String authId = "0";
         if(authCookie!=null){
         	authId = authCookie.getValue();
         }
        UserEntity ue = CacheTools.getCacheForever(authId,UserEntity.class);
        if(ue!=null){
        	logger.debug("authId=="+authId+"userid="+ue.getId()+"   username="+ue.getUserName()+"  usertype="+ue.getType()+"   isFire==="+isFire);
        }else{
        	logger.debug("authId=="+authId+"ue==null   isFire="+isFire);
        }
        if (ue != null && ue.getType().intValue()==UserEntity.USER_TYPE_STUDENT) {
            	isFire = true;
        }else{//清除缓存中用户对象
        	CacheTools.delCache(authId);
        }
        if (!isFire) {
            response.sendRedirect(request.getContextPath() + loginUrl);
        }
        return isFire;
    }
    /**
     * preHandle返回true调用
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
    	super.postHandle(request, response, handler, modelAndView);
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
