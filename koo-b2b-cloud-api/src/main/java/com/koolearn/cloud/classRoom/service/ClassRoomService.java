package com.koolearn.cloud.classRoom.service;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.exam.entity.TpExamAttachmentComment;
import com.koolearn.cloud.exam.entity.TpExamStudentNote;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.util.ListPage;

import java.util.List;

/**
 * 创建课堂service
 */
public interface ClassRoomService {


    /**
     * 查询老师课堂(分页)
     *
     * @param subjectId
     * @param searchValue
     * @param pageNo
     * @param userId
     * @return
     */
    ListPage findTeacherClassRoomPage(int subjectId, String searchValue, int pageNo, int userId);

    /**
     * 删除课堂
     *
     * @param classRoomId
     */
    void deleteClassRoom(int classRoomId);


    /**
     * 修改资源名称
     *
     * @param attachmentId
     * @param attachmentName
     */
    void updateAttachmentName(int attachmentId, String attachmentName);

    /**
     * 创建课堂
     *
     * @param classRoom
     * @return
     */
    int create(TpExam classRoom);
    /**
     * 查询老师下班级的分组
     *
     * @param teacherId
     * @param classesId
     * @return
     */
    List<Classes> findClassesGroupByTeacherIdSubjectId(int subjectId,int teacherId, int classesId);

    /**
     * 保存课堂
     *
     * @param bean
     * @param teacherId
     */
    void save(TpExam bean, int teacherId, int type);

    /**
     * 根据课堂查询班级名称
     *
     * @param classRoomId
     * @return
     */
    List<String> findClassesFullNameByClassRoomId(int classRoomId);

    /**
     * 查询班级
     *
     * @param classesIds
     * @return
     */
    List<Classes> findClassesById(String[] classesIds);

    /**
     * 保存课堂
     *
     * @param paper
     * @param classesIds
     * @param teacherId
     * @param examName
     * @return
     */
    int saveClassRoomExam(TestPaper paper, String[] classesIds, int teacherId, String examName);

    /**
     * 根据班级Id查询名称
     *
     * @param classesIds
     * @return
     */
    List<String> findClassesFullNameByIds(String[] classesIds);

    /**
     * 查询课堂
     *
     * @param classRoomId
     * @return
     */
    TpExam getClassRoomById(int classRoomId, String type);

    /**
     * 修改课堂
     *
     * @param bean
     * @param teacherId
     */
    void update(TpExam bean, int teacherId, int type);

    /**
     * 根据课堂查询班级
     *
     * @param classRoomId
     * @return
     */
    List<Classes> findClassesByClassRoomId(int classRoomId);

    /**
     * 查询课堂附件
     *
     * @param classRoomId
     * @return
     */
    List<TpExamAttachment> findAttachmentByClassRoomId(int classRoomId);

    /**
     * 查询附件
     *
     * @param id
     * @return
     */
    TpExamAttachment getTpExamAttachmentById(int id);

    /**
     * 保存回复
     *
     * @param comment
     * @param user
     */
    TpExamAttachmentComment saveComment(TpExamAttachmentComment comment, UserEntity user);

    /**
     * 查询所有课件回复
     *
     * @param examAttachmentId
     * @return
     */
    List<TpExamAttachmentComment> findCommentByExamAttachmentId(int examAttachmentId);

    /**
     * 根据班级ID查询班级
     *
     * @param classesId
     * @return
     */
    Classes getClassesById(int classesId);

    /**
     * 查询课堂下一个班级所有学生名称
     *
     * @param classRoomId
     * @param classesId
     * @return
     */
    List<User> findStudentNameByClassRoomId(int classRoomId, int classesId);


    /**
     * 查询单个附件完成学生名称
     *
     * @param tpExamAttachmentId
     * @return
     */
    List<User> findFinishAttachment(int tpExamAttachmentId, int classesId);

    /**
     * 查询单个作业班级完成的学生
     *
     * @param attachmentId
     * @return
     */
    List<User> findFinishExamResult(int attachmentId, List<Integer> classesId);

    /**
     * 查询学生下的课堂
     *
     * @param studentId
     * @param subjectId
     * @return
     */
    ListPage findStudentClassRoom(int studentId, int subjectId, int pageNo, String searchValue, String endTimeStr);

    /**
     * 根据课堂id查询附件id集合
     *
     * @param classRoomId
     * @return
     */
    List<String> findAttachmentIdsByClassRoomId(int classRoomId);

    /**
     * 查询实时新课堂
     *
     * @param studentId
     * @param subjectId
     * @return
     */
    boolean findSubjectView(int studentId, int subjectId);

    /**
     * 修改学成查看附件为已查看
     *
     * @param studentId
     * @param subjectId
     */
    void deleteSubjectView(int studentId, int subjectId);

    /**
     * 判断课堂作业是否包含当前学生
     *
     * @param examId
     * @param studentId
     * @return
     */
    boolean isExistExamByStudent(int examId, int studentId);

    /**
     * 保存笔记
     *
     * @param t
     * @return
     */
    int saveNote(TpExamStudentNote t);

    /**
     * 查询当前笔记
     *
     * @param tpExamAttachmentId
     * @return
     */
    List<TpExamStudentNote> findNote(int studentId, int tpExamAttachmentId);

    /**
     * 查询课堂
     *
     * @param tpExamId
     * @return
     */
    TpExam getClassRoomById(int tpExamId);

    /**
     * 课件是否被学习
     *
     * @param studentId
     * @param tpExamAttachmentId
     * @return
     */
    boolean getComplete(int studentId, int tpExamAttachmentId);

    /**
     * 保存学习记录
     *
     * @param studentId
     * @param tpExamAttachmentId
     */
    void saveComplete(int studentId, int tpExamAttachmentId);

    /**
     * 查询课堂作业是否完成
     *
     * @param studentId
     * @param tpExamId
     * @return
     */
    boolean getExamComplete(int studentId, int tpExamId);

    /**
     * 学生是否存在课堂
     *
     * @param tpExamId
     * @param studentId
     * @return
     */
    boolean getClassRoom(int tpExamId, int studentId);

    /**
     * 根据学段，学科，老师查询课堂的班级
     *
     * @param rangeId
     * @param subjectId
     * @param teacherId
     * @return
     */
    List<Classes> findAllClassByRangeSub(int rangeId, int subjectId, int teacherId);

    /**
     * 班级下是否有学生
     *
     * @param classesId
     * @return
     */
    boolean isExistClassesStudentByClassesId(int classesId);

    /**
     * 保存学生完成课堂时间
     *
     * @param studentId
     * @param tpExamId
     */
    void updateFinishTime(int studentId, int tpExamId);

    /**
     * 获取学生完成时间
     *
     * @param studentId
     * @param tpExamId
     * @return
     */
    TpExamStudent getTpExamStudent(int studentId, int tpExamId);

    /**
     * 查询学生考试结果
     *
     * @param tpExamId
     * @param studentId
     * @return
     */
    TaskDto getStudentTpExamResult(int tpExamId, int studentId);

    /**
     * 查询小组
     *
     * @param classesId
     * @return
     */
    List<Classes> findClassesByParentId(int subjectId, int classesId, int teacherId);

    /**
     * 查询所有未完成作业的学生
     *
     * @param examId
     * @return
     */
    List<User> findUnFinishExamResult(int examId,List<Integer> classesIds);

    /**
     * 查询所有完成附件的学生
     *
     * @param studentId
     * @param examAttachmentId
     * @return
     */
    boolean getFinishAttachment(int studentId, int examAttachmentId);

    /**
     * 查询课堂所有学生
     *
     * @param classRoomId
     * @return
     */
    List<User> findAllStudentNameByClassRoomId(int classRoomId);

    /**
     * 查询班级下所有小组id
     * @param id
     * @return
     */
    List<Integer> findClassesChild(int id);

    boolean isExistExamByClassesId(int attachmentId, int classesId);

    boolean isExistExamByClassesParentId(int attachmentId, int classesId);

    /**
     * 查询指定班级的某个学科的老师ID
     * @param classIds
     * @param subjectId
     * @return
     */
    List<Integer> queryTeachersByClassIdsAndSubjectId(List<Integer> classIds,Integer subjectId);

    /**
     * 查询学生加入的学科班级和行政班级
     * @param studentId
     * @return
     */
    List<Classes> queryClassesByStudentId(Integer studentId);
}