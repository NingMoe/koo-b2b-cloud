package com.koolearn.cloud.login.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacher.listenCourse.player.MD5;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.DateFormatUtils;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.sso.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by fn on 2016/10/27.
 */
@Controller
public class SchoolLoginController extends BaseController {

    @Autowired
    LoginService loginService;

    /**
     * 登陆跳转
     * @param request
     * @return
     */
    @RequestMapping("/schoolLogin")
    public String loginPage(HttpServletRequest request){
        request.setAttribute("ssoUrl", GlobalConstant.HOSTS_SSO);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        request.setAttribute("uuid", uuid);
        return "/login/schoolLogin";
    }


    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/schoollorLogin" )
    @ResponseBody
    public Map login(HttpServletRequest request,HttpServletResponse response) {
        Map< String , Object > map = new HashMap< String ,Object >();
        String random = getParamter("random");
        String uuid = request.getParameter("uuid");
        String randomCache = CacheTools.getCache(CommonConstant.getLoginVerifyImage(uuid), String.class);

        System.out.println("输入的验证码:" + random + "  缓存中的验证码：" + randomCache);
        if (StringUtils.isBlank(randomCache)) {
            map.put("code", 400);
            map.put("errMsg", "验证码失效！");
        } else if (StringUtils.isBlank(random) || !random.equalsIgnoreCase(randomCache)) {
            //存储业务系统用户信息
            map.put("code", 400);
            map.put("errMsg", "验证码错误！");
        } else {
            String userName = getParamter("userName");
            String password = getParamter("password");
            String md5Word = MD5.calcMD5( password );
            Manager manager = loginService.findUserByAccout(userName, md5Word);
            if ( manager != null) {
                //判断是否被冻结 1激活  2 冻结
                Integer status = manager.getStatus();
                if( null != status && status == CommonInstence.STATUS_2 ){
                    map.put("code", 400);
                    map.put("errMsg", "账户被冻结，请与学校管理员联系！");
                }else{
                    // TODO 查询学校是否可用
                    School school = loginService.findSchoolStatus( manager.getSchoolId() );
                    if( null != school ){
                        if( null != school.getEndTime() && school.getEndTime().getTime() < new Date().getTime() ){
                            map.put("code", 400);
                            map.put("errMsg", "学校权限已过期，请与学校管理员联系续费！");
                        }else if( school.getEntityStatus() == CommonInstence.STATUS_2 ){
                            map.put("code", 402);
                            map.put("errMsg", "学校已删除");
                        }else if( school.getEntityStatus() == CommonInstence.STATUS_3 ){
                            map.put("code", 403);
                            map.put("errMsg", "学校已屏蔽");
                        }else if( school.getEntityStatus() == CommonInstence.STATUS_1 &&school.getStatus() == CommonInstence.STATUS_0 ){
                            //存储业务系统用户信息
                            CookieUtil.setCookie(request, response, CommonConstant.AUTH_KEY, manager.getId().toString());
                            CacheTools.addCacheForever(manager.getId().toString(), manager);
                            map.put("code", 200 );
                            map.put("system", "cloud");
                            map.put("userName", manager.getManagerName());
                            map.put("schoolId", manager.getSchoolId());
                            map.put("userId", manager.getId());
                        }
                    }else{
                        map.put("code", 400);
                        map.put("errMsg", "未分配学校！");
                    }
                }
            } else {
                map.put("code", 400);
                map.put("errMsg", "用户名密码错误！");
            }
        }
        return map;
    }
    @RequestMapping("/schoolLogout")
    public String schoolLogout(HttpServletRequest request,HttpServletResponse response,Manager manager ){
        Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_KEY );
        if( null != authCookie ){
            String authId = authCookie.getValue();
            CacheTools.delCache(authId);
        }if( manager != null ){
            CookieUtil.deleteCookie(request, response, manager.getId().toString());
        }
        return "redirect:/schoolLoginPage";
    }
}
