package com.koolearn.cloud.exam.examcore.paper.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionBar implements Serializable {
    private  Integer count ;//试题栏总数
    private  Integer subjectId ;//学科
    private  Integer rangeId ;//学段
    private  Integer bookVersion ;//教材版本
    private  Integer obligatoryId ;//必修
    private List<QuestionBarType> questionTypeArr=new ArrayList<QuestionBarType>();//学科题型(含questionId)
    private Map<String, String> editScore=new HashMap<String, String>();//questionId:score 所有试题（含小题）发分值
    private Map<String, Integer> knowledgeCount=new HashMap<String, Integer>();//知识点数统计(知识点名：知识点比例)
    private String commitPaper;//questionTypeSX_teid_questionid_score 对应 49830_0_65507_107.9,0_65507_65509_8.3
    public Integer getCount() {
        return count;
    }


    public QuestionBar(){}
    public QuestionBar( TestPaper testPaper){
    }
    public QuestionBar( String qbarJson){
        JSONObject jb=JSON.parseObject(qbarJson);
        this.count= (Integer) jb.get("count");
        this.questionTypeArr = JSONArray.parseArray(jb.get("questionTypeArr").toString(), QuestionBarType.class);
        this.editScore= (Map<String, String>) jb.get("editScore");
        this.knowledgeCount= (Map<String, Integer>) jb.get("knowledgeCount");
        this.commitPaper= (String) jb.get("commitPaper");
        Integer _subjectId=(Integer) jb.get("subjectId");
        if(_subjectId!=null){
            this.subjectId=_subjectId;
        }
        Integer _rangeId=(Integer) jb.get("rangeId");
        if(_rangeId!=null){
            this.rangeId=_rangeId;
        }
        Integer _bookVersion=(Integer) jb.get("bookVersion");
        if(_bookVersion!=null){
            this.bookVersion=_bookVersion;
        }
        Integer _obligatoryId=(Integer) jb.get("obligatoryId");
        if(_obligatoryId!=null){
            this.obligatoryId=_obligatoryId;
        }
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<QuestionBarType> getQuestionTypeArr() {
        return questionTypeArr;
    }

    public void setQuestionTypeArr(List<QuestionBarType> questionTypeArr) {
        this.questionTypeArr = questionTypeArr;
    }

    public Map getEditScore() {
        return editScore;
    }

    public String getCommitPaper() {
        return commitPaper;
    }

    public void setCommitPaper(String commitPaper) {
        this.commitPaper = commitPaper;
    }

    public static void main(String[] args) {
        QuestionBar qb=new QuestionBar();
        qb.setCount(100);
        List<QuestionBarType> questionBarTypeList=new ArrayList<QuestionBarType>();
         QuestionBarType qbt=new QuestionBarType();
         qbt.setName("听力题");
         qbt.setType(93576);
         List<Integer> examIdArr=new ArrayList<Integer>();
         examIdArr.add(11);examIdArr.add(33);
         qbt.setExamIdArr(examIdArr);
        questionBarTypeList.add(qbt);

        QuestionBarType qbt1=new QuestionBarType();
        qbt1.setName("单项选择题");
        qbt1.setType(93577);
        List<Integer> examIdArr1=new ArrayList<Integer>();
        examIdArr1.add(55);examIdArr1.add(77);
        qbt1.setExamIdArr(examIdArr1);
        questionBarTypeList.add(qbt1);

        qb.setQuestionTypeArr(questionBarTypeList);
        Map<String,String> editScoreMap=new HashMap<String, String>();
        editScoreMap.put("93576","2.5");
        editScoreMap.put("93577","8");
        editScoreMap.put("155985","18");
        qb.setEditScore(editScoreMap);
        qb.setCommitPaper("49830_0_65507_107.9,0_65507_65509_8.3,0_65507_65510_8.3");
        System.out.println(JSON.toJSONString(qb));
    }

    public Map<String, Integer> getKnowledgeCount() {
        return knowledgeCount;
    }

    public void setKnowledgeCount(Map<String, Integer> knowledgeCount) {
        this.knowledgeCount = knowledgeCount;
    }

    public void setEditScore(Map<String, String> editScore) {
        this.editScore = editScore;
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
}

