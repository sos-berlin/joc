if( typeof window['SOS_Exception'] != 'function' ) {
  
  function SOS_Exception( err, msg, file, line ) {
    if(msg) alert( msg );
  } 
}
var SOSDebugWindow              = null;
var SOS_Logger                  = function(){}  
/** @access public */           
/** level zum Loggen am Server */
SOS_Logger.logLevel             = 0;
/** Prefix für SOSPrefixLogger */
SOS_Logger.prefix               = '[CLIENT_LOGGER]';
/** URI des Controllers am Server */
SOS_Logger.controllerURI        = '';
/** Aktionsvariable, die zum Server geschickt wird, um die Clientangaben zu protokollieren */
SOS_Logger.controllerAction     = 'log_client=1';
/** soll allg. Server Log-Level gesetzt werden, oder SOS_Logger.logLevel gilt nur fuer die Clientseitige Funktionen **/
SOS_Logger.changeServerLogLevel = false;
/** level zum Debuggen am Client|Browser */
SOS_Logger.debugLevel           = 0;
/** Name des Debugfensters - Wahrscheinlichkeit, dass die Millis bei 2-3 Fenstern gleich sind, ist ziemlich gering :)   */
// Nachteil : bei F5 immer ein neues Fenster, Vorteil : pro Seite eigenes
//SOS_Logger.windowName           = 'sos_debug_window_'+new Date().getMilliseconds();
SOS_Logger.windowName           = 'sos_debug_window';
/** Debugfensters Attribute */
SOS_Logger.windowWidth          = '1000';
SOS_Logger.windowHeight         = '200';
SOS_Logger.windowTop            = '0';
SOS_Logger.windowLeft           = '0';
SOS_Logger.windowProperties     = 'scrollbars=yes,resizable=yes,status=no,toolbar=no,menubar=no';
/** ob das Debugfenster einen Fokus bekommen soll */
SOS_Logger.enableFocus          = false;
/** Style für die Meta-Ausgaben [debug level, Zeit, Funk. name] */
SOS_Logger.styleDebug           = 'color: #009933;';
/** Style für den Debugtext */
SOS_Logger.styleMsg             = '';
/** image Verzeichnis der Anwendung */
SOS_Logger.imgDir               = 'js/util/images/logger/';
/**
* Kontrolelemente werden in der aufrufenden Anwendung gemalt
* zB: <td id="sos_debugger_control"></td>
* zB: <td id="sos_logger_control"></td>
* siehe SOS_Logger.setDebuggerControl()
* siehe SOS_Logger.setLoggerControl()
*/
SOS_Logger.debuggerControlId    = 'sos_debugger_control';
SOS_Logger.loggerControlId      = 'sos_logger_control';
/** Maus Cursor bei den Controlelementen(wenn SOS_Logger.setControls() verwendet wird) */
SOS_Logger.controlCursor        = 'pointer';
/** Steuerungsdivs werden automatisch von der SOS_Logger gemalt und unter folgenden Namen können angesprochen werden */
SOS_Logger.divDebuggerControlId = 'div_sos_debugger';
SOS_Logger.divLoggerControlId   = 'div_sos_logger';
/** Steuerungsdivs automatisch schliessen, wenn log bzw debug level gesetzt wurden */
SOS_Logger.autoCloseControl     = true;
/** JS Kode, die ausgeführt werden soll, wenn Debuglevel|Loglevel gesetzt oder abgeschaltet wird 
* zB.: SOS_Logger.onSetDebugger = 'my_function_1();my_function_2();';
*/
SOS_Logger.onSetDebugger        = '';
SOS_Logger.onSetLogger          = '';
SOS_Logger.hasError             = false;
/** @access private */
SOS_Logger.WARN                 = 11;
SOS_Logger.ERROR                = 12;
SOS_Logger.errorText            = '';
/**
* Debug bzw Log Error setzen
* wird ausgegeben im Browser, wenn debugLevel > 0, mit der Markierung [ERROR] und dem Messagetext rot
* wird geloggt am Server, wenn logLevel > 0, mit der Markierung [ERROR]
*/
SOS_Logger.error = function(msg){
  SOS_Logger.resetError();
  try{
    if(!msg) var msg = '';
    SOS_Logger.debug(SOS_Logger.ERROR,msg,'color: #ff0000;','SOS_Logger.error.caller');
  }
  catch(x){
    SOS_Logger.setError('SOS_Logger.error : '+x.message); 
  }
}
/**
* Debug bzw Log Warning setzen
* wird ausgegeben im Browser, wenn debugLevel > 0, mit der Markierung [WARNING] und Messagetext grün
* wird geloggt am Server, wenn logLevel > 0, mit der Markierung [WARNING]
*/
SOS_Logger.warn = function(msg){
  SOS_Logger.resetError();
  try{
    if(!msg) var msg = '';
    SOS_Logger.debug(SOS_Logger.WARN,msg,'color:#804040','SOS_Logger.warn.caller');
  }
  catch(x){
    SOS_Logger.setError('SOS_Logger.warn : '+x.message);  
  }
}
/** Debug bzw Log setzen */
SOS_Logger.debug = function(level,msg,styleMsg,functionCallerName){
  SOS_Logger.resetError();
  try{
    if(!level)              var level               = 0;
    if(!msg)                var msg                 = '';
    if(!styleMsg)           var styleMsg            = SOS_Logger.styleMsg;
    if(!functionCallerName) var functionCallerName  = 'SOS_Logger.debug.caller';
    var type = '';
    switch(level){
      case SOS_Logger.WARN :  //11
                              type = '[WARNING]';
                              break;      
      case SOS_Logger.ERROR : //12
                              type = '[ERROR]';
                              break;
    }  
    if((1*level <= 1*SOS_Logger.debugLevel) || (type.length > 0 && 1*SOS_Logger.debugLevel > 0)){ 
      SOS_Logger.getDebugWindow();       
      var debugDisplay = (type.length == 0) ? '[debug'+level+']' : type;
      //SOSDebugWindow.document.writeln( '<span style="'+SOS_Logger.styleDebug+'">'+SOS_Logger.now()+' '+debugDisplay+'</span><span style="'+styleMsg+'"><pre style="display:inline;">'+SOS_Logger.htmlentities(msg)+'</pre></span><br />' ); 
      var span        = SOSDebugWindow.document.createElement('span');
      span.innerHTML  = '<span style="'+SOS_Logger.styleDebug+'">'+SOS_Logger.now()+' '+debugDisplay+'</span><span style="'+styleMsg+'"><pre style="display:inline;">'+SOS_Logger.htmlentities(msg)+'</pre></span><br />';
      SOSDebugWindow.document.getElementsByTagName('body')[0].appendChild(span);
      SOSDebugWindow.scrollTo(0,100000000);
    }
    if((1*level <= 1*SOS_Logger.logLevel) || (type.length > 0 && 1*SOS_Logger.logLevel > 0)){
      //SOS_Logger.sendMsg('[ '+SOS_Logger.getFunctionName()+' ]'+msg,type,false);
      SOS_Logger.sendMsg(msg,type,false);
    }    
  }
  catch(x){
    SOS_Logger.setError('SOS_Logger.debug : '+x.message);
  } 
}
/**
* Debug|Log Steuerung onload Event setzen
* Dialoge werden duch Strg + Doppelclick geöffnet
* zB:
* <td id="sos_debugger_control"></td> oder <td id="sos_logger_control"></td>  
* siehe auch Beispiele SOS_Logger.setDebuggerControl bzw SOS_Logger.setLoggerControl
*/
SOS_Logger.setControls = function(){   
  var dc = document.getElementById(SOS_Logger.debuggerControlId);
  var lc = document.getElementById(SOS_Logger.loggerControlId);
  if(dc != null){
    if(SOS_Logger.controlCursor != null && SOS_Logger.controlCursor.length > 0){
      dc.style.cursor = SOS_Logger.controlCursor;
    }
    dc.title = 'Debugger einschalten';
    if(document.all){ dc.ondblclick   = new Function("SOS_Logger.setDebuggerControl(event); return false;");  }
    else            { dc.setAttribute("ondblclick","SOS_Logger.setDebuggerControl(event); return false;");    }
  }
  if(lc != null){
    if(SOS_Logger.controlCursor != null && SOS_Logger.controlCursor.length > 0){
      lc.style.cursor = SOS_Logger.controlCursor;
    }
    lc.title = 'Logger einschalten';   
    if(document.all){ lc.ondblclick   = new Function("SOS_Logger.setLoggerControl(event); return false;");  }
    else            { lc.setAttribute("ondblclick","SOS_Logger.setLoggerControl(event); return false;");    }
  }
}
/**
* Debugsteuerung setzen (wenn SOS_Logger.setControls() nicht verwendet wird)
* Dialog wird duch Strg + Event geöffnet
* Aufruf nur mit einem Parameter SOS_Logger.setDebuggerControl(event), alle anderen werden intern gesetzt
* zB:
*   <td id="sos_debugger_control" ondblclick="SOS_Logger.setDebuggerControl(event)"></td>
*/
SOS_Logger.setDebuggerControl = function(e,setLevel,level){  
  SOS_Logger.resetError();      
  try{
    return SOS_Logger.setLoggerControl(e,setLevel,level,SOS_Logger.divDebuggerControlId);
  }
  catch(x){
    SOS_Logger.setError('SOS_Logger.setDebuggerControl : '+x.message);
  }
}
/**
* Loggersteuerung setzen (wenn SOS_Logger.setControls() nicht verwendet wird)
* Dialog wird duch Strg + Event geöffnet
* Aufruf nur mit einem Parameter SOS_Logger.setLoggerControl(event), alle anderen werden intern gesetzt
* zB:
*   <td id="sos_debugger_control" ondblclick="SOS_Logger.setLoggerControl(event)"></td>
*/
SOS_Logger.setLoggerControl = function(e,setLevel,level,id){
  SOS_Logger.resetError();
  try{
    var dd  = document.getElementById(SOS_Logger.divDebuggerControlId);
    var dl  = document.getElementById(SOS_Logger.divLoggerControlId);
    if(dd == null && dl == null){  SOS_Logger.paintContolDivs(); }
    id      = (typeof id == 'undefined') ? SOS_Logger.divLoggerControlId : id; 
    var div = document.getElementById(id);
    if(div == null) return;     
    
    var isLogger              = (id == SOS_Logger.divLoggerControlId) ? true : false;
    var title                 = (isLogger) ? 'Logger' : 'Debugger';  
    var td                    = (isLogger) ? document.getElementById(SOS_Logger.loggerControlId) : document.getElementById(SOS_Logger.debuggerControlId);
    var div                   = document.getElementById(id);
    var form                  = document.getElementById('form_'+id);      
    var checkbox              = form.elements[id+'_checkbox'];
    var checkbox_auto_close   = form.elements[id+'_checkbox_auto_close'];
    var checkbox_enable_focus = form.elements[id+'_checkbox_enable_focus'];
    var radio                 = form.elements[id+'_radio'];
        
    setLevel  = (typeof setLevel == 'undefined') ? false : true;
    level     = (typeof level    == 'undefined') ? -1    : 1*level;
    
    if(!setLevel){ // dialog öffnen
      var isPressed = false;
      if (document.all){//IE
        var we    = window.event;
        isPressed = we.ctrlKey;
      }
      else{ // bei FF gibts kein we.ctrlLeft
        isPressed  = e.ctrlKey;
      }
      if(!isPressed) return;
      // dialog zum ersten mal geöffnet
      SOS_Logger.makeVisibleControlDiv(id,e);
      if(typeof SOS_Div != 'undefined'){ SOS_Div.makeActiv(id); }
      if(isLogger){ checkbox.checked = SOS_Logger.changeServerLogLevel; }
      return;
    } 
    for(var i=0;i<radio.length;i++){
      if(level == 0){ // abschalten
        radio[i].checked = false;
      }
      else{
        if(radio[i].checked){
          level = 1*radio[i].value; 
        }
      } 
    }
    // setzen oder abschalten geklickt
    switch(level){
      case -1   : 
                  if(isLogger){
                    // keine Loglevel ausgewählt, eventuell server log level setzen                    
                    //servlet log war auf true
                    if(SOS_Logger.changeServerLogLevel == true && !checkbox.checked){
                      SOS_Logger.stopServerLog(); 
                    }
                    SOS_Logger.changeServerLogLevel = checkbox.checked;
                  }                    
                  break;                  
      case 0    : // abschalten
                  SOS_Logger.autoCloseControl = checkbox_auto_close.checked;
                  if(isLogger){
                    SOS_Logger.logLevel             = 0;
                    SOS_Logger.changeServerLogLevel = false;
                    SOS_Logger.stopServerLog();    
                    if( SOS_Logger.onSetLogger != null            &&
                        typeof SOS_Logger.onSetLogger == 'string' && 
                        SOS_Logger.onSetLogger.length > 0){
                      try{
                        eval(SOS_Logger.onSetLogger);
                      }
                      catch(x){
                        throw x;  
                      }  
                    }       
                  }
                  else{
                    SOS_Logger.enableFocus      = checkbox_enable_focus.checked;
                    SOS_Logger.debugLevel       = 0;
                    SOS_Logger.closeDebugWindow();
                    if( SOS_Logger.onSetDebugger != null            && 
                        typeof SOS_Logger.onSetDebugger == 'string' && 
                        SOS_Logger.onSetDebugger.length > 0){
                      try{
                        eval(SOS_Logger.onSetDebugger);
                      }
                      catch(x){
                        throw x;  
                      }  
                    }
                  }
                  if(td != null){
                    td.setAttribute('title',(td.sosTitle) ? td.sosTitle : '');
                    td.innerHTML = (td.sosInnerHTML) ? td.sosInnerHTML : '';
                  }
                  //SOS_Logger.autoCloseControl = true;
                  //SOS_Logger.enableFocus      = true;
                  break;      
      default   :                   
                  SOS_Logger.autoCloseControl = checkbox_auto_close.checked;
                  if(!isLogger){  SOS_Logger.enableFocus = checkbox_enable_focus.checked;  }  
                  // loglevel setzen
                  if(level >= 1 && level <= 9){                    
                    if(isLogger){
                      SOS_Logger.logLevel = level;
                      if(td != null){
                      	if(!td.sosTitle)		{ td.sosTitle 		= td.getAttribute("title");}
                     		if(!td.sosInnerHTML){ td.sosInnerHTML = td.innerHTML;}
                        td.setAttribute('title',title+' ist aktiv. Level '+SOS_Logger.logLevel);
                        td.innerHTML = '<font color="red">!!! '+title+' ist aktiv. Level '+SOS_Logger.logLevel+'</font>';
                        if(checkbox.checked){
                          td.innerHTML += '<br /><font color="red">!!! Servlet loglevel wurde geändert.<br />Level '+SOS_Logger.logLevel+'</font>';   
                        }
                      }   
                      //servlet log war auf true
                      if(SOS_Logger.changeServerLogLevel == true && !checkbox.checked){
                        SOS_Logger.stopServerLog(); 
                      }
                      SOS_Logger.changeServerLogLevel = checkbox.checked;
                      if( SOS_Logger.onSetLogger != null            && 
                          typeof SOS_Logger.onSetLogger == 'string' &&
                          SOS_Logger.onSetLogger.length > 0){
                        try{
                          eval(SOS_Logger.onSetLogger);
                        }
                        catch(x){
                          throw x;  
                        }  
                      }    
                    }                     
                    else{
                      SOS_Logger.debugLevel = level;
                      if(td != null){
                      	if(!td.sosTitle)		{ td.sosTitle 		= td.getAttribute("title");}
                      	if(!td.sosInnerHTML){ td.sosInnerHTML = td.innerHTML;}
                        td.setAttribute('title',title+' ist aktiv. Level '+SOS_Logger.debugLevel);
                        td.innerHTML = '<font color="red">!!! '+title+' ist aktiv. Level '+SOS_Logger.debugLevel+'</font>';
                      }
                      if( SOS_Logger.onSetDebugger != null            && 
                          typeof SOS_Logger.onSetDebugger == 'string' &&  
                          SOS_Logger.onSetDebugger.length > 0){
                        try{
                          eval(SOS_Logger.onSetDebugger);
                        }
                        catch(x){
                          throw x;  
                        }  
                      }
                    }
                  }
    }
    if(SOS_Logger.autoCloseControl){
      if(typeof SOS_Div != 'undefined') { SOS_Div.close(div.id);      }
      else                              { div.style.display = 'none'; }
    }
    try{
      document.getElementById('form_'+SOS_Logger.divDebuggerControlId).elements[SOS_Logger.divDebuggerControlId+'_checkbox_auto_close'].checked   = SOS_Logger.autoCloseControl; 
      document.getElementById('form_'+SOS_Logger.divLoggerControlId).elements[SOS_Logger.divLoggerControlId+'_checkbox_auto_close'].checked       = SOS_Logger.autoCloseControl; 
    }
    catch(x){}
  }
  catch(x){
    alert('SOS_Logger.setLoggerControl : Exception = '+x.message);
  }  
}
/** Debugfenster schliessen */
SOS_Logger.closeDebugWindow = function(){
  try{
    if(typeof SOSDebugWindow != 'undefined' && SOSDebugWindow != null && SOSDebugWindow.closed == false){
      SOSDebugWindow.close();
    }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);  
  }
}
/**
* Protokollierung am Server stoppen, bzw auf urspr. Stand setzen
* wird verwendet, nur wenn logging mit SOS_Logger.changeServerLevel = true gesetzt wurde
*/  
SOS_Logger.stopServerLog = function(){
  try{
    SOS_Logger.sendMsg('','',true);
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);    
  }
}
/**
* Message zum Server schicken
* @param    msg           Text
* @param    type          [WARNING] , [ERROR] oder ganz normal. Debug
* @param    isStopping    boollean, ob es sicht beim Abschalten von LOG|DEBUG Level handelt
*/  
SOS_Logger.sendMsg = function(msg,type,isStopping){
  try{
  	if(typeof SOS_XML_Caller == 'undefined') return;
    if(!msg)        var msg         = '';
    if(!type)       var type        = '';
    if(!isStopping) var isStopping  = false;
    var xml_caller  = new SOS_XML_Caller();
    xml_caller.url  = SOS_Logger.controllerURI+'?'+SOS_Logger.controllerAction;
    var domDocument = xml_caller.getDocument();
    var xmlRoot     = domDocument.createElement('request');
    var xmlLogger   = domDocument.createElement('logger');
    xmlLogger.setAttribute('level',SOS_Logger.logLevel);
    xmlLogger.setAttribute('change_server_log_level',''+SOS_Logger.changeServerLogLevel+'');
    xmlLogger.setAttribute('stopping',''+isStopping+'');
    var xmlPrefix    = domDocument.createElement('prefix');
    var prefixCDATA  = domDocument.createCDATASection(SOS_Logger.prefix);
    xmlPrefix.appendChild(prefixCDATA);
    var xmlMsg       = domDocument.createElement('msg');
    //var msgCDATA   = domDocument.createCDATASection('CLIENT : '+msg);
    // !!! keine CDATA, weil es wird auch xml protokol, was auch CDATA Sektions enthält
    var msgCDATA     = domDocument.createTextNode('[CLIENT]'+type+' : '+msg);
    xmlMsg.appendChild(msgCDATA);
    xmlLogger.appendChild(xmlPrefix);
    xmlLogger.appendChild(xmlMsg);
    xmlRoot.appendChild(xmlLogger);
    domDocument.appendChild(xmlRoot);
    SOS_Logger.debugLog(3,'SOS_Logger.sendMsg REQUEST : '+domDocument.xml);
   	xml_caller.callHTTP(domDocument.xml);
   	SOS_Logger.debugLog(3,'SOS_Logger.sendMsg RESPONSE : '+xml_caller.response_text);
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);  
  }
}
/** LOG XML, die zum Server geschickt wurde, im Debug-Fenster anzeigen  */
SOS_Logger.debugLog = function(level,msg,styleMsg){    
  try{
    if(!level)    var level     = 0;
    if(!msg)      var msg       = '';
    if(!styleMsg) var styleMsg  = SOS_Logger.styleMsg;
    if(1*level <= 1*SOS_Logger.debugLevel){ 
      SOS_Logger.getDebugWindow();        
      //SOSDebugWindow.document.writeln( '<span style="'+SOS_Logger.styleDebug+'">[debug'+level+']['+SOS_Logger.now()+']'+SOS_Logger.getFunctionName()+'</span><span style="'+styleMsg+'"><pre style="display:inline;">'+SOS_Logger.htmlentities(msg)+'</pre></span><br />' ); 
      var span        = SOSDebugWindow.document.createElement('span');
      span.innerHTML  = '<span style="'+SOS_Logger.styleDebug+'">[debug'+level+']['+SOS_Logger.now()+']'+SOS_Logger.getFunctionName()+'</span><span style="'+styleMsg+'"><pre style="display:inline;">'+SOS_Logger.htmlentities(msg)+'</pre></span><br />';
      SOSDebugWindow.document.getElementsByTagName('body')[0].appendChild(span);
      SOSDebugWindow.scrollTo(0,100000000);
    }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);    
  } 
}
/** Prüft ob Debugfenster bereits aufgemacht wurde, oder öffnet es neu */
SOS_Logger.getDebugWindow = function(){
  try{   
    if(typeof SOSDebugWindow == 'undefined' || SOSDebugWindow == null || SOSDebugWindow.closed == true){
      SOSDebugWindow =  window.open('',SOS_Logger.windowName,'top='+SOS_Logger.windowTop+',left='+SOS_Logger.windowLeft+',width='+SOS_Logger.windowWidth+',innerwidth='+SOS_Logger.windowWidth+',height='+SOS_Logger.windowHeight+',innerheight='+SOS_Logger.windowHeight+','+SOS_Logger.windowProperties);
      if(SOSDebugWindow.document.title != 'Job Scheduler - SOS_Logger') {
        SOSDebugWindow.document.title = 'Job Scheduler - SOS_Logger';
        SOSDebugWindow.document.open();
        //SOSDebugWindow.document.writeln('<html><title>Job Scheduler - SOS_Logger</title><body>');
        SOSDebugWindow.document.writeln('<html><title>Job Scheduler - SOS_Logger</title><body></body></html>');
      }
    }
    if(SOS_Logger.enableFocus){ SOSDebugWindow.focus(); }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);    
  }
}
/** Liefert Funktionsname des Debugcallers */
SOS_Logger.getFunctionName = function(functionCallerName){
  var function_name = '';
  try{
    if(!functionCallerName) var functionCallerName = 'SOS_Logger.debug.caller';
    // ab js 1.3 deprecated
    if(typeof eval(functionCallerName) == 'function'){
      function_name = '['+eval(functionCallerName).toString().match(/( \w+)/)[0]+' ]';
      /*
      var func = SOS_Logger.debug.caller.toString();
      //alert(func);
      if(func.search('^(function){1}') != -1){
        var name      = func.substring(0,func.indexOf('\{'));
        function_name = '['+name.replace('function','')+']';    
      }
      else if(func.search('^(var){1}') != -1){
        var name      = func.substring(0,func.indexOf(' ='));
        function_name = '['+name.replace('var ','')+']';    
      }
      else if(func.search('\.prototype\.') != -1){
        var name      = func.substring(0,func.indexOf(' ='));
        function_name = '['+name.replace('prototype\.','')+']';   
      }
      */
    }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);    
  }
 return function_name;
}
/** Liefert die aktuelle Uhrzeit */
SOS_Logger.now = function(){
  date  = new Date();
  day   = date.getDate(); month = date.getMonth()+1; year = date.getYear();
  hours = date.getHours(); minutes = date.getMinutes(); seconds = date.getSeconds();milliseconds = date.getMilliseconds();
  if(day      < 10){  day     = '0'+day;      } 
  if(month    < 10){  month   = '0'+month;    } 
  if(hours    < 10){  hours   = '0'+hours;    } 
  if(minutes  < 10){  minutes = '0'+minutes;  } 
  if(seconds  < 10){  seconds = '0'+seconds;  }
  if(milliseconds  < 10)      { milliseconds = '00'+milliseconds; }
  else if(milliseconds  < 100){ milliseconds = '0'+milliseconds;  }
  return  year+'-'+month+'-'+day+' '+hours+':'+minutes+':'+seconds+'.'+milliseconds;
}
/** Sonderzeichen und HTML-Tags in den dafür vorgesehenen HTML-Code umwandeln */
SOS_Logger.htmlentities = function(param){
if(!param || param == null) param = '';  
return param.replace(/&/g, '&amp;').replace(/</g,'&lt;').replace(/>/g, '&gt;'); 
}
/** Steuerungsdivs malen */
SOS_Logger.paintContolDivs = function(){       
  try{
    var content             = '';
    var hrefStyle           = 'style="font-size:11px"';
    var checkedAutoClose    = (SOS_Logger.autoCloseControl) ? ' checked="checked" ' : '';
    var titleAutoClose      = ' title="Dialog automatisch schliessen,\nwenn eine Operation\n\nSetzen bzw Abschalten\n\nausgeführt wird" ';
    var checkedEnableFocus  = (SOS_Logger.enableFocus)      ? ' checked="checked" ' : '';
    var titleEnableFocus    = ' title="Debugfenster soll einen Fokus erhalten" ';
    var makeActivDiv        = (typeof SOS_Div == 'undefined') ? '' : 'onmousedown="SOS_Div.makeActiv(this)"';
    
    content += '<div '+makeActivDiv+' id="'+SOS_Logger.divDebuggerControlId+'" style="width:170px;position:absolute;z-index:1;top:0px;left:0px;font-size:8pt;white-space:nowrap;border:1px solid #808080;background-color:#FFFFCC;display:none;">\n';
    content += ' <form name="form_'+SOS_Logger.divDebuggerControlId+'" id="form_'+SOS_Logger.divDebuggerControlId+'" action="#" />\n';
    content += ' <table border="0" cellpadding="0" cellspacing="0">\n';
    content += '  <tr>\n';
    content += '    <td id="'+SOS_Logger.divDebuggerControlId+':1" style="cursor:move;height:18px;font-size: 11px;font-weight:bold;color:#ffffff" background="'+SOS_Logger.imgDir+'bg_title_activ.gif">\n';
    content += '      &nbsp;Debugger\n';
    content += '    </td>\n';
    content += '     <td id="'+SOS_Logger.divDebuggerControlId+':2" width="1%" background="'+SOS_Logger.imgDir+'bg_title_activ.gif" align="right" style="cursor:pointer" title="Schliessen" onclick="SOS_Logger.closeControlDiv(\''+SOS_Logger.divDebuggerControlId+'\');"><img src="'+SOS_Logger.imgDir+'icon_close_activ.gif"/></td>\n';
    content += '  </tr>\n';
    content += '\n';
    content += '  <tr>\n';
    content += '    <td colspan="2">&nbsp;</td>\n';
    content += '  </tr>\n';
    
    content += '  <tr>\n';
    content += '    <td '+titleAutoClose+' valign="top" colspan="2" style="cursor:help;font-size:11px">\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <input '+checkedAutoClose+' type="checkbox" name="'+SOS_Logger.divDebuggerControlId+'_checkbox_auto_close" id="'+SOS_Logger.divDebuggerControlId+'_checkbox_auto_close" /> Dialog autom. schliessen\n';
    content += '    </td>\n';
    content += '  </tr>\n';
    content += '  <tr>\n';
    content += '    <td '+titleEnableFocus+' valign="top" colspan="2" style="cursor:help;font-size:11px">\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <input '+checkedEnableFocus+' type="checkbox" name="'+SOS_Logger.divDebuggerControlId+'_checkbox_enable_focus" id="'+SOS_Logger.divDebuggerControlId+'_checkbox_enable_focus" /> Fokus für Debugfenster\n';
    content += '    </td>\n';
    content += '  </tr>\n';
    content += '  <tr>\n';
    content += '    <td colspan="2">&nbsp;</td>\n';
    content += '  </tr>\n';
        
                  for(var d=1;d<=9;d++){
    content += '    <tr>\n';
    content += '      <td colspan="2" style="font-size:11px">\n';
    content += '        &nbsp;&nbsp;\n';
    content += '        <input type="radio" ';
    content += (SOS_Logger.debugLevel == d) ? ' checked="checked" ' : '';
    content += '        name="'+SOS_Logger.divDebuggerControlId+'_radio" id="'+SOS_Logger.divDebuggerControlId+'_radio" value="'+d+'" /> debuglevel '+d+'\n';
    content += '      </td>\n';
    content += '   </tr>\n';
                  }
    content += '  <tr style="height:30px">\n';
    content += '    <td valign="bottom" colspan="2" style="font-size:11px">\n';
    content += '      &nbsp;&nbsp;&nbsp;&nbsp;\n';
    content += '      <a '+hrefStyle+' title="Debuglevel setzen" href="#" onclick="SOS_Logger.setDebuggerControl(event,true);return false;"><b>Setzen</b></a>\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <a '+hrefStyle+' title="Debugger abschalten" href="#" onclick="SOS_Logger.setDebuggerControl(event,true,\'0\');return false;">\n';
    content += '        <b>Abschalten</b>\n';
    content += '      </a>\n';
    content += '    </td>\n';
    content += '  </tr>\n';
    content += ' </table>\n';
    content += ' </form>\n';
    content += ' <br />\n';
    content += '</div>\n';
    content += '\n\n';
    
    content += '<div '+makeActivDiv+' id="'+SOS_Logger.divLoggerControlId+'" style="width:200px;position:absolute;z-index:1;top:0px;left:100px;font-size:8pt;white-space:nowrap;border:1px solid #808080;background-color:#FFFFCC;display:none;">\n';
    content += '<form name="form_'+SOS_Logger.divLoggerControlId+'" id="form_'+SOS_Logger.divLoggerControlId+'" action="#" />\n';
    content += '<table border="0" cellpadding="0" cellspacing="0">\n';
    content += '  <tr>\n';
    content += '    <td id="'+SOS_Logger.divLoggerControlId+':1" style="cursor:move;height:18px;font-size: 11px;font-weight:bold;color:#ffffff" background="'+SOS_Logger.imgDir+'bg_title_activ.gif">\n';
    content += '      &nbsp;Logger\n';
    content += '    </td>\n';
    content += '    <td id="'+SOS_Logger.divLoggerControlId+':2" width="1%" background="'+SOS_Logger.imgDir+'bg_title_activ.gif" align="right" style="cursor:pointer" title="Schliessen" onclick="SOS_Logger.closeControlDiv(\''+SOS_Logger.divLoggerControlId+'\');"><img src="'+SOS_Logger.imgDir+'icon_close_activ.gif"/></td>\n';
    content += '  </tr>\n';
    content += '\n';  
    content += '  <tr>\n';
    content += '    <td colspan="2">&nbsp;</td>\n';
    content += '  </tr>\n';
    content += '\n';  
    
    content += '  <tr style="height:30px">\n';
    content += '    <td '+titleAutoClose+' valign="top" colspan="2" style="cursor:help;font-size:11px">\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <input '+checkedAutoClose+' type="checkbox" name="'+SOS_Logger.divLoggerControlId+'_checkbox_auto_close" id="'+SOS_Logger.divLoggerControlId+'_checkbox_auto_close" /> Dialog autom. schliessen\n';
    content += '    </td>\n';
    content += '  </tr>\n';
    
    
    content += '  <tr style="height:30px">\n';
    content += '    <td title="Server loglevel wird geändert" valign="top" colspan="2" style="cursor:help;font-size:11px">\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <input type="checkbox" name="'+SOS_Logger.divLoggerControlId+'_checkbox" id="'+SOS_Logger.divLoggerControlId+'_checkbox" /> Server loglevel ändern\n';
    content += '    </td>\n';
    content += '  </tr>\n';
                  for(var d=1;d<=9;d++){
    content += '    <tr>\n';
    content += '      <td colspan="2" style="font-size:11px">\n';
    content += '        &nbsp;&nbsp;\n';
    content += '        <input type="radio" ';
    content += (SOS_Logger.logLevel == d) ? ' checked="checked" ' : '';
    content += '        name="'+SOS_Logger.divLoggerControlId+'_radio" id="'+SOS_Logger.divLoggerControlId+'_radio" value="'+d+'" /> loglevel '+d+'\n';
    content += '      </td>\n';
    content += '  </tr>\n';
                  }
    content += '  <tr style="height:30px">\n';
    content += '    <td valign="bottom" colspan="2" style="font-size:11px">\n';
    content += '      &nbsp;&nbsp;&nbsp;&nbsp;\n';
    content += '      <a '+hrefStyle+' title="Loglevel setzen" href="#" onclick="SOS_Logger.setLoggerControl(event,true);return false;"><b>Setzen</b></a>\n';
    content += '      &nbsp;&nbsp;\n';
    content += '      <a '+hrefStyle+' title="Logger abschalten" href="#" onclick="SOS_Logger.setLoggerControl(event,true,\'0\');return false;">\n';
    content += '        <b>Abschalten</b>\n';
    content += '      </a>\n';
    content += '\n';      
    content += '    </td>\n';
    content += '  </tr>\n';
    content += '\n';  
    content += '</table>\n';
    content += '</form>\n';
    content += '<br />\n';
    content += '</div>\n';    
    //document.getElementsByTagName('body')[0].innerHTML += content;
    var span        = document.createElement('span');
    span.innerHTML  = content;
    document.getElementsByTagName('body')[0].appendChild(span);
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);  
  }
}
/** Steuerungsdivs schliessen */
SOS_Logger.closeControlDiv = function(id){
  try{
    var div   =  document.getElementById(id);
    var form  =  document.getElementById('form_'+id);
    switch(id){
      case SOS_Logger.divLoggerControlId :
                                    var checkbox    = form.elements[SOS_Logger.divLoggerControlId+'_checkbox'];
                                    var radio       = form.elements[SOS_Logger.divLoggerControlId+'_radio'];
                                    checkbox.checked = SOS_Logger.changeServerLogLevel;
                                    for(var i=0;i<radio.length;i++){
                                      radio[i].checked = false;
                                      if(radio[i].value == SOS_Logger.logLevel){
                                        radio[i].checked = true;  
                                      } 
                                    }
                                    break;
      case SOS_Logger.divDebuggerControlId :
                                    var radio       = form.elements[SOS_Logger.divDebuggerControlId+'_radio'];
                                    for(var i=0;i<radio.length;i++){
                                      radio[i].checked = false;
                                      if(radio[i].value == SOS_Logger.debugLevel){
                                        radio[i].checked = true;  
                                      } 
                                    }
                                    break;
    }
    if(typeof SOS_Div != 'undefined') { SOS_Div.close(div.id);      }
    else                              { div.style.display = 'none'; }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0);  
  }  
}
/** SteuerungsDIVs sichtbar machen und autom. positionieren */
SOS_Logger.makeVisibleControlDiv = function(id ,e ) {  
  try{
    if(document.getElementById(id).style.display != 'none') {return;}
    if (!e){ 
      var e = window.event;
    } 
    if(e){
      var dc          = document.getElementById(SOS_Logger.divDebuggerControlId);
      var lc          = document.getElementById(SOS_Logger.divLoggerControlId);
      var isDebugger  = (id == SOS_Logger.divDebuggerControlId) ? true : false;
      var windowWidth = document.body.clientWidth;
      var topMargin   = 20;
      var leftMargin  = 20;        
      var dcLeft      = parseInt(dc.style.left);
      var dcWidth     = parseInt(dc.style.width);
      var lcLeft      = parseInt(lc.style.left);
      var lcWidth     = parseInt(lc.style.width);
      var top         = e.clientY+topMargin;   
      if(isDebugger){ 
        if(lc.style.display != 'none'){
          if(((lcLeft + lcWidth) + 2*leftMargin + dcWidth) >= windowWidth){
            dc.style.left = lcLeft-leftMargin-dcWidth; 
          }
          else{
            dc.style.left = lcLeft+lcWidth+leftMargin;
          }
          top = parseInt(lc.style.top);
        }
        else{
          var left =  e.clientX - parseInt(dc.style.width);
          if(left <= 0){
            left = leftMargin + parseInt(dc.style.width);
          }
          dc.style.left = left+'px';
        }
      }
      else{
        if(dc.style.display != 'none'){
          if(((dcLeft + dcWidth) + 2*leftMargin + lcWidth) >= windowWidth){
            lc.style.left = dcLeft-leftMargin-lcWidth; 
          }
          else{
            lc.style.left = dcLeft+dcWidth+leftMargin;
          }
          top = parseInt(dc.style.top);
        }
        else{
          var left =  e.clientX - parseInt(lc.style.width);
          if(left <= 0){
            left = leftMargin + parseInt(lc.style.width);
          }
          lc.style.left = left+'px';
        }
      }
      document.getElementById(id).style.top     = top+'px';
      document.getElementById(id).style.display = '';
    }
  }
  catch(x){
    throw new SOS_Exception(x,x.message,'',0); 
  }
}
/** setzt Fehlerzustand */
SOS_Logger.setError = function(errorText){
  if(!errorText) var errorText = '';
  SOS_Logger.errorText = errorText;
  SOS_Logger.hasError  = true;
}
/** */
SOS_Logger.getError = function(){
  return SOS_Logger.errorText;
}
/** setzt Fehlerzustand zurück */
SOS_Logger.resetError = function(){
  SOS_Logger.errorText = '';
  SOS_Logger.hasError  = false;
}
//SOSDebugWindow.document.execCommand('SaveAs', true,'sos_logger.txt');
