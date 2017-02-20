package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

/**
 * 矩阵题服务接口
 * TODO
 * @author tangshuren
 * @date 2012-10-29
 *
 */
public interface MatrixQuestionService {

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MatrixQuestionDto getById(int id)  throws Exception;
	
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 * @throws Exception
	 */
	public MatrixQuestionDto getByQuestionId(int questionId);
	
	/**
	 * 添加或修改
	 * @param matrixQuestionDto
	 * @throws Exception
	 */
	public int saveOrUpdate(MatrixQuestionDto matrixQuestionDto) throws Exception;
	
	/**
	 * 添加或修改
	 * @param matrixQuestionDto
	 * @param saveType 题目保存状态（保存或另存为）
	 * @throws Exception
	 */
	public void saveOrUpdate(Connection conn, MatrixQuestionDto matrixQuestionDto, int saveType) throws Exception;
	
	/**
	 * 添加或修改
	 * @param matrixQuestionDto
	 * @param idMap 要删除的子试题IDs，只在未审核状态修改时起作用，（key=qid:基础题目ID，key=id:子题目ID）
	 * @throws Exception
	 */
	public void saveOrUpdate(MatrixQuestionDto matrixQuestionDto, Map<String, String> idMap) throws Exception;
	
	/**
	 * 另存，当矩阵题作为子试题时使用
	 * @param conn
	 * @param questionId 旧的父试题Id
	 * @param new_id 新的父试题Id
	 * @throws Exception
	 */
	public void saveAs(Connection conn, Question question, int new_id) throws Exception;

	/**
	 * 从缓存中批量获取题目
	 * @param questionIds
	 * @return
	 * @throws Exception
	 */
	public List<MatrixQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception;
	
	/**
	 * 删除矩阵题（同时删除子题和材料）
	 * @param conn
	 * @param questionId
	 * @throws Exception
	 */
	public void deleteMatrixQuestion(Connection conn, int questionId) throws Exception;
	
}
