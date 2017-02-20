package com.koolearn.cloud.exam.examcore.exam.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;


/**
 * @author DuHongLin
 * 考试结果结构DAO操作接口
 */
@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ExamResultStructureDao extends Serializable
{

	@SQL("SELECT * FROM te_exam_result_structure WHERE result_id = :resultId")
	public List<TpExamResultStructure> selectByResultId(@SQLParam("resultId") int resultId);
	
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, TpExamResultStructure resultStructure);
	
	@SQL("SELECT * FROM te_exam_result_structure WHERE structure_id = :structureId and question_id=:questionId")
	public TpExamResultDetail selectByStructureId(@SQLParam("questionId") int questionId, @SQLParam("structureId") int structureId);
}
