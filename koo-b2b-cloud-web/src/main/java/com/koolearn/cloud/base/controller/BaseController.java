package com.koolearn.cloud.base.controller;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.operation.entity.OperationUser;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.library.maintain.util.MD5Util;
import com.koolearn.security.client.core.data.SecurityXmlUsers;
import com.koolearn.security.client.entity.SecurityUser;
import com.koolearn.sso.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BaseController {
    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
    private String errorMessage;

    public static HttpServletRequest getHttpRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;
    }

    public Object getAttribute(String key) {
        return getHttpRequest().getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        getHttpRequest().setAttribute(key, value);
    }

    public String getParamter(String key) {
        return getHttpRequest().getParameter(key);
    }

    public int getParamterForInt(String key, int defaultValue) {
        if (StringUtils.isNotBlank(getParamter(key))) {
            return Integer.parseInt(getParamter(key));
        } else {
            return defaultValue;
        }
    }

    public static HttpSession getHttpSession() {
        return getHttpRequest().getSession();
    }

    public static void setSessionObject(String key, Object value) {
        getHttpSession().setAttribute(key, value);
    }

    public static Object getSessionObject(String key) {
        return getHttpSession().getAttribute(key);
    }

    public static void removeSessionObject(String key) {
        getHttpSession().removeAttribute(key);
    }

    public static Object getCookie(String key) {
        Cookie[] cookies = getHttpRequest().getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (key.equals(ck.getName())) {
                return ck.getValue();
            }
        }
        return null;
    }

    public static void delCookie(String key, HttpServletResponse response) {
        Cookie[] cookies = getHttpRequest().getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (key.equals(ck.getName())) {
                    cookie = ck;
                    break;
                }
            }
        }
        cookie.setDomain("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void setCookie(String key, String o, Integer maxAge, HttpServletResponse response) {
        Cookie cookies = new Cookie(key, o);
        if (maxAge == null) {
            //会话cookie
        } else {
            cookies.setMaxAge(maxAge);
        }
        cookies.setPath("/");
        response.addCookie(cookies);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    /**
     * 将结果写到页面内容上，作为Ajax请求的结果
     * TODO
     *
     * @param response
     * @param message
     */
    protected void printWriterAjax(HttpServletResponse response, String message) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        try {
            PrintWriter pwriter = response.getWriter();
            pwriter.print(message);
            pwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * print  对象为json
     *
     * @param response
     */
    protected void printWriterAjax(HttpServletResponse response, Object obj) {
        printWriterAjax(response, JSON.toJSONString(obj));
    }
    
    public UserEntity changeUser(UserEntity ue){
    	HttpServletRequest request = getHttpRequest();
    	Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    	String authId = authCookie.getValue();
    	CacheTools.addCache(authId, ue);
    	return ue;
    }
    
    public UserEntity getUser(){
    	HttpServletRequest request = getHttpRequest();
    	Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    	String authId = authCookie.getValue();
    	UserEntity ue = CacheTools.getCacheForever(authId, UserEntity.class);
    	return ue;
    }

    /**
     * 通过用户id获取用户
     * @param userId
     * @return
     */
    public UserEntity getUserByUserId( String userId ){
        return CacheTools.getCacheForever( userId , UserEntity.class);
    }

    /**
     * 得到登录的管理员名
     *
     * @return 登录的管理员名
     */
    public String getSecurityUserName() {
        SecurityUser user = this.getSecurityUser();
        if (user == null) {
            return "游客";
        } else {
            return user.getName();
        }
    }

    /**
     * 权限系统：获取整体云平台平台的使用用户
     * @return
     */
    public List<SecurityUser> getSecurityUserList(){
        List<SecurityUser> securityUsers=CacheTools.getCache(OperationUser.SECURITY_USERLIST_CACHE_KEY, List.class);
        if(securityUsers==null||securityUsers.size()<1){
            String appId = PropertiesConfigUtils.getProperty(OperationUser.SECURITY_CAS_APPID);
            securityUsers= SecurityXmlUsers.read(Integer.parseInt(appId));
            CacheTools.addCache(OperationUser.SECURITY_USERLIST_CACHE_KEY,securityUsers);
        }
        return securityUsers;

    }

    /**
     * 权限系统：根据用户ID获取用户信息
     * @param userId
     * @return
     */
    public OperationUser getSecurityUserById(int userId){
        OperationUser operationUser=null;
        List<SecurityUser> securityUsers=this.getSecurityUserList();
        if(securityUsers!=null && securityUsers.size()>0){
            for (SecurityUser user:securityUsers){
                if(userId==user.getId()){
                    operationUser=new OperationUser();
                    operationUser.setSecurityUser(user);
                }
            }
        }
        return operationUser;

    }

    /**
     * 得到登录用户
     *
     * @return
     */
    public SecurityUser getSecurityUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return securityUser;
        }
        return null;
    }

    /**
     * 得到登录用户ID
     *
     * @return
     */
    public int getSecurityUserId() {
        SecurityUser user = this.getSecurityUser();
        if (user == null) {
            return -1;
        }
        return user.getId();
    }


}

