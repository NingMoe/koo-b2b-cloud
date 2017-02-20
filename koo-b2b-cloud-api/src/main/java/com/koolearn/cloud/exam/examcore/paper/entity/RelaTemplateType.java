package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "")
public class RelaTemplateType implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 试卷模板ID */
	@Column(name = "template_id")
	private int templateId;
	/** 分类ID */
	@Column(name = "tag_id")
	private int tagId;
	/** 分类名称 */
	@Column(name = "tag_name")
	private String tagName;
	/** 标签全路径 */
	@Column(name = "tag_full_path")
	private String tagFullPath;
	
//	/**
//	 * 模板结构id
//	 */
//	@Column(name = "structure_id")
//	private int structureId;
//	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagFullPath() {
		return tagFullPath;
	}

	public void setTagFullPath(String tagFullPath) {
		this.tagFullPath = tagFullPath;
	}

//	public int getStructureId() {
//		return structureId;
//	}
//
//	public void setStructureId(int structureId) {
//		this.structureId = structureId;
//	}
	
}
