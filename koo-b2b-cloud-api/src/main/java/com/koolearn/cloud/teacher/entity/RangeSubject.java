package com.koolearn.cloud.teacher.entity;

import com.koolearn.cloud.school.entity.dto.ClassesDto;
import com.koolearn.cloud.school.entity.dto.SubjectDto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by dongfangnan on 2016/3/31.
 */
public class RangeSubject implements Serializable {

    private int subjectId;
    private String subjectName;//学科名称
    private List<Range> rangelist;//学段
    private int rangeId;
    private String rangeName;//学段名称
    private List<SubjectDto> subjectDtoList;//每个学段下所有的学科
    /**
     * 学段下所有的年级
     */
    private List<ClassesDto> classesDtosList;

    public List<SubjectDto> getSubjectDtoList() {
        return subjectDtoList;
    }

    public void setSubjectDtoList(List<SubjectDto> subjectDtoList) {
        this.subjectDtoList = subjectDtoList;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public List<ClassesDto> getClassesDtosList() {
        return classesDtosList;
    }

    public void setClassesDtosList(List<ClassesDto> classesDtosList) {
        this.classesDtosList = classesDtosList;
    }

    public String toString(){
        return "学段id是:"+ getSubjectId() + "学段名称是:" + getSubjectName();
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<Range> getRangelist() {
        return rangelist;
    }

    public void setRangelist(List<Range> rangelist) {
        this.rangelist = rangelist;
    }
}
