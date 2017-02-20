package com.koolearn.cloud.exam.examcore.paper.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface PaperSubScoreDao {
	
	@SQL("select * from te_paper_sub_score where paper_id=:paperId and parent_code=:parentCode")
	List<PaperSubScore> findItemByPaperId(@SQLParam("paperId") int paperId, @SQLParam("parentCode") String parentCode);
	
	
	
	
	
	//以上方法为更新后方法
	
	
	
	
	
	
	@SQL("delete from te_paper_sub_score where paper_id=:paperId")
	void deleteByPaperId(@SQLParam("paperId") int paperId);
	@SQL("delete from te_paper_sub_score where paper_id=:paperId")
	void deleteByPaperId(Connection conn, @SQLParam("paperId") int paperId);
	@SQL(type=SQLType.WRITE_INSERT)
	void save(Connection conn, PaperSubScore subScore);

	@SQL("SELECT * FROM te_paper_sub_score WHERE paper_id IN (:pids)")
	public List<PaperSubScore> selectByPaperIds(@SQLParam("pids") List<Integer> pids);
	@SQL("select * from te_paper_sub_score where paper_id=:paperId ")
	List<PaperSubScore> findItemByPaperId(@SQLParam("paperId") int paperId);

}
