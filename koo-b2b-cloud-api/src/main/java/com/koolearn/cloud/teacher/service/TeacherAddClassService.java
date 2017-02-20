package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.dictionary.entity.Dictionary;

import java.util.List;

/**
 * Created by fn on 2016/4/5.
 */
public interface TeacherAddClassService {
    /**
     * 根据学段id和学科id查询对应的所有班级
     * @param schoolId : 学校id
     * @param rangeId ：学段id
     * @param subjectId
     * @return
     */
    List<Classes> findAllClassByRangeSub(int schoolId , Integer rangeId, Integer subjectId,int teacherId);

    /**
     * 查询所有学科
     * @return
     */
    List<Dictionary> findAllSubject();

    /**
     * 创建班级
     * @param classes
     * @return
     */
    int checkAndInsertClassInfo(Classes classes , String realName );

    /**
     * 查询老师名字
     * @param teacherId
     * @return
     */
    User findTeacherNameById(int teacherId);

    /**
     * 生成编辑编码（四位字母）
     * @return
     */
    String makeClassCode( List< String > listCode );

    /**
     * 查询系统内所有班级编码
     * @return
     */
    public List< String > findClassesCode();
    /**
     * 查询某学校内所有编辑编码
     * @return
     */
    public List< String > findClassesCode( int schoolId );
    /**
     * 根据班级id查询班级信息
     * @param classesId
     * @return
     */
    Classes findClassesById(int classesId);

    /**
     * 拼装班级老师数据
     * @param classes
     * @return
     */
    public ClassesTeacher makeClassesTeacher( Classes classes );

    /**
     * 感觉班级id查询老师
     * @param classesId
     * @return
     */
    List<ClassesTeacher>  findClassesTeacherByClassesId( Integer classesId ,String rangeName );

    /**
     * 验证并拼装班级对象
     * @param className
     * @param year
     * @param type
     * @param rangeId
     * @param rangeName
     * @param subjectId
     *; @param subjectName
     * @return
     */
    Classes checkInfo(String className ,String year ,String type ,String  rangeId,String rangeName,
                      String subjectId,String subjectName );



    /**
     * 查询班级人数
     * @param classesIdInt
     * @return
     */
    int findClassesNum( int classesIdInt);

    /**
     * 查询老师所有的学科信息
     * @param teacherId
     * @return
     */
    List<TeacherBookVersion> findTeacherBookVersion(int teacherId , int status );
    /**
     * 查询学科下面的所有小组和学生
     * @param subjectId
     * @return
     */
    List<Team> findAllTeamStudentsBySubject(int subjectId ,int rangeId ,int classesId ,int schoolId ,int teacherId );

    /**
     * 查询老师名下所有学科
     * @param teacherId
     * @return
     */
    List<TeacherBookVersion> findAllSubjectByTeacher(int teacherId);

    /**
     * 根据学科查询学段id
     * @param subjectId
     * @param rangeType
     * @return
     */
    int findRangeIdBySubjectId(Integer subjectId , String rangeType );

    /**
     * 按入学年份和学科学段查询所有班级
     * @param year
     * @param rangeId
     * @param subjectId
     * @param teacherId
     * @return
     */
    List<Classes> findAllClassesByYearAndSubRange(int schoolId , Integer year, Integer rangeId, Integer subjectId,int teacherId);

    /**
     * 删除班级
     * @param classesId
     * @param teacherId
     * @return
     */
    int deleteClasses(Integer classesId, int teacherId);

    /**
     * 插入老师班级表
     * @param classesTeacher
     */
    int insertClassesTeacher(ClassesTeacher classesTeacher , Integer teacherId , Classes classes ,String dynamicInfo ,boolean isDynamic );

    /**
     * 组装动态表的数据
     * @param teacherId
     * @param classesId
     * @param dynamicInfo
     * @return
     */
    ClassesDynamic makeClassesDynamic( int teacherId ,Integer classesId ,String dynamicInfo);
    /**
     * 查询某学段下老师的所有学科
     * @return
     */
    List<TeacherBookVersion> findTeacherBookVersionByRangeIdAndSubjectId(int teacherId, int rangeId ,int subjectId  , int status );

    /**
     * 查询老师的某学段下的所有学科
     * @param teacherId
     * @param status
     * @return
     */
    List<TeacherBookVersion> findTeacherBookVersionByRangeId(int teacherId  , int status);

    /**
     * 通过学段名称查询学科列表
     * @param teacherId
     * @param rangeName
     * @param status
     * @return
     */
    List<TeacherBookVersion> findTeacherBookVersionByRangeName(int teacherId , String rangeName , int status);

    /**
     * 从缓存获取班级
     * @param classesId
     * @return
     */
    Classes getClassesFromCache(int classesId );
    /**
     * 查询老师当前学段下的所有学科
     * @param teacherId
     * @param rangeName
     * @return
     */
    List< TeacherBookVersion> findTeacherSubjects(int teacherId , String rangeName);

   /**
     * 班级统一升级
     * @return
     */
    int upgradeClasses( int schoolId , int status , int graduate );

    int upgradeClassesBySchoolId( int status , int graduate ,int schoolId ,int outTime  );
    /**
     * 老师加入班级产生的班级动态，入库
     * @param classesDynamic
     */
    int insertClassesDynamic(ClassesDynamic classesDynamic);
    /**
     * 将班级存入缓存
     * @param key
     * @param classes
     */
    boolean addClassesToCache( String key , Classes classes );

    /**
     * 从缓存中获取班级对象
     * @param key
     * @return
     */
    public Classes getClassesFromCache( String mapKey , String key );

    /**
     * 从缓存删除班级
     * @param mapKey
     * @param fieldkey
     */
    public void deleteClassesFromCache( String mapKey , String fieldkey );
}
