/*
 * Media Plugin for FCKeditor 2.5 SVN
 * Copyright (C) 2007 Riceball LEE (riceballl@hotmail.com)
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
 * Scripts related to the Media dialog window (see fck_media.html).
 */

function _Import(aSrc) {
   document.write('<scr'+'ipt type=\"text/javascript\" src=\"' + aSrc + '\"></sc' + 'ript>');
};

var oEditor   = window.parent.InnerDialogLoaded() ;
var FCK     = oEditor.FCK ;
var FCKLang   = oEditor.FCKLang ;
var FCKConfig = oEditor.FCKConfig ;

_Import(FCK.Plugins.Items['Media'].Path + 'js/fck_media_inc.js');

//#### Dialog Tabs

// Set the dialog tabs.
window.parent.AddTab( 'Info', oEditor.FCKLang.DlgInfoTab ) ;

if ( FCKConfig.FlashUpload )
  window.parent.AddTab( 'Upload', FCKLang.DlgLnkUpload ) ;

//if ( !FCKConfig.FlashDlgHideAdvanced )
// window.parent.AddTab( 'Advanced', oEditor.FCKLang.DlgAdvancedTag ) ;

// Function called when a dialog tag is selected.
function OnDialogTabChange( tabCode )
{
  ShowE('divInfo'   , ( tabCode == 'Info' ) ) ;
  ShowE('divUpload' , ( tabCode == 'Upload' ) ) ;
  ShowE('divAdvanced' , ( tabCode == 'Advanced' ) ) ;
}

// Get the selected media embed (if available).
var oFakeImage = FCK.Selection.GetSelectedElement() ;
var oEmbed ;

if ( oFakeImage )
{
  if ( oFakeImage.tagName == 'IMG' && oFakeImage.getAttribute('_fckmedia') )
    oEmbed = FCK.GetRealElement( oFakeImage ) ;
  else
    oFakeImage = null ;
}

function MeidaPlayerTypeChanged(aTypeId) {
  MakeHtmlMediaTypeParams('divMediaParams', oFCKMediaPlayers[aTypeId]);
}

window.onload = function()
{
  var vElement = document.getElementById('cmbMeidaPlayerType');
  //vElement.options.length = 0;

  with (vElement) {
    for (var i = 0; i < oFCKMediaPlayers.length; i++) {
      options[i] = new Option(FCKLang['DlgMediaType_'+oFCKMediaPlayers[i].id], oFCKMediaPlayers[i].id);
    }
    //selectedIndex = 0;
    options[0].checked = true;
  }
  MeidaPlayerTypeChanged(0);

  // Translate the dialog box texts.
  oEditor.FCKLanguageManager.TranslatePage(document) ;

  // Load the selected element information (if any).
  LoadSelection() ;

  // Show/Hide the "Browse Server" button.
  GetE('tdBrowse').style.display = FCKConfig.MediaBrowser ? '' : 'none' ;

  // Set the actual uploader URL.
  if ( FCKConfig.MediaUpload )
    GetE('frmUpload').action = FCKConfig.MediaUploadURL ;

  window.parent.SetAutoSize( true ) ;

  // Activate the "OK" button.
  window.parent.SetOkButton( true ) ;
}

function LoadSelection()
{
  if ( ! oEmbed ) return ;

  var vMediaPlayer = GetMediaPlayerObject(GetAttribute( oEmbed, 'type', ''));
  GetE('cmbMeidaPlayerType').value = vMediaPlayer.id;
  //oEmbed = oEmbed.getElementById('fckMedia');
  GetE('txtUrl').value    = GetAttribute( oEmbed, 'src', '' ) ;
  GetE('txtWidth').value  = GetAttribute( oEmbed, 'width', '' ) ;
  GetE('txtHeight').value = GetAttribute( oEmbed, 'height', '' ) ;

  // Get Advances Attributes
  MakeHtmlMediaTypeParams('divMediaParams', vMediaPlayer);
  for (var vParamName in vMediaPlayer.Params) {
    var vE = GetE(cFCKMediaParamPrefix+vParamName);
    var vParam = vMediaPlayer.Params[vParamName];
    switch (typeof(vParam)) {
      case 'string':
        vE.value    = GetAttribute( oEmbed, vParamName, vParam) ;
        break;
      case 'boolean':
        vE.checked  = GetAttribute( oEmbed, vParamName, vParam)  == 'true';
        break;
      case 'object':
        if (vParam.constructor == Array) {
          var vParamValue = GetAttribute( oEmbed, vParamName, vParam[0]);
          with(vE) for (var i = 0; i < options.length; i++) {
            options[i].selected = (options[i].value == vParamValue);
          }
        }
        break;
    } //switch end
  }//for end

  GetE('txtAttId').value    = oEmbed.id ;
  GetE('txtAttTitle').value   = oEmbed.title ;

  if ( oEditor.FCKBrowserInfo.IsIE )
  {
    GetE('txtAttClasses').value = oEmbed.getAttribute('className') || '' ;
    GetE('txtAttStyle').value = oEmbed.style.cssText ;
  }
  else
  {
    GetE('txtAttClasses').value = oEmbed.getAttribute('class',2) || '' ;
    GetE('txtAttStyle').value = oEmbed.getAttribute('style',2) || '' ;
  }

  UpdatePreview() ;
}

//#### The OK button was hit.
function Ok()
{
  if ( GetE('txtUrl').value.length == 0 )
  {
    window.parent.SetSelectedTab( 'Info' ) ;
    GetE('txtUrl').focus() ;

    alert( oEditor.FCKLang.DlgAlertUrl ) ;

    return false ;
  }

  oEditor.FCKUndo.SaveUndoStep() ;
  if ( !oEmbed )
  {
    oEmbed    = FCK.EditorDocument.createElement( cFckMediaElementName ) ;
    oFakeImage  = null ;
  }
  UpdateEmbed( oEmbed ) ;
  if ( !oFakeImage )
  {
   // oFakeImage  = oEditor.FCKDocumentProcessor_CreateFakeImage( 'FCK__Media_'+oEmbed.attributes[cMediaTypeAttrName].value, oEmbed ) ;
   var vMediaPlayerTypeId = parseInt(GetE('cmbMeidaPlayerType' ).value);
   vMediaPlayerTypeId = (vMediaPlayerTypeId==NaN) ? cDefaultMediaPlayer : vMediaPlayerTypeId;
	  if(vMediaPlayerTypeId==0){
	    oFakeImage  = document.createElement("span");
	    oFakeImage.className = 'jpy1';
	    oFakeImage.innerHTML="&nbsp;";
	  }else if(vMediaPlayerTypeId==1){
	    oFakeImage  = document.createElement("div");
	    oFakeImage.className = 'jpy2';
	    oFakeImage.innerHTML="&nbsp;";
	  }else if(vMediaPlayerTypeId==2){
	    oFakeImage  = document.createElement("div");
	    oFakeImage.className = 'jpy3';
	    oFakeImage.innerHTML="&nbsp;";
	  }else{
	  	oFakeImage  = document.createElement("a");
	    oFakeImage.className = 'jpy#';
	    var v=GetE('txtUrl').value;
	    oFakeImage.innerHTML=v.substr(v.lastIndexOf("/")+1);
	  }
    oFakeImage.setAttribute( 'src',  GetE('txtUrl').value ) ;
    oFakeImage.setAttribute( '_fckmedia', 'true', 0 ) ;
    //oFakeImage.contentEditable = false ;
    oFakeImage  = FCK.InsertElement( oFakeImage ) ;
    
     oP  = document.createElement("span");
	 oP.innerHTML="&nbsp;";
	 FCK.InsertElement( oP ) ;
  }else {
  //  oFakeImage.className = 'FCK__Media_'+oEmbed.attributes[cMediaTypeAttrName].value;
    oFakeImage.className = 'jpy1';
  }

  oEditor.FCKMediaProcessor.RefreshView( oFakeImage, oEmbed ) ;
  return true ;
}

function UpdateEmbed( e )
{
  var vMediaPlayerTypeId = parseInt(GetE('cmbMeidaPlayerType' ).value);
  var vParam;

  vMediaPlayerTypeId = (vMediaPlayerTypeId==NaN) ? cDefaultMediaPlayer : vMediaPlayerTypeId;
  SetAttribute( e, cMediaTypeAttrName     , vMediaPlayerTypeId );

  //SetAttribute( e, 'type'     , cMediaPlayerTypes[vMediaPlayerTypeId] );
  //SetAttribute( e, 'pluginspage'  , cMediaPlayerCodebase[vMediaPlayerTypeId] ) ;

  SetAttribute( e, 'src', GetE('txtUrl').value ) ;
  //SetAttribute( e, "width" , '400') ;
  //SetAttribute( e, "height", '50' ) ;

  // Advances Attributes
  var vMediaPlayer = oFCKMediaPlayers[vMediaPlayerTypeId];
  for (var vParamName in vMediaPlayer.Params) {
    var vE = GetE(cFCKMediaParamPrefix+vParamName);
    var vParam = vMediaPlayer.Params[vParamName];
    switch (typeof(vParam)) {
      case 'string':
        SetAttribute( e, vParamName, vE.value ) ;
        break;
      case 'boolean':
        SetAttribute( e, vParamName, vE.checked ? 'true' : 'false' ) ;
        break;
      case 'object':
        if (vParam.constructor == Array) {
          SetAttribute( e, vParamName, vE.options[vE.selectedIndex].value ) ;
        }
        break;
    } //switch end
  }//for end

  SetAttribute( e, 'id' , GetE('txtAttId').value ) ;
  SetAttribute( e, 'title'  , GetE('txtAttTitle').value ) ;
_setCustomAttr(e,vMediaPlayerTypeId);
/*
  if ( oEditor.FCKBrowserInfo.IsIE )
  {
    SetAttribute( e, 'className', GetE('txtAttClasses').value ) ;
    e.style.cssText = GetE('txtAttStyle').value ;
  }
  else
  {
    SetAttribute( e, 'class', GetE('txtAttClasses').value ) ;
    SetAttribute( e, 'style', GetE('txtAttStyle').value ) ;
  }
  */
}

var ePreview ;

function SetPreviewElement( previewEl )
{
  ePreview = previewEl ;

  if ( GetE('txtUrl').value.length > 0 )
    UpdatePreview() ;
}

function UpdatePreview()
{
  if ( !ePreview )
    return ;
  while ( ePreview.firstChild )
    ePreview.removeChild( ePreview.firstChild ) ;

  if ( GetE('txtUrl').value.length == 0 )
    ePreview.innerHTML = '&nbsp;' ;
  else
  {
    var oDoc  = ePreview.ownerDocument || ePreview.document ;
  //  var e   = oDoc.createElement( 'EMBED' ) ;
    var vMediaPlayerTypeId = parseInt(GetE('cmbMeidaPlayerType').value);
    vMediaPlayerTypeId = (vMediaPlayerTypeId==NaN) ? cDefaultMediaPlayer : vMediaPlayerTypeId;
  if(vMediaPlayerTypeId==0){
    var e   = oDoc.createElement( 'SPAN' ) ;
    }else if(vMediaPlayerTypeId==1||vMediaPlayerTypeId==2){
    var e   = oDoc.createElement( 'DIV' ) ;
    }else{
    var e   = oDoc.createElement( 'A' ) ;
    }

    SetAttribute( e, 'src', GetE('txtUrl').value ) ;
    SetAttribute( e, cMediaTypeAttrName, vMediaPlayerTypeId);
    //SetAttribute( e, 'type', cMediaPlayerTypes[vMediaPlayerTypeId] ) ;
   // SetAttribute( e, 'width', '100%' ) ;
   // SetAttribute( e, 'height', '100%' ) ;
	_setCustomAttr(e,vMediaPlayerTypeId);
    ePreview.appendChild( e ) ;

    //e.innerHTML = WrapObjectToMedia(e.innerHTML, 'embed') ; //IE can not support!
    ePreview.innerHTML = WrapObjectToMedia(ePreview.innerHTML, 'embed'); 

  }
}
function _setCustomAttr(e,typeId){
	if(typeId==0){
		if ( oEditor.FCKBrowserInfo.IsIE ){
			SetAttribute( e, 'className', 'jpy1' ) ;
		}else{
			SetAttribute( e, 'class', 'jpy1' ) ;
		}
	}else if(typeId==1){
		if ( oEditor.FCKBrowserInfo.IsIE ){
			SetAttribute( e, 'className', 'jpy2' ) ;
		}else{
			SetAttribute( e, 'class', 'jpy2' ) ;
		}
	}else if(typeId==2){
		if ( oEditor.FCKBrowserInfo.IsIE ){
			SetAttribute( e, 'className', 'jpy3' ) ;
		}else{
			SetAttribute( e, 'class', 'jpy3' ) ;
		}
	}else{
		if ( oEditor.FCKBrowserInfo.IsIE ){
			SetAttribute( e, 'className', 'jpy#' ) ;
		}else{
			SetAttribute( e, 'class', 'jpy#' ) ;
		}
		var v=GetE('txtUrl').value;
	    e.innerHTML=v.substr(v.lastIndexOf("/")+1);
	}
}
// <embed id="ePreview" src="fck_flash/claims.swf" width="100%" height="100%" style="visibility:hidden" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer">

function BrowseServer()
{
  OpenFileBrowser( FCKConfig.MediaBrowserURL, FCKConfig.MediaBrowserWindowWidth, FCKConfig.MediaBrowserWindowHeight ) ;
}

function SetUrl( url, width, height )
{
  GetE('txtUrl').value = url ;

  if ( width )
    GetE('txtWidth').value = width ;

  if ( height )
    GetE('txtHeight').value = height ;

  UpdatePreview();
  //alert(window.frames[0]);
  //alert(window.frames[1]);
  
  _dealWithPart3Method();
  
  window.parent.SetSelectedTab( 'Info' ) ;
}
function  _dealWithPart3Method(){
	var vMediaPlayerTypeId = parseInt(GetE('cmbMeidaPlayerType').value);
	vMediaPlayerTypeId = (vMediaPlayerTypeId==NaN) ? cDefaultMediaPlayer : vMediaPlayerTypeId;
	if(vMediaPlayerTypeId==1){
		if(typeof(window.frames[0].jpy2)=='function'){
			var aa=window.frames[0].document.getElementsByTagName('div');
			for(i=0;i<aa.length;i++){
				if(aa[i].className=='jpy2'){
		  			window.frames[0].jpy2.call(aa[i]);
				}
			}
		}
	}else if(vMediaPlayerTypeId==2){
		if(typeof(window.frames[0].jpy3)=='function'){
	  		var aa=window.frames[0].document.getElementsByTagName('div');
			for(i=0;i<aa.length;i++){
				if(aa[i].className=='jpy3'){
		  			window.frames[0].jpy3.call(aa[i]);
				}
			}
		}
  	}else if(vMediaPlayerTypeId==0){
  		if(typeof(window.frames[0].myplay)=='function'){
		  			//window.frames[0].myplay();
		}
  	}
}

function OnUploadCompleted( errorNumber, fileUrl, fileName, customMsg )
{
  switch ( errorNumber )
  {
    case 0 :  // No errors
      alert( 'Your file has been successfully uploaded' ) ;
      break ;
    case 1 :  // Custom error
      alert( customMsg ) ;
      return ;
    case 101 :  // Custom warning
      alert( customMsg ) ;
      break ;
    case 201 :
      alert( 'A file with the same name is already available. The uploaded file has been renamed to "' + fileName + '"' ) ;
      break ;
    case 202 :
      alert( '无效的类型' ) ;
      return ;
    case 203 :
      alert( "Security error. You probably don't have enough permissions to upload. Please check your server." ) ;
      return ;
    default :
      alert( 'Error on file upload. Error number: ' + errorNumber ) ;
      return ;
  }

  SetUrl( fileUrl ) ;
  GetE('frmUpload').reset() ;
}

var oUploadAllowedExtRegex  = new RegExp( FCKConfig.MediaUploadAllowedExtensions, 'i' ) ;
var oUploadDeniedExtRegex = new RegExp( FCKConfig.MediaUploadDeniedExtensions, 'i' ) ;

function CheckUpload()
{
  var sFile = GetE('txtUploadFile').value ;

  if ( sFile.length == 0 )
  {
    alert( 'Please select a file to upload' ) ;
    return false ;
  }

  if ( ( FCKConfig.MediaUploadAllowedExtensions.length > 0 && !oUploadAllowedExtRegex.test( sFile ) ) ||
    ( FCKConfig.MediaUploadDeniedExtensions.length > 0 && oUploadDeniedExtRegex.test( sFile ) ) )
  {
    OnUploadCompleted( 202 ) ;
    return false ;
  }

  return true ;
}

//<div id='divMediaParams'></div>
function MakeHtmlMediaTypeParams(aElement, aMediaPlayer) {
  if (typeof(aElement == 'string')) aElement = document.getElementById(aElement);
  //aElement.innerHTML = ''; 
  var vHtml = '<table cellSpacing="0" cellPadding="0" border="0">';
  var i = 0;
  for (var vParamName in aMediaPlayer.Params) {
    var vE = GetE(cFCKMediaParamPrefix+vParamName);
    var vParam = aMediaPlayer.Params[vParamName];
    if (i == 0)  vHtml += '<tr>';
    vHtml += '<td nowrap="1">'+ (FCKLang[cFCKMediaParamPrefix+vParamName]|| vParamName) +'</td><td>';
    switch (typeof(vParam)) {
      case 'string':
        vHtml += '<input type="text" id="' + cFCKMediaParamPrefix+vParamName + '" value="'+ vParam + '">' ;
        break;
      case 'boolean':
        vHtml += '<input type="checkbox" id="' + cFCKMediaParamPrefix+vParamName  + '" '+ (vParam?'checked="1"':'') + '>' ;
        break;
      case 'object':
        if (vParam.constructor == Array) {
          vHtml += '<select id="'+ cFCKMediaParamPrefix+vParamName+'"><option value="" selected></option>';
          for (var i = 0; i < vParam.length; i++) {
            vHtml += '<option value="'+ vParam[i]+'">'+(FCKLang[cFCKMediaParamPrefix+vParamName+'_'+vParam[i]] || vParam[i])+'</option>';
          }
          vHtml += '</select>';
        }
        break;
    } //switch end
    vHtml += '</td>';
    if (i == 1) {
      vHtml += '</tr>';
      i = 0;
    } else i++;
  }//for end
  vHtml += '</table>';
  aElement.innerHTML = vHtml;
}
