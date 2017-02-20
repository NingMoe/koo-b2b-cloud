package com.koolearn.cloud.exam.examcore.paper.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;

/***老师: 我的试卷库和新东方试卷关联表*/
@Entity
@Table(name = "te_paper_teacher")
public class TestPaperTeacher implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
    @Column(name = "paper_id")
    private Integer paperId;//新东方试卷id
    @Column(name = "teacher_id")
    private Integer teacherId;//新东方试卷id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}
