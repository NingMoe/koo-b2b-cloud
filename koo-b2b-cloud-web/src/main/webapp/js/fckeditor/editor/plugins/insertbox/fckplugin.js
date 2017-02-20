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


var oInsertBox = new FCKToolbarButton('InsertBox', '方框');

//设置按钮的图标路径
oInsertBox.IconPath = FCKPlugins.Items['insertbox'].Path + 'placeholder.png';
//注册按钮项
FCKToolbarItems.RegisterItem('InsertBox', oInsertBox);

// The object used for all Format168 operations.
var InsertBox = new Object();

InsertBox = function(name){
    this.Name = name;
}

//FCK_TRISTATE_ON为默认是选中状态
//下面的两个方法是实现接口的两个必须的方法，否则会报脚本错误
InsertBox.prototype.GetState = function() {
    
    return FCK_TRISTATE_OFF;
}
var FCK_INSERTBOX_INDEX=1;
function addInsertBoxIndex1(){
	FCK_INSERTBOX_INDEX=FCK_INSERTBOX_INDEX+1;
}
function getInsertBoxIndex(){
	return FCK_INSERTBOX_INDEX;
}
function setInsertBoxIndex(i){
	FCK_INSERTBOX_INDEX=i;
}
//此方法是点击按钮后要完成的操作
InsertBox.prototype.Execute = function(){
if(_isSelectedSome()){
	setTimeout('alert("不要选中内容")',200);
	return ;
}
    var pp=FCK.InsertElement("span");
    pp.className='tb1';
    pp.innerHTML='&nbsp;';
    //pp.innerHTML=getInsertBoxIndex();
    //addInsertBoxIndex1();
    if(typeof(parent.fck_insertbox_fun)=='function'){
    	var v = FCK.EditorDocument.body.innerHTML ;
    	parent.fck_insertbox_fun(v,FCK.EditorDocument.body,FCK.Name);
    }
}
function _isSelectedSome(){
	var a=FCKSelection.GetSelection();
	var b=false;
	if(!FCKBrowserInfo.IsIE){
		c=a.toString();
		if(c.length>0){b=true;}
	}else{
		if(a['type']!='None'){
			b=true;
	}
	return b;
}
}
function fck_callBackFun(html){
	FCK.EditorDocument.body.innerHTML=html;
}
//-->utopia add start for outo delete
// On Gecko we must do this trick so the user select all the SPAN when clicking on it.
InsertBox._SetupClickListener = function()
{
	InsertBox._ClickListener = function( e )
	{
		if ( e.target.tagName == 'SPAN' && (e.target._fckplaceholder ||e.target.className=='tb1')){
			FCKSelection.SelectNode( e.target ) ;
		}
	}

	FCK.EditorDocument.addEventListener( 'click', InsertBox._ClickListener, true ) ;
}

// Open the Placeholder dialog on double click.
InsertBox.OnDoubleClick = function( span )
{
	if ( span.tagName == 'SPAN' && (span._fckplaceholder || span.className=='tb1' )){
		if(typeof(parent.fck_insertbox_fun)=='function'){
			span.parentNode.removeChild(span);
	    	var v = FCK.EditorDocument.body.innerHTML ;
	    	parent.fck_insertbox_fun(v,FCK.EditorDocument.body,FCK.Name);
	    }
	}
}
FCK.RegisterDoubleClickHandler( InsertBox.OnDoubleClick, 'SPAN' ) ;
//<--utopia add end for outo delete


// Register the related command
FCKCommands.RegisterCommand('InsertBox', new InsertBox('InsertBox'));