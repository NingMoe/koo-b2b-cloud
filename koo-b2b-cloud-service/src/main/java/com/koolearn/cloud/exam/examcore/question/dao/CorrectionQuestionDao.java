package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.CorrectionQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * 
 * @author yangzhenye
 * @date 2015-3-5
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface CorrectionQuestionDao {
	
	/**
	 * 插入题目
	 * @param correctionQuestion 题目对象
	 * @return 题目ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(CorrectionQuestion correctionQuestion);
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, CorrectionQuestion correctionQuestion);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param correctionQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(Connection conn, List<CorrectionQuestion> correctionQuestions);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param correctionQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(List<CorrectionQuestion> correctionQuestions);
	
	/**
	 * 修改题目
	 * @param correctionQuestion 题目对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(CorrectionQuestion correctionQuestion);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, CorrectionQuestion correctionQuestion);
	
	/**
	 * 批量更新
	 * @param conn
	 * @param correctionQuestion
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<CorrectionQuestion> correctionQuestions);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public CorrectionQuestion getById(int id);
	
	/**
	 * 根据大题ID查询
	 * @param questionId
	 * @return
	 */
	@SQL("select * from te_correctionquestion where complex_id=:complexId ")
	public List<CorrectionQuestion> getByComplexId(@SQLParam("complexId") int complexId);
	
	/**
	 * 根据题目ID查询
	 * @param questionId
	 * @return
	 */
	@SQL("select * from te_correctionquestion where question_id=:questionId ")
	public CorrectionQuestion getByQuestionId(@SQLParam("questionId") int questionId);
	
	/**
	 * 删除
	 * @param id
	 */
	@SQL("delete from te_correctionquestion where id=:id")
	public void deleteById(@SQLParam("id") int id);
	
	@SQL("delete from te_correctionquestion where id=:id")
	public void deleteById(Connection conn, @SQLParam("id") int id);
	
	/**
	 * 根据题目ID删除
	 * @param questionId
	 */
	@SQL("delete from te_correctionquestion where question_id=:questionId")
	public void deleteByQuestionId(@SQLParam("questionId") int questionId);
	
	@SQL("delete from te_correctionquestion where question_id=:questionId")
	public void deleteByQuestionId(Connection conn, @SQLParam("questionId") int questionId);
	
	@SQL("delete from te_correctionquestion where question_id in (select id from te_question where te_id=:teId)")
	public void deleteByTeId(Connection conn, @SQLParam("teId") int teId);
	
	@SQL("select s.* from te_correctionquestion s where  s.question_id in (:ids)")
	public List<CorrectionQuestion> batchFind(@SQLParam("ids") List<Integer> ids);
	
}
