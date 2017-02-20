package com.koolearn.cloud.resource.dto;

import java.io.Serializable;

public class NodeKeyValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
