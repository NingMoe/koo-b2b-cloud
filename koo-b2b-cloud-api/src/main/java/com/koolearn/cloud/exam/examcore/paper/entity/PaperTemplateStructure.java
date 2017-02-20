package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;


@Entity
@Table(name = "")
public class PaperTemplateStructure implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 模板ID */
	@Column(name = "template")
	private int template;
	/** 上层结构ID */
	@Column(name = "parent")
	private int parent;
	@Column(name = "name")
	private String name;
	/** 题目数量限制 ,0 表示没有做限制
	 * 存在2中情况
	 * eg.
	 *  10
	 *  10-16
	 * */
	@Column(name = "question_count")
	private String questionCount;
	/** 题目类型限制,主客观 主观0 客观1*/
	@Column(name = "question_type")
	private int questionType;
	/** 分值 */
	@Column(name = "points")
	private Double points;
	/** 限时,单位 分钟 , 作为描述 有2种情况
	 *  eg.
	 *  30
	 *  30-50
	 * */
	@Column(name = "timeout")
	private String timeout;
	/** 描述 */
	@Column(name = "descript")
	private String descript;
	/** 排序 */
	@Column(name = "odr")
	private int odr;
	
	/**
	 * 业务扩展类型,默认0,不影响其他操作
	 * 雅思需要设置
	 */
	@Column(name = "biz_type")
	private int bizType;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
	}
	
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(String questionCount) {
		this.questionCount = questionCount;
	}
	
	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public int getOdr() {
		return odr;
	}

	public void setOdr(int odr) {
		this.odr = odr;
	}


	public int getBizType() {
		return bizType;
	}

	public void setBizType(int bizType) {
		this.bizType = bizType;
	}
	
}
