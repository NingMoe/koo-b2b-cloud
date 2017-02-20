package com.koolearn.cloud.school.authority.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.teacher.listenCourse.player.MD5;
import kafka.controller.RequestSendThread;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 个人中心管理
 * Created by fn on 2016/11/24.
 */
@RequestMapping("/school/personal")
@Controller
public class PersonalCenterController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    LoginService loginService;

    /**
     * 个人中心跳转
     * @return
     */
    @RequestMapping( value="/goPersonalManage" ,method= RequestMethod.GET )
    public String goPersonalManage( HttpServletRequest request , Manager manager ){
        return "/school/personalCenter";
    }

    /**
     * 修改个人密码
     * @param request
     * @param manager
     * @return
     */
    @RequestMapping( value = "/updatePersonalPassword" ,method= RequestMethod.POST )
    public String updatePersonalPassword(HttpServletRequest request , Manager manager ,
                                         @RequestParam String oldPassword , @RequestParam String newPassword ){
        if(StringUtils.isNotEmpty( oldPassword ) && StringUtils.isNotEmpty( newPassword )){
            String oldMd5Pw = MD5.calcMD5( oldPassword );
            String newMd5Pw = MD5.calcMD5( oldPassword );
            if( null != manager && manager.getManagerEmail() != null ){
                Manager managerLogin = loginService.findUserByAccout(manager.getManagerEmail(), oldMd5Pw );
                if( null != managerLogin ){
                    //更新密码
                }

            }else if( null != manager && manager.getManagerMobile() != null ){
                Manager managerLogin = loginService.findUserByAccout(manager.getManagerMobile(), oldMd5Pw );
                if( null != managerLogin ){
                    //更新密码
                }
            }
        }
        return "";
    }

}
