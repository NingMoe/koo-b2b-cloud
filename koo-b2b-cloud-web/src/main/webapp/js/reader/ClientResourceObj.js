/* @grunt-copy */
/* @grunt-raw */
//老师离线
var offlineUse='<a href="javascript:showLocalResource();" ><em class="sp icw2-s mr5"></em>使用</a>';
var progressBar='<p class="hcjd pos_r"><span style="width:#barValue#%"></span> </p> <p class="ft14">已缓存#barValue#%</p>';
var offlineDownload='<a href="javascript:clientDownload();"  ><em class="sp icw2 mr5"></em>离线使用</a>';
//学生家长离线
var offlineUseStu='<a href="javascript:showLocalResource();" class="ft16 btn3 tac"><em class="sp icw2-s mr5"></em>使用</a>';
var offlineDownloadStu='<a href="javascript:clientDownload();" class="ft16 btn3 tac"><em class="sp icw2 mr5"></em>离线使用</a>';
var progressBarStu='<div class="hcjdbox"><p class="hcjd pos_r"><span style="width:#barValue#%"></span></p><p class="ft14 tac">已缓存#barValue#%</p></div>';

//教师备课下载
//var offlineUseC='<a href="javascript:showLocalResource();" ><em class="sp icw2-s mr5"></em>离线使用</a>';
//var offlineUseC='已下载';
//var progressBarC='<p class="hcjd pos_r"><span style="width:#barValue#%"></span> </p> <p class="ft14">已缓存#barValue#%</p>';
//var offlineDownloadC='<a href="javascript:clientDownload();" class="mr10 ft16 downup">下载</a>';
function CreateResourceObj() {
	    var obj = new Object();

	    obj.ResourceId ;
	    obj.ResourceName ;
	    obj.ResourceType ;
	    obj.CourseName ;
	    obj.CourseTime ;
	    obj.SubjectId ;
	    obj.SubjectName ;
	    obj.ResourceUrl;

	    return obj;
	}
 function CreatePdfObj() {
	    var obj = new Object();

	    obj.url ;
	    obj.page ;
	    obj.totalPage ;
	    obj.bookId ;
	    obj.bookName;
	    obj.author;
	    return obj;
	}
