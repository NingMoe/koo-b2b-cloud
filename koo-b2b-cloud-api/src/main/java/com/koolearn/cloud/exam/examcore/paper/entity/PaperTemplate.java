package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;
import java.util.Date;


@Entity
@Table(name = "")
public class PaperTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 编号 */
	@Column(name = "code")
	private String code;
	/** 名称 */
	@Column(name = "name")
	private String name;
	/** 描述 */
	@Column(name = "descript")
	private String descript;
	/** 创建人 */
	@Column(name = "creator")
	private int creator;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 最后修改人 */
	@Column(name = "last_editor")
	private int lastEditor;
	/** 最后修改时间 */
	@Column(name = "last_update_time")
	private Date lastUpdateTime;
	/**
	 * 模板类型,默认为0无特殊功能,主要是雅思,托福之类的有些扩展信息,放入其他表
	 */
	@Column(name = "template_type")
	private int templateType;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public int getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(int lastEditor) {
		this.lastEditor = lastEditor;
	}
	
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}
	
}
