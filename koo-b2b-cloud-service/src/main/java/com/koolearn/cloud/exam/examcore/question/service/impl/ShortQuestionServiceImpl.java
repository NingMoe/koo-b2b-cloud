package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.ShortQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.ShortQuestion;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.question.service.ShortQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


public class ShortQuestionServiceImpl implements ShortQuestionService {
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private ShortQuestionDao shortQuestionDao;
	
	@Autowired
	private QuestionAttachDao questionattachDao;
	
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

    public ShortQuestionDao getShortQuestionDao() {
        return shortQuestionDao;
    }

    public void setShortQuestionDao(ShortQuestionDao shortQuestionDao) {
        this.shortQuestionDao = shortQuestionDao;
    }

    public QuestionAttachDao getQuestionattachDao() {
        return questionattachDao;
    }

    public void setQuestionattachDao(QuestionAttachDao questionattachDao) {
        this.questionattachDao = questionattachDao;
    }

    public String getShortQuestionKey() {
        return shortQuestionKey;
    }

    public void setShortQuestionKey(String shortQuestionKey) {
        this.shortQuestionKey = shortQuestionKey;
    }

    private String shortQuestionKey = "shortQuestion_";
	
	/**
	 * 创建简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertShortQuestion(ShortQuestionDto dto) throws Exception{
		Connection conn = null;
		int questionId = 0;
		try{
			conn = ConnUtil.getTransactionConnection();
//			Question question = dto.getQuestion();
//			QuestionDto questionDto = new QuestionDto();
//			questionDto.setQuestion(question);
//			questionDto.setQuestionAttachs(dto.getQuestionAttachs());
//			questionDto.setSaveType(dto.getSaveType());
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	

			ShortQuestion shortQuestion = dto.getShortQuestion();
			shortQuestion.setQuestionId(questionId);
			//保存简答题信息
			shortQuestionDao.insert(conn,shortQuestion);
			
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
	 * @param questionId
	 * @return
	 */
	public ShortQuestionDto getShortQuestionDto(int questionId){
		ShortQuestionDto dto = this.getShortDto(questionId);
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		dto.setQuestionDto(questionDto);
		return dto;
	}	
	/**
	 * 根据questionId获取shortQuestion信息
	 * TODO
	 * @param questionId
	 * @return
	 */
	private ShortQuestionDto getShortDto(int questionId){
		ShortQuestionDto dto = new ShortQuestionDto();
		ShortQuestion shortQuestion = shortQuestionDao.getByQuestionid(questionId);
		if(shortQuestion != null){			
			shortQuestion.setQuestionId(questionId);
			dto.setShortQuestion(shortQuestion);
			List<String> keyWordList = new ArrayList<String>(); 
			if(shortQuestion.getMarktype() == ConstantTe.MARK_TYPE_SYSTEM && StringUtils.isNotBlank(shortQuestion.getScorestandad())){
				String keyWords = shortQuestion.getScorestandad();
				String[] keyWordsList = keyWords.split(";");
				for(int i=0;i<keyWordsList.length;i++){
					if(StringUtils.isNotBlank(keyWordsList[i])){
						keyWordList.add(keyWordsList[i]);
					}
				}
				shortQuestion.setKeyWordList(keyWordList);
			}
		}

		return dto;
	}
	
	public void updateShortQuestion(Connection conn,ShortQuestionDto dto) throws Exception{
		ShortQuestion shortQuestion = dto.getShortQuestion();
		//修改简答题信息
		shortQuestionDao.updateShortQuestion(conn, shortQuestion);
		//修改基本题目信息
		questionService.saveOrUpate(conn, dto.getQuestionDto());	
	}
	/**
	 * 修改简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public void updateShortQuestion(ShortQuestionDto dto) throws Exception{
		Connection conn = null;
		try{
			conn = ConnUtil.getTransactionConnection();
			Question question = dto.getQuestionDto().getQuestion();
			int oldId = question.getId();
			updateShortQuestion(conn,dto);
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
	public void saveAs(Connection conn,int questionId,int new_id) throws Exception{
		List<ShortQuestion> shortList = shortQuestionDao.batchFindByTeId(conn,questionId);
		for(ShortQuestion shortQuestion:shortList){
			ShortQuestionDto dto = this.toDto(shortQuestion);
			dto.getQuestionDto().getQuestion().setTeId(new_id);
			dto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
			this.insertShortQuestion(conn,dto);
		}
	}
	
	/**
	 * 创建简答题
	 * TODO
	 * @param dto
	 * @throws Exception
	 */
	public int insertShortQuestion(Connection conn,ShortQuestionDto dto) throws Exception{	
		int questionId = 0;
		try{		
			questionId = questionService.saveOrUpate(conn, dto.getQuestionDto());	

			ShortQuestion shortQuestion = dto.getShortQuestion();
			shortQuestion.setQuestionId(questionId);
			//保存简答题信息
			shortQuestionDao.insert(conn,shortQuestion);
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
	private ShortQuestionDto toDto(ShortQuestion shortQuestion){		
		ShortQuestionDto dto = new ShortQuestionDto();
		dto.setShortQuestion(shortQuestion);
		QuestionDto questionDto = questionService.getQuestionDtoByQuestionId(shortQuestion.getQuestionId());
		dto.setQuestionDto(questionDto);
		return dto;
	}
	/**
	 * 根据id集合获取简答题的list列表信息
	 * TODO
	 * @param ids
	 * @return
	 */
	public List<ShortQuestionDto> getShortQuestionListRepository( List<Integer> ids){
		List<ShortQuestionDto> dtoList = new ArrayList<ShortQuestionDto>();
		for(int id : ids){
			ShortQuestionDto dto = this.getShortQuestionRepository(id);
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
	public ShortQuestionDto getShortQuestionRepository(int id){
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
		Question question = new Question();
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
		return attachList;
	}
	
	/**
	 * 批量删除简答题
	 * TODO
	 * @param conn
	 * @param ids
	 */
	@Override
	public void deleteByIds(Connection conn, List<Integer> ids) {
		shortQuestionDao.deleteByIds(conn, ids);
		for(Integer id:ids){
			questionBaseService.delCommQuestion(conn, id);
		}
	}
	
	@Override
	public void saveAs(Connection conn, Question question, int new_id) throws Exception {
		ShortQuestion shortQuestion = shortQuestionDao.getByQuestionid(question.getId());
		ShortQuestionDto dto = this.toDto(shortQuestion);
		dto.getQuestionDto().getQuestion().setTeId(new_id);
		dto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
		this.insertShortQuestion(conn,dto);
		
	}
	public void saveOrUpdate(Connection conn, ShortQuestionDto dto)
			throws Exception {
		int id=dto.getQuestionDto().getQuestion().getId();
		int saveType=dto.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			insertShortQuestion(conn, dto);
		}else if(saveType==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			insertShortQuestion(conn, dto);
		}else{//更新
			updateShortQuestion(conn,dto);
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
