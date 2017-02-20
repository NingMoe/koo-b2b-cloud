package com.koolearn.cloud.exam.examcore.question.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.task.dto.QuestionErrUser;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.klb.tags.entity.Tags;

/**
 * 试题基类
 * 
 * @author wangpeng
 * @date Oct 24, 2012 技术教辅社区组@koolearn.com
 */
@Entity
@Table(name = "te_question")
public class Question extends BaseEntity {

	// 待审核
	@Transient
	public static final int QUESTION_STATUS_UNAUDIT = 0;

	// 1 已审核
	@Transient
	public static final int QUESTION_STATUS_AUDIT = 1;

	// 2 停用
	@Transient
	public static final int QUESTION_STATUS_NOUSE = 2;
	
	
//	 2 正确答案
	@Transient
	public static final int QUESTION_ANSWER_IS_RIGHT = 1;

//	 2 错误答案
	@Transient
	public static final int QUESTION_ANSWER_NOT_RIGHT = 0;

	
	// 普通单选
	@Transient
	public static final int QUESTION_TYPE_DANXUAN = 6;

	// 普通多选
	@Transient
	public static final int QUESTION_TYPE_DUOXUAN = 1;

	//  判断题judge
	@Transient
	public static final int QUESTION_TYPE_JUDGE = 35;
	// 填空题
	@Transient
	public static final int QUESTION_TYPE_FILL_BLANK = 2;
	
	// 普通填空题
	@Transient
	public static final int QUESTION_TYPE_NORMAL_FILL_BLANK = 39;
	
	// 计算题（填空）
	@Transient
	public static final int QUESTION_TYPE_FILL_CALCULATION = 16;
	//	 排序题
	@Transient
	public static final int QUESTION_TYPE_SORT = 13;
	//选择型完形填空题
	@Transient
	public static final int QUESTION_TYPE_CHOICE_FILL_BLANK = 15;
	
	//填空型完形填空题
	@Transient
	public static final int QUESTION_TYPE_CLOZE_FILL_BLANK = 8;
	
	//复合听写
	@Transient
	public static final int QUESTION_TYPE_COMPOSITE_DICTATION = 9;
	//单选阴影
	@Transient
	public static final int QUESTION_TYPE_DANXUAN_SHADE = 20;
	//单选方框
	@Transient
	public static final int QUESTION_TYPE_DANXUAN_BOX = 21;
	//单选点句
	@Transient
	public static final int QUESTION_TYPE_DANXUAN_POINT = 14;
	//单选段落 跟随题 箭头
	@Transient
	public static final int QUESTION_TYPE_DANXUAN_GRAPH = 22;
	//多选阴影
	@Transient
	public static final int QUESTION_TYPE_DUOXUAN_SHADE = 17;
	//多选段落 跟随题 箭头
	@Transient
	public static final int QUESTION_TYPE_DUOXUAN_GRAPH = 23;
	//简答题
	@Transient
	public static final int QUESTION_TYPE_SHORT = 3;
	//写作题
	@Transient
	public static final int QUESTION_TYPE_WHRITE = 4;
	//口语题
	@Transient
	public static final int QUESTION_TYPE_SPOKEN = 18;
//	//拖拽题
//	@Transient
//	public static final int QUESTION_TYPE_DRAG = 11;
	//表格题
	@Transient
	public static final int QUESTION_TYPE_TABLE = 12;
	//阅读理解题
	@Transient
	public static final int QUESTION_TYPE_READ = 7;
	//听力题
	@Transient
	public static final int QUESTION_TYPE_LISTEN= 19;
	
	//选择填空题
	@Transient
	public static final int QUESTION_TYPE_CHOICE_BLANK= 36;
	
	//选词填空
	@Transient
	public static final int QUESTION_TYPE_CHOICE_WORD= 37;
	
	//口语训练
	@Transient
	public static final int QUESTION_TYPE_ORAL_TRAINING = 38;
	
	//听口训练子试题_选择填空
	@Transient
	public static final int QUESTION_TYPE_LISTEN_CHOICE_BLANK= 101;
	
	//读写训练子试题-点句多选
	@Transient
	public static final int QUESTION_TYPE_READ_MULTICHOICE= 102;
	
	//读写训练子试题-引导作文
	@Transient
	public static final int QUESTION_TYPE_GUIDE_WHRITE= 103;
	
	//不作为子题的用于交互练习题的选词填空
	@Transient
	public static final int QUESTION_TYPE_STANDALONE_CHOICE_WORD= 104;
	
	//限时找词
	@Transient
	public static final int QUESTION_TYPE_LOOKUP_WORD= 105;
	
	//改错题
	@Transient
	public static final int QUESTION_TYPE_CORRECTION= 106;
	
	//改错题子题
	@Transient
	public static final int QUESTION_TYPE_SUB_CORRECTION= 107;
	//表格矩阵
	@Transient
	public static final int QUESTION_SHOWFORM_BIAOGE= 2;
	//矩阵-拖拽排序
	@Transient
	public static final int QUESTION_SHOWFORM_SORT= 1;
	//主观题
	@Transient
	public static final int QUESTION_SUBJECTIVE_Y = 0;
	//客观题
	@Transient
	public static final int QUESTION_SUBJECTIVE_N = 1;
	
	//矩阵题的展现形式
	@Transient
	public static final int[] QUESTION_TYPE_TABLE_TYPE= {40,41,42};
	
	private static final long serialVersionUID = 739891246125699312L;

	// 编码
	private String code;

	// 名称
	private String name;

	// 试题状态: 0 未审核 1 已审核 2 停用
	private int status = QUESTION_STATUS_UNAUDIT;

	// 题目类型编码
	@Column(name = "question_type_id")
	private int questionTypeId;
    @Transient
    private Integer questionTypeSX;//  属性标签->学科题型->学科->学段->学科题型->题型id
    @Transient
    private String questionTypeSXN;//  属性标签->学科题型->学科->学段->学科题型->题型name
	// 题目表_ID
	@Column(name = "te_id")
	private int teId;

	// 创建人
	@Column(name = "create_by")
	private String createBy;

	// 最后修改人
	@Column(name = "last_update_by")
	private String lastUpdateBy;

	// 创建时间
	@Column(name = "create_date")
	private Date createDate;

	// 最后修改时间
	@Column(name = "last_update_date")
	private Date lastUpdateDate;

	// 备注
	private String remark;

	// 答案解析
	private String explan;

	// 版本
	private int version;

	// 顺序
	@Column(name = "sequence_id")
	private int sequenceId;

	// 答案提示
	@Column(name = "question_tip")
	private String questionTip;

	// 新版本为 1 非新版本为0 default 1
	@Column(name = "new_version")
	private int newVersion = 1;

	// 是否客观题 1 为客观题 0为非客观题 default 1
	private int issubjectived = 1;

	//错误率
	@Column(name = "error_per")
	private float errorPer=0;
    /**  yong hu cuo ti ce shi */
    @Transient
    private Integer errorTimes;
	//输入类型 1 手工输入 2为批量录入
	@Column(name = "input_type")
	private int inputType=1;
	//扩展材料,用于子题目,根据te_id判断是否是子题
	@Column(name = "topic_ext")
	private String topicExt;
	//是否可分割为子试题，用于不包含子试题但存储结构为子试题的题型（0：否，1：是，默认为1）
	private int atomic;
	
	private String vin;
	
	@Column(name = "tag_full_path")
	private String tagPath;
	
	@Column(name = "show_form")
	private int showForm;
	
	@Column(name = "template_ids")
	private String templateIds = "";
	
	@Transient
	private boolean available = true;
    @Transient
    private List<Tags> knowledgeTagList=new ArrayList<Tags>();// 作业知识点统计分析查询知识点标签
    @Transient
    private List<Integer> knowledgeTagIdList=new ArrayList<Integer>();
    @Transient
    private String knowledgeTags;// 查询试题封装知识点标签
    @Transient
    private String knowledgeTagsFullPath;// 查询试题封装知识点标签 全路径
    @Transient
    private String teacheringTags;// 查询试题封装教材目录标签
    @Transient
    private String teacheringTagsFullPath;// 查询试题封装教材目录标签 全路径
    @Transient
    private boolean good=false;//是否精品
    @Transient
    private String kaoChaNl;// 考察能力
    @Transient
    private String searchContent;// 关键字搜索字段
    /**  用户使用次数 */
    @Transient
    private Integer useTimes;
    /**  用户使用 IDS  */
    @Transient
    private String userUseIds;
    /** * 用户收藏IDS */
    @Transient
    private String userCollectionIds;
    /** * 当前用户是否收藏查询题列表时封装*/
    @Transient
    private boolean loginUserCollectioned=false;
    @Transient
    private List<QuestionErrUser> questionErrUser = new ArrayList<QuestionErrUser>();
    /**  组卷默认分值  */
    @Transient
    private Double defaultScore;

    @Transient
    QuestionViewDto questionViewDto=new QuestionViewDto();
    @Transient
    private boolean subAllObjective=true;//学生自测 ：默认小题全是客观题
	/** default constructor */
	public Question() {
	}

	/** minimal constructor */
	public Question(String code) {
		this.code = code;
	}

	/** full constructor */
	public Question(String code, String name, Integer status,
			int questionTypeId, int teId, String createBy, String lastUpdateBy,
			Date createDate, Date lastUpdateDate, String remark, String explan,
			int version, int sequenceId, String questionTip, int newVersion,
			int issubjectived) {
		this.code = code;
		this.name = name;
		this.status = status;
		this.questionTypeId = questionTypeId;
		this.teId = teId;
		this.createBy = createBy;
		this.lastUpdateBy = lastUpdateBy;
		this.createDate = createDate;
		this.lastUpdateDate = lastUpdateDate;
		this.remark = remark;
		this.explan = explan;
		this.version = version;
		this.sequenceId = sequenceId;
		this.questionTip = questionTip;
		this.newVersion = newVersion;
		this.issubjectived = issubjectived;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return this.lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExplan() {
		return this.explan;
	}

	public void setExplan(String explan) {
		this.explan = explan;
	}

	public String getQuestionTip() {
		return this.questionTip;
	}

	public void setQuestionTip(String questionTip) {
		this.questionTip = questionTip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(int questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public int getTeId() {
		return teId;
	}

	public void setTeId(int teId) {
		this.teId = teId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}

	public int getIssubjectived() {
		return issubjectived;
	}

	public void setIssubjectived(int issubjectived) {
		this.issubjectived = issubjectived;
	}

	public float getErrorPer() {
		return errorPer;
	}

	public void setErrorPer(float errorPer) {
		this.errorPer = errorPer;
	}

	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public String getTopicExt() {
		return topicExt;
	}

	public void setTopicExt(String topicExt) {
		this.topicExt = topicExt;
	}

	public void setAtomic(int atomic) {
		this.atomic = atomic;
	}

	public int getAtomic() {
		return atomic;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getTagPath() {
		return tagPath;
	}

	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}

	public int getShowForm() {
		return showForm;
	}

	public void setShowForm(int showForm) {
		this.showForm = showForm;
	}

	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

    public String getTeacheringTags() {
        return teacheringTags;
    }

    public void setTeacheringTags(String teacheringTags) {
        this.teacheringTags = teacheringTags;
    }

    public String getKnowledgeTags() {
        return knowledgeTags;
    }

    public void setKnowledgeTags(String knowledgeTags) {
        this.knowledgeTags = knowledgeTags;
    }

    public String getTeacheringTagsFullPath() {
        return teacheringTagsFullPath;
    }

    public void setTeacheringTagsFullPath(String teacheringTagsFullPath) {
        this.teacheringTagsFullPath = teacheringTagsFullPath;
    }

    public String getKnowledgeTagsFullPath() {
        return knowledgeTagsFullPath;
    }

    public void setKnowledgeTagsFullPath(String knowledgeTagsFullPath) {
        this.knowledgeTagsFullPath = knowledgeTagsFullPath;
    }

    public Integer getQuestionTypeSX() {
        return questionTypeSX;
    }

    public void setQuestionTypeSX(Integer questionTypeSX) {
        this.questionTypeSX = questionTypeSX;
    }

    public String getUserCollectionIds() {
        return userCollectionIds;
    }

    public void setUserCollectionIds(String userCollectionIds) {
        this.userCollectionIds = userCollectionIds;
    }

    public String getUserUseIds() {
        return userUseIds;
    }

    public void setUserUseIds(String userUseIds) {
        this.userUseIds = userUseIds;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public boolean isLoginUserCollectioned() {
        return loginUserCollectioned;
    }

    public void setLoginUserCollectioned(boolean loginUserCollectioned) {
        this.loginUserCollectioned = loginUserCollectioned;
    }

    public List<Tags> getKnowledgeTagList() {
        return knowledgeTagList;
    }

    public void setKnowledgeTagList(List<Tags> knowledgeTagList) {
        this.knowledgeTagList = knowledgeTagList;
    }

    public List<Integer> getKnowledgeTagIdList() {
        return knowledgeTagIdList;
    }

    public void setKnowledgeTagIdList(List<Integer> knowledgeTagIdList) {
        this.knowledgeTagIdList = knowledgeTagIdList;
    }

	public List<QuestionErrUser> getQuestionErrUser() {
		return questionErrUser;
	}

	public void setQuestionErrUser(List<QuestionErrUser> questionErrUser) {
		this.questionErrUser = questionErrUser;
	}

    public Integer getErrorTimes() {
        return errorTimes;
    }

    public void setErrorTimes(Integer errorTimes) {
        this.errorTimes = errorTimes;
    }

    public String getQuestionTypeSXN() {
        return questionTypeSXN;
    }

    public void setQuestionTypeSXN(String questionTypeSXN) {
        this.questionTypeSXN = questionTypeSXN;
    }

    public Double getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(Double defaultScore) {
        this.defaultScore = defaultScore;
    }

    public QuestionViewDto getQuestionViewDto() {
        return questionViewDto;
    }

    public void setQuestionViewDto(QuestionViewDto questionViewDto) {
        this.questionViewDto = questionViewDto;
    }

    public String getKaoChaNl() {
        return kaoChaNl;
    }

    public void setKaoChaNl(String kaoChaNl) {
        this.kaoChaNl = kaoChaNl;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isSubAllObjective() {
        return subAllObjective;
    }

    public void setSubAllObjective(boolean subAllObjective) {
        this.subAllObjective = subAllObjective;
    }
}
