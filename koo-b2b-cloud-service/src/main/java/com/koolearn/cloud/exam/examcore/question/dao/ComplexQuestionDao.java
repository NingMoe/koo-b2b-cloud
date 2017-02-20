package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * 
 * @author yangzhenye
 * @date 2015-3-5
 *
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ComplexQuestionDao {

	/**
	 * 插入题目
	 * @param complexQuestion 题目对象
	 * @return 题目ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(ComplexQuestion complexQuestion);
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, ComplexQuestion complexQuestion);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param complexQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(Connection conn, List<ComplexQuestion> complexQuestions);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param complexQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(List<ComplexQuestion> complexQuestions);
	
	/**
	 * 修改题目
	 * @param complexQuestion 题目对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(ComplexQuestion complexQuestion);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, ComplexQuestion complexQuestion);
	
	/**
	 * 批量更新
	 * @param conn
	 * @param complexQuestion
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<ComplexQuestion> complexQuestions);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public ComplexQuestion getById(int id);
	
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 */
	@SQL("select * from te_complexquestion where question_id=:questionId limit 1")
	public ComplexQuestion getByQuestionId(@SQLParam("questionId") int questionId);
	
	/**
	 * 删除
	 * @param id
	 */
	@SQL("delete from te_complexquestion where id=:id")
	public void deleteById(@SQLParam("id") int id);
	
	/**
	 * 根据题目ID删除
	 * @param questionId
	 */
	@SQL("delete from te_complexquestion where question_id=:questionId")
	public void deleteByQuestionId(@SQLParam("questionId") int questionId);
	
	@SQL("delete from te_complexquestion where question_id=:questionId")
	public void deleteByQuestionId(Connection conn, @SQLParam("questionId") int questionId);
	
	@SQL("select c.* from te_choicequestion c where c.question_id in (select id from  te_question  where te_id=:questionId)")
	public List<ChoiceQuestion> getSubChoiceByQuestionid(@SQLParam("questionId") int questionId);

	/**
	 * 删除多个试题
	 * @param ids
	 */
	@SQL("delete from te_complexquestion where question_id in(:questionIds)")
	public void deleteByQuestionIds(Connection conn, @SQLParam("questionIds") List<Integer> ids);
	
	@SQL("select * from te_complexquestion where question_id in(:ids)")
	public List<ComplexQuestion> getByQuestionIds(@SQLParam("ids") List<Integer> ids);
	
}
