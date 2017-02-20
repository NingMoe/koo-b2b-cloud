package com.koolearn.cloud.classRoom.dto;

import java.io.Serializable;

/**
 * Created by xin on 16/4/22.
 */
public class ClassRoomAttachment implements Serializable{
    private Integer attachmentId;
    private String attachmentName;
    private Integer attachmentType;
    private Integer attachmentOrder;

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Integer getAttachmentOrder() {
        return attachmentOrder;
    }

    public void setAttachmentOrder(Integer attachmentOrder) {
        this.attachmentOrder = attachmentOrder;
    }
}
