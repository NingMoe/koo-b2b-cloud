package com.koolearn.cloud.exam.examcore.paper.entity;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaperQuestionType implements Serializable {
    private Integer questionType;
    private String questionTypeName;
    private List<IExamQuestionDto>  questionDtoList=new ArrayList<IExamQuestionDto>();

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public List<IExamQuestionDto> getQuestionDtoList() {
        return questionDtoList;
    }

    public void setQuestionDtoList(List<IExamQuestionDto> questionDtoList) {
        this.questionDtoList = questionDtoList;
    }
}

