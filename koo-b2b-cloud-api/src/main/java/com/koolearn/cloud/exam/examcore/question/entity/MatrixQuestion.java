package com.koolearn.cloud.exam.examcore.question.entity;

import java.util.HashMap;
import java.util.Map;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;

/**
 * 矩阵题实体类
 * TODO
 * @author tangshuren
 * @date 2012-10-29
 *
 */
@Table(name="te_matrixquestion")
@Entity
public class MatrixQuestion extends BaseEntity{
	
	/**
	 * 展现形式
	 */
	//拖拽型
	public static final int DRAG_FORM = 1;
	
	//表格型
	public static final int TABLE_FOMR = 2;

	@Column(name="question_id")
	private Integer questionId;
	
	@Column(name="sub_question_count")
	private Integer subQuestionCount;

	private String topic;
	
	@Column(name="show_form")
	private Integer showForm;
	@Transient
	public static final int QUESTION_TYPE_TABLE_BIAOGE = 41;
	public static final int QUESTION_TYPE_TABLE_TUOZHUAI = 40;
	public static final int QUESTION_TYPE_TABLE_LIANXIAN = 42;
	@Transient
	public static int[] JUZHEN_FORMS = new int[]{40,41,42};
	@Transient
	public static Map<Integer,Integer> showFormMap;
	static{
		showFormMap = new HashMap<Integer,Integer>();
		showFormMap.put(40, 1);
		showFormMap.put(41, 2);
		showFormMap.put(42, 3);
	}
	
	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getSubQuestionCount() {
		return subQuestionCount;
	}

	public void setSubQuestionCount(Integer subQuestionCount) {
		this.subQuestionCount = subQuestionCount;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getShowForm() {
		return showForm;
	}

	public void setShowForm(Integer showForm) {
		this.showForm = showForm;
	}
	
	
}
