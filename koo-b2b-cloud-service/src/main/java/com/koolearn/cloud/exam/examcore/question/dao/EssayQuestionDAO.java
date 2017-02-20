package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;


/**
 * 填空题的DAO层  数据操作
 * @author liaoqiangang
 *
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface EssayQuestionDAO  {

	/**
	 * 创建连接，单个添加
	 * @param conn
	 * @param essayQuestion
	 */
	@SQL(type = SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, EssayQuestion essayQuestion);
	
	/*
	 * 根据ID查询对象
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public EssayQuestion select(int id);
	
	/**
	 * 单个对象的  更新
	 * @param essayQuestion
	 */
	@SQL(type = SQLType.WRITE_UPDATE)
	public void update(EssayQuestion essayQuestion);

	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, EssayQuestion essayQuestion);

	/**
	 * 根据题目ID查选择题
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from TE_ESSAYQUESTION where questtion_id = :questtionId")
	public EssayQuestion getByQuestionid(@SQLParam("questtionId") Integer questtionId);

	/**
	 * 删除题(填空/计算)
	 * @param conn
	 * @param ids
	 */
	@SQL("delete from TE_ESSAYQUESTION  where questtion_id in (:ids)")
	public void deleteEssayByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据ID删除
	 * @param conn
	 * @param id
	 */
	@SQL("delete from TE_ESSAYQUESTION where id = :id")
	public void deleteById(Connection conn, @SQLParam("id") int id);
	
	/**
	 * 根据题目ID查选择题
	 * @param questionIds 题目IDs
	 * @return
	 */
	@SQL("select * from TE_ESSAYQUESTION where questtion_id in (:questtionIds)")
	public EssayQuestion getByQuestionids(@SQLParam("questtionIds") List<Integer> questtionIds);
	
	/**
	 * 根据ID批量删除
	 * @param conn
	 * @param ids
	 */
	@SQL("delete from TE_ESSAYQUESTION where id in (:id)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);

	
	@SQL("select s.* from TE_ESSAYQUESTION s where  s.questtion_id in (:ids)")
	public List<EssayQuestion> batchFind(@SQLParam("ids") List<Integer> ids);
	
	@SQL("select s.id from TE_ESSAYQUESTION s where  s.questtion_id in (select id from te_question where te_id=:teid)")
	public List<Integer> findByTeId(@SQLParam("teid") int teid);
}
