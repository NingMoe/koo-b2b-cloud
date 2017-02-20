package com.koolearn.cloud.school.authoritySchool.dao;

import com.koolearn.cloud.school.authorityschool.vo.SchoolManagerDTO;
import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/11/15.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface AuthoritySchoolDao {

    /**
     * 学校端管理用户分页的总行数
     * @param begin
     * @param end
     * @return
     */
    @SQL( " select count( 1 ) from tb_school_manager a inner join tb_school_role b on a.role_type_id = b.type where a.status != 3" )
    int findSchoolManageTotalPage( @SQLParam( "begin" ) int begin , @SQLParam( "end" ) Integer end );

    /**
     * 学校端管理用户分页内容
     * @param begin
     * @param end
     * @return
     */
    @SQL( " select a.id , a.school_id ,a.manager_name , a.manager_email,a.manager_mobile ,a.status ,b.name as roleName ,a.creator ,a.create_time " +
          " from tb_school_manager a inner join tb_school_role b on a.role_type_id = b.type " +
          " where a.status != 3 order by a.create_time desc limit :begin , :end " )
    List<Manager> findSchoolMangePage( @SQLParam( "begin" )int begin , @SQLParam( "end" )Integer end  );

    /**
     * 冻结，解冻学校管理者账号
     * @param managerId
     * @param status
     */
    @SQL( " update tb_school_manager set status=:status where id=:managerId " )
    void updateSchoolManageStatus( Connection conn ,@SQLParam( "managerId" )Integer managerId , @SQLParam( "status" )int status);

    /**
     * 密码重置
     * @param managerId
     * @param password
     */
    @SQL( " update tb_school_manager set pass_word=:password  where id=:managerId  " )
    void resetManagePassword(Connection conn , @SQLParam( "managerId" )Integer managerId , @SQLParam( "password" ) String password);

    /**
     * 创建学校管理用户
     * @param manager
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int insertSchoolManager(Connection conn , Manager manager );
    /**
     * 验证管理员邮箱是否可用
     * @param email
     * @return
     */
    @SQL( " select count( 1 ) from tb_school_manager where status != 3 and manager_email=:email " )
    int findManagerEmailExist(@SQLParam( "email" )String email);
    /**
     * 验证管理员邮箱是否可用
     * @param mobile
     * @return
     */
    @SQL( " select count( 1 ) from tb_school_manager where status != 3 and manager_mobile=:mobile " )
    int findManagerMobileExist(@SQLParam( "mobile" )String mobile);

    /**
     * 根据管理者主键查询其信息
     * @param managerId
     * @return
     */
    @SQL( " select id as managerId, school_id as schoolId , manager_name as managerName ,manager_mobile as managerMobile ,manager_email as managerEmail ," +
          " role_type_id as roleTypeId ,subject_ids as subjectIdStr , classes_levels as classesLevelStr ,version " +
          " from tb_school_manager where id=:managerId and status !=3 " )
    SchoolManagerDTO findManagerForUpdate(@SQLParam( "managerId" )Integer managerId);
    /**
     * 更新学校管理者信息
     * @param managerName
     * @return
     */
    @SQL( " update tb_school_manager set manager_name=:managerName ,manager_mobile=:managerMobile ,manager_email=:managerEmail ," +
          " role_type_id=:roleTypeId ,subject_ids=:subjectIdStr ,classes_levels=:classesLevelStr ,updater=:updater ,update_time=now() " +
          " where id=:managerId " )
    int updateManagerInfo(Connection conn,@SQLParam( "managerId" )Integer managerId, @SQLParam( "managerName" )String managerName,
                          @SQLParam( "managerEmail" )String managerEmail, @SQLParam( "managerMobile" )String managerMobile,
                          @SQLParam( "roleTypeId" )Integer roleTypeId, @SQLParam( "subjectIdStr" )String subjectIdStr,@SQLParam( "classesLevelStr" ) String classesLevelStr, @SQLParam( "updater" )String updater);

}
