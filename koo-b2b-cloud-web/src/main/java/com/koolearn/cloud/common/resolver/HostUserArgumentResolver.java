package com.koolearn.cloud.common.resolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.koolearn.cloud.operation.entity.OperationUser;
import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.security.client.entity.SecurityUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.sso.util.CookieUtil;

public class HostUserArgumentResolver implements WebArgumentResolver{
	
	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter arg0, NativeWebRequest arg1)
			throws Exception {
		HttpServletRequest request = arg1.getNativeRequest(HttpServletRequest.class);
		if (arg0.getParameterType().equals(UserEntity.class)) {
			Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
	    	String authId = authCookie.getValue();
			UserEntity ue = CacheTools.getCacheForever(authId, UserEntity.class);
			if (ue != null) {
				return ue;
			}else{
				return null;
			}
		}else if (arg0.getParameterType().equals(Manager.class)) {
            Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_KEY);
            if( null != authCookie ){
                String authId = authCookie.getValue();
                Manager manager = CacheTools.getCacheForever(authId, Manager.class);
                if (manager != null) {
                    return manager;
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else if(arg0.getParameterType().equals(OperationUser.class)){
			OperationUser operationUser=new OperationUser();
			try{
				SecurityContext securityContext = SecurityContextHolder.getContext();
				if (securityContext == null) {
					operationUser.setSecurityUser(null);
				}
				Authentication authentication = securityContext.getAuthentication();
				if (authentication == null) {
					operationUser.setSecurityUser(null);
				}
				Object principal = authentication.getPrincipal();
				if (principal instanceof SecurityUser) {
					SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
					operationUser.setSecurityUser(securityUser);
				}
			}catch (Exception e){
				operationUser=null;
			}
			return operationUser;

		}
		return UNRESOLVED;
	}
}