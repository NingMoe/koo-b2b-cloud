package com.koolearn.cloud.common.serializer;

/**
 * Created by fn on 2016/4/15.
 */
public enum RangeTypeEnum {
    /**
     * 小学
     */
    XIAOXUE(1,"小学"),
    /**
     * 初中
     */
    CHUZHONG(2,"初中"),
    /**
     * 高中
     */
    GAOZHONG(3,"高中");

    private int index;
    private String value;

    RangeTypeEnum(int index,String value){
        this.index = index;
        this.value = value;
    }

    public static RangeTypeEnum getSource(int index){
        for (RangeTypeEnum p:RangeTypeEnum.values()) {
            if(p.getIndex()==index){
                return p;
            }
        }
        return null;
    }

    /**
     * 获取index
     * @return index int
     */
    public int getIndex() {
        return index;
    }

    /**
     * 设置index
     * @param index int
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 获取value
     * @return value String
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value
     * @param value String
     */
    public void setValue(String value) {
        this.value = value;
    }

}
