package com.koolearn.cloud.common.entity;

import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.util.GlobalConstant;

import java.io.Serializable;

/**
 * ajax分页对象
 */
public class ResponseAjaxData implements Serializable {
	private static final long serialVersionUID = 1L;
	private  AjaxPagingEntity datas=new AjaxPagingEntity();
	private  String message;
	private int status;

	private ResponseAjaxData() {

	}
	public  static ResponseAjaxData getResponseAjaxData(Pager pager) {
		ResponseAjaxData rad=new ResponseAjaxData();
		rad.getDatas().setCurrentPage(pager.getPageNo());
		long totalpage=pager.getTotalRows()/GlobalConstant.PAGER_SIZE_DEFAULT_20;
		long mod=pager.getTotalRows()% GlobalConstant.PAGER_SIZE_DEFAULT_20;
		if(mod>0){ ++totalpage;}
		rad.getDatas().setTotalPage(totalpage);
		rad.getDatas().setDataList(pager.getResultList());
		rad.setMessage("成功");
		rad.setStatus(CommonInstence.CODE_0);
		return rad;

	}

	public AjaxPagingEntity getDatas() {
		return datas;
	}

	public void setDatas(AjaxPagingEntity datas) {
		this.datas = datas;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
