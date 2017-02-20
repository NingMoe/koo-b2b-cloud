package com.koolearn.cloud.resource.service;

import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dto.SearchResourceBean;
import com.koolearn.cloud.resource.entity.ResourceInfo;

import java.util.Map;

public interface ResourceInfoService {

    /**
     * 创建资源
     */
    ResourceInfo createResourceInfo(ResourceInfo resourcesInfo);

    /**
     * 更新资源
     *
     * @param resourceId
     * @param status
     * @return
     */
    boolean updateResourcesStatus(int resourceId, int status);

    /**
     * 查询资源
     *
     * @param bean
     * @return
     */
    Map<String, Object> searchResourceList(SearchResourceBean bean);

    /**
     * 根据id过去资源
     *
     * @param resourceId
     * @return
     */
    ResourceInfo getResoueceById(int resourceId);

    /**
     * 收藏
     *
     * @param user
     */
    void collection(int resourceId, UserEntity user);

    /**
     * 是否收藏
     *
     * @param userId
     * @param resourceId
     * @param i
     * @return
     */
    boolean isCollection(int userId, int resourceId, int i);


    /**
     * 选择
     *
     * @param resourceId
     * @param user
     */
    void use(int resourceId, UserEntity user);

    /**
     * 修改索引
     *
     * @param resourcesInfo
     */
    void updateResourcesInfoIndex(ResourceInfo resourcesInfo);

    UserEntity getUserById(int uploadUserId);

    /**
     * 索引重建索引
     */
    void rebuildSearch();

    /**
     * 查询学校学段
     * @param schoolId
     * @return
     */
    int findSchoolInfoById(int schoolId);
    public Long searchResourceCount(SearchResourceBean bean);

    public ResourceInfo parseResourceTagInfo(ResourceInfo resourcesInfo);

    ResourceInfo updateResourcesInfo(ResourceInfo resource);
    public ResourceInfo searchResourceById(Integer resourceId);
}
