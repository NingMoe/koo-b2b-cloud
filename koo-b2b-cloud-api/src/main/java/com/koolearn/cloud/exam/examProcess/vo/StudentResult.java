package com.koolearn.cloud.exam.examProcess.vo;


import java.io.Serializable;

public class StudentResult implements Serializable {
   public String correctRate;//正确率  得分总分比
   public Double correctRateNum;//正确率
   public Integer correctNum;//正确树
   public Integer errorNum;//错题数

    public String getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(String correctRate) {
        this.correctRate = correctRate;
    }

    public Integer getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(Integer correctNum) {
        this.correctNum = correctNum;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    public Double getCorrectRateNum() {
        return correctRateNum;
    }

    public void setCorrectRateNum(Double correctRateNum) {
        this.correctRateNum = correctRateNum;
    }
}
