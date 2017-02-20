<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<title>导航页面</title>
<body>
<ul>
<li>老师</li>
<li><a href="/teacher/task/assign" target="_blank">布置作业</a></li>
<li><a href="/teacher/task/list" target="_blank">作业列表</a></li>
<li><a href="/teacher/task/situation?examId=2&classId=99" target="_blank">作业情况</a></li>
<li><a href="/teacher/task/mark?examId=2&classId=99" target="_blank">作业批阅</a></li>
<li><a href="/teacher/task/comment?examId=2&classId=99" target="_blank">作业讲评</a></li>
<li><a href="/student/classes/perfectInfo?examId=2&classId=99" target="_blank">学生完善资料</a></li>
<li><a href="/teacher/info/index" target="_blank">个人中心</a></li>
<li><a href="/teacher/info/myClasses" target="_blank">个人中心--我的班级</a></li>
</ul>
<ul>
<li>学生</li>
<li><a href="/student/classes/perfectInfo" target="_blank">加入班级页面</a></li>
<li><a href="/student/pc/index" target="_blank">作业列表</a></li>
<li><a href="http://cloud.trunk.koolearn.com/exam/core/syncQuestion" target="_blank">同步数据</a></li>
</ul>
</body>
</html>