package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.WhriteQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;


@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface WhriteQuestionDao {
	/**
	 * 插入写作题
	 * @param whritequestion 写作题对象
	 * @return 写作题ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(WhriteQuestion whritequestion);
	
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(Connection conn, WhriteQuestion whritequestion);
	
	/**
	 * 根据写作题id查询写作题基本信息
	 * TODO
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public WhriteQuestion selectWhriteQuestionById(int id);
	/**
	 * 根据题目ID查写作题
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from te_whritequestion where question_id = :questionId")
	public WhriteQuestion getByQuestionid(@SQLParam("questionId") Integer questionId);
	/**
	 * 更新写作题信息
	 * TODO
	 * @param conn
	 * @param test
	 */
	@SQL(type = SQLType.WRITE_UPDATE)
	public void updateWhriteQuestion(Connection conn, WhriteQuestion whritequestion);
	/**
	 * 根据批量考试id查询写作题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("select s.* from te_whritequestion s where  s.question_id in (:ids)")
	public List<WhriteQuestion> batchFind(@SQLParam("ids") List<Integer> ids);

	/**
	 * 批量删除写作题
	 * @param ids
	 */
	@SQL("delete from te_whritequestion where question_id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据批量考试父id查询简答题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("select w.* from te_whritequestion w join te_question t on w.question_id=t.id  where t.te_id  =:teId")
	public List<WhriteQuestion> batchFindByTeId(Connection conn, @SQLParam("teId") Integer teId);
}
