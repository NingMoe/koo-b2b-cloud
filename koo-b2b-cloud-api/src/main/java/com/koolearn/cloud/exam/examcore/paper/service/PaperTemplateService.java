package com.koolearn.cloud.exam.examcore.paper.service;

import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperTemplateDto;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperTemplate;
import com.koolearn.cloud.exam.examcore.paper.entity.RelaTemplateType;

/**
 * 模板接口
 * @author wangpeng
 *
 */
public interface PaperTemplateService {
	/**
	 * 导入模板的保存操作
	 * @param dto
	 * @param relaTemplateType 
	 */
	void saveTemplate4Import(PaperTemplateDto dto, RelaTemplateType relaTemplateType);

	/**获取模板实体
	 * @param templateId
	 * @return
	 */
	PaperTemplate findItemById(int templateId);

	/**
	 * * @Description: TODO(模版数据导入前要清除老数据) 
	   *      
	   * @return void    
	   * @author: 葛海松
	   * @time:    2015年5月19日 下午3:54:57 
	   * @throws
	 */
	void clearTemplate();
}
