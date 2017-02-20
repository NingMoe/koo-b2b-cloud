package com.koolearn.cloud.common.serializer;

import java.io.Serializable;

/**
 * Created by fn on 2016/3/31.
 */
public  class CommonInstence implements Serializable{

    public static final String ALL_SUBJECT = "全部";
    public static final String JIAO_CAI_MU_LU = "教材目录";
    public static final String CODE_200 = "200";
    public static final String STATUS = "status";//标识
    public static final String DATA = "data";//标识
    public static final String ERROR_DATA = "errorData";//错误标识
    public static final String CODE_400 = "400";
    public static final String MESSAGE_FAIL = "返回结果异常";
    public static final String MESSAGE_SSO = "SSO接口异常";
    public static final String RESET_PASSWORD = "000000";//初始密码
    public static final String PROVINCE_BEIJING= "北京";//
    public static final String PROVINCE_TIANJIN= "天津";//
    public static final String PROVINCE_CHONGQING= "重庆";//
    public static final String PROVINCE_SHANGHAI= "上海";//
    public static final String FILE_DISCRIPTION= "p-book-01.png";//
    public static final String REDIS_CLASSKY= "classes_";//
    public static final String classes_map= "classes_map";//存储redis为HashMap的key
    public static final String USER_CHANNEL = "cloud";//sso的channel字段值，云平台渠道
    public static final String begin = " 00:00:00";
    public static final String end = " 23:59:59";
    public static final String currentPage = "currentPage";//当前页
    public static final String totalPage = "totalPage";//总页
    public static final String dataList = "dataList";//返回结果
    public static final String SUCCESS_LINE = "successLine";//成功条数结果
    public static final String ERROR_LINE = "errorLine";//失败条数结果
    public static final String RESULT = "result";// 结果
    public static final String XIAOXUE = "小学";//
    public static final String CHUZHONG = "初中";//
    public static final String GAOZHONG = "高中";//
    public static final String LOCATION_ALL_DATA_KEY="location_all_data_";//获取区域表缓存数据

    public static final int UPTIME_815 = 815;//用于判断年级是否升级
    public static final int CODE_0 = 0;//成功
    public static final int CODE_1 = 1;//失败
    public static final int add_student_length = 30;//批量添加学生时截取姓名的长度
    public static final int examResultStatus_2 = 2;//0,未开始 1.考试中    2.已交卷
    public static final int teacherView_1 = 1;//1.老师未浏览  2.老师已浏览
    public static final int STATUS_0 = 0;//正常状态
    public static final int GRADUATE_0 = 0;//正常未毕业状态
    public static final int STATUS_1 = 1;//实体状态：默认1 有效 0,失效',(课堂)
    public static final int STATUS_4 = 4;//1.新建(修改) 2.撤回 3.删除 4.发布(翻转课堂)'
    public static final int STATUS_2 = 2;// 状态: 0,未开始 1.考试中    2.已交卷
    public static final int STATUS_3 = 3;// 状态: 3:已删除
    public static final int STATUS_10 = 10;// 状态: 用户被冻结
    public static final int CLASSES_TYPE_3 = 3;//type=0 行政班   type=1 学科班、type=3班级小组
    public static final int CLASSES_TYPE_1 = 1;//type=0 行政班   type=1 学科班、type=3班级小组
    public static final int CLASSES_TYPE_0 = 0;//type=0 行政班   type=1 学科班、type=3班级小组
    public static final int STUDENT_TYPE = 2;//身份类型学生
    public static final int TEACHER_TYPE_1 = 1;//身份类型 教师
    public static final int PAGE_SIZE_20 = 20;//每页行数
    public static final int PAGE_SIZE_10 = 10;//每页行数
    public static final int PAGE_NO_0 = 0;//当前页
    public static final int STUDENT_STATUS_DELETE = 1;//删除
    public static final int STUDENT_CAN_DELETE_1 = 1;//1:可以删除
    public static final int STUDENT_CAN_DELETE_2 = 2;//1:不可以删除
    public static final int USER_PROCESS_1 = 1;//sso用户：1.新注册用户
    public static final int STUDENT_PROCESS_2 = 2;//学生：1.新注册用户  2.学生加入班级完成'
    public static final int STUDENT_TEACHER_1 = 1;//老师：1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int STUDENT_TEACHER_2 = 2;//老师：1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int STUDENT_TEACHER_3 = 3;//老师：1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int TEACHER_BOOK_VERSION_1 = 1;//0:删除，1：有效
    public static final int EXAM_TYPE_1 = 1;// 1作业   2课堂   20课堂作业
    public static final int EXAM_TYPE_2 = 2;// 1作业   2课堂   20课堂作业
    public static final int EXAM_TYPE_3 = 3;// 1作业  2课堂   20课堂作业 3.学生自测 4学生错题复习
    public static final int EXAM_TYPE_4 = 4;// 1作业  2课堂   20课堂作业 3.学生自测 4学生错题复习
    public static final int EXAM_TYPE_20 = 20;// 1作业   2课堂   20课堂作业
    public static final int PROVINCE_TYPE_2 = 2;// 城市类型：2：直辖市
    public static final int PROCESS_1 = 1;//1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int PROCESS_2 = 2;//1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int PROCESS_3 = 3;//1.新注册用户  2.完善资料完成 3.教材版本完成
    public static final int EXPIRE_TIME_300 = 300;//缓存失效时间（五分钟 = 300 秒）
    public static final int LOGIN_CODE_200 = 200;//返回结果类型，成功
    public static final int LOGIN_CODE_400 = 400;//返回结果类型,失败
    public static final int SOURCE_10 = 10;//用户来源：10: 小学说课用户批量导入，20:教师端批量导入教师
    public static final int SOURCE_20 = 20;//用户来源：10: 小学说课用户批量导入，20:教师端批量导入教师


    /**学段常量*/
    public static final int GRADE_TYPE_xx=8;//小学
    public static final int GRADE_TYPE_cz=6;//初中
    public static final int GRADE_TYPE_gz=2;//高中
    public static final int GRADE_TYPE_dx=1;//大学
    public static final int GRADE_TYPE_nnyg=9;//九年一贯制
    public static final int GRADE_TYPE_zx=7;//职校
    /**学校字典表搜搜状态：*/
    public static final int DIC_SCHOOL_SEARCH_STATUS_HASUSER=1;//有用户
    public static final int DIC_SCHOOL_SEARCH_STATUS_NOUSER=2;//无用户
    public static final int DIC_SCHOOL_SEARCH_STATUS_SHIELD=3;//已屏蔽
    public static String getGradeName(int gradeid){
        String name="";
        if(gradeid==GRADE_TYPE_xx) name="小学";
        if(gradeid==GRADE_TYPE_cz) name="初中";
        if(gradeid==GRADE_TYPE_gz) name="高中";
        if(gradeid==GRADE_TYPE_dx) name="大学";
        if(gradeid==GRADE_TYPE_nnyg) name="九年一贯制";
        if(gradeid==GRADE_TYPE_zx)  name="职校";
        return name;

    }

    /**学校业务审核结果状态：0可用、1不可用 （业务流程状态，通过延期表定时任务修改此状态*/
    public static  final  int SCHOOL_SHENHE_STATUS_YES=0;
    public static  final  int SCHOOL_SHENHE_STATUS_NO=1;
    /**学校字典表状态：1正常 2删除 3屏蔽*/
    public static final int SCHOOL_ENTITY_STATUS_OK=1;
    public static final int SCHOOL_ENTITY_STATUS_DEL=2;
    public static final int SCHOOL_ENTITY_STATUS_PINGBI=3;



}
