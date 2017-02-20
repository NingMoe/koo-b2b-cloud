package com.koolearn.cloud.exam.examcore.util;

/**
 * 原考试-题目常量
 * @author yangzhenye
 */
public interface ConstantTe {
	int CACHE_TIME = 60*20;
//	String DEFAULT_DATASOURCE="exam_datasource";
	String SESSION_USER_KEY="exam_session_user";
	String SEPERATOR_ANSWER_AND="&";
	//试题保存操作状态
	int QUESTION_SAVETYPE_SAVE=0;
	
	int QUESTION_SAVETYPE_UPDATE=1;
	int QUESTION_SAVETYPE_SAVEAS=2;
	
	//试题响应类型
	int QUESTION_RESPONSETYPE_NORMAL=0;//正常操作
	int QUESTION_RESPONSETYPE_SUBITEM=1;//子试题试题,弹窗响应操作
//	控制导航入口  2：试题审核；3：全部试题；4：我的试题  5：试卷
	int QUESTION_RESPONSETYPE_ADUT=2;
	int QUESTION_RESPONSETYPE_ALL=3;
	int QUESTION_RESPONSETYPE_MY=4;
	int QUESTION_RESPONSETYPE_TEST=5;
	int QUESTION_RESPONSETYPE_ADUT_1=6;
	int QUESTION_RESPONSETYPE_iframe=7;
	//缓存单个对象前缀key
	String REPOSITORY_QUSTION_ID="repository_qustion_id_";//试题前缀
	String REPOSITORY_ATTACH_QUESTION_ID="repository_attach_question_id_";//材料附件
	String REPOSITORY_HASATTACH_QUESTIONID="repository_hasattach_questionid_";//试题是否存在附件
	String REPOSITORY_CHOICE_QUESTION_ID="repository_choice_question_id_";//选择题
	String REPOSITORY_CHOICE_ANSWER_CHOICE_ID="repository_choice_answer_choice_id_";//选择题答案选项
	String REPOSITORY_MATRIX_ID="repository_matrix_id_";//矩阵题
	String REPOSITORY_COMPLEX_ID="repository_complex_id_";//完型填空、阅读理解题
	String REPOSITORY_LABEL_ID="repository_label_id_";//矩阵题
	String REPOSITORY_ESSAY_QUESTION_ID = "repository_essay_question_id_";//填空题
	String REPOSITORY_FILL_BLANK_ANSWER_ID = "repository_fill_blank_answer_id_";//填空题答案
	String REPOSITORY_QUSTION_TEID="repository_qustion_teid_";//子试题ID前缀
	String REPOSITORY_QUSTION_SUBS="repository_qustion_subs_";//子试题集合前缀
	
	String REPOSITORY_LOOKUP_WORD_ID="repository_lookup_word_id_"; //限时找词
	String REPOSITORY_LOOKUP_WORD_ANSWER_ID="repository_lookup_word_answer_id_";//限时找词答案
	String REPOSITORY_CORRECTION_QUESTION_ID="repository_correction_question_id_";//改错题
	
	String REPOSITORY_COMMENT_ID="repository_comment_id_";//评语缓存前缀
	
	//前台试卷结构缓存
	String REPOSITORY_PAPER_STRUCTURE_FRONT_ID = "repository_paper_structure_front_id_";
	String REPOSITORY_MODULE_SECTION_ID = "REPOSITORY_MODULE_SECTION_ID_";//模块-大题缓存
	String REPOSITORY_EXPRESSION_ID = "REPOSITORY_EXPRESSION_ID_"; //说明缓存
	String REPOSITORY_PAPERINFO_ID = "REPOSITORY_PAPERINFO_ID_"; //试卷信息缓存
	String REPOSITORY_PAPER_MODULELIST_FRONT_ID = "repository_paper_modulelist_front_id_"; //试卷模块结构缓存
	String REPOSITORY_PAPER_MODULE_STRUCTURE_FRONT_ID = "repository_paper_module_structure_front_id_"; //模块详细内容缓存
	String REPOSITORY_PAPER_STANDBYMODULELIST_FRONT_ID = "repository_paper_standbymodulelist_front_id_"; //试卷备选模块结构缓存
	//缓存多个对象列表id 格式 repository_tableXXX_list_xxx_xxx  缓存中要存储对象key
	
	//考试接口缓存
	String REPOSITORY_INTERFACE_USERTESTRESULTLIST = "USERTESTRESULTLIST_";
	String REPOSITORY_INTERFACE_LASTUSERTESTRESULTLIST = "LASTUSERTESTRESULTLIST_";
	String REPOSITORY_INTERFACE_SEARCHTESTRESULT = "SEARCHTESTRESULT_";
	String REPOSITORY_INTERFACE_USERRESULTMODULE = "USERRESULTMODULE_";
	
	//考试过程——试卷信息缓存
	public static final String PROCESS_EXAMPAPER_PAPERID="PROCESS_EXAMPAPER_PAPERID_";
	//考试过程——考试信息缓存
	public static final String PROCESS_EXAMPAPER_EXAMID="PROCESS_EXAMPAPER_EXAMID_";
	//考试过程——考试ip段信息缓存
	public static final String PROCESS_EXAMPAPER_EXAMID_IP="PROCESS_EXAMPAPER_EXAMID_IP_";
	
	
	int POSTIL_SERVICE_TYPE=9;
	int SPOKENPOSTIL_SERVICE_TYPE=10;
	// 作文批改详情入口
    String POSTIL_SERVICE_URL = "POSTIL_SERVICE_URL";
    // 口语批改详情入口
    String SPOKENPOSTIL_SERVICE_URL = "SPOKENPOSTIL_SERVICE_URL";
    //老考试域名
    String OLD_EXAM_HOST = "OLD_EXAM_HOST";
    //知识库标签开关
    String KNOWLEDGE_SWITCH = "knowledge_switch";
    
    int KLB_OPERATIONTYPE_SAVE=1;//保存
    int KLB_OPERATIONTYPE_UPDATE=2;//更新
    int KLB_OPERATIONTYPE_DELETE=3;//删除
    
    /**
	 * 标签自定义图片
	 */
	public String ICONOPEN = "/images/iconOpen.jpg";
	
	public String ICONCLOSE = "/images/iconClose.jpg";
	
	public String ICONOKOPEN = "/images/okOpen.png";
	
	public String ICONOKCLOSE = "/images/okClose.png";
	
	public String ICONFAOPEN = "/images/faOpen.png";
	
	public String ICONFACLOSE = "/images/faClose.png";
	
	int PURE_RANDOM = 1;
	int CONDITIONAL_RANDOM = 2;
	//考试阶段 老考试为0 当前考试为1,默认是当前考试
	int EXAM_EXAMSTAGE_OLD=0;
	int EXAM_EXAMSTAGE_NOW=1;
	
	//考试状态
	
	int testStart = 1;
	int testEnd = 2;
	
	//默认答题已用时间
	int defaultTime = 10;
	
	//默认缓存时长-秒
	int expir = 10800*2;
	//pager_info 类型
	int PAGER_INFO_SHIJUAN=1;
	int PAGER_INFO_MOBAN=2;
	int PAGER_INFO_LX=3;
	
	//test 考试类型
	int TEST_TYPE_SUITANGLX=1;
	int TEST_TYPE_KETANGKAOSHI=2;
	int TEST_TYPE_SHUIPINGCESHI=3;
	int TEST_TYPE_YOUKETIYAN=4;
	int TEST_TYPE_LX=5;
	int TEST_TYPE_SUITANGLX_KETANGKAOSHI=6;
	int TEST_TYPE_SUITANGLX1=7;
	
	//teTestPaperModule 模块大题类型
	int PAPER_COMMON_MODULR=1;//普通模块
	int PAPER__SECTION=2;//大题
	int PAPER_PACKAGE=3;//提包
	int PAPER_RANDOM_MODULR=11; //随机模块
	int PAPER_OPTIONAL_MODULR=12; //备选模块
	
	String entExamKey="ENT&exam";
	
	public static final int TOUZHUAI_JUZHEN = 1;
	
	public static final int BIAOGE_JUZHEN = 2;
	
	public static final int LIANXIAN_JUZHEN = 3;
	
	int FROMTYPE_UPLOAD = 2;
	int FROMTYPE_MANUAL = 3;
	
	/**
	 * 日志操作类型
	 */
	
	String LOG_ADD = "add";
	
	String LOG_UPDATE = "update";
	
	String LOG_DELETE = "delete";
	
	String LOG_UPDATE_STATUS = "update_status";
	
	String LOG_BATCH = "batch_operate";
	
	String LOG_EDIT_TAG = "edit_tag";
	
	
	/**
	 * 对象类型
	 */
	int QUESTION_OBJECT_TYPE = 1;
	
	int PAPER_OBJECT_TYPE = 2;
	
	int TEST_OBJECT_TYPE = 3;
	
	int PRACTICE_OBJECT_TYPE = 4;
	
	String FILE_ENCRYPT_KEY = "1234567812345678";
	
	/**
	 *大小写敏感
	 */
	int SENSITIVE=1;
	/**
	 * 大小写不敏感
	 */
	int UN_SENSITIVE=0;
	
	int MARK_TYPE_MANUAL = 1; //手动批改
	int MARK_TYPE_SYSTEM = 2; //系统批改
	
	int SERVICE_TYPE_POSTIL = 9; //作文批改服务
	int SERVICE_TYPE_SPOKEN = 10; //口语批改服务
	
	//KlbUtil 标签类型
	int TAG_TYPE_QUESTION_ID = 2;//题目标签

	/**  考试过程题目ID集合 杜鸿麟  */
	public static final String PROCESS_EXAM_QID_LIST = "PROCESS_EXAM_QID_LIST_";
	/**  考试过程题目Code集合 杜鸿麟  */
	public static final String PROCESS_EXAM_QCDS_SET = "PROCESS_EXAM_QCDS_SET_";
	/**  考试过程题目集合 杜鸿麟  */
	public static final String PROCESS_EXAM_QUESTION_LIST = "PROCESS_EXAM_QUESTION_LIST_";
	/**  考试试卷题目集合 杜鸿麟  */
	public static final String PROCESS_EXAM_PAPER_QUESTION_LIST = "PROCESS_EXAM_PAPER_QUESTION_LIST_";
	/**  考试过程题目HTML集合 杜鸿麟  */
	public static final String PROCESS_EXAM_QUESTION_MAP_HTML = "PROCESS_EXAM_QUESTION_MAP_HTML_";
	/**  考试过程页面展示HTML字符串 杜鸿麟  */
	public static final String PROCESS_EXAM_HTML_PAGE = "PROCESS_EXAM_HTML_PAGE_";
	/**  考试过程ID做key题目Map 杜鸿麟  */
	public static final String PROCESS_EXAM_QID_QUESTION_MAP = "PROCESS_EXAM_QID_QUESTION_MAP_";
	/**  考试过程Code做key题目Map 杜鸿麟  */
	public static final String PROCESS_EXAM_QCD_QUESTION_MAP= "PROCESS_EXAM_QCD_QUESTION_MAP_";
	/**  考试过程试卷树形结构 杜鸿麟  */
	public static final String PROCESS_EXAM_PAPER_TREE= "PROCESS_EXAM_PAPER_TREE_";
	public static final String REPOSITORY_QUESTION_TOPIC= "question_topic_";
	public static final String STUDENT_EXAM_PAPER_KEY= "STUDENT_EXAM_PAPER_KEY_";
	public static final int CACHE_TIME_FIVE_MINUTES= 60*5;
	public static final int QUESTION_OBJCECTIVE_FLAG=1;//标识客观题
}
