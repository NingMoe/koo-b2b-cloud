package com.koolearn.cloud.util;

import com.koolearn.sso.dto.Authorization;
import com.koolearn.sso.dto.Users;
import com.koolearn.sso.filter.SSOLoginFilterRemote;
import com.koolearn.sso.util.CookieUtil;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends SSOLoginFilterRemote{
	private static final long serialVersionUID = 896191300709327492L;
    public void init(FilterConfig filterConfig) throws ServletException
    {
    	super.init(filterConfig);
    }
	@Override
	public Authorization getLoginUser(HttpServletRequest request,
			HttpServletResponse response) {
		Object obj = request.getSession().getAttribute("AUTHORIZATION");
		if(obj!=null){
			Authorization auth = (Authorization) obj;
			return auth;
		}else{
			return null;
		}
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response,
			Users user) {
		if(user!=null){
			Authorization au = new Authorization();
			au.setId(user.getId());
			au.setName(user.getUsername());
			au.setChannel("cloud");
			request.getSession().setAttribute("AUTHORIZATION",au);
		}else{
			//sso登录不成功,清除云平台登录信息
			Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
	    	String authId = "0";
	        if(authCookie!=null){
	        	authId = authCookie.getValue();
	        }
	        CacheTools.addCache(authId, null);
			request.getSession().setAttribute("AUTHORIZATION",null);
			System.out.println("not find users");
		}
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("AUTHORIZATION");
	}

}
