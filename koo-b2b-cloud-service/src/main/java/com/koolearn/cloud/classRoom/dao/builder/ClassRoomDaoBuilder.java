package com.koolearn.cloud.classRoom.dao.builder;

import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import com.koolearn.framework.common.page.ListPager;

public class ClassRoomDaoBuilder implements AriesDynamicSqlBuilder {

    private static final int PAGE_SIZE = 12;

    public String findTeacherClassRoomPageCount(int subjectId, String searchValue, int userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from tp_exam where teacher_id = :userId and type = 2 and status <> 3 ");
        if (-1 != subjectId) {
            sql.append(" and subject_id = :subjectId ");
        }
        if (searchValue != null && !"".equals(searchValue.trim())) {
            sql.append(" and exam_name like :searchValue ");
        }
        return sql.toString();
    }

    public String findTeacherClassRoomPage(int subjectId, String searchValue, int userId, int pageNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select *,if(end_time < sysdate(),0,1) as finishStatus from tp_exam where teacher_id = :userId and type = 2 and status <> 3 ");
        if (-1 != subjectId) {
            sql.append(" and subject_id = :subjectId ");
        }
        if (searchValue != null && !"".equals(searchValue.trim())) {
            sql.append(" and exam_name like :searchValue ");
        }
        sql.append(" order by create_time desc");
        if (pageNo == 0) {
            sql.append(" limit 0 , ").append(PAGE_SIZE - 1);
        } else {
            sql.append(" limit ").append(PAGE_SIZE * pageNo - 1).append(" , ").append(PAGE_SIZE);
        }
        return sql.toString();
    }


    public String findStudentClassRoomCount(int studentId, int subjectId, String searchValue, String endTimeStr) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count( distinct t.id) from tp_exam_student ts ,tp_exam t " +
                "where ts.student_id = :studentId and ts.exam_id = t.id and ts.subject_id = :subjectId and ts.subject_id = t.subject_id " +
                "and ts.status=1 and t.status = 4 and t.start_time < sysdate() and t.type = 2 ");
        if (searchValue != null && !"".equals(searchValue.trim())) {
            sql.append(" and t.exam_name like :searchValue ");
        }
        if (endTimeStr != null && !"".equals(endTimeStr.trim())) {
            sql.append(" and t.end_time < date_format(:endTimeStr, '%Y-%m-%d %H:%i:%s') ");
        }
        return sql.toString();
    }

    public String findStudentClassRoom(ListPager listPager, int studentId, int subjectId, String searchValue, String endTimeStr) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct t.*,if(t.end_time < sysdate(),0,1) as finishStatus from tp_exam_student ts ,tp_exam t " +
                "where ts.student_id = :studentId and ts.exam_id = t.id and ts.subject_id = :subjectId and ts.subject_id = t.subject_id " +
                "and ts.status = 1 and t.status = 4 and t.start_time < sysdate() and t.type = 2 ");
        if (searchValue != null && !"".equals(searchValue.trim())) {
            sql.append(" and t.exam_name like :searchValue ");
        }
        if (endTimeStr != null && !"".equals(endTimeStr.trim())) {
            sql.append(" and t.end_time < date_format(:endTimeStr, '%Y-%m-%d %H:%i:%s') ");
        }
        sql.append(" order by t.end_time desc, t.id desc");
        return sql.toString();
    }

}
