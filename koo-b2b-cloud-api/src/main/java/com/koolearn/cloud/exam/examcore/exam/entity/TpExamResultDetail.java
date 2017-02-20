package com.koolearn.cloud.exam.examcore.exam.entity;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;


@Entity
@Table(name = "te_exam_result_detail")
public class TpExamResultDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    //是否正确,-1为 未判题，0为不正确，1为正确 2.部分正确
    public static final int RESULT_ANSWER_NOT = -1;
    public static final int RESULT_ANSWER_NOT_CORRECT = 0;
    public static final int RESULT_ANSWER_CORRECT = 1;
    public static final int RESULT_ANSWER_PART_CORRECT = 2;
    public static final int SUBJECTIVE = 1;
    public static final int TE_ID = 0;
    

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /** 考试结果id */
    @Column(name = "result_id")
    private Integer resultId;
    /** 考试结果结构id */
    @Column(name = "exam_result_structure")
    private Integer examResultStructure;
    /** 题目ID */
    @Column(name = "question_id")
    private Integer questionId;
    /** 题目类型ID */
    @Column(name = "question_type_id")
    private Integer questionTypeId;
    /** 题目编码 */
    @Column(name = "question_code")
    private String questionCode;
    /** 父级l题目id，te_id=0  */
    @Column(name = "te_id")
    private Integer teId;
    /** 父级l题目类型 */
    @Column(name = "te_type")
    private Integer teType;
    /** 分值 */
    @Column(name = "points")
    private Double points;
    /** 得分 */
    @Column(name = "score")
    private Double score;
    /** 正确答案 */
    @Column(name = "right_answer")
    private String rightAnswer;
    /** 用户答案 */
    @Column(name = "user_answer")
    private String userAnswer;
    /** 是否正确,-1为 未判题，0为不正确，1为正确 */
    @Column(name = "result_answer")
    private int resultAnswer;
    /** 是否是主观题，1为是，0为不是 */
    @Column(name = "subjective")
    private Integer subjective;
    /** 老师批阅内容 */
    @Column(name = "reply")
    private String reply;

    private List<TpExamResultDetail> subDetails = new ArrayList<TpExamResultDetail>(0);

    //学生id
    @Transient
    private Integer studentId;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Integer getExamResultStructure() {
        return examResultStructure;
    }

    public void setExamResultStructure(Integer examResultStructure) {
        this.examResultStructure = examResultStructure;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public Integer getTeId() {
        return teId;
    }

    public void setTeId(Integer teId) {
        this.teId = teId;
    }

    public Integer getTeType() {
        return teType;
    }

    public void setTeType(Integer teType) {
        this.teType = teType;
    }

    public Double getPoints() {
        if (null != this.points)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            return Double.valueOf(nf.format(this.points));
        }
        else
        {
            return this.points;
        }
    }

    public void setPoints(Double points) {
        if (null != points)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            this.points = Double.valueOf(nf.format(points));
        }
        else
        {
            this.points = points;
        }
    }

    public Double getScore() {
        if (null != this.score)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            return Double.valueOf(nf.format(this.score));
        }
        else
        {
            return this.score;
        }
    }

    public void setScore(Double score) {
        if (null != score)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            this.score = Double.valueOf(nf.format(score));
        }
        else
        {
            this.score = score;
        }
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getResultAnswer() {
        return resultAnswer;
    }

    public void setResultAnswer(int resultAnswer) {
        this.resultAnswer = resultAnswer;
    }

    public Integer getSubjective() {
        return subjective;
    }

    public void setSubjective(Integer subjective) {
        this.subjective = subjective;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<TpExamResultDetail> getSubDetails() {
        return subDetails;
    }

    public void setSubDetails(List<TpExamResultDetail> subDetails) {
        this.subDetails = subDetails;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }
}
