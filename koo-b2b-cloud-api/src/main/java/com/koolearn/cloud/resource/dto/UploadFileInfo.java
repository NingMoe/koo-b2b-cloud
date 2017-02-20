package com.koolearn.cloud.resource.dto;

import java.io.Serializable;

public class UploadFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    //附件属性
    private String filePath;// 文件相对路径路径(包含第三方资源路径)
    private String fileConverPath;// 资源文件转换目录
    private Integer format;//资源格式
    private Long storageSize;//资源存储大小
    private Integer pageSize;//资源页数
    private Integer timeLength;//资源时长
    private String fileOldName;//原文件名
    private String fileNewName;//新文件名
    private String extendName;//扩展名
    private String documentIcon;//文档icon图片名称
    private String documentIconSmall;//文档icon小图片名称（不含扩展名）
    private String tooBigSizeInfo;//附件过大提示信息
    private boolean error;//true 附件过大
    private String frontcoverUrl;// 封面图片路径(资源图片缩略图)
    private Integer status = 0;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileConverPath() {
        return fileConverPath;
    }

    public void setFileConverPath(String fileConverPath) {
        this.fileConverPath = fileConverPath;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
    }

    public String getFileOldName() {
        return fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName;
    }

    public String getFileNewName() {
        return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public String getDocumentIcon() {
        return documentIcon;
    }

    public void setDocumentIcon(String documentIcon) {
        this.documentIcon = documentIcon;
    }

    public String getDocumentIconSmall() {
        return documentIconSmall;
    }

    public void setDocumentIconSmall(String documentIconSmall) {
        this.documentIconSmall = documentIconSmall;
    }

    public String getTooBigSizeInfo() {
        return tooBigSizeInfo;
    }

    public void setTooBigSizeInfo(String tooBigSizeInfo) {
        this.tooBigSizeInfo = tooBigSizeInfo;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getFrontcoverUrl() {
        return frontcoverUrl;
    }

    public void setFrontcoverUrl(String frontcoverUrl) {
        this.frontcoverUrl = frontcoverUrl;
    }

}
