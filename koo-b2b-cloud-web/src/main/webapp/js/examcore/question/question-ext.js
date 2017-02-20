//如果当前页面不在iframe中，则强制放入iframe中。

if(window==top){
	//window.location.href = "/pages/main.jsp?rwin="+window.location.href.replace(/.*?\/([^\/.]*?)\.jsp*/ig,"$1");
}

$(function(){
	$(".fck").each(function(i){
		i = i+1+100;
		addFck(this,i);
		$(this).closest(".fmt").attr("n",i);
	});
});

