package com.koolearn.cloud.common.index;

import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ParseDate;
import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;

public class QuestionIndexUtils {
    public static final String INDEX_ID = "id";// 索引-ID
    public static final String INDEX_TEID = "te_id";// 索引-题目父id
    public static final String INDEX_NAME = "name";// 索引-名称
    public static final String INDEX_CODE = "code"; // 索引-题目编码
    public static final String INDEX_STATUS = "status"; // 索引-状态
    public static final String INDEX_NEW_VERSION = "new_version"; // 索引- 是否是最新版本 1为最新版本 0为非最新版本
    public static final String INDEX_ISSUBJECTIVED = "issubjectived"; // 索引- 是否客观题 1 为客观 0 为非客观
    public static final String INDEX_INPUT_TYPE = "input_type"; // 索引- 试题录入类型 1 手工录入 2 批量录入
    public static final String INDEX_QUESTION_TYPE_ID = "question_type_id";// 索引-题型id（搜索的题型tag在fullpath）
    public static final String INDEX_CREATE_DATE = "create_date";// 创建时间
    public static final String INDEX_LAST_UPDATE_TIME="last_update_date";//最后更新时间
    public static final String INDEX_LAST_UPDATE_BY="last_update_by";//最后更新人
    public static final String INDEX_USE_TIMES="use_times";//所有用户使用的总次数次数
    public static final String INDEX_USER_USE_ID="user_use_id"; //用户使用id(格式：11_12_13)
    public static final String INDEX_USER_COLLECTION_ID="user_collection_id";//用户收藏id(格式：11_12_13)
    public static final String INDEX_SEARCH_CONTENT = "searchContent";// 索引-关键字搜索字段(explan,remark,提干 、name、等)
    public static final String INDEX_TAG_FULL_PATH="tag_full_path";//学科学段难道题型等tagID(格式：10_12_13)

    /**
     * 生成创建题目索引  所需 map对象
     * @param
     * @return
     * @throws Exception
     */
    public static HashMap<String, Object[]> initQuestionMap(
            Question question)
            throws Exception {
        HashMap<String, Object[]> map = null;

        map = new HashMap<String, Object[]>();
        map.put(INDEX_ID, new Integer[] { question.getId() });
        map.put(INDEX_TEID, new Integer[] { question.getTeId() });
        map.put(INDEX_NAME, new String[] { question.getName() });
        map.put(INDEX_CODE, new String[] { question.getCode() });
        map.put(INDEX_STATUS, new Integer[] { question.getStatus() });
        map.put(INDEX_NEW_VERSION, new Integer[] { question.getNewVersion() });
        map.put(INDEX_ISSUBJECTIVED, new Integer[] { question.getIssubjectived() });
        map.put(INDEX_QUESTION_TYPE_ID, new Integer[] { question.getQuestionTypeId() });
        map.put(INDEX_INPUT_TYPE, new Integer[] { question.getInputType()});
        map.put(INDEX_LAST_UPDATE_TIME, new String[] { ParseDate.formatByDate(question.getLastUpdateDate(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_CREATE_DATE,new String[] { ParseDate.formatByDate(question.getCreateDate(), ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_LAST_UPDATE_BY,new String[] { question.getCreateBy()});
        //是否要设置默认标签-默认设置难道中
        if(!"false".equals(GlobalConstant.DEFAULT_ADD_QUESTION_TAGS)){
            //默认题目难度中：外网：104403_2321_2301_20
            question.setTagPath(question.getTagPath()+","+GlobalConstant.DEFAULT_ADD_QUESTION_TAGS);
        }
        map.put(INDEX_TAG_FULL_PATH, new String[] { question.getTagPath() });
        map.put(INDEX_USE_TIMES, new Integer[] {question.getUseTimes()});
        map.put(INDEX_USER_USE_ID, new String[] { question.getUserUseIds() });
        map.put(INDEX_USER_COLLECTION_ID, new String[] { question.getUserCollectionIds() });
        map.put(INDEX_SEARCH_CONTENT, new String[] {question.getSearchContent() });
        return map;
    }

    /**
     * 根据索引查询返回MAP创建题目信息对象
     * 题目对象还要查缓存，这里至返回 id、使用，收藏等字段
     * @param map
     *            索引查询返回MAP
     * @return 创建题目信息对象
     * @throws Exception
     */
    public static Question getQuestionByMap(Map<String, Object> map)
            throws Exception {
        Question question = null;
        if (map != null) {
            question = new Question();
            question .setId(isConverType(map,INDEX_ID));
            question .setQuestionTypeId(isConverType(map,INDEX_QUESTION_TYPE_ID));
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_USE_TIMES)))){
                question.setUseTimes(isConverType(map,INDEX_USE_TIMES));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_USER_USE_ID))){
                question.setUserUseIds((String)map.get(INDEX_USER_USE_ID));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_USER_COLLECTION_ID))){
                question.setUserCollectionIds((String)map.get(INDEX_USER_COLLECTION_ID));
            }
        }
        return question;
    }

    private static Integer  isConverType(Map<String, Object> map,String key){
        Object obj=	map.get(key);
        if(obj!=null && obj instanceof Integer){
            return (Integer) map.get(key);
        }else if(obj!=null&& obj instanceof String ){
            String val=(String)map.get(key);
            if(StringUtils.isBlank(val)) return null;
            return Integer.parseInt((String)map.get(key));
        }
        return null;
    }
//    public static HashMap<String, Object[]> converResourcesInfotMap(
//            Map<String, Object> indexMap) {
//        HashMap<String, Object[]> resourcesInfoMap = new HashMap<String, Object[]>();
//
//        initResourcesInfoValue(INDEX_ID, resourcesInfoMap, indexMap.get(INDEX_ID));
//        return resourcesInfoMap;
//    }
//
//    private static void initResourcesInfoValue(String key, HashMap<String, Object[]> resourcesInfoMap, Object object) {
//        if (object != null && !object.equals("")) {
//            resourcesInfoMap.put(key, new Object[] { object });
//        }
//    }
}
