<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<%
    Boolean loadKonwledgeObj= CacheTools.getCache(GlobalConstant.loadKonwledge, Boolean.class);
    boolean loadKonwledge=loadKonwledgeObj==null?false:loadKonwledgeObj;
    String name=loadKonwledge?"知识点":"进度点";
    Boolean finishedObj= CacheTools.getCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish+loadKonwledge, Boolean.class);
    boolean finished=finishedObj==null?false:finishedObj;
        Map<String,List<?>> sheetMap=CacheTools.getCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM+loadKonwledge, Map.class);
        if(sheetMap!=null){
            Iterator it=sheetMap.keySet().iterator();
            while (it.hasNext()){
                String key= (String) it.next();
                List<?> list=sheetMap.get(key);
                if(list!=null){ %>
                    <div><%=key%><%=name%>数量：<%=list.size()%></div>
               <% }
            }
        }
       String param=loadKonwledge?"":"?f=f";
      if(finished){%>
          <form action="/exam/core/excelDowload<%=param%>" method="get" >
              <input type="submit" value="下载<%=name%>统计列表" />
          </form>
    <%} %>
完成状态：<%=finished%>
</body>
<script>
    var finished='<%=finished%>';
    var t=setTimeout(function () {   // 定时循环：setInterval 定时：setTimeout
        location.href="/exam/core/excelDowload<%=param%>";
    }, 2000);
    if(finished=='true'){
        clearTimeout(t);
    }

</script>