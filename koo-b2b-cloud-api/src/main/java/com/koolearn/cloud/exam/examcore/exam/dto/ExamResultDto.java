package com.koolearn.cloud.exam.examcore.exam.dto;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ExamResultDto implements Serializable{
	private TpExamResult examResult;
	private List<ExamResultDetailDto> details=new ArrayList<ExamResultDetailDto>();
	public TpExamResult getExamResult() {
		return examResult;
	}
	public void setExamResult(TpExamResult examResult) {
		this.examResult = examResult;
	}
	public List<ExamResultDetailDto> getDetails() {
		return details;
	}
	public void setDetails(List<ExamResultDetailDto> details) {
		this.details = details;
	}
	
}
