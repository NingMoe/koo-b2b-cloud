package com.koolearn.cloud.exam.examcore.paper.dao.builder;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.commons.lang.StringUtils;

public class TestPaperDaoBuilder implements AriesDynamicSqlBuilder {

    public String findPaperListTotalRows(PaperPager pager){
        StringBuffer sql ;
        sql = findPaperListSql(pager);
        String sqlResult = " select count(*) from ("+ sql.toString() +") lin ";
        return sqlResult;
    }

    /**查询试卷 */
	public String findPaperList(PaperPager pager){
		StringBuffer sql ;
		sql = findPaperListSql(pager);
		sql.append(" order by tp.create_time desc ");
		sql.append("limit ").append(pager.getPageNo()*pager.getPageSize())
                .append(",").append(pager.getPageSize());
		return sql.toString();
	}
    private StringBuffer findPaperListSql(PaperPager pager) {
        StringBuffer sql = new StringBuffer(" select tp.id paperId,tp.paper_name paperName,tp.* ");
        sql.append(" from te_paper tp  ");
        sql.append(" where 1=1  ");
        if(StringUtils.isNotBlank(pager.getSearchText())){
            String searchText="%"+pager.getSearchText()+"%";
            sql.append(" and tp.paper_name like '"+searchText+"' ");
        }
        if(pager.getPagerTag()!=null&&pager.getPagerTag().size()>0){
            for (int i = 0; i < pager.getPagerTag().size(); i++) {
                String tagId="%"+pager.getPagerTag().get(i)+"%";
                sql.append(" and tp.tag_full_path like '"+tagId+"' ");
            }
        }
        return sql;
    }
    public String deleteSyncQuestion(String deleteSql){
        return deleteSql;
    }
}
