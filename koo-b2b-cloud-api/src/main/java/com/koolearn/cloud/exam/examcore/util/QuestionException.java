package com.koolearn.cloud.exam.examcore.util;

/**
 * 题目异常类
 * @date 2014-12-4
 * @author yangzhenye
 * @version 1.0
 */
@SuppressWarnings("serial")
public class QuestionException extends Exception {

	private String code;
	private String msg;
	private Throwable e;

	public QuestionException() {
	}

	public QuestionException(String msg, Throwable e) {
		this.msg = msg;
		this.e = e;
	}

	public QuestionException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String toString() {
		return (new StringBuilder("QuestionException [code=")).append(code)
				.append(", e=").append(e).append(",, msg=").append(msg)
				.append("]").toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
