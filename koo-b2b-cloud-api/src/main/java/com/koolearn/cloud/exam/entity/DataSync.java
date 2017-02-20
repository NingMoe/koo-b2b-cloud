package com.koolearn.cloud.exam.entity;

import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.exam.structure.entity.TeTestPaperInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import java.io.Serializable;
import java.util.*;

/**
 * Created by gehaisong on 2016/5/11.
 */
public class DataSync implements Serializable {
    public boolean canNextSycn=true; //默认是第一次同步：是否可以开始下一次同步
    private String cloudPlatform;//标签名[标签id]
    private String currentSynctime;//同步试卷更新时间
    private String startSynctime;//开始同步时间
    private boolean status ;
    private String statusStr="同步开始......" ;
    private List<String> syncInfoList=new ArrayList<String>();
    private List<DataSyncError> errorPaper=new ArrayList<DataSyncError>();//错误试卷记录试卷编码和试卷名称
    private List<DataSyncError> errorQuestion=new ArrayList<DataSyncError>();//错误题目记录编码
    private Map<String,List<TeTestPaperInfo>> syncPapgeCodeCountMap=new HashMap<String,List<TeTestPaperInfo>> ();//记录相同code的试卷
    private int errorPaperNum=0;
    private int successPaperNum=0;
    private int errorQuestionNum=0;
    private int successQuestionNum=0;
    public static  String getCurrrentCacheKey(){
        String     currentSyncCacheKey= CacheTools.getCache(GlobalConstant.SYNCHRO_CURRENT_KEY, String.class);//获取本次同步缓存的key
        currentSyncCacheKey= StringUtils.isBlank(currentSyncCacheKey)?"N!@#$%&*O":currentSyncCacheKey;
        return currentSyncCacheKey;
    }
    public static DataSync updateErrorNumCache(String p,boolean isSuccess){
        String currentSyncCacheKey= DataSync.getCurrrentCacheKey();
        DataSync  currentDataSync=CacheTools.getCache(currentSyncCacheKey,DataSync.class);
        if(currentDataSync==null) return null;
        if("p".equals(p)){
            if(isSuccess){
                int pNum=currentDataSync.getSuccessPaperNum();
                currentDataSync.setSuccessPaperNum(++pNum);
            }else{
                int pNum=currentDataSync.getErrorPaperNum();
                currentDataSync.setErrorPaperNum(++pNum);
            }

        }else{
            if(isSuccess){
                int qNum=currentDataSync.getSuccessQuestionNum();
                currentDataSync.setSuccessQuestionNum(++qNum);
            }else{
                int qNum=currentDataSync.getErrorQuestionNum();
                currentDataSync.setErrorQuestionNum(++qNum);
            }
        }
        CacheTools.addCache(currentSyncCacheKey,currentDataSync);
        return currentDataSync;
    }
    public static DataSync updateDataSuncFromCache(String errorCode,String p,Exception  exception){
        // DataSync.updateDataSuncFromCache("","p");
        String     currentSyncCacheKey= CacheTools.getCache(GlobalConstant.SYNCHRO_CURRENT_KEY, String.class);//获取本次同步缓存的key
        currentSyncCacheKey= StringUtils.isBlank(currentSyncCacheKey)?"N!@#$%&*O":currentSyncCacheKey;
        DataSync  currentDataSync=CacheTools.getCache(currentSyncCacheKey,DataSync.class);
        if(currentDataSync==null) return null;
        String errorInfo=exception!=null?exception.getMessage():"";
        DataSyncError dse=new DataSyncError();
        dse.setErrorCode(errorCode);
        dse.setErrorInfo(errorInfo);
        if("p".equals(p)){
            currentDataSync.getErrorPaper().add(dse);
        }else{
            currentDataSync.getErrorQuestion().add(dse);
        }
        CacheTools.addCache(currentSyncCacheKey,currentDataSync);
        return currentDataSync;
    }
    public static DataSync updateDataSuncFromCache(String errorCode,Exception  exception){
        return updateDataSuncFromCache(errorCode,"question",exception);
    }
    public String getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(String cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public String getCurrentSynctime() {
        return currentSynctime;
    }

    public void setCurrentSynctime(String currentSynctime) {
        this.currentSynctime = currentSynctime;
    }

    public List<String> getSyncInfoList() {
        if( syncInfoList.size()>20){
            String[] infoArrdest=new String[10];
            Collections.reverse(syncInfoList);
            for(int i=0;i<infoArrdest.length;i++){
                infoArrdest[i]=syncInfoList.get(i);
            }
            syncInfoList=new ArrayList<String>();
            CollectionUtils.addAll(syncInfoList,infoArrdest);
            Collections.reverse(syncInfoList);
        }
        return syncInfoList;
    }

    public void setSyncInfoList(List<String> syncInfoList) {
        this.syncInfoList = syncInfoList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public boolean isCanNextSycn() {
        return canNextSycn;
    }

    public void setCanNextSycn(boolean canNextSycn) {
        this.canNextSycn = canNextSycn;
    }

    public List<DataSyncError> getErrorPaper() {
        return errorPaper;
    }

    public void setErrorPaper(List<DataSyncError> errorPaper) {
        this.errorPaper = errorPaper;
    }

    public List<DataSyncError> getErrorQuestion() {
        return errorQuestion;
    }

    public void setErrorQuestion(List<DataSyncError> errorQuestion) {
        this.errorQuestion = errorQuestion;
    }

    public Map<String, List<TeTestPaperInfo>> getSyncPapgeCodeCountMap() {
        return syncPapgeCodeCountMap;
    }

    public void setSyncPapgeCodeCountMap(Map<String, List<TeTestPaperInfo>> syncPapgeCodeCountMap) {
        this.syncPapgeCodeCountMap = syncPapgeCodeCountMap;
    }

    public int getErrorQuestionNum() {
        return errorQuestionNum;
    }

    public void setErrorQuestionNum(int errorQuestionNum) {
        this.errorQuestionNum = errorQuestionNum;
    }

    public int getErrorPaperNum() {
        return errorPaperNum;
    }

    public void setErrorPaperNum(int errorPaperNum) {
        this.errorPaperNum = errorPaperNum;
    }

    public int getSuccessQuestionNum() {
        return successQuestionNum;
    }

    public void setSuccessQuestionNum(int successQuestionNum) {
        this.successQuestionNum = successQuestionNum;
    }

    public int getSuccessPaperNum() {
        return successPaperNum;
    }

    public void setSuccessPaperNum(int successPaperNum) {
        this.successPaperNum = successPaperNum;
    }

    public String getStartSynctime() {
        return startSynctime;
    }

    public void setStartSynctime(String startSynctime) {
        this.startSynctime = startSynctime;
    }
}
