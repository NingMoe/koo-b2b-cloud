package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

/**
 * @author yangzhenye
 * @date 2015-3-5
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface QuestionAttachDao {

	/**
	 * 删除不在ids范围内的材料
	 * @param questionID
	 * @param ids
	 * @return
	 */
	@SQL(type = SQLType.WRITE)
	public void deleteAttatch(Connection conn, int questionID, List<Integer> ids) ;
	@SQL("delete from te_questionattach where question_id=:questionId and attach_type=0;")
	public void deleteAttatchByQuestionId(Connection conn, @SQLParam("questionId") int questionID);
	/**
	 * 插入题目材料
	 * @param questionattach 题目材料对象
	 * @return 题目材料ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(QuestionAttach questionattach);
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, QuestionAttach questionattach);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param questionattachs
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(Connection conn, List<QuestionAttach> questionattachs);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param questionattachs
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(List<QuestionAttach> questionattachs);
	
	/**
	 * 修改题目材料
	 * @param questionattach 题目材料对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(QuestionAttach questionattach);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, QuestionAttach questionattach);
	
	/**
	 * 批量更新
	 * TODO
	 * @param conn
	 * @param questionattach
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<QuestionAttach> questionattachs);
	
	/**
	 * 根据题目ID查询材料
	 * @param questionId 题目ID
	 * @return
	 */
	@SQL("select * from te_questionattach where question_id = :questionId")
	public List<QuestionAttach> getByQuestionid(@SQLParam("questionId") Integer questionId);
	@SQL("select * from te_questionattach where question_id = :questionId")
	public List<QuestionAttach> getByQuestionid(Connection conn, @SQLParam("questionId") Integer questionId);

	/**
	 * 根据试题ID集合批量获取对象
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select * from te_questionattach where question_id in( :ids)")
	public List<QuestionAttach> batchFindAttaches(Connection conn, @SQLParam("ids") List<Integer> ids);
	/**
	 * 根据试题ID集合批量获取对象
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select * from te_questionattach where question_id in( :ids)")
	public List<QuestionAttach> batchFindAttaches(@SQLParam("ids") List<Integer> ids);
	/**
	 * 删除多个试题的材料
	 * @param conn
	 * @param qustionIds
	 */
	@SQL("delete from te_questionattach where question_id in( :ids) and attach_type=0;")
	public void deleteAttatchsByQuestionIds(Connection conn, @SQLParam("ids") List<Integer> qustionIds);
	
	/**
	 * 根据id查询QuestionAttach
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public QuestionAttach getQuestionAttachById(int id);
}
