package com.koolearn.cloud.exam.examspread.question.dto;

import java.io.Serializable;
import java.util.Date;

import com.koolearn.cloud.util.ParseDate;


/**
 * 接口服务返回，题目信息DTO
 * @author yangzhenye
 */
public class IQuestionDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;//试题id
	private Integer questionTypeId;//试题类型id
	private Integer tag2;//试题标签二级
	private Integer tag3;//试题标签三级
	private String code;//试题编码
	private String topic;//试题题干
	private Date createDate;//创建时间
	private String createDateStr;
	private String createByName;//创建人
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(Integer questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getCreateDateStr() {
		return ParseDate.formatByDate(this.createDate, ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public Integer getTag2() {
		return tag2;
	}
	public void setTag2(Integer tag2) {
		this.tag2 = tag2;
	}
	public Integer getTag3() {
		return tag3;
	}
	public void setTag3(Integer tag3) {
		this.tag3 = tag3;
	}
}
