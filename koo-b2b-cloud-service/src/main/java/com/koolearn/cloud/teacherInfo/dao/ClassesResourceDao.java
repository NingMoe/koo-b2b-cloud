package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.teacher.entity.ExamNum;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.common.page.ListPager;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询班级课堂和作业资源
 * Created by fn on 2016/4/14.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface ClassesResourceDao {
    /**
     * 分页查询班级资源总行数
     * @param classesId
     * @return
     */
    @SQL(" select sum( c.num ) from (  select count(1) as num  from tp_exam_student a INNER JOIN tp_exam b on a.exam_id = b.id " +
            "where a.classes_id =:classesId and b.status = 4 group by b.id   ) as c ")
    int findPageLineTpExamByClassesId(@SQLParam("classesId")int classesId);

    /**
     * 查询班级下所有资源
     * @param classesId
     * @return
     */
    @SQL("select b.id, b.exam_name , b.start_time , b.end_time ,b.type from tp_exam_student a INNER JOIN tp_exam b on a.exam_id = b.id " +
         "where a.classes_id =:classesId and b.status = 4 group by b.id ORDER BY b.type , b.create_time desc ")
    List<TpExam> findTpExamByClassesId(@SQLParam("classesId")int classesId);

    /**
     * 分页查询班级班级或课堂的总行数
     * @param classesId
     * @param typeId
     * @return
     */
    @SQL("select sum( c.num ) from (  select count(1) as num from tp_exam_student a INNER JOIN tp_exam b on a.exam_id = b.id " +
            "where b.type=:typeId and a.classes_id =:classesId and b.status = 4   ) as c")
    int findPageLineTpExamByClassesIdAndType(@SQLParam("classesId")int classesId ,@SQLParam("typeId")String typeId);

    /**
     * 分类查询班级下所有资源
     * @param classesId
     * @param typeId
     * @return
     */
    @SQL("select b.id, b.exam_name , b.start_time , b.end_time ,b.type from tp_exam_student a INNER JOIN tp_exam b on a.exam_id = b.id " +
            "where b.type=:typeId and a.classes_id =:classesId and b.status = 4 ORDER BY b.type , b.create_time desc")
    List<TpExam> findTpExamByClassesIdAndType(@SQLParam("classesId")int classesId ,@SQLParam("typeId")String typeId);

    /**
     * 完成作业的学生个数
     * @param examId
     * @return
     */
    @SQL(" select count( 1 ) from te_exam_result where exam_id =:examId and status = 2")
   int findZuoYeDoneNum(@SQLParam("examId")Integer examId);

    /**
     * 查询每个课堂的全部附件
     * @param examId
     * @param status
     * @return
     */
    @SQL(" select id ,exam_id , attachment_id  from tp_exam_attachment where status =:status and exam_id =:examId and attachment_type != 2" )
    List<TpExamAttachment> findAttachmentList(@SQLParam("examId")Integer examId ,@SQLParam("status")Integer status);


    /**
     * 查询当前学生的每个课堂的全部附件
     * @param examId
     * @return
     */
    @SQL(" select a.id ,a.exam_id , a.attachment_id  from tp_exam_attachment a INNER JOIN tp_exam_student b on a.exam_id=b.exam_id " +
         " where a.status =:status and a.exam_id =:examId and a.attachment_type != 2  and b.student_id =:studentId and b.exam_id =:examId " )
    List<TpExamAttachment> findAttachmentStudentList(@SQLParam("examId")Integer examId ,@SQLParam("status")Integer status ,@SQLParam("studentId")int studentId );

    /**
     * 查询课堂下所有作业
     * @param examId
     * @param status
     * @return
     */
    @SQL(" select id ,exam_id , attachment_id  from tp_exam_attachment where status =:status and exam_id =:examId and attachment_type = 2" )
    List<TpExamAttachment> findExamAttachmentZuoye(@SQLParam("examId")Integer examId ,@SQLParam("status")Integer status);

    /**
     * 查询当前学生下所有课堂作业
     * @param examId
     * @param status
     * @return
     */
    @SQL(" select a.id ,a.exam_id , a.attachment_id  from tp_exam_attachment a INNER JOIN tp_exam_student b on a.attachment_id=b.exam_id " +
         " where a.status =:status and a.attachment_id =:examId and a.attachment_type = 2 and b.student_id =:studentId " )
    List<TpExamAttachment> findStudentAttachementZuoye(@SQLParam("examId")Integer examId ,@SQLParam("status")Integer status ,@SQLParam("studentId")Integer studentId);



    /**
     * 判断学生是否完成当前的附件
     * @param studentId
     * @param
     * @return
     */
    @SQL("select count( 1 ) from tp_exam_attachment_complete where exam_attachment_id=:examAttachmentId and student_id=:studentId")
    int findStudentReadAttachmentLine(@SQLParam("studentId")Integer studentId,@SQLParam("examAttachmentId") Integer examAttachmentId);



    //升级查询班级和老师下的所有作业和课堂

    /**
     * 班级和老师下的所有作业和课堂行数
     * @param teamIdList
     * @param teacherId
     * @return
     */
    @SQL( "SELECT count( num ) from ( select count( 1 ) as num from tp_exam_student a inner join tp_exam b on a.exam_id = b.id  where a.classes_id in ( :teamIdList ) and b.status=4 and a.status = 1 and a.teacher_id =:teacherId group by a.exam_id ) c " )
    int findExamLineByClassesIdAndTeacherId(@SQLParam("teamIdList")List< Integer> teamIdList ,@SQLParam("teacherId") int teacherId);

    /**
     * 按照作业或课堂类型查询总行数
     * @param classesIdList
     * @param teacherId
     * @param typeId
     * @return
     */
    @SQL( "SELECT count( num ) from ( select count( 1 ) as num from tp_exam_student a inner join tp_exam b on a.exam_id = b.id  where a.classes_id in (:classesIdList) and b.status=4 and a.status = 1 and a.teacher_id =:teacherId and b.type=:typeId group by a.exam_id ) c " )
    int findExamLineByClassesIdAndTeacherIdAndType(@SQLParam("classesIdList")List<Integer> classesIdList ,@SQLParam("teacherId") int teacherId ,@SQLParam("typeId")String typeId);



    /**
     * 查询班级和老师下的所有作业和课堂
     * @param classesIds
     * @param teacherId
     * @return
     */
    @SQL(type=SQLType.READ)
    List<TpExamStudent > findExamByClassesIdAndTeacherId( @SQLParam("classesIds")String classesIds ,
                                                          @SQLParam("teacherId") int teacherId,
                                                          @SQLParam("begin") int begin ,
                                                          @SQLParam("end") int end );

    /**
     * 根据班级，老师，作业或课堂类型查询课堂资源列表
     * @param
     * @param teacherId
     * @param begin
     * @param end
     * @return
     */
    @SQL( "select count( 1 )  as studentNum ,a.classes_id , a.exam_id ,b.exam_name,b.type ,b.create_time ,b.start_time ,b.end_time " +
          " from tp_exam_student a inner join tp_exam b on a.exam_id = b.id  " +
          " where b.type=:typeId and a.classes_id in( :classesIdList ) and a.teacher_id =:teacherId  and b.status = 4 and a.status = 1" +
          " group by a.exam_id  order by b.create_time desc limit :begin , :end " )
    List<TpExamStudent > findExamByClassesIdAndTeacherIdAndType( @SQLParam("classesIdList")List<Integer> classesIdList ,
                                                                 @SQLParam("teacherId")int teacherId ,
                                                                 @SQLParam("begin") int begin ,
                                                                 @SQLParam("end") int end ,
                                                                 @SQLParam("typeId")String typeId );
    /**
     * 查询参与每个课堂的所有学生
     * @param examId
     * @param classesId
     * @param teacherId
     * @return
     */
    @SQL("select student_id , exam_id ,type from tp_exam_student where classes_id =:classesId  and teacher_id =:teacherId and exam_id=:examId and status=1")
    List<TpExamStudent> findKeTangStudents(@SQLParam("examId")int examId,@SQLParam("classesId") int classesId,@SQLParam("teacherId") int teacherId);

    /**
     * 查询课堂作业是否完成
     * @param studentId
     * @param examId
     * @return
     */
    @SQL( " select count( 1 ) from te_exam_result where exam_id=:examId and student_id=:studentId and status=:status" )
    int findExamZuoyeLine( @SQLParam("studentId")Integer studentId, @SQLParam("examId")Integer examId , @SQLParam("status")Integer status);

    /**
     * 查询班级下所有小组
     * @param classesId
     * @return
     */
    @SQL( "select id from classes where parent_id=:classesId" )
    List<Integer> findAllTeamByClassesId( @SQLParam("classesId")int classesId);

    /**
     * 查询主课堂下 的所有附件和课堂作业
     * @param examId
     * @return
     */
    @SQL( " select id,exam_id , attachment_id , attachment_type from tp_exam_attachment where exam_id=:examId and status=:status " )
    List<TpExamAttachment> findAllZuoYeByExamId( @SQLParam("examId")int examId ,@SQLParam("status")int status);

    /**
     * 查询每个小组或班级完成作业的人数
     * @param examIdKeTang
     * @param status
     * @return
     */
    @SQL( " select count( * ) as num , classes_id as classesId from tp_exam_student where exam_id=:examId and status=:status GROUP BY classes_id " )
    List< ExamNum > findExamKeTang(@SQLParam("examId")int examIdKeTang ,@SQLParam("status")Integer status);

    /**
     * 查询所有做作业的学生
     * @param attachmentIds （ 作业id集合 ）
     * @return
     */
    @SQL( " select  DISTINCT student_id as studentId ,COUNT( student_id ) num from tp_exam_student where exam_id =:attachmentIds GROUP BY student_id " )
    List< ExamNum > findExamKeTangStudents(@SQLParam("attachmentIds")String attachmentIds);

    /**
     * 查询当前学生完成作业的次数
     * @param attachmentIds
     * @param studentId
     * @return
     */
    @SQL(type=SQLType.READ)
    Integer findStudentDoneNumByExamId( String attachmentIds,  Integer studentId );

    /**
     * 所有参与完成课堂附件的学生
     * @param examId
     * @param status
     * @return
     */
    @SQL( " select student_id  from tp_exam_student where exam_id=:examId and status=:status " )
    List< Integer> findExamStudents(@SQLParam("examId")Integer examId ,@SQLParam("status")int status);

    /**
     * 查询用户最早（有效班级）加入班级的时间
     * @param userId
     * @return
     */
    @SQL(" select ")
    Date findUserOutInClassDate(Integer userId);
}
