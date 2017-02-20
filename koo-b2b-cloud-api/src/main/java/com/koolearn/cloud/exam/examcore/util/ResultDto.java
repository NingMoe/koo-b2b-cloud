package com.koolearn.cloud.exam.examcore.util;

import com.koolearn.cloud.exam.examcore.question.dto.BaseDto;

/**
 * ajax调用返回信息使用的DTO
 * @author wangpeng
 * @date Oct 29, 2012
 * 技术教辅社区组@koolearn.com
 */
public class ResultDto extends BaseDto {
	private String result="";
	private String message="";
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void fail(){
		result="false";
	}
	public void success(){
		result="true";
		
	}
	
	
}
