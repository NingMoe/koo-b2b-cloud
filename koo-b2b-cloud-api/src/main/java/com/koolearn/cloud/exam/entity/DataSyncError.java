package com.koolearn.cloud.exam.entity;

import java.io.Serializable;

/**
 * Created by gehaisong on 2016/5/11.
 */
public class DataSyncError implements Serializable {
     private String errorCode ;//错误标题
    private String  errorInfo;// 错误日志

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
