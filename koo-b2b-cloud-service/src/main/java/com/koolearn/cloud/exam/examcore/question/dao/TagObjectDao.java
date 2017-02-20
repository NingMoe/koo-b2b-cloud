package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.exam.examcore.question.entity.TagsRelaQts;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface TagObjectDao {

	/**
	 * 插入标签
	 * TODO
	 * @param tagObject 标签对象
	 * @return 标签ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, TagObject tagObject);
	/**
	 * 插入标签
	 * TODO
	 * @param tagObject 标签对象
	 * @return 标签ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(TagObject tagObject);
	
	/**
	 * 批量插入
	 * TODO
	 * @param conn
	 * @param tagObjects
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(Connection conn, List<TagObject> tagObjects);
	
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(List<TagObject> tagObjects);
	
	/**
	 * 修改标签
	 * @param tagObject 标签对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public int update(Connection conn, TagObject tagObject);
	
	/**
	 * 修改标签
	 * @param tagObject 标签对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public int update(TagObject tagObject);
	
	/**
	 * 批量更新
	 * TODO
	 * @param conn
	 * @param tagObject
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<TagObject> tagObjects);
	
	/**
	 * 删除标签
	 * TODO
	 * @param id
	 */
	@SQL("delete from te_tag_object where id = :id")
	public void deleteById(@SQLParam("id") int id);
	@SQL("delete from te_tag_object where id in (:ids )")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据对象ID和类型查询标签
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	@SQL("select * from te_tag_object where object_id = :objectId and object_type=:objectType")
	public List<TagObject> searchByObject(@SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);
	
	/**
	 * 根据对象ID和类型删除标签
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	@SQL("delete from te_tag_object where object_id = :objectId and object_type=:objectType")
	public void deleteByObject(@SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);
	/**
	 * 根据对象ID和类型删除标签
	 * @param objectId
	 * @param objectType
	 * @return
	 */
	@SQL("delete from te_tag_object where object_id = :objectId and object_type=:objectType")
	public void deleteByObject(Connection conn, @SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);
	@SQL("delete from te_tag_object where object_id  in( :objectId) and object_type=:objectType")
	public void deleteByInObject(Connection conn, @SQLParam("objectId") List<Integer> objectId, @SQLParam("objectType") int objectType);
	
	@SQL("select tag_id,tag_name from te_tag_object where object_id = :objectId and object_type=:objectType and tag_id in (:tagIds) limit 1")
	public TagObject getTagIdByTagIds(@SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType, @SQLParam("tagIds") int[] tagIds);
	
	/**
	 * 根据ID查询标签与题型关系
	 * @param id
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.READ_BY_ID)
	public TagsRelaQts selectById(Integer id);
	
	/**
	 * 写入标签与题目类型关系
	 * @param conn
	 * @param tagsRelaQts
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insertTagsRelaQts(Connection conn, TagsRelaQts tagsRelaQts);
	
	/**
	 * 更新标签与题目类型关系
	 * @param conn
	 * @param tagsRelaQts
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void updateTagsRelaQts(Connection conn, TagsRelaQts tagsRelaQts);
	
	@SQL("SELECT * FROM te_tags_rela_qts WHERE tag = :tag")
	public TagsRelaQts selectByTag(@SQLParam("tag") Integer tag);
}
