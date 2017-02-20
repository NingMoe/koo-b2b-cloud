<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<script type="text/javascript">
var oFCKeditor = new FCKeditor('${fckFormName}');
    oFCKeditor.BasePath = "${ctx}/js/fckeditor/";
    oFCKeditor.ToolbarSet = "Basic";
    oFCKeditor.Width="100%";
    oFCKeditor.Height="170";
    oFCKeditor.ReplaceTextarea() ;
</script>
