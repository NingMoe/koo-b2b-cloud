/**
 * upImg: 1.0.1
 * User: huanhuan
 * Email: th.wanghuan@gmail.com
 * 微博: huanhuan的天使
 * Date: 14-8-31.
 * 图片上传接口调用
 * $('#upword').change(function(){
        $(this).previewImages(
                this, //对象自己
                [
                	{partId:'imgBox',selfId:'imgView', width:56, height:56}  //需要改变的图，以及图的大小。可以是多个
                ]
        );
    });
 *
 **/
 //图片上传预览    IE是用了滤镜。
$.fn.extend({
    previewImages : function(file,obj){
        var fileDiv = document.getElementById(file.id),
            format = 'jpg jpeg png gif',
            i = 0,
            len = obj.length;
        for(; i<len; i++){
            var imgDiv = document.getElementById(obj[i].selfId),
                partImgDiv = document.getElementById(obj[i].partId),
                imgW = obj[i].width,
                imgH = obj[i].height;
            if(fileDiv.files && fileDiv.files[0]){
                var fileName = file.files[0].name,
                    fileSuffix = fileName.substring(fileName.lastIndexOf('.')+1).toLocaleLowerCase();
                //格式不存在
                if(format.indexOf(fileSuffix)==-1){
                    layer.alert('文件格式不符，请上传jpg、jpeg、png、gif格式', -1, !1);
                    return false;
                }
                //火狐下，直接设img属性
                imgDiv.style.display = 'inline-block';
                imgDiv.style.width = imgW+'px';
                imgDiv.style.height = imgH+'px';
                //imgObjPreview.src = docObj.files[0].getAsDataURL();
                //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
                imgDiv.src = window.URL.createObjectURL(fileDiv.files[0]);
            }else{
                //IE下，使用滤镜
                fileDiv.select();
                fileDiv.blur();
                var imgSrc = document.selection.createRange().text;
                //必须设置初始大小
                partImgDiv.style.width = imgW+'px';
                partImgDiv.style.height = imgH+'px';
                imgDiv.className='hide';
                //图片异常的捕捉，防止用户修改后缀来伪造图片
                try{
                    partImgDiv.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                    partImgDiv.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
                }catch(e){
                    alert("您上传的图片格式不正确，请重新选择!");
                    return false;
                }
                document.selection.empty();
            }
        }
        return true;
    }
});