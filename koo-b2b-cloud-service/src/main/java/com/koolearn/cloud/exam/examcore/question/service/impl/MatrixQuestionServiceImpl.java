package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.awt.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dao.*;
import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.*;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.MatrixQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.ExamException;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.koolearn.framework.aries.util.ConnectionUtils;

public class MatrixQuestionServiceImpl implements MatrixQuestionService {

	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private MatrixQuestionDao matrixQuestionDao;
	
	@Autowired
	private QuestionAttachDao questionAttachDao;
	
	@Autowired
	private ChoiceQuestionDao choiceQuestionDao;
	
	@Autowired
	private ChoiceAnswerDao choiceAnswerDao;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ChoiceQuestionService choiceQuestionService;
	
	@Autowired
	private ComplexQuestionDao complexQuestionDao;

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    @Override
	public MatrixQuestionDto getById(int id) throws ExamException {
		return getMatrixById(id,true);
	}

	@Override
	public MatrixQuestionDto getByQuestionId(int questionId) {
		return getMatrixById(questionId,false);
	}
	
	/**
	 * 根据矩阵题ID或基础题目ID查询
	 * @param id
	 * @param isBaseId ture: id为基础题目ID，false：id为完型填空题ID
	 * @return
	 */
	private MatrixQuestionDto getMatrixById(int questionId,boolean isBaseId){
		MatrixQuestionDto matrixQuestionDto = new MatrixQuestionDto();
		MatrixQuestion matrixQuestion = null;
		if(isBaseId){
			matrixQuestion = matrixQuestionDao.getById(questionId);
		}else{
			matrixQuestion = matrixQuestionDao.getByQuestionId(questionId);
		}
		if(null != matrixQuestion){
//			Question question = questionDao.getQuestionById(matrixQuestion.getQuestionId());
//			List<QuestionAttach> questionAttachs = questionAttachDao.getByQuestionid(matrixQuestion.getQuestionId());
			matrixQuestionDto.setMatrixQuestion(matrixQuestion);
			QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
//			QuestionDto questionDto = new QuestionDto();
//			questionDto.setQuestion(question);
//			questionDto.setQuestionAttachs(questionAttachs);
			matrixQuestionDto.setQuestionDto(questionDto);
			List<ChoiceQuestionDto> choiceQuestionDtos = new ArrayList<ChoiceQuestionDto>();
			List<ChoiceQuestion> choiceQuestions = new ArrayList<ChoiceQuestion>();
			choiceQuestions = questionDao.getSubChoiceByQuestionid(matrixQuestion.getQuestionId());
			
			if(null != choiceQuestions){
				for(ChoiceQuestion choiceQuestion : choiceQuestions){
					ChoiceQuestionDto choiceQuestionDto = new ChoiceQuestionDto();
					choiceQuestionDto.setChoiceQuestion(choiceQuestion);
					QuestionDto cqDto = new QuestionDto();
					Question cQuestion = questionDao.getQuestionById(choiceQuestion.getQuestionId());
					cqDto.setQuestion(cQuestion);
					List<ChoiceAnswer> choiceAnswers = choiceAnswerDao.getByChoiceId(choiceQuestion.getId());
					choiceQuestionDto.setQuestionDto(cqDto);
					choiceQuestionDto.setChoiceAnswers(choiceAnswers);
					choiceQuestionDtos.add(choiceQuestionDto);
					matrixQuestionDto.setChoiceAnswers(choiceAnswers);
				}
				matrixQuestionDto.setChoiceQuestionDtos(choiceQuestionDtos);
			}
		}
		return matrixQuestionDto;
	}
	@Override
	public void saveOrUpdate(Connection conn,MatrixQuestionDto matrixQuestionDto,int saveType) throws Exception {
		QuestionDto questionDto = matrixQuestionDto.getQuestionDto();
		if(questionDto == null){
			questionDto = new QuestionDto();
		}
		questionDto.setSaveType(saveType);
		MatrixQuestion matrixQuestion = matrixQuestionDto.getMatrixQuestion();
		Question question = questionDto.getQuestion();
		question.setAtomic(0);
		question.setShowForm(matrixQuestion.getShowForm());
		question.setQuestionTypeId(Question.QUESTION_TYPE_TABLE);
		//保存基本题目信息
		int questionId = questionService.saveOrUpate(conn, questionDto);
		
		matrixQuestion.setQuestionId(questionId);
		//保存矩阵题信息
		matrixQuestionDao.insert(conn,matrixQuestion);
		//保存子题目
		saveSubQuestion(conn, matrixQuestionDto, question.getCode(), questionId, saveType);
		
	}

	
	@Override
	public int saveOrUpdate(MatrixQuestionDto matrixQuestionDto) throws ExamException {
		Connection conn = null;
		int questionId = 0;
		try{
			conn = ConnUtil.getTransactionConnection();
			
			QuestionDto questionDto = matrixQuestionDto.getQuestionDto();
			if(questionDto == null){
				questionDto = new QuestionDto();
			}
			int saveType = questionDto.getSaveType();
			MatrixQuestion matrixQuestion = matrixQuestionDto.getMatrixQuestion();
			Question question = questionDto.getQuestion();
			question.setAtomic(0);
			question.setShowForm(matrixQuestion.getShowForm());
			question.setQuestionTypeId(Question.QUESTION_TYPE_TABLE);
			//保存基本题目信息
			questionId = questionService.saveOrUpate(conn, questionDto);
			
			matrixQuestion.setQuestionId(questionId);
			//保存矩阵题信息
			saveMatrixQuestion(conn,matrixQuestion,saveType);
			//保存子题目
			saveSubQuestion(conn, matrixQuestionDto, question.getCode(), questionId, saveType);
			conn.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
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
	
	@Override
	public void saveOrUpdate(MatrixQuestionDto matrixQuestionDto, Map<String, String> idMap) throws ExamException {
		Connection conn = null;
		try{
			conn = ConnUtil.getTransactionConnection();
			QuestionDto questionDto = matrixQuestionDto.getQuestionDto();
			if(questionDto == null){
				questionDto = new QuestionDto();
			}
			int oldId = questionDto.getQuestion().getId();
			int saveType = questionDto.getSaveType();
			if(saveType == ConstantTe.QUESTION_SAVETYPE_UPDATE){
				
				List<Question> questionList = questionService.getQuestionByTeId(matrixQuestionDto.getQuestionDto().getQuestion().getId());
				if(questionList!=null&&questionList.size()>0){
					for (int i = 0; i < questionList.size(); i++) {
						Question quest = questionList.get(i);
						choiceQuestionService.deleteChoiceQuestion(conn, quest.getId());
					}
				}
//				deleteSubQuestion(conn, idMap);
			}
			MatrixQuestion matrixQuestion = matrixQuestionDto.getMatrixQuestion();
			Question question = questionDto.getQuestion();
			question.setAtomic(0);
			question.setShowForm(matrixQuestion.getShowForm());
			question.setQuestionTypeId(Question.QUESTION_TYPE_TABLE);
			//保存基本题目信息
			int questionId = questionService.saveOrUpate(conn, questionDto);
			
			matrixQuestion.setQuestionId(questionId);
			//保存矩阵题信息
			saveMatrixQuestion(conn,matrixQuestion,saveType);
			//保存子题目
			saveSubQuestion(conn, matrixQuestionDto, question.getCode(), questionId, saveType);
			conn.commit();
			CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + oldId);
		}catch (Exception e) {
			e.printStackTrace();
			try {
				if(conn != null){
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
		
	}
	
	/**
	 * 保存矩阵题信息
	 * @param conn
	 * @param matrixQuestion
	 * @param saveType
	 * @throws Exception
	 */
	private void saveMatrixQuestion(Connection conn,MatrixQuestion matrixQuestion,int saveType) throws Exception{
		if(saveType == ConstantTe.QUESTION_SAVETYPE_UPDATE){
			matrixQuestionDao.update(conn,matrixQuestion);
		}else{
			matrixQuestionDao.insert(conn,matrixQuestion);
		}
	}
	/**
	 * 保存选择题,如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceQuestion
	 * @param code 编码
	 * @param parentId 父题目ID
	 * @return 选择题ID
	 * @param status 题目保存状态
	 */
	private int saveChoiceQuestion(Connection conn,Question complexQuestion,ChoiceQuestionDto choiceQuestionDto,String code,int sequence,int parentId,int status) throws Exception{
		int choiceId = 0;
		ChoiceQuestion choiceQuestion = choiceQuestionDto.getChoiceQuestion();
		QuestionDto questionDto = choiceQuestionDto.getQuestionDto();
		String createBy = complexQuestion.getCreateBy();
		String lastUpdateBy = complexQuestion.getLastUpdateBy();
		if(questionDto == null){
			questionDto = new QuestionDto();
		}
		Question question = questionDto.getQuestion();
		if(question == null){
			question = new Question();
		}
		question.setId(0);
		if(choiceQuestion.getQuestionId() != null){
			question.setId(choiceQuestion.getQuestionId());
		}
		question.setAtomic(0);
		question.setTeId(parentId);
		question.setCode(code);
		question.setSequenceId(sequence);
		if(question.getQuestionTypeId()==0){
			question.setQuestionTypeId(Question.QUESTION_TYPE_DANXUAN);
		}
		question.setCreateBy(createBy);
		question.setLastUpdateBy(lastUpdateBy);
		questionDto.setSaveType(status);
		questionDto.setQuestion(question);
		int cqId = questionService.saveOrUpate(conn, questionDto);
		choiceQuestion.setQuestionId(cqId);
		choiceQuestion.setChinaEnglish(0);
		choiceQuestion.setComposeType(1);
//		if(status == ConstantTe.QUESTION_SAVETYPE_UPDATE){
//			choiceQuestionDao.update(conn, choiceQuestion);
//			choiceId = choiceQuestion.getId();
//		}else{
			choiceId = choiceQuestionDao.insert(conn, choiceQuestion);
//		}
		
		return choiceId;
	}
	
	/**
	 * 保存选择题答案，如果状态为保存或另存为，插入，否则修改
	 * @param conn
	 * @param choiceAnswers
	 * @param choiceId 选择题ID
	 * @param status 题目保存状态
	 */
	private void saveChoiceAnswers(Connection conn,List<ChoiceAnswer> choiceAnswers,int choiceId,String[] answerStr){
		Map answerMap = new HashMap();
		if(answerStr!=null&&answerStr.length>0)
		for(int i=0;i<answerStr.length;i++){
			int answerInt = Integer.parseInt(answerStr[i]);
			answerMap.put(answerInt, answerInt);
		}
		if(choiceAnswers != null && choiceAnswers.size() > 0){
			choiceAnswerDao.deleteByChoiceId(conn, choiceId);
			int i=1;
			for(ChoiceAnswer choiceAnswer : choiceAnswers){
				choiceAnswer.setChoiceId(choiceId);
				choiceAnswer.setSequenceId(i-1);
				choiceAnswer.setDescription(HtmlUtils.htmlEscape(choiceAnswer.getDescription()));
				if(answerMap!=null&&answerMap.containsKey(i)){
					choiceAnswer.setIsright(1);
				}else{
					choiceAnswer.setIsright(0);
				}
				choiceAnswerDao.insert(conn, choiceAnswer);
				i++;
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
	private void saveSubQuestion(Connection conn,MatrixQuestionDto matrixQuestionDto,String code,int questionId,int status) throws Exception{
		List<ChoiceQuestionDto> choiceQuestionDtos = matrixQuestionDto.getChoiceQuestionDtos();
		Integer showForm = matrixQuestionDto.getMatrixQuestion().getShowForm();
		if(choiceQuestionDtos != null && choiceQuestionDtos.size() > 0){
			for(int i=0;i<choiceQuestionDtos.size();i++){
				ChoiceQuestionDto choiceQuestionDto = choiceQuestionDtos.get(i);
				
				ChoiceQuestion choiceQuestion = choiceQuestionDto.getChoiceQuestion();
				choiceQuestion.setId(0);
				if(status!=ConstantTe.QUESTION_SAVETYPE_SAVEAS){
					status = ConstantTe.QUESTION_SAVETYPE_SAVE;
				}
				if(showForm==Question.QUESTION_SHOWFORM_SORT){
					//拖拽题设置为多选
					//choiceQuestionDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_DUOXUAN);
				}
				int choiceId = saveChoiceQuestion(conn, matrixQuestionDto.getQuestionDto().getQuestion(),choiceQuestionDto, code+"-"+(i+1), i+1,questionId, status);
				String[] answerStr = choiceQuestion.getAnswer().split(",");
				List<ChoiceAnswer> choiceAnswers = matrixQuestionDto.getChoiceAnswers();
				saveChoiceAnswers(conn, choiceAnswers, choiceId, answerStr);
			}
		}
	}
	
	private void deleteSubQuestion(Connection conn,Map<String,String> idMap){
		if(idMap != null && idMap.size() > 0){
			String qid = idMap.get("qid");
			String sid = idMap.get("sid");
			
			if(!StringUtils.isEmpty(sid)){
				String[] sids = sid.split(",");
				List<Integer> ids = new ArrayList<Integer>();
				for(String id:sids){
					ids.add(Integer.valueOf(id));
				}
				choiceAnswerDao.deleteByChoiceIds(conn, ids);
				choiceQuestionDao.deleteByIds(conn, ids);
			}
			
			if(!StringUtils.isEmpty(qid)){
				String[] qids = qid.split(",");
				List<Integer> ids = new ArrayList<Integer>();
				for(String id:qids){
					ids.add(Integer.valueOf(id));
				}
				questionDao.deleteByIds(conn, ids);
			}
		}
		
	}
	@Override
	public List<MatrixQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception {
		//获取Question
		List<Question> questions=null;//questionRepository.getQuestionsAndFetch(questionIds);
		
		//获取MatrixQuestion
		List<MatrixQuestion> matrixs=null;//martixQuestionRepository.getMatrixQuestionsAndFetch(questionIds);
		
		//获取Attach
		List<List<QuestionAttach>> attaches=null;//questionAttachRepository.getQuestionAttachAndFetch(questionIds);
		
		List<MatrixQuestionDto> list=new ArrayList<MatrixQuestionDto>();
		Map<Integer, MatrixQuestion> questionid_MatrixQuestion=new HashMap<Integer, MatrixQuestion>();
		Map<Integer, List<QuestionAttach>> questionid_QuestionAttachs=new HashMap<Integer, List<QuestionAttach>>();
		if(matrixs!=null){
			for(MatrixQuestion matrix:matrixs){
				questionid_MatrixQuestion.put(matrix.getQuestionId(), matrix);
			}
		}
		if(attaches!=null){
			for(List<QuestionAttach> lq:attaches){
				if(lq!=null&&lq.size()>0){
					questionid_QuestionAttachs.put(lq.get(0).getQuestionId(), lq);
				}
			}
		}
		for(Question question:questions){
			MatrixQuestionDto dto=new MatrixQuestionDto();
			QuestionDto dto2=new QuestionDto();
			dto2.setQuestion(question);
			dto2.setQuestionAttachs(questionid_QuestionAttachs.get(question.getId()));
			dto.setQuestionDto(dto2);
			dto.setMatrixQuestion(questionid_MatrixQuestion.get(question.getId()));
			List<Integer> cids = null;
			//cids = questionRepository.getQuestionIdsByTeIdFecth(question.getId());
			if(cids == null || cids.size() == 0){
				cids = questionDao.getQuestionIdByTeIds(question.getId());
			}
			//子试题
			List<ChoiceQuestionDto> choiceQuestionDtos = choiceQuestionService.batchFindRepository(cids);
			List<ChoiceAnswer> choiceAnswers = null;
			if(choiceQuestionDtos != null && choiceQuestionDtos.size() > 0){
				choiceAnswers = choiceQuestionDtos.get(0).getChoiceAnswers();
			}
			dto.setChoiceQuestionDtos(choiceQuestionDtos);
			dto.setChoiceAnswers(choiceAnswers);
			list.add(dto);
		}
		return list;
	}
	public QuestionDao getQuestionDao() {
		return questionDao;
	}

	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	public MatrixQuestionDao getMatrixQuestionDao() {
		return matrixQuestionDao;
	}

	public void setMatrixQuestionDao(MatrixQuestionDao matrixQuestionDao) {
		this.matrixQuestionDao = matrixQuestionDao;
	}

	public QuestionAttachDao getQuestionattachDao() {
		return questionAttachDao;
	}

	public void setQuestionattachDao(QuestionAttachDao questionattachDao) {
		this.questionAttachDao = questionattachDao;
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

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	public ChoiceQuestionService getChoiceQuestionService() {
		return choiceQuestionService;
	}

	public void setChoiceQuestionService(ChoiceQuestionService choiceQuestionService) {
		this.choiceQuestionService = choiceQuestionService;
	}

	public ComplexQuestionDao getComplexQuestionDao() {
		return complexQuestionDao;
	}

	public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
		this.complexQuestionDao = complexQuestionDao;
	}

	@Override
	public void saveAs(Connection conn, Question question, int newId) throws Exception {
		if(question.getQuestionTypeId() != Question.QUESTION_TYPE_TABLE){
			return;
		}
		
		MatrixQuestionDto matrixQuestionDto = getByQuestionId(question.getId());
		matrixQuestionDto.getQuestionDto().getQuestion().setNewVersion(0);
		questionService.saveOrUpate(conn, matrixQuestionDto.getQuestionDto());
		matrixQuestionDto.getQuestionDto().getQuestion().setNewVersion(1);
		if(matrixQuestionDto != null){
			matrixQuestionDto.getQuestionDto().getQuestion().setTeId(newId);
//			matrixQuestionDto.getQuestionDto().getQuestion().setId(0);
			matrixQuestionDto.getMatrixQuestion().setId(0);
			List<ChoiceQuestionDto> dtos=matrixQuestionDto.getChoiceQuestionDtos();
			if(dtos!=null&&dtos.size()>0){
				for(ChoiceQuestionDto dto:dtos){
					dto.getChoiceQuestion().setQuestionId(null);
				}
			}
			
			saveOrUpdate(conn, matrixQuestionDto, ConstantTe.QUESTION_SAVETYPE_SAVEAS);
		}
	}

	@Override
	public void deleteMatrixQuestion(Connection conn, int questionId) throws Exception {
		if(questionId != 0){
			List<Integer> cids = choiceQuestionDao.findByTeId(questionId);
			if(cids != null && cids.size() > 0){
				choiceAnswerDao.deleteByChoiceIds(conn, cids);
				choiceQuestionDao.deleteByIds(conn, cids);
				questionDao.deleteQuestionByTeid(conn, questionId);
			}
			questionAttachDao.deleteAttatchByQuestionId(conn, questionId);
			matrixQuestionDao.deleteByQuestionId(conn, questionId);
			questionDao.deleteById(conn, questionId);
		}
		
	}
}
