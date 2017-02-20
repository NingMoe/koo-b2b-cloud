<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="kl" uri="kl_security" %>
<fe:html title="资源管理" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/op-zyk/page.css">
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/opheader.jsp">
        <jsp:param name="nav" value="zy"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="p-zygl-top">
        <div class="i-box p-position">
            <form action="#" method="post" id="jp-zygl-form">
                <input type="hidden" name="source" value="<%=GlobalConstant.RESOURCE_SOURCE_SYSTEM%>"/>
                <p>
                <label>资源名称：</label>
                <input id="jp-zyk-name" class="p-w278" type="text" name="keyTxt">
                <label>学科：</label>
                <select class="i-sel2 all-sel" id="sel-show1" name="subjectId">
                    <option value="">全部</option>
                </select>
                <label>学段：</label>
                <select class="i-sel2 all-sel" id="sel-show2" name="rangeId">
                    <option value="">全部</option>
                </select>
            </p>
            <p>
                <label>更新时间：</label>
                <input class="p-w120 p-set-tim-input jp-set-startime" type="text" name="updateBeginTime" readonly>
                <span class="p-span">至</span>
                <input class="p-w120 p-set-tim-input jp-set-endtime" type="text" name="updateEndTime" readonly>
                <label>上传人：</label>
                <select class="i-sel2" id="jp-uploaduser" name="uploadUser">
                    <option value="">全部</option>
                </select>
                <label>类型：</label>
                <select class="i-sel2" id="jp-typelist" name="type">
                    <option value="">全部</option>
                </select>
            </p>
            <div class="p-btns">
                <button type="button" id="jp-search-btn">搜索</button>
                <button type="reset">重置</button>
            </div>
            <input type="hidden" name="pageNo" value="0" id="jp-page-num">
        </form>
    </div>
</div>
<div class="i-main p-zy-manag">
    <p class="p-upload-btn">
        <a href="/operation/core/resource/uploadResourceIndex">上传资源</a>
    </p>
    <div class="p-zy-list" id="jp-zy-list">
        <%--资源列表--%>
    </div>
    <!-- 分页 -->
    <div class="p-pager-box"></div>
    <kl:view url="/operation/core/resource/editResourceIndex">
        <input type="hidden" id="jp-amendUrl" />
    </kl:view>
</div>
<jsp:include page="/footer.jsp"/>

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/op-zyk/page': 'project/b-ms-cloud/1.x/js/back-end/op-zyk/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/op-zyk/page',function(init){
            init({
                uploadUser : '/operation/core/resource/uploadUser',//上传人列表
                typeList : '/operation/core/resource/typeList',//资源类型
                subjectIdUrl : '/operation/core/resource/getAllSubject',//学科
                rangeIdUrl : '/operation/core/resource/getTreeData',//学段
                resourcesList : '/operation/core/resource/search',//资源列表请求数据
                previewUrl : '/operation/core/resource/preview/reader/',//预览跳转地址
                amendUrl : '<kl:view url="/operation/core/resource/editResourceIndex">/operation/core/resource/editResourceIndex</kl:view>' ,//修改跳转地址（空不显示次按钮）
            });
        });
    </script>

</fe:html>