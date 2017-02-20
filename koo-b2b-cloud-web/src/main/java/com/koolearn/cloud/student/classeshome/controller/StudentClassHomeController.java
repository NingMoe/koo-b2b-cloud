package com.koolearn.cloud.student.classeshome.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.student.StudentClassHomeService;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
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
 * 班级首页展示
 * Created by fn on 2016/4/5.
 */
@RequestMapping("/student/classHome")
@Controller
public class StudentClassHomeController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentClassHomeService studentClassHomeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    LoginService loginService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    /**
     * 跳转到学生班级的首页
     * @return
     * http://cloud.trunk.koolearn.com/student/classHome/goStuClassHome
     */
    @RequestMapping(value ="/goStuClassHome"  , method = RequestMethod.GET )
    public String goStuClassHome(  ModelMap modelMap ,HttpServletRequest request ,UserEntity userEntity){
        // TODO 获取当前登录人ID
        int studentId = userEntity.getId();
        //查询当前登录学生名下所有班级
        List<Classes> list = studentClassHomeService.findAllClassesByStudentId( studentId );
        modelMap.put("code", CommonInstence.CODE_200);
        modelMap.put("list", list );
        modelMap.put("studentId", userEntity.getId() );
        modelMap.put("nav", "bj");
        request.setAttribute("nav", "bj");

        return "/student/classesHome/ClassesHome";
    }

    /**
     * 学生进入班级列表
     * @param modelMap
     * @param request
     * @param userEntity
     * @return
     */
    @RequestMapping(value ="/goClass"  , method = RequestMethod.GET )
    public String goClass(  ModelMap modelMap ,HttpServletRequest request ,UserEntity userEntity){
        String classesId = request.getParameter( "classesId" );
        //查询 班级
        if( StringUtils.isNotBlank( classesId )){
            List< Integer > classesIdList = studentClassHomeService.findAllClassesIdByStudentId( userEntity.getId() );
            List<User> list = null;
            Map< String ,Object > map = null;
            //只能查询当前学生所在的自己的班级
            if( classesIdList != null && classesIdList.contains(  new Integer( classesId.trim() ))){
                list = studentClassHomeService.findClassStudentByClassesId( new Integer( classesId ) );
                //查询左侧信息
                map = commonService.findLeftInfo( new Integer( classesId ) ,0 , null ,null );
                //班级信息
                modelMap.put( "classes" ,map.get("classes") );
                modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
                //任课老师信息
                modelMap.put( "classesTeacherList" ,map.get("classesTeacherList"));
            }
            modelMap.put("list", list );
        }
        modelMap.put("code", CommonInstence.CODE_200);
        modelMap.put("studentId", userEntity.getId() );
        return "/student/classesHome/ClassStudentList";
    }

    /**
     * 学生加入班级
     * @param modelMap
     * @param request
     * @param userEntity
     * @return
     */
    @RequestMapping(value ="/addStudentToClasses"  , method = RequestMethod.POST )
    public String addStudentToClasses(ModelMap modelMap ,HttpServletRequest request,UserEntity userEntity){
        String classesId = request.getParameter( "classesId" );
        //学生加入班级， 并记录班级动态
        if( StringUtils.isNotBlank( classesId )){
            int num = studentClassHomeService.addStudentToClasses( new Integer( classesId ) ,userEntity.getId() ,userEntity.getRealName() );
            //合心接口--添加学生到班级
            Classes   classes = teacherAddClassService.findClassesById( new Integer( classesId ));
            loginService.hexinAccountRelation(userEntity,classes);
        }else{
            return "/student/classesHome/ClassesHome";
        }
        //成功后跳转
        List<User> list = studentClassHomeService.findClassStudentByClassesId( new Integer( classesId ) );
        //查询左侧信息
        Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,0 , null ,null );
        modelMap.put("list", list );
        //班级信息
        modelMap.put( "classes" ,map.get("classes") );
        modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
        //任课老师信息
        modelMap.put( "classesTeacherList" ,map.get("classesTeacherList"));
        modelMap.put("studentId", userEntity.getId() );
        return "/student/classesHome/ClassStudentList";
    }

    /**
     * 验证学生是否已经加入该班级
     * @param request
     * @param userEntity
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/checkStudentClasses"  , method = RequestMethod.POST )
    public String checkStudentClasses(HttpServletRequest request,UserEntity userEntity){
        String classesCode = request.getParameter( "classesCode" );
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( classesCode )) {
            int result = studentClassHomeService.checkStudentClasses( classesCode , userEntity.getId());
            if( 1 == result ){
                map.put( "code" , 1 );
                map.put( "message" , "编码不存在" );
            }else if( 2 == result ){
                map.put( "code" , 2  );
                map.put( "message" , "已经加入该班级" );
            }else{
                map.put( "code" , CommonInstence.CODE_200 );
                map.put( "classesId" , result );
                map.put( "message" , "验证通过" );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 退出班级
     * @return
     */
    @RequestMapping(value ="/getOutClasses"  , method = RequestMethod.POST )
    public String getOutClasses( HttpServletRequest request,UserEntity userEntity ){
        String classesId = request.getParameter( "classesId" );
        if( StringUtils.isNotBlank( classesId )){
            int studentId = userEntity.getId();
            //验证该班级是否有此学生
            int num = studentClassHomeService.findStudentByClassIdAndStuId( new Integer( classesId ) , studentId );
            if( num > 0 ){
                boolean result = studentClassHomeService.getOutClasses(  new Integer( classesId ) , studentId ,userEntity.getRealName() );
            }
        }
        return "redirect:/student/classHome/goStuClassHome";
    }

    /**
     * 学生的班级动态展示
     * @param request
     * @param userEntity
     * @return
     */
    @RequestMapping(value ="/goClassesDynamic"  , method = RequestMethod.GET )
    public String goClassesDynamic(  HttpServletRequest request,UserEntity userEntity ,ModelMap modelMap ){
        String classesId = request.getParameter( "classesId" );
        String pageNoStr = request.getParameter( "pageNo" );
        if( StringUtils.isNotBlank( classesId )){
            int pageNo = 0 ;
            if( StringUtils.isNotBlank( pageNoStr )){
                pageNo = new Integer( pageNoStr );
            }
            Pager pager =  studentClassHomeService.findClassesDynamic( new Integer( classesId ) ,userEntity.getId() , pageNo );
            //查询左侧信息
            Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,0 , null ,null );
            //班级信息
            modelMap.put( "classes" ,map.get("classes") );
            //班级人数
            modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
            //任课老师信息
            modelMap.put( "classesTeacherList" ,map.get("classesTeacherList"));
            modelMap.put( "classesId" , classesId );
            modelMap.put( "pageInfo" , pager );
            modelMap.put( "dynamicList" , pager.getResultList() );
        }
        return "/student/classesHome/ClassStudentDynamic";
    }
}
