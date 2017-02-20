package com.koolearn.cloud.exam.examcore.question.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.MatrixQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.service.MatrixQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.util.BeanUtils;

@Controller
@RequestMapping("/question/base/matrix")
public class MatrixQuestionController extends QuestionController{

	private final static String PAGE_PATH = "/examcore/question/";
	@Autowired
	private MatrixQuestionService matrixQuestionService;
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public String insert(@ModelAttribute MatrixQuestionDto dto,HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,PrintWriter out){
		try{
			//要删除的题目ID
			String delQuestionId =  request.getParameter("delQuestionId");
			String delSubId =  request.getParameter("delSubId");
			Map<String,String> idMap = new HashMap<String,String>();
			idMap.put("qid", delQuestionId);
			idMap.put("sid", delSubId);
			MatrixQuestion matrixQuestion = dto.getMatrixQuestion();
			preProcess(dto,request);
			
			QuestionDto questionDto = dto.getQuestionDto();
			Question question = questionDto.getQuestion();
			createQuestionCode(modelMap, dto, response, request);//生成试题表(te_question)code
			matrixQuestionService.saveOrUpdate(dto,idMap);
			if(question.getTeId() != 0){
				out.print(printClose());
				return null;
			}
			//各个列表 修改后 跳转
			//modelMap.addAttribute("message", "保存成功");
			if(questionDto.getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVE){
				//表格showForm转换
				if(matrixQuestion.getShowForm()==MatrixQuestion.TABLE_FOMR){
					dto.getQuestionDto().getQuestion().setQuestionTypeId(MatrixQuestion.QUESTION_TYPE_TABLE_BIAOGE);
				}else if(matrixQuestion.getShowForm()==MatrixQuestion.DRAG_FORM){
					dto.getQuestionDto().getQuestion().setQuestionTypeId(MatrixQuestion.QUESTION_TYPE_TABLE_TUOZHUAI);
				}else{
					dto.getQuestionDto().getQuestion().setQuestionTypeId(MatrixQuestion.QUESTION_TYPE_TABLE_BIAOGE);
				}
				return toAddIndexUrl(dto.getQuestionDto());
			}
			return indexUrl;
		}catch(Exception e){
			e.printStackTrace();
			modelMap.addAttribute("dto", dto);
			modelMap.addAttribute("message", "保存失败");
		}
		
		return indexUrl;
	}
	
	@RequestMapping(value="/toInsert/{showForm}")
	public String toInsert(@PathVariable("showForm")int showForm,HttpServletRequest request,ModelMap modelMap){
		setAttribute("showForm", showForm);
		return PAGE_PATH + "matrix/add";
	}
	
	@RequestMapping(value="/modifyIndex")
	public String toEdit(HttpServletRequest request,ModelMap modelMap){
		try{
			Integer questionId=(Integer)getAttribute("questionId");
			Integer saveType=(Integer)getAttribute("saveType");
			Integer responseType = (Integer)getAttribute("responseType");
			Integer questionType = (Integer)getAttribute("questionType");
			Integer teId = (Integer)getAttribute("te_id");
			modelMap.put("questionType",questionType);
			MatrixQuestionDto dto = null;
			if(saveType != ConstantTe.QUESTION_SAVETYPE_SAVE){
				dto = matrixQuestionService.getByQuestionId(questionId);
				dto.getQuestionDto().setSaveType(saveType);
				dto.getQuestionDto().setResponseType(responseType);
				modelMap.put("dto", dto);
				
				int showForm = dto.getMatrixQuestion().getShowForm();
				setAttribute("showForm", showForm);
			}
			if(teId != null && teId != 0){
				modelMap.put("teId", teId);
				return PAGE_PATH + "matrix/subAdd";
			}else{
				//return PAGE_PATH + "matrix/add";
				return toModifyIndex(dto);
			}
			
		} catch (Exception ex) {
			modelMap.put("ex", ex);
			ex.printStackTrace();
			return "/error";
		}
	}
	
	@RequestMapping(value="/ajaxInsert",method=RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String ajaxInsert(@RequestBody ComplexQuestionDto dto,PrintWriter out){
		Map<String,String> map = new HashMap<String,String>();
		try{
//			dtoJson = URLDecoder.decode(dtoJson, "UTF-8");
//			System.out.println(dtoJson);
//			dtoJson = StringUtils.substring(dtoJson, 0, dtoJson.length()-1);
//			ComplexQuestionDto dto = JSON.parseObject(dtoJson, ComplexQuestionDto.class);
			QuestionDto questionDto = dto.getQuestionDto();
			List<QuestionAttach> attachs = questionDto.getQuestionAttachs();
			if(attachs != null && attachs.size() > 0){
				dto.getComplexQuestion().setTopic(attachs.get(0).getContent());
				List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
				for(int i=1;i<attachs.size();i++){
					QuestionAttach attach = new QuestionAttach();
					attach.setContent(attachs.get(i).getContent());
					attach.setSequenceId(1);
					questionAttachs.add(attach);
				}
				questionDto.setQuestionAttachs(questionAttachs);
			}
//			complexQuestionService.insert(dto);
			map.put("result", "true");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.print(JSON.toJSONString(map));
			out.close();
		}
		
		return null;
	}
	
	/**
	 * @author DuHongLin 2014-06-18 15:07
	 */
	private void fixChoiceQuestionDto(MatrixQuestionDto dto){
		if(null != dto.getChoiceQuestionDtos() && dto.getChoiceQuestionDtos().size() > 0){
			for(ChoiceQuestionDto choiceQuestionDto : dto.getChoiceQuestionDtos()){
				if(null == choiceQuestionDto.getQuestionDto()){
					choiceQuestionDto.setQuestionDto(dto.getQuestionDto());
				}
				choiceQuestionDto.getChoiceQuestion().setQuestionId(choiceQuestionDto.getQuestionDto().getQuestion().getId());
			}
		}
	}
	
	/**
	 * 预览
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/preview/{step}")
	public String preview(MatrixQuestionDto dto,@PathVariable("step")int step, HttpServletRequest request){
		if(step == 1){
			preProcess(dto,request);
			this.fixChoiceQuestionDto(dto);
			setAttribute("dto", dto);
			setAttribute("questionType", dto.getQuestionType());
		}
		setAttribute("step", step);
		return PAGE_PATH +"/matrix/preview";
	}
	
	
	/**
	 * 预览	拖拽 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/preview2/{step}")
	public String preview2(MatrixQuestionDto dto,@PathVariable("step")int step, HttpServletRequest request){
		if(step == 1){
			preProcess(dto,request);
			this.fixChoiceQuestionDto(dto);
			setAttribute("dto", dto);
			setAttribute("questionType", dto.getQuestionType());
		}
		setAttribute("step", step);
		return PAGE_PATH +"/matrix/preview2";
	}
	
	@RequestMapping(value="/preview3/{step}")
	public String preview3(MatrixQuestionDto dto,@PathVariable("step")int step, HttpServletRequest request){
		if(step == 1){
			preProcess(dto,request);
			this.fixChoiceQuestionDto(dto);
			setAttribute("dto", dto);
			setAttribute("questionType", dto.getQuestionType());
			
//			TestProcessDto process=new TestProcessDto();
//			process.setPaperObjectList(new ArrayList<Object>());
//			process.getPaperObjectList().add(dto);
//			
//			process.setPaperItemDtoList(new ArrayList<PaperItemDto>());
//			PaperItemDto paperItemDto=new PaperItemDto();
//			paperItemDto.setQuestionNo(1);
//			process.getPaperItemDtoList().add(paperItemDto);
//			
//			setAttribute("process", process);
			setAttribute("i", 0); 
		}
		setAttribute("step", step);
		return PAGE_PATH +"/matrix/preview3";
	}
	
	/**
	 * 保存之前处理题干和创建人
	 * @param dto
	 * @param request
	 */
	private void preProcess(MatrixQuestionDto dto,HttpServletRequest request){
//		MatrixQuestion matrixQuestion = dto.getMatrixQuestion();
//		if(null == matrixQuestion){
//			matrixQuestion = new MatrixQuestion();
//		}
		QuestionDto questionDto = dto.getQuestionDto();
		Question question = questionDto.getQuestion();
		if(question != null){
			question.setQuestionTypeId(Question.QUESTION_TYPE_TABLE);
			question.setCreateBy(getLoginUserName());
			question.setLastUpdateBy(getLoginUserName());
		}
		//处理题干材料
		List<String> contents=new ArrayList<String>();
		String[] aa=dto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		if(contents.size() > 0){
			List<QuestionAttach> questionAttachs = new ArrayList<QuestionAttach>();
			for(int i=0;i<contents.size();i++){
				if(i==contents.size()-1){
					dto.getMatrixQuestion().setTopic(request.getParameter(contents.get(i)));
					continue;
				}
				QuestionAttach attach = new QuestionAttach();
				attach.setContent(request.getParameter(contents.get(i)));
				attach.setSequenceId(i);
				if(question != null){
					attach.setQuestionId(question.getId());
				}
				questionAttachs.add(attach);
			}
			questionDto.setQuestionAttachs(questionAttachs);
		}
		
		
		
		
		
		List<ChoiceQuestionDto> choiceQuestionDtos = dto.getChoiceQuestionDtos();
		List<ChoiceAnswer> choiceAnswers = dto.getChoiceAnswers();
		//处理各个题目下的答案
		if(choiceQuestionDtos != null && choiceQuestionDtos.size() > 0){
			for(int i=0;i<choiceQuestionDtos.size();i++){
				ChoiceQuestionDto choiceQuestionDto = choiceQuestionDtos.get(i);
				ChoiceQuestion choiceQuestion = choiceQuestionDto.getChoiceQuestion();
				String[] answerStr = choiceQuestion.getAnswer().split(",");
				Map answerMap = new HashMap();
				if(answerStr!=null&&answerStr.length>0)
				for(int j=0;j<answerStr.length;j++){
					int answerInt = Integer.parseInt(answerStr[j]);
					answerMap.put(answerInt, answerInt);
				}
				List<ChoiceAnswer> choiceAnswersN = new ArrayList<ChoiceAnswer>();
				if(choiceAnswers != null && choiceAnswers.size() > 0){
					int k = 1;
					for(ChoiceAnswer choiceAnswer : choiceAnswers){
						choiceAnswer.setSequenceId(k-1);
						choiceAnswer.setDescription(HtmlUtils.htmlEscape(choiceAnswer.getDescription()));
						if(answerMap!=null&&answerMap.containsKey(k)){
							choiceAnswer.setIsright(1);
						}else{
							choiceAnswer.setIsright(0);
						}
						k++;
						ChoiceAnswer choiceAnswerN = new ChoiceAnswer();
						choiceAnswersN.add(choiceAnswerN);
						BeanUtils.copyProperties(choiceAnswerN, choiceAnswer);
					}
				}
				choiceQuestionDto.setChoiceAnswers(choiceAnswersN);
			}
		}
		dto.setChoiceQuestionDtos(choiceQuestionDtos);
	}
}
