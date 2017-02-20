package com.koolearn.cloud.exam.examcore.paper.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface TestPaperStructureDao {
	
	@SQL("SELECT  t.* FROM te_paper_structure  t WHERE t.paper_id=:paperId order by id asc")
	List<TestPaperStructure> findItemsByPaperId(@SQLParam("paperId") int paperId);
	
	
	
	//以上方法为更新后方法
	
	
	
	
	
	
	
	@SQL(type=SQLType.WRITE_INSERT)
	int save(Connection conn, TestPaperStructure testPaperStructure);
	@SQL("delete from te_paper_structure where paper_id=:paperId")
	void deleteByPaperId(@SQLParam("paperId") int paperId);
	
	@SQL("SELECT  t.* FROM te_paper_structure  t WHERE t.paper_id=:paperId AND t.structure_type=:type2")
	List<TestPaperStructure> findItems4Type(@SQLParam("paperId") int paperId, @SQLParam("type2") int structureType);
	
	@SQL("SELECT  count(1)  FROM te_paper_structure  t WHERE t.paper_id=:paperId AND t.structure_type=:type2")
	int findItemsCount4Type(@SQLParam("paperId") int paperId, @SQLParam("type2") int structureTypeQuestion);
	@SQL("delete from te_paper_structure where paper_id=:paperId")
	void deleteByPaperId(Connection conn, @SQLParam("paperId") int paperId);
    @SQL("SELECT  t.* FROM te_paper_structure  t WHERE t.paper_id=:paperId and structure_type=1")
    List<TestPaperStructure> findStructureQuestionByPaperId(@SQLParam("paperId") int paperId);
    @SQL("SELECT  t.* FROM te_paper_structure  t WHERE t.paper_id=:paperId and structure_type=0 and paper_id <> 0")
    List<TestPaperStructure> findStructureQuestionTypeByPaperId(@SQLParam("paperId") int paperId);

    @SQL("SELECT  `code`,`points`,parent_code FROM te_paper_sub_score WHERE paper_id=:paperId")
    List<PaperSubScore> findSubQuestionPoints(@SQLParam("paperId") Integer paperId);
}
