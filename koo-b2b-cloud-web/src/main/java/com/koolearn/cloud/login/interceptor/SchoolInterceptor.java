package com.koolearn.cloud.login.interceptor;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.sso.util.CookieUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fn on 2016/10/12.
 */
public class SchoolInterceptor extends HandlerInterceptorAdapter {

    private static Log logger = LogFactory.getLog(SchoolInterceptor.class);

    /**
     * 被拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isFire = false;
        String loginUrl = "/schoolLogin";
        Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_KEY);
        if(authCookie!=null){
            String authId = authCookie.getValue();
            Manager manager = CacheTools.getCacheForever(authId, Manager.class);
            if (manager == null ) {
                CacheTools.delCache(authId);
                response.sendRedirect(request.getContextPath() + loginUrl);
            }else{
                isFire = true;
            }
        }else{
            response.sendRedirect(request.getContextPath() + loginUrl);
        }
        // TODO 修改返回值
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
