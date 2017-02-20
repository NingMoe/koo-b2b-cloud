package com.koolearn.cloud.mobi.dto;

import com.koolearn.cloud.login.dto.UserMobi;
import java.io.Serializable;

public class UserJson implements Serializable {
	public static String CODE_VALID = "0";
	public static String CODE_INVALID = "-1";
	
	private String code;//-1失败 0正常
	private String messge;//返回提示信息
	private UserMobi ue;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessge() {
		return messge;
	}
	public void setMessge(String messge) {
		this.messge = messge;
	}
	public UserMobi getUe() {
		return ue;
	}
	public void setUe(UserMobi ue) {
		this.ue = ue;
	}
}
