package com.koolearn.cloud.exam.examcore.paper.entity;
import com.koolearn.cloud.util.numberToChinese.NumberText;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;


public class QuestionBarHtml implements Serializable {
    private String htmlQuestionNum;//页面试题序号dom块
    private String htmlQuestionScore;//页面试题序号dom块
    private String htmlQuestionKnowledge;//页面试题序号dom块


    public QuestionBarHtml(QuestionBar bar){
        packageHtmlQuestionNum(bar);//生成题序html
    }

    public String getHtmlQuestionNum() {
        return htmlQuestionNum;
    }

    public void setHtmlQuestionNum(String htmlQuestionNum) {
        this.htmlQuestionNum = htmlQuestionNum;
    }

    public String getHtmlQuestionScore() {
        return htmlQuestionScore;
    }

    public void setHtmlQuestionScore(String htmlQuestionScore) {
        this.htmlQuestionScore = htmlQuestionScore;
    }

    public String getHtmlQuestionKnowledge() {
        return htmlQuestionKnowledge;
    }

    public void setHtmlQuestionKnowledge(String htmlQuestionKnowledge) {
        this.htmlQuestionKnowledge = htmlQuestionKnowledge;
    }
    public void packageHtmlQuestionNum(QuestionBar bar){
        StringBuilder numHtml=new StringBuilder();
        StringBuilder scoreHtml=new StringBuilder();
        numHtml.append("<div class='p-exam jp-list-info' id='jp-exam'> <dl> ");
        scoreHtml.append("<div class='p-score jp-list-info' id='jp-score'> ");
        NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
        long typeCount=0;
        int num=1;
        String reg="##%%**&&##";
        if(bar!=null){
	        for(QuestionBarType qbt:bar.getQuestionTypeArr()){
	            if(qbt.getExamIdArr()==null ||qbt.getExamIdArr().size()<1) continue;//没有题的类型不显示
	            typeCount++;
	            numHtml.append(" <dt>").append(nt.getText(typeCount)).append("、").append(qbt.getName()).append("（ ").append(qbt.getExamIdArr().size()).append(" ）</dt>");
	            numHtml.append("<dd>");
	            scoreHtml.append(" <dl class='s'>  <dt class='fc'>").append(nt.getText(typeCount)).append("、").append(qbt.getName()).append("（<span class='jp-sum'>").append(reg).append("</span>）<em></em></dt>");
	            double questionTypescore=0d;
	            for(Integer qid:qbt.getExamIdArr()){
	                String scoreStr=(String)bar.getEditScore().get(qid+"");
	                double score= StringUtils.isBlank(scoreStr)?0d:Double.parseDouble(scoreStr);
	                questionTypescore+=score;
	                numHtml.append("<i data-id='" ).append(qid).append("' name='t-1'>") .append(num).append("</i>");
	                scoreHtml.append(" <dd class='fc' data-id='").append(qid).append("' id='").append(qid).append("'><span class='fl p-n'>").append(num).append("</span>");
	                      scoreHtml.append(" <em class='fl'></em><span data-id='").append(qid).append("' class='fl p-t'>").append(score).append("</span>");
	                scoreHtml.append(" </dd>");
	                num++;
	            }
	            scoreHtml.replace(scoreHtml.indexOf(reg),scoreHtml.indexOf(reg)+reg.length(),questionTypescore+"");
	            scoreHtml.append("</dl>");
	            numHtml.append("</dd>");
	        }
        }
        scoreHtml.append(" </div>");
        numHtml.append(" </dl></div>");
        htmlQuestionNum=numHtml.toString();
        htmlQuestionScore=scoreHtml.toString();
    }
}

