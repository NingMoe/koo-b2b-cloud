package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class ChoiceTemplate extends TemplateFtl{
	
	public static ChoiceTemplate instance(){
		ChoiceTemplate t=new ChoiceTemplate();
		return t;
	}
	public  String outTemplate(ChoiceQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/choiceQuestion.ftl";
		setTemplateFile(fileName);
		
		Map<String, Object> data = installMethods();
		if (questionViewDto.getViewType() == QuestionViewDto.view_type_question
				&& questionViewDto.isExaming())
		{
			dto.unSortAnswers();
		}
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
	public static void main(String[] a){
		ChoiceTemplate t = new ChoiceTemplate();
	}
}
