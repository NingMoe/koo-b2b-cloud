package com.koolearn.cloud.dictionary.service;

import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.util.SelectDTO;

import java.util.List;

public interface DictionaryService {
	/**
	 * 获得所有可用的字典
	 * @param status
	 * @return
	 */
	List<Dictionary> getDataDictionaryByStats(int status);

	/**
	 * 根据类型获得字典信息分类集合
	 * @param type
	 * @return
	 */
	List<Dictionary> getDataDictionaryByType(Integer type);


    List<Dictionary> getDataDictionaryByTypeOrder(Integer type);

	/**
	 * 获取科目
	 * @param teacherId
	 * @return
     */
	List<SelectDTO> findTeacherSubject(int teacherId);

	/**
	 * 获取学段
	 * @param teacherId
	 * @param subjectId
     * @return
     */
	List<SelectDTO> findTeacherRange(int teacherId, int subjectId);

	/**
	 * 获取教材版本
	 * @param teacherId
	 * @param rangeId
     * @return
     */
	List<SelectDTO> findTeacherBookVersion(int teacherId, int rangeId);

	/**
	 * 获取年级.学科组合
	 * @param teacherId
	 * @return
     */
	List<SelectDTO> findTeacherSubjectName(int teacherId);

    /**
     * 查询满足名字和类型第一个字典实体对象
     * @param name
     * @param type
     * @return
     */
    Dictionary queryDictionaryByTypeAndName(String name,Integer type);
}
