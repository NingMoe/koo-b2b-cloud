package com.koolearn.cloud.exam.examcore.question.controller;

import static com.koolearn.exam.question.entity.Question.QUESTION_TYPE_FILL_BLANK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.EssayQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.FillBlankAnswerUtil;

@Controller
@RequestMapping(value = "/question/base/essay")
public class EssayQuestionController extends QuestionController {

	@Autowired
	private EssayQuestionService essayQuestionService;

	private final String SESSION_ERRMSG_KEY = "errMsg";

	private final String SESSION_OBJ_KEY = "myObject";
	
	private final String ATTR_ESSAY_DTO="essayQuestionDTO";
	private final String LIST_PAGE="redirect:/question/base/questionList";

	/**
	 * 跳转到 填空题的录入页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String toInsert(HttpServletRequest request, ModelMap modelMap)
			throws Exception {
		String page="/examcore/question/essayquestion/add";
		//修改 另存
		if(getAttribute(ATTR_ESSAY_DTO)!=null){
			return page;
		}
		
		//进入添加页面 保存出错页面
		String e=(String)getSessionObject(SESSION_ERRMSG_KEY);
//		ResultDto result=new ResultDto();
		if(e!=null){
			removeSessionObject(SESSION_ERRMSG_KEY);
			setAttribute("errMsg", e);
			if(getSessionObject(SESSION_OBJ_KEY)!=null){
				setAttribute("essayQuestionDTO", getSessionObject(SESSION_OBJ_KEY));
				removeSessionObject(SESSION_OBJ_KEY);
			}
		}
		return page;
	}
	
	@RequestMapping(value = "/index2")
	public String toInsert2(HttpServletRequest request, ModelMap modelMap)
			throws Exception {
		String page="/examcore/question/essayquestion/add2";
		//修改 另存
		if(getAttribute(ATTR_ESSAY_DTO)!=null){
			return page;
		}
		
		//进入添加页面 保存出错页面
		String e=(String)getSessionObject(SESSION_ERRMSG_KEY);
//		ResultDto result=new ResultDto();
		if(e!=null){
			removeSessionObject(SESSION_ERRMSG_KEY);
			setAttribute("errMsg", e);
			if(getSessionObject(SESSION_OBJ_KEY)!=null){
				setAttribute("essayQuestionDTO", getSessionObject(SESSION_OBJ_KEY));
				removeSessionObject(SESSION_OBJ_KEY);
			}
		}
		return page;
	}

	/**
	 * 填空题、计算题的录入功能实现
	 * 
	 * @param dto
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute EssayQuestionDto essayQuestionDTO, HttpServletRequest request,HttpServletResponse response,
			ModelMap modelMap) throws Exception {
		preSave(essayQuestionDTO,request);
		int markType = essayQuestionDTO.getEssayQuestion().getMarkType(); 
		essayQuestionDTO.getQuestionDto().getQuestion().setIssubjectived(markType);
		try{
			if(essayQuestionDTO.getQuestionType()==QUESTION_TYPE_FILL_BLANK){
				FillBlankAnswerUtil.unWrap(essayQuestionDTO.getFillblankAnswers());
			}
			//生成试题编码
			createQuestionCode(modelMap, essayQuestionDTO, response, request);
			essayQuestionService.saveOrUpdate(essayQuestionDTO);
		}catch(Exception e){
			setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
			setSessionObject(SESSION_OBJ_KEY, essayQuestionDTO);
		}
		if(getSessionObject(SESSION_ERRMSG_KEY)==null){
			setSessionObject(SESSION_ERRMSG_KEY,"保存成功");
			if(essayQuestionDTO.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
				removeSessionObject(SESSION_ERRMSG_KEY);
				//保存/修改全部返回一个响应
				if(essayQuestionDTO.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_FILL_BLANK || essayQuestionDTO.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_NORMAL_FILL_BLANK){
					printWriterAjax(response, printClose());
				}
				return null;
			}
			
			if(essayQuestionDTO.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				removeSessionObject(SESSION_ERRMSG_KEY);
				return toAddIndexUrl(essayQuestionDTO.getQuestionDto());
			}
		}
		return indexUrl;
	} 
	
	/**
	 * 跳转到 计算题的录入页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/calcul")
	public String calculation(HttpServletRequest request, ModelMap modelMap)
			throws Exception {
		String page="/examcore/question/calculation/add";
//		修改 另存
		if(getAttribute(ATTR_ESSAY_DTO)!=null){
			return page;
		}
		//进入添加页面 保存出错页面
		String e=(String)getSessionObject(SESSION_ERRMSG_KEY);
//		ResultDto result=new ResultDto();
		if(e!=null){
			removeSessionObject(SESSION_ERRMSG_KEY);
			setAttribute("errMsg", e);
			if(getSessionObject(SESSION_OBJ_KEY)!=null){
				setAttribute("essayQuestionDTO", getSessionObject(SESSION_OBJ_KEY));
				removeSessionObject(SESSION_OBJ_KEY);
			}
		}
		return page;
	}
	
	
	/**
	 * 填空题、计算题的录入功能实现
	 * 
	 * @param dto
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/calculSave", method = RequestMethod.POST)
	public String calculSave(@ModelAttribute EssayQuestionDto essayQuestionDTO, HttpServletRequest request,HttpServletResponse response,
			ModelMap modelMap) throws Exception {
		preSave(essayQuestionDTO,request);
		try{
			//生成试题编码
			createQuestionCode(modelMap, essayQuestionDTO, response, request);
			essayQuestionService.saveOrUpdate(essayQuestionDTO);
		}catch(Exception e){
			setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
			setSessionObject(SESSION_OBJ_KEY, essayQuestionDTO);
		}
		if(getSessionObject(SESSION_ERRMSG_KEY)==null){
			setSessionObject(SESSION_ERRMSG_KEY,"保存成功");
			if(essayQuestionDTO.getQuestionDto().getResponseType()==ConstantTe.QUESTION_RESPONSETYPE_SUBITEM){//子试题窗口
				removeSessionObject(SESSION_ERRMSG_KEY);
				//保存/修改全部返回一个响应
				if(essayQuestionDTO.getQuestionDto().getQuestion().getQuestionTypeId()==Question.QUESTION_TYPE_FILL_CALCULATION){
					printWriterAjax(response, printClose());
					return null;
				}
			}
			if(essayQuestionDTO.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				removeSessionObject(SESSION_ERRMSG_KEY);
				return toAddIndexUrl(essayQuestionDTO.getQuestionDto());
			}
		}
		return LIST_PAGE;
	}
	
	
	/**
	 * 试题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/detail/{step}")
	public String detail(EssayQuestionDto essayQuestionDTO,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap){
		setAttribute("step", step);
		if(step==1){
			preSave(essayQuestionDTO, request);
			setAttribute("dto", essayQuestionDTO);
			setAttribute("questionType", essayQuestionDTO.getQuestionDto().getQuestion().getQuestionTypeId());
		}
		String questionType = getParamter("questionType");
		if(Integer.valueOf(questionType)==Question.QUESTION_TYPE_FILL_BLANK){
			return "/examcore/question/essayquestion/detailessay";
		}else if(Integer.valueOf(questionType)==Question.QUESTION_TYPE_FILL_CALCULATION){
			return "/examcore/question/calculation/detailcaculation";
		}
		return "error";
	}
	
	
	/**
	 * 保存前封装对象
	 * @param essayQuestionDTO
	 * @param request
	 */
	private void preSave(EssayQuestionDto essayQuestionDTO, HttpServletRequest request) {
		Enumeration en=request.getParameterNames();
		List<String> contents=new ArrayList<String>();
		List<String> answerName=new ArrayList<String>();
		while(en.hasMoreElements()){
			String a=(String)en.nextElement();
//			if(a.indexOf("content")==0){
//				contents.add(a);
//			}
			if(a.endsWith(".answer")){
				answerName.add(a);
			}
		}
//		Collections.sort(contents);
		String[] aa=essayQuestionDTO.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		Collections.sort(answerName);
		answerName=extSort(answerName);
		Question question=essayQuestionDTO.getQuestionDto().getQuestion();
		for(int i=0;i<contents.size();i++){
			if(i==contents.size()-1){
				essayQuestionDTO.getEssayQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach=new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i-1);
			QuestionDto questionDto= essayQuestionDTO.getQuestionDto();
			if(questionDto.getQuestionAttachs()==null){
				questionDto.setQuestionAttachs(new ArrayList<QuestionAttach>());
			}
			questionDto.getQuestionAttachs().add(attach);
		}
		//id
		String strId=getParamter("questionDto_question_id");
		if(org.apache.commons.lang.StringUtils.isNotEmpty(strId)){
			int id=Integer.parseInt(strId);
			essayQuestionDTO.getQuestionDto().getQuestion().setId(id);
		}

//		String answer[] = request.getParameterValues("answer");// 答案描述
		List<FillblankAnswer> answers = new ArrayList<FillblankAnswer>();
		if (answerName!= null&&answerName.size()>0) {
			Integer j = 1;
			for (int i = 0; i < answerName.size(); i++) {
				FillblankAnswer fillblankAnswer = new FillblankAnswer();
				fillblankAnswer.setAnswer(getParamter(answerName.get(i)));
				fillblankAnswer.setSequnceId(j++);

				// 计算题 判断 单位 的位置 //单位的位置:0-无单位；1-前面；2-后面；
				if (question.getQuestionTypeId() == Question.QUESTION_TYPE_FILL_CALCULATION) {
					
					String company = request.getParameter("answer.company");
					if(company!=null&&!company.equals("")){
						fillblankAnswer.setCompany(company);
					}else{
						fillblankAnswer.setCompany("");
					}
					int place = Integer.parseInt(request.getParameter("answer.place"));
					fillblankAnswer.setPlace(place);
					
					
					/*String answerStr = getParamter(answerName.get(i));
					answerStr = answerStr.trim();
					String regex = "\\d*";// 全部是数字
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(answerStr);
					if (matcher.matches()) {
						fillblankAnswer.setPlace(0);// 无单位，全是数字
					} else {
						regex = "\\d+$";
						pattern = Pattern.compile(regex);
						matcher = pattern.matcher(answerStr);
						if (matcher.find()) {
							fillblankAnswer.setPlace(1);// 单位 在 前面
						}
						regex = "^(\\d+)(.*)";
						pattern = Pattern.compile(regex);
						matcher = pattern.matcher(answerStr);
						if (matcher.matches()) {
							fillblankAnswer.setPlace(2);// 单位在 后面
						}
					}*/
				}
				if(question.getQuestionTypeId()==Question.QUESTION_TYPE_FILL_BLANK){
					fillblankAnswer.setExts(FillBlankAnswerUtil.fitAnswerExt(i, 0, request));
				}
				answers.add(fillblankAnswer);
			}
			essayQuestionDTO.setFillblankAnswers(answers);
		}
	}

	private List<String> extSort(List<String> answerName) {
		if(answerName==null||answerName.size()<11){
			return answerName;
		}
		List<String> list=new ArrayList<String>();
		List<Integer> list2=new ArrayList<Integer>();
		Map<Integer,String> index_name=new HashMap<Integer,String>();
		for(String s:answerName){
			int index=Integer.parseInt(s.substring(s.indexOf("[")+1, s.indexOf("]")));
			index_name.put(index, s);
			list2.add(index);
		}
		Collections.sort(list2);
		for(int i=0;i<list2.size();i++){
			list.add(index_name.get(list2.get(i)));
		}
		return list;
		
	}

	/**
	 * 修改选择题,包括另存??
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex( HttpServletRequest request) throws Exception{
		int questionId=(Integer)getAttribute("questionId");
		int questionType=(Integer)getAttribute("questionType");
		int saveType=(Integer)getAttribute("saveType");
		int responseType=(Integer)getAttribute("responseType");
		int teId=(Integer)getAttribute("te_id"); 

		//获取选择题对象 放到attr中
		EssayQuestionDto dto = null;
		if(questionId!=0){
			dto = essayQuestionService.getEssayQuestionDTO(questionId);
		}else{
			dto=new EssayQuestionDto();
			QuestionDto questionDto=new QuestionDto();
			dto.setQuestionDto(questionDto);
			Question question=new Question();
			questionDto.setQuestion(question);
		}
		dto.getQuestionDto().getQuestion().setTeId(teId);
		dto.getQuestionDto().setResponseType(responseType);
		dto.getQuestionDto().setSaveType(saveType);
		setAttribute(ATTR_ESSAY_DTO, dto);
		
		//根据类型跳转到具体action
		String path="index";
		switch (questionType) {
			case Question.QUESTION_TYPE_FILL_BLANK:
				FillBlankAnswerUtil.wrap(dto.getFillblankAnswers());
				path="index";
				break;	
//			case Question.QUESTION_TYPE_NORMAL_FILL_BLANK:
//				path="index2";
//				break;	
			case Question.QUESTION_TYPE_FILL_CALCULATION:
				path="calcul";
				break;
			
		default:
			setAttribute(ATTR_ESSAY_DTO,null);
			break;
		}
		if(dto.getQuestionDto().getQuestion().getTeId()==0){
	    	return toModifyIndex(dto);
	    }
		//各个action 根据savetpye 跳转到是录入页面还是试题列表
		return "forward:/question/base/essay/"+path;
	}


}
