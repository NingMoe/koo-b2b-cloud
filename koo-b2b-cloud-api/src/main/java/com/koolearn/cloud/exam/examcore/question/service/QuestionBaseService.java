package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;

/**
 * 题目类基础服务
 * 包括获取各种试题,标签,材料等与题目相关的信息
 * @author wangpeng
 * @date Oct 25, 2012
 * 技术教辅社区组@koolearn.com
 */
public interface QuestionBaseService{
	
    /**
     * 考试试题批量查找，根据试题code
     * @param questionCodes
     * @return
     * @throws Exception
     */
    public List<IExamQuestionDto> findQuestionByCodes(List<String> questionCodes, Integer schoolId)throws Exception;
    /**
     * 考试试题批量查找，根据试题id
     * @param questionIds
     * @return
     * @throws Exception
     */
    public List<IExamQuestionDto> findQuestionByIds(List<Integer> questionIds, Integer schoolId)throws Exception;
    /**
     * 考试试题批量查找，根据试题id
     * @param questionIds
     * @return
     * @throws Exception
     */
    public List<IExamQuestionDto> findQuestionByIds(List<Integer> questionIds)throws Exception;
	/**
	 * 非缓存中获取对象
	 * @param questionTypeId
	 * @param id
	 * @return
	 */
	IExamQuestionDto getExamQuestionNoCache(int questionTypeId, int id) throws Exception;
	/**
	 * 根据code获取最新版本ID
	 * @param code
	 * @return
	 */
	Question getQuestionByCode(String code);
	/**
	 * 获取考试试题实体
	 * @param questionId
	 * @return
	 */
	Question getQuestionById(int questionId);
	  /**
	  * 根据父ID获取子题目集合（有缓存）
	  * @param teId
	  * @return
	  * @throws Exception
	  */
	 public List<Question> getSubQuestions(int teId) throws Exception;
	/**
	 * 根据父ID获取子题目ID（有缓存）
	 * @param teId
	 * @return
	 * @throws Exception
	 */
	List<Integer> getQuestionIdsByTeIdRepository(int teId) throws Exception;
	
	public IExamQuestionDto findQuestionByCode(String questionCode) throws Exception;
	/**
	  * 考试试题数据保存
	  * @param conn
	  * @param examQuestionDto
	  * @throws Exception
	  */
	 public void saveExamQuestion(Connection conn, IExamQuestionDto examQuestionDto)throws Exception;

	/**
	 * 删除标签,材料附件以及question表
	 * @param conn
	 * @param qeustionId
	 */
	void delCommQuestion(Connection conn, int qeustionId);
	
	
	
	
	
	
	
	
	//以上方法为更新后方法
	
	
	
	
	
	

	/**
	 * 根据试题对象ID获取材料对象
	 * @param questionId
	 * @return
	 */
	List<QuestionAttach> getAttchesByQuestionId(int questionId);
	/**
	 * 根据试题ID获取选择题对象
	 * @param questionId
	 * @return
	 */
	ChoiceQuestion getChoiceQuestionByQuestionId(int questionId);
	/**
	 * 根据选择题ID获取选择题答案对象
	 * @param
	 * @return
	 */
	List<ChoiceAnswer> getChoiceAnswernByChoiceId(int choiceId);
	/**
	 * 获取考试题
	 * <前台接口>
	 * @param typeId 题目类型
	 * @param questionId 题目ID
	 * @return
	 */
	IExamQuestionDto getExamQuestion(int typeId, int questionId) throws Exception;
	/**
	 * 初始化对象到缓存中
	 * <前台接口>
	 * @param type 题目类型
	 * @param ids 题目ID集合
	 */
	Map<Integer, IExamQuestionDto> initExamQuestionDto(int type, List<Integer> ids)throws Exception;
	/**
	 * 根据过滤条件查询Question数据
	 * @param filter
	 * @return
	 */
	List<Question> searchQuestionByFilter(QuestionFilter filter);
	/**
	 * 将传入的试题id状态修改为已审核,弃用和已审核的不在操作
	 * @param ids
	 */
	void authedQuestion(String[] ids, String status);
	/**
	 * 删除试题
	 * @param
	 */
	void delQuestion(int id, QuestionDto dto);
	/**
	 * 根据过滤条件计算数量
	 * @param filter
	 * @return
	 */
	int searchTotalNumByFilter(QuestionFilter filter);
	/**
	 * 根据id获取材料对象
	 * @param QuestionAttachId
	 * @return
	 */
	QuestionAttach getQuestionAttach(int QuestionAttachId);
	
	

	/**
	 * 获取所有可用的题目类型
	 * @return
	 */
	List<Questiontype> getAllTypes();

	/**
	 * 在缓存中获取question对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Question getQuestionRepository(int id)throws Exception;
	
	public void updateTagFullPath(int id, String fullPath) throws Exception;
	
	/**
	 * 获取题干
	 * TODO
	 * @param typeId
	 * @param questionId
	 * @param maxLength 截取的字符数
	 * @return
	 * @throws Exception
	 */
	String getTopic(int typeId, int questionId, int maxLength) throws Exception;
	
	/**
	 * 获取最近操作的10条记录，按状态（新建、有效）和时间排序
	 * @param filter
	 * @return
	 */
	List<Question> getLastOperateRecord(QuestionFilter filter);
	
	
	/**
	 * 查找同步试题
	 * */
	List<Question> getSyncQuestionBySysNo(QuestionFilter filter) throws Exception;
	/**
	 * 查找同步试题数量
	 * */
	public int getSyncQuestionBySysNoCount(QuestionFilter filter) throws Exception ;
    

	Map<String, String> findQuestionTopicByCodes(List<String> qCodeList, Integer schoolId);
	Map<String, String> findQuestionTopicByIds(List<Integer> qIdList,Integer schoolId);
    public void  parseQuestionSearchContent(IExamQuestionDto questionDto);
    //通过id查询题目信息
	public com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto findQuestionById(int questionId) throws Exception;


}
