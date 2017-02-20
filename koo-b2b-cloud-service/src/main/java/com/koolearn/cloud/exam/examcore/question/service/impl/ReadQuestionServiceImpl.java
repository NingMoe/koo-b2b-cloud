package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.ComplexQuestionDao;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.*;
import com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.*;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ReadQuestionServiceImpl implements ReadQuestionService {
	private static final Log logger = LogFactory.getLog(ReadQuestionServiceImpl.class);
	@Autowired
	private ComplexQuestionDao complexQuestionDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private ComplexQuestionService complexQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseService questionBaseService;
	//子试题服务
	//选择题
	@Autowired
	private ChoiceQuestionService choiceQuestionService;
	//填空\计算
	@Autowired
	private EssayQuestionService essayQuestionService;
	//口语写作
	@Autowired
	private SpokenQuestionService spokenQuestionService;
	//简答
	@Autowired
	private ShortQuestionService shortQuestionService;
	//单选矩阵
	@Autowired
	private MatrixQuestionService matrixQuestionService;
	@Autowired
	private WhriteQuestionService whriteQuestionService;

    public ComplexQuestionDao getComplexQuestionDao() {
        return complexQuestionDao;
    }

    public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
        this.complexQuestionDao = complexQuestionDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public MatrixQuestionService getMatrixQuestionService() {
        return matrixQuestionService;
    }

    public void setMatrixQuestionService(MatrixQuestionService matrixQuestionService) {
        this.matrixQuestionService = matrixQuestionService;
    }

    public WhriteQuestionService getWhriteQuestionService() {
        return whriteQuestionService;
    }

    public void setWhriteQuestionService(WhriteQuestionService whriteQuestionService) {
        this.whriteQuestionService = whriteQuestionService;
    }

    @Override
	public void deleteReadByQuestionId(Connection conn,int questionId) throws Exception  {
			List<Integer> list=new ArrayList<Integer>();
			
			List<Question> qs=questionService.getQuestionByTeId(conn, questionId);
			
			for(Question q:qs){
				int type=q.getQuestionTypeId();
				int id=q.getId();
				switch (type) {
				case Question.QUESTION_TYPE_DANXUAN:
				case Question.QUESTION_TYPE_DANXUAN_BOX:
				case Question.QUESTION_TYPE_DANXUAN_GRAPH:
				case Question.QUESTION_TYPE_DANXUAN_POINT:
				case Question.QUESTION_TYPE_DANXUAN_SHADE:
				case Question.QUESTION_TYPE_DUOXUAN:
				case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
				case Question.QUESTION_TYPE_DUOXUAN_SHADE:
					choiceQuestionService.deleteChoiceQuestion(conn, id);
					break;
				case Question.QUESTION_TYPE_FILL_CALCULATION:
				case Question.QUESTION_TYPE_FILL_BLANK:
					
					
					
					essayQuestionService.deleteEssayQuestion(conn, id);
					break;
				case Question.QUESTION_TYPE_SORT:
				case Question.QUESTION_TYPE_JUDGE:
					choiceQuestionService.deleteChoiceQuestion(conn, id);
					break;
				case Question.QUESTION_TYPE_SHORT:
					list=new ArrayList<Integer>();
					list.add(id);
					shortQuestionService.deleteByIds(conn, list);
					break;
				case Question.QUESTION_TYPE_SPOKEN:
					list=new ArrayList<Integer>();
					list.add(id);
					spokenQuestionService.deleteByIds(conn, list);
					break;
				case Question.QUESTION_TYPE_WHRITE:
					list=new ArrayList<Integer>();
					list.add(id);
					//whriteQuestionService.deleteByIds(conn, list);
					break;
				case Question.QUESTION_TYPE_GUIDE_WHRITE:
					list=new ArrayList<Integer>();
					list.add(id);
					//guideWhriteQuestionService.deleteByQuestionids(conn, list);
					//guideWhriteQuestionService.deleteParagraphByQuestionids(conn, list);
					break;
				default:
					break;
				}
			}
			complexQuestionDao.deleteByQuestionId(conn, questionId);
			questionBaseService.delCommQuestion(conn, questionId);
	}


	@Override
	public ComplexQuestionDto getReadByQuestionId(int questionId) throws Exception {
		Connection conn=null;
		ComplexQuestionDto readDto=new ComplexQuestionDto();
		List<IExamQuestionDto> subExamList = new ArrayList<IExamQuestionDto>();
		readDto.setSubItems(subExamList);
		try{
			conn= ConnUtil.getTransactionConnection();
		//提取本身
			ComplexQuestion complexQuestion= complexQuestionDao.getByQuestionId(questionId);
			readDto.setComplexQuestion(complexQuestion);
		//提取大题
		QuestionDto questionDto= questionService.getQuestionDtoByQuestionId(questionId);
		readDto.setQuestionDto(questionDto);
		//根据大题提取子题基本信息
		List<Question> questions=questionService.getQuestionByTeId(conn,questionId);
		//根据各个子题类型获取子题
		for(Question dto:questions){
			int type=dto.getQuestionTypeId();
			switch (type) {
				case Question.QUESTION_TYPE_DANXUAN:
				case Question.QUESTION_TYPE_DUOXUAN:
				case Question.QUESTION_TYPE_DANXUAN_BOX://单选方框
				case Question.QUESTION_TYPE_SORT://排序题
					ChoiceQuestionDto choiceQuestionDto= choiceQuestionService.getChoiceQuestion(dto.getId());
					readDto.getSubItems().add(choiceQuestionDto);
					break;
				//case Question.QUESTION_TYPE_FILL_CALCULATION:
				//case Question.QUESTION_TYPE_NORMAL_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
					EssayQuestionDto essayDTO = essayQuestionService.getEssayQuestionDTO(dto.getId());
					readDto.getSubItems().add(essayDTO);
					break;
				case Question.QUESTION_TYPE_SHORT:
					ShortQuestionDto shortQuestionDto= shortQuestionService.getShortQuestionDto(dto.getId());
					readDto.getSubItems().add(shortQuestionDto);
					break;
				case Question.QUESTION_TYPE_SPOKEN:
					SpokenQuestionDto spokenQuestionDto= spokenQuestionService.getSpokenQuestionDto(dto.getId());
					readDto.getSubItems().add(spokenQuestionDto);
					break;
				case Question.QUESTION_TYPE_TABLE:
					MatrixQuestionDto matrixQuestionDto = matrixQuestionService.getByQuestionId(dto.getId());
					readDto.getSubItems().add(matrixQuestionDto);
					break;
				case Question.QUESTION_TYPE_WHRITE:
					WhriteQuestionDto whriteQuestionDto = whriteQuestionService.getWhriteQuestionDto(dto.getId());
					readDto.getSubItems().add(whriteQuestionDto);
					break;
				default:
					break;
			}
		}
		}catch(Exception e){
			conn.rollback();
			e.fillInStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return readDto;
	}

	@Override
	public int[] saveOrUpdateRead(ComplexQuestionDto complexQuestionDto) throws Exception {
		
		int id[]=new int[2];
		Connection conn=null;
		try{
			int oldId=complexQuestionDto.getQuestionDto().getQuestion().getId();
			conn=ConnUtil.getTransactionConnection();
			if(complexQuestionDto.getQuestionDto().getQuestion().getId()!=0){
				questionDao.updateNewVersion(conn, complexQuestionDto.getQuestionDto().getQuestion().getId(), 1);
			}
			saveOrUpdateRead(conn,complexQuestionDto);
			conn.commit();
			id[0]=complexQuestionDto.getQuestionDto().getQuestion().getId();
			id[1]=complexQuestionDto.getComplexQuestion().getId();
			if(complexQuestionDto.getQuestionDto().getSaveType()== ConstantTe.QUESTION_SAVETYPE_SAVEAS){
				updateSaveAsSequence(complexQuestionDto,id[0]);
			}
			CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + oldId);
		}catch (Exception e) {
			e.fillInStackTrace();
			if(conn!=null){
				conn.rollback();
			}
			return id;
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return id;

	}
	/**
	 * 复合题目编辑前备份
	 * @param complexQuestionDto
	 * @return
	 * @throws Exception
	 */
	public int[] backReadQuestion(ComplexQuestionDto complexQuestionDto) throws Exception {
		
		int id[]=new int[2];
		Connection conn=null;
		try{
			conn=ConnUtil.getTransactionConnection();
			int questionId = complexQuestionDto.getQuestionDto().getQuestion().getId();
			//copy questionDto
			int new_id=questionService.backQuestion(conn, complexQuestionDto.getQuestionDto());
			//copy read
			//保存 read
			ComplexQuestion complexQuestion=complexQuestionDto.getComplexQuestion();
			complexQuestion.setQuestionId(new_id);
			complexQuestionDao.insert(conn, complexQuestion);
			
			List<Question> questions = questionService.getQuestionByTeId(conn, questionId); 
			subReadQuestionSaveAs(conn,questions,new_id);
			
			conn.commit();
			id[0]=complexQuestionDto.getQuestionDto().getQuestion().getId();
			id[1]=complexQuestionDto.getComplexQuestion().getId();
			if(complexQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVEAS){
				updateSaveAsSequence(complexQuestionDto,id[0]);
			}
		}catch (Exception e) {
			e.fillInStackTrace();
			if(conn!=null){
				conn.rollback();
			}
			return id;
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return id;
		
	}
	/**
	 * 包括保存更新,另存
	 * @param conn
	 * @param complexQuestionDto
	 * @throws Exception
	 */
	public void saveOrUpdateRead(Connection conn, ComplexQuestionDto complexQuestionDto) throws Exception{
		
		int questionId=complexQuestionDto.getQuestionDto().getQuestion().getId();
		int new_id=questionId;
		int id=complexQuestionDto.getComplexQuestion().getId();
		if(questionId==0){//保存
			//保存questionDto
			new_id=questionService.saveOrUpate(conn, complexQuestionDto.getQuestionDto());
			
			//保存 read
			ComplexQuestion complexQuestion=complexQuestionDto.getComplexQuestion();
			complexQuestion.setQuestionId(new_id);
			id=complexQuestionDao.insert(conn, complexQuestion);
			
			//保存子试题顺序
			String orderStr=complexQuestionDto.getSubItemOrderStr();
			if(StringUtils.isNotEmpty(orderStr)){
				questionService.updateQuestionOrder(conn,orderStr);
			}
		}else if(complexQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//审核之后另存
			//将复合题副本设置为最新版本
			questionDao.updateNewVersion(conn,questionId,1);
			//copy questionDto
			new_id=questionService.saveOrUpate(conn, complexQuestionDto.getQuestionDto());
			//copy read
			//保存 read
			ComplexQuestion complexQuestion=complexQuestionDto.getComplexQuestion();
			complexQuestion.setQuestionId(new_id);
			id=complexQuestionDao.insert(conn, complexQuestion);
			
			List<Question> questions = questionService.getQuestionByTeId(conn, questionId); 
			if(questions!=null&&questions.size()>0){
				for (Question question : questions) {
					int type = question.getQuestionTypeId();
					switch (type) {
					case Question.QUESTION_TYPE_DANXUAN:
					case Question.QUESTION_TYPE_DUOXUAN:
					//case Question.QUESTION_TYPE_JUDGE:
					case Question.QUESTION_TYPE_DANXUAN_BOX://单选方框
					case Question.QUESTION_TYPE_SORT://排序题
					case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
						choiceQuestionService.saveAs(conn,question,new_id);//OK
						break;
					case Question.QUESTION_TYPE_SHORT:
						shortQuestionService.saveAs(conn, question, new_id);//OK
						break;
					case Question.QUESTION_TYPE_FILL_BLANK:
					case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
						essayQuestionService.saveAs(conn,question,new_id);//OK
						break;
					case Question.QUESTION_TYPE_WHRITE://作文题
						whriteQuestionService.saveAs(conn,question,new_id);//OK
						break;
					case Question.QUESTION_TYPE_TABLE://表格矩阵
						matrixQuestionService.saveAs(conn,question,new_id);//OK
						break;
					default:;
						break;
					}
				}

			}
		}else{//更新
			//保存questionDto
			int question_id=questionService.saveOrUpate(conn, complexQuestionDto.getQuestionDto());
			//保存 read
			ComplexQuestion complexQuestion=complexQuestionDto.getComplexQuestion();
			complexQuestion.setQuestionId(question_id);
			complexQuestionDao.update(conn, complexQuestion);
			//保存子试题顺序
			String orderStr=complexQuestionDto.getSubItemOrderStr();
			if(StringUtils.isNotEmpty(orderStr)){
				questionService.updateQuestionOrder(conn,orderStr);
			}
		}
		complexQuestionDto.getQuestionDto().getQuestion().setId(new_id);
		complexQuestionDto.getComplexQuestion().setId(id);
	}

	/**
	 * 另存时，修改子题顺序
	 * @param complexQuestionDto
	 * @param teId
	 * @throws Exception
	 */
	private void updateSaveAsSequence(ComplexQuestionDto complexQuestionDto,int teId) throws Exception{
		Connection conn=null;
		try{
			conn=ConnUtil.getTransactionConnection();
			String orderStr=complexQuestionDto.getSubItemOrderStr();
			String[] orderArr = orderStr.split(",");
			List<Question> newQuestions = questionService.getQuestionByTeId(conn, teId);
			String[] newOrders = new String[newQuestions.size()];
			for(int i=0;i<orderArr.length;i++){
				for(Question q : newQuestions){
					Question oq = questionBaseService.getQuestionById(Integer.parseInt(orderArr[i]));
					if(q.getCode().equals(oq.getCode())){
						newOrders[i] = q.getId()+"";
						break;
					}
				}
			}
			StringBuffer orders = new StringBuffer();
			if(newOrders.length > 0){
				for(String no : newOrders){
					orders.append(no+",");
				}
				questionService.updateQuestionOrder(conn,orders.toString());
			}
			conn.commit();	

		}catch (Exception e) {
			e.fillInStackTrace();
			if(conn!=null){
				conn.rollback();
			}
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void deleteSubItem(int questionId, int typeId) throws Exception {
		Connection conn=null;
		try{
			conn=ConnUtil.getTransactionConnection();
			List<Integer> list=new ArrayList<Integer>();
			list.add(questionId);
			questionService.deleteQuestion(conn, list, typeId);
			conn.commit();
		}catch (Exception e) {
			e.fillInStackTrace();
			if(conn!=null){
				conn.rollback();
			}
			throw e;
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
				
	}
	public void saveSyncRead(Connection conn, ComplexQuestionDto complexQuestionDto) throws Exception{
		int questionId=complexQuestionDto.getQuestionDto().getQuestion().getId();
		int new_id=questionId;
		int id=complexQuestionDto.getComplexQuestion().getId();
		//copy questionDto
		new_id=questionService.saveOrUpate(conn, complexQuestionDto.getQuestionDto());
		//copy read
		//保存 read
		ComplexQuestion complexQuestion=complexQuestionDto.getComplexQuestion();
		complexQuestion.setQuestionId(new_id);
		id=complexQuestionDao.insert(conn, complexQuestion);
		
		List<IExamQuestionDto> questions = complexQuestionDto.getSubItems();
		if(questions!=null&&questions.size()>0){
			for (IExamQuestionDto subDto : questions) {
				Question question = subDto.getQuestionDto().getQuestion();
				question.setTeId(new_id);
				question.setId(0);
				int type = question.getQuestionTypeId();
				switch (type) {
				case Question.QUESTION_TYPE_DANXUAN:
				case Question.QUESTION_TYPE_DUOXUAN:
				//case Question.QUESTION_TYPE_JUDGE:
				case Question.QUESTION_TYPE_DANXUAN_BOX://单选方框
				case Question.QUESTION_TYPE_SORT://排序题
				case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
					choiceQuestionService.saveOrUpdate(conn, (ChoiceQuestionDto)subDto);//OK
					break;
				case Question.QUESTION_TYPE_SHORT:
					shortQuestionService.saveOrUpdate(conn, (ShortQuestionDto)subDto);//OK
					break;
				case Question.QUESTION_TYPE_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
					essayQuestionService.saveOrUpdate(conn,(EssayQuestionDto)subDto);//OK
					break;
				case Question.QUESTION_TYPE_TABLE://矩阵题 （表格，拖拽）
					matrixQuestionService.saveOrUpdate(conn,(MatrixQuestionDto)subDto,ConstantTe.QUESTION_SAVETYPE_SAVE);//OK
					break;
				case Question.QUESTION_TYPE_WHRITE://作文题
					whriteQuestionService.saveOrUpdate(conn,(WhriteQuestionDto)subDto);
					break;
				default:;
					break;
				}
			}

		}
	}
	/**
	 * 复合子题保存
	 */
	private void subReadQuestionSaveAs(Connection conn,List<Question> questions,int new_id)throws Exception{
		if(questions!=null&&questions.size()>0){
			for (Question question : questions) {
				int type = question.getQuestionTypeId();
				switch (type) {
				case Question.QUESTION_TYPE_DANXUAN:
				case Question.QUESTION_TYPE_DUOXUAN:
				//case Question.QUESTION_TYPE_JUDGE:
				case Question.QUESTION_TYPE_DANXUAN_BOX://单选方框
				case Question.QUESTION_TYPE_SORT://排序题
				case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
					choiceQuestionService.saveAs(conn,question,new_id);//OK
					break;
				case Question.QUESTION_TYPE_SHORT:
					shortQuestionService.saveAs(conn, question, new_id);//OK
					break;
				case Question.QUESTION_TYPE_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
					essayQuestionService.saveAs(conn,question,new_id);//OK
					break;
				case Question.QUESTION_TYPE_WHRITE://作文题
					whriteQuestionService.saveAs(conn,question,new_id);//OK
					break;
				case Question.QUESTION_TYPE_TABLE://表格矩阵
					matrixQuestionService.saveAs(conn,question,new_id);//OK
					break;
				default:;
					break;
				}
			}

		}
	}

	@Override
	public List<ComplexQuestionDto> batchFindRepository(List<Integer> ids) throws Exception {
		
		List<ComplexQuestionDto> list=new ArrayList<ComplexQuestionDto>();
		
		return list;
	}

	public ComplexQuestionService getComplexQuestionService() {
		return complexQuestionService;
	}

	public void setComplexQuestionService(ComplexQuestionService complexQuestionService) {
		this.complexQuestionService = complexQuestionService;
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

	public SpokenQuestionService getSpokenQuestionService() {
		return spokenQuestionService;
	}

	public void setSpokenQuestionService(SpokenQuestionService spokenQuestionService) {
		this.spokenQuestionService = spokenQuestionService;
	}

	public ShortQuestionService getShortQuestionService() {
		return shortQuestionService;
	}

	public void setShortQuestionService(ShortQuestionService shortQuestionService) {
		this.shortQuestionService = shortQuestionService;
	}

}
