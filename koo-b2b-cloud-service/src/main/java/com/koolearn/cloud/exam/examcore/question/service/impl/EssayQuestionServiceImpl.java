package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.EssayQuestionDAO;
import com.koolearn.cloud.exam.examcore.question.dao.FillblankAnswerDAO;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.EssayQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;


/**
 * 
 * @author liaoqiangang
 * @date 2012-10-22
 */
public class EssayQuestionServiceImpl implements EssayQuestionService {

	private static final Log logger = LogFactory.getLog(EssayQuestionServiceImpl.class);
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private EssayQuestionDAO essayQuestionDAO;
	
	@Autowired
	private QuestionAttachDao questionAttachDao;

	@Autowired
	private FillblankAnswerDAO fillblankAnswerDAO;
	
	@Autowired
	private QuestionService questionService;

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public EssayQuestionDAO getEssayQuestionDAO() {
        return essayQuestionDAO;
    }

    public void setEssayQuestionDAO(EssayQuestionDAO essayQuestionDAO) {
        this.essayQuestionDAO = essayQuestionDAO;
    }

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    public FillblankAnswerDAO getFillblankAnswerDAO() {
        return fillblankAnswerDAO;
    }

    public void setFillblankAnswerDAO(FillblankAnswerDAO fillblankAnswerDAO) {
        this.fillblankAnswerDAO = fillblankAnswerDAO;
    }

    public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	/**
	 * 
	 */
	public int saveOrUpdate(EssayQuestionDto essayQuestionDTO)throws Exception {
		EssayQuestion essayQuestion=essayQuestionDTO.getEssayQuestion();
		Connection conn = null;
		int new_id = 0;
		try{
		conn = ConnUtil.getTransactionConnection();
		//判断是否为新题 新题直接保存
		Question question=essayQuestionDTO.getQuestionDto().getQuestion();
		int id=question.getId();
		new_id=questionService.saveOrUpate(conn,essayQuestionDTO.getQuestionDto());
		int saveType=essayQuestionDTO.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			int essay_id=saveEssay(conn,new_id,essayQuestionDTO.getEssayQuestion());
			saveFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
		}else if(saveType== ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			//设置选择题 关联id
			EssayQuestion essayQuestion2=essayQuestionDTO.getEssayQuestion();
			essayQuestion2.setQuesttionId(new_id);
			essayQuestion2.setId(0);
			int essay_id=saveEssay(conn,new_id,essayQuestionDTO.getEssayQuestion());
			//设置选择题选项id
			List<FillblankAnswer> answers=essayQuestionDTO.getFillblankAnswers();
			for(FillblankAnswer answer:answers){
				answer.setId(0);
				answer.setFillblankId(essay_id);
			}
			saveFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
			
		}else{//更新
			int essay_id=updateEssay(conn,essayQuestionDTO.getEssayQuestion(),new_id);
			updateFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
		}
		conn.commit();
		CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + id);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.fillInStackTrace();
				}
			}
		}
		return new_id;
	}

	/**
	 * 
	 * @param conn
	 * @param choice_id
	 * @param fillblankAnswers
	 */
	private void updateFillblankAnswer(Connection conn, int essay_id, List<FillblankAnswer> fillblankAnswers) {
		List<FillblankAnswer> updateObj=new ArrayList<FillblankAnswer>();
		List<Integer> updateIds=new ArrayList<Integer>();
		List<FillblankAnswer> saveObj=new ArrayList<FillblankAnswer>();
		
		for(FillblankAnswer answer:fillblankAnswers){
			int id=answer.getId();
			answer.setFillblankId(essay_id);
			answer.setAnswer(HtmlUtils.htmlEscape(answer.getAnswer()));
			if(id==0){
				saveObj.add(answer);
			}else{
				updateObj.add(answer);
				updateIds.add(id);
			}
		}
		//删除不存在的ids
		fillblankAnswerDAO.deleteAttatch(conn, essay_id, updateIds);
		//更新存在的对象
		fillblankAnswerDAO.batchUpdate(conn, updateObj);
		//插入新增的对象
		fillblankAnswerDAO.batchInsert(conn, saveObj);	
		
	}

	/**
	 * 更新  EssayQuestion  填空大表
	 * @param conn
	 * @param essayQuestion
	 * @return
	 */
	private int updateEssay(Connection conn, EssayQuestion essayQuestion,int questionId) {
		essayQuestion.setQuesttionId(questionId);
		essayQuestionDAO.update(conn, essayQuestion);
		return essayQuestion.getId();
	}

	/**
	 * 更新填空题Id  保存答案
	 * @param conn
	 * @param fillblankId
	 * @param fillblankAnswers
	 */
	private void saveFillblankAnswer(Connection conn, int fillblankId, List<FillblankAnswer> fillblankAnswers) {
		for(FillblankAnswer answer:fillblankAnswers){
			answer.setFillblankId(fillblankId);
			answer.setAnswer(HtmlUtils.htmlEscape(answer.getAnswer()));
		}
		fillblankAnswerDAO.batchInsert(conn, fillblankAnswers);
		
		
		
		
	}

	/**
	 * 更新题目Id  在保存 填空题
	 * @param conn
	 * @param new_id
	 * @param essayQuestion
	 * @return
	 */
	private int saveEssay(Connection conn, int new_id, EssayQuestion essayQuestion) {
		essayQuestion.setQuesttionId(new_id);
		int essay_id=essayQuestionDAO.insert(conn, essayQuestion);
		return essay_id;
	}

	public EssayQuestion getEssayQuestionByQuestionId(int questionId) {
		EssayQuestion question=essayQuestionDAO.getByQuestionid(questionId);
		return question;
	}

	public List<FillblankAnswer> getFillblankAnswersById(int essayQuestionId) {
		List<FillblankAnswer>  list=fillblankAnswerDAO.getFillblankAnswersById(essayQuestionId);
		return list;
	}

	
	/**
	 * 通过questionId 获取EssayQuestionDTO
	 */
	public EssayQuestionDto getEssayQuestionDTO(int questionId) {
		
		EssayQuestionDto dto = new EssayQuestionDto();
		EssayQuestion essayQuestion = essayQuestionDAO.getByQuestionid(questionId);
		dto.setEssayQuestion(essayQuestion);
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		dto.setQuestionDto(questionDto);
		List<FillblankAnswer> answers = null;
		try {
			answers = fillblankAnswerDAO.getFillblankAnswersById(essayQuestion.getId());
		} catch (Exception e) {
			logger.error("9");
			e.printStackTrace();
		}
		
		dto.setFillblankAnswers(answers);
		return dto;
	}

	/**
	 *	另存为  子试题  保存 
	 */
	public void saveAs(Connection conn, Question question, int new_id)throws Exception {

		//重新拼装  ChoiceQuestionDto对象
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(conn, question.getId());
		EssayQuestionDto dto=new EssayQuestionDto();
		dto.setQuestionDto(questionDto);
		EssayQuestion essayQuestion=essayQuestionDAO.getByQuestionid(question.getId());
		dto.setEssayQuestion(essayQuestion);
		List<FillblankAnswer> fillblankAnswers=fillblankAnswerDAO.getFillblankAnswersById(essayQuestion.getId());
		dto.setFillblankAnswers(fillblankAnswers);
		
		//更新老的数据，最新版本改为 0；
		questionDto.getQuestion().setNewVersion(0);
		questionDto.setSaveType(ConstantTe.QUESTION_SAVETYPE_UPDATE);
		questionService.saveOrUpate(conn, questionDto);
		
		//另存  父Id，最新版本号
		int newId=0;
		questionDto.getQuestion().setTeId(new_id);
		questionDto.setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
		newId=questionService.saveOrUpate(conn,questionDto);
		saveAs(dto, conn, newId);
	}

	/**
	 * 审核后，另存
	 * @param dto
	 * @param conn
	 * @param newId
	 */
	private void saveAs(EssayQuestionDto dto, Connection conn, int newId) {
//		设置选择题 关联id
		EssayQuestion essayQuestion2=dto.getEssayQuestion();
		essayQuestion2.setQuesttionId(newId);
		essayQuestion2.setId(0);
		int essayId=saveEssay(conn,newId,dto.getEssayQuestion());
		//设置选择题选项id
		List<FillblankAnswer> answers=dto.getFillblankAnswers();
		for(FillblankAnswer answer:answers){
			answer.setId(0);
			answer.setFillblankId(essayId);
		}
		saveFillblankAnswer(conn, essayId, dto.getFillblankAnswers());
	}

	/**
	 * 
	 */
	public List<EssayQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception {
		return null;
	}

	/**
	 * 获取Ids集合
	 * @param essays
	 * @return
	 */
	private List<Integer> findEssayIds(List<EssayQuestion> essays) {
		List<Integer> list=new ArrayList<Integer>();
		for(EssayQuestion essayQuestion:essays){
			list.add(essayQuestion.getId());
		}
		return list;
	}
	
	
	/**
	 *  级联删除   真删除
	 */
	@Override
	public void deleteEssayQuestion(Connection conn,int questionId) {
		Question q=questionDao.getQuestionById(questionId);
		List<Integer> list=new ArrayList<Integer>();
		list.add(questionId);
		fillblankAnswerDAO.deleteAnswerByQuestionIds(conn, list);
		essayQuestionDAO.deleteEssayByQuestionIds(conn, list);
		questionAttachDao.deleteAttatchsByQuestionIds(conn, list);
		questionDao.deleteByIds(conn, list);
	}

	@Override
	public int saveOrUpdate(Connection conn, EssayQuestionDto essayQuestionDTO)
			throws Exception {
		EssayQuestion essayQuestion=essayQuestionDTO.getEssayQuestion();
		int new_id = 0;
		//判断是否为新题 新题直接保存
		Question question=essayQuestionDTO.getQuestionDto().getQuestion();
		int id=question.getId();
		new_id=questionService.saveOrUpate(conn,essayQuestionDTO.getQuestionDto());
		int saveType=essayQuestionDTO.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			int essay_id=saveEssay(conn,new_id,essayQuestionDTO.getEssayQuestion());
			saveFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
		}else if(saveType==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			//设置选择题 关联id
			EssayQuestion essayQuestion2=essayQuestionDTO.getEssayQuestion();
			essayQuestion2.setQuesttionId(new_id);
			essayQuestion2.setId(0);
			int essay_id=saveEssay(conn,new_id,essayQuestionDTO.getEssayQuestion());
			//设置选择题选项id
			List<FillblankAnswer> answers=essayQuestionDTO.getFillblankAnswers();
			for(FillblankAnswer answer:answers){
				answer.setId(0);
				answer.setFillblankId(essay_id);
			}
			saveFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
			
		}else{//更新
			int essay_id=updateEssay(conn,essayQuestionDTO.getEssayQuestion(),new_id);
			updateFillblankAnswer(conn,essay_id,essayQuestionDTO.getFillblankAnswers());
		}
		return new_id;
	}
	
}
