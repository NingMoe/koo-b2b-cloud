package com.koolearn.cloud.exam.examcore.exam.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.ExamResultStatus;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examProcess.util.JudgeQuestion;
import com.koolearn.cloud.exam.examProcess.util.QuestionUtil;
import com.koolearn.cloud.exam.examcore.exam.dao.ExamResultDao;
import com.koolearn.cloud.exam.examcore.exam.dao.ExamResultDetailDao;
import com.koolearn.cloud.exam.examcore.exam.dao.ExamResultSearchDao;
import com.koolearn.cloud.exam.examcore.exam.dao.ExamResultStructureDao;
import com.koolearn.cloud.exam.examcore.exam.dto.SearchResultDto;
import com.koolearn.cloud.exam.examcore.exam.entity.TpErrorNote;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;
import com.koolearn.cloud.exam.examcore.exam.service.ExamResultService;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examspread.exam.dto.ExamResultSearchDto;
import com.koolearn.cloud.exam.examspread.exam.entity.ExamResultSearch;
import com.koolearn.cloud.login.dao.LoginDao;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dao.ExamDao;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;


public class ExamResultServiceImpl implements ExamResultService {
    private static Logger logger=Logger.getLogger(ExamResultServiceImpl.class);
	private ExamResultDao examResultDao;
	private LoginDao loginDao;
	private ExamResultDetailDao examResultDetailDao;
	private ExamResultSearchDao resultSearchDao;
	/** 考试结果结构 */
	@Autowired(required = true)
	private ExamResultStructureDao resultStructureDao;
	/** 考试DAO */
	@Autowired(required = true)
	private ExamDao examDao;
	/** 题目Service */
	@Autowired(required = true)
	private QuestionBaseService questionBaseService;
	public void setExamResultDao(ExamResultDao examResultDao) {
		this.examResultDao = examResultDao;
	}

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	public void setExamResultDetailDao(ExamResultDetailDao examResultDetailDao) {
		this.examResultDetailDao = examResultDetailDao;
	}

	public void setResultSearchDao(ExamResultSearchDao resultSearchDao) {
		this.resultSearchDao = resultSearchDao;
	}

	public void setResultStructureDao(ExamResultStructureDao resultStructureDao) {
		this.resultStructureDao = resultStructureDao;
	}

	public void setExamDao(ExamDao examDao) {
		this.examDao = examDao;
	}

	public void setQuestionBaseService(QuestionBaseService questionBaseService) {
		this.questionBaseService = questionBaseService;
	}
	
	

	@Override
	public int editResultForSubmit(int rid, int status, String userResultStr) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			/* 获取考试结果 */
			List<Integer> ids = new ArrayList<Integer>(0);
			ids.add(Integer.valueOf(rid));
			TpExamResult result = this.examResultDao.findResultsByIds(ids).get(0);
			if (ExamResultStatus.SUBMITTED.getValue() != result.getStatus()) // 如果是已提交的状态直接跳过，不执行
			{
				/* 获取考试信息 */
//				TpExam examEntity = this.examDao.queryExamById(result.getExamId());
				/* 拆分答案 */
				String[] urArr = userResultStr.split("\",\"");
				Map<String, String> urMap = new HashMap<String, String>(0);
				for (String string : urArr)
				{
					String[] temp = string.split("\":\"");
					String key = temp[0].substring(temp[0].trim().lastIndexOf("_") + 1);
					String value = "";
					try
					{
						value = temp[1].trim().replace("\\n", "").replace("\\t", "");
						if((value.lastIndexOf("\"")+1)==value.length()){
							value = value.substring(0,value.length()-1) + value.substring(value.length()-1).replace("\"", "");
						}
					}
					catch (Exception e)
					{
						value = "";
					}
                    logger.info("结果id："+rid+" 学生答案解析,题id："+key+"  答案： "+value);
					urMap.put(key, value);
				}
				/* 明细判题 */
				List<TpExamResultDetail> details = this.examResultDetailDao.selectByResultId(rid);
				for (int i = 0; i < details.size(); i++)
				{
					TpExamResultDetail temp = details.get(i);
					temp.setUserAnswer(urMap.get(String.valueOf(temp.getQuestionId())));
					IExamQuestionDto qdto = null;
					if (temp.getQuestionTypeId() == 2) // 如果是普通填空题
					{
						qdto = this.questionBaseService.getExamQuestionNoCache(temp.getQuestionTypeId(), temp.getQuestionId());
					}
					details.set(i, JudgeQuestion.judge(temp, qdto));
				}
				/* 更新明细 */
				List<TpExamResultDetail> group = QuestionUtil.groupDetail(details);
				result.setScore(Double.valueOf(0.0)); // 将原有的分数置零
//				List<TpErrorNote> errorNotes = new ArrayList<TpErrorNote>();
				for (TpExamResultDetail detail : group)
				{
					if (detail.getResultAnswer() == 0 && detail.getSubjective() == 0 && status == ExamResultStatus.SUBMITTED.getValue())
					{
						IExamQuestionDto qdto = this.questionBaseService.getExamQuestionNoCache(detail.getQuestionTypeId(), detail.getQuestionId());
						
						detail.setResultAnswer(0);
						/* 写入错题本 */
						TpErrorNote en = new TpErrorNote();
//						en.setExamId(Integer.valueOf(result.getExamId()));
//						en.setResultId(detail.getResultId());
						en.setQuestionId(detail.getQuestionId());
						en.setQuestionCode(detail.getQuestionCode());
						en.setStudentId(result.getStudentId());
//						en.setType(examEntity.getType());
						en.setTagFullPath(qdto.getQuestionDto().getQuestion().getTagPath());
						en.setStatus(TpErrorNote.STATUS_VALID);
						en.setTimes(1);
						//查询是否已经存在该错题
						TpErrorNote note = this.examDao.findErrorNote(en);
						if(note!=null){
							note.setStatus(TpErrorNote.STATUS_VALID);
							note.setTimes(note.getTimes() + 1);
							this.examDao.updateErrorNote(conn,note);
						}else{
							this.examDao.insertErrorNote(conn, en);
						}
//						errorNotes.add(en);
					}
					result.setScore(BigDecimal.valueOf(result.getScore()).add(BigDecimal.valueOf(detail.getScore())).doubleValue());
				}
				
				this.examResultDetailDao.updateDetailBatch(conn, details); // 批量更新明细
				
//                if(errorNotes!=null&&errorNotes.size()>0){
//                	this.examDao.insertErrorNote(conn, errorNotes); // 批量写入错题本
//                }
				/* 更新结果 */
				if (status == ExamResultStatus.SUBMITTED.getValue())
				{
					result.setStatus(ExamResultStatus.SUBMITTED.getValue());
				}
				else if (status == ExamResultStatus.PROCESSING.getValue())
				{
					result.setStatus(ExamResultStatus.PROCESSING.getValue());
				}
				/*由此向下为雅思特殊处理*/
				// 判断是否为雅思试卷考试
				// 如果是，特殊处理
					// 处理听力和阅读
					// 计算正确题目个数
					// 原有总分减去已得分数
					// 按照公式给定新的分数
				// 如果不是，过
				/*由此向上为雅思特殊处理*/
				result.setObjectivesScore(result.getScore());
				result.setTimeOff(Double.valueOf((new Date().getTime() - result.getBeginTime().getTime()) / 1000).intValue());
				this.examResultDao.updateResult(conn, result);
				conn.commit();
				return 1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}

		return 0;
	}
	
	/**
	 * 根据考试结果查询ID查询结果
	 */
	@Override
	public TpExamResult searchById(int resultId) throws Exception
	{
		TpExamResult result = null;
		try
		{
			List<Integer> ids = new ArrayList<Integer>(0);
			ids.add(resultId);
			List<TpExamResult> rs = this.examResultDao.findResultsByIds(ids);
			if (null != rs && rs.size() == 1)
			{
				result = rs.get(0);
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
	 * 根据考试过程对象获取考试结果所有信息
	 */
	@Override
	public Process searchFullByProcess(Process process) throws Exception
	{
		try
		{
			List<TpExamResultStructure> f = this.resultStructureDao.selectByResultId(process.getExamResult().getId());
			process.setExamResultStructures(this.resultStructureDao.selectByResultId(process.getExamResult().getId()));
			process.setExamResultDetails(this.examResultDetailDao.selectByResultId(process.getExamResult().getId()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return process;
	}
	
	/**
	 * 更新主观题得分(score)和题目正确性(result_answer)
	 * 老师批阅时，主观题判分写结果表（te_exam_result_detail）
	 * @throws Exception 
	 */
	@Override
	public int updateDetailScoreResultAnswer(List<TpExamResultDetail> examResultDetails, Map<String, String> map, TpExamResult examResult) throws Exception {
		int count = 0;
		Connection conn = ConnUtil.getTransactionConnection();
		Map<Integer,Integer> bigResultAnswer = new HashMap<Integer, Integer>();//大题正确性集合
		Map<Integer,Double> bigUserScore = new HashMap<Integer, Double>();//大题正确性集合
		try {
			for (int i = 0; i < examResultDetails.size(); i++) {
				TpExamResultDetail detail = examResultDetails.get(i);
				if(detail.getSubjective().intValue()==TpExamResultDetail.SUBJECTIVE){//主观题老师打分
					String resultAnswer = map.get("result_answer_"+detail.getId());
					String score = map.get("score_"+detail.getId());
					if(StringUtils.isNotBlank(resultAnswer)&&StringUtils.isNotBlank(score)){
//						if(detail.getTeId().intValue()==0){
//							updateDetailScoreResultAnswerSub(conn,detail,examResultDetails,examResult,Integer.valueOf(resultAnswer),Double.valueOf(score));
//						}
						//将所有主观题判题后的大题正确性结果放到map中
						if("2".equals(resultAnswer)||"0".equals(resultAnswer)){
							bigResultAnswer.put(detail.getTeId(), TpExamResultDetail.RESULT_ANSWER_NOT_CORRECT);
						}
						//将所有主观题判题后的得分放到大题map中
						Double userScore = bigUserScore.get(detail.getTeId());
						if(userScore==null){
							bigUserScore.put(detail.getTeId(), Double.valueOf(score));
						}else{
							userScore = userScore + Double.valueOf(score);
							bigUserScore.put(detail.getTeId(), userScore);
						}
						//更新小题题目分数及答题结果
						count = this.examResultDetailDao.updateDetailScoreResultAnswer(conn,detail.getId(),resultAnswer,score);
					}
					if(String.valueOf(TpExamResultDetail.RESULT_ANSWER_NOT_CORRECT).equals(resultAnswer)
							||String.valueOf(TpExamResultDetail.RESULT_ANSWER_PART_CORRECT).equals(resultAnswer)){//主观题判题为错或部分正确的时候写错题本
						/** 记录错题本 start */
						IExamQuestionDto qdto = this.questionBaseService.getExamQuestionNoCache(detail.getQuestionTypeId(), detail.getQuestionId());
						/* 写入错题本 */
						TpErrorNote en = new TpErrorNote();
						en.setQuestionId(detail.getQuestionId());
						en.setQuestionCode(detail.getQuestionCode());
						en.setStudentId(examResult.getStudentId());
						en.setTagFullPath(qdto.getQuestionDto().getQuestion().getTagPath());
						en.setStatus(TpErrorNote.STATUS_VALID);
						en.setTimes(1);
						//查询是否已经存在该错题
						TpErrorNote note = this.examDao.findErrorNote(en);
						if(note!=null){//已存在错误本中的错题只更新次数
							note.setStatus(TpErrorNote.STATUS_VALID);//移除过的错题，再次加入错本，将原有错状态改为有效，并加上原有错次数，杨军需求
							note.setTimes(note.getTimes() + 1);
							this.examDao.updateErrorNote(conn,note);
						}else{
							this.examDao.insertErrorNote(conn, en);
						}
						/** 记录错题本 end */
					}
				}
			}
			for (Map.Entry<Integer, Integer> bigQuestion:bigResultAnswer.entrySet()) {
				/** 主观题批阅更新大题正确性 */
				examResultDetailDao.updateDetailBigQuestionResultAnswer(conn, examResult.getId(),bigQuestion.getKey(),bigQuestion.getValue());
			}
			for (Map.Entry<Integer, Double> bigQuestionScore:bigUserScore.entrySet()) {
				/** 主观题批阅更新大题得分 */
				examResultDetailDao.updateDetailBigQuestionScore(conn, examResult.getId(),bigQuestionScore.getKey(),bigQuestionScore.getValue());
			}
			examResultDao.updateResult(conn, examResult);//更新结果表中的得分和老师批阅状态
			conn.commit();
		} catch (SQLException e) {
			ConnUtil.rollbackConnection(conn);
			e.printStackTrace();
		}finally{
			ConnUtil.closeConnection(conn);
		}
		return count;
	}

	/**
	 * 同updateDetailScoreResultAnswer方法.
	 * 此方法为大题批阅结果得分均摊到子题得分,并设置子题的题目正确性同大题
	 * @param examResult 
	 * @throws Exception 
	 */
	private void updateDetailScoreResultAnswerSub(Connection conn,TpExamResultDetail detailParent,List<TpExamResultDetail> examResultDetails, 
			TpExamResult examResult, Integer resultAnswer,Double score) throws Exception {
		//此list为子题更新数据
		List<TpExamResultDetail> list = new ArrayList<TpExamResultDetail>();
		for (int i = 0; i < examResultDetails.size(); i++) {
			TpExamResultDetail detail = examResultDetails.get(i);
			//判断是否为detailParent的子题,是子题则处理子题的结果明细数据
			if(detailParent.getQuestionId().intValue()==detail.getTeId()){
				TpExamResultDetail de = new TpExamResultDetail();
				de.setResultAnswer(Integer.valueOf(resultAnswer));
				de.setScore(Double.valueOf(score));
				de.setId(detail.getId());
				list.add(de);
				//主观题判题为错或部分正确的时候写错题本
				if(TpExamResultDetail.RESULT_ANSWER_NOT_CORRECT==resultAnswer.intValue()
						||TpExamResultDetail.RESULT_ANSWER_PART_CORRECT==resultAnswer.intValue()){
					/** 记录错题本 start */
					IExamQuestionDto qdto = this.questionBaseService.getExamQuestionNoCache(detail.getQuestionTypeId(), detail.getQuestionId());
					/* 写入错题本 */
					TpErrorNote en = new TpErrorNote();
					en.setQuestionId(detail.getQuestionId());
					en.setQuestionCode(detail.getQuestionCode());
					en.setStudentId(examResult.getStudentId());
					en.setTagFullPath(qdto.getQuestionDto().getQuestion().getTagPath());
					en.setStatus(TpErrorNote.STATUS_VALID);
					en.setTimes(1);
					//查询是否已经存在该错题
					TpErrorNote note = this.examDao.findErrorNote(en);
					if(note!=null){//已存在错误本中的错题只更新次数
						note.setStatus(TpErrorNote.STATUS_VALID);//移除过的错题，再次加入错本，将原有错状态改为有效，并加上原有错次数，杨军需求
						note.setTimes(note.getTimes() + 1);
						this.examDao.updateErrorNote(conn,note);
					}else{
						this.examDao.insertErrorNote(conn, en);
					}
					/** 记录错题本 end */
				}
			}
		}
		int count = list.size();//子题数目
		if(count>0){
			double end = round(score-round(score/count,1,1)*(count-1),1,1);//如果除不尽则将在最后一个得分处做精度处理
			double subScore = 0;
			for (int i = 0; i < count; i++) {
				TpExamResultDetail resultDetail = list.get(i);
				if(count-1!=i){
					subScore = round(score/count,1,1);
				}else{
					subScore = round(end,1,1);
				}
				System.out.println("resultDetail.getId()="+resultDetail.getId()+"   subScore="+subScore+"    resultAnswer="+resultAnswer);
				this.examResultDetailDao.updateDetailScoreResultAnswer(conn,resultDetail.getId(),String.valueOf(resultAnswer),String.valueOf(subScore));
			}
		}
	}
	/**
	 * double精度处理
	 * @param value
	 * @param scale
	 * @param roundingMode
	 * @return
	 */
	public static double round(double value, int scale, 
            int roundingMode) {   
       BigDecimal bd = new BigDecimal(value);   
       bd = bd.setScale(scale, roundingMode);   
       double d = bd.doubleValue();   
       bd = null;   
       return d;   
   }   

	/**
	 * 更新主观题图片路径
	 */
	@Override
	public int updateDetailSubjectiveUrl(String questionId, String resultId,String realPath) {
		return examResultDetailDao.updateDetailSubjectiveUrl(questionId, resultId,realPath);
	}
	
	/** 作业批阅  保存老师评语 */
	@Override
	public int saveMarkReply(int resultId, String reply) {
		return examResultDao.saveMarkReply(resultId,reply);
	}
	
	
	
	/**
	 * 根据考试结果id获取考试结果所有信息
	 */
	@Override
	public List<TpExamResultDetail> searchFullExamResultDetail(int resultId) throws Exception
	{
		return this.examResultDetailDao.selectByResultId(resultId);
	}
	
	/**
	 * 所有主观题考试结果明细集合
	 * @param subjective 1主观题
	 */
	@Override
	public List<TpExamResultDetail> searchResultDetailSubjective(Integer resultId,
			int subjective) {
		return this.examResultDetailDao.selectSubjectiveByResultId(resultId,subjective);
	}
	
	/**
	 * 所有某考试结果明细集合
	 */
	@Override
	public List<TpExamResultDetail> searchResultDetail(Integer resultId) {
		return this.examResultDetailDao.searchResultDetail(resultId);
	}
	
	//以上方法为更新后方法
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public ExamResultSearchDto findResult4SubjectiveByExamId(SearchResultDto searchResultDto) {
		ExamResultSearchDto result=new ExamResultSearchDto();
		int count=resultSearchDao.findItems4SearchCount(searchResultDto);
		if(count==0){
			return result;
		}
		result.setCount(count);
		List<ExamResultSearch> list2=null;
		list2=resultSearchDao.findItems4Search(searchResultDto);
		if(CollectionUtils.isEmpty(list2)){
			return result;
		}
		result.setList(list2);

		return result;
	}


	public ExamResultSearchDao getExamResultSearchDao() {
		return resultSearchDao;
	}

	public void setExamResultSearchDao(ExamResultSearchDao examResultSearchDao) {
		this.resultSearchDao = examResultSearchDao;
	}

	//获取最新考试结果
	@Override
	public TpExamResult searchLast(int examId, int studentId) throws Exception
	{
		TpExamResult result = null;
		try
		{
			result = this.examResultDao.selectLast(examId, studentId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Process saveByProcess(Process process)  throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			int erId = this.examResultDao.insert(conn, process.getExamResult()); // 保存考试结果
			process.getExamResult().setId(erId);
			UserEntity student = null;
			List<UserEntity> list = loginDao.findClassesStudent(process.getStudentId());
			if(!list.isEmpty()){
				student = list.get(0);
			}
			//查询班级
//			int classId = schoolManageDao.getClassIdByStudentId(student.getId());
//			student.setClassId(classId);

			process.setStudent(student);
//			ExamResultExt ext = new ExamResultExt();
//			Integer examclassid=student.getClassesId();
//			ext.setClassId(examclassid);
//			ext.setExamId(process.getExam().getId().intValue());
//			ext.setResultId(erId);
//			ext.setSchoolId(student.getSchoolId());
//			ext.setStudentId(student.getId());
//			ext.setTeacherId(process.getExam().getTeacherId().intValue());
//			this.extDao.insert(conn, ext);
			@SuppressWarnings("unchecked")
			List<TestPaperStructureDto> paperTree = CacheTools.getCache(ConstantTe.PROCESS_EXAM_PAPER_TREE + process.getExam().getId(), List.class);
			if (null == paperTree || paperTree.size() < 1)
			{
				paperTree = process.getTestPaperDto().getStructuresTree(); // 获取树形试卷结构
				CacheTools.addCache(ConstantTe.PROCESS_EXAM_PAPER_TREE + process.getExam().getId(), ConstantTe.CACHE_TIME, paperTree);
			}
			process = this.handlerSD(conn, erId, paperTree, process, 0, 1); // 处理考试结果结构和明细
			conn.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
		return process;
	}

	/**
	 * 处理考试结果结构和明细， sd ==> Structure And Detail
	 * @param conn
	 * @param erId
	 * @param list
	 * @param process
	 * @param parent
	 * @param odr
	 * @throws Exception
	 * @author DuHongLin
	 */
	private Process handlerSD(Connection conn, int erId, List<TestPaperStructureDto> list, Process process,int parent,int odr) throws Exception
	{
		TpExamResultStructure structure = null;
		TpExamResultDetail detail = null;
		for (TestPaperStructureDto temp : list)
		{
			structure = new TpExamResultStructure();
			TestPaperStructure ps = temp.getTestPaperStructure();
			structure.setResultId(erId);
			structure.setDescript(ps.getDescript());
			structure.setParent(parent);
			structure.setOdr(odr ++);
			structure.setName(temp.getTestPaperStructure().getName());
			structure.setPoint(Double.valueOf(ps.getPoints().toString()));
			structure.setTimeout(ps.getTimeout());
			structure.setType(ps.getStructureType());
			int structureId = this.resultStructureDao.insert(conn, structure);
			structure.setId(structureId);
			process.getExamResultStructures().add(structure);
			if (TestPaperStructure.structure_type_question == ps.getStructureType())
			{
				IExamQuestionDto eqDto = process.getMqcs().get(ps.getName());
				try {
					process = this.handlerDetail(conn, detail, temp, eqDto, erId, structureId, process, eqDto.getQuestionType(),null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (TestPaperStructure.structure_type_structure == ps.getStructureType())
			{
				List<TestPaperStructureDto> cdtos = temp.getChildren();
				if (null != cdtos && cdtos.size() > 0)
				{
					process = this.handlerSD(conn, erId, cdtos, process, structureId, odr * 100);
				}
			}
		}
		return process;
	}

	/**
	 * 处理考试结果明细写入数据库
	 * @param conn
	 * @param detail
	 * @param temp
	 * @param eqDto
	 * @param erId
	 * @param structureId
	 * @param process
	 * @param teType
	 * @return
	 * @author DuHongLin
	 */
	private Process handlerDetail(Connection conn, TpExamResultDetail detail, TestPaperStructureDto temp, IExamQuestionDto eqDto, int erId, int structureId, Process process, int teType, IExamQuestionDto parentDto)
	{
		detail = new TpExamResultDetail();
		String ds = process.getTestPaperDto().getPaper().getId() + "_" + eqDto.getQuestionDto().getQuestion().getCode();
		if (null != process.getSubScores().get(ds)) // 当前题是否含有子题分数数据
		{
			// 取子题分数数据
			detail.setPoints(Double.valueOf(process.getSubScores().get(process.getTestPaperDto().getPaper().getId() + "_" + eqDto.getQuestionDto().getQuestion().getCode()).getPoints())); // 分值
		}
		else // 当前题不含有子题分数数据
		{
			if (null != parentDto) // 如果有父级题目
			{
				String ps = process.getTestPaperDto().getPaper().getId() + "_" + parentDto.getQuestionDto().getQuestion().getCode();
				if (null != process.getSubScores().get(ps)) // 如果父级含有分数数据
				{
					int countSub = parentDto.getSubQuestions().size(); // 子题个数
					double p = process.getSubScores().get(ps).getPoints(); // 分数数据
					double sp = p / countSub; // 平均分
					detail.setPoints(Double.valueOf(String.valueOf(sp)));
				}
				else if (parentDto.haveSubQuestion()) // 如果父级不含有分数数据且含有子题
				{
					int countSub = parentDto.getSubQuestions().size(); // 子题个数
					double p = temp.getTestPaperStructure().getPoints(); // 当前节点分数
					double sp = p / countSub; // 平均分
					detail.setPoints(Double.valueOf(String.valueOf(sp)));
				}
				else // 如果不含有子题分数数据其不含有子题
				{
					detail.setPoints(Double.valueOf(temp.getTestPaperStructure().getPoints().toString())); // 分值
				}
			}
			else // 如果没有父级题目
			{
				detail.setPoints(Double.valueOf(temp.getTestPaperStructure().getPoints().toString())); // 分值
			}
		}
		detail.setQuestionId(eqDto.getQuestionDto().getQuestion().getId()); // 题目ID
		detail.setQuestionTypeId(eqDto.getQuestionType()); // 题目类型
		detail.setResultAnswer(-1); // 是否正确，首次创建无判题
		detail.setResultId(erId); // 结果ID
		detail.setRightAnswer(eqDto.getRightAnswer()); // 正确答案
		detail.setScore(0.0); // 得分，首次创建为0分
		detail.setExamResultStructure(structureId); // 结构ID
		detail.setSubjective(eqDto.getQuestionDto().getQuestion().getIssubjectived() == 1 ? 0 : 1); // 是否是主观题
		detail.setTeId(eqDto.getQuestionDto().getQuestion().getTeId()); // 题目父级ID
		detail.setUserAnswer(""); // 用户答案，首次创建无用户答案
		detail.setReply(""); // 老师批复，首次创建无批复
//		detail.setTag1(eqDto.getQuestionDto().getQuestionTag1());
//		detail.setTag2(eqDto.getQuestionDto().getQuestionTag2());
//		detail.setTag3(eqDto.getQuestionDto().getQuestionTag3());
		detail.setQuestionCode(eqDto.getQuestionDto().getQuestion().getCode());
		detail.setTeType(teType);
		int did = this.examResultDetailDao.insert(conn, detail);

//		if (eqDto.getQuestionDto().getQuestion().getIssubjectived() == 0 && eqDto.getQuestionType() != Question.QUESTION_TYPE_CLOZE_FILL_BLANK && eqDto.getQuestionType() != Question.QUESTION_TYPE_CORRECTION) // 为主观题时
//		{
//			ExamResultSearch resultSearch = new ExamResultSearch();
//
//
//			Integer examclassid=process.getStudent().getClassesId();
////			if(process.getExam().getIspart().equals(Constant.EXAM_ISPART_YES)){
////				examclassid=schoolManageDao.getPartExamClassID(process.getExam().getId());
////			}
//
//			resultSearch.setClassId(examclassid);
//			resultSearch.setExamId(process.getExam().getId().intValue());
//			resultSearch.setPgStatus(0);
//			resultSearch.setQuestionId(detail.getQuestionId());
//			//tagId
////			if(eqDto.getQuestionDto().getQuestionTag3()==0&&parentDto!=null){
////				resultSearch.setQuestionTagType(parentDto.getQuestionDto().getQuestionTag3());
////			}else{
////				resultSearch.setQuestionTagType(eqDto.getQuestionDto().getQuestionTag3());
////			}
//			resultSearch.setQuestionType(detail.getQuestionTypeId());
//			resultSearch.setResultId(erId);
//			resultSearch.setSchoolId(process.getStudent().getSchoolId());
//			resultSearch.setStudentId(process.getStudentId());
//			resultSearch.setTeacherId(process.getExam().getTeacherId().intValue());
//			resultSearch.setTeId(eqDto.getQuestionDto().getQuestion().getTeId());
//			this.resultSearchDao.insert(conn, resultSearch);
//		}


		detail.setId(did);
		process.getExamResultDetails().add(detail);
		if (eqDto.haveSubQuestion())
		{
			List<IExamQuestionDto> ddtos = eqDto.getSubQuestions();
			for (IExamQuestionDto dto : ddtos)
			{
				//给子题增加三级标签
//				dto.getQuestionDto().setQuestionBankExt(eqDto.getQuestionDto().getQuestionBankExt());
				process = this.handlerDetail(conn, detail, temp, dto, erId, structureId, process, eqDto.getQuestionType(),eqDto);
			}
		}
		return process;
	}



	@Override
	public void edit(TpExamResult examResult,
			List<TpExamResultStructure> examResultStructures,
			List<TpExamResultDetail> examResultDetails) throws Exception
	{
		try
		{

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public TpExamResultDetail findOne(int questionId, int structureId) {
		return resultStructureDao.selectByStructureId(questionId,structureId);
	}

	@Override
	public void updateDetail(int detailId, float score, String reply) {
		examResultDetailDao.updateItem(detailId,score,reply);
	}

	@Override
	public void editResult(TpExamResult examResult) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			this.examResultDao.updateResult(conn, examResult);
			conn.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
	}

	

	@Override
	public Map<String, TpExamResult> searchLasts(List<Integer> eids, int uid)
			throws Exception
	{
		Map<String, TpExamResult> result = new HashMap<String, TpExamResult>(0);
		try
		{
			for (Integer eid : eids)
			{
				result.put(uid + "_" + eid, this.examResultDao.selectLast(eid.intValue(), uid));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	public TpExamResultDetail selectByQuestionIdResultId(int questionId, int resultId){
		return examResultDetailDao.selectByQuestionIdResultId(questionId, resultId);
	}

	@Override
	public void reply(String replyStr, double score, int resultId, int questionId, int teId, ExamResultSearch search) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			TpExamResultDetail detail = this.examResultDetailDao.selectByQuestionIdResultId(questionId, resultId);
			detail.setScore(score); // 得分
			detail.setReply(replyStr); // 批复
			detail.setResultAnswer(1); // 正确性质
			this.examResultDetailDao.updateDetail(conn, detail);
			if (teId > 0)
			{
				TpExamResultDetail detailTe = this.examResultDetailDao.selectByQuestionIdResultId(teId, resultId);
				detailTe.setScore(detailTe.getScore() + score);
				this.examResultDetailDao.updateDetail(conn, detailTe);
			}
			List<Integer> ids = new ArrayList<Integer>(0);
			ids.add(resultId);
			TpExamResult examResult = this.examResultDao.findResultsByIds(ids).get(0);
			examResult.setScore(examResult.getScore() + score);
			this.examResultDao.updateResult(conn, examResult);
			search.setPgStatus(1);
			search.setRtime(new Date());
			this.resultSearchDao.updateSearch(conn, search);
			conn.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
	}
	//批改记录查询
	public ExamResultSearch findExamResultSearchById(Integer id) {
		return resultSearchDao.findById(id);
	}

    @Override
	public List<Process> saveByProcesses(List<Process> processes) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = ConnUtil.getTransactionConnection();
			for (int i = 0; i < processes.size(); i++)
			{
				int erId = this.examResultDao.insert(conn, processes.get(i).getExamResult()); // 保存考试结果
				processes.get(i).getExamResult().setId(erId);
				UserEntity student = this.loginDao.getStudentById(processes.get(i).getStudentId());
				//查询班级
//				int classId = schoolManageDao.getClassIdByStudentId(student.getId());
//				student.setClassId(classId);
//				processes.get(i).setStudent(student);
//				ExamResultExt ext = new ExamResultExt();
//				ext.setClassId(student.getClassId());
//				ext.setExamId(processes.get(i).getExam().getId().intValue());
//				ext.setResultId(erId);
//				ext.setSchoolId(student.getSchoolId());
//				ext.setStudentId(student.getId());
//				ext.setTeacherId(processes.get(i).getExam().getCreator().intValue());
//				this.extDao.insert(conn, ext);
				@SuppressWarnings("unchecked")
				List<TestPaperStructureDto> paperTree = CacheTools.getCache(ConstantTe.PROCESS_EXAM_PAPER_TREE + processes.get(i).getExam().getId(), List.class);
				if (null == paperTree || paperTree.size() < 1)
				{
					paperTree = processes.get(i).getTestPaperDto().getStructuresTree(); // 获取树形试卷结构
					CacheTools.addCache(ConstantTe.PROCESS_EXAM_PAPER_TREE + processes.get(i).getExam().getId(), 60 * 20, paperTree);
				}
				processes.set(i, this.handlerSD(conn, erId, paperTree, processes.get(i), 0, 1)); // 处理考试结果结构和明细
			}
			conn.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (null != conn)
			{
				conn.close();
			}
		}
		return processes;
	}

    /**
     * 本次考试批改完毕，修改考试成绩发布状态issue_result=1
     * @param examId
     */
    @Override
    public void changeIssueResultStatus(int examId) {
       int notPgNum=examResultDao.findNotPgNum(examId);
        if(notPgNum<1){
            //批改完毕，修改考试issue_result=1发布成绩状态
        	examResultDao.updateExamIssueResult(examId);
        }
    }

	@Override
	public UserEntity queryStudentByExamresultID(int rsid) {
		return examResultDao.queryStudentByExamresultID(rsid);
	}

	@Override
	public TpExam queryExamByExamresultID(int rsid) {
		// TODO Auto-generated method stub
		return  examResultDao.queryExamByExamresultID(rsid);
	}



}
