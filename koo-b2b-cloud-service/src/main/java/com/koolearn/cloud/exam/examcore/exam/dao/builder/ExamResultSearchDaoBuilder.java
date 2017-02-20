package com.koolearn.cloud.exam.examcore.exam.dao.builder;

import com.koolearn.cloud.exam.examcore.exam.dto.SearchResultDto;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;



public class ExamResultSearchDaoBuilder  implements AriesDynamicSqlBuilder{
	public String findItems4Search(SearchResultDto searchResultDto){
		StringBuilder sb=new StringBuilder(200);
		sb.append("select * from te_exam_result_search s");
		innerSearch(searchResultDto, sb);
		
		sb.append(" order by rtime desc,id desc ");
		return sb.toString();
	}
	private void innerSearch(SearchResultDto searchResultDto, StringBuilder sb) {

	}
	public String findItems4SearchCount(SearchResultDto searchResultDto){
		StringBuilder sb=new StringBuilder(200);
		sb.append("select count(1) from te_exam_result_search s");
		innerSearch(searchResultDto, sb);
		sb.append("");
		return sb.toString();
	}
}
