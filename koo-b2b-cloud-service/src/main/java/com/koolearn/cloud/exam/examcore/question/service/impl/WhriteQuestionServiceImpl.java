package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.WhriteQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.WhriteQuestion;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.question.service.WhriteQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.ExamException;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WhriteQuestionServiceImpl implements WhriteQuestionService {
	private Log logger = LogFactory.getLog(WhriteQuestionServiceImpl.class);
	@Autowired
	private WhriteQuestionDao whriteQuestionDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private QuestionAttachDao questionAttachDao;
	
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseService questionBaseService;

    public WhriteQuestionDao getWhriteQuestionDao() {
        return whriteQuestionDao;
    }

    public void setWhriteQuestionDao(WhriteQuestionDao whriteQuestionDao) {
        this.whriteQuestionDao = whriteQuestionDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    private String whriteQuestionKey = "whriteQuestion_";
	
	/**
	 * 创建写作题
	 * TODO
	 * @param dto
	 * @throws ExamException
	 */
	public int insertWhriteQuestion(WhriteQuestionDto dto) throws ExamException {
		Connection conn = null;
		int questionId = 0;
		try{
			conn = ConnUtil.getTransactionConnection();
//			Question question = dto.getQuestionDto().getQuestion();
//			QuestionDto questionDto = new QuestionDto();
//			questionDto.setQuestion(question);
//			questionDto.setQuestionAttachs(dto.getQuestionDto().getQuestionAttachs());
//			questionDto.setSaveType(dto.getQuestionDto().getSaveType());
			//questionDto.setKnowledge_switch(dto.isKnowledge_switch());
			questionId = questionService.saveOrUpate(conn,  dto.getQuestionDto());	
			
			WhriteQuestion WhriteQuestion = dto.getWhriteQuestion();
			WhriteQuestion.setQuestionId(questionId);
			//保存写作题信息
			whriteQuestionDao.insert(conn,WhriteQuestion);
					
			conn.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn!=null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new ExamException(e1);
			}
			throw new ExamException(e);
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return questionId;
		
	}
	/**
	 * 根据questionId获取试题的所有信息
	 * TODO
	 * @param id
	 * @return
	 */
	public WhriteQuestionDto getWhriteQuestionDto(int questionId){
		WhriteQuestionDto dto = this.getWhriteDto(questionId);
		
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		dto.setQuestionDto(questionDto);
		
		return dto;
	}
	/**
	 * 根据questionId获取试题WhriteQuestion的基本信息
	 * TODO
	 * @param questionId
	 * @return
	 */
	private WhriteQuestionDto getWhriteDto(int questionId){
		WhriteQuestionDto dto = new WhriteQuestionDto();
		WhriteQuestion whriteQuestion = whriteQuestionDao.getByQuestionid(questionId);		
		if(whriteQuestion != null){	
			dto.setWhriteQuestion(whriteQuestion);
		}
		return dto;
	}
	/**
	 * 修改写作题
	 * TODO
	 * @param dto
	 * @throws ExamException
	 */
	public void updateWhriteQuestion(Connection conn,WhriteQuestionDto dto) throws Exception{
		
		WhriteQuestion whriteQuestion = dto.getWhriteQuestion();
		//修改简答题信息
		whriteQuestionDao.updateWhriteQuestion(conn, whriteQuestion);
		//修改基本题目信息
		questionService.saveOrUpate(conn, dto.getQuestionDto());
		
	}
	
	/**
	 * 修改简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public void updateWhriteQuestion(WhriteQuestionDto dto) throws Exception{
		Connection conn = null;
		try{
			conn = ConnUtil.getTransactionConnection();
			Question question = dto.getQuestionDto().getQuestion();
			int oldId = question.getId();
			updateWhriteQuestion(conn,dto);
			conn.commit();

			//删除缓存

            CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + oldId);
			
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn!=null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new Exception(e1);
			}
			throw new Exception(e);
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 子题目的另存为过程
	 * TODO
	 * @param conn
	 * @param questionId
	 * @param new_id
	 * @throws ExamException 
	 */
	public void saveAs(Connection conn,Question question,int new_id) throws ExamException{
//		List<WhriteQuestion> whriteList = whritequestionDao.batchFindByTeId(conn,questionId);
//		for(WhriteQuestion whriteQuestion:whriteList){
//			WhriteQuestionDto dto = this.toDto(whriteQuestion);
//			dto.getQuestion().setTeId(new_id);
//			dto.setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
//			this.insertWhriteQuestion(conn,dto);
//		}
		WhriteQuestion whriteQuestion =whriteQuestionDao.getByQuestionid(question.getId());
		WhriteQuestionDto dto = this.toDto(whriteQuestion);
		dto.getQuestionDto().getQuestion().setTeId(new_id);
		dto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
		dto.getQuestionDto().getQuestion().setId(0);
		this.insertWhriteQuestion(conn,dto);
	}
	
	
	
	/**
	 * 创建写作题
	 * TODO
	 * @param dto
	 * @throws ExamException
	 */
	public int insertWhriteQuestion(Connection conn,WhriteQuestionDto dto) throws ExamException{
		int questionId = 0;
		try{
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	
			
			WhriteQuestion WhriteQuestion = dto.getWhriteQuestion();
			WhriteQuestion.setQuestionId(questionId);
			//保存写作题信息
			whriteQuestionDao.insert(conn,WhriteQuestion);

		}catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new ExamException(e1);
			}
			throw new ExamException(e);
		}
		return questionId;
		
	}
	/**
	 * 实体转换dto       
	 * TODO
	 * @param id
	 * @return
	 */
	private WhriteQuestionDto toDto(WhriteQuestion whriteQuestion){		
		WhriteQuestionDto dto = new WhriteQuestionDto();
		dto.setWhriteQuestion(whriteQuestion);
		QuestionDto questionDto = questionService.getQuestionDtoByQuestionId(whriteQuestion.getQuestionId());
		dto.setQuestionDto(questionDto);
		return dto;
	}
	/**
	 * 根据id集合获取简答题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<WhriteQuestionDto> getWhriteQuestionListRepository( List<Integer> ids){
		List<WhriteQuestionDto> dtoList = new ArrayList<WhriteQuestionDto>();
		for(int id : ids){
			WhriteQuestionDto dto = this.getWhriteQuestionRepository(id);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	/**
	 * 根据题目id获取写作题的所有信息（从缓存获取）
	 * TODO
	 * @param id
	 * @return
	 */
	public WhriteQuestionDto getWhriteQuestionRepository(int id){
		WhriteQuestionDto dto = new WhriteQuestionDto();
		String key = whriteQuestionKey +id;
		try {
			//dto = whriteQuestionRepository.getCache(key);
			//if(dto == null || dto.getQuestionDto().getQuestion().getId() == 0){
				dto = this.getWhriteDto(id);				
				QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(id);
				dto.setQuestionDto(questionDto);
				
				//whriteQuestionRepository.deleteCache(key);
				//whriteQuestionRepository.setCache(key, dto);
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
	}
	/**
	 * 从缓存中获取单个题目
	 * TODO
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Question getQuestionRespository(int id) throws Exception{
		Question question = new Question();
		List<Integer> questionIds = new ArrayList<Integer>();
		questionIds.add(id);
		List<Question> questions = null;//RepositoryManager.getInstance().getQuestionRepository().getQuestionsAndFetch(questionIds);
		if(!questions.isEmpty() && questions.size() > 0){					
			question = questions.get(0);
		}
		return question;
	}
	/**
	 * 从缓存中获取题目材料
	 * TODO
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private List<QuestionAttach> getQuestionAttachRespository(int id) throws Exception{
		List<QuestionAttach> attachList = new ArrayList<QuestionAttach>();
		List<Integer> questionIds = new ArrayList<Integer>();
		questionIds.add(id);
		List<List<QuestionAttach>> attachLists = null;//RepositoryManager.getInstance().getQuestionAttachRepository().getQuestionAttachAndFetch(questionIds);
//		if(!attachLists.isEmpty() && attachLists.size() > 0){					
//			attachList  = attachLists.get(0);
//		}
		return attachList;
	}
		
	
	
	/**
	 * 批量删除写作题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	@Override
	public void deleteByIds(Connection conn, List<Integer> ids) {
		whriteQuestionDao.deleteByIds(conn, ids);
		for(Integer id:ids){
			questionBaseService.delCommQuestion(conn, id);
		}
	}
	
	/**
	 * 保存更新写作题
	 */
	public void saveOrUpdate(WhriteQuestionDto questionDto) throws Exception{
		Connection conn = null;
		int questionId=0;
		try{
			conn = ConnUtil.getTransactionConnection();
			saveOrUpdate(conn, questionDto);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
			try {
				if(conn!=null){
					conn.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.fillInStackTrace();
				}
			}
		}
		
	}
	/**
	 * 保存更新写作题
	 */
	public void saveOrUpdate(Connection conn, WhriteQuestionDto questionDto) throws Exception{
		//判断是否为新题 新题直接保存
		Question question=questionDto.getQuestionDto().getQuestion();
		int id=question.getId();
		int saveType=questionDto.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			insertWhriteQuestion(conn, questionDto);
		}else if(saveType==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			insertWhriteQuestion(conn, questionDto);
			
		}else{//更新
			//修改写作题信息
			whriteQuestionDao.updateWhriteQuestion(conn, questionDto.getWhriteQuestion());
			//questionDto.setKnowledge_switch(dto.isKnowledge_switch());
			questionService.saveOrUpate(conn, questionDto.getQuestionDto());	
		}
		
	}
	
}
