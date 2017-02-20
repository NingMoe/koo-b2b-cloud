package com.koolearn.cloud.common.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fn on 2016/5/16.
 */
@Entity
@Table(name = "classes_dynamic_teacher")
public class ClassesDynamicTeacher implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int STATUS_ZERO = 0;
    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /** 班级动态id */
    @Column(name = "classes_dynamic_id")
    private Integer classesDynamicId;
    /**老师主键*/
    @Column(name="teacher_id")
    private Integer teacherId;
    /**0为未读，1为已读*/
    @Column(name="status")
    private int status;
    @Column(name="update_time")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassesDynamicId() {
        return classesDynamicId;
    }

    public void setClassesDynamicId(Integer classesDynamicId) {
        this.classesDynamicId = classesDynamicId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
