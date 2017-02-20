package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

public interface ShortQuestionService {
	/**
	 * 创建简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertShortQuestion(ShortQuestionDto dto) throws Exception;
	public int insertShortQuestion(Connection conn, ShortQuestionDto obj)throws Exception;
	/**
	 * 根据简答题id获取试题的所有信息
	 * TODO
	 * @param id
	 * @return
	 */
	public ShortQuestionDto getShortQuestionDto(int questionId);
	/**
	 * 修改简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public void updateShortQuestion(ShortQuestionDto dto) throws Exception;
	/**
	 * 子题目的另存为过程
	 * TODO
	 * @param conn
	 * @param questionId
	 * @param new_id
	 * @throws Exception 
	 */
	@Deprecated
	public void saveAs(Connection conn, int questionId, int new_id) throws Exception;
	/**
	 * 根据题目id获取写作题的所有信息（从缓存获取）
	 * TODO
	 * @param id
	 * @return
	 */
	public ShortQuestionDto getShortQuestionRepository(int id);
	/**
	 * 根据id集合获取简答题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<ShortQuestionDto> getShortQuestionListRepository(List<Integer> ids);
	/**
	 * 批量删除简答题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 另存子题目
	 * @param conn
	 * @param question
	 * @param new_id
	 * @throws Exception 
	 */
	public void saveAs(Connection conn, Question question, int new_id) throws Exception;
	/**
	 * 简答题保存
	 * @param conn
	 * @param dto
	 * @throws Exception
	 */
	public void saveOrUpdate(Connection conn, ShortQuestionDto dto) throws Exception;
	
}
