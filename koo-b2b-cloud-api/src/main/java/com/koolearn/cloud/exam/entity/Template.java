package com.koolearn.cloud.exam.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Template implements Serializable {
	private static final long serialVersionUID = 1L;
    private String name;//结构名称
    private Integer id;//结构id
    private Double points=0D;//题型总分
    private Integer orderAsc;//排序字段
    private List<String> questionList=new ArrayList<String>();
}
