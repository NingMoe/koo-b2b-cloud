package com.koolearn.cloud.exam.examProcess.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.ExamResultStatus;
import com.koolearn.cloud.exam.examProcess.ExamType;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.service.ExamResultService;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examProcess.util.DateUtil;
import com.koolearn.cloud.exam.examProcess.validate.ValidateExamTime;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.service.ExamService;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.CacheTools;

/**
 * @author DuHongLin 考试过程基础控制器
 */
public class ProcessBaseController extends BaseController implements
		Serializable
{

	private static final long serialVersionUID = -2577405913518559538L;
	/** 考试信息 */
	@Autowired(required = true)
	protected ExamService examService;

	/** 试卷信息 */
	@Autowired(required = true)
	protected TestPaperService testPaperService;

	/** 题目信息 */
	@Autowired(required = true)
	protected QuestionBaseService questionBaseService;

	/** 考试结果 */
	@Autowired(required = true)
	protected ExamResultService resultService;

	/** 返回地址前缀 */
	protected String prePath = "student/examProcess/";

	/**
	 * 将考试类型放到作用域中
	 * 
	 * @param request
	 * @author DuHongLin
	 */
	protected void setExamType(HttpServletRequest request)
	{
		request.setAttribute("etE", ExamType.EXAM.getValue());
		request.setAttribute("etL", ExamType.LESSON.getValue());
	}

	/**
	 * 处理考试
	 * @param process
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	protected Process handlerExam(Process process) throws Exception // 处理考试
	{
		Process p ;
		Map<String, Object> resultMap = new HashMap<String, Object>(0); // 结果信息
		resultMap.put("path", "Start"); // 返回路径
		resultMap = DateUtil.CalDuration(process.getExam().getStartTime(), process.getExam().getEndTime(), resultMap); // 剩余时间
		resultMap.put("et", process.getExam().getType());


		Calendar now = Calendar.getInstance(); // 当前时间
		Date begin = process.getExam().getStartTime(); // 开考时间
		Calendar start = Calendar.getInstance();
		start.setTime(begin); // 开考时间转换成Calendar
		Date end = process.getExam().getEndTime();
		Calendar finish = Calendar.getInstance();
		finish.setTime(end); // 考试结束时间转换成Calendar

		process.setExamResult(this.resultService.searchLast(process.getExam().getId().intValue() , process.getStudentId())); // 获取最新考试结果
		if (null == process.getExamResult()|| process.getExamResult().getStatus()== TpExamResult.STATUS_PRE ) // 判定是否为空
		{
			boolean pass = true;
			//作业或课堂作业需要验证时间,其它类型不需要验证考试时间范围
			if(process.getExam().getType().intValue()==TpExam.EXAM_TYPE_TASK||process.getExam().getType().intValue()==TpExam.EXAM_TYPE_CLASS_TASK){
				pass = ValidateExamTime.validateExamTime(0, now, start, finish, resultMap); // 时间校验 验证是否在考试时间范围内
			}
					
			if (pass)
			{
				process.setExamResult(new TpExamResult());
				process = this.createExamResult(process);
			}
		}
		else
		{
			// 如果有，判断考试结果是否为正常提交状态
			if (ExamResultStatus.SUBMITTED.getValue() == process.getExamResult().getStatus())
			{
				// 如果是正常提交状态则跳转到作业复习
				resultMap.put("ErrMsg", "您已提交答案，当前考试不允许再次进入！");
				resultMap.put("target", "/student/pc/index");
				resultMap.put("path", "Error");
			}
			else if (ExamResultStatus.PROCESSING.getValue() == process.getExamResult().getStatus())
			{
				boolean pass = ValidateExamTime.validateExamEndTime(now, finish, resultMap);
				if (pass)
				{
					// 如果是未提交状态则继续当前考试，取出当前考试结果信息
					process = this.resultService.searchFullByProcess(process);
				}
				else
				{
					// 更新考试结果为提交状态
					process.getExamResult().setStatus(ExamResultStatus.SUBMITTED.getValue());
					this.resultService.editResult(process.getExamResult());
				}
			}
		}
		process.setResultMap(resultMap);
		return process;
	}


	/**
	 * 创建考试结果
	 * @param process
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	protected Process createExamResult(Process process) throws Exception
	{
        clearExamCahce(process.getExam().getId().intValue());//创建结果时清除考试缓存，解决考试过程中试题同步造成试卷缓存数据与结果表不一致
		// 以下组织考试结果
		process = this.handlerQuestions(process);
		process.getExamResult().setBeginTime(new Date());
//		process.getExamResult().setCode(process.getExam().getCode() + "_result_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		process.getExamResult().setExamId(process.getExam().getId().intValue());
		process.getExamResult().setExamName(process.getExam().getExamName());
		process.getExamResult().setPoints(process.getTestPaperDto().getPaper().getPoints());
		process.getExamResult().setScore(Double.valueOf(0.0));
		process.getExamResult().setStatus(ExamResultStatus.PROCESSING.getValue());
		process.getExamResult().setStudentId(process.getStudentId());
		process.getExamResult().setTimeOff(0);
		process = this.resultService.saveByProcess(process);
		
		return process;
	}
    /**
     *学生进入考试： 创建结果时清除考试缓存，解决考试过程中试题同步造成试卷缓存数据与结果表不一致
     * @param examId
     */
    private void clearExamCahce(int examId) {
        CacheTools.delCache(ConstantTe.PROCESS_EXAM_QID_LIST + examId);
        CacheTools.delCache(ConstantTe.PROCESS_EXAM_QUESTION_LIST + examId);
        CacheTools.delCache(ConstantTe.PROCESS_EXAM_QUESTION_MAP_HTML + examId);
        CacheTools.delCache(ConstantTe.PROCESS_EXAM_HTML_PAGE + examId);
    }
	/**
	 * 批量创建考试结果
	 * @param examEntity
	 * @param loginUser
	 * @param students
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 * 2015年10月12日 上午11:36:23
	 */
	protected List<Process> createExamResults(TpExam examEntity, UserEntity loginUser, List<UserEntity> students) throws Exception
	{
		List<Process> result = null;
		try
		{
			if (null != students && students.size() > 0)
			{
				result = new ArrayList<Process>(0);
				Process process = null;
                Date  date=new Date();
				for (UserEntity student : students)
				{
					process = new Process();
					process.setExam(examEntity);
					process.setLoginUser(loginUser);
					process.setStudentId(student.getId());
					process = this.handlerQuestions(process);
                    process.setExamResult(new TpExamResult());
					process.getExamResult().setBeginTime(date);//学生进入考试时间，预处理和创建时间一致
                    process.getExamResult().setCreateTime(date);//发布考试时创建考试结果时间，如果创建失败，学生进入考试时可以在创建
//					process.getExamResult().setCode(process.getExam().getCode() + "_result_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
					process.getExamResult().setExamId(process.getExam().getId().intValue());
					process.getExamResult().setExamName(process.getExam().getExamName());
					process.getExamResult().setPoints(process.getTestPaperDto().getPaper().getPoints());
					process.getExamResult().setScore(Double.valueOf(0.0));
					process.getExamResult().setStatus(-1);
					process.getExamResult().setStudentId(process.getStudentId());
					process.getExamResult().setTimeOff(0);
					result.add(process);
				}
				result = this.resultService.saveByProcesses(result);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		return result;
	}
	
	/**
	 * 组织题目数据
	 * @param process
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	private Process handlerQuestions(Process process) throws Exception // 组织题目数据
	{
		TpExam exam = process.getExam();
		TestPaperDto testPaperDto = this.testPaperService.findTestPaperDtoByIdCache(exam.getPaperId()); // 获取试卷信息
		/* 获取题目 */
		@SuppressWarnings("unchecked")
		Set<String> qcodes = CacheTools.getCache(ConstantTe.PROCESS_EXAM_QCDS_SET + process.getExam().getId(), Set.class);
		if (null == qcodes || qcodes.size() < 1)
		{
			qcodes = testPaperDto.takeQcodes(); // qcodes ==> Question Codes
			CacheTools.addCache(ConstantTe.PROCESS_EXAM_QCDS_SET + process.getExam().getId(), ConstantTe.CACHE_TIME, qcodes);
		}
		List<String> qcs = new ArrayList<String>(0); // qcs ==> Question Codes
		qcs.addAll(qcodes); // 将Set填充到List中
		@SuppressWarnings("unchecked")
		List<IExamQuestionDto> qdtos = CacheTools.getCache(ConstantTe.PROCESS_EXAM_PAPER_QUESTION_LIST + process.getExam().getId(), List.class);
		if (null == qdtos || qdtos.size() < 1)
		{
			qdtos = this.questionBaseService.findQuestionByCodes(qcs,0); // 获取题目列表
			CacheTools.addCache(ConstantTe.PROCESS_EXAM_PAPER_QUESTION_LIST + process.getExam().getId(), ConstantTe.CACHE_TIME, qdtos);
		}
		
		/* 准备题目数据 */
		@SuppressWarnings("unchecked")
		Map<String, IExamQuestionDto> mqis = CacheTools.getCache(ConstantTe.PROCESS_EXAM_QID_QUESTION_MAP + process.getExam().getId(), Map.class);
		@SuppressWarnings("unchecked")
		Map<String, IExamQuestionDto> mqcs = CacheTools.getCache(ConstantTe.PROCESS_EXAM_QCD_QUESTION_MAP + process.getExam().getId(), Map.class);
		if (null == mqis || mqis.size() < 1 || null == mqcs || mqcs.size() < 1)
		{
			mqis = new HashMap<String, IExamQuestionDto>(0); // mqcs ==> Map Question Codes，CODE做KEY
			mqcs = new HashMap<String, IExamQuestionDto>(0); // qs ==> Questions，ID做KEY
			for (IExamQuestionDto qdto : qdtos)
			{
				int qid = qdto.getQuestionDto().getQuestion().getId(); // qid ==> Question Id
				mqis.put(String.valueOf(qid), qdto); // ID作为KEY
				mqcs.put(qdto.getQuestionDto().getQuestion().getCode(), qdto); // Code作为KEY
			}
			CacheTools.addCache(ConstantTe.PROCESS_EXAM_QID_QUESTION_MAP + process.getExam().getId(), ConstantTe.CACHE_TIME, mqis);
			CacheTools.addCache(ConstantTe.PROCESS_EXAM_QCD_QUESTION_MAP + process.getExam().getId(), ConstantTe.CACHE_TIME, mqcs);
		}

		/* 组织数据对象 */
		process.setMqcs(mqcs);
		process.setMqis(mqis);
		process.setTestPaperDto(testPaperDto);
		List<Integer> pids = new ArrayList<Integer>(0);
		pids.add(exam.getPaperId());
		process.setSubScores(this.testPaperService.searchByPids(pids)); // 子题分数数据
		
		return process;
	}
	
	
}
