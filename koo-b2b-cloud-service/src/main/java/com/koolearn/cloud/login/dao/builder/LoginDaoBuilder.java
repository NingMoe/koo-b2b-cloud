package com.koolearn.cloud.login.dao.builder;

import java.sql.Connection;

import com.koolearn.framework.aries.annotation.SQLParam;
import org.apache.commons.lang.StringUtils;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

public class LoginDaoBuilder implements AriesDynamicSqlBuilder{
	
	public String updateUserEntity(Connection conn,UserEntity ue){
		StringBuffer sql = new StringBuffer();
		sql.append(" update user set update_time=now() ");
		if(StringUtils.isNotBlank(ue.getRealName())){
			sql.append(" ,real_name=:ue.realName ");
		}
		if(StringUtils.isNotBlank(ue.getStudentCode())){
			sql.append(" ,student_code=:ue.studentCode ");
		}
		if(StringUtils.isNotBlank(ue.getMobile())){
			sql.append(" ,mobile=:ue.mobile ");
		}
		if(StringUtils.isNotBlank(ue.getEmail())){
			sql.append(" ,email=:ue.email ");
		}
		if(ue.getSchoolId()!=null&&ue.getSchoolId()>0){
			sql.append(" ,school_id=:ue.schoolId ");
		}
		if(StringUtils.isNotBlank(ue.getSchoolName())){
			sql.append(" ,school_name=:ue.schoolName ");
		}
		if(ue.getProvinceId()!=null&&ue.getProvinceId()>0){
			sql.append(" ,province_id=:ue.provinceId ");
		}
		if(StringUtils.isNotBlank(ue.getProvinceName())){
			sql.append(" ,province_name=:ue.provinceName ");
		}
		if(ue.getCityId()!=null&&ue.getCityId()>0){
			sql.append(" ,city_id=:ue.cityId ");
		}
		if(StringUtils.isNotBlank(ue.getCityName())){
			sql.append(" ,city_name=:ue.cityName ");
		}
		if(ue.getCountyId()!=null&&ue.getCountyId()>0){
			sql.append(" ,county_id=:ue.countyId ");
		}
		if(StringUtils.isNotBlank(ue.getCountyName())){
			sql.append(" ,county_name=:ue.countyName ");
		}
		if(ue.getProcess()!=null&&ue.getProcess()>0){
			sql.append(" ,process=:ue.process ");
		}
		sql.append(" where id=").append(ue.getId());
		//System.out.println("updateUserEntity=="+sql.toString());
		return sql.toString();
	}

    /**
     * 同步koolearn用户信息到user表
     * @param conn
     * @param userId
     * @param mobile
     * @param email
     * @return
     */
    public String updateKoolearnUserToLocal(Connection conn, Integer userId,String mobile,String email){
        StringBuffer sb = new StringBuffer();
        sb.append( " update user set update_time = now() " );
        if( StringUtils.isNotBlank( mobile )){
            sb.append( " , mobile =:mobile ");
        }
        if( StringUtils.isNotBlank( email )){
            sb.append( " , email =:email ");
        }
        sb.append( " where id="+ userId );
        return sb.toString();
    }
}
