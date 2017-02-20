package com.koolearn.cloud.util;

import com.koolearn.cloud.dictionary.service.DictionaryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;


/**
 * 获取学科,学段,教材版本 信息工具类.配置bean
 *
 * @version 1.0
 */
public class DataSelectUtil implements ApplicationContextAware {

    private DictionaryService dictionaryService;

    private static DataSelectUtil instance = null;

    public static DataSelectUtil getInstance() {
        return instance;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        dictionaryService = (DictionaryService) applicationContext
                .getBean("dictionaryService");
        DataSelectUtil.instance = this;
    }


    /**
     * 获取进度点学科
     * @param teacherId
     * @return
     */
    public List<SelectDTO> getSubject(int teacherId) {
        return dictionaryService.findTeacherSubject(teacherId);
    }

    /**
     * 获取进度点学段
     * @param teacherId
     * @param subjectId
     * @return
     */
    public List<SelectDTO> getRange(int teacherId, int subjectId) {
        return dictionaryService.findTeacherRange(teacherId, subjectId);
    }

    /**
     * 获取进度点的教材版本
     * @param teacherId
     * @param rangeId
     * @return
     */
    public List<SelectDTO> getBookVersion(int teacherId, int rangeId) {
        return dictionaryService.findTeacherBookVersion(teacherId, rangeId);

    }


    public List<SelectDTO> getSubjectName(Integer teacherId) {
        return dictionaryService.findTeacherSubjectName(teacherId);
    }


}
