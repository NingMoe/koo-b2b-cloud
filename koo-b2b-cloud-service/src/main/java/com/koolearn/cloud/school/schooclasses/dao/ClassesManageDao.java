package com.koolearn.cloud.school.schooclasses.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.dto.ClassPageDto;
import com.koolearn.cloud.school.dto.ClassTeacherPageDto;
import com.koolearn.cloud.school.entity.dto.TeacherDto;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/10/31.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface ClassesManageDao {
    /**
     * 班级分页查询
     * @param classPageDto
     * @return
     */
    @SQL(type= SQLType.READ)
    Integer findClassesPageLine(ClassPageDto classPageDto);

    /**
     * 班级分页查询
     * @param classPageDto
      * @return
     */
    @SQL(type= SQLType.READ)
    List<Classes> findClassesPageInfo(ClassPageDto classPageDto);

    /**
     * 查询班级下老师数量
     */
    @SQL( " select count( distinct teacher_id ) from classes_teacher where classes_id =:classesId " )
    Integer findTeacherNumByClassesId(@SQLParam("classesId") Integer classesId);

    /**
     * 查询班级下所有老师名称
     * @param classesId
     * @return
     */
    @SQL( "select  distinct b.real_name   from classes_teacher a inner join user b on a.teacher_id = b.id " +
          " where a.classes_id =:classesId " )
    List<String> findTeacherNameByClassesId(@SQLParam("classesId") Integer classesId);

    /**
     * 查询班级下所有学生数量
     * @param classesId
     * @return
     */
    @SQL(  "select count( student_id ) from classes_student where classes_id=:classesId " )
    Integer findStudentNumByClassesId(@SQLParam("classesId") Integer classesId);

    /**
     * 修改班级激活，关闭状态
     * @param conn
     * @param classesId
     * @param status
     */
    @SQL( " update classes set status=:status where id=:classesId " )
    void closeOrOpenClasses(Connection conn, @SQLParam("classesId") Integer classesId, @SQLParam("status") int status);

    /**
     * 某班级下老师的总数
     * @param classesId
     * @return
     */
    @SQL( " select count( distinct( teacher_id ) ) from classes_teacher where classes_id=:classesId and status=:status" )
    int findClassTeacherTotalPage(@SQLParam("classesId") Integer classesId, @SQLParam("status") Integer status);

    /**
     * 某班级下教师列表
     * @param classesId
     * @param begin
     * @param end
     * @return
     */
    @SQL( " select distinct( b.id ) as userId , b.user_id as ssoUserId , b.real_name as realName ,b.user_name as userName ,b.mobile as mobile ,b.email as email " +
          " from classes_teacher a inner join user b on a.teacher_id = b.id " +
          " where a.classes_id =:classesId and a.status =0 and b.status=0 group by b.id order by convert( b.real_name using gbk) COLLATE gbk_chinese_ci asc limit :begin , :end" )
    List<ClassTeacherPageDto> findClassesTeacherPageList(@SQLParam("classesId") Integer classesId, @SQLParam("begin") Integer begin, @SQLParam("end") Integer end);

    /**
     * 解除教师与班级关系
     * @param teacherId
     * @param classesId
     * @param status_0
     * @return
     */
    @SQL( " update classes_teacher set status = 1 where teacher_id =:teacherId and classes_id=:classesId and status=:status_0 " )
    int updateClassesTeacherStatus(Connection conn, @SQLParam("teacherId") Integer teacherId, @SQLParam("classesId") Integer classesId, @SQLParam("status_0") int status_0);

    /**
     * 解除学生与班级关系
     * @param conn
     * @param studentId
     * @param classesId
     * @param status_0
     * @return
     */
    @SQL( " update classes_student set status = 1 where student_id=:studentId and classes_id=:classesId and status=:status_0 " )
    int updateClassesStudentStatus(Connection conn, @SQLParam("studentId") Integer studentId, @SQLParam("classesId") Integer classesId, @SQLParam("status_0") int status_0);


    /**
     * 查询教师所有的学科
     * @param userId
     * @return
     */
    @SQL( "select subject_name from teacher_book_version where teacher_id=:userId and status=1 " )
    List<String> findTeacherBookVersionByTeacherId(@SQLParam("userId") Integer userId);

    /**
     * 查询班级下所有学生个数
     * @param classesId
     * @param status_0
     * @return
     */
    @SQL( " select count( 1 ) from classes_student  where classes_id=:classesId and status=:status_0 " )
    int findClassStudentTotalPage(@SQLParam("classesId") Integer classesId, @SQLParam("status_0") int status_0);
    /**
     * 查询班级下所有学生列表
     * @param classesId
     * @param begin
     * @param end
     * @return
     */
    @SQL( " select b.id as userId ,b.user_id as ssoUserId ,b.real_name as realName, b.mobile as mobile , b.email as email ,b.user_name as userName , b.student_code as studentCode" +
          " from classes_student a inner join user b on a.student_id = b.id where a.classes_id=:classesId and a.status=:status_0 order by convert( b.real_name using gbk) COLLATE gbk_chinese_ci asc limit :begin ,:end" )
    List<ClassTeacherPageDto> findClassesStudentPageList(@SQLParam("classesId") Integer classesId, @SQLParam("status_0") int status_0, @SQLParam("begin") Integer begin, @SQLParam("end") Integer end);

    /**
     * 查询班级下所有老师
     * @param classesId
     * @return
     */
    @SQL( " select teacher_id from classes_teacher where classes_id=:classesId and subject_id=:subjectId and status=:status_0" )
    List<Integer> findClassesTeacher(@SQLParam("classesId") Integer classesId, @SQLParam("subjectId") Integer subjectId, @SQLParam("status_0") Integer status_0);

    /**
     * 查询某学科下除了当前学科下已加入该班级以外的所有老师
     */
    @SQL( " select b.id as id , b.real_name as name from teacher_book_version a inner join user b on a.teacher_id = b.id " +
          " where b.school_id=:schoolId  and a.subject_id=:subjectId  and a.teacher_id not in(:teacherDtoList )  and a.status =0 and b.status=0 and b.type =1 " )
    List<TeacherDto> findAllTeacherBySubjectAndClass(@SQLParam("teacherDtoList") List<Integer> teacherDtoList, @SQLParam("schoolId") Integer schoolId, @SQLParam("subjectId") Integer subjectId);

    @SQL( " select b.id as id , b.real_name as name from teacher_book_version a inner join user b on a.teacher_id = b.id " +
            " where b.school_id=:schoolId  and a.subject_id=:subjectId  and a.status =0 and b.status=0 and b.type =1 " )
    List<TeacherDto> findAllTeacherBySubjectAndClassNoTeacher(@SQLParam("schoolId") Integer schoolId, @SQLParam("subjectId") Integer subjectId);

    /**
     * 查询当前班级是否存在
     * @param className
     * @param year
     * @param type
     * @param subjectId
     * @param subjectName
     * @param rangeId
     * @param rangeName
     * @return
     */
    @SQL( " select count( 1 ) from classes where class_name=:className and year=:year and type=:type and subject_id=:subjectId and subject_name=:subjectName " +
          " and range_id=:rangeId and range_name=:rangeName and status=0 " )
    int IsExistClassesNum(@SQLParam("className")String className,@SQLParam("year") Integer year, @SQLParam("type")Integer type, @SQLParam("subjectId")Integer subjectId,
                          @SQLParam("subjectName")String subjectName, @SQLParam("rangeId")Integer rangeId, @SQLParam("rangeName")String rangeName);

}
