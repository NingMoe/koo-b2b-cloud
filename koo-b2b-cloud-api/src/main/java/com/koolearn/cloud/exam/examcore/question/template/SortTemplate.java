package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class SortTemplate extends TemplateFtl {
	private String bastPath;

	public static SortTemplate instance() {
		SortTemplate t = new SortTemplate();
		return t;
	}

	public String outTemplate(ChoiceQuestionDto dto,QuestionViewDto questionViewDto)
			throws IOException, TemplateException {
		String fileName = "questions/sortQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
