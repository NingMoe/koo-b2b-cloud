package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.MatrixQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface MatrixQuestionDao {

	/**
	 * 插入题目
	 * @param matrixQuestion 题目对象
	 * @return 题目ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(MatrixQuestion matrixQuestion);
	
	/**
	 * 插入题目，有事务
	 * @param matrixQuestion 题目对象
	 * @return 题目ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, MatrixQuestion matrixQuestion);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param matrixQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(Connection conn, List<MatrixQuestion> matrixQuestions);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param matrixQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(List<MatrixQuestion> matrixQuestions);
	
	/**
	 * 修改题目
	 * @param matrixQuestion 题目对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(MatrixQuestion matrixQuestion);
	
	/**
	 * 修改题目，有事务
	 * @param matrixQuestion 题目对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, MatrixQuestion matrixQuestion);
	
	/**
	 * 批量更新
	 * @param conn
	 * @param matrixQuestion
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<MatrixQuestion> matrixQuestions);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public MatrixQuestion getById(int id);
	
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 */
	@SQL("select * from te_matrixquestion where question_id=:questionId limit 1")
	public MatrixQuestion getByQuestionId(@SQLParam("questionId") int questionId);
	
	@SQL("delete from te_matrixquestion where question_id in (:ids)")
	public void deleteByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	@SQL("select * from te_matrixquestion where question_id in (:ids)")
	public List<MatrixQuestion> getByQuestionIds(@SQLParam("ids") List<Integer> ids);
	
	@SQL("delete from te_matrixquestion where question_id =:questionId")
	public void deleteByQuestionId(Connection conn, @SQLParam("questionId") int questionId);
}
