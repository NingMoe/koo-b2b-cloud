<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<script type="text/javascript">
    seajs.config({
        base: '<%=PropertiesConfigUtils.getProperty("domains.ui")%>',
        importBase:'http://static.koocdn.com/', // 用于辨识css中图片的绝对地址，如果和当前base的不一样（测试环境）可以转换成base的地址来正常打开图片
        // 某些自加载插件的模块不能combo，如tinymce
        comboExcludes: function(uri) {
            // 某些特定目录下的文件不合并
            if (uri.indexOf('/common/tinymce/') > 0) {
                return true
            }
        }
    });
</script>
<fe:seaConfig>
    alias: {
    'common/backbone/backbone': 'common/backbone/backbone.js',
    'common/backbone/plugins/backbone-nested': 'common/backbone/plugins/backbone-nested.js',
    'common/crop-upload/crop-upload': 'common/crop-upload/crop-upload.js',
    'common/dialog/dialog': 'common/dialog/dialog.js',
    'common/file-upload/file-upload': 'common/file-upload/file-upload.js',
    'common/jcrop/jcrop': 'common/jcrop/jcrop.js',
    'common/jstree/jstree': 'common/jstree/jstree.js',
    'common/kalendae/kalendae': 'common/kalendae/kalendae.js',
    'common/kooplayer/kooplayer': 'common/kooplayer/kooplayer.js',
    'common/kooplayer-collect/kooplayer-collect': 'common/kooplayer-collect/kooplayer-collect.js',
    'common/kooplayer-new-2015/kooplayer': 'common/kooplayer-new-2015/kooplayer.js',
    'common/linkup/linkup': 'common/linkup/linkup.js',
    'common/moment/moment': 'common/moment/moment.js',
    'common/pickadate/pickadate': 'common/pickadate/pickadate.js',
    'common/raphael/raphael': 'common/raphael/raphael.js',
    'common/select2/select2': 'common/select2/select2.js',
    'common/tinymce/tinymce': 'common/tinymce/tinymce.js',
    'common/validate/validate': 'common/validate/validate.js',
    'common/ztree/ztree': 'common/ztree/ztree.js',
    'common/viewInit/baseViewInit': 'common/viewInit/baseViewInit.js',
    'common/viewInit/viewBindEvent1.1': 'common/viewInit/viewBindEvent1.1.js'
    }
</fe:seaConfig>