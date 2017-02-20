package com.koolearn.cloud.exam.examcore.question.dao;


import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

import java.util.List;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface QuestiontypeDao {
	@SQL(type = SQLType.READ_BY_ID)
    Questiontype findItemById(int id);
	
	@SQL("SELECT * FROM te_questiontype WHERE parent_type = :parentType")
	public List<Questiontype> selectByParent(@SQLParam("parentType") int parentType);

	@SQL("SELECT * FROM te_questiontype")
	public List<Questiontype> selectAll();
}
