package com.koolearn.cloud.util;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstant {
    /**
     * MySQL 数据源
     */
	public static final String AUTH_KEY = "cloudu";
    public static final String MYSQL_DATASOURCE = "cloud";
    public static final String HOSTS_SSO=PropertiesConfigUtils.getProperty("hosts.sso");
    public static final String DEFAULT_ADD_QUESTION_TAGS=PropertiesConfigUtils.getProperty("default.tags");//默认增加的题目标签（题难度中，fullpath）
    public static final String SYNCHRO_CURRENT_KEY="current_sync_key";//获取同步信息key的key
    public static final String SYNCHRO_CURRENT_KEY_oldsync_data="current_sync_key_oldsync_data";//获取同步信息key的key
    public static final String REBUILD_RESOURCE_INFO_KEY="current_rebuild_key__data";//资源重建信息 key
    public  static String USER_COLLECTION_KEY="user_all_question_collection_";//用户获取题目收藏key
    public static final String EXAM_HOST_KEY = "exam.host";
    public static final String DEFAULT_SYNC_BEGIN_TIME="2015-09-01 00:00:00";//第一次同步时间
    public static final String DEFAULT_SYNC_CURRENT_KEY="sycn_exampaper_flag"+ParseDate.formatByDate(ParseDate.parse(GlobalConstant.DEFAULT_SYNC_BEGIN_TIME),ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS);
    public static final String AUTH_KEY_K12="k12tsgUser"; //用户cookiekey
    public static final String K12TSG_AUTOURL=PropertiesConfigUtils.getProperty("k12tsg.autourl"); //中小学数字图书馆登录地址
    public static final String KAOYUE_AUTOURL_DOMAIN=PropertiesConfigUtils.getProperty("kaoyue.autourl"); //考悦
    //图书馆cookiekey
    public static final String LIB_KEY = "k12tsgKey";
    public static final String DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM="DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_sheet";//根据知识点下载资源数量
    public static final String DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish="DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish";//完成标识
    public static final String loadKonwledge="loadKonwledge";//true下载知识点，fals下载进度点
    /**
     * 身份 1学生 2老师
     */
    public static final int CLIENT_TYPE_STUDENT=2;
    public static final int CLIENT_TYPE_TEACHER=1;
    /**
     * 前端资源域名key
     */
    public static final String STATIC_SOURCE_DOMAIN = "static.source.domain";
    public static final String CLOUD_HOST_DOMAIN = "domain";
    public static final String CLOUD_ueditor_uplad_jsp = "/ueditorjsp/controller.jsp";

    /** 63581 属性标签->内容分类->中小学云平台  */
    public static final String CLOUD_TAG_NAME = "cloud.tag";
    /**  _93400_20 属性标签->学科题型  **/
    public static final String CLOUD_TAG_NAME_QUSTION_TYPE = "question.type.tag";
    public static final String EXAM_STATIC_PAGECSS = "exam.css";
    public static final Double PAPER_QUESTION_DEFAULT_SCORE = 1D;//如果klb系统没有设置默认分值，则去1d
    /**
     * 实体状态默认0：可用  1 删除
     */
    public static final int ENTITY_STATUS_USABLE = 0;
    public static final int ENTITY_STATUS_DELETE = 1;
    /**
     * 0：新东方同步试卷 1:老师创建试卷2.学生自测试卷
     */
    public static final int PAPER_FORM_XDF_NEW = 0;
    public static final int PAPER_FORM_TEACHER_NEW = 1;
    public static final int PAPER_FORM_STUDENT_NEW = 2;
    public static final int PAPER_FORM_XDFtoTEACHER_NEW = 3;

    public static final String DOWNLOAD_PAPER_JIEX_FENLI = "1";// 试题跟答案解析分离
    public static final String DOWNLOAD_PAPER_JIEX_AFTER = "2";//试题后面紧跟答案解析
    /**
     * 资源状态:转换中0
     */
    public static final int RESOURCE_STATUS_CONVERING = 0;

    /**
     * 资源状态：转换失败1
     */
    public static final int RESOURCE_STATUS_CONVER_FAIL = 1;

    /**
     * 资源状态:转换完成(可用)2
     */
    public static final int RESOURCE_STATUS_CONVERED = 2;
    /**
     * 资源状态：资源不可用3
     */
    public static final int RESOURCE_STATUS_UNAVAILABLE = 3;

    /**
     * 资源格式：Word1 PPT2 EXCEL3 视频4 音频5 图片6 文本7 Pdf8
     */

    public static final int RESOURCE_FORMAT_WORD = 1;

    /**
     * 资源格式：PPT2
     */
    public static final int RESOURCE_FORMAT_PPT = 2;

    /**
     * 资源格式：EXCEL3
     */
    public static final int RESOURCE_FORMAT_EXCEL = 3;

    /**
     * 资源格式：视频4
     */
    public static final int RESOURCE_FORMAT_VIDEO = 4;

    /**
     * 资源格式：音频5
     */
    public static final int RESOURCE_FORMAT_AUDIO = 5;

    /**
     * 资源格式：图片6
     */
    public static final int RESOURCE_FORMAT_IMAGE = 6;

    /**
     * 资源格式：文本7
     */
    public static final int RESOURCE_FORMAT_TEXT = 7;
    /**
     * 资源格式：Pdf8
     */
    public static final int RESOURCE_FORMAT_PDF = 8;

    /**
     * 资源格式：其他9
     */
    public static final int RESOURCE_FORMAT_OTHER = 9;
    public static final int RESOURCE_MARROW_YES=2;
    public static final int RESOURCE_MARROW_NO=1;

    /**
     * 来源(1运营、2学校、3教师、4公共 )
     */
    public static final int RESOURCE_SOURCE_SYSTEM = 1;
    public static final int RESOURCE_SOURCE_SCHOOL = 2;
    public static final int RESOURCE_SOURCE_TEACHER = 3;
    public static final int RESOURCE_SOURCE_PUBLIC = 4;

    /**
     * KLB关联表类型 标签类型：1：学科 2：学段 3:知识点 4:教材目录
     */
    public static final int KLB_TAG_TYPE_SUBJECT = 1;
    public static final int KLB_TAG_TYPE_STAGE = 2;
    public static final int KLB_TAG_TYPE_KNOWLEDGE = 3;
    public static final int KLB_TAG_TYPE_BOOKVERSION = 4;

    /**
     * 对象类型：1：试卷，2：试题、3：作业/练习 4：资源 5：教学进度点 6: 学校
     * 书香中国资源（云阅读）7  影视百科资源8
     */
    public static final int KLB_OBJECT_TYPE_EXAMPAPER = 1;
    public static final int KLB_OBJECT_TYPE_QUESTION = 2;
    public static final int KLB_OBJECT_TYPE_HOMEWORK = 3;
    public static final int KLB_OBJECT_TYPE_RESOURCE = 4;
    public static final int KLB_OBJECT_TYPE_TEACH_SCHEDULE = 5;
    public static final int KLB_OBJECT_TYPE_SCHOOL = 6;
    public static final int KLB_OBJECT_TYPE_CHINESEALL_BOOK = 7;
    public static final int KLB_OBJECT_TYPE_TELEVISION = 8;

    /**区分资源的类型 0：教材目录，1：知识点 2：所有（一个资源分别打在2个类别上） **/
    public static final int KLB_TYPE_BOOKVERSION = 0;
    public static final int KLB_TYPE_KNOWLEDGE = 1;
    public static final int KLB_TYPE_ALL = 2;
    public static final int DICTIONARY_TYPE_KLBSX_length =6;//属性标签：学科题型标签的深度是6
    /**数据字典数据类型*/
    public static final int DICTIONARY_TYPE_KLB_SUBJECT = 1;//学科
    public static final int DICTIONARY_TYPE_KLBSX_AREA = 2;//属性标签：地区
    public static final int DICTIONARY_TYPE_KLBSX_YEAR = 3;//属性标签：年份
    public static final int DICTIONARY_TYPE_KLBSX_PAPERTYPE = 4;//属性标签：试卷类型
    public static final int DICTIONARY_TYPE_KLBSX_GRADE = 5;// 适用年级：标签管理-》学习阶段
    public static final int DICTIONARY_TYPE_RESOURCE_TYPE = 6;//资源类型 （微课、教案、学案、教学素材、复习讲义、其它）
    public static final int DICTIONARY_TYPE_RESOURCE_FORMAT = 7;//资源格式（ppt、word、Exce、视频、音频、图片）
    public static final int DICTIONARY_TYPE_FILTER = 8;//过滤使用过的、只选收藏的、只选使用过的
    public static final int DICTIONARY_TYPE_QUESTION_HARD =9;//属性标签：考试属性-难度
    public static final int DICTIONARY_TYPE_KLBSX_QUESTION_TYPE =10;//属性标签：学科题型
    public static final int DICTIONARY_TYPE_KLBSX_PAPER_SUBJECT =11;//科目与考试分:试卷学科
    public static final int DICTIONARY_TYPE_KLBSX_KCNL =12;//12 属性标签->考试属性->考察能力
    public static final int DICTIONARY_TYPE_KLBSX_JPT =13;//13属性标签->考试属性->精品题
    public static final  String SUBJECT_NAME_xiaoxue="小学",SUBJECT_NAME_chuzhong="初中",SUBJECT_NAME_gaozhong="高中",SUBJECT_NAME_chuxiao="初小";
    public static final  String SUBJECT_KEY_subject="subject",SUBJECT_KEY_range="range";
    /***试卷搜索类型:xdf 试卷组题  bxsj 本校试卷库  wdsj  我的试卷（创建Union加入的试卷）*/
    public static final String PAPER_SEARCH_TYPE_XDF="xdf";
    public static final String PAPER_SEARCH_TYPE_BXSJ="bxsj";
    public static final String PAPER_SEARCH_TYPE_WDSJ="wdsj";
    public static final Integer PAPER_SEARCH_TYPE_XDF_Grade_all=-1;//试卷组卷、年级全部值
    public static final Integer PAPER_SEARCH_TYPE_XDF_Subject_all=-2;//试卷组卷、年级全部值
    /***试卷创建类型:xdf 试卷组题  bxsj 本校试卷库  wdsj  我的试卷（创建Union加入的试卷）*/
    public static final String PAPER_CREATE_TYPE_JDD="1";
    public static final String PAPER_CREATE_TYPE_ZSD="2";
    public static final String PAPER_CREATE_TYPE_SJZT="3";
    public static final String PAPER_CREATE_TYPE_ZNZT="4";
    public static final String PAPER_CREATE_TYPE_bxsj="5";
    public static final String PAPER_CREATE_TYPE_wdsj="6";
    /***试卷创建来源： 题库   作业 课堂*/
    public static final String PAPER_CREATE_FROM_QUESTIONLIB="1";
    public static final String PAPER_CREATE_FROM_HOMEWORK="2";
    public static final String PAPER_CREATE_FROM_CLASS="3";

    /**知识点、进度点节点名*/
    public static final String KLB_TAB_KNOWLEDGE_NAME="知识点";
    public static final int KLB_TAB_TYPE_KNOWLEDGE=1;//查询知识点节点
    public static final String KLB_TAB_TEACHING_NAME="教材目录";
    public static final int KLB_TAB_TYPE_TEACHING=0;//查询进度点
    /**题目知识点集合分隔符**/
    public static final String KLB_KNOWLEDGE_LIST_SEPARATOR=",";
    public static final String KLB_KNOWLEDGE_FULLPATH_SEPARATOR="/";
    public static final String KLB_KNOWLEDGE_FULLPATH_SPACE=" ";
    public static final int KLB_KNOWLEDGE_LEVEL_VALUE=4;
    public static final int KLB_KNOWLEDGE_NAME_OFFSET=0;
    public static final int KLB_TEACHING_NAME_OFFSET=1;
    /**题目列表过滤条件*/
    public static final int QUESTION_FILTER_USERED_NOT=1;
    public static final int QUESTION_FILTER_COLLECTION=2;
    public static final int QUESTION_FILTER_USERED=3;
    public static final String[] CLOUD_PAPER_NUM={"一","二","三","四","五","六","七","八","九","十"};
    /**
     * 状态 1 有效 0 无效
     */
    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 0;

    /**
     * TP_EXMA TYPE
     * 0作业 1考试 2课堂 20课堂作业
     */
    public static final int TP_EXAM_TYPE_WORK = 0;
    public static final int TP_EXAM_TYPE_EXAM = 1;
    public static final int TP_EXAM_TYPE_CLASS_ROOM = 2;
    public static final int TP_EXAM_TYPE_CLASS_ROOM_WORk = 20;

    /**
     * TP_EXAM STATUS
     * 1新建(修改) 2撤回 3删除 4发布
     */
    public static final int TP_EXAM_STATUS_NEW = 1;
    public static final int TP_EXAM_STATUS_RECALL = 2;
    public static final int TP_EXAM_STATUS_DELETE = 3;
    public static final int TP_EXAM_STATUS_RELEASE = 4;
    public static final int PAGER_SIZE_DEFAULT_20=20;

   /**发送短信*/
   public static final String SEND_MESSAGE_SUB_SYSTEMT_ID="sendMessage.subsystemtId";//单个手机发送短信子系统id
   public static final String SEND_MESSAGE_DEPARTMENTID="sendMessage.departmentId";//单个手机发送短信部门id

   public static final int FUSUI_LOCATION_ID=451421;//扶绥地区id
   public static final Map<String,String> KAOYUE_URL_MAP=new HashMap<String, String>();
   public static final String KAOYUE_URL_login="KAOYUE_URL_login";//合心登录
   public static final String KAOYUE_URL_accoutStudent="KAOYUE_URL_accoutStudent";//合心学生注册
   public static final String KAOYUE_URL_accoutTeacher="KAOYUE_URL_accoutTeacher";//合心老师注册
   public static final String KAOYUE_URL_accoutGroup="KAOYUE_URL_accoutGroup";//合心添加班级
   public static final String KAOYUE_URL_accoutRelation="KAOYUE_URL_accoutRelation";//合心编辑用户和编辑关系
    static{
        KAOYUE_URL_MAP.put(KAOYUE_URL_login,KAOYUE_AUTOURL_DOMAIN+"/api/open/login");
        KAOYUE_URL_MAP.put(KAOYUE_URL_accoutStudent,KAOYUE_AUTOURL_DOMAIN+"/api/open/account/user");
        KAOYUE_URL_MAP.put(KAOYUE_URL_accoutTeacher,KAOYUE_AUTOURL_DOMAIN+"/api/open/account/teacher");
        KAOYUE_URL_MAP.put(KAOYUE_URL_accoutGroup,KAOYUE_AUTOURL_DOMAIN+"/api/open/account/group");
        KAOYUE_URL_MAP.put(KAOYUE_URL_accoutRelation,KAOYUE_AUTOURL_DOMAIN+"/api/open/account/relation");
    }

}
