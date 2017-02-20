package com.koolearn.cloud.util;

import java.io.Serializable;
import java.util.List;

/**
 * excel的一个sheet实体
 * @author fangjianwei
 *
 */
@SuppressWarnings("unchecked")
public class ExcelSheetModel implements Serializable
{
	/** sheet名称*/
	private String title = null;
	/**行版本*/
	private String[] versionName = null;
	
	/**行应收单*/
	private String[] billName = null;
	
	/**行名称*/
	private String[] colEName = null;

	/**行名称*/
	private String[] colName = null;
	
	/**内容 list 中存放LinkHashMap*/
	private List content = null;
	
	/**保存excel文件名*/
	private String fileName = null;

	public String[] getColName() {
		return colName;
	}

	public void setColName(String[] colName) {
		this.colName = colName;
	}

	public List getContent() {
		return content;
	}

	public void setContent(List content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getVersionName() {
		return versionName;
	}

	public void setVersionName(String[] versionName) {
		this.versionName = versionName;
	}

	public String[] getBillName() {
		return billName;
	}

	public void setBillName(String[] billName) {
		this.billName = billName;
	}

	public String[] getColEName() {
		return colEName;
	}

	public void setColEName(String[] colEName) {
		this.colEName = colEName;
	}
	

}
