package com.koolearn.cloud.school.homePage.controller;


import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.school.homePage.HomePageService;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by fn on 2016/10/12.
 */
@RequestMapping("/school/homePage")
@Controller
public class HomePageController extends BaseController {
    @Autowired
    private HomePageService homePageService;

    @RequestMapping(value="/test" )
    public String test(HttpServletRequest request){
        return null;
    }

    /**
     * 跳转到学校首页
     * @param request
     * @param response
     * @return
     *  cloud.trunk.koolearn.com/school/homePage/goHomePage?userId=1&schoolId=334487
     */
    @RequestMapping(value="/goHomePage" ,method = RequestMethod.GET)
    public String goHomePage(HttpServletRequest request , HttpServletResponse response ,ModelMap modelMap ){
        String schoolId = request.getParameter( "schoolId" );
        if(StringUtils.isNotEmpty( schoolId )){
            School school = homePageService.findSchoolInfoById( new Integer( schoolId) );
            //查询学校教师，学生，班级的数量
            Map< String , Object > map = homePageService.findShoolPropertyNum( new Integer( schoolId) );
            if( null != school ){
                modelMap.put( "school" , school );
                Date endTime = school.getEndTime();
                if( null == endTime ){
                    modelMap.put( "endTime" , "" );
                }else{
                    modelMap.put( "endTime" , DateFormatUtils.getDay( school.getEndTime()) );
                }
            }
            if( null != map ){
                modelMap.put( "teacherNum",map.get( "teacherNum"));
                modelMap.put( "studentNum",map.get( "studentNum"));
                modelMap.put( "classesNum",map.get( "classesNum"));
            }
        }
        return "/school/homePage";
    }

}
