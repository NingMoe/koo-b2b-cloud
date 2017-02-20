<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.cloud.util.IndexNameUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<link rel="stylesheet" type="text/css" href="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/themes/default/css/ueditor.css">
<!--百度编辑器图片上传路径-->
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/lang/zh-cn/zh-cn.js"></script>
<!-- 公式插件begin   -->
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/kityformula-plugin/addKityFormulaDialog.js"></script>
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/kityformula-plugin/getKfContent.js"></script>
<script type="text/javascript" src="<%=IndexNameUtils.getValueByKey(GlobalConstant.CLOUD_HOST_DOMAIN)%>/ueditor/kityformula-plugin/defaultFilterFix.js"></script>
<!-- 公式插件end   -->
<!-- UE end  -->
<script>
    $(function(){
        //ue start
        $('.ueditor').each(function(){
            var newid,ue;
            var $textareaThis=$(this);
            var $ueDiv;
            $(this).css({
                "width":"700px",
                "height":"200px"
            });
            if($(this).attr("id")){
                ue=UE.getEditor($(this).attr("id"));
            }else{
                newid = 'editor-' +parseInt(Math.random()*10)
                $(this).attr("id", newid);
                ue=UE.getEditor(newid);

            }
            ue.addListener("blur", function (type, event) {
                $ueDiv=$textareaThis;//||$textareaThis.prev('.ueditor')
                var textarea = $textareaThis;
                if ($ueDiv.hasClass("ueditorShort") ){
                    // 绑定简答题答案操作 成富文本  class='ueditor ueditorShort'
                    var _ur = jQuery("#hidUrKey").val();//获取取消localStroge key
                    var _id =  textarea.attr('id');;
//                    var _v = ue.getAllHtml();//this.value.trim();
                    var _v = ue.getContent();//this.value.trim();
                    var _temp = takeStructureLS(_ur);
                    _temp[_id] = _v;
                    LSSET(_ur, tostr(_temp));
                }
            });
        });
        //ue end
    });
</script>