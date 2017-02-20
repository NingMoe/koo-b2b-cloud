package com.koolearn.cloud.exam.examcore.question.entity;

import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.util.pdf.HtmlCssUtil;
import org.springframework.web.util.HtmlUtils;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.cloud.exam.examcore.util.FillBlankAnswerUtil;


/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，请勿做客户化操作， 对应表:TE_FILLBLANK_ANSWER	填空答案表
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2012-10-22 11:47:04<br />
 * 作者:liaoqiangang
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
@Entity
@Table(name="TE_FILLBLANK_ANSWER")
public class FillblankAnswer extends BaseEntity {
	
	@Column(name="fillblank_id")
	private int fillblankId; 

	private String answer;
	
	@Column(name="sequnce_id")
	private int sequnceId;
	
	//单位的位置:0-前面；1-后面；
	private int place;
	
	//计算题  单位
	private String company;
	
	/**
	 * 扩展
	 */
	private String ext;
	/**
	 * 扩展信息 答案有一个变为多个
	 */
	@Transient
	private List<String> exts=new ArrayList<String>();
	


	public FillblankAnswer(int fillblankId, String answer, int sequnceId,
			int place, String company) {
		super();
		this.fillblankId = fillblankId;
		this.answer = answer;
		this.sequnceId = sequnceId;
		this.place = place;
		this.company = company;
	}

	
	
	public FillblankAnswer() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getFillblankId() {
		return fillblankId;
	}

	public void setFillblankId(int fillblankId) {
		this.fillblankId = fillblankId;
	}

	public String getAnswer() {
		if(this.answer!=null){
			return HtmlUtils.htmlUnescape(this.answer);
		}else{
			return this.answer;
		}
	}
	public String getAnswer2() {
		FillBlankAnswerUtil.wrap(this);
		if(this.exts!=null&&exts.size()>0){
			StringBuilder sb=new StringBuilder(100);
			String s="或";
			for(String str:this.exts){
				sb.append(s).append(str);
			}
			return HtmlCssUtil.parseImageUrl(sb.substring(s.length()));
		}
		return "";
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSequnceId() {
		return sequnceId;
	}

	public void setSequnceId(int sequnceId) {
		this.sequnceId = sequnceId;
	}

	public List<String> getExts() {
		return exts;
	}

	public void setExts(List<String> exts) {
		this.exts = exts;
	}
	
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}

	
}
