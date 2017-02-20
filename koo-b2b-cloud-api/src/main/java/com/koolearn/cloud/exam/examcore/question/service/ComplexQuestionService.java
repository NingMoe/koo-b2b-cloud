package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

/**
 * 完形填空题服务接口
 * @author tangshuren
 * @date 2012-10-29
 *
 */
public interface ComplexQuestionService{

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ComplexQuestionDto getById(int id)  throws Exception;
	
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 * @throws Exception
	 */
	public ComplexQuestionDto getByQuestionId(int questionId) ;
	
	/**
	 * 删除题目
	 * @param questionId 题目ID
	 * @throws Exception
	 */
	public void deleteByQuestionId(int questionId)  throws Exception;
	
	/**
	 * 添加或修改
	 * @param complexQuestionDto
	 * @throws Exception
	 */
	public int saveOrUpdate(ComplexQuestionDto complexQuestionDto) throws Exception;
	public int saveOrUpdate(Connection conn, ComplexQuestionDto complexQuestionDto)throws Exception;
	/**
	 * 添加或修改、删除
	 * @param complexQuestionDto
	 * @param idMap 要删除的子试题IDs,答案IDs，只在未审核状态修改时起作用，（key=qid:基础题目ID，key=id:子题目ID，key=aid:答案ID）
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdate(ComplexQuestionDto complexQuestionDto, Map<String, String> idMap) throws Exception;
	
	/**
	 * 从缓存中批量获取题目
	 * @param questionIds
	 * @return
	 * @throws Exception
	 */
	public List<ComplexQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception ;
	
	/**
	 * 删除完型填空题（同时删除子题和材料）
	 * @param conn
	 * @param questionId
	 * @throws Exception
	 */
	public void deleteComplexQuestion(Connection conn, int questionId) throws Exception;

	/**
	 * 另存,为子试题时使用
	 * @param conn
	 * @param questionId te_id 原来的
	 * @param new_id te_id 新的
	 * @throws Exception 
	 */
	public void saveAs(Connection conn, Question question, int new_id) throws Exception;

	
}
