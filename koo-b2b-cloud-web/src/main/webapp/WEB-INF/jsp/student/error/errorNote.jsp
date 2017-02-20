<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.cloud.util.ParseDate" %>
<%@ page import="java.util.Date" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<fe:html title="错题本" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-wrong-topic/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js"
        defaultHead="<html>">
    <body>
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="ctb"/>
    </jsp:include>
    <jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
    <div class="i-main">
        <div class="p-navig">
            <dl class="p-select" id="jp-select">
            </dl>
            <div class="p-subject-nav">
            </div>
        </div>
        <div class="p-wrong-topic fc">
            <div class="p-side fl">
                <div class="p-side-top fc">
                    <b class="p-title-name fl"><!--测试名--></b>
                    <div class="p-all-btn fr">
                        <input type="checkbox" class="for-checkbox" id="" value="">
                        <em>全部加入复习</em>
                    </div>
                </div>
                <div class="p-quest" id="questionList">  </div>
            </div>
            <div class="p-cont fr">
                <div class="p-tabs fc" id="jp-spans">
                    <span class="second-item-title fl item-selected">习题考试</span>
                    <span class="first-item-title fl">章节进度</span>
                </div>
                <div id="jp-divs">
                    <div class="p-public p-exam on">
                        <p class="p-search-p fc">
                            <input type="text" placeholder="输入关键词" class="fl" id="jp-keytxt">
                            <a href="javascript:;" class="fr" id="jp-keybtn"></a>
                        </p>
                        <div class="p-exam-list">
                            <!-- <p class="p-txt" data-id="1">自测：第一单元 幂函数</p> -->
                        </div>
                    </div>
                    <div class="p-public p-chapter">
                        <div class="p-chos">  <select id="bookVersionSel" class="i-sel4">  </select> </div>
                        <div class="p-chos">   <select id="obligatorySel" class="i-sel4">  </select>  </div>
                        <p class="p-pd15" id="jp-all-btn">
                            <a class="p-allbtn cur" href="javascript:;">全部</a>
                        </p>
                        <ul id="treeDemo" class="ztree p-pd15"></ul>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="i-foot">
        <div class="i-box fc">
            <form id="jp-qusetion-form">
                <input type="hidden" value="" id="jp-questinpt">
            </form>
            <div class="p-addexam-numb fl"> 复习题：<em id="jp-tnum">0</em>
                <ul id="jp-addexam-box" class="p-addexam-box"> <!--复习栏--> </ul>
                <i class="p-addicon"></i>
            </div>
            <div class="fr">
                <a id="jp-download-btn" class="green-btn green-btn-style" href="javascript:;">开始复习</a>
            </div>
        </div>
    </div>
    <form id="jp-form-chos" action=""  enctype="multipart/form-data">
        <input id="subjectId" type="hidden" name="subjectId" value="">
        <input id="rangeId" type="hidden" name="rangeId" value="">
        <input id="bookVersion" type="hidden" name="bookVersion" value="">
        <input id="obligatoryId" type="hidden" value="" name="obligatoryId" />
        <input id="keyWord" type="hidden" value="" name="keyWord" />
        <input id="examId" type="hidden" value="" name="examId" />
        <input id="jp-tagId" type="hidden" value="" name="tagId" />
        <input type="hidden" name="pageNo" id="pageNo" value="0"/>
        <!-- <input id="jp-txId" type="hidden" value="" name="questionType" />
        <input id="jp-ndId" type="hidden" value="" name="questionHard" />
        <input id="jp-scId" type="hidden" value="" name="questionFilter" />
         -->
    </form>
    <input type="hidden" name="commitUrl" id="commitUrl" value=""/>
<!-- 试题栏-->
    <%--栏 &rdquo;栏&ldquo;栏&nbsp;栏&quot;栏&lt;栏&gt;栏&amp;栏&copy;栏&reg;栏&radic;栏&times;栏&divide;--%>
<jsp:include page="/footer.jsp"/>
</body>
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/s-wrong-topic/page': 'project/b-ms-cloud/1.x/js/s-wrong-topic/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/s-wrong-topic/page',function(init){
            init({
                range: '/student/test/findRange',  //学段
                subject: '/student/test/findSubject',  //学科
                bookVersion: '/student/test/getBookVersion',  //教材版本
                obligatoryId:'/student/test/getObligatory',  //必修
                tree:'/student/test/getTreeRefChild', //树json
                examList: '/student/test/testList',   //习题考试列表
                actionExam: '/student/test/errornote/list',  //考试获取试题
                actionProgress: '/student/test/errornote/jddList',  //进度获取试题
                submit: '/student/test/question/errorfx', //提交数据
                removeQuestion: '/student/test/errorDel',  //删除题目（status等于0表示成功）
                time:'<%=ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_POINT)%>'
            });
        });
        serachFenye=function(pageNo){
            if(!pageNo){
                pageNo=0;
            }
            $("#pageNo").val(pageNo);
            $('#jp-form-chos').submit();
        }
        $('#jp-form-chos').submit(function(){
            var commitUrl=$("#jp-form-chos").attr("action");
                var options = {
                    url : commitUrl ,
                    contentType : "multipart/form-data; charset=utf-8",
                    type : 'POST',
                    success : function(data) {
                        $("#questionList").html(data);
                        window.initStatus && window.initStatus();
                        window._ifhtml && window._ifhtml.ifhtmlheight();
                        $('body,html').animate({ scrollTop: 0 }, 200);
                    }
                };
                // 提交表单
                $(this).ajaxSubmit(options);
                // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
                return false;
        });
    </script>

</fe:html>