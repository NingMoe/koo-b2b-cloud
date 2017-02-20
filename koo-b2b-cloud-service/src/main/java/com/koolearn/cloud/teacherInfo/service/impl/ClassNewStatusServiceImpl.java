package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.ClassesDynamic;
import com.koolearn.cloud.common.entity.ClassesDynamicTeacher;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.teacher.service.ClassNewStatusService;
import com.koolearn.cloud.teacherInfo.dao.ClassNewStatusDao;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by fn on 2016/4/13.
 */
@Service("classNewStatusService")
public class ClassNewStatusServiceImpl implements ClassNewStatusService {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ClassNewStatusDao classNewStatusDao;

    public ClassNewStatusDao getClassNewStatusDao() {
        return classNewStatusDao;
    }

    public void setClassNewStatusDao(ClassNewStatusDao classNewStatusDao) {
        this.classNewStatusDao = classNewStatusDao;
    }

    /**
     * 查询班级动态
     * @param classesId
     * @return
     */
    @Override
    public Pager findClassesDynamicByClassId(Integer classesId ,int pageSize , int pageNo ) {
        Pager pagerBean = new Pager();
        int pageLine = 0 ;
       // ListPager listPager = new ListPager();
        pagerBean.setPageSize( pageSize );
        pagerBean.setPageNo(pageNo);
        pageLine = classNewStatusDao.findClassesDynamicLine( classesId );
        pagerBean.setTotalRows( pageLine );
        if( pageLine > 0 ){
            List<ClassesDynamic> list = classNewStatusDao.findClassesDynamicByClassId( classesId  , pageNo*pageSize , pageSize);
            if( null != list && list.size() > 0 ){
                for( int i = 0 ;i < list.size();i++ ){
                    if( list.get( i ).getCreateTime() != null ){
                        list.get( i ).setCreateTimeStr( DateFormatUtils.dateLFormat( list.get( i ).getCreateTime() ) );
                    }
                }
            }
            pagerBean.setResultList( list );
        }
        return pagerBean;
    }

    /**
     * 更新老师下关于当前班级的所有动态状态
     * @param classesId
     * @param teacherId
     */
    @Override
    public void updateTeacherDynamicStatus(Integer classesId , Integer teacherId ) throws Exception {
        List< ClassesDynamic> list = classNewStatusDao.findNoReadDynamic( classesId , CommonInstence.STATUS_0 );
        if( null != list && list.size() > 0 ){
            try{
                for( int i = 0 ; i< list.size();i++ ){
                    List<ClassesDynamicTeacher > dynamicTeacherList = classNewStatusDao.findClassesDynamicTeacherById( list.get(i).getId() , CommonInstence.STATUS_0 );
                    if( null == dynamicTeacherList || dynamicTeacherList.size()  == 0 ){
                        // 如果没有未读的动态，则更新班级动态的status状态为1（ 全部已读 ）

                        updateClassesDynamicStatus( list.get( i ));
                    }else{
                        int readNum = 0 ;
                        int selfReadNum = 0 ;
                        for( int j = 0 ; j < dynamicTeacherList.size();j++ ){
                            if( dynamicTeacherList.get( j ).getTeacherId().intValue() == teacherId.intValue() &&
                                    dynamicTeacherList.get( j ).getStatus() == 0 ){
                                selfReadNum++;
                                //更新班级动态老师的状态为已读
                                updateClassesDynamicTeacherStatus( teacherId , list.get( i ).getId() );
                                //如果只有一个老师，或者其他老师都为已读状态则更新主表的已读状态
                                if( dynamicTeacherList.size() == 1 ){
                                    updateClassesDynamicStatus( list.get( i ));
                                }
                            }
                            if( dynamicTeacherList.get( j ).getTeacherId().intValue() != teacherId.intValue() &&
                                    dynamicTeacherList.get( j ).getStatus() == 1 ){
                                readNum++;
                            }
                            //如果其他的老师都已经读过则更新主表状态
                            if( readNum == dynamicTeacherList.size() - 1 &&selfReadNum== 1 ){
                                updateClassesDynamicStatus( list.get( i ));
                            }
                        }
                    }
                }
            }catch( Exception e ){
                log.error( "更新老师下关于当前班级的所有动态状态异常" + e.getMessage() , e );
                throw new Exception("更新老师下关于当前班级的所有动态状态异常"  + e.getMessage());
            }

        }


    }

    /**
     * 更新班级动态的status状态为1（ 全部已读 ）
     * @param classesDynamic
     * @return
     */
    public int updateClassesDynamicStatus( ClassesDynamic classesDynamic) throws Exception {
        int num =0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = classNewStatusDao.updateClassesDynamicStatus( conn , classesDynamic.getId() , CommonInstence.STATUS_1 );
            conn.commit();
        }catch (SQLException e) {
            log.error( " 更新班级动态的状态异常 :"    + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new Exception( "更新班级动态的状态异常 " + e.getMessage() , e );
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }

    public int updateClassesDynamicTeacherStatus( int teacherId , int classesDynamicId ) throws Exception {
        int num =0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = classNewStatusDao.updateClassesDynamicTeacherStatus( conn , teacherId ,classesDynamicId  , CommonInstence.STATUS_1 );
            conn.commit();
        }catch (SQLException e) {
            log.error( " 更新班级动态老师的状态异常 :"    + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new Exception( "更新班级动态老师的状态异常 " + e.getMessage() , e );
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }


}
