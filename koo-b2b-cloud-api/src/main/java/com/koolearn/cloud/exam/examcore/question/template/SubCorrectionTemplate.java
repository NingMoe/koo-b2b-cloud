package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class SubCorrectionTemplate extends TemplateFtl{
	public static SubCorrectionTemplate instance(){
		SubCorrectionTemplate t=new SubCorrectionTemplate();
			return t;
	}
	public  String outTemplate(CorrectionQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/subCorrectionQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
