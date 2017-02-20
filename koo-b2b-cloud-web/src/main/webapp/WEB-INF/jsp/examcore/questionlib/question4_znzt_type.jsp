<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>

    <c:forEach items="${questionType}" var="tag"  varStatus="status">
        <span class="practice-sle">
                    <label class="set-num-tit">${tag.name}</label>
                    <span class="jp-set-num set-num">
                        <a href="javascript:;" class="jp-minus-link minus-link"></a>
                        <input type="text" value="0" id="${tag.id}" class="jp-num-input num-input"><span class="i">道</span>
                        <a href="javascript:;" class="jp-add-link add-link"></a>
                    </span>
                </span>
    </c:forEach>
