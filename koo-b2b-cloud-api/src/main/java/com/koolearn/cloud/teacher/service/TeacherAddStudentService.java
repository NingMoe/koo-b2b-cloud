package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.exam.base.service.BaseService;

import java.util.List;

/**
 * Created by fn on 2016/4/6.
 */
public interface TeacherAddStudentService extends BaseService {
    /**
     * 验证SSO学号，并入库
     * @param studentStr
     * @return
     */
    List<User> checkStudentNameAndInsert(String studentStr , String classNo) throws Exception;

    /**
     * 批量添加学生添加消息队列
     * @param studentStr
     * @param classNo
     * @param classesId
     * @param userEntity
     */
    public void setStudentInfoQueue( String studentStr , String classNo ,int classesId ,UserEntity userEntity);


    /**
     * 插入用户表和学生表
     * @param userList
     * @param classesId
     */
    List<User> insertUserAndStudent(List<User> userList ,int classesId,UserEntity userEntity) throws Exception;



    /**
     * 查询未分组的学生
     * @param classesId
     * @param subjectId
     * @;return
     */
    List<ClassesStudent> findNoClassesTeamStudents(Integer classesId , Integer subjectId ,Integer teacherId );

    /**
     * 将学生分组入库
     * @param classesId ：班级主键
     * @param studentIds
     * @return
     */
    List< ClassesStudent > insertClassesStudents(Integer classesId , String studentIds );

    /**
     * 设置组长
     * @param newClassesStudentPrimary :新组长的主键
     * @param oldClassesStudentPrimary :原组长的主键
     * @return
     */
    int updateStudentJob(Integer newClassesStudentPrimary ,Integer oldClassesStudentPrimary) throws Exception;

    /**
     * 查询当前班级所有学生
     * @param classesIdInt
     * @return
     */
    List<User> findClassesStudentByClassesId(Integer userId ,int classesIdInt);

    /**
     * 删除学生
     * @param classesId
     * @param userId
     * @return
     */
    int deleteUserAndClassStudent(Integer classesId, Integer userId);

    /**
     * 修改学生学号和姓名
     * @param userId
     * @param realName
     * @param studentCode
     * @return
     */
    int updateRealNameAndStudentCode(Integer userId, String realName, String studentCode) throws Exception;

    /**
     * 通过班级编码查询班级
     * @param classesCode
     * @return
     */
	Classes findClassesByCode(String classesCode);
}

