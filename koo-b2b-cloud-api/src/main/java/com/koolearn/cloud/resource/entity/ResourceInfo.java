package com.koolearn.cloud.resource.entity;

import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资源信息表
 *
 * @author 葛海松
 */
@Entity
@Table(name = "yy_resources")
public class ResourceInfo implements Serializable {

    private static final long serialVersionUID = -2434728846894283235L;
    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id; // 编号
    @Column(name = "name")
    private String name; // 名称
    @Column(name = "description")
    private String description;//资源描述
    @Column(name = "type")
    private Integer type;//资源类型  (教案、学案、教学素材、复习讲义、其它)
    @Transient
    private String typeName;
    @Column(name = "format")
    private Integer format;//资源格式（ppt、word、Exce、视频、音频、图片）
    @Transient
    private String formatName;
    @Column(name = "marrow")
    private Integer marrow;//是否精华    2是   1否
    @Column(name = "upload_time")
    private Date uploadTime;//资源上传时间
    @Transient
    private String uploadTimeStr;
    @Column(name = "upload_user_id")
    private Integer uploadUserId;//资源上传人
    @Transient
    private String uploadUserName;
    @Column(name = "storage_size")
    private Long storageSize;//资源存储大小(单位字节: size/1024=K)
    @Transient
    private String storageSizeView;
    @Column(name = "page_size")
    private Integer pageSize;//资源页数
    @Column(name = "time_length")
    private Integer timeLength;//资源时长
    @Column(name = "source")
    private Integer source;//来源(运营1、学校2、教师3、公共4 )
    @Column(name = "status")//转换中0  转换失败1  转换完成(可用)2  资源不可用3
    private Integer status;//状态
    @Column(name = "clsss_video")
    private Integer clsssVideo;//2是 1否为课堂视频(面向学生端推荐热门课程)
    @Column(name = "subject_tag_id")
    private Integer subjectTagId;//学科标签id
    @Column(name = "stage_tag_id")
    private Integer stageTagId;//学习阶段tagid
    @Column(name = "file_old_name")
    private String fileOldName;//原文件名
    @Column(name = "file_path")
    private String filePath;//文件路径
    @Column(name = "file_conver_path")
    private String fileConverPath;//资源文件转换目录
    @Transient
    private boolean reConver;//资源修改时，是否有需要包含重新转换的文档
    @Column(name = "frontcover_url")
    private String frontcoverUrl;//视频封面图片路径
    @Column(name = "opt_user_id")
    private Integer optUserId;//操作人
    @Column(name = "school_id")
    private Integer schoolId;//操作人

    @Column(name = "file_new_name")
    private String fileNewName;//新文件名
    @Column(name = "subject_tag_name")
    private String subjectTagName;//学科标签name
    @Column(name = "stage_tag_name")
    private String stageTagName;//学习阶段tagname
    @Column(name = "extend_name")
    private String extendName;//扩展名
    @Column(name = "document_icon")
    private String documentIcon;//文档icon图片名称

    @Column(name = "opt_time")
    private Date optTime;//操作时间 最后修改时间
    @Transient
    private String optTimeStr;
    @Column(name = "update_time")
    private Date updateTime;//弃用


    @Column(name = "update_user_id")
    private Integer updateUserId;
    /**
     * 1，教材目录，0，知识点, 全部为2
     */
    @Column(name = "klb_type")
    private Integer klbType;
    //知识点、进度点
    @Transient
    private Integer tagId;

    /**
     * 标签父ID集合（父级知识点搜索）
     */
    @Column(name = "tag_full_path")
    private String tagFullPath;
    @Transient
    private String documentIconSmall;//小文档icon图片名称
    @Transient
    private Integer recommendId;//推荐资源列表用
    @Transient
    private Integer recommendFlag;//2 推荐
    @Transient
    private String content;
    @Transient
    private  Integer newStatus;//初始化资源上传修改时保存的状态
    @Transient
    private String newDefaultrontcoverUrl;//初始化资源上传修改时封面
    @Transient
    private boolean callConver;//上传或编辑资源时判断是否需要调用转换服务器
    /**
     * 资源相关知识点 不能为空
     */
    @Transient
    private Integer[] knowledgeTags;
    @Transient
    private String knowledgeNames;//全路径
    @Transient
    private String knowledgeSingleName;//叶子节点路径
    @Transient
    private  List<Tags> konwledgeTagsEntityList;//修改资源展示
    /**
     * 资源相关进度点
     */
    @Transient
    private Integer[] bookVersionIds;
    @Transient
    private String bookVersionNames;//全路径
    @Transient
    private List<Tags> bookVersionTagsEntityList;//修改资源展示
    /**
     * 用户使用次数
     */
    @Column(name="use_times")
    private Integer useTimes;
    /**
     * 用户使用 IDS
     */
    @Transient
    private String userUseIds;
    /**
     * 用户收藏IDS
     */
    @Transient
    private String userCollectionIds;

    /**
     * 收藏id 判断该资源是否被此登录人收藏
     */
    @Transient
    private boolean collection;
    /**
     * 收藏  标注
     */
    @Transient
    private String shortNote;
    /**
     * PDF文档内容
     */
    @Transient
    private String pdfContent;
    @Transient
    private String knowledgeAndSchedulePath;
    /**
     * 用户信息
     */
    @Transient
    private String userStr;
    @Transient
    private boolean error = false;
    @Transient
    private boolean newUpfile=false;//新上传文件，重新计算封面路径

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getUserStr() {
        return userStr;
    }

    public void setUserStr(String userStr) {
        this.userStr = userStr;
    }

    public String getKnowledgeAndSchedulePath() {
        return knowledgeAndSchedulePath;
    }

    public void setKnowledgeAndSchedulePath(String knowledgeAndSchedulePath) {
        this.knowledgeAndSchedulePath = knowledgeAndSchedulePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public Integer getMarrow() {
        return marrow;
    }

    public void setMarrow(Integer marrow) {
        this.marrow = marrow;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadTimeStr() {
        return ParseDate.formatByDate(this.getUploadTime(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMM);
    }

    public void setUploadTimeStr(String uploadTimeStr) {
        this.uploadTimeStr = uploadTimeStr;
    }

    public Integer getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Integer uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public Long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }

    public String getStorageSizeView() {
        return storageSizeView;
    }

    public void setStorageSizeView(String storageSizeView) {
        this.storageSizeView = storageSizeView;
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

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClsssVideo() {
        return clsssVideo;
    }

    public void setClsssVideo(Integer clsssVideo) {
        this.clsssVideo = clsssVideo;
    }

    public Integer getSubjectTagId() {
        return subjectTagId;
    }

    public void setSubjectTagId(Integer subjectTagId) {
        this.subjectTagId = subjectTagId;
    }

    public Integer getStageTagId() {
        return stageTagId;
    }

    public void setStageTagId(Integer stageTagId) {
        this.stageTagId = stageTagId;
    }

    public String getFileOldName() {
        return fileOldName;
    }

    public void setFileOldName(String fileOldName) {
        this.fileOldName = fileOldName;
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

    public boolean isReConver() {
        return reConver;
    }

    public void setReConver(boolean reConver) {
        this.reConver = reConver;
    }

    public String getFrontcoverUrl() {
        return frontcoverUrl;
    }

    public void setFrontcoverUrl(String frontcoverUrl) {
        this.frontcoverUrl = frontcoverUrl;
    }

    public Integer getOptUserId() {
        return optUserId;
    }

    public void setOptUserId(Integer optUserId) {
        this.optUserId = optUserId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public String getOptTimeStr() {
        return ParseDate.formatByDate(this.getOptTime(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMM);
    }

    public void setOptTimeStr(String optTimeStr) {
        this.optTimeStr = optTimeStr;
    }

    public String getFileNewName() {
        return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getSubjectTagName() {
        return subjectTagName;
    }

    public void setSubjectTagName(String subjectTagName) {
        this.subjectTagName = subjectTagName;
    }

    public String getStageTagName() {
        return stageTagName;
    }

    public void setStageTagName(String stageTagName) {
        this.stageTagName = stageTagName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getKlbType() {
        return klbType;
    }

    public void setKlbType(Integer klbType) {
        this.klbType = klbType;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagFullPath() {
        return tagFullPath;
    }

    public void setTagFullPath(String tagFullPath) {
        this.tagFullPath = tagFullPath;
    }

    public String getDocumentIconSmall() {
        return documentIconSmall;
    }

    public void setDocumentIconSmall(String documentIconSmall) {
        this.documentIconSmall = documentIconSmall;
    }

    public Integer getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Integer recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(Integer recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer[] getKnowledgeTags() {
        return knowledgeTags;
    }

    public void setKnowledgeTags(Integer[] knowledgeTags) {
        this.knowledgeTags = knowledgeTags;
    }


    public Integer[] getBookVersionIds() {
        return bookVersionIds;
    }

    public void setBookVersionIds(Integer[] bookVersionIds) {
        this.bookVersionIds = bookVersionIds;
    }

    public String getBookVersionNames() {
        return bookVersionNames;
    }

    public void setBookVersionNames(String bookVersionNames) {
        this.bookVersionNames = bookVersionNames;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public String getKnowledgeNames() {
        return knowledgeNames;
    }

    public void setKnowledgeNames(String knowledgeNames) {
        this.knowledgeNames = knowledgeNames;
    }

    public String getUserUseIds() {
        return userUseIds;
    }

    public void setUserUseIds(String userUseIds) {
        this.userUseIds = userUseIds;
    }

    public String getUserCollectionIds() {
        return userCollectionIds;
    }

    public void setUserCollectionIds(String userCollectionIds) {
        this.userCollectionIds = userCollectionIds;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public String getShortNote() {
        return shortNote;
    }

    public void setShortNote(String shortNote) {
        this.shortNote = shortNote;
    }

    public String getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(String pdfContent) {
        this.pdfContent = pdfContent;
    }

    public String getKnowledgeSingleName() {
        if(StringUtils.isBlank(this.knowledgeNames)) return "";
        String[] knowFull=this.knowledgeNames.split(",");
        List<String> knowlist=new ArrayList<String>();
        for(int i=0;i<knowFull.length;i++){
            if(StringUtils.isNotBlank(knowFull[i])){
                String[] know=knowFull[i].split("/");
                knowlist.add(know[know.length-1]);
            }
        }
        return StringUtils.join(knowlist, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR);
    }

    public void setKnowledgeSingleName(String knowledgeSingleName) {
        this.knowledgeSingleName = knowledgeSingleName;
    }

    public boolean isNewUpfile() {
        return newUpfile;
    }

    public void setNewUpfile(boolean newUpfile) {
        this.newUpfile = newUpfile;
    }

    public Integer getNewStatus() {
        this.newStatus=GlobalConstant.RESOURCE_STATUS_CONVERING;
        if (GlobalConstant.RESOURCE_FORMAT_VIDEO == this.getFormat()
                ||GlobalConstant.RESOURCE_FORMAT_AUDIO == this.getFormat()
                ||GlobalConstant.RESOURCE_FORMAT_IMAGE == this.getFormat()) {
            //不需要转换
            this.newStatus=GlobalConstant.RESOURCE_STATUS_CONVERED;
        }
        return this.newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewDefaultrontcoverUrl() {
        this.newDefaultrontcoverUrl=this.getFrontcoverUrl();
        int format = this.getFormat();
        if( StringUtils.isBlank(this.getFrontcoverUrl())) {
            if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO) {
                this.newDefaultrontcoverUrl="/cloud/image/defaultFrontcover_MP4.jpg";
            } else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
                this.newDefaultrontcoverUrl="/cloud/image/defaultFrontcover_MP3.jpg";
            }   else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
                this.newDefaultrontcoverUrl=this.getFilePath();
            } else {
                this.newDefaultrontcoverUrl=this.getFileConverPath()+"/index_1.png";
            }
        }

        return newDefaultrontcoverUrl;
    }

    public void setNewDefaultrontcoverUrl(String newDefaultrontcoverUrl) {
        this.newDefaultrontcoverUrl = newDefaultrontcoverUrl;
    }

    public boolean isCallConver() {
        this.callConver=false;
        int stat=this.getStatus().intValue();
        if(stat==GlobalConstant.RESOURCE_STATUS_CONVERING||
            stat==GlobalConstant.RESOURCE_STATUS_CONVER_FAIL){
            this.callConver=true;//子上传或编辑资源时，失败或转换中的状态需要调用转换服务
        }
        return callConver;
    }

    public void setCallConver(boolean callConver) {
        this.callConver = callConver;
    }

    public List<Tags> getKonwledgeTagsEntityList() {
        return konwledgeTagsEntityList;
    }

    public void setKonwledgeTagsEntityList(List<Tags> konwledgeTagsEntityList) {
        this.konwledgeTagsEntityList = konwledgeTagsEntityList;
    }

    public List<Tags> getBookVersionTagsEntityList() {
        return bookVersionTagsEntityList;
    }

    public void setBookVersionTagsEntityList(List<Tags> bookVersionTagsEntityList) {
        this.bookVersionTagsEntityList = bookVersionTagsEntityList;
    }
}
