package com.koolearn.cloud.exam.examcore.paper.dto;

import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.login.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gehaisong on 2016/4/5.
 */
public class PaperPager extends Pager implements Serializable {
    private UserEntity loginUser;
    private Integer paperId=0;
    private String paperName;//试卷组题，新试卷取名老试卷
    private String title;//试卷名称
    private String addType;//add  添加   del  移除
    private String searchText;
    private String searchFrom;//试卷搜索类型xdf 试卷组题  bxsj 本校试卷库  wdsj  我的试卷（创建Union加入的试卷）
    private Integer subjectId;//学科
    private Integer rangeId;//学段
    private Integer bookVersion;//教材目录标签
    private Integer   obligatoryId;//必修
    private Integer questionHard;//难度标签
    private String  userName;//创建人名
    private Integer tagId ;//树节点id
    private List<Integer> pagerTag=new ArrayList<Integer>();//智能组卷:进度点标签集合
    private String  pagerTagStr;//智能组卷:进度点标签集合 逗号隔开提交
    private Integer[] paperTagIds;//试卷组卷
    private List<Integer> sjztGJAll=new ArrayList<Integer>();//试卷组卷， 学科为全部时的过滤标签
    private List<Integer> sjztGJGradeAll=new ArrayList<Integer>();//试卷组卷，当年级 为全部时的过滤标签
//    private String[] questionCount;//智能组卷:题型_数量
    private String questionCount;//智能组卷:题型_数量,逗号
    private String questionBar;//组建过程中的数据：questionTypeSX_teid_questionid_score ： 49830_0_65507_107.9,0_65507_65508_8.3,
    private String commitPaper;//创建试卷提交的数据：questionTypeSX_teid_questionid_score ： 49830_0_65507_107.9,0_65507_65508_8.3,
    private Integer  createPaperType;//创建试卷类型：1 进度点组题 2知识点组题  3 试卷组题  4智能组题
    private Integer  createPaperSource;//试卷创建来源页面： 1 题库 2创建作业过来  3创建课堂过来
    private Integer  teacherId;//老师id
    private String navigation;//创建试卷类型：jdd  zsd  sjzt znzt
    private String createFrom;//试卷创建来源：题库、作业、课堂
    private Map<String, TpExamResultDetail> detailsMap = null;//试卷创建来源：题库、作业、课堂

    public Map<String, TpExamResultDetail> getDetailsMap() {
		return detailsMap;
	}

	public void setDetailsMap(Map<String, TpExamResultDetail> detailsMap) {
		this.detailsMap = detailsMap;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Integer> getPagerTag() {
        if(StringUtils.isNotBlank(this.pagerTagStr)){
            String[] tagStr=this.pagerTagStr.split(",");
            for(String tag:tagStr){
                this.pagerTag.add(Integer.parseInt(tag));
            }
        }
        return pagerTag;
    }

    public void setPagerTag(List<Integer> pagerTag) {
        this.pagerTag = pagerTag;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    public String getSearchFrom() {
        return searchFrom;
    }

    public void setSearchFrom(String searchFrom) {
        this.searchFrom = searchFrom;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(Integer bookVersion) {
        this.bookVersion = bookVersion;
    }

    public Integer getQuestionHard() {
        return questionHard;
    }

    public void setQuestionHard(Integer questionHard) {
        this.questionHard = questionHard;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public String getCommitPaper() {
        return commitPaper;
    }

    public void setCommitPaper(String commitPaper) {
        this.commitPaper = commitPaper;
    }

    public Integer getCreatePaperType() {
        return createPaperType;
    }

    public void setCreatePaperType(Integer createPaperType) {
        this.createPaperType = createPaperType;
    }

    public Integer getCreatePaperSource() {
        return createPaperSource;
    }

    public void setCreatePaperSource(Integer createPaperSource) {
        this.createPaperSource = createPaperSource;
    }

    public String getQuestionBar() {
        return questionBar;
    }

    public void setQuestionBar(String questionBar) {
        this.questionBar = questionBar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
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

    public UserEntity getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(UserEntity loginUser) {
        this.loginUser = loginUser;
    }

    public Integer[] getPaperTagIds() {
        return paperTagIds;
    }

    public void setPaperTagIds(Integer[] paperTagIds) {
        this.paperTagIds = paperTagIds;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public List<Integer> getSjztGJAll() {
        return sjztGJAll;
    }

    public void setSjztGJAll(List<Integer> sjztGJAll) {
        this.sjztGJAll = sjztGJAll;
    }

    public String getPagerTagStr() {
        return pagerTagStr;
    }

    public void setPagerTagStr(String pagerTagStr) {
        this.pagerTagStr = pagerTagStr;
    }

    public List<Integer> getSjztGJGradeAll() {
        return sjztGJGradeAll;
    }

    public void setSjztGJGradeAll(List<Integer> sjztGJGradeAll) {
        this.sjztGJGradeAll = sjztGJGradeAll;
    }
}
