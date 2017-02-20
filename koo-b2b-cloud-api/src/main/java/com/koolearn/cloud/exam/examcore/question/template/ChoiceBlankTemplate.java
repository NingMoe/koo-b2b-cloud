package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;
/**
 * 选择填空题 36 
 * @author yangzhenye
 */
public class ChoiceBlankTemplate extends TemplateFtl{
	public static ChoiceBlankTemplate instance(){
		ChoiceBlankTemplate t=new ChoiceBlankTemplate();
			return t;
	}
	public  String outTemplate(ComplexQuestionDto  dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		String fileName="questions/choiceBlankQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result=outTemplate(data);
		return result;
	}
}
