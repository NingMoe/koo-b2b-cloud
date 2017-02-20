//package com.koolearn.cloud.student.task.controller;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.koolearn.cloud.base.controller.BaseController;
//import com.koolearn.cloud.common.entity.Classes;
//import com.koolearn.cloud.common.entity.TeacherBookVersion;
//import com.koolearn.cloud.dictionary.entity.Dictionary;
//import com.koolearn.cloud.exam.entity.ExamQueryDto;
//import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
//import com.koolearn.cloud.exam.examcore.exam.service.ExamResultService;
//import com.koolearn.cloud.login.entity.UserEntity;
//import com.koolearn.cloud.task.dto.StudentHasTaskLog;
//import com.koolearn.cloud.task.dto.TaskDto;
//import com.koolearn.cloud.task.service.TaskService;
//import com.koolearn.cloud.util.DataDictionaryUtil;
//import com.koolearn.cloud.util.ListPage;
//import com.koolearn.cloud.util.ParseDate;
//
//@Controller
//@RequestMapping("/student/task")
//public class TaskStudentController extends BaseController{
//	@Resource
//	TaskService taskService;
//	@Autowired
//	private ExamResultService examResultService;//考试结果
//	
//	/**
//	 * 作业首页
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/review")
//	public String review(HttpServletRequest request,UserEntity ue){
//		
//		return "";
//	}
//	
//	/**
//	 * 作业首页
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/index")
//	public String index(HttpServletRequest request,UserEntity ue){
//		String keyWord = request.getParameter("keyWord");//搜索关键字
//		int pageNo = getParamterForInt("pageNo", 0);
//		int pageSize = 10;
//		//首页显示 布置作业 所以显示9条数据
//		if(pageNo == 0){
//			pageSize = 1;
//		}else{
//			pageSize = 1;
//		}
//		int classesId = 0;
//		List<Classes> clist = taskService.findClassesByStudentId(ue);
//		if(clist!=null && !clist.isEmpty()){
//			classesId = clist.get(0).getId();
//		}
//	/*学科new有新作业标识实现  实现方式类似站内信 start*/
//		//将老师发布作业插入学生浏览作业日志表中
//		taskService.insertStudentHasTaskLog(classesId,ue.getId());
//		//查询未查看作业与标签的关系
//		Map<String,StudentHasTaskLog> map = taskService.findTaskLogByStudentId(ue.getId());
//	/*学科new有新作业标识实现  实现方式类似站内信 end*/
//		
//		
//		//左侧的学科切换标签
//		List<TeacherBookVersion> subjectList = taskService.findClassesTeacherSubject(ue);
//		int subjectId = getParamterForInt("subjectId", 0);//选择科目
//		if(subjectList!=null &&subjectList.size()>0){
//			if(subjectId==0){
//				TeacherBookVersion d = subjectList.get(0);
//				subjectId = d.getSubjectId();
//			}
//			for (int i = 0; i < subjectList.size(); i++) {
//				TeacherBookVersion dict = subjectList.get(i);
//				StudentHasTaskLog st = map.get(ue.getId()+"_"+dict.getSubjectId());
//				//判断是否有新作业    1为有新作业   
//				if(st!=null&&st.getStatus()==1){
//					dict.setTagStatus(StudentHasTaskLog.STATUS_SEE_NO);
//				}else{
//					dict.setTagStatus(StudentHasTaskLog.STATUS_SEE_YES);
//				}
//			}
//		}
//				
//		TaskDto dto = new TaskDto();
//		dto.setPageNo(pageNo);
//		dto.setPageSize(pageSize);
//		dto.setExamName(keyWord);
//		dto.setSubjectId(subjectId);
//		
//		//右侧的学科下所有作业列表
//		ListPage page = taskService.searchTaskBySubject(dto);
//		setAttribute("resultList", page.getResultList());
//		setAttribute("pager", page.getListPager());
//		setAttribute("subjectList", subjectList);
//		setAttribute("subjectId", subjectId);
//		setAttribute("taskDto", dto);
//		//更新学生作业日志表，将subject科目变成已阅读（去掉new标识）
//		taskService.updateTaskLog(subjectId,ue.getId());
//		return "/student/task/taskIndex";
//	}
//	
//	/**
//	 * * @Description: TODO(点击进入考试时判断是否可以继续考试) 
//	 */
//	@RequestMapping("/joinExam")
//	@ResponseBody
//	public Map joinExam(ModelMap modelMap,UserEntity loginUser,@ModelAttribute ExamQueryDto examQueryDto,HttpServletRequest request) throws Exception {
//		Map map = new HashMap();//返回值 success成功标识 errMsg错误信息
//		TaskDto ed=taskService.findTaskById(examQueryDto.getExamId());
//		Calendar now=ParseDate.getCalendar(new Date());
//		List<Integer> examIds=new ArrayList<Integer>();
//		examIds.add(ed.getId());
//		examQueryDto.setUserId(loginUser.getId());
//		//获取考试结果
//		Map<String,TpExamResult> erList=examResultService.searchLasts(examIds,examQueryDto.getUserId());
//		parseOneExamResult(ed,erList,examQueryDto,now,map);
//		return map;
//	}
//	
//	private void parseOneExamResult(TaskDto ed, Map<String, TpExamResult> erList,
//			ExamQueryDto examQueryDto, Calendar now,Map map) throws Exception {
//		Calendar start=ParseDate.getCalendar(ed.getStartTime());
//		Calendar finish=ParseDate.getCalendar(ed.getEndTime());
//		TpExamResult er= erList.get(examQueryDto.getUserId() + "_" + ed.getId());//examResultService.searchLast(ed.getId(), examQueryDto.getUserId());
//		if(er!=null&&er.getStatus()!=TpExamResult.STATUS_PRE){
////			ed.setPoints(er.getPoints());
////			ed.setScore(er.getScore());
////			ed.setExamTimeStr(ParseDate.formatByDate(er.getBeginTime(), ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS_SLASHE));
////			DecimalFormat df2 = new DecimalFormat("###.0");
////			ed.setTimeOffMin(Double.parseDouble(df2.format((er.getTimeOff()*1.0D)/60)));
////			ed.setResultId(er.getId());
////			if(er.getStatus()==Constant.EXAMRESULTTYPE_TIJIAO){
////				ed.setCommite(true);// 提交
////			}else{
////				ed.setCommite(false);//参考未提交
////			}
//		}else{
////			ed.setCommite(false);//未参考未提交
//		}
//		//是否进入作业
//		map.put("success", !ParseDate.isOverdue(ed.getStartTime(),ed.getEndTime()));//是否在作业时间内
//		if(TpExamResult.STATUS_COMPLETE==examQueryDto.getCompleteType().intValue()){
//			 //已完成测评
//			 if(er==null  || er.getStatus()==TpExamResult.STATUS_PRE){//未参加考试
//				 ed.setLastNewStatus("未测评");
//			 }else if(er.getStatus()==TpExamResult.STATUS_PRE){
//				 //考试没提交
//				 ed.setLastNewStatus("未交卷");
//			 }else if(er.getStatus()==TpExamResult.STATUS_COMPLETE){
//				 //考试已提交
//				 ed.setLastNewStatus("已交卷");
//			 }
//		 }
//	}
//	
//}
