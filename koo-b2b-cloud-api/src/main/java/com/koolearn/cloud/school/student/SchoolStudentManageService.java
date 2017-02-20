package com.koolearn.cloud.school.student;

import com.koolearn.cloud.school.student.vo.StudentPageVo;

import java.util.Map;

/**
 * Created by fn on 2016/11/10.
 */
public interface SchoolStudentManageService {
    /**
     * 学生分页查询
     * @param studentPageVo
     * @return
     */
    Map<String,Object> findStudentPage(StudentPageVo studentPageVo);











}
