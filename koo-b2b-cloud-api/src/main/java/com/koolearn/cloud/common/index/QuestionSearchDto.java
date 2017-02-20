package com.koolearn.cloud.common.index;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.rest.dto.BaseDTO;

import java.util.Date;
import java.util.List;

/**
 * 
 * @classname: QuestionSearchDto
 * @description: 索引题对象
 * @author yangzhenye
 * @date 2014-5-14 上午10:24:24
 */
public class QuestionSearchDto extends BaseDTO {

    private static final long serialVersionUID = 1L;

    // 试题id
    private Integer id;

    // 学科
    private Integer subjectId;

    // 多个科目
    private List<Integer> subjectIdList;

    // 学习阶段
    private Integer gradeId;

    // 题型
    private Integer typeId;

    // 多题型 （选择完形，填空完形）
    private List<Integer> typeIdList;

    // 数据难易程度
    private Integer level;

    private Integer status;

    // 创建者
    private Integer creatorId;

    private Date createTime;

    // 知识点id，多个使用 空格分开
    private Integer knowledgeTags;

    @Transient
    private Integer[] knowledgeTagIds;

    // 试题信息描述 包括（提干，答案，解析，材料等）
    private String description;

    // 扩展
    private String questionCode;
    private Integer newVersion;
    private Integer extendId1;
    private String extendDesc1;
    private Integer extendId2;
    private String extendDesc2;
    /**更新标记0插入，1更新*/
    private Integer insertUpdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getKnowledgeTags() {
        return knowledgeTags;
    }

    public void setKnowledgeTags(Integer knowledgeTags) {
        this.knowledgeTags = knowledgeTags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExtendId1() {
        return extendId1;
    }

    public void setExtendId1(Integer extendId1) {
        this.extendId1 = extendId1;
    }

    public String getExtendDesc1() {
        return extendDesc1;
    }

    public void setExtendDesc1(String extendDesc1) {
        this.extendDesc1 = extendDesc1;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer[] getKnowledgeTagIds() {
        return knowledgeTagIds;
    }

    public void setKnowledgeTagIds(Integer[] knowledgeTagIds) {
        this.knowledgeTagIds = knowledgeTagIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<Integer> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }

    public List<Integer> getTypeIdList() {
        return typeIdList;
    }

    public void setTypeIdList(List<Integer> typeIdList) {
        this.typeIdList = typeIdList;
    }

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public Integer getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(Integer newVersion) {
		this.newVersion = newVersion;
	}

	public Integer getExtendId2() {
		return extendId2;
	}

	public void setExtendId2(Integer extendId2) {
		this.extendId2 = extendId2;
	}

	public String getExtendDesc2() {
		return extendDesc2;
	}

	public void setExtendDesc2(String extendDesc2) {
		this.extendDesc2 = extendDesc2;
	}

    
    public Integer getInsertUpdate() {
        return insertUpdate;
    }

    
    public void setInsertUpdate(Integer insertUpdate) {
        this.insertUpdate = insertUpdate;
    }

}
