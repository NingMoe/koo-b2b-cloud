package com.koolearn.cloud.exam.examcore.paper.service;

import com.koolearn.cloud.exam.examcore.paper.entity.RelaTemplateType;

public interface TemplateTypeService {

	RelaTemplateType findItemByPaperId(int paperId);
	/**
	 * 根据标签返回试卷模板
	 * @param tagId
	 * @return
	 */
	RelaTemplateType findItemByTagId(int tagId);

}
