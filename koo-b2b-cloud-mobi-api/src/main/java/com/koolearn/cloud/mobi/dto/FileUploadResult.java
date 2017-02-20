package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;

/**
 * Created by haozipu on 2016/8/1.
 */
public class FileUploadResult implements Serializable {

    private String filePath;
    private String msg;
    private String error;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
