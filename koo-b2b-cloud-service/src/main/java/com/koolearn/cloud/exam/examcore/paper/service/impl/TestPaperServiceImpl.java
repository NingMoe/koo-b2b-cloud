package com.koolearn.cloud.exam.examcore.paper.service.impl;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import com.koolearn.cloud.common.exceldowload.Knowledge;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.exam.entity.DataSync;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.exam.dao.ExamResultDetailDao;
import com.koolearn.cloud.exam.examcore.exam.entity.TpErrorNote;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.paper.dao.*;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperDetailDto;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.*;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.*;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionConvertService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.queue.ProducerPaperServiceImpl;
import com.koolearn.cloud.resource.dto.SearchResourceBean;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.task.dao.ExamDao;
import com.koolearn.cloud.task.dao.TaskDao;
import com.koolearn.cloud.task.dto.QuestionErrUser;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.cloud.util.*;
import com.koolearn.cloud.util.numberToChinese.NumberText;
import com.koolearn.cloud.util.pdf.HtmlCssUtil;
import com.koolearn.cloud.util.pdf.ItextPDFUtil;
import com.koolearn.cloud.util.pdf.ItextpdfUtils;
import com.koolearn.exam.base.dto.IExamQuestionDto;
import com.koolearn.exam.paperInterface.IPaperInterface;
import com.koolearn.exam.paperInterface.PaperVO;
import com.koolearn.exam.structure.dto.PaperDto;
import com.koolearn.exam.structure.dto.PaperStructureDto;
import com.koolearn.exam.structure.entity.TeTestPaperInfo;
import com.koolearn.exam.structure.entity.TeTestPaperStructure;
import com.koolearn.exam.syncQuestion.service.QuestionSyncService;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.framework.common.utils.spring.SpringContextUtils;
import com.koolearn.klb.tags.entity.Tags;
import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.koolearn.framework.common.page.ListPager;
import org.springframework.beans.factory.annotation.Autowired;

public class TestPaperServiceImpl implements TestPaperService {
	private static final Logger log=Logger.getLogger(TestPaperServiceImpl.class);
	private TestPaperDao testPaperDao;
    private ExamDao examDao;
	private TestPaperStructureDao testPaperStructureDao;
	private PaperDetailDao paperDetailDao;
	private PaperSubScoreDao paperSubScoreDao;
    private QuestionConvertService questionConvertService;
    private IPaperInterface paperInterface;
    private QuestionService questionService;
    @Autowired
    private ResourceInfoService resourceInfoService;
    private ProducerPaperServiceImpl producerPaperService;
    private TaskDao taskDao;
    private ExamResultDetailDao examResultDetailDao;
    @Autowired
    protected QuestionBaseService questionBaseService;

    public ResourceInfoService getResourceInfoService() {
        return resourceInfoService;
    }

    public void setResourceInfoService(ResourceInfoService resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public ProducerPaperServiceImpl getProducerPaperService() {
        return producerPaperService;
    }

    public void setProducerPaperService(ProducerPaperServiceImpl producerPaperService) {
        this.producerPaperService = producerPaperService;
    }

    public ExamResultDetailDao getExamResultDetailDao() {
		return examResultDetailDao;
	}

	public void setExamResultDetailDao(ExamResultDetailDao examResultDetailDao) {
		this.examResultDetailDao = examResultDetailDao;
	}

	public ExamDao getExamDao() {
        return examDao;
    }

    public void setExamDao(ExamDao examDao) {
        this.examDao = examDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public IPaperInterface getPaperInterface() {
        return paperInterface;
    }

    public void setPaperInterface(IPaperInterface paperInterface) {
        this.paperInterface = paperInterface;
    }

    public TestPaperDao getTestPaperDao() {
		return testPaperDao;
	}

	public void setTestPaperDao(TestPaperDao testPaperDao) {
		this.testPaperDao = testPaperDao;
	}

	public PaperSubScoreDao getPaperSubScoreDao() {
		return paperSubScoreDao;
	}

	public void setPaperSubScoreDao(PaperSubScoreDao paperSubScoreDao) {
		this.paperSubScoreDao = paperSubScoreDao;
	}

    public QuestionConvertService getQuestionConvertService() {
        return questionConvertService;
    }

    public void setQuestionConvertService(QuestionConvertService questionConvertService) {
        this.questionConvertService = questionConvertService;
    }
    
    private static  final String QUESTION_TYPE_ILLEGAL="";//"(!!)";

    
    
    
    /**
     * 获取试卷
     */
	@Override
	public TestPaperDto findTestPaperDtoByIdCache(Integer paperId) {
		TestPaperDto testPaperDto =CacheTools.getCache(ConstantTe.PROCESS_EXAMPAPER_PAPERID+paperId, TestPaperDto.class);
		if(testPaperDto==null){
			testPaperDto =  findTestPaperDtoById(paperId);
			CacheTools.addCache(ConstantTe.PROCESS_EXAMPAPER_PAPERID+paperId, ConstantTe.CACHE_TIME, testPaperDto);
		}
		return testPaperDto;
	}
    

	/**
	 * 查询试卷
	 */
	@Override
	public TestPaperDto findTestPaperDtoById(int paperId) {
		TestPaperDto dto=new TestPaperDto();
		dto.setTree(false);
		dto.setPaper(testPaperDao.findItemById(paperId));
		List<TestPaperStructure> structures=testPaperStructureDao.findItemsByPaperId(paperId);
		TestPaperStructure structure=null;
		TestPaperStructureDto structureDto=null;
		for(int i=0;i<structures.size();i++){
			structure=structures.get(i);
			structureDto=new TestPaperStructureDto();
			structureDto.setTestPaperStructure(structure);
			structureDto.setDetailDto(new PaperDetailDto());
			if(structure.getStructureType()==TestPaperStructure.structure_type_question){
				List<PaperSubScore> subscores=paperSubScoreDao.findItemByPaperId(paperId,structure.getName());
				if(CollectionUtils.isNotEmpty(subscores)){
					structureDto.setSubScores(subscores);
				}
			}
			structureDto.getDetailDto().setPaperDetail(paperDetailDao.findItemByStructureId(structure.getId()));
			dto.getStructures().add(structureDto);
		}
		return dto;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
    
    
    
    
    
    
    
    //以上方法为修改后方法
    
    
    
    
    
    
    
    
    

    @Override
	public List<TestPaper> findTestPaperByTagId(ListPager listPager,int tagId) {
		List<TestPaper> list=testPaperDao.findTestPaperByTagId(listPager,tagId);
		return list;
	}

	@Override
	public int findTestPaperCountByTagId(int tagId) {
		return testPaperDao.findTestPaperCountByTagId(tagId);
	}
	@Override
	public void deletPaperDtoByPaperId(int paperId) {
		testPaperStructureDao.deleteByPaperId(paperId);
		paperSubScoreDao.deleteByPaperId(paperId);
		testPaperDao.deleteByPaperId(paperId);
	}
    @Override
    public boolean deletPaperAndIndexByPaperId(int paperId) {
        try{
            TestPaper testPaper=testPaperDao.findTestPaper(paperId);
            if(testPaper!=null){
                deletPaperDtoByPaperId(  paperId);
            }else{
                testPaper=new TestPaper();
                testPaper.setId(paperId);
            }
            questionService.deletePaperIndex(testPaper);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
	@Override
	public int savePaper4Template(TestPaperDto testPaperDto) {
		if(testPaperDto.getPaper().getId()>0){
			fillCode(testPaperDto);
		}else{
			autoCreatCode(testPaperDto);
		}
		return savePaper4Exam(testPaperDto);
	}
	private void fillCode(TestPaperDto testPaperDto) {
		TestPaper paper=testPaperDao.findItemById(testPaperDto.getPaper().getId());
		if(StringUtils.isEmpty(testPaperDto.getPaper().getPaperCode())){
			String code=paper.getPaperCode();
			testPaperDto.getTestPaperExtDto().getTestPaperExt().setPaperCode(code);
		}
		testPaperDto.getPaper().setCreateTime(paper.getCreateTime());
	}

	private void autoCreatCode(TestPaperDto testPaperDto){
		String paperCode=testPaperDto.getPaper().getPaperCode();
		if(StringUtils.isEmpty(paperCode)){
			testPaperDto.getPaper().setPaperCode("智能测试组卷"+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
		}
	}
	@Override
	public int  savePaper4Exam(TestPaperDto testPaperDto) {
		
		Connection conn=null;
		int paperId=0;
		try{
        List<TestPaper> oldList=testPaperDao.findPaperByPaperCode(testPaperDto.getPaper().getPaperCode());
            if(oldList!=null&&oldList.size()>0){
                paperId=oldList.get(0).getId();
                testPaperDto.getPaper().setId(paperId);
            }
		conn= ConnUtil.getTransactionConnection();
		//1.paper
		if(paperId>0){
			testPaperDao.update(conn,testPaperDto.getPaper());
            //更新索引(同时转换试卷的学科学段)
            questionService.deletePaperIndex(testPaperDto.getPaper());
		}else{
		   paperId=testPaperDao.save(conn,testPaperDto.getPaper());
            testPaperDto.getPaper().setId(paperId);
		}
		//4.结构
		paperDetailDao.deleteByPaperId(conn,paperId);
		testPaperStructureDao.deleteByPaperId(conn,paperId);
		paperSubScoreDao.deleteByPaperId(conn,paperId);
		List<TestPaperStructureDto> list=testPaperDto.getStructures();
		if(list!=null&&list.size()>0){
			int pid;
			TestPaperStructureDto testPaperStructureDto=null;
			PaperDetail paperDetail;
			TestPaperStructure testPaperStructure;
			
			for(int i=0,size=list.size();i<size;i++){
				testPaperStructureDto=list.get(i);
				pid=saveStructure(conn, paperId, testPaperStructureDto,0);//题型结构 的父id为0
				
			}
		}
		conn.commit();
        Thread.sleep(500);
        //更新或新建索引
        questionService.createPaperIndex(testPaperDto.getPaper());
        parseQuntionNum(paperId);
		}catch(Exception e){
               DataSync.updateDataSuncFromCache(testPaperDto.getPaper().getPaperName() + "<_>" + testPaperDto.getPaper().getPaperCode() + "<_>保存失败！", "p", e);
               questionService.deletePaperIndex(testPaperDto.getPaper());
            log.info("题库试卷同步>>>>>>试卷保存失败[试卷id：" + paperId + "]");
			e.printStackTrace();
			ConnUtil.rollbackConnection(conn);
			paperId=-1;
		}finally{
			ConnUtil.closeConnection(conn);
			return paperId;
		}
	}

    private void parseQuntionNum(Integer paperId) {
        try{
            Integer questionCount=0;//'以大题计算题数',
            Integer questionMinCount=0;//'以小题题计算题数',
            //试卷结构子试题列表
            List<PaperSubScore> subScoreList=testPaperStructureDao.findSubQuestionPoints(paperId);
            //试卷结构试题列表
            List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperId);
            if(questionCodeList!=null&&questionCodeList.size()>0){
                questionCount=questionCodeList.size();
                for (int i=0 ;i<questionCodeList.size();i++){
                    questionMinCount++;
                    if(subScoreList!=null&&subScoreList.size()>0){
                        int subNum=0;
                        for (PaperSubScore pss:subScoreList){
                            if(pss.getParentCode().equals(questionCodeList.get(i).getName())){
                                subNum++;
                            }
                        }
                        if(subNum>0){
                            questionMinCount=questionMinCount+subNum-1;
                        }
                    }
                }
            }
            testPaperDao.updateQuestionNum(paperId,questionCount,questionMinCount);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveRecordSet(Connection conn, List<TestPaperStructureDto> list,int creator) {
	}

		
	private  void findCodes(TestPaperStructureDto dto,List<String> codes) {
		TestPaperStructure structure=dto.getTestPaperStructure();
		if(structure!=null){
			if(structure.getStructureType()==TestPaperStructure.structure_type_question){
				codes.add(structure.getName().trim());
			}else{
				List<TestPaperStructureDto> list=dto.getChildren();
				if(CollectionUtils.isNotEmpty(list)){
					for(TestPaperStructureDto dto2:list){
						findCodes(dto2, codes);
					}
				}
			}
		}
	}

	private void saveSubScore(Connection conn, int paperId, String name,List<PaperSubScore> subScores) {
		PaperSubScore subScore=null;
		for(int i=0,size=subScores.size();i<size;i++){
			subScore=subScores.get(i);
			subScore.setPaperId(paperId);
			subScore.setParentCode(name);
			paperSubScoreDao.save(conn,subScore);
		}
	}

	private int saveStructure(Connection conn, int paperId,
			TestPaperStructureDto testPaperStructureDto,int pid) {
		//1.结构
		TestPaperStructure testPaperStructure;
		testPaperStructure=testPaperStructureDto.getTestPaperStructure();
		testPaperStructure.setPaperId(paperId);
		testPaperStructure.setParent(pid);
		pid=testPaperStructureDao.save(conn, testPaperStructure);
		
		//2.paperDetail
		PaperDetail paperDetail;
		paperDetail=testPaperStructureDto.getDetailDto().getPaperDetail();
		paperDetail.setPaperStructureId(pid);
		paperDetailDao.save(conn,paperDetail);
		List<TestPaperStructureDto> children=testPaperStructureDto.getChildren();
		if(children!=null&&children.size()>0){
			int pid2;
			for(int i=0,size=children.size();i<size;i++){
				pid2=saveStructure(conn,paperId,children.get(i),pid);
			}
		}
		if(testPaperStructure.getStructureType()==TestPaperStructure.structure_type_question){
		//5.子题分数
			saveSubScore(conn,paperId,testPaperStructureDto.getTestPaperStructure().getName(),testPaperStructureDto.getSubScores());
		}
		return pid;
	}

	public TestPaperStructureDao getTestPaperStructureDao() {
		return testPaperStructureDao;
	}

	public void setTestPaperStructureDao(TestPaperStructureDao testPaperStructureDao) {
		this.testPaperStructureDao = testPaperStructureDao;
	}

	public PaperDetailDao getPaperDetailDao() {
		return paperDetailDao;
	}

	public void setPaperDetailDao(PaperDetailDao paperDetailDao) {
		this.paperDetailDao = paperDetailDao;
	}


	@Override
	public TestPaperDto findScoreQuestionCount(int paperId) {
		//试卷参与考试后不会修改
		TestPaper paper= CacheTools.getCache(ConstantTe.STUDENT_EXAM_PAPER_KEY + paperId, TestPaper.class);
		if(paper==null){
			paper=testPaperDao.findItemById(paperId);
            CacheTools.addCache(ConstantTe.STUDENT_EXAM_PAPER_KEY+paperId, 60*60*24, paper);
		}
		Integer count=CacheTools.getCache(ConstantTe.STUDENT_EXAM_PAPER_KEY+paperId+"count", Integer.class);
		if(count==null){
			count=testPaperStructureDao.findItemsCount4Type(paperId, TestPaperStructure.structure_type_question);
			if(count==null) count=0;
            CacheTools.addCache(ConstantTe.STUDENT_EXAM_PAPER_KEY+paperId+"count", 60*60*24, count);
		}
//		TestPaper paper=testPaperDao.findItemById(paperId);
//		Integer count=testPaperStructureDao.findItemsCount4Type(paperId, TestPaperStructure.structure_type_question);
		TestPaperDto dto=new TestPaperDto();
		dto.setPaper(paper);
		dto.setQuestionCount(count.intValue());
		return dto;
	}

	@Override
	public TestPaper findItemById(int paperId) {
		return testPaperDao.findItemById(paperId);
	}

	@Override
	public List<TestPaperDto> findScoreQuestionCount(List<Integer> paperIds) {
		
		log.debug("findScoreQuestionCount  进入" +paperIds );
		long t1=System.currentTimeMillis();
		
		List<TestPaperDto> result=new ArrayList<TestPaperDto>();
		for(int paperId:paperIds){
			result.add(findScoreQuestionCount(paperId));
		}
		long t2=System.currentTimeMillis();
		log.debug("findScoreQuestionCount  结束[" +(t2-t1)+"]data= "+JSONArray.fromObject(result));
		return result;
		
	}


	@Override
	public Map<String, TestPaperStructureDto> findQuestionInfo(int paperId) {
		Map<String, TestPaperStructureDto> result=new HashMap<String, TestPaperStructureDto>();
		List<TestPaperStructure> list=testPaperStructureDao.findItems4Type(paperId, TestPaperStructure.structure_type_question);
		if(CollectionUtils.isNotEmpty(list)){
			TestPaperStructureDto dto;
			for(TestPaperStructure structure:list){
				dto=new TestPaperStructureDto();
				dto.setTestPaperStructure(structure);
				List<PaperSubScore>  list2=paperSubScoreDao.findItemByPaperId(paperId, structure.getName());
				if(CollectionUtils.isNotEmpty(list2)){
					dto.setSubScores(list2);
				}
				result.put(structure.getName(), dto);
			}
		}
		return result;
	}

	@Override
	public Map<String, PaperSubScore> searchByPids(List<Integer> pids) throws Exception
	{
		Map<String, PaperSubScore> result = new HashMap<String, PaperSubScore>(0);
		try
		{
			List<PaperSubScore> subScores = this.paperSubScoreDao.selectByPaperIds(pids);
			if (null != subScores && subScores.size() > 0)
			{
				for (PaperSubScore score : subScores)
				{
					result.put(score.getPaperId() + "_" + score.getCode(), score);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	/**
	 * 增加试卷浏览量
	 * @return
	 */
	public int paperHot(Integer paperId) throws Exception {
		try {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Map<String, List<PaperSubScore>> findSubScoreMap(int paperId) {
		Map<String, List<PaperSubScore>> result=new HashMap<String, List<PaperSubScore>>();
		List<PaperSubScore> list=paperSubScoreDao.findItemByPaperId(paperId);
		if(CollectionUtils.isEmpty(list)){
			return result;
		}
		int size=list.size();
		PaperSubScore item;
		String code;
		for(int i=0;i<size;i++){
			item=list.get(i);
			code=item.getParentCode();
			if(!result.containsKey(code)){
				result.put(code, new ArrayList<PaperSubScore>());
			}
			result.get(code).add(item);
		}
		return result;
	}

	@Override
	public boolean isSpecialTypePaper(int paperId) {
//		int size=paperTemplateDao.findOneSpecialType(paperId);
//		if(size>0){
//			return true;
//		}
		return false;
	}



    /***
     * 同步试卷
     * @param paperIds
     * @return
     */
    @Override
    public Map<Integer,String> savePaper4ExamPaper( List<String> paperIds) throws Exception {
        final Map<Integer,String> erroySyncQuestionMsg=new HashMap<Integer, String>();
        TestPaperDto testPaperDto=null;
        PaperDto paperDto=null;
        int savedId=0;int paperId=0;
        /**同步试题map集合key：paperId*/
        Map<Integer,List<IExamQuestionDto>> syncQuestionMap=null;
        /**同步试卷map集合key：paperId*/
        final Map<Integer,PaperDto> syncPaperMap=new HashMap<Integer, PaperDto>();
        try {
            //1.检查并同步试卷&试题
            final QuestionSyncService questionSyncService = (QuestionSyncService) SpringContextUtils.getBean("questionSyncServiceClient");
            syncQuestionMap=checkQuestion(paperIds,erroySyncQuestionMsg,questionSyncService,syncPaperMap);
            //2.保存试卷和题
              Map<Integer,List<IExamQuestionDto>> syncQuestionMap2=syncQuestionMap;
//            savePaperAndQuestion( paperIds,syncPaperMap,syncQuestionMap2);//开线程处理
            if(paperIds!=null&&paperIds.size()>0){
                log.info("题库试卷同步>>>>>>同步保存本页["+paperIds.size()+"]个试卷【"+ StringUtils.join(paperIds, "','")+"】");
            }
            savePaperAndQuestionNoThread( paperIds,syncPaperMap,syncQuestionMap2);//关闭线程处理
        } catch (Exception e1) {
            throw new Exception(e1.getMessage());
        }
        return erroySyncQuestionMsg;
    }

    @Override
    public PaperPager findPaperList(PaperPager pager) {
        long count=testPaperDao.findPaperListTotalRows(pager);
        pager.setTotalRows(count);
        pager.setResultList( testPaperDao.findPaperList( pager));
        return pager;
    }

    /**
     * 开线程：把正常同步过来的试卷和题保存到本系统
     * @param syncPaperMap
     * @param syncQuestionMap2
     */
    private void savePaperAndQuestion(final List<String> paperIds, final Map<Integer, PaperDto> syncPaperMap, final Map<Integer, List<IExamQuestionDto>> syncQuestionMap2) {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        for(final String paperIdStr:paperIds){
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
            int savedId2=0;
            try{
                int  paperId2 = new Integer(paperIdStr);
                List<IExamQuestionDto> questions=syncQuestionMap2.get(paperId2);
                if(questions==null || questions.size()<1){
                    //试题为空，则不保存试卷
                    log.info("题库试卷同步>>>>>>循环保存本页试卷[试卷id：" + paperIdStr + "]-->试卷无题,同步失败");
                    DataSync.updateDataSuncFromCache(paperId2+"_考试系统改卷无题","p",null);
				    return;
                }
                //获取考试试卷信息
                PaperDto paperDto2=syncPaperMap.get(paperId2);
                TestPaperDto testPaperDto2=convertTestPaperDto(paperDto2);//试卷对象转换
                //保存为本地试卷
                savedId2=savePaper4Exam(testPaperDto2);
                if(savedId2==0){
                    throw new RuntimeException("savedId==0");
                }
                //保存试题为本地试题
                for(IExamQuestionDto examQuestionDto:questions){
                    questionConvertService.saveQuestion(examQuestionDto);
                }
                log.info("题库试卷同步>>>>>>循环保存本页试卷[试卷id："+paperIdStr+"]-->题目保存成功");
            }catch(Exception e){
                log.error("error-paperIdStr:"+paperIdStr,e);
                e.printStackTrace();
                if(savedId2!=0){
                    deletPaperDtoByPaperId(savedId2);
                }
                refreshCache(paperIdStr);
                throw new RuntimeException(e.getMessage());
            }

				}
			});
        }
    }
    /**
     * 不开线程：把正常同步过来的试卷和题保存到本系统
     * @param syncPaperMap
     * @param syncQuestionMap2
     */
    private void savePaperAndQuestionNoThread( List<String> paperIds,  Map<Integer, PaperDto> syncPaperMap,  Map<Integer, List<IExamQuestionDto>> syncQuestionMap2) {
        for( String paperIdStr:paperIds){
            int savedId2=0;
            try{
                int  paperId2 = new Integer(paperIdStr);
                List<IExamQuestionDto> questions=syncQuestionMap2.get(paperId2);
                if(questions==null || questions.size()<1){
                    //试题为空，则不保存试卷
                    log.info("*****************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>****************");
                    log.info("题库试卷同步>>>>>>循环保存本页试卷-->试卷无题,同步失败[试卷id："+paperIdStr+"]");
                    log.info("*****************<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<****************");
                    DataSync.updateDataSuncFromCache(paperId2+"_考试系统试卷没有题目！","p",null);
                    DataSync.updateErrorNumCache("p",false);//记录试卷失败个数
                    continue;
                }
                //获取考试试卷信息
                PaperDto paperDto2=syncPaperMap.get(paperId2);
                TestPaperDto testPaperDto2=convertTestPaperDto(paperDto2);//试卷对象转换
                //保存为本地试卷
                savedId2=savePaper4Exam(testPaperDto2);
                if(savedId2==0){
                    throw new RuntimeException("savedId==0");
                }
                //保存试题为本地试题
                for(IExamQuestionDto examQuestionDto:questions){
                    questionConvertService.saveQuestion(examQuestionDto);
                }
                Thread.sleep(50);
                log.info("题库试卷同步>>>>>>循环保存本页试卷[试卷id："+paperIdStr+"]-->题目保存成功");
                DataSync.updateErrorNumCache("p",true);//记录试卷成功个数
            }catch(Exception e){
                log.error("error-paperIdStr:"+paperIdStr,e);
                e.printStackTrace();
                if(savedId2!=0){
                    deletPaperDtoByPaperId(savedId2);
                }
                refreshCache(paperIdStr);
            }
        }
    }

    /**
     * 检查题目是否有标签和适应于本系统的题型,如果没有添加到erroySyncQuestionMsg中
     * @param paperIds
     * @param questionSyncService
     * @return 试卷对应的题目集合的map
     * @throws Exception
     */
    private Map<Integer, List<IExamQuestionDto>> checkQuestion(final List<String> paperIds, Map<Integer, String> erroySyncQuestionMsg,final QuestionSyncService questionSyncService,Map<Integer,PaperDto> syncPaperMap) throws Exception {
        final List<Integer> allSize= Collections.synchronizedList(new ArrayList<Integer>(paperIds.size() * 2));
        final Map<Integer, List<IExamQuestionDto>> temp=new ConcurrentHashMap<Integer, List<IExamQuestionDto>>();
        final Map<Integer, PaperDto> paperDtos=new ConcurrentHashMap<Integer, PaperDto>();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        for(int i=0,size=paperIds.size();i<size;i++){
            //循环获取试卷和题
            final int  index=i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //该线程更加试卷id加载题目，  key是 试卷id(此接口需要替换鸿麟的新接口：检查题目的tag_full_path是否完整)
                        Map<Integer, List<IExamQuestionDto>> temp2=questionSyncService.getExamQuestionDto4Map(Integer.parseInt(paperIds.get(index)));
                        temp.putAll(temp2);
                    }  catch (Exception e) {
                        log.error("根据试卷加载题报错："+paperIds.get(index));
                        e.printStackTrace();
                    }finally{
                        allSize.add(Integer.parseInt(paperIds.get(index)));
                    }
                }
            });
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //该线程根据试卷id 加载试卷信息
                        PaperDto paperDto=questionSyncService.getPaperById(Integer.parseInt(paperIds.get(index)));
                        paperDtos.put(paperDto.getPaperInfo().getId(), paperDto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally{
                        allSize.add(1);
                    }
                }
            });

        }
        while(allSize.size()<paperIds.size()*2){
            //线程加载试卷题目完成前，休眠
            Thread.sleep(1000);
        }
        //试卷  题目加载完毕
        allSize.clear();
        fixedThreadPool.shutdownNow();fixedThreadPool=null;

        Map<Integer, List<IExamQuestionDto>> syncQuestionMap=new HashMap<Integer, List<IExamQuestionDto>>();
        for(Integer paperId:temp.keySet()){
            List<IExamQuestionDto> qlist=temp.get(paperId);
            if(qlist==null ||qlist.size()<1){
                log.error("题库试卷同步>>>>>>试卷无题-->试卷id"+paperId+"无题");
                continue;
            }
            boolean isAdd=true;
            List<IExamQuestionDto>  questionListOfPaper=new ArrayList<IExamQuestionDto>();
            for(IExamQuestionDto dto:temp.get(paperId)){
                try {
                    //题目非法性验证（判断支持题目类型及其他验证）
                    if(questionConvertService.saveCheckQuestion(dto)){
                        questionListOfPaper.add(dto);
                    }else {
                        erroySyncQuestionMsg.put(paperId, "题目类型不支持，questionId"+dto.getQuestionDto().getQuestion().getId()+"题目类型："+dto.getQuestionDto().getQuestion().getQuestionTypeId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isAdd=false;
                    break;
                }
            }
            temp.put(paperId,questionListOfPaper);
//            log.error("同步题目check标识："+isAdd+"_试卷id"+paperId+"_题目集合size"+temp.get(paperId).size());
            if(isAdd){
                syncQuestionMap.put(paperId, temp.get(paperId));//根据试卷id获取题目
                syncPaperMap.put(paperId, paperDtos.get(paperId)); //根据试卷id获取 试卷
            }else{
                log.info("排除试卷(有非法题目类型)试卷id"+paperId);
                refreshCache(paperId + "");
            }
        }

        return syncQuestionMap;
    }
    /**0.试卷信息DTO转换*/
    private TestPaperDto convertTestPaperDto(PaperDto paperDto){
        TestPaperDto testPaperDto=new TestPaperDto();
        TestPaper testPaper=convertTestPaper(paperDto.getPaperInfo());
        List<TestPaperStructureDto> structures=null;
        if(paperDto.getStructures()!=null
                && paperDto.getStructures().size()>0
                && paperDto.getStructures().get(0).getChild()!=null){
            structures = convertPaperStructure(paperDto.getStructures().get(0).getChild());
        }
        testPaperDto.setPaper(testPaper);
        testPaperDto.setStructures(structures);
        return testPaperDto;
    }

    /**1.试卷信息转换*/
    private TestPaper convertTestPaper(TeTestPaperInfo paperInfo) {
        TestPaper testPaper=new TestPaper();
        testPaper.setExamPaperId(paperInfo.getId());
        testPaper.setPaperName(paperInfo.getName());
        testPaper.setPaperCode(paperInfo.getCode());
        testPaper.setDescript(paperInfo.getDescription());
        testPaper.setPoints(Double.valueOf(String.valueOf(paperInfo.getTotalScore())));
        testPaper.setUseTimes(0);
        testPaper.setBrowseTimes(0);
        testPaper.setCreateTime(paperInfo.getCreateDate());
        testPaper.setUpdateTime(paperInfo.getLastUpdateDate());
        testPaper.setFromwh(GlobalConstant.PAPER_FORM_XDF_NEW);
        testPaper.setStatus(GlobalConstant.ENTITY_STATUS_USABLE);
        testPaper.setTagFullPath(paperInfo.getTagFullPath());
        //查询新东方试卷学科学段在fullpath里过滤，加入到我的试卷是解析成知识点树的学科学段保存
        Map<String ,Tags> map=questionService.getSubjectByPaperFullPath( testPaper);
        Tags subject=map.get(GlobalConstant.SUBJECT_KEY_subject);
        Tags range=map.get(GlobalConstant.SUBJECT_KEY_range);
        if(subject!=null){
            testPaper.setSubject(subject.getName());
            testPaper.setSubjectId(subject.getId());
        }else {
            testPaper.setSubject("未打学科标签");
            testPaper.setSubjectId(-1);
        }
       if(range!=null){
           testPaper.setRangeId(range.getId());
           testPaper.setRange(range.getName());
       }else{
           testPaper.setRangeId(-1);
           testPaper.setRange("未打学段标签");
       }
        return testPaper;
    }
    /**2.试卷结构转换*/
    private List<TestPaperStructureDto> convertPaperStructure(List<PaperStructureDto> structures) {
        TestPaperStructureDto dto=null;
        List<TestPaperStructureDto> result=new ArrayList<TestPaperStructureDto>();

        PaperStructureDto paperStructureDto=null;
        for(int i=0,size=structures.size();i<size;i++){
            paperStructureDto=structures.get(i);
            dto=convertStructure(paperStructureDto);
            result.add(dto);
        }
        return result;
    }
    private TestPaperStructureDto convertStructure(PaperStructureDto paperStructureDto) {
        TestPaperStructureDto result=new TestPaperStructureDto();
        TestPaperStructure testPaperStructure=new TestPaperStructure();

        testPaperStructure.setName(paperStructureDto.getNodeName());
        testPaperStructure.setPoints(Double.valueOf(String.valueOf(paperStructureDto.getScore())));
        if(paperStructureDto.getNodeType()== TeTestPaperStructure.NODE_PAPER_QUESTION||paperStructureDto.getNodeType()==TeTestPaperStructure.NODE_PAPER_SUBQUESTION){
            testPaperStructure.setStructureType(TestPaperStructure.structure_type_question);
        }

        result.setTestPaperStructure(testPaperStructure);

        PaperDetail paperDetail=new PaperDetail();
        paperDetail.setPoints(Double.valueOf(String.valueOf(paperStructureDto.getScore())));
        paperDetail.setQuestionCode(paperStructureDto.getNodeName());
        result.setDetailDto(new PaperDetailDto());
        result.getDetailDto().setPaperDetail(paperDetail);

        List<PaperStructureDto> children=paperStructureDto.getChild();
        if(children!=null&&children.size()>0){
            TestPaperStructureDto childResult=null;
            for(int i=0,size=children.size();i<size;i++){
                childResult=convertStructure(children.get(i));
                result.getChildren().add(childResult);

            }
        }

        return result;
    }
    private void refreshCache(String paperId){
        List<String> paperIds2 = CacheTools.getCache("import_paper_", ArrayList.class);
        if(paperIds2!=null&&paperIds2.size()>0){
            paperIds2.remove(paperId+"");
            CacheTools.addCache("import_paper_", paperIds2);
        }
    }
    @Override
    public DataSync syncExamPaper() throws Exception {
        String currentSyncCacheKey= DataSync.getCurrrentCacheKey();
         DataSync  currentDataSync=CacheTools.getCache(currentSyncCacheKey,DataSync.class);//根据当前缓存key获取同步状态信息
        if(currentDataSync!=null && !currentDataSync.isCanNextSycn()){
            //线程中同步完成后删除该缓存标识
           return CacheTools.getCache(currentSyncCacheKey,DataSync.class);
        }
        String cloudTagd=PropertiesConfigUtils.getProperty(GlobalConstant.CLOUD_TAG_NAME);
       final String cloudTag=StringUtils.isBlank(cloudTagd)?"63581":cloudTagd;
        Date lastSyncTimeO=testPaperDao.getlastSyncTime();
        if(lastSyncTimeO==null){
            lastSyncTimeO=ParseDate.parse(GlobalConstant.DEFAULT_SYNC_BEGIN_TIME);
            if(!"159469".equals(cloudTag)){
                //trunk环境测试数据
                lastSyncTimeO=ParseDate.parse(GlobalConstant.DEFAULT_SYNC_BEGIN_TIME);
            }
        }
        final  Date lastSyncTime=lastSyncTimeO;
        final String syncCacheKey="sycn_exampaper_flag"+ParseDate.formatByDate(lastSyncTime,ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS); //默认值GlobalConstant.DEFAULT_SYNC_CURRENT_KEY
        CacheTools.addCache(GlobalConstant.SYNCHRO_CURRENT_KEY,syncCacheKey);//保存获取同步缓存信息的key
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                         DataSync  dataSync=new DataSync();
//                        //同步完成清除数据同步标识
//                        CacheTools.delCache(GlobalConstant.SYNCHRO_CURRENT_KEY);
                            boolean syncFlag=true;
                            int pageSize=200;//依旧发，考试系统限制200
                            int pageNo=0;
                            int lastPageSize=0;
                            int minPageNo=0;
                            String statusInfo="";
                            dataSync.setStatusStr("开始同步......");
                            dataSync.setCanNextSycn(false);//正在同步，不进行下一次同步
                            dataSync.setCloudPlatform("中小学云平台id：" + cloudTag);
                            dataSync.setStartSynctime(ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
                            dataSync.setCurrentSynctime(ParseDate.formatByDate(lastSyncTime,ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
                            CacheTools.addCache(syncCacheKey,dataSync);
                            while (syncFlag){
                                dataSync=CacheTools.getCache(syncCacheKey,DataSync.class);//获取最新的缓存
                                int currentSize=0;
                                minPageNo=pageNo;
                                List<PaperVO> paperList= null;
                                 if(pageNo%10==9){
                                     //每5也休息1秒
                                     Thread.sleep(1000*5);
                                 }
                                paperList = paperInterface.syncPaper(lastSyncTime,cloudTag,pageNo*pageSize,pageSize);
                                pageNo++;
                                currentSize=paperList!=null?paperList.size():0;
                                String paStr="【每页："+pageSize+"，正在同步第"+pageNo+"页，本页返回："+currentSize+"】";
                                statusInfo=paStr;
                                if(paperList==null || currentSize<pageSize){
                                    lastPageSize=paperList==null?0:currentSize;
                                    minPageNo--;
                                    syncFlag=false;
                                    if(pageNo==1&&(paperList==null||currentSize==0)){
                                        statusInfo="当前时间没有需要同步的试卷【63581中小学云平台id："+cloudTag+"】"
                                                + ParseDate.formatByDate(lastSyncTime, ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);
                                    }else{
                                        String d=((minPageNo+1)*pageSize+lastPageSize)+"份【共"+(pageNo)+"页】";
                                        statusInfo="试卷同步完成，试卷共："+d;
                                        //同步完成，入库保存同步日志(时间戳、)
                                    }
                                }
                                List<String> paperIds=getPaperIdList(paperList);//调用getsyncPapger打印试卷信息
                                savePaper4ExamPaper(paperIds);//同步保存试卷和试题
                                dataSync=CacheTools.getCache(syncCacheKey,DataSync.class);//获取最新的缓存
                                dataSync.getSyncInfoList().add(statusInfo);
                                CacheTools.addCache(syncCacheKey,dataSync);
                                Thread.sleep(100);
                            }//退出while循环
                            //同步完成清除数据同步标识
                            dataSync.setCanNextSycn(true);
                            dataSync.setStatusStr("【共"+(pageNo)+"页】同步结束......");
                             dataSync.setCurrentSynctime(ParseDate.formatByDate(lastSyncTime, ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
                             dataSync.setStatus(true);
                            //同步完成清除数据同步标识
                            CacheTools.addCacheForever(GlobalConstant.SYNCHRO_CURRENT_KEY_oldsync_data,dataSync);
                            CacheTools.delCache(GlobalConstant.SYNCHRO_CURRENT_KEY);
                            log.info("同步试卷总数："+((minPageNo+1)*pageSize+lastPageSize)+"，共"+(pageNo)+"页，每页"+pageSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }
        });
        return CacheTools.getCache(syncCacheKey,DataSync.class);
    }
    
    /**
     * 个人作业详情查看错时
     * 手动给大题添加小题
     */
	@Override
	public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> createPartTestPaper(
			List<TpExamResultDetail> teIdDetail,
			List<QuestionErrUser> resultDetail, Integer examId,
			int viewType, Map<String, TpExamResultDetail> detailsMap) throws Exception {
		List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList = createPartTestPaperInit(teIdDetail,resultDetail, examId);
		int count=0;//试卷总大题s数
		for (int i = 0; i < questionDtoList.size(); i++) {
			count++;
			com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto question = questionDtoList.get(i);
            QuestionViewDto questionViewDto= question.getQuestionDto().getQuestion().getQuestionViewDto();
            if(questionViewDto==null) questionViewDto=new QuestionViewDto();
            questionViewDto.setViewType(viewType);
            question.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
            questionViewDto.setQuestionNo(count+"");
		}
		parseSubQuestionPoints(questionDtoList,examId,detailsMap);
		return questionDtoList;
	}
	/**
     * 手动给大题添加小题
     */
    public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> createPartTestPaperInit(List<TpExamResultDetail> teIdDetail,
			List<QuestionErrUser> resultDetail, Integer examId) throws Exception{
    	boolean addBig = true;//是否添加大题
		boolean hasSubQ = false;
		List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
		//子题临时存储集合
		List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> subQuestionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
		for (int i = teIdDetail.size()-1; i >= 0; i--) {//循环大题
			addBig = false;//是否添加大题
			TpExamResultDetail detail = teIdDetail.get(i);
			Question q = questionBaseService.getQuestionById(detail.getQuestionId());
			com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto = questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(), detail.getQuestionId());
			for (int j = 0; j < resultDetail.size(); j++) {
				QuestionErrUser nDetail = resultDetail.get(j);
				System.out.println("nDetail.getTeId()==="+nDetail.getTeId()+"  detail.getQuestionId()=="+detail.getQuestionId()+"  nDetail.getQuestionId()="+nDetail.getQuestionId());
				if(nDetail.getTeId()==detail.getQuestionId().intValue()){
					q = questionBaseService.getQuestionById(nDetail.getQuestionId());
					com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto nq = questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(), nDetail.getQuestionId());
					subQuestionDtoList.add(nq);
					addBig = true;
				}else if(nDetail.getQuestionId().intValue()==detail.getQuestionId().intValue()){
					addBig = true;
				}
			}
			if(addBig){//添加大题
				questionDto.setSubQuestions(subQuestionDtoList);//将新组装的子题添加到大题中
				questionDtoList.add(questionDto);//添加大题
				subQuestionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
			}
		}
		return questionDtoList;
    }

    @Override
    public TestPaper createOrEditTestPaper(PaperPager paper, UserEntity loginUser) throws Exception{
        return createOrEditTestPaper(paper, loginUser, null);
    }
    
	@Override
    public TestPaper createOrEditTestPaper(PaperPager paperFilter, UserEntity loginUser,String questionBarkey) throws Exception {
        return createOrEditTestPaper(paperFilter,loginUser,questionBarkey,null);
    }
	
	public TestPaper createOrEditTestPaper(PaperPager paperFilter, UserEntity loginUser,String questionBarkey, Integer viewType) throws Exception {
        QuestionBar questionBar=null;
        TestPaper testPaper=null;
        boolean isEdit=paperFilter.getPaperId()>0?true:false;
        if(isEdit){
//            paperFilter.setPageSize(OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE);
//            paperFilter.setLoginUser(loginUser);
//            paperFilter=questionService.searchPaper(paperFilter);
//            if(paperFilter.getResultList()!=null && paperFilter.getResultList().size()>0){
//                testPaper= (TestPaper) paperFilter.getResultList().get(0);//testPaperDao.findTestPaper(paperFilter.getPaperId());
//            }
            testPaper=testPaperDao.findTestPaper(paperFilter.getPaperId());
            if(testPaper==null){
                //同步的试卷没有入库，但建了索引
                return null;
            }
            String joinUser=testPaper.getJoinselfUser();
            if(loginUser!=null && StringUtils.isNotBlank(joinUser)&& joinUser.indexOf("_"+loginUser.getId()+"_")>-1){
                testPaper.setJoin(true);
            }
            if(StringUtils.isNotBlank(paperFilter.getTitle())) testPaper.setPaperName(paperFilter.getTitle());
//            Map<String ,Tags> map=questionService.getSubjectByPaperFullPath(testPaper);//试卷同步时保存学科学段
//            Tags subject=map.get(GlobalConstant.SUBJECT_KEY_subject);//设置学科学段
//            Tags range=map.get(GlobalConstant.SUBJECT_KEY_range);
//            questionBarkey+=subject.getId()+"_"+range.getId();
            questionBarkey+=testPaper.getSubjectId()+"_"+testPaper.getRangeId();
            Map<String,Tags> questionTypeMap= KlbTagsUtil.getInstance().findQuestionTypeMap(testPaper.getSubjectId(),testPaper.getRangeId());
            //编辑是封装数据
            questionBar=new QuestionBar( );
            int count=0;//试卷总大题s数
            //试卷结构试题列表
            List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperFilter.getPaperId());
            //试卷试题题型列表
            List<TestPaperStructure> questionTypeList=testPaperStructureDao.findStructureQuestionTypeByPaperId(paperFilter.getPaperId());
            Map<String,Double> subScoreMap= findSubQuestionPoints(paperFilter.getPaperId());
            List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
            for(TestPaperStructure tps:questionTypeList){
                Tags qtype=questionTypeMap.get(tps.getName());
                if(qtype==null){
                    qtype=new Tags();
                    qtype.setName(tps.getName()+QUESTION_TYPE_ILLEGAL);
                    qtype.setId(9999);
                    log.info(testPaper.getSubject() +testPaper.getRange()+"试卷展示,不存在的题型【"+tps.getName()+"】");
//                    continue;
                }
                //迭代题型
                PaperQuestionType paperQuestionType=new PaperQuestionType();
                paperQuestionType.setQuestionTypeName(qtype.getName());
                paperQuestionType.setQuestionType(qtype.getId());
                QuestionBarType qbt=new QuestionBarType();qbt.setName(qtype.getName());qbt.setType(qtype.getId());
                if(tps.getPoints()!=null ||tps.getPoints()>0){
                    //试卷题目有分值
                    qbt.setDefaultScore(tps.getPoints());
                }else{
                    //获取题型默认值
                    String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qtype.getId());
                    Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                    qbt.setDefaultScore(defaultScored);
                }
                questionBar.getQuestionTypeArr().add(qbt);//1）试题栏添加题型信息
                List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();//获取题目
                for(TestPaperStructure questionCode:questionCodeList){
                    if(tps.getId()==questionCode.getParent()){
                        //封装每个题型下的试题（te_test_paper_structure 关联的全是大题）
                        com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=questionBaseService.findQuestionByCode(questionCode.getName());
                        if(questionDto==null){
                           continue;
                        }
                        count++;//大题数加一
                        questionDtoList.add(questionDto);
                        Integer questionId=questionDto.getQuestionDto().getQuestion().getId();
                        //2)试题栏：封装大题分值
                        questionBar.getEditScore().put(questionId,""+questionCode.getPoints());
                        //3)试题栏封装每个题型下的大题id
                        qbt.getExamIdArr().add(questionId);
                        QuestionViewDto questionViewDto= questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
                        if(questionViewDto==null) questionViewDto=new QuestionViewDto();
                        if(viewType!=null){//不同页面模板显示样式不同
                        	questionViewDto.setViewType(viewType);
                        }
                        questionDto.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
//                        questionViewDtoFillUserAnswer(questionViewDto, questionDto, paperFilter.getDetailsMap());
                        questionViewDto.setScore(questionCode.getPoints());//设置大题分值
                        questionViewDto.setQuestionNo(count+"");
                        //处理小题分值
                        setsubPoint(questionDto, subScoreMap, paperFilter.getDetailsMap());
                    }
                }
                if(loginUser!=null){
                    //设置显示是否收藏
                    List<Integer> collectionIds=questionService.getCollectionQuestionByUser(loginUser.getId(), qbt.getExamIdArr(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                    for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto:questionDtoList){
                        if(collectionIds.contains(questionDto.getQuestionDto().getQuestion().getId())){
                            questionDto.getQuestionDto().getQuestion().setLoginUserCollectioned(true);//该题当前用户已收藏
                        }
                    }
                }
                paperQuestionType.setQuestionDtoList(questionDtoList);
                paperQuestionTypeList.add(paperQuestionType);
            }
            questionBar.setCount(count);//4)试题栏 试题总数
            testPaper.setPaperQuestionTypeList(paperQuestionTypeList);
            CacheTools.addCache(questionBarkey, questionBar);
            testPaper.setQuestionBar(questionBar);
//            System.out.println("组卷缓存前缀："+questionBarkey);
            //更新试卷浏览
            testPaper.setBrowseTimes(1 + testPaper.getBrowseTimes());
            testPaperDao.update(testPaper);
            questionService.updatePaperIndex(testPaper);
        }else{
            //创建试卷，题目从缓存获取
            testPaper=new TestPaper();
            testPaper.setPaperName(paperFilter.getTitle());//智能试卷名称
            testPaper.setPaperName("");//智能试卷名称
            testPaper.setRangeId(paperFilter.getRangeId());
            testPaper.setSubjectId(paperFilter.getSubjectId());
            questionBarkey+=paperFilter.getSubjectId()+"_"+paperFilter.getRangeId();
            questionBar=CacheTools.getCache(questionBarkey,QuestionBar.class);
            int count=0;//试卷总大题s数
            List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
            //创建试卷从缓存获取试卷结构
            for(QuestionBarType qbt:questionBar.getQuestionTypeArr()){
                List<Integer> examIdArr=qbt.getExamIdArr();//获取已加入的试题id
                if(examIdArr==null&&examIdArr.size()<1) continue;
                //封装题型
                PaperQuestionType paperQuestionType=new PaperQuestionType();
                paperQuestionType.setQuestionTypeName(qbt.getName());
                paperQuestionType.setQuestionType(qbt.getType());
                //获取题型默认值
                String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qbt.getType());
                Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();//获取题目
                if(examIdArr!=null&&examIdArr.size()>0){
                    for(Integer qid:examIdArr){
                        List<Integer> questionIds=new ArrayList<Integer>(); questionIds.add(qid);
                        List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoOneList=
                                questionBaseService.findQuestionByIds(questionIds,-1);
                        com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto   question=questionDtoOneList.get(0);
                        question.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo(++count+"");
                        //处理每个大题的子题
                        QuestionViewDto questionViewDto = QuestionUtil.getSubQuestionViewDto(question,
                                question.getQuestionDto().getQuestion().getQuestionViewDto());//组织小题的questionViewDto （小题序号和分值）
                        question.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
                        questionViewDto.setScore(defaultScored);
                        //处理默认分值
                        parsedefaultScore(questionViewDto);
                        question.getQuestionDto().getQuestion().setDefaultScore(questionViewDto.getScore());
                        qbt.setDefaultScore(questionViewDto.getScore());
                        questionDtoList.add(question);
                    }
                }
                //设置显示是否收藏
                List<Integer> collectionIds=questionService.getCollectionQuestionByUser(loginUser.getId(), qbt.getExamIdArr(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto:questionDtoList){
                    if(collectionIds.contains(questionDto.getQuestionDto().getQuestion().getId())){
                        questionDto.getQuestionDto().getQuestion().setLoginUserCollectioned(true);//该题当前用户已收藏
                    }
                }
                paperQuestionType.setQuestionDtoList(questionDtoList);
                paperQuestionTypeList.add(paperQuestionType);
            }
            questionBar.setCount(count);//4)试题栏 试题总数
            testPaper.setPaperQuestionTypeList(paperQuestionTypeList);
            testPaper.setQuestionBar(questionBar);
            CacheTools.addCache(questionBarkey, questionBar);
            //试卷预览
        }
        return testPaper;
    }

    private void parsedefaultScore(QuestionViewDto questionViewDto) {
        List<QuestionViewDto> subViews =questionViewDto.getSubDtos();
        if(subViews!=null &&subViews.size()>0){
            for(QuestionViewDto sub:subViews){
                sub.setScore(questionViewDto.getScore());
            }
            //大题分值是默认分值乘以小题个数
            questionViewDto.setScore(questionViewDto.getScore()*subViews.size());
        }
    }

    @Override
    public QuestionBarHtml getQuestionBarHtml(Integer paperId) throws Exception {
                TestPaper testPaper=testPaperDao.findTestPaper(paperId);
                if(testPaper==null){
                    //同步的试卷没有入库，但建了索引
                    return null;
                }
                Map<String,Tags> questionTypeMap= KlbTagsUtil.getInstance().findQuestionTypeMap(testPaper.getSubjectId(),testPaper.getRangeId());
                //编辑是封装数据
                QuestionBar questionBar=new QuestionBar( );
                int count=0;//试卷总大题s数
                //试卷结构试题列表
                List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperId);
                //试卷试题题型列表
                List<TestPaperStructure> questionTypeList=testPaperStructureDao.findStructureQuestionTypeByPaperId(paperId);
                List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
                for(TestPaperStructure tps:questionTypeList){
                    Tags qtype=questionTypeMap.get(tps.getName());
                    if(qtype==null){
                        qtype=new Tags();
                        qtype.setName(tps.getName()+QUESTION_TYPE_ILLEGAL);
                        qtype.setId(9999);
                        log.info(testPaper.getSubject() +testPaper.getRange()+"试卷展示,不存在的题型【"+tps.getName()+"】");
//                    continue;
                    }
                    //迭代题型
                    PaperQuestionType paperQuestionType=new PaperQuestionType();
                    paperQuestionType.setQuestionTypeName(qtype.getName());
                    paperQuestionType.setQuestionType(qtype.getId());
                    QuestionBarType qbt=new QuestionBarType();qbt.setName(qtype.getName());qbt.setType(qtype.getId());

                    //获取题型默认值
                    String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qtype.getId());
                    Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                    qbt.setDefaultScore(defaultScored);
                    questionBar.getQuestionTypeArr().add(qbt);//1）试题栏添加题型信息
                    List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();//获取题目
                    for(TestPaperStructure questionCode:questionCodeList){
                        if(tps.getId()==questionCode.getParent()){
                            //封装每个题型下的试题（te_test_paper_structure 关联的全是大题）
                            com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=questionBaseService.findQuestionByCode(questionCode.getName());
                            if(questionDto==null){
                                continue;
                            }
                            count++;//大题数加一
                            questionDtoList.add(questionDto);
                            Integer questionId=questionDto.getQuestionDto().getQuestion().getId();
                            //2)试题栏：封装大题分值
                            questionBar.getEditScore().put(questionId,""+questionCode.getPoints());
                            //3)试题栏封装每个题型下的大题id
                            qbt.getExamIdArr().add(questionId);
                        }
                    }
                    paperQuestionType.setQuestionDtoList(questionDtoList);
                    paperQuestionTypeList.add(paperQuestionType);
                }
                questionBar.setCount(count);//4)试题栏 试题总数
                testPaper.setPaperQuestionTypeList(paperQuestionTypeList);
                testPaper.setQuestionBar(questionBar);
                QuestionBarHtml q = new QuestionBarHtml(questionBar);
               return q;
    }
    private List<String> getPaperIdList(List<PaperVO> paperList) {
        List<String> idList=new ArrayList<String>();
        if(paperList!=null &&paperList.size()>0){
            for (int i = 0; i < paperList.size(); i++) {
                idList.add(paperList.get(i).getPaper().getId()+"");
//                getsyncPapger(paperList.get(i).getPaper());//getsyncPapger
            }
        }
        return idList;
    }

    /**
     * 同步试卷是记录 相同code的试卷信息
     * @param paper
     */
    private void getsyncPapger(TeTestPaperInfo paper) {
        String currentSyncCacheKey= DataSync.getCurrrentCacheKey();
        DataSync  currentDataSync=CacheTools.getCache(currentSyncCacheKey,DataSync.class);
        if (currentDataSync==null) return ;
        List<TeTestPaperInfo> pvoList=currentDataSync.getSyncPapgeCodeCountMap().get(paper.getCode());
        if(pvoList==null) pvoList=new ArrayList<TeTestPaperInfo>();
        pvoList.add(paper);
        currentDataSync.getSyncPapgeCodeCountMap().put(paper.getCode(),pvoList);
        CacheTools.addCache(currentSyncCacheKey,currentDataSync);
    }

    /**
     * 更新组卷知识统计
     * @param qb
     * @throws Exception
     */
    @Override
    public QuestionBar parseKnowledgeCount(QuestionBar qb) throws Exception{
        if(qb==null||qb.getQuestionTypeArr().size()<1) return qb;
        Map<String, Integer> knowledgeCount=new HashMap<String, Integer>();//qb.getKnowledgeCount();
        List<QuestionBarType> qta=qb.getQuestionTypeArr();
        for(QuestionBarType qt:qta){
            List<Integer> examIdList=qt.getExamIdArr();
            if(examIdList.size()<1) continue;
            //获取每个题型下的所有的题
            List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList=
                    questionBaseService.findQuestionByIds(examIdList, -1);
            if(questionDtoList!=null &&questionDtoList.size()>0){
                for(int i=0;i<questionDtoList.size();i++){
                    String knowledgeStr=questionDtoList.get(i).getQuestionDto().getQuestion().getKnowledgeTags();
                    if(StringUtils.isBlank(knowledgeStr)) continue;
                    String[] knowArr=knowledgeStr.split(GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR);
                    for(String knowName:knowArr){
                        Integer count=knowledgeCount.get(knowName);
                        if(count==null){
                            knowledgeCount.put(knowName,1);
                            continue;
                        }
                        knowledgeCount.put(knowName,++count);
                    }
                }
            }
        }
        qb.setKnowledgeCount(knowledgeCount);
        return qb;
    }

    @Override
    public void jionMyself(Integer paperId, UserEntity loginUser) throws Exception {
        PaperPager pager=new PaperPager();
        pager.setPaperId(paperId);
        TestPaper testPaper=testPaperDao.findTestPaper(paperId);
        if(testPaper==null) return ;
        String jionUser=testPaper.getJoinselfUser();
        String userId="_"+loginUser.getId()+"_";
        if(StringUtils.isBlank(jionUser)||"_".equals(jionUser)){
            jionUser=userId;
        }else {
            int index=jionUser.indexOf(userId);
            if(index<0){
                //不存在，则加入试卷库
                jionUser+=loginUser.getId()+"_";;
            }else{
                //存在则移除试卷库
                jionUser= jionUser.replace(userId,"_");
            }
        }
        testPaper.setJoinselfUser(jionUser);
        testPaperDao.update(testPaper);
        producerPaperService.send(testPaper);
    }

    /**
     * 生成下载试卷的数据
     * @param paperId
     * @return
     * @throws Exception
     */
    @Override
    public TestPaper downloadPaperPDF(Integer paperId) throws Exception {
        TestPaper testPaper=  testPaper=testPaperDao.findTestPaper(paperId);
        if(testPaper==null) return null;
            Map<String,Tags> questionTypeMap= KlbTagsUtil.getInstance().findQuestionTypeMap(testPaper.getSubjectId(),testPaper.getRangeId());
            int count=0;//试卷总大题s数
            //试卷结构试题列表
            List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperId);
            //试卷试题题型列表
            List<TestPaperStructure> questionTypeList=testPaperStructureDao.findStructureQuestionTypeByPaperId(paperId);
            List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
            for(TestPaperStructure tps:questionTypeList){
                Tags qtype=questionTypeMap.get(tps.getName());
                if(qtype==null){
                    qtype=new Tags();
                    qtype.setName(tps.getName()+QUESTION_TYPE_ILLEGAL);
                    qtype.setId(9999);
                    log.info(testPaper.getSubject() +testPaper.getRange()+"试卷展示,不存在的题型【"+tps.getName()+"】");
//                    continue;
                }
                //迭代题型
                PaperQuestionType paperQuestionType=new PaperQuestionType();
                paperQuestionType.setQuestionTypeName(qtype.getName());
                paperQuestionType.setQuestionType(qtype.getId());
                QuestionBarType qbt=new QuestionBarType();qbt.setName(qtype.getName());qbt.setType(qtype.getId());
                //获取题型默认值
                String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qtype.getId());
                Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                qbt.setDefaultScore(defaultScored);
                List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();//获取题目
                for(TestPaperStructure questionCode:questionCodeList){
                    if(tps.getId()==questionCode.getParent()){
                        //封装每个题型下的试题（te_test_paper_structure 关联的全是大题）
                        com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=questionBaseService.findQuestionByCode(questionCode.getName());
                       if(questionDto==null){
                           continue;
                       }
                        count++;//大题数加一
                        questionDtoList.add(questionDto);
                        Integer questionId=questionDto.getQuestionDto().getQuestion().getId();
                        //3)试题栏封装每个题型下的大题id
                        qbt.getExamIdArr().add(questionId);
                    }
                }
                paperQuestionType.setQuestionDtoList(questionDtoList);
                paperQuestionTypeList.add(paperQuestionType);
            }
            testPaper.setPaperQuestionTypeList(paperQuestionTypeList);
        return testPaper;
    }

    /**
     * 生成pdf文件
     * @param testPaper
     * @return
     * @throws Exception
     */
    @Override
    public void generalPaperFrame(TestPaper testPaper) throws Exception {
        if(testPaper==null)return;
//        testPaper.setDownloadPaperName(GlobalConstant.DOWNLOAD_PAPER_JIEX_AFTER);
        String pdfFile =ItextpdfUtils.downloadPaperPath(testPaper.getDownloadPaperName());
        Document document = ItextpdfUtils.addTitle(pdfFile, testPaper.getPaperName());
        String ss="班级：_______ 姓名：_______  考号：_______  满分：_______";
        ItextpdfUtils.addParagraphToDocument(document,ss,ItextpdfUtils.contentType_txx,ItextpdfUtils.contentType_default_200);
        NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
        long typeCount=0;
        int count=1;// 试卷题序
        List<String> explanafterList=new ArrayList<String>();
        for(PaperQuestionType questionType:testPaper.getPaperQuestionTypeList()){
            if(questionType.getQuestionDtoList()!=null && questionType.getQuestionDtoList().size()>0) typeCount++;
            //隐藏没有题的题型
            if(questionType.getQuestionDtoList()==null||questionType.getQuestionDtoList().size()<1) continue;
            String qyName=nt.getText(typeCount)+"、"+questionType.getQuestionTypeName()+"（共"+questionType.getQuestionDtoList().size()+"小题）";
            ItextpdfUtils.addParagraphToDocument(document, qyName, ItextpdfUtils.contentType_tmlx, ItextpdfUtils.contentType_tmlx_80);
            for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto:questionType.getQuestionDtoList()){
                if(questionDto.getQuestionDto().getQuestion().getQuestionViewDto()==null) questionDto.getQuestionDto().getQuestion().setQuestionViewDto(new QuestionViewDto());
                QuestionViewDto questionViewDto=questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
                questionViewDto.setQuestionNo((count++)+"");
                questionViewDto=QuestionUtil.getSubQuestionViewDto(questionDto,questionViewDto);
                questionViewDto.setDownloadType(testPaper.getDownloadType());
                generalQuestion(document, questionViewDto, questionDto, explanafterList,null);//加载题目信息
            }
        }
        if(GlobalConstant.DOWNLOAD_PAPER_JIEX_FENLI.equals(testPaper.getDownloadType())){
            document.newPage();
            if(explanafterList!=null &&explanafterList.size()>0){
                ItextpdfUtils.addParagraphToDocument(document,"试卷题目解析：",ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                for(String explan:explanafterList){
                    //最后加载解析
                    ItextpdfUtils.addParagraphToDocument(document,explan,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                }
            }
        }
        document.close();
    }

    @Override
    public void generalQuestion(Document document,QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto ) throws IOException, DocumentException {
        generalQuestion(  document,  questionViewDto,  questionDto,null,null);
    }
    @Override
    public void generalQuestion(Document document,QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto,List<String> explanafterList, Map<Integer, QuestionErrUser> mapAll ) throws IOException, DocumentException {
        int questionType=questionDto.getQuestionType();
        int teid=questionDto.getQuestionDto().getQuestion().getTeId();
        String searchContent="";//提干
        String answerReference="";//参考答案
        String scorestandad="";//评分标准
        String subexplan="";//解析

        int kongHangNum=1;//题目间距，默认1行，简答题4行
        // 判断
        switch (questionType) {
            case Question.QUESTION_TYPE_DANXUAN:
            case Question.QUESTION_TYPE_DUOXUAN:
            case Question.QUESTION_TYPE_SORT:
                ChoiceQuestionDto cq= (ChoiceQuestionDto) questionDto;
                searchContent=  cq.getChoiceQuestion().getTopic();
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                java.util.List<ChoiceAnswer> choiceAnswers=cq.getChoiceAnswers();
                String[] aa={"A","B","C","D","E","F","G","H","I","J","K","L","M"};
                if(StringUtils.isBlank(searchContent)&&teid!=0){
                    //子题只有选项
                    ItextpdfUtils.addParagraphToDocument(document,questionViewDto.getQuestionNo(), ItextpdfUtils.contentType_txx,ItextpdfUtils.contentType_txx_110);
                }
                if(choiceAnswers!=null&&choiceAnswers.size()>0){
                    //选择题选项
                    int i=0;
                    for(ChoiceAnswer ca:choiceAnswers){
                        //设置选择选项
                        ItextpdfUtils.addParagraphToDocument(document, "【" + aa[i] + "】" + ca.getDescription(), ItextpdfUtils.contentType_txx,ItextpdfUtils.contentType_txx_110);
                        i++;
                    }
                }

                //参考答案
                answerReference="    答案:   "+QuestionUtil.getAnswer(questionDto);
                subexplan = teid>0?"":"    试题解析:   "+HtmlCssUtil.delHTMLTag(questionDto.getQuestionDto().getQuestion().getExplan());//忽略小题解析，
                     if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
                         explanafterList.add(answerReference);
                         if(teid>0){
                            explanafterList.add(subexplan);
                         }
                     }else if(isAfterDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())) {
                         ItextpdfUtils.addParagraphToDocument(document,answerReference,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                         if(teid>0){
                            ItextpdfUtils.addParagraphToDocument(document,subexplan,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                         }
                     }
                break;
            case Question.QUESTION_TYPE_SHORT:
                kongHangNum=4;
                ShortQuestionDto shp= (ShortQuestionDto) questionDto;
                searchContent= shp.getShortQuestion().getTopic() ;
                addTiganToPdf(  document,  searchContent ,questionViewDto);//封装提干及图片到pdf文档
                //参考答案
                answerReference="   参考答案:   "+QuestionUtil.getAnswer(questionDto);
                //评分标准
                scorestandad="    评分标准:   "+shp.getShortQuestion().getScorestandad();
                if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
                    explanafterList.add(answerReference);
                    explanafterList.add(scorestandad);
                }else if(isAfterDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())) {
                    ItextpdfUtils.addParagraphToDocument(document,scorestandad,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                    ItextpdfUtils.addParagraphToDocument(document,answerReference,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                }
                break;
            case Question.QUESTION_TYPE_FILL_BLANK:
            case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
                EssayQuestionDto sq= (EssayQuestionDto) questionDto;
                searchContent=sq.getEssayQuestion().getTopic() ;
                if(StringUtils.isBlank(searchContent)){
                    //填空子题
                    searchContent="填写答案： ________";
                }
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                answerReference="   正确答案:   "+QuestionUtil.getAnswer(questionDto);
                if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
                    if(teid>0){
                        explanafterList.add(answerReference);
                    }
                }else if(isAfterDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())) {
                    if(teid>0){
                        ItextpdfUtils.addParagraphToDocument(document,answerReference,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                    }
                }
                break;
            case Question.QUESTION_TYPE_READ:
            case Question.QUESTION_TYPE_LISTEN:
                ComplexQuestionDto cop= (ComplexQuestionDto) questionDto;
                searchContent=cop.getComplexQuestion().getTopic() ;
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                break;
            case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
            case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
            case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
                ComplexQuestionDto cxp= (ComplexQuestionDto) questionDto;
                searchContent=cxp.getComplexQuestion().getTopic() ;
                if(StringUtils.isBlank(searchContent)){
                    //填空子题
                    searchContent="    填写答案： ________";
                }
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
//                String parseContent="解析gsh:"+cxp.getQuestionDto().getQuestion().getExplan();
//                addTiganToPdf(  document,  parseContent,questionViewDto);
                answerReference="   正确答案:   "+QuestionUtil.getAnswer(questionDto);
                if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
                    if(teid>0){
                        explanafterList.add(answerReference);
                    }
                }else if(isAfterDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())) {
                    if(teid>0){
                        ItextpdfUtils.addParagraphToDocument(document,answerReference,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                    }
                }
                break;
            case Question.QUESTION_TYPE_SPOKEN:
                SpokenQuestionDto skp= (SpokenQuestionDto) questionDto;
                searchContent= skp.getSpokenQuestion().getTopic();
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                break;
            case Question.QUESTION_TYPE_CORRECTION://改错题106
                ComplexCorrectionQuestionDto ccp= (ComplexCorrectionQuestionDto) questionDto;
                searchContent= ccp.getComplexQuestion().getTopic() ;
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                break;
            case Question.QUESTION_TYPE_SUB_CORRECTION://改错题子题107
                CorrectionQuestionDto subCorrect=(CorrectionQuestionDto)questionDto;
                searchContent=subCorrect.getCorrectionQuestion().getClause();//改错题子题提干
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                break;
            case Question.QUESTION_TYPE_WHRITE:
                WhriteQuestionDto wp= (WhriteQuestionDto) questionDto;
                searchContent=wp.getWhriteQuestion().getTopic() ;
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                //参考答案
                answerReference="    参考答案:   "+QuestionUtil.getAnswer(questionDto);
                if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
                    explanafterList.add(answerReference);
                }else if(isAfterDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())) {
                   ItextpdfUtils.addParagraphToDocument(document,answerReference,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
                }
                break;
            case Question.QUESTION_TYPE_TABLE://表格（showForm 1：拖拽型，2：表格型）:
                MatrixQuestionDto mxp= (MatrixQuestionDto) questionDto;
                searchContent=mxp.getMatrixQuestion().getTopic() ;
                addTiganToPdf(  document,  searchContent,questionViewDto);//封装提干及图片到pdf文档
                break;
            default:
        }
        //处理子题
        List<QuestionViewDto> subviewList=questionViewDto.getSubDtos();
        List<String> explanafterListSub=null;
        if(subviewList!=null&&subviewList.size()>0){
            explanafterListSub=new ArrayList<String>();
            for(int i=0 ;i<subviewList.size();i++){
                subviewList.get(i).setDownloadType(questionViewDto.getDownloadType());//设置下载是否分离解析
                subviewList.get(i).setButtonType(questionViewDto.getButtonType());
                generalQuestion( document,subviewList.get(i),   questionDto.getSubQuestions().get(i),explanafterListSub ,mapAll);
            }
        }else if(questionType==Question.QUESTION_TYPE_FILL_BLANK&&questionDto.getQuestionDto().getQuestion().getTeId()==0){
            //补全大题填空题作答区域
            List<FillblankAnswer> faList=((EssayQuestionDto) questionDto).getFillblankAnswers();
            String arrea="填写答案：";
            if(faList!=null&&faList.size()>0){
                for(int i=0;i<faList.size();i++){
                    arrea+="("+(i+1)+") ________";
                }
                ItextpdfUtils.addParagraphToDocument(document,arrea, ItextpdfUtils.contentType_txx,ItextpdfUtils.contentType_txx_110);
            }
        }
        if(questionViewDto.getButtonType()==QuestionViewDto.button_ype_zuoye_jiangping
                &&questionType!=Question.QUESTION_TYPE_READ
                &&questionType!=Question.QUESTION_TYPE_LISTEN
                &&questionType!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                &&questionType!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){//作业讲评下载特殊处理，显示题目得分率，答错人，未做答人
        	if(mapAll!=null){
        		QuestionErrUser questionErr= mapAll.get(questionDto.getQuestionDto().getQuestion().getId());
            	String signStr = "";
            	if(StringUtils.isNotBlank(questionErr.getAvgRate())){
            		signStr += "【错误率】"+questionErr.getAvgRate()+"%";
            	}else{
            		signStr += "【错误率】0%"; //"      平均得分：0");
            	}
            	ItextpdfUtils.addParagraphToDocument(document,signStr,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	
//            	signStr = "";
//            	if(avg!=null){
//            		signStr += "【 平均得分】"+avg.getAvgScore();
//            	}else{
//            		signStr += "【 平均得分】0";
//            	}
//            	ItextpdfUtils.addParagraphToDocument(document,signStr,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	signStr = "";
            	if( StringUtils.isNotBlank(questionErr.getErrUserName()) && StringUtils.isNotBlank(questionErr.getErrUserNum()) ){
            		signStr += "【答错】"+questionErr.getErrUserName()+"等"+questionErr.getErrUserNum()+"人";
            	}else{
            		signStr += "【答错】无";
            	}
            	ItextpdfUtils.addParagraphToDocument(document,signStr,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	
            	signStr = "";
            	if( StringUtils.isNotBlank(questionErr.getNoAnswerUserName()) && StringUtils.isNotBlank(questionErr.getNoAnswerUserNum()) ){
            		signStr += "【未作答】"+questionErr.getNoAnswerUserName()+"等"+questionErr.getNoAnswerUserNum()+"人";
            	}else{
            		signStr += "【未作答】无";
            	}
            	ItextpdfUtils.addParagraphToDocument(document,signStr,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);

            	document.newPage();//新建页.题目解析和题目数据分页显示需求
            	if(questionType==Question.QUESTION_TYPE_SHORT||questionType==Question.QUESTION_TYPE_WHRITE){
            		signStr = "【参考答案】";
            	}else{
            		signStr = "【正确答案】";
            	}
            	signStr = signStr + QuestionUtil.getAnswer(questionDto);
            	ItextpdfUtils.addParagraphToDocument(document,signStr,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	
            	String explan = "【试题解析】"+HtmlCssUtil.delHTMLTag(questionDto.getQuestionDto().getQuestion().getExplan());
            	explan = StringEscapeUtils.unescapeHtml(explan);
            	ItextpdfUtils.addParagraphToDocument(document,explan,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	
//                String knowledgeHtml=questionDto.getQuestionDto().getQuestion().getKnowledgeTags();
//                knowledgeHtml=StringUtils.isBlank(knowledgeHtml)?"":knowledgeHtml;
//                ItextpdfUtils.addParagraphToDocument(document,"【知识点】"+knowledgeHtml,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            	document.newPage();//新建页.题目解析和题目数据分页显示需求
        	}
        }else if(isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())){
            //解析跟试题 分离到最后
            explanafterList.add(questionViewDto.getQuestionNo()+".  ");
            String knowledgeHtml=questionDto.getQuestionDto().getQuestion().getKnowledgeTags();
            knowledgeHtml=StringUtils.isBlank(knowledgeHtml)?"":knowledgeHtml;
            String explan = "试题解析:  "+HtmlCssUtil.delHTMLTag(questionDto.getQuestionDto().getQuestion().getExplan());
            explanafterList.add("知识点："+knowledgeHtml);
            explanafterList.add(explan);
            if(explanafterListSub!=null&&explanafterListSub.size()>0){
                explanafterList.addAll(explanafterListSub);
            }
        }else if(!isFenliDownload(questionViewDto.getDownloadType(), questionViewDto.getButtonType())
                &&questionType!=Question.QUESTION_TYPE_READ
                &&questionType!=Question.QUESTION_TYPE_LISTEN
                &&questionType!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                &&questionType!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){
            String explan = "试题解析:  "+HtmlCssUtil.delHTMLTag(questionDto.getQuestionDto().getQuestion().getExplan());
            ItextpdfUtils.addParagraphToDocument(document,explan,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
            //试题后面紧跟解析，排除老师讲评
            String knowledgeHtml=questionDto.getQuestionDto().getQuestion().getKnowledgeTags();
            knowledgeHtml=StringUtils.isBlank(knowledgeHtml)?"":knowledgeHtml;
            ItextpdfUtils.addParagraphToDocument(document,"知识点："+knowledgeHtml,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
        }

       for(int i=0;i<kongHangNum*20;i++){
           //生成题间距
           ItextPDFUtil.addBlankLine(document);
//            document.newPage();
       }
    }
    public boolean isFenliDownload(String downloadType,int buttonType){
        //下载分离，不包含讲评
        return GlobalConstant.DOWNLOAD_PAPER_JIEX_FENLI.equals(downloadType)&&
                buttonType!=QuestionViewDto.button_ype_zuoye_jiangping;
    }
    public boolean isAfterDownload(String downloadType,int buttonType){
        //下载紧跟解析，不包含讲评
        return GlobalConstant.DOWNLOAD_PAPER_JIEX_AFTER.equals(downloadType)&&
                buttonType!=QuestionViewDto.button_ype_zuoye_jiangping;
    }
    /**
     *  封装提干及图片到pdf文档
     * @param document
     * @param searchContent
     * @throws IOException
     * @throws DocumentException
     */
  public static void addTiganToPdf(Document document,String searchContent,QuestionViewDto questionViewDto) throws IOException, DocumentException {
      List<String> imageUrlList=HtmlCssUtil.getImageUrl(searchContent);//提取题目图片
      ItextpdfUtils.addImageListToDocument(document,imageUrlList);
      //设置提干
      List<String> paragraphList= parseParagraphByPtag(questionViewDto,searchContent);
      ItextpdfUtils.addParagraphListToDocument(document,paragraphList,ItextpdfUtils.contentType_tg,ItextpdfUtils.contentType_tg_100);
  }
    /**
     * 解析提干锻炼
     * @param
     * @return
     */
    private static List<String> parseParagraphByPtag(QuestionViewDto questionViewDto,String searchContent) {
        List<String> pList=new ArrayList<String>();
        if(StringUtils.isBlank(searchContent)) return pList;
        String rp="&&Paragranph@@";
        searchContent=searchContent.replace("<p>",rp).replace("</p>",rp);
        searchContent= HtmlCssUtil.delHTMLTag(searchContent);
        String[] p=searchContent.split(rp);
        if(p!=null && p.length>0){
            int begin=0;
            for(int i=0;i<p.length;i++){
                if(StringUtils.isNotBlank(p[i])){
                    String no=begin==0?questionViewDto.getQuestionNo():"";//每个题干的第一段 添加编号
                    if(StringUtils.isNotBlank(no)&&no.indexOf(".")<0){
                        no+=".  ";
                    }
                    pList.add(no+p[i]);
                    ++begin;
                }
            }
        }
        return pList;
    }

	@Override
	public void downloadTaskComment(
			List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList,
			List<QuestionErrUser> avg, List<QuestionErrUser> errUser,
			List<QuestionErrUser> noAnswerUser, TaskDto task,Map<Integer, QuestionErrUser> mapAll,
			String downloadName) throws IOException, DocumentException {
		String pdfFile =ItextpdfUtils.downloadPaperPath(downloadName);
        Document document = ItextpdfUtils.addTitle(pdfFile, task.getExamName());
        //查询题目得分率和平均得分 每道题的
        Map<Integer,QuestionErrUser> mapAvg = Maps.uniqueIndex(avg, new Function <QuestionErrUser,Integer>(){
			public Integer apply(QuestionErrUser from){
				return from.getQuestionId();   
		}});
        //查询未作答人 	未作答人，指的是提交作业但该题没有选择或上传答案的人
        Map<Integer,QuestionErrUser> mapNoAnswerUser = Maps.uniqueIndex(noAnswerUser, new Function <QuestionErrUser,Integer>(){
			public Integer apply(QuestionErrUser from){
				return from.getQuestionId();   
		}});
        //题目id和题目对应的答错人及人数集合
        Map<Integer,QuestionErrUser> mapErrUser = Maps.uniqueIndex(errUser, new Function <QuestionErrUser,Integer>(){
			public Integer apply(QuestionErrUser from){
				return from.getQuestionId();   
		}});
        long typeCount=0;
        int count=1;// 试卷题序
        for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto:questionDtoList){
        	QuestionViewDto questionViewDto=new QuestionViewDto();
            questionViewDto.setQuestionNo((count++)+"");
            questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_jiangping);
            questionViewDto=QuestionUtil.getSubQuestionViewDto(questionDto,questionViewDto);
            List<QuestionErrUser> li = new ArrayList<QuestionErrUser>();
            li.add( mapAvg.get(questionDto.getQuestionDto().getQuestion().getId()) );
            li.add( mapErrUser.get(questionDto.getQuestionDto().getQuestion().getId()) );
            li.add( mapNoAnswerUser.get(questionDto.getQuestionDto().getQuestion().getId()) );
            questionDto.getQuestionDto().getQuestion().setQuestionErrUser(li);
            this.generalQuestion(  document ,questionViewDto,questionDto,mapAll);//加载题目信息
        }
        document.close();
		
	}
	
    public void generalQuestion(Document document,QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto, Map<Integer, QuestionErrUser> mapAll) throws IOException, DocumentException {
        generalQuestion(  document,  questionViewDto,  questionDto,null,mapAll);
    }

    @Override
    public TaskPager findSelfTest(TaskPager task) {
            try {
                Long count = taskDao.searchTaskOfErrorNoteCount(task);
                task.setTotalRows(count);
                List<TreeBean> listBysearch = taskDao.searchTaskOfErrorNote(task);
                task.setResultList(listBysearch);
                return task;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
    /**
     * gen ju zuoye cha zhao cuo ti !
     * @param userId
     * @param examId
     * @return
     */
    @Override
    public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> findErrorQuestionByExamId(Integer userId, Integer examId) {
        List<Integer> questionIdList=taskDao.findErrorQids(userId,examId);
        try {
            List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> curq=questionBaseService.findQuestionByIds(questionIdList);
            List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> errorNote=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
            if(curq!=null && curq.size()>0){
                List<TpErrorNote> notes=taskDao.findErrorNoteByQids(  userId,questionIdList);
                for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto dto:curq){
                    dto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo("");
                    if(notes!=null &&notes.size()>0){
                        for(TpErrorNote te:notes){
                            if(te.getQuestionId().equals(dto.getQuestionDto().getQuestion().getId())){
                                dto.getQuestionDto().getQuestion().setErrorTimes(te.getTimes());
                                errorNote.add(dto);
                                continue;
                            }
                        }
                    }
                }
            }
            return errorNote;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * gen ju jindudian cha zhao cuo ti !
     * @return
     */
    @Override
    public QuestionFilter findErrorQuestionByJdd(QuestionFilter questionFilter) {
        List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
        try {
        int count=taskDao.searchErrorQuestionCount(questionFilter);
        questionFilter.setTotalRows(count);
        questionFilter.setPageSize(OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE);
        List<TpErrorNote> tpErrorNotes= taskDao.searchErrorQuestion(questionFilter);
        if(tpErrorNotes!=null &&tpErrorNotes.size()>0){
            for(TpErrorNote ten:tpErrorNotes){
                List<Integer> questionIdList=new ArrayList<Integer>();
                questionIdList.add(ten.getQuestionId());
                List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> curq=questionBaseService.findQuestionByIds(questionIdList);
                if(curq!=null&&curq.size()>0){
                    com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=curq.get(0);
                    questionDto.getQuestionDto().getQuestion().setErrorTimes(ten.getTimes());
                    questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo("");
                    questionDtoList.add(questionDto);
                }
            }
        }
         parseSubQuestionDefaultPoints(questionDtoList);
        questionFilter.setResultList(questionDtoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionFilter;
    }

    @Override
    public void deleteSyncQuestion() {
        List<String> deleteSqlList=new ArrayList<String>();
        deleteSqlList.add("DELETE FROM te_choiceanswer;");
        deleteSqlList.add("DELETE FROM te_choicequestion;");
        deleteSqlList.add("DELETE FROM te_complexquestion;");
        deleteSqlList.add("DELETE FROM te_correctionquestion;");
        deleteSqlList.add("DELETE FROM te_essayquestion;");
        deleteSqlList.add("DELETE FROM te_fillblank_answer;");
        deleteSqlList.add("DELETE FROM te_matrixquestion;");
        deleteSqlList.add("DELETE FROM te_multifillblankquestion;");
        deleteSqlList.add("DELETE FROM te_shortquestion;");
        deleteSqlList.add("DELETE FROM te_spokenequestion;");
        deleteSqlList.add("DELETE FROM te_whritequestion;");
        deleteSqlList.add("DELETE FROM te_questionattach;");
        deleteSqlList.add("DELETE FROM te_paper_question_detail;");
        deleteSqlList.add("DELETE FROM te_paper_structure;");
        deleteSqlList.add("DELETE FROM te_paper_sub_score;");
        deleteSqlList.add("DELETE FROM te_question;");
        deleteSqlList.add("DELETE FROM te_paper;");
        for(String sql:deleteSqlList){
            try {
                testPaperDao.deleteSyncQuestion(sql);
                Thread.sleep(1000*3);
            } catch (InterruptedException e) {
                log.error("题库清空失败");
                e.printStackTrace();
            }
        }
    }

	@Override
	public Map<String, TestPaperStructure> findStructureQuestionByPaperId(int paperId) {
		List<TestPaperStructure> questionCodeList = testPaperStructureDao.findStructureQuestionByPaperId(paperId);
		Map<String,TestPaperStructure> structMap = new HashMap<String, TestPaperStructure>();
		for(TestPaperStructure questionCode:questionCodeList){//循环试卷结构
			structMap.put(questionCode.getName(), questionCode);
        }
		return structMap;
	}
    @Override
    public Map<String,Double> findSubQuestionPoints(Integer paperId){
        Map<String,Double> subMap=new HashMap<String, Double>();
        //试卷结构子试题列表
        List<PaperSubScore> subScoreList=testPaperStructureDao.findSubQuestionPoints(paperId);
        if(subScoreList!=null &&subScoreList.size()>0){
            for(PaperSubScore pss:subScoreList){
                double score=pss.getPoints()==null?0:pss.getPoints();
                subMap.put(pss.getCode(),score);
            }
        }
        //试卷结构试题列表
        List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperId);
        if(questionCodeList!=null &&questionCodeList.size()>0){
            for(TestPaperStructure questionStructure:questionCodeList){
                double score=questionStructure.getPoints()==null?0:questionStructure.getPoints();
                subMap.put(questionStructure.getName(),score);
            }
        }
        return subMap;
    }
    /***
     * 错题本进度点：处理试题为默认分值
     * @return
     */
    private void parseSubQuestionDefaultPoints(List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList) {
        if(questionDtoList!=null && questionDtoList.size()>0){
            for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto qto:questionDtoList){
                Integer questionType =  qto.getQuestionDto().getQuestion().getQuestionTypeSX();
                //获取题型默认值
                String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(questionType);
                Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> subQuestion=qto.getSubQuestions();//子题集合
                Double allScore=subQuestion!=null&&subQuestion.size()>0?defaultScored*subQuestion.size():defaultScored;
                qto.getQuestionDto().getQuestion().getQuestionViewDto().setScore(allScore);
                if(subQuestion!=null&&subQuestion.size()>0){
                    for(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto sub:subQuestion){
                        sub.getQuestionDto().getQuestion().getQuestionViewDto().setScore(defaultScored);
                    }
                }
            }
        }
    }

    /**
     * 处理单个题
     * @param
     * @return
     */
    @Override
    public com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto parseSubQuestionPoints(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto){
        List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> list=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
        list.add(questionDto);
        parseSubQuestionPoints( list,-1,null);
        return list.get(0);
    }
    /***
     * 处理试卷中子题分值
     * @param exampId
     * @return
     */
    @Override
    public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  parseSubQuestionPoints(List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList,Integer exampId,Map<String, TpExamResultDetail> detailsMap){
        Map<String,Double> subScoreMap=null;
        if(questionDtoList!=null && questionDtoList.size()>0){
            if(exampId!=-1){//非题库
                TpExam exam=examDao.queryExamById(exampId);
                subScoreMap= findSubQuestionPoints(exam.getPaperId());
            }
            for (int i = 0; i < questionDtoList.size(); i++) {
                setsubPoint( questionDtoList.get(i) ,  subScoreMap,detailsMap);
            }
        }
        return questionDtoList;
    }
    @Override
    public void setsubPoint(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto , Map<String,Double> subScoreMap, Map<String, TpExamResultDetail> detailsMap){
        QuestionViewDto qvd=questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
        if(qvd==null) qvd=new QuestionViewDto();
        questionDto.getQuestionDto().getQuestion().setQuestionViewDto(qvd);
          Double  score=subScoreMap==null?null:subScoreMap.get(questionDto.getQuestionDto().getQuestion().getCode());
          score=score==null?1:score;//题库都设为o
          questionDto.getQuestionDto().getQuestion().setDefaultScore(score);
         qvd.setScore(score);
        //处理每个大题的子题
        QuestionViewDto questionViewDto = QuestionUtil.getSubQuestionViewDto(questionDto,
                questionDto.getQuestionDto().getQuestion().getQuestionViewDto());//组织小题的questionViewDto （小题序号和分值）
        questionDto.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
        questionViewDtoFillUserAnswer(questionViewDto, questionDto, detailsMap);
        //处理小题分值
        List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> subQuestionList=questionDto.getSubQuestions();//子题集合
        if(subQuestionList!=null &&questionDto.getSubQuestions().size()>0){
            List<QuestionViewDto> subViewList=questionViewDto.getSubDtos();//获取子题view集合
            for(int i=0;i<subQuestionList.size();i++){
                String subCode=subQuestionList.get(i).getQuestionDto().getQuestion().getCode();
                Double subScore=subScoreMap==null?null:subScoreMap.get(subCode);
                subScore=subScore==null?0:subScore;//题库都设为0
                subViewList.get(i).setScore(subScore);//设置小题题分值
                questionViewDtoFillUserAnswer(subViewList.get(i),subQuestionList.get(i),detailsMap);
            }
        }
    }
    
    public void questionViewDtoFillUserAnswer(QuestionViewDto questionViewDto, com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto iExamQuestionDto, Map<String, TpExamResultDetail> detailsMap){
    	if(detailsMap!=null){//设置子题的用户答题情况,用户做卷子的答案
        	TpExamResultDetail detail = detailsMap.get(String.valueOf(iExamQuestionDto.getQuestionDto().getQuestion().getId() ));
        	if(detail!=null){
        		questionViewDto.handlerUserResult(detail, iExamQuestionDto);
        		questionViewDto.setIsCorrect(detail.getResultAnswer());
        	}else{
        		log.info("questionid="+iExamQuestionDto.getQuestionDto().getQuestion().getId());
        	}
        }
    }
    
    /**
     * 作业讲评页面
     * 手动给大题添加小题
     */
    @Override
	public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> createPartTestPaper(List<TpExamResultDetail> teIdDetail,
			List<QuestionErrUser> resultDetail, Integer examId, Map<Integer, QuestionErrUser> map, int viewType) throws Exception {
    	int count=0;//试卷总大题s数
    	List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList = createPartTestPaperInit(teIdDetail,resultDetail, examId,map);
		for (int i = 0; i < questionDtoList.size(); i++) {
			count++;
			com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto question = questionDtoList.get(i);
            QuestionViewDto questionViewDto= question.getQuestionDto().getQuestion().getQuestionViewDto();
            if(questionViewDto==null) questionViewDto=new QuestionViewDto();
            questionViewDto.setViewType(viewType);
            question.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
            questionViewDto.setQuestionNo(count+"");
		}
		parseSubQuestionPoints(questionDtoList,examId,null);
		return questionDtoList;
	}
    
    /**
     * 作业讲评页面
     * 手动给大题添加小题
     */
    public List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> createPartTestPaperInit(List<TpExamResultDetail> teIdDetail,
			List<QuestionErrUser> resultDetail, Integer examId,Map<Integer, QuestionErrUser> map) throws Exception{
    	boolean addBig = true;//是否添加大题
		List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> questionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
		//子题临时存储集合
		List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> subQuestionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
		for (int i = teIdDetail.size()-1; i >= 0; i--) {//循环大题
			addBig = false;//是否添加大题
			TpExamResultDetail detail = teIdDetail.get(i);
			Question q = questionBaseService.getQuestionById(detail.getQuestionId());
			com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto = questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(), detail.getQuestionId());
			setQuestionErrUser(questionDto,map);
			for (int j = 0; j < resultDetail.size(); j++) {
				QuestionErrUser nDetail = resultDetail.get(j);
				System.out.println("nDetail.getTeId()==="+nDetail.getTeId()+"  detail.getQuestionId()=="+detail.getQuestionId()+"  nDetail.getQuestionId()="+nDetail.getQuestionId());
				if(nDetail.getTeId()==detail.getQuestionId().intValue()){
					q = questionBaseService.getQuestionById(nDetail.getQuestionId());
					com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto nq = questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(), nDetail.getQuestionId());
					setQuestionErrUser(nq,map);
					subQuestionDtoList.add(nq);
					addBig = true;
				}else if(nDetail.getQuestionId().intValue()==detail.getQuestionId().intValue()){
					addBig = true;
				}
			}
			if(addBig){//添加大题
				questionDto.setSubQuestions(subQuestionDtoList);//将新组装的子题添加到大题中
				questionDtoList.add(questionDto);//添加大题
				subQuestionDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
			}
		}
		return questionDtoList;
    }
    
    public void setQuestionErrUser(com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto, Map<Integer, QuestionErrUser> map){
    	if(map!=null){
    		QuestionErrUser avg = map.get(questionDto.getQuestionDto().getQuestion().getId());
        	questionDto.setQuestionErrUser(avg);
    	}
    }
    
    
    /**
     * 通过te_exam_result_detail回朔卷子
     */
    @Override
	public TestPaper createOrEditTestPaperResultDetail(PaperPager paper,UserEntity loginUser,int resultId ,int viewType) throws Exception {
    	return createTestPaper(paper, loginUser,resultId,viewType);
	}

    public TestPaper createTestPaper(PaperPager paperFilter, UserEntity loginUser, int resultId ,Integer viewType) throws Exception {
        TestPaper testPaper=null;
        testPaper=testPaperDao.findTestPaper(paperFilter.getPaperId());
        if(testPaper==null){
            //同步的试卷没有入库，但建了索引
            return null;
        }
        String joinUser=testPaper.getJoinselfUser();
        if(loginUser!=null && StringUtils.isNotBlank(joinUser)&& joinUser.indexOf("_"+loginUser.getId()+"_")>-1){
            testPaper.setJoin(true);
        }
        if(StringUtils.isNotBlank(paperFilter.getTitle())) testPaper.setPaperName(paperFilter.getTitle());
        Map<String,Tags> questionTypeMap= KlbTagsUtil.getInstance().findQuestionTypeMap(testPaper.getSubjectId(),testPaper.getRangeId());
        //编辑是封装数据
        int count=0;//试卷总大题s数
        //试卷结构试题列表(查询结果明细表题目信息)
        List<TestPaperStructure> questionCodeList= examResultDetailDao.findExamResultDetail(resultId);
//        List<TestPaperStructure> questionCodeList=testPaperStructureDao.findStructureQuestionByPaperId(paperFilter.getPaperId());
        //试卷试题题型列表(查询结果明细结构表题目信息)
        List<TestPaperStructure> questionTypeList= examResultDetailDao.findExamResultStructure(resultId);
//        List<TestPaperStructure> questionTypeList=testPaperStructureDao.findStructureQuestionTypeByPaperId(paperFilter.getPaperId());
        Map<String,Double> subScoreMap= findSubQuestionPoints(paperFilter.getPaperId());
        List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
        for(TestPaperStructure tps:questionTypeList){
            Tags qtype=questionTypeMap.get(tps.getName());
            if(qtype==null){
                qtype=new Tags();
                qtype.setName(tps.getName()+QUESTION_TYPE_ILLEGAL);
                qtype.setId(9999);
                log.info(testPaper.getSubject() +testPaper.getRange()+"试卷展示,不存在的题型【"+tps.getName()+"】");
//                    continue;
            }
            //迭代题型
            PaperQuestionType paperQuestionType=new PaperQuestionType();
            paperQuestionType.setQuestionTypeName(qtype.getName());
            paperQuestionType.setQuestionType(qtype.getId());
            QuestionBarType qbt=new QuestionBarType();qbt.setName(qtype.getName());qbt.setType(qtype.getId());
            if(tps.getPoints()!=null ||tps.getPoints()>0){
                //试卷题目有分值
                qbt.setDefaultScore(tps.getPoints());
            }else{
                //获取题型默认值
                String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qtype.getId());
                Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                qbt.setDefaultScore(defaultScored);
            }
            List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>  questionDtoList=new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();//获取题目
            for(TestPaperStructure questionCode:questionCodeList){
                if(tps.getId()==questionCode.getParent()){
                    //封装每个题型下的试题（te_test_paper_structure 关联的全是大题）
//                    com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=questionBaseService.findQuestionByCode(questionCode.getName());
                    com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto questionDto=questionBaseService.findQuestionById(questionCode.getQuestionId());
                    if(questionDto==null){
                       continue;
                    }
                    count++;//大题数加一
                    questionDtoList.add(questionDto);
                    Integer questionId=questionDto.getQuestionDto().getQuestion().getId();
                    //3)试题栏封装每个题型下的大题id
                    qbt.getExamIdArr().add(questionId);
                    QuestionViewDto questionViewDto= questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
                    if(questionViewDto==null) questionViewDto=new QuestionViewDto();
                    if(viewType!=null){//不同页面模板显示样式不同
                    	questionViewDto.setViewType(viewType);
                    }
                    questionDto.getQuestionDto().getQuestion().setQuestionViewDto(questionViewDto);
//                        questionViewDtoFillUserAnswer(questionViewDto, questionDto, paperFilter.getDetailsMap());
                    questionViewDto.setScore(questionCode.getPoints());//设置大题分值
                    questionViewDto.setQuestionNo(count+"");
                    //处理小题分值
                    setsubPoint(questionDto, subScoreMap, paperFilter.getDetailsMap());
                }
            }
            paperQuestionType.setQuestionDtoList(questionDtoList);
            paperQuestionTypeList.add(paperQuestionType);
        }
        testPaper.setPaperQuestionTypeList(paperQuestionTypeList);
        return testPaper;
    }
    @Override
    public void rebuildUpdate(String xiaoxiao) {
        taskDao.rebuildUpdate(xiaoxiao);
    }

    @Override
    public List<Knowledge> tongjiResourceNumByKnowledge(final Map<String, Boolean> paramMap) {
        final boolean loadKonwledge=paramMap.get(GlobalConstant.loadKonwledge);
        CacheTools.delCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM+loadKonwledge);
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                CacheTools.addCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish+loadKonwledge,false);
                Map<String,List<Knowledge>>  subjectTJ=new HashMap<String, List<Knowledge>>();//按学科存放统计，没一个学科对应一个sheet
                List<Dictionary> subjectList=DataDictionaryUtil.getInstance().getDataDictionaryListByTypeOrder(1);//获取学科
                if(subjectList!=null&subjectList.size()>0){
                    for(Dictionary dictionary:subjectList){
                        List<Knowledge> excelDtoList=new ArrayList<Knowledge>();//excel一条记录实体
                        List<Tags> nextTagsList=new ArrayList<Tags>();
                        nextTagsList.add(toTags(dictionary));
                        excelDtoList=diguiByParentId( nextTagsList,0 ,  excelDtoList,loadKonwledge );
                        subjectTJ.put(dictionary.getName(),excelDtoList);
                        CacheTools.addCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM + loadKonwledge, subjectTJ);
                    }
                }
                CacheTools.addCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM+loadKonwledge,subjectTJ);
                CacheTools.addCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish+loadKonwledge,true);
            }
        });
            return null;
    }
    private List<Knowledge> diguiByParentId( List<Tags> nextTagsList,int index ,List<Knowledge> excelDtoList,boolean loadKonwledge){
        String except="知识点" ;//排除的节点名称
        if(loadKonwledge) except="教材目录";
        if(nextTagsList==null || nextTagsList.size()<1 ) return excelDtoList;
        //当前迭代的数组下标
        for(Tags tag: nextTagsList){
            if(tag.getName().contains(except)  ){
                continue  ;   //跳过 知识点 或教材目录 节点
            }
            if(index>1){
                try {
                    SearchResourceBean bean=new SearchResourceBean();
                    bean.setTagId(tag.getId());bean.setSource(GlobalConstant.RESOURCE_SOURCE_PUBLIC);
                    Thread.sleep(100);
                    Long count = resourceInfoService. searchResourceCount(bean);
                    tag.setName(tag.getName() + "【count:" + count + "】");
                    System.out.println(tag.getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Knowledge know=getKnowledge(tag ,index);
            excelDtoList.add(know);//下载数据集合
            nextTagsList=KlbTagsUtil.getInstance().getTagById(tag.getId());//获取当前节点的子节点
            diguiByParentId(nextTagsList, index + 1, excelDtoList, loadKonwledge);
        }
        return excelDtoList;
    }

    private   Knowledge getKnowledge(Tags tag,int i) {
        String tagName=HtmlCssUtil.parseEntityCharacter(tag.getName());
        Knowledge k=new Knowledge();
        k.setTag0(tag.getId()+"");
        if(i==0) k.setTag1(tagName); //i=0  说明这是学科 数组中的元素
        if(i==1) k.setTag2(tagName);
        if(i==2) k.setTag3(tagName);
        if(i==3) k.setTag4(tagName);
        if(i==4) k.setTag5(tagName);
        if(i==5) k.setTag6(tagName);
        if(i==6) k.setTag7(tagName);
        if(i==7) k.setTag8(tagName);
        if(i==8) k.setTag9(tagName);
        if(i==9) k.setTag10(tagName);
        if(i==10) k.setTag11(tagName);
        if(i==11) k.setTag12(tagName);
        if(i>5&&StringUtils.isNotBlank(tag.getFull_Path())&&tag.getFull_Path().endsWith("_2465_60")){
            //数学、四级知识点以后名称有问题不处理
            k.setTag7("oo");
            k.setTag8("oo");
            k.setTag9("oo");
            k.setTag10("oo");
            k.setTag11("oo");
            k.setTag12("oo");
        }
        return k;
    }
    private Tags toTags(Dictionary dictionary) {
        Tags t=new Tags();
        t.setId(dictionary.getId());
        t.setName(dictionary.getName());
        return t;
    }
}
