package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

public interface EssayQuestionService{

	/**
	 * 保存，修改 功能
	 * @param essayQuestionDTO
	 */
	public int saveOrUpdate(EssayQuestionDto essayQuestionDTO)throws Exception;
	public int saveOrUpdate(Connection conn, EssayQuestionDto essayQuestionDTO)throws Exception;
	
	/**
	 * 根据试题ID获取选择题对象
	 * @param questionId
	 * @return
	 */
	EssayQuestion getEssayQuestionByQuestionId(int questionId);
	
	/**
	 * 根据填空题ID获取填空题答案对象
	 * @param questionId
	 * @return
	 */
	List<FillblankAnswer> getFillblankAnswersById(int essayQuestionId);


	public EssayQuestionDto getEssayQuestionDTO(int questionId);


	/**
	 * 另存
	 * @param conn
	 * @param questionId
	 * @param new_id
	 * @throws Exception
	 */
	public void saveAs(Connection conn, Question question, int new_id)throws Exception;
	
	
	/**
	 * 批量获取对象,only DAO
	 * 缓存<外部调用>
	 * @param questionIds 
	 * @return
	 */
	List<EssayQuestionDto> batchFindRepository(List<Integer> questionIds)throws Exception;
	
	
	public void deleteEssayQuestion(Connection conn, int questionId);



}
