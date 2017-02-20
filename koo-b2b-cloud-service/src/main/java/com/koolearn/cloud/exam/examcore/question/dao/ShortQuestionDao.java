package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.ShortQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;


@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ShortQuestionDao {
	/**
	 * 插入简答题
	 * @param shortQuestion 简答题对象
	 * @return 简答题ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(ShortQuestion shortQuestion);
	
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(Connection conn, ShortQuestion shortQuestion);
	
	/**
	 * 根据简答题id查询简答题基本信息
	 * TODO
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public ShortQuestion selectShortQuestionById(int id);
	/**
	 * 根据题目ID查简答题
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from te_shortquestion where question_id =:questionId")
	public ShortQuestion getByQuestionid(@SQLParam("questionId") Integer questionId);
	/**
	 * 更新简答题信息
	 * TODO
	 * @param conn
	 * @param test
	 */
	@SQL(type = SQLType.WRITE_UPDATE)
	public void updateShortQuestion(Connection conn, ShortQuestion shortQuestion);
	/**
	 * 根据批量考试id查询简答题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("select s.* from te_shortquestion s where  s.question_id in (:ids)")
	public List<ShortQuestion> batchFind(@SQLParam("ids") List<Integer> ids);

	/**
	 * 批量删除简答题 
	 * @param ids
	 */
	@SQL("delete from te_shortquestion where question_id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据批量考试父id查询简答题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("SELECT s.* FROM te_shortquestion s ,te_question t WHERE s.question_id=t.id AND t.te_id  =:teId")
	public List<ShortQuestion> batchFindByTeId(Connection conn, @SQLParam("teId") Integer teId);
}
