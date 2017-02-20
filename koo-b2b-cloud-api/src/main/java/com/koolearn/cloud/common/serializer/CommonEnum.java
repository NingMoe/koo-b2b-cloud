package com.koolearn.cloud.common.serializer;

/**
 * Created by fn on 2016/4/28.
 */
public class CommonEnum {
    public enum ExamEnum {
        ZUOYE(0,"作业"),
        KAOSHI(1,"作业"),
        KETANG(2,"课堂"),
        KETANGZUOYE( 20 ,"课堂作业");

        private int index;
        private String value;

        ExamEnum(int index,String value){
            this.index = index;
            this.value = value;
        }

        public static ExamEnum getSource(int index){
            for (ExamEnum p:ExamEnum.values()) {
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
            if( null == value ){
                value = "其它";
            }
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


    public enum classesTypeEnum {
        XUEKE(0,"行政班"),
        XIAOZU(3,"小组"),
        XINGZHENG( 1 ,"学科班");

        private int index;
        private String value;

        classesTypeEnum(int index,String value){
            this.index = index;
            this.value = value;
        }

        public static classesTypeEnum getSource(int index){
            for (classesTypeEnum p:classesTypeEnum.values()) {
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
            if( null == value ){
                value = "其它";
            }
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

    /**
     * 获取数字对应关系
     */
    public enum getNumberEnum {
        zore(0,"一"),
        ONE(1,"一"),
        TOW(2,"二"),
        THREE(3,"三"),
        FOUR(4,"四"),
        FIVE(5,"五"),
        SIX(6,"六");

        private int index;
        private String value;

        getNumberEnum( int index,String value){
            this.index = index;
            this.value = value;
        }

        public static getNumberEnum getSource(int index){
            for (getNumberEnum p:getNumberEnum.values()) {
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

    public enum RangeTypeEnum {
        /**
         * 小学
         */
        XIAOXUE( "小学" ,"年级"),
        /**
         * 初中
         */
        CHUZHONG( "初中" ,"初"),
        /**
         * 高中
         */
        GAOZHONG("高中" , "高");

        private String index;
        private String value;

        RangeTypeEnum(String index,String value){
            this.index = index;
            this.value = value;
        }

        public static RangeTypeEnum getSource(String index){
            for (RangeTypeEnum p:RangeTypeEnum.values()) {
                if(p.getIndex().equals( index ) ){
                    return p;
                }
            }
            return null;
        }

        /**
         * 获取index
         * @return index int
         */
        public String getIndex() {
            return index;
        }

        /**
         * 设置index
         * @param index int
         */
        public void setIndex(String index) {
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



    /**
     * 获取年级对应级别关系
     */
    public enum getLevelEnum {
        ONE("一年级",1),
        TOW("二年级",2),
        THREE("三年级",3),
        FOUR("四年级",4),
        FIVE("五年级",5),
        SIX("六年级",6),
        SEVEN("七年级",7),

        eleven("初一",11),
        twelve("初二",12),
        thirteen("初三",13),
        fourteen("初四",14),

        twentyone("高一",21),
        twentyow("高二",22),
        twentythree("高三",23);
        private String index;
        private int value;

        getLevelEnum( String index,int value){
            this.index = index;
            this.value = value;
        }

        public static getLevelEnum getSource(String index){
            for (getLevelEnum p:getLevelEnum.values()) {
                if(p.getIndex().equals( index ) ){
                    return p;
                }
            }
            return null;
        }

        /**
         * 获取index
         * @return index int
         */
        public String getIndex() {
            return index;
        }

        /**
         * 设置index
         * @param index int
         */
        public void setIndex(String index) {
            this.index = index;
        }

        /**
         * 获取value
         * @return value String
         */
        public int getValue() {
            return value;
        }

        /**
         * 设置value
         * @param value String
         */
        public void setValue(int value) {
            this.value = value;
        }

    }

    /**
     * 获取年级对应级别关系
     */
    public enum getClassesNameByLevelEnum {
        ZORE( 0 ,"一年级"),
        ONE( 1 ,"一年级"),
        TOW( 2 , "二年级"),
        THREE( 3 , "三年级"),
        FOUR( 4, "四年级"),
        FIVE( 5, "五年级" ),
        SIX( 6 , "六年级" ),
        SEVEN( 7 , "七年级" ),

        eleven( 11 , "初一"),
        twelve( 12 , "初二" ),
        thirteen( 13 , "初三"),
        fourteen(14 , "初四" ),

        twentyone( 21 , "高一" ),
        twentyow( 22 , "高二" ),
        twentythree( 23 , "高三" ),
        twentyfour( 24 , "高三" );
        private int index;
        private String value;

        getClassesNameByLevelEnum( int index,String value){
            this.index = index;
            this.value = value;
        }

        public static getClassesNameByLevelEnum getSource(int index){
            for (getClassesNameByLevelEnum p:getClassesNameByLevelEnum.values()) {
                if( p.getIndex() ==  index ){
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

    /**
     * 获取作业关系
     */
    public enum getExamProgressEnum {
        ONE(0,"开始作业"),
        TOW(1,"继续作业"),
        THREE(2,"复习") ;
        private int index;
        private String value;

        getExamProgressEnum( int index,String value){
            this.index = index;
            this.value = value;
        }

        public static getExamProgressEnum getSource(int index){
            for (getExamProgressEnum p:getExamProgressEnum.values()) {
                if( p.getIndex() == index ){
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

    /**
     * 获取作业关系
     */
    public enum getExamKeTangProgressEnum {
        ONE(0,"开始学习"),
        TOW(1,"继续学习"),
        THREE(2,"复习") ;
        private int index;
        private String value;

        getExamKeTangProgressEnum( int index,String value){
            this.index = index;
            this.value = value;
        }

        public static getExamKeTangProgressEnum getSource(int index){
            for (getExamKeTangProgressEnum p:getExamKeTangProgressEnum.values()) {
                if( p.getIndex() == index ){
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
    /**
     * 状态对应的学段描述
     * 1 : 大学， 2：高中， 6:初中 ，8 ： 小学 , 9: 九年一贯制，7：职校'
     */
    public enum getRangeNameEnum {
        XIAOXUE(8,"小学"),
        CHUZHONG(6,"初中"),
        GAOZHONG( 2 ,"高中"),
        JIUNIANZHI(9,"小学,初中"),
        ZHIXIAO( 7 ,"职校,高中");

        private int index;
        private String value;
        getRangeNameEnum(int index,String value){
            this.index = index;
            this.value = value;
        }
        public static getRangeNameEnum getSource(int index){
            for (getRangeNameEnum p:getRangeNameEnum.values()) {
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
            if( null == value ){
                value = "其它";
            }
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

    /**
     * 获取年级对应的所有班级level集合
     *  2：高中 ：21,22,23
     *  8: 小学 : 1,2,3,4,5,6
     *  6: 初中  : 11,12,13
     */
    public enum getClassesLevelsEnum {
        XIAOXUE(8,"1:一年级,2:二年级,3:三年级,4:四年级,5:五年级,6:六年级"),
        CHUZHONG(6,"11:初一,12:初二,13:初三"),
        GAOZHONG( 2 ,"21:高一,22:高二,23:高三"),
        JIUNIANZHI(9,"小学,初中"),
        ZHIXIAO( 10 ,"职校,高中");

        private int index;
        private String value;
        getClassesLevelsEnum(int index,String value){
            this.index = index;
            this.value = value;
        }
        public static getClassesLevelsEnum getSource(int index){
            for (getClassesLevelsEnum p:getClassesLevelsEnum.values()) {
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
            if( null == value ){
                value = "其它";
            }
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

    /**
     * 班级状态描述
     * 1 :
     */
    public enum getClassesStatusEnum {
        ZHENGCHANG(0,"有效"),
        FENGBI(10,"封闭"),
        GAOZHONG( 2 ,"高中"),
        JIUNIANZHI(9,"小学,初中"),
        ZHIXIAO( 7 ,"职校,高中");

        private int index;
        private String value;
        getClassesStatusEnum(int index,String value){
            this.index = index;
            this.value = value;
        }
        public static getClassesStatusEnum getSource(int index){
            for (getClassesStatusEnum p:getClassesStatusEnum.values()) {
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
            if( null == value ){
                value = "其它";
            }
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


    /**
     * 获取学科id
     */
    public enum getSubjectIdEnum {
        YUWEN("语文",2464),
        SHUXUE("数学",2465 ),
        YINGYU("英语",2466),
        WULI("物理",2468),
        HUAXUE("化学",2467 ),
        LISHI("历史",2471 ),
        DILI("地理",2472 ),
        ZHENGZHI("政治",2470 ),
        SHENGWU("生物",2469 ),
        OTHER("其他",1111 );
        private String index;
        private int value;
        getSubjectIdEnum( String index,int value){
            this.index = index;
            this.value = value;
        }
        public static getSubjectIdEnum getSource(String index){
            for (getSubjectIdEnum p:getSubjectIdEnum.values()) {
                if(p.getIndex().equals( index ) ){
                    return p;
                }
            }
            return null;
        }
        /**
         * 获取index
         * @return index int
         */
        public String getIndex() {
            return index;
        }
        /**
         * 设置index
         * @param index int
         */
        public void setIndex(String index) {
            this.index = index;
        }
        /**
         * 获取value
         * @return value String
         */
        public int getValue() {
            return value;
        }
        /**
         * 设置value
         * @param value String
         */
        public void setValue(int value) {
            this.value = value;
        }

    }

    /**
     * 获取学科id
     */
    public enum getClassTypeEnum {
        PUTONG("普通班",0),
        XINGZHENG("行政班",0),
        XUEKE("学科班",1);
        private String index;
        private int value;
        getClassTypeEnum( String index,int value){
            this.index = index;
            this.value = value;
        }
        public static getClassTypeEnum getSource(String index){
            for (getClassTypeEnum p:getClassTypeEnum.values()) {
                if(p.getIndex().equals( index ) ){
                    return p;
                }
            }
            return null;
        }
        /**
         * 获取index
         * @return index int
         */
        public String getIndex() {
            return index;
        }
        /**
         * 设置index
         * @param index int
         */
        public void setIndex(String index) {
            this.index = index;
        }
        /**
         * 获取value
         * @return value String
         */
        public int getValue() {
            return value;
        }
        /**
         * 设置value
         * @param value String
         */
        public void setValue(int value) {
            this.value = value;
        }

    }
}
