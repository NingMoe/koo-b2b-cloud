package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.SpokenQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.SpokenQuestion;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.question.service.SpokenQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class SpokenQuestionServiceImpl implements SpokenQuestionService {
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private SpokenQuestionDao spokenQuestionDao;
	
	@Autowired
	private QuestionAttachDao questionAttachDao;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionBaseService questionBaseService;

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public SpokenQuestionDao getSpokenQuestionDao() {
        return spokenQuestionDao;
    }

    public void setSpokenQuestionDao(SpokenQuestionDao spokenQuestionDao) {
        this.spokenQuestionDao = spokenQuestionDao;
    }

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    /**
	 * 创建口语题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertSpokenQuestion(SpokenQuestionDto dto) throws Exception{
		Connection conn = null;
		int questionId = 0;
		try{
			conn = ConnUtil.getTransactionConnection();
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	
			
			SpokenQuestion spokenQuestion = dto.getSpokenQuestion();
			spokenQuestion.setQuestionId(questionId);
			//保存口语题信息
			spokenQuestionDao.insert(conn,spokenQuestion);
			conn.commit();
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
		return questionId;
		
	}
	/**
	 * 根据questionId获取试题的所有信息
	 * TODO
	 * @param id
	 * @return
	 */
	public SpokenQuestionDto getSpokenQuestionDto(int questionId){
		SpokenQuestionDto dto = this.getSpokenDto(questionId);
		
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		dto.setQuestionDto(questionDto);
		return dto;
	}
	/**
	 * 根据questionId获取spoken的基本属性
	 * TODO
	 * @param questionId
	 * @return
	 */
	private SpokenQuestionDto getSpokenDto(int questionId){
		SpokenQuestionDto dto = new SpokenQuestionDto(); 
		SpokenQuestion spokenQuestion = spokenQuestionDao.getByQuestionid(questionId);		
		if(spokenQuestion != null){
			dto.setSpokenQuestion(spokenQuestion);
		}
		return dto;
	}
	/**
	 * 同步保存口语题
	 */
	public void saveOrUpdate(Connection conn,SpokenQuestionDto dto) throws Exception{
		//判断是否为新题 新题直接保存
		Question question=dto.getQuestionDto().getQuestion();
		int questionId=question.getId();
		int saveType=dto.getQuestionDto().getSaveType();
		if(questionId==0){//保存
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	
			SpokenQuestion spokenQuestion = dto.getSpokenQuestion();
			spokenQuestion.setQuestionId(questionId);
			//保存口语题信息
			spokenQuestionDao.insert(conn,spokenQuestion);
		}else if(saveType== ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			int new_id=questionService.saveOrUpate(conn, dto.getQuestionDto());
			SpokenQuestion spokenQuestion = dto.getSpokenQuestion();
			spokenQuestion.setQuestionId(new_id);
			//保存口语题信息
			spokenQuestionDao.insert(conn,spokenQuestion);
		}else{//更新
			//修改口语题信息
			spokenQuestionDao.updateSpokenQuestion(conn, dto.getSpokenQuestion());
			//修改基本题目信息
			questionService.saveOrUpate(conn, dto.getQuestionDto());
		}
		
	}
	/**
	 * 修改口语题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public void updateSpokenQuestion(SpokenQuestionDto dto) throws Exception{
		Connection conn = null;
		try{
			conn = ConnUtil.getTransactionConnection();
			int oldId = dto.getQuestionDto().getQuestion().getId();
			//修改口语题信息
			spokenQuestionDao.updateSpokenQuestion(conn, dto.getSpokenQuestion());
			//修改基本题目信息
			questionService.saveOrUpate(conn, dto.getQuestionDto());				
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
	 * @throws Exception 
	 */
	public void saveAs(Connection conn,Question question,int new_id) throws Exception{
//		List<SpokenQuestion> spokenList = spokenQuestionDao.batchFindByTeId(conn,questionId);
//		for(SpokenQuestion spokenQuestion:spokenList){
//			SpokenQuestionDto dto = this.toDto(spokenQuestion);
//			dto.getQuestion().setTeId(new_id);
//			dto.setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
//			this.insertSpokenQuestion(conn,dto);
//		}
		
		SpokenQuestion spokenQuestion =spokenQuestionDao.getByQuestionid(question.getId());
		SpokenQuestionDto dto = this.toDto(spokenQuestion);
		dto.getQuestionDto().getQuestion().setTeId(new_id);
		dto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
		dto.getQuestionDto().getQuestion().setId(0);
		this.insertSpokenQuestion(conn,dto);
	}
	/**
	 * 创建口语题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertSpokenQuestion(Connection conn,SpokenQuestionDto dto) throws Exception{
		int questionId = 0;
		try{
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	
			
			//保存口语题信息
			spokenQuestionDao.insert(conn,dto.getSpokenQuestion());

		}catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new Exception(e1);
			}
			throw new Exception(e);
		}
		return questionId;
		
	}
	/**
	 * 实体转换dto
	 * TODO
	 * @param id
	 * @return
	 */
	private SpokenQuestionDto toDto(SpokenQuestion spokenQuestion){		
		SpokenQuestionDto dto = new SpokenQuestionDto();
		dto.setSpokenQuestion(spokenQuestion);
		Question question = questionDao.getQuestionById(spokenQuestion.getQuestionId());
		dto.getQuestionDto().setQuestion(question);
		List<QuestionAttach> attachList = questionAttachDao.getByQuestionid(spokenQuestion.getQuestionId());
		dto.getQuestionDto().setQuestionAttachs(attachList);
		return dto;
	}
	/**
	 * 根据id集合获取写作题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<SpokenQuestionDto> getSpokenQuestionListRepository( List<Integer> ids){
		List<SpokenQuestionDto> dtoList = new ArrayList<SpokenQuestionDto>();
		for(int id : ids){
			SpokenQuestionDto dto = this.getSpokenQuestionRepository(id);
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
	public SpokenQuestionDto getSpokenQuestionRepository(int id){
		return null;
	}	
	/**
	 * 从缓存中获取单个题目
	 * TODO
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Question getQuestionRespository(int id) throws Exception{
		return null;
	}
	/**
	 * 从缓存中获取题目材料
	 * TODO
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private List<QuestionAttach> getQuestionAttachRespository(int id) throws Exception{
		return null;
	}
	
	
	/**
	 * 批量删除口语题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	@Override
	public void deleteByIds(Connection conn, List<Integer> ids) {
		spokenQuestionDao.deleteByIds(conn, ids);
		for(Integer id:ids){
			questionBaseService.delCommQuestion(conn, id);
		}
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
	
}
