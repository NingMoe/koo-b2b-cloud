package com.koolearn.cloud.school.entity;

import java.io.Serializable;

/**
 * Created by fn on 2016/10/31.
 */
public class SchoolPage implements Serializable {
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 分页总行数
     */
    private Integer totalPage;
    /**
     * 每页行数
     */
    private Integer pageSize;
    /**
     * 总行数
     */
    private Integer totalLine;

    public Integer getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(Integer totalLine) {
        this.totalLine = totalLine;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        if( null != getTotalLine() && null != getPageSize() ){
            int pageNum = getTotalLine() / getPageSize();
            int last = getTotalLine() % getPageSize();
            if( last > 0 ){
                pageNum +=1;
            }
            totalPage = pageNum;
        }
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
