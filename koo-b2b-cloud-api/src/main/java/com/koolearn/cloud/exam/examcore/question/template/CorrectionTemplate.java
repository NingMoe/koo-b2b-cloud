package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class CorrectionTemplate extends TemplateFtl{
	public static CorrectionTemplate instance(){
		CorrectionTemplate t=new CorrectionTemplate();
			return t;
	}
	public  String outTemplate(ComplexCorrectionQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/correctionQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
