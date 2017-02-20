/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2009 Frederico Caldeira Knabben
 *
 * == BEGIN LICENSE ==
 *
 * Licensed under the terms of any of the following licenses at your
 * choice:
 *
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 *
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 *
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * == END LICENSE ==
 *
 * Plugin to insert "Placeholders" in the editor.
 */

// Register the related command.
//FCKCommands.RegisterCommand( 'BoxFormat', new FCKDialogCommand( 'BoxFormat', FCKLang.PlaceholderDlgTitle, FCKPlugins.Items['boxformat'].Path + 'fck_placeholder.html', 340, 160 ) ) ;

// Create the "Plaholder" toolbar button.
var oSentenceFormat = new FCKToolbarButton('SentenceFormat', '点句');

//设置按钮的图标路径
oSentenceFormat.IconPath = FCKPlugins.Items['sentenceformat'].Path + 'placeholder.png';
//注册按钮项
FCKToolbarItems.RegisterItem('SentenceFormat', oSentenceFormat);

// The object used for all Format168 operations.
var FCKSentenceFormats = new Object();

FCKSentenceFormats = function(name){
    this.Name = name;
}

//FCK_TRISTATE_ON为默认是选中状态
//下面的两个方法是实现接口的两个必须的方法，否则会报脚本错误
FCKSentenceFormats.prototype.GetState = function() {
    
    return FCK_TRISTATE_OFF;
}

//此方法是点击按钮后要完成的操作
FCKSentenceFormats.prototype.Execute = function(){
    var el=FCKSelection.GetSelection();
    if(document.all){
    el=el.createRange().text;
    }else{
	    el=FCK.EditorWindow;
    	el=el.getSelection().toString();
    }
    FCKSentenceFormats.Add(el)
}

// Register the related command

//--------------------***************************************--


// Add a new placeholder at the actual selection.
FCKSentenceFormats.Add = function( name )
{
	var oSpan = FCK.InsertElement( 'span' ) ;
	this.SetupSpan( oSpan, name ) ;
	if(typeof(parent.fck_insertbox_fun)=='function'){
	    	var v = FCK.EditorDocument.body.innerHTML ;
	    	parent.fck_insertbox_fun(v,FCK.EditorDocument.body,FCK.Name);
	    }
}

FCKSentenceFormats.SetupSpan = function( span, name )
{
	
	//span.innerHTML = '[[ ' + name + ' ]]' ;
	span.innerHTML = '<span class="hot">[</span>' + name + '<span class="hot">]</span>' ;

	//span.style.backgroundColor = '#ffff00' ;
	//span.style.color = '#000000' ;
	span.className='tb4'
	if ( FCKBrowserInfo.IsGecko )
		span.style.cursor = 'default' ;

	span._fckplaceholder = name ;
	span.contentEditable = false ;

	// To avoid it to be resized.
	span.onresizestart = function()
	{
		FCK.EditorWindow.event.returnValue = false ;
		return false ;
	}
}

// On Gecko we must do this trick so the user select all the SPAN when clicking on it.
FCKSentenceFormats._SetupClickListener = function()
{
	FCKSentenceFormats._ClickListener = function( e )
	{
		if ( e.target.tagName == 'SPAN' && e.target._fckplaceholder ){
			FCKSelection.SelectNode( e.target ) ;
		}
	}

	FCK.EditorDocument.addEventListener( 'click', FCKSentenceFormats._ClickListener, true ) ;
}

// Open the Placeholder dialog on double click.
FCKSentenceFormats.OnDoubleClick = function( span )
{
	if ( span.tagName == 'SPAN' && span.className=='tb4' ){
		//FCK.SetHTML('7987979879');
		if(FCKBrowserInfo.IsIE){
		var oldTxt=span.innerText.replace('[','').replace(']','');
		}else{
		var oldTxt=span.textContent.replace('[','').replace(']','');
		}
		var oSpan = FCK.InsertElement( 'span' ) ;
		oSpan.innerHTML=oldTxt;
		try{
		span.textContent='';
		}catch(e){}
		try{
		span.innerText='';
		}catch(e){}
		span.className='';
		span.removeAttribute('contenteditable');
		FCK.InsertElement(oSpan);
		if(typeof(parent.fck_insertbox_fun)=='function'){
	    	var v = FCK.EditorDocument.body.innerHTML ;
	    	parent.fck_insertbox_fun(v,FCK.EditorDocument.body,FCK.Name);
	    }
	}
	//	FCKCommands.GetCommand( 'SentenceFormat' ).Execute() ;
}

FCK.RegisterDoubleClickHandler( FCKSentenceFormats.OnDoubleClick, 'SPAN' ) ;

// Check if a Placholder name is already in use.
FCKSentenceFormats.Exist = function( name )
{
	var aSpans = FCK.EditorDocument.getElementsByTagName( 'SPAN' ) ;

	for ( var i = 0 ; i < aSpans.length ; i++ )
	{
		if ( aSpans[i]._fckplaceholder == name )
			return true ;
	}

	return false ;
}

if ( FCKBrowserInfo.IsIE )
{
	FCKSentenceFormats.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;
		if(FCK.ToolbarSet.Name!='SentenceFormat'){
			return ;
		}
		var aPlaholders = FCK.EditorDocument.body.innerText.match( /\[\[[^\[\]]+\]\]/g ) ;
		if ( !aPlaholders )
			return ;

		var oRange = FCK.EditorDocument.body.createTextRange() ;

		for ( var i = 0 ; i < aPlaholders.length ; i++ )
		{
			if ( oRange.findText( aPlaholders[i] ) )
			{
				var sName = aPlaholders[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;
				//oRange.pasteHTML( '<span style="color: #000000; background-color: #ffff00" contenteditable="false" _fckplaceholder="' + sName + '">' + aPlaholders[i] + '</span>' ) ;
				var v=aPlaholders[i].replace('[','').replace(']','');
				v='<span class="hot">[</span>' + v + '<span class="hot">]</span>'
				oRange.pasteHTML( '<span class="tb4" contenteditable="false" _fckplaceholder="' + sName + '">' + v + '</span>' ) ;
			}
		}
	}
}
else
{
	FCKSentenceFormats.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;
		if(FCK.ToolbarSet.Name!='SentenceFormat'){
			return ;
		}
		var oInteractor = FCK.EditorDocument.createTreeWalker( FCK.EditorDocument.body, NodeFilter.SHOW_TEXT, FCKSentenceFormats._AcceptNode, true ) ;

		var	aNodes = new Array() ;

		while ( ( oNode = oInteractor.nextNode() ) )
		{
			aNodes[ aNodes.length ] = oNode ;
		}

		for ( var n = 0 ; n < aNodes.length ; n++ )
		{
			var aPieces = aNodes[n].nodeValue.split( /(\[\[[^\[]*\]\])/g ) ;

			for ( var i = 0 ; i < aPieces.length ; i++ )
			{
				if ( aPieces[i].length > 0 )
				{
					if ( aPieces[i].indexOf( '[' ) == 0 )
					{
						var sName = aPieces[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;
						var oSpan = FCK.EditorDocument.createElement( 'span' ) ;
						FCKSentenceFormats.SetupSpan( oSpan, sName ) ;
						aNodes[n].parentNode.insertBefore( oSpan, aNodes[n] ) ;
					}
					else
						aNodes[n].parentNode.insertBefore( FCK.EditorDocument.createTextNode( aPieces[i] ) , aNodes[n] ) ;
				}
			}

			aNodes[n].parentNode.removeChild( aNodes[n] ) ;
		}

		FCKSentenceFormats._SetupClickListener() ;
	}

	FCKSentenceFormats._AcceptNode = function( node )
	{
		if ( /\[[^\[]+\]/.test( node.nodeValue ) )
			return NodeFilter.FILTER_ACCEPT ;
		else
			return NodeFilter.FILTER_SKIP ;
	}
}

FCK.Events.AttachEvent( 'OnAfterSetHTML', FCKSentenceFormats.Redraw ) ;

// We must process the SPAN tags to replace then with the real resulting value of the placeholder.
/* 这段是处理文本显示的,现在不需要
FCKXHtml.TagProcessors['span'] = function( node, htmlNode )
{
	if ( htmlNode._fckplaceholder )
		node = FCKXHtml.XML.createTextNode( '[[' + htmlNode._fckplaceholder + ']]' ) ;
	else
		FCKXHtml._AppendChildNodes( node, htmlNode, false ) ;

	return node ;
}
*/
//FCKCommands.RegisterCommand('BoxFormat', new FCKBoxFormats());
FCKCommands.RegisterCommand('SentenceFormat', new FCKSentenceFormats());