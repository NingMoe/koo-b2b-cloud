package com.koolearn.cloud.dictionary.entity ;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;


@Entity
@Table(name = "dictionary")
public class Dictionary implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS = 1;
	public static final int TYPE_SUBJECT = 1;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;  //返回value值
	@Column(name = "name")
	private String name;
	@Column(name = "value")
	private Integer value;
    @Column(name = "question_type")
    private Integer questionType;
	/** 1.学科 2.地区 3.年份 4.试卷类型 */
	@Column(name = "type")
	private Integer type;
	/** 1:有效  0无效 */
	@Column(name = "status")
	private Integer status;
	@Column(name = "notes")
	private String notes;
    @Column(name = "sort")
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    /** 1.有新作业未查看  2.已查看所有新作业 */
	@Transient
	private Integer tagStatus;
	
	public Integer getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(Integer tagStatus) {
		this.tagStatus = tagStatus;
	}

	public Integer getId() {
		return this.getValue();
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }
}
