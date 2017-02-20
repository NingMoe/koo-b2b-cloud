<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<a class="show_div show_div_click" href="javascript:;" 
	data-questionid="${dto.questionDto.question.id}"
	data-questiontypeid="${dto.questionDto.question.questionTypeId}"
>展开详情</a>