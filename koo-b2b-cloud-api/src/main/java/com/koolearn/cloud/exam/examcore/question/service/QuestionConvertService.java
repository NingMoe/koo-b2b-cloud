package com.koolearn.cloud.exam.examcore.question.service;

import com.koolearn.exam.base.dto.IExamQuestionDto;

/**
 * 考试试题转换，接口服务
 * @author yangzhenye
 */
public interface QuestionConvertService {

	/**
	 * 试题数据转换和保存
	 * @param examQuestionDto
	 * @param schoolId 
	 * @throws Exception
	 */
	public void saveQuestion(IExamQuestionDto examQuestionDto)throws Exception;
	/**
	 * 试题数据转换验证接口
	 * @param examQuestionDto
	 * @throws Exception
	 */
	public boolean saveCheckQuestion(IExamQuestionDto examQuestionDto) ;
	
}
