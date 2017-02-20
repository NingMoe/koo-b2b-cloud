package com.koolearn.cloud.task.dto;

import java.io.Serializable;
import java.util.Date;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;

public class TaskClassesName implements Serializable{
	private int examId;
	private String classesName;//一个作业对应的所有班级名
	private String classesfullName;//一个作业对应的所有班级全名

	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

    public String getClassesfullName() {
        return classesfullName;
    }

    public void setClassesfullName(String classesfullName) {
        this.classesfullName = classesfullName;
    }
}
