package com.koolearn.cloud.task.service;

import java.util.List;
import java.util.Map;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.vo.StudentResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.QuestionErrUser;
import com.koolearn.cloud.task.dto.StudentHasTaskLog;
import com.koolearn.cloud.task.dto.TaskClassesName;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.cloud.task.dto.TaskStudentNum;
import com.koolearn.klb.tags.entity.Tags;

public interface TaskService {
	
	/* 学生端使用的方法 start*/
	/**
	 * 根据学科查询作业列表
	 * @param task
	 * @return
	 */
	public TaskPager searchTaskBySubject(TaskPager task);
	/**
	 * 查询学生学科是否已阅
	 * @param 
	 * @return
	 */
	public Map<String, StudentHasTaskLog> findTaskLogByStudentId(int studentId);
	/**
	 * 插入学生浏览作业日志表数据
	 * @param studentId
	 */
	public void insertStudentHasTaskLog(int studentId);
	/**
	 * 更新学生作业日志表，将已查看过的科目更新为已阅
	 * @param subject
	 * @param studentId
	 */
	public void updateTaskLog(int subject, int studentId);
	/* 学生端使用的方法 end*/
	
	public TaskDto findTaskById(int taskId);
	/**
	 * 作业列表
	 * @param taskPager
	 * @return
	 */
	public TaskPager searchTask(TaskPager taskPager);
	/**
	 * 保存作业
	 * @param dto
	 * @param studentList 
	 * @param
	 * @return
	 */
	public int saveTask(TaskDto dto, List<UserEntity> studentList);
	/**
	 * 查询某老师某作业对应的班级或分组列表
	 * @param taskId
	 * @param
	 * @return
	 */
	public List<Classes> findClassesByTaskId(int taskId, int teacherId);
	/**
	 * 查询已完成作业的学生列表
	 * @param t
	 * @return
	 */
	public List<TpExamResult> findStudentByExamResult(TaskPager t);
	/**
	 * 查询作业对应的学生总人数
	 * @param userId 
	 * @param ids
	 * @return
	 */
	public List<TaskStudentNum> findStudentNumByTaskIds(int userId, String ids);
	/**
	 * 查询已经完成作业的学生人数
	 * @param userId
	 * @param ids
	 * @return
	 */
	public List<TaskStudentNum> findCompStudentNumByTaskIds(int userId,String ids);
	/**
	 * 查询作业对应班级列表
	 * @param
	 * @return
	 */
	public List<TaskClassesName> searchClassesByTaskId(int userId, String ids);
	/**
	 * 查询题目对应的答错人及人数集合
	 * @param t
	 * @return
	 */
	public List<QuestionErrUser> findQuestionErrAnswerUser(TaskPager t);
	/**
	 * 查询题目对应的未作答人及人数集合
	 * @param t
	 * @return
	 */
	public List<QuestionErrUser> findQuestionNoAnswerUser(TaskPager t);
	/**
	 * 查询题目平均得分率和平均得分
	 * @param t
	 * @return
	 */
	public List<QuestionErrUser> findQuestionAvgScore(TaskPager t);
	/**
	 * 查询提交作业的学生按班级id
	 * @param t
	 * @return
	 */
	public List<UserEntity> findStudentResultByClassesId(TaskPager t);
	/**
	 * 查询未提交作业的学生
	 * @param t
	 * @return
	 */
	public List<TpExamResult> findStudentNoResultByClassesId(TaskPager t);
	/**
	 * 查询老师拥有的班级列表
	 * @param ue
	 * @return
	 */
	public List<Classes> findClassesByUserId(UserEntity ue);
	/**
	 * 通过班级id集合查询班级列表
	 * @param classesIds
	 * @return
	 */
	public List<Classes> findClassesByIds(String classesIds);
	/**
	 * 撤回作业
	 * @param taskId
	 * @return
	 */
	public int revokeTask(int taskId);
	/**
	 * 删除作业
	 * @param taskId
	 * @return
	 */
	public int deleteTask(int taskId);
	/**
	 * 修改作业
	 * @param dto
	 * @param
	 * @param studentList 
	 * @return
	 */
	public int modifySaveTask(TaskDto dto, List<UserEntity> studentList);
	/**
	 * 查找班级下的学生(有效的)
	 * @param classesIds
	 * @return
	 */
	public List<UserEntity> findStudentByClassesIds(String classesIds);
	/**
	 * 查询te_paper单表数据，根据paperid
	 * @param paperId
	 * @return
	 */
	public TestPaper findPaperById(int paperId);
	/**
	 * 查询学生所属班级
	 * @param ue
	 * @return
	 */
	public List<Classes> findClassesByStudentId(UserEntity ue);
	/**
	 * 查询学生对应的班级及老师拥有的学科
	 */
	public List<TeacherBookVersion> findClassesTeacherSubject(UserEntity ue,int type);
	/**
	 * 查询考试by考试id
	 * @param examId
	 * @return
	 */
	public TpExam queryExamByIdCache(Integer examId);
	/**
	 * 更新考试结果表老师浏览状态：由未查看改成已查看
	 * @param t
	 */
	public void updateTeExamResultTeacherView(TaskPager t);
	/**
	 * 根据学生id和作业id查询结果
	 * @param pager
	 * @return
	 */
	public TpExamResult findExamResult(TaskPager pager);
	/**
	 * 根据考试结果id,查询结果信息
	 * @param resultId
	 * @return
	 */
	public TpExamResult findExamResultById(int resultId);
	/**
	 * 查询学生id集合，通过班级id
	 * @param classId
	 * @return
	 */
	public String findStudentIdsByClassesId(int classId);
	/**
	 * 题目得分率
	 * @param t
	 * @return
	 */
	public List<QuestionErrUser> findStudentScoreRate(TaskPager t);
	/**
	 * 成绩分布数据 
	 * @param t
	 * @return
	 */
	public List<TpExamResult> findStudentScore(TaskPager t);
	/**
	 * 知识点得分率
	 * 题目得分情况数据
	 * @param t
	 * @return
	 */
	public List<TpExamResultDetail> findStudentQuestionScore(TaskPager t);
	/**
	 * 查询学生用户
	 * @param studentId
	 * @return
	 */
	public UserEntity findUserById(Integer studentId);
	/**
	 * 根据结果id
	 * 查看结果明细中的错题id集合
	 * @param
	 * @return
	 */
	public List<QuestionErrUser> findTpExamResultDetailQuestionIds(int rid);

	/**
	 * 学生浏览状态更新  更新为学生已查看评语
	 * @param resultId
	 */
	public void updateTaskStudentView(int resultId);

    /**
     * 获取学生答题 结果
     * @param resultId
     * @return
     */
    StudentResult findDetailByResult(Integer resultId);

    void deleteError(Integer questionId, Integer userId);
    /**
     * 通过结果id查询作业
     * @param rid
     * @return
     */
	public TpExam findTpExamByResultId(int rid);
	/**
     * 按班级和错误题查询，该班的所有学生答题结果id
     */
	public String findAllResultId(TaskPager taskPager);
	/*
	 * 作业讲评 根据错误率查询题目
	 */
	public List<QuestionErrUser> findAllResultDetail(TaskPager taskPager);
	
	public List<TpExamResultDetail> findResultDetailTeId(String resultId);
}