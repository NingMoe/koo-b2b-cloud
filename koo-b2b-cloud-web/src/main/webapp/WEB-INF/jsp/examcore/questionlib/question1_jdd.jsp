<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="题库-进度点" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-progress/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
<body>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="tk"/>
</jsp:include>
<!--jp-section  0加载进度点   1加载知识点-->
<input id="urlType" type="hidden" value="0" name="urlType">
<!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
<div class="i-main fc jp-main">
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
    <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
        <jsp:param name="nav" value="<%=GlobalConstant.PAPER_CREATE_TYPE_JDD%>"/>
        <jsp:param name="createFrom" value="${questionFilter.createFrom}"/>
    </jsp:include>
    <!-- 左侧 -->
    <div class="p-side fl jp-side">
        <dl class="p-chos">
            <dt class="p-chos-dt jp-chos"><span></span><i></i></dt>
            <dd class="p-chos-dd" id="jp-temp">
                <div class="p-pd12">
                    <form id="jp-form-chos" enctype="multipart/form-data">
                        <div class="p-psnew"><p>学科</p> <p>学段</p> <p>教材版本</p> </div>
                        <div class="p-selnew">
                            <select class="i-sel1 sel1" id="sel-show" name="subjectId"> </select>
                            <select class="i-sel1 sel2" id="sel-show1" name="rangeId"> </select>
                            <select class="i-sel1 sel3" id="sel-show2" name="bookVersion"></select>
                        </div>
                        <input id="jp-oblig" type="hidden" value="" name="obligatoryId" />
                        <input id="jp-txId" type="hidden" value="" name="questionType" />
                        <input id="jp-ndId" type="hidden" value="" name="questionHard" />
                        <input id="jp-scId" type="hidden" value="" name="questionFilter" />
                        <input id="jp-keyId" type="hidden" value="" name="keyTxt" />
                        <input id="jp-tagId" type="hidden" value="" name="tagId" />
                        <input type="hidden" name="pageNo" id="pageNo" value="0"/>
                        <input type="hidden" name="navigation"   value="<%=GlobalConstant.PAPER_CREATE_TYPE_JDD%>"/>
                        <input type="hidden" name="createFrom"   value="${questionFilter.createFrom}"/>
                        <input type="hidden" name="viewType" id="viewType" value="<%=QuestionViewDto.view_type_question_analysis%>"/>
                        <%--<input type="hidden" name="buttonType" value="${buttonType}"/>--%>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_null%>"/>--%>
                        <input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_question_lib%>"/>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_question_lib_change%>"/>--%>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_create_paper%>"/>--%>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_zuoye_jiangping%>"/>--%>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_zuoye_detaile%>"/>--%>
                        <%--<input type="hidden" name="buttonType" value="<%=QuestionViewDto.button_ype_zuoye_error%>"/>--%>
                    </form>
                </div>
                <a class="p-sure jp-sure" href="javascript:;">确定</a>
            </dd>
        </dl>
        <div class="p-chos p-compu">
            <select id="obligatoryId" class="obligatoryId i-sel4" name="obligatory"></select>
        </div>
        <!-- 章节 -->
        <div class="p-chapter"><ul id="treeDemo" class="ztree p-pd15"></ul></div>
    </div>
    <!-- 右侧内容 -->
    <div class="p-cont fr">
        <div class="p-search">
            <p class="p-search-p fc">
                <input id="jp-keytxt" class="fl" type="text" placeholder="输入关键词" />
                <a  id="jp-keybtn" class="fr" href="javascript:;"></a>
            </p>
            <!--题型、难度和过滤条件-->
            <jsp:include page="questionFilter.jsp">
                <jsp:param name="questionHard" value="${questionHard}"/>
            </jsp:include>
        </div>
        <div class="p-quest" id="jp-quest"><!-- 试题模板   <span class="goods-ico p-fine"></span> 精选图标 --> </div>
    </div>

</div>
<!-- 试题栏-->
<jsp:include page="/WEB-INF/jsp/examcore/questionlib/questionBar.jsp"/>
<jsp:include page="/footer.jsp"/>

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-tiku-progress/page': 'project/b-ms-cloud/1.x/js/t-tiku-progress/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-tiku-progress/page', function (init) {
            init({
                treeUrl: '/teacher/resource/getTree',//树json
                treeId: "#treeDemo",//树Id
                getreeId: "treeDemo",//树Id
                sureUrl: '/teacher/resource/search',
                //联级
                selProductLine: '/teacher/resource/getSubject',
                selExamSeasonId: '/teacher/resource/getRange',
                //获取教材版本 id='jp-section'  0加载进度点   1加载知识点
                selSubjectId: '/teacher/resource/getBookVersion',
                selLastId: '/teacher/resource/getStr',
                //获取必修
                obligatoryId: '/teacher/resource/getObligatory',
                //获取学科学段题型
                asPhpOne:'/teacher/exam/core/questionType',
                //组卷begin
                questionBar: '/teacher/exam/core/paper/barInit',
                clearCache: '/teacher/exam/core/paper/barDel',
                barUpdate: '/teacher/exam/core/paper/barUpdate',
                createPaper: '/teacher/exam/core/paper/create',
                previewPaper: '/teacher/exam/core/paper/preview'
                //组卷end
            });
        });
        serachFenye=function(pageNo){
            if(!pageNo){
                pageNo=0;
            }
            $("#pageNo").val(pageNo);
            window.isGetQuestions=true;
            $('#jp-form-chos').submit();
        }
        $('#jp-form-chos').submit(function(){
            if(window.isGetQuestions){
                var options = {
                    url : '/exam/core/questionlib' ,
                    contentType : "multipart/form-data; charset=utf-8",
                    type : 'POST',
                    success : function(data) {
                        $("#jp-quest").html(data);
                        window._Q && window._Q.initPageFromLS();//刷新试题栏
                        initStyle();//处理下方知识点样式
                        window._ifhtml && window._ifhtml.ifhtmlheight();
                        $('body,html').animate({ scrollTop: 0 }, 200);
                    } ,
                    error:function(data){ }

                };
                // 提交表单
                $(this).ajaxSubmit(options);
                // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
                return false;
            }
            return true;
        });
        $("#jp-quest").on("click",".jp-quest-temp .collect-btn",function(){
            //题目收藏
            var hasCur=$(this).hasClass('cur');
            var _this=this;
            var qid=$(this).attr("data-qustid");
            setTimeout(function(){
                $.ajax({
                    url: "/teacher/exam/core/question/collection?questionId="+qid,    //请求的url地址
                    dataType: "json",   //返回格式为json
                    async: true,//请求是否异步，默认为异步，这也是ajax重要特性
                    type: "post",   //请求方式
                    success: function (data) {
                        if(hasCur){
                            $(_this).removeClass('cur');
                        }else{
                            $(_this).addClass('cur');
                        }
                    }, error: function () {
                        if(hasCur){
                            $(_this).removeClass('cur');
                        }else{
                            $(_this).addClass('cur');
                        }
                    }
                })
            },1000);
        });

    </script>

</fe:html>