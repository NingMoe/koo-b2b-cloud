package com.koolearn.cloud.exam.examcore.paper.controller;


import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.paper.service.TemplateTypeService;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperStructureService;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.question.template.TemplateFtl;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PaperAssistant extends BaseController {

//	@Autowired
//	protected PaperTemplateService paperTemplateService;
	@Autowired
	protected TestPaperService testPaperService;
	@Autowired
	protected TestPaperStructureService paperStructureService;
	@Autowired
	protected QuestionBaseService questionBaseService;
    @Autowired
    protected QuestionService questionService;
//	@Autowired
//	protected QuestionSearchService questionSearchService;
//	@Autowired
//	protected TemplateTypeService templateTypeService;
	private static final Logger logger = Logger.getLogger(PaperAssistant.class);
	protected void fillPapreSubScore(String code, QuestionViewDto view,
			Map<String, List<PaperSubScore>> codeSubScore) {
		if(!codeSubScore.containsKey(code)){
			return;
		}
		List<PaperSubScore> list=codeSubScore.get(code);
		int size=list.size();
		for(int i=0;i<size;i++){
			view.getSubDtos().get(i).setScore(list.get(i).getPoints());
		}
	}

	protected Map<String,String> findQuestionHtmlMap(List<IExamQuestionDto> dtos,List<TestPaperStructureDto> structures) throws IOException, TemplateException {
		Map<String,IExamQuestionDto> codeQuestion=new HashMap<String, IExamQuestionDto>();
		for(IExamQuestionDto dto:dtos){
			codeQuestion.put(dto.getQuestionDto().getQuestion().getCode(), dto);
		}
		
		Map<String,String> codeHtmlMap=new HashMap<String, String>();
		TestPaperStructure structure;
		int index=1;
		String code;
		QuestionViewDto questionViewDto;
		for(int i=0,size=structures.size();i<size;i++){
			structure=structures.get(i).getTestPaperStructure();
			if(structure.getStructureType()==TestPaperStructure.structure_type_question){
				code=structure.getName();
				questionViewDto=new QuestionViewDto();
				questionViewDto.setViewType(QuestionViewDto.view_type_all);
				questionViewDto.setQuestionNo(index+"");
				if(structure.getPoints()==null){
					questionViewDto.setScore(0);
				}else{
					questionViewDto.setScore(structure.getPoints());
				}
				questionViewDto.setStructureId(structure.getParent());
				questionViewDto= QuestionUtil.getSubQuestionViewDto(codeQuestion.get(code), questionViewDto);
				List<PaperSubScore> subScores= structures.get(i).getSubScores();
				if(CollectionUtils.isNotEmpty(subScores)){
					for(int j=0,size2=subScores.size();j<size2;j++){
						questionViewDto.getSubDtos().get(j).setScore(subScores.get(j).getPoints());
					}
				}
				codeHtmlMap.put(code, TemplateFtl.outHtml(codeQuestion.get(code), questionViewDto));
				index++;
			}
		}
		
		return codeHtmlMap;
	}
	protected String findQuestionIdFromPaper(int paperId) {
        //从试卷结构表获取题目编码
		List<TestPaperStructure> list=paperStructureService.findQuestionIdsByPaperId(paperId);
		StringBuilder sb=new StringBuilder(20);
		Question question=null;
		for(int i=0,size=list.size();i<size;i++){
            //根据题目编码获取题目id
			question=questionBaseService.getQuestionByCode(list.get(i).getName());
			sb.append(",").append(question.getId());
		}
		if(sb.length()>0){
			return sb.substring(1);
		}
		return sb.toString();
	}
	protected TestPaperDto cacheTestPaperDto(HttpSession session, int paperId){
//		TestPaperDto dto=K12CacheUtil.getCache(Constant.CACHE_PAPER_PREFIX+session.getId(), TestPaperDto.class);
//		if(dto!=null){
//			return dto;
//		}
		TestPaperDto dto=testPaperService.findTestPaperDtoById(paperId);
//		CacheTools.addCache(Constant.CACHE_PAPER_PREFIX + session.getId(), dto);
		return dto;
	}

	public TestPaperService getTestPaperService() {
		return testPaperService;
	}
	public void setTestPaperService(TestPaperService testPaperService) {
		this.testPaperService = testPaperService;
	}
	public TestPaperStructureService getPaperStructureService() {
		return paperStructureService;
	}
	public void setPaperStructureService(
			TestPaperStructureService paperStructureService) {
		this.paperStructureService = paperStructureService;
	}
	public QuestionBaseService getQuestionBaseService() {
		return questionBaseService;
	}
	public void setQuestionBaseService(QuestionBaseService questionBaseService) {
		this.questionBaseService = questionBaseService;
	}

    public QuestionService getQuestionService() {
        return questionService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
}
