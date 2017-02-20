package com.koolearn.cloud.school.schooclasses.dao.builder;

import com.koolearn.cloud.school.dto.ClassPageDto;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

/**
 * Created by fn on 2016/10/31.
 */
public class ClassesManageDaoBuilder implements AriesDynamicSqlBuilder {
    //private Logger log = Logger.getLogger(this.getClass());


    public String findClassesPageLine(ClassPageDto classPageDto){
        StringBuffer sb = new StringBuffer();
        sb.append( " select count( 1 ) " );
        sb.append( " from classes  " );
        sb.append( " where 1 = 1 and type != 3 " );
        if( null != classPageDto ){
            //若不是全部学段
            if( classPageDto.getRangeName()!= null && !classPageDto.getRangeName().equals( "0") ){//学段名称
                sb.append( " and range_name ='" + classPageDto.getRangeName() +"'");
            }
            if(classPageDto.getClassesLevel() !=null && classPageDto.getClassesLevel() != 0 ){//年级（ 0 标识全部 ）
                sb.append( " and level =" + classPageDto.getClassesLevel() );
            }
            if( classPageDto.getStatus() != null ){
                if( classPageDto.getStatus() == 1 ) { //选择已毕业的班级
                    sb.append( " and graduate = " + classPageDto.getStatus() );
                }else{
                    sb.append( " and status = " + classPageDto.getStatus() );//班级状态
                }
            }
            if( classPageDto.getType() != null && classPageDto.getType() != 10 ){//班级类型( 10 标识全部 )
                sb.append( " and type = " + classPageDto.getType()  );
            }
            if( classPageDto.getSchoolId() != null ){
                sb.append( " and school_id ="+classPageDto.getSchoolId() );
            }
        }
        //System.out.println( "班级分页sql:" +sb.toString() );
        return sb.toString();
    }

    /**
     * 分页查询学校班级信息
     * @param classPageDto
     * @return
     */
    public String findClassesPageInfo(ClassPageDto classPageDto){
        StringBuffer sb = new StringBuffer();
        sb.append( " select id, class_name ,range_name ,level ,year ,class_code ,type ,subject_name ,status , graduate " );
        sb.append( " from classes " );
        sb.append( " where 1 = 1 and type != 3" );
        if( null != classPageDto ){
            //若不是全部学段
            if( classPageDto.getRangeName()!= null && !classPageDto.getRangeName().equals( "0") ){//学段名称
                sb.append( " and range_name ='" + classPageDto.getRangeName() +"'" );
            }
            if(classPageDto.getClassesLevel() !=null && classPageDto.getClassesLevel() != 0 ){//年级（ 0 标识全部 ）
                sb.append( " and level =" + classPageDto.getClassesLevel() );
            }
            if( classPageDto.getStatus() != null ){
                sb.append( " and status = " + classPageDto.getStatus() );//班级状态
            }
            if( classPageDto.getType() != null && classPageDto.getType() != 10 ){//班级类型( 10 标识全部 )
                sb.append( " and type = " + classPageDto.getType()  );
            }
            if( classPageDto.getSchoolId() != null ){
                sb.append( " and school_id ="+classPageDto.getSchoolId() );//学校
            }
            if( classPageDto.getCurrentPage() != null && null!= classPageDto.getPageSize() ) {
                sb.append(" order by create_time desc limit " + classPageDto.getCurrentPage() * classPageDto.getPageSize() + "," + classPageDto.getPageSize());
            }
        }
        //System.out.println( "班级分页sql:" +sb.toString() );
        return sb.toString();

    }

}
