package com.koolearn.cloud.exam.examcore.paper.vo;

import java.io.Serializable;

public class PaperPaparmVo  implements Serializable{
	private PaperPaparmVo(){}
	public static PaperPaparmVo instance(){
		return new PaperPaparmVo();
	}
/**
 * 只能组卷,是否缺少题目,默认不缺少题目
 */
private boolean lackQuesiton=false;

public boolean isLackQuesiton() {
	return lackQuesiton;
}

public PaperPaparmVo setLackQuesiton(boolean lackQuesiton) {
	this.lackQuesiton = lackQuesiton;
	return this;
}

}
