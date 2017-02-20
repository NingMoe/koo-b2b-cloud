package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;



public interface SpokenQuestionService{
	/**
	 * 口语题保存
	 * @param conn
	 * @param dto
	 * @throws Exception
	 */
	public void saveOrUpdate(Connection conn, SpokenQuestionDto dto) throws Exception;
	/**
	 * 创建口语题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertSpokenQuestion(SpokenQuestionDto dto) throws Exception;
	/**
	 * 根据口语题id获取试题的所有信息
	 * TODO
	 * @param id
	 * @return
	 */
	public SpokenQuestionDto getSpokenQuestionDto(int questionId);
	/**
	 * 修改口语题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public void updateSpokenQuestion(SpokenQuestionDto dto) throws Exception;
	/**
	 * 子题目的另存为过程
	 * TODO
	 * @param conn
	 * @param question
	 * @param new_id
	 * @throws Exception 
	 */
	public void saveAs(Connection conn, Question question, int new_id) throws Exception;
	/**
	 * 根据题目id获取写作题的所有信息（从缓存获取）
	 * TODO
	 * @param id
	 * @return
	 */
	public SpokenQuestionDto getSpokenQuestionRepository(int id);
	/**
	 * 根据id集合获取写作题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<SpokenQuestionDto> getSpokenQuestionListRepository(List<Integer> ids);
	/**
	 * 批量删除口语题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	public void deleteByIds(Connection conn, List<Integer> ids);
}
