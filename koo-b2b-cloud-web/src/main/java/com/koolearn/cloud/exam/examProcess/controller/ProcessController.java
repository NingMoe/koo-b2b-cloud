package com.koolearn.cloud.exam.examProcess.controller;

import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.ExamQueryDto;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examProcess.util.HtmlUtil;
import com.koolearn.cloud.exam.examProcess.util.QuestionUtil;
import com.koolearn.cloud.exam.examProcess.vo.ResultVO;
import com.koolearn.cloud.exam.examProcess.vo.StudentResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.service.ExamResultService;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.StudentHasTaskLog;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.FileUploadUtils;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author DuHongLin 考试过程控制器
 */
@Controller
@RequestMapping("/student/pc")
public class ProcessController extends ProcessBaseController
{
	private static final long serialVersionUID = -1271479511382327980L;
	private static Logger logger=Logger.getLogger(ProcessController.class);
	
	@Resource
	TaskService taskService;
	@Autowired
	private ExamResultService examResultService;//考试结果
	@Resource 
	TestPaperService testPaperService;
    @Resource
    ClassRoomService classRoomService;

	/**
	 * 学生端作业统一入口
	 * @param request
	 * @return
	 */
	@RequestMapping("/taskPortal")
	public String taskPortal(HttpServletRequest request,UserEntity ue){
		Integer resultStatus = 0;//定义作业列表按钮 0.开始作业 1.继续作业 2.有结果复习 3.无结果复习
		int examId = getParamterForInt("examId", 0);
		TaskDto task = taskService.findTaskById(examId);
		TaskPager pager = new TaskPager();
		pager.setExamId(examId);
		pager.setUserId(ue.getId());
		TpExamResult examResult = taskService.findExamResult(pager);
		long endTime = task.getEndTime().getTime();
		long nowTime = new Date().getTime();
		if(examResult==null){//没有考试结果
			if(endTime<nowTime){//作业已经结束
				resultStatus = 3;
			}else{
				resultStatus = 0;
			}
		}else{
			if(endTime<nowTime){//作业已经结束
				resultStatus = 2;
			}else{
				resultStatus = examResult.getStatus();
			}
		}
		//是否进入作业
		if(resultStatus.intValue()==2){
			return "redirect:/student/pc/reviewPage?resultId="+examResult.getId();
		}else if(resultStatus.intValue()==3){
			return "redirect:/student/pc/showPaper?examId="+examId;
		}else{
			return "redirect:/student/pc/start_"+task.getId()+"_"+ue.getId();
		}
	}
	
	/**
	 * 作业首页
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,UserEntity ue){
		String keyWord = request.getParameter("keyWord");//搜索关键字
//		int classesId = 0;
//		List<Classes> clist = taskService.findClassesByStudentId(ue);
//		if(clist!=null && !clist.isEmpty()){
//			classesId = clist.get(0).getId();
//		}
	/*学科new有新作业标识实现  实现方式类似站内信 start*/
		//将老师发布作业插入学生浏览作业日志表中
		taskService.insertStudentHasTaskLog(ue.getId());
		//查询未查看作业与标签的关系
		Map<String,StudentHasTaskLog> map = taskService.findTaskLogByStudentId(ue.getId());
	/*学科new有新作业标识实现  实现方式类似站内信 end*/
		//左侧的学科切换标签
		List<TeacherBookVersion> subjectList = taskService.findClassesTeacherSubject(ue, GlobalConstant.TP_EXAM_TYPE_EXAM);
		Integer subjectId = getParamterForInt("subjectId", 0);//选择科目
		if(subjectList!=null &&subjectList.size()>0){
			if(subjectId==0){
				TeacherBookVersion d = subjectList.get(0);
				subjectId = d.getSubjectId();
			}
			for (int i = 0; i < subjectList.size(); i++) {
				TeacherBookVersion dict = subjectList.get(i);
				StudentHasTaskLog st = map.get(ue.getId()+"_"+dict.getSubjectId());
				//判断是否有新作业    1为有新作业   
				if(st!=null&&st.getStatus()==1){
					dict.setTagStatus(StudentHasTaskLog.STATUS_SEE_NO);
				}else{
					dict.setTagStatus(StudentHasTaskLog.STATUS_SEE_YES);
				}
			}
		}
				
		int pageNo = getParamterForInt("pageNo", 0);
		setAttribute("subjectList", subjectList);
		setAttribute("subjectId", subjectId);
		setAttribute("pageNo", pageNo);
		//更新学生作业日志表，将subject科目变成已阅读（去掉new标识）
		taskService.updateTaskLog(subjectId,ue.getId());
		return "/student/task/taskIndex";
	}
	
	@RequestMapping("indexData")
	public String indexData(HttpServletRequest request,UserEntity ue,TaskPager task){
		task.setPageSize(10);
		task.setUserId(ue.getId());
		int subjectId = getParamterForInt("subjectId", 0);//选择科目
		task.setSubjectId(subjectId);
		task.setUserId(ue.getId());
		TaskPager pager = taskService.searchTaskBySubject(task);
		//更新学生作业日志表，将subject科目变成已阅读（去掉new标识）
		taskService.updateTaskLog(subjectId,ue.getId());
		setAttribute("resultList", pager.getResultList());
		setAttribute("listPager", pager);
		setAttribute("ue", ue);
		return "/student/task/taskIndexData";
	}
	
	/**
	 * * @Description: TODO(点击进入考试时判断是否可以继续考试) 
	 */
	@RequestMapping("/joinExam")
	@ResponseBody
	public boolean joinExam(ModelMap modelMap,UserEntity loginUser,@ModelAttribute ExamQueryDto examQueryDto,HttpServletRequest request) throws Exception {
		Map map = new HashMap();//返回值 success成功标识 errMsg错误信息
		TaskDto ed=taskService.findTaskById(examQueryDto.getExamId());
		Calendar now=ParseDate.getCalendar(new Date());
		List<Integer> examIds=new ArrayList<Integer>();
		examIds.add(ed.getId());
		examQueryDto.setUserId(loginUser.getId());
		//获取考试结果
		Map<String,TpExamResult> erList=examResultService.searchLasts(examIds,examQueryDto.getUserId());
		return parseOneExamResult(ed,erList,examQueryDto,now,map);
	}
	
	private boolean parseOneExamResult(TaskDto ed, Map<String, TpExamResult> erList,ExamQueryDto examQueryDto, Calendar now,Map map) throws Exception {
		Calendar start=ParseDate.getCalendar(ed.getStartTime());
		Calendar finish=ParseDate.getCalendar(ed.getEndTime());
		TpExamResult er= erList.get(examQueryDto.getUserId() + "_" + ed.getId());//examResultService.searchLast(ed.getId(), examQueryDto.getUserId());
		//是否进入作业
		return !ParseDate.isOverdue(ed.getStartTime(),ed.getEndTime());//是否在作业时间内
	}
	
	/**
	 * 开始考试
	 * @param examId
	 * @param studentId
	 * @param request
	 * @param response
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/start_{examId}_{studentId}")
	public String start(@PathVariable("examId") int examId,
						@PathVariable("studentId") int studentId,
						HttpServletRequest request,
						HttpServletResponse response,UserEntity loginUser) throws Exception
	{
		String result = null;
		try
		{
            //处理预生成的考试结果
            this.examService.parsePreExamResult(examId,studentId);
			/* 通过考试ID获取考试信息 */
			boolean ipValid = false;
			TpExam exam = this.examService.queryExamByIdCache(examId);
			int examType = exam.getType(); // 获取考试类型
			
			/* 获取题目结构 */
			QuestionBarHtml questionBarHtml = testPaperService.getQuestionBarHtml(exam.getPaperId());
			request.setAttribute("questionBarHtml", questionBarHtml);
			
			/* 过程数据传递对象 */
			Process process = new Process();
			process.setExam(exam);
			process.setLoginUser(loginUser);
			process.setStudentId(studentId);
			process = this.handlerExam(process);
            //课堂作业处理
            String urlType = request.getParameter("urlType");
            if(urlType != null && !"".equals(urlType)){
                Calendar now = Calendar.getInstance(); // 当前时间
                Date end = process.getExam().getEndTime();
                Calendar finish = Calendar.getInstance();
                finish.setTime(end); // 考试结束时间转换成Calendar
                request.setAttribute("urlType",urlType);//课堂表示
                request.setAttribute("classRoomId",request.getParameter("classRoomId"));//课堂id
                if (now.after(finish))
                {
                    return "redirect:/student/pc/showPaper?examId="+process.getExam().getId()+"&urlType=1";
                }
            }

			/* 判定异常结果 */
			Map<String, Object> resultMap = process.getResultMap();
			if (resultMap.containsKey("ErrMsg"))
			{
				request.setAttribute("msg", resultMap.get("ErrMsg").toString());
				request.setAttribute("target", resultMap.get("target").toString());
				return this.prePath + resultMap.get("path").toString(); 
			}
			
			/* 组织用户答案结构 ==> Key是题目ID，Value是用户答案  */
			List<TpExamResultDetail> resultDetails = process.getExamResultDetails(); 
			Map<String, String> userResult = QuestionUtil.handlerUserResult(resultDetails); // 存储用户答案，Key是题目ID，Value是用户答案
			
			/*学生答题结果 wujun 后加,此部分为了取到学生第二次进入试卷时主观题的答题结果 start **/
			Map<String, TpExamResultDetail> detailsMap = new HashMap<String, TpExamResultDetail>(0);
			Map<String, Integer> detailsCodeMap = new HashMap<String, Integer>(0);
			for (TpExamResultDetail detail : process.getExamResultDetails())
			{
				detailsMap.put(String.valueOf(detail.getQuestionId()), detail);
                detailsCodeMap.put( detail.getQuestionCode(), detail.getQuestionId());
			}
			/*学生答题结果 wujun 后加,此部分为了取到学生第二次进入试卷时主观题的答题结果 end  **/
			
			PaperPager paperFilter = new PaperPager();
			paperFilter.setPaperId(exam.getPaperId());
			//试卷预览
            TestPaper testPaper = testPaperService.createOrEditTestPaper(paperFilter,loginUser);
            parsePaperQuestionId(testPaper,detailsCodeMap);
            QuestionViewDto questionViewDto=new QuestionViewDto();
            questionViewDto.setDetailsMap(detailsMap);//放学生答题明细
            questionViewDto.setViewType(QuestionViewDto.view_type_question);
            questionViewDto.setButtonType(QuestionViewDto.button_ype_null);//学生：QuestionViewDto.button_ype_null
            request.setAttribute("questionViewDto", questionViewDto);
            request.setAttribute("testPaper",testPaper);
            request.setAttribute("paperFilter", paperFilter);
            request.setAttribute("detailsMap", detailsMap);
            
			/* 向作用域中添加值 */
			request.setAttribute("examId", examId); // 考试ID
			request.setAttribute("studentId", studentId); // 学生ID
			request.setAttribute("resultId", process.getExamResult().getId()); // 考试结果ID
			String ur = JSONObject.fromObject(userResult).toString();
			request.setAttribute("userResult", ur); // 用户考试结果
			String urKey = process.getExamResult().getId() + "_" + examId + "_" + studentId; // LocalStorage中用户考试结果Key
			request.setAttribute("urKey", urKey);
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			request.setAttribute("exam", exam); // 考试名称
			request.setAttribute("handExam", request.getParameter("handExam")); // 考试名称
//			this.setExamType(request); // 设置所有考试类型



			result = this.prePath + resultMap.get("path").toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return result;
	}

    private void parsePaperQuestionId(TestPaper testPaper, Map<String, Integer> detailsCodeMap) {
        //处理题目版本升级题目id不一致的问题
        if(testPaper.getPaperQuestionTypeList()!=null&&testPaper.getPaperQuestionTypeList().size()>0){
            for(PaperQuestionType pqt:testPaper.getPaperQuestionTypeList()){
                if(pqt.getQuestionDtoList()!=null &&pqt.getQuestionDtoList().size()>0){
                    for(IExamQuestionDto dto:pqt.getQuestionDtoList()){
                        Question q=dto.getQuestionDto().getQuestion();
                        dto.getQuestionDto().getQuestion().setId(detailsCodeMap.get(q.getCode()));
                    }
                }
            }
        }
    }


    /**
	 * 用户交卷
	 * @param rid
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author DuHongLin
	 */
	@RequestMapping("/submit_{status}_{rid}")
	public void submit(@PathVariable("rid") int rid, @PathVariable("status") int status, HttpServletRequest request, HttpServletResponse response, @RequestParam("urs") String userResultStr) throws Exception
	{
		PrintWriter out = response.getWriter();
		try
		{
			userResultStr = userResultStr.substring(1, userResultStr.length() - 1).replace("\\n", "").replace("\\t", "").replace("\\\"","'");
			System.out.println("解开的数据为rid="+rid+";urs="+userResultStr);
			logger.info("解开的数据为rid="+rid+";urs="+userResultStr);
			this.resultService.editResultForSubmit(rid, status, userResultStr);
			out.print("{\"flag\":true}");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			out.print("{\"flag\":false}");
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
	/**
	 * 作业复习(显示卷子,无答题结果)
	 * @param loginUser
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showPaper")
	public String showPaper(UserEntity loginUser,HttpServletRequest request) throws Exception{
		int examId = getParamterForInt("examId", 0);
		TpExam exam = this.examService.queryExamByIdCache(examId);
		
		PaperPager paperFilter = new PaperPager();
		paperFilter.setPaperId(exam.getPaperId());
		QuestionBarHtml questionBarHtml = testPaperService.getQuestionBarHtml(exam.getPaperId());
		//试卷预览
        TestPaper testPaper = testPaperService.createOrEditTestPaper(paperFilter,loginUser);
        QuestionViewDto questionViewDto=new QuestionViewDto();
        questionViewDto.setViewType(QuestionViewDto.view_type_question);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_null);//学生：QuestionViewDto.button_ype_null
        
        request.setAttribute("questionViewDto", questionViewDto);
        request.setAttribute("testPaper",testPaper);
        request.setAttribute("paperFilter", paperFilter);
        request.setAttribute("exam", exam); // 考试名称
        request.setAttribute("questionBarHtml", questionBarHtml);
        request.setAttribute("urlType",request.getParameter("urlType"));//课堂表示
		return "/student/examProcess/showPaper";
	}
	
	/**
	 * 作业复习,有答题结果的复习
	 * @param loginUser
	 * @param pager
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/reviewPage")
	public String review(UserEntity loginUser,TaskPager pager,HttpServletRequest request) throws Exception{
		int resultId = getParamterForInt("resultId", 0);
//		TpExamResult examResult = taskService.findExamResult(pager);
		taskService.updateTaskStudentView(resultId);//学生浏览评语状态更新为2,即已浏览
		TpExamResult examResult = taskService.findExamResultById(resultId);//为啥只查提交的考试结果？？
		TaskDto task = taskService.findTaskById(examResult.getExamId());
		QuestionBarHtml questionBarHtml = testPaperService.getQuestionBarHtml(task.getPaperId());
		request.setAttribute("examResult", examResult);
		request.setAttribute("questionBarHtml", questionBarHtml);
		request.setAttribute("task", task);
		request.setAttribute("py", getParamter("py"));//是否定位评语
		request.setAttribute("urlType",request.getParameter("urlType"));//课堂表示
        StudentResult studentResult = taskService.findDetailByResult(resultId);//根据正确题数计算的正确率，咱不用
		request.setAttribute("studentResult",studentResult);//正确率
        //课堂处理完成时间
        String classRoomId = request.getParameter("classRoomId");//课堂id
        if(null != classRoomId && !"".equals(classRoomId)){
            setFinishTime(loginUser, classRoomId);
        }
		return "/student/task/taskReview";
	}

    /**
     * 设置课堂完成时间
     * @param loginUser
     * @param classRoomId
     */
    private void setFinishTime(UserEntity loginUser, String classRoomId) {
        int _classRoomId = Integer.valueOf(classRoomId);
        TpExamStudent tes = classRoomService.getTpExamStudent(loginUser.getId(), _classRoomId);
        //保存完成时间
        if (tes != null && tes.getFinishTime() == null) {
            double finishCount = 0;
            List<TpExamAttachment> teaList = classRoomService.findAttachmentByClassRoomId(_classRoomId);
            TpExamAttachment ta;
            for (int i = 0; i < teaList.size(); i++) {
                ta = teaList.get(i);
                if (ta.getAttachmentType().equals(2)) {
                    boolean isExist = classRoomService.isExistExamByStudent(ta.getAttachmentId(), loginUser.getId());
                    if (!isExist) {
                        teaList.remove(i);
                        i--;
                        continue;
                    }
                    if (classRoomService.getExamComplete(loginUser.getId(), ta.getAttachmentId())) {
                        finishCount++;
                    }
                } else {
                    if (classRoomService.getComplete(loginUser.getId(), ta.getId())) {
                        finishCount++;
                    }
                }
            }
            if (finishCount == teaList.size()) {
                classRoomService.updateFinishTime(loginUser.getId(), _classRoomId);
            }
        }
    }

    /**
	 * 作业复习试卷数据
	 * @param request
	 * @return
	 */
    @RequestMapping("/review_{rid}")
	public String reviewData(@PathVariable("rid") int rid,UserEntity loginUser,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String result = null;
		try
		{
			/* 获取考试结果 */
			TpExamResult examResult = this.resultService.searchById(rid);
			if(examResult==null){
				return "/student/task/reviewShowPaperResult";
			}
			Process process = new Process();
			process.setExamResult(examResult);
			process = this.resultService.searchFullByProcess(process);
			
			/* 获取考试信息 */
			TpExam exam = this.taskService.queryExamByIdCache(examResult.getExamId());
			process.setExam(exam);
			
			//将考试结果明细数据组装成map方便通过题目id获取数据
			Map<String, TpExamResultDetail> detailsMap = new HashMap<String, TpExamResultDetail>(0);
			for (TpExamResultDetail detail : process.getExamResultDetails())
			{
				detailsMap.put(String.valueOf(detail.getQuestionId()), detail);
			}
			
			PaperPager paperFilter = new PaperPager();
			paperFilter.setPaperId(exam.getPaperId());
			paperFilter.setDetailsMap(detailsMap);//用户答题结果map
			QuestionBarHtml questionBarHtml = testPaperService.getQuestionBarHtml(exam.getPaperId());
			//试卷预览
	        TestPaper testPaper = testPaperService.createOrEditTestPaperResultDetail(paperFilter,loginUser,rid,QuestionViewDto.view_type_review);
	        QuestionViewDto questionViewDto=new QuestionViewDto();
	        request.setAttribute("questionViewDto", questionViewDto);
	        request.setAttribute("testPaper",testPaper);
	        request.setAttribute("paperFilter", paperFilter);
	        request.setAttribute("exam", exam); // 考试名称
	        request.setAttribute("questionBarHtml", questionBarHtml);
	        request.setAttribute("detailsMap", detailsMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return "/student/task/reviewShowPaperResult";
	}
	
	private static String BASE_DIR = PropertiesConfigUtils.getProperty("upload_base_dir");
	/**
	 * 口语录音文件上传
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	@RequestMapping("/usf")
	public String uploadSpokenFile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String targetFolder = null;
		String fileName = null;
		String fullName = null;
		String message = null;
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try
		{
			targetFolder = BASE_DIR + "/video/";
			fileName = System.currentTimeMillis() + ".mp3";
			fullName = targetFolder + fileName;
			inputStream = request.getInputStream();
			File fileD = new File(BASE_DIR+"/video/");
	        if(!fileD.exists()) {  
	        	fileD.mkdirs(); 
	        }
			fileOutputStream = new FileOutputStream(new File(fullName));
			int formLength = request.getContentLength();
			byte[] formContent = new byte[formLength];
			int totalRead = 0;
			int nowRead = 0;
			while (totalRead < formLength) {
				nowRead = inputStream.read(formContent, totalRead, formLength);
				totalRead += nowRead;
			}
			fileOutputStream.write(formContent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			message = "0,";
			printWriterAjax(response, message);
			return null;
		}
		finally
		{
			if (null != fileOutputStream)
			{
				fileOutputStream.flush();
				fileOutputStream.close();
			}
			if (null != inputStream)
			{
				inputStream.close();
			}
		}
		message = "1,/upload/video/" + fileName;
		printWriterAjax(response, message);
		return null;
	}
	
	
	/**
	 * 验证是否可以交卷
	 * @param examId
	 * @param
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author DuHongLin
	 */
	@RequestMapping("vs_{examId}")
	public void validateSubmit(@PathVariable("examId") int examId, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter out = response.getWriter();
		try
		{
//			TpExam exam = this.examService.queryExamByIdCache(examId);
			boolean pass = true;
			if (pass)
			{
				out.print("{\"flag\":true}");
			}
			else
			{
				out.print("{\"flag\":false}");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			out.print("{\"flag\":false}");
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping("/fileUpload")
	@ResponseBody
	public Map fileUpload(MultipartFile file,HttpServletRequest request) throws IOException{
		String questionId = request.getParameter("questionId");
		String resultId = request.getParameter("resultId");
		Map map = new HashMap();
		map.put("success", false);
		map.put("errMsg", "上传失败，请重试!");
		if(!file.isEmpty()){
			if(file!=null && file.getSize()!=0){
				String head_url = file.getOriginalFilename();
				String extendName = head_url.substring(head_url.lastIndexOf(".") + 1).toLowerCase();
				if(!FileUploadUtils.verifySizeType(file.getSize(),extendName,100*1024*1024)){
					map.put("errMsg", "文件大小不得大于100M,文件类型jpg,jpeg,png,gif,bmg!");
					return map;
				}
				//生成文件上传的相对路径
				String realPath = FileUploadUtils.getPathByRandomId(extendName , "/questionpic");
				FileUploadUtils.saveQuestionPic(file.getInputStream(),realPath);
				realPath = "/data" +realPath;
				System.out.println("realPath="+realPath);
				int count = examResultService.updateDetailSubjectiveUrl(questionId,resultId,realPath);
				if(count>0){
					map.put("success", true);
					map.put("errMsg", realPath);
				}
			}
		}
		return map;
	}
}
