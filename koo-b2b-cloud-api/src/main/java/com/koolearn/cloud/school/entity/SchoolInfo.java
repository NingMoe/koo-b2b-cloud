package com.koolearn.cloud.school.entity;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.school.entity.dto.ClassesDto;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.cloud.teacher.entity.RangeSubject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/10/31.
 */
public class SchoolInfo implements Serializable{

    /**
     * 学校的学段集合
     */
    private List<RangeSubject> rangeList;

    /**
     * 学校的班级集合
     */
    private List<Classes > classesList;
    /**
     * 存放所有学科
     */
    private List<SubjectDto> subjectDtoList;
    private Integer status;

    /**
     * 记录学校下所有年级
     * @return
     */
    private Map<String , Object > levelMap;
    /**
     * 记录学校下所有学科
     * @return
     */
    private Map<Integer , Object > subjectMap;

    public Map<String, Object> getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(Map<String, Object> levelMap) {
        this.levelMap = levelMap;
    }

    public Map<Integer, Object> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<Integer, Object> subjectMap) {
        this.subjectMap = subjectMap;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
// private Map< Integer ,  List<ClassesDto> > classesMap;


    public List<SubjectDto> getSubjectDtoList() {
        return subjectDtoList;
    }

    public void setSubjectDtoList(List<SubjectDto> subjectDtoList) {
        this.subjectDtoList = subjectDtoList;
    }

    public List<RangeSubject> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<RangeSubject> rangeList) {
        this.rangeList = rangeList;
    }

    public List<Classes> getClassesList() {
        return classesList;
    }

    public void setClassesList(List<Classes> classesList) {
        this.classesList = classesList;
    }
}
