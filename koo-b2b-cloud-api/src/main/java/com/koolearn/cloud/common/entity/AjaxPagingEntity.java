package com.koolearn.cloud.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gehaisong on 2016/11/16.
 */
public class AjaxPagingEntity implements Serializable {
    private Integer currentPage;
    private Long totalPage;
    private List dataList=new ArrayList();

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
}