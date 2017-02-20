package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.SpokenQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.exam.util.OppFlowSiftUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.oro.text.regex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/question/base/spokenQuestion")
public class SpokenQuestionController  extends QuestionController{
	
	@Autowired
	private SpokenQuestionService spokenQuestionService;
		
	@Autowired
	@Qualifier("choiceQuestionService")
	private ChoiceQuestionService choiceQuestionService;
	
	
	public static HttpServletRequest getHttpRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
			      .getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 创建口语题页面
	 * TODO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertForm", method = RequestMethod.GET)
	public String insertForm(HttpServletRequest request,ModelMap modelMap) throws Exception    
	{
	    request.setCharacterEncoding("utf-8");	    
	    SpokenQuestionDto dto =(SpokenQuestionDto)request.getSession().getAttribute("insertDto");	    
	    if(dto == null){
	    	dto = new SpokenQuestionDto();	    	
	    }
	    QuestionDto questinDto = (QuestionDto)getAttribute("questionDto");
	    if(questinDto == null){
	    	questinDto =  new QuestionDto();
	    }
	    modelMap.addAttribute("questinDto", questinDto);
	    modelMap.addAttribute("insertDto", dto);
	    return "pages/spokenquestion/add";
	}
	/**
	 * 创建口语题
	 * TODO saveType=0为普通的创建过程.saveType=2为审核后修改的过程(新增版本)
	 * @param request
	 * @param dto 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@ModelAttribute SpokenQuestionDto dto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws Exception    
    {
    	request.setCharacterEncoding("utf-8");   	
    	int saveType = 0;

    	try {
//    		List errorList = this.validTest(request, dto);
//    		if(!errorList.isEmpty()){
//	    		  request.getSession().setAttribute("insertDto", dto);
//	       		  return "redirect:/maintain/spokenQuestion/insertForm";
//    		}else{
    			Date date = new Date();
    			String userName = getLoginUserName();
    			dto.getQuestionDto().getQuestion().setCreateDate(date);
    			dto.getQuestionDto().getQuestion().setCreateBy(userName);
    			dto.getQuestionDto().getQuestion().setStatus(Question.QUESTION_STATUS_UNAUDIT);  	
    			dto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_SPOKEN);
    			dto.getQuestionDto().getQuestion().setIssubjectived(0);
    			dto = this.getParamTopics(dto);
    			if(StringUtils.isNotBlank(request.getParameter("saveType"))){
    	    		saveType= Integer.parseInt(request.getParameter("saveType"));
    	    	}
    	    	dto.getQuestionDto().setSaveType(saveType);     			
    	    	Map<Integer, String> templateTagMap = (Map<Integer, String>)request.getSession().getAttribute(OppFlowSiftUtil.SESSION_KEY_TEMPLATE_TAG_LIST);
    	    	createQuestionCode(modelMap, dto, response, request);//生成试题表(te_question)code
    	    	int questionId = spokenQuestionService.insertSpokenQuestion(dto);
    			String code = dto.getQuestionDto().getQuestion().getCode();
    			//保存标签
    			//List<Label>  lableList = saveLabelData.getLabelList(request, questionId, Label.IDTYPE_QUESTION);
    			//saveLabelService.saveData(lableList, null);
    			
    			request.getSession().removeAttribute("insertDto");
    			modelMap.addAttribute("success", "创建口语题成功!");
//    		} 			
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "创建口语题失败");
		}
    	
    	if(dto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
			printWriterAjax(response, printClose());			
			return null;
		}else{
			if(saveType==ConstantTe.QUESTION_SAVETYPE_SAVE){
				return toAddIndexUrl(dto.getQuestionDto());
			}
			return indexUrl;
		}
    	
     }
    
	/**
	 * 修改口语题页面
	 * TODO
	 * @param request type=2代表审核后修改过程，其他代表正常修改过程
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/modifyForm", method = RequestMethod.GET)
	public String modifyForm(HttpServletRequest request,ModelMap modelMap) throws Exception    
	{
	    request.setCharacterEncoding("utf-8");	
	    int pageNo = 0;
	    
	    int id = 0;
	    if(StringUtils.isNotBlank(request.getParameter("id"))){
	    	id = Integer.parseInt(request.getParameter("id"));
	    }
	    int saveType = 2;
	    if(StringUtils.isNotBlank(request.getParameter("saveType"))){
	    	saveType = Integer.parseInt(request.getParameter("saveType"));
	    }
	    SpokenQuestionDto dto =(SpokenQuestionDto)request.getSession().getAttribute("updateDto");	    
	    if(dto == null){
	    	dto = spokenQuestionService.getSpokenQuestionDto(id);    	
	    }
	    QuestionDto questinDto = (QuestionDto)getAttribute("questionDto");
	    if(questinDto == null){
	    	questinDto =  new QuestionDto();
	    }
	    if(StringUtils.isNotBlank(request.getParameter("pageNo"))){
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
	    int redictType = 0;
	    if(StringUtils.isNotBlank(request.getParameter("redictType"))){
	    	redictType = Integer.parseInt(request.getParameter("redictType"));
	    }
	    if(dto.getQuestionDto().getQuestion().getTeId()==0
	    		&& dto.getQuestionDto().getQuestion().getStatus()==Question.QUESTION_STATUS_AUDIT){
	    	saveType = ConstantTe.QUESTION_SAVETYPE_SAVEAS;
		}
	    modelMap.addAttribute("questinDto", questinDto);
	    modelMap.addAttribute("dto", dto);
	    modelMap.addAttribute("saveType", saveType);
	    modelMap.addAttribute("pageNo",pageNo);
	    modelMap.addAttribute("redictType", redictType);
	    return toModifyIndex(dto);
//	    return "pages/spokenquestion/modify";
	}
	/**
	 * 修改口语题
	 * TODO
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(@ModelAttribute SpokenQuestionDto dto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws Exception    
    {
    	request.setCharacterEncoding("utf-8");   	
    	int pageNo = 0;
    	try {      		
//    		List errorList = this.validTest(request, dto);
//    		if(!errorList.isEmpty()){
//	    		  request.getSession().setAttribute("updateDto", dto);
//	       		  return "redirect:/maintain/spokenQuestion/modifyForm?id="+dto.getQuestionDto().getQuestion().getId();
//    		}else{
    			Date date = new Date();
    			String userName = getLoginUserName();
    			dto.getQuestionDto().getQuestion().setLastUpdateBy(userName);
    			dto.getQuestionDto().getQuestion().setLastUpdateDate(date);
    			dto = this.getParamTopics(dto);
//    			dto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_UPDATE);    		
//    			Map<Integer, String> templateTagMap = (Map<Integer, String>)request.getSession().getAttribute(OppFlowSiftUtil.SESSION_KEY_TEMPLATE_TAG_LIST);
    			//设置修改类型，修改或另存
				setSaveType(request, dto);
    			spokenQuestionService.updateSpokenQuestion(dto);
    			request.getSession().removeAttribute("updateDto");
    			if(StringUtils.isNotBlank(request.getParameter("pageNo"))){
    				pageNo = Integer.parseInt(request.getParameter("pageNo"));
    			}
    			String code = dto.getQuestionDto().getQuestion().getCode();

    			modelMap.addAttribute("success", "修改口语题成功!");	
//    		}
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "修改口语题失败");
		}
    	
    	String redirctUrl="redirect:/question/base/questionList";
    	if(dto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
			printWriterAjax(response, printClose());			
			return null;
		}else{	
			return redirctUrl;
		}
    	
    }
	/**
	 * 预览口语题
	 * TODO 
	 * @param request
	 * @param
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/preview")
    public String preview(@ModelAttribute SpokenQuestionDto dto,HttpServletRequest request,ModelMap modelMap) throws Exception    
 {
		request.setCharacterEncoding("utf-8");
		try {
			// 分析dto
			dto = this.getParamTopics(dto);
			modelMap.addAttribute("dto", dto);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "预览口语题失败");
		}

		return "/examcore/question/spokenquestion/preview";
	}
    	
    /**
     * 处理多题干
     * TODO
     * @param dto
     * @return
     */
    private SpokenQuestionDto getParamTopics(SpokenQuestionDto dto){
    	if(StringUtils.isNotBlank(dto.getSpokenQuestion().getAnswer())){
    		dto.getSpokenQuestion().setAnswer(HtmlUtils.htmlEscape(dto.getSpokenQuestion().getAnswer()));
    	}
		HttpServletRequest request = getHttpRequest();
		// 处理题干材料
		Enumeration en = request.getAttributeNames();
		en = request.getParameterNames();
		List<String> contents = new ArrayList<String>();
		List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
//		while (en.hasMoreElements()) {
//			String a = (String) en.nextElement();
//			if (a.indexOf("content") == 0) {
//				contents.add(a);
//			}
//		}
//		Collections.sort(contents);
		String[] aa=dto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		for (int i = 0; i < contents.size(); i++) {
			if (i == contents.size()-1) {
				dto.getSpokenQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach = new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i - 1);
			if(dto.getQuestionDto().getQuestion() != null && dto.getQuestionDto().getQuestion().getId() != 0){
				attach.setQuestionId(dto.getQuestionDto().getQuestion().getId());
			}else{
				attach.setQuestionId(0);
			}
			questionAttachs.add(attach);
		}
		dto.getQuestionDto().setQuestionAttachs(questionAttachs);
		
		return dto;
    }
	/**
	 * 在添加子题目时候选择添加或者修改
	 * 
	 * @return
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex(){
		int questionId=(Integer)getAttribute("questionId");
		int saveType=(Integer)getAttribute("saveType");
		int responseType=(Integer)getAttribute("responseType");
		int teId=(Integer)getAttribute("te_id");
		
		QuestionDto dto= new QuestionDto();
		Question question=new Question();
		dto.setQuestion(question);
		dto.getQuestion().setTeId(teId);
		dto.setResponseType(responseType);
		dto.setSaveType(saveType);
		dto.getQuestion().setId(questionId);
		setAttribute("questionDto", dto);
		
		String path="insertForm";
		switch (saveType) {
			case ConstantTe.QUESTION_SAVETYPE_SAVE:
				path="insertForm";
				break;
			case ConstantTe.QUESTION_SAVETYPE_UPDATE:
				path="modifyForm?id="+questionId+"&saveType="+saveType;
				break;
		default:
			setAttribute("questionDto",null);
			break;
		}
		
		return "forward:/question/base/spokenQuestion/"+path;
	}
    /**
     * 表单验证
     * TODO
     * @param request
     * @param dto
     * @return
     */
   private List validTest(HttpServletRequest request,SpokenQuestionDto dto){
   	List errorList = new ArrayList();
		try {		
			String id = String.valueOf(dto.getQuestionDto().getQuestion().getId());
			String code = String.valueOf(dto.getQuestionDto().getQuestion().getCode());
			if(StringUtils.isBlank(code)){
				errorList.add("试题编码不能为空");
			}else {
				boolean isUnique=choiceQuestionService.checkUniqueCode(id,code);
				if(!isUnique){
					errorList.add("试题编码不能重复");
				}
			}
			PatternCompiler orocom=new Perl5Compiler(); 
			PatternMatcher matcher=new Perl5Matcher(); 
			if(dto.getSpokenQuestion().getRecordtime() != 0){
				  String recordtimetring = "^(([0-9]+[\\.]?[0-9]+)|[0-9])$";
			      Pattern pattern=orocom.compile(recordtimetring);    	          
			      if(!matcher.contains(String.valueOf(dto.getSpokenQuestion().getRecordtime()),pattern)){ 
			    	  errorList.add("录音必须为大于0的正数");
			      }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
		return errorList;

   }
}
