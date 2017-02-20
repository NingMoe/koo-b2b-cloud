package com.koolearn.cloud.login.interceptor;

import com.alibaba.dubbo.common.utils.Assert;
import com.koolearn.cloud.composition.service.CompositionService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.redis.client.KooJedisClient;
import com.koolearn.sso.util.CookieUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by haozipu on 2016/8/22.
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    private static Log logger = LogFactory.getLog(PermissionInterceptor.class);

    @Autowired
    KooJedisClient redisClient;

    @Autowired
    CompositionService compositionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
        String authId = "0";
        if(authCookie!=null){
            authId = authCookie.getValue();
        }
        UserEntity ue = CacheTools.getCacheForever(authId, UserEntity.class);

        request.setAttribute("compositionFlag",false);

        if(compositionService!=null){
            Boolean f = redisClient.get("composition_teacher_flag_"+ue.getUserId(),Boolean.class);
            if(f==null){
                f =compositionService.queryIsCompositionTeacher(ue.getUserId());
                redisClient.setex("composition_teacher_flag_"+ue.getUserId(),6*60,f);
            }
            request.setAttribute("compositionFlag",f);
        }

        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(compositionService, "权限拦截器注入compositionService失败");
        Assert.notNull(redisClient,"权限拦截器注入redisClient失败");
    }
}
