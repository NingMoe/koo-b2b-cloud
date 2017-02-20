package com.koolearn.cloud.util;

import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;

import java.util.Date;

/**
 * Created by fn on 2016/5/11.
 */
public class TeacherCommonUtils {

    /**
     * 组装班级名称
     * @param classesName ：班级名称
     * @param yearObj        ：入学年份
     * @param rangeName   ：学段名称
     * @return
     */
    public static String getClassesName(String classesName , Integer yearObj , String rangeName ,int outTime ){
        StringBuffer sb = new StringBuffer();
        //当前年
        int nowYear = DateFormatUtils.getYear( new Date( ));
        //当前月
        int nowMouthDay = DateFormatUtils.getMouthDay();
        int num = nowYear - ( yearObj != null ? yearObj.intValue() : nowYear);
        if( nowMouthDay <= ( outTime != 0 ? outTime :  CommonInstence.UPTIME_815 ) ){
            sb.append( getRangeName( rangeName , num ) );
        }else{
            sb.append( getRangeName( rangeName , num + 1 ) );
        }
        sb.append( classesName );
        return sb.toString();
    }
    /**
     * 学段拼年级
     * @param rangeName
     * @param year
     * @return
     */
    public static String getRangeName(String rangeName , int year){
        String range = CommonEnum.RangeTypeEnum.getSource( rangeName ).getValue();

        String number =getYearWord( rangeName , year );
        if( "年级".equals( range )){
            return number + range;
        }else{
            return range + number;
        }
    }

    public static String getYearWord( String rangeName , int year ){
        if( "小学".equals( rangeName )){
            if( year == 1 ){
                return "一";
            }else if( year == 2 ){
                return "二";
            }else if( year == 3 ){
                return "三";
            }else if( year == 4 ){
                return "四";
            }else if( year == 5 ){
                return "五";
            }else if( year == 6 ){
                return "六";
            }else if( year == 0 ){
                return "一";
            } else{
                return "六";
            }
        }else if( "初中".equals( rangeName )){
            if( year == 1 ){
                return "一";
            }else if( year == 2 ){
                return "二";
            }else if( year == 3 ){
                return "三";
            } else if( year == 0 ){
                return "一";
            }else{
                return "三";
            }
        }else{
            if( year == 0 ){
                return "一";
            }else if( year == 1 ){
                return "一";
            }else if( year == 2 ){
                return "二";
            }else if( year == 3 ){
                return "三";
            } else{
                return "三";
            }
        }
    }

}
