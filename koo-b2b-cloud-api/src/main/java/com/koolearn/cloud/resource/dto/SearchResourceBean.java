package com.koolearn.cloud.resource.dto;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.operation.entity.OperationUser;

import java.io.Serializable;


public class SearchResourceBean extends FormBeanBase implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	/**
	 * 父： subjectid gradeid
	 */
	private Integer resourceId;
	private String fileConverPath;
	private String filePath;
	private String leftType;// 过滤知识点or进度点 knowledge schedule
	private Integer leftTreeNodeId;// 知识点or进度点id
	private Integer formatQuery;//资源预览时传递forma查询条件
	private Integer marrow;// 资源格式
	private Integer uploadUser;// 上传人
	private String resourceSearchText;// 搜索关键字
	private String listSource;//resource 查资源库  myupload 查我的上传
	private String  frombeike;//从备课预览
	private String  orderByMarrow;//从备课预览
	private Integer studentPageSize;//家长和学生客户端页面数量


    private Integer subjectId;  // 学科
    private Integer rangeId;     //学段
    private Integer bookVersion; // 教材版本
    private Integer obligatory;  // 必修id
    private Integer tagId;        //树节点id

    private Integer source;//来源
    private Integer type;//类型
    private Integer format;//格式
    private String keyTxt;//搜索内容
    private Integer selectType;//过滤类型

    private Integer klbType;//0：教材版本，1：知识点
    private Integer pageNo;
    private UserEntity userEntity;//老师学生用户
	private OperationUser operationUser;//运营用户
	private Integer urlType;
	private  String updateBeginTime;//搜索范围：更新开始时间
	private  String updateEndTime;//搜索范围：更新结束时间

	public Integer getUrlType() {
		return urlType;
	}

	public void setUrlType(Integer urlType) {
		this.urlType = urlType;
	}

	public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Integer getKlbType() {
        return klbType;
    }

    public void setKlbType(Integer klbType) {
        this.klbType = klbType;
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

    public Integer getObligatory() {
        return obligatory;
    }

    public void setObligatory(Integer obligatory) {
        this.obligatory = obligatory;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getKeyTxt() {
        return keyTxt;
    }

    public void setKeyTxt(String keyTxt) {
        this.keyTxt = keyTxt;
    }

    public Integer getSelectType() {
        return selectType;
    }

    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    public Integer getStudentPageSize() {
		return studentPageSize;
	}

	public void setStudentPageSize(Integer studentPageSize) {
		this.studentPageSize = studentPageSize;
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

	public String getLeftType() {
		return leftType;
	}

	public void setLeftType(String leftType) {
		this.leftType = leftType;
	}

	public Integer getLeftTreeNodeId() {
		return leftTreeNodeId;
	}

	public void setLeftTreeNodeId(Integer leftTreeNodeId) {
		this.leftTreeNodeId = leftTreeNodeId;
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

	public Integer getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(Integer uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getResourceSearchText() {
		return resourceSearchText;
	}

	public void setResourceSearchText(String resourceSearchText) {
		this.resourceSearchText = resourceSearchText;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public String getFileConverPath() {
		return fileConverPath;
	}

	public void setFileConverPath(String fileConverPath) {
		this.fileConverPath = fileConverPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getListSource() {
		return listSource;
	}

	public void setListSource(String listSource) {
		this.listSource = listSource;
	}

	public Integer getFormatQuery() {
		return formatQuery;
	}

	public void setFormatQuery(Integer formatQuery) {
		this.formatQuery = formatQuery;
	}

	public String getFrombeike() {
		return frombeike;
	}

	public void setFrombeike(String frombeike) {
		this.frombeike = frombeike;
	}

	public String getOrderByMarrow() {
		return orderByMarrow;
	}

	public void setOrderByMarrow(String orderByMarrow) {
		this.orderByMarrow = orderByMarrow;
	}

	public String getUpdateEndTime() {
		return updateEndTime;
	}

	public void setUpdateEndTime(String updateEndTime) {
		this.updateEndTime = updateEndTime;
	}

	public String getUpdateBeginTime() {
		return updateBeginTime;
	}

	public void setUpdateBeginTime(String updateBeginTime) {
		this.updateBeginTime = updateBeginTime;
	}

	public OperationUser getOperationUser() {
		return operationUser;
	}

	public void setOperationUser(OperationUser operationUser) {
		this.operationUser = operationUser;
	}
}
