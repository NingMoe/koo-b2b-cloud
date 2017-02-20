package com.koolearn.cloud.exam.examcore.question.service.impl;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.ChoiceAnswerDao;
import com.koolearn.cloud.exam.examcore.question.dao.ChoiceQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionAttachDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;


public class ChoiceQuestionServiceImpl implements ChoiceQuestionService {
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private ChoiceQuestionDao choiceQuestionDao;
	@Autowired
	private ChoiceAnswerDao choiceAnswerDao;
	@Autowired
	private QuestionAttachDao questionAttachDao;
	@Autowired
	private QuestionService questionService;

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
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

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    @Override
	public void deleteChoiceQuestion(Connection conn,int questionId) {
		Question q=questionDao.getQuestionById(questionId);
		List<Integer> list=new ArrayList<Integer>();
		list.add(questionId);
		choiceAnswerDao.deleteAnswerByQuestionIds(conn, list);
		choiceQuestionDao.deleteChoiceByQuestionIds(conn, list);
		questionAttachDao.deleteAttatchsByQuestionIds(conn, list);
		questionDao.deleteByIds(conn, list);

	}

	@Override
	public ChoiceQuestionDto getChoiceQuestion(int questionId){
		//题目
//		Question question=questionDao.getQuestionById(questionId);
//		if(question==null){
//			return null;
//		}
//		QuestionDto questionDto=new QuestionDto(question);
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
		

		//选择题本身
		ChoiceQuestion choiceQuestion=choiceQuestionDao.getByQuestionid(questionId);
		ChoiceQuestionDto choiceQuestionDto=new ChoiceQuestionDto();
		choiceQuestionDto.setChoiceQuestion(choiceQuestion);
		choiceQuestionDto.setQuestionDto(questionDto);
		
		//答案
		List<ChoiceAnswer> answers=choiceAnswerDao.getByChoiceId(choiceQuestion.getId());
		choiceQuestionDto.setChoiceAnswers(answers);
		
		return choiceQuestionDto;
	}

	/* (non-Javadoc)
	 * @see com.koolearn.exam.question.service.ChoiceQuestionService#saveOrUpdate(com.koolearn.exam.question.dto.ChoiceQuestionDto)
	 */
	/* (non-Javadoc)
	 * @see com.koolearn.exam.question.service.ChoiceQuestionService#saveOrUpdate(com.koolearn.exam.question.dto.ChoiceQuestionDto)
	 */
	@Override
	public int saveOrUpdate(ChoiceQuestionDto choiceQuestionDto) {
		ChoiceQuestion choiceQuestion=choiceQuestionDto.getChoiceQuestion();
		Connection conn = null;
		int new_id=0;
		try{
		conn = ConnUtil.getTransactionConnection();
		//判断是否为新题 新题直接保存
		Question question=choiceQuestionDto.getQuestionDto().getQuestion();
		int id=question.getId();
		new_id=questionService.saveOrUpate(conn,choiceQuestionDto.getQuestionDto());
		int saveType=choiceQuestionDto.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			int choice_id=saveChoice(conn,new_id,choiceQuestionDto.getChoiceQuestion());
			saveChoiceAnswer(conn,choice_id,choiceQuestionDto.getChoiceAnswers());
		}else if(saveType== ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			saveAs(choiceQuestionDto, conn, new_id);
			
		}else{//更新
			int choice_id=updateChoice(conn,choiceQuestionDto.getChoiceQuestion(),new_id);
			updateChoiceAnswer(conn,choice_id,choiceQuestionDto.getChoiceAnswers());
		}
		
		conn.commit();
		CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + id);
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
		return new_id;

	}

	private void saveAs(ChoiceQuestionDto choiceQuestionDto, Connection conn, int new_id) {
		//设置选择题 关联id
		ChoiceQuestion choiceQuestion2=choiceQuestionDto.getChoiceQuestion();
		choiceQuestion2.setQuestionId(new_id);
		choiceQuestion2.setId(0);
		int choice_id=saveChoice(conn,new_id,choiceQuestionDto.getChoiceQuestion());
		//设置选择题选项id
		List<ChoiceAnswer> answers=choiceQuestionDto.getChoiceAnswers();
		for(ChoiceAnswer answer:answers){
			answer.setId(0);
			answer.setChoiceId(choice_id);
		}
		saveChoiceAnswer(conn,choice_id,choiceQuestionDto.getChoiceAnswers());
	}

	/**
	 * 批量更新选择题答案,包括删除不存在答案,更新已有的答案,插入不存在的答案
	 * @param conn
	 * @param choice_id
	 * @param choiceAnswers
	 */
	private void updateChoiceAnswer(Connection conn, int choice_id, List<ChoiceAnswer> choiceAnswers) {
		List<ChoiceAnswer> updateObj=new ArrayList<ChoiceAnswer>();
		List<Integer> updateIds=new ArrayList<Integer>();
		List<ChoiceAnswer> saveObj=new ArrayList<ChoiceAnswer>();
		
		for(ChoiceAnswer answer:choiceAnswers){
			int id=answer.getId();
			answer.setChoiceId(choice_id);
			answer.setDescription(HtmlUtils.htmlEscape(answer.getDescription()));
			if(id==0){
				saveObj.add(answer);
			}else{
				updateObj.add(answer);
				updateIds.add(id);
			}
		}
		//删除不存在的ids
		choiceAnswerDao.deleteAttatch(conn, choice_id, updateIds);
		//更新存在的对象
		choiceAnswerDao.batchUpdate(conn, updateObj);
		//插入新增的对象
		choiceAnswerDao.batchInsert(conn, saveObj);
		
	}

	/**
	 * 更新选择题
	 * @param conn
	 * @param choiceQuestion
	 * @return
	 */
	private int updateChoice(Connection conn, ChoiceQuestion choiceQuestion,int questionId) {
		choiceQuestion.setQuestionId(questionId);
		choiceQuestionDao.update(conn, choiceQuestion);
		return choiceQuestion.getId();
	}

	/**
	 * 保存答案
	 * @param conn
	 * @param choice_id
	 * @param choiceAnswers
	 */
	private void saveChoiceAnswer(Connection conn, int choice_id, List<ChoiceAnswer> choiceAnswers) {
		for(ChoiceAnswer answer:choiceAnswers){
			answer.setChoiceId(choice_id);
			answer.setDescription(HtmlUtils.htmlEscape(answer.getDescription()));
		}
		choiceAnswerDao.batchInsert(conn, choiceAnswers);
		
	}

	/**
	 * 保存选择题
	 * @param conn
	 * @param new_id
	 * @param choiceQuestion
	 * @return
	 */
	private int saveChoice(Connection conn, int new_id, ChoiceQuestion choiceQuestion) {
		choiceQuestion.setQuestionId(new_id);
		int choice_id=choiceQuestionDao.insert(conn, choiceQuestion);
		return choice_id;
	}

	@Override
	public boolean checkUniqueCode(String strId, String strCode) {
		if(isEmpty(strCode)){
			return false;
		}
		int cnt=0;
		if(isEmpty(strId)){
			cnt=questionDao.codeCount(strCode);
		}else{
			cnt=questionDao.codeCountWithId(Integer.parseInt(strId),strCode);
		}
		if(cnt==0){
			return true;
		}
		return false;
	}

	@Override
	public void saveAs(Connection conn, Question question, int new_id) throws Exception {
		//重新拼装  ChoiceQuestionDto对象
		QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(conn, question.getId());
		ChoiceQuestionDto dto=new ChoiceQuestionDto();
		dto.setQuestionDto(questionDto);
		ChoiceQuestion choiceQuestion=choiceQuestionDao.getByQuestionid(question.getId());
		dto.setChoiceQuestion(choiceQuestion);
		List<ChoiceAnswer> choiceAnswers=choiceAnswerDao.getByChoiceId(choiceQuestion.getId());
		dto.setChoiceAnswers(choiceAnswers);
		
		int newId=0;
		questionDto.getQuestion().setTeId(new_id);
		questionDto.setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);//另存  父Id，最新版本号
		newId=questionService.saveOrUpate(conn,questionDto);
		saveAs(dto, conn, newId);
		
	}

	@Override
	public List<ChoiceQuestionDto> batchFindRepository(List<Integer> questionIds) throws Exception {
		if(questionIds==null||questionIds.size()==0){
			return new ArrayList<ChoiceQuestionDto>();
		}
		List<ChoiceQuestionDto> list=new ArrayList<ChoiceQuestionDto>();
		return list;
	}
	/**
	 * 获取选择题ID
	 * @param choices
	 * @return
	 */
	private List<Integer> findchoiceIds(List<ChoiceQuestion> choices) {
		List<Integer> list=new ArrayList<Integer>();
		for(ChoiceQuestion choiceQuestion:choices){
			list.add(choiceQuestion.getId());
		}
		return list;
	}

	@Override
	public String getSortSequence(int choice_id) {
		// TODO Auto-generated method stub
		List<ChoiceAnswer> choiceAnswers=choiceAnswerDao.getByChoiceIdOrderBy(choice_id);
		String str="";
		for (ChoiceAnswer choiceAnswer : choiceAnswers) {
			if("".equals(str))
				str+=choiceAnswer.getSequenceId();
			else
				str+=ConstantTe.SEPERATOR_ANSWER_AND+choiceAnswer.getSequenceId();
		}
		return str;
	}

	@Override
	public int saveOrUpdate(Connection conn, ChoiceQuestionDto choiceQuestionDto) throws Exception {
		ChoiceQuestion choiceQuestion=choiceQuestionDto.getChoiceQuestion();
		int new_id=0;
		//判断是否为新题 新题直接保存
		Question question=choiceQuestionDto.getQuestionDto().getQuestion();
		int id=question.getId();
		new_id=questionService.saveOrUpate(conn,choiceQuestionDto.getQuestionDto());
		int saveType=choiceQuestionDto.getQuestionDto().getSaveType();
		//保存还是更新
		if(id==0){//保存
			int choice_id=saveChoice(conn,new_id,choiceQuestionDto.getChoiceQuestion());
			saveChoiceAnswer(conn,choice_id,choiceQuestionDto.getChoiceAnswers());
		}else if(saveType==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			saveAs(choiceQuestionDto, conn, new_id);
			
		}else{//更新
			int choice_id=updateChoice(conn,choiceQuestionDto.getChoiceQuestion(),new_id);
			updateChoiceAnswer(conn,choice_id,choiceQuestionDto.getChoiceAnswers());
		}
		
		return new_id;
	}
	
	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}
