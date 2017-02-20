package com.koolearn.cloud.operation.entity;

import com.koolearn.cloud.common.entity.Pager;

import java.io.Serializable;
import java.util.List;

public class SchoolFilter extends Pager implements Serializable {

	private static final long serialVersionUID = 6052915944634248391L;
	private  Integer id;//学校id
	private  String name;
	private  String shortname;//学校简称
	private  Integer province;
	private  Integer city;
	private  Integer county;
	private  Integer grade;//搜索学段
	private  List<Integer> grades;//修改学校学段
	private  Integer schoolStatus;
	private List<Integer> locationIdList;//按地区搜索
	private String cancel;//取消屏蔽标示


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getCounty() {
		return county;
	}

	public void setCounty(Integer county) {
		this.county = county;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getSchoolStatus() {
		return schoolStatus;
	}

	public void setSchoolStatus(Integer schoolStatus) {
		this.schoolStatus = schoolStatus;
	}

	public List<Integer> getLocationIdList() {

		return locationIdList;
	}

	public void setLocationIdList(List<Integer> locationIdList) {
		this.locationIdList = locationIdList;
	}

	public List<Integer> getGrades() {
		return grades;
	}

	public void setGrades(List<Integer> grades) {
		this.grades = grades;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
}
