<%@ page import="java.util.UUID" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/js/webuploader/js/webuploader.css"/>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/js/webuploader/js/webuploader.js"></script>
</head>
<body>
<span id="uploadBtnContainer"></span>
<span id="uploadBtnContainer1"></span>
<ul id="fileListView"><!--显示已加入上传队列的文件--></ul>
<ul id="imagereview"><!--图片预览--></ul>
<!-- 上传后进度条 -->
<div id="progressDiv" class="uploader-list">
    <div class="progress progress-striped active">
        <div class="progress-bar" role="progressbar" style="width: 0%"></div>
    </div>
</div>
<script>
    var count=0;
    var $list = $("#progressDiv"),
        $fileListView = $("#fileListView"),
        $imagereview=$("#imagereview");
    var uploader = WebUploader.create({
        // swf文件路径
        swf: '/js/webuploader/js/Uploader.swf',
        // 文件接收服务端。
        server: "/web/upload/uploadFile",
        formData: {uuid: '<%=UUID.randomUUID()%>'},//文件上传请求的参数表，每次发送都会发送此对象中的参数
        fileNumLimit: 3,//验证文件总数量, 超出则不允许加入队列，自动丢弃后面的文件
        fileSingleSizeLimit: 100 * 1024 * 1024, //验证单个文件大小是否超出限制, 超出则不允许加入队列
        threads: 1,//上传并发数。允许同时最大上传进程数
        auto: true,
        chunked: true,
        chunkSize: 11 * 1024 * 1024,
        chunkRetry:4 ,//默认2如果某个分片由于网络问题出错，允许自动重传多少次
        pick:{//选择文件的按钮容器，不指定则不创建按钮
            id:'#uploadBtnContainer',
            innerHTML:'选择文件',
            multiple:true//是否开起同时选择多个文件能力 fileNumLimit
        },
        resize: false,// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        accept: {
            title: 'file',
            extensions: 'jpg,png,mp3,mp4,pdf,ppt,gif,jpeg,doc,docx,pptx,gz,tar,rar',//多文件上传时，自动丢弃非法格式的文件，上传符合要求的
            mimeTypes: 'file/*'
        },
        thumb :{   //配置生成缩略图的选项
            width: 120,height:120,
            quality: 70,// 图片质量，只有type为`image/jpeg`的时候才有效
            allowMagnify: true,// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false
            crop: true,// 是否允许裁剪
            // 为空的话则保留原有图片格式。
            // 否则强制转换成指定的类型。
            type: 'image/jpeg'
        },
        duplicate:true //去重， 根据文件名字、文件大小和最后修改时间来生成hash Key
//       ,fileNumLimit:5, //验证文件总数量, 超出则不允许加入队列。
//       fileSizeLimit: 111 * 1024 * 1024, //验证文件总大小是否超出限制, 超出则不允许加入队列。
//       fileSingleSizeLimit:11 * 1024 * 1024,  //验证单个文件大小是否超出限制, 超出则不允许加入队列
    });
    uploader.addButton({
        //添加文件选择按钮，如果一个按钮不够，需要调用此方法来添加。参数跟options.pick
        id: '#uploadBtnContainer1',
        innerHTML: '选择文件1'
    });
    uploader.on('uploadBeforeSend', function (object, data, headers) {
        // 1.扩展上传参数：当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次
        data.userName ="ghs";//formData
    });
    uploader.on('beforeFileQueued', function (file) {
       //2.校验: 当文件被加入队列之前触发，此事件的handler返回值为false，则此文件不会被添加进入队列。
        //校验
        var extension = file.ext,//上传文件扩展名
                fileSize = file.size;//上传文件大小
        if(extension == 'ppt' || extension == 'doc' || extension == 'docx') {
            if (fileSize > 1024 * 1024 * 10) {
                $list.hide();
                window._hintPop.hintFun("文件大小超出限制，最大10m");
                return false;
            }
        }
    })
    uploader.on('fileQueued', function (file) {
        //3.加入上传队列后：当文件被加入队列以后触发
        $fileListView.append('<li id="' + file.id + '" class="aaa li_' + count + '" name="li_' + count + '" ><h4><span>附件：</span><b>' + file.name + '</b>' +
                '<input id="input_' + count + '" class="p-amend jp-inpts" type="text" value="' + file.name + '" placeholder="" name="">' +
                '<em onclick="setName(' + count + ')">确定</em></h4><div class="p-as">' +
                '<a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(' + count + ')"></a>' +
                '<a class="p-down bbb" style="display: none" href="javascript:;" onclick="down(' + count + ')"></a>' +
                '<a class="p-del" href="javascript:;" onclick="del(' + count + ')"></a></div></li>');
        uploader.makeThumb( file, function( error, ret ) {
            // 创建缩略图,如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100， 覆盖thumb的配置
            if ( !error ) {
                //图片预览,ret是base64的图片地址:data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAA....
                $imagereview.append('<img src="' + ret + '" />');
            }
        } );
        uploader.md5File( file )
                .progress(function(percentage) {
                    // 及时显示进度
                    console.log('md5File.Percentage:', percentage);
                })
                .then(function(val) {
                    // 完成
                    console.log('md5 result:', val);
                });
    });
    //上传过程中触发，携带上传进度
    uploader.on('uploadProgress', function (file, percentage) {
        console.log('uploadProgress:', percentage);
        //创建进度条
        var $li = $('#' + file.id),
                $percent = $li.find('.progress .progress-bar');
        if (!$percent.length) {
            $percent = $('<div class="progress">' +
                    '<div class="progress-bar" role="progressbar" style="width: 0%;background:#4fd07f;max-height: 20px">' +
                    '&nbsp;</div>' +
                    '</div>').appendTo($li).find('.progress-bar');
        }
        $percent.css('width', percentage * 100 + '%');
        // 避免重复创建
        $('#filePath').val("");
    });

    uploader.on('uploadSuccess', function (file, responseObj) {
        //当文件上传成功时触发 ,responseObj服务端返回的数据
        if (responseObj.error == true) {
            $(".li_" + count).remove();
        } else {
            $("#fileId").append(
                            '<div id="div_' + count + '">' +
                            '<input type="hidden" value="' + responseObj.id + '" name="attachments[' + count + '].attachmentId"/>' +
                            '<input type="hidden" value="0" name="attachments[' + count + '].attachmentType"/>' +
                            '<input id="attachmentName_' + count + '" type="hidden" value="' + responseObj.name + '" name="attachments[' + count + '].attachmentName"/>' +
                            '<input id="attachmentOrder_' + count + '" type="hidden" value="' + count + '" name="attachments[' + count + '].sort"/>' +
                            '</div>');
            count++;
        }
        uploader.removeFile(file);//上传成功把文件移出队列
    });
    //上传格式错误
    uploader.on('error', function (message) {
        if ("F_EXCEED_SIZE" == message) {
            alert("最大文件限制为100m，您的文件已超过最大限制");
        } else if ("Q_TYPE_DENIED" == message) {
            alert("上传格式错误");
        } else if('Q_EXCEED_NUM_LIMIT'==message){
            alert("超过文件上传个数，自动丢弃后面的文件");
        }else {
            alert("上传文件错误，请尝试刷新页面重新上传");
        }
    });
    //上传错误
    uploader.on('uploadError', function (file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });
    //不管成功或者失败，文件上传完成时触发
    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });

    function del(count){
       // class="aaa li_' + count + '"
        $(".li_" + num).remove();
        //id="div_' + count + '"
        $("#div_" + num).remove();
    }
</script>
</body>
</html>