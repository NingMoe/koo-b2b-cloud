 function SetCwinHeight(iframeObj){ 
  if (document.getElementById){ 
   if (iframeObj && !window.opera){ 
    if (iframeObj.contentDocument && iframeObj.contentDocument.body.offsetHeight){ 
     iframeObj.height = iframeObj.contentDocument.body.offsetHeight; 
    }else if(document.frames[iframeObj.name].document && document.frames[iframeObj.name].document.body.scrollHeight){ 
     iframeObj.height = document.frames[iframeObj.name].document.body.scrollHeight; 
    } 
   } 
  } 
 } 