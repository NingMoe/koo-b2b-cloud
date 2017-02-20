package com.koolearn.cloud.exam.examcore.util;

import com.koolearn.cloud.exam.examcore.question.dto.BaseDto;

public class AjaxMessageDto extends BaseDto {

	private static final long serialVersionUID = 2129552789787331897L;

	private Object messageInfo; // 请求信息

	private boolean success;// ajax请求状态

	private String callback;// 用于跨域

	public Object getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(Object messageInfo) {
		this.messageInfo = messageInfo;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

}
