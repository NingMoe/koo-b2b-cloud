<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<fe:html title="资源库" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk/page.css">
<head>
    <title></title>
</head>
<body>
<form name="add" action="<%=request.getContextPath()%>/school/teacher/uploadTeacherExcel" method="post"
      enctype="multipart/form-data">
    <p class="upload undis"><span>添加 ：/school/classes/uploadTeacherExcel  uploadClassExcel</span> </p>
    <input class="webuploader-element-invisible" name="file" multiple="multiple" accept="file/*" type="file">
    <input type="submit" value="导入信息" >
</form>
</body>
</fe:html>
