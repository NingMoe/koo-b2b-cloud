<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="题库-进度点" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-progress/page.css">
<body>
<!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="tk"/>
</jsp:include>
<div class="i-main fc jp-main">
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
    <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
        <jsp:param name="nav" value="bxsj"/>
    </jsp:include>
    <!-- 左侧 -->
    <div class="p-side fl jp-side">
        <dl class="p-chos">
            <dt class="p-chos-dt jp-chos">
                <span>高中物理、新课标新课标新课标</span>
                <i></i>
            </dt>
            <dd class="p-chos-dd">
                <div class="p-pd12">
                    <form id="jp-form-chos">
                        <dl>
                            <dt>教材选择</dt>
                            <dd class="p-dd-one jp-mater">
                                <span class="cur" data-id="1">小学</span>
                                <span data-id="2">初中</span>
                                <span data-id="3">高中</span>
                                <input class="jp-materid" type="hidden" value="1" />
                            </dd>
                        </dl>
                        <dl>
                            <dt>科目</dt>
                            <dd>
                                <select id="sel-show1">
                                    <option>语文</option>
                                    <option>数学</option>
                                    <option>英语</option>
                                </select>
                            </dd>
                        </dl>
                        <dl>
                            <dt>教材版本</dt>
                            <dd>
                                <select id="sel-show2">
                                    <option>人教版</option>
                                    <option>苏教版</option>
                                </select>
                            </dd>
                        </dl>
                    </form>
                </div>
                <a class="p-sure jp-sure" href="javascript:;">确定</a>
            </dd>
        </dl>
        <div class="p-chos p-compu">
            <select id="sel-show3">
                <option>必修一</option>
                <option>必修二</option>
                <option>必修三</option>
            </select>
        </div>
        <!-- 章节 -->
        <div class="p-chapter">
            <!-- 树 -->
            <ul id="treeDemo" class="ztree p-pd15"></ul>
        </div>
    </div>
    <!-- 右侧内容 -->
    <div class="p-cont fr">
        <div class="p-search">
            <p class="p-search-p fc">
                <input class="fl" type="text" id="" name="" placeholder="输入关键词" />
                <a class="fr" href="javascript:;"></a>
            </p>
            <dl class="p-search-dl jp-search">
                <form action="#" id="jq-spnForm">
                    <input id="jp-txId" type="hidden" value="" name="txid" />
                    <input id="jp-ndId" type="hidden" value="" name="ndid" />
                </form>
                <dt>题型</dt>
                <dd>
                    <a class="white-btn-tab on" href="javascript:;" data-tx="0">全部</a>
                    <a class="white-btn-tab" href="javascript:;" data-tx="1">单组题</a>
                    <a class="white-btn-tab" href="javascript:;" data-tx="2">解答题</a>
                    <a class="white-btn-tab" href="javascript:;" data-tx="3">填空题</a>
                    <a class="white-btn-tab" href="javascript:;" data-tx="4">计算题</a>
                    <a class="white-btn-tab" href="javascript:;" data-tx="5">证明题</a>
                </dd>
                <dt>难度</dt>
                <dd>
                    <a class="white-btn-tab on" href="javascript:;" data-nd="1-0">全部</a>
                    <a class="white-btn-tab" href="javascript:;" data-nd="1-1">简单</a>
                    <a class="white-btn-tab" href="javascript:;" data-nd="1-2">中等</a>
                    <a class="white-btn-tab" href="javascript:;" data-nd="1-3">困难</a>
                </dd>
            </dl>
            <p class="p-screen checkbox_area">
                <input type="checkbox" id="e" class="for-checkbox"><b>过滤使用过的</b>
                <input type="checkbox" id="f" class="for-checkbox"><b>只选收藏的</b>
                <input type="checkbox" id="g" class="for-checkbox"><b>只选使用过的</b>
            </p>
        </div>
        <div class="p-quest">
            <!-- 试题模板 -->
            <div class="p-quest-temp">
                <div class="p-img">
                    <img src="http://b-ms-cloud.ui.koolearn-inc.com/project/b-ms-cloud/1.x/i/def.png">
                </div>
                <dl class="p-parsi">
                    <dt>
                    <p class="p-parsi-btn">
                        <a class="collect-btn jp-collect" href="javascript:;"></a>
                        <a class="white-btn white-btn-jx jp-parsi" href="javascript:;">
                            <i class="ico"></i>解析
                        </a>
                        <a class="white-btn pdlr4 p-addtest jp-addtest" href="javascript:;">加入试题篮</a>
                    </p>
                    <p class="p-parsi-kldge">
                        <span>使用：234 </span>
                        <span>知识点：相似三角形应用  平行投影  用三角函数解决实际问题</span>
                    </p>
                    </dt>
                    <dd>
                        <p class="p-answer">答案<em>AC</em></p>
                        <p class="p-inter">
                            解析：f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增
                        </p>
                    </dd>
                </dl>
            </div>
            <div class="p-quest-temp">
                <span class="goods-ico p-fine"></span><!-- 精选图标 -->
                <div class="p-img">
                    <img src="/project/b-ms-cloud/1.x/i/def.png">
                </div>
                <dl class="p-parsi">
                    <dt>
                    <p class="p-parsi-btn">
                        <a class="collect-btn jp-collect" href="javascript:;"></a>
                        <a class="white-btn white-btn-jx jp-parsi" href="javascript:;">
                            <i class="ico"></i>解析
                        </a>
                        <a class="white-btn pdlr4 p-addtest jp-addtest" href="javascript:;">加入试题篮</a>
                    </p>
                    <p class="p-parsi-kldge">
                        <span>使用：234 </span>
                        <span>知识点：相似三角形应用  平行投影  用三角函数解决实际问题</span>
                    </p>
                    </dt>
                    <dd>
                        <p class="p-answer">答案<em>AC</em></p>
                        <p class="p-inter">
                            解析：f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增f(x)在其定义域上是增函数f(x)在其定义域上是增
                        </p>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    <!-- 分页 注:此分页只是一个暂时的样式-->
    <div class="p-page fr">
        <img src="/project/b-ms-cloud/1.x/i/page.png" style="margin:0 auto;display: block;">
    </div>
</div>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-tiku-progress/page': 'project/b-ms-cloud/1.x/js/t-tiku-progress/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-tiku-progress/page',function(init){
            init({
                treeUrl:'/project/b-ms-cloud/1.x/json/t-tiku-progress/data.json',//树json
                treeId:"#treeDemo",//树Id
                sureUrl:'/project/b-ms-cloud/1.x/json/t-find-pass/111.php'
            });
        });
    </script>
</fe:html>