package com.koolearn.cloud.exam.examcore.exam.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.examcore.exam.dto.SearchResultDto;
import com.koolearn.cloud.exam.examspread.exam.entity.ExamResultSearch;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;

@DAO(dbtype=DbType.MYSQL,source= GlobalConstant.MYSQL_DATASOURCE)
public interface ExamResultSearchDao {

	@SQL(type=SQLType.READ)
	List<ExamResultSearch> findItems4Search(@PageBy SearchResultDto searchResultDto);

	@SQL(type=SQLType.READ)
	int findItems4SearchCount(SearchResultDto searchResultDto);
	
	/**
	 * 写入数据
	 * @param conn
	 * @param resultSearch
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	public int insert(Connection conn, ExamResultSearch resultSearch);

	/**
	 * 更新批改状态
	 * @param conn
	 * @param search
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void updateSearch(Connection conn, ExamResultSearch search);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	@SQL(type=SQLType.READ_BY_ID)
	ExamResultSearch findById(int id);
}
