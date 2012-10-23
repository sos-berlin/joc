/********************************************************* begin of preamble
**
** Copyright (C) 2003-2011 Software- und Organisations-Service GmbH. 
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

//-----------------------------------------------------------------------------------------language  
if( window['_sos_lang'] ) {
  document.writeln('<script type="text/javascript" src="scheduler_lang_'+_sos_lang+'.js"></sc'+'ript>');
}


//----------------------------------------------------------------------------------cookie_handling
if( !window['_server_settings'] && window['_disable_cookie_settings'] ) {
  _server_settings = true; 
}

//----------------------------------------------------------------------------------------Scheduler
// public

function Scheduler()
{
    this._base_url                                       = ( document.location.href + "" ).replace( /\/[^\/]*$/, "/" );
    this._url                                            = this._base_url;
    this._engine_cpp_url                                 = this._base_url;
		this._port                                           = null;
    if(this._base_url.toLowerCase().indexOf("/jobscheduler/operations_gui/") >= 0) {
			this._url                                        	 = this._base_url;
			this._engine_cpp_url                             	 = this._base_url.replace(/\/jobscheduler\/operations_gui\//, "/jobscheduler/engine-cpp/");
			this._port                                         = window.location.port;
		}
    this._dependend_windows                              = new Object();
    this._logger                                         = ( typeof window['SOS_Logger'] == 'function' ) ? SOS_Logger : null;
    this._debug_timer                                    = new Object();
    this._ftimer                                         = new Object();
    this._lang_file_exists                               = ( typeof window['_lang_file_exists'] == 'boolean' ) ? _lang_file_exists : false;
    this._tree_view_enabled                              = false; //2.1.0.6101  (2010-04-06 11:53:57)
    this._activeRequestCount                             = 0;
    this._ie                                             = navigator.appVersion.match(/\bMSIE\b/);
    this._supported_tree_views                           = {'jobs':'Jobs','job_chains':'Job Chains','orders':'Orders','schedules':'Schedules'}; 
    this._view                                           = {'jobs'             :'list',
                                                            'job_chains'       :'list',
                                                            'orders'           :'list',
                                                            'schedules'        :'list',
                                                            'process_classes'  :'list',
                                                            'locks'            :'list',
                                                            'cluster'          :'list',
                                                            'remote_schedulers':'list',
                                                            'last_activities'  :'list'};
    this._subsystems                                     = {'jobs'             :'job',
                                                            'job_chains'       :'order',
                                                            'orders'           :'order',
                                                            //'orders'           :'standing_order',
                                                            'schedules'        :'schedule',
                                                            'process_classes'  :'process_class',
                                                            'locks'            :'lock',
                                                            'cluster'          :'',
                                                            'remote_schedulers':'',
                                                            'last_activities'  :'job order'};
    this._update_counter                                 = 1;
    this._update_finished                                = true;
    this._version_date                                   = '';
    this._version_no                                     = '';
    this._gui_subversion_no                              = '';
    this._gui_release_no                                 = '';
    this._id                                             = '';
    this._host                                           = '';
    this._cookie_prefix                                  = 'SOS_SCHEDULER_GUI_'+window.location.host.replace(/:/,'_')+'_';
    this._update_seconds                                 = 5;
    this._show_card                                      = 'jobs';
    this._update_periodically                            = false;
    
    this._runtime_settings                               = new Object();
    this._runtime_settings.debug_level                   = 0;
    this._runtime_settings.max_orders                    = 10;
    this._runtime_settings.max_task_history              = 10;
    this._runtime_settings.max_order_history             = 50;
    this._runtime_settings.max_last_activities           = 30;
    this._runtime_settings.terminate_timeout             = 60;
    this._runtime_settings.start_at_default_is_now       = true;
    this._runtime_settings.start_next_period_enabled     = false;
    
    this._checkbox_states                                = new Object();
    this._checkbox_states.show_tasks_checkbox            = true;
    this._checkbox_states.show_job_chain_orders_checkbox = false;
    this._checkbox_states.show_job_chain_jobs_checkbox   = false;
    this._checkbox_states.show_order_history_checkbox    = false;
    this._checkbox_states.show_task_error_checkbox       = false;
    this._checkbox_states.show_error_checkbox            = false;
    
    this._radio_states                                   = new Object();
    this._radio_states.last_activities_radios            = 'all';
    
    this._select_states                                  = new Object();
    this._select_states.sort_jobs_select                 = 'name_asc';
    this._select_states.sort_job_chains_select           = 'name_asc';
    this._select_states.filter_jobs_select               = '-1';
    this._select_states.filter_job_chains_select         = '-1';
    this._select_states.show_jobs_select                 = 'all';
    this._select_states.jobs_state_select                = 'all';
    this._select_states.jobs_process_class_select        = 'all';
    
    this._imgFolderOpen                                  = 'explorer_folder_open.gif';
    this._imgFolderClose                                 = 'explorer_folder_closed.gif';
}


//----------------------------------------------------------------------------------Scheduler.close
// public

Scheduler.prototype.close = function()
{
    this._logger           = null;
    
    for( var window_name in this._dependend_windows )  this._dependend_windows[ window_name ].close();
    this._depended_windows = new Object();    
}


//--------------------------------------------------------------------------------Scheduler.loadXML
// public

Scheduler.prototype.loadXML = function( xml )
{
    this.logger(3,'START LOAD XML TO DOM','scheduler_dom');
    var dom_document;
    if(!this._ie && window.DOMParser)
    {   
        var dom_parser = new DOMParser();
        dom_document   = dom_parser.parseFromString( xml, "text/xml" );
        if( dom_document.documentElement.nodeName == "parsererror" )  throw new Error( this.getTranslation( "Error at XML answer:" ) + " " + dom_document.documentElement.firstChild.nodeValue );
    }
    else
    {   
        dom_document   = new ActiveXObject( "MSXML2.DOMDocument" );
        dom_document.validateOnParse = false;
        if( !dom_document.loadXML( xml ) )  throw new Error( this.getTranslation( "Error at XML answer:" ) + " " + dom_document.parseError.reason );
    }
    this.logger(3,'ELAPSED TIME FOR LOAD XML TO DOM','scheduler_dom');
    return dom_document;
}


//------------------------------------------------------------------------------Scheduler.executeGet

Scheduler.prototype.executeGet = function( href, txt_mode, callback_on_success )
{         
    if( typeof txt_mode == "undefined" ) txt_mode = true;
    var async     = ( typeof callback_on_success == "function"  );
    var scheduler = this;
    var ret       = null;
    var err       = null;
	  if(href.search(/^https?:/) == -1) href = scheduler._url+href;
    
    new Ajax.Request( href,
    {
       asynchronous   : async,
       method         : 'get',
       onSuccess      : function(transport) {
                          if( txt_mode ) {
                            if( async ) callback_on_success( transport.responseText );
                            else ret = transport.responseText;
                          } else {
                            if( transport.responseXML.parseError ) {
                              if( transport.responseXML.parseError.errorCode != 0 ) {
                                throw new Error( scheduler.getTranslation( "Error at HTTP answer '$url':", {'url':href} ) + " " + transport.responseXML.parseError.reason );
                              }
                            } else {
                              if( transport.responseXML.documentElement.nodeName == "parsererror" ) {
                                throw new Error( scheduler.getTranslation( "Error at HTTP answer '$url':", {'url':href} ) + " " + transport.responseXML.documentElement.firstChild.nodeValue );
                              }
                            }
                            if( async ) callback_on_success( transport.responseXML );
                            else ret = transport.responseXML;
                          }
                        },
       onFailure:       function(transport) {
                          throw new Error( getTranslation( "Error at HTTP answer '$url':", {'url':href} ) + " " + transport.statusText + " (" + transport.status + ")" );
                        },
       onException:     function(requester, x) {
                          if( async ) {
                            if( parent.top_frame && typeof parent.top_frame.showError == 'function' ) {
                              parent.top_frame.showError(x);
                            } 
                            else if(typeof window['show_error'] == 'function') {
                              show_error(x);
                            }
                            else alert(x.message);
                          } else {
                            err = x;
                          } 
                        }  
    });
    if( err ) throw err;
    return ret;
}


//------------------------------------------------------------------------------Scheduler.executePost

Scheduler.prototype.executePost = function( xml, callback_on_success, async, with_modify_datetime, with_add_path, with_all_errors, params )
{         
    var rand = Math.random();
    this.logger(3,'START SCHEDULER REQUEST: ' + xml,rand);
    window.status = this.getTranslation("Waiting for response from JobScheduler ...");
    if( typeof with_add_path        != "boolean"  ) { with_add_path        = true;  }
    if( typeof with_modify_datetime != "boolean"  ) { with_modify_datetime = true;  }
    if( typeof with_all_errors      != "boolean"  ) { with_all_errors      = false; }
    if( typeof async                != "boolean"  ) { async                = true;  }
    var scheduler      = this;
    var ret            = null;
    var err            = null;
    var this_scheduler = false;
    if( xml.search( /this_scheduler=(\'|\")yes(\'|\")/i ) > -1 ) {
      this_scheduler   = true;
      xml              = xml.replace( /this_scheduler=(\'|\")yes(\'|\")/i, "" );
    }
    if( xml.search( /^<modify_spooler cmd=(\'|\")[^\'\"]*(abort|terminate)[^\'\"]*(\'|\")\/>/i ) > -1 
      || (xml.search( /^<terminate/i ) > -1 && ( xml.search( /cluster_member_id=/i ) == -1 || this_scheduler ))) { 
    	
    	err = new Error();
    	parent.left_frame.clear_update();
    }
    
    new Ajax.Request( scheduler._engine_cpp_url,
    {
       asynchronous   : async,
       method         : 'post',
       postBody       : xml,
       contentType    : 'text/xml',
       requestHeaders : { 'Cache-Control':'no-cache', 
                          'Pragma':'no-cache', 
                          //'Transfer-Encoding':'identity', 
                          'Content-Length': xml.length 
                        },
       onCreate       : function() { scheduler._activeRequestCount++; },
       onComplete     : function(transport) { 
                          //scheduler.logger(6,'ACTIVE CONNECTIONS:' + scheduler._activeRequestCount);
                          scheduler._activeRequestCount--; 
                          if(scheduler._activeRequestCount == 0) window.status = ''; 
                          scheduler.logger(3,'HTTP REQUEST STATUS onComplete '+transport.status);
                        },
       onSuccess      : function(transport) { 
       	                  //alert(document.documentMode);
       	                  //alert(transport.getAllResponseHeaders());
                          if( !transport.responseText ) err = new Error();
                          if( err ) throw err;
                          var with_reset = (!scheduler._update_finished);
                          scheduler._update_counter  = 1;
                          scheduler._update_finished = true;
                          scheduler.logger(6,'SCHEDULER RESPONSE:\n' + transport.responseXML.xml);  
                          if( xml == '<check_folders/>' ) {
                            ret = true;
                            return null;
                          } 
                          if( transport.responseXML.parseError ) {
                             if( transport.responseXML.parseError.errorCode != 0 ) {
                               throw new Error( scheduler.getTranslation( "Error at XML answer:" ) + " " + transport.responseXML.parseError.reason );
                             }
                          } else {
                             if( transport.responseXML.documentElement.nodeName == "parsererror" ) {
                               throw new Error( scheduler.getTranslation( "Error at XML answer:" ) + " " + transport.responseXML.documentElement.firstChild.nodeValue );
                             }
                          }
                          var error_element = transport.responseXML.selectSingleNode( "spooler/answer/ERROR" );
                          if( error_element )
                          {
                              //Per default no exception is thrown if remove job, job_chain, order is clicked
                              if( with_all_errors || error_element.getAttribute( "code" ).search(/SCHEDULER-(108|161|162)/i) == -1 ) {
                                  throw new Error( error_element.getAttribute( "text" ) );
                              } 
                          }
                          scheduler.logger(3,'ELAPSED TIME FOR SCHEDULER REQUEST',rand);
                          if( with_modify_datetime ) scheduler.modifyDatetimeForXSLT( transport.responseXML );  
                          if( with_add_path && !scheduler.versionIsNewerThan( "2007-10-08 14:00:00" ) ) scheduler.addPathAttribute( transport.responseXML );
                          if( with_reset && parent.top_frame && typeof parent.top_frame.resetError == 'function' ) {
                            parent.top_frame.resetError();
                          }
                          if( typeof callback_on_success == 'function' ) callback_on_success( transport.responseXML, params );
                          if( !async ) ret = transport.responseXML;
                        },
       onFailure:       function(transport) {
                          if( transport.status >= 12000 ) err = new Error();
                          scheduler.logger(3,'HTTP REQUEST STATUS onFailure '+transport.status);
                          if( err ) throw err;
                          throw new Error( scheduler.getTranslation( "Error at XML answer:" ) + " " + transport.statusText + " (" + transport.status + ")" );
                        },
       onException:     function(requester, x) {
                          scheduler.logger(3,'ELAPSED TIME FOR SCHEDULER REQUEST',rand);
                          if( err || !scheduler._update_finished ) {
                          	var message = '';
                          	var update_counter_txt = ['First','Second','Third','Fourth','Last'];
                            if( scheduler._update_counter == 6 ) {
                               message = scheduler.getTranslation("No connection to JobScheduler") 
                               scheduler._update_finished = true;
                            } else {
                               message = scheduler.getTranslation("$trial trial (of 5) to (re)connect to JobScheduler",{trial:scheduler.getTranslation(update_counter_txt[scheduler._update_counter-1])});
                               scheduler._update_finished = false;
                            }
                            x = new Error( message );
                          }
                          if( async ) {
                            if( parent.top_frame && typeof parent.top_frame.showError == 'function' ) {
                              parent.top_frame.showError(x);
                            } 
                            else if(typeof window['show_error'] == 'function') {
                              show_error(x);
                            }
                            else alert(x.message);
                          } else {
                            err = x;
                          }
                        }  
    }); 
    if( err ) throw err;
    return ret;
}


Scheduler.prototype.executeSynchron = function( xml, with_modify_datetime, with_add_path, with_all_errors )
{
  return this.executePost( xml, null, false, with_modify_datetime, with_add_path, with_all_errors );
}

Scheduler.prototype.executeAsynchron = function( xml, callback_on_success, with_modify_datetime, with_add_path, with_all_errors, params )
{  
  return this.executePost( xml, callback_on_success, true, with_modify_datetime, with_add_path, with_all_errors, params );
}


//-------------------------------------------------------Scheduler.addDatetimeAttributesForXSLT

Scheduler.prototype.addDatetimeAttributesForXSLT = function( response, now, attribute_name )
{   
    var elements = response.selectNodes( "//*[@" + attribute_name + "]" );
    for( var i = 0; i < elements.length; i++ )
    {
        var element = elements[ i ];
        if( element.tagName == 'order_queue') continue;
        var value   = element.getAttribute( attribute_name );
        if( value )
        {   
            if( value == "never" || value == "now" ) {
              value = this.getTranslation(value);
              element.setAttribute( attribute_name, value );
            }
            switch (attribute_name) {
              case "valid_from"            : 
              case "valid_to"              : 
              case "spooler_running_since" :
              case "time"                  :
              case "end_time"              : element.setAttribute( attribute_name + "__xslt_datetime"               , this.xsltFormatDatetime( value ) );
                                             break;
              case "start_time"            : element.setAttribute( attribute_name + "__xslt_datetime"               , this.xsltFormatDatetime( value ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             break;
              case "start_at"              :
              case "idle_since"            : element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             break;
              case "in_process_since"      : element.setAttribute( attribute_name + "__xslt_datetime_diff"          , this.xsltFormatDatetimeDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             break;
              case "running_since"         : element.setAttribute( attribute_name + "__xslt_datetime_diff"          , this.xsltFormatDatetimeDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_date_or_time_with_diff" , this.xsltFormatDateOrTimeWithDiff( value, now ) );
                                             break;
              case "next_start_time"       : element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_date_or_time_with_diff" , this.xsltFormatDateOrTimeWithDiff( value, now ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff_plus", this.xsltFormatDatetimeWithDiff( value, now, true ) );
                                             break;
              case "enqueued"              : element.setAttribute( attribute_name + "__xslt_date_or_time"           , this.xsltFormatDateOrTime ( value, now ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             break;
              case "setback"               : element.setAttribute( attribute_name + "__xslt_date_or_time_with_diff" , this.xsltFormatDateOrTimeWithDiff( value, now ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             break;
              case "connected_at"          :
              case "disconnected_at"       : 
              case "last_write_time"       : element.setAttribute( attribute_name + "__xslt_datetime_zone_support"  , this.xsltFormatDatetimeZoneSupport( value ) );
                                             break;
              default                      : element.setAttribute( attribute_name + "__xslt_datetime"               , this.xsltFormatDatetime( value ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_diff"          , this.xsltFormatDatetimeDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff"     , this.xsltFormatDatetimeWithDiff( value, now, false ) );
                                             element.setAttribute( attribute_name + "__xslt_datetime_with_diff_plus", this.xsltFormatDatetimeWithDiff( value, now, true ) );
                                             element.setAttribute( attribute_name + "__xslt_date_or_time"           , this.xsltFormatDateOrTime ( value, now ) );
                                             element.setAttribute( attribute_name + "__xslt_date_or_time_with_diff" , this.xsltFormatDateOrTimeWithDiff( value, now ) );
            }
        }
    }
}


//---------------------------------------------------------------Scheduler.addPathAttribute

Scheduler.prototype.addPathAttribute = function( response )
{   
    this.logger(3,'ADDING PATH ATTRIBUTE:','scheduler_addpath');
    var elements = response.selectNodes( "//job|//job_chain|//process_class|//lock|//order" );
    
    for( var i = 0; i < elements.length; i++ )
    { 
      if( typeof elements[i].getAttribute('path') == 'string' ) continue;
      switch(elements[i].tagName) {
        case 'order'         : var path = elements[i].getAttribute('order'); break;
        case 'job'           : var path = elements[i].getAttribute('job') ? elements[i].getAttribute('job') : elements[i].getAttribute('name'); break;
        case 'process_class' : 
        case 'lock'          : 
        case 'job_chain'     : var path = elements[i].getAttribute('name'); break;
      }
      if( !path ) path = '';
      elements[i].setAttribute('path', path );
    }
    this.logger(3,'ELAPSED TIME FOR ADDING PATH ATTRIBUTE','scheduler_addpath');
}

//---------------------------------------------------------------Scheduler.modifyDatetimeForXSLT

Scheduler.prototype.modifyDatetimeForXSLT = function( response )
{   
    this.logger(3,'MODIFY DATETIME ATTRIBUTES:','scheduler_datetime');
    // Fuer Firefox, dass kein Skript im Stylesheet zulaesst.
    var now;

    var datetime = response.selectSingleNode( "/spooler/answer/@time" );
    if( datetime )  now = this.dateFromDatetime( datetime.nodeValue );
    
    //task attributes
    this.addDatetimeAttributesForXSLT( response, now, "running_since"         );
    this.addDatetimeAttributesForXSLT( response, now, "in_process_since"      );
    this.addDatetimeAttributesForXSLT( response, now, "idle_since"            );
    //task_queue and task attributes
    this.addDatetimeAttributesForXSLT( response, now, "start_at"              );
    this.addDatetimeAttributesForXSLT( response, now, "enqueued"              );
    //order_queue, order and job attributes
    this.addDatetimeAttributesForXSLT( response, now, "next_start_time"       );
    //order attributes
    this.addDatetimeAttributesForXSLT( response, now, "setback"               );
    //remote_scheduler attributes
    this.addDatetimeAttributesForXSLT( response, now, "connected_at"          );
    this.addDatetimeAttributesForXSLT( response, now, "disconnected_at"       );
    //schedule attributes
    this.addDatetimeAttributesForXSLT( response, now, "start_time"            );
    this.addDatetimeAttributesForXSLT( response, now, "end_time"              );
    this.addDatetimeAttributesForXSLT( response, now, "valid_from"            );
    this.addDatetimeAttributesForXSLT( response, now, "valid_to"              );
    //file_based attribute
    this.addDatetimeAttributesForXSLT( response, now, "last_write_time"       );
    
    this.logger(3,'ELAPSED TIME FOR MODIFY DATETIME ATTRIBUTES','scheduler_datetime');
}

//---------------------------------------------------------------------xsltFormatDatetime

Scheduler.prototype.xsltFormatDatetime = function( datetime )
{
    return ( !datetime ) ? "" : datetime.replace( /\.\d*Z*$/, "" );
}

//-----------------------------------------------------------------xsltFormatDateOrTime

Scheduler.prototype.xsltFormatDateOrTime = function( datetime )
{
    if( !datetime )  return "";

    var now = new Date();

    if(    1*datetime.substr( 0, 4 ) == now.getFullYear()
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

//---------------------------------------------------------------xsltFormatDatetimeWithDiff

Scheduler.prototype.xsltFormatDatetimeWithDiff = function( datetime, now, show_plus )
{
    var date   = this.dateFromDatetime( datetime );
    var result = this.xsltFormatDatetime( datetime );
    if( result && now && date )  result += "\xA0(" + this.xsltFormatDatetimeDiff( date, now, show_plus ) + ")";

    return result;
}

//---------------------------------------------------------------xsltFormatDateOrTimeWithDiff

Scheduler.prototype.xsltFormatDateOrTimeWithDiff = function( datetime, now )
{
    var date   = this.dateFromDatetime( datetime );
    var result = this.xsltFormatDateOrTime( datetime );
    if( result && now && date )  result += "\xA0(" + this.xsltFormatDatetimeDiff( date, now ) + ")";

    return result;
}

//----------------------------------------------------------------xsltFormatDatetimeDiff

Scheduler.prototype.xsltFormatDatetimeDiff = function( datetime_earlier, datetime_later, show_plus )
{
    var show_ms;
    if( show_ms   == undefined )  show_ms   = false;
    if( show_plus == undefined )  show_plus = false;

    var date_later   = typeof datetime_later   == "string"? this.dateFromDatetime( datetime_later )   : datetime_later;
    var date_earlier = typeof datetime_earlier == "string"? this.dateFromDatetime( datetime_earlier ) : datetime_earlier;

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
    if( abs <    60*60 ) { 
        var minutes = Math.floor( abs / 60 );
        if( minutes < 60 ) {
          var seconds = Math.floor(abs - 60*minutes);
          if( seconds < 10 ) seconds = "0" + seconds; 
          result = minutes + ":" + seconds + "min";
        } else {
          result = minutes + "min";
        }
    }
    else
    if( abs < 24*60*60 ) {
        result = Math.floor( abs / (    60*60 ) ) + "h";
    }
    else {
        result = Math.floor( abs / ( 24*60*60 ) ) + this.getTranslation("days");
    }
    return diff < 0             ? "-" + result :
           show_plus && diff > 0? "+" + result
                                : result;
}

//-----------------------------------------------------------------------dateFromDatetime
// datetime == yyyy-mm-dd hh-mm-ss[.mmm]

Scheduler.prototype.dateFromDatetime = function( datetime )
{
    if( !datetime )  return null;
    if( datetime == this.getTranslation("never") ) return null;
    if( datetime == this.getTranslation("now") ) return null;

    var date = new Date( 1*datetime.substr( 0, 4 ),
                         1*datetime.substr( 5, 2 ) - 1,
                         1*datetime.substr( 8, 2 ),
                         1*datetime.substr( 11, 2 ),
                         1*datetime.substr( 14, 2 ),
                         1*datetime.substr( 17, 2 ),
                         datetime.length < 23? 0 : 1*datetime.substr( 20, 3 ) );

    return date;
}


//-----------------------------------------------------------------------xsltFormatDatetimeZoneSupport
// datetime == yyyy-mm-ddThh-mm-ss[.mmm]Z

Scheduler.prototype.xsltFormatDatetimeZoneSupport = function( datetime ) 
{
    if( datetime.lastIndexOf('Z') == -1 ) return this.xsltFormatDatetime( datetime );
    var dateobj = this.dateFromDatetime( datetime );
    if( dateobj ) {
      dateobj.setTime( dateobj.getTime() - ( dateobj.getTimezoneOffset()*60*1000 ) );
      return dateobj.getFullYear()+'-'+this.addLeadingZero(dateobj.getMonth()+1)+'-'+this.addLeadingZero(dateobj.getDate())+' '+this.addLeadingZero(dateobj.getHours())+':'+this.addLeadingZero(dateobj.getMinutes())+':'+this.addLeadingZero(dateobj.getSeconds());
    }
    return this.xsltFormatDatetime( datetime );
}


Scheduler.prototype.addLeadingZero = function( num ) 
{
    return ( num < 10 ) ? '0'+num : num;
}


//----------------------------------------------------------------------------------------logger
Scheduler.prototype.logger = function(level, msg, timerKey)
{   
    try{
      if( !this._logger || !this._runtime_settings.debug_level ) return false;
      if( typeof timerKey == 'undefined' ) timerKey = '';
      
      this._logger.debugLevel = this._runtime_settings.debug_level;
      this._logger.styleDebug = 'font-family:\'Courier New\';font-size:10pt;color: #009933;white-space:nowrap;';
      if( timerKey != '' && this._debug_timer[timerKey] ) {
        this._logger.debug( level, msg + ': ' + ((new Date().getTime() - this._debug_timer[timerKey])/1000) + 's' );
        this._debug_timer[timerKey] = undefined;
      } else if( timerKey != '' && !this._debug_timer[timerKey] ) {
        this._debug_timer[timerKey] = new Date().getTime();
        this._logger.debug( level, msg );
      } else {
        this._logger.debug( level, msg ); 
      }
      if(this._logger.hasError) throw new Error(this._logger.getError());
      return true;
    } catch(x) {
      //this._logger.closeDebugWindow();
      //this._logger = null;
      return false;
    }
}


//------------------------------------------------------------------------readGuiVersion
Scheduler.prototype.readGuiVersion = function()
{
    try {
      var responseText = this.executeGet( '.version' );
      var version = ["0000 0000-00-00","0.0.0.0000"];
      if(responseText) {
        version = responseText.split("\n");
      }
      this._gui_release_no = version[1].replace(/^\s+/,'').replace(/\s+$/,'');
      this._gui_subversion_no = version[0].replace(/[^-0-9:\. ]/g,'');
      var pattern = /([0-9\.]+)\s+(\d{4}-\d{2}-\d{2})*/;
      pattern.exec(this._gui_subversion_no);
      this._gui_subversion_no = RegExp.$1 + ' (' + RegExp.$2 + ')';
    }
    catch(x) {
    }
}    


//------------------------------------------------------------------------------setState
Scheduler.prototype.setState = function( state )
{
    if( state ) {
        var version           = state.getAttribute( "version" ).split("  ");
        this._version_no      = version[0];
        if(version.length > 1) {
          this._version_date  = version[1].replace( /\D*$/, "" ).replace( /^\D*/, "" ); //trim
        }
        this._host            = state.getAttribute('host');
        if( this._port == null ) {
        	this._port          = state.getAttribute('tcp_port');
        } 
        this._id              = state.getAttribute('id');
        this._cookie_prefix   = 'SOS_SCHEDULER_GUI_'+this._host+'_'+this._port+'_';
    }
}


//------------------------------------------------------------------------------setTimeout
Scheduler.prototype.setTimeout = function( js, delay, debug )
{   
    if( typeof debug == 'undefined' ) debug = 4;
    if( this._runtime_settings.debug_level >= debug ) {
      this.logger(debug,'DELAYED EXEC ('+(delay/1000)+'s): ' + js + ' in frameset' );
    } 
    return window.setTimeout( js, delay );
}


//------------------------------------------------------------------------------versionIsNewerThan
Scheduler.prototype.versionIsNewerThan = function( version_date )
{
    //version_date must be in ISO-format
    try {
        if(this._version_date) {
          var pattern = /(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
          pattern.exec(this._version_date);
          var timestamp = Date.UTC(parseInt(RegExp.$1,10),(parseInt(RegExp.$2,10)-1),parseInt(RegExp.$3,10),parseInt(RegExp.$4,10),parseInt(RegExp.$5,10),parseInt(RegExp.$6,10)); 
          pattern.exec(version_date);
          timestamp -= Date.UTC(parseInt(RegExp.$1,10),(parseInt(RegExp.$2,10)-1),parseInt(RegExp.$3,10),parseInt(RegExp.$4,10),parseInt(RegExp.$5,10),parseInt(RegExp.$6,10)); 
          return ( timestamp >= 0 );
        } else return true;
    }
    catch(x) {
        return false;  
    }   
}


//---------------------------------------------------------------------------------------loadXSLT
Scheduler.prototype.loadXSLT = function( url )
{
    if( window.XSLTProcessor ) {
      this._xslt = new XSLTProcessor();
      this._xslt.importStylesheet( this.executeGet( url, false ) );       
    } else {
      this._xslt = this.executeGet( url, false );
    }
}


//------------------------------------------------------------------------------------xmlTransform
Scheduler.prototype.xmlTransform = function( dom_document, with_translate, text_output )
{   
    this.logger(3,'START TRANSFORM RESPONSE frameset','transform_response');
    try{
    	var txtNode = dom_document.createTextNode("\n"); 
    	dom_document.documentElement.insertBefore(txtNode, dom_document.documentElement.firstChild);
    	dom_document.documentElement.appendChild(txtNode.cloneNode(true));
    }
    catch(x) {}
    var result_dom = null;
    if( typeof with_translate != 'boolean' ) with_translate = true;
    if( typeof text_output    != 'boolean' ) text_output    = true;
    
    if( !this._ie ) {
    	result_dom = this._xslt.transformToDocument( dom_document );
    } else {
      if( !with_translate && text_output ) {
        var transformed = dom_document.transformNode( this._xslt );
        this.logger(3,'ELAPSED TIME FOR TRANSFORM RESPONSE frameset','transform_response');
        return transformed;
      } else {
        result_dom = new ActiveXObject( "MSXML2.DOMDocument" );
        dom_document.transformNodeToObject( this._xslt, result_dom );
      }
    }
      
    if( with_translate && this._lang_file_exists )
    {
      var spans = result_dom.selectNodes("//label|//option|//span[@class='translate' or @class='label' or @class='red_label' or @class='green_label' or @class='caption' or @class='job_error' or @class='file_based_error']|//a[@class='translate']");
      for( var i=0; i<spans.length; i++ ) {
        if( !spans[i].firstChild || !spans[i].firstChild.nodeValue ) continue;
        spans[i].firstChild.nodeValue = this.getTranslation(spans[i].firstChild.nodeValue); 
      }
      var attributes = result_dom.selectNodes("//td[@title]|//tr[@title]|//span[@title]|//div[@title]");
      for( var i=0; i<attributes.length; i++ ) {
        var old_title = attributes[i].getAttribute( "title" );
        if( attributes[i].getAttribute("arg") ) {
          old_title = old_title.replace( new RegExp( attributes[i].getAttribute("arg") ), "$arg");
          old_title = this.getTranslation(old_title,{arg:attributes[i].getAttribute("arg")}); 
        } else {
          old_title = this.getTranslation(old_title);
        }
        attributes[i].setAttribute( "title", old_title ); 
      }
    } 
    this.logger(3,'ELAPSED TIME FOR TRANSFORM RESPONSE frameset','transform_response');
    if( text_output ) return result_dom.xml;
    return result_dom;
}


//------------------------------------------------------------------------------getTranslation
Scheduler.prototype.getTranslation = function( s, args )
{   
    if( typeof window['getTranslation'] == 'function' ) {
      return getTranslation( s, args );
    } else { 
      if( typeof args == 'object' )  {
        for( var entry in args ) {
          s = s.replace(new RegExp('\\$'+entry,'g'),args[entry]);
        }
      }
      return s;
    }
}


//---------------------------------------------------------------------------readCustomSettings
Scheduler.prototype.readCustomSettings = function()
{  
    for( var entry in this._runtime_settings ) {                   
      if( typeof window['_'+entry] != 'undefined' ) {
        this._runtime_settings[entry]                = window['_'+entry];
      }
    }
    if( typeof _checkbox_states == 'object' ) {
      for( var state in _checkbox_states ) {                   
        this._checkbox_states[state]                 = _checkbox_states[state];
      }
    }
    if( typeof _select_states == 'object' ) {                                                      
      for( var state in _select_states ) {                   
        this._select_states[state]                   = _select_states[state];
      }
    }
    if( typeof _radio_states == 'object' ) {                                                        
      for( var state in _radio_states ) {                      
        this._radio_states[state]                    = _radio_states[state];
      }
    }
    if( typeof _update_seconds == 'number' ) {
      this._update_seconds                           = _update_seconds;
    }
    if( typeof _show_card == 'string' ) {
      this._show_card                                = _show_card;
    }
    if( typeof _update_periodically == 'boolean' ) {
      this._update_periodically                      = _update_periodically;
    }
}


//-------------------------------------------------------------------readCustomVersionedSettings
Scheduler.prototype.readCustomVersionedSettings = function()
{  
    if(this._tree_view_enabled && typeof _view == 'object') {
      for( var entry in this._supported_tree_views ) {
        if( _view[entry] ) this._view[entry]         = _view[entry];
      }
    }
}


//---------------------------------------------------------------------------------getDebugLevel
Scheduler.prototype.setDebugLevel = function()
{
    if( typeof window['_debug_level'] != 'undefined' ) {
      this._runtime_settings.debug_level             = _debug_level;
    }
    this._runtime_settings.debug_level               = Math.max(_debug_level,parseInt(this.getCookie( 'debug_level', this._runtime_settings.debug_level),10)); 
}
  
//---------------------------------------------------------------------------------readCookies
Scheduler.prototype.readCookies = function()
{  
    var logged = this.logger(3,'START READING COOKIES frameset','scheduler_read_cookies');
    this._runtime_settings.debug_level               = Math.max(_debug_level,parseInt(this.getCookie( 'debug_level', this._runtime_settings.debug_level),10));
    if(!logged) this.logger(3,'START READING COOKIES frameset','scheduler_read_cookies');
    this._update_periodically                        = (this.getCookie( 'update_periodically', this._update_periodically.toString()) == 'true');
    this._update_seconds                             = parseInt(this.getCookie( 'update_seconds', this._update_seconds),10);
    this._show_card                                  = this.getCookie( 'show_card', this._show_card);
    
    for( var state in this._radio_states ) {                      
        this._radio_states[state]                    = this.getCookie( state, this._radio_states[state]);
    }
    var cookie_entries = ['max_orders','max_last_activities','max_order_history','max_task_history','terminate_timeout'];
    for( var i=0; i < cookie_entries.length; i++ ) {
      this._runtime_settings[cookie_entries[i]]      = parseInt(this.getCookie( cookie_entries[i], this._runtime_settings[cookie_entries[i]]),10);
    }
    this._runtime_settings.start_at_default_is_now   = (this.getCookie( 'start_at_default_is_now', this._runtime_settings.start_at_default_is_now.toString()) == 'true');
    this._runtime_settings.start_next_period_enabled = (this.getCookie( 'start_next_period_enabled', this._runtime_settings.start_next_period_enabled.toString()) == 'true');
    
    for( var state in this._checkbox_states ) {                   
      this._checkbox_states[state]                   = (this.getCookie( state, this._checkbox_states[state].toString()) == 'true');
    }                                                      
    for( var state in this._select_states ) {
      if( state.search(/^filter_(jobs|job_chains|orders)_select$/) > -1 ) continue;
      var value = this.getCookie( 'select_states_'+state );
      if( value != '' ) this._select_states[state]   = value;
    }
    if( this._tree_view_enabled ) {
      for( var entry in this._supported_tree_views ) {
        var value = this.getCookie( 'view_'+entry );
        if( value != '' ) this._view[entry]            = value;
      }
    }                                                      
                                                          
    this.logger(6,'ALL AVAILABLE COOKIES:\n  ' + document.cookie.replace(/;/g,"\n "));
    this.logger(3,'ELAPSED TIME FOR READING COOKIES frameset','scheduler_read_cookies');
}


//------------------------------------------------------------------------------getCookies
Scheduler.prototype.getCookie = function(name, default_value) 
{   
    name = this._cookie_prefix + name;
    this.logger(3,'LOOKING FOR COOKIE: ' + name);
    if( typeof default_value == 'undefined' ) default_value = "";
    var value   = "";
    var pattern = new RegExp(name+"=([^;]*);");
    if (document.cookie.length > 0 && pattern.test(document.cookie+";") ) {
      var result = pattern.exec(document.cookie+";");
      if( result.length > 0 ) {
        if( RegExp.$1 != "" ) value = unescape(RegExp.$1);
        this.logger(3,'COOKIE FOUND: ' + name + '=' + value);
      }
    } else {
      this.logger(2,'COOKIE NOT FOUND (default value is "' + default_value + '")');
    }
    if( value == "" ) value = default_value;
    return value;
}


//--------------------------------------------------------------------------------treeDisplay
Scheduler.prototype.treeDisplay = function( li_element )
{
    var ul          = li_element.down('ul');
    var img_folder  = li_element.down('img');
    if( ul.getAttribute('sos_mode') == 'closed' ) {
      ul.setAttribute('sos_mode','open');
      ul.style.display  = 'block';
      img_folder.src    = this._imgFolderOpen;
      return true;
    }
    else {
      ul.style.display  = 'none';
      ul.setAttribute('sos_mode','closed');
      img_folder.src    = this._imgFolderClose;
      return false;
    }
}
