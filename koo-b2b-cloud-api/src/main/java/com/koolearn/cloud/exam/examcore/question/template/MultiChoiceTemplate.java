package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koolearn.exam.testProcess.dto.TestProcessDto;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class MultiChoiceTemplate extends TemplateFtl{
	public static MultiChoiceTemplate instance(){
		MultiChoiceTemplate t=new MultiChoiceTemplate();
			return t;
	}
	public  String outTemplate(IExamQuestionDto dto, QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/multiChoice.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}
}
