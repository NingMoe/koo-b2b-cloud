package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * 选择题答案Dao
 * @author yangzhenye
 * @date 2015-3-5
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ChoiceAnswerDao {
	
	/**
	 * 删除不在ids范围内的材料
	 * @param questionID
	 * @param ids
	 * @return
	 */
	@SQL(type = SQLType.WRITE)
	public void deleteAttatch(Connection conn, int choice_id, List<Integer> ids) ;
	/**
	 * 插入选择题
	 * @param choiceAnswer 选择题答案对象
	 * @return 选择题ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(ChoiceAnswer choiceAnswer);
	
	
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, ChoiceAnswer choiceAnswer);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param choiceQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(Connection conn, List<ChoiceAnswer> choiceAnswers);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param choiceQuestions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int[] batchInsert(List<ChoiceAnswer> choiceAnswers);
	
	/**
	 * 修改选择题答案
	 * @param choiceAnswer 选择题答案对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(ChoiceAnswer choiceAnswer);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, ChoiceAnswer choiceAnswer);
	
	/**
	 * 批量更新
	 * @param conn
	 * @param choiceAnswer
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<ChoiceAnswer> choiceAnswers);
	
	/**
	 * 根据选择题ID查选择题答案
	 * @param choiceId 选择题ID
	 * @return
	 */
	@SQL("select * from TE_CHOICEANSWER where choice_id = :choice_id ORDER BY sequence_id,id ASC")//DuHongLin 2014-06-25 15:50
	public List<ChoiceAnswer> getByChoiceId(@SQLParam("choice_id") Integer choiceId);
	@SQL("select s.* from TE_CHOICEANSWER s where  s.choice_id in (:ids) order by choice_id,sequence_id asc ")
	public List<ChoiceAnswer> batchFind(@SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据选择题ID删除答案
	 * @param conn
	 * @param choiceId
	 */
	@SQL("delete from TE_CHOICEANSWER where choice_id = :choiceId")
	public void deleteByChoiceId(Connection conn, @SQLParam("choiceId") int choiceId);
	
	/**
	 * 根据试题id集合删除选择题答案
	 * @param conn
	 * @param ids
	 */
//	@SQL("delete from TE_CHOICEANSWER  where choice_id in (select id from TE_CHOICEQUESTION where question_id in (:ids))")
	@SQL("delete t1.* from TE_CHOICEANSWER t1,TE_CHOICEQUESTION t2 where t1.choice_id=t2.id and t2.question_id in (:ids)")
	public void deleteAnswerByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	/**
	 * 根据选择题ID删除答案
	 * @param conn
	 * @param choiceIds
	 */
	@SQL("delete from TE_CHOICEANSWER where choice_id in (:choiceIds)")
	public void deleteByChoiceIds(Connection conn, @SQLParam("choiceIds") List<Integer> choiceIds);
	
	/**
	 * 根据ID删除答案
	 * @param conn
	 * @param choiceId
	 */
	@SQL("delete from TE_CHOICEANSWER where id = :id")
	public void deleteById(Connection conn, @SQLParam("id") int id);
	
	/**
	 * 根据ID删除答案
	 * @param conn
	 * @param choiceId
	 */
	@SQL("delete from TE_CHOICEANSWER where id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 根据orderby升序返回排序题ChoiceAnswer
	 * @param choiceId
	 * @return
	 */
	@SQL("select * from TE_CHOICEANSWER where choice_id = :choice_id ORDER BY orderby")
	public List<ChoiceAnswer> getByChoiceIdOrderBy(@SQLParam("choice_id") Integer choiceId);
}