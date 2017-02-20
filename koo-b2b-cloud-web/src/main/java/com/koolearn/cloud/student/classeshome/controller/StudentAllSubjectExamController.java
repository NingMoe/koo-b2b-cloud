package com.koolearn.cloud.student.classeshome.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.student.StudentAllSubjectExamService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fn on 2016/5/24.
 */
@RequestMapping("/student/allSubject")
@Controller
public class StudentAllSubjectExamController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentAllSubjectExamService studentAllSubjectExamService;

    /**
     * 查询当前学生所有班级的所有学科作业和课堂
     * @return
     * cloud.trunk.koolearn.com/student/allSubject/findAllSubjectExam
     */
    @RequestMapping(value ="/findAllSubjectExam"  , method = RequestMethod.GET )
    public String findAllSubjectExam( ModelMap modelMap ,HttpServletRequest request ,UserEntity userEntity ){
        Integer studentId = userEntity.getId();
        String pageNo = request.getParameter( "pageNo" );
        if(StringUtils.isBlank( pageNo )){
            pageNo = "0";
        }
        if( null != studentId ){
            Pager pager = null;
            try{
                pager = studentAllSubjectExamService.findAllSubjectsByExam( studentId ,new Integer( pageNo ));
            }catch( Exception e ){
                log.error( "学生端首页查询异常" + e.getMessage() , e  );
            }
            if( null == pager.getResultList() || pager.getResultList().size() == 0 ){
                modelMap.put("message", "没有收到课堂及作业" );
            }
            modelMap.put( "studentSubjectList" ,pager.getResultList() );
            modelMap.put( "pageInfo" ,pager );
            modelMap.put("studentId", studentId);
        }
        return "/student/classesHome/StudentAllSubjectExam";
    }












}
