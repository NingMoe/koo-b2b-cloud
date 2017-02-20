package com.koolearn.cloud.classRoom.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.entity.*;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;
import java.sql.Connection;
import java.util.List;

@DAO(dbtype = DbType.MYSQL, source = GlobalConstant.MYSQL_DATASOURCE)
public interface ClassRoomDao {

    @SQL(type = SQLType.READ)
    int findTeacherClassRoomPageCount(@SQLParam("subjectId") int subjectId, @SQLParam("searchValue") String searchValue, @SQLParam("userId") int userId);

    @SQL(type = SQLType.READ)
    List<TpExam> findTeacherClassRoomPage(@SQLParam("subjectId") int subjectId, @SQLParam("searchValue") String searchValue,
                                          @SQLParam("userId") int userId, int pageNo);

    @SQL("update tp_exam set status = 3 where id = :id ")
    void deleteClassRoom(Connection conn, @SQLParam("id") int classRoomId);


    @SQL("update tp_exam_attachment set attachment_name = :attachmentName where attachment_id = :attachmentId ")
    void updateAttachmentName(@SQLParam("attachmentId") int attachmentId, @SQLParam("attachmentName") String attachmentName);

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int create(TpExam classRoom);


    @SQL("select distinct c.* ,'false' as ok from classes c , classes_student cs  " +
            "where c.id = cs.classes_id and c.teacher_id = :teacherId and c.type = 3 " +
            "and c.status = 0 and c.parent_id = :classesId and cs.status = 0")
    List<Classes> findClassesGroupByTeacherId(@SQLParam("teacherId") int teacherId, @SQLParam("classesId") int classesId);

    @SQL("select distinct c.* ,'false' as ok from classes c , classes_student cs  " +
            "where c.id = cs.classes_id and c.teacher_id = :teacherId and c.type = 3 " +
            "and c.status = 0 and c.parent_id = :classesId and cs.status = 0 and c.subject_id = :subjectId")
    List<Classes> findClassesGroupByTeacherIdSubjectId(@SQLParam("subjectId") int subjectId, @SQLParam("teacherId") int teacherId, @SQLParam("classesId") int classesId);


    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveClassRoomAttachment(Connection conn, TpExamAttachment ta);

    @SQL("select student_id from classes_student where classes_id = :classesId and status = 0")
    List<Integer> findStudentByClassesId(@SQLParam("classesId") int classesId);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveTpExamStudent(Connection conn, TpExamStudent ts);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveTpExamStudent(Connection conn, List<TpExamStudent> tsList);

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveTpExam(Connection conn, TpExam te);

    @SQL("select distinct c.full_name from classes c ,tp_exam_student tc where c.id = tc.classes_id and tc.exam_id = :classRoomId and tc.status = 1 order by c.full_name")
    List<String> findClassesFullNameByClassRoomId(@SQLParam("classRoomId") int classRoomId);

    @SQL("select distinct c.full_name from classes c ,tp_exam_student tc where c.id = tc.classes_id and tc.exam_id = :classRoomId and tc.status = 1 order by c.full_name")
    List<String> findClassesFullNameByClassRoomId(Connection conn, @SQLParam("classRoomId") int classRoomId);

    @SQL("select id,full_name,parent_id,type from classes where id in (:classesIds) order by full_name")
    List<Classes> findClassesById(@SQLParam("classesIds") String[] classesIds);

    @SQL("select full_name from classes where id in (:classesIds) order by full_name")
    List<String> findClassesFullNameByIds(@SQLParam("classesIds") String[] classesIds);

    @SQL("select *, if(end_time < sysdate(),0,1) as finishStatus from tp_exam where id = :id ")
    TpExam getClassRoomById(@SQLParam("id") int id);

    @SQL("select * from tp_exam_attachment where exam_id = :id and status = 1 order by sort asc ")
    List<TpExamAttachment> findAttachmentByClassRoomId(@SQLParam("id") int id);

    @SQL("select distinct c.id from classes c ,tp_exam_student tc where tc.exam_id = :id and tc.status = 1 and tc.classes_id = c.id order by c.full_name")
    List<String> findClassesIdByClassRoomId(@SQLParam("id") int id);

    @SQL(type = SQLType.WRITE_UPDATE)
    int updateTpExam(Connection conn, TpExam te);

    @SQL("update tp_exam_attachment set status = 0 where exam_id = :classRoomId")
    void deleteClassRoomAttachment(Connection conn, @SQLParam("classRoomId") int classRoomId);

    @SQL("update tp_exam_student set status = 0 ,view = 0 where exam_id = :classRoomId")
    void deleteClassRoomStudent(Connection conn, @SQLParam("classRoomId") int classRoomId);

    @SQL("select distinct c.* from classes c ,tp_exam_student tc where c.id = tc.classes_id and tc.exam_id = :classRoomId and tc.status = 1 order by c.full_name")
    List<Classes> findClassesByClassRoomId(@SQLParam("classRoomId") int classRoomId);

    @SQL("select * from tp_exam_attachment where id = :id")
    TpExamAttachment getTpExamAttachmentById(@SQLParam("id") int id);

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveTpExamAttachmentComment(TpExamAttachmentComment comment);

    @SQL("select * from tp_exam_attachment_comment where exam_attachment_id = :examAttachmentId and parent_id is null order by create_time desc ,id desc")
    List<TpExamAttachmentComment> findCommentByExamAttachmentId(@SQLParam("examAttachmentId") int examAttachmentId);

    @SQL("select * from tp_exam_attachment_comment where parent_id = :id order by create_time desc ,id desc ")
    List<TpExamAttachmentComment> findCommentByParentId(@SQLParam("id") int id);

    @SQL(type = SQLType.READ_BY_ID)
    Classes getClassesById(int id);

    @SQL("select * from tp_exam_student where exam_id = :examId and status = 1 ")
    List<TpExamStudent> findTpExamStudent(@SQLParam("examId") int examId);

    @SQL("select distinct u.id,u.real_name from tp_exam_student t,user u where t.exam_id = :classRoomId and t.classes_id = :classesId and t.status = 1 and t.student_id = u.id")
    List<User> findStudentNameByClassRoomId(@SQLParam("classRoomId") int classRoomId, @SQLParam("classesId") int classesId);

    @SQL("select distinct u.id,u.real_name from tp_exam_attachment_complete t ,user u ,tp_exam_student ts  where t.exam_attachment_id = :tpExamAttachmentId and t.student_id = u.id and u.id = ts.student_id and ts.classes_id = :classesId and ts.status = 1")
    List<User> findFinishAttachment(@SQLParam("tpExamAttachmentId") int tpExamAttachmentId, @SQLParam("classesId") int classesId);

    @SQL("select distinct u.id,u.real_name from tp_exam_student ts , te_exam_result tr,user u where  ts.exam_id = :attachmentId and ts.exam_id = tr.exam_id and ts.classes_id in (:classesId) and ts.status= 1 and ts.student_id = tr.student_id and tr.status = 2 and  tr.student_id = u.id")
    List<User> findFinishExamResult(@SQLParam("attachmentId") int attachmentId, @SQLParam("classesId") List<Integer> classesId);

    @SQL(type = SQLType.READ)
    int findStudentClassRoomCount(@SQLParam("studentId") int studentId, @SQLParam("subjectId") int subjectId, @SQLParam("searchValue") String searchValue, @SQLParam("endTimeStr") String endTimeStr);

    @SQL(type = SQLType.READ)
    List<TpExam> findStudentClassRoom(@PageBy ListPager listPager, @SQLParam("studentId") int studentId, @SQLParam("subjectId") int subjectId, @SQLParam("searchValue") String searchValue, @SQLParam("endTimeStr") String endTimeStr);

    @SQL("select id from tp_exam_attachment where exam_id = :classRoomId and status = 1 order by sort asc")
    List<String> findAttachmentIdsByClassRoomId(@SQLParam("classRoomId") int classRoomId);

    @SQL("select count(1) from tp_exam_student ts,tp_exam t where ts.student_id = :studentId and ts.subject_id = :subjectId " +
            "and ts.status = 1 and ts.type = 2 and ts.view = 1 and ts.exam_id = t.id and ts.subject_id = t.subject_id " +
            "and t.status = 4 and t.start_time < sysdate() and t.type = 2")
    int findSubjectView(@SQLParam("studentId") int studentId, @SQLParam("subjectId") int subjectId);

    @SQL("update tp_exam_student set view = 0 where student_id = :studentId and subject_id = :subjectId and status = 1 and type = 2 and view = 1")
    void deleteSubjectView(@SQLParam("studentId") int studentId, @SQLParam("subjectId") int subjectId);

    @SQL("select count(id) from tp_exam_student where exam_id = :examId and student_id =:studentId and status = 1")
    int isExistExamByStudent(@SQLParam("examId") int examId, @SQLParam("studentId") int studentId);

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveNote(TpExamStudentNote t);

    @SQL("select * from tp_exam_student_note where student_id = :studentId and exam_attachment_id = :tpExamAttachmentId order by create_time desc")
    List<TpExamStudentNote> findNote(@SQLParam("studentId") int studentId, @SQLParam("tpExamAttachmentId") int tpExamAttachmentId);

    @SQL("select count(1) from tp_exam_attachment_complete where student_id = :studentId and exam_attachment_id = :tpExamAttachmentId")
    int getComplete(@SQLParam("studentId") int studentId, @SQLParam("tpExamAttachmentId") int tpExamAttachmentId);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveComplete(TpExamAttachmentComplete t);

    @SQL("select count(1) from te_exam_result where student_id = :studentId and exam_id = :tpExamId and status = 2")
    int getExamComplete(@SQLParam("studentId") int studentId, @SQLParam("tpExamId") int tpExamId);

    @SQL("select count(1) from tp_exam_student where student_id = :studentId and exam_id = :tpExamId and status = 1")
    int getClassRoom(@SQLParam("tpExamId") int tpExamId, @SQLParam("studentId") int studentId);

    @SQL("select count(1) from classes_student where classes_id = :classesId and status = 0")
    int isExistClassesStudentByClassesId(@SQLParam("classesId") int classesId);

    @SQL("update tp_exam_student set finish_time = sysdate() where exam_id = :tpExamId and student_id = :studentId and status = 1")
    void updateFinishTime(@SQLParam("studentId") int studentId, @SQLParam("tpExamId") int tpExamId);

    @SQL("select DISTINCT exam_id,paper_id,student_id,status,type,subject_id,view,finish_time from tp_exam_student where exam_id = :tpExamId and student_id = :studentId and status = 1")
    TpExamStudent getTpExamStudent(@SQLParam("studentId") int studentId, @SQLParam("tpExamId") int tpExamId);

    @SQL("select ter.*,te.end_time from te_exam_result ter ,tp_exam te where te.id = :tpExamId and te.id = ter.exam_id and ter.student_id = :studentId")
    TaskDto getStudentTpExamResult(@SQLParam("tpExamId") int tpExamId, @SQLParam("studentId") int studentId);

    @SQL("select id,class_name as fullName from classes where parent_id = :classesId and teacher_id=:teacherId and subject_id = :subjectId and status = 0 order by year desc ,convert( class_name USING gbk) COLLATE gbk_chinese_ci asc")
    List<Classes> findClassesByParentId(@SQLParam("subjectId") int subjectId, @SQLParam("classesId") int classesId, @SQLParam("teacherId") int teacherId);

    @SQL("select distinct u.id,u.real_name from tp_exam_student ts ,user u where ts.exam_id = :examId and ts.classes_id in (:classesIds) and ts.status= 1 and ts.student_id = u.id ")
    List<User> findUnFinishExamResult(@SQLParam("examId") int examId, @SQLParam("classesIds") List<Integer> classesIds);

    @SQL("select distinct u.id,u.real_name from tp_exam_student t,user u where t.exam_id = :classRoomId and t.status = 1 and t.student_id = u.id")
    List<User> findAllStudentNameByClassRoomId(@SQLParam("classRoomId") int classRoomId);

    @SQL("select id from classes where parent_id = :id and status = 0")
    List<Integer> findClassesChild(@SQLParam("id") int id);

    @SQL("select distinct ts.subject_id from tp_exam_student ts ,tp_exam tp where ts.student_id = :studentId and ts.type = :type and ts.status = 1 and ts.exam_id = tp.id and tp.status = 4")
    List<TpExamStudent> findSubjectByTpExam(@SQLParam("studentId") int studentId, @SQLParam("type") int type);

    @SQL("select count(id) from tp_exam_student where exam_id = :attachmentId and classes_id =:classesId and status = 1")
    int isExistExamByClassesId(@SQLParam("attachmentId") int attachmentId, @SQLParam("classesId") int classesId);

    @SQL("select count(te.id) from tp_exam_student te , classes c where te.exam_id = :attachmentId and te.classes_id = c.id and c.parent_id = :classesId and te.status = 1")
    int isExistExamByClassesParentId(@SQLParam("attachmentId") int attachmentId, @SQLParam("classesId") int classesId);

    /**
     * 查询指定班级的某个学科的老师ID (去处重复)
     *
     * @param classIds
     * @param subjectId
     * @return
     */
    @SQL("SELECT DISTINCT (SELECT user_id FROM USER WHERE id =ct.teacher_id LIMIT 1) FROM classes_teacher ct WHERE ct.classes_id IN(:classIds) AND ct.subject_id =:subjectId AND ct.STATUS = 0")
    List<Integer> queryTeacherIdsByClassIdAndSubjectId(@SQLParam("classIds") List<Integer> classIds, @SQLParam("subjectId") Integer subjectId);

    /**
     * 查询某个学生加入的行政班和学科班级
     *
     * @param studentId
     * @return
     */
    @SQL("SELECT c.* FROM (SELECT * FROM classes_student WHERE student_id =:studentId) t INNER JOIN(SELECT * FROM classes WHERE graduate=0 AND (TYPE=0 OR TYPE =1)  )c ON t.classes_id =c.id ")
    List<Classes> queryStudentClassInfo(@SQLParam("studentId") Integer studentId);

    /**
     * 修改课堂or作业状态
     *
     * @param conn
     * @param examId
     * @param status
     */
    @SQL("update tp_exam set status = :status where id = :examId")
    void updateTpExamStatus(Connection conn, @SQLParam("examId") int examId, @SQLParam("status") int status);

    @SQL("SELECT DISTINCT classes_id FROM `classes_teacher` WHERE teacher_id =(select id from user where user_id=:teacherId limit 1) AND STATUS =0")
    List<Integer> queryClassIdsByTeacherId(@SQLParam("teacherId")Integer teacherId);
}
