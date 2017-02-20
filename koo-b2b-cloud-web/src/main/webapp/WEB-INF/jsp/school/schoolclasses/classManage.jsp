<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<fe:html title="班级" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-class/page.css">
<head>

    <!-- 上传文件 -->
    <script type="text/javascript" src="http://b-ms-cloud.ui.koolearn-inc.com/project/b-ms-cloud/1.x/common/webuploader/js/webuploader-min.js"></script>
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/back-end/sch-class/page': 'project/b-ms-cloud/1.x/js/back-end/sch-class/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-class/page',function(init){
            init({
                subjectClasses:'/school/classes/findAllSubjectClasses',//学科、学段数据
                tabListPhp:'/school/classes/findClassPage',//tab数据
                shieldingClassPhp:'/school/classes/closeOrOpenClasses',//封闭
                managementUrl:'/school/classes//goClassTeacherOrStudentList',//管理-跳转页面
                fileSwfUrl:'/project/b-ms-cloud/1.x/common/webuploader/js/Uploader.swf',//上传插件兼容ie8/9浏览器flash文件
                fileUrlDo:'/school/classes/uploadClassExcel',//导入上传地址
                formDataObj:'${uuid}'
            });
        });
    </script>
</head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="bj"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="p-zygl-top">
    <div class="i-box p-class">
        <form action="#" method="post" id="jp-class-form">
            <input id="jp-range" type="hidden" value="" name="rangeId">
            <input id="jp-range-name" type="hidden" value="" name="rangeName">
            <input id="jp-grade" type="hidden" value="0" name="classesLevel">
            <input id="jp-state" type="hidden" value="0" name="status">
            <input id="jp-type" type="hidden" value="10" name="type">
            <!-- 当前页码 -->
            <input type="hidden" name="currentPage" value="0" id="jp-page-num">
            <dl class="p-class-navs" id="jp-class-navs">
                <dt>学段</dt>
                <dd id="jp-list-range">
                    <!-- <a class="cur" href="javascript:;" data-id="1">小学</a>
                    <a href="javascript:;" data-id="2">初中</a>
                    <a href="javascript:;" data-id="3">高中</a> -->
                </dd>
                <dt>年级</dt>
                <dd id="jp-list-grade">
                    <a class="cur" href="javascript:;" data-grade="0">全部</a>
                    <!-- <a href="javascript:;" data-grade="1">一年级</a>
                    <a href="javascript:;" data-grade="2">二年级</a>
                    <a href="javascript:;" data-grade="3">三年级</a>
                    <a href="javascript:;" data-grade="4">四年级</a>
                    <a href="javascript:;" data-grade="5">五年级</a>
                    <a href="javascript:;" data-grade="6">六年级</a> -->
                </dd>
                <dt>状态</dt>
                <dd>
                    <a class="cur" href="javascript:;" data-state="0">有效</a>
                    <a href="javascript:;" data-state="1">已毕业</a>
                    <a href="javascript:;" data-state="10">封闭</a>
                </dd>
                <dt>班级类型</dt>
                <dd>
                    <a class="cur" href="javascript:;" data-type="10">全部</a>
                    <a href="javascript:;" data-type="1">学科班</a>
                    <a href="javascript:;" data-type="0">行政班</a>
                </dd>
            </dl>
        </form>
    </div>
</div>
<div class="i-main p-agent">
    <div class="p-new-box">
        <a href="javascript:;" class="new-btn" id="jp-export-class">导入数据</a>
        <a class="p-green-a fr" target="_parent" href="/school/classes/downClassTemplate?type=c" id="jp-download-file">下载模板</a>
    </div>
    <div class="p-table-box">
        <table cellpadding="0" cellspacing="0" border="0">
            <thead>
            <tr>
                <th width="30%">班级名称</th>
                <th>学段</th>
                <th>年级</th>
                <th>班级码</th>
                <th>老师人数</th>
                <th>学生人数</th>
                <th>班级类型</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="jp-class-table">
            <!-- <tr data-id="1">
                <td>
                    <span class="p-name" title="十一中学">七年级(1)班</span>
                </td>
                <td>初中</td>
                <td>七年级</td>
                <td>12345</td>
                <td>111</td>
                <td>222</td>
                <td>学科班</td>
                <td>有效</td>
                <td>
                    <a href="javascript:;" class="op-btn jp-shiel">封闭</a>
                    <a href="javascript:;" class="op-btn jp-manag">管理</a>
                </td>
            </tr> -->
            </tbody>
        </table>
    </div>
    <!-- 分页 -->
    <div class="p-pager-box"></div>
</div>
</body>
</fe:html>