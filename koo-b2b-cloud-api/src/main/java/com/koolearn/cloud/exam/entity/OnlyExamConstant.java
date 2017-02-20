package com.koolearn.cloud.exam.entity;

public class OnlyExamConstant {
	
	/** mysql数据源 */
	public static final String MYSQL_DATASOURCE="cloud";
	/**默认pager size大小*/
	public static final int KOOTEST_DEFAULT_PAGESIZE=20;
    /***题型模版路径：webRoot下**/
    public static final String QUESTION_TEMPLATE_PATH="examcore/template/ftl/";
	public static final int QUESTION_TAG_SHOW=1;//显示录题标签
	public static final String LOGIN_REDIS_PREFIX = "loginuser_";
	public static final int EXAMTYPE_MYEXAM=1;
    /**试卷常量*/
    public static final String CACHE_PAPER_PREFIX="cloud_cache_paper_";
    /**模版数据缓存前缀*/
    public static final String TEMPLATE_PREFIX="cloud_paper_bars_";
    /**组卷类型： 1 进度点组题 2知识点组题  3 试卷组题  4智能组题*/
    public static final Integer CREATE_PAPER_TYPE_JJD=1;
    public static final Integer CREATE_PAPER_TYPE_ZSD=2;
    public static final Integer CREATE_PAPER_TYPE_SJZT=3;
    public static final Integer CREATE_PAPER_TYPE_ZNZT=4;
    /**试卷创建来源页面： 1 题库 2创建作业过来  3创建课堂过来*/
    public static final Integer CREATE_PAPER_SOURCE_QLIB=1;
    public static final Integer CREATE_PAPER_SOURCE_WORK=2;
    public static final Integer CREATE_PAPER_SOURCE_CLASS=3;
    /**组卷题目id cookieName**/
    public static final String COOKIE_EXAMIDARR="examIdArr";
    public static final String TEMPLATE_PAPER_PREFIX="template_paper_";


}
