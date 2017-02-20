package com.koolearn.cloud.exam.examcore.question.service;

import java.util.List;

import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;

public interface QuestiontypeService {
	String findItemName(int id);
	
	/**
	 * 根据父ID查询类型信息
	 * @param parent 父ID
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public List<Questiontype> searchByParent(int parent) throws Exception;
	
	/**
	 * 查询所有题型
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public List<Questiontype> searchAll() throws Exception;
}
