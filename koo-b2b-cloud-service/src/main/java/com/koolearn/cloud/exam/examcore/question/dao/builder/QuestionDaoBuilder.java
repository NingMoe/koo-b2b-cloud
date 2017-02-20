package com.koolearn.cloud.exam.examcore.question.dao.builder;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import org.apache.commons.lang.StringUtils;

import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

public class QuestionDaoBuilder  implements AriesDynamicSqlBuilder{
	/**
	 * 根据过滤条件查询试题列表。分页查询
	 * @param
	 * @return
	 */
	public String searchQuestionByFilter(QuestionFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        sql.append(" order by q.create_date DESC ");
        sql.append("limit ").append(filter.getPageNo()*filter.getPageSize())
                .append(",").append(filter.getPageSize());
        return sql.toString();
	}
	/**
	 * 构造sql语句片段
	 * @param filter
	 * @param
	 * @param
	 */
	private StringBuffer structureSql(QuestionFilter filter) {
        StringBuffer sql = new StringBuffer(" select q.*");
        sql.append(" from te_question q  ");
        return sql;

	}
	/**
     * 根据过滤条件查询试题数量
     * @param
     * @return
     */
    public String searchQuestionCountByFilter(QuestionFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        String sqlResult = " select count(*) from ("+ sql.toString() +") lin ";
        return sqlResult;

    }
    /**
	 * 返回随机试题编码code
	 * 随机题目数量 randomCount
	 * 随机题目标签 tag3
	 */
	public String queryRandomQuestion(QuestionFilter filter){
		StringBuffer sqlBuffer = new StringBuffer("select q.code ");
		queryRandomQuestionComon(filter, sqlBuffer);
		return sqlBuffer.toString();
	}
	/**
	 * 返回随机试题总数量
	 * 随机题目数量 randomCount
	 * 随机题目标签 tag3
	 */
	public String queryRandomQuestionCount(QuestionFilter filter){
		StringBuffer sqlBuffer = new StringBuffer("select count(distinct q.id) ");
		queryRandomQuestionComon(filter, sqlBuffer);
		return sqlBuffer.toString();
	}
	public String queryRandomQuestionComon(QuestionFilter filter,StringBuffer sqlBuffer){
		sqlBuffer.append(" from te_question q , te_question_bank_ext qe  ");
		sqlBuffer.append(" where q.code=qe.question_code ");
		sqlBuffer.append(" and q.status=1 and q.new_version=1 and qe.new_version=1 ");
		
		return sqlBuffer.toString();
	}
}
