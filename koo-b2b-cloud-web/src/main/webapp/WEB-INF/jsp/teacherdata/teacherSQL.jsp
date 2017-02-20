<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fe:html title=" " initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zc-wszl/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zc-wszl/page': 'project/b-ms-cloud/1.x/js/t-zc-wszl/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        function goStudentList( ){
            var tearInfo = $("#readsql").val();
            var turnForm = document.createElement("form");
            //一定要加入到body中！！
            document.body.appendChild(turnForm);
            turnForm.method = "post";
            turnForm.action = "/teacher/data/teacherInfo/write";
            turnForm.target = '_self';
            //创建隐藏表单
            var subjectIdElement = document.createElement("input");
            subjectIdElement.setAttribute("name","subjectId");
            subjectIdElement.setAttribute("type","hidden");
            subjectIdElement.setAttribute("value",tearInfo);
            turnForm.appendChild(subjectIdElement);
            turnForm.submit();
        }
    </script>
</head>
<body>
<form >
    <table>
        <tr>
        <textarea name="readsql" id="readsql" rows="5" cols="30">

        </textarea>

        </tr>
        <tr>
        <textarea name="showSql" id="showSql" rows="60" cols="60" >
                ${result}
        </textarea>
        </tr>
        <input type="button" name="ti" value="提交" onclick="goStudentList()"/>
    </table>

</form>
</body>
</fe:html>
