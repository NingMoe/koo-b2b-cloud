package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.WhriteQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/question/base/whriteQuestion")
public class WhriteQuestionController  extends QuestionController{
	
	@Autowired
	private WhriteQuestionService whriteQuestionService;
		
	public static HttpServletRequest getHttpRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
			      .getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 创建写作题页面
	 * TODO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertForm", method = RequestMethod.GET)
	public String insertForm(HttpServletRequest request,ModelMap modelMap) throws Exception    
	{
	    request.setCharacterEncoding("utf-8");	    
	    WhriteQuestionDto dto =(WhriteQuestionDto)request.getSession().getAttribute("insertDto");	    
	    if(dto == null){
	    	dto = new WhriteQuestionDto();	    	
	    }
	    QuestionDto questionDto = (QuestionDto)getAttribute("questionDto");
	    if(questionDto == null){
	    	questionDto =  new QuestionDto();
	    }
	    modelMap.addAttribute("questionDto", questionDto);
	    modelMap.addAttribute("insertDto", dto);
	    return "/examcore/question/whriteQuestion/add";
	}
	/**
	 * 创建写作题
	 * TODO saveType=0为普通的创建过程.saveType=2为审核后修改的过程(新增版本)
	 * @param request
	 * @param dto 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@ModelAttribute WhriteQuestionDto dto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws Exception    
    {
    	request.setCharacterEncoding("utf-8");   	
    	int saveType = 0;

    	try {
    		createQuestionCode(modelMap, dto, response, request);
		
			Date date = new Date();
			String userName = getLoginUserName();
			dto.getQuestionDto().getQuestion().setCreateDate(date);
			dto.getQuestionDto().getQuestion().setCreateBy(userName);
			dto.getQuestionDto().getQuestion().setStatus(Question.QUESTION_STATUS_UNAUDIT);  	
			dto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_WHRITE);
			dto.getQuestionDto().getQuestion().setIssubjectived(0);
			dto = this.getParamTopics(dto);
			if(StringUtils.isNotBlank(request.getParameter("saveType"))){
	    		saveType= Integer.parseInt(request.getParameter("saveType"));
	    	}
	    	dto.getQuestionDto().setSaveType(saveType);     			
			whriteQuestionService.insertWhriteQuestion(dto);
			
			request.getSession().removeAttribute("insertDto");
			modelMap.addAttribute("success", "创建写作题成功!");
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "创建写作题失败");
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
	 * 修改写作题页面
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
	    if(StringUtils.isNotBlank(request.getParameter("pageNo"))){
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}

	    int id = 0;
	    if(StringUtils.isNotBlank(request.getParameter("id"))){
	    	id = Integer.parseInt(request.getParameter("id"));
	    }
	    int type = 0;
	    if(StringUtils.isNotBlank(request.getParameter("type"))){
	    	type = Integer.parseInt(request.getParameter("type"));
	    }
	    WhriteQuestionDto dto = (WhriteQuestionDto)questionBaseService.getExamQuestionNoCache(Question.QUESTION_TYPE_WHRITE, id);
//	    dto = (WhriteQuestionDto)questionBaseService.setQuestionExt(dto, com.koolearn.cloud.exam.examcore.util.QuestionUtil.getSchoolId(request));
	    if(dto == null){
	    	dto = whriteQuestionService.getWhriteQuestionDto(id);    	
	    }
	    QuestionDto questionDto = (QuestionDto)getAttribute("questionDto");
	    if(questionDto == null){
	    	questionDto =  new QuestionDto();
	    }
	    
	    int redictType = 0;
	    if(StringUtils.isNotBlank(request.getParameter("redictType"))){
	    	redictType = Integer.parseInt(request.getParameter("redictType"));
	    }
	    if(dto.getQuestionDto().getQuestion().getTeId()==0
	    		&& dto.getQuestionDto().getQuestion().getStatus()==Question.QUESTION_STATUS_AUDIT){
	    	type = ConstantTe.QUESTION_SAVETYPE_SAVEAS;
		}
	    modelMap.addAttribute("questionDto", questionDto);
	    modelMap.addAttribute("dto", dto);
	    modelMap.addAttribute("type", type);
	    modelMap.addAttribute("pageNo",pageNo);
	    modelMap.addAttribute("redictType", redictType);
	    if(dto.getQuestionDto().getQuestion().getTeId()==0){
	    	return toModifyIndex(dto);
	    }else{
	    	return "/examcore/question/whriteQuestion/modify";
	    }
	}
	/**
	 * 修改写作题
	 * TODO
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(@ModelAttribute WhriteQuestionDto dto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws Exception    
    {
    	request.setCharacterEncoding("utf-8");   	
    	try {      		
    			Date date = new Date();
    			String userName = getLoginUserName();
    			dto.getQuestionDto().getQuestion().setLastUpdateBy(userName);
    			dto.getQuestionDto().getQuestion().setLastUpdateDate(date);
    			dto = this.getParamTopics(dto);
    			//设置修改类型，修改或另存
    			setSaveType(request, dto);
    			whriteQuestionService.updateWhriteQuestion(dto);
    			request.getSession().removeAttribute("updateDto");
    			
    			modelMap.addAttribute("success", "修改写作题成功!");	
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "修改写作题失败");
		}
    	
    	if(dto.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
			printWriterAjax(response, printClose());			
			return null;
		}else{	
			return indexUrl;
		}
    }
	/**
	 * 预览写作题
	 * TODO 
	 * @param request
	 * @param dto 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/preview")
    public String preview(@ModelAttribute WhriteQuestionDto dto,HttpServletRequest request,ModelMap modelMap) throws Exception    
 {
		request.setCharacterEncoding("utf-8");
		try {
			// 分析dto
			dto = this.getParamTopics(dto);
			modelMap.addAttribute("whriteQuestionDto", dto);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "预览写作题失败");
		}

		return "/examcore/question/whriteQuestion/preview";
	}
    /**
     * 处理多题干及处理答题框高度
     * TODO
     * @param dto
     * @return
     */
    private WhriteQuestionDto getParamTopics(WhriteQuestionDto dto){
    	if(StringUtils.isNotBlank(dto.getWhriteQuestion().getAnswer())){
    		dto.getWhriteQuestion().setAnswer(HtmlUtils.htmlEscape(dto.getWhriteQuestion().getAnswer()));
    	}
		HttpServletRequest request = getHttpRequest();
		// 处理题干材料
		Enumeration en = request.getAttributeNames();
		en = request.getParameterNames();
		List<String> contents = new ArrayList<String>();
		List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
		String[] aa=dto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		for (int i = 0; i < contents.size(); i++) {
			if (i == contents.size()-1) {
				dto.getWhriteQuestion().setTopic(request.getParameter(contents.get(i)));
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
		//处理答题框高度
		if(dto.getWhriteQuestion().getBoxheight() == 0 && StringUtils.isNotBlank(request.getParameter("otherHeight"))){
			dto.getWhriteQuestion().setBoxheight(Integer.parseInt(request.getParameter("otherHeight")));
		}
		return dto;
    }
	/**
	 * 在添加子题目时候选择添加或者修改
	 * 
	 * @return
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex(){
		Integer questionId = (Integer) getAttribute("questionId");
		Integer saveType = (Integer) getAttribute("saveType");
		Integer responseType = (Integer) getAttribute("responseType");
		Integer teId=(Integer)getAttribute("te_id");
		
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
				path="modifyForm?id="+questionId+"&type="+saveType;
				break;
		default:
			setAttribute("questionDto",null);
			break;
		}
		
		return "forward:/question/base/whriteQuestion/"+path;
	}
}
