package com.koolearn.cloud.studentHome.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.student.entity.StudentSubject;
import com.koolearn.cloud.student.entity.TeacherSubject;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

import java.util.List;
import java.util.Set;

/**
 * Created by fn on 2016/5/24.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface StudentAllSubjectExamDao {
    /**
     * 查询学生所属所有班级
     * @param studentId
     * @return
     */
    @SQL( "select a.id ,a.full_name ,a.level ,a.range_id,a.range_name, a.subject_id,a.subject_name , a.school_id ,a.type ,a.class_name ,a.year,a.range_name " +
            " from classes a  inner join classes_student b on a.id = b.classes_id " +
            " where a.status=:status and b.status=:status and b.student_id=:studentId and a.type !=3" )
    List<Classes> findAllClassesByStudentId(@SQLParam("studentId")Integer studentId  , @SQLParam("status")Integer status );

    /**
     * 查询某班级下所有老师
     * @param classesId
     * @param status
     * @return
     */
    @SQL( " select teacher_id from classes_teacher where status=:status and classes_id=:classesId " )
    List< Integer> findTeacherIdListByClassesId( @SQLParam("classesId")Integer classesId ,@SQLParam("status")Integer status );

    /**
     * 查询老师所有学科
     * @param teacherId
     * @return
     */
    @SQL( "select subject_id   from teacher_book_version where status=:status and teacher_id=:teacherId  and range_name in ( :rangeNameSet) " )
    List<Integer> findTeacherBookVersionByTeacherId(@SQLParam("teacherId")Integer teacherId ,@SQLParam("status")Integer status ,@SQLParam("rangeNameSet")Set< String > rangeNameSet);

    /**
     * 查询学科下所有作业和课堂
     * @param subjectId
     * @param studentId
     * @return
     */
    @SQL( " select DISTINCT a.id , a.exam_name , a.start_time , a.end_time ,a.type  from tp_exam a inner join tp_exam_student b on a.id = b.exam_id " +
          " where a.subject_id =:subjectId and a.status=:status and ( a.type = 1 or a.type  = 2 ) and  " +
          " b.student_id=:studentId and b.status=1 and a.end_time >= now() and a.start_time <= now()  order by a.end_time desc limit 0,:size" )
    List< TpExam > findSubjectExamList( @SQLParam("subjectId")Integer subjectId, @SQLParam("studentId")Integer studentId ,
                                        @SQLParam("status")Integer status  ,@SQLParam("size")Integer size);

    @SQL( "select a.id , a.exam_name , a.start_time , a.end_time ,a.type  from tp_exam a inner join tp_exam_student b on a.id = b.exam_id " +
            "where a.subject_id =:subjectId and a.status=:status and " +
            " b.student_id=:studentId and b.status=1 and a.end_time < now() order by a.end_time desc limit 0,4" )
    List< TpExam > findSubjectExamByEndTimeList( @SQLParam("subjectId")Integer subjectId, @SQLParam("studentId")Integer studentId ,
                                        @SQLParam("status")Integer status  );
    /**
     * 作业进度状态
     * @param examId
     * @param studentId
     * @return
     */
    @SQL( " select status from te_exam_result where exam_id=:examId and student_id=:studentId order by id desc" )
    List<Integer> findExamProgress(@SQLParam("examId")Integer examId, @SQLParam("studentId")Integer studentId);

    /**
     * 查询学科名称
     * @param subjectId
     * @return
     */
    @SQL( " select name from dictionary where value=:subjectId" )
    String findSubjectNameById(@SQLParam("subjectId")Integer subjectId );

    /**
     * 查询老师姓名
     * @param teacherId
     * @return
     */
    @SQL( "select real_name from user where id=:teacherId" )
    String findUserRealNameByUserId(@SQLParam("teacherId")Integer teacherId);
    //***********************************new rule
    /**
     * 查询留过作业和课堂的所有学科
     * @return
     */
    @SQL( "select distinct a.subject_id as subjectId  from tp_exam a inner join tp_exam_student b on a.id = b.exam_id where b.subject_id is not null " +
            "and a.end_time >= now() and a.start_time <= now() and a.status=:status and b.status=:statusStu " +
          "  and b.student_id =:studentId  limit :beginSize ,:endSize" )
    List<Integer> findExamHistorySubject(@SQLParam("studentId")Integer studentId ,
                                         @SQLParam("beginSize") int beginSize ,
                                         @SQLParam("endSize") int endSize ,
                                         @SQLParam("status")int status ,
                                         @SQLParam("statusStu")int statusStu );
    /**
     * 查询留过作业和课堂的所有学科对应的学段
     * @return
     */
    @SQL( "select distinct a.subject_id as subjectId  " +
          " from tp_exam a inner join tp_exam_student b on a.id = b.exam_id where b.subject_id is not null " +
          " and a.end_time >= now() and a.start_time <= now() and a.status=:status and b.status=:statusStu " +
          " and b.student_id =:studentId  limit :beginSize ,:endSize" )
    List<StudentSubject> findExamHistorySubjectAndRange(@SQLParam("studentId")Integer studentId ,
                                         @SQLParam("beginSize") int beginSize ,
                                         @SQLParam("endSize") int endSize ,
                                         @SQLParam("status")int status,
                                         @SQLParam("statusStu")int statusStu );
    /**
     * 查询留过作业和课堂的所有学科行数
     * @param studentId
     * @param status
     * @param statusStu
     * @return
     */
    @SQL( "select count(distinct a.subject_id) from tp_exam a inner join tp_exam_student b on a.id = b.exam_id where b.subject_id is not null" +
          " and a.end_time >= now() and a.start_time <= now() and a.status=:status and b.status=:statusStu and b.student_id =:studentId  " )
    int findExamHistorySubjectLine(@SQLParam("studentId")Integer studentId ,
                                   @SQLParam("status")int status,
                                   @SQLParam("statusStu")int statusStu);

    /**
     * 查询某个学科下的所有老师
     * @param subjectId
     * @param studentId
     * @return
     */
    @SQL( " select distinct teacher_id from tp_exam_student where status=:status and student_id=:studentId and subject_id=:subjectId " )
    Set<String> findExamSubjectAllTeacher(@SQLParam("status")Integer status , @SQLParam("subjectId")Integer subjectId , @SQLParam("studentId")int studentId);

    /**
     * 查询某学科在作业课堂表的所有班级
     * @param subjectId
     * @param studentId
     * @return
     */
    @SQL( "select distinct classes_id from tp_exam_student where status=:status and subject_id=:subjectId and student_id=:studentId " )
    List< Integer> findAllClassesBySubjectId( @SQLParam("status")Integer status ,@SQLParam("subjectId")Integer subjectId ,@SQLParam("studentId") int studentId );

    /**
     * 根据班级id查询所有老师
     * @param classesId
     * @return
     */
    @SQL( " select b.real_name as teacherName ,b.id as teacherId from classes_teacher a inner join user b " +
            " on a.teacher_id = b.id where a.classes_id =:classesId and a.status=:status group by b.id" )
    List<TeacherSubject> findAllTeacherByclassesId( @SQLParam("classesId")Integer classesId ,@SQLParam("status")Integer status );

    /**
     *
     * @param rangeId
     * @return
     */
    @SQL( "select distinct range_name from teacher_book_version where range_id =:rangeId  and status = 1 limit 0,1 " )
    String findRangeNameBySubjectRangeId( @SQLParam("rangeId")Integer rangeId );

    /**
     * 查询老师负责的所有班级
     * @param teacherIdSet
     * @return
     */
    @SQL( " select b.full_name from classes_teacher a inner join classes b on a.classes_id = b.id " +
          " where a.teacher_id in( :teacherIdList )  and a.`status` = 0 and ( b.type = 0 or ( b.type = 1 and b.subject_id=:subjectId ) ) "  )
    Set<String> findAllClassesByTeacherIds(@SQLParam("teacherIdList")Set<Integer> teacherIdSet ,@SQLParam("subjectId")Integer subjectId );


}
