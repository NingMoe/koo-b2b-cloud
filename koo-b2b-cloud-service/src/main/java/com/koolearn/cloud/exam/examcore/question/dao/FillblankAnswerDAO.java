package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * 填空题的 DAO层  数据操作  
 * @author liaoqiangang
 *
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface FillblankAnswerDAO {

	/**
	 * 建立连接，插入对象
	 * @param conn
	 * @param fillblankAnswer
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, FillblankAnswer fillblankAnswer);
	
	
	
	/**
	 * 批量 添加答案表  
	 * @param conn
	 * @param fillblankAnswerList
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insertList(Connection conn, List<FillblankAnswer> fillblankAnswerList);
	
	


	/**
	 * 
	 * @param conn
	 * @param fillblankAnswers
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public void batchInsert(Connection conn, List<FillblankAnswer> fillblankAnswers);


	/**
	 * 
	 * @param conn
	 * @param updateObj
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<FillblankAnswer> updateObj);


	/**
	 * 
	 * @param conn
	 * @param choice_id
	 * @param updateIds
	 */
	@SQL(type = SQLType.WRITE)
	public void deleteAttatch(Connection conn, int choice_id, List<Integer> updateIds);


	/**
	 * 根据选择题ID查选择题答案
	 * @param choiceId 选择题ID
	 * @return
	 */
	@SQL("select * from TE_FILLBLANK_ANSWER where fillblank_id = :fillblank_id")
	public List<FillblankAnswer> getFillblankAnswersById(@SQLParam("fillblank_id") Integer fillblank_id);


	/**
	 * 根据试题id集合删除填空题答案
	 * @param conn
	 * @param ids
	 */
	/**
	*删除
	*/
	@SQL("delete from TE_FILLBLANK_ANSWER  where fillblank_id in (select id from TE_ESSAYQUESTION where questtion_id in (:ids))")
	public void deleteAnswerByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	@SQL(type = SQLType.WRITE_UPDATE)
	public void update(Connection conn, FillblankAnswer fillblankAnswer);
	
	/**
	 * 根据填空题ID删除答案
	 * @param conn
	 * @param fillblankId
	 */
	@SQL("delete from TE_FILLBLANK_ANSWER where fillblank_id = :fillblankId")
	public void deleteByEssayId(Connection conn, @SQLParam("fillblankId") int fillblankId);
	
	/**
	 * 根据填空题ID删除答案
	 * @param conn
	 * @param fillblankIds
	 */
	@SQL("delete from TE_FILLBLANK_ANSWER where fillblank_id in (:fillblankIds)")
	public void deleteByEssayIds(Connection conn, @SQLParam("fillblankIds") List<Integer> fillblankIds);
	
	/**
	 * 根据ID删除答案
	 * @param conn
	 * @param ids
	 */
	@SQL("delete from TE_FILLBLANK_ANSWER where id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);


	@SQL("select s.* from TE_FILLBLANK_ANSWER s where  s.fillblank_id in (:ids)")
	public List<FillblankAnswer> batchFind(@SQLParam("ids") List<Integer> ids);
	
}
