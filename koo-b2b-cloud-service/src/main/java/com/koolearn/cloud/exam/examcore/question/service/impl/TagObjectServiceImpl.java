package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.TagObjectDao;
import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.exam.examcore.question.entity.TagsRelaQts;
import com.koolearn.cloud.exam.examcore.question.service.TagObjectService;
import com.koolearn.cloud.util.ConnUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class TagObjectServiceImpl implements TagObjectService {

	@Autowired
	private TagObjectDao tagObjectDao;

    public TagObjectDao getTagObjectDao() {
        return tagObjectDao;
    }

    public void setTagObjectDao(TagObjectDao tagObjectDao) {
        this.tagObjectDao = tagObjectDao;
    }

    //根据id删除标签
	public void deleteByIds(Connection conn, List<Integer> ids) {
		tagObjectDao.deleteByIds(conn,ids);
	}
	public int[] batchInsert(Connection conn, List<TagObject> tagObjects) {
		return tagObjectDao.batchInsert(conn, tagObjects);
	}
	public String save(Connection conn, List<TagObject> tagObjects, int objectType) {
		String fullPath = "";
		if(tagObjects != null){
			for(TagObject to : tagObjects){
				to.setCreateTime(new Date());
				to.setObjectType(objectType);
//				Tags tag = tagsService.getTag(to.getTagId());
//				if(tag != null){
//					to.setTagName(tag.getName());
//					if(StringUtils.isNotBlank(tag.getFull_Path())){
//						to.setFullPath(tag.getFull_Path());
//						fullPath += tag.getFull_Path()+",";
//					}
//				}
			}
			tagObjectDao.batchInsert(conn, tagObjects);
		}
		if(fullPath.endsWith(",")){
			fullPath = fullPath.substring(0,fullPath.length()-1);
		}
		return fullPath;
	}
	@Override
	public List<TagObject> searchByObject(int objectId, int objectType) {
		return tagObjectDao.searchByObject(objectId, objectType);
	}
	@Override
	public void deleteByObject(Connection conn, int objectId, int objectType) {
		tagObjectDao.deleteByObject(conn,objectId, objectType);
		
	}
	@Override
	public TagsRelaQts searchById(Integer id) throws Exception
	{
		return this.tagObjectDao.selectById(id);
	}
	@Override
	public int add(TagsRelaQts tagsRelaQts) throws Exception
	{
		int result = -1;
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			result = this.tagObjectDao.insertTagsRelaQts(conn, tagsRelaQts);
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
		return result;
	}
	@Override
	public void edit(TagsRelaQts tagsRelaQts) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			this.tagObjectDao.updateTagsRelaQts(conn, tagsRelaQts);
			conn.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
	}
	@Override
	public TagsRelaQts searchByTag(Integer tag) throws Exception
	{
		return this.tagObjectDao.selectByTag(tag);
	}
}
