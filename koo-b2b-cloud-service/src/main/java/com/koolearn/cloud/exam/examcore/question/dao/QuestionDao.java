package com.koolearn.cloud.exam.examcore.question.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;

/**
 * 
 * @author yangzhenye
 * @date 2015-3-5
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface QuestionDao {
	
	/**
	 * 根据code获取最新版本
	 * @param code
	 * @return
	 */
	@SQL("select * from TE_QUESTION where code=:code and new_version=1")
	public Question getQuestionByCode(@SQLParam("code") String code);
	
	/**
	 * 根据id查询Question
	 * @param id
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public Question getQuestionById(int id);


    @SQL("SELECT count(1) from te_question where te_id=0 ")
    public Long findAllQuestionCount();
    @SQL("SELECT * from te_question where te_id=0  ")
    public List<Question> findAllQuestion(@PageBy ListPager listPager);

    @SQL("SELECT count(1) from user where status = 0 ")
    public Long findAllUserCount();
    @SQL("SELECT * from user where status = 0 ")
    public List<UserEntity> findAllUser(@PageBy ListPager listPager);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 插入题目
	 * TODO
	 * @param question 题目对象
	 * @return 题目ID
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Question question);
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, Question question);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param questions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(Connection conn, List<Question> questions);
	
	/**
	 * 批量插入
	 * @param conn
	 * @param questions
	 * @return
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int[] batchInsert(List<Question> questions);
	
	/**
	 * 修改题目
	 * @param question 题目对象
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Question question);
	
	@SQL(type=SQLType.WRITE_UPDATE)
	public void update(Connection conn, Question question);
	
	/**
	 * 批量更新
	 * TODO
	 * @param conn
	 * @param question
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void batchUpdate(Connection conn, List<Question> questions);
	
	/**
	 * 修改题目状态
	 * @param id id
	 * @param status 状态
	 */
	@SQL("update te_question set status=:status where id=:id")
	public void updateStatus(@SQLParam("id") Integer id, @SQLParam("status") Integer status);
	
	/**
	 * 修改题目状态
	 * @param conn
	 * @param id
	 * @param status
	 */
	@SQL("update te_question set status=:status where id=:id")
	public void updateStatus(Connection conn, @SQLParam("id") Integer id, @SQLParam("status") Integer status);
	/**
	 * 修改题目状态
	 * @param conn
	 * @param id
	 * @param status
	 */
	@SQL("update te_question set status=:status where te_id=:id")
	public void updateSubQuestionStatus(Connection conn, @SQLParam("id") Integer id, @SQLParam("status") Integer status);
	
	/**
	 * 删除试题 根据表关联进行级联删除,其它类型试题也调用此方法
	 * @param id
	 */
	@SQL("delete from te_question where id=:id")
	public void deleteQuestion(@SQLParam("id") int id);
	
	@SQL("select count(id) num from te_question where code=:code")
	public int codeCount(@SQLParam("code") String strCode);

	/**
	 * 计算在不是该id最新试题中是否存在当前code
	 * @param id
	 * @param strCode
	 * @return
	 */
	@SQL("select count(id) num from te_question where code=:code and id !=:id and new_version=1")
	public int codeCountWithId(@SQLParam("id") int id, @SQLParam("code") String strCode);
	@SQL("select * from te_question s where  s.id in (:ids)")
	public List<Question> batchFind(@SQLParam("ids") List<Integer> ids);

	/**
	 * 根据过滤器条件获取试题集合,分页操作
	 * @param filter
	 * @return
	 */
	@SQL(type = SQLType.READ)
	public List<Question> searchQuestionByFilter( QuestionFilter filter);

	/**
	 * 批量更新试题状态		审核
	 * @param list
	 */
	@SQL("update te_question set status=1 where (id in (:ids) or te_id in (:ids2)) and status=0")
	public void batchFindWithUnaudit(@SQLParam("ids") List<Integer> list, @SQLParam("ids2") List<Integer> list2);
	
	/**
	 * 批量更新试题状态		废除
	 * @param list
	 */
	@SQL("update te_question set status=2 where (id in (:ids) or te_id in (:ids2)) and status=1")
	public void batchFindWithNouse(@SQLParam("ids") List<Integer> list, @SQLParam("ids2") List<Integer> list2);

	/**
	 * 查找当前code编码下最新版本号
	 * @param conn
	 * @param code
	 * @return
	 */
	@SQL("select max(version) num from te_question where code=:code ")
	public int getMaxVersion(Connection conn, @SQLParam("code") String code);

	/**
	 * 根据过滤器条件获取试题集合数量
	 * @param filter
	 * @return
	 */
	@SQL(type = SQLType.READ)
	public int searchQuestionCountByFilter(QuestionFilter filter);
/**
	 * 修改题目最新版本号
	 * @param conn
	 * @param id
	 * @param status
	 */
	@SQL("update TE_QUESTION set new_version=:new_version where code=:code")
	public void updateNewVersion(Connection conn, @SQLParam("code") String code, @SQLParam("new_version") int new_version);

	/**
	 * 更新是否最新版本状态
	 * @param conn
	 * @param oldId
	 * @param i
	 */
	@SQL("update TE_QUESTION set new_version=:new_version where id=:id")
	public void updateNewVersion(Connection conn, @SQLParam("id") int oldId, @SQLParam("new_version") int i);

	/**
	 * 根据ID获取Question
	 * @param conn
	 * @param qustionId
	 * @return
	 */
	@SQL(type = SQLType.READ_BY_ID)
	public Question getQuestionById(Connection conn, int questionId);

	/**
	 * 根据teId获取Question集合
	 * @param conn
	 * @param teId
	 * @return
	 */
	@SQL("select s.* from TE_QUESTION s where  s.te_id = :teId order by sequence_id asc")
	public List<Question> getQuestionByTeId(Connection conn, @SQLParam("teId") int teId);
	/**
	 * 根据teId获取Question集合
	 * @param conn
	 * @param teId
	 * @return
	 */
	@SQL("select s.* from TE_QUESTION s where  s.te_id = :teId order by sequence_id asc")
	public List<Question> getQuestionByTeId(@SQLParam("teId") int teId);

	/**
	 * 更新试题顺序
	 * @param subItemOrderStr
	 */
	@SQL("update TE_QUESTION set sequence_id=:sequence_id where id=:id")
	public void updateOrder(Connection conn, @SQLParam("sequence_id") int order, @SQLParam("id") int questionId);

	/**
	 * 删除试题表
	 * @param conn
	 * @param qustionIds
	 */
	@SQL("delete from te_question where id in(:ids)")
	public void deleteQuestions(Connection conn, @SQLParam("ids") List<Integer> qustionIds);

	/**
	 * 根据te_id集合获取子题表
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select * from TE_QUESTION  where  te_id in (:teIds) ")
	public List<Question> getQuestionByTeIds(Connection conn, @SQLParam("teIds") List<Integer> ids);
/**
	 * 批量删除试题 
	 * @param ids
	 */
	@SQL("delete from te_question where id in (:ids)")
	public void deleteByIds(Connection conn, @SQLParam("ids") List<Integer> ids);
	
	/**
	 * 查询子选择题
	 * @param questionId
	 * @return
	 */
	@SQL("select s.* from TE_CHOICEQUESTION s join te_question t on s.question_id=t.id where t.te_id=:questionId")
	public List<ChoiceQuestion> getSubChoiceByQuestionid(@SQLParam("questionId") int questionId);
	
	/**
	 * 查询子填空题
	 * @param questionId
	 * @return
	 */
	@SQL("select e.* from te_essayquestion e join te_question t on e.questtion_id=t.id where t.te_id=:questionId")
	public List<EssayQuestion> getSubEssayByQuestionid(@SQLParam("questionId") int questionId);
	
	/**
	 * 查询出“已经审核通过”的和“用户自己新建”的试题列表的总数
	 * TODO
	 * @param filter
	 * @return
	 */
	@SQL(type = SQLType.READ)
	public int howmanyQuestionByFilter(@SQLParam("filter") QuestionFilter filter, String createBy, int paperId);
	
	/**
	 * 查询出“已经审核通过”的和“用户自己新建”的试题列表，分页查询，支持按条件过滤搜索
	 * TODO
	 * @param filter
	 * @return
	 */
	@SQL(type = SQLType.READ)
	public List<Question> searchQuestionPageByFilter(@PageBy @SQLParam("filter") QuestionFilter filter, String createBy, int paperId);
	
	/**
	 * 根据父试题编码获取子试题集合
	 * @param conn
	 * @param parentCode
	 * @return
	 */
	@SQL("select s.* from TE_QUESTION s where  s.te_id = (select p.id from TE_QUESTION p where p.code=:parentCode and p.new_version=1) order by s.sequence_id asc")
	public List<Question> getQuestionByTeCode(@SQLParam("parentCode") String parentCode);
	/**
	 * 根据父试题编码获取子试题集合
	 * @param conn
	 * @param parentCode
	 * @return
	 */
	@SQL("select s.* from TE_QUESTION s where  s.te_id = (select p.id from TE_QUESTION p where p.code=:parentCode and p.new_version=1) order by s.sequence_id asc")
	public List<Question> getQuestionByTeCode(Connection conn, @SQLParam("parentCode") String parentCode);
	
	/**
	 * 根据te_id集合获取子题ID列表
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select id from TE_QUESTION  where  te_id in (:teIds) ")
	public List<Integer> getQuestionIdByTeIds(@SQLParam("teIds") List<Integer> ids);
	
	/**
	 * 根据te_id获取子题ID列表
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select id from TE_QUESTION  where  te_id =:teId ")
	public List<Integer> getQuestionIdByTeIds(@SQLParam("teId") int id);
	
	/**
	 * 根据te_id集合获取子题表
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select * from TE_QUESTION  where  te_id in (:teIds) ")
	public List<Question> getQuestionByTeIds(@SQLParam("teIds") List<Integer> ids);
	
	/**
	 * 根据te_id获取子题列表
	 * @param conn
	 * @param ids
	 * @return
	 */
	@SQL("select * from TE_QUESTION  where  te_id =:teId ")
	public List<Question> getQuestionByTeIds(@SQLParam("teId") int id);
	
	
	/**
	 * 删除试题表
	 * @param conn
	 * @param qustionIds
	 */
	@SQL("delete from te_question where te_id =:teId")
	public void deleteQuestionByTeid(Connection conn, @SQLParam("teId") int teId);
	
	@SQL("delete from te_question where id =:id")
	public void deleteById(Connection conn, @SQLParam("id") int id);

	/**
	 * 更新 新子题的默认顺序
	 * @param conn
	 * @param teId 父id
	 * @param new_id 试题本身id
	 */
	@SQL("UPDATE TE_QUESTION SET sequence_id=(SELECT MAX(sequence_id)+1 FROM (SELECT a.sequence_id FROM TE_QUESTION  AS a  WHERE a.te_id=:teId) b ) WHERE id=:id")
	public void updateSequence4TeId(Connection conn, @SQLParam("teId") int teId, @SQLParam("id") int new_id);
	
	/**
	 * 获取所有可用的题目类型
	 * @return
	 */
	@SQL("SELECT * FROM te_questiontype;")
	List<Questiontype> getAllTypes();
	
	@SQL("UPDATE TE_QUESTION SET tag_full_path=:fullPath where id=:id")
	public void updateTagFullPath(@SQLParam("id") int id, @SQLParam("fullPath") String fullPath);
	@SQL("UPDATE TE_QUESTION SET tag_full_path=:fullPath where id=:id")
	public void updateTagFullPath(Connection conn, @SQLParam("id") int id, @SQLParam("fullPath") String fullPath);
	
	@SQL("UPDATE TE_QUESTION SET template_ids=:templateIds where id=:id")
	public void updateTemplateIds(Connection conn, @SQLParam("id") int id, @SQLParam("templateIds") String templateIds);
	
	/**
	 * 获取最近操作的10条记录，按状态（新建、有效）和时间排序
	 * @param createBy
	 * @return
	 */
	@SQL(type = SQLType.READ)
	public List<Question> getLastOperateRecord(QuestionFilter filter);
	
	@SQL("select * from te_question where code=:code order by id desc limit 1")
	public Question getLastSubQuestion(@SQLParam("code") String code);
	
	@SQL("select id,code from te_question where code=:code order by id")
	public List<Question> getAllQuestionByCode(@SQLParam("code") String code);
	
	/**
	 * 根据系统编号获得最近同步试题
	 */
	@SQL(type = SQLType.READ)
	public int getSyncQuestionBySysNoCount(@SQLParam("filter") QuestionFilter filter);
	/**
     * 根据系统编号获得最近同步试题
     */
    @SQL(type = SQLType.READ)
    public List<Question> getSyncQuestionBySysNo(@PageBy @SQLParam("filter") QuestionFilter filter);

	/**
	 * 更新时间
	 * @param list id集合
	 * @param date  时间
	 */
    @SQL("update te_question set last_update_date=:dd where id in (:ids) or te_id in (:ids)")
	public void updateLastUpdateDate(@SQLParam("ids") List<Integer> list, @SQLParam("dd") Date date);
    @SQL("update te_question set last_update_date=:dd where id =:id or te_id =:id")
    public void updateLastUpdateDate(Connection conn, @SQLParam("id") int id, @SQLParam("dd") Date date);
    
    /**
     * 移动图书馆（二期）根据用户ID和考试ID集合查询错题信息<br/>
     * @param userId 用户ID<br/>
     * @param examIds 考试ID集合<br/>
     * @return java.util.List<Question> 题目集合<br/>
     * @throws Exception<br/>
     * @author DuHongLin 2014-06-25 10:31<br/>
     */
    @SQL("SELECT tq.* FROM te_question tq,te_error_question teq,te_error_note ten WHERE tq.id = teq.question_Id AND tq.te_id = 0 AND teq.error_Note_Id = ten.id AND teq.`status` = 1 AND ten.user_Id = :userId AND ten.exam_Id IN (:examIds) AND ten.`status` = 0 GROUP BY tq.id ORDER BY tq.id ASC")
    public List<Question> selectErrorQuestions(@SQLParam("userId") int userId, @SQLParam("examIds") List<Integer> examIds) throws Exception;
    /**
     * 移动图书馆二期 根据问题ID查询类型为普通单选的题目
     * @param questionId 问题ID
     * @return java.util.List<ChoiceQuestion> 普通单选题目集合
     * @throws Exception
     * @author DuHongLin 2014-06-26 17:47
     */
    @SQL("SELECT * FROM te_choicequestion tcq WHERE tcq.question_id = :questionId")
    public List<ChoiceQuestion> selectChoiceByQuestionId(@SQLParam("questionId") int questionId) throws Exception;
    /**
	 * 返回随机试题编码code
	 * @param questionFilter 
	 * 随机题目数量 randomCount
	 * 随机题目标签 tag3
	 * @return
	 * @throws Exception
	 */
    @SQL(type = SQLType.READ)
	public List<String> queryRandomQuestion(@PageBy @SQLParam("filter") QuestionFilter filter);
	/**
	 * 返回随机试题总数量
	 * @param questionFilter 
	 * 随机题目数量 randomCount
	 * 随机题目标签 tag3
	 * @return
	 * @throws Exception
	 */
    @SQL(type = SQLType.READ)
	public int queryRandomQuestionCount(@SQLParam("filter") QuestionFilter filter);
}
