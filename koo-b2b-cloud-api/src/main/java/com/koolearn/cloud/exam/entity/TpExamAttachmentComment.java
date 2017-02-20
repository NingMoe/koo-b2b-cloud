package com.koolearn.cloud.exam.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tp_exam_attachment_comment")
public class TpExamAttachmentComment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /**
     * 课堂附件关联id
     */
    @Column(name = "exam_attachment_id")
    private Integer examAttachmentId;
    /**
     * 提交评论用户id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 提交评论用户name
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 回复用户id
     */
    @Column(name = "reply_user_id")
    private Integer replyUserId;
    /**
     * 回复用户name
     */
    @Column(name = "reply_user_name")
    private String replyUserName;
    /**
     * 默认0: 学生    1  老师
     */
    @Column(name = "user_type")
    private Integer userType;
    /**
     * 评论内容
     */
    @Column(name = "comment")
    private String comment;
    /**
     * 评论提交时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 最新评论时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 被回复记录ID
     */
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     * 回复集合
     */
    @Transient
    private List<TpExamAttachmentComment> lists;

    @Transient
    private String createTimeStr;

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public List<TpExamAttachmentComment> getLists() {
        return lists;
    }

    public void setLists(List<TpExamAttachmentComment> lists) {
        this.lists = lists;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExamAttachmentId() {
        return examAttachmentId;
    }

    public void setExamAttachmentId(Integer examAttachmentId) {
        this.examAttachmentId = examAttachmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
