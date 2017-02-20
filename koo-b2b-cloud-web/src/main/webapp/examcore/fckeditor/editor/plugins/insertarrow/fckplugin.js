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


var oInsertArrow = new FCKToolbarButton('InsertArrow', '箭头');

//设置按钮的图标路径
oInsertArrow.IconPath = FCKPlugins.Items['insertarrow'].Path + 'placeholder.png';
//注册按钮项
FCKToolbarItems.RegisterItem('InsertArrow', oInsertArrow);

// The object used for all Format168 operations.
var InsertArrow = new Object();

InsertArrow = function(name){
    this.Name = name;
}

//FCK_TRISTATE_ON为默认是选中状态
//下面的两个方法是实现接口的两个必须的方法，否则会报脚本错误
InsertArrow.prototype.GetState = function() {
    
    return FCK_TRISTATE_OFF;
}

//此方法是点击按钮后要完成的操作
InsertArrow.prototype.Execute = function(){
    var pp=FCK.InsertElement("span");
    pp.className='tb3';
    pp.innerHTML='&nbsp;';
	pp.contentEditable = false ;    
}

// Register the related command
FCKCommands.RegisterCommand('InsertArrow', new InsertArrow('InsertArrow'));