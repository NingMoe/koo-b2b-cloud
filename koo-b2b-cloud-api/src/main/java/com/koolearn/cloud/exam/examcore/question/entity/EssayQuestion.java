package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;

/**
 * <ul>
 * <li><b>目的:</b> <br />
 * <p>
 * 实体对象，请勿做客户化操作， 对应表:TE_ESSAYQUESTION 客观填空题
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li><b>修改历史：</b><br />
 * <p>
 * 创建:2012-10-22 11:47:05<br />
 * 作者:liaoqiangang
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
	
@Entity
@Table(name = "TE_ESSAYQUESTION")
public class EssayQuestion extends BaseEntity {

	// 题目Id
	@Column(name = "questtion_id")
	private int questtionId;

	// 0-系统批改；1-人工批改 
	private int markType;

	// 题干
	private String topic;
	
	// 评分标准		0-全对得分；1-按空平均
	private int metewand;

	//	0-无序；1-一一对应
	private int one2one;
	/**
	 * 大小写是否敏感 默认为敏感, {@link ConstantTe.SENSITIVE}
	 */
	private int sensing =ConstantTe.SENSITIVE; 

	public EssayQuestion(int questtionId, int markType, String topic, int metewand,int one2one) {
		super();
		this.questtionId = questtionId;
		this.markType = markType;
		this.topic = topic;
		this.metewand = metewand;
		this.one2one = one2one;
	}

	
	
	public int getMarkType() {
		return markType;
	}



	public void setMarkType(int markType) {
		this.markType = markType;
	}



	public int getMetewand() {
		return metewand;
	}

	public int getOne2one() {
		return one2one;
	}



	public void setOne2one(int one2one) {
		this.one2one = one2one;
	}



	public void setMetewand(int metewand) {
		this.metewand = metewand;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public EssayQuestion() {
	}


	public int getQuesttionId() {
		return questtionId;
	}

	public void setQuesttionId(int questtionId) {
		this.questtionId = questtionId;
	}

	public int getSensing() {
		return sensing;
	}
	public void setSensing(int sensing) {
		this.sensing = sensing;
	}

}
