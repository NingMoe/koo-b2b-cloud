package com.koolearn.cloud.common.exceldowload;

import java.io.Serializable;

/**
 * Created by gehaisong on 2015/12/17.
 */
public class Knowledge implements Serializable {
    public static final int  KNOWLEDGE_TREE_LENGTH=13;
    //默认知识点最大深度9
    //99861_101813_95651_27071_27068_26996_26995_2464_60
    private String tag0="";//保存叶子节点id
    private String tag1="";//二级知识点  2464   学科名
    private String tag2="";
    private String tag3="";
    private String tag4="";
    private String tag5="";
    private String tag6="";
    private String tag7="";
    private String tag8="";
    private String tag9="";
    private String tag10="";
    private String tag11="";
    private String tag12="";

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
    }

    public String getTag5() {
        return tag5;
    }

    public void setTag5(String tag5) {
        this.tag5 = tag5;
    }

    public String getTag6() {
        return tag6;
    }

    public void setTag6(String tag6) {
        this.tag6 = tag6;
    }

    public String getTag7() {
        return tag7;
    }

    public void setTag7(String tag7) {
        this.tag7 = tag7;
    }

    public String getTag8() {
        return tag8;
    }

    public void setTag8(String tag8) {
        this.tag8 = tag8;
    }

    public String getTag0() {
        return tag0;
    }

    public void setTag0(String tag0) {
        this.tag0 = tag0;
    }

    public String getTag9() {
        return tag9;
    }

    public void setTag9(String tag9) {
        this.tag9 = tag9;
    }

    public String getTag10() {
        return tag10;
    }

    public void setTag10(String tag10) {
        this.tag10 = tag10;
    }

    public String getTag11() {
        return tag11;
    }

    public void setTag11(String tag11) {
        this.tag11 = tag11;
    }

    public String getTag12() {
        return tag12;
    }

    public void setTag12(String tag12) {
        this.tag12 = tag12;
    }

}
