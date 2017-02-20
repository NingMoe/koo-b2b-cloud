package com.koolearn.cloud.util;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

/**
 * 索引名成工具类.
 */
public class IndexNameUtils {

    public static final String RESOURCE_INDEX_NAME = "cloud.resource.index";
    public static final String QUESTION_INDEX_NAME = "cloud.question.index";
    public static final String PAPER_INDEX_NAME = "cloud.paper.index";

    private static String resourceIndexName;
    private static String questionIndexName;
    private static String paperIndexName;

    static {
        try {
            resourceIndexName = PropertiesConfigUtils.getProperty(RESOURCE_INDEX_NAME);
            resourceIndexName = PropertiesConfigUtils.getProperty(RESOURCE_INDEX_NAME);
            paperIndexName = PropertiesConfigUtils.getProperty(PAPER_INDEX_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得资源索引名
     *
     * @return
     */
    public static String getResourceIndexName() {
        return resourceIndexName;
    }

    public static String getQuestionIndexName() {
        return questionIndexName;
    }

    public static String getValueByKey(String key) {
        return PropertiesConfigUtils.getProperty(key);
    }

}
