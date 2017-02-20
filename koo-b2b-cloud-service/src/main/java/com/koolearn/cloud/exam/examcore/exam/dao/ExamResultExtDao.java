package com.koolearn.cloud.exam.examcore.exam.dao;

import java.io.Serializable;
import java.sql.Connection;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.exam.entity.ExamResultExt;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLType;


/**
 * @author DuHongLin
 * 考试结果扩展操作DAO
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ExamResultExtDao extends Serializable
{

	/**
	 * 写入数据
	 * @param conn
	 * @param ext
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type = SQLType.WRITE_INSERT)
	public int insert(Connection conn, ExamResultExt ext);
	
}
