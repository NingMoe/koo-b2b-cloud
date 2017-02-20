package com.koolearn.cloud.resource.dto;

import java.io.Serializable;

public class FormBeanBase implements Serializable {
	private String name;
	private String description;
	private Integer subjectid;
	private Integer gradeid;
	private String searchText;
	private Integer classVideo;
	/**一级节点id*/
	private Integer fistNodeId;
	/**true：查询该节点下子节点   
	 * false：查询学段下一级子节点
	 * NOTF  关键字搜索*/
	private String searchFirstChild;

    private String currentId;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSubjectid() {
		return subjectid;
	}
	public void setSubjectid(Integer subjectid) {
		this.subjectid = subjectid;
	}
	public Integer getGradeid() {
		return gradeid;
	}
	public void setGradeid(Integer gradeid) {
		this.gradeid = gradeid;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Integer getFistNodeId() {
		return fistNodeId;
	}
	public void setFistNodeId(Integer fistNodeId) {
		this.fistNodeId = fistNodeId;
	}
	public String getSearchFirstChild() {
		return searchFirstChild;
	}
	public void setSearchFirstChild(String searchFirstChild) {
		this.searchFirstChild = searchFirstChild;
	}
	public Integer getClassVideo() {
		return classVideo;
	}
	public void setClassVideo(Integer classVideo) {
		this.classVideo = classVideo;
	}

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }
}
