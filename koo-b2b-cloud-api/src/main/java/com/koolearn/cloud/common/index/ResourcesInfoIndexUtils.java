package com.koolearn.cloud.common.index;

import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.util.DataDictionaryUtil;
import com.koolearn.cloud.util.ParseDate;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class ResourcesInfoIndexUtils {


    // 索引-ID
    public static final String INDEX_ID = "id";

    // 索引-名称
    public static final String INDEX_NAME = "name";

    // 索引-资源描述
    public static final String INDEX_DESCRIPTION = "description";

    // 索引-资源类型
    public static final String INDEX_TYPE = "type";

    // 索引-资源格式
    public static final String INDEX_FORMAT = "format";

    // 索引-精华标识
    public static final String INDEX_MARROW = "marrow";

    // 索引-上传时间
    public static final String INDEX_UPLOAD_TIME = "upload_time";

    // 索引-上传人
    public static final String INDEX_UPLOAD_USER_ID = "upload_user_id";

    // 索引-存储大小
    public static final String INDEX_STORAGE_SIZE = "storage _size";

    // 索引-页数
    public static final String INDEX_PAGE_SIZE= "page_size";

    // 索引-视频时长
    public static final String INDEX_TIME_LENGTH = "time_length";

    // 索引-来源（学校 运营 个人）
    public static final String INDEX_SOURCE = "source";

    // 索引-状态
    public static final String INDEX_STATUS = "status";

    // 索引-是否课堂视频
    public static final String INDEX_CLASS_VIDEO = "clsss_video";

    // 索引-学科标签id
    public static final String INDEX_SUBJECT_TAG_ID = "subject_tag_id";

    // 索引-学段标签id
    public static final String INDEX_STAGE_TAG_ID= "stage_tag_id";

    // 索引-关联 教学进度点标签
    public static final String INDEX_TEACH_SCHEDULE_IDS = "teach_schedule_ids";

    // 索引-关联知识点标签
    public static final String INDEX_KNOWLEDGE_TAGS = "knowledge_tags";

    // 索引-原文件名
    public static final String INDEX_FILE_OLD_NAME="file_old_name" ;
    // 索引-文件路径
    public static final String INDEX_FILE_PATH="file_path";
    //索引-资源文件转换目录
    public static final String INDEX_FILE_CONVER_PATH="file_conver_path";
    // 索引-视频封面图片路径
    public static final String INDEX_FRONTCOVER_URL="frontcover_url";
    // 索引-操作人
    public static final String INDEX_OPT_USERID="opt_user_id";
    // 索引-操作时间
    public static final String INDEX_OPT_TIME="opt_time";
    // 索引-文件新名
    public static final String INDEX_FILE_NEW_NAME="file_new_name";
    // 索引-学段标签名
    public static final String INDEX_STAGE_TAG_NAME="stage_tag_name";
    // 索引-学科标签名
    public static final String INDEX_SUBJECT_TAG_NAME="subject_tag_name";
    // 索引-文件扩展名
    public static final String INDEX_EXTEND_NAME="extend_name";
    //索引搜索文本(知识点和进度点)
    public static final String INDEX_CONTENT="content";
    //索引搜索文本(文档内容)
    public static final String INDEX_DOCUMENT_CONTENT="document_content";
    //索引搜索知识点和教材目录的路径
    public static final String INDEX_KNOWLEDGE_SCHEDULE_PATH="know_sche_path";
    //tagID(格式：10_12_13)
    public static final String INDEX_TAG_FULL_PATH="tag_full_path";
    //知识点名称
    public static final String INDEX_KNOWLEDGE_NAME="knowledge_name";
    //教材版本名称
    public static final String INDEX_BOOK_VERSION_NAME="book_version_name";
    //用户使用次数
    public static final String INDEX_USE_TIMES="use_times";
    //用户使用id(格式：11_12_13)
    public static final String INDEX_USER_USE_ID="user_use_id";
    //用户收藏id(格式：11_12_13)
    public static final String INDEX_USER_COLLECTION_ID="user_collection_id";
    //知识点，教材进度点 (0,1)
    public static final String INDEX_KLB_TYPE="klb_type";
    //类型名称
    public static final String INDEX_TYPE_NAME="type_name";
    //学校id
    public static final String INDEX_SCHOOL_ID="school_ID";

    private ResourcesInfoIndexUtils(){}


    /**
     * 生成创建 资源索引  所需 map对象
     * @param resourcesInfo
     * @return
     * @throws Exception
     */
    public static HashMap<String, Object[]> initResourcesInfoMap(
            ResourceInfo resourcesInfo)  {
        // 判断资源状态为已审批，而且资源类型不为教案
        HashMap<String, Object[]> map;

        map = new HashMap<String, Object[]>();
        map.put(INDEX_ID, new Integer[] { resourcesInfo.getId() });
        map.put(INDEX_NAME, new String[] { resourcesInfo.getName() });
        map.put(INDEX_DESCRIPTION, new String[] {resourcesInfo.getDescription()  });
        map.put(INDEX_TYPE, new Integer[] {resourcesInfo.getType()  });
        map.put(INDEX_FORMAT, new Integer[] {resourcesInfo.getFormat()  });
        map.put(INDEX_MARROW, new Integer[] {resourcesInfo.getMarrow() });
        map.put(INDEX_UPLOAD_TIME,
                new String[] { ParseDate.formatByDate(resourcesInfo.getUploadTime(),
                        ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_UPLOAD_USER_ID,
                new Integer[] {resourcesInfo.getUploadUserId()  });
        map.put(INDEX_STORAGE_SIZE,
                new Long[] { resourcesInfo.getStorageSize() });
        map.put(INDEX_PAGE_SIZE, new Integer[] {resourcesInfo.getPageSize()});
        map.put(INDEX_TIME_LENGTH, new Integer[] {resourcesInfo.getTimeLength()});
        map.put(INDEX_SOURCE, new Integer[] {resourcesInfo.getSource()  });
        map.put(INDEX_STATUS, new Integer[] { resourcesInfo.getStatus() });
        map.put(INDEX_CLASS_VIDEO, new Integer[] {resourcesInfo.getClsssVideo()  });
        map.put(INDEX_SUBJECT_TAG_ID, new Integer[] { resourcesInfo.getSubjectTagId() });
        map.put(INDEX_STAGE_TAG_ID, new Integer[] { resourcesInfo.getStageTagId() });
        map.put(INDEX_FILE_OLD_NAME, new String[] { resourcesInfo.getFileOldName() });
        map.put(INDEX_FILE_PATH, new String[] { resourcesInfo.getFilePath() });
        map.put(INDEX_FILE_CONVER_PATH, new String[]{resourcesInfo.getFileConverPath()});
        map.put(INDEX_FRONTCOVER_URL, new String[] { resourcesInfo.getFrontcoverUrl()});
        map.put(INDEX_OPT_USERID, new Integer[] {resourcesInfo.getUploadUserId()});
        map.put(INDEX_OPT_TIME,
                new String[] { ParseDate.formatByDate(resourcesInfo.getOptTime(),
                        ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_FILE_NEW_NAME, new String[] { resourcesInfo.getFileNewName() });
        map.put(INDEX_STAGE_TAG_NAME, new String[] { resourcesInfo.getStageTagName() });
        map.put(INDEX_SUBJECT_TAG_NAME, new String[] { resourcesInfo.getSubjectTagName() });
        map.put(INDEX_EXTEND_NAME, new String[] { resourcesInfo.getExtendName() });
        map.put(INDEX_CONTENT, new String[] { resourcesInfo.getContent() });
        map.put(INDEX_TAG_FULL_PATH, new String[] { resourcesInfo.getTagFullPath() });
        map.put(INDEX_TYPE_NAME, new String[] { resourcesInfo.getTypeName() });

        map.put(INDEX_KNOWLEDGE_NAME, new String[] { resourcesInfo.getKnowledgeNames() });
        map.put(INDEX_BOOK_VERSION_NAME, new String[] { resourcesInfo.getBookVersionNames() });
        map.put(INDEX_USE_TIMES, new Integer[] {resourcesInfo.getUseTimes()});
        map.put(INDEX_USER_USE_ID, new String[] { resourcesInfo.getUserUseIds() });
        map.put(INDEX_USER_COLLECTION_ID, new String[] { resourcesInfo.getUserCollectionIds() });
        map.put(INDEX_KLB_TYPE, new Integer[] { resourcesInfo.getKlbType() });
        map.put(INDEX_SCHOOL_ID, new Integer[] { resourcesInfo.getSchoolId() });

        map.put(INDEX_DOCUMENT_CONTENT,new String[]{resourcesInfo.getPdfContent()!=null?resourcesInfo.getPdfContent():""});
        map.put(INDEX_KNOWLEDGE_SCHEDULE_PATH,new String[]{resourcesInfo.getKnowledgeAndSchedulePath()!=null?resourcesInfo.getKnowledgeAndSchedulePath():""});

        Integer[] ktl=resourcesInfo.getKnowledgeTags();
        Integer[] tsl=resourcesInfo.getBookVersionIds();
        if(integerToString(ktl)!=null){
            //知识点
            map.put(INDEX_KNOWLEDGE_TAGS,integerToString(ktl));
        }
        if(integerToString(tsl)!=null){
            //教学进度点
            map.put(INDEX_TEACH_SCHEDULE_IDS,integerToString(tsl));
        }

        return map;
    }
    private static String[] integerToString(Integer[] ktl){
        if (ktl!= null && ktl.length > 0) {
            String ids = "";
            for (Integer id : ktl) {
                ids += " " + id;
            }

            ids = ids.substring(1);

            return new String[] { ids };
        }
        return new String[0];
    }
    /**
     * 根据索引查询返回MAP创建资源信息对象
     *
     * @param map
     *            索引查询返回MAP
     * @return 创建资源信息对象
     * @throws Exception
     */
    public static ResourceInfo getResourceInfoByMap(Map<String, Object> map) {
        ResourceInfo resourcesInfo = null;
        if (map != null) {
            resourcesInfo = new ResourceInfo();
            resourcesInfo .setId(isConverType(map,INDEX_ID));
            if(StringUtils.isNotBlank((String)map.get(INDEX_NAME))){
                resourcesInfo.setName((String)map.get(INDEX_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_DESCRIPTION))){
                resourcesInfo.setDescription((String)map.get(INDEX_DESCRIPTION));
            }
            resourcesInfo.setType(isConverType(map,INDEX_TYPE));
            resourcesInfo.setFormat(isConverType(map,INDEX_FORMAT));
            resourcesInfo.setMarrow(isConverType(map,INDEX_MARROW));
            resourcesInfo.setUploadTime(ParseDate.parse((String)map.get(INDEX_UPLOAD_TIME)) );
            resourcesInfo.setUploadUserId(isConverType(map,INDEX_UPLOAD_USER_ID));
            resourcesInfo.setStorageSize(Long.valueOf(isConverType(map,INDEX_STORAGE_SIZE)==null?101:isConverType(map,INDEX_STORAGE_SIZE)));
            resourcesInfo.setPageSize(isConverType(map,INDEX_PAGE_SIZE));
            resourcesInfo.setTimeLength(isConverType(map,INDEX_TIME_LENGTH));
            resourcesInfo.setSource(isConverType(map,INDEX_SOURCE));
            resourcesInfo.setStatus(isConverType(map,INDEX_STATUS));
            resourcesInfo.setClsssVideo(isConverType(map, INDEX_CLASS_VIDEO));
            resourcesInfo.setSubjectTagId(isConverType(map,INDEX_SUBJECT_TAG_ID) );
            resourcesInfo.setStageTagId(isConverType(map,INDEX_STAGE_TAG_ID) );
            if(StringUtils.isNotBlank((String)map.get(INDEX_FILE_OLD_NAME))){
                resourcesInfo.setFileOldName((String)map.get(INDEX_FILE_OLD_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_FILE_PATH))){
                resourcesInfo.setFilePath ((String)map.get(INDEX_FILE_PATH));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_FILE_CONVER_PATH))){
                resourcesInfo.setFileConverPath((String)map.get(INDEX_FILE_CONVER_PATH));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_FRONTCOVER_URL))){
                resourcesInfo.setFrontcoverUrl((String)map.get(INDEX_FRONTCOVER_URL));
            }
            resourcesInfo.setOptUserId(isConverType(map,INDEX_OPT_USERID) );
            resourcesInfo.setOptTime(ParseDate.parse((String)map.get(INDEX_OPT_TIME)) );

            if(StringUtils.isNotBlank((String)map.get(INDEX_FILE_NEW_NAME))){
                resourcesInfo.setFileNewName((String)map.get(INDEX_FILE_NEW_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_STAGE_TAG_NAME))){
                resourcesInfo.setStageTagName((String)map.get(INDEX_STAGE_TAG_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_SUBJECT_TAG_NAME))){
                resourcesInfo.setSubjectTagName((String)map.get(INDEX_SUBJECT_TAG_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_EXTEND_NAME))){
                resourcesInfo.setExtendName((String)map.get(INDEX_EXTEND_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_CONTENT))){
                resourcesInfo.setContent((String)map.get(INDEX_CONTENT));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_TAG_FULL_PATH))){
                resourcesInfo.setTagFullPath((String)map.get(INDEX_TAG_FULL_PATH));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_KNOWLEDGE_NAME))){
                resourcesInfo.setKnowledgeNames((String)map.get(INDEX_KNOWLEDGE_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_BOOK_VERSION_NAME))){
                resourcesInfo.setBookVersionNames((String)map.get(INDEX_BOOK_VERSION_NAME));
            }
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_USE_TIMES)))){
                resourcesInfo.setUseTimes(isConverType(map,INDEX_USE_TIMES));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_USER_USE_ID))){
                resourcesInfo.setUserUseIds((String)map.get(INDEX_USER_USE_ID));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_USER_COLLECTION_ID))){
                resourcesInfo.setUserCollectionIds((String)map.get(INDEX_USER_COLLECTION_ID));
            }
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_KLB_TYPE)))){
                resourcesInfo.setKlbType(isConverType(map,INDEX_KLB_TYPE));
            }
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_TYPE_NAME)))){
                resourcesInfo.setTypeName((String) map.get(INDEX_TYPE_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_DOCUMENT_CONTENT))){
                resourcesInfo.setPdfContent((String)map.get(INDEX_DOCUMENT_CONTENT));
            }
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_SCHOOL_ID)))){
                resourcesInfo.setSchoolId(isConverType(map,INDEX_SCHOOL_ID));
            }
            Object obj = map.get(INDEX_KNOWLEDGE_SCHEDULE_PATH);
            if(obj!=null){
                resourcesInfo.setKnowledgeAndSchedulePath((String)obj);
            }
            resourcesInfo.setKnowledgeTags(getIntegerArray((String) map.get(INDEX_KNOWLEDGE_TAGS)));
            resourcesInfo.setBookVersionIds(getIntegerArray((String) map.get(INDEX_TEACH_SCHEDULE_IDS)));
            //类型名称
            resourcesInfo.setTypeName(DataDictionaryUtil.getInstance().getDictionaryName(6, resourcesInfo.getType()));
        }
        return resourcesInfo;
    }
    private static Integer[] getIntegerArray(String tagsId) {
        if(StringUtils.isNotBlank(tagsId)){
            String[] tagsArr=tagsId.trim().split(" ");
            Integer[] tagsInt=new Integer[tagsArr.length];
            for (int i = 0; i < tagsArr.length; i++) {
                if(StringUtils.isNotBlank(tagsArr[i]))  {
                    tagsInt[i]=Integer.parseInt(tagsArr[i]);
                }
            }
            return tagsInt;
        }
        return new Integer[0];
    }
    private static Integer  isConverType(Map<String, Object> map,String key){
        Object obj=	map.get(key);
        if(obj!=null && obj instanceof Integer){
            return (Integer) map.get(key);
        }else if(obj!=null&& obj instanceof String ){
            String val=(String)map.get(key);
            if(StringUtils.isNotBlank(val)) {
                return Integer.parseInt((String)map.get(key));
            }
        }
        return null;
    }
    public static HashMap<String, Object[]> converResourcesInfotMap(
            Map<String, Object> indexMap) {
        HashMap<String, Object[]> resourcesInfoMap = new HashMap<String, Object[]>();

        initResourcesInfoValue(INDEX_ID, resourcesInfoMap, indexMap.get(INDEX_ID));
        initResourcesInfoValue(INDEX_NAME, resourcesInfoMap, indexMap.get(INDEX_NAME));
        initResourcesInfoValue(INDEX_DESCRIPTION, resourcesInfoMap, indexMap.get(INDEX_DESCRIPTION));
        initResourcesInfoValue(INDEX_TYPE, resourcesInfoMap, indexMap.get(INDEX_TYPE));
        initResourcesInfoValue(INDEX_FORMAT, resourcesInfoMap, indexMap.get(INDEX_FORMAT));
        initResourcesInfoValue(INDEX_MARROW, resourcesInfoMap, indexMap.get(INDEX_MARROW));
        initResourcesInfoValue(INDEX_UPLOAD_TIME, resourcesInfoMap, indexMap.get(INDEX_UPLOAD_TIME));
        initResourcesInfoValue(INDEX_UPLOAD_USER_ID, resourcesInfoMap, indexMap.get(INDEX_UPLOAD_USER_ID));
        initResourcesInfoValue(INDEX_STORAGE_SIZE, resourcesInfoMap, indexMap.get(INDEX_STORAGE_SIZE));
        initResourcesInfoValue(INDEX_PAGE_SIZE, resourcesInfoMap, indexMap.get(INDEX_PAGE_SIZE));
        initResourcesInfoValue(INDEX_TIME_LENGTH, resourcesInfoMap, indexMap.get(INDEX_TIME_LENGTH));
        initResourcesInfoValue(INDEX_SOURCE, resourcesInfoMap, indexMap.get(INDEX_SOURCE));
        initResourcesInfoValue(INDEX_STATUS, resourcesInfoMap, indexMap.get(INDEX_STATUS));
        initResourcesInfoValue(INDEX_CLASS_VIDEO, resourcesInfoMap, indexMap.get(INDEX_CLASS_VIDEO));
        initResourcesInfoValue(INDEX_KNOWLEDGE_TAGS, resourcesInfoMap, indexMap.get(INDEX_KNOWLEDGE_TAGS));
        initResourcesInfoValue(INDEX_TEACH_SCHEDULE_IDS, resourcesInfoMap, indexMap.get(INDEX_TEACH_SCHEDULE_IDS));
        initResourcesInfoValue(INDEX_SUBJECT_TAG_ID, resourcesInfoMap, indexMap.get(INDEX_SUBJECT_TAG_ID));
        initResourcesInfoValue(INDEX_STAGE_TAG_ID, resourcesInfoMap, indexMap.get(INDEX_STAGE_TAG_ID));
        initResourcesInfoValue(INDEX_FILE_OLD_NAME, resourcesInfoMap, indexMap.get(INDEX_FILE_OLD_NAME));
        initResourcesInfoValue(INDEX_FILE_PATH, resourcesInfoMap, indexMap.get(INDEX_FILE_PATH));
        initResourcesInfoValue(INDEX_FILE_CONVER_PATH, resourcesInfoMap, indexMap.get(INDEX_FILE_CONVER_PATH));
        initResourcesInfoValue(INDEX_FRONTCOVER_URL, resourcesInfoMap, indexMap.get(INDEX_FRONTCOVER_URL));
        initResourcesInfoValue(INDEX_OPT_USERID, resourcesInfoMap, indexMap.get(INDEX_OPT_USERID));
        initResourcesInfoValue(INDEX_OPT_TIME, resourcesInfoMap, indexMap.get(INDEX_OPT_TIME));
        initResourcesInfoValue(INDEX_FILE_NEW_NAME, resourcesInfoMap, indexMap.get(INDEX_FILE_NEW_NAME));
        initResourcesInfoValue(INDEX_STAGE_TAG_NAME, resourcesInfoMap, indexMap.get(INDEX_STAGE_TAG_NAME));
        initResourcesInfoValue(INDEX_SUBJECT_TAG_NAME, resourcesInfoMap, indexMap.get(INDEX_SUBJECT_TAG_NAME));
        initResourcesInfoValue(INDEX_EXTEND_NAME, resourcesInfoMap, indexMap.get(INDEX_EXTEND_NAME));
        initResourcesInfoValue(INDEX_CONTENT, resourcesInfoMap, indexMap.get(INDEX_CONTENT));
        initResourcesInfoValue(INDEX_KNOWLEDGE_NAME, resourcesInfoMap, indexMap.get(INDEX_KNOWLEDGE_NAME));
        initResourcesInfoValue(INDEX_BOOK_VERSION_NAME, resourcesInfoMap, indexMap.get(INDEX_BOOK_VERSION_NAME));
        initResourcesInfoValue(INDEX_USE_TIMES, resourcesInfoMap, indexMap.get(INDEX_USE_TIMES));
        initResourcesInfoValue(INDEX_USER_USE_ID, resourcesInfoMap, indexMap.get(INDEX_USER_USE_ID));
        initResourcesInfoValue(INDEX_TAG_FULL_PATH, resourcesInfoMap, indexMap.get(INDEX_TAG_FULL_PATH));
        initResourcesInfoValue(INDEX_USER_COLLECTION_ID, resourcesInfoMap, indexMap.get(INDEX_USER_COLLECTION_ID));
        initResourcesInfoValue(INDEX_KLB_TYPE, resourcesInfoMap, indexMap.get(INDEX_KLB_TYPE));
        initResourcesInfoValue(INDEX_TYPE_NAME, resourcesInfoMap, indexMap.get(INDEX_TYPE_NAME));
        initResourcesInfoValue(INDEX_DOCUMENT_CONTENT, resourcesInfoMap, indexMap.get(INDEX_DOCUMENT_CONTENT));
        initResourcesInfoValue(INDEX_KNOWLEDGE_SCHEDULE_PATH,resourcesInfoMap,indexMap.get(INDEX_KNOWLEDGE_SCHEDULE_PATH));
        initResourcesInfoValue(INDEX_SCHOOL_ID,resourcesInfoMap,indexMap.get(INDEX_SCHOOL_ID));
        return resourcesInfoMap;
    }

    private static void initResourcesInfoValue(String key, HashMap<String, Object[]> resourcesInfoMap, Object object) {
        if (object != null && !object.equals("")) {
            resourcesInfoMap.put(key, new Object[] { object });
        }
    }
}
