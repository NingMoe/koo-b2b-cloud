package com.koolearn.cloud.exam.examcore.question.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
/**
 * 选择题录入编辑操作
 * @author yangzhenye
 */
@Controller
@RequestMapping("/question/base/choice")
public class ChoiceController extends QuestionController {
	private static final Logger logger = Logger.getLogger(ChoiceController.class);
	@Autowired
	private ChoiceQuestionService choiceQuestionService;
	
	private  final String SESSION_ERRMSG_KEY="errMsg";
	private  final String SESSION_OBJ_KEY="myObject";
	private final String ATTR_CHOICE_DTO="choiceQuestionDto";
	private final String LIST_PAGE="forward:/question/base/questionList";
	@RequestMapping("/index")
	public String index(){
		
		String page="/examcore/question/choice/commChoice";
		//修改 另存
		if(getAttribute(ATTR_CHOICE_DTO)!=null){
			return page;
		}
		
		//进入添加页面 保存出错页面
		String e=(String)getSessionObject(SESSION_ERRMSG_KEY);
		if(e!=null){
			removeSessionObject(SESSION_ERRMSG_KEY);
			setAttribute("errMsg", e);
			if(getSessionObject(SESSION_OBJ_KEY)!=null){
				setAttribute("choiceQuestionDto", getSessionObject(SESSION_OBJ_KEY));
				removeSessionObject(SESSION_OBJ_KEY);
			}
		}
		return page;
	}
	//保存，修改 单选，多选题（调用）
	@RequestMapping("/save2")
	public String save(ChoiceQuestionDto choiceQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		//处理题干材料
		preSave(choiceQuestionDto, request);
		//生成试题编码
		try{
			createQuestionCode(modelMap, choiceQuestionDto, response, request);
			choiceQuestionService.saveOrUpdate(choiceQuestionDto);
		}catch(Exception e){
			logger.error(e);
			setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
			setSessionObject(SESSION_OBJ_KEY, choiceQuestionDto);
		}
		if(getSessionObject(SESSION_ERRMSG_KEY)==null){
			setSessionObject(SESSION_ERRMSG_KEY,"保存成功");
			if(choiceQuestionDto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
				removeSessionObject(SESSION_ERRMSG_KEY);
				//保存/修改全部返回一个响应
				if(choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_DANXUAN){
					printWriterAjax(response, printClose());
				}
				return null;
			}
			
			removeSessionObject(SESSION_ERRMSG_KEY);
			if(choiceQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				return toAddIndexUrl(choiceQuestionDto.getQuestionDto());
			}else{
				return indexUrl;
			}
		}
		
		 
		return "redirect:/question/base/choice/index";
	}
	/**
	 * 保存之前做的处理,在预览时候也使用
	 * @param choiceQuestionDto
	 * @param request
	 */
	private void preSave(ChoiceQuestionDto choiceQuestionDto, HttpServletRequest request) {
		List<String> contents=new ArrayList<String>();
		String[] aa=choiceQuestionDto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		
		Question question=choiceQuestionDto.getQuestionDto().getQuestion();
		for(int i=0;i<contents.size();i++){
			if(i==contents.size()-1){
				choiceQuestionDto.getChoiceQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach=new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i-1);
			QuestionDto questionDto= choiceQuestionDto.getQuestionDto();
			if(questionDto.getQuestionAttachs()==null){
				questionDto.setQuestionAttachs(new ArrayList<QuestionAttach>());
			}
			questionDto.getQuestionAttachs().add(attach);
		}
		//id
		String strId=getParamter("questionDto_question_id");
		if(org.apache.commons.lang.StringUtils.isNotEmpty(strId)){
			int id=Integer.parseInt(strId);
			choiceQuestionDto.getQuestionDto().getQuestion().setId(id);
		}
		//添加当前登录用户信息
		question.setLastUpdateBy(getLoginUserName());
		question.setLastUpdateDate(new Date(System.currentTimeMillis()));
		if(question.getId()==0){
			question.setCreateBy(getLoginUserName());
			question.setCreateDate(new Date(System.currentTimeMillis()));
		}
		//处理选项
		String[] descs=getParaNamesByPrefix("fmbx1_");
		String[] isright=request.getParameterValues("isright");
		Set<Integer> ids_index=new HashSet<Integer>();
		if(isright!=null){
			for(int i=0;i<isright.length;i++){
				ids_index.add(isright[i].hashCode()-65);
			}
		}
		
		List<ChoiceAnswer> answers=new ArrayList<ChoiceAnswer>();
			for(int i=0;i<descs.length;i++){
				ChoiceAnswer answer=new ChoiceAnswer();
				answer.setDescription(getParamter(descs[i]));
				answer.setSequenceId(i);
				if(ids_index.contains(i)){
					answer.setIsright(1);
				}else{
					answer.setIsright(0);
				}
				answers.add(answer);
			}
		choiceQuestionDto.setChoiceAnswers(answers);
		
		checkChoiceType(choiceQuestionDto);
	}
	@SuppressWarnings("rawtypes")
	private String[] getParaNamesByPrefix(String prefix) {
		HttpServletRequest request= getHttpRequest();
		Enumeration en=request.getParameterNames();
		List<String> keys=new ArrayList<String>();
		while(en.hasMoreElements()){
			String a=(String)en.nextElement();
			if(a.indexOf(prefix)==0){
				keys.add(a);
			}
		}
		Collections.sort(keys);
		String[] contents=new String[keys.size()];
		for(int i=0,size=keys.size();i<size;i++){
			contents[i]=keys.get(i);
		}
		return contents;
	}
	@RequestMapping("/multiIndex")
	public String multiIndex(){
		index();
		String page="/examcore/question/choice/multiChoice";
		return page;
	}
	//保存 修改 多选题
	@RequestMapping("/saveMulti")
	public String saveMulti(ChoiceQuestionDto choiceQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response)throws Exception{
		choiceQuestionDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_DUOXUAN);
		
		try {
			//createQuestionCode(modelMap, choiceQuestionDto, response, request);
		} catch (Exception e) {
			logger.error(e);
			setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
			return null;
		}
		
		save(choiceQuestionDto,request,modelMap,response);
		
		if(choiceQuestionDto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题 保存
			printWriterAjax(response, printClose());
			return null;
		}
		
		if(choiceQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
			return toAddIndexUrl(choiceQuestionDto.getQuestionDto());
		}
		return LIST_PAGE;
	}
	/**
	 * 单选方框
	 * @return
	 */
	@RequestMapping("/boxIndex")
	public String boxIndex(){
		index();
		return "/examcore/question/choice/boxChoice";
	}
	/**
	 * 单选方框
	 * @return
	 */
	@RequestMapping("/sortIndex")
	public String sortIndex(){
		index();
		return "/examcore/question/sortquestion/add";
	}
	@RequestMapping("/saveBox")
	public String saveBox(ChoiceQuestionDto choiceQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		try {
			choiceQuestionDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_DANXUAN_BOX);
			createQuestionCode(modelMap, choiceQuestionDto, response, request);
			save(choiceQuestionDto,request,modelMap,response);
			
			if(choiceQuestionDto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题 保存
				printWriterAjax(response, printClose());
				return null;
			}
			
			if(choiceQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				return toAddIndexUrl(choiceQuestionDto.getQuestionDto());
			}else{
//				toAddIndex(request, modelMap, response);
//				String redirctUrl="redirect:/question/base/choice/boxIndex";
				return indexUrl;
			}
		} catch (Exception e) {
			logger.error(e);
			return LIST_PAGE;
		}
	}
	/**
	 * 排序题的  保存
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ("/sortSave"))
	public String sortSave(ChoiceQuestionDto choiceQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){//处理题干材料
		preSortSave(choiceQuestionDto, request);
		try{
			createQuestionCode(modelMap, choiceQuestionDto, response, request);
			choiceQuestionService.saveOrUpdate(choiceQuestionDto);
		}catch(Exception e){
			setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
			setSessionObject(SESSION_OBJ_KEY, choiceQuestionDto);
		}
		if(getSessionObject(SESSION_ERRMSG_KEY)==null){
			setSessionObject(SESSION_ERRMSG_KEY,"保存成功");
			if(choiceQuestionDto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
				removeSessionObject(SESSION_ERRMSG_KEY);
				//保存/修改全部返回一个响应
				if(choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_SORT){
					printWriterAjax(response, printClose());
					return null;
				}
			}
			//各个列表 修改后 跳转
			if(choiceQuestionDto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				removeSessionObject(SESSION_ERRMSG_KEY);
				return toAddIndexUrl(choiceQuestionDto.getQuestionDto());
			}
			return LIST_PAGE;
		}
		
		return "redirect:/question/base/choice/sortIndex";
	}
	/**
	 * 在试题审核时候使用
	 * 修改选择题,包括另存
	 * @return
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex(){
		int questionId=new Integer(getParamter("questionId"));
		//int questionType=new Integer(getParamter("questionType"));
//		int saveType=(Integer)getAttribute("saveType");
//		int responseType=(Integer)getAttribute("responseType");
//		int teId=(Integer)getAttribute("te_id");
		//获取选择题对象 放到attr中
		ChoiceQuestionDto dto= null;
		if(questionId!=0){
			dto=choiceQuestionService.getChoiceQuestion(questionId);
		}else{
			dto=new ChoiceQuestionDto();
			QuestionDto questionDto=new QuestionDto();
			dto.setQuestionDto(questionDto);
			Question question=new Question();
			questionDto.setQuestion(question);
		}
//		dto.getQuestionDto().getQuestion().setTeId(teId);
//		dto.getQuestionDto().setResponseType(responseType);
//		dto.getQuestionDto().setSaveType(saveType);
		setAttribute(ATTR_CHOICE_DTO, dto);
		
		String page = toModifyIndex(dto);
		
		//各个action 根据savetpye 跳转到是录入页面还是试题列表
		return page;
	}
	@RequestMapping("/modifyIndex2")
	public String modifyIndex2(){
		int questionId=(Integer)getAttribute("questionId");
		int questionType=(Integer)getAttribute("questionType");
		int saveType=(Integer)getAttribute("saveType");
		int responseType=(Integer)getAttribute("responseType");
		int teId=(Integer)getAttribute("te_id");
		//获取选择题对象 放到attr中
		ChoiceQuestionDto dto= null;
		if(questionId!=0){
			dto=choiceQuestionService.getChoiceQuestion(questionId);
		}else{
			dto=new ChoiceQuestionDto();
			QuestionDto questionDto=new QuestionDto();
			dto.setQuestionDto(questionDto);
			Question question=new Question();
			questionDto.setQuestion(question);
		}
		dto.getQuestionDto().getQuestion().setTeId(teId);
		dto.getQuestionDto().setResponseType(responseType);
		dto.getQuestionDto().setSaveType(saveType);
		setAttribute(ATTR_CHOICE_DTO, dto);
		
		//根据类型跳转到具体action
		String path="index";
		switch (questionType) {
			case Question.QUESTION_TYPE_DANXUAN:
				path="index";
				break;
			case Question.QUESTION_TYPE_DANXUAN_BOX:
				path="boxIndex";
				break;
			case Question.QUESTION_TYPE_DUOXUAN:
				path="multiIndex";
				break;
			case Question.QUESTION_TYPE_READ_MULTICHOICE:
				path = "readMultiChoice";
				break;
			case Question.QUESTION_TYPE_SORT:
				path = "sortIndex";
				break;
		default:
			setAttribute(ATTR_CHOICE_DTO,null);
			break;
		}
		if(dto.getQuestionDto().getQuestion().getTeId()==0){
		    	return toModifyIndex(dto);
	    }else{
	    	//各个action 根据savetpye 跳转到是录入页面还是试题列表
	    	return "forward:/question/base/choice/"+path;
	    }
	}
	/**
	 * 检测选择题类型,如果为0,表示是单选选择题
	 * @param dto
	 */
	private void checkChoiceType(ChoiceQuestionDto dto){
		Question question= dto.getQuestionDto().getQuestion();
		if(question.getQuestionTypeId()==0){
			question.setQuestionTypeId(Question.QUESTION_TYPE_DANXUAN);
		}
	}
	/**
	 * 试题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/preview/{step}")
	public String preview(ChoiceQuestionDto choiceQuestionDto,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap){
		setAttribute("step", step);
		if(step==1){
			preSave(choiceQuestionDto, request);
			choiceQuestionDto.getChoiceQuestion().setQuestionId(choiceQuestionDto.getQuestionDto().getQuestion().getId());
			setAttribute("dto", choiceQuestionDto);
			QuestionViewDto QuestionViewDto = new QuestionViewDto() ;
			setAttribute("questionViewDto", QuestionViewDto);
			setAttribute("questionType", choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId());
			setAttribute("index", 0); 
		}
		return "/examcore/question/choice/preview";
	}
	public ChoiceQuestionService getChoiceQuestionService() {
		return choiceQuestionService;
	}
	public void setChoiceQuestionService(ChoiceQuestionService choiceQuestionService) {
		this.choiceQuestionService = choiceQuestionService;
	}
	@RequestMapping("/readMultiChoice")
	public String readMultiChoice(){
		index();
		String page="/examcore/question/choice/readMultiChoice";
		return page;
	}
	//暂无题型
	@RequestMapping("/saveReadMultiChoice")
	public String saveReadMultiChoice(ChoiceQuestionDto choiceQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		choiceQuestionDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_READ_MULTICHOICE);
		save(choiceQuestionDto,request,modelMap,response);
		
		if(choiceQuestionDto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题 保存
			printWriterAjax(response, printClose());
			return null;
		}
		
		String redirctUrl="redirect:/question/choice/pointIndex";
		if(choiceQuestionDto.getQuestionDto().getSaveType()!=ConstantTe.QUESTION_SAVETYPE_SAVE){
			return LIST_PAGE;
		}
		return redirctUrl;
	}
	/**
	 * 排序试题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/detail/{step}")
	public String detail(ChoiceQuestionDto choiceQuestionDto,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap){
		setAttribute("step", step);
		
		if(step==1){
			preSortSave(choiceQuestionDto, request);
				
			setAttribute("dto", choiceQuestionDto);
			setAttribute("questionType", choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId());
		}
		return "/examcore/question/sortquestion/detailSort";
		
	}
	
	/**
	 * 排序试题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/detail2/{step}")
	public String detail2(ChoiceQuestionDto choiceQuestionDto,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap){
		setAttribute("step", step);
		if(step==1){
			preSortSave(choiceQuestionDto, request);
				
			setAttribute("dto", choiceQuestionDto);
			setAttribute("questionType", choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId());
		}
		return "/examcore/question/sortquestion/detailSort";
	}
	
	/**
	 * 排序处理
	 * @param choiceQuestionDto
	 * @param request
	 */
	private void preSortSave(ChoiceQuestionDto choiceQuestionDto, HttpServletRequest request) {
		List<String> contents=new ArrayList<String>();
		String[] aa=choiceQuestionDto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		Question question=choiceQuestionDto.getQuestionDto().getQuestion();
		for(int i=0;i<contents.size();i++){
			if(i==contents.size()-1){
				choiceQuestionDto.getChoiceQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach=new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i-1);
			QuestionDto questionDto= choiceQuestionDto.getQuestionDto();
			if(questionDto.getQuestionAttachs()==null){
				questionDto.setQuestionAttachs(new ArrayList<QuestionAttach>());
			}
			questionDto.getQuestionAttachs().add(attach);
		}
		//id
		String strId=getParamter("questionDto_question_id");
		if(org.apache.commons.lang.StringUtils.isNotEmpty(strId)){
			int id=Integer.parseInt(strId);
			choiceQuestionDto.getQuestionDto().getQuestion().setId(id);
		}
		//添加当前登录用户信息
		question.setLastUpdateBy(getLoginUserName());
		if(question.getId()==0){
			question.setCreateBy(getLoginUserName());
		}
		
//		处理选项
		String[] descs=getParaNamesByPrefix("fmbx1_");
		descs=extSort(descs,"fmbx1_");
		String[] isright=request.getParameterValues("isright");//正确答案---判断题
		String[] orderby=getParaNamesByPrefix("orderby_");//正确顺序---排序题
		List<ChoiceAnswer> answers=new ArrayList<ChoiceAnswer>();
		Set<Integer> ids_index=new HashSet<Integer>();
		if(choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_JUDGE){
			if(isright!=null){
				for(int i=0;i<isright.length;i++){
					ids_index.add(isright[i].hashCode()-65);
				}
			}
			for(int i=0;i<descs.length;i++){
				ChoiceAnswer answer=new ChoiceAnswer();
				answer.setDescription(getParamter(descs[i]));
				answer.setSequenceId(i);
				if(ids_index.contains(i)){
					answer.setIsright(1);
				}else{
					answer.setIsright(0);
				}
				answers.add(answer);
			}
		}else if(choiceQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_SORT){
			for(int i=0;i<descs.length;i++){
				ChoiceAnswer answer=new ChoiceAnswer();
				answer.setDescription(getParamter(descs[i]));
				answer.setSequenceId(i);
				answer.setOrderby(Integer.valueOf(getParamter(orderby[i+1]))-1);
				answers.add(answer);
			}
		}
		choiceQuestionDto.setChoiceAnswers(answers);
		checkChoiceType(choiceQuestionDto);
	}
	private String[] extSort(String[] descs,String prefix) {
		if(descs==null||descs.length<10){
			return descs;
		}
		boolean b=false;
		for(String s:descs){
			if(s.equalsIgnoreCase(prefix+"0")){
				b=true;
			}
		}
		String[] arr=new String[descs.length];
		for(int i=0;i<descs.length;i++){
			if(!b){
				arr[i]=prefix+(i+1);
			}else{
				arr[i]=prefix+(i);
			}
		}
		return arr;
	}
}
