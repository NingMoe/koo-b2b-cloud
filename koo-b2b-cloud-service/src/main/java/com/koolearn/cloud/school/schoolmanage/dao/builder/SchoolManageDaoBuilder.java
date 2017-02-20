package com.koolearn.cloud.school.schoolmanage.dao.builder;

import com.koolearn.cloud.school.schoolmanage.vo.SchoolPowerDto;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by fn on 2016/11/22.
 */
public class SchoolManageDaoBuilder implements AriesDynamicSqlBuilder {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 更新学校扩展信息
     * @param schoolPowerDto
     * @return
     */
    public String updateSchoolExtendInfo(SchoolPowerDto schoolPowerDto){
        StringBuffer sb = new StringBuffer( );
        sb.append( " update tb_school_extend set update_time=now() " );
        if(StringUtils.isNotEmpty( schoolPowerDto.getUpdater() ) ){
            sb.append( " , updater = '" + schoolPowerDto.getUpdater() + "'" );
        }
        //联系人
        if( StringUtils.isNotEmpty( schoolPowerDto.getContacter() )){
            sb.append( " , contacter ='" + schoolPowerDto.getContacter() + "'" );
        }
        //联系人邮箱
        if( StringUtils.isNotEmpty( schoolPowerDto.getContacterMail() )){
            sb.append( " , contacter_mail = '" + schoolPowerDto.getContacterMail() + "'" );
        }
        //联系人手机
        if( StringUtils.isNotEmpty( schoolPowerDto.getContacterMobile() )){
            sb.append( " , contacter_mobile = '" + schoolPowerDto.getContacterMobile() + "'" );
        }

        log.info( "更新学校扩展信息SQL:" + sb.toString() );
        return sb.toString();
    }

}
