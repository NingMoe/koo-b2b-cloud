package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.template.TemplateFtl;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * 试题
 * @author yangzhenye
 */
@Controller
@RequestMapping("/question/view")
public class QuestionTemplateController extends QuestionBaseController {
	private static final Logger logger = Logger.getLogger(QuestionTemplateController.class);
	/**
	 * 题目结构html
	 */
	@ResponseBody
	@RequestMapping("/body")
	public String body(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		try {
			String questionId = getParamter("questionId");
			Question question = questionBaseService.getQuestionById(new Integer(questionId));
			IExamQuestionDto dto = null;
			dto = questionBaseService.getExamQuestionNoCache(new Integer(questionId), question.getQuestionTypeId());
//			dto = questionBaseService.setQuestionExt(dto, QuestionUtil.getSchoolId(request));
			String html = "";
			html = TemplateFtl.outHtml(dto, null);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "";
	}
	
	/**
	 * 题目列表页面，预览试题，传入题目Id,题目类型id,需要解析传viewType
	 * @param request
	 * @param modelMap
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		try {
			String path = "";
			String questionId = getParamter("questionId");
			String questionTypeId = getParamter("questionTypeId");
			String viewType = getParamter("viewType");
			QuestionViewDto viewDto = new QuestionViewDto();
			if(StringUtils.isNotBlank(viewType)){
				viewDto.setViewType(Integer.parseInt(viewType));
			}
			IExamQuestionDto dto = null;
			dto = questionBaseService.getExamQuestionNoCache(new Integer(questionTypeId), new Integer(questionId));
//			dto = questionBaseService.setQuestionExt(dto, QuestionUtil.getSchoolId(request));
			viewDto=QuestionUtil.getSubQuestionViewDto(dto, viewDto);
			switch(new Integer(questionTypeId)){
			case Question.QUESTION_TYPE_DANXUAN:
			case Question.QUESTION_TYPE_DUOXUAN:
				path = "/examcore/question/choice/preview";
				break;
			case Question.QUESTION_TYPE_SPOKEN:
				path = "/examcore/question/spokenquestion/preview";
				break;
			case Question.QUESTION_TYPE_FILL_BLANK://普通填空
			case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空
				path = "/examcore/question/essayquestion/detailessay";
				break;
			case Question.QUESTION_TYPE_SHORT:
				path = "/examcore/question/shortQuestion/preview";
				break;
			case Question.QUESTION_TYPE_WHRITE:
				path = "/examcore/question/whriteQuestion/preview";
				break;
			case Question.QUESTION_TYPE_READ:
			case Question.QUESTION_TYPE_LISTEN:
				path = "/examcore/question/read/preview";
				break;
			case Question.QUESTION_TYPE_CORRECTION:
				path = "/examcore/question/correction/preview";
				break;
			case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			case Question.QUESTION_TYPE_CLOZE_FILL_BLANK://完形填空
			case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空
				path = "/examcore/question/complex/preview";
				break;
			case Question.QUESTION_TYPE_TABLE:
				path = "/examcore/question/matrix/preview";
				break;
			
			}
			if(flagErrorOne()){
				dealErrorOne(viewDto, dto, request);
			}
			setAttribute("questionViewDto", viewDto);
			setAttribute("dto", dto);
			setAttribute("step", 1);
			setAttribute("questionType", dto.getQuestionType());
			
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "";
	}

	/**
	 * 标识为错体本来源
	 * @param
	 * @throws Exception
	 */
	private boolean flagErrorOne() throws Exception {
		if(StringUtils.isNotEmpty(getParamter("errorOne"))){
			return true;
		}
		return false;
	}

	/**
	 * 题目列表页面，预览试题，传入题目Id,题目类型id,需要解析传viewType
	 * @param request
	 * @param modelMap
	 * @param response
	 * @return
	 */
	@RequestMapping("/preview")
	public String preview(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		try {
			String questionId = getParamter("questionId");
			String questionTypeId = getParamter("questionTypeId");
			String viewType = getParamter("viewType");
			QuestionViewDto viewDto = new QuestionViewDto();
			if(StringUtils.isNotBlank(viewType)){
				viewDto.setViewType(Integer.parseInt(viewType));
			}
			IExamQuestionDto dto = null;
			dto = questionBaseService.getExamQuestionNoCache(new Integer(questionTypeId), new Integer(questionId));
//			dto = questionBaseService.setQuestionExt(dto, com.koolearn.cloud.exam.examcore.util.QuestionUtil.getSchoolId(request));
			if(flagErrorOne()){
				//如果是改错去除主观子题
				dealErrorOne(viewDto, dto, request);
			}
			setAttribute("questionViewDto", viewDto);
			setAttribute("dto", dto);
			return "/examcore/question/q-question-all-view";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "";
	}
}
