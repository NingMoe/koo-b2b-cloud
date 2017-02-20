package com.koolearn.cloud.exam.examcore.question.dao.builder;

import java.sql.Connection;
import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

public class ChoiceAnswerDaoBuilder  implements AriesDynamicSqlBuilder{
	

	public String  deleteAttatch(Connection conn,int choice_id,List<Integer> ids){
		StringBuilder sb=new StringBuilder(100);
		sb.append("delete from te_choiceanswer where choice_id=").append(choice_id);
		if(ids!=null&&ids.size()>0){
			sb.append(" and id not in(");
			boolean isAppend=false;
			for(int num:ids){
				if(isAppend){
					sb.append(",");
					sb.append(num);
				}else{
					sb.append(num);
					isAppend=true;
				}
			}
			sb.append(" )");
		}
		return sb.toString();
	}
}
