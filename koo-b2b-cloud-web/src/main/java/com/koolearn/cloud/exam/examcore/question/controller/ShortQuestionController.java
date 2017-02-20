package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.ShortQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.exam.util.QuestionUtil;
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
@RequestMapping("/question/base/shortQuestion")
public class ShortQuestionController extends QuestionController {

	@Autowired
	private ShortQuestionService shortQuestionService;

	@Autowired
	private ChoiceQuestionService choiceQuestionService;

	public static HttpServletRequest getHttpRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 创建简答题页面 TODO
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertForm", method = RequestMethod.GET)
	public String insertForm(HttpServletRequest request, ModelMap modelMap)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		ShortQuestionDto dto = (ShortQuestionDto) request.getSession()
				.getAttribute("insertDto");
		if (dto == null) {
			dto = new ShortQuestionDto();
		}
		QuestionDto questinDto = (QuestionDto) getAttribute("questionDto");
		if (questinDto == null) {
			questinDto = new QuestionDto();
		}
		modelMap.addAttribute("insertDto", dto);
		modelMap.addAttribute("questinDto", questinDto);
		return "/examcore/question/shortQuestion/add";
	}

	/**
	 * 创建简答题 TODO saveType=0为普通的创建过程.saveType=2为审核后修改的过程(新增版本)
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute ShortQuestionDto dto,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int saveType = 0;
		try {
			List<String> errorList = this.validTest(request, dto);
			if (!errorList.isEmpty()) {
				request.getSession().setAttribute("insertDto", dto);
				return "redirect:/question/base/shortQuestion/insertForm";
			} else {
				
				if(dto.getQuestionDto().getQuestion().getId()==0){
					createQuestionCode(modelMap, dto, response, request);
				}

				String userName = getLoginUserName();
				Date date = new Date();
				
				dto.getQuestionDto().getQuestion().setCreateDate(date);
				dto.getQuestionDto().getQuestion().setCreateBy(userName);
				dto.getQuestionDto().getQuestion().setStatus(Question.QUESTION_STATUS_UNAUDIT);
				dto.getQuestionDto().getQuestion().setQuestionTypeId(
						Question.QUESTION_TYPE_SHORT);

				// 分析dto
				dto = this.getParamTopics(dto);
				if (StringUtils.isNotBlank(request.getParameter("saveType"))) {
					saveType = Integer.parseInt(request
							.getParameter("saveType"));
				}
				dto.getQuestionDto().setSaveType(saveType);
				shortQuestionService.insertShortQuestion(dto);

				request.getSession().removeAttribute("insertDto");
				modelMap.addAttribute("success", "创建简答题成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "创建简答题失败");
		}

		if (dto.getQuestionDto().getResponseType() == ConstantTe.QUESTION_RESPONSETYPE_SUBITEM) {// 子试题窗口
			printWriterAjax(response, printClose());
			return null;
		} else {
			return "redirect:/question/base/questionList";
		}

	}

	/**
	 * 修改简答题页面 TODO
	 * 
	 * @param request
	 *            type=2代表新增版本，其他代表正常修改过程
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/modifyForm", method = RequestMethod.GET)
	public String modifyForm(HttpServletRequest request, ModelMap modelMap)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		int pageNo = 0;
		int id = 0;
		if (StringUtils.isNotBlank(request.getParameter("id"))) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		if (StringUtils.isNotBlank(request.getParameter("pageNo"))) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		int type = 2;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		ShortQuestionDto dto = (ShortQuestionDto)questionBaseService.getExamQuestionNoCache(Question.QUESTION_TYPE_SHORT, id);//(ShortQuestionDto) request.getSession().getAttribute("updateDto");
//		dto = (ShortQuestionDto)questionBaseService.setQuestionExt(dto, com.koolearn.cloud.exam.examcore.util.QuestionUtil.getSchoolId(request));
		if (dto == null) {
			dto = shortQuestionService.getShortQuestionDto(id);
		}
		QuestionDto questinDto = (QuestionDto) getAttribute("questionDto");
		if (questinDto == null) {
			questinDto = new QuestionDto();
		}
		int redictType = 0;
		if (StringUtils.isNotBlank(request.getParameter("redictType"))) {
			redictType = Integer.parseInt(request.getParameter("redictType"));
		}
		//modelMap.addAttribute("questinDto", questinDto);
		modelMap.addAttribute("dto", dto);
		modelMap.addAttribute("type", type);
		modelMap.addAttribute("pageNo", pageNo);
		modelMap.addAttribute("redictType", redictType);
		toModifyIndex(dto);
		return "/examcore/question/shortQuestion/modify";
	}

	/**
	 * 修改简答题 TODO
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute ShortQuestionDto dto,
			HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		try {
			List<String> errorList = this.validTest(request, dto);
			if (!errorList.isEmpty()) {
				request.getSession().setAttribute("updateDto", dto);
				return "redirect:/question/base/shortQuestion/modifyForm?id="
						+ dto.getQuestionDto().getQuestion().getId();
			} else {
				
				if(dto.getQuestionDto().getQuestion().getId()==0){
					createQuestionCode(modelMap, dto, response, request);
				}

				Date date = new Date();
				String userName = getLoginUserName();
				dto.getQuestionDto().getQuestion().setLastUpdateBy(userName);
				dto.getQuestionDto().getQuestion().setLastUpdateDate(date);

				// 分析dto
				dto = this.getParamTopics(dto);
				//设置修改类型，修改或另存
				setSaveType(request, dto);
				shortQuestionService.updateShortQuestion(dto);
				request.getSession().removeAttribute("updateDto");

				modelMap.addAttribute("success", "修改简答题成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "修改简答题失败");
		}
		if (dto.getQuestionDto().getResponseType() == ConstantTe.QUESTION_RESPONSETYPE_SUBITEM) {// 子试题窗口
			printWriterAjax(response, printClose());
			return null;
		} else {
			return "redirect:/question/base/questionList";
		}

	}

	/**
	 * 预览简答题 TODO
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preview")
	public String preview(@ModelAttribute ShortQuestionDto dto,
			HttpServletRequest request, ModelMap modelMap) throws Exception {
		request.setCharacterEncoding("utf-8");
		try {
			QuestionViewDto questionViewDto = (QuestionViewDto)request.getAttribute("questionViewDto");
			modelMap.addAttribute("questionViewDto", questionViewDto);
			// 分析dto
			dto = this.getParamTopics(dto);
			modelMap.addAttribute("shortQuestionDto", dto);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("error", "创建简答题失败");
		}

		return "/examcore/question/shortQuestion/preview";
	}

	/**
	 * 处理多题干及关键词及处理答题框高度 TODO
	 * 
	 * @param dto
	 * @return
	 */
	private ShortQuestionDto getParamTopics(ShortQuestionDto dto) {
		if (StringUtils.isNotBlank(dto.getShortQuestion().getAnswer())) {
			dto.getShortQuestion().setAnswer(HtmlUtils.htmlEscape(dto.getShortQuestion().getAnswer()));
		}
		HttpServletRequest request = getHttpRequest();
		// 处理题干材料
		Enumeration en = request.getAttributeNames();
		en = request.getParameterNames();
		List<String> contents = new ArrayList<String>();
		List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
		String[] aa = dto.getQuestionDto().getTextarea_param().split(",");
		for (int i = 0; i < aa.length; i++) {
			contents.add(aa[i]);
		}
		for (int i = 0; i < contents.size(); i++) {
			if (i == contents.size() - 1) {
				dto.getShortQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach = new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i - 1);
			if (dto.getQuestionDto().getQuestion() != null && dto.getQuestionDto().getQuestion().getId() != 0) {
				attach.setQuestionId(dto.getQuestionDto().getQuestion().getId());
			} else {
				attach.setQuestionId(0);
			}
			questionAttachs.add(attach);
		}
		dto.getQuestionDto().setQuestionAttachs(questionAttachs);
		// 处理关键词
		if (dto.getShortQuestion().getMarktype() == QuestionUtil.MARK_TYPE_SYSTEM
				&& StringUtils.isNotBlank(request.getParameter("keyWords"))) {
			dto.getShortQuestion().setScorestandad(request.getParameter("keyWords"));
			dto.getQuestionDto().getQuestion().setIssubjectived(1);
		} else {
			dto.getQuestionDto().getQuestion().setIssubjectived(0);
		}
		// 处理答题框高度
		if (dto.getShortQuestion().getBoxheight() == 0
				&& StringUtils.isNotBlank(request.getParameter("otherHeight"))) {
			dto.getShortQuestion().setBoxheight(Integer.parseInt(request
					.getParameter("otherHeight")));
		}
		return dto;
	}

	/**
	 * 在添加子题目时候选择添加或者修改
	 * 
	 * @return
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex() {
		int questionId = (Integer) getAttribute("questionId");
		int saveType = (Integer) getAttribute("saveType");
		int responseType = (Integer)getAttribute("responseType");
		int teId = (Integer) getAttribute("te_id");

		QuestionDto dto = new QuestionDto();
		Question question = new Question();
		dto.setQuestion(question);
		dto.getQuestion().setTeId(teId);
		dto.setResponseType(responseType);
		dto.setSaveType(saveType);
		dto.getQuestion().setId(questionId);
		setAttribute("questionDto", dto);

		String path = "insertForm";
		switch (saveType) {
		case ConstantTe.QUESTION_SAVETYPE_SAVE:
			path = "insertForm";
			break;
		case ConstantTe.QUESTION_SAVETYPE_UPDATE:
			path = "modifyForm?id=" + questionId + "&type=" + saveType;
			break;
		default:
			setAttribute("questionDto", null);
			break;
		}

		return "forward:/question/base/shortQuestion/" + path;
	}

	/**
	 * 表单验证 TODO
	 * 
	 * @param request
	 * @param dto
	 * @return
	 */
	private List<String> validTest(HttpServletRequest request, ShortQuestionDto dto) {
		List<String> errorList = new ArrayList<String>();
		try {
			if (dto.getShortQuestion().getMarktype() == QuestionUtil.MARK_TYPE_SYSTEM
					&& StringUtils.isBlank(request.getParameter("keyWords"))) {
				errorList.add("关键词不能为空");
			}
			if (dto.getShortQuestion().getMarktype() == QuestionUtil.MARK_TYPE_MANUAL
					&& StringUtils.isBlank(dto.getShortQuestion().getScorestandad())) {
				errorList.add("评分标准不能为空");
			}
			if (StringUtils.isBlank(dto.getShortQuestion().getAnswerreference())) {
				errorList.add("参考答案不能为空");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorList;

	}

	public ChoiceQuestionService getChoiceQuestionService() {
		return choiceQuestionService;
	}

	public void setChoiceQuestionService(
			ChoiceQuestionService choiceQuestionService) {
		this.choiceQuestionService = choiceQuestionService;
	}
}
