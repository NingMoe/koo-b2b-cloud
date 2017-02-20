<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fe:html title="老师" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-tea-stu/page.css">

    <script type="text/javascript" src="http://b-ms-cloud.ui.koolearn-inc.com/project/b-ms-cloud/1.x/common/webuploader/js/webuploader-min.js"></script>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-teacher': 'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-teacher.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-teacher',function(init){
            init({
                api:{
                    //学科学段加载接口
                    ajaxSubjectsUrl:'/school/teacher/findTeacherCondition',
                    //加载教师列表
                    actionUrl: '/school/teacher/findTeacherList',
                    //激活账户请求地址
                    activateUrl: '/school/teacher/updateTeacherOrStudentStatus',
                    //重置密码请求地址
                    resetPwUrl : '/school/teacher/resetTeacherPassword',
                    addTeacherUrl: '/school/teacher/addTeacherBaseInfo',
                    editTeacherUrl: '/school/teacher/updateTeacherBaseInfo',
                    //批量导入相关内容
                    exportUrl: '/project/b-ms-cloud/1.x/json/back-end/op-school/data-schools.php',
                    fileSwfUrl:'/project/b-ms-cloud/1.x/common/webuploader/js/Uploader.swf',//上传插件兼容ie8/9浏览器flash文件
                    fileUrlDo:'/school/teacher/uploadTeacherExcel',//上传地址
                    formDataObj:'${uuid}',//,
                    //检查手机号是否存在Url
                    isExistEmailUrl: '/school/teacher/checkIsExistMail',
                    //检查邮箱是否存在Url
                    isExistMobileUrl:'/school/teacher/checkIsExistMobile',
                    findTeacherById: "/school/teacher/findTeacherInfoForUpdate"
                    //ajaxSchoolTypeUrl:'/project/b-ms-cloud/1.x/json/back-end/sch-tea-stu/schooltypes.php'
                }
            });
        });
    </script>
</head>
    <body>
    <!-- dialog start-->
    <div class="b-dialog p-sub-review" style="display: none">

    </div>
    <!-- dialog end-->
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="ls"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="p-main">
        <div class="i-box search">
            <form>
                <input type="hidden" name="rangeId" value="">
                <input type="hidden" name="rangeName" value="">
                <input type="hidden" name="subjectName" value="">
                <input type="hidden" name="status" value="100">
                <input type="hidden" name="teacherName" value="">
            </form>
            <dl>
                <dt>学段</dt>
                <dd class="first range">

                </dd>
                <dt>学科</dt>
                <dd class="subject">

                </dd>
                <dt>状态</dt>
                <dd class="status">
                    <a href="javascript:;" class="jp-tab-link white-btn-tab on" data-id="100">全部</a>
                    <a href="javascript:;" class="jp-tab-link white-btn-tab "  data-id="0">有效</a>
                    <a href="javascript:;" class="jp-tab-link white-btn-tab " data-id="10">冻结</a>
                </dd>
            </dl>
            <div class="inputWrap absolute">
                <input type="text" placeholder="输入关键词"  id="searchTxt">
                <a href="#" id="jp-search"></a>
            </div>
        </div>
    </div>
    <div class="i-main p-con">
        <p class="p-btns">
            <a class="p-green-a fr" target="_parent" href="/school/classes/downClassTemplate?type=t" id="jp-download-file">下载模板</a>
            <a href="#" class="jp-batch-import mr20">批量导入</a>
            <a href="#" class="jp-add-teacher">添加老师</a>
        </p>
        <div class="p-con-box">
            <div class="p-box-header">
            </div>
            <div class="table-box">

            </div>
            <!-- 分页开始 -->
            <div  class="pagination">
            </div>
            <!-- 分页结束 -->
        </div>

    </div>

    </body>
</fe:html>

