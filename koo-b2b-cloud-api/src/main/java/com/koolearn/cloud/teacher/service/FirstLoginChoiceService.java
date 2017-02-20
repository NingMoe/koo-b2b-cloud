package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.exam.base.service.BaseService;
import com.koolearn.klb.tags.entity.Tags;

import java.util.List;

/**
 * 教师第一次登陆选择科目
 * Created by fangnan on 2016/3/31.
 */
public interface FirstLoginChoiceService extends BaseService {
    /**
     * 根据学科和学段ID查询所有教材
     * @param subjectId
     * @param rangeId
     * @
     */
    List<Tags> findKeMuList(int subjectId, int rangeId);


    int findTeacherSubjectNum(int teacherId );

    /**
     * 验证学科参数合法性及拼装teacher_book_version对象
     * @param rangeId
     * @param rangeName
     * @param subjectId
     * @param subjectName
     * @return
     */
    TeacherBookVersion checkAndMakeTeacherVersion(String rangeId,
                                                  String rangeName,
                                                  String subjectId,
                                                  String subjectName,
                                                  String bookVersionId,
                                                  String booVersionName);

    /**
     * 添加老师的学科和学段
     * @param teacher
     */
    TeacherBookVersion addTeacherRangeAndJiaoCai(TeacherBookVersion teacher ,int teacherId) throws Exception;

    List<TeacherBookVersion> findTeacherSubject(int teacherId);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    User findUserInfo(int userId );

    /**
     * 查询老师教材版本关联表
     * @param teacherBookVersionId
     * @return
     */
    TeacherBookVersion  findTeacherBookVersionById(int teacherBookVersionId );

    /**
     * 添加老师姓名
     * @param teacherId
     * @return
     */
    int addTeacherRealName(int teacherId ,String realName );
}
