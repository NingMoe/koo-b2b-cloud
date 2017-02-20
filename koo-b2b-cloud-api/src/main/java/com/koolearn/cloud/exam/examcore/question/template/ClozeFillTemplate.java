package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

/**
 * 复合听写暂未使用 
 * @author yangzhenye
 */
public class ClozeFillTemplate extends TemplateFtl{
	public static ClozeFillTemplate instance(){
		ClozeFillTemplate t=new ClozeFillTemplate();
			return t;
	}
	public  String outTemplate(ComplexQuestionDto dto,
			QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/clozeFillQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
