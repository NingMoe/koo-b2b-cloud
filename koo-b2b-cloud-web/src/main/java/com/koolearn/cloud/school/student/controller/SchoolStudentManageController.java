package com.koolearn.cloud.school.student.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.common.SchoolCommonService;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.student.SchoolStudentManageService;
import com.koolearn.cloud.school.student.vo.StudentPageVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fn on 2016/11/10.
 */
@RequestMapping("/school/student")
@Controller
public class SchoolStudentManageController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private SchoolStudentManageService schoolStudentManageService;
    @Autowired
    private SchoolCommonService schoolCommonService;
    /**
     * 学生管理页面跳转
     * @param request
     * @param response
     * @return
     * /school/student/goStudentManage
     */
    @RequestMapping(value = "/goStudentManage"  , method = RequestMethod.GET )
    public String goStudentManage(HttpServletRequest request , HttpServletResponse response){
        return "/school/student/studentManage";
    }

    /**
     * 初始化查询所有学段和年级
     * @param manager
     * @return
     * /school/student/findAllSubjectClasses
     */
    @ResponseBody
    @RequestMapping(value = "/findAllSubjectClasses"  , method = RequestMethod.POST )
    public String findAllSubjectClasses( Manager manager ){
        Integer schoolId = manager.getSchoolId();
        Integer userId = manager.getId();
        Map< String , Object> map = new HashMap<String, Object>( );
        if(null != schoolId && null != userId ){
            SchoolInfo schoolInfo = schoolCommonService.findAllSchoolBySchoolId( schoolId , userId);
            map.put( "data" , schoolInfo );
            map.put(CommonInstence.STATUS, CommonInstence.CODE_0 );
            JSONObject param = (JSONObject) JSONObject.toJSON( map );
            log.info("初始化查询条件:"+ param );
            System.out.println( param );
            return param.toString();
        }
        return "";
    }

    /**
     * 学生分页查询
     * @param manager
     * @return
     * /school/student/findStudentPage?Level=12
     */
    @ResponseBody
    @RequestMapping(value = "/findStudentPage"  , method = RequestMethod.POST )
    public String findStudentPage( @ModelAttribute StudentPageVo studentPageVo ,Manager manager){
        Map< String , Object > resultMap = new HashMap<String, Object>();
        if( null != studentPageVo ){
            studentPageVo.setSchoolId( manager.getSchoolId() );
            if( studentPageVo.getCurrentPage() == null ){
                studentPageVo.setCurrentPage(0);
            }if( studentPageVo.getPageSize() == null ){
                studentPageVo.setPageSize( CommonInstence.PAGE_SIZE_20 );
            }
            Map< String , Object > map = schoolStudentManageService.findStudentPage( studentPageVo );
            resultMap.put( "datas" ,map );
            resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }else{
            resultMap.put( CommonInstence.DATA ,"" );
            resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
        log.info("学生分页查询结果:"+ param );
        System.out.println( "学生分页查询结果:" + param );
        return param.toString();
    }



}
