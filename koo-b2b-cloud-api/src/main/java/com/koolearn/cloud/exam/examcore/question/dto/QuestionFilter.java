package com.koolearn.cloud.exam.examcore.question.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.login.entity.UserEntity;

public class QuestionFilter extends Pager implements Serializable {

	private static final long serialVersionUID = 6052915944634248391L;
    private Integer questionId;
    private Integer subjectId;//学科
    private Integer rangeId;//学段
    private Integer bookVersion;//教材版本
    private Integer obligatoryId ;//必修id
    private Integer tagId ;//树节点id
    private List<Integer> pagerTag;//智能组卷:进度点标签集合
//    private String[] questionCount;//智能组卷:题型_数量
    private String questionCount;//智能组卷:题型_数量,逗号
    private Integer questionType;//题型id
    private Integer questionHard;//题难度
    private Integer questionFilter;//过滤条件
    private String keyTxt;//搜索关键字
    private String orderBy;//排序字段
    private UserEntity loginUser;
    private String createFrom;//试卷创建来源 题库  作业 课堂
    private Integer id;//换题排除id
    private Integer mustNotPaperId;//换题排除id
    private Integer number;//学生组题自测，题目数量
    private Integer issubjectived;// 1 为客观 0 为非客观
    private Integer zcCount;// 自测抽提个数，判断题库数量是否满足抽取数量
    private String classesIds;//学生组题自测，进度点标签id
    private Set<Integer> mustNotQuestionSet;//已加入试卷的题目id
    private String questionTypeArr;//学生错题本复习提交数据
    private boolean hasQuestion=false;//智能组卷：标识本次组卷是否有题
    private boolean  excludeSubjective=false;//取题是判断是否需要排除小题有主观题的题,默认不排除
//    private Integer questionViewType;//题展示类型
//    private Integer questionButtonType;//试题操作按钮显示类型

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

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionHard() {
        return questionHard;
    }

    public void setQuestionHard(Integer questionHard) {
        this.questionHard = questionHard;
    }

    public Integer getQuestionFilter() {
        return questionFilter;
    }

    public void setQuestionFilter(Integer questionFilter) {
        this.questionFilter = questionFilter;
    }

    public String getKeyTxt() {
        return keyTxt;
    }

    public void setKeyTxt(String keyTxt) {
        this.keyTxt = keyTxt;
    }



    /**
     * 编码
     */
    private String code;
    /**
     * 试题状态 0新建 1有效已审核 2 作废 -1不做处理
     */
    private int status=-1;
    /**
     * 创建试题方式 1手工 2批量 0不区分
     */
    private int inputType=0;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private String beginTimeStr;
    /**
     * 创建时间
     */
    private String endTimeStr;
    /**
     * 试题错误率
     */
    private String beginErrorPer;
    /**
     * 试题错误率
     */
    private String endErrorPer;


    private String labelIds;

    private String labelTexts;
    private String defaultQuestionType=null;

    private int showForm=-1;

    private String templateIds;

    private String paperCode;
    /**
     * 多个状态，以逗号分隔
     */
    private String statusStr;

    private String lastUpdateBy;

    private int teId=-1;

    private int  schoolId=-1;//学校id
    private int  questionSource=-1;//试题来源（新东方1、 我的上传2、本校共享3）QUESTION_SOURCE_TYPE_XDF
    private int  creatorId=-1;//题目创建者id
    private int  filterType=-1;  //过滤类型  1过滤使用过的试题  2只选收藏的试题    FILTER_TYPE_USEED
    private int  randomCount; //产生随机试题数量

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getBeginErrorPer() {
        return beginErrorPer;
    }

    public void setBeginErrorPer(String beginErrorPer) {
        this.beginErrorPer = beginErrorPer;
    }

    public String getEndErrorPer() {
        return endErrorPer;
    }

    public void setEndErrorPer(String endErrorPer) {
        this.endErrorPer = endErrorPer;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    public String getLabelTexts() {
        return labelTexts;
    }

    public void setLabelTexts(String labelTexts) {
        this.labelTexts = labelTexts;
    }

    public String getDefaultQuestionType() {
        return defaultQuestionType;
    }

    public void setDefaultQuestionType(String defaultQuestionType) {
        this.defaultQuestionType = defaultQuestionType;
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

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getTeId() {
        return teId;
    }

    public void setTeId(int teId) {
        this.teId = teId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getQuestionSource() {
        return questionSource;
    }

    public void setQuestionSource(int questionSource) {
        this.questionSource = questionSource;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public UserEntity getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(UserEntity loginUser) {
        this.loginUser = loginUser;
    }

    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }

    public List<Integer> getPagerTag() {
        return pagerTag;
    }

    public void setPagerTag(List<Integer> pagerTag) {
        this.pagerTag = pagerTag;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMustNotPaperId() {
        return mustNotPaperId;
    }

    public void setMustNotPaperId(Integer mustNotPaperId) {
        this.mustNotPaperId = mustNotPaperId;
    }

    public String getClassesIds() {
        return classesIds;
    }

    public void setClassesIds(String classesIds) {
        this.classesIds = classesIds;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Integer> getMustNotQuestionSet() {
        return mustNotQuestionSet;
    }

    public void setMustNotQuestionSet(Set<Integer> mustNotQuestionSet) {
        this.mustNotQuestionSet = mustNotQuestionSet;
    }

    public String getQuestionTypeArr() {
        return questionTypeArr;
    }

    public void setQuestionTypeArr(String questionTypeArr) {
        this.questionTypeArr = questionTypeArr;
    }

    public Integer getIssubjectived() {
        return issubjectived;
    }

    public void setIssubjectived(Integer issubjectived) {
        this.issubjectived = issubjectived;
    }

    public Integer getZcCount() {
        return zcCount;
    }

    public void setZcCount(Integer zcCount) {
        this.zcCount = zcCount;
    }

    public boolean isHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(boolean hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

    public boolean isExcludeSubjective() {
        return excludeSubjective;
    }

    public void setExcludeSubjective(boolean excludeSubjective) {
        this.excludeSubjective = excludeSubjective;
    }
}
