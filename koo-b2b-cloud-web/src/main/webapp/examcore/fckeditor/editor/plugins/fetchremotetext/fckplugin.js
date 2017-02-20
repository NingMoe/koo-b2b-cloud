/**//*
 * FCKeditor - The text editor for internet
 * Copyright (C) 2003-2006 Frederico Caldeira Knabben
 * 
 * Licensed under the terms of the GNU Lesser General Public License:
 *         http://www.opensource.org/licenses/lgpl-license.php
 * 
 * For further information visit:
 *         http://www.fckeditor.net/
 * 
 * "Support Open Source software. What about a donation today?"
 * 
 * File Name: fckplugin.js
 *     Plugin for Format168 background
 *
 * 
 * File Authors:
 *         Yogananthar Ananthapavan (rollbond@gmail.com)
 */

// Create the "Format168" toolbar button
// fecth from remote get te_complexquestion topic 

var oFetchRemoteText = new FCKToolbarButton('FetchRemoteText', '获取大题内容');

//设置按钮的图标路径
oFetchRemoteText.IconPath = FCKPlugins.Items['fetchremotetext'].Path + 'placeholder.jpg';
//注册按钮项
FCKToolbarItems.RegisterItem('FetchRemoteText', oFetchRemoteText);

// The object used for all Format168 operations.
var FetchRemoteText = new Object();

FetchRemoteText = function(name){
    this.Name = name;
}

//FCK_TRISTATE_ON为默认是选中状态
//下面的两个方法是实现接口的两个必须的方法，否则会报脚本错误
FetchRemoteText.prototype.GetState = function() {
    
    return FCK_TRISTATE_OFF;
}

//此方法是点击按钮后要完成的操作
FetchRemoteText.prototype.Execute = function(){
	/*
    var pp=FCK.InsertElement("span");
    pp.className='tb3';
    pp.innerHTML='&nbsp;';
	pp.contentEditable = false ;
	*/    
	_fecth_remote_text();
}
function _fecth_remote_text(){
	if(parent.window.dialogArguments){
		if(parent.window.dialogArguments&&typeof(parent.window.dialogArguments.findFristFwbContent)=='function'){
			var str=parent.window.dialogArguments.findFristFwbContent();
			FCK.EditorDocument.body.innerHTML=str;
		}else{
			alert2("该操作只用来获取大题的题干内容,其他情况操作无效");
		}
	}else{
		if(parent.window.opener&&typeof(parent.window.opener.findFristFwbContent)=='function'){
			var str=parent.window.opener.findFristFwbContent();
			FCK.EditorDocument.body.innerHTML=str;
		}else{
			alert2("该操作只用来获取大题的题干内容,其他情况操作无效");
		}
	}
	
}
function alert2(msg){
	setTimeout('alert("'+msg+'");',100);
}
// Register the related command
FCKCommands.RegisterCommand('FetchRemoteText', new FetchRemoteText('FetchRemoteText'));