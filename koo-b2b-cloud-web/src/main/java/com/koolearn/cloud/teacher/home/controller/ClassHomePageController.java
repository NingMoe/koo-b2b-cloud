package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.service.FirstLoginChoiceService;
import com.koolearn.cloud.util.BizException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 班级首页展示
 * Created by fn on 2016/4/5.
 */
@RequestMapping("/teacher/classHomePage")
@Controller
public class ClassHomePageController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private FirstLoginChoiceService firstLoginChoiceService;
    @Autowired
    LoginService loginService;
    /**
     * 教师班级首页--教师浏览后更新状态
     * @return
     */
    @RequestMapping(value ="/updateExamTeacherView")
    public String updateExamTeacherView( HttpServletRequest request ,UserEntity userEntity){
        String examId = request.getParameter( "examId" );
        //查询老师名下所有班级
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("教师浏览后更新状态:"+param);
        return "/teacherdata/ClassHomePage";
    }

    /**
     * 添加老师姓名
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addTeacherRealName"  , method = RequestMethod.POST )
    public String addTeacherRealName(HttpServletRequest request ,UserEntity userEntity){
        String name = request.getParameter( "realName" );
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank(name)){
            try {
                int num = firstLoginChoiceService.addTeacherRealName(userEntity.getId(), name);
                if (num == 1) {
                    userEntity.setRealName( name );
                    //更新缓存
                    UserEntity user = changeUser( userEntity );
                    loginService.hexinAccountTeacher(user);//合心接口--修改老师资料
                    map.put("code", CommonInstence.CODE_200);
                }
            }catch ( BizException e ){
                map.put("code", CommonInstence.CODE_400);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }
}
