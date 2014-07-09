/********************************************************* begin of preamble
**
** Copyright (C) 2003-2008 Software- und Organisations-Service GmbH. 
** All rights reserved.
**
** This file may be used under the terms of either the 
**
**   GNU General Public License version 2.0 (GPL)
**
**   as published by the Free Software Foundation
**   http://www.gnu.org/licenses/gpl-2.0.txt and appearing in the file
**   LICENSE.GPL included in the packaging of this file. 
**
** or the
**  
**   Agreement for Purchase and Licensing
**
**   as offered by Software- und Organisations-Service GmbH
**   in the respective terms of supply that ship with this file.
**
** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
** IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
** THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
** PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
** BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
** CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
** SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
** INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
** CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
** ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
** POSSIBILITY OF SUCH DAMAGE.
********************************************************** end of preamble*/

var SOS_Div = function(){};
/** @access public */
SOS_Div.imgDir                      = 'js/site/images/';
SOS_Div.styleBorderActiv            = 'border:solid 1px #0555eb';
SOS_Div.styleBorderUnActiv          = 'border:solid 1px #81a7e8';
SOS_Div.opacityUnactiv              = 65;
SOS_Div.opacityPageContentDisabled  = 35;
/** @access private */
SOS_Div.id                          = null;
SOS_Div.zIndex                      = 1;
SOS_Div.disabledAreaId              = 'sos_div_disabled_area';
SOS_Div.allListboxProperties        = new Array();
SOS_Div.activeId                    = '';
SOS_Div.isPageDisabled              = false;
SOS_Div.onMove              				= '';//siehe SOS_Div.setOnMove
SOS_Div.arrOnMove										= new Array();
/**
* Soll bei window.onload() aufgerufen werden 
*/
SOS_Div.moveInit = function(){
  document.onmousedown  = SOS_Div.moveStart;
  document.onmouseup    = SOS_Div.moveEnd;
}

/**
* per Mausklick DIV auf aktiven Zustand setzen
*/
SOS_Div.makeActiv = function(division){
  try{
    if(SOS_Div.isPageDisabled) return;
    
    if(!division || division == null) { return; } 
    if(typeof division == 'string')   { division    = document.getElementById(division); } 
    if(SOS_Div.id == null)            { SOS_Div.id  = division.id;}
    
    SOS_Div.activeId  = division.id;
    
    if(division.disabled == true){ SOS_Div.setIframe(division.id); return;}
    
    if(!division.getAttribute("sos_move_div")){
    	division.setAttribute("sos_move_div","yes");	
    }
    
    //window.status = division.id+' = '+SOS_Div.id;
    
    /*
   	if((division.id == SOS_Div.id) && (1*division.style.zIndex == 1*SOS_Div.zIndex)){  
      SOS_Div.setIframe(division.id);
      return;      
    }
    */
    var id        = division.id;
    var elements  = document.getElementsByTagName('div');
    
    // gehen alle divs durch
    var maxZ = 0;
    for( var i = 0; i < elements.length; i++ ){
      var element    	= elements[i];
      var elementId  	= element.getAttribute('id');
      var elementZI		= parseInt(element.style.zIndex);
      
      if(elementZI > maxZ){
        maxZ = elementZI;   
      }
      
      //window.status = element.getAttribute('sos_move_div');
    
      
      if(element.getAttribute('sos_move_div') != null && elementId != null && elementId.length > 0){
        if(document.getElementById(elementId+'_iframe') != null){
          document.getElementById(elementId+'_iframe').style.zIndex = elementZI-1;
        }
    
        var filter      = '';
        var borderStyle = SOS_Div.styleBorderUnActiv;
        
        if(elementId == id) { borderStyle = SOS_Div.styleBorderActiv;                   }
        else                { filter      = 'alpha(opacity='+SOS_Div.opacityUnactiv+')';}
        
        // jetzt Titelleisten ändern
        // voraussetzung td haben id =   div_id:irgendwas
        var tds = element.getElementsByTagName('td');
        
        if(tds != null){
        	
          for(var j=0;j < tds.length;j++){
            var td    = tds[j];
            var tdId  = td.getAttribute('id');
            if(tdId != null && tdId.search(elementId+':') != -1){
              if(document.all){ //IE
                td.style.filter = filter;
              }
              else{
                if(filter.length > 0){
                  td.style["-moz-opacity"]  = '0.'+SOS_Div.opacityUnactiv;
                  td.style["opacity"]       = '0.'+SOS_Div.opacityUnactiv;
                }
                else{
                  td.style["-moz-opacity"]  = '';
                  td.style["opacity"]       = '';
                }
              }
            }
          }
        }
      }
    }// for divs
    //if(maxZ != division.style.zIndex){
      maxZ+=2; 
    //}
    SOS_Div.zIndex = maxZ;
    document.getElementById(id).style.zIndex  = SOS_Div.zIndex;           
    SOS_Div.setIframe(id);
  }
  catch(x){ 
    throw new SOS_Exception(x,x.message, "",0);
  }
}

/**
* DIV schliessen
*/
SOS_Div.close = function(div_id){
  try{
  	
    var id = (typeof div_id == 'object' && div_id.id) ? div_id.id : div_id;
    if(document.getElementById(id) != null){
      document.getElementById(id).style.display = 'none';
    }
    if(document.getElementById(id+'_iframe') != null){
      document.getElementById(id+'_iframe').style.display = 'none'; 
    }
  }
catch(x){}
}

/**
* Anzahl 
*/
SOS_Div.getCountDocuments = function(){
  var countDocuments = (parent.frames.length == 0) ? 1 : parent.frames.length; 
  for (var i = 0; i < parent.frames.length; i++){
    if(typeof parent.frames[i] == 'undefined' || parent.frames[i].name == ''){
      countDocuments -=1;
    }
  }
//return if(countDocuments <= 0) countDocuments = 1; 
return (countDocuments <= 0) ? 1 : countDocuments;
}

/**
* Seiteninhalt|Frameset auf inaktiv setzen - keine Aktionen ausser im aktiven DIV sind dann möglich 
*/
SOS_Div.makePageContentDisabled = function(windowObj){
  try{   
    var countDocuments = ( typeof windowObj != 'object' ) ? SOS_Div.getCountDocuments() : 1;
    
    if(countDocuments > 1) SOS_Div.zIndex = 10000;
    
    if(SOS_Div.activeId.length > 0){
      var activDiv          = document.getElementById(SOS_Div.activeId);
      activDiv.setAttribute("original_zindex",activDiv.style.zIndex);
      activDiv.style.zIndex = SOS_Div.zIndex;
      //SOS_Div.id = SOS_Div.activeId;
    }
    
    for (var i = 0; i < countDocuments; i++){
      //var doc           = (countDocuments == 1) ? document : parent.frames[i].document; 
      var doc           = (countDocuments == 1) ? ((typeof windowObj != 'object') ? document : windowObj.document ) : parent.frames[i].document; 
      var disabledArea  = doc.getElementById(SOS_Div.disabledAreaId);        
      
      if(disabledArea == null){ // wenn exist-t, dann muss alles per hand gemacht werden : höhe, breite usw- nocht nicht impl-t
        // Kein Iframe, weil Iframe lasst die <select bei IE verschwinden
        disabledArea  = doc.createElement('p');
        if(document.all){//IE,Opera
          disabledArea.style.zIndex           = SOS_Div.zIndex-1;
          if(navigator.userAgent.toLowerCase().indexOf('opera') == -1){ // ? mal kucken wie es bei Opera 9 ist
            disabledArea.style.backgroundColor  = '#ffffff';
            disabledArea.style.filter           = 'alpha(opacity='+SOS_Div.opacityPageContentDisabled+')';
          }
          else{
            //disabledArea.style["-moz-opacity"]  = '0.'+SOS_Div.opacityPageContentDisabled;
            //disabledArea.style["opacity"]       = '0.'+SOS_Div.opacityPageContentDisabled;
          }
          disabledArea.style.position         = 'absolute';
          disabledArea.style.overflow         = 'visible';
          //disabledArea.style.width            = (document.all) ? '105%' : '100%';
          //disabledArea.style.height           = (doc.getElementsByTagName('body')[0].offsetHeight*1+20)+'px';
          disabledArea.style.left             = 0;
          disabledArea.style.top              = 0;
          disabledArea.style.margin           = 0;
        }
        else{// andere
          disabledArea.setAttribute('style','z-index:'+(SOS_Div.zIndex-1)+';background-color:#ffffff;-moz-opacity:0.'+SOS_Div.opacityPageContentDisabled+';opacity:0.'+SOS_Div.opacityPageContentDisabled+';position:absolute;overflow:visible;width:100%;height:100%;left:0;top:0;margin:0;');  
        }
        disabledArea.style.width  = doc.getElementsByTagName('body')[0].scrollWidth+'px'; 
        disabledArea.style.height = doc.getElementsByTagName('body')[0].scrollHeight+'px';
   
        disabledArea.id           = SOS_Div.disabledAreaId;
        disabledArea.style.cursor = 'text';
        
        doc.getElementsByTagName('body')[0].appendChild(disabledArea);
      }
      SOS_Div.setListbox(doc,true);
      
      disabledArea.style.zIndex   = SOS_Div.zIndex-1;
      disabledArea.style.display  = '';
      SOS_Div.isPageDisabled      = true; 
    }
  }
  catch(x){}
}

/**
* Seiteninhalt|Frameset auf aktiv setzen 
*/
SOS_Div.makePageContentEnabled = function(){
  try{
    var countDocuments = SOS_Div.getCountDocuments();
    for (var i = 0; i < countDocuments; i++){
      var doc           = (countDocuments == 1) ? document : parent.frames[i].document; 
      var disabledArea  = doc.getElementById(SOS_Div.disabledAreaId);
  
      if(disabledArea != null){
        disabledArea.style.display  = 'none';
      }
      SOS_Div.setListbox(doc,false);
      //doc.getElementsByTagName('body')[0].oncontextmenu = null;
    }
    SOS_Div.allListboxProperties = new Array();
    try{
      if(SOS_Div.activeId.length > 0){
        var activDiv  = document.getElementById(SOS_Div.activeId);
        if(activDiv.getAttribute("original_zindex") !=null){
          SOS_Div.zIndex        = 1*activDiv.getAttribute("original_zindex");
          activDiv.style.zIndex = SOS_Div.zIndex;
          SOS_Div.setIframe(SOS_Div.activeId)
        } 
      }
    }
    catch(x){}
  }
  catch(x){}
  finally{
    SOS_Div.isPageDisabled  = false;  
  }
}

/**
* Listbox(IE) auf einer Seite aktivieren|deaktivieren 
*/
SOS_Div.setListbox = function(doc,deactivate){
  try{
    if(document.all && navigator.userAgent.toLowerCase().indexOf('opera') == -1){
      var allListbox = doc.getElementsByTagName('select');
      
      if(deactivate == true){
        var activeDiv                   = doc.getElementById(SOS_Div.activeId);
        var activeDivListbox            = null;
        var activeDivListboxProperties  = null;
        
        if(activeDiv != null){
          // Listbox beim aktivin DIV lesen und die Eigenschaften speichern
          activeDivListbox            = activeDiv.getElementsByTagName('select');
          activeDivListboxProperties  = new Array(activeDivListbox.length);
        
          for(var i=0;i<activeDivListbox.length;i++){
            activeDivListboxProperties[i] = { disabled : activeDivListbox[i].disabled }
          }
        }
        // Alle Listbox deaktivieren. Eigenschaften vorher speichern
        for(var i=0;i< allListbox.length; i++){
          SOS_Div.allListboxProperties[i] = { disabled : allListbox[i].disabled }
          allListbox[i].disabled = true;  
        }
        // gespeicherte Eigenschaften des Listbox beim aktiven DIV wiederherstellen
        if(activeDivListbox != null){
          for(var i=0;i<activeDivListbox.length;i++){
            activeDivListbox[i].disabled = activeDivListboxProperties[i].disabled; 
          }
        }
      }
      else{
        if(SOS_Div.allListboxProperties.length > 0){
          for(var i=0;i< allListbox.length; i++){
            if(typeof SOS_Div.allListboxProperties[i] != 'undefined'){
              allListbox[i].disabled = SOS_Div.allListboxProperties[i].disabled;  
            }
          }
        }
      }
    }
  }
  catch(x){}
}

/**
*/
SOS_Div.resizeDisabled = function(){
  if(SOS_Div.isPageDisabled){
    var disabledArea  = document.getElementById(SOS_Div.disabledAreaId);  
    if(disabledArea){
      disabledArea.style.width  = document.getElementsByTagName('body')[0].scrollWidth+'px'; 
      disabledArea.style.height = document.getElementsByTagName('body')[0].scrollHeight+'px';
    }
  } 
}

/**
* Verschiebung starten : divs und images (es können im Prinzip auch andere Elemente sein)
*/
SOS_Div.moveStart = function(e){
  SOS_Div.id    = null;
  var el        = (document.all) ? event.srcElement : e.target;
  var isResize  = false;
  var isNew     = true;
  
  try{
    if(typeof el.id == 'undefined' || el.id == null || el.id.length == 0){
      return null;  
    }
    switch(el.tagName.toLowerCase()){
      case 'img'  :
      case 'div'  :
                      if(el.getAttribute("sos_move_div") != null){  
                        //el.onscroll = new Function("SOS_Div.moveEnd();");
                        if(SOS_Div.activeId == el.id){
                          isNew = false;  
                        }
                        SOS_Div.id  = el.id;  
                      }
                      // !!!! kein BREAK
      default     :
                    var ids = el.id.split(':');
                    if(ids.length > 1){
                      var parentId  = ids[0]; 
                      var parentEl  = document.getElementById(parentId);
                      //window.status = parentEl;
                      if( typeof parentEl   != 'undefined' && 
                          parentEl          != null && 
                          parentEl.tagName.toLowerCase() == 'div'){
                        
                        if(SOS_Div.activeId  == parentId){
                          isNew = false;  
                        }
                        parentEl.setAttribute("sos_move_div","yes");
                        parentEl.onscroll = new Function("SOS_Div.moveEnd();");
                        
                        SOS_Div.id = parentId;
                        
                        if(ids[1].indexOf('resize') != -1){
                          isResize = true;  
                        }
                      }
                    }
                    break;   
    }
  }
  catch(x){}

  if(SOS_Div.id){
    var divEl = document.getElementById(SOS_Div.id);
    if(divEl.style.position == '' &&  (divEl.style.top == '' || divEl.style.left == '')){
      SOS_Div.id = null;
      return null;
    }
    if(!document.all){ //!IE
      event = e;
    }
    startX = event.clientX;
    startY = event.clientY;

    SOS_Div.activeId = SOS_Div.id;
    if(isNew){
      //SOS_Div.zIndex += 2;
      //document.getElementById(SOS_Div.id).style.zIndex = SOS_Div.zIndex;
    }
    XpositionOld          = document.getElementById(SOS_Div.id).style.left;
    YpositionOld          = document.getElementById(SOS_Div.id).style.top;
 
    if(isResize){ document.onmousemove  = SOS_Div.resize; }
    else        { document.onmousemove  = SOS_Div.move;   }
    SOS_Div.setIframe(SOS_Div.id);
  return false;
  }
  else{
    document.onmousemove  = null;
  }
}


/**
* Unter jedem DIV iframe erzeugen (nur bei IE)
* um die Anzeige von Listbox zu unterdrücken
*/
SOS_Div.setIframe = function(id,parentEl){
  if(document.all && navigator.userAgent.toLowerCase().indexOf('opera') == -1){
    var iframe = document.getElementById(id+'_iframe');
    if(iframe == null){
      iframe                = document.createElement('iframe');
      iframe.id             = id+'_iframe';
      iframe.style.position = 'absolute';
      iframe.style.filter   = 'alpha(opacity=0)';
      if(typeof parentEl == 'undefined') parentEl = document.getElementsByTagName('body')[0];
      parentEl.appendChild(iframe);
    }
    iframe.style.top      = parseInt(document.getElementById(id).offsetTop)+'px';
    iframe.style.left     = parseInt(document.getElementById(id).offsetLeft)+'px';  
    
    iframe.style.width    = parseInt(document.getElementById(id).offsetWidth)+'px';
    iframe.style.height   = parseInt(document.getElementById(id).offsetHeight)+'px';
    //iframe.style.offsetWidth  = document.getElementById(id).offsetWidth;
    //iframe.style.offsetHeight = document.getElementById(id).offsetHeight;
    iframe.style.zIndex   = document.getElementById(id).style.zIndex-1; 
    iframe.style.display  = ''; 
  }
}

/**
* Grösse dynamisch ändern
*/
SOS_Div.resize = function(e){
  if(SOS_Div.id){
    if(!document.all){ event = e; }
    try{
      var scrollLeft    = 0;
      var scrollTop     = 0;
      
      if(self.innerHeight){
        scrollLeft    = self.pageXOffset;
        scrollTop     = self.pageYOffset;
      }
      else if(document.documentElement && document.documentElement.clientHeight){ //IE 6
        scrollLeft    = document.documentElement.scrollLeft;
        scrollTop     = document.documentElement.scrollTop;
      }
      else if(document.body){
        scrollLeft    = document.body.scrollLeft;
        scrollTop     = document.body.scrollTop;
      }
      Xvalue = event.clientX-parseInt(XpositionOld)+scrollLeft;
      Yvalue = event.clientY-parseInt(YpositionOld)+scrollTop;
      
      document.getElementById(SOS_Div.id).style.width   = Xvalue+'px';
      document.getElementById(SOS_Div.id).style.height  = Yvalue+'px';
      SOS_Div.setIframe(SOS_Div.id);
    }
    catch(x){}
  }
return false;
}

/**
* Elemente verschieben
*/
SOS_Div.move = function(e){
  if(SOS_Div.id){
    if(!e){ e = window.event;}
    try{
      var div = document.getElementById(SOS_Div.id);
      if(div){
        var actX  = parseInt(div.style.left   || 0);
        var actY  = parseInt(div.style.top  || 0);
        Xvalue    = e.clientX-startX;
        Yvalue    = e.clientY-startY;
        //if(actX > 0){ div.style.left  = (parseInt(XpositionOld || 0)+parseInt(Xvalue || 0))+'px'; }
        //if(actY > 0){ div.style.top   = (parseInt(YpositionOld || 0)+parseInt(Yvalue || 0))+'px'; }
        div.style.left  = (parseInt(XpositionOld || 0)+parseInt(Xvalue || 0))+'px';
        div.style.top   = (parseInt(YpositionOld || 0)+parseInt(Yvalue || 0))+'px';
        SOS_Div.setIframe(SOS_Div.id);
       	eval(SOS_Div.onMove);
      }
    }
    catch(x){}
  }
return false;
}

/**
* für mehrere
*/
SOS_Div.setOnMove = function(range,val){
	SOS_Div.onMove						= '';
  SOS_Div.arrOnMove[range]	= val;
  for(temp in SOS_Div.arrOnMove){
		if(typeof SOS_Div.arrOnMove[temp] == 'string' && SOS_Div.arrOnMove[temp].length > 0){
			SOS_Div.onMove += SOS_Div.arrOnMove[temp];
		}	
	} 														
}

/**
* Verschiebungsende
*/
SOS_Div.moveEnd = function(){
  if(SOS_Div.id && document.getElementById(SOS_Div.id)){
    var div     = document.getElementById(SOS_Div.id);
    var changed = false;
    if(parseInt(div.style.left  || 0) <= 0){  div.style.left  = '1px'; changed = true;}
    if(parseInt(div.style.top   || 0) <= 0){  div.style.top   = '1px'; changed = true;}
    if(changed){
    	SOS_Div.setIframe(SOS_Div.id);
    	eval(SOS_Div.onMove);
    }
  }
  SOS_Div.id            = null;
  document.onmousemove  = null;
}
