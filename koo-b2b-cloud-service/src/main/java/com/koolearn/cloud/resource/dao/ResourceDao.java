package com.koolearn.cloud.resource.dao;

import com.koolearn.cloud.common.entity.Collection;
import com.koolearn.cloud.common.entity.UseRecord;
import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

@DAO(dbtype = DbType.MYSQL, source = GlobalConstant.MYSQL_DATASOURCE)
public interface ResourceDao {

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveResource(Connection conn, ResourceInfo resources);

    @SQL(type = SQLType.WRITE_UPDATE)
    void updateResource(Connection conn, ResourceInfo resources);

    @SQL(type = SQLType.READ_BY_ID)
    ResourceInfo getResourceById(int id);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveTagObject(Connection conn, List<TagObject> to);

    @SQL("select tag_name from te_tag_object where object_id = :id and tag_type =:type ")
    List<String> getTagName(@SQLParam("id") int id, @SQLParam("type") int type);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveCollection(Connection conn, Collection c);

    @SQL("select user_id from yy_collection where object_id = :resourceId and status = 1 and object_type=:objectType")
    List<Integer> findCollectionByResourceId(@SQLParam("resourceId") int resourceId, @SQLParam("objectType") int objectType);

    @SQL("select * from yy_collection where user_id = :userId and object_id = :resourceId and status = :status and object_type=:objectType")
    Collection getCollectionByUserIdAndResourceId(@SQLParam("userId") int userId, @SQLParam("resourceId") int resourceId, @SQLParam("status") int status, @SQLParam("objectType") int objectType);

    @SQL("select object_id from yy_collection where user_id = :userId  and object_id in (:questionIds) and status = :status and object_type=:objectType")
    List<Integer> getCollectionQuestionByUser(@SQLParam("userId") int userId, @SQLParam("questionIds") List<Integer> questionIds, @SQLParam("status") int status, @SQLParam("objectType") int objectType);
    @SQL("select object_id from yy_collection where user_id = :userId and status = :status and object_type=:objectType")
    List<Integer> getAllCollectionQuestionByUser(@SQLParam("userId") int userId, @SQLParam("status") int status, @SQLParam("objectType") int objectType);

    @SQL(type = SQLType.WRITE_UPDATE)
    void updateCollection(Connection conn, Collection c);

    @SQL("select tag_id from te_tag_object where object_id = :id and tag_type =:type ")
    List<Integer> getTagId(@SQLParam("id") int id, @SQLParam("type") int type);

    @SQL("select DISTINCT user_id from yy_use_record  where object_id = :objectId and status = 1 and object_type=:objectType")
    Set<Integer> findUseRecordUser(@SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);

    @SQL("select sum(use_times) from yy_use_record  where object_id = :objectId and status = 1 and object_type=:objectType")
    int findUseTimes(@SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);

    @SQL(type = SQLType.WRITE_INSERT)
    void saveUseRecord(Connection conn, UseRecord c);

    @SQL(type = SQLType.WRITE_UPDATE)
    void updateUseRecord(Connection conn, UseRecord c);

    @SQL("select * from yy_use_record where user_id = :userId and object_id = :objectId and status =1 and object_type=:objectType")
    UseRecord findUseRecordByuser(@SQLParam("userId") int userId, @SQLParam("objectId") int objectId, @SQLParam("objectType") int objectType);

    @SQL("select user_id from yy_use_record where object_id = :resourceId and status =1 and object_type=:objectType")
    List<Integer> findUseByResourceId(@SQLParam("resourceId") int resourceId, @SQLParam("objectType") int objectType);

    @SQL(type = SQLType.READ_BY_ID)
    UserEntity getUserById(int id);
    @SQL("select y.* from yy_resources y where y.status=2 order by id ")
    public List<ResourceInfo> findAllResource(@PageBy ListPager listPager);
    @SQL("select count(1) from yy_resources y where y.status=2 order by id ")
    public Long findAllResourceCount();

    /**
     * 根据学校id查询学校学段id
     * @param schoolId
     * @return
     */
    @SQL( " select grade_id from school where id =:schoolId " )
    int findSchoolInfoById( @SQLParam("schoolId")int schoolId);

    @SQL(" UPDATE te_tag_object SET `status` = 0 WHERE object_id = :resourceId ")
    void delTagObject(Connection conn, @SQLParam("resourceId") int resourceId);
}
