package com.koolearn.cloud.common.entity;

import com.koolearn.framework.common.page.ListPager;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 */
public class Pager extends ListPager implements Serializable {
	private static final long serialVersionUID = 1L;
	private List resultList;
	/**
	 * 总页数
	 */
	private int totalPage;
	public int getTotalPage() {
		return totalPage=super.getTotalPage();
	}
	public List getResultList() {
		return resultList;
	}
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	
}
