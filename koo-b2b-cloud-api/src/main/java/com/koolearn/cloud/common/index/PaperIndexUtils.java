package com.koolearn.cloud.common.index;

import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.util.ParseDate;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PaperIndexUtils {
    public static final String INDEX_ID = "id";// 索引-ID
    public static final String INDEX_PAPER_NAME = "paper_name";// 索引-试卷名称
    public static final String INDEX_PAPER_CODE = "paper_code"; // 索引-题目编码
    public static final String INDEX_POINTS = "points"; // 索引-状态
    public static final String INDEX_BROWSE_TIMES="browse_times";//试卷流量
    public static final String INDEX_NEW_TEACHER_ID = "teacher_id"; // 索引-试卷创建人id
    public static final String INDEX_TEACHER_NAME = "teacher_name"; // 索引- 试卷创建人
    public static final String INDEX_SCHOOL_ID = "school_id"; // 索引- 学校id
    public static final String INDEX_CREATE_DATE = "create_date";// 创建时间
    public static final String INDEX_UPDATE_TIME="update_time";//最后更新时间
    public static final String INDEX_JOINSELF_USER="joinself_user"; //用户加入我的试卷库的老师id(格式：11_12_13)
    public static final String INDEX_FROMWH = "fromwh";// 索引-0：新东方同步试卷 1:老师创建试卷 2.学生自测试卷
    public static final String INDEX_STATUS="status";//默认0：可用  1 删除
    public static final String INDEX_TAG_FULL_PATH="tag_full_path";//学科学段难道题型等tagID(格式：10_12_13)
    public static final String INDEX_SUBJECT_ID="subject_id";//学科id
    public static final String INDEX_SUBJECT="subject";//学科
    public static final String INDEX_RANGE="range";//学段
    public static final String INDEX_RANGE_ID="range_id";//学段id
    public static final String INDEX_SEARCH_CONTENT = "searchContent";// 索引-关键字搜索字段

    /**
     * 生成创建试卷索引  所需 map对象
     * @param
     * @return
     * @throws Exception
     */
    public static HashMap<String, Object[]> initPaperMap(
            TestPaper paper)
            throws Exception {
        HashMap<String, Object[]> map = null;

        map = new HashMap<String, Object[]>();
        map.put(INDEX_ID, new Integer[] { paper.getId() });
        map.put(INDEX_PAPER_NAME, new String[] { paper.getPaperName() });
        map.put(INDEX_PAPER_CODE, new String[] { paper.getPaperCode()});
        map.put(INDEX_POINTS, new Double[] { paper.getPoints() });
        map.put(INDEX_BROWSE_TIMES, new Integer[] { paper.getBrowseTimes()});
        map.put(INDEX_NEW_TEACHER_ID, new Integer[] { paper.getTeacherId() });
        map.put(INDEX_TEACHER_NAME, new String[] { paper.getTeacherName() });
        map.put(INDEX_SCHOOL_ID, new Integer[] { paper.getSchoolId() });
        map.put(INDEX_CREATE_DATE,new String[] { ParseDate.formatByDate(paper.getCreateTime(), ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_UPDATE_TIME,new String[] { ParseDate.formatByDate(paper.getUpdateTime(), ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS)  });
        map.put(INDEX_JOINSELF_USER, new String[] {paper.getJoinselfUser() });
        map.put(INDEX_FROMWH, new Integer[] { paper.getFromwh()});
        map.put(INDEX_STATUS, new Integer[] { paper.getStatus()});
        map.put(INDEX_TAG_FULL_PATH, new String[] {paper.getTagFullPath() });
        map.put(INDEX_SUBJECT_ID, new Integer[] {paper.getSubjectId()});
        map.put(INDEX_RANGE_ID, new Integer[] {paper.getRangeId()});
        map.put(INDEX_SUBJECT,new String[] { paper.getSubject()});
        map.put(INDEX_RANGE,new String[] { paper.getRange()});
//        map.put(INDEX_SEARCH_CONTENT, new String[] {paper.getPaperName()+ paper.getSubject()+ paper.getRange() });
        map.put(INDEX_SEARCH_CONTENT, new String[] {paper.getPaperName() });
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
    public static TestPaper getPaperByMap(Map<String, Object> map)
            throws Exception {
        TestPaper paper = null;
        if (map != null) {
            paper=new TestPaper();
            paper .setId(isConverType(map,INDEX_ID));
            if(StringUtils.isNotBlank((String)map.get(INDEX_PAPER_NAME))){
            paper.setPaperName((String)map.get(INDEX_PAPER_NAME));
            }
            if(StringUtils.isNotBlank((String)map.get(INDEX_PAPER_CODE))){
            paper.setPaperCode((String)map.get(INDEX_PAPER_CODE));
            }
            paper.setPoints(isConverTypeToDouble(map,INDEX_POINTS));
            paper.setBrowseTimes(isConverType(map,INDEX_BROWSE_TIMES));
            paper.setTeacherId(isConverType(map,INDEX_NEW_TEACHER_ID));
            if(StringUtils.isNotBlank((String)map.get(INDEX_TEACHER_NAME))){
                paper.setTeacherName((String)map.get(INDEX_TEACHER_NAME));
            }
            paper.setSchoolId(isConverType(map,INDEX_SCHOOL_ID));
            paper.setCreateTime(ParseDate.parse((String)map.get(INDEX_CREATE_DATE))  );
            paper.setUpdateTime(ParseDate.parse((String)map.get(INDEX_UPDATE_TIME))  );
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_JOINSELF_USER)))){
               paper.setJoinselfUser((String)map.get(INDEX_JOINSELF_USER));
            }
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_TAG_FULL_PATH)))){
               paper.setTagFullPath((String)map.get(INDEX_TAG_FULL_PATH));
            }
            paper.setFromwh(isConverType(map,INDEX_FROMWH));
            paper.setStatus(isConverType(map,INDEX_STATUS));
            paper.setSubjectId(isConverType(map,INDEX_SUBJECT_ID));
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_SUBJECT)))){
                paper.setSubject((String)map.get(INDEX_SUBJECT));
            }
            paper.setRangeId(isConverType(map,INDEX_RANGE_ID));
            if(StringUtils.isNotBlank(String.valueOf(map.get(INDEX_RANGE)))){
                paper.setRange((String)map.get(INDEX_RANGE));
            }
        }
        return paper;
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
    private static Double  isConverTypeToDouble(Map<String, Object> map,String key){
        Object obj=	map.get(key);
        if(obj!=null && obj instanceof Double){
            return (Double) map.get(key);
        }
        return null;
    }

}
