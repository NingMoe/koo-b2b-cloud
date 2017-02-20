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
 * Plugin to insert "SubHelps" in the editor.
 */

// Register the related command.
FCKCommands.RegisterCommand( 'SubHelp', new FCKDialogCommand( 'SubHelp', "添加二级帮助", FCKPlugins.Items['subhelp'].Path + 'fck_subhelp.html', 500, 400 ) ) ;

// Create the "Plaholder" toolbar button.
var oSubHelpItem = new FCKToolbarButton( 'SubHelp', "添加二级帮助", "添加二级帮助" ) ;
oSubHelpItem.IconPath = FCKPlugins.Items['subhelp'].Path + 'subhelp.png' ;

FCKToolbarItems.RegisterItem( 'SubHelp', oSubHelpItem ) ;


// The object used for all SubHelp operations.
var FCKSubHelps = new Object() ;

// Add a new placeholder at the actual selection.
FCKSubHelps.AddTitle = function( id,name )
{
	this.addDelDiv("help_title_",id);
	var oSpan = FCK.InsertElement( 'h2' ) ;
	oSpan.className="sub_help";
	oSpan.id="help_title_"+id;
	var a = FCK.InsertElement( 'a' ) ;
	a.href="#help_content_"+id;
	a.innerHTML = name;
	oSpan.appendChild(a);
	this.SetupSpan( oSpan) ;
	
}
FCKSubHelps.AddContent = function( id,name )
{
	this.addDelDiv("help_content_",id);
	var oSpan = FCK.InsertElement( 'div' ) ;
	oSpan.className="sub_help";
	oSpan.id="help_content_"+id;
	oSpan.innerHTML = name;
	this.SetupSpan( oSpan) ;
	
}
FCKSubHelps.addDelDiv=function(aidName,id){
	var delDiv = FCK.InsertElement( 'div' ) ;
	delDiv.className="del_sub_help";
	delDiv.setAttribute("aid",aidName+id);
	if(aidName == "help_content_"){
		delDiv.title="双击删除帮助内容";
	}else{
		delDiv.title="双击删除帮助标题";
	}
	delDiv.innerHTML="";
}
FCKSubHelps.SetupSpan = function( span)
{
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
	FCK.InsertElement( 'p' ) ;
}

// On Gecko we must do this trick so the user select all the SPAN when clicking on it.
FCKSubHelps._SetupClickListener = function()
{
	FCKSubHelps._ClickListener = function( e )
	{
		if ( e.target.tagName == 'div' && e.target.className=="sub_help" )
			FCKSelection.SelectNode( e.target ) ;
	}

	FCK.EditorDocument.addEventListener( 'click', FCKSubHelps._ClickListener, true ) ;
}

// Open the SubHelp dialog on double click.
FCKSubHelps.OnDoubleClick = function( e )
{
	if ( e.tagName == 'DIV' && e.className=="del_sub_help"){
		var id = e.getAttribute("aid");
		var s = FCK.EditorDocument.getElementById(id);
		s.innerHTML="";
		s.parentNode.removeChild(s);
		e.parentNode.removeChild(e);
	}
}

FCK.RegisterDoubleClickHandler( FCKSubHelps.OnDoubleClick, 'DIV' ) ;

// Check if a Placholder name is already in use.
FCKSubHelps.Exist = function( name )
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
	FCKSubHelps.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;

		var aPlaholders = FCK.EditorDocument.body.innerText.match( /\[\[[^\[\]]+\]\]/g ) ;
		if ( !aPlaholders )
			return ;

		var oRange = FCK.EditorDocument.body.createTextRange() ;

		for ( var i = 0 ; i < aPlaholders.length ; i++ )
		{
			if ( oRange.findText( aPlaholders[i] ) )
			{
				var sName = aPlaholders[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;
				oRange.pasteHTML( '<span style="color: #000000; background-color: #ffff00" contenteditable="false" _fckplaceholder="' + sName + '">' + aPlaholders[i] + '</span>' ) ;
			}
		}
	}
}
else
{
	FCKSubHelps.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;

		var oInteractor = FCK.EditorDocument.createTreeWalker( FCK.EditorDocument.body, NodeFilter.SHOW_TEXT, FCKSubHelps._AcceptNode, true ) ;

		var	aNodes = new Array() ;

		while ( ( oNode = oInteractor.nextNode() ) )
		{
			aNodes[ aNodes.length ] = oNode ;
		}

		for ( var n = 0 ; n < aNodes.length ; n++ )
		{
			var aPieces = aNodes[n].nodeValue.split( /(\[\[[^\[\]]+\]\])/g ) ;

			for ( var i = 0 ; i < aPieces.length ; i++ )
			{
				if ( aPieces[i].length > 0 )
				{
					if ( aPieces[i].indexOf( '[[' ) == 0 )
					{
						var sName = aPieces[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;

						var oSpan = FCK.EditorDocument.createElement( 'span' ) ;
						FCKSubHelps.SetupSpan( oSpan, sName ) ;

						aNodes[n].parentNode.insertBefore( oSpan, aNodes[n] ) ;
					}
					else
						aNodes[n].parentNode.insertBefore( FCK.EditorDocument.createTextNode( aPieces[i] ) , aNodes[n] ) ;
				}
			}

			aNodes[n].parentNode.removeChild( aNodes[n] ) ;
		}

		FCKSubHelps._SetupClickListener() ;
	}

	FCKSubHelps._AcceptNode = function( node )
	{
		if ( /\[\[[^\[\]]+\]\]/.test( node.nodeValue ) )
			return NodeFilter.FILTER_ACCEPT ;
		else
			return NodeFilter.FILTER_SKIP ;
	}
}

FCK.Events.AttachEvent( 'OnAfterSetHTML', FCKSubHelps.Redraw ) ;

// We must process the SPAN tags to replace then with the real resulting value of the placeholder.
FCKXHtml.TagProcessors['span'] = function( node, htmlNode )
{
	if ( htmlNode._fckplaceholder )
		node = FCKXHtml.XML.createTextNode( '[[' + htmlNode._fckplaceholder + ']]' ) ;
	else
		FCKXHtml._AppendChildNodes( node, htmlNode, false ) ;

	return node ;
}
