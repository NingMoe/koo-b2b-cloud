package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.entity.PaperTemplate;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperTemplateStructure;
import com.koolearn.cloud.exam.examcore.paper.entity.RelaTemplateType;

public class PaperTemplateDto implements Serializable {
	private PaperTemplate paperTemplate;
//	private PaperTemplateStructure paperTemplateStructure;
	private List<PaperTemplateStructureDto> templateStructures=new ArrayList<PaperTemplateStructureDto>();
	private RelaTemplateType templateType;
	public PaperTemplate getPaperTemplate() {
		return paperTemplate;
	}
	public void setPaperTemplate(PaperTemplate paperTemplate) {
		this.paperTemplate = paperTemplate;
	}
	public List<PaperTemplateStructureDto> getTemplateStructures() {
		return templateStructures;
	}
	public void setTemplateStructures(
			List<PaperTemplateStructureDto> templateStructures) {
		this.templateStructures = templateStructures;
	}
	public RelaTemplateType getTemplateType() {
		return templateType;
	}
	public void setTemplateType(RelaTemplateType templateType) {
		this.templateType = templateType;
	}
	
	
}
