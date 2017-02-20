package com.koolearn.cloud.exam.examcore.question.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.*;
import com.koolearn.cloud.exam.examcore.question.service.ChoiceQuestionService;
import com.koolearn.cloud.exam.examcore.question.service.ComplexQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.ResultDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/question/base/complex")
public class ComplexQuestionController extends QuestionController {

	private final static String PAGE_PATH = "/examcore/question/";
	@Autowired
	private ComplexQuestionService complexQuestionService;
	@Autowired
	private ChoiceQuestionService choiceQuestionService;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute ComplexQuestionDto dto,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		try {
			// 要删除的题目ID和答案ID
			String delQuestionId = request.getParameter("delQuestionId");
			String delSubId = request.getParameter("delSubId");
			String delAnswerId = request.getParameter("delAnswerId");
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("qid", delQuestionId);
			idMap.put("sid", delSubId);
			idMap.put("aid", delAnswerId);
			if (null == dto.getComplexQuestion()) {
				dto.setComplexQuestion(new ComplexQuestion());
			}

			preProcess(dto, request);
			createQuestionCode(modelMap, dto, response, request);
//			dto.getQuestionDto().getQuestionBankExt().setSchoolId(QuestionUtil.getSchoolId(request));
			complexQuestionService.saveOrUpdate(dto, idMap);
			if (dto.getQuestionDto().getResponseType() == ConstantTe.QUESTION_RESPONSETYPE_SUBITEM) {// 子试题
																									// 保存
				printWriterAjax(response, printClose());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.addAttribute("dto", dto);
			modelMap.addAttribute("message", "保存失败");
		}
		if(dto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
			return toAddIndexUrl(dto.getQuestionDto());
		}
		return indexUrl;
	}

	// 选择型完型填空添加页面
	@RequestMapping(value = "/toInsert")
	public String toInsert(HttpServletRequest request, ModelMap modelMap) {
		return PAGE_PATH + "complex/add";
	}

	// 填空型完型填空添加页面
	@RequestMapping(value = "/clozeInsert")
	public String clozeInsert(HttpServletRequest request, ModelMap modelMap) {
		return PAGE_PATH + "complex/cloze";
	}

	// 复合听写添加页面
	@RequestMapping(value = "/compositeInsert")
	public String compositeInsert(HttpServletRequest request, ModelMap modelMap) {

		return PAGE_PATH + "complex/composite";
	}

	// 选择填空添加页面
	@RequestMapping(value = "/cbInsert")
	public String choiceBlankInsert(HttpServletRequest request,
			ModelMap modelMap) {

		return PAGE_PATH + "complex/choiceBlank";
	}

	@RequestMapping(value = "/modifyIndex/{saveType}/{questionId}")
	public String toEdit(@PathVariable("saveType") int saveType,
			@PathVariable("questionId") int questionId,
			HttpServletRequest request, ModelMap modelMap) {
		try {
			int responseType = (Integer) getAttribute("responseType");
			int teId = (Integer) getAttribute("te_id");
			int questionType = (Integer) getAttribute("questionType");
			ComplexQuestionDto dto = null;
			if (questionId != 0)
				dto = complexQuestionService.getByQuestionId(questionId);
			else {
				dto = new ComplexQuestionDto();
				QuestionDto questionDto = new QuestionDto();
				dto.setQuestionDto(questionDto);
				dto.setQuestionType(questionType);
				Question question = new Question();
				questionDto.setQuestion(question);
			}
			dto.getQuestionDto().getQuestion().setTeId(teId);
			dto.getQuestionDto().setSaveType(saveType);
			dto.getQuestionDto().setResponseType(responseType);
			modelMap.put("dto", dto);
			
			return toModifyIndex(dto);
		} catch (Exception ex) {
			modelMap.put("ex", ex);
			ex.printStackTrace();
			return "/error";
		}
	}

	// 编码唯一性验证
	@RequestMapping(value = "/uniqueCode", method = RequestMethod.POST)
	public String uniqueCode(@RequestBody String jsonStr,
			HttpServletResponse response) {
		try {
			jsonStr = URLDecoder.decode(jsonStr, "UTF-8");
			jsonStr = StringUtils.substring(jsonStr, 0, jsonStr.length() - 1);
			JSONArray jsonArr = JSON.parseArray(jsonStr);
			String strId = ((JSONObject) jsonArr.get(0))
					.getString("questionDto.question.id");
			String strCode = ((JSONObject) jsonArr.get(1))
					.getString("questionDto.question.code");
			boolean isUnique = choiceQuestionService.checkUniqueCode(strId,
					strCode);
			ResultDto result = new ResultDto();
			if (isUnique) {
				result.setResult("true");
			} else {
				result.setResult("false");
				result.setMessage("试题编码重复");
			}
			printWriterAjax(response, JSON.toJSONString(result));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 预览
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/preview/{step}")
	public String preview(ComplexQuestionDto dto,
			@PathVariable("step") int step, HttpServletRequest request) {
		if (step == 1) {
			preProcess(dto, request);
			List<ChoiceQuestionDto> choiceQuestionDTOs = dto.getChoiceQuestionDtos();
			if(choiceQuestionDTOs!=null&&choiceQuestionDTOs.size()>0){
				int composeType=-1;
				for(ChoiceQuestionDto dto2:choiceQuestionDTOs){
					if(dto2.getChoiceQuestion()==null){
						dto2.setChoiceQuestion(new ChoiceQuestion());
					}
					dto2.getChoiceQuestion().setQuestionId(dto.getQuestionDto().getQuestion().getId());
					Integer ct=dto2.getChoiceQuestion().getComposeType();
					if(composeType==-1&&ct!=null){
						composeType=ct.intValue();
					}
				}
				for(ChoiceQuestionDto dto2:choiceQuestionDTOs){
					if(composeType!=-1){
						dto2.getChoiceQuestion().setComposeType(composeType);
					}
				}
			}
			setAttribute("dto", dto);
			setAttribute("questionType", dto.getQuestionType());


			List<EssayQuestionDto> list = dto.getEssayQuestionDTOs();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					EssayQuestionDto essayQuestionDTO = list.get(i);
					if(null == essayQuestionDTO.getEssayQuestion()){
						essayQuestionDTO.setEssayQuestion(new EssayQuestion());
					}

					QuestionDto questionDto = essayQuestionDTO.getQuestionDto();
					if (questionDto == null) {
						questionDto = new QuestionDto();
						essayQuestionDTO.setQuestionDto(questionDto);
					}
					Question question = questionDto.getQuestion();
					if (question == null) {
						question = new Question();
						question.setQuestionTypeId(Question.QUESTION_TYPE_FILL_BLANK);
						questionDto.setQuestion(question);
					}
				}
			}
			List<ChoiceQuestionDto> list2 = dto.getChoiceQuestionDtos();
			if (list2 != null && list2.size() > 0) {
				for (int i = 0; i < list2.size(); i++) {
					ChoiceQuestionDto choiceQuestionDto = list2.get(i);
					QuestionDto questionDto = choiceQuestionDto
							.getQuestionDto();
					if (questionDto == null) {
						questionDto = new QuestionDto();
						choiceQuestionDto.setQuestionDto(questionDto);
					}
					Question question = questionDto.getQuestion();
					if (question == null) {
						question = new Question();
						questionDto.setQuestion(question);
					}
				}
			}

			setAttribute("i", 0);
		}
		setAttribute("step", step);

		return PAGE_PATH + "/complex/preview";
	}

	private void preProcess(ComplexQuestionDto dto, HttpServletRequest request) {
		if (dto != null) {
			QuestionDto questionDto = dto.getQuestionDto();
			Question question = questionDto.getQuestion();
			List<ChoiceAnswer> answers = dto.getChoiceAnswers();
			List<ChoiceQuestionDto> choiceQuestionDTOs = dto
					.getChoiceQuestionDtos();
			if (choiceQuestionDTOs != null && choiceQuestionDTOs.size() > 0) {
				for (ChoiceQuestionDto choiceQuestionDto : choiceQuestionDTOs) {
					QuestionDto qDto = choiceQuestionDto.getQuestionDto();
					if (qDto != null) {
						Question cq = qDto.getQuestion();
						if (cq != null) {
							if (dto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_BLANK
									|| dto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_FILL_BLANK
									|| dto.getQuestionType() == Question.QUESTION_TYPE_CHOICE_WORD
									|| dto.getQuestionType() == Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK
									|| dto.getQuestionType() == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD) {
								cq.setQuestionTypeId(Question.QUESTION_TYPE_DANXUAN);
							}
							cq.setCreateBy(getLoginUserName());
							cq.setLastUpdateBy(getLoginUserName());
						}
					}
					List<ChoiceAnswer> choiceAnswers = choiceQuestionDto
							.getChoiceAnswers();
					if (CollectionUtils.isNotEmpty(answers)) {
						String an = choiceQuestionDto.getChoiceQuestion()
								.getAnswer();
						choiceAnswers = new ArrayList<ChoiceAnswer>();
						for (int i = 0; i < answers.size(); i++) {
							ChoiceAnswer ca = answers.get(i);
							ChoiceAnswer canswer = new ChoiceAnswer();
							canswer.setDescription(ca.getDescription());
							if (i + 1 == Integer.parseInt(an)) {
								canswer.setIsright(1);
							}
							choiceAnswers.add(canswer);
						}
						choiceQuestionDto.setChoiceAnswers(choiceAnswers);
					}
					if (choiceAnswers != null && choiceAnswers.size() > 0) {

						for (int i = 0; i < choiceAnswers.size(); i++) {
							ChoiceAnswer answer = choiceAnswers.get(i);
							answer.setSequenceId(i);
						}
					}
				}
			}
			List<EssayQuestionDto> essayQuestionDTOs = dto
					.getEssayQuestionDTOs();
			if (essayQuestionDTOs != null && essayQuestionDTOs.size() > 0) {
				for (EssayQuestionDto essayQuestionDTO : essayQuestionDTOs) {
					QuestionDto qDto = essayQuestionDTO.getQuestionDto();
					if (qDto != null) {
						Question eq = qDto.getQuestion();
						if (eq != null) {
							eq.setCreateBy(getLoginUserName());
							eq.setLastUpdateBy(getLoginUserName());
						}
					}

				}
			}
			if (question == null) {
				question = new Question();
			}
			question.setCreateBy(getLoginUserName());
			question.setLastUpdateBy(getLoginUserName());
			question.setQuestionTypeId(dto.getQuestionType());
			questionDto.setQuestion(question);
			// 处理题干材料
			Enumeration en = request.getAttributeNames();
			en = request.getParameterNames();
			List<String> contents = new ArrayList<String>();
			while (en.hasMoreElements()) {
				String a = (String) en.nextElement();
				if (a.indexOf("content") == 0) {
					contents.add(request.getParameter(a));
				}
			}
			if (contents.size() > 0) {
				Collections.reverse(contents);
				List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
				for (int i = 0; i < contents.size(); i++) {
					if (i == 0) {
						dto.getComplexQuestion().setTopic(contents.get(0));
						continue;
					}
					QuestionAttach attach = new QuestionAttach();
					attach.setContent(contents.get(i));
					attach.setSequenceId(i);
					if (question != null) {
						attach.setQuestionId(question.getId());
					}
					questionAttachs.add(attach);
				}
				questionDto.setQuestionAttachs(questionAttachs);
			}
			if (null == dto.getComplexQuestion().getQuestionId()) {
				dto.getComplexQuestion().setQuestionId(
						dto.getQuestionDto().getQuestion().getId());
			}
		}

	}

	// added @ 0320 暂无题型
	@RequestMapping(value = "/insertLcb", method = RequestMethod.POST)
	public String insertListenChoiceBlank(
			@ModelAttribute ComplexQuestionDto dto, HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		String returnResult = "forward:/question/base/complex/toInsert";
		try {
			// 要删除的题目ID和答案ID
			String delQuestionId = request.getParameter("delQuestionId");
			String delSubId = request.getParameter("delSubId");
			String delAnswerId = request.getParameter("delAnswerId");
			Map<String, String> idMap = new HashMap<String, String>();
			idMap.put("qid", delQuestionId);
			idMap.put("sid", delSubId);
			idMap.put("aid", delAnswerId);

			if (null == dto.getComplexQuestion()) {
				dto.setComplexQuestion(new ComplexQuestion());
			}

			preProcess(dto, request);
			complexQuestionService.saveOrUpdate(dto, idMap);
			if (dto.getQuestionDto().getResponseType() == ConstantTe.QUESTION_RESPONSETYPE_SUBITEM) {// 子试题
																									// 保存
				printWriterAjax(response, printClose());
				return null;
			}

			modelMap.addAttribute("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.addAttribute("dto", dto);
			modelMap.addAttribute("message", "保存失败");
		}
		if (dto.getQuestionDto().getSaveType() != 0) {
			returnResult = indexUrl;
		}
		return returnResult;
	}

	// 选词填空添加页面
	@RequestMapping(value = "/choiceWordInsert")
	public String choiceWordInsert(HttpServletRequest request, ModelMap modelMap) {
		String questionType = request.getParameter("questionType");
		modelMap.addAttribute("questionType", questionType);
		return PAGE_PATH + "complex/choiceWord";
	}

	// 选词填空添加页面
	@RequestMapping(value = "/oralTrainingInsert")
	public String oralTrainingInsert(HttpServletRequest request,
			ModelMap modelMap) {
		return PAGE_PATH + "complex/oralTraining";
	}
}
