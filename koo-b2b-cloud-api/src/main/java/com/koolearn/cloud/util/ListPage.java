package com.koolearn.cloud.util;

import java.io.Serializable;
import java.util.List;

import com.koolearn.framework.common.page.ListPager;

/**
 * 分页组件
 * TODO
 * @author miaoyoumeng
 * @date Nov 1, 2012
 *
 */
public class ListPage implements Serializable{

	private static final long serialVersionUID = -7217252255368722486L;
	
	private int totalRows;
	
	@SuppressWarnings("rawtypes")
	private List resultList;
	
	private ListPager listPager;

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	@SuppressWarnings("rawtypes")
	public List getResultList() {
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public ListPager getListPager() {
		return listPager;
	}

	public void setListPager(ListPager listPager) {
		this.listPager = listPager;
	}
	
}