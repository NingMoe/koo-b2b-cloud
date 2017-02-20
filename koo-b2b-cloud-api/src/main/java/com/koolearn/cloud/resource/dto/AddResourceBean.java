package com.koolearn.cloud.resource.dto;


import java.io.Serializable;

public class AddResourceBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private  Integer resourceId;
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
    private String documentIcon;//文档icon图片名称（不含扩展名）
    private String documentIconSmall;//文档icon小图片名称
    private String frontcoverUrl;// 封面图片路径(资源图片缩略图)

    private Integer subjectId;  // 学科id
    private Integer rangeId;     //学段id
    private Integer bookVersion; // 版本id
    private Integer obligatoryId;  // 教材id
    private Integer type; //资源类型
    private Integer shareType; //私密、同时上传到本校、同时上传到公共
    private Integer marrow;//是否精华

    private Integer[] knowlgedIds;//知识点树id
    private Integer[] bookVersionIds;//进度点树id
    private String description;//资源描述
    private String name;//资源标题，空取资源文件名


    private boolean error = false;
    private String tooBigSizeInfo;


    public void setKnowlgedIds(Integer[] knowlgedIds) {
        this.knowlgedIds = knowlgedIds;
    }

    public Integer[] getBookVersionIds() {
        return bookVersionIds;
    }

    public Integer[] getKnowlgedIds() {
        return knowlgedIds;
    }

    public void setBookVersionIds(Integer[] bookVersionIds) {
        this.bookVersionIds = bookVersionIds;
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

    public String getDocumentIconSmall() {
        return documentIconSmall;
    }

    public void setDocumentIconSmall(String documentIconSmall) {
        this.documentIconSmall = documentIconSmall;
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

    public String getFrontcoverUrl() {
        return frontcoverUrl;
    }

    public void setFrontcoverUrl(String frontcoverUrl) {
        this.frontcoverUrl = frontcoverUrl;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Integer getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(Integer bookVersion) {
        this.bookVersion = bookVersion;
    }

    public Integer getObligatoryId() {
        return obligatoryId;
    }

    public void setObligatoryId(Integer obligatoryId) {
        this.obligatoryId = obligatoryId;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getMarrow() {
        return marrow;
    }

    public void setMarrow(Integer marrow) {
        this.marrow = marrow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

}
