package com.koolearn.cloud.exam.examcore.question.service.impl;
import com.koolearn.cloud.exam.examcore.question.dao.QuestionDao;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dao.ResourceDao;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.framework.common.page.ListPager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class QuestionScheduled  {
    @Autowired
    protected QuestionBaseService questionBaseService;
    @Autowired
    private ResourceDao resourceDao ;
    @Autowired
    private QuestionDao questionDao ;
    public static final  String SCHEDULED_QUESTION_KEY="scheduled_question_key";
    public static final  String SCHEDULED_User_KEY="scheduled_user_key";
    /**
     * 定期更新题目缓存
     * 每月15日凌晨1点 0 0 1 15 * ?
     */
    @Scheduled(cron = "0 0 1 15 * ?")
    public void initQuestionCacheScheduled(){
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                    initQuestionCache();
                }
        });
    }

    /**
     * 定期更新题目收藏缓存
     * 每周五凌晨3点 0 0 3 ? * 6
     */
    @Scheduled(cron = "0 0 3 ? * 6")
    public void initUserCacheScheduled(){
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                initUserCache();
            }
        });
    }
    public void initQuestionCache(){
        try{
            String scheduledStatus =CacheTools.getCache(SCHEDULED_QUESTION_KEY, String.class);
            if(StringUtils.isNotBlank(scheduledStatus)&&!"scheduEnd".equals(scheduledStatus)){
                return;
            }
            CacheTools.addCache(SCHEDULED_QUESTION_KEY,"scheduStarting");
            Long count = questionDao.findAllQuestionCount();
            if (count != null && count > 0) {
                ListPager listPager = new ListPager();
                listPager.setTotalRows(count);
                listPager.setPageSize(1000);
                for (int i = 0; i < listPager.getTotalPage(); i++) {
                    listPager.setPageNo(i);
                    List<Question> qList=questionDao.findAllQuestion(listPager);
                    if(qList!=null&&qList.size()>0){
                        for(Question q:qList){
                            IExamQuestionDto questionDto=questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(),q.getId());
                        }
                    }
                }
                CacheTools.addCache(SCHEDULED_QUESTION_KEY,"scheduEnd");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initUserCache(){
        try{
            String scheduledStatus =CacheTools.getCache(SCHEDULED_User_KEY, String.class);
            if(StringUtils.isNotBlank(scheduledStatus)&&!"scheduEnd".equals(scheduledStatus)){
                return;
            }
            CacheTools.addCache(SCHEDULED_User_KEY,"scheduStarting");
            Long count = questionDao.findAllUserCount();
            if (count != null && count > 0) {
                ListPager listPager = new ListPager();
                listPager.setTotalRows(count);
                listPager.setPageSize(1000);
                for (int i = 0; i < listPager.getTotalPage(); i++) {
                    listPager.setPageNo(i);
                    List<UserEntity> uList=questionDao.findAllUser(listPager);
                    if(uList!=null&&uList.size()>0){
                        for(UserEntity  u:uList){
                            //获取用户全部的题目收藏，收藏时更新缓存
                            List<Integer> collectionIds=CacheTools.getCache(GlobalConstant.USER_COLLECTION_KEY+u.getId(),List.class);
                            if(collectionIds==null){
                                setAllCollectionQuestionByUser(  u.getId());
                            }
                        }
                    }
                    CacheTools.addCache(SCHEDULED_User_KEY,"scheduEnd");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setAllCollectionQuestionByUser(int userId) {
            String isSearchedKey=GlobalConstant.USER_COLLECTION_KEY+userId+"_flag_"+ ParseDate.formatByDate(new Date(), ParseDate.DATE_FORMAT_YYYYMMDDHH);
            List<Integer> collectionIds = resourceDao.getAllCollectionQuestionByUser(userId, GlobalConstant.STATUS_ON,GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
            CacheTools.addCache(GlobalConstant.USER_COLLECTION_KEY + userId, collectionIds);
            CacheTools.addCache(isSearchedKey,"false&&false");
    }
}

