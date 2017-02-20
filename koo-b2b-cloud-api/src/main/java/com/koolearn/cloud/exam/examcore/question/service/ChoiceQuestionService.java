package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

/**
 * 选择题service
 * @author wangpeng
 * @date Oct 25, 2012
 * 技术教辅社区组@koolearn.com
 */
public interface ChoiceQuestionService {
	/**
	 * 保存选择题
	 * 包括选择本身,选择题答案,试题基础表,材料,标签
	 * @param choiceQuestionDto
	 */
	int saveOrUpdate(ChoiceQuestionDto choiceQuestionDto);
	int saveOrUpdate(Connection conn, ChoiceQuestionDto obj) throws Exception;
	
	/**
	 * 获取选择题信息
	 * 包括选择本身,选择题答案,试题基础表,材料,标签
	 * @param questionId 试题ID
	 * @return 选择题所有涉及信息
	 */
	ChoiceQuestionDto getChoiceQuestion(int questionId);
	
	/**
	 * 删除选择题
	 * @param questionId
	 */
	void deleteChoiceQuestion(Connection conn, int questionId);

	/**
	 * 通过id,code检查编码是否重复
	 * @param strId
	 * @param strCode
	 * @return 
	 */
	boolean checkUniqueCode(String strId, String strCode);

	/**
	 * 另存作为子试题的选择题
	 * @param conn
	 * @param questionId te_id 原来的
	 * @param new_id te_id 新的
	 * @throws Exception 
	 */
	void saveAs(Connection conn, Question question, int new_id) throws Exception;

	/**
	 * 批量获取对象,only DAO
	 * 缓存<外部调用>
	 * @param questionIds 
	 * @return
	 */
	List<ChoiceQuestionDto> batchFindRepository(List<Integer> questionIds)throws Exception;
	
	/**
	 * 通过题目choice_id获得排序题的正确排序
	 * @param questionId
	 * @return
	 */
	String getSortSequence(int choice_id);

	
}
