package com.koolearn.cloud.exam.examspread.exam.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examspread.exam.entity.ExamResultSearch;



public class ExamResultSearchDto implements Serializable {
	private int count;
	private List<ExamResultSearch> list=new ArrayList<ExamResultSearch>();
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<ExamResultSearch> getList() {
		return list;
	}
	public void setList(List<ExamResultSearch> list) {
		this.list = list;
	}
	
}
