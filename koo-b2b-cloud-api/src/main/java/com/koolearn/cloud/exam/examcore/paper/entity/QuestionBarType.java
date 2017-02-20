package com.koolearn.cloud.exam.examcore.paper.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionBarType implements Serializable {
    private String name ;//属性题型标签name
    private Integer type ;//属性题型标签id
    private Double defaultScore;//题型默认小题分值
    private List<Integer> examIdArr=new ArrayList<Integer>();//已加入的试题id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getExamIdArr() {
        return examIdArr;
    }

    public void setExamIdArr(List<Integer> examIdArr) {
        this.examIdArr = examIdArr;
    }

    public Double getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(Double defaultScore) {
        this.defaultScore = defaultScore;
    }
}

 