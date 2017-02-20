package com.koolearn.cloud.teacher.home.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.service.ClassesResourceService;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/5/5.
 */
@RequestMapping("/teacher/classesResource")
@Controller
public class ClassesResourceController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private ClassesResourceService classesResourceService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private CommonService commonService;
    /**
     * 班级资源
     * @param request
     * @param userEntity
     * @param modelMap
     * @return
     */

    @RequestMapping(value = "/findClassBaseInfo"  , method = RequestMethod.POST )
    public String findClassBaseInfo( HttpServletRequest request ,UserEntity userEntity , ModelMap modelMap ){
        String pageNo = request.getParameter( "pageNo" );
        String classesId = request.getParameter( "classesId" );
        String typeId = request.getParameter( "typeId" );
        //所有学科
        List<TeacherBookVersion> teacherBookVersionsList = null;
        if( StringUtils.isNotBlank( classesId) ){
            if( null == pageNo ){
                pageNo = "0";
            }
            Pager pager =  classesResourceService.findExamByClassesIdAndTeacherId( new Integer( classesId) , userEntity.getId(), typeId ,new Integer( pageNo) , CommonInstence.PAGE_SIZE_20);
            if( null != pager ){
                modelMap.put( "tpExamClassesList" , pager.getResultList() );
                modelMap.put( "pageInfo" , pager );
            }
            //查询左侧信息
            Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,userEntity.getId() , null ,null );
            //班级信息
            modelMap.put( "classes" ,map.get("classes") );
            modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
            //任课老师信息
            modelMap.put( "classesTeacherList" ,map.get("classesTeacherList"));
            modelMap.put( "teacherBookVersionsList" , map.get("teacherBookVersionsList") );
        }
        if( StringUtils.isBlank( typeId) || "0".equals( typeId )){
            return "/teacherdata/ClassesResource";
        }else if( "1".equals( typeId )){
            return "/teacherdata/ClassesResourceZuoYe";
        }else{
            return "/teacherdata/ClassesResourceKeTang";
        }
    }




}
