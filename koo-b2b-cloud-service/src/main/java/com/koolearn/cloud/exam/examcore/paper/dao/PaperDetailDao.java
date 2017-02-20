package com.koolearn.cloud.exam.examcore.paper.dao;

import java.sql.Connection;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperDetail;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface PaperDetailDao {
	@SQL(type=SQLType.WRITE_INSERT)
	int save(Connection conn, PaperDetail paperDetail);
	
	@SQL("delete d from te_paper_question_detail d,te_paper_structure s where s.paper_id=:paperId and s.id=d.paper_structure_id")
	void deleteByPaperId(Connection conn, @SQLParam("paperId") int paperId);
	
	@SQL("select * from te_paper_question_detail  where paper_structure_id=:id")
    PaperDetail findItemByStructureId(@SQLParam("id") int id);

}
