package com.koolearn.cloud.ireport.controller;

import java.io.Serializable;

public class Exam implements Serializable{
	private static final long serialVersionUID = 4078924027323737401L;
	
	private int id;
	
	

	public Exam(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
    

}