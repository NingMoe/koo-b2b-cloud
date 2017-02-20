package com.koolearn.cloud.exam.examcore.question.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "te_tag_object")
public class TagObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /** 标签ID */
    @Column(name = "tag_id")
    private Integer tagId;
    /** 标签名称 */
    @Column(name = "tag_name")
    private String tagName;
    /** 对象ID */
    @Column(name = "object_id")
    private Integer objectId;
    /** 对象类型（1：试卷，2：试题、3：考试/练习） */
    @Column(name = "object_type")
    private Integer objectType;
    /** 标签父ID集合（父级知识点搜索） */
    @Column(name = "full_path")
    private String fullPath;
    /** 创建人 */
    @Column(name = "create_by")
    private String createBy;
    /** 创建时间 */
    @Column(name = "create_time")
    private Date createTime;
    /** 对象编码 */
    @Column(name = "object_code")
    private String objectCode;
    /** 对象最新版本的ID */
    @Column(name = "object_nid")
    private Integer objectNid;
    @Column(name = "tag_type")
    private Integer tagType;
    @Column(name = "status")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public Integer getObjectNid() {
        return objectNid;
    }

    public void setObjectNid(Integer objectNid) {
        this.objectNid = objectNid;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }
}
