package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.exam.examcore.question.entity.TagsRelaQts;

public interface TagObjectService{

	
	/**
	 * 批量插入
	 * @param conn
	 * @param tagObjects
	 * @return
	 */
	public int[] batchInsert(Connection conn, List<TagObject> tagObjects);
	
	/**
	 * 保存标签，返回fullPath
	 * 
	 * @param conn
	 * @param tagObjects
	 * @param objectType  1：试卷，2：试题，3：考试
	 * @return fullPath
	 */
	public String save(Connection conn, List<TagObject> tagObjects, int objectType);
	
	/**
	 * 根据对象ID和类型查询标签
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public List<TagObject> searchByObject(int objectId, int objectType);
	/**
	 * 根据对象ID和类型删除标签
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	public void deleteByObject(Connection conn, int questionId, int questionId2);
	/**
	 * 根据id删除标签
	 * @param conn
	 * @param ids
	 */
	public void deleteByIds(Connection conn, List<Integer> ids);
	
	/**
	 * 根据ID查询标签与题型关系
	 * @param id
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public TagsRelaQts searchById(Integer id) throws Exception;
	
	/**
	 * 保存关系
	 * @param tagsRelaQts
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public int add(TagsRelaQts tagsRelaQts) throws Exception;
	
	/**
	 * 更新
	 * @param tagsRelaQts
	 * @throws Exception
	 * @author DuHongLin
	 */
	public void edit(TagsRelaQts tagsRelaQts) throws Exception;
	
	/**
	 * 跟句标签获取题型
	 * @param tag
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public TagsRelaQts searchByTag(Integer tag) throws Exception;
	
}
