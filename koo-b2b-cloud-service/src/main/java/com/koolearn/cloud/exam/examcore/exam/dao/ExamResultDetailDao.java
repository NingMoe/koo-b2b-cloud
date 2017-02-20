package com.koolearn.cloud.exam.examcore.exam.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ExamResultDetailDao {
	
	/**
	 * 根据结果ID查询明细
	 * @param resultId
	 * @return
	 * @author DuHongLin
	 */
	@SQL("SELECT * FROM te_exam_result_detail WHERE result_id = :resultId order by id asc ")
	List<TpExamResultDetail> selectByResultId(@SQLParam("resultId") int resultId);
	/**
	 * 批量更新结果明细
	 * @param conn
	 * @param details
	 * @author DuHongLin
	 */
	@SQL(type = SQLType.WRITE_UPDATE)
	public void updateDetailBatch(Connection conn, List<TpExamResultDetail> details);
	/**
	 * 更新主观题得分(score)和题目正确性(result_answer)
	 * 老师批阅时，主观题判分写结果表（te_exam_result_detail）
	 * @return 
	 */
	@SQL(" update te_exam_result_detail terd set terd.score=:score,terd.result_answer=:resultAnswer where id=:id ")
	int updateDetailScoreResultAnswer(Connection conn,@SQLParam("id") Integer id,@SQLParam("resultAnswer") String resultAnswer,@SQLParam("score") String score);
	
	@SQL(" update te_exam_result_detail set user_answer=:realPath where result_id=:resultId and question_id=:questionId and subjective="+TpExamResultDetail.SUBJECTIVE)
	int updateDetailSubjectiveUrl(@SQLParam("questionId") String questionId,@SQLParam("resultId") String resultId,@SQLParam("realPath") String realPath);
	
	//以上方法为更新后方法
	
	
	
	
	
	
	

	@SQL("SELECT d.* FROM te_exam_result_structure s,te_exam_result_detail  d WHERE s.result_id=:id2 AND s.id=d.structure_id AND d.subjective=0 ")
    List<TpExamResultDetail> findDetais4SubjectiveByResultId(@SQLParam("id2") int id);
	

	/**
	 * 根据题目id，考试结果id查询考试结果
	 * @param questionId
	 * @param resultId
	 */
	@SQL("SELECT  `id`, `structure_id` , `question_id` , `points` , `score` ,`right_answer` , `user_answer` , `result_answer` , `subjective` ,"
			+ " `question_type_id` , `result_id`,`te_id` ,`reply` , `tag1` , `tag2` , `tag3`, `te_type` , `question_code` "
			+ "  FROM te_exam_result_detail WHERE question_id = :questionId and result_id = :resultId ")
	TpExamResultDetail selectByQuestionIdResultId(@SQLParam("questionId") int questionId, @SQLParam("resultId") int resultId);
	
	/**
	 * 写入结果明细
	 * @param conn
	 * @param resultDetail
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, TpExamResultDetail resultDetail);
	@SQL("update te_exam_result_detail set score=:score2 ,reply=:reply2 where id=:detailId ")
	void updateItem(@SQLParam("detailId") int detailId, @SQLParam("score2") float score, @SQLParam("reply2") String reply);

	/**
	 * 更新考试明细信息
	 * @param conn
	 * @param detail
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void updateDetail(Connection conn, TpExamResultDetail detail);

	/**
	 * 获得考试结果明细：只返回统计中需的客观题
	 */
	@SQL(" select count(1) from te_exam_result res , te_exam_result_detail resD " +
			" where   res.id=resD.result_id  and res.exam_id=:examId  ")
	int getTpExamResultDetailCount(@SQLParam("examId") Integer examId);
	/**
	 * 获得考试结果明细
	 */
	@SQL(" select resD.* ,res.student_id as studentId from te_exam_result res , te_exam_result_detail resD" +
			" where   res.id=resD.result_id  and res.exam_id=:examId  ")
	List<TpExamResultDetail> getTpExamResultDetail(@SQLParam("examId") Integer examId,
                                               @SQLParam("filter") @PageBy QuestionFilter filter);
	
	/**
	 * 所有主观题考试结果明细集合
	 * @param subjective 1主观题
	 */
	@SQL(" SELECT * FROM te_exam_result_detail WHERE result_id =:resultId and subjective=:subjective ")
	List<TpExamResultDetail> selectSubjectiveByResultId(@SQLParam("resultId") Integer resultId,@SQLParam("subjective") int subjective);
	/**
	 * 某考试结果明细集合
	 */
	@SQL(" SELECT * FROM te_exam_result_detail WHERE result_id =:resultId order by id asc ")
	List<TpExamResultDetail> searchResultDetail(@SQLParam("resultId") Integer resultId);
	
	/**
	 * 主观题批阅
	 * 更新大题正确性
	 */
	@SQL(" update te_exam_result_detail set result_answer=:resultAnswer where question_id=:bigQuestionId and result_id=:resultId ")
	void updateDetailBigQuestionResultAnswer(Connection conn,@SQLParam("resultId") Integer resultId,@SQLParam("bigQuestionId") Integer bigQuestionId,@SQLParam("resultAnswer") Integer resultAnswer);
	/**
	 * 主观题批阅
	 * 更新大题得分
	 */
	@SQL(" update te_exam_result_detail set score=score+:score where question_id=:bigQuestionId and result_id=:resultId ")
	void updateDetailBigQuestionScore(Connection conn,@SQLParam("resultId") Integer resultId,@SQLParam("bigQuestionId") Integer bigQuestionId,@SQLParam("score") Double score);
	/**
	 * 查询结果明细表数据
	 * @param resultId
	 * @return
	 */
	@SQL(" SELECT DISTINCT ters.id,ters.parent,ters.`name`,ters.point points,ters.type structure_type,terd.question_id questionId " +
			" from te_exam_result_structure ters " +
			" left join te_exam_result_detail terd on ters.id=terd.exam_result_structure " +
			" where ters.result_id=:resultId and ters.type="+TpExamResultStructure.TYPE_BIG_QUESTION+" and terd.te_id="+TpExamResultDetail.TE_ID)
	List<TestPaperStructure> findExamResultDetail(@SQLParam("resultId") int resultId);
	/**
	 * 查询结果结构表数据
	 * @param resultId
	 * @return
	 */
	@SQL(" select ters.id,ters.parent,ters.`name`,ters.point points,ters.type structure_type From te_exam_result_structure ters where result_id=:resultId and ters.type="+TestPaperStructure.structure_type_structure)
	List<TestPaperStructure> findExamResultStructure(@SQLParam("resultId") int resultId);

}
