package com.koolearn.cloud.school.homePage;


import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.homePage.dao.HomePageDao;
import com.koolearn.cloud.teacher.entity.School;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fn on 2016/10/12.
 */
public class HomePageServiceImpl implements HomePageService {

    private Logger log = Logger.getLogger( this.getClass() );

    private HomePageDao homePageDao;

    public HomePageDao getHomePageDao() {
        return homePageDao;
    }
    public void setHomePageDao(HomePageDao homePageDao) {
        this.homePageDao = homePageDao;
    }

    /**
     *  通过学校主键查询学校信息
     * @param schoolId
     * @return
     */
    @Override
    public School findSchoolInfoById(Integer schoolId) {
        School school = homePageDao.findSchoolInfoById( schoolId , CommonConstant.STATUS_0 );
        return school;
    }

    /**
     * 查询学校教师，学生，班级的数量
     * @param schoolId
     * @return
     */
    @Override
    public Map<String, Object> findShoolPropertyNum(Integer schoolId) {
        Integer teacherNum = homePageDao.findTeacherNumBySchoolId( schoolId ,CommonConstant.TYPE_1 ,CommonConstant.STATUS_0);
        Integer studentNum = homePageDao.findTeacherNumBySchoolId( schoolId ,CommonConstant.TYPE_2 ,CommonConstant.STATUS_0);
        Integer classesNum = homePageDao.findClassesNumBySchoolId( schoolId );
        Map< String , Object > map = new HashMap<String, Object>();
        map.put( "teacherNum" , teacherNum);
        map.put( "studentNum" , studentNum );
        map.put( "classesNum" , classesNum);
        return map;
    }








}
