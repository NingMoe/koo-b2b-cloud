package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * 选择题
 * @author yangzhenye
 * @date 2015-3-5
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ChoiceQuestionDao {
	/**
	 * 插入选择题
	 * @param choiceQuestion 选择题对象
	 * @return 选择题ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(ChoiceQuestion choiceQuestion);
	

	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, ChoiceQuestion choiceQuestion);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param choiceQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(Connection conn, List<ChoiceQuestion> choiceQuestions);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param choiceQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(List<ChoiceQuestion> choiceQuestions);
	
	/**
	 * 修改选择题
	 * @param choiceQuestion 选择题对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(ChoiceQuestion choiceQuestion);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, ChoiceQuestion choiceQuestion);
	
	/**
	 * 批量更新
	 * @param conn
	 * @param choiceQuestion
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<ChoiceQuestion> choiceQuestions);
	
	/**
	 * 根据题目ID查选择题
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from TE_CHOICEQUESTION where question_id = :questionId")
	public ChoiceQuestion getByQuestionid(@SQLParam("questionId") Integer questionId);
	@SQL("select s.* from TE_CHOICEQUESTION s where  s.question_id in (:ids)")
	public List<ChoiceQuestion> batchFind(@SQLParam("ids") List<Integer> ids);



	/**
	 * 删除选择题
	 * @param conn
	 * @param ids
	 */
	@SQL("delete from TE_CHOICEQUESTION where question_id in (:ids)")
	public void deleteChoiceByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 删除
	 * @param id
	 */
	@SQL("delete from TE_CHOICEQUESTION where id=:id")
	public void deleteById(Connection conn, @SQLParam("id") int id);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	@SQL("delete from TE_CHOICEQUESTION where id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	@SQL("select s.id from TE_CHOICEQUESTION s join te_question t on s.question_id=t.id where t.te_id=:teid")
	public List<Integer> findByTeId(@SQLParam("teid") int teid);
}
