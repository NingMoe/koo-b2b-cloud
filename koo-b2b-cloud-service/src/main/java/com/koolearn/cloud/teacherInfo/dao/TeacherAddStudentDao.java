package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/4/6.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface TeacherAddStudentDao {
    /**
     * 查询所有的用户账号
     *
     * @return
     */
    @SQL("select user_name from user")
    List<String> findUserNameInUser();

    /**
     * 添加user表
     *
     * @param user
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    Integer insertUser(Connection conn, User user);

    /**
     * 入库ClassesStudent
     *
     * @param conn
     * @param classesStudent
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    Integer insertClassesStudent(Connection conn, ClassesStudent classesStudent);

    /**
     * 查询当前班级所有学生
     *
     * @param classesIdInt
     * @return
     */
    @SQL("select a.id ,a.real_name ,a.user_id ,a.user_name,a.student_code,b.headman  from user a inner join classes_student b on a.id = b.student_id where a.status=0 and b.status =0 and b.classes_id=:classesIdInt")
    List<User> findClassesStudentByClassesId(@SQLParam("classesIdInt") int classesIdInt);

    /**
     * 修改班长
     * java.lang.Integer ;@param conn
     *
     * @param classesStudentPrimary
     * @param status
     * @return
     */
    @SQL("update classes_student set headman=:status where id=:classesStudentPrimary")
    Integer updateStudentJob(Connection conn, @SQLParam("classesStudentPrimary") int classesStudentPrimary, @SQLParam("status") int status);

    /**
     * 删除User学生
     *
     * @param conn
     * @param userId
     * @return
     */
    @SQL("update user set status=:status where id=:userId")
    int deleteUser(Connection conn, @SQLParam("userId") Integer userId, @SQLParam("status") int status);

    /**
     * 删除班级或小组的学生
     * @param conn
     * @param userId
     * @param classesId
     * @param status
     * @ return
     */
    @SQL("update classes_student set status=:status where student_id=:userId and classes_id=:classesId")
    int deleteClassesStudent(Connection conn ,@SQLParam("userId")int userId ,@SQLParam("classesId")int classesId ,@SQLParam("status")int status );


    /**
     * 查询某班级下的所有小组
     * @param classesId
     * @param status
     * @return
     */
    @SQL("select id ,class_name ,class_code,range_id ,subject_id , subject_name ,year, range_name,full_name from classes where parent_id=:classesId and status=:status ")
    List<Classes> findClassesByParentId(@SQLParam("classesId")int classesId ,@SQLParam("status")int status );

    /**
     * 根据学生学号查询学生信息
     * @param studentCode
     * @return
     */
    @SQL("select id ,real_name ,user_name from user where student_code=:studentCode")
    List<User> findUserByStudentCode(@SQLParam("studentCode")String studentCode );

    /**
     * 修改学生信息
     * @param userId
     * @param realName
     * @param studentCode
     * @return
     */
    @SQL("update user set real_name=:realName , student_code=:studentCode where id=:userId" )
    int updateRealNameAndStudentCode(@SQLParam("userId")Integer userId , @SQLParam("realName")String realName , @SQLParam("studentCode")String studentCode );

    /**
     * 根据班级id查询班级学生
     * @param classesId
     * @param studentId
     * @return
     */
    @SQL("select id ,student_id from classes_student where status = 0 and student_id=:studentId and classes_id=:classesId" )
    List< ClassesStudent> findClassesStudentByClassesIdAndStudentId(@SQLParam("classesId")Integer classesId ,@SQLParam("studentId")Integer studentId );

    /**
     * 判断当前学生和小组是否存在
     * @param studentId
     * @param classesId
     * @param status
     * @return
     */
    @SQL("select id , headman ,student_id from classes_student where status =:status and student_id=:studentId and classes_id=:classesId")
    ClassesStudent findStudentTeamExist(@SQLParam("studentId")Integer studentId,@SQLParam("classesId") Integer classesId ,@SQLParam("status") int status);

    /**
     * 查询当前老师自身创建的小组
     * @param techerId
     * @param classesId
     * @param status
     * @return
     */
    @SQL("select id ,class_name ,class_code,range_id ,subject_id , subject_name ,year, range_name,full_name " +
         " from classes where parent_id=:classesId and status=:status and teacher_id=:techerId ")
    List< Classes > findTeacherTeamClassesByParentId(@SQLParam("techerId")Integer techerId , @SQLParam("classesId")int classesId ,@SQLParam("status") int status);

    /**
     * 查询班级id
     * @param classNo
     * @return
     */
    @SQL("select id from classes where class_code=:classNo and status=0")
    Integer findClassesIdByClassCode(@SQLParam("classNo")String classNo);

    }
