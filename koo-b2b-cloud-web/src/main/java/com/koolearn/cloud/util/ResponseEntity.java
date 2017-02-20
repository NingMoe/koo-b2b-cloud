package com.koolearn.cloud.util;


public class ResponseEntity<E> {
	private String code;
	private String msg;
	private E data;

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

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public ResponseEntity(String code, String msg, E data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <E> ResponseEntity<E> success(E data) {
		return success("success", data);
	}
	
	public static <E> ResponseEntity<E> success(String msg, E data) {
		return new ResponseEntity<E>("200", msg, data);
	}
	
	public static <E> ResponseEntity<E> fail(String msg) {
		return new ResponseEntity<E>("400", msg, null);
	}
	
	public static <E> ResponseEntity<E> unAuthentication(String msg) {
	    return new ResponseEntity<E>("401", msg, null);
	}
	
	public static <E> ResponseEntity<E> noAuth(String msg) {
	    return new ResponseEntity<E>("401", msg, null);
	}
	
}
