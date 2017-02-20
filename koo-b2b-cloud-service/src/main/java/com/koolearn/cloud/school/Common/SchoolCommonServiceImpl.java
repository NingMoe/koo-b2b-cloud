package com.koolearn.cloud.school.Common;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.school.Common.dao.SchoolCommonDao;
import com.koolearn.cloud.school.common.SchoolCommonService;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.common.entity.TbErrorLog;
import com.koolearn.cloud.school.entity.TbSchoolUp;
import com.koolearn.cloud.school.entity.dto.ClassesDto;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.cloud.teacher.entity.RangeSubject;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DateFormatUtils;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 学校端公共服务（ 1.学校下所有学段和班级
 *                  2.
 * ）
 * Created by fn on 2016/10/31.
 */
public class SchoolCommonServiceImpl implements SchoolCommonService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchoolCommonDao schoolCommonDao;



    /**
     * 当前用户下的所有班级
     * @param schoolId
     * @param userId
     * @return
     */
    @Override
    public SchoolInfo findAllSchoolBySchoolId(Integer schoolId, Integer userId) {
        SchoolInfo schoolInfo = new SchoolInfo();
        List<RangeSubject > rangeSubjectList = new ArrayList<RangeSubject>();

        //查询当前学校下所有学段
        String rangeType = schoolCommonDao.findAllRangeBySchoolId( schoolId );
        if(StringUtils.isNotEmpty( rangeType )){
            String[] rangeArray = rangeType.split( "," );
            if( null != rangeArray ){
                //获取学段id对应的学段名称
                for( String range : rangeArray ){
                    Integer rangeId = new Integer( range );
                    String rangeName = CommonEnum.getRangeNameEnum.getSource( rangeId ).getValue();
                    RangeSubject rangeSubject = new RangeSubject();
                    rangeSubject.setRangeId( rangeId );
                    rangeSubject.setRangeName( rangeName );
                    //获取所有班级 :1:一年级,2:二年级,3:三年级,4:四年级,5:五年级,6:六年级
                    String classes = CommonEnum.getClassesLevelsEnum.getSource( rangeId ).getValue();
                    if( StringUtils.isNoneEmpty( classes )){
                        List< ClassesDto > classesDtosList = new LinkedList<ClassesDto>();
                        String[] classLevelsArray = classes.split( "," );
                        if( null != classLevelsArray ){
                            for( String classIdAndName : classLevelsArray ){
                                ClassesDto classesDto = new ClassesDto();
                                if( null != classIdAndName ){
                                    String[] idNameArray = classIdAndName.split(":");
                                    for( int i = 0 ; i < idNameArray.length;i++ ){
                                        //存储班级id和名称
                                        classesDto.setId( new Integer( idNameArray[ 0 ] ) );
                                        classesDto.setName( idNameArray[ 1 ] );
                                    }
                                }
                                classesDtosList.add( classesDto );
                            }
                        }
                        rangeSubject.setClassesDtosList(classesDtosList);
                        rangeSubjectList.add( rangeSubject );
                    }
                }
            }
        }
        schoolInfo.setStatus( 0 );
        schoolInfo.setRangeList( rangeSubjectList );
        //schoolInfo.setClassesMap( classesMap );
        //获取当前用户的权限班级
        return schoolInfo;
    }

    /**
     * 查询学校升级信息
     * @param schoolId
     * @return
     */
    public TbSchoolUp findSchoolUpInfo(Integer schoolId){
        return schoolCommonDao.findSchoolUpInfo( schoolId );
    }
    /**
     * 查询班级的毕业日期
     * @param schoolId
     * @param classes
     * @return
     */
    public String findClassFinishDay(Integer schoolId, Classes classes ){
        String finishDay = "";
        TbSchoolUp schoolUp = findSchoolUpInfo( schoolId );
        if( null != schoolUp ){
            String finishClasses = schoolUp.getClassGraduateLevel();
            //如果当前班级的Level包含在学校毕业设置的年级（level）集合中则可取该学校设置的毕业时间
            if( null !=finishClasses && finishClasses.indexOf( classes.getLevel() +"" ) != -1 ){
                finishDay = DateFormatUtils.getDay( schoolUp.getAutoGraduateTime());
            }
        }
        return finishDay;
    }

    /**
     * 查询所有学科
     */
    public List<SubjectDto> findAllSubject(){
        return schoolCommonDao.findAllSubject();
    }


    /**
     * 查询学校下所有学段
     * @param schoolId
     * @param userId
     * @return
     */
    public SchoolInfo findAllSchoolRangeBySchoolId(Integer schoolId, Integer userId){
        SchoolInfo schoolInfo = new SchoolInfo();
        //存放所有学段
        List<RangeSubject > rangeList = new ArrayList<RangeSubject>();
        List< SubjectDto > dbSubjectDto = schoolCommonDao.findAllSubject();
        //查询当前学校下所有学段
        String rangeType = schoolCommonDao.findAllRangeBySchoolId( schoolId );
        if(StringUtils.isNoneEmpty( rangeType )) {
            String[] rangeArray = rangeType.split(",");
            if (null != rangeArray) {
                //获取学段id对应的学段名称
                for (String range : rangeArray) {
                    Integer rangeId = new Integer(range);
                    String rangeName = CommonEnum.getRangeNameEnum.getSource(rangeId).getValue();
                    RangeSubject rangeSubject = new RangeSubject();
                    rangeSubject.setRangeId(rangeId);
                    rangeSubject.setRangeName(rangeName);
                    //存放所有学科
                    List<SubjectDto> subjectDtoList = new ArrayList<SubjectDto>();
                    for( int i = 0 ; i < dbSubjectDto.size();i++ ){
                        List<Tags> tagsList = KlbTagsUtil.getInstance().getTagById( dbSubjectDto.get( i ).getId());
                        if( null != tagsList ){
                            int hasNum = 0;
                            for( int j = 0 ; j < tagsList.size();j++ ){
                                if( tagsList.get( j ).getName().equals( rangeName )){
                                    hasNum++;
                                }
                            }
                            if( hasNum > 0 ){
                                SubjectDto subjectDto = new SubjectDto();
                                subjectDto.setId( dbSubjectDto.get( i ).getId() );
                                subjectDto.setName( dbSubjectDto.get( i ).getName() );
                                subjectDtoList.add( subjectDto );
                            }
                        }
                    }
                    rangeSubject.setSubjectDtoList( subjectDtoList );
                    rangeList.add( rangeSubject );
                }
            }
        }
        schoolInfo.setRangeList( rangeList );
        //schoolInfo.setSubjectDtoList( dbSubjectDto );
        return schoolInfo;
    }

    /**
     * 接口异常信息记录
     * @param interfaceName
     * @param logInfo
     */
    public void insertErrorLog( String interfaceName , String logInfo ,String remark ){
        TbErrorLog tbErrorLog = new TbErrorLog();
        tbErrorLog.setCreateTime( new Date());
        tbErrorLog.setInterfaceName( interfaceName );
        tbErrorLog.setLogInfo( logInfo );
        tbErrorLog.setRemark( remark );
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            schoolCommonDao.insertErrorLog( conn , tbErrorLog );
            conn.commit();
        }catch (SQLException e) {
            log.error( "记录异常日志异常"  + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
        } catch (Exception e) {
            log.error( "记录异常日志异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }









}
