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

 /*********************************************************************
  **                                                                 **
  ** Job Editor for the Job Scheduler     IE,FF                      **
  **                                                                 **
  ** The editor supports Job RunTime and Job RunOptions              **
  **                                                                 **
  ** @copyright Software- und Oranisation-Service GmbH, Germany      **
  ** @author    Florian Schreiber <florian.schreiber@sos-berlin.com> **
  ** @since     1.0-2006/07/19                                       **
  **                                                                 **
  *********************************************************************/


  /**
  * Baum    IE,FF,NS
  *
  * @copyright    Software- und Oranisation-Service GmbH, Germany
  * @author       Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @since        1.0-2006/03/03
  *
  * @access       public
  * @package      SITE
  */
  
  SOS_Simple_Tree                 = function(){};

  /** @access public */

  /** ID des Tree Elementes */
  SOS_Simple_Tree.id              = 'sos_simple_tree';

  SOS_Simple_Tree.rootId          = 'sos_simple_tree_root';

  /** Image Verzeichnis der Anwendung */
  SOS_Simple_Tree.imgDir          = 'js/editor';

  /** Images der Anwendung */
  SOS_Simple_Tree.imgOpen         = 'icon_minus.png';
  SOS_Simple_Tree.imgClose        = 'icon_plus.png';
  SOS_Simple_Tree.imgBlank        = 'blank.png';

  /** reservierte Breite fuer den Knotennamen (falls nicht nur den Namen, sonder auch Titel etc angezeigt werden sollen) */
  SOS_Simple_Tree.nameWidth       = '100px';

  /** @access private */

  SOS_Simple_Tree.root            = null;


  /**
  * Root LI erstellen
  *
  * @param    el              HTML Element (hier wird Tree erstellt)
  * @param    rootContent     string -Inhalt des Root-Elementes
  * @access   public
  * @author   Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @version  1.0-2006/03/03
  */
  SOS_Simple_Tree.createRoot = function(el, rootContent, doc, showImage) {

    try {

      if ( typeof showImage == 'undefined' )
        showImage = true;

      if (typeof doc == 'undefined')
        throw new Error('parameter \'doc\' is undefined');

      if(!el)   return;
      if(!rootContent){ rootContent = '' ;}

      var ul = doc.createElement('ul');
      ul.id                   = SOS_Simple_Tree.id;
      ul.style.margin         = '0px 0px';
      ul.style.padding        = '0px 0px'; // wegen FF
      ul.style.listStyleType  = 'none';
      //ul.style.border         = 'solid 1px red';

      var li        = doc.createElement('li');
      li.id         = SOS_Simple_Tree.rootId;

      disp = (showImage) ? '' : 'display:none;';

      li.innerHTML  = '<img style="cursor:pointer; '+disp+'" src="'+SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgOpen+'" onclick="SOS_Simple_Tree.display(this);" />';
      li.innerHTML += rootContent;

      SOS_Simple_Tree.root = li;

      ul.appendChild(li);
      el.appendChild(ul);

      return SOS_Simple_Tree.root;
      
    } catch(x){
      throw new Error("\nSOS_Simple_Tree.createRoot()\n" + x.message);
    }
  }
  
 /**
  * einen neuen Knoten hinzufuegen
  *
  * @param    liContent   Inhalt des neues Elementes
  * @param    parentLi    sein Parent LI Element
  * @param    isOpen      boolean ob die Knoten auf|zu sein sollen
  * @return   doc         Zieldokument
  * @access   public
  * @author   Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @version  1.0-2006/03/03
  */
  SOS_Simple_Tree.appendChild = function(liContent, parentLi, isOpen, doc, leftMargin) {

   try {

      if ( typeof leftMargin == 'undefined' )
        leftMargin = '18px';

      if (typeof doc == 'undefined')
        throw new Error('parameter \'doc\' is undefined');

      if (parentLi == null) return;
      if( typeof isOpen == 'undefined') isOpen = true;

      var li = doc.createElement('li');
      //li.innerHTML  = '<img src="'+SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgBlank+'">\n<span style="width:'+SOS_Simple_Tree.nameWidth+';display:-moz-inline-box">'+liContent+'</span>';
      li.style.whiteSpace = "nowrap";
      li.innerHTML  = '<img src="'+SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgBlank+'">'+liContent;
      

      var ul        = parentLi.getElementsByTagName('ul')[0];
      var img       = parentLi.getElementsByTagName('img')[0];
      var imgSrc    = (isOpen) ? SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgOpen : SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgClose;

      if(img == null){ // Images des ausgewaelten Objektes
        img     = doc.createElement('img');
        img.src = imgSrc;

        var firstChild  = parentLi.childNodes[0];
        parentLi.replaceChild(img,firstChild);
      }
      else{
        img.src = imgSrc;
      }

      img.style.cursor  = 'pointer';
      img.onclick       = new Function('SOS_Simple_Tree.display(this);');

      if (ul == null) {
        ul = doc.createElement('ul');
        ul.style.margin         = '0px 0px 0px '+leftMargin;
        ul.style.padding        = '0px 0px'; // wegen FF
        ul.style.listStyleType  = 'none';
      }

      ul.appendChild(li);
      ul.style.display = (isOpen) ? '' : 'none';
      parentLi.appendChild(ul);

      return li;
    }
    catch(x){
      throw new Error("\nSOS_Simple_Tree.appendChild():\n" + x.message);
    }
  }


  /**
  * Knoten entfernen
  *
  * @access   public
  * @author   Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @version  1.0-2006/03/03
  */
  SOS_Simple_Tree.removeChild = function(el){

    try{
      //ul
      var ul = el.parentNode;
      ul.removeChild(el);

      if(ul.childNodes.length == 0){
        var img = ul.parentNode.getElementsByTagName('img')[0];
        ul.parentNode.removeChild(ul);
        if(img != null){
          img.src = SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgBlank;
        }
      }
    }
    catch(x){
      throw new Error("\nSOS_Simple_Tree.removeChild():\n" + x.message);
    }

  }


  /**
  * Root LI aufmachen
  *
  * @access   public
  * @author   Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @version  1.0-2006/03/03
  */
  SOS_Simple_Tree.openRoot = function(){

    try{
      if(!SOS_Simple_Tree.root){
        throw new Error('SOS_Simple_Tree.root Element fehlt');
      }

      // ROOT aufmachen
      SOS_Simple_Tree.root.getElementsByTagName('ul')[0].style.display  = '';
      SOS_Simple_Tree.root.getElementsByTagName('img')[0].src           = SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgOpen;
    }
    catch(x){
      throw new Error("\nSOS_Simple_Tree.openRoot():\n" + x.message);
    }
  }


  /**
  * Knoten anzeigen|wegblenden
  *
  * @param    img           Image Element(PLUS bzw MINUS)
  * @access   public
  * @author   Robert Ehrlich <robert.ehrlich@sos-berlin.com>
  * @version  1.0-2006/03/03
  */
  SOS_Simple_Tree.display = function(img) {
    
    try{
      var ul  = img.parentNode.getElementsByTagName('ul')[0];
      if(ul == null) return;
      if(ul.style.display == 'none'){
        ul.style.display  = '';
        img.src           = SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgOpen;
      }
      else{
        ul.style.display  = 'none';
        img.src           = SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgClose;
      }
    }
    catch(x){
      throw new Error("\nSOS_Simple_Tree.display():\n" + x.message);
    }
  }


/***************************************************************************************************************************
 **  Class DOMManager  *****************************************************************************************************
 ***************************************************************************************************************************/

DOMManager = function() {}

DOMManager.dom = null;
DOMManager.tree = null;

DOMManager.init = function(xml) {

  try {
    xml = xml.replace(/^\s*<\?[^\?]+\?>\s*/,'');
    DOMManager.dom = DOMManager.loadXML(xml);
    DOMManager.dom = DOMManager.prepareDOM(DOMManager.dom);
    DOMManager.tree = DOMManager.createTree(DOMManager.dom);
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.init():\n" : "";
    throw new Error(msg + x.message);
  }
}

// fuegt den Konten <run_time> ein, falls er nicht existiert
DOMManager.prepareDOM = function(dom) {

  try {

    var childnodes = dom.childNodes;
    var jobNode = null;

    childnodes = dom.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName=="job" ) {
        jobNode = childnodes[i];
        break;
      }
    }

    if ( jobNode==null ) throw new Error("XMLDOM: missing element \'<job>\'");

    childnodes = jobNode.childNodes;
    var node = null;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName==Editor.root ) {
        node = childnodes[i];
        break;
      }
    }

    // wenn kein Knoten run_time existiert
    if ( node==null ) {
      // Knoten run_time einfuegen
      var rt = dom.createElement(Editor.root);
      //jobNode.appendChild(dom.createTextNode("\n"));
      jobNode.appendChild(rt);
    }
    
    return dom;
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.prepareDOM():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * XML -> DOM
 */
// DOM-Objekt: XML-String einlesen und DOM-Objekt erstellen
DOMManager.loadXML = function( xml ) {

  var dom_document;

  try {

    if ( typeof xml == 'undefined' ) throw new Error("parameter xml is undefined");
    if ( xml==null ) throw new Error("parameter xml is null");

    //if ( xml=="" ) xml = "<"+Editor.root+"/>";

    xml = '<job name="">\n' + xml;
    xml += '\n</job>';

    if ( !navigator.appVersion.match(/\bMSIE\b/) && window.DOMParser ) {

      var dom_parser = new DOMParser();
      dom_document = dom_parser.parseFromString( xml, "text/xml" );
      if ( dom_document.documentElement.nodeName == "parsererror" )
        throw new Error( "Error at XML answer: " + dom_document.documentElement.firstChild.nodeValue );

    } else {

      dom_document = new ActiveXObject("MSXML2.DOMDocument");

      var ok = dom_document.loadXML( xml );
      if ( !ok )  throw new Error( "Error at XML answer: " + dom_document.parseError.reason );

      dom_document.async="false";
      dom_document.onreadystatechange=DOMManager.verify;
    }
    
    return dom_document;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.loadXML():\n" : "";
    throw new Error(msg + x.message);
  }
}

DOMManager.verify = function() {
  // 0 Object is not initialized
  // 1 Loading object is loading data
  // 2 Loaded object has loaded data
  // 3 Data from object can be worked with
  // 4 Object completely initialized

  return (this.xmldom.readyState == 4)
}

/**
 * DOM -> XML
 */
// XML-String: aus DOM-Objekt XML-String erzeugen und zurueckliefern
DOMManager.getXML = function() {

  try {

    if ( DOMManager.dom==null )
      return -1;
    else
      return DOMManager.xmlToString(DOMManager.dom);

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.getXML():\n" : "";
    throw new Error(msg + x.message);
  }
}

DOMManager.xmlToString = function(dom_document) {

  var xmlAsString = null;

  try {

    if ( window.DOMParser ) {
      xmlAsString = new XMLSerializer().serializeToString( dom_document );

    } else {
      xmlAsString = dom_document.documentElement.xml;
      xmlAsString = xmlAsString.replace(/\t/g, "  ");
    }

    xmlAsString = xmlAsString.substring(0, xmlAsString.length-6);
    xmlAsString = xmlAsString.substring(13);
    xmlAsString = xmlAsString.substring( xmlAsString.indexOf("<") );
    
    return xmlAsString;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.xmlToString():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * DOM -> Tree
 */
// Array-Tree: aus DOM-Objekt Array-Tree erzeugen
DOMManager.createTree = function(node) {
   
  var tree = new Object();
  var childnodes = null;
  
  try {
    
    var jobNode = null;
    childnodes = node.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName=="job" ) {
        jobNode = childnodes[i];
        break;
      }
    }
    if ( jobNode==null ) throw new Error("XMLDOM: missing element \'<job>\'");
    
    
    childnodes = jobNode.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName==Editor.root ) {
        node = childnodes[i];
        break;
      }
    }
    if ( node.nodeName!=Editor.root ) throw new Error("XMLDOM: missing element \'"+Editor.root+"\'");
    
    tree[Editor.root] = DOMManager.getPeriod(node, "once");
    
    // start_time_function
    tree["start_time_function"] = node.getAttribute("start_time_function");
    
    // schedule attribute
    tree["scheduleA"] = node.getAttribute("schedule");
    if(typeof tree["scheduleA"] != 'string') tree["scheduleA"] = '';
    
    // schedule node attributes
    tree["title"] = node.getAttribute("title");
    if(typeof tree["title"] != 'string') tree["title"] = '';
    tree["substitute"] = node.getAttribute("substitute");
    if(typeof tree["substitute"] != 'string') tree["substitute"] = '';
    tree["valid_from"] = node.getAttribute("valid_from");
    if(typeof tree["valid_from"] != 'string') tree["valid_from"] = '';
    tree["valid_to"]   = node.getAttribute("valid_to");
    if(typeof tree["valid_to"] != 'string') tree["valid_to"] = '';
    
    childnodes = node.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      
      switch ( childnodes[i].nodeName ) {
        
        case "period":  // everyday

            DOMManager.createTree_everyday(tree, childnodes[i]);
            break;

        case "weekdays":

            DOMManager.createTree_daylist(tree, childnodes[i], "weekdays", "weekdays_items");
            break;

        case "monthdays":
            
            DOMManager.createTree_monthdays(tree, childnodes[i]);
            break;
            
        case "ultimos":

            DOMManager.createTree_daylist(tree, childnodes[i], "ultimos", "ultimos_items");
            break;

        case "date":  // specific days
            
            DOMManager.createTree_date(tree, childnodes[i]);
            break;
        
        case "holidays":
            
            DOMManager.createTree_holidays(tree, childnodes[i]);
            break;
            
        case "at":
            
            DOMManager.createTree_at(tree, childnodes[i]);
            break;
        
        case "month":
            if(!tree["month"]) tree["month"] = new Object();
            var months = childnodes[i].getAttribute("month").split(" ");
            for( var j=0; j < months.length; j++ ) {
              if( typeof Editor.map_month[months[j].toString().toLowerCase()] != 'undefined' ) {
                months[j] = Editor.map_month[months[j].toString().toLowerCase()];
              }
            }
            months.sort(Editor.NumSort);
            var month = months.join(" ");
            if(!tree["month"][month]) tree["month"][month] = new Object();
            //tree["month"][month]        = DOMManager.createTree(childnodes[i], false, tree["month"][month]);
            var month_childnodes = childnodes[i].childNodes;
            for (var j=0; j<month_childnodes.length; j++) {
              var child = month_childnodes[j];
              switch ( child.nodeName ) {
                case "period":  // everyday
                
                    DOMManager.createTree_everyday(tree["month"][month], child);
                    break;
                
                case "weekdays":
                
                    DOMManager.createTree_daylist(tree["month"][month], child, "weekdays", "weekdays_items");
                    break;
                
                case "monthdays":
                    
                    DOMManager.createTree_monthdays(tree["month"][month], child);
                    break;
                    
                case "ultimos":
                
                    DOMManager.createTree_daylist(tree["month"][month], child, "ultimos", "ultimos_items");
                    break;
                /*
                case "date":  // specific days
                    
                    DOMManager.createTree_date(tree["month"][month], child);
                    break;
                    
                case "at":
                    
                    DOMManager.createTree_at(tree["month"][month], child);
                    break;  */              
              }
            }
            break;        
      }
    }
    
  
    /*  run_options  */
    var start_when_directory_changed = null;
    var delay_order_after_setback = null;
    var delay_after_error = null;
    var o;
    
    childnodes = jobNode.childNodes;
    for (var i=0; i<childnodes.length; i++) {
    
      if ( childnodes[i].nodeName=="start_when_directory_changed" ) {
        node = childnodes[i];
    
        if ( start_when_directory_changed==null )
          start_when_directory_changed = new Array();
    
        o = new Object();
        o["directory"] = node.getAttribute("directory");
        o["regex"] = node.getAttribute("regex");
        if ( o["regex"]==null ) o["regex"]="";
        start_when_directory_changed[start_when_directory_changed.length] = o;
      }
    
      if ( childnodes[i].nodeName=="delay_order_after_setback" ) {
        node = childnodes[i];
    
        if ( delay_order_after_setback==null )
          delay_order_after_setback = new Array();
    
        o = new Object();
        o["setback_count"] = node.getAttribute("setback_count");
        o["is_maximum"] = node.getAttribute("is_maximum");
        if ( o["is_maximum"]==null || o["is_maximum"]=="" ) o["is_maximum"]="no";
        o["delay"] = node.getAttribute("delay");
        if ( o["delay"]==null || o["delay"]=="" ) o["delay"]="0";
        delay_order_after_setback[delay_order_after_setback.length] = o;
      }
    
      if ( childnodes[i].nodeName=="delay_after_error" ) {
        node = childnodes[i];
    
        if ( delay_after_error==null )
          delay_after_error = new Array();
    
        o = new Object();
        o["error_count"] = node.getAttribute("error_count");
        o["delay"] = node.getAttribute("delay");
        if ( o["delay"]==null || o["delay"]=="" ) o["delay"]="00";
        delay_after_error[delay_after_error.length] = o;
      }
    }
    
    if ( start_when_directory_changed!=null || delay_after_error!=null || delay_order_after_setback!=null ) {
      if ( tree["run_options"]==null )
        tree["run_options"] = new Object();
    }
    
    if ( start_when_directory_changed!=null )
      tree["run_options"]["start_when_directory_changed"] = start_when_directory_changed;
    
    if ( delay_order_after_setback!=null )
      tree["run_options"]["delay_order_after_setback"] = delay_order_after_setback;
    
    if ( delay_after_error!=null )
      tree["run_options"]["delay_after_error"] = delay_after_error;
    
    return tree;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.createTree():\n" : "";
    throw new Error(msg + x.message);
  }
}


DOMManager.createTree_everyday = function(tree, node) {
  
  if ( tree["everyday"]==null ) tree["everyday"] = new Array();
  tree["everyday"].push( DOMManager.getPeriod(node) );
}


DOMManager.createTree_monthdays = function(tree, node) {
  
  // alle <day> einlesen
  DOMManager.createTree_daylist(tree, node, "monthdays", "monthdays_items");
            
  // Specific Weekdays einlesen ( alle <weekday> )
  tree["specific_weekdays"] = new Object();
  tree["specific_weekdays"].days = new Array();
  
  var days = node.childNodes;
  for (var d=0; d<days.length; d++) {
    
    var day = days[d];
    if ( day.nodeName=="weekday" ) {
      
      var pArr = new Array();
      
      var periods = day.childNodes;
      if ( periods!=null && periods.length > 0 ) {
        for (var p=0; p<periods.length; p++) {
          if ( periods[p].nodeName=="period" )
            pArr.push( DOMManager.getPeriod( periods[p] ) );
        }
      }
      
      tree["specific_weekdays"].days[tree["specific_weekdays"].days.length] = day.getAttribute("which")+'.'+day.getAttribute("day");
      tree["specific_weekdays"][day.getAttribute("which")+'.'+day.getAttribute("day")] = pArr;
    }
  }
}


DOMManager.createTree_date = function (tree, node) {

  if ( tree["specificdays"]      == null ) tree["specificdays"]       = new Array();
  if ( tree["specificdays_dates"]== null ) tree["specificdays_dates"] = new Object();
  
  var date = DOMManager.dateToNumber( node.getAttribute("date") );
  
  if ( tree["specificdays_dates"][date]==null ) {
    tree["specificdays"].push( date );
    tree["specificdays_dates"][date] = new Array();
  }
  
  var periods = node.childNodes;
  for (var n=0; n<periods.length; n++) {
    if ( periods[n].nodeName=="period" )
      tree["specificdays_dates"][date].push( DOMManager.getPeriod(periods[n]) );
  }
}


DOMManager.createTree_holidays = function (tree, node) {
     
  if ( tree["holidays"]        ==null ) tree["holidays"]         = new Array();
  if ( tree["holiday_includes"]==null ) tree["holiday_includes"] = new Array();
  if ( tree["holiday_weekdays"]==null ) tree["holiday_weekdays"] = new Object();
            
  var holidays = node.childNodes;
            
  for (var n=0; n<holidays.length; n++) {
    if ( holidays[n].nodeName=="holiday" )
      tree["holidays"].push( DOMManager.dateToNumber( holidays[n].getAttribute("date") ) );
    else
    if ( holidays[n].nodeName=="include") {
      for( var i=0; i < holidays[n].attributes.length; i++ ) {
        switch( holidays[n].attributes[i].nodeName ) {
          case "file"      : tree["holiday_includes"].push( 'file: '+holidays[n].getAttribute("file") ); break;
          case "live_file" : tree["holiday_includes"].push( 'live_file: '+holidays[n].getAttribute("live_file") ); break;
        }
      }
    }
  }
  
  var holiday_childnodes = node.childNodes;
  for (var j=0; j<holiday_childnodes.length; j++) {
    if ( holiday_childnodes[j].nodeName == "weekdays" ) {
      DOMManager.createTree_daylist(tree, holiday_childnodes[j], "holiday_weekdays", "holiday_weekdays_items");
    }
  }
}


DOMManager.createTree_at = function(tree, node) {
  
  if ( tree["specificdays"]      == null ) tree["specificdays"]       = new Array();
  if ( tree["specificdays_dates"]== null ) tree["specificdays_dates"] = new Object();
  if ( tree["at_date"]           == null ) tree["at_date"]            = new Array();
  
  var at   = node.getAttribute("at").split(' ');
  var date = DOMManager.dateToNumber( at[0] );
  
  if ( tree["specificdays_dates"][date]==null ) {
    tree["specificdays"].push( date );
    tree["specificdays_dates"][date] = new Array();
  }
  
  var exists = false;
  for (var n=0; n<tree["at_date"].length; n++) {
    if ( tree["at_date"][n] == date ) exists = true;
  }
  if ( !exists )
    tree["at_date"].push( date );
  
  
  var period = DOMManager.createNewPeriod();
  period["singlestart_hh"] = DOMManager.timeGetHH(at[1]);
  period["singlestart_mm"] = DOMManager.timeGetMM(at[1]);
  period["singlestart_ss"] = DOMManager.timeGetSS(at[1]);
  period.isAT = true;
  
  tree["specificdays_dates"][date].push( period );
}

/**
 * Hilfsfunktion fuer create_tree()
 * Liefert einen Teilbaum zurueck.
 * Wird verwendet fuer weekdays, monthdays und ultimos (=mode).
 * tree ist die Referenz des Baums
 * dataA der name des Datenarrays (Object)
 */
DOMManager.createTree_daylist = function(tree, node, mode, dataA) {

  try {

    if ( !tree[mode]  ) tree[mode]  = new Object();
    if ( !tree[dataA] ) tree[dataA] = new Object();

    var days  = node.childNodes;
    var attr;
    var oVals = {'1':0,'2':0,'3':0,'4':0,'5':0,'6':0,'7':0};

    for (var d=0; d<days.length; d++) {

      if ( days[d].nodeName=="day" ) {

        var aVal  = days[d].getAttribute("day");
        var aVals = aVal.split(" ");
        if( mode == "weekdays" || mode == "holiday_weekdays" ) {
          for( var i=0; i < aVals.length; i++ ) {
            if( aVals[i].toString() == "0" ) aVals[i] = 7;
            if( typeof Editor.map_weekdays[aVals[i].toString().toLowerCase()] != 'undefined' ) {
              aVals[i] = Editor.map_weekdays[aVals[i].toString().toLowerCase()];
            }
          }
        }
        if( mode == "holiday_weekdays" ) {
          for( var i=0; i < aVals.length; i++ ) oVals[aVals[i]]++;
          continue; //Keine Perioden bei Holidays
        } else {
          aVals.sort(Editor.NumSort);
          aVal      = aVals.join(" ");
          for( var i=0; i < aVals.length; i++ ) aVals[i] = Editor[mode][aVals[i]];
          tree[mode][aVal] = aVals.join(" ");
        }
        
        var periods = days[d].childNodes;

        if ( periods.length > 0 && tree[dataA][aVal]==null )
          tree[dataA][aVal] = new Array();

        for (var p=0; p<periods.length; p++) {
          if ( periods[p].nodeName=="period" )
            tree[dataA][aVal].push( DOMManager.getPeriod( periods[p] ) );
        }
      }
    }
    
    if( mode == "holiday_weekdays" ) {
      for(var entry in oVals) {
        if( oVals[entry] > 0 ) tree[mode][entry] = Editor['weekdays'][entry];
      } 
    }

  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.createTree_daylist():\n" : "";
    throw new Error(msg + x.message);
  }
}

// bekommt ein Date und liefert eine Zahl zurueck
// z.B. "2006-06-17" -> "20060617"
DOMManager.dateToNumber = function(date) {

  var num = null;

  try {

    num = date.substring(0, 4)+date.substring(5, 7)+date.substring(8);
    
    return num;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.dateToNumber():\n" : "";
    throw new Error(msg + x.message);
  }
}

// bekommt eine Datums-Zahl und liefert ein Date zurueck
// z.B. "20060617" -> "2006-06-17"
DOMManager.numberToDate = function(num) {

  var date = null;

  try {

    date = num.substring(0,4)+"-"+num.substring(4,6)+"-"+num.substring(6);
    
    return date;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.numberToDate():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
  liefert alle Attribute einer Periode aus dem DOM des Knotens node in einem Array zurueck.
  Nicht existierende aber moeglich Attribute werden mit einem Leerstring als Wert gespeichert.
  Fuer Runtime existiert in period zusaetzlich das Attribut "once".
  Wenn der Parameter once definiert wird, wird dieses Attribut zusaetzlich aus dem DOM gelesen
*/
DOMManager.getPeriod = function(node, once) {

  var p = new Object();
  var attr;

  try {

    if ( node.nodeName!="period" && node.nodeName!=Editor.root )
      throw new Error("illegal node "+node.nodeName);
    
    //if ( node.nodeName=="period" || node.nodeName=="run_time" ) {
      if ( typeof once != 'undefined' ) {
        attr = node.getAttribute("once");
        p["run_once"] = !( attr==null || attr=="" || attr=="no");
      }

      attr = node.getAttribute("let_run");
      p["let_run"] = !( attr==null || attr=="" || attr=="no");

      attr = node.getAttribute("begin");
      p["begintime_hh"] = DOMManager.timeGetHH(attr);
      p["begintime_mm"] = DOMManager.timeGetMM(attr);
      p["begintime_ss"] = DOMManager.timeGetSS(attr);

      attr = node.getAttribute("end");
      p["endtime_hh"] = DOMManager.timeGetHH(attr);
      p["endtime_mm"] = DOMManager.timeGetMM(attr);
      p["endtime_ss"] = DOMManager.timeGetSS(attr);

      attr = node.getAttribute("repeat");
      p["repeattime_hh"] = DOMManager.timeGetHH(attr);
      p["repeattime_mm"] = DOMManager.timeGetMM(attr);
      p["repeattime_ss"] = DOMManager.timeGetSS(attr);
    
      attr = node.getAttribute("absolute_repeat");
      p["absoluterepeat_hh"] = DOMManager.timeGetHH(attr);
      p["absoluterepeat_mm"] = DOMManager.timeGetMM(attr);
      p["absoluterepeat_ss"] = DOMManager.timeGetSS(attr);

      attr = node.getAttribute("single_start");
      p["singlestart_hh"] = DOMManager.timeGetHH(attr);
      p["singlestart_mm"] = DOMManager.timeGetMM(attr);
      p["singlestart_ss"] = DOMManager.timeGetSS(attr);
      
      attr = node.getAttribute("when_holiday");
      p["when_holiday"] = ( attr==null || attr.search(/^(previous_non|next_non|ignore)_holiday$/) == -1 ) ? "suppress" : attr;
    
    //}
    
    return p;
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.getPeriod():\n" : "";
    throw new Error(msg + x.message);
  }
}

// Erzeugt ein neues Period-Objekt und liefert dieses zurueck
DOMManager.createNewPeriod = function(once) {

  var p = new Object();

  try {

    if ( typeof once != 'undefined' )
      p["run_once"] = false;

    p["let_run"] = false;
    p["when_holiday"] = "suppress";

    for (var i=0; i<Editor.attribute_indices.length; i++)
      p[ Editor.attribute_indices[i] ] = "";
    
    return p;
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.createNewPeriod():\n" : "";
    throw new Error(msg + x.message);
  }
}

// ueberprueft, ob die uebergebene Periode gueltig fuer <at> ist
DOMManager.isValidAT = function(p) {
  
  try {
    
    if ( p["singlestart_hh"]=='' && p["singlestart_mm"]=='' )
      return false;
    
    for (var i=0; i<Editor.attribute_indices.length-3; i++)
      if ( p[ Editor.attribute_indices[i] ] != '' ) return false;
    
    return true;
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.isValidAT():\n" : "";
    throw new Error(msg + x.message);
  }
}

DOMManager.clonePeriod = function(period) {

  var p = new Object();

  try {

    if ( period["run_once"] != null )
      p["run_once"] = period["run_once"];

    p["let_run"] = period["let_run"];
    p["when_holiday"] = period["when_holiday"];

    for (var i=0; i<Editor.attribute_indices.length; i++)
      p[ Editor.attribute_indices[i] ] = period[ Editor.attribute_indices[i] ];
    
    return p;
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.clonePeriod():\n" : "";
    throw new Error(msg + x.message);
  }
}

// setzt im DOM alle Attribute einer period entsprechend der Elemente des period-Arrays period
// das period-Array period enthaelt zu allen Attributen Werte, z.B. auch "" fuer undefiniert
// Attribute koennen auch geloescht werden
DOMManager.setPeriod = function(node, period) {

  try {

    if ( node.nodeName!="period" && node.nodeName!=Editor.root )
      throw new Error("illegal node "+node.nodeName);
      
    
    if ( period["let_run"]==true )
      node.setAttribute("let_run", "yes");
    else {
      if ( node.getAttribute("let_run")!=null )
        node.removeAttribute("let_run");
    }
    

    if ( period["run_once"]!=null ) {
      if ( period["run_once"]==true )
        node.setAttribute("once", "yes");
      else {
        if ( node.getAttribute("once")!=null )
          node.removeAttribute("once");
      }
    }
    
    if ( period["when_holiday"]!=null && period["when_holiday"].search(/^(previous_non|next_non|ignore)_holiday$/) > -1 ) {
      node.setAttribute("when_holiday", period["when_holiday"]);
    } else {
      node.removeAttribute("when_holiday");
    }
    
    DOMManager.updateDOMAttribute( node, "begin", DOMManager.getTimestamp(period["begintime_hh"],period["begintime_mm"],period["begintime_ss"]) );
    DOMManager.updateDOMAttribute( node, "end", DOMManager.getTimestamp(period["endtime_hh"],period["endtime_mm"],period["endtime_ss"]) );
    DOMManager.updateDOMAttribute( node, "repeat", DOMManager.getTimestamp(period["repeattime_hh"],period["repeattime_mm"],period["repeattime_ss"]) );
    DOMManager.updateDOMAttribute( node, "absolute_repeat", DOMManager.getTimestamp(period["absoluterepeat_hh"],period["absoluterepeat_mm"],period["absoluterepeat_ss"]) );
    DOMManager.updateDOMAttribute( node, "single_start", DOMManager.getTimestamp(period["singlestart_hh"],period["singlestart_mm"],period["singlestart_ss"]) );
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.setPeriod():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * Aktualisiert ein Attribut des Knotens node
 * Wenn value nicht leer ist wird das Attribut gesetzt
 * Wenn value "" ist und das Attribut bisher gesetzt war, wird das Attribut geloescht.
 */
DOMManager.updateDOMAttribute = function(node, attribute, value) {

  try {

    if ( attribute==null || attribute=="" ) throw new Error("DOMManager.updateDOMAttribute(): parameter 'attribute' is null or empty");
    if ( value==null ) throw new Error("DOMManager.updateDOMAttribute(): value for attribute "+ attribute +" is null");

    if ( value!="" )
      node.setAttribute(attribute, value);
    else {
      if ( node.getAttribute(attribute)!=null )
        node.removeAttribute(attribute);
    }

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.updateDOMAttribute():\n" : "";
    throw new Error(msg + x.message);
  }
}


/**
 * time hat die Form hh:mm:ss
 */
DOMManager.timeGetHH = function(time) {

  try {

    if (time==null)
      return "";

    if ( time.indexOf(':')==-1 ) // es gibt nur Sekunden
      return "";

    var hh = time.substring(0,time.indexOf(':'));

    hh = Math.round(hh);
    if (hh>24) throw new Error(hh +" is no valid hour");

    if (hh<10) hh = "0"+hh;

    return hh;

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.timeGetHH():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * time hat die Form hh:mm:ss
 */
DOMManager.timeGetMM = function(time) {

  try {

    if (time==null)
      return "";

    if ( time.indexOf(':')==-1 ) // es gibt nur Sekunden
      return "";

    var mm = time.substring(time.indexOf(':')+1);

    if ( mm.indexOf(':')!=-1 ) {
      mm = mm.substring(0,time.indexOf(':'));
    }

    mm = Math.round(mm);
    if (mm>=60) throw new Error(mm +" is no valid minute");
    if (mm<10) mm = "0"+mm;

    return mm;

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.timeGetMM():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * time hat die Form hh:mm:ss
 */
DOMManager.timeGetSS = function(time) {

  try {

    if (time==null)
      return "";

    var index = time.indexOf(':');

    if ( index==-1 )
      return time;

    if ( index==time.lastIndexOf(':') )
      return "";

    var ss = time.substring(time.lastIndexOf(':')+1);

    return ss;

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.timeGetSS():\n" : "";
    throw new Error(msg + x.message);
  }
}

DOMManager.getTimestamp = function(hh, mm, ss) {

  try {

    var time = "";

    var nan = null;
    if ( hh==null ) nan="hh";
    if ( mm==null ) nan="mm";
    if ( ss==null ) nan="ss";
    if ( nan!=null ) {
      throw new Error("DOMManager.getTimestamp("+hh+""+mm+""+ss+"): "+nan+" ist null");
      return "";
    }

    if (hh=="" && mm=="" && ss=="")
      return "";

    var h = Math.round(hh);
    var m = Math.round(mm);
    var s = Math.round(ss);

    // nur Sekundenanzeige
    if ( s>=60 ) {
      time += s;

    // Zeitformat hh:mm oder hh:mm:ss
    } else {

      if (h<10) h = "0"+h;
      if (m<10) m = "0"+m;
      time = h+":"+m;

      if (ss!="" && s!=0) {
        if (s<10) s = "0"+s;
        time += ":"+s;
      }
    }

    return time;

  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.getTimestamp():\n" : "";
    throw new Error(msg + x.message);
  }
}

/**
 * Tree -> DOM
 */
// DOM-Objekt: DOM-Objekt mit aktuellem Array-Tree updaten
DOMManager.updateDOM = function(dom, tree) {
  
  var node = null;
  var childnodes;
  //Editor.remote.Alert( dom.xml );
  try {
    
    if ( typeof tree == 'undefined')
      throw new Error("tree is undefined");
    
    if ( tree==null )
      throw new Error("tree is null");
    
    if ( typeof dom != 'object' )
      throw new Error("DOMManager.dom is no object");
    
    
    var jobNode = null;
    childnodes = dom.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName=="job" ) {
        jobNode = childnodes[i];
        break;
      }
    }
    if ( jobNode==null ) throw new Error("XMLDOM: missing element \'<job>\'");
    
    // run_options
    var affectedNodes = new Object();
    affectedNodes["start_when_directory_changed"] = 1;
    affectedNodes["delay_order_after_setback"] = 1;
    affectedNodes["delay_after_error"] = 1;

    // betroffene run_options-Knoten loeschen
    childnodes = jobNode.childNodes;
    for (var i=childnodes.length-1; i>=0; i--) {
      node = childnodes[i];
      if ( affectedNodes[node.nodeName]!=null )
        jobNode.removeChild(node);
    }
    
    childnodes = jobNode.childNodes;
    for (var i=childnodes.length-1; i>0; i--) {
      if (childnodes[i].nodeType==3 && childnodes[i-1].nodeType==3)
        jobNode.removeChild(childnodes[i]);
    }
    
    
// run_time
    childnodes = jobNode.childNodes;
    for (var i=0; i<childnodes.length; i++) {
      if ( childnodes[i].nodeName==Editor.root ) {
        node = childnodes[i];
        break;
      }
    }
    if ( node==null ) throw new Error("XMLDOM: missing element \'"+Editor.root+"\'");
    
    var runtimeNode = node;
    
  // betroffene Knoten aus run_time loeschen
    affectedNodes = new Object();
    affectedNodes["at"] = 1;
    affectedNodes["period"] = 1;
    affectedNodes["weekdays"] = 1;
    affectedNodes["monthdays"] = 1;
    affectedNodes["ultimos"] = 1;
    affectedNodes["date"] = 1;
    affectedNodes["holidays"] = 1;
    affectedNodes["month"] = 1;
    
    
    childnodes = runtimeNode.childNodes;
    for (var i=childnodes.length-1; i>=0; i--) {
      node = childnodes[i];
      if ( affectedNodes[node.nodeName]!=null )
        runtimeNode.removeChild(node);
    }
    
    childnodes = runtimeNode.childNodes;
    for (var i=childnodes.length-1; i>0; i--) {
      if (childnodes[i].nodeType==3 && childnodes[i-1].nodeType==3)
        runtimeNode.removeChild(childnodes[i]);
    }

    node = runtimeNode.lastChild;
    if ( node!=null && node.nodeType==3 ) {
      if ( runtimeNode.childNodes.length == 1 )
        runtimeNode.removeChild(node);
      else
        runtimeNode.replaceChild(dom.createTextNode("\n"), node);
    }
    
    // Knoten runtime aus DOM entfernen solange die run_options noch nicht eingefuegt sind
    runtimeNode = jobNode.removeChild(runtimeNode);

  // alle betroffenen Knoten sind nun geloescht

    /* run_options */
    // werden in jobNode eingehaengt
    var run_options = tree["run_options"];
    if ( run_options!=null ) {

      var roArr;
      var ro;

      if ( run_options["start_when_directory_changed"]!=null ) {

        RunOption.sort_DelayOrderAfterSetback();

        roArr = run_options["start_when_directory_changed"];
        for (var i=0; i<roArr.length; i++) {
          ro = dom.createElement("start_when_directory_changed");
          ro.setAttribute("directory", roArr[i]["directory"]);
          if ( roArr[i]["regex"]!=null && roArr[i]["regex"]!="" )
            ro.setAttribute("regex", roArr[i]["regex"]);
          jobNode.appendChild(dom.createTextNode("\n"));
          jobNode.appendChild( ro );
        }
      }
      
      if ( run_options["delay_after_error"]!=null ) {

        roArr = run_options["delay_after_error"];
        for (var i=0; i<roArr.length; i++) {
          ro = dom.createElement("delay_after_error");
          ro.setAttribute("error_count", roArr[i]["error_count"]);
          ro.setAttribute("delay", roArr[i]["delay"]);
          jobNode.appendChild(dom.createTextNode("\n"));
          jobNode.appendChild( ro );
        }
      }
      
      if ( run_options["delay_order_after_setback"]!=null ) {
        
        RunOption.sort_DelayAfterError();
        
        roArr = run_options["delay_order_after_setback"];
        for (var i=0; i<roArr.length; i++) {
          ro = dom.createElement("delay_order_after_setback");
          ro.setAttribute("setback_count", roArr[i]["setback_count"]);
          ro.setAttribute("is_maximum", roArr[i]["is_maximum"]);
          ro.setAttribute("delay", roArr[i]["delay"]);
          jobNode.appendChild(dom.createTextNode("\n"));
          jobNode.appendChild( ro );
        }
      }
    }
    
    
    // run_time
    if ( tree[Editor.root]==null ) throw new Error('tree["'+Editor.root+'"] is null');
    
    if ( runtimeNode.getAttribute("start_time_function")!=null )
      runtimeNode.removeAttribute("start_time_function");
      
    if ( runtimeNode.getAttribute("title")!=null )
      runtimeNode.removeAttribute("title");
    if ( runtimeNode.getAttribute("substitute")!=null )
      runtimeNode.removeAttribute("substitute");
    if ( runtimeNode.getAttribute("valid_from")!=null )
      runtimeNode.removeAttribute("valid_from");
    if ( runtimeNode.getAttribute("valid_to")!=null )
      runtimeNode.removeAttribute("valid_to");
    
    // schedule aktualisieren
    if ( runtimeNode.getAttribute("schedule")!=null )
      runtimeNode.removeAttribute("schedule");
    if ( tree["scheduleA"].length > 0 )
      DOMManager.updateDOMAttribute(runtimeNode, "schedule", tree["scheduleA"]);
    
    if (runtimeNode.getAttribute("schedule")==null ) {
    
      // schedule node attributes aktualisieren
      if ( tree["title"].length > 0 )
        DOMManager.updateDOMAttribute(runtimeNode, "title", tree["title"]);
      if ( tree["substitute"].length > 0 )
        DOMManager.updateDOMAttribute(runtimeNode, "substitute", tree["substitute"]);
      if ( tree["valid_from"].length > 0 )
        DOMManager.updateDOMAttribute(runtimeNode, "valid_from", tree["valid_from"]);
      if ( tree["valid_to"].length > 0 )
        DOMManager.updateDOMAttribute(runtimeNode, "valid_to", tree["valid_to"]);
      
      // start_time_function aktualisieren
      if ( tree["start_time_function"]!=null && tree["start_time_function"].length > 0 )
        DOMManager.updateDOMAttribute(runtimeNode, "start_time_function", tree["start_time_function"]);
      
      DOMManager.setPeriod(runtimeNode, tree[Editor.root]);
      
      // everyday
      DOMManager.updateDOM_everyday( dom, tree, runtimeNode, "  " );
      
      // at
      DOMManager.updateDOM_at( dom, tree, runtimeNode, "  " );    
      
      // specific days
      DOMManager.updateDOM_specificdays( dom, tree, runtimeNode, "  " );
      
      // weekdays
      DOMManager.updateDOM_daylist(dom, runtimeNode, "weekdays", tree["weekdays"], tree["weekdays_items"], "  ");
      
      // monthdays
      DOMManager.updateDOM_daylist(dom, runtimeNode, "monthdays", tree["monthdays"], tree["monthdays_items"], "  ");
      
      // Specific Weekdays schreiben ( alle <weekday> ) 
      DOMManager.updateDOM_specificWeekdays( dom, tree, runtimeNode, "  " );
      
      // ultimos
      DOMManager.updateDOM_daylist(dom, runtimeNode, "ultimos", tree["ultimos"], tree["ultimos_items"], "  ");
      
      // month
      if ( tree["month"]!=null ) {
        for( var entry in tree.month ) {
          if ( Editor.xmlBlankLines ) runtimeNode.appendChild(dom.createTextNode("\n"));
          runtimeNode.appendChild(dom.createTextNode("\n  "));
          var month = dom.createElement("month");
          month.setAttribute("month", entry );
          var month_tree = tree.month[entry];
          DOMManager.updateDOM_everyday( dom, month_tree, month, "    " );
          //DOMManager.updateDOM_at( dom, month_tree, month, "    " );
          //DOMManager.updateDOM_specificdays( dom, month_tree, month, "    " );
          DOMManager.updateDOM_daylist(dom, month, "weekdays", month_tree["weekdays"], month_tree["weekdays_items"], "    ");
          DOMManager.updateDOM_daylist(dom, month, "monthdays", month_tree["monthdays"], month_tree["monthdays_items"], "    ");
          DOMManager.updateDOM_specificWeekdays( dom, month_tree, month, "    " );
          DOMManager.updateDOM_daylist(dom, month, "ultimos", month_tree["ultimos"], month_tree["ultimos_items"], "    ");
          if(month.childNodes.length > 0) month.appendChild(dom.createTextNode("\n  "));
          runtimeNode.appendChild(month);
          //tree.month[entry] = null;
        }
      }
        
      // holidays
      var holiday_weekdays = new Array();
      for( var entry in tree["holiday_weekdays"] ) {
        holiday_weekdays.push( entry );
      }
      if ( (tree["holidays"]!=null && tree["holidays"].length > 0)
        || (tree["holiday_includes"]!=null && tree["holiday_includes"].length > 0)
        || (holiday_weekdays.length > 0 ) ) {
        
        if ( Editor.xmlBlankLines ) runtimeNode.appendChild(dom.createTextNode("\n"));
        runtimeNode.appendChild(dom.createTextNode("\n  "));
        node = dom.createElement("holidays");
        
        if ( tree["holidays"]!=null && tree["holidays"].length > 0) {
          for (var i=0; i<tree["holidays"].length; i++) {
            node.appendChild(dom.createTextNode("\n    "));
            var h = dom.createElement("holiday");
            h.setAttribute("date", DOMManager.numberToDate(tree["holidays"][i]) );
            node.appendChild(h);
          }
        }
        
        if ( tree["holiday_includes"]!=null && tree["holiday_includes"].length > 0) {
          for (var i=0; i<tree["holiday_includes"].length; i++) {
            node.appendChild(dom.createTextNode("\n    "));
            var h = dom.createElement("include");
            var holiday_includes = tree["holiday_includes"][i].split(": ");
            h.setAttribute(holiday_includes.shift(), holiday_includes.join(": ") );
            node.appendChild(h);
          }
        }
        
        if ( holiday_weekdays.length > 0) {
          holiday_weekdays.sort(Editor.NumSort);
          node.appendChild(dom.createTextNode("\n    "));
          var h = dom.createElement("weekdays");
          node.appendChild(h);
          h.appendChild(dom.createTextNode("\n      "));
          var hd = dom.createElement("day");
          hd.setAttribute("day", holiday_weekdays.join(" ") );
          h.appendChild(hd);
          h.appendChild(dom.createTextNode("\n    "));
        }
        
        node.appendChild(dom.createTextNode("\n  "));
        runtimeNode.appendChild(node);
      }
    
      if ( runtimeNode.childNodes.length > 0 ) {
        runtimeNode.appendChild(dom.createTextNode("\n"));
        if ( Editor.xmlBlankLines ) runtimeNode.appendChild(dom.createTextNode("\n"));
      }
    }
    // zum Schluss den Knoten runtime wieder in das DOM haengen
    jobNode.appendChild(dom.createTextNode("\n")); // vorher einen Umbruch
    jobNode.appendChild(runtimeNode);
    
  } catch (x) {
    msg = ( Editor.debug ) ? "DOMManager.updateDOM():\n" : "";
    throw new Error(msg + x.message);
  }
}


DOMManager.updateDOM_everyday = function( dom, tree, parentNode, indent ) {
    
    if ( tree["everyday"]!=null && tree["everyday"].length > 0 ) {
      if (Editor.xmlBlankLines) parentNode.appendChild(dom.createTextNode("\n"));
      for (var i=0; i<tree["everyday"].length; i++) {
        parentNode.appendChild(dom.createTextNode("\n"+indent));
        var node = dom.createElement("period");
        DOMManager.setPeriod(node, tree["everyday"][i]);
        parentNode.appendChild(node);
      }
    }
}


DOMManager.updateDOM_at = function( dom, tree, parentNode, indent ) {
    
    if ( tree["at_date"]!=null ) {
      
      var date;
      var period;
      var single_at;
      
      for (var i=0; i<tree["at_date"].length; i++) {
        
        date = tree["at_date"][i];
        single_at = false;
        
        for (var n=0; n<tree["specificdays_dates"][date].length; n++) {
          period = tree["specificdays_dates"][date][n];
          if ( period.isAT ) {
            if ( DOMManager.isValidAT(period) ) {
              
              single_at = true;
              
              var hh = period["singlestart_hh"];
              var mm = period["singlestart_mm"];
              var ss = period["singlestart_ss"];
              
              parentNode.appendChild(dom.createTextNode("\n"+indent));
              var node = dom.createElement("at");
              node.setAttribute("at", DOMManager.numberToDate(date)+' '+DOMManager.getTimestamp(hh,mm,ss) );
              parentNode.appendChild(node);
              
              tree["specificdays_dates"][date][n] = null;
            }
          }
        }
        
        // existierte nur einmal fuer <at>
        if ( tree["specificdays_dates"][date].length==1 && single_at ) {
          for (var n=0; n<tree["specificdays"].length; n++) {
            if ( tree["specificdays"][n] == date )
              tree["specificdays"][n] = null;
          }
        }
        
      }
    }
}

DOMManager.updateDOM_specificdays = function( dom, tree, parentNode, indent ) {
   
   if ( tree["specificdays"]!=null && tree["specificdays"].length > 0 ) {

      var period;
      var dateElem;
      if ( Editor.xmlBlankLines ) parentNode.appendChild(dom.createTextNode("\n"));

      for (var i=0; i<tree["specificdays"].length; i++) {
        
        if ( tree["specificdays"][i]==null )
          continue;
        
        parentNode.appendChild(dom.createTextNode("\n"+indent));
        var node = dom.createElement("date");
        node.setAttribute("date", DOMManager.numberToDate(tree["specificdays"][i]));
        
        if ( tree["specificdays_dates"]!=null ) {
          
          dateElem = tree["specificdays_dates"][tree["specificdays"][i]];
          
          if ( dateElem!=null && dateElem.length > 0 ) {
            for (var p=0; p<dateElem.length; p++) {
              
              if ( dateElem[p]==null ) // Periode wurde evtl. wegen <at> entfernt
                continue;
              
              node.appendChild(dom.createTextNode("\n  "+indent));
              period = dom.createElement("period");
              DOMManager.setPeriod(period, dateElem[p]);
              node.appendChild(period);
            }
            node.appendChild(dom.createTextNode("\n"+indent));
          }
        }
        
        parentNode.appendChild(node);
      }
    }
}

DOMManager.updateDOM_specificWeekdays = function( dom, tree, parentNode, indent ) {
    
    if ( tree["specific_weekdays"]!=null &&
         tree["specific_weekdays"].days!=null &&
         tree["specific_weekdays"].days.length > 0 ) {
      
      var monthdays = null;
      if ( parentNode.getElementsByTagName("monthdays").length == 0 ) {
        monthdays = dom.createElement("monthdays");
        parentNode.appendChild(dom.createTextNode("\n"+indent));
        parentNode.appendChild(monthdays);
      } else {
        monthdays = parentNode.getElementsByTagName("monthdays")[0];
      }
      var days = tree["specific_weekdays"].days;
      
      for (var i=0; i<days.length; i++) {
        
        var parts = days[i].split('.'); // z.B. "-1.monday"
        
        var neu = dom.createElement("weekday")
        neu.setAttribute("day", parts[1]);
        neu.setAttribute("which", parts[0]);
        
        var periods = tree["specific_weekdays"][days[i]];
        
        for (var p=0; p<periods.length; p++) {
          var period = dom.createElement("period");
          DOMManager.setPeriod(period, periods[p]);
          neu.appendChild(dom.createTextNode("\n    "+indent));
          neu.appendChild(period);
        }
        
        if ( periods.length > 0 )
          neu.appendChild(dom.createTextNode("\n  "+indent));
        
        if ( monthdays.lastChild!=null && monthdays.lastChild.nodeType==3 )
          monthdays.appendChild(dom.createTextNode(indent));  // es existiert bereits ein Textknoten von monthdays
        else
          monthdays.appendChild(dom.createTextNode("\n  "+indent));
        
        monthdays.appendChild(neu);
      }
      
      if ( days.length > 0 )
          monthdays.appendChild(dom.createTextNode("\n"+indent));
    }
}

/**
 * Hilfsfunktion fuer updateDOM
 * Erzeugt das Element <weekdays>, <monthdays> oder <ultimos> und haengt es in den run_time-Knoten
 * dom : dom_document, wichtig fuer createElement()
 * runtimeNode : Knoten run_time
 * elementName : Name des zu erstellenden Elements
 * groupA : assoziatives Array der Tage ( tree["weekdays"], tree["monthdays"] oder tree["ultimos"] )
 * dataA  : assoziatives Array der Tage ( tree["weekdays_items"], tree["monthdays_items"] oder tree["ultimos_items"] )
 */
DOMManager.updateDOM_daylist = function(dom, parentNode, elementName, groupA, dataA, indent) {
  
  try {
    
    if ( groupA != null ) {
      
      var elem;
      var node;
      var first = true;
      
      for (var entry in groupA) {
        if( first ) {
          if ( Editor.xmlBlankLines ) parentNode.appendChild(dom.createTextNode("\n"));
          parentNode.appendChild(dom.createTextNode("\n"+indent));
          elem = dom.createElement(elementName);
          first = false;
        }
        elem.appendChild(dom.createTextNode("\n  "+indent));
        node = dom.createElement("day");
        if( elementName == "weekdays" ) node.setAttribute("day", entry.toString().replace(/7/g,"0").split(" ").sort(Editor.NumSort).join(" "));
        else node.setAttribute("day", entry);
        
        if ( dataA && dataA[entry] && dataA[entry].length > 0 ) {
          var period;
          for (var p=0; p<dataA[entry].length; p++  ) {
            node.appendChild(dom.createTextNode("\n    "+indent));
            period = dom.createElement("period");
            DOMManager.setPeriod(period, dataA[entry][p]);
            node.appendChild(period);
          }
          node.appendChild(dom.createTextNode("\n  "+indent));
        }
        
        elem.appendChild(node);
      }
      if(!first) {
        elem.appendChild(dom.createTextNode("\n"+indent));
        parentNode.appendChild(elem);
      }
    }
    
  } catch(x) {
    msg = ( Editor.debug ) ? "DOMManager.updateDOM_daylist():\n" : "";
    throw new Error(msg + x.message);
  }
}


/***************************************************************************************************************************
 **  Class Editor  *********************************************************************************************************
 ***************************************************************************************************************************/

Editor                                      = function() {}
                                            
Editor.debug                                = false; // Editor im Debugmodus
Editor.xmlBlankLines                        = false; // Leerzeilen zwischen den Kind-Tags von run_time
                                            
Editor.remote                               = null;
Editor.isStarted                            = false;
Editor.mode                                 = "";
Editor.root                                 = "run_time";
Editor.root_display                         = "Run Time";
Editor.mode_index                           = "";
Editor.cbFunction                           = null;
// Referenz auf den aktuellen Teilbaum fuer das Formular period
Editor.period_currentPeriodTree             = null;
// Referenz auf die aktuelle Periode im Formular period
Editor.period_currentPeriod                 = null;
// ist die aktuelle Periode valide, so dass sie zum Baum hinzugefuegt werden darf
Editor.period_currentPeriod_isAttachable    = false;
Editor.dateform_dates                       = null;
Editor.dateform_dates_ass                   = null;
Editor.periodlist_minEntries                = 20; // minimale Anzahl der Eintraege (freie Plaetze, nicht Perioden !)
Editor.periodlist_highlightedRow            = -1;
Editor.periodlist_entries                   = 0;
Editor.periodlist_periods                   = 0;
Editor.naviTree                             = null;
Editor.dyedTreeElement                      = null;
Editor.imgDir                               = "editor/";
Editor.hasRunOptions                        = true;
Editor.hasSubstituteAttribute               = true;
Editor.calendar                             = {src:'',div:'',imgDir:'',skin:'.'};
Editor.schedules                            = [];
Editor.naviRootImg                          = null;
Editor.ie                                   = (navigator.userAgent.match(/\bMSIE\b/));
Editor.ff_version                           = Editor.ie ? 0 : 1*navigator.userAgent.replace(/.*Firefox\/(\d).*/i,"$1"); 
                                            
// Editor Farben                            
Editor.bgColor                              = '#ECE9D8';

// innerHTML fuer das gui-div
Editor.gui                                  = "";
                                            
Editor.attribute_indices                    = ["begintime_hh","begintime_mm","begintime_ss","endtime_hh","endtime_mm","endtime_ss","repeattime_hh","repeattime_mm","repeattime_ss","absoluterepeat_hh","absoluterepeat_mm","absoluterepeat_ss","singlestart_hh","singlestart_mm","singlestart_ss"];
Editor.when_holiday_options                 = {'previous_non_holiday':'previous non holiday','next_non_holiday':'next non holiday','suppress':'suppress execution (default)','ignore_holiday':'ignore holiday'};
Editor.when_holiday_list                    = {'previous_non_holiday':'previous...','next_non_holiday':'next...','suppress':'suppress...','ignore_holiday':'ignore...'};
                                            
Editor.update_groupping                     = null;
Editor.weekdays                             = {1:"Monday",2:"Tuesday",3:"Wednesday",4:"Thursday",5:"Friday",6:"Saturday",7:"Sunday"};
Editor.holiday_weekdays                     = Editor.weekdays;
Editor.monthdays                            = {1:"1st",2:"2nd",3:"3rd",4:"4th",5:"5th",6:"6th",7:"7th",8:"8th",9:"9th",10:"10th",11:"11th",12:"12th",13:"13th",14:"14th",15:"15th",16:"16th",17:"17th",18:"18th",19:"19th",20:"20th",21:"21st",22:"22nd",23:"23rd",24:"24th",25:"25th",26:"26th",27:"27th",28:"28th",29:"29th",30:"30th",31:"31st"};
Editor.ultimos                              = {0:"last day",1:"1 day",2:"2 days",3:"3 days",4:"4 days",5:"5 days",6:"6 days",7:"7 days",8:"8 days",9:"9 days",10:"10 days",11:"11 days",12:"12 days",13:"13 days",14:"14 days",15:"15 days",16:"16 days",17:"17 days",18:"18 days",19:"19 days",20:"20 days",21:"21 days",22:"22 days",23:"23 days",24:"24 days",25:"25 days",26:"26 days",27:"27 days",28:"28 days",29:"29 days",30:"30 days"};
Editor.month                                = {1:"January",2:"February",3:"March",4:"April",5:"May",6:"June",7:"July",8:"August",9:"September",10:"October",11:"November",12:"December"};
                                            
Editor.map_weekdays                         = {"monday":1,"tuesday":2,"wednesday":3,"thursday":4,"friday":5,"saturday":6,"sunday":7};
Editor.map_month                            = {"january":1,"february":2,"march":3,"april":4,"may":5,"june":6,"july":7,"august":8,"september":9,"october":10,"november":11,"december":12};
Editor.cur_month                            = "";

Editor.map_specific_weekdays                = new Object();
Editor.map_specific_weekdays["1"]           = "First";
Editor.map_specific_weekdays["2"]           = "Second";
Editor.map_specific_weekdays["3"]           = "Third";
Editor.map_specific_weekdays["4"]           = "Fourth";
Editor.map_specific_weekdays["-1"]          = "Last";
Editor.map_specific_weekdays["-2"]          = "Second Last";
Editor.map_specific_weekdays["-3"]          = "Third Last";
Editor.map_specific_weekdays["-4"]          = "Fourth Last";
Editor.map_specific_weekdays["monday"]      = "Monday";
Editor.map_specific_weekdays["tuesday"]     = "Tuesday";
Editor.map_specific_weekdays["wednesday"]   = "Wednesday";
Editor.map_specific_weekdays["thursday"]    = "Thursday";
Editor.map_specific_weekdays["friday"]      = "Friday";
Editor.map_specific_weekdays["saturday"]    = "Saturday";
Editor.map_specific_weekdays["sunday"]      = "Sunday";
Editor.map_specific_weekdays["First"]       = "1";
Editor.map_specific_weekdays["Second"]      = "2";
Editor.map_specific_weekdays["Third"]       = "3";
Editor.map_specific_weekdays["Fourth"]      = "4";
Editor.map_specific_weekdays["Last"]        = "-1";
Editor.map_specific_weekdays["Second Last"] = "-2";
Editor.map_specific_weekdays["Third Last"]  = "-3";
Editor.map_specific_weekdays["Fourth Last"] = "-4";
Editor.map_specific_weekdays["Monday"]      = "monday";
Editor.map_specific_weekdays["Tuesday"]     = "tuesday";
Editor.map_specific_weekdays["Wednesday"]   = "wednesday";
Editor.map_specific_weekdays["Thursday"]    = "thursday";
Editor.map_specific_weekdays["Friday"]      = "friday";
Editor.map_specific_weekdays["Saturday"]    = "saturday";
Editor.map_specific_weekdays["Sunday"]      = "sunday";


Editor.getHTML = function() {
  
  var h = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">\n';
  h+= '<html>\n';
  h+= '  <head>\n';
  h+= '    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>\n';
  h+= '    <title>Job Editor</title>\n';
  h+= '    \n';
  h+= '    <script type="text/javascript">\n';
  h+= '      \n';
  
  h+= '      function add_option_tag(name, value, select_id) {\n';
  h+= '        \n';
  h+= '        try {\n';
  h+= '          var obj = (typeof select_id == "object") ? select_id : document.getElementById(select_id);';
  h+= '          var objLength = obj.length;\n';
  h+= '          obj.options[ objLength ] = new Option(name,value,false,false);\n';
  h+= '          return objLength;\n';
  h+= '        } catch(e) { throw new Error("add_option_tag(): " + e.message) }\n';
  h+= '      }\n';
  h+= '      \n';
  
  h+= '      function add_option_tags(names, values, node) {\n';
  h+= '       try {\n';
  h+= '         var newOpt;\n';
  h+= '         for (var i=0; i<names.length; i++) {\n';
  h+= '           newOpt = new Option(names[i],values[i],false,false);\n';
  h+= '           node.options[ node.length ] = newOpt;\n';
  h+= '         }\n';
  h+= '       } catch(e) {\n';
  h+= '         throw new Error("add_option_tags(): " + e.message)\n';
  h+= '       }\n';
  h+= '      }\n';
  
  h+= '      \n';
  h+= '      function Alert(val) {\n';
  h+= '        \n';
  h+= '        alert(val);\n';
  h+= '      }\n';
  h+= '      function alertError(func_name, err) {\n';
  h+= '        \n';
  h+= '        var msg = (typeof err == "object") ? err.message : err;\n';
  if(Editor.debug) h+= '        throw err;';
  else h+= '        alert(func_name + "\\n" + msg);\n';
  h+= '      }\n';
  h+= '      \n';
  h+= '    </scr'+'ipt>\n';
  h+= '    \n';
  h+= '    <style type="text/css">\n';
  h+= '      body { background-color:'+Editor.bgColor+'; font-family: "Arial", "Helvetica", sans-serif; color:black; font-size:12px; }\n';
  h+= '      a.tree                { text-decoration:none; font-weight:normal; cursor:pointer; }\n';
  h+= '      a.tree:link           { color:black; }\n';
  h+= '      a.tree:visited        { color:black; }\n';
  h+= '      a.tree:hover          { color:black; }\n';
  h+= '      a.tree:active         { color:black; }\n';
  if(Editor.ie) h+= '      hr                    { border-style:groove; }\n';
  h+= '      table                 { font-size:12px; }\n';
  h+= '      input,select,option   { font-family:Arial,Helvetica,Sans-Serif;margin:0px; }\n';
  h+= '      input                 { font-size:12px; height:20px; }\n';
  h+= '      select                { font-size:11px; height:'+(Editor.ff_version > 2 ? '20' : '18')+'px; }\n';
  h+= '      select.multiple       { font-size:12px; }\n';
  h+= '      option                { font-size:11px; }\n'; 
  h+= '      fieldset              { padding:'+(Editor.ie?'1':'0')+'px 4px 4px 4px; }\n';
  h+= '      td.period             { padding:0px 2px;font-size:11px;height:14px;line-height:14px;border:'+(Editor.ie?'':'threedface ')+'solid 1px; }\n';
  h+= '      .period_inputtext     { width:100%; font-size:11px; }\n';
  h+= '      .button               { width:80px; font-size:11px; }\n';
  h+= '      .dateform_button      { width:110px; font-size:11px; }\n';
  h+= '      .entrylist_button     { width:110px; font-size:11px; }\n';
  h+= '      .daylist_button       { width:110px; font-size:11px; }\n';
  h+= '      .specdays_button      { width:110px; font-size:11px; }\n';
  h+= '      .periodlist_button    { width:110px; font-size:11px; }\n';
  h+= '      .run_option_button    { width:90px; font-size:11px; }\n';
  h+= '      .run_option_caption   { position:absolute; top:0; left:8; height:15; background-color:'+Editor.bgColor+'; text-align:center; padding-left:3px; padding-right:3px; }\n';
  h+= '    </style>\n';
  
  h+= '  </head>\n';
  h+= '  \n';
  h+= '  <body onload="self.opener.Editor.initGUI();"><form id="editor_form" name="editor_form" method="post" onsubmit="return self.opener.Editor.ok();">\n';
  h+= '    <div id="sos_runtime_editor_tree" style="padding-top:2; padding-left:5; position:absolute; top:10; left:10; width:180; height:508; overflow:auto; background-color:white;"></div>\n';
  h+= '    <div style="position:absolute; top:528; left:10; width:698; height:20; text-align:right;">\n';
  h+= '      <input class="button" id="ok" name="ok" type="submit"  style="margin-right:5; vertical-align:top;" value="OK"/>\n';
  h+= '      <input name="cancel" class="button" type="button" style="margin-left:5;" value="Cancel" onclick="window.close();return true;"/>\n';
  h+= '    </div>\n';
  h+= '    <div id="gui" style="position:absolute; top:10; left:200; width:508; height:508;"></div>\n';
  if(Editor.calendar.div != '') h+= '    <script type="text/javascript" src="'+Editor.calendar.div+'"></scr'+'ipt>\n';
  if(Editor.calendar.src != '') h+= '    <script type="text/javascript" src="'+Editor.calendar.src+'"></scr'+'ipt>\n';
  h+= '  </form></body>\n';
  h+= '</html>\n';
  return h;
}


Editor.initGUI = function() {
  
  if (Editor.ie) {
    Editor.remote.document.getElementById("sos_runtime_editor_tree").style.border = "groove 2px";
  } else {
    Editor.remote.document.getElementById("sos_runtime_editor_tree").style.left = 9;
    Editor.remote.document.getElementById("sos_runtime_editor_tree").style.width = 172;
    Editor.remote.document.getElementById("sos_runtime_editor_tree").style.height = 502;
    Editor.remote.document.getElementById("sos_runtime_editor_tree").style.border = "threedface groove 2px";
  }

  if(Editor.remote["SOS_Div"]) {
    Editor.remote.SOS_Div.moveInit();
  }
  if(Editor.remote["SOS_Calendar"]) {
    Editor.remote.SOS_Calendar.imgDir          = Editor.calendar.imgDir;
    Editor.remote.SOS_Calendar.language        = "en";
    Editor.remote.SOS_Calendar.fullYear        = true;
    Editor.remote.SOS_Calendar.returnISO       = true;
    Editor.remote.SOS_Calendar.onFillForm      = "self.opener.Editor.listen_valids(SOS_Calendar.formElement.split('.')[1]);";
    Editor.remote.SOS_Calendar.showHolidays    = false;
    Editor.remote.SOS_Calendar.indent          = 8;
    Editor.remote.SOS_Calendar.bgSaturday      = "white";
    Editor.remote.SOS_Calendar.bgSunday        = "white";
    Editor.remote.SOS_Calendar.bgDayCurrent    = "";
    Editor.remote.SOS_Calendar.bgDaySelected   = "#F2F4F7";
    Editor.remote.SOS_Calendar.borderDays      = "";
    Editor.remote.SOS_Calendar.skin            = Editor.calendar.skin;
    Editor.remote.SOS_Calendar.fullDatetime    = false;
  }  
}


Editor.isClosed = function() {

  return ( Editor.remote==null || Editor.remote.closed );
}

Editor.ok = function() {
  
  if( Editor.root == 'schedule' && !Editor.plausi_valids() ) return false;
  
  var run_time_applied = true;
  
  // zuerst run_time updaten, falls etwas geaendert wurde
  if ( Editor.mode=="run_time" )
    run_time_applied = Editor.periodlist_action_apply_period(null);
  
  if ( run_time_applied ) {

    try {

      DOMManager.updateDOM(DOMManager.dom, DOMManager.tree);

      if ( Editor.cbFunction != null )
        Editor.cbFunction();
        
      SOS_Simple_Tree.root = null;
      DOMManager.dom       = null;
      DOMManager.tree      = null;

    } catch(e) {
      throw e;
      msg = ( Editor.debug ) ? "Editor.ok():" : "";
      Editor.remote.alertError(msg, e);
    }
  }
  
  // Editor schliessen
  Editor.remote.close();
}

Editor.ok_set_enabled = function(state) {

  Editor.remote.document.getElementById("ok").disabled = (state) ? "" : "disabled";
}


Editor.setCallback = function(func) {

  if ( typeof func != 'function' )
    throw new Error("Editor.setCallback(f): parameter [f] has to be a function");

  Editor.cbFunction = func;

  return true;
}


Editor.close = function() {
  
  try {
    
    if ( !Editor.isClosed() ) {
      Editor.remote.close();
    }
    
    return true;
      
  } catch(e) {
    msg = ( Editor.debug ) ? "Editor.close():\n" : "";
    alert(msg + "Cannot close Editor:\n" + e);
    return false;
  }
}


Editor.start = function(xmlString) {
  
  var opts = "location=no, menubar=no, resizable=no, scrollbars=no, status=no, toolbar=no, top=150, left=200, width=717, height=557";
  
  if ( Editor.isClosed() ) {
    
    Editor.isStarted = false;
    
    // Initialisierung der Variablen
    Editor.mode = "";
    Editor.mode_index = "";
    if(Editor.root == 'schedule') Editor.root_display = "Schedule";
    
    // Referenz auf den aktuellen Teilbaum fuer das Formular period
    Editor.period_currentPeriodTree = null;
    
    // Referenz auf die aktuelle Periode im Formular period
    Editor.period_currentPeriod = null;
    
    // Referenz auf die aktuelle Periode im Formular period
    Editor.period_currentPeriod_isAttachable = false;
    
    Editor.dateform_dates = null;
    Editor.dateform_dates_ass = null;
    Editor.periodlist_highlightedRow = -1;
    Editor.periodlist_entries = 0;
    Editor.periodlist_periods = 0;
    Editor.naviTree = null;
    Editor.dyedTreeElement = null;
    
    try {
      
      DOMManager.init(xmlString);
      
      Editor.remote = window.open("dummy.html", "RuntimeEditor", opts);  //dummy.html instead of about:blank because about:blank supports no https
      
      Editor.remote.document.open();
      Editor.remote.document.write(Editor.getHTML());
      Editor.remote.document.close();
      
      Editor.NaviTreeInit(Editor.remote.document.getElementById("sos_runtime_editor_tree"), DOMManager.tree);
      
      Editor.showRuntime();
      
      // runtime im Tree oeffnen
      Editor.NaviRootImgDisplay(false);
    
      Editor.isStarted = true;
      
    } catch (x) {
      msg = ( Editor.debug ) ? "Editor.start():\n" : "";
      alert(msg + "Cannot start Editor:\n" + x.message);
      return false;
    }
  }
  
  Editor.remote.focus();
  return true;
}


Editor.dyeTreeElement = function(elem) {

  try {

    if ( Editor.dyedTreeElement!=null ) {
      Editor.dyedTreeElement.style.backgroundColor = "white";
    }

    if ( elem!=null )
      elem.style.backgroundColor = Editor.bgColor;
    
    Editor.dyedTreeElement = elem;

  } catch(x) {
    throw new Error("Editor.dyeTreeElement(): " + x.message);
  }
}


Editor.clearGUI = function() {
  
  try {
    
    Editor.mode = "cleared";
    Editor.remote.document.getElementById("gui").innerHTML = "";
    
  } catch(x) {
    throw new Error("Editor.clearGUI(): " + x.message);
  }
}


Editor.clearInputBgColor = function(elem) {
  if( Editor.ie ) elem.style.removeAttribute('backgroundColor');
  else elem.style.backgroundColor = 'white';
} 


Editor.NaviTreeInit = function(target, tree) {

  try {

    Editor.naviTree = new Object();

    // setzen des Image-Verzeichnisses
    SOS_Simple_Tree.imgDir = Editor.imgDir;

    // Wurzel erstellen, ohne Bild (param showImage=false )
    root_job = SOS_Simple_Tree.createRoot(target, '&nbsp;', Editor.remote.document, false);

    if ( Editor.hasRunOptions )
      SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showRunOptions();">RunOptions</a>', root_job, false, Editor.remote.document, '0px');

    root = SOS_Simple_Tree.appendChild('<a id="navi_run_time" class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showRuntime();">'+Editor.root_display+'</a>', root_job, false, Editor.remote.document, '0px');

    //root = SOS_Simple_Tree.createRoot(target, '<a id="navi_run_time" class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showRuntime();">'+Editor.root_display+'</a>', Editor.remote.document);

    // Blaetter einfuegen
    SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'everyday\');">Everyday</a>', root, false, Editor.remote.document);
    Editor.naviTree["weekdays"]     = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showWeekdays();">Weekdays</a>', root, false, Editor.remote.document);
    Editor.naviTree["monthdays"]    = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showMonthdays();">Monthdays</a>', root, false, Editor.remote.document);
    Editor.naviTree["ultimos"]      = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showUltimos();">Ultimos</a>', root, false, Editor.remote.document);
    
    Editor.naviTree["specific_weekdays"]      = new Object();
    Editor.naviTree["specific_weekdays"].root = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showSpecificWeekdays();">Specific Weekdays</a>', root, false, Editor.remote.document);
    
    Editor.naviTree["specificdays"] = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showSpecificDays(\'specificdays\');">Specific Days</a>', root, false, Editor.remote.document);
    Editor.naviTree["holidays"]     = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showSpecificDays(\'holidays\');">Holidays</a>', root, false, Editor.remote.document);
    Editor.naviTree.month           = new Object();
    Editor.naviTree.month.root      = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showMonth();">Specific Month</a>', root, false, Editor.remote.document);
    
    // ROOT-Knoten aufmachen
    SOS_Simple_Tree.openRoot();
    
    // weekdays
    if ( tree["weekdays"] ) {
      for (var entry in tree["weekdays"]) {
        var entries = entry.split(" ");
        for(var i=0; i < entries.length; i++ ) entries[i] = Editor.weekdays[ entries[i] ].substr(0,2);
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'weekdays\' ,\''+ entry +'\');">'+ ((entries.length == 1) ? Editor.weekdays[ entry ] : entries.join(" ")) +'</a>', Editor.naviTree["weekdays"], true, Editor.remote.document);
      }
    }
    
    // monthdays
    if ( tree["monthdays"]!=null ) {
      for (var entry in tree["monthdays"])
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'monthdays\' ,\''+ entry +'\');">'+ ((entry.split(" ").length == 1) ? Editor.monthdays[ entry ] : entry) +'</a>', Editor.naviTree["monthdays"], true, Editor.remote.document);
    }
    
    // ultimos
    if ( tree["ultimos"]!=null ) {
      for (var entry in tree["ultimos"])
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'ultimos\' ,\''+ entry +'\');">'+ ((entry.split(" ").length == 1) ? Editor.ultimos[ entry ] : entry) +'</a>', Editor.naviTree["ultimos"], true, Editor.remote.document);
    }
    
    // specific weekdays
    if ( tree["specific_weekdays"]!=null && tree["specific_weekdays"].days!=null ) {
      
      for (var i=0; i<tree["specific_weekdays"].days.length; i++) {
        
        var components     = tree["specific_weekdays"].days[i].split('.');
        var whichComponent = components[0];
        var dayComponent   = components[1];
        whichComponent     = Editor.map_specific_weekdays[whichComponent];
        dayComponent       = Editor.map_specific_weekdays[dayComponent];
        
        if ( Editor.naviTree["specific_weekdays"][dayComponent]==null ) {
          Editor.naviTree["specific_weekdays"][dayComponent] = SOS_Simple_Tree.appendChild( '<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">' + dayComponent + '</a>', Editor.naviTree["specific_weekdays"].root, true, Editor.remote.document );
        }
        // Knoten des Tages existiert jetzt auf jeden Fall
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'specific_weekdays\' ,\''+ tree["specific_weekdays"].days[i] +'\');">'+ whichComponent +'</a>', Editor.naviTree["specific_weekdays"][dayComponent], true, Editor.remote.document);
      }
    }
    
    // specificdays
    if ( tree["specificdays"]!=null ) {
      for (var i=0; i<tree["specificdays"].length; i++)
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.showDay(\'specificdays\' ,\''+ tree["specificdays"][i] +'\');">'+ DOMManager.numberToDate(tree["specificdays"][i]) +'</a>', Editor.naviTree["specificdays"], true, Editor.remote.document);
    }
    
    // month
    if ( tree.month != null ) {
      for (var entry in tree.month) {
        var entries = entry.split(" ");
        for(var i=0; i < entries.length; i++ ) entries[i] = Editor.month[ entries[i] ].substr(0,3);
        Editor.naviTree.month[entry] = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">'+ ((entries.length == 1) ? Editor.month[ entry ] : entries.join(" ")) +'</a>', Editor.naviTree.month.root, true, Editor.remote.document);
        SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'everyday\',null,\'month\');">Everyday</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
        Editor.naviTree.month[entry].weekdays     = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showWeekdays(\'month\');">Weekdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
        Editor.naviTree.month[entry].monthdays    = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showMonthdays(\'month\');">Monthdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
        Editor.naviTree.month[entry].ultimos      = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showUltimos(\'month\');">Ultimos</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
        //Editor.naviTree.month[entry].specific_weekdays      = new Object();
        //Editor.naviTree.month[entry].specific_weekdays.root = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showSpecificWeekdays(\'month\');">Spec. Weekdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
        //Editor.naviTree.month[entry].specificdays = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showSpecificDays(\'specificdays\',\'month\');">Specific Days</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
    
        if ( tree.month[entry].weekdays ) {
          for (var day in tree.month[entry].weekdays) {
            var days = day.split(" ");
            for(var i=0; i < days.length; i++ ) days[i] = Editor.weekdays[ days[i] ].substr(0,2);
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'weekdays\' ,\''+ day +'\',\'month\');">'+ ((days.length == 1) ? Editor.weekdays[ day ] : days.join(" ")) +'</a>', Editor.naviTree.month[entry].weekdays, true, Editor.remote.document);
          }
        }
        if ( tree.month[entry].monthdays ) {
          for (var day in tree.month[entry].monthdays)
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'monthdays\' ,\''+ day +'\',\'month\');">'+ ((day.split(" ").length == 1) ? Editor.monthdays[ day ] : day) +'</a>', Editor.naviTree.month[entry].monthdays, true, Editor.remote.document);
        }
        if ( tree.month[entry].ultimos ) {
          for (var day in tree.month[entry].ultimos)
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'ultimos\' ,\''+ day +'\',\'month\');">'+ ((day.split(" ").length == 1) ? Editor.ultimos[ day ] : day) +'</a>', Editor.naviTree.month[entry].ultimos, true, Editor.remote.document);
        }
        /*
        if ( tree.month[entry].specific_weekdays && tree.month[entry].specific_weekdays.days ) {
      
          for (var i=0; i<tree.month[entry].specific_weekdays.days.length; i++) {
            var components     = tree.month[entry].specific_weekdays.days[i].split('.');
            var whichComponent = components[0];
            var dayComponent   = components[1];
            whichComponent     = Editor.map_specific_weekdays[whichComponent];
            dayComponent       = Editor.map_specific_weekdays[dayComponent];
            
            if ( Editor.naviTree.month[entry].specific_weekdays[dayComponent]==null ) {
              Editor.naviTree.month[entry].specific_weekdays[dayComponent] = SOS_Simple_Tree.appendChild( '<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">' + dayComponent + '</a>', Editor.naviTree.month[entry].specific_weekdays.root, true, Editor.remote.document );
            }
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'specific_weekdays\' ,\''+ tree.month[entry].specific_weekdays.days[i] +'\',\'month\');">'+ whichComponent +'</a>', Editor.naviTree.month[entry].specific_weekdays[dayComponent], true, Editor.remote.document);
          }
        }
        if ( tree.month[entry].specificdays ) {
          for (var i=0; i<tree.month[entry].specificdays.length; i++)
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'specificdays\' ,\''+ tree.month[entry].specificdays[i] +'\',\'month\');">'+ DOMManager.numberToDate(tree.month[entry].specificdays[i]) +'</a>', Editor.naviTree.month[entry].specificdays, true, Editor.remote.document);
        } */
      }
    }
    
    Editor.naviRootImg = root.getElementsByTagName('img')[0];
    
  } catch (x) {
    throw new Error("\nEditor.NaviTreeInit():\n" + x.message);
  }
}


Editor.NaviRootImgDisplay = function(toggle) {
  
  if( DOMManager.tree["scheduleA"] != "" ) {
    if( Editor.ie && Editor.remote.document.getElementById("scheduler_hint_ie") ) {
      Editor.remote.document.getElementById("scheduler_hint_ie").style.display = "block";
    }
    if( Editor.remote.document.getElementById("scheduler_hint") ) {
      Editor.remote.document.getElementById("scheduler_hint").style.display = "block";
    }
    Editor.naviRootImg.onclick = function() {
      Editor.remote.Alert("You must clear the schedule attribute\nbefore other run times are supported.");
      if( Editor.remote.document.getElementById("scheduleA") ) {
        Editor.remote.document.getElementById("scheduleA").focus();
        Editor.remote.document.getElementById("scheduleA").select();
      }
    }
    if( toggle ) SOS_Simple_Tree.display(Editor.naviRootImg);
  } else {
    Editor.naviRootImg.onclick = new Function('SOS_Simple_Tree.display(this);');
    SOS_Simple_Tree.display(Editor.naviRootImg);
    if( Editor.ie && Editor.remote.document.getElementById("scheduler_hint_ie") ) {
      Editor.remote.document.getElementById("scheduler_hint_ie").style.display = "none";
    }
    if( Editor.remote.document.getElementById("scheduler_hint") ) {
      Editor.remote.document.getElementById("scheduler_hint").style.display = "none";
    }
  }
}


Editor.NaviTreeRemoveSubtree = function(parentnode) {

  try {

    var ul = parentnode.getElementsByTagName('ul')[0];

    // Unterelemente loeschen, wenn es welche gibt
    if ( ul != null ) {

      var li;
      for (var i=ul.childNodes.length-1; i>=0; i--) {
        li = ul.childNodes[i];
        ul.removeChild(li);
      }

      parentnode.removeChild(ul);

      var img = parentnode.getElementsByTagName('img')[0];
      if ( img != null ) {

        img.src = SOS_Simple_Tree.imgDir+SOS_Simple_Tree.imgBlank;
        img.style.cursor  = 'auto';
      }
    }

  } catch(x) {
    throw new Error("Editor.NaviTreeRemoveSubtree():\n" + x.message);
  }
}

Editor.NaviTreeUpdate = function(key, specifier) {

  try {
    if( specifier == "holiday_weekdays" || specifier == "holidays" ) return;
    var tree       = DOMManager.tree;
    var parentnode = Editor.naviTree[specifier];
    var set_cur    = "";
    var call_key   = null;
    if(key == "month") {
      tree         = tree[key][Editor["cur_"+key]];
      parentnode   = Editor.naviTree[key][Editor["cur_"+key]][specifier];
      set_cur      = 'self.opener.Editor[\'cur_'+key+'\']=\''+Editor['cur_'+key]+'\';';
      call_key     = '\''+key+'\'';
    }
    if( !tree[specifier] ) return false;
    if( specifier == "specific_weekdays" && !tree[specifier].days ) return false;
    
    /* Elemente einfuegen: */
    switch(specifier) {

      case "ultimos":
      case "monthdays":
      case "weekdays":
          Editor.NaviTreeRemoveSubtree(parentnode);
          for (var entry in tree[specifier]) {
            var entries = entry.split(" ");
            if( specifier == "weekdays" ) {
              for(var i=0; i < entries.length; i++) entries[i] = Editor.weekdays[entries[i]].substr(0,2);
            }
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);'+set_cur+'self.opener.Editor.showDay(\''+ specifier +'\' ,\''+ entry +'\' ,'+ call_key +');">'+ ((entries.length == 1) ? tree[specifier][entry] : entries.join(" ")) +'</a>', parentnode, true, Editor.remote.document);
          }
      break;

      case "specificdays":
          Editor.NaviTreeRemoveSubtree(parentnode);
          for (var i=0; i<tree["specificdays"].length; i++)
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);'+set_cur+'self.opener.Editor.showDay(\''+ specifier +'\' ,\''+ tree[specifier][i] +'\' ,'+ call_key +');">'+ DOMManager.numberToDate(tree[specifier][i]) +'</a>', parentnode, true, Editor.remote.document);
      break;
      
      case "month":
          var parentnode1 = parentnode.root;
          parentnode      = new Object();
          parentnode.root = parentnode1;
          Editor.NaviTreeRemoveSubtree(parentnode1);
          for (var entry in tree.month) {
            var entries     = entry.split(" ");
            for(var i=0; i < entries.length; i++ ) {
              entries[i] = Editor.month[ entries[i] ].substr(0,3);
            }
            Editor.naviTree.month[entry] = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">'+ ((entries.length == 1) ? Editor.month[ entry ] : entries.join(" ")) +'</a>', Editor.naviTree.month.root, true, Editor.remote.document);
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'everyday\',null,\'month\');">Everyday</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
            Editor.naviTree.month[entry].weekdays     = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showWeekdays(\'month\');">Weekdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
            Editor.naviTree.month[entry].monthdays    = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showMonthdays(\'month\');">Monthdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
            Editor.naviTree.month[entry].ultimos      = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showUltimos(\'month\');">Ultimos</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
            //Editor.naviTree.month[entry].specific_weekdays      = new Object();
            //Editor.naviTree.month[entry].specific_weekdays.root = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showSpecificWeekdays(\'month\');">Spec. Weekdays</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
            //Editor.naviTree.month[entry].specificdays = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showSpecificDays(\'specificdays\',\'month\');">Specific Days</a>', Editor.naviTree.month[entry], false, Editor.remote.document);
          
            if ( tree.month[entry].weekdays ) {
              for (var day in tree.month[entry].weekdays) {
                var days = day.split(" ");
                for(var i=0; i < days.length; i++ ) days[i] = Editor.weekdays[ days[i] ].substr(0,2);
                SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'weekdays\' ,\''+ day +'\',\'month\');">'+ ((days.length == 1) ? Editor.weekdays[ day ] : days.join(" ")) +'</a>', Editor.naviTree.month[entry].weekdays, true, Editor.remote.document);
              }
            }
            if ( tree.month[entry].monthdays ) {
              for (var day in tree.month[entry].monthdays)
                SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'monthdays\' ,\''+ day +'\',\'month\');">'+ ((day.split(" ").length == 1) ? Editor.monthdays[ day ] : day) +'</a>', Editor.naviTree.month[entry].monthdays, true, Editor.remote.document);
            }
            if ( tree.month[entry].ultimos ) {
              for (var day in tree.month[entry].ultimos)
                SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'ultimos\' ,\''+ day +'\',\'month\');">'+ ((day.split(" ").length == 1) ? Editor.ultimos[ day ] : day) +'</a>', Editor.naviTree.month[entry].ultimos, true, Editor.remote.document);
            }
            /*
            if ( tree.month[entry].specific_weekdays && tree.month[entry].specific_weekdays.days ) {
          
              for (var i=0; i<tree.month[entry].specific_weekdays.days.length; i++) {
                var components     = tree.month[entry].specific_weekdays.days[i].split('.');
                var whichComponent = components[0];
                var dayComponent   = components[1];
                whichComponent     = Editor.map_specific_weekdays[whichComponent];
                dayComponent       = Editor.map_specific_weekdays[dayComponent];
                
                if ( Editor.naviTree.month[entry].specific_weekdays[dayComponent]==null ) {
                  Editor.naviTree.month[entry].specific_weekdays[dayComponent] = SOS_Simple_Tree.appendChild( '<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">' + dayComponent + '</a>', Editor.naviTree.month[entry].specific_weekdays.root, true, Editor.remote.document );
                }
                SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'specific_weekdays\' ,\''+ tree.month[entry].specific_weekdays.days[i] +'\',\'month\');">'+ whichComponent +'</a>', Editor.naviTree.month[entry].specific_weekdays[dayComponent], true, Editor.remote.document);
              }
            }
            if ( tree.month[entry].specificdays ) {
              for (var i=0; i<tree.month[entry].specificdays.length; i++)
                SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.cur_month=\''+entry+'\';self.opener.Editor.showDay(\'specificdays\' ,\''+ tree.month[entry].specificdays[i] +'\',\'month\');">'+ DOMManager.numberToDate(tree.month[entry].specificdays[i]) +'</a>', Editor.naviTree.month[entry].specificdays, true, Editor.remote.document);
            }*/
          }
      break;
      
      case "specific_weekdays":
          var parentnode1 = parentnode.root;
          parentnode      = new Object();
          parentnode.root = parentnode1;
          Editor.NaviTreeRemoveSubtree(parentnode1);
          for (var i=0; i<tree["specific_weekdays"].days.length; i++) {
            
            var components     = tree["specific_weekdays"].days[i].split('.');
            var whichComponent = components[0];
            var dayComponent   = components[1];
            whichComponent     = Editor.map_specific_weekdays[whichComponent];
            dayComponent       = Editor.map_specific_weekdays[dayComponent];
            
            if ( Editor.naviTree["specific_weekdays"][dayComponent]==null ) {
              parentnode[dayComponent] = SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);self.opener.Editor.clearGUI();">'+ dayComponent+ '</a>',parentnode.root,true,Editor.remote.document);
            }
            // Knoten des Tages existiert jetzt auf jeden Fall
            SOS_Simple_Tree.appendChild('<a class="tree" onclick="self.opener.Editor.dyeTreeElement(this);'+set_cur+'self.opener.Editor.showDay(\'specific_weekdays\' ,\''+ tree["specific_weekdays"].days[i] +'\' ,'+ call_key +');">'+ whichComponent +'</a>', parentnode[dayComponent], true, Editor.remote.document);
          } 
          
      break;
      
      default:
      throw new Error('unknown specifier \''+specifier+'\'');
      break;
    }

  } catch(x) {
    throw new Error("Editor.NaviTreeUpdate():\n" + x.message);
  }
}


function debug(param,debugEl) {

  try {

      var newLine = (debugEl) ? '<br>' : "\n";
      var result  = 'Objekt: '+param+newLine+newLine;

      for(temp in param){
        if (typeof param[temp] != 'function') {
          var tempName = (debugEl) ? '<b>'+temp+'</b>' : temp;
          result += tempName +' : '+param[temp]+newLine;
        }
      }
      if(debugEl) { debugEl.innerHTML += result;  }
      else        { alert(result);                }

  } catch(x) {
    alert('ERROR debug(): '+x.message);
  }
}


Editor.getXML = function() {

  if ( Editor.isClosed() )
    return 0;

  else {
    try {
      return DOMManager.getXML();
    } catch(e) {
      Editor.remote.alertError("Editor.getXML(): Cannot provide XML-String", e);
    }
  }
}

Editor.showRuntime = function() {
  
  try {
    
    // Am Anfang Markierung setzen
    Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
    
    if ( Editor.mode=="run_time" ) {
      
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(null);
      }
    }
    
    Editor.mode = "run_time";
    Editor.ok_set_enabled( true );
    
    Editor.gui = '';
    Editor.gui+= '<fieldset>\n';
    Editor.gui+= '<legend>'+Editor.root_display+'</legend>\n';
    
    if( Editor.root == "schedule" ) {
      Editor.gui+= '<table width="100%" cellpadding="0" cellspacing="0" border="0">\n';
      Editor.gui+= '  <tr height="22">\n';
      Editor.gui+= '    <td>Title:</td>\n';
      if(Editor.calendar.src != "") {
        Editor.gui+= '    <td width="15">&nbsp;</td>\n';
      }
      Editor.gui+= '    <td width="400" colspan="2"><input style="width:100%;" id="title" name="title" onkeyup="self.opener.Editor.listen_input(this);" type="text"/></td>\n';
      Editor.gui+= '  </tr>\n';
      if( Editor.hasSubstituteAttribute ) {
        Editor.gui+= '  <tr height="22">\n';
        Editor.gui+= '    <td>Substitute:</td>\n';
        if(Editor.calendar.src != "") {
          Editor.gui+= '    <td width="15">&nbsp;</td>\n'
        }
        Editor.gui+= '    <td width="400" colspan="2">\n';
        Editor.gui+= '      <select id="schedules" name="schedules" size="1" style="width:100%;" onchange="var input=this.form.elements.substitute;input.value=this.value;this.selectedIndex=0;self.opener.Editor.listen_input(input);input.focus();">\n';
        Editor.gui+= '        <option style="font-size:0pt;height:0pt;background-color:white;" selected value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...to apply</option>\n';
        if(Editor.schedules.length == 0) {
          Editor.gui+= '        <option value="">No schedules found</option>\n';
        }
        for( var i=0; i < Editor.schedules.length; i++ ) {
          Editor.gui+= '        <option value="'+Editor.schedules[i]+'">'+Editor.schedules[i]+'</option>\n';
        }
        Editor.gui+= '      </select>\n';
        if(Editor.ie) Editor.gui+= '<iframe style="position:absolute;top:39px;left:100px;width:312px;height:20px;border:0px;"  src="about:blank"></iframe>\n';
        Editor.gui+= '      <div style="position:absolute;top:38px;left:'+(Editor.ie?98:96)+'px;width:314px;height:21px;"><input style="width:100%;" id="substitute" name="substitute" onkeyup="self.opener.Editor.listen_input(this);" type="text"/></div>\n';
        Editor.gui+= '    </td>\n'
        Editor.gui+= '  </tr>\n';
        Editor.gui+= '  <tr height="22">\n';
        Editor.gui+= '    <td>Valid from:</td>\n';
        if(Editor.calendar.src != "") {
          Editor.gui+= '    <td style="padding-right:2px;"><img src="'+Editor.calendar.imgDir+'icon_calendar.gif" alt="calendar" title="calendar" onclick="self.opener.Editor.show_calendar(\'valid_from\',116,event);" /></td>\n'
        }
        Editor.gui+= '    <td width="180"><input style="width:100%;" id="valid_from" name="valid_from" onkeyup="self.opener.Editor.listen_input(this);" onchange="return self.opener.Editor.extend_valids(this);" type="text"/></td>\n';
        Editor.gui+= '    <td width="220" style="padding-left:4px;">(yyyy-mm-dd[ hh:mm:ss])</td>\n'
        Editor.gui+= '  </tr>\n';
        Editor.gui+= '  <tr height="22">\n';
        Editor.gui+= '    <td>Valid to:</td>\n';
        if(Editor.calendar.src != "") {
          Editor.gui+= '    <td style="padding-right:2px;"><img src="'+Editor.calendar.imgDir+'icon_calendar.gif" alt="calendar" title="calendar" onclick="self.opener.Editor.show_calendar(\'valid_to\',116,event);" /></td>\n'
        }
        Editor.gui+= '    <td align="right"><input style="width:100%;" id="valid_to" name="valid_to" onkeyup="self.opener.Editor.listen_input(this);" onchange="return self.opener.Editor.extend_valids(this);" type="text"/></td>\n';
        Editor.gui+= '    <td style="padding-left:4px;">(yyyy-mm-dd[ hh:mm:ss])</td>\n'
        Editor.gui+= '  </tr>\n';
      }
      Editor.gui+= '</table>\n';
      
      Editor.gui += Editor.period_getForm(false, null);
    
    } else {
      
      Editor.gui+= '<table width="100%" cellpadding="0" cellspacing="0" border="0">\n';
      Editor.gui+= '  <tr>\n';
      Editor.gui+= '    <td height="22">Schedule:</td>\n';
      Editor.gui+= '    <td width="426">\n';
      Editor.gui+= '      <select id="schedules" name="schedules" size="1" style="width:100%;" onchange="var input=this.form.elements.scheduleA; input.value=this.value;this.selectedIndex=0;self.opener.Editor.listen_schedule(input);input.focus();">\n';
      Editor.gui+= '        <option style="font-size:0pt;height:0pt;background-color:white;" selected value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...to apply</option>\n';
      if(Editor.schedules.length == 0) {
          Editor.gui+= '        <option value="">No schedules found</option>\n';
        }
      for( var i=0; i < Editor.schedules.length; i++ ) {
        Editor.gui+= '        <option value="'+Editor.schedules[i]+'">'+Editor.schedules[i]+'</option>\n';
      }
      Editor.gui+= '      </select>\n';
      if(Editor.ie) Editor.gui+= '<iframe style="position:absolute;top:17px;left:76px;width:340px;height:20px;border:0px;"  src="about:blank"></iframe>\n';
      Editor.gui+= '      <div style="position:absolute;top:16px;left:'+(Editor.ie?76:74)+'px;width:340px;height:21px;"><input style="width:100%;" id="scheduleA" name="scheduleA" onkeyup="self.opener.Editor.listen_schedule(this);" type="text"/></div>\n';
      Editor.gui+= '    </td>\n'
      Editor.gui+= '  </tr>\n';
      Editor.gui+= '</table>\n';
      
      if(Editor.ie) Editor.gui+= '<iframe id="scheduler_hint_ie" style="position:absolute; top:38; left:4; width:500; height:470px; display:'+(DOMManager.tree["scheduleA"]==''?'none':'block')+'; filter:alpha(opacity=0); z-index:4;"  src="about:blank"></iframe>\n';
      Editor.gui+= '<div id="scheduler_hint" style="position:absolute; top:38px; left:4px; width:'+((Editor.ie)?500:480)+'px; height:'+((Editor.ie)?248:230)+'px; display:'+(DOMManager.tree["scheduleA"]==''?'none':'block')+'; z-index:5; padding:8px; color:green; background-color:'+Editor.bgColor+';">\n';
      Editor.gui+= 'The schedule attribute and the other run times are mutually exclusive.<br/>You must clear the schedule attribute before other run times are supported.';
      Editor.gui+= '</div>\n';
      
      Editor.gui += Editor.period_getForm(true, null);
      
      Editor.gui+= '<table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin-top:6px;">\n';
      Editor.gui+= '  <tr height="22">\n';
      Editor.gui+= '    <td align="left">Start Time Function:</td>\n';
      Editor.gui+= '    <td align="right"><input style="width:370;" id="start_time_function" onkeyup="self.opener.Editor.listen_input(this);" type="text"/></td>\n';
      Editor.gui+= '  </tr>\n';
      Editor.gui+= '</table>\n';
    }
    
    Editor.gui+= '</fieldset>\n';  
    // zuweisen
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    
    Editor.period_currentPeriod = DOMManager.tree[Editor.root];
    Editor.period_init(Editor.period_currentPeriod, true, "once");
    Editor.period_currentPeriod_isAttachable = true;
    
    if( Editor.root == "schedule" ) {
      Editor.remote.document.getElementById("title").value = DOMManager.tree["title"];
      if( Editor.hasSubstituteAttribute ) {
        Editor.remote.document.getElementById("substitute").value = DOMManager.tree["substitute"];
        Editor.remote.document.getElementById("valid_from").value = DOMManager.tree["valid_from"];
        Editor.remote.document.getElementById("valid_to").value   = DOMManager.tree["valid_to"];
      }  
    } else {
      
      if ( DOMManager.tree["start_time_function"]!=null )
        Editor.remote.document.getElementById("start_time_function").value = DOMManager.tree["start_time_function"];
      
      if ( DOMManager.tree["scheduleA"]!=null )
        Editor.remote.document.getElementById("scheduleA").value = DOMManager.tree["scheduleA"];
    }   
    
  } catch (x) {

    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showRuntime():", x);
    else
      throw new Error("Editor.showRuntime():\n" + x.message);
  }
}

Editor.showWeekdays = function(key) {

  try {
    
    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }

    Editor.mode = "weekdays";
    Editor.ok_set_enabled( true );

    Editor.gui = Editor.daylist_getForm(key, Editor.mode);
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    Editor.daylist_init(key, Editor.mode);

  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showWeekdays():", x);
    else
      throw new Error("Editor.showWeekdays():\n" + x.message);
  }
}

Editor.showMonthdays = function(key) {

  try {

    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }

    Editor.mode = "monthdays";
    Editor.ok_set_enabled( true );
    
    Editor.gui = Editor.daylist_getForm(key, Editor.mode);
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    Editor.daylist_init(key, Editor.mode);

  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showMonthdays():", x);
    else
      throw new Error("Editor.showMonthdays():\n" + x.message);
  }
}

Editor.showUltimos = function(key) {

  try {

    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }

    Editor.mode = "ultimos";
    Editor.ok_set_enabled( true );
    
    Editor.gui = Editor.daylist_getForm(key, Editor.mode);
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    Editor.daylist_init(key, Editor.mode);

  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showUltimos():", x);
    else
      throw new Error("Editor.showUltimos():\n" + x.message);
  }
}


Editor.showMonth = function(key) {

  try {

    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }

    Editor.mode = "month";
    Editor.ok_set_enabled( true );
    
    Editor.gui = Editor.daylist_getForm(key, Editor.mode);
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    Editor.daylist_init(key, Editor.mode);

  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showMonth():", x);
    else
      throw new Error("Editor.showMonth():\n" + x.message);
  }
}


Editor.showSpecificWeekdays = function(key) {
  
  try {
    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }
    Editor.mode = "specific_weekdays";
    Editor.ok_set_enabled( true );
    
    var call_key = (!key) ? null : '\''+key+'\'';
    
    var html = '  \n';
    
    html+= '<fieldset>\n';
    html+= '<legend>Monthdays</legend>\n';
    html+= '  <table width="100%" cellspacing="0" cellpadding="0" border="0">\n';
    html+= '    <tr height="24">\n';
    html+= '      <td>\n';
    html+= '        <table width="100%" cellspacing="0" cellpadding="0"><tr height="24">\n';
    html+= '          <td width="50%">\n';
    html+= '            <select id="specific_weekdays_which" style="width:100%;" size="1">\n';
    html+= '              <option value="1">First</option>\n';
    html+= '              <option value="2">Second</option>\n';
    html+= '              <option value="3">Third</option>\n';
    html+= '              <option value="4">Fourth</option>\n';
    html+= '              <option value="-1">Last</option>\n';
    html+= '              <option value="-2">Second Last</option>\n';
    html+= '              <option value="-3">Third Last</option>\n';
    html+= '              <option value="-4">Fourth Last</option>\n';
    html+= '            </select>\n';
    html+= '          </td>\n';
    html+= '          <td>&nbsp;</td>\n';
    html+= '          <td width="50%%">\n';
    html+= '            <select id="specific_weekdays_day" style="width:100%;" size="1">\n';
    html+= '              <option value="monday">Monday</option>\n';
    html+= '              <option value="tuesday">Tuesday</option>\n';
    html+= '              <option value="wednesday">Wednesday</option>\n';
    html+= '              <option value="thursday">Thursday</option>\n';
    html+= '              <option value="friday">Friday</option>\n';
    html+= '              <option value="saturday">Saturday</option>\n';
    html+= '              <option value="sunday">Sunday</option>\n';
    html+= '            </select>\n';
    html+= '          </td>\n';
    html+= '      </tr></table></td>\n';
    html+= '      <td style="width:120px;text-align:right;"><input class="specdays_button" type="button" id="specific_weekdays_add" value="Add Weekday" onclick="self.opener.Editor.specific_weekdays_action_add('+call_key+');"></td>\n';
    html+= '    </tr>\n';
    html+= '    <tr height="16">\n';
    html+= '      <td colspan="2"><hr/></td>\n';
    html+= '    </tr>\n';
    html+= '    <tr>\n';
    html+= '      <td><select class="multiple" id="specific_weekdays_selection" size="32" style="width:374px; height:446px; overflow:auto;" onChange="document.getElementById(\'specific_weekdays_remove\').disabled=false;"></select></td>\n';
    html+= '      <td style="text-align:right;vertical-align:top;"><input class="specdays_button" type="button" id="specific_weekdays_remove" value="Remove Weekday" disabled onclick="self.opener.Editor.specific_weekdays_action_remove('+call_key+');"></td>\n';
    html+= '    </tr>\n';
    html+= '  </table>\n';
    html+= '</fieldset>';
    
    Editor.remote.document.getElementById("gui").innerHTML = html;
    
    // initialisieren
    var tree = (key != 'month') ? DOMManager.tree.specific_weekdays : DOMManager.tree[key][Editor["cur_"+key]].specific_weekdays;
    if ( tree!=null && tree.days!=null ) {
      
      var names = new Array();
      for (var i=0; i<tree.days.length; i++) {
        var d = tree.days[i].split('.');
        names[i] = Editor.map_specific_weekdays[d[0]]+'.'+ Editor.map_specific_weekdays[d[1]];
      }
      
      // gesamten Inhalt anfangs loeschen
      Editor.remote.document.getElementById("specific_weekdays_selection").options.length = 0;
      Editor.remote.add_option_tags(names, tree.days, Editor.remote.document.getElementById("specific_weekdays_selection"));
    }
    
  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showSpecificWeekdays():", x);
    else
      throw new Error("Editor.showSpecificWeekdays():\n" + x.message);
  }
}

Editor.specific_weekdays_updateGUI = function(key) {
  
  try {
    // Navigationsbaum updaten
    Editor.NaviTreeUpdate(key, Editor.mode);
    
    var tree = (key != "month") ? DOMManager.tree.specific_weekdays : DOMManager.tree[key][Editor["cur_"+key]].specific_weekdays;
    if ( tree!=null && tree.days!=null ) {
      
      var names = new Array();
      for (var i=0; i<tree.days.length; i++) {
        var d = tree.days[i].split('.');
        names[i] = Editor.map_specific_weekdays[d[0]]+'.'+ Editor.map_specific_weekdays[d[1]];
      }
      
      // gesamten Inhalt anfangs loeschen
      Editor.remote.document.getElementById("specific_weekdays_selection").options.length = 0;
      Editor.remote.add_option_tags(names, tree.days, Editor.remote.document.getElementById("specific_weekdays_selection"));
    }
    
  } catch (x) {
    throw new Error("Editor.specific_weekdays_updateGUI():\n" + x.message);
  }
}

Editor.specific_weekdays_action_add = function(key) {
  
  try {
    
    var whichComponent = Editor.remote.document.getElementById("specific_weekdays_which").value;
    var dayComponent = Editor.remote.document.getElementById("specific_weekdays_day").value;
    var tree = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
    
    if ( tree.specific_weekdays==null ) tree.specific_weekdays = new Object();
    if ( tree.specific_weekdays.days==null ) tree.specific_weekdays.days = new Array();
    if ( tree.specific_weekdays[whichComponent+"."+dayComponent] != null ) {
      
      var msg = Editor.map_specific_weekdays[whichComponent]+"."+Editor.map_specific_weekdays[dayComponent];
          msg+= " already exists.";
      
      Editor.remote.Alert(msg);
    }
    else {
      tree.specific_weekdays.days.push( whichComponent+"."+dayComponent );
      tree.specific_weekdays[whichComponent+"."+dayComponent] = new Array();
    }
    
    Editor.specific_weekdays_updateGUI(key);
    
  } catch(e) {
    Editor.remote.alertError("Editor.specific_weekdays_action_add():", e);
  }
}

Editor.specific_weekdays_action_remove = function(key) {
  
  try {
    Editor.remote.document.getElementById("specific_weekdays_remove").disabled=true;
    if (Editor.remote.document.getElementById("specific_weekdays_selection").options.length == 0) return;
    
    selectedIndex = Editor.remote.document.getElementById("specific_weekdays_selection").selectedIndex;
    if ( selectedIndex < 0 ) return;
    
    var value = Editor.remote.document.getElementById("specific_weekdays_selection").value;
    var tree = (key != "month") ? DOMManager.tree.specific_weekdays : DOMManager.tree[key][Editor["cur_"+key]].specific_weekdays;
    
    tree[value] = null;
    
    var index = 0;
    while ( tree.days[index]!=value )
      index++;
    
    for (var i=index; i<tree.days.length-1; i++) {
        tree.days[i] = tree.days[i+1];
    }
    
    tree.days.length = tree.days.length-1;
    
    
    Editor.specific_weekdays_updateGUI(key);
    
    
    if (selectedIndex > Editor.remote.document.getElementById("specific_weekdays_selection").options.length-1)
      selectedIndex--;
    if ( selectedIndex >= 0 )
      Editor.remote.document.getElementById("specific_weekdays_selection").options[selectedIndex].selected = true;
    
  } catch(e) {
    Editor.remote.alertError("Editor.specific_weekdays_action_remove():", e);
  }
}


Editor.showSpecificDays = function(mode, key) {

  try {

    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }

    Editor.mode = mode;
    Editor.ok_set_enabled( true );
    
    Editor.gui = "";
    Editor.gui += Editor.dateform_getForm(key, mode);
    
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    var tree = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]]; 
    Editor.dateform_init(tree[mode]);
    if( mode == "holidays" ) {
      Editor.daylist_init(key, "holiday_weekdays");
      Editor.entrylist_init();
    }

  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showSpecificDays():", x);
    else
      throw new Error("Editor.showSpecificDays():\n" + x.message);
  }
}


Editor.period_enable = function(enabled) {
  
  try {
    
    var state = (enabled ? "" : "disabled");
    
    Editor.remote.document.getElementById("period_let_run").disabled = state;
    Editor.remote.document.getElementById("period_when_holiday").disabled = state;
    
    try {
      if ( Editor.remote.document.getElementById("period_run_once") )
        Editor.remote.document.getElementById("period_run_once").disabled = state;
    } catch(e) {}
    
    var doc = Editor.remote.document;
    
    var elem;
    for (var i=0; i<Editor.attribute_indices.length; i++) {
      elem = doc.getElementById("period_" + Editor.attribute_indices[i]);
      elem.disabled = state;
      if( enabled ) Editor.clearInputBgColor(elem);
      else elem.style.backgroundColor = Editor.bgColor;
    }
    
  } catch(x) {
    throw new Error("Editor.period_enable(): " + x.message);
  }
}


Editor.listen_begintime_endtime = function(doc) {

  try {

    beginHHElem = doc.getElementById("period_begintime_hh");
    beginMMElem = doc.getElementById("period_begintime_mm");
    beginSSElem = doc.getElementById("period_begintime_ss");
    endHHElem = doc.getElementById("period_endtime_hh");
    endMMElem = doc.getElementById("period_endtime_mm");
    endSSElem = doc.getElementById("period_endtime_ss");

    Editor.clearInputBgColor(beginHHElem);
    Editor.clearInputBgColor(beginMMElem);
    Editor.clearInputBgColor(beginSSElem);
    Editor.clearInputBgColor(endHHElem);
    Editor.clearInputBgColor(endMMElem);
    Editor.clearInputBgColor(endSSElem);

    beginHH = beginHHElem.value;
    beginMM = beginMMElem.value;
    beginSS = beginSSElem.value;
    endHH = endHHElem.value;
    endMM = endMMElem.value;
    endSS = endSSElem.value;

    if ( isNaN(beginHH) || beginHH<0 || beginHH>23 ) {
      beginHHElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(beginMM) || beginMM<0 || beginMM>59 ) {
      beginMMElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(beginSS) || beginSS<0 || beginSS>59 ) {
      beginSSElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(endHH) || endHH<0 || endHH>24 ) {
      endHHElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(endMM) || endMM<0 || endMM>59 ) {
      endMMElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(endSS) || endSS<0 || endSS>59 ) {
      endSSElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    // fuer endtime
    if ( Editor.period_currentPeriod_isAttachable ) {
      if ( endHH==24 && ( endMM>0 || endSS>0 ) ) {
        endHHElem.style.backgroundColor = "yellow";
        endMMElem.style.backgroundColor = "yellow";
        endSSElem.style.backgroundColor = "yellow";
        Editor.period_currentPeriod_isAttachable = false;
      }
    }

    if ( Editor.period_currentPeriod_isAttachable == false ) {
      if ( Editor.mode=="run_time" )
        Editor.ok_set_enabled( Editor.period_currentPeriod_isAttachable );
      else
        Editor.periodlist_btn_apply_period_set_enabled( Editor.period_currentPeriod_isAttachable );

      return;
    }

    var begin = beginHH + beginMM + beginSS;
    var end = endHH + endMM + endSS;

    if ( begin!="" && end!="" ) {

      if ( beginMM=="" ) beginMM="00";
      if ( beginSS=="" ) beginSS="00";
      if ( beginMM.length==1 ) beginMM = "0"+beginMM;
      if ( beginSS.length==1 ) beginSS = "0"+beginSS;
      begin = Number(beginHH + beginMM + beginSS);

      if ( endMM=="" ) endMM="00";
      if ( endSS=="" ) endSS="00";
      if ( endMM.length==1 ) endMM = "0"+endMM;
      if ( endSS.length==1 ) endSS = "0"+endSS;
      end = Number(endHH + endMM + endSS);

      if ( end < begin ) {

        beginHHElem.style.backgroundColor = "yellow";
        beginMMElem.style.backgroundColor = "yellow";
        beginSSElem.style.backgroundColor = "yellow";
        endHHElem.style.backgroundColor = "yellow";
        endMMElem.style.backgroundColor = "yellow";
        endSSElem.style.backgroundColor = "yellow";
        Editor.period_currentPeriod_isAttachable = false;
      }
    }

    if ( Editor.mode=="run_time" )
      Editor.ok_set_enabled( Editor.period_currentPeriod_isAttachable );
    else
      Editor.periodlist_btn_apply_period_set_enabled( Editor.period_currentPeriod_isAttachable );

  } catch(x) {
    throw new Error("Editor.listen_begintime_endtime():\n" + x.message);
  }
}

Editor.listen_repeattime = function(doc, specifier) {

  try {

    hhElem = doc.getElementById("period_"+specifier+"_hh");
    mmElem = doc.getElementById("period_"+specifier+"_mm");
    ssElem = doc.getElementById("period_"+specifier+"_ss");

    hh = hhElem.value;
    mm = mmElem.value;
    ss = ssElem.value;

    Editor.clearInputBgColor(hhElem);
    Editor.clearInputBgColor(mmElem);
    Editor.clearInputBgColor(ssElem);

    if ( isNaN(hh) || hh<0 || hh>23 ) {
      hhElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(mm) || mm<0 || mm>59 ) {
      mmElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( isNaN(ss) || mm<0 || (ss>59 && (hh!="" || mm!="")) ) {
      ssElem.style.backgroundColor = "yellow";
      Editor.period_currentPeriod_isAttachable = false;
    }

    if ( Editor.mode=="run_time" )
      Editor.ok_set_enabled( Editor.period_currentPeriod_isAttachable );
    else
      Editor.periodlist_btn_apply_period_set_enabled( Editor.period_currentPeriod_isAttachable );

  } catch(x) {
    throw new Error("Editor.listen_repeattime():\n" + x.message);
  }
}

Editor.listen_singlestart = function(doc) {

  try {

    hhElem = doc.getElementById("period_singlestart_hh");
    mmElem = doc.getElementById("period_singlestart_mm");
    ssElem = doc.getElementById("period_singlestart_ss");

    var hh = hhElem.value;
    var mm = mmElem.value;
    var ss = ssElem.value;

    Editor.clearInputBgColor(hhElem);
    Editor.clearInputBgColor(mmElem);
    Editor.clearInputBgColor(ssElem);

    if ( hh=="" && mm=="" && ss=="" ) {

      doc.getElementById("period_let_run").disabled = "";
      var elem;
      for (var i=0; i<Editor.attribute_indices.length-3; i++) {
        elem = doc.getElementById("period_" + Editor.attribute_indices[i]);
        elem.disabled = "";
        Editor.clearInputBgColor(elem);
      }

      Editor.listen_begintime_endtime(doc);
      Editor.listen_repeattime(doc, "repeattime");
      Editor.listen_repeattime(doc, "absoluterepeat");

    } else {

      doc.getElementById("period_let_run").disabled = "disabled";
      var elem;
      for (var i=0; i<Editor.attribute_indices.length-3; i++) {
        elem = doc.getElementById("period_" + Editor.attribute_indices[i]);
        elem.disabled = "disabled";
        elem.style.backgroundColor = Editor.bgColor;
      }

      if ( isNaN(hh) || hh<0 || hh>23 ) {
        hhElem.style.backgroundColor = "yellow";
        Editor.period_currentPeriod_isAttachable = false;
      }

      if ( isNaN(mm) || mm<0 || mm>59 ) {
        mmElem.style.backgroundColor = "yellow";
        Editor.period_currentPeriod_isAttachable = false;
      }

      if ( isNaN(ss) || mm<0 || ss>59 ) {
        ssElem.style.backgroundColor = "yellow";
        Editor.period_currentPeriod_isAttachable = false;
      }
    }

    if ( Editor.mode=="run_time" )
      Editor.ok_set_enabled( Editor.period_currentPeriod_isAttachable );
    else
      Editor.periodlist_btn_apply_period_set_enabled( Editor.period_currentPeriod_isAttachable );

  } catch(x) {
    throw new Error("Editor.listen_singlestart():\n" + x.message);
  }
}

// horcht am inputfeld fuer "Function" unter Runtime im mode "run_time"
Editor.listen_input = function(elem) {
  
  try {
      
      DOMManager.tree[elem.id] = elem.value;
      
  } catch (e) {
    Editor.remote.alertError("Editor.listen_input():", e);
  }
}


Editor.listen_schedule = function(elem) {
  
  try {
    var oldValue = DOMManager.tree["scheduleA"];
    if( oldValue != elem.value ) {
      DOMManager.tree[elem.id] = elem.value;
      if(oldValue == "" || DOMManager.tree["scheduleA"] == "") Editor.NaviRootImgDisplay(true);
    }
  } catch (e) {
    Editor.remote.alertError("Editor.listen_schedule():", e);
  }
}


Editor.listen_valids = function(elem_name) {
  
  var elem          = Editor.remote.document.getElementById(elem_name);
  var old_timestamp = DOMManager.tree[elem.id].split(" ");
  var new_time      = (elem.id == 'valid_from') ? '00:00:00' : '24:00:00';
  if( old_timestamp.length == 2 && old_timestamp[1] != "" ) new_time = old_timestamp[1];
  elem.value        += " " + new_time;
  DOMManager.tree[elem.id] = elem.value;
  Editor.extend_valids(elem);
}


Editor.extend_valids = function(elem, val) {
  
  if( typeof elem != "object" ) elem = {id:elem, value:val, focus:new Function()};
  var msg = "";
  if( elem.value == "" ) return true;
  if( elem.value.search(/^\s*\d{2,4}\D+\d{1,2}\D+\d{1,2}/) == -1 ) { msg = "Please enter the date of the "+ elem.id +" attribute\nin ISO format (YYYY-MM-DD hh:mm:ss)."; }
  if( msg == "" ) {
    var m = elem.value.match(/^\s*(\d{2,4})\D+(\d{1,2})\D+(\d{1,2})(?:\D+(\d{1,2}))?(?:\D+(\d{1,2}))?(?:\D+(\d{1,2}))?/);
    if( m[1].length == 2 ) m[1] = '0'+m[1];
    if( m[1].length == 3 ) m[1] = '2'+m[1];
    for(var i=2; i < m.length; i++) {
      if( i>3 && typeof m[i] != 'string' ) m[i] = '';
      if( m[i].length == 1 ) m[i] = '0'+m[i];
    }
    if( m.length == 4 || m[4] == "" ) m[4] = (elem.id == 'valid_from') ? '00' : '24';
    if( m.length == 5 || m[5] == "" ) m[5] = '00';
    if( m.length == 6 || m[6] == "" ) m[6] = '00'; 
    elem.value = m[1]+"-"+m[2]+"-"+m[3]+" "+m[4]+":"+m[5]+":"+m[6];
    DOMManager.tree[elem.id] = elem.value;
    
    var daysInMonth      = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
    var year             = parseInt(m[1],10);
    if( year%4    == 0 ) { daysInMonth[1]++; }
    if( year%100  == 0 ) { daysInMonth[1]--; }
    if( year%400  == 0 ) { daysInMonth[1]++; }
    if( msg == "" && (parseInt(m[2],10) < 1 || parseInt(m[2],10) > 12)                               ) { msg = "Please type 00-12 for the month\nof the "+ elem.id +" attribute."; }
    if( msg == "" && (parseInt(m[3],10) < 1 || parseInt(m[3],10) > daysInMonth[parseInt(m[2],10)-1]) ) { msg = "Please type 01-"+daysInMonth[parseInt(m[2],10)-1]+" for the day\nof the "+ elem.id +" attribute."; }
    if( msg == "" && (parseInt(m[4],10) < 0 || parseInt(m[4],10) > 23 ||
          parseInt(m[5],10) < 0 || parseInt(m[5],10) > 59  ||
          parseInt(m[6],10) < 0 || parseInt(m[6],10) > 59) && (m[4]+":"+m[5]+":"+m[6]) != "24:00:00" ) { msg = "Please type 00:00:00-24:00:00 for the time\nof the "+ elem.id +" attribute." }
  }
  if( msg != "" ) {
    Editor.remote.Alert(msg);
    elem.focus();
    return false;
  }
  return true;
}


Editor.plausi_valids = function() {
  
  if( !Editor.hasSubstituteAttribute ) return true;
  var ok         = true;
  var cnt_values = 0;
  var significants = ['substitute', 'valid_from', 'valid_to'];
  for( var i = 0; i < significants.length; i++ ) {
    if( DOMManager.tree[significants[i]] != "" ) cnt_values++;
  }
  if( cnt_values == 0 ) return true;
  if( cnt_values > 0 && cnt_values < 3 ) {
    ok = false;
    Editor.remote.Alert('The attributes substitute, valid_from and valid_to must be empty or filled simultaneously.' );
  }
  if( ok ) ok = Editor.extend_valids( 'valid_from', DOMManager.tree.valid_from);
  if( ok ) ok = Editor.extend_valids( 'valid_to',   DOMManager.tree.valid_to);
  if( ok && (parseInt(DOMManager.tree.valid_from.replace(/\D/g,"")) - parseInt(DOMManager.tree.valid_to.replace(/\D/g,"")) > 0) ) {
    ok = false;
    Editor.remote.Alert('The valid_from attribute must be older than the valid_to attribute.' );
  }
  if( !ok && !Editor.remote.document.getElementById('substitute')) Editor.showRuntime();
  return ok;
} 


Editor.period_listen = function(elem) {

  try {

    if ( Editor.period_currentPeriod==null )
      throw new Error("current period is null");

    var doc = Editor.remote.document;

    // Index fuer den Zugriff auf das assoziative Array ermitteln
    subElem = elem.id.substring(7);

    if ( subElem=="let_run" || subElem=="run_once" )
      Editor.period_currentPeriod[subElem] = ( elem.checked );
    
    if ( subElem=="when_holiday" )
      Editor.period_currentPeriod[subElem] = elem.value;
    
    if ( subElem=="let_run" || subElem=="run_once" || subElem=="when_holiday" ) {
      if ( Editor.mode=="run_time" )
        Editor.ok_set_enabled( Editor.period_currentPeriod_isAttachable );
      else
        Editor.periodlist_btn_apply_period_set_enabled( Editor.period_currentPeriod_isAttachable );

      return;
    }

    specifier = subElem.substring(0, subElem.length-3); // z.B. begintime
    timePart = subElem.substring(subElem.length-2); // z.B. hh
    //value = elem.value;

    Editor.period_currentPeriod_isAttachable = true;

    if ( specifier=="begintime" || specifier=="endtime" || specifier=="repeattime" || specifier=="absoluterepeat" ) {
      Editor.listen_begintime_endtime(doc);
      Editor.listen_repeattime(doc, "repeattime");
      Editor.listen_repeattime(doc, "absoluterepeat");
      return;
    }

    if ( specifier=="singlestart" ) {
      Editor.listen_singlestart(doc);
      return;
    }

  } catch (e) {
    Editor.remote.alertError("Editor.period_listen():", e);
  }
}


Editor.show_calendar = function(elem,indentY,evt) {
  
  if(Editor.remote['SOS_Calendar']) {
    Editor.remote.SOS_Calendar.indentY = indentY; 
    Editor.remote.SOS_Calendar.show(evt,'editor_form.'+elem);
  } else {
    Editor.remote.Alert('Sorry, but the calendar feature is not yet supported');
  }
}

// wenn run_once gesetzt ist (!= undefined) wird die Checkbox "period_run_once" geschrieben, bei false nicht
Editor.period_init = function(tree, enabled, run_once) {
  
  try { 
    
    // falls run_once nicht vorkommt, 'run_once'-Maske entfernen
    if ( typeof run_once == 'undefined' ) {
      Editor.remote.document.getElementById("period_ronce1").removeChild(Editor.remote.document.getElementById("period_ronce1").firstChild);
      Editor.remote.document.getElementById("period_ronce2").removeChild(Editor.remote.document.getElementById("period_ronce2").firstChild);
    }
    
    if ( enabled )
      Editor.period_fillForm(tree, run_once);
    else
      Editor.period_enable(false); //Elemente deaktivieren

  } catch (x) {
    throw new Error("Editor.period_init():\n" + x.message);
  }
}


Editor.period_clearForm = function() {

  try {

    var doc = Editor.remote.document;

    doc.getElementById("period_let_run").checked = "";
    doc.getElementById("period_when_holiday").value = "suppress";

    try {
      if ( doc.getElementById("period_run_once") )
        doc.getElementById("period_run_once").checked = "";
    } catch(e) {}

    for (var i=0; i<Editor.attribute_indices.length; i++)
      doc.getElementById("period_" + Editor.attribute_indices[i]).value = "";

  } catch(x) {
    throw new Error("Editor.period_clearForm():\n" + x.message);
  }
}


Editor.period_fillForm = function(tree, run_once) {

  try {

    if (tree==null) throw new Error("parameter tree is null");

    var doc = Editor.remote.document;

    if (typeof run_once != 'undefined') {
      doc.getElementById("period_run_once").checked = (tree["run_once"] ? "checked" : "");
    }
    
    doc.getElementById("period_when_holiday").value = (tree["when_holiday"].search(/^(previous_non|next_non|ignore)_holiday$/) > -1 ? tree["when_holiday"] : "suppress");

    if ( tree["singlestart_hh"]=="" && tree["singlestart_mm"]=="" && tree["singlestart_ss"]=="") {

      doc.getElementById("period_let_run").checked = (tree["let_run"] ? "checked" : "");

      var elem = doc.getElementById("period_singlestart_hh");
      elem.value = "";
      Editor.clearInputBgColor(elem);

      elem = doc.getElementById("period_singlestart_mm");
      elem.value = "";
      Editor.clearInputBgColor(elem);

      elem = doc.getElementById("period_singlestart_ss");
      elem.value = "";
      Editor.clearInputBgColor(elem);

      var IDs = new Array("begintime", "endtime", "repeattime", "absoluterepeat");
      var timestamp;

      for (var i=0; i<IDs.length; i++) {
        
        timestamp = DOMManager.getTimestamp(tree[IDs[i]+"_hh"], tree[IDs[i]+"_mm"], tree[IDs[i]+"_ss"]);

        elem = doc.getElementById("period_"+IDs[i]+"_hh");
        elem.value = DOMManager.timeGetHH(timestamp);
        Editor.clearInputBgColor(elem);

        elem = doc.getElementById("period_"+IDs[i]+"_mm");
        elem.value = DOMManager.timeGetMM(timestamp);
        Editor.clearInputBgColor(elem);

        elem = doc.getElementById("period_"+IDs[i]+"_ss");
        elem.value = DOMManager.timeGetSS(timestamp);
        Editor.clearInputBgColor(elem);
      }

    } else {

      var timestamp;

      var elem = doc.getElementById("period_let_run");
      elem.checked = "";
      elem.disabled = "disabled"
      
      timestamp = DOMManager.getTimestamp(tree["singlestart_hh"], tree["singlestart_mm"], tree["singlestart_ss"]);

      elem = doc.getElementById("period_singlestart_hh");
      elem.value = DOMManager.timeGetHH(timestamp);
      Editor.clearInputBgColor(elem);

      elem = doc.getElementById("period_singlestart_mm");
      elem.value = DOMManager.timeGetMM(timestamp);
      Editor.clearInputBgColor(elem);

      elem = doc.getElementById("period_singlestart_ss");
      elem.value = DOMManager.timeGetSS(timestamp);
      Editor.clearInputBgColor(elem);

      for (var i=0; i<Editor.attribute_indices.length-3; i++) {
        elem = doc.getElementById("period_"+Editor.attribute_indices[i]);
        elem.value = "";
        elem.style.backgroundColor = Editor.bgColor;
        elem.disabled = "disabled";
      }
    }

  } catch(x) {
    throw new Error("Editor.period_fillForm():\n" + x.message);
  }
}

Editor.period_getForm = function(display, key) {
  
  var display  = (typeof display == 'boolean' && !display) ? 'display:none;' : '';
  var call_key = (!key) ? null : '\''+key+'\''; 

  var html = '  \n';
  if(Editor.mode != 'run_time') html+= '  <div style="float:left;width:380px;">\n';
  
  html+= '  <fieldset style="'+display+'">\n';
  html+= '    <legend>Time Slot</legend>\n';
  html+= '      <table cellspacing="0" cellpadding="0" border="0" style="float:left;width:100%;">\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">Let Run:</td>\n';
  html+= '          <td width="40" valign="bottom"><input type="checkbox" id="period_let_run" onclick="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8">&nbsp;</td>\n';
  html+= '          <td width="88" colspan="3" id="period_ronce1"><span>Run Once:</span><br/></td>\n';
  html+= '          <td width="" id="period_ronce2" valign="bottom"><input type="checkbox" id="period_run_once" onclick="self.opener.Editor.period_listen(this);"/><br/></td>\n';
  html+= '        </tr>\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">Begin Time:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_begintime_hh" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_begintime_mm" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_begintime_ss" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td>&nbsp;hh:mm:ss</td>\n';
  html+= '        </tr>\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">End Time:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_endtime_hh" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_endtime_mm" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_endtime_ss" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td>&nbsp;hh:mm:ss</td>\n';
  html+= '        </tr>\n';
  html+= '      </table>\n';
  html+= '  </fieldset> <!-- Time Slot -->\n';
  html+= '  <fieldset style="'+display+'">\n';
  html+= '    <legend>Start Time</legend>\n';
  html+= '      <table cellspacing="0" cellpadding="0" border="0" style="float:left;width:100%;">\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">Repeat Time:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_repeattime_hh" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_repeattime_mm" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_repeattime_ss" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td>&nbsp;hh:mm:ss or ss</td>\n';
  html+= '        </tr>\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">Absolute Repeat:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_absoluterepeat_hh" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_absoluterepeat_mm" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_absoluterepeat_ss" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td>&nbsp;hh:mm:ss or ss</td>\n';
  html+= '        </tr>\n';
  html+= '        <tr height="22">\n';
  html+= '          <td width="100">Single Start:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_singlestart_hh" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_singlestart_mm" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td width="8" style="text-align:center;">:</td>\n';
  html+= '          <td width="40"><input class="period_inputtext" id="period_singlestart_ss" maxlength="2" type="text" onkeyup="self.opener.Editor.period_listen(this);"/></td>\n';
  html+= '          <td>&nbsp;hh:mm:ss</td>\n';
  html+= '        </tr>\n';
  html+= '      </table>\n';
  html+= '  </fieldset> <!-- Start Time -->\n';
  html+= '  <fieldset style="'+display+'">\n';
  html+= '    <legend>When Holiday</legend>\n';
  html+= '      <table cellspacing="0" cellpadding="0" border="0" style="float:left;width:100%;">\n';
  html+= '        <tr height="22">\n';
  html+= '          <td><select id="period_when_holiday" style="width:100%;" onchange="self.opener.Editor.period_listen(this);">\n';
  for(var entry in Editor.when_holiday_options ) {
    var opt_selected = ( entry == "suppress" ) ? " selected" : "";
    html+= '            <option'+opt_selected+' value='+entry+'>'+Editor.when_holiday_options[entry]+'</option>\n';
  }
  html+= '          </select></td>\n';
  html+= '        </tr>\n';
  html+= '      </table>\n';
  html+= '  </fieldset> <!-- When Holiday -->\n';
  if(Editor.mode != 'run_time') {
    html+= '  </div>\n';
    html+= '  <div style="text-align:right">\n';
    html+= '      <input id="periodlist_apply_period" type="button" class="periodlist_button" value="Apply Period" onclick="self.opener.Editor.periodlist_action_apply_period('+call_key+');"/>\n';
    html+= '  </div>\n';
  }
  html+= '  \n';

  return html;
}


// sort callback functions
Editor.NumSort = function(a, b) {
  return a - b;
}

Editor.GrouppedDaysSort = function(a, b) {
  var c = a.split(" ");
  var d = b.split(" ");
  var l = c.length;
  if( l != d.length ) return d.length - l;
  for(var i=0; i < l; i++) {
    if( c[i] != d[i] ) return c[i] - d[i];
  }
  return c[l-1] - d[l-1];
}



Editor.dateform_getForm = function(key, target) {

  try {
    
    var call_key = (!key) ? null : '\''+key+'\'';
    var sHeight  = ( target == "holidays" ? 100 : 446 );
    
    var html = '  \n';
    if( target == "holidays" ) {
      html+= '    <fieldset>\n';
      html+= '      <legend>Holidays</legend>\n';
      html+= '            \n';
    }
    html+= '        <fieldset>\n';
    html+= '          <legend>Specific Dates</legend>\n';
    html+= '            <table width="100%" cellspacing="0" cellpadding="0" border="0">\n';
    html+= '              <tr><td><table width="100%" cellspacing="0" cellpadding="0" border="0">\n';
    html+= '                  <tr height="24">\n';
    html+= '                    <td style="width:20px;">Year:</td>\n';
    html+= '                    <td style="width:44px;padding-left:4px;"><input maxlength="4" id="dateform_year" onkeyup="self.opener.Editor.dateform_listen(this);" style="width:40px" type="text"/></td>\n';
    html+= '                    <td style="width:20px;padding-left:15px;">Month:</td>\n';
    html+= '                    <td style="width:44px;padding-left:4px;"><input maxlength="2" id="dateform_month" onkeyup="self.opener.Editor.dateform_listen(this);" style="width:40px" type="text"/></td>\n';
    html+= '                    <td style="width:20px;padding-left:15px;">Day:</td>\n';
    html+= '                    <td style="width:44px;padding-left:4px;"><input maxlength="2" id="dateform_day" onkeyup="self.opener.Editor.dateform_listen(this);" style="width:40px" type="text"/></td>\n';
    html+= '                    <td>&nbsp;</td>\n';
    html+= '                  </tr></table>\n';
    html+= '                </td>\n';
    html+= '                <td style="width:120px;text-align:right;"><input type="button" id="dateform_add_date" class="dateform_button" value="Add Date" onclick="self.opener.Editor.dateform_action_add_date('+call_key+',\''+ target +'\');"/></td>\n';
    html+= '              </tr>\n';
    html+= '              <tr height="16px">\n';
    html+= '                <td colspan="2"><hr/></td>\n';
    html+= '              </tr>\n';
    html+= '              <tr>\n';
    html+= '                <td><select class="multiple" id="dateform_dates" style="width:100%; height:'+sHeight+';" size="30" onchange="self.opener.Editor.dateform_action_enable_btn_remove_date();"></select></td>\n';
    html+= '                <td style="text-align:right;vertical-align:top"><input type="button" id="dateform_remove_date" class="dateform_button" value="Remove Date" onclick="self.opener.Editor.dateform_action_remove_date('+call_key+',\''+ target +'\');"/></td>\n';
    html+= '              </tr>\n';
    html+= '            </table>\n';
    html+= '        </fieldset> <!-- specificdays -->\n';
    html+= '          \n';
    if ( target == "holidays") {
      html+= Editor.daylist_getForm(key, "holidays", sHeight);
      html+= Editor.entrylist_getForm("Include Files", sHeight);
      if(!Editor.ie) html+= '    <div style="height:3px;">&nbsp;</div>\n'; 
      html+= '    </fieldset> <!-- Holidays -->\n';
      html+= '    \n';
    }
    
    return html;
    
  } catch(x) {
    throw new Error("Editor.dateform_getForm():\n" + x.message);
  }
}


Editor.dateform_checkDate = function( year, month, day ) {

  var ok               = true;
  var daysInMonth      = new Array(31,28,31,30,31,30,31,31,30,31,30,31);

  if( !isFinite(year) || !isFinite(month) || !isFinite(day) ) {
    return false;
  }

  if( year%4    == 0 ) { daysInMonth[1]++; }
  if( year%100  == 0 ) { daysInMonth[1]--; }
  if( year%400  == 0 ) { daysInMonth[1]++; }

  if( ok && ( month  < 1 || month  > 12                   ) ) { ok = false; }
  if( ok && ( day    < 1 || day    > daysInMonth[month-1] ) ) { ok = false; }

  return ok;
}


Editor.dateform_listen = function(elem) {

  try {
    
    var date_isAttachable = true;

    yElem = Editor.remote.document.getElementById("dateform_year");
    mElem = Editor.remote.document.getElementById("dateform_month");
    dElem = Editor.remote.document.getElementById("dateform_day");

    Editor.clearInputBgColor(yElem);
    Editor.clearInputBgColor(mElem);
    Editor.clearInputBgColor(dElem);
    
    yVal = yElem.value;
    mVal = mElem.value;
    dVal = dElem.value;

    if ( isNaN(yVal) || yVal<0 ) {
      yElem.style.backgroundColor = "yellow";
      date_isAttachable = false;
    }

    if ( isNaN(mVal) || mVal<0 || mVal>12 ) {
      mElem.style.backgroundColor = "yellow";
      date_isAttachable = false;
    }

    if ( isNaN(dVal) || dVal<0 || dVal>31 ) {
      dElem.style.backgroundColor = "yellow";
      date_isAttachable = false;
    }

    if ( date_isAttachable && yVal!="" && mVal!="" && dVal!="") {

      date_isAttachable = Editor.dateform_checkDate( Number(yVal), Number(mVal), Number(dVal) );
      if ( !date_isAttachable ) {
        yElem.style.backgroundColor = "yellow";
        mElem.style.backgroundColor = "yellow";
        dElem.style.backgroundColor = "yellow";
      }
    }

    Editor.remote.document.getElementById("dateform_add_date").disabled = (date_isAttachable) ? "" : "disabled";

  } catch(e) {
    Editor.remote.alertError("Editor.dateform_listen():", e);
  }
}


Editor.dateform_init = function(tree) {

  try {
    // aktuellen Tag voreinstellen
    var now = new Date();
    Editor.remote.document.getElementById("dateform_year").value = now.getFullYear();
    Editor.remote.document.getElementById("dateform_month").value = now.getMonth()+1;
    Editor.remote.document.getElementById("dateform_day").value = now.getDate();

    // Button "Remove Date" beim Start deaktivieren
    Editor.remote.document.getElementById("dateform_remove_date").disabled = "disabled";

    Editor.dateform_dates = new Array();
    Editor.dateform_dates_ass = new Object();

    // fuer holidays und specificdays
    if ( typeof tree != 'undefined' ) {

      if ( tree==null )
        throw new Error("parameter tree is null");

      for (var i=0; i<tree.length; i++) {

        Editor.dateform_dates_ass[ tree[i] ] = 1;
        Editor.dateform_dates[Editor.dateform_dates.length] = tree[i];
        Editor.dateform_dates.sort(Editor.NumSort); // sortieren
      }

      var d;
      // Daten geordnet einfuegen
      for (var i=0; i<Editor.dateform_dates.length; i++) {
        d = Editor.dateform_dates[i];
        Editor.remote.add_option_tag(d.substring(0,4)+"-"+d.substring(4,6)+"-"+d.substring(6), d, "dateform_dates");
      }
    }
  } catch (x) {
    throw new Error("Editor.dateform_init:\n" + x.message);
  }
}

Editor.dateform_action_add_date = function(key, target) {

  try {

    var year = Editor.remote.document.getElementById("dateform_year").value;
    var month = Editor.remote.document.getElementById("dateform_month").value;
    var day = Editor.remote.document.getElementById("dateform_day").value;

    if (year=="") { Editor.remote.Alert("Please specify a year");return; }
    if (month=="") { Editor.remote.Alert("Please specify a month");return; }
    if (day=="") { Editor.remote.Alert("Please specify a day");return; }

    if (isNaN(year) || year.length!=4) { Editor.remote.Alert("\""+year + "\" is no valid year");return; }

    if (isNaN(month)) { Editor.remote.Alert("\""+month + "\" is no valid month");return; }
    if (month<1 || month>12) { Editor.remote.Alert("\""+month + "\" is no valid month");return; }
    if (month.length>2) { Editor.remote.Alert("\""+month + "\" is no valid month");return; }

    if (isNaN(day)) { Editor.remote.Alert("\""+day + "\" is no valid day");return; }
    if (day<1 || day>31) { Editor.remote.Alert("\""+day + "\" is no valid day");return; }
    if (day.length>2) { Editor.remote.Alert("\""+day + "\" is no valid day");return; }

    if (month.length!=2) month = "0"+month;
    if (day.length!=2) day = "0"+day;
    var date = year+month+day;  // Datum ist jetzt formatiert, z.B. 20060131

    if (Editor.dateform_dates_ass[date] == 1) { Editor.remote.Alert("This date already exists");return; }
    // ansonsten neues Datum in die Liste aufnehmen
    Editor.dateform_dates_ass[date] = 1;
    Editor.dateform_dates[Editor.dateform_dates.length] = date;
    Editor.dateform_dates.sort(Editor.NumSort); // sortieren

    // in den Baum uebertragen
    if ( target!='holidays' && target!='specificdays' ) {
      throw new Error('parameter target is unknown : ' + target);
    } else {
      
      var tree = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
      if ( tree[target]==null ) tree[target] = new Array();

      tree[target].push( date );
      tree[target].sort(Editor.NumSort); //sortieren
    }

    // alten Inhalt loeschen
    Editor.remote.document.getElementById("dateform_dates").options.length = 0;

    var d;
    // und neuen geordnet einfuegen
    for (var i=0; i<Editor.dateform_dates.length; i++) {
      d = Editor.dateform_dates[i];
      Editor.remote.add_option_tag(d.substring(0,4)+"-"+d.substring(4,6)+"-"+d.substring(6), d, "dateform_dates");
    }

    if ( target=='specificdays' ) {
      // Navigationsbaum updaten
      Editor.NaviTreeUpdate(key, "specificdays");
    }

  } catch (e) {
    Editor.remote.alertError("Editor.dateform_action_add_date():", e);
  }
}

Editor.dateform_action_remove_date = function(key, target) {

  try {
    
    var tree = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
    var dateform_dates = Editor.remote.document.getElementById("dateform_dates");
    if (dateform_dates.length == 0 || dateform_dates.selectedIndex < 0) return;
    
    var index = dateform_dates.selectedIndex;
    var value = dateform_dates.options[index].value;
    Editor.dateform_dates_ass[value] = null;

    Editor.dateform_dates = new Array();

    var i = 0;
    for (var date in Editor.dateform_dates_ass) {
      if ( Editor.dateform_dates_ass[date]==1 )
        Editor.dateform_dates[i++] = date;
    }
    Editor.dateform_dates.sort(Editor.NumSort); // sortieren

    if ( target!='holidays' && target!='specificdays' ) {
      throw new Error('parameter target is invalid : ' + target);
    } else {
      
      tree[target].length = 0;
      
      for (var i=0; i<Editor.dateform_dates.length; i++)
        tree[target].push( Editor.dateform_dates[i] );
    }
    
    if ( target == 'specificdays' ) {
      
      // Datenarray fuer specificdays aktualisieren
      if ( tree['specificdays_dates']!=null )
        tree['specificdays_dates'][value] = null;
      
      // falls ein <at>-Datum fuer das date existiert
      if ( tree["at_date"]!=null ) {
        for (var i=0; i<tree["at_date"].length; i++) {
          if ( tree["at_date"][i]==value ) {
            for (var d=i; d<tree["at_date"].length-1; d++)
              tree["at_date"][d] = tree["at_date"][d+1];
            tree["at_date"].length = tree["at_date"].length-1;
            break;
          }
        }
      }
      
      // Navigationsbaum updaten
      Editor.NaviTreeUpdate(key,"specificdays");
    }
    
    // alten Inhalt loeschen
    dateform_dates.options.length = 0;
    
    var d;
    // und neuen geordnet einfuegen
    for (var i=0; i<Editor.dateform_dates.length; i++) {
      d = Editor.dateform_dates[i];
      Editor.remote.add_option_tag(d.substring(0,4)+"-"+d.substring(4,6)+"-"+d.substring(6), d, dateform_dates);
    }
    
    // Markierung neu positionieren
    if (index > dateform_dates.options.length-1) index--;
    
    if (index>=0)
      dateform_dates.options[index].selected = true;
    
    if (Editor.dateform_dates.length==0)
      Editor.remote.document.getElementById("dateform_remove_date").disabled = "disabled";
    
  } catch (e) {
    Editor.remote.alertError("Editor.dateform_action_remove_date():", e);
  }
}

Editor.dateform_action_enable_btn_remove_date = function() {
  Editor.remote.document.getElementById("dateform_remove_date").disabled = "";
}


Editor.entrylist_getForm = function(caption, sHeight) {
  
  try {
    
    var html = '\n';
    
    html+= '<fieldset>\n';
    html+= '<legend>'+caption+'</legend>\n';
    
    html+= '<table width="100%" cellspacing="0" cellpadding="0" border="0">\n';
    html+= '  <tr height="24">\n';
    html+= '    <td style="width:30px;padding-right:4px;"><select id="entrylist_file_type"><option value="file">File:</option><option value="live_file">Live File:</option></select></td>\n';
    html+= '    <td><input id="entrylist" style="width:'+((Editor.ie) ? '294px' : '100%')+'" type="text"/></td>\n';
    html+= '    <td style="width:120px;text-align:right;"><input type="button" id="entrylist_add" class="entrylist_button" value="Add File" onclick="self.opener.Editor.entrylist_action_add();"/></td>\n';
    html+= '  </tr>\n';
    html+= '  <tr height="16px">\n';
    html+= '    <td colspan="8"><hr/></td>\n';
    html+= '  </tr>\n';
    html+= '  <tr>\n';
    html+= '    <td valign="top" colspan="2"><select class="multiple" id="entrylist_dates" style="width:'+((Editor.ie) ? 366 : 356)+'px; height:'+sHeight+'px; overflow:auto;" size="30" onchange="self.opener.Editor.entrylist_action_enable_btn_remove();"></select></td>\n';
    html+= '    <td style="text-align:right;vertical-align:top"><input type="button" id="entrylist_remove" class="entrylist_button" value="Remove File" onclick="self.opener.Editor.entrylist_action_remove();"/></td>\n';
    html+= '  </tr>\n';
    html+= '</table>\n';
    
    html+= '</fieldset>';
    html+= '\n';
    
    return html;
    
  } catch(x) {
    throw new Error("Editor.entrylist_getForm("+caption+"):\n" + x.message);
  }
}


Editor.entrylist_init = function() {
  
  // Button "Remove Date" beim Start deaktivieren
  Editor.remote.document.getElementById("entrylist_remove").disabled = "disabled";
  
  // Auswahlliste (select) aktualisieren
  Editor.entrylist_show(DOMManager.tree["holiday_includes"]);
}


Editor.entrylist_action_add = function() {
  
  var file = Editor.remote.document.getElementById("entrylist").value;
  var file_type = Editor.remote.document.getElementById("entrylist_file_type").value;
  if ( file=='' ) return; // leer?
  
  file = file_type + ": " + file;
  // aktuellen Wert des inputfeld dem tree hinzufuegen
  if ( DOMManager.tree["holiday_includes"]==null )
    DOMManager.tree["holiday_includes"] = new Array();
  
  // existiert der Name schon?
  for (i=0;i<DOMManager.tree["holiday_includes"].length;i++) {
    if ( DOMManager.tree["holiday_includes"][i]==file ) {
      Editor.remote.Alert("Include file ["+file+"] already exists");
      return;
    }
  }
  
  DOMManager.tree["holiday_includes"][DOMManager.tree["holiday_includes"].length] = file;
  DOMManager.tree["holiday_includes"].sort();
  
  // Auswahlliste (select) aktualisieren
  Editor.entrylist_show(DOMManager.tree["holiday_includes"]);
  
  // Status des Eingabefeldes wiederherstellen
  Editor.remote.document.getElementById("entrylist").value = '';
  Editor.remote.document.getElementById("entrylist").focus();
}


Editor.entrylist_show = function(list) {
  
  // gesamten Inhalt anfangs loeschen
  Editor.remote.document.getElementById("entrylist_dates").options.length = 0;
  
  if ( list!=null && list.length > 0 )
  Editor.remote.add_option_tags(list, list, Editor.remote.document.getElementById("entrylist_dates"));
}


Editor.entrylist_action_remove = function() {
  
  try {
    
    if (Editor.remote.document.getElementById("entrylist_dates").options.length == 0) return;
    if (Editor.remote.document.getElementById("entrylist_dates").selectedIndex < 0) return;
    
    var index = Editor.remote.document.getElementById("entrylist_dates").selectedIndex;
    var tree = DOMManager.tree["holiday_includes"];
    
    for (var i=index; i<tree.length-1; i++) {
      tree[i] = tree[i+1];
    }
    
    tree.length = tree.length-1;
    
    Editor.entrylist_show(tree);
    
    if (index > Editor.remote.document.getElementById("entrylist_dates").options.length-1) index--;
    if ( index >= 0 )
      Editor.remote.document.getElementById("entrylist_dates").options[index].selected = true;
    
  } catch(e) {
    Editor.remote.alertError("Editor.entrylist_action_remove():", e);
  }
}


Editor.entrylist_action_enable_btn_remove = function() {
  
  Editor.remote.document.getElementById("entrylist_remove").disabled = "";
}



Editor.daylist_getForm = function(key, mode, sHeight) {
  
  var caption;
  var width      = 374;
  var height     = (Editor.ie) ? 443 : 441;
  var call_key   = (!key) ? null : '\''+key+'\'';
  var with_group = '';
  
  switch(mode) {
    case "holidays":
        caption = 'Weekday';
        width   = (Editor.ie) ? 366 : 356;
        height  = sHeight/0.3;
        with_group = ' style="display:none;"';
        mode    = "weekdays";
    break;
    case "weekdays":
        caption = 'Weekday';
    break;
    case "monthdays":
        caption = 'Monthday';
    break;
    case "ultimos":
        caption = 'Ultimo';
    break;
    case "month":
        caption = 'Month';
    break;
  }
  
  var border = (Editor.ie) ? 'groove 2px' : 'threedface groove 2px';
  
  var html = '  \n';
  html+= '  <fieldset>\n';
  html+= '    <legend>'+caption+'s</legend>\n';
  html+= '      <table style="width:100%;" cellspacing="0" cellpadding="0" border="0">\n';
  html+= '        <tr height="24px">\n';
  html+= '          <td><table style="width:'+width+';" cellspacing="0" cellpadding="0"><tr> ';
  html+= '            <td style="width:1%;padding-right:4px;">'+caption+':</td>\n';
  html+= '            <td width="99%">\n';
  html+= '              <select id="daylist_days" style="width:100%;" size="1">\n';
  var selectedOpt = " selected"
  for(var opt in Editor[mode]) {
    html+= '                <option'+selectedOpt+' value="' + opt + '" >' + Editor[mode][opt] + '</option>\n';
    selectedOpt = "";
  }
  html+= '              </select>\n';
  html+= '            </td></tr></table>\n';
  html+= '            </td>\n';
  html+= '          <td style="width:120px;text-align:right;"><input id="daylist_add_day" type="button" class="daylist_button" value="Add '+caption+'" onclick="self.opener.Editor.daylist_action_add_day('+call_key+');"/></td>\n';
  html+= '        </tr>\n';
  html+= '        <tr height="16px">\n';
  html+= '          <td colspan="2"><hr/></td>\n';
  html+= '        </tr>\n';
  html+= '          <td valign="top" style="padding:0px;">\n';
  html+= '            <select class="multiple" id="daylist_selected_days" size="32" style="width:'+width+'px;height:'+(height*0.3)+'; overflow:auto;" onclick="self.opener.Editor.daylist_action_select_day();"></select>\n'; 
  html+= '          </td>\n';
  html+= '          <td style="text-align:right;vertical-align:top;"><input id="daylist_remove_day" type="button" disabled class="daylist_button" value="Remove '+caption+'" onclick="self.opener.Editor.daylist_action_remove_day('+call_key+');"/></td>\n';
  html+= '        </tr>\n';
  html+= '        <tr'+with_group+' height="16px">\n';
  html+= '          <td colspan="2"><hr/></td>\n';
  html+= '        </tr>\n';
  html+= '        <tr'+with_group+'>\n';
  html+= '          <td valign="top" style="padding:0px;">\n';
  html+= Editor.show_groupping(mode, width, (height*0.7)-12);
  html+= '          </td>\n';
  html+= '          <td style="text-align:right;vertical-align:top;">\n';
  html+= '            <input id="new_group"   type="button" class="daylist_button" value="New Group"   onclick="self.opener.Editor.daylist_action_new_group();"/><br/>\n';
  html+= '            <input id="apply_group" type="button" class="daylist_button" value="Apply Group" onclick="self.opener.Editor.daylist_action_apply_group('+call_key+');" style="position:relative;top:4px;" disabled="true"/>\n';
  html+= '          </td>\n';
  html+= '        </tr>\n';
  html+= '      </table>\n';
  html+= '  </fieldset> <!-- daylist -->\n';
  html+= '  \n';
  
  return html;
}

Editor.show_groupping = function(mode, width, height)
{
  try {
    var s = "";
    s += '<table cellspacing="0" cellpadding="0" border="0" style="width:'+width+'px;">\n';
    s += '  <tr>\n';
    s += '    <td width="'+((width-58)/2)+'" style="padding:0px;">\n';
    s += '      <select id="src_select" name="src_select" size="32" style="width:100%;height:'+ height + 'px" multiple="true" disabled="true" ondblclick="self.opener.Editor.modify_target_selectbox(\''+mode+'\',\'single_add\')">\n';
    for( var opt in Editor[mode] ) {
      s += '        <option value="' + opt + '">' + Editor[mode][opt] + '</option>\n';
    }
    s += '      </select>\n';
    s += '    </td>\n';
    s += '    <td valign="middle" width="54" style="padding:0px 2px;">\n';
    s += '      <input type="button" id="add_select" class="daylist_button" style="width:54px;position:relative;top:-2px;" value="Add"    onclick="self.opener.Editor.modify_target_selectbox(\''+mode+'\',\'add\')"    disabled="true"/><br/>\n';
    s += '      <input type="button" id="del_select" class="daylist_button" style="width:54px;position:relative;top:2px;"  value="Remove" onclick="self.opener.Editor.modify_target_selectbox(\''+mode+'\',\'remove\')" disabled="true"/>\n';
    s += '    </td>\n';
    s += '    <td width="'+((width-58)/2)+'" style="padding:0px;">\n';
    s += '      <select id="target_select" name="target_select" size="32" style="width:100%;height:'+ height + 'px" multiple="true" ondblclick="self.opener.Editor.modify_target_selectbox(\''+mode+'\',\'single_remove\')">\n';
    s += '      </select>\n';
    s += '    </td>\n';
    s += '  </tr>\n';
    s += '</table>\n';
  
    return s;
  } catch(x) {
    throw new Error("Editor.show_groupping():\n" + x.message);
  } 
}

Editor.modify_target_selectbox = function( mode, operation ) {
  
  try {   
    var srcObj      = Editor.remote.document.getElementById('src_select'); 
    var targetObj   = Editor.remote.document.getElementById('target_select');
      
    switch( operation ) {
      case 'single_add'   : if(srcObj.selectedIndex > -1 ) {
                              var opt = srcObj.options[srcObj.selectedIndex];
                              if( Editor.modify_select_plausi(mode, opt.value, targetObj) ) {
                                Editor.remote.add_option_tag(opt.text, opt.value, targetObj);
                              }
                            }
                            break;
      case 'add'          : if(srcObj.selectedIndex > -1 ) {
                              for(var i=0; i < srcObj.length; i++ ) {
                                if(srcObj.options[i].selected && Editor.modify_select_plausi(mode, srcObj.options[i].value, targetObj)) {
                                  Editor.remote.add_option_tag(srcObj.options[i].text, srcObj.options[i].value, targetObj);
                                }
                              }
                            }
                            break;
      case 'single_remove': if(targetObj.selectedIndex > -1) {
                              targetObj.options[targetObj.selectedIndex] = null;
                            }
                            break;
      case 'remove'       : if(targetObj.selectedIndex > -1) {
                              for(var i = targetObj.length-1; i >= 0; i-- ) {
                                if(targetObj.options[i].selected) targetObj.options[i] = null;
                              }
                            }
                            break;
    }
    Editor.remote.document.getElementById('del_select').disabled  = (targetObj.length == 0);
    Editor.remote.document.getElementById('apply_group').disabled = (targetObj.length == 0);
  } catch(E) {
    Editor.remote.alertError("Editor.modify_target_selectbox():", E);
  }   
}


Editor.modify_select_plausi = function( mode, val, selectObj ) {
  
  try {
    var found = false;
    for( var i=0; i < selectObj.length; i++ ) {
      if( selectObj.options[i].value == val ) {
        found = true;
        break;
      }
    }
    return ( !found );
  } catch(x) {
    throw new Error("Editor.modify_select_plausi():\n" + x.message);
  }
}


Editor.daylist_init = function(key, mode) {

  try {
    // gueltige modi: "weekdays", "monthdays" oder "ultimos"
    if ( mode!="holiday_weekdays" && mode!="weekdays" && mode!="monthdays" && mode!="ultimos" && mode!="month" )
      throw new Error("invalid mode parameter '" + mode + "'");
    
    if ( !Editor[mode] ) throw new Error("Editor."+mode+" is undefined");
    var tree          = (key != "month") ? DOMManager.tree[mode] : DOMManager.tree[key][Editor["cur_"+key]][mode];
    
    if ( !tree ) tree = new Object();
    
    var daylist_days           = Editor.remote.document.getElementById("daylist_days");
    var daylist_selected_days  = Editor.remote.document.getElementById("daylist_selected_days");
    daylist_selected_days.options.length = 0;

    for (var entry in tree) {
      if( mode == "month" ) Editor.remote.add_option_tag(Editor.getMonthNames(entry), entry, daylist_selected_days);
      else Editor.remote.add_option_tag(tree[entry], entry, daylist_selected_days);
      if ( typeof Editor[mode][entry] == 'string' ) {
        for( var i=0; i < daylist_days.length; i++ ) {
          if( entry == daylist_days.options[i].value ) {
            daylist_days.options[i] = null;
            break;
          }
        }
      }
    }

  } catch(x) {
    throw new Error("Editor.daylist_init():\n" + x.message);
  }
}


Editor.getMonthNames = function(entry) {
  
  var entries = entry.split(" ");
  for(var i=0; i < entries.length; i++) entries[i] = Editor.month[entries[i]];
  return entries.join(" ");
}


Editor.daylist_action_add_day = function( key, apply_group ) {

  try { 
    
    var tree                  = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
    var mode                  = (Editor.mode != "holidays") ? Editor.mode : "holiday_weekdays";
    var daylist_days          = Editor.remote.document.getElementById("daylist_days");
    var daylist_selected_days = Editor.remote.document.getElementById("daylist_selected_days");
      
    if( typeof apply_group != 'object' ) {
      Editor.update_groupping = null;
      if ( daylist_days.options.length == 0 ) return false;
      var val = daylist_days.value;
      var txt = daylist_days.options[daylist_days.selectedIndex].text;
    } else {
      var val = apply_group.value;
      var txt = apply_group.text;
      if( !Editor.modify_select_plausi( mode, val, daylist_selected_days ) ) {
        Editor.remote.Alert("A group '" + txt + "' is already exist.");
        return false;
      }
    }
    if( Editor.update_groupping ) {
      daylist_selected_days.options[Editor.update_groupping.index].value = val; 
      daylist_selected_days.options[Editor.update_groupping.index].text  = txt; 
      //if ( tree[mode] && tree[mode][Editor.update_groupping.value] ) {
      //  tree[mode][val] = txt;
      //  delete tree[mode][Editor.update_groupping.value];
      //}
      if(mode == 'month') {
        for( var entry in tree[mode] ) {
          var entryObj = tree[mode][entry];
          delete tree[mode][entry];
          if( entry == Editor.update_groupping.value ) {
            tree[mode][val] = entryObj;
          } else {
            tree[mode][entry] = entryObj;
          }
        }       
      } else {
        for( var entry in tree[mode] ) {
          var entryTxt = tree[mode][entry];
          delete tree[mode][entry];
          if( entry == Editor.update_groupping.value ) {
            tree[mode][val] = txt;
          } else {
            tree[mode][entry] = entryTxt;
          }
        }
        //Periode aktualisieren
        if ( tree[mode+"_items"] && tree[mode+"_items"][Editor.update_groupping.value] ) {
          tree[mode+"_items"][val] = tree[mode+"_items"][Editor.update_groupping.value];
          delete tree[mode+"_items"][Editor.update_groupping.value];
        }
      }
      Editor.update_groupping = null;
    } else {
      var inserted     = new Array();
      var insertedTxt  = new Object();
      for (var i=0; i<daylist_selected_days.length; i++) {
        inserted.push( daylist_selected_days.options[i].value );
        insertedTxt[daylist_selected_days.options[i].value] = daylist_selected_days.options[i].text;
      }
      inserted.push( val );
      insertedTxt[val] = txt;
      
      //inserted.sort(Editor.GrouppedDaysSort);
      
      // daylist_selected_days gesamten Inhalt voruebergehend loeschen und neu aufbauen
      daylist_selected_days.options.length = 0;
      for (var i=0; i<inserted.length; i++) {
        Editor.remote.add_option_tag(insertedTxt[inserted[i]], inserted[i], daylist_selected_days);
      }
      
      if ( tree[mode]==null )  tree[mode] = new Object();
      if(mode == 'month') tree[mode][val] = new Object();
      else tree[mode][val] = txt;
    }
    //TODO Sortierung des DOMManager.tree[mode]
    // Navigationsbaum updaten
    Editor.NaviTreeUpdate(key, mode);

    // Eintrag aus der Drop-Down Liste entfernen
    for( var i=0; i < daylist_days.length; i++ ) {
      if( daylist_days.options[i].value == val ) {
        daylist_days.options[i] = null;
        break;
      }
    }

    if ( daylist_days.options.length == 0 ) {
      Editor.remote.document.getElementById("daylist_add_day").disabled = "disabled";
    } else if( typeof apply_group != 'object' ) daylist_days.options[0].selected = true;
    
    
    Editor.remote.document.getElementById('target_select').options.length = 0;
    Editor.remote.document.getElementById('src_select').disabled          = true;
    Editor.remote.document.getElementById('src_select').selectedIndex     = -1;
    Editor.remote.document.getElementById('add_select').disabled          = true;
    Editor.remote.document.getElementById('del_select').disabled          = true;
    Editor.remote.document.getElementById('apply_group').disabled         = true;
    
    return true;  
  } catch(e) {
    Editor.remote.alertError("Editor.daylist_action_add_day():", e);
  }
}


Editor.daylist_action_select_day = function() {
  
  try {   
    var mode                  = Editor.mode == "holidays" ? "holiday_weekdays" : Editor.mode;
    var daylist_selected_days = Editor.remote.document.getElementById("daylist_selected_days");
    if (daylist_selected_days.length == 0 || daylist_selected_days.selectedIndex < 0) return;
    
    var targetObj             = Editor.remote.document.getElementById('target_select');
    var opt                   = daylist_selected_days.options[daylist_selected_days.selectedIndex];
    Editor.update_groupping   = {index:daylist_selected_days.selectedIndex, value:opt.value, text:opt.text};
    var keys                  = opt.value.split(" ");
    
    targetObj.options.length  = 0;
    for( var i=0; i < keys.length; i++ ) {
      Editor.remote.add_option_tag(Editor[mode][keys[i]], keys[i], targetObj);
    }
    Editor.remote.document.getElementById('src_select').disabled         = false;
    Editor.remote.document.getElementById('src_select').selectedIndex    = -1;
    Editor.remote.document.getElementById('add_select').disabled         = false;
    Editor.remote.document.getElementById('del_select').disabled         = false;
    Editor.remote.document.getElementById('apply_group').disabled        = true;
    Editor.remote.document.getElementById("daylist_remove_day").disabled = false;
    
  } catch(e) {
    Editor.remote.alertError("Editor.daylist_action_select_day():", e);
  }
}


Editor.daylist_action_remove_day = function(key) {

  try {
    
    Editor.update_groupping   = null;
    var tree                  = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
    var mode                  = Editor.mode == "holidays" ? "holiday_weekdays" : Editor.mode;
    var daylist_days          = Editor.remote.document.getElementById("daylist_days");
    var daylist_selected_days = Editor.remote.document.getElementById("daylist_selected_days");
    
    if (daylist_selected_days.length == 0 || daylist_selected_days.selectedIndex < 0) return;
    var index = daylist_selected_days.selectedIndex;
    var val   = daylist_selected_days[index].value;
    
    if ( tree[mode+"_items"] && tree[mode+"_items"][val] ) {
      delete tree[mode+"_items"][val];
    }
    if ( tree[mode] && tree[mode][val] ) {
      delete tree[mode][val];
    }
    // Navigationsbaum updaten
    Editor.NaviTreeUpdate(key, mode);
    // Gruppe aus target_select loeschen
    Editor.remote.document.getElementById('target_select').options.length  = 0;
    
    if( val.split(" ").length == 1 ) { //wenn geloeschter Eintrag keine Gruppe ist
      var inserted = new Array();
      for (var i=0; i<daylist_days.length; i++) inserted.push( daylist_days.options[i].value );
      inserted.push( val );
      inserted.sort(Editor.NumSort);
      // gesamten Inhalt voruebergehend loeschen
      daylist_days.options.length = 0;

      for (var i=0; i<inserted.length; i++) {
        Editor.remote.add_option_tag(Editor[mode][inserted[i]], inserted[i], daylist_days);
      }      
    }
    // Eintrag aus der Selectbox entfernen
    daylist_selected_days.options[index] = null;

    Editor.remote.document.getElementById("daylist_add_day").disabled     = (daylist_days.length == 0);
    Editor.remote.document.getElementById("daylist_remove_day").disabled  = (daylist_selected_days.length == 0);
    Editor.remote.document.getElementById('target_select').options.length = 0;
    Editor.remote.document.getElementById('src_select').disabled          = true;
    Editor.remote.document.getElementById('src_select').selectedIndex     = -1;
    Editor.remote.document.getElementById('add_select').disabled          = true;
    Editor.remote.document.getElementById('del_select').disabled          = true;
    Editor.remote.document.getElementById('apply_group').disabled         = true;
    
    if (index > daylist_selected_days.length-1) index--;
    if (daylist_selected_days.length > 0)  daylist_selected_days.options[index].selected = true;
  } catch(e) {
    Editor.remote.alertError("Editor.daylist_action_remove_day():", e);
  }
}


Editor.daylist_action_new_group = function() {
  
  try {
    Editor.update_groupping = null;
    Editor.remote.document.getElementById('target_select').options.length = 0;
    Editor.remote.document.getElementById('src_select').disabled          = false;
    Editor.remote.document.getElementById('src_select').selectedIndex     = -1;
    Editor.remote.document.getElementById('add_select').disabled          = false;
    Editor.remote.document.getElementById('del_select').disabled          = true;
    Editor.remote.document.getElementById('apply_group').disabled         = true;
  } catch(e) {
    Editor.remote.alertError("Editor.daylist_action_new_group():", e);
  }
}


Editor.daylist_action_apply_group = function(key) {
  
  try {
    var mode                  = Editor.mode;
    var targetObj             = Editor.remote.document.getElementById('target_select');
    var apply_group           = { value:"", text:"" };
    var values                = new Array();
    for(var i=0; i < targetObj.length; i++ ) {
      values.push( targetObj.options[i].value );
    }
    values.sort(Editor.NumSort);
    apply_group.value = values.join(" ");
    for(var i=0; i < values.length; i++ ) { 
      apply_group.text       += Editor[mode][values[i]] + " "; 
    }
    apply_group.text         = apply_group.text.replace( /\s+$/,'' ); //rtrim
    return Editor.daylist_action_add_day( key, apply_group );
  } catch(e) {
    Editor.remote.alertError("Editor.daylist_action_apply_group():", e);
  }
}

Editor.showDay = function(specifier, index, key) {
  
  try {
    
    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(key);
      }
    }
    
    if ( typeof specifier=='undefined') throw new Error("undefined parameter specifier");
    if ( specifier!="everyday" && typeof index=='undefined' ) throw new Error("missing parameter index");
    
    var id = "";
    switch ( specifier ) {
      case "everyday":
        id = "everyday";
        break;

      case "weekdays":
        id = "weekdays_items";
        break;

      case "monthdays":
        id = "monthdays_items";
        break;

      case "ultimos":
        id = "ultimos_items";
        break;

      case "specificdays":
        id = "specificdays_dates";
        break;
      
      case "specific_weekdays":
        id = "specific_weekdays";
        break;
      
      default:
        throw new Error("invalid specifier \'"+specifier+"\'");
    }
    
    Editor.mode = specifier;
    Editor.mode_index = String(index);
    Editor.ok_set_enabled( true );
    
    var tree = (key != "month") ? DOMManager.tree[id] : DOMManager.tree[key][Editor["cur_"+key]][id];
    // kann null sein
    
    // nur everyday braucht keinen weiteren index
    if ( specifier!="everyday" && tree!=null )
      tree = tree[String(index)];
    
    // falls das Element nicht existiert
    if ( typeof tree=='undefined' ) tree = null;
    
    // Teilbaum global verfuegbar machen
    Editor.period_currentPeriodTree = tree; // darf auch null sein
    
    Editor.gui = "";
    
    Editor.gui += Editor.periodlist_getForm(key, 0, 0, 508, 508);
    
    Editor.remote.document.getElementById("gui").innerHTML = Editor.gui;
    
    Editor.period_init(null, false);
    Editor.periodlist_init( tree );
    
  } catch (x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showDay():", x);
    else
      throw new Error("Editor.showDay():\n" + x.message);
  }
}


Editor.periodlist_init = function(tree) {

  try {

    if ( typeof tree=='undefined' ) throw new Error("parameter tree is undefined");

    // minimale Anzahl an freien Eintraegen wurde schon in Editor.periodlist_getForm() erzeugt
    Editor.periodlist_entries = Editor.periodlist_minEntries;
    Editor.periodlist_periods = 0;
    Editor.periodlist_highlightedRow = -1;
    
    // Eintraege laden
    if ( tree!=null ) {
      for (var i=0; i<tree.length; i++)
        Editor.periodlist_addPeriod( tree[i] );
    }

    Editor.periodlist_btn_apply_period_set_enabled(false);

  } catch(x) {
    throw new Error("Editor.periodlist_init():\n" + x.message);
  }
}


// eine Zeile markieren und die Periode in das period Formular laden
Editor.periodlist_highlightRow = function(index) {

  var tr;

  try {

    if ( index < Editor.periodlist_periods ) {

      // es existiert schon eine markierte Reihe => Markierung entfernen
      if ( Editor.periodlist_highlightedRow >= 0 ) {

        tr = Editor.remote.document.getElementById("tr_"+Editor.periodlist_highlightedRow);
        tr.style.backgroundColor = "white";
        tr.style.color = "black";
      }

      // Markierung setzen
      Editor.periodlist_highlightedRow = index;

      tr = Editor.remote.document.getElementById("tr_"+index);
      tr.style.backgroundColor = "highlight";
      tr.style.color = "white";

      Editor.periodlist_btn_remove_period_set_enabled(true);
      Editor.periodlist_btn_apply_period_set_enabled(false);

      Editor.period_enable(true);

      // Referenz auf die aktuelle Periode
      Editor.period_currentPeriod = DOMManager.clonePeriod( Editor.period_currentPeriodTree[index] );
      
      // <at>
      if (Editor.period_currentPeriodTree[index].isAT )
        Editor.period_currentPeriod.isAT = true;
      
      Editor.period_fillForm( Editor.period_currentPeriod );
    }

  } catch(x) {
    throw new Error("Editor.periodlist_highlightRow():\n" + x.message);
  }
}

Editor.periodlist_remove_highlighting = function() {

  try {

    if ( Editor.periodlist_highlightedRow >= 0 ) {

      tr = Editor.remote.document.getElementById("tr_"+Editor.periodlist_highlightedRow);
      tr.style.backgroundColor = "white";
      tr.style.color = "black";

      Editor.periodlist_btn_remove_period_set_enabled(false);

      Editor.periodlist_highlightedRow = -1;
    }

  } catch(x) {
    throw new Error("Editor.periodlist_remove_highlighting():\n" + x.message);
  }
}

Editor.periodlist_btn_apply_period_set_enabled = function(state) {

  Editor.remote.document.getElementById("periodlist_apply_period").disabled = (state) ? "" : "disabled";
}

Editor.periodlist_btn_remove_period_set_enabled = function(state) {

  Editor.remote.document.getElementById("periodlist_remove_period").disabled = (state) ? "" : "disabled";
}

Editor.periodlist_action_apply_period = function(key) {
  
  try {
    
    if ( Editor.period_currentPeriod==null )
      throw new Error("Editor.period_currentPeriod is null");

    if ( !(Editor.mode=="run_time" || Editor.mode=="everyday" ||
           Editor.mode=="weekdays" || Editor.mode=="monthdays" ||
           Editor.mode=="ultimos"  || Editor.mode=="specificdays" ||
           Editor.mode=="month"    || Editor.mode=="specific_weekdays") ) {
      throw new Error("Editor.mode is invalid: Editor.mode=\""+Editor.mode+"\"");
    }

    if ( !Editor.period_currentPeriod_isAttachable )
      Editor.remote.Alert("Cannot apply period.\nThe current period is not valid.");

    var doc = Editor.remote.document;

    if ( Editor.period_currentPeriod["run_once"]!=null )
      Editor.period_currentPeriod["run_once"] = doc.getElementById("period_run_once").checked;
      
    Editor.period_currentPeriod["when_holiday"] = doc.getElementById("period_when_holiday").value;

    singlestartHHElem = doc.getElementById("period_singlestart_hh");
    singlestartMMElem = doc.getElementById("period_singlestart_mm");
    singlestartSSElem = doc.getElementById("period_singlestart_ss");

    var singlestartHH = singlestartHHElem.value;
    var singlestartMM = singlestartMMElem.value;
    var singlestartSS = singlestartSSElem.value;

    if ( singlestartHH=="" && singlestartMM=="" && singlestartSS=="" ) {

      Editor.period_currentPeriod["singlestart_hh"] = "";
      Editor.period_currentPeriod["singlestart_mm"] = "";
      Editor.period_currentPeriod["singlestart_ss"] = "";

      Editor.period_currentPeriod["let_run"] = doc.getElementById("period_let_run").checked;

      for (var i=0; i<Editor.attribute_indices.length-3; i++) {
        Editor.period_currentPeriod[ Editor.attribute_indices[i] ] = doc.getElementById("period_" + Editor.attribute_indices[i]).value;
      }

    } else {
      Editor.period_currentPeriod["singlestart_hh"] = doc.getElementById("period_singlestart_hh").value;
      Editor.period_currentPeriod["singlestart_mm"] = doc.getElementById("period_singlestart_mm").value;
      Editor.period_currentPeriod["singlestart_ss"] = doc.getElementById("period_singlestart_ss").value;

      Editor.period_currentPeriod["let_run"] = false;

      for (var i=0; i<Editor.attribute_indices.length-3; i++) {
        Editor.period_currentPeriod[ Editor.attribute_indices[i] ] = "";
      }
    }
    
    // Periode in den Baum einfuegen
    if ( Editor.mode=="run_time" ) {
      DOMManager.tree[Editor.root] = Editor.period_currentPeriod;
      
    } else {
      
      var domManagerTree = (key != "month") ? DOMManager.tree : DOMManager.tree[key][Editor["cur_"+key]];
      index = String(Editor.mode_index);
      var tree = null;

      switch ( Editor.mode ) {
        case "everyday":
          if ( domManagerTree["everyday"]==null || typeof domManagerTree["everyday"]=='undefined' )
            domManagerTree["everyday"] = new Array();

          tree = domManagerTree["everyday"];
          break;

        //case "month":
        case "ultimos":
        case "monthdays":
        case "weekdays":
          if ( domManagerTree[Editor.mode+"_items"]==null || typeof domManagerTree[Editor.mode+"_items"]=='undefined' )
            domManagerTree[Editor.mode+"_items"] = new Object();

          if ( domManagerTree[Editor.mode+"_items"][index]==null || typeof domManagerTree[Editor.mode+"_items"][index]=='undefined' )
            domManagerTree[Editor.mode+"_items"][index] = new Array();

          tree = domManagerTree[Editor.mode+"_items"][index];
          break;
          
        case "specificdays":
          if ( domManagerTree["specificdays_dates"]==null || typeof domManagerTree["specificdays_dates"]=='undefined' )
            domManagerTree["specificdays_dates"] = new Object();

          if ( domManagerTree["specificdays_dates"][index]==null || typeof domManagerTree["specificdays_dates"][index]=='undefined' )
            domManagerTree["specificdays_dates"][index] = new Array();

          tree = domManagerTree["specificdays_dates"][index];
          break;
          
        case "specific_weekdays":
          if ( domManagerTree["specific_weekdays"]==null || typeof domManagerTree["specific_weekdays"]=='undefined' ) {
            domManagerTree["specific_weekdays"] = new Object();
            domManagerTree["specific_weekdays"].days = new Array();
          }
          
          if ( domManagerTree["specific_weekdays"][index]==null || typeof domManagerTree["specific_weekdays"][index]=='undefined' ) {
            domManagerTree["specific_weekdays"][index] = new Array();
            domManagerTree["specific_weekdays"].days.push(index);
          }
          
          tree = domManagerTree["specific_weekdays"][index];
          break;
      }

      if ( Editor.periodlist_highlightedRow >= 0 ) {
        tree[Editor.periodlist_highlightedRow] = Editor.period_currentPeriod;

        var period = tree[Editor.periodlist_highlightedRow];
        var fields = new Array();
        fields[0] = ( period["let_run"]==true ) ? "Yes" : "No";
        fields[1] = DOMManager.getTimestamp(period["begintime_hh"],period["begintime_mm"],period["begintime_ss"]);
        fields[2] = DOMManager.getTimestamp(period["endtime_hh"],period["endtime_mm"],period["endtime_ss"]);
        fields[3] = DOMManager.getTimestamp(period["repeattime_hh"],period["repeattime_mm"],period["repeattime_ss"]);
        fields[4] = DOMManager.getTimestamp(period["absoluterepeat_hh"],period["absoluterepeat_mm"],period["absoluterepeat_ss"]);
        fields[5] = DOMManager.getTimestamp(period["singlestart_hh"],period["singlestart_mm"],period["singlestart_ss"]);
        fields[6] = Editor.when_holiday_list[period["when_holiday"]];
        for (var i=0; i<fields.length; i++)
          if ( fields[i]=="" ) fields[i] = "&nbsp;";
        
        Editor.periodlist_editEntry(Editor.periodlist_highlightedRow, fields);
        Editor.periodlist_remove_highlighting();

      } else {

        tree[tree.length] = Editor.period_currentPeriod;
        Editor.periodlist_addPeriod( tree[tree.length-1] );
      }

      Editor.periodlist_btn_apply_period_set_enabled(false);
      Editor.period_enable(false);

      // period tree muss neu gesetzt werden
      switch ( Editor.mode ) {
        case "everyday"    : Editor.period_currentPeriodTree = domManagerTree["everyday"];break;
        case "weekdays"    : Editor.period_currentPeriodTree = domManagerTree["weekdays_items"][index];break;
        case "monthdays"   : Editor.period_currentPeriodTree = domManagerTree["monthdays_items"][index];break;
        case "ultimos"     : Editor.period_currentPeriodTree = domManagerTree["ultimos_items"][index];break;
        case "specificdays": Editor.period_currentPeriodTree = domManagerTree["specificdays_dates"][index];break;
        //case "month"       : Editor.period_currentPeriodTree = domManagerTree["month_items"][index];break;
      }

    }
    
    return true;
    
  } catch(e) {
    msg =  ( Editor.debug ) ? "Editor.periodlist_action_apply_period():\n" : "";
    Editor.remote.alertError( msg + "Cannot apply period", e);
    return false;
  }
}

Editor.periodlist_action_new_period = function() {

  try {

    Editor.periodlist_remove_highlighting();
    Editor.period_enable(true);
    Editor.period_currentPeriod = DOMManager.createNewPeriod();
    Editor.period_fillForm( Editor.period_currentPeriod );
    Editor.periodlist_btn_apply_period_set_enabled(true);
    Editor.period_currentPeriod_isAttachable = true;

  } catch(e) {
    Editor.remote.alertError("Editor.periodlist_action_new_period():", e);
  }
}

Editor.periodlist_action_remove_period = function( cols ) {

  try {

    if ( Editor.periodlist_highlightedRow >= 0 ) {
      Editor.periodlist_removePeriod( Editor.periodlist_highlightedRow, cols );
    }
    
    var hRow = Editor.periodlist_highlightedRow;
    Editor.periodlist_highlightedRow = -1;
    
    if ( Editor.periodlist_periods > 0 ) {
      
      if ( hRow==Editor.periodlist_periods )
        hRow--;
      
      Editor.periodlist_highlightRow( hRow );
      
    } else {
      Editor.periodlist_btn_remove_period_set_enabled(false);
      Editor.period_enable(false);
      Editor.period_clearForm();
    }
    
  } catch(e) {
    Editor.remote.alertError("Editor.periodlist_action_remove_period():", e);
  }
}

Editor.periodlist_width = new Array("10%", "14%", "14%", "14%", "14%", "14%", "20%");

Editor.periodlist_getForm = function(key, top, left, width, height) {
  
  var border = ( Editor.ie ) ? '' : 'threedface ';
  var header = new Array("Let<br/>Run", "Begin", "End", "Repeat", "Absolute<br/>Repeat", "Single<br/>Start", "When<br/>Holiday");
  var html = '    \n';
  html+= '    <fieldset>\n';
  html+= '      <legend>Periods</legend>\n';
  html+= Editor.period_getForm(true,key);
  //html+= '      <fieldset style="margin-top:4px;padding-top:4px;">\n';
  html+= '      <div style="clear:left;'+(Editor.ie?'':'padding-top:2px;')+'">\n';
  html+= '      <hr/>\n';
  
  html+= '        <div id="periodlist_list" style="float:left;width:'+(Editor.ie?380:376)+'px; height:'+((Editor.ie)?240:251)+'px; border:'+border+'inset 1px;">\n';
  html+= '            <table id="periodlist_th" border="0" cellspacing="0" cellpadding="2" style="width:100%;background-color:white">\n';
  
  html+= '              <tr>\n';
  for( i=0; i < Editor.periodlist_width.length; i++ ) {
    html+= '                <td id="periodlist_th'+(i+1)+'" style="padding:0px 0px 2px;text-align:center;font-size:11px;height:11px;line-height:11px;background-color:'+Editor.bgColor+'; width:'+Editor.periodlist_width[i]+'; border:'+border+'outset 1px;">'+header[i]+'</td>\n';
  }
  html+= '                <td id="dummy" style="background-color:'+Editor.bgColor+'; border:'+border+'outset 1px;">&nbsp;&nbsp;&nbsp;&nbsp;</td>\n';
  html+= '              </tr>\n';
  html+= '            </table>\n';
  
  html+= '            <div id="periodlist_td_div" style="width:'+(Editor.ie?378:374)+';height:'+((Editor.ie)?222:225)+'px; background-color:white; overflow:auto;">\n';
  html+= '              <table id="periodlist_td" style="width:100%;background-color:white;border-collapse:collapse" cellspacing="0" cellpadding="2" border="0">\n';
  
  for (var i=0; i<Editor.periodlist_minEntries; i++) {
    html += '<tr id="tr_'+i+'" onclick="self.opener.Editor.periodlist_highlightRow( this.id.substring(3) )">';
    for( var j=0; j < Editor.periodlist_width.length; j++ ) {
      html += '<td id="td_'+i+'_'+j+'" width="'+Editor.periodlist_width[j]+'" class="period">&nbsp;</td>';
    }
    html += '</tr>';
  }

  html+= '</table>\n'; // textnode unter FF vermeiden
  html+= '            </div>\n';
  html+= '        </div>\n';

  html+= '        <div style="text-align:right;padding-left:4px;">\n';
  html+= '                <input id="periodlist_new_period" type="button" class="periodlist_button" value="New Period" onclick="self.opener.Editor.periodlist_action_new_period();"/><br/>\n';
  //html+= '                <div style="height:4px;">&nbsp;</div>\n';
  html+= '                <input id="periodlist_remove_period" type="button" disabled class="periodlist_button" style="position:relative;top:4px;" value="Remove Period" onclick="self.opener.Editor.periodlist_action_remove_period('+Editor.periodlist_width.length+');"/>\n';
  html+= '        </div>\n';
  html+= '      </div>\n';
  //html+= '      </fieldset>\n';
  html+= '    </fieldset> <!-- periodlist -->\n';
  html+= '    \n';
  
  return html;
}


Editor.periodlist_addEntry = function(cols, fields) {

  try {

    var newTR = Editor.remote.document.createElement("tr");
    newTR.setAttribute("id", "tr_"+Editor.periodlist_entries);
    newTR.onclick = new Function( "Editor.periodlist_highlightRow( this.id.substring(3) )" );

    var newTD;
    for (var i=0; i<cols; i++) {

      newTD = Editor.remote.document.createElement("td");
      newTD.setAttribute("id", "td_"+Editor.periodlist_entries+"_"+i);
      newTD.setAttribute("width", Editor.periodlist_width[i]);
      newTD.setAttribute("class", "period");
      
      if (typeof fields=='undefined')
        newTD.innerHTML = "&nbsp;";
      else
        newTD.innerHTML = fields[i];

      newTR.appendChild(newTD);
    }

    // lastChild ist TBODY
    Editor.remote.document.getElementById("periodlist_td").lastChild.appendChild(newTR);

    var border = (Editor.ie) ? "inset 1px;" : "threedface inset 1px;";

    for (var i=0; i<cols; i++) {
      Editor.remote.document.getElementById( ("td_"+Editor.periodlist_entries+"_"+i) ).style.border = border;
    }
    
    return ++Editor.periodlist_entries;
    
  } catch(x) {
    throw new Error("Editor.periodlist_addEntry():\n" + x.message);
  }
}


Editor.periodlist_editEntry = function(index, fields) {

  try {

    if (typeof index=='undefined' || typeof fields=='undefined') throw new Error("insufficient number of parameters");
    if (fields==null) throw new Error("parameter fields is null");

    for (var i=0; i<fields.length; i++)
      Editor.remote.document.getElementById("td_"+index+"_"+i).innerHTML = ((fields!=undefined) ? fields[i] : "&nbsp;");

  } catch(x) {
    throw new Error("Editor.periodlist_editEntry():\n" + x.message);
  }
}


Editor.periodlist_removeEntry = function(index, cols) {

  try {

    if ( index < 0 ) throw new Error("invalid index: " + index);

    node = Editor.remote.document.getElementById("tr_"+index);

    // lastChild ist TBODY
    Editor.remote.document.getElementById("periodlist_td").lastChild.removeChild(node);

    Editor.periodlist_entries--;

    // neue ids vergeben
    // hier: i muss eine Zahl sein, kein String
    for (var i=Math.abs(index); i<Editor.periodlist_entries; i++) {

      // td
      for (var n=0; n<cols; n++)
        Editor.remote.document.getElementById("td_"+(i+1)+"_"+n).setAttribute("id", "td_"+i+"_"+n);

      // tr
      Editor.remote.document.getElementById("tr_"+(i+1)).setAttribute("id", "tr_"+i);
    }

    // minimale Anzahl der Plaetze bereithalten
    if ( Editor.periodlist_entries < Editor.periodlist_minEntries ) {
      Editor.periodlist_addEntry(cols);
    }

  } catch(x) {
    throw new Error("Editor.periodlist_removeEntry():\n" + x.message);
  }
}

Editor.periodlist_addPeriod = function(period) {

  try {

    if ( typeof period=='undefined' ) throw new Error("parameter period is undefined");
    if ( period==null ) throw new Error("parameter period is null");

    var fields = new Array();
    fields[0] = ( period["let_run"]==true ) ? "Yes" : "No";
    fields[1] = DOMManager.getTimestamp(period["begintime_hh"],period["begintime_mm"],period["begintime_ss"]);
    fields[2] = DOMManager.getTimestamp(period["endtime_hh"],period["endtime_mm"],period["endtime_ss"]);
    fields[3] = DOMManager.getTimestamp(period["repeattime_hh"],period["repeattime_mm"],period["repeattime_ss"]);
    fields[4] = DOMManager.getTimestamp(period["absoluterepeat_hh"],period["absoluterepeat_mm"],period["absoluterepeat_ss"]);
    fields[5] = DOMManager.getTimestamp(period["singlestart_hh"],period["singlestart_mm"],period["singlestart_ss"]);
    fields[6] = Editor.when_holiday_list[period["when_holiday"]];
        
    // leere Felder behandeln
    for (var i=0; i<fields.length; i++) {
      if ( fields[i]=="" ) fields[i] = "&nbsp;";
    }

    // Daten in die Tabelle einfuegen:
    // es ist noch Platz
    if ( Editor.periodlist_periods < Editor.periodlist_entries)
      Editor.periodlist_editEntry(Editor.periodlist_periods, fields);

    // Tabelle muss erweitert werden
    else
      Editor.periodlist_addEntry(fields.length, fields);

    Editor.periodlist_periods++;

  } catch(x) {
    throw new Error("Editor.periodlist_addPeriod():\n" + x.message);
  }
}

Editor.periodlist_removePeriod = function(index, cols) {

  try {

    if ( Editor.periodlist_periods <= 0 )
      throw new Error("invalid number of periods: " + Editor.periodlist_periods);
    else
      Editor.periodlist_periods--;

    // Periode aus dem Baum entfernen
    // globale Referenz "Editor.period_currentPeriodTree" wurde in Editor.showDay() gesetzt
    for (var i=Number(index); i<Editor.period_currentPeriodTree.length-1; i++)
      Editor.period_currentPeriodTree[i] = Editor.period_currentPeriodTree[i+1];

    Editor.period_currentPeriodTree.length--;

    Editor.period_currentPeriod = null;

    Editor.periodlist_removeEntry(index, cols);

  } catch(x) {
    throw new Error("Editor.periodlist_removePeriod():\n" + x.message);
  }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
/*   Class RunOptions                                                                             */
////////////////////////////////////////////////////////////////////////////////////////////////////

Editor.showRunOptions = function() {

  try {
    
    if ( Editor.mode=="run_time" ) {
      if ( !Editor.period_currentPeriod_isAttachable ) {
        Editor.dyeTreeElement( Editor.remote.document.getElementById("navi_run_time") );
        Editor.remote.Alert(Editor.root_display+" is not valid.\nPlease adjust the period.");
        return;
      } else {
        Editor.periodlist_action_apply_period(null);
      }
    }
    
    Editor.mode = "runoptions";
    Editor.ok_set_enabled( true );
    
    RunOption.initVariables( new Array("1","2","3") );
    
    var width = 504;
    var height = 497;
    
    if ( Editor.ie ) {
      width += 4;
      height += 4;
    }
    
    var border = ( Editor.ie ) ? 'groove 2px' : 'threedface groove 2px';
    
    var html = "";
    html+= '<div id="run_option_groovy" style="position:absolute; background-color:'+Editor.bgColor+'; top:7; width:'+width+'; height:'+height+'; border:'+border+';">\n';
    html+= '</div>\n';
    html+= '<div class="run_option_caption">Run Options</div>\n';
    
    html += RunOption.getForm("1", 30,  7, 494, 142);
    html += RunOption.getForm("2", 196, 7, 494, 142);
    html += RunOption.getForm("3", 359, 7, 494, 142);
    
    // html in der Oberflaeche anzeigen
    Editor.remote.document.getElementById("gui").innerHTML = html;
    
    RunOption.initForm("1");
    RunOption.initForm("2");
    RunOption.initForm("3");
    
  } catch(x) {
    if ( Editor.isStarted )
      Editor.remote.alertError("Editor.showRunOptions():", x);
    else
      throw new Error("Editor.showRunOptions():\n" + x.message);
  }
}


RunOption = function() {}

RunOption.SetbackCountSort = function(a, b) {

  return ( a["setback_count"] - b["setback_count"] );
}

RunOption.ErrorCountSort = function(a, b) {

  return ( a["error_count"] - b["error_count"] );
}

RunOption.sort_DelayOrderAfterSetback = function() {

  try {

    if ( DOMManager.tree["run_options"]!=null )
      if ( DOMManager.tree["run_options"]["delay_order_after_setback"]!=null )
        DOMManager.tree["run_options"]["delay_order_after_setback"].sort(RunOption.SetbackCountSort);

  } catch(x) {
    throw new Error("RunOption.sort_DelayOrderAfterSetback():\n" + x.message);
  }
}

RunOption.sort_DelayAfterError = function() {

  try {

    if ( DOMManager.tree["run_options"]!=null )
      if ( DOMManager.tree["run_options"]["delay_after_error"]!=null )
        DOMManager.tree["run_options"]["delay_after_error"].sort(RunOption.ErrorCountSort);

  } catch(x) {
    throw new Error("RunOption.sort_DelayAfterError():\n" + x.message);
  }
}


RunOption.mode = null; // "1","2" oder "3" : darf nur null oder String sein

/*  Variablen sind Object auf die mit RunOption.mode zugegriffen wird
  z.B. RunOption.highlightedRow = new Object();
  RunOption.highlightedRow["1"] = -1;
  RunOption.mode = "1";
  RunOption.highlightedRow[ RunOption.mode ] -> -1
*/

RunOption.minimalRows;    // minimale Anzahl der Reihen
RunOption.nrOfRows;       // tatsaechliche Anzahl der Reihen
RunOption.nrOfEntries;    // aktuell Anzahl der Daten-Eintraege
RunOption.highlightedRow; // Index der aktuell markierten Reihen

RunOption.caption;
RunOption.columns;
RunOption.tableCaption;
RunOption.width;
RunOption.ths;
RunOption.tds;


/**
 * initialisiert die Variablen der RunOption-Klasse
 * modes enthaelt die Bezeichner fuer die modi
 * modes = new Array("1", "2", "3");
 */
RunOption.initVariables = function(modes) {

  try {

    RunOption.minimalRows = new Object();
    RunOption.nrOfRows = new Object();
    RunOption.nrOfEntries = new Object();
    RunOption.highlightedRow = new Object();

    // modespezifische Konstanten
    RunOption.columns = new Object();
    RunOption.width = new Object();
    RunOption.ths = new Object();
    RunOption.tds = new Object();
    RunOption.caption = new Object();
    RunOption.tableCaption = new Object();

    var mode = "";
    for (var i=0; i<modes.length; i++) {
      mode = String( modes[i] );
      RunOption.minimalRows[mode] = 4;
      RunOption.nrOfRows[mode] = 0;
      RunOption.nrOfEntries[mode] = 0;
      RunOption.highlightedRow[mode] = -1;
    }

    mode = "1";
    RunOption.caption[mode] = "Start When Directory Changed";
    RunOption.columns[mode] = 2;
    RunOption.tableCaption[mode] = new Array("Directory", "Regex");
    RunOption.width[mode] = new Array("177px", "176px");
    RunOption.ths[mode] = new Array("run_option"+mode+"_th1", "run_option"+mode+"_th2");
    RunOption.tds[mode] = new Array("run_option"+mode+"_td1", "run_option"+mode+"_td2");


    mode = "2";
    RunOption.caption[mode] = "Delay Order After Set Back";
    RunOption.columns[mode] = 3;
    RunOption.tableCaption[mode] = new Array("Set Back Count", "Is Maximum", "Delay [hh:mm:]ss");
    RunOption.width[mode] = new Array("114px", "117px", "116px");
    RunOption.ths[mode] = new Array("run_option"+mode+"_th1", "run_option"+mode+"_th2", "run_option"+mode+"_th3");
    RunOption.tds[mode] = new Array("run_option"+mode+"_td1", "run_option"+mode+"_td2", "run_option"+mode+"_td3");

    mode = "3";
    RunOption.caption[mode] = "Delay After Error";
    RunOption.columns[mode] = 2;
    RunOption.tableCaption[mode] = new Array("Error Count", "Delay [hh:mm:]ss");
    RunOption.width[mode] = new Array("177px", "176px");
    RunOption.ths[mode] = new Array("run_option"+mode+"_th1", "run_option"+mode+"_th2");
    RunOption.tds[mode] = new Array("run_option"+mode+"_td1", "run_option"+mode+"_td2");

  } catch(x) {
    throw new Error("RunOption.initVariables():\n" + x.message);
  }
}


// liefert HTML fuer das RunOption Formular zurueck
RunOption.getForm = function(mode, top, left, width, height) {
  
  try {
    
    mode = String(mode);
    
    var html = "";
    html+= '<div id="run_option'+mode+'" style="position:absolute; top:'+top+'; left:'+left+'; width:'+width+'; height:'+height+'; background-color:'+Editor.bgColor+';">\n';
    
    var gw, gh;
    
    if ( Editor.ie ) {
      gw = width;
      gh = height-7;
    } else {
      gw = width-14;
      gh = height-31;
    }
    
    var w = 381;
    var h = 84;
    
    var border = ( Editor.ie ) ? 'groove 2px' : 'threedface groove 2px';
    
    html+= '<div id="run_option'+mode+'_groovy" style="position:absolute; top:7; width:'+gw+'; height:'+gh+'; padding:5px; padding-top:15px; border:'+border+';">\n';
    
    html+= '<div style="position:absolute; top:11; width:385; height:22; background-color:'+Editor.bgColor+';">'
    html+= '<table border="0" cellspacing="0" cellpadding="0" style="width:385; height:22;">';
    html+= '<tr>';
    
    if ( mode=="1" ) {
      html+= '<td width="91">Watch Directory:&nbsp;</td>';      
      html+= '<td><input id="run_option1_dir" onkeyup="self.opener.RunOption.listen_1();" class="run_option_inputtext" style="width:108;" type="text"/></td>';
      html+= '<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;File Regex:&nbsp;</td>';
      html+= '<td width="108"><input id="run_option1_regex" onkeyup="self.opener.RunOption.listen_1();" class="run_option_inputtext" style="width:108;" type="text"/></td>';
    }
    else
    if ( mode=="2" ) {
      html+= '<td>Set Back Count:&nbsp;</td>';
      html+= '<td><input id="run_option2_count" onkeyup="self.opener.RunOption.listen_2();" class="run_option_inputtext" style="width:42;" type="text"/></td>';
      html+= '<td>&nbsp;&nbsp;&nbsp;<input type="checkbox" id="run_option2_max"  onclick="self.opener.RunOption.listen_2();"/></td>';
      html+= '<td>Max&nbsp;&nbsp;&nbsp;</td>';
      html+= '<td>&nbsp;Delay:&nbsp;</td>';
      html+= '<td><input id="run_option2_delay_hh" onkeyup="self.opener.RunOption.listen_2();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td>&nbsp;:&nbsp;</td>';
      html+= '<td><input id="run_option2_delay_mm" onkeyup="self.opener.RunOption.listen_2();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td>&nbsp;:&nbsp;</td>';
      html+= '<td><input id="run_option2_delay_ss" onkeyup="self.opener.RunOption.listen_2();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td align="right">&nbsp;[hh:mm:]ss</td>';
    }
    else
    if ( mode=="3" ) {
      html+= '<td>Error Count:&nbsp;</td>';
      html+= '<td><input id="run_option3_count" onkeyup="self.opener.RunOption.listen_3();" class="run_option_inputtext" style="width:42;" type="text"/></td>';
      html+= '<td>&nbsp;&nbsp;<input type="radio" name="run_option3_delay" value="stop" onclick="self.opener.RunOption.listenRadio_3();"></td>';
      html+= '<td>stop&nbsp;&nbsp;</td>';
      html+= '<td>&nbsp;<input type="radio" name="run_option3_delay" value="delay" onclick="self.opener.RunOption.listenRadio_3();"></td>';
      html+= '<td>Delay:&nbsp;</td>';
      html+= '<td><input id="run_option3_delay_hh" onkeyup="self.opener.RunOption.listen_3();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td>&nbsp;:&nbsp;</td>';
      html+= '<td><input id="run_option3_delay_mm" onkeyup="self.opener.RunOption.listen_3();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td>&nbsp;:&nbsp;</td>';
      html+= '<td><input id="run_option3_delay_ss" onkeyup="self.opener.RunOption.listen_3();" class="run_option_inputtext" style="width:23;" maxlength="2" type="text"/></td>';
      html+= '<td align="right">&nbsp;[hh:mm:]ss</td>';
    }
    
    html+= '</tr>';
    html+= '</table>';
    html+= '</div>'
    
    html+= '\n';
    
    border = ( Editor.ie ) ? 'inset 2px' : 'threedface inset 2px';
    
    var lw;
    var lh;
    
    if ( Editor.ie ) {
      lw = w+4;
      lh = h+4;
    } else {
      lw = w;
      lh = h;
    }
    
    html+= '<div id="run_option'+mode+'_list" style="position:absolute; top:38; left:5; width:'+lw+'; height:'+lh+'; border:'+border+'; background-color:'+Editor.bgColor+';">\n';
    html+= '  <table id="run_option'+mode+'_th" bgcolor="white" cellspacing="0" cellpadding="2">\n';
    html+= '    <tr>\n';
    
    border = ( Editor.ie ) ? 'outset 1px' : 'threedface outset 1px';
    
    for (var i=1; i<=RunOption.columns[mode]; i++)
      html+= '      <td id="run_option'+mode+'_th'+i+'" style="width:'+RunOption.width[mode][i-1]+'; background-color:'+Editor.bgColor+'; border:'+border+';">'+RunOption.tableCaption[mode][i-1]+'</th>\n';
    
    html+= '    </tr>\n';
    html+= '  </table>\n';
    html+= '\n';
    
    html+= '  <div id="run_option'+mode+'_td_div" style="position:absolute; top:21; width:'+w+'; height:63; background-color:'+Editor.bgColor+'; overflow:auto;">\n';
    html+= '    <table id="run_option'+mode+'_td" bgcolor="white" cellspacing="0" cellpadding="2">\n';
    
    border = ( Editor.ie ) ? 'inset 1px;' : 'threedface inset 1px;';
    
    for (var i=0; i<RunOption.minimalRows[mode]; i++) {

      html += '<tr id="run_option'+mode+'_tr_'+i+'" onclick="self.opener.RunOption.action_highlight(this)">';

      for (var col=0; col<RunOption.columns[mode]; col++)
        html += '<td id="run_option'+mode+'_td_'+i+'_'+col+'" width="'+RunOption.width[mode][col]+'" style="border:'+border+'">&nbsp;</td>';

      html += '</tr>';
    }
    
    html+= '</table>\n'; // textnode unter FF vermeiden
    html+= '  </div>\n';
    html+= '</div>\n';
    html+= '\n';
    
    var btnApply = "";
    var btnNew = "";
    var btnRemove = "";
    
    if ( mode=="1" ) {
      btnApply = "Apply Dir";
      btnNew = "New Dir";
      btnRemove = "Remove Dir";
    } else if ( mode=="2" ) {
      btnApply = "Apply Delay";
      btnNew = "New Delay";
      btnRemove = "Remove Delay";
    } else if ( mode=="3" ) {
      btnApply = "Apply Delay";
      btnNew = "New Delay";
      btnRemove = "Remove Delay";
    }
    
    html+= '<div style="position:absolute; top:12; left:395; width:90; height:20; background-color:white;">\n';
    html+= '  <input id="run_option'+mode+'_apply" type="button" class="run_option_button" value="'+btnApply+'" onclick="self.opener.RunOption.action_apply('+mode+');"/>\n';
    html+= '</div>\n';
    
    html+= '<div style="position:absolute; top:38; left:395; width:90; height:20; background-color:white;">\n';
    html+= '  <input id="run_option'+mode+'_new" type="button" class="run_option_button" value="'+btnNew+'" onclick="self.opener.RunOption.action_new('+mode+');"/>\n';
    html+= '</div>\n';
    
    html+= '<div style="position:absolute; top:64; left:395; width:90; height:20; background-color:white;">\n';
    html+= '  <input id="run_option'+mode+'_remove" type="button" class="run_option_button" value="'+btnRemove+'" onclick="self.opener.RunOption.action_remove('+mode+');"/>\n';
    html+= '</div>\n';
    
    html+= '\n';
    html+= '</div>\n';
    html+= '<div id="run_option'+mode+'_caption" class="run_option_caption">'+RunOption.caption[mode]+'</div>\n';
    html+= '</div> <!-- RunOptions -->\n';
    
    return html;
    
  } catch(x) {
    throw new Error("RunOption.getForm():\n" + x.message);
  }
}

RunOption.inputFields_enabled = function(mode, enabled) {
  
  try {
    
    mode      = String(mode);
    var state = ( enabled ) ? "" : "disabled";
    var elem;
    
    if ( mode=="1" ) {
      elem = Editor.remote.document.getElementById("run_option1_dir");
      elem.style.backgroundColor = bg;
      if( enabled ) Editor.clearInputBgColor(elem);
      else elem.style.backgroundColor = Editor.bgColor;
      elem.disabled = state;
      elem = Editor.remote.document.getElementById("run_option1_regex");
      if( enabled ) Editor.clearInputBgColor(elem);
      else elem.style.backgroundColor = Editor.bgColor;
      elem.disabled = state;

    } else if ( mode=="2" ) {
      elem = Editor.remote.document.getElementById("run_option2_count");
      if( enabled ) Editor.clearInputBgColor(elem);
      else elem.style.backgroundColor = Editor.bgColor;
      elem.disabled = state;
      elem = Editor.remote.document.getElementById("run_option2_max");
      elem.disabled = state;
      RunOption.inputDelay_enabled(mode, enabled);

    } else if ( mode=="3" ) {
      elem = Editor.remote.document.getElementById("run_option3_count");
      if( enabled ) Editor.clearInputBgColor(elem);
      else elem.style.backgroundColor = Editor.bgColor;
      elem.disabled = state;
      elem = Editor.remote.document.getElementsByName("run_option3_delay");
      elem[0].disabled = state;
      elem[1].disabled = state;
      RunOption.inputDelay_enabled(mode, enabled);
    }

  } catch(x) {
    throw new Error("RunOption.inputFields_enabled():\n" + x.message);
  }
}

RunOption.btn_apply_enabled = function(mode, enabled) {
  Editor.remote.document.getElementById("run_option"+mode+"_apply").disabled = ( enabled ) ? "" : "disabled";
}

RunOption.btn_new_enabled = function(mode, enabled) {
  Editor.remote.document.getElementById("run_option"+mode+"_new").disabled = ( enabled ) ? "" : "disabled";
}

RunOption.btn_remove_enabled = function(mode, enabled) {
  Editor.remote.document.getElementById("run_option"+mode+"_remove").disabled = ( enabled ) ? "" : "disabled";
}

RunOption.inputDelay_enabled = function(mode, enabled) {

  try {

    mode      = String(mode);
    if ( mode!="2" && mode!="3" ) throw new Error("unsupported mode " + mode);
    var state = ( enabled ) ? "" : "disabled";

    var elem;
    elem = Editor.remote.document.getElementById("run_option"+mode+"_delay_hh");
    if( enabled ) Editor.clearInputBgColor(elem);
    else elem.style.backgroundColor = Editor.bgColor;
    elem.disabled = state;
    elem = Editor.remote.document.getElementById("run_option"+mode+"_delay_mm");
    if( enabled ) Editor.clearInputBgColor(elem);
    else elem.style.backgroundColor = Editor.bgColor;
    elem.disabled = state;
    elem = Editor.remote.document.getElementById("run_option"+mode+"_delay_ss");
    if( enabled ) Editor.clearInputBgColor(elem);
    else elem.style.backgroundColor = Editor.bgColor;
    elem.disabled = state;

  } catch(x) {
    throw new Error("RunOption.inputDelay_enabled():\n" + x.message);
  }
}


RunOption.initList = function(mode) {

  try {

    mode = String(mode);
    if ( DOMManager.tree["run_options"]==null ) return;

    var tree;

    if ( mode=="1" )
      tree = DOMManager.tree["run_options"]["start_when_directory_changed"];

    else if ( mode=="2" )
      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];

    else if ( mode=="3" )
      tree = DOMManager.tree["run_options"]["delay_after_error"];

    else
      throw new Error("unsupported mode "+mode);

    if ( tree==null ) return;

    for (var i=0; i<tree.length; i++)
      RunOption.putElementIntoList(mode, tree[i] );

  } catch(x) {
    throw new Error("RunOption.initList():\n" + x.message);
  }
}


RunOption.updateList = function(mode) {

  try {

    mode = String(mode);
    RunOption.nrOfEntries[mode] = 0;
    RunOption.fillList(mode);

  } catch(x) {
    throw new Error("RunOption.updateList():\n" + x.message);
  }
}


RunOption.fillList = function(mode) {

  try {

    mode = String(mode);
    if ( DOMManager.tree["run_options"]==null ) return;

    var tree;

    if ( mode=="1" ) {

      tree = DOMManager.tree["run_options"]["start_when_directory_changed"];
      if ( tree==null ) return;
      for (var i=0; i<tree.length; i++)
        RunOption.putElementIntoList(mode, tree[i] );

    } else if ( mode=="2" ) {

      RunOption.sort_DelayOrderAfterSetback();
      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
      if ( tree==null ) return;

      for (var i=0; i<tree.length; i++)
        RunOption.putElementIntoList(mode, tree[i] );

    } else if ( mode=="3" ) {

      RunOption.sort_DelayAfterError();
      tree = DOMManager.tree["run_options"]["delay_after_error"];
      if ( tree==null ) return;

      for (var i=0; i<tree.length; i++)
        RunOption.putElementIntoList(mode, tree[i] );

    } else
      throw new Error("unsupported mode "+mode);

  } catch(x) {
    throw new Error("RunOption.fillList():\n" + x.message);
  }
}


RunOption.objToFields = function(mode, obj) {

  var fields = null;

  try {

    if ( obj==null ) throw new Error("parameter 'obj' is null");

    mode = String(mode);

    if ( mode=="1" ) {
      var dir = obj["directory"];
      var regex = obj["regex"];
      if ( regex==null ) regex = "";
      fields = new Array(dir,regex);

    } else if ( mode=="2" ) {
      var count = obj["setback_count"]
      var max = obj["is_maximum"]
      var delay = obj["delay"]
      fields = new Array(count,max,delay);

    } else if ( mode=="3" ) {
      var count = obj["error_count"]
      var delay = obj["delay"]
      fields = new Array(count,delay);

    } else
      throw new Error("unsupported mode " + mode);
    
    return fields;
    
  } catch(x) {
    throw new Error("RunOption.objToFields("+mode+", obj):\n" + x.message);
  }
}


RunOption.putElementIntoList = function(mode, obj, index) {

  try {

    mode = String(mode);

    var fields = RunOption.objToFields(mode, obj);

    // bestehenden Eintrag index aendern
    if ( typeof index!='undefined' ) {

      RunOption.editEntry(mode, fields, index);

    // neuen Eintrag hinzufuegen
    } else {

      // es gibt noch leere vorbereitete Reihen
      if ( RunOption.nrOfEntries[mode] < RunOption.nrOfRows[mode] ) {
        RunOption.editEntry(mode, fields, RunOption.nrOfEntries[mode]);

      // alle Reihen sind belegt -> neue Reihe einfuegen
      } else {
        RunOption.addEntry(mode, fields);
      }

      RunOption.nrOfEntries[mode]++;
    }

  } catch(x) {
    throw new Error("RunOption.putElementIntoList():\n" + x.message);
  }
}


RunOption.addEntry = function(mode, fields) {

  try {

    mode = String(mode);

    if ( typeof fields!='undefined' )
      if ( RunOption.columns[mode]!=fields.length ) throw new Error("length of parameter fields is wrong");

    var newTR = Editor.remote.document.createElement("tr");
    newTR.setAttribute("id", "run_option"+mode+"_tr_"+RunOption.nrOfRows[mode]);

    newTR.onclick = new Function( "RunOption.action_highlight(this)" );

    var newTD;
    for (var i=0; i<RunOption.columns[mode]; i++) {

      newTD = Editor.remote.document.createElement("td");
      newTD.setAttribute("id", "run_option"+mode+"_td_"+RunOption.nrOfRows[mode]+"_"+i);
      newTD.setAttribute("width", RunOption.width[mode][i]);

      if (fields==undefined)
        newTD.innerHTML = "&nbsp;";
      else {
        newTD.innerHTML = ( (fields[i]=="") ? "&nbsp;" : fields[i] );
        RunOption.nrOfEntries[mode]++; // es wurde ein Eintrag hinzugefuegt
      }

      newTR.appendChild(newTD);
    }

    // lastChild soll TBODY sein
    Editor.remote.document.getElementById("run_option"+mode+"_td").lastChild.appendChild(newTR);

    var border = (Editor.ie) ? "inset 1px;" : "threedface inset 1px;";

    for (var i=0; i<RunOption.columns[mode]; i++)
      Editor.remote.document.getElementById("run_option"+mode+"_td_"+RunOption.nrOfRows[mode]+"_"+i).style.border = border;
    
    return ++RunOption.nrOfRows[mode];
    
  } catch(x) {
    throw new Error("RunOption.addEntry("+mode+"):\n" + x.message);
  }
}


RunOption.editEntry = function(mode, fields, index) {

  try {

    mode = String(mode);

    if (typeof index=='undefined' || typeof fields=='undefined') throw new Error("insufficient number of parameters");
    if (fields==null) throw new Error("parameter fields is null");
    if ( RunOption.columns[mode]!=fields.length ) throw new Error("length of parameter fields is wrong");

    for (var i=0; i<fields.length; i++) {
      Editor.remote.document.getElementById("run_option"+mode+"_td_"+index+"_"+i).innerHTML = ( (fields[i]!="") ? fields[i] : "&nbsp;");
    }

  } catch(x) {
    throw new Error("RunOption.editEntry():\n" + x.message);
  }
}


RunOption.removeRow = function(mode, index) {

  try {

    mode = String(mode);

    if ( index < 0 ) throw new Error("invalid index: " + index);

    node = Editor.remote.document.getElementById("run_option"+mode+"_tr_"+index);

    // lastChild soll TBODY sein
    Editor.remote.document.getElementById("run_option"+mode+"_td").lastChild.removeChild(node);

    RunOption.nrOfRows[mode]--;

    // neue ids vergeben
    // hier: i muss eine Zahl sein, kein String
    for (var i=Number(index); i<RunOption.nrOfRows[mode]; i++) {

      // td
      for (var n=0; n<RunOption.columns[mode]; n++)
        Editor.remote.document.getElementById("run_option"+mode+"_td_"+(i+1)+"_"+n).setAttribute("id", "run_option"+mode+"_td_"+i+"_"+n);

      // tr
      Editor.remote.document.getElementById("run_option"+mode+"_tr_"+(i+1)).setAttribute("id", "run_option"+mode+"_tr_"+i);
    }

    // minimale Anzahl der Plaetze bereithalten
    if ( RunOption.nrOfRows[mode] < RunOption.minimalRows[mode] ) {
      RunOption.addEntry(mode);
    }

  } catch(x) {
    throw new Error("RunOption.removeRow:\n" + x.message);
  }
}


RunOption.removeEntry = function(mode, index) {

  try {

    mode = String(mode);

    if ( RunOption.nrOfEntries[mode] <= 0 )
      throw new Error("invalid number of entries for mode "+mode+": " + RunOption.nrOfEntries[mode]);
    else
      RunOption.nrOfEntries[mode]--;

    if ( DOMManager.tree["run_options"]==null ) throw new Error("DOMManager.tree['run_options'] is null");

    var tree = null;
    if ( mode=="1" )
      tree = DOMManager.tree["run_options"]["start_when_directory_changed"];
    else if ( mode=="2" )
      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
    else if ( mode=="3" )
      tree = DOMManager.tree["run_options"]["delay_after_error"];
    else
      throw new Error("unsupported mode "+mode);

    if ( tree==null ) throw new Error("tree is null");
    if ( tree.length==0 ) throw new Error("tree.length is "+tree.length);

    for (var i=Number(index); i<tree.length-1; i++)
      tree[i] = tree[i+1];

    tree.length--;

    RunOption.removeRow(mode, index); // Zeile aus der Liste entfernen

    if ( mode=="1" )
      DOMManager.tree["run_options"]["start_when_directory_changed"] = tree;
    else if ( mode=="2" )
      DOMManager.tree["run_options"]["delay_order_after_setback"] = tree;
    else if ( mode=="3" )
      DOMManager.tree["run_options"]["delay_after_error"] = tree;

  } catch(e) {
    Editor.remote.alertError("RunOption.removeEntry():", e);
  }
}


// initialisiert ein RunOption Formular
RunOption.initForm = function(mode) {
  
  mode = String(mode);
  
  try {
    
    // minimale Anzahl an leeren Zeilen wurde in RunOption.getForm() erzeugt
    RunOption.nrOfRows[mode] = RunOption.minimalRows[mode];
    
    // Eingabefelder deaktivieren
    RunOption.inputFields_enabled(mode, false);
    
    RunOption.btn_apply_enabled(mode, false);
    RunOption.btn_remove_enabled(mode, false);
    
    // modeabhaengige Daten in Liste einfuegen
    RunOption.fillList(mode);
    
  } catch(e) {
    Editor.remote.alertError("RunOption.initForm("+mode+"):", e);
  }
}


RunOption.action_apply = function(mode) {

  try {

    mode = String(mode);

  // start_when_directory_changed
    if ( mode=="1" ) {

      obj = new Object();
      obj["directory"] = Editor.remote.document.getElementById("run_option1_dir").value;
      obj["regex"] = Editor.remote.document.getElementById("run_option1_regex").value;

      // Neues Element
      if ( RunOption.highlightedRow[mode]==-1 ) {

        if ( DOMManager.tree["run_options"]==null ) DOMManager.tree["run_options"] = new Object();
        if ( DOMManager.tree["run_options"]["start_when_directory_changed"]==null )
          DOMManager.tree["run_options"]["start_when_directory_changed"] = new Array();

        l = DOMManager.tree["run_options"]["start_when_directory_changed"].length;
        DOMManager.tree["run_options"]["start_when_directory_changed"][l] = obj;

        // Element an Liste anhaengen
        RunOption.putElementIntoList(mode, obj);

      // markiertes Element aktualisieren
      } else {

        DOMManager.tree["run_options"]["start_when_directory_changed"][RunOption.highlightedRow[mode]] = obj;
        RunOption.putElementIntoList(mode, obj, RunOption.highlightedRow[mode]);

        RunOption.remove_Highlighting(mode);
      }


  // delay_order_after_setback
    } else if ( mode=="2" ) {

      msg = RunOption.apply_DelayOrderAfterSetback();
      if ( msg!="" ) {
        Editor.remote.Alert(msg);
        return;
      }


  // delay_after_error
    } else if ( mode=="3" ) {

      msg = RunOption.apply_DelayAfterError();
      if ( msg!="" ) {
        Editor.remote.Alert(msg);
        return;
      }

    } else
      throw new Error("unsupported mode "+mode);


    // Nachdem Apply ausgefuehrt wurde Felder und Btn disablen
    RunOption.btn_apply_enabled(mode, false);
    RunOption.clearInputFields(mode);
    RunOption.inputFields_enabled(mode, false);

    // Liste updaten
    RunOption.updateList(mode);

  } catch(e) {
    Editor.remote.alertError("RunOption.action_apply():", e);
  }
}


RunOption.apply_DelayOrderAfterSetback = function() {

  try {

    mode = "2";

    var tree;
    count = Editor.remote.document.getElementById("run_option2_count").value;
    is_maximum = Editor.remote.document.getElementById("run_option2_max").checked;

    // Neuer Eintrag soll erstellt werden
    if ( RunOption.highlightedRow[mode]==-1 ) {

      if ( DOMManager.tree["run_options"]!=null && DOMManager.tree["run_options"]["delay_order_after_setback"]!=null ) {
        tree = DOMManager.tree["run_options"]["delay_order_after_setback"];

        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["setback_count"]==count )
            return "Set Back Count already defined";
        }

        // existiert stop bereits und soll ein Eintrag dahinter eingefuegt werden?
        var maxCount = null; // Set Back Count des stop Eintrags
        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["is_maximum"].toLowerCase()=="yes" ) {
            maxCount = tree[i]["setback_count"];
            break;
          }
        }
        if ( maxCount!=null && maxCount<count )
          return "There cannot be an item with a higher Set Back Count than the item with maximum=yes \n(at Set Back Count "+maxCount+").";
      }
    }

    // Objekt fuer "delay_order_after_setback" erstellen
    obj = new Object();
    obj["setback_count"] = count;
    obj["is_maximum"] = ( (is_maximum) ? "yes" : "no" );
    if ( is_maximum )
      obj["delay"]="0";
    else {
      hh = Editor.remote.document.getElementById("run_option2_delay_hh").value;
      mm = Editor.remote.document.getElementById("run_option2_delay_mm").value;
      ss = Editor.remote.document.getElementById("run_option2_delay_ss").value;
      obj["delay"] = DOMManager.getTimestamp(hh,mm,ss);
      if ( obj["delay"]=="" ) obj["delay"]="0";
    }

    // Neuen Eintrag erstellen
    if ( RunOption.highlightedRow[mode]==-1 ) {

      if ( DOMManager.tree["run_options"]==null ) DOMManager.tree["run_options"] = new Object();
      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
      if ( tree==null ) tree = new Array();

      tree[tree.length] = obj;

    // Bestehenden Eintrag veraendern
    } else {

      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];

      // wenn der Set Back Count veraendert wurde
      if ( tree[ RunOption.highlightedRow[mode] ]["setback_count"] != obj["setback_count"] ) {

        // Gibt es den Set Back Count schon in einer anderen Zeile?
        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["setback_count"]==obj["setback_count"] )
            return "Set Back Count already defined";
        }

        // die markierte Zeile hat maximum=yes
        if ( tree[ RunOption.highlightedRow[mode] ]["is_maximum"].toLowerCase()=="yes" ) {
          // der neue Set Back Count darf nicht kleiner oder gleich sein, als der naechst kleinere Set Back Count,
          // falls dieser existiert

          if ( tree.length >= 2 ) {

            next_lower_count = tree[tree.length-2]["setback_count"];
            if ( Number(obj["setback_count"]) < Number(next_lower_count) ) {
              return "There cannot be an item with a higher Set Back Count than the item with maximum=yes.";
            }
          }

        // die markierte Zeile hat KEIN maximum=yes
        } else {
          // wenn maximum=yes existiert, darf der Set Back Count nicht groesser sein als Set Back Count von maximum
          var max_count = null;
          for (var i=0; i<tree.length; i++) {
            if ( tree[i]["is_maximum"].toLowerCase()=="yes" )
              max_count = tree[i]["setback_count"];
          }

          if ( max_count!=null ) {
            if ( Number(obj["setback_count"]) > Number(max_count) )
              return "There cannot be an item with a higher Set Back Count than the item with maximum=yes \n(Set Back Count with maximum=yes is "+max_count+").";
          }
        }
      }

      tree[ RunOption.highlightedRow[mode] ] = obj;
      RunOption.remove_Highlighting(mode);
    }

    // Wert uebertragen
    DOMManager.tree["run_options"]["delay_order_after_setback"] = tree;

    return "";

  } catch(x) {
    throw new Error("RunOption.apply_DelayOrderAfterSetback():\n" + x.message);
  }
}


RunOption.apply_DelayAfterError = function() {

  try {

    mode = "3";

    var tree;
    count = Editor.remote.document.getElementById("run_option3_count").value;
    is_stop = Editor.remote.document.getElementsByName("run_option3_delay")[0].checked;

    // Neuer Eintrag soll erstellt werden
    if ( RunOption.highlightedRow[mode]==-1 ) {

      if ( DOMManager.tree["run_options"]!=null && DOMManager.tree["run_options"]["delay_after_error"]!=null ) {
        tree = DOMManager.tree["run_options"]["delay_after_error"];

        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["error_count"]==count )
            return "Error Count already defined";
        }

        // existiert stop bereits und soll ein Eintrag dahinter eingefuegt werden?
        var maxCount = null; // Set Back Count des stop Eintrags
        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["delay"].toLowerCase()=="stop" ) {
            maxCount = tree[i]["error_count"];
            break;
          }
        }
        if ( maxCount!=null && maxCount<count )
          return "There cannot be an item with a higher Error Count than the item with delay=stop \n(at Error Count "+maxCount+").";
      }
    }

    // Objekt fuer "delay_after_error" erstellen
    obj = new Object();
    obj["error_count"] = count;

    if ( is_stop )
      obj["delay"] = "stop";
    else {
      hh = Editor.remote.document.getElementById("run_option3_delay_hh").value;
      mm = Editor.remote.document.getElementById("run_option3_delay_mm").value;
      ss = Editor.remote.document.getElementById("run_option3_delay_ss").value;
      obj["delay"] = DOMManager.getTimestamp(hh,mm,ss);
      if ( obj["delay"]=="" || obj["delay"]=="00:00" ) obj["delay"]="00";
    }

    // Neuen Eintrag erstellen
    if ( RunOption.highlightedRow[mode]==-1 ) {

      if ( DOMManager.tree["run_options"]==null ) DOMManager.tree["run_options"] = new Object();
      tree = DOMManager.tree["run_options"]["delay_after_error"];
      if ( tree==null ) tree = new Array();

      tree[tree.length] = obj;

    // Bestehenden Eintrag veraendern
    } else {

      tree = DOMManager.tree["run_options"]["delay_after_error"];

      // wenn der Error Count veraendert wurde
      if ( tree[ RunOption.highlightedRow[mode] ]["error_count"] != obj["error_count"] ) {

        // Gibt es den Error Count schon in einer anderen Zeile?
        for (var i=0; i<tree.length; i++) {
          if ( tree[i]["error_count"]==obj["error_count"] )
            return "Error Count already defined";
        }

        // die markierte Zeile hat delay=stop
        if ( tree[ RunOption.highlightedRow[mode] ]["delay"].toLowerCase()=="stop" ) {
          // der neue Error Count darf nicht kleiner oder gleich sein, als der naechst kleinere Error Count,
          // falls dieser existiert

          if ( tree.length >= 2 ) {

            next_lower_count = tree[tree.length-2]["error_count"];
            if ( Number(obj["error_count"]) < Number(next_lower_count) ) {
              return "There cannot be an item with a higher Error Count than the item with delay=stop.";
            }
          }

        // die markierte Zeile hat KEIN delay=stop
        } else {
          // wenn stop existiert, darf der Error Count nicht groesser sein als Error count von stop
          var stop_count = null;
          for (var i=0; i<tree.length; i++) {
            if ( tree[i]["delay"].toLowerCase()=="stop" )
              stop_count = tree[i]["error_count"];
          }

          if ( stop_count!=null ) {
            if ( Number(obj["error_count"]) > Number(stop_count) )
              return "There cannot be an item with a higher Error Count than the item with delay=stop \n(Error Count with stop is "+stop_count+").";
          }
        }
      }

      tree[ RunOption.highlightedRow[mode] ] = obj;
      RunOption.remove_Highlighting(mode);
    }

    // Wert uebertragen
    DOMManager.tree["run_options"]["delay_after_error"] = tree;

    return "";

  } catch(x) {
    throw new Error("RunOption.apply_DelayAfterError():\n" + x.message);
  }
}


RunOption.action_new = function(mode) {

  try {

    mode = String(mode);

    RunOption.remove_Highlighting(mode);

    // Eingabefelder bereinigen
    RunOption.clearInputFields(mode);

    // enable Eingabefelder
    RunOption.inputFields_enabled(mode, true);

  } catch(e) {
    Editor.remote.alertError("RunOption.action_new():", e);
  }
}


RunOption.listen_1 = function() {

  try {

    mode = "1";

    if ( Editor.remote.document.getElementById("run_option1_dir").value != "" ) {

      // Btn Apply enablen
      RunOption.btn_apply_enabled(mode, true);

    } else {

      // Btn Apply disablen
      RunOption.btn_apply_enabled(mode, false);
    }

  } catch(e) {
    Editor.remote.alertError("RunOption.listen_1():", e);
  }
}


RunOption.listen_2 = function() {

  try {
    mode = "2";

    // befindet sich in Set Back Count eine ungueltiger Wert?
    setback_count = Editor.remote.document.getElementById("run_option2_count").value;
    if ( setback_count!="" ) {
      if ( isNaN(setback_count) ) {
        Editor.remote.document.getElementById("run_option2_count").style.backgroundColor = "yellow";
        Editor.remote.document.getElementById("run_option2_max").disabled = "disabled";
        RunOption.inputDelay_enabled(mode, false);
        RunOption.btn_apply_enabled(mode, false);
        return;
      } else {
        Editor.clearInputBgColor(Editor.remote.document.getElementById("run_option2_count"));
        Editor.remote.document.getElementById("run_option2_max").disabled = "";
        RunOption.inputDelay_enabled(mode, true);
        RunOption.btn_apply_enabled(mode, true);
      }
    } else {
      Editor.clearInputBgColor(Editor.remote.document.getElementById("run_option2_count"));
      Editor.remote.document.getElementById("run_option2_max").disabled = "";
      RunOption.inputDelay_enabled(mode, true);
      RunOption.btn_apply_enabled(mode, false);
    }

    var currentEntry_has_max = false;

    // ist die aktuell markierte Zeile bereits mit maximum,
    // dann darf delay auch beliebig geschaltet werden
    if ( RunOption.highlightedRow[mode]!=-1 ) {
      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
      currentEntry_has_max = (tree[ RunOption.highlightedRow[mode] ]["is_maximum"].toLowerCase() == "yes");
    }

    // falls ein Zeile markiert ist steht sie auf jeden Fall bisher nicht auf maximum
    // wurde auf maximum gestellt?
    if ( !currentEntry_has_max && Editor.remote.document.getElementById("run_option2_max").checked ) {

      var allowed = true;

      // Plausibilitaet pruefen:
      val = Editor.remote.document.getElementById("run_option2_count").value;

      var msg = "";
      if ( val == "" ) {

        allowed = false;
        msg = "Set Back Count with maximum=yes must be highest Set Back Count in list";

      } else {

        // Ist Set Back Count bereits definiert?
        var tree = DOMManager.tree["run_options"];
        if ( tree!=null ) tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
        if ( tree!=null ) {

          for (var i=0; i<tree.length; i++) {

            // gibt es schon ein maximum?
            if ( tree[i]["setback_count"] != val ) {
              if ( tree[i]["is_maximum"].toLowerCase()=="yes" ) {
                allowed = false;
                msg = "maximum=yes already defined for Set Back Count "+tree[i]["setback_count"]+".";
                msg += "\n(Only one item can have maximum=yes \nand Set Back Count with maximum=yes must be highest Set Back Count in list)";
                break;
              }
            }
          }

          if ( allowed )
            for (var i=0; i<tree.length; i++) {
              // gibt es bereits einen hoeheren Count?
              if ( Number(tree[i]["setback_count"]) > Number(val) ) {
                allowed = false;
                msg += "Set Back Count with maximum=yes must be highest Set Back Count in list";
                break;
              }
            }
        }
      }

      if ( allowed ) {
        RunOption.inputDelay_enabled(mode, false);

      } else {
        Editor.remote.Alert(msg);
        // maximum uncheck
        Editor.remote.document.getElementById("run_option2_max").checked = "";
        RunOption.inputDelay_enabled(mode, true);
      }
    }

    if ( Editor.remote.document.getElementById("run_option2_count").value != "" ) {

      // Btn Apply enablen
      RunOption.btn_apply_enabled(mode, true);

    } else {

      // Btn Apply disablen
      RunOption.btn_apply_enabled(mode, false);
    }

    // Eingabefelder pruefen falls noch erforderlich (maximum nicht gesetzt)
    if ( !Editor.remote.document.getElementById("run_option2_max").checked ) {

      var hhElem = Editor.remote.document.getElementById("run_option2_delay_hh");
      var mmElem = Editor.remote.document.getElementById("run_option2_delay_mm");
      var ssElem = Editor.remote.document.getElementById("run_option2_delay_ss");

      Editor.clearInputBgColor(hhElem);
      Editor.clearInputBgColor(mmElem);
      Editor.clearInputBgColor(ssElem);

      hh = hhElem.value;
      mm = mmElem.value;
      ss = ssElem.value;

      var applyable = true;

      if ( isNaN(hh) || hh<0 || hh>23 ) {
        hhElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( isNaN(mm) || mm<0 || mm>59 ) {
        mmElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( isNaN(ss) || ss<0 || ss>59 ) {
        ssElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( !applyable )
        RunOption.btn_apply_enabled(mode, false);
    }

  } catch(e) {
    Editor.remote.alertError("RunOption.listen_2():", e);
  }
}


RunOption.listen_3 = function() {

  try {

    mode = "3";

    // befindet sich in Set Back Count eine ungueltiger Wert?
    error_count = Editor.remote.document.getElementById("run_option3_count").value;
    if ( error_count!="" ) {
      if ( isNaN(error_count) ) {
        Editor.remote.document.getElementById("run_option3_count").style.backgroundColor = "yellow";
        Editor.remote.document.getElementsByName("run_option3_delay")[0].disabled = "disabled";
        Editor.remote.document.getElementsByName("run_option3_delay")[1].disabled = "disabled";
        RunOption.inputDelay_enabled(mode, false);
        RunOption.btn_apply_enabled(mode, false);
        return;
      } else {
        Editor.clearInputBgColor(Editor.remote.document.getElementById("run_option3_count"));
        Editor.remote.document.getElementsByName("run_option3_delay")[0].disabled = "";
        Editor.remote.document.getElementsByName("run_option3_delay")[1].disabled = "";
        RunOption.inputDelay_enabled(mode, true);
        RunOption.btn_apply_enabled(mode, true);
      }
    } else {
      Editor.clearInputBgColor(Editor.remote.document.getElementById("run_option3_count"));
      Editor.remote.document.getElementsByName("run_option3_delay")[0].disabled = "";
      Editor.remote.document.getElementsByName("run_option3_delay")[1].disabled = "";
      RunOption.inputDelay_enabled(mode, true);
      RunOption.btn_apply_enabled(mode, false);
    }

    var currentEntry_has_stop = false;

    // ist die aktuell markierte Zeile bereits mit stop,
    // dann darf delay auch beliebig geschaltet werden
    if ( RunOption.highlightedRow[mode]!=-1 ) {
      tree = DOMManager.tree["run_options"]["delay_after_error"];
      currentEntry_has_stop = (tree[ RunOption.highlightedRow[mode] ]["delay"].toLowerCase() == "stop");
    }

    // falls ein Zeile markiert ist steht sie auf jeden Fall nicht auf stop -> radio pruefen
    // wurde auf Stop gestellt?
    if ( !currentEntry_has_stop && Editor.remote.document.getElementsByName("run_option3_delay")[0].checked ) {

      var allowed = true;

      // Plausibilitaet pruefen:
      val = Editor.remote.document.getElementById("run_option3_count").value;

      var msg = "";
      if ( val == "" ) {

        allowed = false;
        msg = "Error Count with stop must be highest Error Count in list";

      } else {

        // Ist Error Count bereits definiert?
        var tree = DOMManager.tree["run_options"];
        if ( tree!=null ) tree = DOMManager.tree["run_options"]["delay_after_error"];
        if ( tree!=null ) {

          for (var i=0; i<tree.length; i++) {

            // gibt es schon ein stop?
            if ( tree[i]["error_count"] != val ) {
              if ( tree[i]["delay"].toLowerCase()=="stop" ) {
                allowed = false;
                msg = "stop already defined for Error Count "+tree[i]["error_count"]+".";
                msg += "\n(Only one item can have delay=stop \nand Error Count with delay=stop must be highest Error Count in list)";
                break;
              }
            }
          }

          if ( allowed )
            for (var i=0; i<tree.length; i++) {
              // gibt es bereits einen hoeheren Count?
              if ( Number(tree[i]["error_count"]) > Number(val) ) {
                allowed = false;
                msg += "Error Count with delay=stop must be highest Error Count in list";
                break;
              }
            }
        }
      }

      if ( allowed ) {
        RunOption.inputDelay_enabled(mode, false);

      } else {
        Editor.remote.Alert(msg);
        // Radio zurueck auf delay schalten
        Editor.remote.document.getElementsByName("run_option3_delay")[1].checked = "checked";
        RunOption.inputDelay_enabled(mode, true);
      }
    }

    // radio steht auf delay
    if ( Editor.remote.document.getElementsByName("run_option3_delay")[1].checked ) {
      RunOption.inputDelay_enabled(mode, true);
    }

    if ( Editor.remote.document.getElementById("run_option3_count").value != "" ) {

      // Btn Apply enablen
      RunOption.btn_apply_enabled(mode, true);

    } else {

      // Btn Apply disablen
      RunOption.btn_apply_enabled(mode, false);
    }

    // Eingabefelder pruefen falls noch erforderlich (stop nicht gesetzt)
    if ( !Editor.remote.document.getElementsByName("run_option3_delay")[0].checked ) {

      var hhElem = Editor.remote.document.getElementById("run_option3_delay_hh");
      var mmElem = Editor.remote.document.getElementById("run_option3_delay_mm");
      var ssElem = Editor.remote.document.getElementById("run_option3_delay_ss");

      Editor.clearInputBgColor(hhElem);
      Editor.clearInputBgColor(mmElem);
      Editor.clearInputBgColor(ssElem);

      hh = hhElem.value;
      mm = mmElem.value;
      ss = ssElem.value;

      var applyable = true;

      if ( isNaN(hh) || hh<0 || hh>23 ) {
        hhElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( isNaN(mm) || mm<0 || mm>59 ) {
        mmElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( isNaN(ss) || ss<0 || ss>59 ) {
        ssElem.style.backgroundColor = "yellow";
        applyable = false;
      }

      if ( !applyable )
        RunOption.btn_apply_enabled(mode, false);
    }

  } catch(e) {
    Editor.remote.alertError("RunOption.listen_3():", e);
  }
}


RunOption.listenRadio_3 = function() {

  try {

    cstop = Editor.remote.document.getElementsByName("run_option3_delay")[0].checked;
    cdelay = Editor.remote.document.getElementsByName("run_option3_delay")[1].checked;

    RunOption.inputDelay_enabled("3" , cdelay);

    RunOption.listen_3();

  } catch(x) {
    throw new Error("RunOption.listenRadio_3():\n" + x.message);
  }
}


RunOption.action_highlight = function(elem) {

  try {

    mode = elem.id.substring(10,11);
    index = elem.id.substring(15);

    // es wurde auf eine unbenutzte Reihe geklickt
    if ( (Number(index)+1) > RunOption.nrOfEntries[mode] )
      return;

    // Eingabefelder enablen
    RunOption.inputFields_enabled(mode, true);

    RunOption.highlightRow(mode, index);

  } catch(e) {
    Editor.remote.alertError("RunOption.action_highlight():", e);
  }
}


RunOption.highlightRow = function(mode, index) {

  try {

    mode = String(mode);

    // Markierung von alter Zeile entfernen
    RunOption.remove_Highlighting(mode);

    // Btn Remove enablen
    RunOption.btn_remove_enabled(mode, true);

    RunOption.highlightedRow[mode] = index;

    // neue Zeile index farblich markieren
    tr = Editor.remote.document.getElementById("run_option"+mode+"_tr_"+RunOption.highlightedRow[mode]);
    tr.style.backgroundColor = "highlight";
    tr.style.color = "white";

    // Daten in die Eingabefelder laden
    RunOption.loadInputFields(mode, index);

  } catch(x) {
    throw new Error("RunOption.highlightRow():\n" + x.message);
  }
}


RunOption.loadInputFields = function(mode, index) {

  try {

    mode = String(mode);
    index = Number(index);

    if ( DOMManager.tree["run_options"]==null )
      throw new Error("DOMManager.tree['run_options'] is null");

    var tree = null;

    RunOption.clearInputFields(mode);

    if ( mode=="1" ) {

      tree = DOMManager.tree["run_options"]["start_when_directory_changed"];
      if ( tree==null ) throw new Error("tree is null");
      if ( index >= tree.length ) throw new Error("Array index out of bound");

      Editor.remote.document.getElementById("run_option1_dir").value =  tree[index]["directory"];
      Editor.remote.document.getElementById("run_option1_regex").value = ( (tree[index]["regex"]==null) ? "" : tree[index]["regex"] );

    } else if ( mode=="2" ) {

      tree = DOMManager.tree["run_options"]["delay_order_after_setback"];
      if ( tree==null ) throw new Error("tree is null");
      if ( index >= tree.length ) throw new Error("index out of bound (Array)");

      Editor.remote.document.getElementById("run_option2_count").value = tree[index]["setback_count"];
      is_maximum = tree[index]["is_maximum"].toLowerCase()=="yes";
      Editor.remote.document.getElementById("run_option2_max").checked = ( is_maximum ) ? "checked" : "";

      if ( is_maximum ) {
        RunOption.inputDelay_enabled(mode, false);

      } else {
        RunOption.inputDelay_enabled(mode, true);

        hh = DOMManager.timeGetHH(tree[index]["delay"]);
        mm = DOMManager.timeGetMM(tree[index]["delay"]);
        ss = DOMManager.timeGetSS(tree[index]["delay"]);

        Editor.remote.document.getElementById("run_option2_delay_hh").value = hh;
        Editor.remote.document.getElementById("run_option2_delay_mm").value = mm;
        Editor.remote.document.getElementById("run_option2_delay_ss").value = ss;
      }

    } else if ( mode=="3" ) {

      tree = DOMManager.tree["run_options"]["delay_after_error"];
      if ( tree==null ) throw new Error("tree is null");
      if ( index >= tree.length ) throw new Error("index out of bound (Array)");

      Editor.remote.document.getElementById("run_option3_count").value = tree[index]["error_count"];

      if ( tree[index]["delay"]=="stop" ) {
        Editor.remote.document.getElementsByName("run_option3_delay")[0].checked = "checked";
        RunOption.inputDelay_enabled(mode, false);

      } else {
        Editor.remote.document.getElementsByName("run_option3_delay")[1].checked = "checked";
        RunOption.inputDelay_enabled(mode, true);

        hh = DOMManager.timeGetHH(tree[index]["delay"]);
        mm = DOMManager.timeGetMM(tree[index]["delay"]);
        ss = DOMManager.timeGetSS(tree[index]["delay"]);

        Editor.remote.document.getElementById("run_option3_delay_hh").value = hh;
        Editor.remote.document.getElementById("run_option3_delay_mm").value = mm;
        Editor.remote.document.getElementById("run_option3_delay_ss").value = ss;
      }

    } else
      throw new Error("unsupported mode "+mode);

  } catch(x) {
    throw new Error("RunOption.loadInputFields("+mode+", "+index+"):\n" + x.message);
  }
}


RunOption.clearInputFields = function(mode) {

  try {

    mode = String(mode);

    if ( mode=="1" ) {
      Editor.remote.document.getElementById("run_option1_dir").value = "";
      Editor.remote.document.getElementById("run_option1_regex").value = "";

    } else if ( mode=="2" ) {
      Editor.remote.document.getElementById("run_option2_count").value = "";
      Editor.remote.document.getElementById("run_option2_max").checked = "";
      Editor.remote.document.getElementById("run_option2_delay_hh").value = "";
      Editor.remote.document.getElementById("run_option2_delay_mm").value = "";
      Editor.remote.document.getElementById("run_option2_delay_ss").value = "";

    } else if ( mode=="3" ) {
      Editor.remote.document.getElementById("run_option3_count").value = "";
      
      Editor.remote.document.getElementsByName("run_option3_delay")[1].checked = "checked";

      Editor.remote.document.getElementById("run_option3_delay_hh").value = "";
      Editor.remote.document.getElementById("run_option3_delay_mm").value = "";
      Editor.remote.document.getElementById("run_option3_delay_ss").value = "";

    } else
      throw new Error("unsupported mode "+mode);

  } catch(x) {
    throw new Error("RunOption.clearInputFields("+mode+"):\n" + x.message);
  }
}


RunOption.remove_Highlighting = function(mode) {

  try {

    mode = String(mode);

    if ( RunOption.highlightedRow[mode] != -1 ) {

      tr = Editor.remote.document.getElementById("run_option"+mode+"_tr_"+RunOption.highlightedRow[mode]);
      tr.style.backgroundColor = "white";
      tr.style.color = "black";

      RunOption.highlightedRow[mode] = -1;

      // Btn Remove disablen
      RunOption.btn_remove_enabled(mode, false);
    }

  } catch(x) {
    throw new Error("RunOption.remove_Highlighting():\n" + x.message);
  }
}


RunOption.action_remove = function(mode) {

  try {

    mode = String(mode);

    // RunOption.highlightedRow loeschen

    // nur wenn ein Eintrag markiert ist, kann geloescht werden
    if ( RunOption.highlightedRow[mode] != -1 ) {

      index = RunOption.highlightedRow[mode]; // Index merken
      RunOption.remove_Highlighting(mode);    // Markierung loeschen
      RunOption.removeEntry(mode, index);     // Element aus Baum und Liste entfernen

      // Markierung neu setzen falls noch Elemente in der Liste sind
      if ( RunOption.nrOfEntries[mode]>0 ) {
        if ( index == RunOption.nrOfEntries[mode] )
          index--;

        RunOption.highlightRow(mode, index);

      // Wenn keine Elemente mehr in der Liste sind, Eingabefelder daktivieren
      } else {

        RunOption.inputFields_enabled(mode, false);
        RunOption.clearInputFields(mode);
      }
    }

  } catch(e) {
    Editor.remote.alertError("RunOption.action_remove():", e);
  }
}
