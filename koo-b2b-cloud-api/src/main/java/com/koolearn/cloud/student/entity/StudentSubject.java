package com.koolearn.cloud.student.entity;

import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.exam.entity.TpExam;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by fn on 2016/5/24.
 */
public class StudentSubject implements Serializable , Comparator<Object>{
    private static final long serialVersionUID = 1L;
    /**
     * 学科id
     */
    private Integer subjectId;
    /**
     * 学科名称
     */
    private String subjectName;
    /**
     * 学段id
     */
    private Integer rangeId;
    /**
     * 是否显示组题自测按钮
     */
    private int showButton;
    /**
     * 任课老师
     */
    private Set< String > teacherSet;

    /**
     * 每个学科对应的所有班级
     */
    private Set< String > classesSet;
    /**
     * 每个学科的所有作业，课堂
     */
    private List< TpExam > tpExamList;

    private long createDate;

    public int getShowButton() {
        return showButton;
    }

    public void setShowButton(int showButton) {
        this.showButton = showButton;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Set<String> getClassesSet() {
        return classesSet;
    }

    public void setClassesSet(Set<String> classesSet) {
        this.classesSet = classesSet;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Set<String> getTeacherSet() {
        return teacherSet;
    }

    public void setTeacherSet(Set<String> teacherSet) {
        this.teacherSet = teacherSet;
    }

    public List<TpExam> getTpExamList() {
        return tpExamList;
    }

    public void setTpExamList(List<TpExam> tpExamList) {
        this.tpExamList = tpExamList;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if(   o1 !=null && o2 != null  ){
            StudentSubject studentSubject1 = (StudentSubject )o1;
            StudentSubject studentSubject2 = (StudentSubject )o2;
            long date1 = studentSubject1.getCreateDate();
            long date2 = studentSubject2.getCreateDate();

            if( 0 != date1 && 0 != date2 ){
                if( date1 > date2 ) {
                    return -1;
                }else if( date1 == date2 ) {
                    return 0;
                }else{
                    return 1;
                }
            } else{
                return 1;
            }
        }else{
            return 1;
        }
    }
}
