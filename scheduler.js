// $Id: scheduler.js,v 1.21 2004/12/02 10:17:36 jz Exp $

//----------------------------------------------------------------------------------------------var

var _popup;

// Die Variablen enthalten die Versionnummer (numerisch, z.B. 5.5) des Browsers, oder 0.
var ie       = 0;   // Microsoft Internet Explorer
var netscape = 0;   // Netscape
var firefox  = 0;   // Mozilla Firefox
var opera    = 0;   // Opera

//--------------------------------------------------------------------------------------------const

var NODE_ELEMENT = 1;

//------------------------------------------------------------------------------------check_browser

function check_browser()
{
    if( window != undefined )
    {
        if( window.navigator != undefined  &&  window.navigator.appName )
        {
            if( window.navigator.appName == "Microsoft Internet Explorer" )
            {
                //if( !window.clientInformation )       K�nnte Opera sein, oder auch nicht.
                //{
                //    opera = 7;  // Vermutlich Opera, der sich als ie ausgibt. Aber welche Version von Opera ist das?
                //}
                //else
                {
                    var match = window.navigator.appVersion.match( /MSIE (\d+\.\d+);/ );
                    if( match )  ie = 1 * RegExp.$1;
                }
            }
            else
            if( window.navigator.appName == "Netscape" )
            {
                if( window.navigator.vendor == "Firefox" )
                {
                    if( window.navigator.productSub >= 20041122 )  firefox = 1.0; 
                }
                else
                {
                    var match = window.navigator.appVersion.match( /^(\d+\.\d+) / );
                    if( match )  netscape = 1 * RegExp.$1;
                }
            }
        }
    }
    
    if( ie < 6.0  &&  firefox < 1.0 )  
    {
        var msg = "The page may not work with this browser, which doesn't seem to be one of the following:\n" + 
                  "Microsoft Internet Explorer 6\n" +
                  "Mozilla Firefox 1";
                  
        if( window.navigator != undefined )
        {
            msg += "\n\n\n";
            msg += "appName="         + window.navigator.appName            + "\n";
            msg += "appVersion="      + window.navigator.appVersion         + "\n";
            msg += "appMinorVersion=" + window.navigator.appMinorVersion    + "\n";
            msg += "vendor="          + window.navigator.vendor             + "\n";
            msg += "product="         + window.navigator.product            + "\n";
            msg += "productSub="      + window.navigator.productSub         + "\n";
        }
        
        alert( msg );
    }
}

//----------------------------------------------------------------------------------------Scheduler
// public

function Scheduler()
{
    this._url           = "http://" + document.location.host + "/";
    this._xml_http      = window.XMLHttpRequest? new XMLHttpRequest() : new ActiveXObject( "Msxml2.XMLHTTP" );
    this._log_window    = undefined;
    //this._configuration = new Scheduler_html_configuration( this._url + "config.xml" );
}

//----------------------------------------------------------------------------------Scheduler.close
// public

Scheduler.prototype.close = function()
{
    //this._configuration = null;
    this._xml_http = null;
    
    if( this._log_window )  this._log_window.close(),  this._log_window = undefined;
}

//--------------------------------------------------------------------------------Scheduler.execute
// public

Scheduler.prototype.execute = function( xml )
{
    this.call_http( xml );

    var dom_document;
    
    if( window.DOMParser ) 
    {
        var dom_parser = new DOMParser();
        dom_document = dom_parser.parseFromString( this._xml_http.responseText, "text/xml" );
        if( dom_document.documentElement.nodeName == "parsererror" )  throw new Error( "Fehler in der XML-Antwort: " + dom_document.documentElement.firstChild.nodeValue );
    }
    else
    {
        dom_document = new ActiveXObject( "MSXML2.DOMDocument" );
        var ok = dom_document.loadXML( this._xml_http.responseText );
        if( !ok )  throw new Error( "Fehlerhafte XML-Antwort: " + dom_document.parseError.reason );
    }
    
    var error_element = dom_document.selectSingleNode( "spooler/answer/ERROR" );
    if( error_element )
    {
        throw new Error( error_element.getAttribute( "text" ) );
    }

    this.modify_datetime_for_xslt( dom_document );
    
    return dom_document;
}

//------------------------------------------------------------------Scheduler.execute_and_fill_html
// public
/*
Scheduler.prototype.execute_and_fill_html = function( xml_query )
{
    var dom_document   = this.execute( xml_query );
    var answer_element = dom_document.selectSingleNode( "spooler/answer" );
    
    //dom_document.setProperty( "SelectionLanguage", "XPath" );
    if( !answer_element )  return;

    this.fill_html_state( document.all.scheduler_state, answer_element.selectSingleNode( "state" ) );
    this.fill_html_jobs ( document.all.scheduler_jobs , answer_element.selectSingleNode( "state/jobs" ) );
}
*/
//------------------------------------------------------------------------Scheduler.fill_html_state
/*
Scheduler.prototype.fill_html_state = function( html_element, state_response_element )
{
    if( html_element )
    {
        var htmls = new Array();
        var state_config_element = this._configuration._dom.selectSingleNode( "scheduler/html/state" );
        
        if( state_response_element && state_config_element )
        {
            var state_element = state_config_element.cloneNode( true );
            this.fill_spans( state_element, state_response_element );
            
            for( var child = state_element.firstChild; child; child = child.nextSibling )  htmls.push( child.xml );
        }
        
        html_element.innerHTML = htmls.join( "" );
    }
}
*/
//-------------------------------------------------------------------------Scheduler.fill_html_jobs
/*
Scheduler.prototype.fill_html_jobs = function( html_element, jobs_response_element )
{
    if( html_element )
    {
        var htmls = new Array();
        var job_config_element = this._configuration._dom.selectSingleNode( "scheduler/html/job" );
        
        if( jobs_response_element && job_config_element )
        {
            var job_response_elements = jobs_response_element.selectNodes( "job" );
            for( var i = 0; i < job_response_elements.length; i++ )
            {
                var job_response_element = job_response_elements[ i ];

                var job_element = job_config_element.cloneNode( true );
                
                var tasks_element = job_element.selectSingleNode( ".//scheduler.tasks" );
                if( tasks_element )
                {
                    this.fill_html_tasks( tasks_element, jobs_response_element.selectSingleNode( "tasks" ) );
                    tasks_element.nodeName = "span";
                }
                
                this.fill_spans( job_element, job_response_element );
                for( var child = job_element.firstChild; child; child = child.nextSibling )  htmls.push( child.xml );
            }
        }    

        html_element.innerHTML = htmls.join( "" );
    }
}
*/
//-------------------------------------------------------------------------Scheduler.fill_html_jobs
/*
Scheduler.prototype.make_html = function( html_element, response_element, config_element, sub_element_name )
{
    var result = null;
    
    if( html_element )
    {
        var htmls = new Array();
        
        if( response_element && config_element )
        {
            var sub_response_elements = response_element.selectNodes( sub_element_name );

            for( var i = 0; i < sub_response_elements.length; i++ )
            {
                var sub_response_element = sub_response_elements[ i ];

                var result = config_element.cloneNode( true );
                
                var tasks_element = job_element.selectSingleNode( ".//scheduler.tasks" );
                if( tasks_element )
                {
                    this.fill_html_tasks( tasks_element, jobs_response_element.selectSingleNode( "tasks" ) );
                    tasks_element.nodeName = "span";
                }
                
                this.fill_spans( job_element, job_response_element );
            }
        }    
    }

    return result;
}
*/
//-----------------------------------------------------------------------------Scheduler.fill_spans
/*
Scheduler.prototype.fill_spans = function( html_element, xml_element )
{
    var span_elements = html_element.getElementsByTagName( "span" );
    for( var i = 0; i < span_elements.length; i++ )
    {
        var span_element = span_elements[ i ];
        var text = span_element.text;
        if( text.substring( 0, 1 ) == "=" )
        {
            try
            {
                var found_element = xml_element.selectSingleNode( text.substring( 1, text.length ) );
                span_element.text = found_element? found_element.text : "";
            }
            catch( x ) { span_element.text = x.message; }
        }
    }
}
*/
//------------------------------------------------------------------------------Scheduler.call_http

Scheduler.prototype.call_http = function( text, debug_text )
{
    this._xml_http.open( "POST", this._url, false );
    this._xml_http.setRequestHeader( "Cache-Control", "no-cache" );

    var status = window.status;
    window.status = "Waiting for response from scheduler ...";//text;

    try
    {
        this._xml_http.send( text );
    }
    finally
    {
        window.status = status;
    }
}

//-------------------------------------------------------Scheduler.add_datetime_attributes_for_xslt

Scheduler.prototype.add_datetime_attributes_for_xslt = function( response, now, attribute_name )
{
    var elements = response.selectNodes( "//*[ @" + attribute_name + "]" );
    for( var i = 0; i < elements.length; i++ )
    {
        var element = elements[ i ];
        var value   = element.getAttribute( attribute_name );
        if( value )
        {
            element.setAttribute( attribute_name + "__xslt_datetime"               , xslt_format_datetime     ( value, now ) );
            element.setAttribute( attribute_name + "__xslt_datetime_diff"          , xslt_format_datetime_diff( value, now, false ) );
            element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , xslt_format_datetime_with_diff( value, now, false ) );
            element.setAttribute( attribute_name + "__xslt_datetime_with_diff_plus", xslt_format_datetime_with_diff( value, now, true ) );
            element.setAttribute( attribute_name + "__xslt_date_or_time"           , xslt_format_date_or_time ( value, now ) );
        }
    }    
}

//---------------------------------------------------------------Scheduler.modify_datetime_for_xslt

Scheduler.prototype.modify_datetime_for_xslt = function( response )
{
    // F�r Firefox, dass kein Skript im Stylesheet zul�sst.
    var now;

    var datetime = response.selectSingleNode( "/spooler/answer/@time" );
    if( datetime )  now = date_from_datetime( datetime.nodeValue );   
        
    this.add_datetime_attributes_for_xslt( response, now, "time"                  );
    this.add_datetime_attributes_for_xslt( response, now, "spooler_running_since" );
    this.add_datetime_attributes_for_xslt( response, now, "running_since"         );
    this.add_datetime_attributes_for_xslt( response, now, "in_process_since"      );
    this.add_datetime_attributes_for_xslt( response, now, "spooler_running_since" );
    this.add_datetime_attributes_for_xslt( response, now, "next_start_time"       );
    this.add_datetime_attributes_for_xslt( response, now, "start_at"              );
    this.add_datetime_attributes_for_xslt( response, now, "idle_since"            );
    this.add_datetime_attributes_for_xslt( response, now, "enqueued"              );
    this.add_datetime_attributes_for_xslt( response, now, "created"               );
}

//---------------------------------------------------------------------xslt_format_datetime

function xslt_format_datetime( datetime ) 
{
    if( !datetime )  return "";
    return datetime.replace( /\.\d*$/, "" );
    /*            
    var date = typeof datetime == "string"? date_from_datetime( datetime ) : datetime;
    
    //var ms = date.getMilliseconds();

    return date.toLocaleDateString() + ", " + date.toLocaleTimeString();
            //+ ( ms? ".<span class='milliseconds'>" + ( ms + "000" ).substring( 0, 3 ) + "</span>" : "" );
    */                   
}

//-----------------------------------------------------------------xslt_format_date_or_time

function xslt_format_date_or_time( datetime ) 
{
    if( !datetime )  return "";
    
    var now = new Date();
    
    if(    1*datetime.substr( 0, 4 ) == now.getYear()
        && 1*datetime.substr( 5, 2 ) == now.getMonth() + 1
        && 1*datetime.substr( 8, 2 ) == now.getDate()  )
    {
        return datetime.substr( 11, 8 );
    }
    else
    {
        return datetime.substr( 0, 10 );
    }
}

//-----------------------------------------------------------------xslt_format_date_or_time

function xslt_format_datetime_with_diff( datetime, now, show_plus )
{
    var date = date_from_datetime( datetime );
    var result = xslt_format_datetime( datetime );
    if( result && now )  result += " \xA0(" + xslt_format_datetime_diff( date, now, show_plus ) + ")";
    
    return result;
}

//----------------------------------------------------------------xslt_format_datetime_diff

function xslt_format_datetime_diff( datetime_earlier, datetime_later, show_plus ) 
{
    var show_ms;
    if( show_ms   == undefined )  show_ms   = false;
    if( show_plus == undefined )  show_plus = false;
    
    var date_later   = typeof datetime_later   == "string"? date_from_datetime( datetime_later )   : datetime_later;
    var date_earlier = typeof datetime_earlier == "string"? date_from_datetime( datetime_earlier ) : datetime_earlier;

    if( !date_later   )  return "";
    if( !date_earlier )  return "";
    
    var diff = ( date_later.getTime() - date_earlier.getTime() ) / 1000.0;
    var abs  = Math.abs( diff );
    var result;

    if( abs < 60 )
    {
        if( show_ms ) 
        {
            result = abs.toString();
            if( result.match( "." ) )  result = result.replace( ".", ".<span class='milliseconds'>" ) + "</span>";
        }
        else
        {
            result = Math.floor( abs );
        }
        result += "s";
    }
    else
    if( abs <    60*60 )  result = Math.floor( abs / (       60 ) ) + "min";
    else
    if( abs < 24*60*60 )  result = Math.floor( abs / (    60*60 ) ) + "h";
    else
                          result = Math.floor( abs / ( 24*60*60 ) ) + "days";
                            
    return diff < 0             ? "-" + result : 
           show_plus && diff > 0? "+" + result
                                : result;
}

//-----------------------------------------------------------------------date_from_datetime
// datetime == yyyy-mm-dd-hh-mm-ss[.mmm]

function date_from_datetime( datetime ) 
{
    if( !datetime )  return null;
    
    var date = new Date( 1*datetime.substr( 0, 4 ), 
                         1*datetime.substr( 5, 2 ) - 1, 
                         1*datetime.substr( 8, 2 ),
                         1*datetime.substr( 11, 2 ),
                         1*datetime.substr( 14, 2 ),
                         1*datetime.substr( 17, 2 ),
                         datetime.length < 23? 0 : 1*datetime.substr( 20, 3 ) );
    
    return date;
}

//---------------------------------------------------------------------Scheduler_html_configuration
/*
function Scheduler_html_configuration( url )
{
    this._dom = new ActiveXObject( "MSXML2.DOMDocument" );

    this._dom.async = false;
    this._dom.validateOnParse = false;  // Wegen DOCTYPE in config.xml (aber warum?)
    var ok  = this._dom.load( url );
    if( !ok )  throw new Error( "Fehler in der Konfiguration " + url + ": " + this._dom.parseError.reason );
}
*/
//----------------------------------------------------------------------------------update__onclick

function update__onclick()
{
    window.parent.left_frame.reset_error();
    update();
}

//----------------------------------------------------------------show_order_jobs_checkbox__onclick

function show_order_jobs_checkbox__onclick()
{
    save_checkbox_state( "show_order_jobs_checkbox" );
    window.parent.left_frame.reset_error();
    update();
}

//---------------------------------------------------------------------show_tasks_checkbox__onclick

function show_tasks_checkbox__onclick()
{
    save_checkbox_state( "show_tasks_checkbox" );
    window.parent.left_frame.reset_error();
    update();
}

//------------------------------------------------------------show_job_chain_jobs_checkbox__onclick

function show_job_chain_jobs_checkbox__onclick( event )
{
    save_checkbox_state( "show_job_chain_jobs_checkbox" );
    window.parent.left_frame.reset_error();
    update();
}

//----------------------------------------------------------show_job_chain_orders_checkbox__onclick

function show_job_chain_orders_checkbox__onclick()
{
    save_checkbox_state( "show_job_chain_orders_checkbox" );
    window.parent.left_frame.reset_error();
    update();
}

//-------------------------------------------------------------------Popup_menu_builder.add_command
// Erweiterung von Popup_menu_builder, s. popup_builder.js

function Popup_menu_builder__add_command( html, xml_command, is_active )
{
    this.add_entry( html, "parent.popup_menu__execute( &quot;" + xml_command + "&quot; )", is_active );
}

//-------------------------------------------------------------------------------popup_menu.execute
// F�r Popup_menu_builder.add_command()

function popup_menu__execute( xml_command )
{
    _popup.hide();
    
    try
    {
        _scheduler.execute( xml_command );
        window.parent.left_frame.update();
    }
    catch( x )
    {
        if( x.number + 0xFFFFFFFF == 0x800C0007 )
        {
            alert( "Scheduler connection closed" );
        }
        else
        {
            throw x;
            //alert( "Error 0x" + hex_string( x.number, 8 ) + ": " + x.message );
        }
    }
}

//-----------------------------------------------------------------------Popup_menu_builder.add_show_log
// Erweiterung von Popup_menu_builder, s. popup_builder.js

function Popup_menu_builder__add_show_log( html, show_log_command, window_name, is_active )
{
    this.add_entry( html, "parent.popup_menu__show_log__onclick( &quot;" + show_log_command + "&quot;, &quot;" + window_name + "&quot; )", is_active );
}

//--------------------------------------------------------------------popup_menu__show_log__onclick
// F�r Popup_menu_builder.add_show_log()

function popup_menu__show_log__onclick( show_log_command, window_name )
{
    //window_name = window_name.replace( /[~a-zA-Z0-9]/g, "_" );
    window_name = "show_log";  // Nur ein Fenster. ie6 will nicht mehrere Logs gleichzeitig lesen, nur nacheinander

    var features = "menubar=no, toolbar=no, location=no, directories=no, scrollbars=yes, resizable=yes, status=no";
    features +=   " width="  + ( window.screen.availWidth - 11 ) +
                 ", height=" + ( Math.floor( window.screen.availHeight * 0.2 ) - 32 ) +
                 ", left=0"  +
                 ", top="    +  Math.floor( window.screen.availHeight * 0.8 );
    
    var log_window = window.open( "http://" + document.location.host + "/" + show_log_command, window_name, features, true );
    log_window.focus();
    
    if( _scheduler )  _scheduler._log_window = log_window;
    
    _popup.hide();
}

//--------------------------------------------------------------------------scheduler_menu__onclick

function scheduler_menu__onclick()
{
    var popup_builder = new Popup_menu_builder();
    
    var state = _response.selectSingleNode( "spooler/answer/state" ).getAttribute( "state" );
    
    var command = function( cmd ) { return "<modify_spooler cmd='" + cmd + "'/>"; }
    
    popup_builder.add_show_log( "Show log"                       , "show_log", "show_log" );
    popup_builder.add_bar();
  //popup_builder.add_command ( "Stop"                           , command( "stop" ), state != "stopped"  &&  state != "stopping"  &&  state != "stopping_let_run" );
    popup_builder.add_command ( "Pause"                          , command( "pause"                         ), state != "paused" );
    popup_builder.add_command ( "Continue"                       , command( "continue"                      ), state == "paused" );
    popup_builder.add_bar();
  //popup_builder.add_command ( "Reload"                         , command( "reload"                        ), state != "stopped"  &&  state != "stopping" );
    popup_builder.add_command ( "Terminate"                      , command( "terminate"                     ) );
    popup_builder.add_command ( "Terminate and restart"          , command( "terminate_and_restart"         ) );
    popup_builder.add_command ( "Let run, terminate and restart" , command( "let_run_terminate_and_restart" ) );
    popup_builder.add_bar();
    popup_builder.add_command ( "Abort immediately"              , command( "abort_immediately"             ) );
    popup_builder.add_command ( "Abort immediately and restart"  , command( "abort_immediately_and_restart" ) );
    
    _popup = popup_builder.show_popup();
}

//--------------------------------------------------------------------------------job_menu__onclick

function job_menu__onclick( job_name )
{
    var popup_builder = new Popup_menu_builder();

    var job_element = //document.all._job_element != undefined?
                        _job_element  // _job_element in task_frame.html (detail_frame)                                                                                  // Rechter Rahmen
                      //: _response.selectSingleNode( "spooler/answer/state/jobs/job [ @job = '" + job_name + "' ]" );  // Linker Rahmen
    
    var state = job_element.getAttribute( "state" );
    
    popup_builder.add_show_log( "Show log"        , "show_log&job=" + job_name, "show_log_job_" + job_name );
    
    var description_element = job_element.selectSingleNode( "description" );
    var is_active = description_element? description_element.text != "" : false;
    popup_builder.add_entry   ( "Show description", "parent.show_job_description()", is_active );
    
    popup_builder.add_bar();
    popup_builder.add_command ( "Start task now", "<start_job job='" + job_name + "'/>" );
    popup_builder.add_command ( "Stop"          , "<modify_job job='" + job_name + "' cmd='stop'    />", state != "stopped"  &&  state != "stopping" );
    popup_builder.add_command ( "Unstop"        , "<modify_job job='" + job_name + "' cmd='unstop'  />", state == "stopped"  ||  state == "stopping" );
//    popup_builder.add_command ( "Wake"          , "<modify_job job='" + job_name + "' cmd='wake'    />" );
    popup_builder.add_command ( "Start at runtime"         , "<modify_job job='" + job_name + "' cmd='start'   />" );
    popup_builder.add_command ( "Reread"        , "<modify_job job='" + job_name + "' cmd='reread'  />" );
    popup_builder.add_bar();
    popup_builder.add_command ( "End tasks"     , "<modify_job job='" + job_name + "' cmd='end'     />" );
    popup_builder.add_command ( "Suspend tasks" , "<modify_job job='" + job_name + "' cmd='suspend' />" );
    popup_builder.add_command ( "Continue tasks", "<modify_job job='" + job_name + "' cmd='continue'/>" );
    
    _popup = popup_builder.show_popup();
}

//-------------------------------------------------------------------------------task_menu__onclick

function task_menu__onclick( task_id )
{
    var popup_builder = new Popup_menu_builder();

    popup_builder.add_show_log( "Show log"        , "show_log&task=" + task_id, "show_log_task_" + task_id );
    popup_builder.add_bar();
    popup_builder.add_command ( "End"             , "<kill_task job='" + _job_name + "' id='" + task_id + "'/>" );
    popup_builder.add_command ( "Kill immediately", "<kill_task job='" + _job_name + "' id='" + task_id + "' immediately='yes'/>" );
    
    _popup = popup_builder.show_popup();
}

//-------------------------------------------------------------------------------task_menu__onclick

function task_menu__onclick( task_id )
{
    var popup_builder = new Popup_menu_builder();

    popup_builder.add_show_log( "Show log"        , "show_log&task=" + task_id, "show_log_task_" + task_id );
    popup_builder.add_bar();
    popup_builder.add_command ( "End"             , "<kill_task job='" + _job_name + "' id='" + task_id + "'/>" );
    popup_builder.add_command ( "Kill immediately", "<kill_task job='" + _job_name + "' id='" + task_id + "' immediately='yes'/>" );
    
    _popup = popup_builder.show_popup();
}

//------------------------------------------------------------------------------order_menu__onclick

function order_menu__onclick( job_chain_name, order_id )
{
    var popup_builder = new Popup_menu_builder();

    popup_builder.add_show_log( "Show log"        , "show_log&job_chain=" + job_chain_name + 
                                                            "&order=" + order_id, "show_log_order_" + job_chain_name + "__" + order_id );
    
    _popup = popup_builder.show_popup();
}

//-------------------------------------------------------------------------------string_from_object

function string_from_object( object )
{
    var result = "{";
    for( var i in object )  result += i + "=" + object[ i ] + " ";
    return result + "}";
}

//--------------------------------------------------------------------------------------hex_string

function hex_string( value, min_length )
{
    var result = "";
    var hex    = "0123456789ABCDEF";

    if( min_length == undefined )  min_length = 1

    do
    {
        var digit = value % 16;
        if( digit < 0 )  digit += 15;
        result = hex.substring( digit, digit + 1 ) + result;
        value >>>= 4;
    }
    while( value != 0  ||  result.length < min_length );

    return result;
}

//---------------------------------------------------------------------------------------xml_encode

function xml_encode( text )
{
    if( text == null )  return "";
    return text.toString().replace( /&/g, "&amp;" ).replace( /</g, "&lt;" ).replace( />/g, "&gt;" );
    //TODO Regul�re Ausdr�cke vorkompilieren
}

//-----------------------------------------------------------------------------------scheduler_init

function scheduler_init()
{
    Popup_menu_builder.prototype.add_command  = Popup_menu_builder__add_command;
    Popup_menu_builder.prototype.add_show_log = Popup_menu_builder__add_show_log;
}

//-------------------------------------------------------------------------------string_from_object

function string_from_object( object )
{
    var result = "{";

    for( var i in object )
    {
        result += i + "=";
        
        try
        {
            result += object[ i ] + " ";
        }
        catch( x )
        {
            result += "(ERROR " + x.message + ") ";
        }
    }

    return result + "}";
}

/*

function __XMLNodes(result) 
{
    this.length = 0;
    this.pointer = 0;
    this.array = new Array();
    var i = 0;
    while((this.array[i]=result.iterateNext())!=null)  i++;
    this.length = this.array.length;
}

XMLNodes.prototype.nextNode = function() 
{
    this.pointer++;
    return this.array[pointer-1];
}

XMLNodes.prototype.reset = function() 
{
    this.pointer = 0;
}

XMLDocument.prototype.selectNodes = function(tagname) 
{
    var result = this.evaluate(tagname, this, null, 0, null);
    var xns = new __XMLNodes(result);
    return xns;
}


*/



//-----------------------------------------------------------------
/*
// Browsers Supported:

//  * <B style="COLOR: black; BACKGROUND-COLOR: #ffff66">Mozilla</B> 1.0+ and browsers based on that and later versions (Netscape, Galeon)
//  * Internet Explorer 5.0+ with MSXML3.0+ installed (4.0 RTM and later preffered)

//-----------------------------------------------------------------
// What does it do:
// This script allows the following in <B style="COLOR: black; BACKGROUND-COLOR: #ffff66">Mozilla</B>
//  Methods: transformNode(), transformNodeToObject()
//  Properties: xml (read), innerText(read/write), parseError (you should only read) 
//-----------------------------------------------------------------
// NOTES
// 1 Asynchronus loading
//  In IE, the async property specifies whether the script should wait for a
//  domObject.load("somefile.xml");
//  to finish loading before continuing execution of the next code line.
//  <B style="COLOR: black; BACKGROUND-COLOR: #ffff66">Mozilla</B> does sem to have implemented such a property yet, but one 
//  can use the readyState property to see if the file is loaded (the property value will be 4)

//-----------------------------------------------------------------

// Sample use

/ *

// Use the factory class to get the right object for the browser,
// then treat it as it's an IE DOMObject (meaning methods 'transFormNode',
// 'transFormNodeToObject' and the 'xml' property
var xmlDoc = MB_crossBrowserXMLFactory.createDOMDocument("","",null);
xmlDoc.load("pathTo/fileNameOf.xml");
var xslDoc = MB_crossBrowserXMLFactory.createDOMDocument("","",null);
xslDoc.load("pathTo/fileNameOf.xslt");
function transform()
{
		var oOut = MB_crossBrowserXMLFactory.createDOMDocument("","",null);
		xmlDoc.transformNodeToObject(xslDoc, oOut);
		document.getElementById("gridView").innerHTML = oOut.xml;
		document.getElementById("sourceView").value = oOut.xml;
}
* /

//-----------------------------------------------------------------
//browser detection
//-----------------------------------------------------------------
var isIE_XML  = (navigator.userAgent.toLowerCase().indexOf("msie") &gt; -1)?true:false;
var isMoz_XML = (document.implementation &amp;&amp; document.implementation.createDocument)?true:false;
//=================================================================
// IE Initialization
//=================================================================
//Possible prefixes ActiveX strings for DOM DOcument
var PROGID_LIST = ["Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XmlDom"];
//When the proper prefix is found, store it here
var PROGID = "";
var nsCounter = 0;

// <B style="COLOR: black; BACKGROUND-COLOR: #ffff66">Mozilla</B> Initialization
if(isMoz_XML)
{

	// Emulate IE's innerText (write)
	Element.prototype.__defineSetter__("innerText", function (sText)
	{
		var s = "" + sText;
		this.innerHTML = s.replace(/\&amp;/g, "&amp;amp;").replace(/&lt;/g, "&amp;lt;").replace(/&gt;/g, "&amp;gt;");
	});
	// Emulate IE's innerText (read)
	Element.prototype.__defineGetter__("innerText", function ()
	{
		function scrapTextNodes(oElem)
		{
			var s = "";
			for(i=0;i&lt;oElem.childNodes.length;i++)
			{
				var oNode = oElem.childNodes[i];
				if(oNode.nodeType == 3)
					s += oNode.nodeValue;
				else if(oNode.nodeType == 1)
					s += "\n" + scrapTextNodes(oNode);
			}
			return s;
		};
		return scrapTextNodes(this);
	});	
	// Emulate the async property (doesn't really do anything...)
	Document.prototype.async = true;
	// Emulate the readystate property
	Document.prototype.readyState = "0";
	// Emulate the onreadystatechange event
	Document.prototype.onreadystatechange = null;
	// The parseError attribute
	Document.prototype.parseError = 0;
	// We save a reference to the original load() method
	Document.prototype.__load__ = Document.prototype.load;
	// Get a document object
	MB_crossBrowserXMLFactory.createDOMDocument  = function(strNamespaceURI, strRootTagName)
	{
		var oDOMDoc = null;
		//create the DOM Document the standards way
		oDOMDoc = document.implementation.createDocument(strNamespaceURI, strRootTagName, null);
		//add the event listener for the load event
		oDOMDoc.addEventListener("load", _Document_onload, false);
		//return the object
		return oDOMDoc;
	};
	// Emulate IE's loadXML() method
	Document.prototype.loadXML = function(strXML) 
	{
		//change the readystate
		changeReadyState(this, 1);
		// parse the string to a new doc
		var oDOMDoc = (new DOMParser()).parseFromString(strXML, "text/xml");
		// remove exising children from the document object
		while (this.hasChildNodes())
			this.removeChild(this.lastChild);
		// insert and import all new
		for (var i = 0; i &lt; oDOMDoc.childNodes.length; i++)
			this.appendChild(this.importNode(oDOMDoc.childNodes[i], true));
		// immitate an onLoad event fire
		handleOnLoad(this);
	};
	//=============================================================
	// XSLT Section: transformNodeToObject, transformNode
	//=============================================================
	// Emulate IE's transformNodeToObject() method
	Document.prototype.transformNodeToObject = function(xslDoc, oResult)
	{
		var xsltProcessor = new XSLTProcessor();
		try
		{
			xsltProcessor.transformDocument(this, xslDoc, oResult, null);
		}
		catch(e)
		{
			throw(e);
		}
	};
	// Emulate IE's transformNode() method
	Document.prototype.transformNode = function(xslDoc)
	{
		var out = document.implementation.createDocument("", "", null);
		this.transformNodeToObject(xslDoc, out);
		var serializer = new XMLSerializer();
		try
		{
			var str = serializer.serializeToString(out);
		}
		catch(e)
		{
			throw(e);
		}
		return str;
	};	
	//=============================================================
	// XPath Section: selectNodes, <B style="COLOR: black; BACKGROUND-COLOR: #a0ffff">selectSingleNode</B>
	//=============================================================
	/ *
	Document.prototype.setProperty = function(s1, s2){}
	
	// Emulate IE's selectNodes() method
	Document.prototype.selectNodes = function(sExp)
	{
		
	}
	
	// Emulate IE's <B style="COLOR: black; BACKGROUND-COLOR: #a0ffff">selectSingleNode</B>() method
	Document.prototype.<B style="COLOR: black; BACKGROUND-COLOR: #a0ffff">selectSingleNode</B> = function(sExp)
	{
		
	}
	* /
	// Emulate IE's xml property
	Document.prototype.__defineGetter__("xml", function ()
	{
		return (new XMLSerializer()).serializeToString(this);
	});

	// Extend the load() method
	Document.prototype.load = function(strURL)
	{
		//set the parseError to 0
		this.parseError = 0;
		//change the readyState
		changeReadyState(this, 1);
		//watch for errors
		try
		{
			//call the original load method
			this.__load__(strURL);
		}
		catch (objException)
		{
			//set the parseError attribute
			this.parseError = -9999999;
			//change the readystate
			changeReadyState(this, 4);
		}
	};
}
else if(isIE_XML)
{

	//define found flag
	var bFound = false;
	// pick up the most recent ActiveX ProgID
	for (var i=0; i &lt; PROGID_LIST.length &amp;&amp; !bFound; i++)
	{
		try 
		{
			var objXML = new ActiveXObject(PROGID_LIST[i]);
			//if we got here it works so save the ProgID
			PROGID = PROGID_LIST[i];
			bFound = true;
		}
		catch(objException){}
	}
	// Throw error if a proper ActiveX object is not available
	if(!bFound)
		throw "No DOM Document found on your computer. Please upgrade your browser.\n" +
		      "Browsers Supported: \n" +
		      " * <B style="COLOR: black; BACKGROUND-COLOR: #ffff66">Mozilla</B> 1.0+ and browsers based on that (Netscape, Galeon)\n" +
		      " * Internet Explorer 5.0+ with MSXML3.0+ installed (4.0 recommended)";

	MB_crossBrowserXMLFactory.createDOMDocument  = function(strNamespaceURI, strRootTagName)
	{
		//variable for the created DOM Document
		var oDOMDoc = null;
		//create the DOM Document the IE way
		oDOMDoc = new ActiveXObject(PROGID);
		// async is false by default
		oDOMDoc.async = false;
		//if there is a root tag name, we need to preload the DOM
		if (strRootTagName)
		{
			//If there is both a namespace and root tag name, then
			//create an artifical namespace reference and load the XML.
			if (strNamespaceURI)
				oDOMDoc.loadXML("&lt;a"+(++nsCounter)+":" + strRootTagName + " xmlns:a"+nsCounter+"=\"" + strNamespaceURI + "\" /&gt;");
			else
				oDOMDoc.loadXML("&lt;" + strRootTagName + "/&gt;");
		}
		//return the object
		return oDOMDoc;
	};
}

//-----------------------------------------------------------------
// Our factory; used to create a DOMDocument object through
// it's createDOMDocument() method
//-----------------------------------------------------------------
function MB_crossBrowserXMLFactory()
{
}


// Moz Utility functions
function handleOnLoad(oDOMDoc)
{
	//check for a parsing error
	if(!oDOMDoc.documentElement || oDOMDoc.documentElement.nodeName == "parsererror")
		oDOMDoc.parseError = -9999999;
	//change the readyState
	changeReadyState(oDOMDoc, 4);
}

function _Document_onload()
{
	//handle the onload event
	handleOnLoad(this);
}

function changeReadyState(oDOMDoc, iRS)
{
	//change the readyState
	oDOMDoc.readyState = iRS;
	//if there is an onreadystatechange event handler, run it
	if((oDOMDoc.onreadystatechange != null) &amp;&amp; (typeof oDOMDoc.onreadystatechange == "function"))
		oDOMDoc.onreadystatechange();
*/
//-------------------------------------------------------------------------------------------------

/* Fehlercodes von xmlhttp:
#define DE_E_INVALID_URL               0x800C0002
#define DE_E_NO_SESSION                0x800C0003
#define DE_E_CANNOT_CONNECT            0x800C0004
#define DE_E_RESOURCE_NOT_FOUND        0x800C0005
#define DE_E_OBJECT_NOT_FOUND          0x800C0006
#define DE_E_DATA_NOT_AVAILABLE        0x800C0007
#define DE_E_DOWNLOAD_FAILURE          0x800C0008
#define DE_E_AUTHENTICATION_REQUIRED   0x800C0009
#define DE_E_NO_VALID_MEDIA            0x800C000A
#define DE_E_CONNECTION_TIMEOUT        0x800C000B
#define DE_E_INVALID_REQUEST           0x800C000C
#define DE_E_UNKNOWN_PROTOCOL          0x800C000D
#define DE_E_SECURITY_PROBLEM          0x800C000E
#define DE_E_CANNOT_LOAD_DATA          0x800C000F
#define DE_E_CANNOT_INSTANTIATE_OBJECT 0x800C0010
#define DE_E_REDIRECT_FAILED           0x800C0014
#define DE_E_REDIRECT_TO_DIR           0x800C0015
#define DE_E_CANNOT_LOCK_REQUEST       0x800C0016
*/
