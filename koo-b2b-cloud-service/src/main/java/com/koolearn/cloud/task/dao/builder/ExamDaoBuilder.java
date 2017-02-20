package com.koolearn.cloud.task.dao.builder;

import java.sql.Connection;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

import com.koolearn.cloud.exam.entity.ExamQueryDto;
import com.koolearn.cloud.testpaper.entity.HandTestPagerDto;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

public class ExamDaoBuilder implements AriesDynamicSqlBuilder{
	
	
	
	public String getTeacherList(String teachers){
		StringBuffer sql = new StringBuffer(" select id,name from teacher where 1=1 ");
		if(StringUtils.isNotBlank(teachers)){
			sql.append(" and id in (").append(teachers).append(")");
		}
		return sql.toString();
	}
	
	
	public String deleteExamClass(Connection conn,@SQLParam("oldClassId") String oldClassId,@SQLParam("teacherId") Integer teacherId,@SQLParam("examId") Integer examId){
		StringBuffer sql = new StringBuffer("delete from tp_exam_class where exam_id=:examId and teacher_id=:teacherId ");
		sql.append(" and sch_id in (").append(oldClassId).append(" ) ");
		return sql.toString();
	}
	
	public String getClassByIds(@SQLParam("classIds") String classIds){
		return "select * From school_level where id in ("+classIds+")";
	}
	
	//**************************监考统计功能（开始）*****************************************//
		/**
	     * 根据考试id，获取其对应的参考班级集合(默认根据id正顺排序)
	     * @param examId 考试id
	     * @return
	     * @author zhengxianyin
	     */
	    public String findExamClassList(Integer examId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  tec.*,sl.level_name  className from  tp_exam_class tec  ");
	    	sql.append("  LEFT JOIN school_level sl on  tec.sch_id = sl.id  ");
	    	sql.append("  where  tec.exam_id = "+examId+"  ");
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,获取考试人数统计对象
	     * @param examId  考试id
	     * @return
	     * @author zhengxianyin
	     */
	    public String findExamCountByExamId(Integer examId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  IFNULL(a.shouldCount,0) shouldCount , IFNULL(b.alreadyCount,0) alreadyCount , IFNULL(c.completCount,0) completCount  ");
	    	sql.append("  from  (  ");
	    			sql.append("  select  count(cs.student_id) shouldCount , tec.exam_id  from  tp_exam_class tec   "); 
	    			sql.append("  left JOIN class_student  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");
	    	sql.append("  ) a   ");
	    	sql.append("  left JOIN  (  ");
	    			sql.append("  select count(cs.student_id) alreadyCount, tec.exam_id   from  tp_exam_class tec  ");  
	    			sql.append("  left JOIN class_student  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.exam_id = tec.exam_id  and  ter.student_id = cs.student_id  ");
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");  
	    			sql.append("  and  ter.id  is not null  and  (ter.`status` =1 or  ter.`status` =2 )  "); 
	    	sql.append("  ) b  on  b.exam_id = a.exam_id  "); 
	    	sql.append("  left JOIN (  ");
	    			sql.append("  select  count(cs.student_id) completCount,tec.exam_id  from  tp_exam_class tec  ");  
	    			sql.append("  left JOIN class_student  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.exam_id = tec.exam_id  and  ter.student_id = cs.student_id  ");
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");  
	    			sql.append("  and  ter.id  is not null  and  ter.`status` =2  ");   
	    	sql.append("  ) c on  c.exam_id = b.exam_id  ");
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,获取考试人数统计对象(补考)
	     * @param examId  考试id
	     * @return
	     * @author zhengxianyin
	     */
	    public String findExamCountByExamIdBK(Integer examId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  IFNULL(a.shouldCount,0) shouldCount , IFNULL(b.alreadyCount,0) alreadyCount , IFNULL(c.completCount,0) completCount  ");
	    	sql.append("  from  (  ");
	    			sql.append("  select  count(cs.student_id) shouldCount , tec.exam_id  from  tp_exam_class tec   "); 
	    			sql.append("  left JOIN class_student_makeup  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");
	    	sql.append("  ) a   ");
	    	sql.append("  left JOIN  (  ");
	    			sql.append("  select count(cs.student_id) alreadyCount, tec.exam_id   from  tp_exam_class tec  ");  
	    			sql.append("  left JOIN class_student_makeup  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.exam_id = tec.exam_id  and  ter.student_id = cs.student_id  ");
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");  
	    			sql.append("  and  ter.id  is not null  and  (ter.`status` =1 or  ter.`status` =2 )  "); 
	    	sql.append("  ) b  on  b.exam_id = a.exam_id  "); 
	    	sql.append("  left JOIN (  ");
	    			sql.append("  select  count(cs.student_id) completCount,tec.exam_id  from  tp_exam_class tec  ");  
	    			sql.append("  left JOIN class_student_makeup  cs    on   cs.class_id   =  tec.sch_id  ");
	    			sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
	    			sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.exam_id = tec.exam_id  and  ter.student_id = cs.student_id  ");
	    			sql.append("  where  tec.exam_id = "+examId+"  and  cs.id  is not null  "); 
	    			sql.append("  and  s.state =1  ");  
	    			sql.append("  and  ter.id  is not null  and  ter.`status` =2  ");   
	    	sql.append("  ) c on  c.exam_id = b.exam_id  ");
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,班级id, 获取考试人数统计对象
	     * @param examId  考试id
	     * @param ClassId   班级id
	     * @return
	     * @author zhengxianyin
	     */
	    public String findExamCountByClassId(Integer examId,Integer classId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  IFNULL(a.shouldCount,0) shouldCount , IFNULL(b.alreadyCount,0) alreadyCount , IFNULL(c.completCount,0) completCount   from (  ");
					 sql.append("  select cs.class_id  ,IFNULL(count(cs.student_id),0) shouldCount  from  class_student  cs  ");  
					 sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					 sql.append("  where  cs.class_id = "+classId+"  and  s.state =1  ");
			sql.append("  )a   "); 
			sql.append("  LEFT JOIN  (  ");
				    sql.append("  select count(cs.student_id) alreadyCount,cs.class_id from class_student  cs  "); 
				    sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id  ");
					sql.append("  where cs.class_id ="+classId+"   and  s.state =1  ");  
					sql.append("  and  ter.exam_id =  "+examId+"  and  ter.id  is not null  and  (ter.`status` =1 or  ter.`status` =2 )  ");  
			sql.append("  ) b  on  a.class_id = b.class_id  ");
			sql.append("  LEFT JOIN (  ");
				    sql.append("  select  count(cs.student_id) completCount,cs.class_id from class_student  cs  "); 
					sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id  ");
					sql.append("  where cs.class_id ="+classId+"   and  s.state =1  ");  
					sql.append("  and  ter.exam_id =  "+examId+"  and  ter.id  is not null  and   ter.`status` =2  "); 
			sql.append("  )c  on   a.class_id = c.class_id  ");
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,班级id, 获取考试人数统计对象(补考)
	     * @param examId  考试id
	     * @param ClassId   班级id
	     * @return
	     * @author zhengxianyin
	     */
	    public String findExamCountByClassIdBK(Integer examId,Integer classId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  IFNULL(a.shouldCount,0) shouldCount , IFNULL(b.alreadyCount,0) alreadyCount , IFNULL(c.completCount,0) completCount   from (  ");
					 sql.append("  select cs.class_id  ,IFNULL(count(cs.student_id),0) shouldCount  from  class_student_makeup  cs  ");  
					 sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					 sql.append("  where  cs.class_id = "+classId+"  and  s.state =1  ");
			sql.append("  )a   "); 
			sql.append("  LEFT JOIN  (  ");
				    sql.append("  select count(cs.student_id) alreadyCount,cs.class_id from class_student_makeup  cs  "); 
				    sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id  ");
					sql.append("  where cs.class_id ="+classId+"   and  s.state =1  ");  
					sql.append("  and  ter.exam_id =  "+examId+"  and  ter.id  is not null  and  (ter.`status` =1 or  ter.`status` =2 )  ");  
			sql.append("  ) b  on  a.class_id = b.class_id  ");
			sql.append("  LEFT JOIN (  ");
				    sql.append("  select  count(cs.student_id) completCount,cs.class_id from class_student_makeup  cs  "); 
					sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  "); 
					sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id  ");
					sql.append("  where cs.class_id ="+classId+"   and  s.state =1  ");  
					sql.append("  and  ter.exam_id =  "+examId+"  and  ter.id  is not null  and   ter.`status` =2  "); 
			sql.append("  )c  on   a.class_id = c.class_id  ");
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,班级id, 获取该班级对应的全部学生列表(学号降序排列)
	     * @param classId
	     * @return
	     * @author zhengxianyin
	     */
	    public String findStudentList(Integer classId, Integer examId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  DISTINCT(s.id) ,s.student_code ,s.`name` ,cs.class_id classId, IFNULL(ter.`status`,0) examSta  from  class_student  cs  ");   
	    	sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  ");   
	    	sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id    and  ter.exam_id = "+examId+"  "); 
	    	sql.append("  where  cs.class_id = "+classId+"  and  s.state =1  ");  
	    	sql.append("  order by  s.student_code desc  ");   
	    	return sql.toString();
	    }
	    
	    /**
	     * 根据考试id,班级id, 获取该班级对应的全部学生列表(学号降序排列)(补考)	
	     * @param classId
	     * @return
	     * @author zhengxianyin
	     */
	    public String findStudentListBK(Integer classId, Integer examId){
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("  select  DISTINCT(s.id) ,s.student_code ,s.`name` ,cs.class_id classId, IFNULL(ter.`status`,0) examSta  from  class_student_makeup  cs  ");   
	    	sql.append("  LEFT JOIN student s  on  s.id = cs.student_id  ");   
	    	sql.append("  LEFT JOIN  te_exam_result  ter  on  ter.student_id = cs.student_id    and  ter.exam_id = "+examId+"  "); 
	    	sql.append("  where  cs.class_id = "+classId+"  and  s.state =1  ");  
	    	sql.append("  order by  s.student_code desc  ");   
	    	return sql.toString();
	    }
	    
	    //**************************监考统计功能（结束）*****************************************//
	    
	
	
}
