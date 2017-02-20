package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.ComplexQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.CorrectionQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.CorrectionQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.CorrectionQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;


public class CorrectionQuestionServiceImpl implements CorrectionQuestionService {

	@Autowired
	private ComplexQuestionDao complexQuestionDao;
	
	@Autowired
	private CorrectionQuestionDao correctionQuestionDao;
	
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QuestionAttachDao questionAttachDao;
	@Autowired
	private QuestionService questionService;

    public ComplexQuestionDao getComplexQuestionDao() {
        return complexQuestionDao;
    }

    public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
        this.complexQuestionDao = complexQuestionDao;
    }

    public CorrectionQuestionDao getCorrectionQuestionDao() {
        return correctionQuestionDao;
    }

    public void setCorrectionQuestionDao(CorrectionQuestionDao correctionQuestionDao) {
        this.correctionQuestionDao = correctionQuestionDao;
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

    @Override
	public ComplexCorrectionQuestionDto getByQuestionId(int questionId) {
		ComplexCorrectionQuestionDto dto = new ComplexCorrectionQuestionDto();
		ComplexQuestion complexQuestion = complexQuestionDao.getByQuestionId(questionId);
		Question question = questionDao.getQuestionById(questionId);
		dto.setComplexQuestion(complexQuestion);
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		dto.setQuestionDto(questionDto);
		List<Question> subQuestion = questionDao.getQuestionByTeId(question.getId());
		List<CorrectionQuestionDto> correctionQuestionDtos = new ArrayList<CorrectionQuestionDto>();
		if(CollectionUtils.isNotEmpty(subQuestion)){
			for(Question q : subQuestion){
				CorrectionQuestionDto cdto = new CorrectionQuestionDto();
				QuestionDto qdto = new QuestionDto();
				qdto.setQuestion(q);
				cdto.setQuestionDto(qdto);
				CorrectionQuestion correctionQuestion = correctionQuestionDao.getByQuestionId(q.getId());
				cdto.setCorrectionQuestion(correctionQuestion);
				correctionQuestionDtos.add(cdto);
			}
		}
		dto.setCorrectionQuestionDtos(correctionQuestionDtos);
		return dto;
	}
	public int saveOrUpdate(Connection conn,ComplexCorrectionQuestionDto complexCorrectionQuestionDto)throws Exception{
		int questionId=0;
		ComplexQuestion complexQuestion = complexCorrectionQuestionDto.getComplexQuestion();
		//判断是否为新题 新题直接保存
		Question question=complexCorrectionQuestionDto.getQuestionDto().getQuestion();
		int id=question.getId();
		question.setIssubjectived(Question.QUESTION_SUBJECTIVE_Y);
		questionId=questionService.saveOrUpate(conn,complexCorrectionQuestionDto.getQuestionDto());
		complexQuestion.setQuestionId(questionId);
		int saveType=complexCorrectionQuestionDto.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			save(conn,questionId,complexQuestion);
			saveCorrectionQuestions(conn, questionId, complexCorrectionQuestionDto.getCorrectionQuestionDtos());
		}else if(saveType== ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			saveAs(conn, questionId,complexCorrectionQuestionDto);
			
		}else{//更新
			update(conn,questionId,complexQuestion);
			updateCorrectionQuestion(conn, questionId, complexCorrectionQuestionDto.getCorrectionQuestionDtos());
		}
		return questionId;
	}
	@Override
	public int saveOrUpdate(ComplexCorrectionQuestionDto complexCorrectionQuestionDto) {
		Connection conn = null;
		int questionId=0;
		try{
			conn = ConnUtil.getTransactionConnection();
			int oldId = complexCorrectionQuestionDto.getQuestionDto().getQuestion().getId();
			questionId = saveOrUpdate(conn, complexCorrectionQuestionDto);
			CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + oldId);
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
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
		return questionId;
	}

	@Override
	public void deleteByQuestionId(Connection conn, int questionId) {
		ComplexQuestion complexQuestion = complexQuestionDao.getByQuestionId(questionId);
		if(complexQuestion != null){
			correctionQuestionDao.deleteByTeId(conn,questionId);
			complexQuestionDao.deleteByQuestionId(conn,questionId);
			questionAttachDao.deleteAttatchByQuestionId(conn, questionId);
			questionDao.deleteById(conn, questionId);
		}
		
	}

	@Override
	public void saveAs(Connection conn, int questionId, ComplexCorrectionQuestionDto complexCorrectionQuestionDto) {
		if(complexCorrectionQuestionDto != null){
			ComplexQuestion complexQuestion = complexCorrectionQuestionDto.getComplexQuestion();
			complexQuestion.setId(0);
			complexQuestionDao.insert(conn, complexQuestion);
			List<CorrectionQuestionDto> correctionQuestions = complexCorrectionQuestionDto.getCorrectionQuestionDtos();
			if(correctionQuestions != null){
				for(CorrectionQuestionDto cq : correctionQuestions){
					CorrectionQuestion correctionQuestion = cq.getCorrectionQuestion();
					Question question = cq.getQuestionDto().getQuestion();
					question.setId(0);
					question.setTeId(questionId);
					question.setQuestionTypeId(Question.QUESTION_TYPE_SUB_CORRECTION);
					question.setIssubjectived(Question.QUESTION_SUBJECTIVE_Y);
					question.setLastUpdateDate(new Date());
					int qid = questionDao.insert(conn, question);
					correctionQuestion.setId(0);
					correctionQuestion.setQuestionId(qid);
					correctionQuestionDao.insert(conn, correctionQuestion);
				}
			}
		}
		
	}

	@Override
	public List<ComplexCorrectionQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception {
		return null;
	}
	
	@Override
	public List<CorrectionQuestionDto> batchFindSubRepository(List<Integer> questionIds) throws Exception {
		List<CorrectionQuestionDto> list=new ArrayList<CorrectionQuestionDto>();
		if(questionIds != null){
			for(int questionId : questionIds){
				CorrectionQuestionDto dto = findCorrectionDtoQuestionByQuestionIdRepository(questionId);
				list.add(dto);
			}
		}
		return list;
	}
	
	private int save(Connection conn,int questionId,ComplexQuestion complexQuestion){
		int id = 0;
		if(complexQuestion != null){
			complexQuestion.setQuestionId(questionId);
			id = complexQuestionDao.insert(conn, complexQuestion);
		}
		return id;
	}
	
	private void saveCorrectionQuestions(Connection conn,int questionId,List<CorrectionQuestionDto> list){
		if(CollectionUtils.isNotEmpty(list)){
			for(CorrectionQuestionDto cq : list){
				CorrectionQuestion correctionQuestion = cq.getCorrectionQuestion();
				Question question = cq.getQuestionDto().getQuestion();
				question.setTeId(questionId);
				question.setQuestionTypeId(Question.QUESTION_TYPE_SUB_CORRECTION);
				question.setIssubjectived(Question.QUESTION_SUBJECTIVE_Y);
				int qid = questionDao.insert(conn, question);
				correctionQuestion.setQuestionId(qid);
				correctionQuestionDao.insert(conn, correctionQuestion);
			}
		}
	}
	private int update(Connection conn,int questionId,ComplexQuestion complexQuestion){
		if(complexQuestion != null){
			complexQuestionDao.update(conn, complexQuestion);
			return complexQuestion.getId();
		}
		return 0;
	}
	
	private void updateCorrectionQuestion(Connection conn,int questionId,List<CorrectionQuestionDto> correctionQuestions){
		if(CollectionUtils.isNotEmpty(correctionQuestions)){
			correctionQuestionDao.deleteByTeId(conn,questionId);
			questionDao.deleteQuestionByTeid(conn, questionId);
			for(CorrectionQuestionDto cq : correctionQuestions){
				CorrectionQuestion correctionQuestion = cq.getCorrectionQuestion();
				Question question = cq.getQuestionDto().getQuestion();
				question.setTeId(questionId);
				question.setId(0);
				question.setQuestionTypeId(Question.QUESTION_TYPE_SUB_CORRECTION);
				int qid = questionDao.insert(conn, question);
				correctionQuestion.setQuestionId(qid);
				correctionQuestion.setId(0);
				correctionQuestionDao.insert(conn,correctionQuestion);
			}
		}
	}
	
	@Override
	public CorrectionQuestionDto findCorrectionDtoQuestionByQuestionIdRepository(int questionId) throws Exception{
		return null;
	}

	@Override
	public CorrectionQuestionDto getSubByQuestionId(int questionId) {
		CorrectionQuestionDto dto = new CorrectionQuestionDto();
		CorrectionQuestion correctionQuestion = correctionQuestionDao.getByQuestionId(questionId);
		Question question = questionDao.getQuestionById(questionId);
		dto.setCorrectionQuestion(correctionQuestion);
		QuestionDto questionDto = new QuestionDto();
		questionDto.setQuestion(question);
		List<QuestionAttach> attaches=questionAttachDao.getByQuestionid(question.getId());
		questionDto.setQuestion(question);
		if(attaches!=null){
			questionDto.setQuestionAttachs(attaches);
		}
		dto.setQuestionDto(questionDto);
		return dto;
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}
