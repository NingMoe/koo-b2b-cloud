package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class TableTemplate extends TemplateFtl{
	public static TableTemplate instance(){
		TableTemplate t=new TableTemplate();
			return t;
	}
	
	public  String outTemplate(MatrixQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/tableQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
