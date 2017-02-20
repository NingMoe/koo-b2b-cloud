package com.koolearn.cloud.school.teacher.vo;

import java.io.Serializable;

/**
 * 教师导入excel数据返回前端的提示信息实体
 * Created by fn on 2016/11/9.
 */
public class TeacherUploadExcelDto implements Serializable {

    private String realName;
    private String mobile;
    private String email;
    private String errorInfo;
    /**
     * 判断是否有异常数据
     */
    private int isError;
    /**
     * 标识错误信息行数
     */
    private int line;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getIsError() {
        return isError;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
