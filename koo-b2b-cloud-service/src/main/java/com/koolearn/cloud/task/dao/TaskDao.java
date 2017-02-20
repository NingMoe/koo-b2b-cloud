package com.koolearn.cloud.task.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.exam.entity.TpErrorNote;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.task.dto.*;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;

import java.sql.Connection;
import java.util.List;


@DAO(dbtype=DbType.MYSQL,source= GlobalConstant.MYSQL_DATASOURCE)
public interface TaskDao {
	
	@SQL(type = SQLType.READ)
	public TaskDto findTaskById(@SQLParam("taskId") int taskId);
	
	@SQL(type = SQLType.READ)
	public List<TaskDto> searchTask(ListPager listPager,TaskPager taskPager);

	@SQL(type = SQLType.READ)
	public int searchTaskCount(TaskPager taskPager);

	@SQL(type=SQLType.WRITE_INSERT)
	public int saveTask(Connection conn, TpExam task);

	@SQL(type=SQLType.WRITE_INSERT)
	public void saveTaskStudent(Connection conn, List<TpExamStudent> sList);
	
	@SQL(type = SQLType.READ)
	public List<TaskDto> searchTaskBySubject(@PageBy ListPager listPager,@SQLParam("task") TaskPager task);

	@SQL(type = SQLType.READ)
	public int searchTaskBySubjectCount(@SQLParam("task") TaskPager task);

	@SQL(" select * From student_has_task_log where student_id=:studentId and status="+StudentHasTaskLog.STATUS_SEE_NO)
	public List<StudentHasTaskLog> findTaskLogByStudentId(@SQLParam("studentId") int studentId);

	@SQL(" select distinct te.subject_id subjectId,tpes.exam_id examId,tpes.classes_id classesId,tpes.student_id studentId From tp_exam_student tpes " +
			" left join tp_exam te on te.id=tpes.exam_id " +
			" left join student_has_task_log sh on sh.student_id=tpes.student_id and sh.subject_id=te.subject_id and tpes.exam_id=sh.exam_id" +
			" where sh.id is null and tpes.student_id=:studentId  and tpes.status="+TpExamStudent.STATUS_VALID +
			" and te.type="+TpExam.EXAM_TYPE_TASK)
	public List<StudentTaskLogDto> findStudentTaskLog(@SQLParam("studentId") int studentId);

	@SQL(type = SQLType.WRITE_INSERT)
	public void insertStudentHasTaskLog(List<StudentHasTaskLog> inList);

	@SQL(" update student_has_task_log set status=:statusSeeYes where subject_id=:subject and status=:statusSeeNo and student_id=:studentId  ")
	public void updateTaskLog(@SQLParam("subject") int subject,@SQLParam("studentId") int studentId,@SQLParam("statusSeeNo") int statusSeeNo,@SQLParam("statusSeeYes") int statusSeeYes);

	@SQL(" select distinct c.* from tp_exam_student tes INNER JOIN classes c on c.id=tes.classes_id where tes.teacher_id=:teacherId and tes.exam_id=:taskId and tes.`status` =1 order by c.id asc  ")
	public List<Classes> findClassesByTaskId(@SQLParam("taskId") int taskId,@SQLParam("teacherId") int teacherId);

	@SQL(type=SQLType.READ)
	public List<TpExamResult> findStudentByExamResult(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<TaskStudentNum> findStudentNumByTaskIds(@SQLParam("userId") int userId,@SQLParam("ids") String ids);

	@SQL(type=SQLType.READ)
	public List<TaskStudentNum> findCompStudentNumByTaskIds(@SQLParam("userId") int userId,@SQLParam("ids")	String ids);

	@SQL(type=SQLType.READ)
	public List<TaskClassesName> searchClassesByTaskId(@SQLParam("userId") int userId,@SQLParam("ids") String ids);

	@SQL(type=SQLType.READ)
	public List<QuestionErrUser> findQuestionErrAnswerUser(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<QuestionErrUser> findQuestionNoAnswerUser(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<QuestionErrUser> findQuestionAvgScore(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<UserEntity> findStudentResultByClassesId(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<TpExamResult> findStudentNoResultByClassesId(@SQLParam("t") TaskPager t);

	@SQL(type=SQLType.READ)
	public List<Classes> findClassesByUserId(@SQLParam("ue") UserEntity ue);

	@SQL(type=SQLType.READ)
	public List<Classes> findGroupByClassesId(@SQLParam("id") Integer id);

	@SQL(type=SQLType.READ)
	public List<Classes> findClassesByIds(@SQLParam("ids") String ids);

	@SQL(" update tp_exam te set te.status=:revokeStatus where te.id=:taskId ")
	public int revokeTask(@SQLParam("taskId") int taskId,@SQLParam("revokeStatus") int revokeStatus);
	
	@SQL(" update tp_exam te set te.status=:deleteStatus where te.id=:taskId ")
	public int deleteTask(@SQLParam("taskId") int taskId,@SQLParam("deleteStatus") int deleteStatus);
	
	@SQL(" update tp_exam te set te.status=:deleteStatus where te.id=:taskId ")
	public int deleteTask(Connection conn, @SQLParam("taskId") int taskId,@SQLParam("deleteStatus") int deleteStatus);

	@SQL(type=SQLType.READ)
	public List<UserEntity> findStudentByClassesIds(@SQLParam("ids") String ids);

	@SQL(type=SQLType.WRITE)
	@GeneratedKey
	public int updateTask(Connection conn,@SQLParam("dto") TaskDto dto);
	
	//删除作业与学生中间表数据
	@SQL(type=SQLType.WRITE)
	public void deleteTpExamStudent(Connection conn,@SQLParam("dto") TaskDto dto);
	//查询te_paper单表
	@SQL(" select * From te_paper where id=:paperId ")
	public TestPaper findPaperById(@SQLParam("paperId") int paperId);

	// 查询学生所属班级
	@SQL(type=SQLType.READ)
	public List<Classes> findClassesByStudentId(@SQLParam("ue") UserEntity ue);

	//查询学生对应的班级及老师拥有的学科
	@SQL(type=SQLType.READ)
	public List<TeacherBookVersion> findClassesTeacherSubject(UserEntity ue);

	/**
	 * 查询题目 根据错误率 作业讲评
	 */
	@SQL(type=SQLType.READ)
	public List<QuestionErrUser> findAllResultDetail(@SQLParam("taskPager") TaskPager taskPager);

	/**
	 * 查询考试by考试id
	 */
	@SQL(type=SQLType.READ_BY_ID)
	public TpExam queryExamById(Integer examId);

	/**
	 * 更新考试结果表老师浏览状态：由未查看改成已查看
	 */
	@SQL(" update te_exam_result ter,tp_exam_student tes set ter.teacher_view=" +TpExamResult.TEACHER_VIEW_TWO +
			" where tes.exam_id=ter.exam_id and tes.student_id=ter.student_id " +
			" and tes.classes_id=:t.classId and tes.exam_id=:t.examId ")
	public void updateTeExamResultTeacherView(@SQLParam("t") TaskPager t);
	/**
	 * 根据学生id和作业id查询结果
	 */
	@SQL(" select * From te_exam_result ter where ter.student_id=:t.userId and ter.exam_id=:t.examId order by ter.id desc LIMIT 0,1 ")
	public List<TpExamResult> findExamResult(@SQLParam("t") TaskPager t);

	/**
	 * 根据考试结果id,查询结果信息 提交的 ???and ter.status="+TpExamResult.STATUS_COMPLETE
	 */
	@SQL(" select te.type examType,ter.* From te_exam_result ter INNER JOIN tp_exam te ON ter.exam_id=te.id where ter.id=:resultId ")
	public TpExamResult findExamResultById(@SQLParam("resultId") int resultId);

	/**
	 * 查询学生id集合，通过班级id
	 */
	@SQL(" select distinct cs.student_id from classes_student cs where cs.classes_id=:classId and cs.status="+ClassesStudent.STATUS_NOMAL)
	public List<Integer> findStudentIdsByClassesId(@SQLParam("classId") int classId);

	/**
	 * 题目得分率
	 */
	@SQL(type=SQLType.READ)
	public List<QuestionErrUser> findStudentScoreRate(TaskPager t);
	/**
	 * 成绩分布数据 
	 */
	@SQL(type=SQLType.READ)
	public List<TpExamResult> findStudentScore(TaskPager t);

	/**
	 * 知识点得分率
	 * 题目得分情况数据
	 */
	@SQL(type=SQLType.READ)
	public List<TpExamResultDetail> findStudentQuestionScore(TaskPager t);
	/**
	 * 查询学生用户
	 */
	@SQL(type=SQLType.READ_BY_ID)
	public UserEntity findUserById(Integer studentId);
	/**
	 * 根据结果id
	 * 查看结果明细中的错题id集合
	 */
	@SQL(" select question_id questionId,te_id teId From te_exam_result_detail where result_id=:rid and (result_answer="+TpExamResultDetail.RESULT_ANSWER_NOT_CORRECT+" or result_answer="+TpExamResultDetail.RESULT_ANSWER_PART_CORRECT+") order by id asc ")
	public List<QuestionErrUser> findTpExamResultDetailQuestionIds(@SQLParam("rid") int rid);

	/**
	 * 用户已做答的题目数量
	 */
	@SQL(type=SQLType.READ)
	public List<TaskCompletRate> searchTaskResultDone(String resultIds);
	
	/**
	 * 用户的结果明细表中总题目数量
	 */
	@SQL(type=SQLType.READ)
	public List<TaskCompletRate> searchTaskResultAll(String resultIds);

    /**错题本 作业列表 **/
    @SQL(type = SQLType.READ)
    public List<TreeBean> searchTaskOfErrorNote( @SQLParam("task") TaskPager task);
    @SQL(type = SQLType.READ)
    public Long searchTaskOfErrorNoteCount(@SQLParam("task") TaskPager task);

    /**
     * 获取错题和部分正确的题目
     * @param userId
     * @param examId
     * @return
     */
    @SQL("select question_id from  te_exam_result_detail " +
            "where  result_answer in(0,2) and result_id =(SELECT id from te_exam_result where exam_id =:examId and student_id =:userId)")
    List<Integer> findErrorQids(@SQLParam("userId")Integer userId, @SQLParam("examId")Integer examId);
    @SQL("SELECT question_id ,`times` from tp_error_note where status=1 and student_id =:userId and  question_id in(:questionIdList)")
    List<TpErrorNote> findErrorNoteByQids(@SQLParam("userId")Integer userId,@SQLParam("questionIdList")List<Integer> questionIdList);
    @SQL(type = SQLType.READ)
    int searchErrorQuestionCount(QuestionFilter questionFilter);
    @SQL(type = SQLType.READ)
    List<TpErrorNote> searchErrorQuestion(QuestionFilter questionFilter);

	/**
	 * 学生浏览状态更新  更新为学生已查看评语
	 * @param resultId
	 */
	@SQL(" update te_exam_result set student_view="+TpExamResult.STUDENT_VIEW_TWO+" where id=:resultId")
	public void updateTaskStudentView(@SQLParam("resultId") int resultId);
    /**
     * 根据结果id
     * 查看结果明细中的错题id集合
     */
    @SQL(" select * From te_exam_result_detail where result_id=:rid ")
    public List<TpExamResultDetail> findDetailByResult(@SQLParam("rid") int rid);
    @SQL(" update tp_error_note set status=0 where question_id=:questionId and student_id=:userId")
    void deleteError(@SQLParam("questionId") Integer questionId, @SQLParam("userId") Integer userId);

    @SQL(" select te.* From tp_exam te,te_exam_result ter where te.id=ter.exam_id and ter.id=:rid ")
	public TpExam findTpExamByResultId(@SQLParam("rid") int rid);

    /**
     * 按班级和错误题查询，该班的所有学生答题结果id
     */
    @SQL(" select GROUP_CONCAT(ter.id) From tp_exam_student tes " +
    		" INNER JOIN te_exam_result ter on ter.student_id=tes.student_id and ter.exam_id=tes.exam_id " +
    		" where tes.classes_id=:taskPager.classId and tes.exam_id=:taskPager.examId  and ter.status="+TpExamResult.STATUS_COMPLETE)
	public String findAllResultId(@SQLParam("taskPager") TaskPager taskPager);

    @SQL(" select * From te_exam_result_detail terd where terd.te_id=0 and terd.result_id=:resultId order by id asc")
	public List<TpExamResultDetail> findResultDetailTeId(@SQLParam("resultId") String resultId);
    @SQL(type = SQLType.WRITE)
    public void rebuildUpdate(String xiaoxiao);
}
