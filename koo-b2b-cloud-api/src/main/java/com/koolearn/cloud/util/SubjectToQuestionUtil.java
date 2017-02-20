package com.koolearn.cloud.util;

import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.klb.tags.entity.Tags;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gehaisong on 2016/4/2.
 */
public class SubjectToQuestionUtil {
    public static    HashMap<String,Integer>   subjectQuestionMap=new HashMap<String, Integer>();
    public static final String RANGE_SUBJECT_TYPE_CZYW="初中语文";
    public static final String RANGE_SUBJECT_TYPE_GZYW="高中语文";
    public static final String RANGE_SUBJECT_TYPE_CZSX="初中数学";
    public static final String RANGE_SUBJECT_TYPE_GZSX="高中数学";
    public static final String RANGE_SUBJECT_TYPE_CZYY="初中英语";
    public static final String RANGE_SUBJECT_TYPE_GZYY="高中英语";
    public static final String RANGE_SUBJECT_TYPE_CZWL="初中物理";
    public static final String RANGE_SUBJECT_TYPE_GZWL="高中物理";
    public static final String RANGE_SUBJECT_TYPE_CZHX="初中化学";
    public static final String RANGE_SUBJECT_TYPE_GZHX="高中化学";
    public static final String RANGE_SUBJECT_TYPE_CZZZ="初中政治";
    public static final String RANGE_SUBJECT_TYPE_GZZZ="高中政治";
    public static final String RANGE_SUBJECT_TYPE_CZLS="初中历史";
    public static final String RANGE_SUBJECT_TYPE_GZLS="高中历史";
    public static final String RANGE_SUBJECT_TYPE_CZDL="初中地理";
    public static final String RANGE_SUBJECT_TYPE_GZDL="高中地理";
    public static final String RANGE_SUBJECT_TYPE_CZSW="初中生物";
    public static final String RANGE_SUBJECT_TYPE_GZSW="高中生物";

    public static final String RANGE_SUBJECT_TYPE_XXYW="小学语文";
    public static final String RANGE_SUBJECT_TYPE_XXSX="小学数学";
    public static final String RANGE_SUBJECT_TYPE_XXYY="小学英语";
    static {
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZYW,99901);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZYW,99902);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZSX,99903);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZSX,99904);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZYY,99905);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZYY,99906);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZWL,99907);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZWL,99908);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZHX,99909);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZHX,99910);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZZZ,99911);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZZZ,99912);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZLS,99913);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZLS,99914);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZDL,99915);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZDL,99916);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_CZSW,99917);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_GZSW,99918);

        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_XXYW,99919);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_XXSX,99920);
        subjectQuestionMap.put(RANGE_SUBJECT_TYPE_XXYY,99921);
    }

    /**
     * 根据学科学段获取 数据字典表中的 题型type
     */
    public static List<Dictionary> getQuestionShowTtpe(Integer subjectId, Integer rangeId){
        if(subjectId==null || rangeId==null)return null;
        Tags subject=KlbTagsUtil.getInstance().tagById(subjectId);
        Tags range=KlbTagsUtil.getInstance().tagById(rangeId);
        int type=subjectQuestionMap.get(range.getName()+subject.getName());
        return DataDictionaryUtil.getInstance().getDataDictionaryListByType(type);
    }
}
