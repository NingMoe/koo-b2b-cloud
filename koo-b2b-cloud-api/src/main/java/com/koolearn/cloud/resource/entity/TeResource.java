package com.koolearn.cloud.resource.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "te_resource")
public class TeResource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	/** 资源类型 （微课、教案、学案、教学素材、复习讲义、其它） */
	@Column(name = "type")
	private Integer type;
	/** 资源格式（ppt、word、Exce、视频、音频、图片） */
	@Column(name = "format")
	private Integer format;
	/** 是否精华 2是   1否 */
	@Column(name = "marrow")
	private Integer marrow;
	/** 资源上传人 */
	@Column(name = "upload_user_id")
	private Integer uploadUserId;
	/** 来源(运营、学校、个人、第三方资源 ) */
	@Column(name = "source")
	private Integer source;
	/** 状态 转换 */
	@Column(name = "status")
	private Integer status;
	@Column(name = "file_new_name")
	private String fileNewName;
	@Column(name = "file_old_name")
	private String fileOldName;
	/** 文件扩展名 */
	@Column(name = "extend_name")
	private String extendName;
	/** 文件相对路径路径(包含第三方资源路径) */
	@Column(name = "file_path")
	private String filePath;
	/** 资源文档转换目录 */
	@Column(name = "file_conver_path")
	private String fileConverPath;
	/** 视频封面图片路径 */
	@Column(name = "frontcover_url")
	private String frontcoverUrl;
	/** 资源页数 */
	@Column(name = "page_size")
	private Integer pageSize;
	/** 资源时长 */
	@Column(name = "time_length")
	private Integer timeLength;
	/** 资源上传时间 */
	@Column(name = "upload_time")
	private Date uploadTime;
	/** 修改时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** 0私人资源  1 上传到校资源库    2 上传到公共资源库 */
	@Column(name = "share_type")
	private Integer shareType;
    /**
     * 标签父ID集合（父级知识点搜索）
     */
    @Column(name = "full_path")
    private String fullPath;
    /**
     * 资源大小
     */
    @Column(name = "storage_size")
    private Long storageSize;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
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
	
	public Integer getFormat() {
		return format;
	}

	public void setFormat(Integer format) {
		this.format = format;
	}
	
	public Integer getMarrow() {
		return marrow;
	}

	public void setMarrow(Integer marrow) {
		this.marrow = marrow;
	}
	
	public Integer getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
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
	
	public String getFileNewName() {
		return fileNewName;
	}

	public void setFileNewName(String fileNewName) {
		this.fileNewName = fileNewName;
	}
	
	public String getFileOldName() {
		return fileOldName;
	}

	public void setFileOldName(String fileOldName) {
		this.fileOldName = fileOldName;
	}
	
	public String getExtendName() {
		return extendName;
	}

	public void setExtendName(String extendName) {
		this.extendName = extendName;
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
	
	public String getFrontcoverUrl() {
		return frontcoverUrl;
	}

	public void setFrontcoverUrl(String frontcoverUrl) {
		this.frontcoverUrl = frontcoverUrl;
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
	
	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}
}
