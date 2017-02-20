package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;

/**
 * 阅读理解service
 * @author wangpeng
 * @date Nov 6, 2012
 * 技术教辅社区组@koolearn.com
 */
public interface ReadQuestionService {
	/**
	 * 保存或更新返回questionid 和read id
	 * @param complexQuestionDto
	 * @throws ExamException
	 * RETURN 
	 */
	public int[] saveOrUpdateRead(ComplexQuestionDto complexQuestionDto) throws Exception;
	
	/**
	 * 删除题目
	 * @param questionId 题目ID
	 * @throws ExamException
	 */
	public void deleteReadByQuestionId(Connection conn, int questionId)throws Exception  ;
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 * @throws ExamException
	 */
	public ComplexQuestionDto getReadByQuestionId(int questionId)  throws Exception;
	/**
	 * 根据题型 id 删除子类
	 * @param questionId
	 * @param typeId
	 */
	public void deleteSubItem(int questionId, int typeId) throws Exception;
	/**
	 * 批量获取对象
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<ComplexQuestionDto> batchFindRepository(List<Integer> ids) throws Exception;

	public void saveOrUpdateRead(Connection conn, ComplexQuestionDto obj)throws Exception;
	public void saveSyncRead(Connection conn, ComplexQuestionDto obj)throws Exception;
	/**
	 * 复合题目编辑前备份
	 * @param complexQuestionDto
	 * @return
	 * @throws Exception
	 */
	public int[] backReadQuestion(ComplexQuestionDto complexQuestionDto) throws Exception;
}
