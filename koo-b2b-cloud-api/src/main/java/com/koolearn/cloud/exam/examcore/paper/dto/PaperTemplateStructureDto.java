package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.entity.PaperTemplateStructure;
import com.koolearn.cloud.exam.examcore.paper.entity.RelaTemplateType;

public class PaperTemplateStructureDto implements Serializable {
private PaperTemplateStructure paperTemplateStructure;
private RelaTemplateType templateType;
private List<PaperTemplateStructureDto> children=new ArrayList<PaperTemplateStructureDto>();

/**
 * serial number 顺序号
 * eg.1-10
 */
private String sn;
public String getSn() {
	return sn;
}
public void setSn(String sn) {
	this.sn = sn;
}
public PaperTemplateStructure getPaperTemplateStructure() {
	return paperTemplateStructure;
}
public void setPaperTemplateStructure(
		PaperTemplateStructure paperTemplateStructure) {
	this.paperTemplateStructure = paperTemplateStructure;
}
public List<PaperTemplateStructureDto> getChildren() {
	return children;
}
public void setChildren(List<PaperTemplateStructureDto> children) {
	this.children = children;
}
public RelaTemplateType getTemplateType() {
	return templateType;
}
public void setTemplateType(RelaTemplateType templateType) {
	this.templateType = templateType;
}


}
