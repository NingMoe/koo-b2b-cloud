package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.util.ExamException;

public interface WhriteQuestionService {
	/**
	 * 创建写作题
	 * TODO
	 * @param dto
	 * @throws ExamException
	 */
	public int insertWhriteQuestion(WhriteQuestionDto dto) throws ExamException;
	public int insertWhriteQuestion(Connection conn, WhriteQuestionDto obj)throws ExamException;
	/**
	 * 根据写作题id获取试题的所有信息
	 * TODO
	 * @param id
	 * @return
	 */
	public WhriteQuestionDto getWhriteQuestionDto(int questionId);
	/**
	 * 修改写作题
	 * TODO
	 * @param dto
	 * @throws ExamException
	 */
	public void updateWhriteQuestion(WhriteQuestionDto dto) throws Exception;
	/**
	 * 子题目的另存为过程
	 * TODO
	 * @param conn
	 * @param questionId
	 * @param new_id
	 * @throws ExamException 
	 */
	public void saveAs(Connection conn, Question question, int new_id) throws ExamException;
	/**
	 * 根据题目id获取写作题的所有信息（从缓存获取）
	 * TODO
	 * @param id
	 * @return
	 */
	public WhriteQuestionDto getWhriteQuestionRepository(int id);
	/**
	 * 根据id集合获取简答题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<WhriteQuestionDto> getWhriteQuestionListRepository(List<Integer> ids);
	/**
	 * 批量删除写作题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	public void deleteByIds(Connection conn, List<Integer> ids);
	/**
	 * 保存更新写作题
	 */
	public void saveOrUpdate(WhriteQuestionDto questionDto)throws Exception;
	/**
	 * 保存更新写作题
	 */
	public void saveOrUpdate(Connection conn, WhriteQuestionDto questionDto)throws Exception;
	

}
