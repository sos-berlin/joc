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

/** SOS_Tab : Karteireiter Darstellung (IE,FF,NS) */ 
function SOS_Tab(instancename){
  if(!instancename){ throw new Error('SOS_Tab : missing instancename'); }
  this.instancename          = instancename; /** Instance */
  this.tabIdPrefix           = 'tab_';
  this.controlId             = 'tab_control';       // ID der Steuerungstabelle
  this.contentId             = 'tab_content';       // ID des Elementes, um die eigentichen TABs
  this.controlCursor         = 'pointer';
  this.controlBgColorActiv   = null;                // Hintergrundfarbe des aktiven Steuerungs Elementes   default - keine Hintergrundfarbe setzen
  this.controlBgColorUnActiv = null;                // Hintergrundfarbe des inaktiven Steuerungs Elementes default - keine Hintergrundfarbe setzen
  this.fillContentBgColor    = false;               // Hintergrundfarbe des TABs Contents mit der Farbe von Steuerungselement füllen
  this.styleBorderActiv      = '1px solid #000'; // Borderstyle des aktiven TABs 
  this.styleBorderUnActiv    = '1px solid #888888'; // Borderstyle des inaktiven TABs
  this.indentFirst           = '10px';              // Abstand von links bis zum ersten TAB
  this.indent                = '3px';               // Abstand zwischen den TABs
  this.onMake                = '';                  // JS Kode, die ausgeführt werden soll, wenn ein TAB aktiviert wurde
  /** private */
  this.activeTabId           = '';                  // ID des gerade aktiven TABs
  this.countTabs             = 1;                   // Anzahl Tabs
}  
  
/** ein bestimmtes TAB aktivieren */
SOS_Tab.prototype.make = function(tabId){       
  try{
    if(!tabId){ throw new SOS_Exception(new Error(),'SOS_Tab.make : TAB ID fehlt','',0);}
    
    this.activeTabId  = tabId;
    this.checkControl();
    
    var bgColorActiv    = (this.controlBgColorActiv     == null) ? '' : this.controlBgColorActiv;
    var bgColorUnActiv  = (this.controlBgColorUnActiv   == null) ? '' : this.controlBgColorUnActiv;
                      
    for(var i=1; i <= this.countTabs; i++){
      var tabControl        = document.getElementById(this.controlId+'_'+i);
      var tabControlIndent  = document.getElementById(this.controlId+'_'+i+':'+i);
      var tab               = document.getElementById(this.tabIdPrefix+i);
      // tabControl & tabControlIndent werden in der Klasse gemalt und stehen immer zur Verfügung 
      if(tab == null){
        throw new Error('SOS_Tab.make : not found element with id '+this.tabIdPrefix+i);
      }
      else{
        if(this.tabIdPrefix+i == tabId){
          tab.style.display                 = '';            
          if(this.controlBgColorUnActiv != null || this.controlBgColorActiv != null){
            if(this.fillContentBgColor) {   tab.style.backgroundColor = bgColorActiv; }
            tabControl.style.backgroundColor  = bgColorActiv;
          }
          tabControl.style.borderBottom     = '';
          tabControl.style.borderTop        = this.styleBorderActiv;
          tabControl.style.borderLeft       = this.styleBorderActiv;
          tabControl.style.borderRight      = this.styleBorderActiv;
       }
        else{
          tab.style.display                 = 'none';
          if(this.controlBgColorUnActiv != null || this.controlBgColorActiv != null){
            if(this.fillContentBgColor){  tab.style.backgroundColor = bgColorUnActiv; }
            tabControl.style.backgroundColor  = bgColorUnActiv;
          }
          tabControl.style.borderBottom     = this.styleBorderActiv;
          tabControl.style.borderTop        = this.styleBorderUnActiv;
          tabControl.style.borderLeft       = this.styleBorderUnActiv;
          tabControl.style.borderRight      = this.styleBorderUnActiv;
        }
      }
    }
    if( this.onMake != null            && 
        typeof this.onMake == 'string' && 
        this.onMake.length > 0){
      eval(this.onMake); 
    }
  }
  catch(x) { 
    throw new Error(x.message);
  } 
}

/** Steuerungspanel nach eigenen Regel formatieren */
SOS_Tab.prototype.checkControl = function(){
  try{
    if(document.getElementById(this.controlId+'_1:1') == null){
      var tBody           = null;
      var controlTr       = null;
      var controlTds      = null;
      var countControlTds = 0;
      try{ tBody = document.getElementById(this.controlId).tBodies[0]; }
      catch(x){ throw new Error('not found tab control with id '+this.controlId); }
      
      if(!this.instancename || (typeof eval(this.instancename) != 'object' && typeof eval(this.instancename) != 'function')){
        throw new Error('SOS_Tab not found instancename  "'+this.instancename+'"'); 
      }
      
      controlTr       = tBody.rows[0];
      countControlTds = (controlTr.cells.length-1);   
      controlTds      = new Array(countControlTds);
      
      for(var c=0; c < countControlTds; c++){
        var cell          = controlTr.cells[c];
        var counter       = c+1;
        cell.onclick      = new Function(this.instancename+'.make(\''+this.tabIdPrefix+counter+'\');'+this.getFunctionContent(cell.onclick));
        cell.style.cursor = this.controlCursor;
        cell.setAttribute('id',this.controlId+'_'+counter);
        
        controlTds[c]     = {col : cell };
      }
      
      this.countTabs = controlTds.length;
      
      for(var c=0; c < this.countTabs; c++){
        var td                = document.createElement('td');
        var width             = (c == 0) ? this.indentFirst : this.indent;
        var counter           = c+1;
        td.style.width        = width;
        td.style.borderBottom = this.styleBorderActiv;
        td.innerHTML          = '&nbsp;';
        td.setAttribute('id',this.controlId+'_'+counter+':'+counter);
        
        controlTr.appendChild(td);
        controlTr.appendChild(controlTds[c].col);
      }
      controlTr.cells[0].style.borderBottom = this.styleBorderActiv;
      controlTr.appendChild(controlTr.cells[0]);
      
      //alert(tBody.innerHTML+' = '+controlTr.cells[3].innerHTML);
      
      var tabContent  = document.getElementById(this.contentId);
      if(tabContent != null){
        tabContent.style.borderBottom = this.styleBorderActiv;
        tabContent.style.borderLeft   = this.styleBorderActiv;
        tabContent.style.borderRight  = this.styleBorderActiv;
        tabContent.style.borderTop    = '';
      }
    }
  }
  catch(x){
    throw new Error(x.message); 
  }
}
  
/** Kode einer Funktion ermitteln  */
SOS_Tab.prototype.getFunctionContent = function(functionName){
  var content = '';
  try{
    content   = eval(functionName).toString();
    var pos   = content.indexOf('{') + 2;
    content   = content.substr(pos,content.length-pos-2);
  }
  catch(x){}
return content; 
}
