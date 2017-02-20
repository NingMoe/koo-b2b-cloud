package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.service.ClassHomePageService;
import com.koolearn.cloud.teacher.service.ClassNewStatusService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/5/5.
 */
@RequestMapping("/teacher/classNewStatus")
@Controller
public class ClassNewStatusController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ClassNewStatusService classNewStatusService;
    @Autowired
    private ClassHomePageService classHomePageService;

    @RequestMapping(value = "/getNews"  , method = RequestMethod.POST )
    public String getNews(  HttpServletRequest request ,UserEntity userEntity , ModelMap modelMap){
        String classesId = request.getParameter( "classesId" );
        String pageNo = request.getParameter( "pageNo" );
        if( StringUtils.isBlank( pageNo )){
            pageNo = "0";
        }
        if(StringUtils.isNotBlank( classesId )){
            //查询老师下所有班级
            List<Classes> classList = classHomePageService.findTeacherClassById( userEntity.getId() , CommonInstence.CLASSES_TYPE_3 ,CommonInstence.STATUS_0);
            Pager pager = classNewStatusService.findClassesDynamicByClassId( new Integer( classesId) , CommonInstence.PAGE_SIZE_20 ,new Integer( pageNo ) );
            //更新老师下关于当前班级的所有动态状态
            try {
                classNewStatusService.updateTeacherDynamicStatus( new Integer( classesId ) , userEntity.getId() );
            } catch (Exception e) {
                e.printStackTrace();
            }
            modelMap.put( "classList" ,classList );
            modelMap.put( "list" , pager.getResultList() );
            modelMap.put( "pageInfo" , pager );
            modelMap.put( "classesId" ,classesId );
        }
        return "/teacherdata/ClassNewStatus";
    }
    @ResponseBody
    @RequestMapping(value = "/getNewsPost"  , method = RequestMethod.POST )
    public String getNewsPost(  HttpServletRequest request ,UserEntity userEntity ){
        String classesId = request.getParameter( "classesId" );
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank( classesId )){
            map.put("code", CommonInstence.CODE_200);
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }




}
