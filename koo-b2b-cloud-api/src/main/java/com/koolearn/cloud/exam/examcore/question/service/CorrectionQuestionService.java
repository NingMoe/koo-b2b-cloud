package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto;

public interface CorrectionQuestionService{
	
	/**
	 * 根据题目表ID查询
	 * @param questionId
	 * @return
	 */
	public ComplexCorrectionQuestionDto getByQuestionId(int questionId);

	/**
	 * 添加或修改
	 * @param ComplexCorrectionQuestionDto
	 * @return
	 */
	public int saveOrUpdate(ComplexCorrectionQuestionDto ComplexCorrectionQuestionDto);
	public int saveOrUpdate(Connection conn, ComplexCorrectionQuestionDto ComplexCorrectionQuestionDto) throws Exception;
	
	/**
	 * 根据 题目表ID删除
	 * @param conn
	 * @param questionId
	 */
	public void deleteByQuestionId(Connection conn, int questionId);
	
	/**
	 * 另存
	 * @param conn
	 * @param questionId
	 * @param lookupWordQuesitonDto
	 */
	public void saveAs(Connection conn, int questionId, ComplexCorrectionQuestionDto lookupWordQuesitonDto);
	
	/**
	 * 从缓存查询多个题目
	 * @param questionIds
	 * @return
	 * @throws Exception
	 */
	public List<ComplexCorrectionQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception;
	
	/**
	 * 从缓存查询单个子试题
	 * @param questionId
	 * @return
	 * @throws Exception
	 */
	public CorrectionQuestionDto findCorrectionDtoQuestionByQuestionIdRepository(int questionId) throws Exception;
	/**
	 * 从缓存查询单个多个试题
	 * @param questionId
	 * @return
	 * @throws Exception
	 */
	public List<CorrectionQuestionDto> batchFindSubRepository(List<Integer> questionIds) throws Exception;
	
	/**
	 * 根据子题目ID查询子题
	 * @param questionId
	 * @return
	 */
	public CorrectionQuestionDto getSubByQuestionId(int questionId);
}
