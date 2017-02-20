package com.koolearn.cloud.resource.service.impl;

import com.koolearn.cloud.common.entity.Collection;
import com.koolearn.cloud.common.entity.UseRecord;
import com.koolearn.cloud.common.index.ResourcesInfoIndexUtils;
import com.koolearn.cloud.exam.entity.DataRebuild;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.queue.ProducerSourceServiceImpl;
import com.koolearn.cloud.resource.dao.ResourceDao;
import com.koolearn.cloud.resource.dto.SearchResourceBean;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.util.*;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.framework.search.declare.SearchResult;
import com.koolearn.framework.search.declare.SearchService;
import com.koolearn.framework.search.declare.sort.Order;
import com.koolearn.framework.search.declare.sort.Sort;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ResourceInfoServiceImpl implements ResourceInfoService {

    private static final Logger logger = Logger.getLogger(ResourceInfoServiceImpl.class);

    private SearchService searchService;
    private ResourceDao resourceDao;
    private ProducerSourceServiceImpl producerSourceService;
    private String indexpng="/index_1.png";
    private String indexpdf="/index.pdf";
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public void setProducerSourceService(ProducerSourceServiceImpl producerSourceService) {
        this.producerSourceService = producerSourceService;
    }

    /**
     * 创建资源
     *
     * @param resourcesInfo
     * @return
     */
    @Override
    public ResourceInfo createResourceInfo(ResourceInfo resourcesInfo) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            resourcesInfo.setStatus(resourcesInfo.getNewStatus());
            // 保存资源
            resourcesInfo.setDocumentIcon(FileUploadUtils.getDocumentIcon(resourcesInfo.getExtendName()));
            int resourceId = resourceDao.saveResource(conn, resourcesInfo);

            // 资源文档转换目录
            String fileConverPath = FileUploadUtils.getDirPathByIdAndType(resourceId,
                    FileUploadUtils.getModuleValue(resourcesInfo.getExtendName()));
            resourcesInfo.setFileConverPath(fileConverPath);
            resourcesInfo.setFrontcoverUrl(resourcesInfo.getNewDefaultrontcoverUrl());
            resourceDao.updateResource(conn, resourcesInfo);
            //创建索引
            this.createResourceInfoIndex(resourcesInfo);
            conn.commit();
        } catch (Exception e) {
            logger.error("",e);
            try {
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("",e1);
            }
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException e1) {
                logger.error("",e1);
            }
        }
        return resourcesInfo;
    }

    /**
     * 修改资源
     * @param resourcesInfo
     * @return
     */
    @Override
    public ResourceInfo updateResourcesInfo(ResourceInfo resourcesInfo) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
              if(StringUtils.isBlank(resourcesInfo.getFileConverPath())){
                  // 资源文档转换目录
                  String fileConverPath = FileUploadUtils.getDirPathByIdAndType(resourcesInfo.getId(),
                          FileUploadUtils.getModuleValue(resourcesInfo.getExtendName()));
                  resourcesInfo.setFileConverPath(fileConverPath);
              }
            resourcesInfo.setFrontcoverUrl(resourcesInfo.getNewDefaultrontcoverUrl());
            resourceDao.updateResource(conn, resourcesInfo);
            //创建索引
            this.updateResourcesInfoIndex(resourcesInfo);
            conn.commit();
        } catch (Exception e) {
            logger.error("",e);
            try {
                    conn.rollback();
            } catch (SQLException e1) {
                logger.error("",e1);
            }
            return null;
        } finally {
            try {
                    conn.close();
            } catch (SQLException e1) {
                logger.error("",e1);
            }
        }
        return resourcesInfo;

    }

    /**
     * 创建资源索引
     *
     * @param resourcesInfo 资源信息
     * @throws Exception 索引创建失败异常处理
     */
    public void createResourceInfoIndex(ResourceInfo resourcesInfo) {
        HashMap<String, Object[]> map = this.initResourcesInfoMap(resourcesInfo);
        if (map != null) {
            //新建资源不创建文档内容索引，转换回调修改状态时在创建
            long l = System.currentTimeMillis();
            boolean flag = searchService.bulkInsertData(
                    IndexNameUtils.getResourceIndexName(), map);
            logger.info("new resoure Index time：" + (System.currentTimeMillis() - l));
            // 判断索引创建是否创建成功，如果失败抛出异常，触发事务回滚.
            if (!flag) {
                String message = "资源索引创建失败：Id= ; Name=";
                logger.error(message);
            }
        }
    }


    @Override
    public Map<String, Object> searchResourceList(SearchResourceBean bean) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            ListPager listPager = new ListPager();
            listPager.setPageNo(bean.getPageNo() == null ? 0 : bean.getPageNo());
            listPager.setPageSize(20);
            Long count = this.searchResourceCount(bean);
            listPager.setTotalRows(count);
            List<ResourceInfo> listBysearch = this.getResourceInfoBySearch(bean, listPager);
            map.put("sourceList", listBysearch);
            map.put("listPager", listPager);
            map.put("searchCount", count);
            listPager.setTotalRows(count);
            return map;
        } catch (Exception e) {
            logger.debug("",e);
        }
        return null;
    }
    @Override
    public ResourceInfo searchResourceById(Integer resourceId) {
        try {
            SearchResourceBean bean=new SearchResourceBean();
            bean.setResourceId(resourceId);
            ListPager listPager = new ListPager();
            listPager.setPageNo(0);
            listPager.setPageSize(1);
            listPager.setTotalRows(1);
            List<ResourceInfo> listBysearch = this.getResourceInfoBySearch(bean, listPager);
            if(listBysearch!=null&&!listBysearch.isEmpty()) {
                return listBysearch.get(0);
            }
        } catch (Exception e) {
            logger.debug("",e);
        }
        return null;
    }

    @Override
    public ResourceInfo getResoueceById(int resourceId) {
        return this.resourceDao.getResourceById(resourceId);
    }


    @Override
    public boolean isCollection(int userId, int resourceId, int status) {
        Collection c = resourceDao.getCollectionByUserIdAndResourceId(userId, resourceId, status, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
        if (c == null) {
            return false;
        }
        return true;
    }

    private List<ResourceInfo> getResourceInfoBySearch(SearchResourceBean bean, ListPager listPager) {

        QueryBuilder query = this.getResourcesInfoQuery(bean);
        logger.debug("资源搜索:" + query);
        String orderFunc = "desc";

        Integer from = listPager.getPageNo() * listPager.getPageSize();
        List<Map<String, Object>> list = null;
        // 最后一个参数是String... 类型，传入需要查询的字段名称 sort.getOrderList().add(new Order("_score", orderFunc))
        Sort sort = new Sort();
        List<Order> orderList = new ArrayList<Order>();
        sort.setOrderList(orderList);
        sort.getOrderList().add(new Order(ResourcesInfoIndexUtils.INDEX_OPT_TIME, orderFunc));
        SearchResult result = searchService.simpleSearch(new String[]{IndexNameUtils.getResourceIndexName()}, query
                        .buildAsBytes().toBytes(), from, listPager.getPageSize(), sort,
                ResourcesInfoIndexUtils.INDEX_ID, ResourcesInfoIndexUtils.INDEX_NAME,
                ResourcesInfoIndexUtils.INDEX_DESCRIPTION, ResourcesInfoIndexUtils.INDEX_TYPE,
                ResourcesInfoIndexUtils.INDEX_FORMAT, ResourcesInfoIndexUtils.INDEX_MARROW,
                ResourcesInfoIndexUtils.INDEX_UPLOAD_TIME, ResourcesInfoIndexUtils.INDEX_UPLOAD_USER_ID,
                ResourcesInfoIndexUtils.INDEX_STORAGE_SIZE, ResourcesInfoIndexUtils.INDEX_PAGE_SIZE,
                ResourcesInfoIndexUtils.INDEX_TIME_LENGTH, ResourcesInfoIndexUtils.INDEX_SOURCE,
                ResourcesInfoIndexUtils.INDEX_STATUS, ResourcesInfoIndexUtils.INDEX_CLASS_VIDEO,
                ResourcesInfoIndexUtils.INDEX_SUBJECT_TAG_ID, ResourcesInfoIndexUtils.INDEX_STAGE_TAG_ID,
                ResourcesInfoIndexUtils.INDEX_TEACH_SCHEDULE_IDS, ResourcesInfoIndexUtils.INDEX_KNOWLEDGE_TAGS,
                ResourcesInfoIndexUtils.INDEX_FILE_OLD_NAME, ResourcesInfoIndexUtils.INDEX_FILE_CONVER_PATH,
                ResourcesInfoIndexUtils.INDEX_FILE_PATH, ResourcesInfoIndexUtils.INDEX_FRONTCOVER_URL,
                ResourcesInfoIndexUtils.INDEX_OPT_USERID, ResourcesInfoIndexUtils.INDEX_OPT_TIME,
                ResourcesInfoIndexUtils.INDEX_FILE_NEW_NAME, ResourcesInfoIndexUtils.INDEX_STAGE_TAG_NAME,
                ResourcesInfoIndexUtils.INDEX_SUBJECT_TAG_NAME, ResourcesInfoIndexUtils.INDEX_EXTEND_NAME,
                ResourcesInfoIndexUtils.INDEX_KNOWLEDGE_SCHEDULE_PATH, ResourcesInfoIndexUtils.INDEX_KNOWLEDGE_NAME,
                ResourcesInfoIndexUtils.INDEX_BOOK_VERSION_NAME, ResourcesInfoIndexUtils.INDEX_USE_TIMES,
                ResourcesInfoIndexUtils.INDEX_TYPE_NAME, ResourcesInfoIndexUtils.INDEX_TYPE_NAME,
                ResourcesInfoIndexUtils.INDEX_USE_TIMES, ResourcesInfoIndexUtils.INDEX_USE_TIMES);
        if (result != null) {
            list = result.getResult();
        }
        List<ResourceInfo> resourceInfoList = new ArrayList<ResourceInfo>();
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                ResourceInfo resourcesInfo = this.getResourceInfoByMap(map);
                if (resourcesInfo != null) {
                    resourceInfoList.add(resourcesInfo);
                }
            }
        }
        return resourceInfoList;
    }

    /**
     * 根据索引查询返回MAP创建资源信息对象
     *
     * @param map 索引查询返回MAP
     * @return 创建资源信息对象
     */
    private ResourceInfo getResourceInfoByMap(Map<String, Object> map) {
        try {
            return ResourcesInfoIndexUtils.getResourceInfoByMap(map);
        } catch (Exception e) {
            logger.error("",e);
            return null;
        }
    }

    /**
     * 获取总数
     */
    @Override
    public Long searchResourceCount(SearchResourceBean bean) {
        QueryBuilder query = this.getResourcesInfoQuery(bean);
        return searchService.getCount(
                new String[]{IndexNameUtils.getResourceIndexName()}, query.buildAsBytes().toBytes());
    }

    /**
     * 创建资源索引搜索Query.
     *
     * @return QueryBuilder 对象
     */
    private QueryBuilder getResourcesInfoQuery(SearchResourceBean bean) {

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(bean.getResourceId()!=null){
        //根据id查询资源
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_ID,bean.getResourceId()));
            return query;
        }
        // 资源状态为：转换成功（可用）(暂时不加状态过滤)
        if (bean.getSource() != null) {
            if (bean.getSource() == GlobalConstant.RESOURCE_SOURCE_TEACHER) {
                //我的上传列表
                query.mustNot(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_STATUS,
                        GlobalConstant.RESOURCE_STATUS_UNAVAILABLE));
            } else {
                //资源库只有转换成功的
                query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_STATUS,
                        GlobalConstant.RESOURCE_STATUS_CONVERED));
            }
        }
        if (bean.getKlbType() != null) {
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            searchQuery.should(QueryBuilders.queryString(String.valueOf(bean.getKlbType())).field(ResourcesInfoIndexUtils.INDEX_KLB_TYPE));
            searchQuery.should(QueryBuilders.queryString(String.valueOf(GlobalConstant.KLB_TYPE_ALL)).field(ResourcesInfoIndexUtils.INDEX_KLB_TYPE));
            query.must(searchQuery);
        }
        if (bean.getTagId() != null) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TAG_FULL_PATH, bean.getTagId()));
        }
        if (bean.getSubjectId() != null) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TAG_FULL_PATH, bean.getSubjectId()));
        }
        if (bean.getBookVersion() != null) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TAG_FULL_PATH, bean.getBookVersion()));
        }
        if (bean.getObligatory() != null) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TAG_FULL_PATH, bean.getObligatory()));
        }
        if (bean.getRangeId() != null) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TAG_FULL_PATH, bean.getRangeId()));
        }
        // 文本检索条件
        if (StringUtils.isNotBlank(bean.getKeyTxt())) {
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            //名称
            searchQuery.should(QueryBuilders.queryString(bean.getKeyTxt()).field(ResourcesInfoIndexUtils.INDEX_NAME).minimumShouldMatch("75%"));
            searchQuery.should(QueryBuilders.queryString(bean.getKeyTxt()).field(ResourcesInfoIndexUtils.INDEX_CONTENT).minimumShouldMatch("75%"));
            searchQuery.should(QueryBuilders.queryString(bean.getKeyTxt()).field(ResourcesInfoIndexUtils.INDEX_DOCUMENT_CONTENT).minimumShouldMatch("75%"));
            query.must(searchQuery);
        }
        int source=-1;
        if (bean.getSource() != null) {
            source = bean.getSource();
        }
        if (source == -1) {//老师端全部
            BoolQueryBuilder schoolQuery = QueryBuilders.boolQuery();
            schoolQuery.should(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_UPLOAD_USER_ID, bean.getUserEntity().getId()));
            schoolQuery.should(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_SYSTEM));
            schoolQuery.should(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_PUBLIC));
            BoolQueryBuilder schoolQuery1 = QueryBuilders.boolQuery();
            schoolQuery1.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SCHOOL_ID, bean.getUserEntity().getSchoolId()));
            schoolQuery1.mustNot(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_TEACHER));
            schoolQuery.should(schoolQuery1);
            query.must(schoolQuery);
        } else if (source == GlobalConstant.RESOURCE_SOURCE_TEACHER) {//老师端个人
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_UPLOAD_USER_ID, bean.getUserEntity().getId()));
        } else if (source == GlobalConstant.RESOURCE_SOURCE_SCHOOL) {//老师端学校
            BoolQueryBuilder schoolQuery = QueryBuilders.boolQuery();
            schoolQuery.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SCHOOL_ID, bean.getUserEntity().getSchoolId()));
            schoolQuery.mustNot(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_SYSTEM));
            schoolQuery.mustNot(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_TEACHER));
            query.must(schoolQuery);
        } else if (source == GlobalConstant.RESOURCE_SOURCE_PUBLIC) {//老师端公共
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            searchQuery.should(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_SYSTEM));
            searchQuery.should(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_PUBLIC));
            query.must(searchQuery);
        } else if (source == GlobalConstant.RESOURCE_SOURCE_SYSTEM) {//运营端资源库
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_SOURCE, GlobalConstant.RESOURCE_SOURCE_SYSTEM));
            if(bean.getUploadUser()!=null){
                query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_UPLOAD_USER_ID, bean.getUploadUser()));
                //根据用户权限控制 上传人 学科的过滤
                //......
            }
            if(StringUtils.isNotBlank(bean.getUpdateBeginTime()) && StringUtils.isNotBlank(bean.getUpdateEndTime())){
                Date begin=ParseDate.parse(bean.getUpdateBeginTime());
                Date end=ParseDate.parse(bean.getUpdateEndTime());
                RangeQueryBuilder rangeQueryBuilder =QueryBuilders.rangeQuery(ResourcesInfoIndexUtils.INDEX_OPT_TIME);
                rangeQueryBuilder.from(ParseDate.formatByDate(begin,ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
                rangeQueryBuilder.to(ParseDate.formatByDate(end, ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
                query.must(rangeQueryBuilder);

            }
        }


        // 资源类型
        if (bean.getType() != null && -1 != bean.getType()) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TYPE, bean.getType()));
        }

        // 格式
        if (bean.getFormat() != null && -1 != bean.getFormat()) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_FORMAT, bean.getFormat()));
        }

        // 过滤条件
        if (bean.getSelectType() != null) {
            if (bean.getSelectType() == 1) {
                query.mustNot(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_USER_USE_ID, bean.getUserEntity().getId()));
            } else if (bean.getSelectType() == 2) {
                query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_USER_COLLECTION_ID, bean.getUserEntity().getId()));
            } else if (bean.getSelectType() == 3) {
                query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_USER_USE_ID, bean.getUserEntity().getId()));
            }
        }

        if (bean.getResourceId() != null && -1 != bean.getResourceId()) {
            query.must(QueryBuilders.termQuery(ResourcesInfoIndexUtils.INDEX_TYPE, bean.getResourceId()));
        }

        return query;
    }

    /**
     * 初始化 创建资源索引时 所需 map
     */
    private HashMap<String, Object[]> initResourcesInfoMap(ResourceInfo resource)  {
        // 初始化资源索引属性
        HashMap<String, Object[]> map = ResourcesInfoIndexUtils.initResourcesInfoMap(resource);
        return map;
    }

    /**
     * 调用：1。转换服务器更新资源状态  2。删除资源更新状态
     */
    @Override
    public boolean updateResourcesStatus(int resourceId, int status) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {

            ResourceInfo resourcesInfo = this.searchResourceById(resourceId);//setResourceInfoValue(resourceId, null)
            if(status==GlobalConstant.RESOURCE_STATUS_CONVERED){
                resourcesInfo=setResourceSearchContent(resourcesInfo);
            }
            resourcesInfo.setStatus(status);
            resourceDao.updateResource(conn, resourcesInfo);
            //把知识点 进度点名称
            this.updateResourcesInfoIndex(resourcesInfo);
            conn.commit();
        } catch (Exception e) {
            logger.error("",e);
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }


        return false;
    }

    /**
     * 转换回调  只有转换成功后调用
     * @param resourcesInfo
     */
    private  ResourceInfo setResourceSearchContent(ResourceInfo resourcesInfo){
        //转换成功修改索引内容和封面
        int format=resourcesInfo.getFormat();
        String pdfpath = "";
        if (format == GlobalConstant.RESOURCE_FORMAT_WORD || format == GlobalConstant.RESOURCE_FORMAT_PPT) {
            pdfpath=FileUploadUtils.getAbsolutePath(resourcesInfo.getFileConverPath()) + indexpdf;
        }else if (resourcesInfo.getFormat() == GlobalConstant.RESOURCE_FORMAT_PDF) {
            pdfpath = FileUploadUtils.getAbsolutePath(resourcesInfo.getFilePath());
        }
        File converFile = new File(pdfpath);
        if (StringUtils.isNotBlank(pdfpath) && converFile.exists()) {
            resourcesInfo.setPdfContent(PDFUtil.getPdfContext(pdfpath));
        }
        resourcesInfo.setFrontcoverUrl(resourcesInfo.getFileConverPath() + indexpng);
        return resourcesInfo;
    }

    @Override
    public void collection(int resourceId, UserEntity user) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            Collection c = resourceDao.getCollectionByUserIdAndResourceId(user.getId(), resourceId, GlobalConstant.STATUS_OFF, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
            if (c == null) {
                c = resourceDao.getCollectionByUserIdAndResourceId(user.getId(), resourceId, GlobalConstant.STATUS_ON, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
                if (c == null) {
                    c = new Collection();
                    c.setObjectType(GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
                    c.setObjectId(resourceId);
                    c.setCreateTime(new Date());
                    c.setSchoolId(user.getSchoolId());
                    c.setUserId(user.getId());
                    c.setIsfrom(GlobalConstant.CLIENT_TYPE_TEACHER);
                    c.setStatus(GlobalConstant.STATUS_ON);
                    resourceDao.saveCollection(conn, c);
                } else {
                    c.setStatus(GlobalConstant.STATUS_OFF);
                    c.setCreateTime(new Date());
                    resourceDao.updateCollection(conn, c);
                }
            } else {
                c.setStatus(GlobalConstant.STATUS_ON);
                c.setCreateTime(new Date());
                resourceDao.updateCollection(conn, c);
            }

            conn.commit();
            ResourceInfo resourceInfo = this.searchResourceById(resourceId);//setResourceInfoValue(resourceId, null)
            //更新索引：收藏人数
            List<Integer> collectionIds = resourceDao.findCollectionByResourceId(resourceInfo.getId(), GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
            String collectionIdsStr = StringUtils.join(collectionIds, "_");
            resourceInfo.setUserCollectionIds(collectionIdsStr);
            //加入消息队列
            producerSourceService.send(resourceInfo);
        } catch (Exception e) {
            ConnUtil.rollbackConnection(conn);
            logger.debug("",e);
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    @Override
    public void use(int resourceId, UserEntity user) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            UseRecord ur = resourceDao.findUseRecordByuser(user.getId(), resourceId, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
            if (ur == null) {
                ur = new UseRecord();
                ur.setObjectType(GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
                ur.setObjectId(resourceId);
                ur.setStatus(GlobalConstant.STATUS_ON);
                ur.setUseTime(new Date());
                ur.setUserId(user.getId());
                ur.setUseTimes(1);
                resourceDao.saveUseRecord(conn, ur);
            } else {
                ur.setUseTimes(ur.getUseTimes() + 1);
                resourceDao.updateUseRecord(conn, ur);
            }
            conn.commit();
            ResourceInfo resourceInfo = this.searchResourceById(resourceId);//setResourceInfoValue(resourceId, null)
            //使用人
            List<Integer> useIds = resourceDao.findUseByResourceId(resourceId, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
            String useIdsStr = StringUtils.join(useIds, "_");
            resourceInfo.setUserUseIds(useIdsStr);
            int useTimes = resourceDao.findUseTimes(resourceId, GlobalConstant.KLB_OBJECT_TYPE_RESOURCE);
            resourceInfo.setUseTimes(useTimes);
            //加入消息队列
            producerSourceService.send(resourceInfo);
        } catch (Exception e) {
            ConnUtil.rollbackConnection(conn);
            logger.debug("",e);
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 更新资源索引：1.文档转换回调修改状态   2.收藏或使用次数队列调用
     *
     * @throws Exception
     */
    @Override
    public void updateResourcesInfoIndex(ResourceInfo resourcesInfo) {
        try {
            HashMap<String, Object[]> map = this.initResourcesInfoMap(resourcesInfo);
            if (map != null) {
                // 根据原有ID 创建 索引查询条件
                HashMap<String, Object[]> searchMap = new HashMap<String, Object[]>();
                searchMap.put(ResourcesInfoIndexUtils.INDEX_ID, new Integer[]{resourcesInfo.getId()});
                long l = System.currentTimeMillis();
                this.searchService.bulkUpdateData(IndexNameUtils.getResourceIndexName(), searchMap, map);
                logger.debug("资源更新");

                String from1 = "资源索引更新time:" + (System.currentTimeMillis() - l);
                logger.debug(from1);
                // 判断索引创建是否创建成功，如果失败抛出异常，触发事务回滚.
                if (logger.isDebugEnabled()) {
                    logger.debug("资源索引更新状态：Id=" + resourcesInfo.getId() + ";");
                }
            }
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    @Override
    public UserEntity getUserById(int uploadUserId) {
        return resourceDao.getUserById(uploadUserId);
    }

  /************** 重建资源库索引 开始***************/
    @Override
    public void rebuildSearch() {
        // 获得数据库中资源总数
         Long count = resourceDao.findAllResourceCount();
        // 补发队列
        List<ResourceInfo> errorResourceList = new ArrayList<ResourceInfo>();
        if (count != null && count > 0) {
            CacheTools.addCacheForever(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,new DataRebuild());
            DataRebuild dr=CacheTools.getCache(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,DataRebuild.class);
            dr.setCurrentRebuildtime(ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
            ListPager listPager = new ListPager();
            listPager.setTotalRows(count);
            listPager.setPageSize(1000);
            // 分页获得资源集合
            for (int i = 0; i < listPager.getTotalPage(); i++) {
                dr.pageAdd();
                CacheTools.addCacheForever(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,dr);
                dr=CacheTools.getCache(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,DataRebuild.class);
                listPager.setPageNo(i);
                // 获取分页资源
                List<ResourceInfo> resourcesInfoList = this.resourceDao.findAllResource(listPager);
                // 循环重建索引，返回失败集合
                List<ResourceInfo> errorlist = this.reIndexResourseByList(resourcesInfoList);
                // 补发队列
                errorResourceList.addAll(errorlist);
                dr.setErrorResourceList(errorResourceList);
                CacheTools.addCacheForever(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,dr);
            }
            dr.setCurrentRebuildEndtime(ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
            CacheTools.addCacheForever(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,dr);
            //for ResourceInfo resourcesInfo : errorResourceList补发重建
        }
        logger.debug("资源重建结束 -----------------------------");
    }

    /**
     * 根据学校id查询学校学段id
     * @param schoolId
     * @return
     */
    @Override
    public int findSchoolInfoById(int schoolId) {
        return resourceDao.findSchoolInfoById( schoolId );
    }

    /**
     *  循环重建索引
     * @param resourceInfoList
     * @return
     */
    private List<ResourceInfo> reIndexResourseByList(List<ResourceInfo> resourceInfoList) {
        List<ResourceInfo> errorResourceList = new ArrayList<ResourceInfo>();
        for (ResourceInfo resourcesInfo : resourceInfoList) {
            if (!this.createIndexByResource(resourcesInfo)) {
                errorResourceList.add(resourcesInfo);
                logger.debug("资源 ID : " + resourcesInfo.getId() + " Name : " + resourcesInfo.getName() + " 索引重建失败");
            }
            DataRebuild dr=CacheTools.getCache(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,DataRebuild.class);
            dr.countAdd();
            CacheTools.addCacheForever(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,dr);
        }
        return errorResourceList;
    }
    /**
     * 根据资源信息重建资源索引
     *
     * @param resourcesInfo 资源信息
     * @return 返回创建状态
     */
    private Boolean createIndexByResource(ResourceInfo resourcesInfo) {
        try {

            parseResourceTagInfo(resourcesInfo);//封装知识点 进度点
            //处理文档内容
            if (resourcesInfo.getFormat() == GlobalConstant.RESOURCE_FORMAT_WORD && resourcesInfo.getStatus() != 0) {
                String converPath = resourcesInfo.getFileConverPath();
                resourcesInfo.setPdfContent(PDFUtil.getPdfContext(FileUploadUtils.getAbsolutePath(converPath + indexpdf)));
            }
            if (resourcesInfo.getFormat() == GlobalConstant.RESOURCE_FORMAT_PPT && resourcesInfo.getStatus() != 0) {
                String converPath = resourcesInfo.getFileConverPath();
                resourcesInfo.setPdfContent(PDFUtil.getPdfContext(FileUploadUtils.getAbsolutePath(converPath + indexpdf)));
            }
            if (resourcesInfo.getFormat() == GlobalConstant.RESOURCE_FORMAT_PDF) {
                resourcesInfo.setPdfContent(PDFUtil.getPdfContext(FileUploadUtils.getAbsolutePath(resourcesInfo.getFilePath())));
            }
            //处理资源类型名称：
            resourcesInfo.setTypeName(DataDictionaryUtil.getInstance().getDictionaryName(GlobalConstant.DICTIONARY_TYPE_RESOURCE_TYPE, resourcesInfo.getType()));
            int format = resourcesInfo.getFormat();
            if(StringUtils.isBlank(resourcesInfo.getFrontcoverUrl())){
                if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO) {
                    resourcesInfo.setFrontcoverUrl("/cloud/image/defaultFrontcover_MP4.jpg");
                } else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
                    resourcesInfo.setFrontcoverUrl("/cloud/image/defaultFrontcover_MP3.jpg");
                } else if (format == GlobalConstant.RESOURCE_FORMAT_OTHER) {
                    resourcesInfo.setFrontcoverUrl("/cloud/image/defaultFrontcover.png");
                } else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
                    resourcesInfo.setFrontcoverUrl(resourcesInfo.getFilePath());
                } else if(format==GlobalConstant.RESOURCE_FORMAT_WORD ||
                        format==GlobalConstant.RESOURCE_FORMAT_PPT||
                        format==GlobalConstant.RESOURCE_FORMAT_PDF){
                    resourcesInfo.setFrontcoverUrl(resourcesInfo.getFileConverPath()+indexpng);
                }else {
                    resourcesInfo.setFrontcoverUrl("/cloud/image/defaultFrontcover.png");
                }
            }
            // 重建索引
            this.createResourceInfoIndex(resourcesInfo);
        }
        catch (Exception e) {
            logger.error("重建索引失败，资源id-----》"+resourcesInfo.getId(),e);
            return false;
        }
        return true;
    }
    /**
     * 1.处理资源 知识点、进度点、题型等标签
     */
    @Override
    public ResourceInfo parseResourceTagInfo(ResourceInfo resourcesInfo) {
        try {
            String tagFullPath=resourcesInfo.getTagFullPath();
            if(StringUtils.isNotBlank(tagFullPath)) {
                String[] tagFullPatArr = tagFullPath.split(",");
                Set<String> tagFullPathSet = new HashSet<String>();
                CollectionUtils.addAll(tagFullPathSet, tagFullPatArr);//set集合去重
                List<Integer> knowledgeTags = new ArrayList<Integer>();
                List<Integer> bookVersionIds = new ArrayList<Integer>();
                List<String> knowNameListFullPath = new ArrayList<String>();
                List<String> teacNameListFullPath = new ArrayList<String>();
                for (String tagFP : tagFullPathSet) {
                    jiexieTagq(tagFP, knowledgeTags, knowNameListFullPath, resourcesInfo, bookVersionIds, teacNameListFullPath);
                }
                //全路径
                resourcesInfo.setKnowledgeNames(StringUtils.join(knowNameListFullPath, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
                resourcesInfo.setBookVersionNames(StringUtils.join(teacNameListFullPath, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
                //知识点、进度点 单个标签id
                Integer[] knowids = {};
                resourcesInfo.setKnowledgeTags(knowledgeTags.toArray(knowids));//知识点id Integer[]
                Integer[] bvids = {};
                resourcesInfo.setBookVersionIds(bookVersionIds.toArray(bvids));//进度id Integer[]
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return resourcesInfo;
    }
    private void jiexieTagq(String tagFP,List<Integer> knowledgeTags,List<String> knowNameListFullPath,ResourceInfo resourcesInfo,List<Integer> bookVersionIds,List<String> teacNameListFullPath){
        if (tagFP.endsWith("_60")) {
            //学科学科树
            String[] tags = tagFP.split("_");
            if (tags.length > 4) {
                jiexieTag(tagFP, tags, knowledgeTags, knowNameListFullPath, resourcesInfo, bookVersionIds, teacNameListFullPath);
            }
        }
    }
private void jiexieTag(String tagFP,String[] tags,List<Integer> knowledgeTags,List<String> knowNameListFullPath,ResourceInfo resourcesInfo,List<Integer> bookVersionIds,List<String> teacNameListFullPath){
    //知识点、进度点层
    String kOrTId=tags[tags.length-4];
    String tagName=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(kOrTId)).getName();
    if(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME.equals(tagName)){
        //该标签是知识点
        Tags tag=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(tags[0]));
        if(tag!=null){
            knowledgeTags.add(tag.getId());
            knowNameListFullPath.add(KlbTagsUtil.getInstance().getCacheTagFullPathName(tagFP,GlobalConstant.KLB_KNOWLEDGE_NAME_OFFSET));
        }else{
            logger.info("*【资源"+resourcesInfo.getId()+"】知识点标签【"+tags[0]+"】不存在>>>>>>>>>>>>>>>>>>>");
        }
    }else if(GlobalConstant.KLB_TAB_TEACHING_NAME.equals(tagName)){
        Tags tag=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(tags[0]));
        //该标签是教材目录
        if(tag!=null){
            bookVersionIds.add(tag.getId());
            teacNameListFullPath.add(KlbTagsUtil.getInstance().getCacheTagFullPathName(tagFP,GlobalConstant.KLB_TEACHING_NAME_OFFSET));
        }else{
            logger.error("【资源"+resourcesInfo.getId()+"】进度点标签【"+tags[0]+"】不存在>>>>>>>>>>>>>>>>>>>");
        }
    }
}
/******************* 重建资源库 结束***********************/
}
