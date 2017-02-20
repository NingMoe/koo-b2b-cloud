package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto;

import freemarker.template.TemplateException;

public class WriteTemplate extends TemplateFtl {

	public static WriteTemplate instance() {
		WriteTemplate result = new WriteTemplate();
		return result;
	}


	public String outTemplate(WhriteQuestionDto dto,QuestionViewDto questionViewDto)
			throws IOException, TemplateException {
		String fileName = "questions/writeQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}

}
