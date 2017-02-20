package com.koolearn.cloud.exam.examcore.exam.service;

import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examcore.exam.dto.SearchResultDto;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;
import com.koolearn.cloud.exam.examspread.exam.dto.ExamResultSearchDto;
import com.koolearn.cloud.exam.examspread.exam.entity.ExamResultSearch;
import com.koolearn.cloud.login.entity.UserEntity;


/**
 * 考试结果接口
 * @author wangpeng
 *
 */
public interface ExamResultService {
	
	
	/**
	 * 提交更新考试结果
	 * @param rid
	 * @param status
	 * @param userResultStr
	 * @throws Exception
	 * @author DuHongLin
	 * @return 1表示有修改，0.表示没有修改
	 */
	public int editResultForSubmit(int rid, int status, String userResultStr) throws Exception;
	/**
	 * 根据考试结果查询ID查询结果
	 * @param resultId 结果ID
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public TpExamResult searchById(int resultId) throws Exception;
	/**
	 * 根据考试过程对象获取考试结果所有信息
	 * @param process
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public Process searchFullByProcess(Process process) throws Exception;
	
	/**
	 * 更新主观题得分(score)和题目正确性(result_answer)
	 * 老师批阅时，主观题判分写结果表（te_exam_result_detail）
	 * @param examResultDetails
	 * @param map
	 * @param examResult 
	 * @return 
	 * @throws Exception 
	 */
	public int updateDetailScoreResultAnswer(List<TpExamResultDetail> examResultDetails, Map<String, String> map, TpExamResult examResult) throws Exception;
	
	/**
	 * 更新主观题图片路径
	 * @param questionId
	 * @param resultId
	 * @param realPath
	 * @return
	 */
	public int updateDetailSubjectiveUrl(String questionId, String resultId,String realPath);
	
	
	
	
	
	//以上方法为更新后方法
	
	
	
	
	
	/**
	 * 通过考试id,获取考完的主观题数据
	 * @param examId
	 * @return
	 */
	 ExamResultSearchDto findResult4SubjectiveByExamId(SearchResultDto searchResultDto);

	/**
	 * 查询最新的考试结果
	 * @param examId 考试ID
	 * @param studentId 学生ID
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public TpExamResult searchLast(int examId, int studentId)  throws Exception;
	/**
	 * 保存考试结果数据
	 * @param process 考试过程数据传输对象
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public Process saveByProcess(Process process)  throws Exception;

	/**
	 * 批量保存考试结果数据
	 * @param processes 考试过程数据传输对象集合
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 * 2015年10月12日 上午11:10:17
	 */
	public List<Process> saveByProcesses(List<Process> processes)  throws Exception;
	/**
	 * 更新考试结果数据
	 * @param examResult 考试结果对象
	 * @param resultStructures 考试结果结构集合
	 * @param resultDetails 考试结果明细集合
	 * @throws Exception
	 * @author DuHongLin
	 */
	public void edit(TpExamResult examResult, List<TpExamResultStructure> resultStructures, List<TpExamResultDetail> resultDetails) throws Exception;

	/**
	 * 查询单个对象
	 * @param questionId
	 * @param structureId
	 * @return
	 */
	TpExamResultDetail findOne(int questionId, int structureId);

	/**
	 * 更新单个对象数据
	 * @param detailId
	 * @param score
	 * @param reply
	 */
	void updateDetail(int detailId, float score, String reply);


	/**
	 * 更新考试结果信息
	 * @param examResult
	 * @throws Exception
	 * @author DuHongLin
	 */
	public void editResult(TpExamResult examResult) throws Exception;

	/**
	 * 根据学生ID和多个考试ID查询多个考试的最新结果
	 * @param eids
	 * @param uid
	 * @return key==》学生ID_考试ID
	 * @throws Exception
	 * @author DuHongLin
	 */
	public Map<String, TpExamResult> searchLasts(List<Integer> eids, int uid) throws Exception;
	/**
	 * 根据题目id，考试结果id查询考试结果
	 * @param questionId
	 * @param resultId
	 */
	public TpExamResultDetail selectByQuestionIdResultId(int questionId, int resultId)throws Exception;
	/**
	 * 老师对主观题批复
	 * @param replyStr 批复内容
	 * @param score 学生得分
	 * @param resultId 结果ID
	 * @param questionId 题目ID
	 * @param teId 父级题目ID
	 * @param search 查询对象
	 * @throws Exception
	 * @author DuHongLin
	 */
	public void reply(String replyStr, double score, int resultId, int questionId, int teId, ExamResultSearch search) throws Exception;

	/**
	 * 批改记录查询
	 * @param integer
	 * @return
	 */
	public ExamResultSearch findExamResultSearchById(Integer integer);

    /**
     * 本次考试批改完毕，修改考试成绩发布状态issue_result=1
     * @param examId
     */
    void changeIssueResultStatus(int examId);
    /**
     * 根据考试结果 查询学生
     * @param int1
     * @return
     */
	UserEntity queryStudentByExamresultID(int erid);

	TpExam queryExamByExamresultID(int int1);
	/** 作业批阅  保存老师评语 */
	public int saveMarkReply(int resultId, String reply);
	/**
	 * 根据考试结果id获取考试结果所有信息
	 * @throws Exception 
	 */
	public List<TpExamResultDetail> searchFullExamResultDetail(int resultId) throws Exception;
	/**
	 * 所有主观题考试结果明细集合
	 * @param id
	 * @param subjective 1主观题
	 * @return
	 */
	public List<TpExamResultDetail> searchResultDetailSubjective(Integer id,int subjective);
	/**
	 * 查询某考试结果明细
	 * @param id
	 * @return
	 */
	public List<TpExamResultDetail> searchResultDetail(Integer id);

}
