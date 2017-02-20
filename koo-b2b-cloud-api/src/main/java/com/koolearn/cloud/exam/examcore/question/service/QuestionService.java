package com.koolearn.cloud.exam.examcore.question.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.klb.tags.entity.Tags;

/**
 * 保存试题对象,包括材料,标签
 * 其他子类型试题可以调用
 * @author wangpeng
 * @date Oct 26, 2012
 * 技术教辅社区组@koolearn.com
 */
public interface QuestionService{
	/**
	 * 保存或更新QuestionDto,包括里面试题,材料,标签
	 * 这里只做逻辑的处理,conn没有关闭,需要再其他调用此service
	 * 中进行关闭操作,并且产生异常会直接抛出
	 * @param questionDto
	 * @param conn 连接
	 * @return QuestionID
	 */
	int saveOrUpate(Connection conn, QuestionDto questionDto) throws Exception;
	
	/**
	 * 删除QuestionDto,包括标签,材料,试题
	 * @param conn
	 * @param qustionIds 集合
	 */
	void deleteQuestionDto(Connection conn, List<Integer> qustionIds) throws Exception;
	/**
	 * 删除试题包括具体的试题以及标签,材料,试题
	 * @param conn
	 * @param ids 集合
	 * @param type 类型
	 */
	void deleteQuestion(Connection conn, List<Integer> ids, int type) throws Exception;
	
	QuestionDto getQuestionDtoByQuestionId(Connection conn, int qustionId) throws Exception;
	QuestionDto getQuestionDtoByQuestionId(int qustionId);

	/**
	 * 根据父ID获取QuestionDto集合 包括标签,材料,试题
	 * @param conn
	 * @param questionId
	 * @return
	 */
	List<QuestionDto> getQuestionDtoByTeId(Connection conn, int teId)throws Exception;

	/**
	 * 根据父ID获取Question集合
	 * @param conn
	 * @param questionId
	 * @return
	 */
	List<Question> getQuestionByTeId(Connection conn, int questionId);
	/**
	 * 根据父ID获取Question集合
	 * @param conn
	 * @param questionId
	 * @return
	 */
	List<Question> getQuestionByTeId(int questionId);

	/**
	 * 更新试题顺序字段
	 * @param subItemOrderStr eg. 1,7,3,2 顺序是 1 2 3 4
	 */
	void updateQuestionOrder(Connection conn, String subItemOrderStr);
	/**
	 * 复合题编辑备份
	 * @param conn
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	int backQuestion(Connection conn, QuestionDto questionDto) throws Exception;

    QuestionFilter findQuestionList(QuestionFilter pager) throws Exception;

    /** 题目搜索 EH*/
    QuestionFilter searchQuestion(QuestionFilter pager);
    /**题目搜索 EH 换题搜索 */
    QuestionFilter searchQuestionChange(QuestionFilter pager);
    /**智能组题*/
    QuestionFilter searchQuestionAuto(QuestionFilter pager,String questionBarkey);
    /**学生组题自测 题数按试卷总量抽取*/
    public Map<String,String> createStudentSelfTest(QuestionFilter pager  );
    /**学生组题自测 题数按题型数量抽取 */
    public String createStudentSelfTestQy(QuestionFilter pager  );
    List<IExamQuestionDto> convertQuestionToDto(List<Question> questionList,UserEntity loginUser) throws Exception;
    IExamQuestionDto findQuestionById(Integer questionId) throws Exception;
    /**
     * 创建索引，同步题库时创建、默认没有收藏和使用
     * @throws Exception 索引创建失败异常处理
     */
    public void createQuestionIndex(Question question) throws Exception;
    /**更新索引：设置收藏，使用次数等 */
    public void updateQuestionIndex(Question question);
    public void deleteQuestionIndex(Question question);
  /**题目收藏**/
    void collection(QuestionFilter pager, UserEntity user);
    /***修改使用次数*/
    void saveUsetimes(Integer questionId, UserEntity user);
   /**获取试卷的学段学科**/
   Map<String,  List<Tags>>  findSubjectRangeOfPaper(UserEntity user);
    /***根据试卷tagFullPath获取知识点树的学科学段*/
    public Map<String,  Tags> getSubjectByPaperFullPath(TestPaper testPaper);
    public List<Integer> getCollectionQuestionByUser(int userId,List<Integer> questionIds,int type);
    /**根据题目id搜索*/
    public IExamQuestionDto searchQuestionByQuestionId(QuestionFilter pager);

    /***试卷索引 begin**/
    /**
     * 创建索引，同步题库时创建、默认没有收藏和使用
     * @throws Exception 索引创建失败异常处理
     */
    public void createPaperIndex(TestPaper paper) throws Exception;
    /**更新索引：设置收藏，使用次数等 */
    public void updatePaperIndex(TestPaper paper);
    public void deletePaperIndex(TestPaper paper);
    public PaperPager searchPaper(PaperPager pager);
    /***试卷索引 end**/
    public Map<Integer,List<Tags>> getQuestionKnowledges(List<IExamQuestionDto> questionList) throws Exception;
    public String createStudentErrorFx(QuestionFilter pagers  );

    IExamQuestionDto getQuestionTagsByQid(Integer qid);
    public Long searchQuestionCount(QuestionFilter pager);
}
