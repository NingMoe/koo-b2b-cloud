package com.koolearn.cloud.school.authoritySchool;

import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.authorityschool.AuthoritySchoolService;
import com.koolearn.cloud.school.authorityschool.vo.SchoolManagerDTO;
import com.koolearn.cloud.school.authoritySchool.dao.AuthoritySchoolDao;
import com.koolearn.cloud.school.common.SchoolCommonService;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.entity.SchoolPage;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.cloud.teacher.entity.RangeSubject;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/11/15.
 */
@Service("authoritySchoolService")
public class AuthoritySchoolServiceImpl implements AuthoritySchoolService {

    Logger log = Logger.getLogger( this.getClass() );

    @Autowired
    private AuthoritySchoolDao authoritySchoolDao;

    public AuthoritySchoolDao getAuthoritySchoolDao() {
        return authoritySchoolDao;
    }

    public void setAuthoritySchoolDao(AuthoritySchoolDao authoritySchoolDao) {
        this.authoritySchoolDao = authoritySchoolDao;
    }
    @Autowired
    private SchoolCommonService schoolCommonService;

    public SchoolCommonService getSchoolCommonService() {
        return schoolCommonService;
    }

    public void setSchoolCommonService(SchoolCommonService schoolCommonService) {
        this.schoolCommonService = schoolCommonService;
    }

    /**
     * 管理用户分页查询
     * @param schoolPage
     * @return
     */
    @Override
    public Map<String, Object> findSchoolMangePage(SchoolPage schoolPage) {
        Map<String , Object > map = new HashMap<String, Object>();
            try{
                int totalPage = authoritySchoolDao.findSchoolManageTotalPage( schoolPage.getCurrentPage() * schoolPage.getPageSize() , schoolPage.getPageSize() );
                schoolPage.setTotalLine( totalPage );
                map.put("totalPage", schoolPage.getTotalPage());
                map.put( "currentPage" , schoolPage.getCurrentPage() );
                if( 0 < totalPage ) {
                    List<Manager> managerList = authoritySchoolDao.findSchoolMangePage(schoolPage.getCurrentPage() * schoolPage.getPageSize(), schoolPage.getPageSize());
                    if(CollectionUtils.isNotEmpty( managerList )){
                        for( int i = 0 ; i < managerList.size();i++ ){
                            if( null != managerList.get( i ).getCreateTime() ){
                                managerList.get( i ).setCreateTimeStr(DateFormatUtils.dateLFormatForMini( managerList.get( i ).getCreateTime()));
                            }
                        }
                    }
                    map.put("dataList", managerList);
                }else{
                    map.put("dataList", "{}" );
                }
            }catch(Exception e ){
                log.error( "管理用户分页查询异常" +e.getMessage() , e);
                map.put( "dataList" , "{}" );
            }
        return map;
    }

    /**
     * 冻结解冻学校管理用户
     * @param type ( 1激活  2 冻结)
     */
    public void updateSchoolManageStatus(Integer managerId ,int type ){
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            if( 1 == type ){
                authoritySchoolDao.updateSchoolManageStatus( conn , managerId , CommonInstence.STATUS_1 );
            }else if( 2 == type ){
                authoritySchoolDao.updateSchoolManageStatus( conn , managerId , CommonInstence.STATUS_2 );
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( "冻结解冻学校管理用户异常 ,id:" + managerId  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 重置管理员密码
     * @param managerId
     */
    public void resetManagePassword(Integer managerId ,String password ){
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            //MD5加密
            authoritySchoolDao.resetManagePassword( conn , managerId , password );
            conn.commit();
        }catch (SQLException e) {
            log.error( "冻结解冻学校管理用户异常 ,id:" + managerId  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }
    /**
     * 获取当前学校的所有学段和年级及学科
     */
    public SchoolInfo findShoolInfo( Integer schoolId ){
        SchoolInfo schoolInfo = schoolCommonService.findAllSchoolRangeBySchoolId( schoolId ,null );
        if( null != schoolInfo ){
            //存储所有年级
            Map<String , Object > levelMap = new LinkedHashMap<String, Object>();
            //存储所有学科
            Map<Integer , Object > subjectMap = new LinkedHashMap<Integer, Object>();
            List<RangeSubject > rangeSubjectList = schoolInfo.getRangeList();
            //遍历所有学段
            for( int i = 0 ; i < rangeSubjectList.size();i++ ){
                RangeSubject rangeSubject = rangeSubjectList.get( i );
                String rangeName = rangeSubject.getRangeName();
                //获取当前学段下所有学科
                List< SubjectDto > subjectDtoList = rangeSubject.getSubjectDtoList();
                for( int j = 0 ; j < subjectDtoList.size();j++ ){
                    subjectMap.put( subjectDtoList.get( j ).getId() , subjectDtoList.get( j ).getName() );
                }
                String levelStr = "";
                if( CommonInstence.XIAOXUE.equals( rangeName ) ){//小学
                    levelStr = CommonEnum.getClassesLevelsEnum.getSource( CommonInstence.GRADE_TYPE_xx ).getValue();
                }else if( CommonInstence.CHUZHONG.equals( rangeName ) ){//初中
                    levelStr = CommonEnum.getClassesLevelsEnum.getSource( CommonInstence.GRADE_TYPE_cz ).getValue();
                }else if( CommonInstence.GAOZHONG.equals( rangeName ) ){//高中
                    levelStr = CommonEnum.getClassesLevelsEnum.getSource( CommonInstence.GRADE_TYPE_gz ).getValue();
                }
                String[] levelArray = levelStr.split( "," );
                for( String levelStr2 : levelArray ){
                    String[] levelDetailArray = levelStr2.split( ":" );
                    levelMap.put( levelDetailArray[ 0 ] , levelDetailArray[ 1 ] );
                }
            }
            schoolInfo.setLevelMap( levelMap );
            schoolInfo.setSubjectMap( subjectMap );
        }
        return schoolInfo;
    }

    /**
     * 创建学校管理用户
     * @param managerNew
     * @return
     */
    public int insertSchoolManager(Manager managerNew){
        Connection conn = null;
        int num=0;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = authoritySchoolDao.insertSchoolManager( conn , managerNew );
            conn.commit();
        }catch (SQLException e) {
            log.error( "创建学校管理用户异常 ,name:" + managerNew.getManagerName()  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            return num;
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 验证管理员邮箱是否可用
     * @param email
     * @return
     */
    public boolean checkManagerEmail( String email ){
        int num = authoritySchoolDao.findManagerEmailExist( email );
        if( num > 0 ){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 验证管理者手机
     * @param mobile
     * @return
     */
    public boolean checkManagerMobile( String mobile ){
        int num = authoritySchoolDao.findManagerMobileExist(mobile);
        if( num > 0 ){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 查询管理者信息用于更新
     * @param managerId
     * @return
     */
    public SchoolManagerDTO findManagerForUpdate(Integer managerId){
        return authoritySchoolDao.findManagerForUpdate( managerId );
    }
    /**
     * 更新学校的管理者信息
     * @param schoolManagerDto
     * @return
     */
    public int updateManagerInfo(SchoolManagerDTO schoolManagerDto){
        Connection conn = null;
        int num=0;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = authoritySchoolDao.updateManagerInfo(conn, schoolManagerDto.getManagerId(),
                    schoolManagerDto.getManagerName(),
                    schoolManagerDto.getManagerEmail(),
                    schoolManagerDto.getManagerMobile(),
                    schoolManagerDto.getRoleTypeId(),
                    schoolManagerDto.getSubjectIdStr(),
                    schoolManagerDto.getClassesLevelStr(),
                    schoolManagerDto.getUpdater());
            conn.commit();
        }catch (SQLException e) {
            log.error( "更新管理者信息异常 ,name:" + schoolManagerDto.getManagerName()  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            return num;
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }






}
