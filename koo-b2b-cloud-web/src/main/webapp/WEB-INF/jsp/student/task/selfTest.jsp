<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="组题自测" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/s-setself-test/page.css">
    <body>
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="zy"/>
    </jsp:include>
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
    <div class="i-main p-setself">
        <div class="p-top fc">
            <form id="jp-test-form" action="" method="">
                <label class="p-tabl fl">学科</label>
                <dl class="p-top-dl fl" id="jp-discipline">
                    <dt> </dt>
                    <dd class="p-top-dd" id="">
                        <div class="p-pd12">
                            <div class="p-psnew">
                                <p>科目选择</p>
                                <p>教材选择</p>
                                <p>版本选择</p>
                            </div>
                            <div class="p-selnew">
                                <select class="i-sel1 sel1" id="sel-show" name="subjectId">  </select>
                                <select class="i-sel1 sel2" id="sel-show1" name="rangeId">  </select>
                                <select class="i-sel1 sel3" id="sel-show2" name="bookVersion">  </select>
                            </div>
                        </div>
                        <a id="jp-sure" class="p-sure" href="javascript:;">确定</a>
                    </dd>
                </dl>
                <select id="obligatoryId" class="i-sel4 fl p-bxiu" name="obligatoryId">
                    <option value="">必修一</option>
                </select>
                <label class="p-tabl fl">题目数量</label>
                <select class="i-sel2 fl p-tilenumb" name="number" >
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10" selected = "selected">10</option>
                </select>
                <input id="jp-classesIds" type="hidden" value="" name="tagId">
            </form>
        </div>
        <div class="p-cont">
            <h2>选择章节</h2>
            <ul class="p-cont-ul fc" id="jp-contul">
                 <li>  <dl> <dd> </dd>  </dl> </li>
            </ul>
        </div>
        <p class="p-pbotm">
            <a id="jp-begin-test" class="green-btn green-btn-style" href="javascript:;">开始自测</a>
        </p>
    </div>
<!-- 试题栏-->
<jsp:include page="/footer.jsp"/>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/s-setself-test/page': 'project/b-ms-cloud/1.x/js/s-setself-test/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/s-setself-test/page',function(init){
            init({
                //联级
                selProductLine: '/student/test/findSubject',//科目
                selExamSeasonId: '/student/test/findRange',//学段?subjectId=2464
                selSubjectId: '/student/test/getBookVersion',//版本?rangeId=26995
                selLastId: '/student/test/getStr',
                obligatoryId:'/student/test/getObligatory',//必修
                chooseClassUrl:'/student/test/getTreeRefChild', //页面中章节加载
                testUrl:'/student/test/question/ztzc',//开始自测按钮点击
                subjectId:'${subjectId}'
            });
        });
    </script>

</fe:html>