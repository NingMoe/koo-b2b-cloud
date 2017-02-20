package com.koolearn.cloud.exam.examcore.paper.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface TestPaperDao {
	
	
	@SQL(" select * from te_paper where id=:paperId ")
	TestPaper findItemById(@SQLParam("paperId") int paperId);
	
	
	
	
	
	
	//以上方法为更新后方法
	
	
	
	
	
	
	
	

	/**
	 * 通过tagid获取对应的试卷集合
	 * @param listPager 
	 * @param tagId
	 * @return
	 */
	@SQL("SELECT a.* FROM te_paper a ,te_rela_paper_type b WHERE a.id=b.paper_id AND b.tag_id=:tagId")
	List<TestPaper> findTestPaperByTagId(@PageBy ListPager listPager, @SQLParam("tagId") int tagId);

	/**
	 * 数量
	 * @see com.koolearn.cloud.exam.examcore.paper.dao.TestPaperDao#findTestPaperByTagId
	 * @param tagId
	 * @return
	 */
	@SQL("SELECT count(a.id) FROM te_paper a ,te_rela_paper_type b WHERE a.id=b.paper_id AND b.tag_id=:tagId")
	int findTestPaperCountByTagId(@SQLParam("tagId") int tagId);

	
	@SQL("delete from te_paper where id=:paperId")
	void deleteByPaperId(@SQLParam("paperId") int paperId);
	
	@SQL("SELECT a.* FROM te_paper a where create_time>=:date2 and code like :prefix order by id desc limit 1")
	TestPaper findTestPaper(@SQLParam("prefix") String prefix, @SQLParam("date2") String date);
	

    /**二期begin*/
    @SQL(type=SQLType.WRITE_INSERT)
    int save(Connection conn, TestPaper paper);

	@SQL(type=SQLType.WRITE_UPDATE)
	void update(Connection conn, TestPaper paper);
    @SQL(type=SQLType.WRITE_UPDATE)
    void update(TestPaper paper);

    /**
     * 查询试卷列表
     * @param
     * @param pager
     */
    @SQL(type=SQLType.READ)
    long findPaperListTotalRows(PaperPager pager);
    @SQL(type=SQLType.READ)
    List<TestPaper> findPaperList(PaperPager pager);

    /**
     * 查询新东方试卷最后同步试卷
     * @return
     */
    @SQL("SELECT update_time FROM te_paper where fromwh=0  ORDER BY update_time DESC LIMIT 0,1")
    Date getlastSyncTime();

    @SQL("SELECT * FROM te_paper where id=:paperId")
    TestPaper findTestPaper(@SQLParam("paperId") Integer paperId);
    @SQL(type=SQLType.READ)
    void deleteSyncQuestion(String deleteSql);
    @SQL("UPDATE te_paper SET question_count = :questionCount ,question_min_count=:questionMinCount  WHERE id = :paperId")
    void updateQuestionNum(@SQLParam("paperId") Integer paperId,@SQLParam("questionCount")Integer questionCount,@SQLParam("questionMinCount") Integer questionMinCount);

    @SQL("SELECT  DISTINCT id,exam_paper_id,paper_code FROM te_paper where paper_code=:paperCode")
    List<TestPaper> findPaperByPaperCode(@SQLParam("paperCode") String paperCode);//试卷同步，防止多个相同试卷
}
