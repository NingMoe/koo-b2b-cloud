<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- 右侧 -->
<div class="p-browse" style="padding: 10px 30px 0;">
    <h2>
        ${examName}
    </h2>
</div>
<div>
    <iframe id="jp-iframe" src="/teacher/task/situation?examId=${examId}&source=1&classRoomClassId=${classesId}"  width="838px" height="600px" frameborder="no" border="0"></iframe>
</div>
