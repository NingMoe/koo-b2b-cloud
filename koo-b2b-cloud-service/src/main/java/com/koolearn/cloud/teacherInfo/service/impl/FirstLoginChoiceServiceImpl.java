package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.teacher.service.FirstLoginChoiceService;
import com.koolearn.cloud.teacherInfo.dao.FirstLoginChoiceDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.klb.tags.entity.Tags;
import com.koolearn.klb.tags.entity.TagsDesc;
import com.koolearn.klb.tags.service.TagsDescService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by fn on 2016/3/31.
 */
public class FirstLoginChoiceServiceImpl implements FirstLoginChoiceService {
    private Logger log = Logger.getLogger(this.getClass());

   
    @Autowired
    private TagsDescService tagsDescService;

    private FirstLoginChoiceDao firstLoginChoiceDao;
    private TeacherDataMaintainDao teacherDataMaintainDao;

    public TeacherDataMaintainDao getTeacherDataMaintainDao() {
        return teacherDataMaintainDao;
    }
    public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }
    public FirstLoginChoiceDao getFirstLoginChoiceDao() {
        return firstLoginChoiceDao;
    }
    public void setFirstLoginChoiceDao(FirstLoginChoiceDao firstLoginChoiceDao) {
        this.firstLoginChoiceDao = firstLoginChoiceDao;
    }

    public TagsDescService getTagsDescService() {
        return tagsDescService;
    }
    public void setTagsDescService(TagsDescService tagsDescService) {
        this.tagsDescService = tagsDescService;
    }

    /**
     * 直接根据学段ID查询所有教材
     * @param subjectId :学科ID
     * @param rangeId   ：学段ID
     * @return
     */
    @Override
    public List<Tags> findKeMuList(int subjectId, int rangeId) {
        log.info( "根据学科和学段ID查询所有教材获取学科ID是：" + subjectId );
        //到第二级目录
        int id = 0;//教材目录的ID值
        List<Tags> tagesList = KlbTagsUtil.getInstance().getTagById(rangeId);//直接根据学段找下一级(教材目录， 知识点 )
        if( null != tagesList ){
            for( Tags tags : tagesList ){
                if( CommonInstence.JIAO_CAI_MU_LU.equals( tags.getName() )){
                    id = tags.getId();
                }
            }
        }
        //根据教材目录的ID查询所有教材
        List<Tags> jiaoCaiList = KlbTagsUtil.getInstance().getTagById(id);

        if( null != jiaoCaiList ){
             findAllJiaoPictrueName( jiaoCaiList );
        }
        return jiaoCaiList;
    }



    public  List<Tags> findAllJiaoPictrueName( List<Tags> jiaoCaiList){
        //System.out.println( " teacher write file result size ******* : " + jiaoCaiList.size() );
        for( int  i = 0 ; i< jiaoCaiList.size();i++ ){
            TagsDesc tagsDesc = tagsDescService.getTagDescByTagId(jiaoCaiList.get( i ).getId() );
           // System.out.println( " tagsDesc is ******* : " + tagsDesc );
            if( null != tagsDesc && StringUtils.isNotBlank( tagsDesc.getDescription() ) ){
                String des = tagsDesc.getDescription();
                des = des.trim();
                if( des.startsWith( "<p>") ){
                    des = des.substring( 3 , des.length() );
                }
                if( des.endsWith( "</p>")){
                    des = des.substring( 0, des.length() - 4 );
                }
                jiaoCaiList.get( i ).setTitle( des );//设置教科目录的描述
            }else{
                jiaoCaiList.get( i ).setTitle( CommonInstence.FILE_DISCRIPTION);
            }
        }
        return  jiaoCaiList;
    }

    public static void main( String[] args ){
        String des = "<p> 1 3re </p>  ";
        des = des.trim();
        if( des.startsWith( "<p>") ){
            des = des.substring( 3 , des.length() );
        }
        if( des.endsWith( "</p>")){
            des = des.substring( 0, des.length() - 4 );
        }

        System.out.println( des );
    }

    /**
     * 查询当前老师名下一共创建的科目数量
     * @param teacherId
     * @return
     */
    @Override
    public int findTeacherSubjectNum(int teacherId) {
        return firstLoginChoiceDao.findTeacherSubjectNum( teacherId );
    }
    /**
     * 验证学科参数合法性及拼装teacher_book_version对象
     * @param rangeId
     * @param rangeName
     * @param subjectId
     * @param subjectName
     * @return
     */
    @Override
    public TeacherBookVersion checkAndMakeTeacherVersion(String rangeId,
                                                         String rangeName,
                                                         String subjectId,
                                                         String subjectName,
                                                         String bookVersionId,
                                                         String bookVersionName ) {
        if( !StringUtils.isNumeric( rangeId )|| !StringUtils.isNumeric( subjectId ) ||
             StringUtils.isBlank( rangeName ) || StringUtils.isBlank( subjectName ) ||
                !StringUtils.isNumeric( bookVersionId )){
            log.info( "老师创建学科学段时数据非法，学段id:" + rangeId + "，学段名称："+rangeName+
                        "，学科id：" + subjectId + "，学科名称：" + subjectName );
            return null;
        }
        TeacherBookVersion teacher = new TeacherBookVersion();
        teacher.setRangeId( new Integer( rangeId ));
        teacher.setRangeName( rangeName );
        teacher.setSubjectId( new Integer( subjectId ));
        teacher.setSubjectName(subjectName);
        teacher.setBookVersionId(new Integer( bookVersionId ) );
        teacher.setBookVersionName( bookVersionName );
        teacher.setProcess( 3 );
        return teacher;
    }
    /**
     * 添加老师的 ,教材版本信息
     * @param teacher
     */
    public TeacherBookVersion addTeacherRangeAndJiaoCai(TeacherBookVersion teacher ,int teacherId) throws Exception {
        Connection conn = null;
        TeacherBookVersion teacherBookVersion = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            teacherDataMaintainDao.updateTeacherBookVersion( conn , teacher );
            teacherDataMaintainDao.updataUserInfoProcess( conn , teacherId,CommonInstence.PROCESS_3  );
            conn.commit();
        }catch (SQLException e) {
            log.error( " 添加老师的学科和学段信息异常 ，登录ID:" +teacher.getId()  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new Exception( "添加老师的 ,教材版本信息异常" + e.getMessage() , e );
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return teacherBookVersion;
    }

    @Override
    public List<TeacherBookVersion> findTeacherSubject(int teacherId) {
        return firstLoginChoiceDao.findTeacherSubject(teacherId);
    }

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Override
    public User findUserInfo(int userId) {
        return teacherDataMaintainDao.findUserInfo( userId );
    }
    /**
     * 查询老师教材版本关联表
     * @param teacherBookVersionId
     * @return
     */
    @Override
    public TeacherBookVersion findTeacherBookVersionById(int teacherBookVersionId) {
        return firstLoginChoiceDao.findTeacherBookVersionById( teacherBookVersionId );
    }

    /**
     * 添加老师姓名
     * @param teacherId
     * @param realName
     * @return
     */
    @Override
    public int addTeacherRealName(int teacherId, String realName) {
        //User user = findUserInfo( teacherId );
        int num =0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            //更新user表老师的信息
            num = teacherDataMaintainDao.updataUserInfoRealName( conn ,teacherId , realName );
            conn.commit();
        } catch (SQLException e) {
            log.error( "添加老师姓名异常 ，登录ID:" + teacherId  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            throw new BizException( "添加老师姓名异常" ,e );
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }






}
