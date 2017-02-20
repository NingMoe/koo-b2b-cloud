package com.koolearn.cloud.exam.examcore.util;

public class ExamException extends Exception{

	private static final long serialVersionUID = -262378565732280706L;
	private Object writeLogObj=null;
	private boolean isWrite=false;
	static {
		// init isWrite status
	}
	public ExamException() {
		super();
	}

	public ExamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExamException(String message) {
		super(message);
	}

	public ExamException(Throwable cause) {
		super(cause);
	}
	public ExamException(Exception exception) {
		super(exception);
	}
	public ExamException(Exception exception,Object writeLogObj) {
		super(exception);
		this.writeLogObj=isWrite;
	}
	
	
}
