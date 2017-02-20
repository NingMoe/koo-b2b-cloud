package com.koolearn.cloud.school.homePage;


import com.koolearn.cloud.teacher.entity.School;

import java.util.Map;

/**
 * Created by fn on 2016/10/12.
 */
public interface HomePageService {

    /**
     * 查询学校信息
     * @param schoolId
     * @return
     */
    public School findSchoolInfoById(Integer schoolId);

    /**
     * 查询学校教师，学生，班级的数量
     * @param schoolId
     * @return
     */
    Map<String,Object> findShoolPropertyNum(Integer schoolId);
}
