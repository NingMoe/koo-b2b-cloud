package com.koolearn.cloud.school.schooclasses;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.school.dto.ClassPageDto;
import com.koolearn.cloud.school.entity.dto.TeacherDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/10/31.
 */
public interface ClassesManageService {
    /**
     * 班级信息分页查询
     * @param classPageDto
     * @return
     */
    Map<String,Object> findClassPage(ClassPageDto classPageDto);

    /**
     * 修改班级状态（激活，关闭 ）
     * @param operType
     * @param classesId
     */
    void closeOrOpenClasses(String operType, Integer classesId) throws SQLException;

    /**
     * 批量插入班级信息
     * @param classesList
     * @return
     */
    int insertClassesLargeInfo(List<Classes> classesList) throws SQLException;

    /**
     * 查询班级下所有教师
     * @param classPageDto
     * @return
     */
    Map<String,Object> findClassTeacherPage(ClassPageDto classPageDto);
    /**
     * 查询班级下所有学生
     * @param classPageDto
     * @return
     */
    Map<String,Object> findClassStudentPage(ClassPageDto classPageDto);

    /**
     * 解除教师与班级关系
     * @param userId
     * @param classesId
     */
    void updateClassesTeacherOrStudentStatus(Integer userId, Integer classesId, String type) throws SQLException;

    /**
     * 查询某学科下除了当前学科下已加入该班级以外的所有老师
     */
    List<TeacherDto> findAllTeacherBySubjectAndClass(Integer schoolId, Integer subjectId, Integer classesId);

    /**
     * 批量添加班级下教师
     * @param classes
     * @param teacherIdStr
     * @param subjectIdStr
     */
    void addClassesTeacher(Classes classes, String teacherIdStr, String subjectIdStr);

}
