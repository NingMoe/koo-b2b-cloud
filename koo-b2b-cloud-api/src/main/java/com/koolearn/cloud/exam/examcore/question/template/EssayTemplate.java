package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class EssayTemplate extends TemplateFtl{

	public static EssayTemplate instance(){
		EssayTemplate t=new EssayTemplate();
			return t;
	}
	public  String outTemplate(EssayQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/essayQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
