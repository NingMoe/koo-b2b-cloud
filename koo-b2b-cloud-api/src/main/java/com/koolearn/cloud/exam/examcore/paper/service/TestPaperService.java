package com.koolearn.cloud.exam.examcore.paper.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.koolearn.cloud.common.exceldowload.Knowledge;
import com.koolearn.cloud.exam.entity.DataSync;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.QuestionErrUser;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;

public interface TestPaperService {
	/**
	 * 通过标签id获取试卷
	 * @param tagId
	 * @return
	 */
	List<TestPaper> findTestPaperByTagId(ListPager listPager, int tagId);
	int findTestPaperCountByTagId(int tagId);
	/**
	 * 保存试卷dto
	 * 包括基本信息,需要设置试卷结构的试卷id和父级id
	 * 标签分类信息
	 * @param conn
	 * @param testPaperDto
	 */
	int savePaper4Exam(TestPaperDto testPaperDto);
	/**
	 * 删除试卷相关信息
	 * see TestPaperService#savePaper4Exam 
	 * @param savedId
	 */
	void deletPaperDtoByPaperId(int savedId);
	/**
	 * 保存从模板设置之后创建的试卷,如果paperId>0,就删除原来的数据
	 * @param dto
	 */
	int savePaper4Template(TestPaperDto dto);
	/**
	 * 获取试卷分数和题目数量
	 * @param paperId
	 * @return
	 */
	TestPaperDto findScoreQuestionCount(int paperId);
	List<TestPaperDto> findScoreQuestionCount(List<Integer> paperIds);
	TestPaper findItemById(int paperId);
	TestPaperDto findTestPaperDtoById(int paperId);
	/**
	 * 获取试卷的的分数和子题分数
	 * @param paperId
	 * @return <name,PaperStructure>
	 */
	Map<String, TestPaperStructureDto> findQuestionInfo(int paperId);
	
	/**
	 * 根据试卷id集合查询试卷下所有子题的分数
	 * @param pids
	 * @return Map key==》PaperId_QuestionCode , value==》PaperSubScore
	 * @throws Exception
	 * @author DuHongLin
	 */
	public Map<String, PaperSubScore> searchByPids(List<Integer> pids) throws Exception;
	/**
	 * 增加试卷浏览量
	 * @return
	 */
	int paperHot(Integer integer)throws Exception;
	/**
	 * 根据paperId分组获取子题分数对象
	 * @param paperId
	 * @return <paprenCode,List<PaperSubScore>>
	 */
	Map<String, List<PaperSubScore>> findSubScoreMap(int paperId);
	
	/**
	 * 判断是否是特殊试卷类型,目前只有雅思IETELS
	 * @see{te_paper_template}
	 * @param paperId
	 * @return 特殊类型为 True,否则为false
	 */
	boolean isSpecialTypePaper(int paperId);
	

	/**
	 * * @Description: TODO(考试过程——添加试卷缓存) 
	   *  @param paperId    
	   * @return void    
	   * @author: 葛海松
	   * @time:    2015年6月5日 下午6:14:23 
	   * @throws
	 */
	public TestPaperDto findTestPaperDtoByIdCache(Integer paperId);

    /**
     * 同步试卷
     * @param paperIds
     * @param
     * @return
     */
    public Map<Integer,String> savePaper4ExamPaper( List<String> paperIds) throws Exception;

    /**
     * 查询试卷（二期）
     */
   public PaperPager findPaperList(PaperPager pager);
   public DataSync syncExamPaper() throws Exception;
    /**编辑和创建试卷是封装数据**/
    TestPaper createOrEditTestPaper(PaperPager paper, UserEntity loginUser,String questionBarkey) throws Exception;
	/**
	 * 试卷创建过程，包装上viewType，用于模板显示定义
	 * @param paperFilter
	 * @param loginUser
	 * @param viewType
	 * @return
	 * @throws Exception 
	 */
    public TestPaper createOrEditTestPaper(PaperPager paper, UserEntity loginUser) throws Exception;
    public QuestionBar parseKnowledgeCount(QuestionBar qb) throws Exception;
    /**加入或移除我的试卷库*/
    void jionMyself(Integer paperId, UserEntity loginUser) throws Exception;
    /**删除试卷索引*/
    public boolean deletPaperAndIndexByPaperId(int paperId);
    public QuestionBarHtml getQuestionBarHtml(Integer paperId) throws Exception;
    public TestPaper downloadPaperPDF(Integer paperId) throws Exception;

    public void generalPaperFrame(TestPaper testPaper) throws Exception ;
	void generalQuestion(Document document,QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto ) throws IOException, DocumentException;
    void generalQuestion(Document document,QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto,List<String> explanafterList,Map<Integer, QuestionErrUser> mapAll ) throws IOException, DocumentException;
	/**
	 * 下载作业讲评
	 * @param mapAll 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	void downloadTaskComment(List<IExamQuestionDto> questionDtoList,
			List<QuestionErrUser> avg, List<QuestionErrUser> errUser,
			List<QuestionErrUser> noAnswerUser, TaskDto task,
			Map<Integer, QuestionErrUser> mapAll, String downloadName) throws IOException, DocumentException;

    public TaskPager findSelfTest(TaskPager task);

    /**
     * cha xun mo yici zuoye de cuoti
     * @param id
     * @param examId
     * @return
     */
    List<IExamQuestionDto> findErrorQuestionByExamId(Integer id, Integer examId);

    /**
     * gen ju jindudian chazhao cuoti
     * @param questionFilter
     * @return
     */
    QuestionFilter findErrorQuestionByJdd(QuestionFilter questionFilter);

    void deleteSyncQuestion();
    /**
     * 通过试卷id查询试卷结构，此方法为了取得题目分数
     * @param paperId
     * @return
     */
    Map<String, TestPaperStructure> findStructureQuestionByPaperId(int paperId);
    public Map<String,Double> findSubQuestionPoints(Integer paperId);//获取小题结构上的分值
    /**
     * 处理试卷中子题分值
     * 如果detailsMap不为空则填入用户答案
     * @param questionDtoList
     * @param paperId
     * @param detailsMap
     * @return
     */
    public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  parseSubQuestionPoints(List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList,Integer paperId, Map<String, TpExamResultDetail> detailsMap);
    public com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto parseSubQuestionPoints(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto);
    /**
     * 作业讲评页面使用
     * @param teIdDetail
     * @param resultDetail
     * @param examId
     * @param viewType 
     * @return
     * @throws Exception
     */
    List<IExamQuestionDto> createPartTestPaper(List<TpExamResultDetail> teIdDetail,List<QuestionErrUser> resultDetail, Integer examId, Map<Integer, QuestionErrUser> map,int viewType) throws Exception;

    public void setsubPoint(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto , Map<String,Double> subScoreMap, Map<String, TpExamResultDetail> detailsMap);

	/**
	 * 个人作业详情
	 * @param teIdDetail
	 * @param resultDetail
	 * @param examId
	 * @param detailsMap
	 * @return
	 * @throws Exception 
	 */
	List<IExamQuestionDto> createPartTestPaper(	List<TpExamResultDetail> teIdDetail,List<QuestionErrUser> resultDetail, Integer examId,int viewType,Map<String, TpExamResultDetail> detailsMap) throws Exception;
	/**
	 * 通过结果回朔卷子结构
	 * @param paper
	 * @param loginUser
	 * @param resultId
	 * @param viewType
	 * @return
	 * @throws Exception
	 */
	TestPaper createOrEditTestPaperResultDetail(PaperPager paper,UserEntity loginUser, int resultId, int viewType) throws Exception;

    void rebuildUpdate(String xiaoxiao);

    /**
     * 更加知识点统计资源数量
     * @param paramMap
     * @return
     */
    List<Knowledge> tongjiResourceNumByKnowledge(Map<String, Boolean> paramMap);
}
