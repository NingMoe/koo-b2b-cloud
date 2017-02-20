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


var oInsertBlank = new FCKToolbarButton('InsertBlank', '填空');

//设置按钮的图标路径
oInsertBlank.IconPath = FCKPlugins.Items['insertblank'].Path + 'placeholder.png';
//注册按钮项
FCKToolbarItems.RegisterItem('InsertBlank', oInsertBlank);

// The object used for all Format168 operations.
var InsertBlank = new Object();

InsertBlank = function(name){
    this.Name = name;
}

//FCK_TRISTATE_ON为默认是选中状态
//下面的两个方法是实现接口的两个必须的方法，否则会报脚本错误
InsertBlank.prototype.GetState = function() {
    
    return FCK_TRISTATE_OFF;
}
var FCK_InsertBlank_INDEX=1;
function addInsertBlankIndex1(){
	FCK_INSERTBLANK_INDEX=FCK_INSERTBLANK_INDEX+1;
}
function getInsertBlankIndex(){
	return FCK_INSERTBLANK_INDEX;
}
function setInsertBlankIndex(i){
	FCK_INSERTBLANK_INDEX=i;
}
//此方法是点击按钮后要完成的操作
InsertBlank.prototype.Execute = function(){
    var pp=FCK.InsertElement("span");
    pp.className='tkbox newtkbox';
    pp.contentEditable = false ;
    //pp.innerHTML=getInsertBlankIndex();
    //addInsertBlankIndex1();
    if(typeof(parent.fck_InsertBlank_fun)=='function'){
    	var v = FCK.EditorDocument.body.innerHTML ;
    	parent.fck_InsertBlank_fun(v,FCK.EditorDocument.body,FCK.Name,pp);
    	pp.className="tkbox";
    	//pp前后插入空格
    	var beforeBlank=document.createTextNode("　");
    	var afterBlank=document.createTextNode("　");
    	if(pp.parentNode){
    		pp.parentNode.insertBefore(beforeBlank,pp);
    		pp.parentNode.insertBefore(afterBlank,pp.nextSibling);
    	}
    }
}
function fck_callBackFun(html){
	FCK.EditorDocument.body.innerHTML=html;
}

//-->utopia add start for outo delete
// On Gecko we must do this trick so the user select all the SPAN when clicking on it.
InsertBlank._SetupClickListener = function()
{
	InsertBlank._ClickListener = function( e )
	{
		if ( e.target.tagName == 'SPAN' && (e.target._fckplaceholder ||e.target.className=='tkbox')){
			FCKSelection.SelectNode( e.target ) ;
		}
	}

	FCK.EditorDocument.addEventListener( 'click', InsertBlank._ClickListener, true ) ;
}

// Open the Placeholder dialog on double click.
InsertBlank.OnDoubleClick = function( span )
{
	if ( span.tagName == 'SPAN' && (span._fckplaceholder || span.className=='tkbox' )){
		//var oSpan = FCK.InsertElement( 'span' ) ;
		//oSpan.innerHTML='';
		//span.removeAttribute("_fckplaceholder");
		//span.className="";
		//FCK.InsertElement(oSpan);
		var lineText = span.innerHTML;
		var index = lineText.substring(1,lineText.length-1);
		index = parseInt(index)-1;
		if(typeof(parent.fck_DeleteBlank_fun)=='function'){
	    	var v = FCK.EditorDocument.body.innerHTML ;
	    	parent.fck_DeleteBlank_fun(v,FCK.EditorDocument.body,index,FCK.Name);
//	    	span.parentNode.removeChild(span);
	    }
	}
}
FCK.RegisterDoubleClickHandler( InsertBlank.OnDoubleClick, 'SPAN' ) ;
//<--utopia add end for outo delete

// Register the related command
FCKCommands.RegisterCommand('InsertBlank', new InsertBlank('InsertBlank'));