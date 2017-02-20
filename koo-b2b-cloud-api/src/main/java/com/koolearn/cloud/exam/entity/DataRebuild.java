package com.koolearn.cloud.exam.entity;

import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**资源重建状态
 * Created by gehaisong on 2016/5/11.
 */
public class DataRebuild implements Serializable {
    private String currentRebuildtime;//重建时间
    private String currentRebuildEndtime;//重建结束时间
    private int currentPage=0;//当前同步时间点
    private long rebuilderCount=0;//已同步条数
    private List<ResourceInfo> errorResourceList=new ArrayList<ResourceInfo>();//
    public void countAdd(){
        this.rebuilderCount++;
    }
    public void pageAdd(){
        this.currentPage++;
    }
    public String getCurrentRebuildtime() {
        return currentRebuildtime;
    }

    public void setCurrentRebuildtime(String currentRebuildtime) {
        this.currentRebuildtime = currentRebuildtime;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getRebuilderCount() {
        return rebuilderCount;
    }

    public void setRebuilderCount(long rebuilderCount) {
        this.rebuilderCount = rebuilderCount;
    }

    public List<ResourceInfo> getErrorResourceList() {
        return errorResourceList;
    }

    public void setErrorResourceList(List<ResourceInfo> errorResourceList) {
        this.errorResourceList = errorResourceList;
    }

    public String getCurrentRebuildEndtime() {
        return currentRebuildEndtime;
    }

    public void setCurrentRebuildEndtime(String currentRebuildEndtime) {
        this.currentRebuildEndtime = currentRebuildEndtime;
    }
}
