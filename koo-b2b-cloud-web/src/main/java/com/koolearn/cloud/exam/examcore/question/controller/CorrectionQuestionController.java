package com.koolearn.cloud.exam.examcore.question.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.CorrectionQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.CorrectionQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;

@Controller
@RequestMapping("/question/base/correctionQuestion")
public class CorrectionQuestionController  extends QuestionController{

	@Autowired
	private CorrectionQuestionService correctionQuestionService;
	
	@RequestMapping("/insertForm")
	public String insertForm(HttpServletRequest request,ModelMap modelMap){
		return "/examcore/question/correction/add";
	}
	
	@RequestMapping("/save")
	public String save(ComplexCorrectionQuestionDto dto,HttpServletResponse response, HttpServletRequest request,ModelMap modelMap) throws Exception{
		
		createQuestionCode(modelMap, dto, response, request);
		preProcess(dto, request);
		
		correctionQuestionService.saveOrUpdate(dto);
		if(dto.getQuestionDto().getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
			return toAddIndexUrl(dto.getQuestionDto());
		}
		return indexUrl;
	}
	
	@RequestMapping("/modifyIndex")
	public String modifyIndex(HttpServletRequest request,ModelMap modelMap){
		int questionId=(Integer)getAttribute("questionId");
		int questionType=(Integer)getAttribute("questionType");
		int saveType=(Integer)getAttribute("saveType");
		int responseType=(Integer)getAttribute("responseType");
		int teId=(Integer)getAttribute("te_id");
		ComplexCorrectionQuestionDto dto= null;
		if(questionId!=0){
			dto=correctionQuestionService.getByQuestionId(questionId);
		}else{
			dto=new ComplexCorrectionQuestionDto();
			QuestionDto questionDto=new QuestionDto();
			dto.setQuestionDto(questionDto);
			Question question=new Question();
			question.setQuestionTypeId(questionType);
			questionDto.setQuestion(question);
		}
		dto.getQuestionDto().getQuestion().setTeId(teId);
		dto.getQuestionDto().setResponseType(responseType);
		dto.getQuestionDto().setSaveType(saveType);
		modelMap.put("dto", dto);
		if(dto.getQuestionDto().getQuestion().getTeId()==0){
	    	return toModifyIndex(dto);
	    }
		return "/examcore/question/correction/add";
	}
	
	/**
	 * 试题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/preview/{step}")
	public String preview(ComplexCorrectionQuestionDto dto,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap){
		setAttribute("step", step);
		if(step==1){
			preProcess(dto, request);
			if(dto != null){
				String topic = dto.getComplexQuestion().getTopic();
				topic = topic.replace("<span class=\"hot\">[</span>", "").replace("<span class=\"hot\">]</span>", "");
				dto.getComplexQuestion().setTopic(topic);
			}
			setAttribute("dto", dto);
			try{
			setAttribute("questionType", dto.getQuestionDto().getQuestion().getQuestionTypeId());
			}catch (NullPointerException e) {
				e.fillInStackTrace();
			}
			setAttribute("index", 0); 
		}
		return "/examcore/question/correction/preview";
	}
	
	@SuppressWarnings("rawtypes")
	private void preProcess(ComplexCorrectionQuestionDto dto,HttpServletRequest request){
		if(dto != null){
			QuestionDto questionDto = dto.getQuestionDto();
			Question question = questionDto.getQuestion();
			if(question == null){
				question = new Question();
			}
			question.setCreateBy(getLoginUserName());
			question.setLastUpdateBy(getLoginUserName());
			question.setQuestionTypeId(dto.getQuestionType());
			question.setAtomic(1);
			question.setIssubjectived(Question.QUESTION_SUBJECTIVE_Y);
			questionDto.setQuestion(question);
			//处理题干材料
			Enumeration en=request.getAttributeNames();
			en=request.getParameterNames();
			List<String> contents=new ArrayList<String>();
			while(en.hasMoreElements()){
				String a=(String)en.nextElement();
				if(a.indexOf("content")==0){
					contents.add(request.getParameter(a));
				}
			}
			if(contents.size() > 0){
//				Collections.reverse(contents);
				List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
				for(int i=0;i<contents.size();i++){
					if(i==0){
						dto.getComplexQuestion().setTopic(contents.get(0));
						continue;
					}
					QuestionAttach attach = new QuestionAttach();
					attach.setContent(contents.get(i));
					attach.setSequenceId(i);
					if(question != null){
						attach.setQuestionId(question.getId());
					}
					questionAttachs.add(attach);
				}
				questionDto.setQuestionAttachs(questionAttachs);
			}
			List<CorrectionQuestionDto> correctionDtos = dto.getCorrectionQuestionDtos();
			if(correctionDtos != null){
				for(int i=0;i<correctionDtos.size();i++){
					CorrectionQuestionDto correctionQuestionDto = correctionDtos.get(i);
					QuestionDto qdto = correctionQuestionDto.getQuestionDto();
					if(qdto == null){
						qdto = new QuestionDto();
					}
					Question q = qdto.getQuestion();
					if(q == null){
						q = new Question();
					}
					q.setCode(question.getCode()+"_"+(i+1));
					q.setCreateBy(getLoginUserName());
					q.setLastUpdateBy(getLoginUserName());
					q.setQuestionTypeId(Question.QUESTION_TYPE_SUB_CORRECTION);
					q.setIssubjectived(Question.QUESTION_SUBJECTIVE_Y);
					qdto.setQuestion(q);
					correctionQuestionDto.setQuestionDto(qdto);
					CorrectionQuestion correctionQuestion = correctionQuestionDto.getCorrectionQuestion();
					correctionQuestion.setOrderNum(i+1);
				}
			}
		}
		
	}
}
