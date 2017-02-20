package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesExam;
import com.koolearn.cloud.common.entity.HomePageClassesExam;
import com.koolearn.cloud.common.entity.User;
import scala.Int;

import java.util.List;

/**
 * Created by fn on 2016/4/5.
 */
public interface ClassHomePageService {
    /**
     * 查询老师名下所有班级
     * @param teacherId
     * @return
     */
    List<Classes> findTeacherClassById(int teacherId ,int classesType ,int status );

    /**
     * 查询班级下课程和作业
     * @return
     */
    List<HomePageClassesExam> findExamInfoByClassId( List<Classes > classList , Integer teacherId);

    /**
     * 根据User主键查询user
     * @param teacherId
     * @return
     */
    User findUserInfoById(int teacherId);

    /**
     * 老师下面的班级动态
     * @param teacherId
     * @param classesType
     * @param status
     * @return
     */
    public List<Classes> findTeacherClassForDynamicById(int teacherId,int classesType ,int status);
}
