package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.SpokenQuestion;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;


@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface SpokenQuestionDao {
	/**
	 * 插入口语题
	 * @param spokenQuestion 口语题对象
	 * @return 口语题ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(SpokenQuestion spokenQuestion);
	
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(Connection conn, SpokenQuestion spokenQuestion);
	
	/**
	 * 根据口语题id查询口语题基本信息
	 * TODO
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public SpokenQuestion selectSpokenQuestionById(int id);
	/**
	 * 根据题目ID查口语题
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from te_spokenequestion where question_id = :questionId")
	public SpokenQuestion getByQuestionid(@SQLParam("questionId") Integer questionId);
	/**
	 * 更新口语题信息
	 * TODO
	 * @param conn
	 * @param test
	 */
	@SQL(type = SQLType.WRITE_UPDATE)
	public void updateSpokenQuestion(Connection conn, SpokenQuestion spokenQuestion);
	/**
	 * 根据批量考试id查询口语题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("select s.* from te_spokenequestion s where  s.question_id in (:ids)")
	public List<SpokenQuestion> batchFind(@SQLParam("ids") List<Integer> ids);

	/**
	 * 批量删除口语题
	 * @param ids
	 */
	@SQL("delete from te_spokenequestion where question_id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	/**
	 * 根据批量考试父id查询简答题信息
	 * TODO
	 * @param ids
	 * @return
	 */
	@SQL("SELECT s.* FROM te_spokenequestion  s ,te_question t WHERE s.question_id=t. id AND t.te_id  =:teId")
	public List<SpokenQuestion> batchFindByTeId(Connection conn, @SQLParam("teId") Integer teId);
	
}
