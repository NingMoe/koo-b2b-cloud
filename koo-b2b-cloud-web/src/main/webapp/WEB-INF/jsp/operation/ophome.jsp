<%@ page import="com.koolearn.cloud.common.serializer.CommonInstence" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="运营端-数据字典管理" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/op-home/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/opheader.jsp">
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <ul class="p-op-wrap fc jp-op-ul">
        <li>
            <div class="p-tab">
                <span>题库累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
        <li>
            <div class="p-tab">
                <span>资源库累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
        <li>
            <div class="p-tab">
                <span>视频累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
        <li>
            <div class="p-tab">
                <span>实验视频累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
        <li>
            <div class="p-tab">
                <span>学校累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
        <li>
            <div class="p-tab">
                <span>用户累计</span>
                <p title="503289">503289</p>
            </div>
            <div class="p-tab">
                <span>本月新增</span>
                <p title="289">289</p>
            </div>
        </li>
    </ul>
</div>
<jsp:include page="/footer.jsp"/>

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/op-home/page': 'project/b-ms-cloud/1.x/js/back-end/op-home/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/op-home/page',function(init){
            init();
        });
    </script>

</fe:html>