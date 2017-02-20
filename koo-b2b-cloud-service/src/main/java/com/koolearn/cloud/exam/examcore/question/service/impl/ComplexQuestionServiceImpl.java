package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dao.*;
import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.*;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.ComplexQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.EssayQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.koolearn.framework.aries.util.ConnectionUtils;

public class ComplexQuestionServiceImpl implements ComplexQuestionService {

	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private ComplexQuestionDao complexQuestionDao;
	
	@Autowired
	private QuestionAttachDao questionAttachDao;
	
	@Autowired
	private ChoiceQuestionDao choiceQuestionDao;
	
	@Autowired
	private ChoiceAnswerDao choiceAnswerDao;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private EssayQuestionDAO essayQuestionDAO;
	
	@Autowired
	private FillblankAnswerDAO fillblankAnswerDAO;
	
	@Autowired
	private ChoiceQuestionService choiceQuestionService;
	
	@Autowired
	private EssayQuestionService essayQuestionService;

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public ComplexQuestionDao getComplexQuestionDao() {
        return complexQuestionDao;
    }

    public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
        this.complexQuestionDao = complexQuestionDao;
    }

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    public ChoiceQuestionDao getChoiceQuestionDao() {
        return choiceQuestionDao;
    }

    public void setChoiceQuestionDao(ChoiceQuestionDao choiceQuestionDao) {
        this.choiceQuestionDao = choiceQuestionDao;
    }

    public ChoiceAnswerDao getChoiceAnswerDao() {
        return choiceAnswerDao;
    }

    public void setChoiceAnswerDao(ChoiceAnswerDao choiceAnswerDao) {
        this.choiceAnswerDao = choiceAnswerDao;
    }

    public EssayQuestionDAO getEssayQuestionDAO() {
        return essayQuestionDAO;
    }

    public void setEssayQuestionDAO(EssayQuestionDAO essayQuestionDAO) {
        this.essayQuestionDAO = essayQuestionDAO;
    }

    public FillblankAnswerDAO getFillblankAnswerDAO() {
        return fillblankAnswerDAO;
    }

    public void setFillblankAnswerDAO(FillblankAnswerDAO fillblankAnswerDAO) {
        this.fillblankAnswerDAO = fillblankAnswerDAO;
    }

    public ChoiceQuestionService getChoiceQuestionService() {
        return choiceQuestionService;
    }

    public void setChoiceQuestionService(ChoiceQuestionService choiceQuestionService) {
        this.choiceQuestionService = choiceQuestionService;
    }

    public EssayQuestionService getEssayQuestionService() {
        return essayQuestionService;
    }

    public void setEssayQuestionService(EssayQuestionService essayQuestionService) {
        this.essayQuestionService = essayQuestionService;
    }

    /**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public ComplexQuestionDto getById(int id) throws Exception {
		return getComplexById(id,false);
	}
	
	/**
	 * 
	 * @param questionId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ComplexQuestionDto getByQuestionId(int questionId) {
		return getComplexById(questionId,true);
	}

	/**
	 * 根据完形填空题ID或基础题目ID查询
	 * @param id
	 * @return
	 */
	private ComplexQuestionDto getComplexById(int id,boolean isBaseId){
		ComplexQuestionDto complexQuestionDto = new ComplexQuestionDto();
		ComplexQuestion complexQuestion = null;
		if(isBaseId){
			complexQuestion = complexQuestionDao.getByQuestionId(id);
		}else{
			complexQuestion = complexQuestionDao.getById(id);
		}
		if(null != complexQuestion){
			QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(complexQuestion.getQuestionId());
			complexQuestionDto.setComplexQuestion(complexQuestion);
			complexQuestionDto.setQuestionDto(questionDto);
			//选择型完型
			int questionType = questionDto.getQuestion().getQuestionTypeId();
			
			if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_BLANK
					|| questionType == Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD){
				
				List<ChoiceQuestionDto> choiceQuestionDtos = new ArrayList<ChoiceQuestionDto>();
				List<ChoiceQuestion> choiceQuestions = new ArrayList<ChoiceQuestion>();
				choiceQuestions = questionDao.getSubChoiceByQuestionid(complexQuestion.getQuestionId());
				if(null != choiceQuestions){
					for(ChoiceQuestion choiceQuestion : choiceQuestions){
						ChoiceQuestionDto choiceQuestionDto = new ChoiceQuestionDto();
						choiceQuestionDto.setChoiceQuestion(choiceQuestion);
						List<ChoiceAnswer> choiceAnswers = choiceAnswerDao.getByChoiceId(choiceQuestion.getId());
						choiceQuestionDto.setChoiceAnswers(choiceAnswers);
						Question subquestion = questionDao.getQuestionById(choiceQuestion.getQuestionId());
						QuestionDto subquestionDto = new QuestionDto();
						subquestionDto.setQuestion(subquestion);
						choiceQuestionDto.setQuestionDto(subquestionDto);
						choiceQuestionDtos.add(choiceQuestionDto);
					}
					complexQuestionDto.setChoiceQuestionDtos(choiceQuestionDtos);
				}
			}
			//填空型完型和复合听写
			else{
				List<EssayQuestionDto> essayQuestionDTOs = new ArrayList<EssayQuestionDto>();
				List<EssayQuestion> essayQuestions = new ArrayList<EssayQuestion>();
				essayQuestions = questionDao.getSubEssayByQuestionid(complexQuestion.getQuestionId());
				if(null != essayQuestions){
					for(EssayQuestion essayQuestion : essayQuestions){
						EssayQuestionDto essayQuestionDTO = new EssayQuestionDto();
						essayQuestionDTO.setEssayQuestion(essayQuestion);
						Question eq = questionDao.getQuestionById(essayQuestion.getQuesttionId());
						QuestionDto eqDto = new QuestionDto();
						eqDto.setQuestion(eq);
						essayQuestionDTO.setQuestionDto(eqDto);
						essayQuestionDTO.setEssayQuestion(essayQuestion);
						List<FillblankAnswer> fillblankAnswers = fillblankAnswerDAO.getFillblankAnswersById(essayQuestion.getId());
						essayQuestionDTO.setFillblankAnswers(fillblankAnswers);
						essayQuestionDTOs.add(essayQuestionDTO);
					}
				}
				complexQuestionDto.setEssayQuestionDTOs(essayQuestionDTOs);
			}
			
		}
		return complexQuestionDto;
	}
	
	/**
	 * 删除
	 * @param questionId 基础题目ID
	 * @throws Exception
	 */
	@Override
	public void deleteByQuestionId(int questionId) throws Exception {
		Connection conn = null;
		try{
			conn = ConnUtil.getTransactionConnection();
			complexQuestionDao.deleteByQuestionId(questionId);
			questionDao.deleteQuestion(questionId);
			
			conn.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
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
	 * 保存选择题,如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceQuestion
	 * @param code 编码
	 * @param parentId 父题目ID
	 * @param status 题目保存状态
	 * @param atomic 是否为子试题
	 */
	private void saveChoiceQuestion(Connection conn,ComplexQuestionDto complexQuestionDto,String code,int parentId,int status,int questionType) throws Exception{
		List<ChoiceQuestionDto> choiceQuestionDtos = complexQuestionDto.getChoiceQuestionDtos();
		if(null != choiceQuestionDtos && choiceQuestionDtos.size() > 0){
			//如果修改，先删除
			if(status == ConstantTe.QUESTION_SAVETYPE_UPDATE){
				List<Integer> cids = choiceQuestionDao.findByTeId(parentId);
				choiceAnswerDao.deleteByChoiceIds(conn, cids);
				choiceQuestionDao.deleteByIds(conn, cids);
				questionDao.deleteQuestionByTeid(conn, parentId);
			}
			int composeType = 0;
			if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK){
				composeType = choiceQuestionDtos.get(0).getChoiceQuestion().getComposeType();
			}
			String createBy = complexQuestionDto.getQuestionDto().getQuestion().getCreateBy();
			String lastUpdateBy = complexQuestionDto.getQuestionDto().getQuestion().getLastUpdateBy();
			int i = 0;
			for(ChoiceQuestionDto choiceQuestionDto : choiceQuestionDtos){
				ChoiceQuestion choiceQuestion = choiceQuestionDto.getChoiceQuestion();
				if(choiceQuestion == null){
					choiceQuestion = new ChoiceQuestion();
				}
				choiceQuestion.setComposeType(composeType);
//				if(choiceQuestion.getId() == 0){
//					status = ConstantTe.QUESTION_SAVETYPE_SAVE;
//				}
				int choiceId = 0;
				QuestionDto questionDto = choiceQuestionDto.getQuestionDto();
				if(questionDto == null){
					questionDto = new QuestionDto();
				}
				questionDto.setSaveType(status);
				Question question = questionDto.getQuestion();
				if(question == null){
					question = new Question();
				}
				if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK ||
						questionType == Question.QUESTION_TYPE_CLOZE_FILL_BLANK){
					question.setAtomic(1);
				}else{
					question.setAtomic(0);
				}
				question.setTeId(parentId);
				question.setCode(code+"-"+i);
				question.setSequenceId(i);
				question.setQuestionTypeId(Question.QUESTION_TYPE_DANXUAN);
				question.setCreateBy(createBy);
				question.setLastUpdateBy(lastUpdateBy);
				questionDto.setQuestion(question);
				List<ChoiceAnswer> choiceAnswers = choiceQuestionDto.getChoiceAnswers();
				int cqId = questionService.saveOrUpate(conn, questionDto);
				choiceQuestion.setQuestionId(cqId);
				choiceId = choiceQuestionDao.insert(conn, choiceQuestion);
				saveChoiceAnswers(conn, choiceAnswers, choiceId);
				i++;
			}
		}
		
		
	}
	
	/**
	 * 保存选择题答案，如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceAnswers
	 * @param choiceId 选择题ID
	 * @param status 题目状态
	 */
	private void saveChoiceAnswers(Connection conn,List<ChoiceAnswer> choiceAnswers,int choiceId){
		if(choiceAnswers != null && choiceAnswers.size() > 0){
			int sequenceId = 0;
			for(ChoiceAnswer choiceAnswer : choiceAnswers){
				choiceAnswer.setChoiceId(choiceId);
				choiceAnswer.setSequenceId(sequenceId);
				choiceAnswer.setDescription(HtmlUtils.htmlEscape(choiceAnswer.getDescription()));
				choiceAnswerDao.insert(conn, choiceAnswer);
				sequenceId++;
			}
		}
	}
	
	
	/**
	 * 保存填空题,如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceQuestion
	 * @param code 编码
	 * @param parentId 父题目ID
	 * @param status 题目保存状态
	 */
	private void saveEssayQuestion(Connection conn,ComplexQuestionDto complexQuestionDto,String code,int parentId,int issubjectived,int status) throws Exception{
		List<EssayQuestionDto> essayQuestionDTOs = complexQuestionDto.getEssayQuestionDTOs();
		if(essayQuestionDTOs != null && essayQuestionDTOs.size() > 0){
			//如果修改，先删除
			if(status == ConstantTe.QUESTION_SAVETYPE_UPDATE){
				List<Integer> eids = essayQuestionDAO.findByTeId(parentId);
				fillblankAnswerDAO.deleteByEssayIds(conn, eids);
				essayQuestionDAO.deleteByIds(conn, eids);
				questionDao.deleteQuestionByTeid(conn, parentId);
				
			}
			String createBy = complexQuestionDto.getQuestionDto().getQuestion().getCreateBy();
			String lastUpdateBy = complexQuestionDto.getQuestionDto().getQuestion().getLastUpdateBy();
			int i = 0;
			for(EssayQuestionDto essayQuestionDTO : essayQuestionDTOs){
				int essayId = 0;
				EssayQuestion essayQuestion = essayQuestionDTO.getEssayQuestion();
				if(essayQuestion == null){
					essayQuestion = new EssayQuestion();
				}
				QuestionDto questionDto = essayQuestionDTO.getQuestionDto();
				if(questionDto == null){
					questionDto = new QuestionDto();
				}
				questionDto.setSaveType(status);
				Question question = questionDto.getQuestion();
				if(question == null){
					question = new Question();
				}
				if(complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CLOZE_FILL_BLANK)
					question.setAtomic(1);
				else
					question.setAtomic(0);
//				question.setId(essayQuestion.getQuesttionId());
				question.setTeId(parentId);
				question.setCode(code+"-"+i);
				question.setSequenceId(i);
				question.setQuestionTypeId(Question.QUESTION_TYPE_FILL_BLANK);
				question.setIssubjectived(complexQuestionDto.getQuestionDto().getQuestion().getIssubjectived());
				question.setCreateBy(createBy);
				question.setLastUpdateBy(lastUpdateBy);
				questionDto.setQuestion(question);
				
				List<FillblankAnswer> fillblankAnswers = essayQuestionDTO.getFillblankAnswers();
				
				int qid = questionService.saveOrUpate(conn, questionDto);
				essayQuestion.setQuesttionId(qid);
				essayQuestion.setMarkType(issubjectived);
				essayId = essayQuestionDAO.insert(conn, essayQuestion);
				saveEssayAnswers(conn, fillblankAnswers, essayId);
				i++;
			}
		}
		
		
	}
	
	/**
	 * 保存填空题答案，如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceAnswers
	 * @param choiceId 填空题ID
	 * @param status 题目状态
	 */
	private void saveEssayAnswers(Connection conn,List<FillblankAnswer> fillblankAnswers,int essayId){
		if(fillblankAnswers != null && fillblankAnswers.size() > 0){
			int sequenceId = 0;
			for(FillblankAnswer fillblankAnswer : fillblankAnswers){
				fillblankAnswer.setFillblankId(essayId);
				fillblankAnswer.setSequnceId(sequenceId);
				fillblankAnswer.setAnswer(HtmlUtils.htmlEscape(fillblankAnswer.getAnswer()));
				fillblankAnswerDAO.insert(conn, fillblankAnswer);
				sequenceId++;
			}
		}
	}
	/**
	 * 保存子题目，如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceQuestionDtos
	 * @param code
	 * @param questionId
	 * @param status
	 */
	private void saveSubQuestion(Connection conn,ComplexQuestionDto complexQuestionDto,String code,int questionId,int status) throws Exception{
		int questionType = complexQuestionDto.getQuestionType();
		if(questionType == Question.QUESTION_TYPE_CHOICE_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK || questionType == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD ){
			saveChoiceQuestion(conn, complexQuestionDto, code, questionId, status,questionType);
		}else{
			int issubjectived = complexQuestionDto.getQuestionDto().getQuestion().getIssubjectived();
			saveEssayQuestion(conn, complexQuestionDto, code, questionId,issubjectived, status);
		}
	}
	
	/**
	 * 保存完型填空题，如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param complexQuestionDto
	 * @param questionId 基础题目ID
	 * @param status 题目状态
	 */
	private void saveComplexQuestion(Connection conn,ComplexQuestionDto complexQuestionDto,int questionId,int status){
		ComplexQuestion complexQuestion = complexQuestionDto.getComplexQuestion();
		if(null == complexQuestion){
			complexQuestion =new ComplexQuestion();
		}
		List<ChoiceQuestionDto> choiceQuestionDtos = complexQuestionDto.getChoiceQuestionDtos();
		if(null != choiceQuestionDtos){
			complexQuestion.setSubQuestionCount(choiceQuestionDtos.size());
		}
		complexQuestion.setQuestionId(questionId);
		if(status == ConstantTe.QUESTION_SAVETYPE_UPDATE){
			complexQuestionDao.update(conn,complexQuestion);
		}else{
			int id=complexQuestionDao.insert(conn,complexQuestion);
			complexQuestion.setId(id);
		}
		
	}
	
	@Override
	public int saveOrUpdate(ComplexQuestionDto complexQuestionDto) throws Exception {
		Connection conn = null;
		int questionId=0;
		try{
			conn = ConnUtil.getTransactionConnection();
			QuestionDto questionDto = complexQuestionDto.getQuestionDto();
			int status = questionDto.getSaveType();
			Question question = questionDto.getQuestion();
			if(complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_FILL_BLANK ||
					complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CLOZE_FILL_BLANK){
				question.setAtomic(1);
			}else{
				question.setAtomic(0);
			}
			//保存基本题目信息
			 questionId = questionService.saveOrUpate(conn, questionDto);
			
			//保存完形填空题信息
			saveComplexQuestion(conn, complexQuestionDto, questionId,status);
			//保存子题目
			saveSubQuestion(conn, complexQuestionDto, questionDto.getQuestion().getCode(), questionId, status);
			conn.commit();
			
			//设置ID
			complexQuestionDto.getQuestionDto().getQuestion().setId(questionId);
			
			
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
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
	
	@Override
	public int saveOrUpdate(ComplexQuestionDto complexQuestionDto, Map<String,String> idMap)
			throws Exception {
		Connection conn = null;
		int questionId = 0;
		int oldId = 0;
		try{
			conn = ConnUtil.getTransactionConnection();
			if(idMap != null && idMap.size() > 0){
				deleteSubQuestion(conn, idMap, complexQuestionDto.getQuestionType());
			}
			QuestionDto questionDto = complexQuestionDto.getQuestionDto();
			int status = questionDto.getSaveType();
			Question question = questionDto.getQuestion();
			if(complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_FILL_BLANK ||
					complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CLOZE_FILL_BLANK){
				question.setAtomic(1);
			}else{
				question.setAtomic(0);
			}
			oldId = question.getId();
			//保存基本题目信息
			questionId = questionService.saveOrUpate(conn, questionDto);
			
			//保存完形填空题信息
			saveComplexQuestion(conn, complexQuestionDto, questionId,status);
			//保存子题目
			saveSubQuestion(conn, complexQuestionDto, questionDto.getQuestion().getCode(), questionId, status);
			conn.commit();
			
			//设置ID
			complexQuestionDto.getQuestionDto().getQuestion().setId(questionId);
			CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + oldId);
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
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
	
	@Override
	public List<ComplexQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception {
		return null;
	}

	private void deleteSubQuestion(Connection conn,Map<String,String> idMap,int questionType){
		String qid = idMap.get("qid");
		String sid = idMap.get("sid");
		String aid = idMap.get("aid");
		if(!StringUtils.isEmpty(aid)){
			String[] aids = aid.split(",");
			List<Integer> ids = new ArrayList<Integer>();
			for(String id:aids){
				if(id.equals("") || id.equals("undefiend")){
					continue;
				}
				ids.add(Integer.valueOf(id));
			}
			if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK){
				choiceAnswerDao.deleteByIds(conn, ids);
			}else{
				fillblankAnswerDAO.deleteByIds(conn, ids);
			}
		}
		
		if(!StringUtils.isEmpty(sid)){
			String[] sids = sid.split(",");
			List<Integer> ids = new ArrayList<Integer>();
			for(String id:sids){
				if(id.equals("") || id.equals("undefiend")){
					continue;
				}
				ids.add(Integer.valueOf(id));
			}
			if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_BLANK || questionType == Question.QUESTION_TYPE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD || questionType == Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK){
				choiceAnswerDao.deleteByChoiceIds(conn, ids);
				choiceQuestionDao.deleteByIds(conn, ids);
			}else{
				fillblankAnswerDAO.deleteByEssayIds(conn, ids);
				essayQuestionDAO.deleteByIds(conn, ids);
			}
		}
		
		if(!StringUtils.isEmpty(qid)){
			String[] qids = qid.split(",");
			List<Integer> ids = new ArrayList<Integer>();
			for(String id:qids){
				if(id.equals("") || id.equals("undefiend")){
					continue;
				}
				ids.add(Integer.valueOf(id));
			}
			questionDao.deleteByIds(conn, ids);
		}
	}

	@Override
	public void deleteComplexQuestion(Connection conn, int questionId) throws Exception {
		if(questionId != 0){
			List<Integer> cids = choiceQuestionDao.findByTeId(questionId);
			if(cids != null && cids.size() > 0){
				choiceAnswerDao.deleteByChoiceIds(conn, cids);
				choiceQuestionDao.deleteByIds(conn, cids);
				questionDao.deleteQuestionByTeid(conn, questionId);
			}
			List<Integer> eids = essayQuestionDAO.findByTeId(questionId);
			if(eids != null && eids.size() > 0){
				fillblankAnswerDAO.deleteByEssayIds(conn, eids);
				essayQuestionDAO.deleteByIds(conn, eids);
				questionDao.deleteQuestionByTeid(conn, questionId);
			}
			questionAttachDao.deleteAttatchByQuestionId(conn, questionId);
			complexQuestionDao.deleteByQuestionId(conn,questionId);
			questionDao.deleteById(conn, questionId);
		}
		
	}

	@Override
	public int saveOrUpdate(Connection conn,
			ComplexQuestionDto complexQuestionDto) throws Exception {
		int questionId=0;
			if(conn == null){
				conn = ConnUtil.getTransactionConnection();
			}
			QuestionDto questionDto = complexQuestionDto.getQuestionDto();
			int status = questionDto.getSaveType();
			Question question = questionDto.getQuestion();
			if(complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_FILL_BLANK ||
					complexQuestionDto.getQuestionType() == Question.QUESTION_TYPE_CLOZE_FILL_BLANK){
				question.setAtomic(1);
			}else{
				question.setAtomic(0);
			}
			//保存基本题目信息
			 questionId = questionService.saveOrUpate(conn, questionDto);
			
			//保存完形填空题信息
			saveComplexQuestion(conn, complexQuestionDto, questionId,status);
			//保存子题目
			saveSubQuestion(conn, complexQuestionDto, questionDto.getQuestion().getCode(), questionId, status);
			conn.commit();
			
			//设置ID
			complexQuestionDto.getQuestionDto().getQuestion().setId(questionId);
			
			
		return questionId;
	}

	@Override
	public void saveAs(Connection conn, Question question, int new_id) throws Exception {
		ComplexQuestionDto complexQuestionDto=getByQuestionId(question.getId());
		if(complexQuestionDto!=null){
			complexQuestionDto.getQuestionDto().getQuestion().setTeId(new_id);
			complexQuestionDto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
			saveOrUpdate(conn, complexQuestionDto);
		}
	}


	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}
