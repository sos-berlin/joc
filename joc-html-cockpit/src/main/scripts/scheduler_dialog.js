/********************************************************* begin of preamble
**
** Copyright (C) 2003-2014 Software- und Organisations-Service GmbH. 
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

//$Id: scheduler_dialog.js 28482 2014-12-03 16:47:06Z oh $

//-------------------------------------------------------------------------------------private vars
var _open_url_features       = "menubar=no, toolbar=no, location=no, directories=no, scrollbars=yes, resizable=yes, status=no, dependent=yes";
var _popup_menu;
var _obj_title               = "";
var _obj_name                = "";
var _stdout_stderr           = {stdout:null,stderr:null,stdout_header:null,stderr_header:null};

//---------------------------------------------------------------------------------showError
function showError( x, url, line )
{
    var err;
    
    if( typeof x == "object" )
    {
        if( x.number )  x.message = "0x" + x.number.toHex( 8 ) + "  " + x.message;
        if( parent._scheduler._runtime_settings.debug_level > 0 ) {
          if( url )  x.message += " [File: " + url + "]";
          else if( x.fileName ) x.message += " [File: " + x.fileName + "]";
          if( line ) x.message += " [Line: " + line + "]";
          else if( x.lineNumber ) x.message += " [Line: " + x.lineNumber + "]";
        }
        err = x;
    }
    else
    {
        err = new Error();
        err.message = x;
        if( parent._scheduler._runtime_settings.debug_level > 0 ) {
          if( url )  err.message += " [File: " + url + "]";
          if( line ) err.message += " [Line: " + line + "]";
        }
        err.stack = "";
    }
    
    if( parent.error_frame && typeof parent.error_frame.show_error == 'function' ) {
      parent.error_frame.show_error(err);
      parent._scheduler.logger(4, "_update_counter=" + parent._scheduler._update_counter + "; _update_finished=" + (parent._scheduler._update_finished) );
      if( parent._scheduler._update_counter < 6 && !parent._scheduler._update_finished ) {
        parent._scheduler._update_counter++;
        //set_timeout( "callErrorChecked( 'update__onclick', false, false );", 15000 ); 
        callErrorChecked( 'update__onclick', false, false, 15000 );
      }
    } 
    else if(typeof window['show_error'] == 'function') {
      show_error(err);
    } 
    else alert(err.message );
        
    return err;
}


function resetError()
{
    if( parent.error_frame && typeof parent.error_frame.reset_error == 'function' ) {
        parent.error_frame.reset_error();
    }
    else if(typeof window['reset_error'] == 'function') {
        reset_error();
    }
    window.status = "";
}


//---------------------------------------------------------------------------------callErrorChecked
function callErrorChecked( fct, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 )
{
    try
    {   
        if( parent._scheduler._runtime_settings.debug_level > 0 ) {
          var fargs  = new Array();
          for( var i=1; i < callErrorChecked.arguments.length; i++ ) {
            if( typeof callErrorChecked.arguments[i] == 'undefined' ) break;
            fargs.push('"'+callErrorChecked.arguments[i]+'"');
          }
          var fname = fct + '(' + fargs.join(',') + ')';
          parent._scheduler._ftimer[fname] = new Date().getTime(); 
          parent._scheduler.logger(2,'EXEC ' + fname + ' in ' + self.name );
        } 
        var frm = self.name;
        if( fct.search(/^parent\./) > -1 ) {
          var fcts = fct.split('.');
          fcts.shift();
          frm = fcts.shift();
          fct = fcts.join('.');
        }
        if( typeof parent[frm][fct] != "function" ) throw new Error('parent.'+frm+'.'+fct+' is not a function.');
        var ret = parent[frm][fct]( arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 );
        if( parent._scheduler._runtime_settings.debug_level > 0 ) {
          parent._scheduler.logger(2,'ELAPSED TIME FOR EXEC ' + fname + ' in ' + self.name + ': ' + ((new Date().getTime() - parent._scheduler._ftimer[fname])/1000) + 's' );
          parent._scheduler._ftimer[fname] = undefined;
        }
        return ret;
    }
    catch( x )
    {
        showError( x );
        return false;
    }
}


//-------------------------------------------------------------------update__onclick
function update__onclick( with_reset, force, time )
{   
    
    //try {
      if( typeof with_reset != 'boolean' ) with_reset = true;
      if( typeof force      != 'boolean' ) force      = false;
      if( typeof time       != 'number'  ) time       = 200;
      if( with_reset ) resetError(); 
      if( parent.left_frame ) {
      	parent.left_frame.clear_update();
        if( force ) {
         	if( parent._scheduler.executeSynchron( '<check_folders/>', false, false ) ) {
           	set_timeout("parent.left_frame.update()",time);
         	}
        } else {
         	set_timeout("parent.left_frame.update()",time);
        }
      }
    //} catch(E) {
    //  showError(E);
    //}
} 

//-------------------------------------------------------------------Popup_menu_builder.add_command
// Erweiterung von Popup_menu_builder, s. popup_builder.js

function Popup_menu_builder__add_command( html, xml_command, is_active, is_hidden, with_confirm, confirm_msg, removeObj )
{
    if( typeof with_confirm == 'undefined' ) with_confirm = false;
    if( typeof confirm_msg == 'undefined' ) confirm_msg = '';
    if( typeof removeObj   == 'undefined' ) removeObj = '';
    var str_with_confirm = with_confirm ? 'true' : 'false';
    this.add_entry( html, "callErrorChecked( 'popup_menu__execute', &quot;" + xml_command.replace( /\\/g, "\\\\" ) + "&quot;, "+str_with_confirm+", &quot;" + confirm_msg + "&quot;, &quot;" + removeObj.replace( /\\/g, "\\\\" ) + "&quot; )", is_active, is_hidden );
}

//-------------------------------------------------------------------------------popup_menu.execute
// Fuer Popup_menu_builder.add_command()

function popup_menu__execute( xml_command, with_confirm, confirm_msg, removeObj )
{   
    _popup_menu.close();
    if( !with_confirm ||  confirm(parent.getTranslation('Do you really want to '+confirm_msg+'?')) ) {
    	var ret = scheduler_exec( xml_command.replace(/&/g,"&amp;"), false );
      if( ret == 1 ) {
        parent.details_frame._removed_obj = removeObj;
        set_timeout("parent.left_frame.update()",1);
      } 
      else if(ret == 379) {
      	set_timeout("parent.left_frame.update()",2500);
      }
    }
}

//------------------------------------------------------------------Popup_menu_builder.add_show_log
// Erweiterung von Popup_menu_builder, s. popup_builder.js

function Popup_menu_builder__add_show_log( html, show_log_command, window_name, is_active )
{
    var cmd = show_log_command.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
    var w = window_name.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
    this.add_entry( html, "popup_menu__show_log__onclick( &quot;" + cmd + "&quot;, &quot;" + w + "&quot; )", is_active );
}

//--------------------------------------------------------------------popup_menu__show_log__onclick
// Fuer Popup_menu_builder.add_show_log()

function show_log__onclick( show_log_command, window_name )
{
    window_name = "show_log";  // Nur ein Fenster. ie6 will nicht mehrere Logs gleichzeitig lesen, nur nacheinander
    
    var features = _open_url_features;
    features +=  ", width="       + ( window.screen.availWidth - 11 ) +
                 ", innerwidth="  + ( window.screen.availWidth - 11 ) +                             // Fuer Firefox
                 ", height="      + ( Math.floor( window.screen.availHeight * 0.2 ) - 32 ) +
                 ", innerheight=" + ( Math.floor( window.screen.availHeight * 0.2 ) - 32 ) +        // Fuer Firefox
                 ", left=0"       +
                 ", top="         + ( Math.floor( window.screen.availHeight * 0.8 ) );
    //if( show_log_command.search(/^http:/) == -1 ) show_log_command = document.location.href.replace( /\/[^\/]*$/, "/" ) + show_log_command;
    if( show_log_command.search(/^https?:/) == -1 ) show_log_command = parent._scheduler._engine_cpp_url+"show_log?"+show_log_command;
    if( parent._scheduler && parent._scheduler._dependend_windows[ window_name ] ) 
    {
    	try {
    		parent._scheduler._dependend_windows[ window_name ].close();
    		//alert(window_name);
    		//parent._scheduler._dependend_windows[ window_name ].location.reload();
    		//parent._scheduler._dependend_windows[ window_name ].alert(window_name);
    		//parent._scheduler._dependend_windows[ window_name ].focus();
    	}
    	catch(x) {}
    }
    //else { 
    	var log_window = parent.open( show_log_command, window_name, features );
      //var log_window = parent.open( show_log_command, window_name );
    
    	if( log_window )   // null, wenn Popups blockiert sind.
    	{
        log_window.focus();
        //log_window.location.reload();
        if( parent._scheduler )  parent._scheduler._dependend_windows[ window_name ] = log_window;
    	}
    //}
}

function popup_menu__show_log__onclick( show_log_command, window_name )
{
    show_log__onclick( show_log_command, window_name );
    _popup_menu.close();
}


//-----------------------------------------------------------------------------open_window
function open_window( window_name, window_url, with_hash )
{
    try
    { 
      if( parent._scheduler._dependend_windows[ window_name ] ) { 
    	  try {
    		  parent._scheduler._dependend_windows[ window_name ].close();
    	  }
      	catch(xx) {}
      }
      open_url( window_url, window_name, with_hash );
      _popup_menu.close();
    }
    catch( x )
    {
      return showError( x );
    }
}


//-------------------------------------------------------------------------------------show_job_desc
function show_job_desc()
{   
    open_window( "scheduler_job_description", "job_description.html", true );
}


//------------------------------------------------------------------------------------show_xml
function show_xml(obj_name)
{
    //only works for Hot Folder Objects in ./live
    open_window( 'show_xml' + obj_name.replace(/[^a-zA-Z_-]/g,'_'), "scheduler_data/config/live" + encodeURI(obj_name), false );
}

//------------------------------------------------------------------------------------show_xml
function show_xml2(type, path)
{
    var window_name       = 'show_xml' + path.replace(/[^a-zA-Z_]/g,'_');
    open_window( window_name, "show_configuration.html#type=" + type + "&path=" + encodeURIComponent(path)+ "&timezone=" + encodeURIComponent(parent._scheduler._timezone), false );
}


//-----------------------------------------------------------------------------show_job_illustration
// Fuer scheduler_menu__onclick()
function show_job_illustration()
{
    open_window( "show_job_illustration", "job_illustration.html", false );
}


//-----------------------------------------------------------------------------show_job_chain_illustration
// Fuer scheduler_menu__onclick()
function show_job_chain_illustration()
{
    open_window( "show_job_chain_illustration", "jobchain_illustration.html", false );  
}


//-----------------------------------------------------------------------------show_administration
// Fuer scheduler_menu__onclick()
function show_administration(range)
{
    open_window( "scheduler_"+range+"_administration", "scheduler_"+range+".html", true );
}


//-------------------------------------------------------------------------------------------settings
//top_frame
function scheduler_settings__onclick(ret)
{  
  if( parent._server_settings ) {
    _popup_menu.close();
    var with_hash   = (parent.location.hash && parent.location.hash.substr(1));
    var custom_file = (with_hash) ? parent.location.hash.substr(1)+'.js' : 'custom.js';
    var msg         = parent.getTranslation('The settings dialog is not available,\nbecause $file is used as settings file.',{file:custom_file});
    if(!with_hash) {
      msg          += '\n\n'+parent.getTranslation('You can enable the settings dialog \nvia the _disable_cookie_settings flag in the settings file');
    }
    alert(msg);
    return false;
  }
  if( typeof ret != "boolean" ) ret = false;
  if(typeof parent.details_frame.hide == 'function') {
  	parent.details_frame.hide();
  }
    
  if( !ret ) {
    _popup_menu.close();
    Input_dialog.close();
    var dialog                     = new Input_dialog(); 
    dialog._with_reload            = true;
    dialog.width                   = 768;
    //var fieldset_height            = (parent._scheduler._ie > 0 && parent._scheduler._ie < 8) ? 320 : (parent._scheduler._chrome ? 311 : 317);
    var cellspacing                = (parent._scheduler._ie > 0 && parent._scheduler._ie < 8) ? 0   : 2;
    var select_opts;               
    dialog.submit_fct              = "callErrorChecked( 'scheduler_settings__onclick', true )";
    dialog.close_after_submit      = false;
    dialog.add_title( "Settings" );
    dialog.add_prompt( "The following values will be stored in a cookie. If no cookies are available the values which are set in <code>./custom.js</code> are used." );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '<table cellspacing="0" cellpadding="2" width="'+dialog.width+'px" border="0">' );
    dialog._html_array.push( '<tr><td width="50%" valign="top">' );
    dialog._html_array.push( '<fieldset style="margin:2px;width:'+((dialog.width/2)-10)+'px;"><legend>'+parent.getTranslation('Onload Values')+'</legend>' );
    
    dialog._html_array.push( '<fieldset><legend>'+parent.getTranslation('Update')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog.add_checkbox( "update_periodically", parent.getTranslation('periodically every')+' <input type="text" name="update_seconds" style="text-align:right;width:28px;padding:0px 2px;font-size:12px;" value="' + window.parent.onload_settings.update_seconds + '"/> '+parent.getTranslation('seconds'), window.parent.onload_settings.update_periodically );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );
    
    dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('Tabs')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog._html_array.push( '<tr><td style="padding:0px;padding-left:2px;">' );
    dialog._html_array.push( parent.getTranslation('Switch to')+' ' );
    
    select_opts = new Array();
    select_opts.push( {key:'jobs', display:' '+parent.getTranslation('Jobs')+' '} );
    select_opts.push( {key:'job_chains', display:' '+parent.getTranslation('Job Chains')+' '} );
    select_opts.push( {key:'orders', display:' '+parent.getTranslation('Orders')+' '} );
    select_opts.push( {key:'schedules', display:' '+parent.getTranslation('Schedules')+' '} );
    select_opts.push( {key:'process_classes', display:' '+parent.getTranslation('Process Classes')+' '} );
    if(window.parent.onload_settings.display_last_activities_tab) {
    	select_opts.push( {key:'last_activities', display:' '+parent.getTranslation('Last Activities')+' '} );
    }
    
    dialog.add_select( "show_card", select_opts, window.parent.onload_settings.show_card, 1, 0, false );
    dialog._html_array.push( ' '+parent.getTranslation('as the beginning view') );
    dialog._html_array.push( '</td></tr>' );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );
    
    if( parent._scheduler._tree_view_enabled ) {  
      dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('View Mode')+'</legend>' );
      dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
      
      select_opts = new Array();
    	select_opts.push( {key:'tree', display:' '+parent.getTranslation('tree view')+' '} );
      select_opts.push( {key:'list', display:' '+parent.getTranslation('list view')+' '} );
    
      for( var entry in parent._scheduler._supported_tree_views ) {
        dialog._html_array.push( '<tr><td style="padding:0px;padding-left:2px;">' );
        dialog.add_select( "view_"+entry, select_opts, window.parent.onload_settings.view[ entry ], 1, 0, false );
        dialog._html_array.push( ' '+parent.getTranslation('for')+' <i>'+parent.getTranslation(parent._scheduler._supported_tree_views[entry])+' </i>' );
        dialog._html_array.push( '</td></tr>' );
      }
      dialog._html_array.push( '</table>' );
      dialog._html_array.push( '</fieldset>' );  
    }
    
    dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('Selects, Checkboxes and Radios')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    
    select_opts = new Array();
    select_opts.push( {key:'all', display:' '+parent.getTranslation('All jobs')+' '} );
    select_opts.push( {key:'standalone', display:' '+parent.getTranslation('Standalone jobs')+' '} );
    select_opts.push( {key:'order', display:' '+parent.getTranslation('Order jobs')+' '} );
    
    dialog._html_array.push( '<tr><td style="padding:0px;padding-left:2px;">' );
    dialog.add_select( "select_states_show_jobs_select", select_opts, window.parent.onload_settings.select_states[ 'show_jobs_select' ], 1, 0, false );
    
    select_opts = new Array();
    select_opts.push( {key:'all', display:' '+parent.getTranslation('(all)')+' '} );
    select_opts.push( {key:'running', display:' '+parent.getTranslation('running')+' '} );
    select_opts.push( {key:'stopped', display:' '+parent.getTranslation('stopped')+' '} );
    select_opts.push( {key:'running_or_stopped', display:' '+parent.getTranslation('running_or_stopped')+' '} );
    select_opts.push( {key:'other', display:' '+parent.getTranslation('(other)')+' '} );
    
    dialog._html_array.push( ' '+parent.getTranslation('with state')+' ' );
    dialog.add_select( "select_states_jobs_state_select", select_opts, window.parent.onload_settings.select_states[ 'jobs_state_select' ], 1, 0, false );
    dialog._html_array.push( '</td></tr>' );
    dialog.add_checkbox( "show_tasks_checkbox"           , parent.getTranslation('Show tasks')+' '+parent.getTranslation('in the <i>jobs</i> tab')                                    , window.parent.onload_settings.checkbox_states[ "show_tasks_checkbox" ] );
    dialog.add_checkbox( "show_job_chain_orders_checkbox", parent.getTranslation('Show orders')+' '+parent.getTranslation('in the <i>job chains</i> tab')                             , window.parent.onload_settings.checkbox_states[ "show_job_chain_orders_checkbox" ] );
    dialog.add_checkbox( "show_job_chain_jobs_checkbox"  , parent.getTranslation('Show jobs')+' '+parent.getTranslation('in the <i>job chains</i> tab')                               , window.parent.onload_settings.checkbox_states[ "show_job_chain_jobs_checkbox" ] );
    dialog.add_checkbox( "show_order_history_checkbox"   , parent.getTranslation('Show order history')+' '+parent.getTranslation('in the <i>job chain</i> details')                   , window.parent.onload_settings.checkbox_states[ "show_order_history_checkbox" ] );
    if(window.parent.onload_settings.display_last_activities_tab) {
    	dialog.add_checkbox( "show_error_checkbox"         , parent.getTranslation('Show only faulty tasks and orders')+' '+parent.getTranslation('in the <i>last activities</i> tab')  , window.parent.onload_settings.checkbox_states[ "show_error_checkbox" ] );
    	dialog.add_checkbox( "show_task_error_checkbox"    , parent.getTranslation('Show last tasks error')+' '+parent.getTranslation('in the <i>last activities</i> tab')              , window.parent.onload_settings.checkbox_states[ "show_task_error_checkbox" ] );
    
    	select_opts                    = new Object();
    	select_opts.all                = ' '+parent.getTranslation('Show all')+' ';
    	select_opts.orders             = ' '+parent.getTranslation('Show only orders')+' ';
    	select_opts.tasks              = ' '+parent.getTranslation('Show only tasks')+' ';
    	dialog._html_array.push( '<tr><td style="padding:0px;padding-left:2px;">' );
    	dialog.add_select( "last_activities_radios", select_opts, window.parent.onload_settings.radio_states[ 'last_activities_radios' ], 1, 0, false );
    	dialog._html_array.push( ' '+parent.getTranslation('in the <i>last activities</i> tab')+'</td></tr>' );
    }
		dialog._html_array.push( '</table>' );
		dialog._html_array.push( '</fieldset>' );  
    
    dialog._html_array.push( '</fieldset>' );  
    dialog._html_array.push( '</td><td width="50%" valign="top">' );
    dialog._html_array.push( '<fieldset style="margin:2px;width:'+((dialog.width/2)-8)+'px;"><legend>'+parent.getTranslation('Runtime Values')+'</legend>' );
    
    dialog._html_array.push( '<fieldset><legend>'+parent.getTranslation('Limits')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog.add_settings_input( parent.getTranslation('Max. number of orders per job chain')+':',      'max_orders',          parent._scheduler._runtime_settings.max_orders, 3 );
    if(window.parent.onload_settings.display_last_activities_tab) {
    	dialog.add_settings_input( parent.getTranslation('Max. number of last activities')+':',         'max_last_activities', parent._scheduler._runtime_settings.max_last_activities, 3 );
    }
    dialog.add_settings_input( parent.getTranslation('Max. number of history entries per order')+':', 'max_order_history',   parent._scheduler._runtime_settings.max_order_history, 3 );
    dialog.add_settings_input( parent.getTranslation('Max. number of history entries per task')+':',  'max_task_history',    parent._scheduler._runtime_settings.max_task_history, 3 );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );  
    
    dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('Terminate within')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog.add_settings_input( parent.getTranslation('Max. seconds within the JobScheduler terminates')+':', 'terminate_timeout', parent._scheduler._runtime_settings.terminate_timeout, 3 );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );
    
    dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('Dialogs')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog.add_checkbox( "start_at_default_is_now", parent.getTranslation('Default start time in the &quot;<i>Start task/order at</i> &quot;-Dialog is <i>now</i>'), parent._scheduler._runtime_settings.start_at_default_is_now );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );  
    
    dialog._html_array.push( '<fieldset style="margin-top:4px;"><legend>'+parent.getTranslation('Debugging of the operations GUI')+'</legend>' );
    dialog._html_array.push( '<table cellspacing="'+cellspacing+'" cellpadding="0" width="100%" border="0">' );
    dialog.add_settings_input( parent.getTranslation('Level')+' (0-9):', 'debug_level', parent._scheduler._runtime_settings.debug_level, 3 );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '</fieldset>' );  
    
    dialog._html_array.push( '</fieldset>' );  
    dialog._html_array.push( '</td></tr>' );
    dialog._html_array.push( '</table>' );
    dialog._html_array.push( '<table cellspacing="0" cellpadding="0" width="'+dialog.width+'px" style="margin:5px;" border="0">' );
    dialog.show();
    return false;
  } else {
    var plausi    = new Array();
    var fields    = parent.left_frame.input_dialog_submit();
    var numFields = ['max_orders','max_task_history','max_order_history','max_last_activities','terminate_timeout','debug_level','update_seconds'];
    for( var i=0; i < numFields.length; i++ ) {
    	if( !window.parent.onload_settings.display_last_activities_tab && numFields[i] == 'max_last_activities') continue; 
      if( !isFinite(parseInt(fields[numFields[i]], 10)) ) plausi.push( numFields[i].replace(/_/g," ") + " must be numeric!" );
    }
    if( plausi.length == 0 ) {
      Input_dialog.close();
      /*for( var entry in fields ) {
        //if( entry.search(/new_params/) == -1 ) parent._scheduler.setCookie( entry, fields[entry] );
        if( entry.search(/^select_states_/) > -1 ) parent._scheduler._select_states[entry.replace(/^select_states_/,'')] = fields[entry];
        if( entry.search(/^view_/) > -1 ) parent._scheduler._view[entry.replace(/^view_/,'')] = fields[entry];
      }
      for( var entry in parent._scheduler._runtime_settings ) {
        if( typeof fields[entry] == "boolean" ) {
          parent._scheduler._runtime_settings[entry] = fields[entry];
        } else { 
          parent._scheduler._runtime_settings[entry] = parseInt(fields[entry],10);
        }
      }
      for( var entry in parent._scheduler._checkbox_states ) {
        parent._scheduler._checkbox_states[entry] = fields[entry];
      } */ 
      parent._scheduler.setCookie('settings',$H(fields).toJSON());
      
      //parent._scheduler.setCookie('settings',$H({'_update_periodically':fields.update_periodically,'_update_seconds':fields.update_seconds,'_show_card':fields.show_card,'_view':$H(parent._scheduler._view), '_runtime_settings': $H(parent._scheduler._runtime_settings), '_checkbox_states':$H(parent._scheduler._checkbox_states), '_select_states':$H(parent._scheduler._select_states)}).toJSON());
      return true;
    } else {
      alert( plausi.join("\n") );
      return false; 
    } 
  }
}



//-----------------------------------------------------------------------------get_start_at_prompt
// Fuer start_task_at() und add_order()
function get_start_at_prompt()
{   
    var prompt_title = '<b>Enter a start time</b><span class="small">';
    prompt_title += " in ISO format &quot;yyyy-mm-dd HH:MM[:SS]&quot; or &quot;now&quot;. ";
    prompt_title += "The time at which a task is to be started &lt;run_time&gt; is deactivated. ";
    prompt_title += "Relative times - &quot;now + HH:MM[:SS]&quot; and &quot;now + SECONDS&quot; - are allowed.";
    prompt_title += "<span>";
    return prompt_title;
}


function mandatory_field( value, msg )
{
  if( value + '' == '' ) return parent.getTranslation('$field must be stated!',{field:msg});
  return ''; 
}


//-----------------------------------------------------------------------------start_task_at
// Fuer job_menu__onclick()

function start_task_at( ret ) {
  
  if( typeof ret != "boolean" ) ret = false;
  if( !ret ) {
    _popup_menu.close();
  
    var dialog        = new Input_dialog();
    dialog.close_after_submit = false;
    dialog.width      = 384;
    dialog.submit_fct = "callErrorChecked( 'start_task_at' ,true )";
    dialog.add_title( "Start task $task", {task:'<br/>'+_obj_name+'<br/>'+_obj_title} );
    if( parent._scheduler._runtime_settings.start_next_period_enabled ) {
      dialog.add_checkbox( 'force', '<b>'+parent.getTranslation('Start enforced')+'</b>', false );
    } else {
      dialog.add_hidden( 'force', 1 );
    }
    dialog.add_prompt( get_start_at_prompt() );
    dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'at\',true,event);" /></td></tr></table>', "at", ((parent._scheduler._runtime_settings.start_at_default_is_now) ? "now" : "") );
    dialog.show();
  } else {
    var fields = input_dialog_submit();
    var msg = mandatory_field( fields.at, parent.getTranslation('Start time') );
    if( msg == '' ) {
    	if( !parent._confirm.start_job || confirm(parent.getTranslation('Do you really want to start this job?'))) {
      	Input_dialog.close();
      	var force = fields.force ? '' : ' force="no"';
      	var at    = fields.at == 'now' ? '' : ' at="' + fields.at + '"';
      	var xml_command = '<start_job job="' + parent.left_frame._job_name + '"'+ at + force +'/>';
      	if( fields.at && scheduler_exec( xml_command, false ) == 1 ) { 
          set_timeout("parent.left_frame.update()",1);
      	}
      }
      else {
      	Input_dialog.close();
      }
    } else {
      alert( msg );
      parent.left_frame.document.forms.__input_dialog__.elements.at.focus(); 
    }
  }
}


function start_task( ret ) {
  
    if( typeof ret != "boolean" ) ret = false;
    var params_element = null;
    var param_names    = new Array();
      
    if( !ret ) {
      
      _popup_menu.close();
      var params             = new Object();
      try { 
          var response       = parent._scheduler.executeSynchron( '<show_job job="' + window.parent.left_frame._job_name + '" what="job_params"/>', false );
          var params_element = response.selectSingleNode('//job/params'); 
      }
      catch( x ) { 
          return showError( x );
      }
      if( params_element ) {  
          var param_elements = params_element.selectNodes('param');
          for( var i = 0; i < param_elements.length; i++ ) {  
              params[param_elements[i].getAttribute( "name" )] = xml_encode(param_elements[i].getAttribute( "value" ).replace( /\\\\/g, "\\"));
              param_names.push(param_elements[i].getAttribute( "name" ));
          }
      }
      var dialog        = new Input_dialog();
      dialog.close_after_submit = false;
      dialog.width      = 384;
      dialog.submit_fct = "callErrorChecked( 'start_task', true )";
      dialog.add_title( "Start task $task", {task:'<br/>'+_obj_name+'<br/>'+_obj_title} );
      if( parent._scheduler._runtime_settings.start_next_period_enabled ) {
        dialog.add_checkbox( 'force', '<b>'+parent.getTranslation('Start enforced')+'</b>', false );
      } else {
        dialog.add_hidden( 'force', 1 );
      }
      dialog.add_prompt( get_start_at_prompt() );
      dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'at\',true,event);" /></td></tr></table>', "at", ((parent._scheduler._runtime_settings.start_at_default_is_now) ? "now" : "") );
      dialog.add_params( params, param_names.sort() );
      dialog.show();
    
    } else {
      try {
          var fields        = input_dialog_submit();
          var params        = getParamsXML(fields);
      }
      catch( x ) {
          return showError( x );
      }
      var msg = mandatory_field( fields.at, parent.getTranslation('Start time') );
      if( msg == '' ) {
      	if( !parent._confirm.start_job || confirm(parent.getTranslation('Do you really want to start this job?'))) {
        	Input_dialog.close();
        	var force = fields.force ? '' : ' force="no"';
        	var at    = fields.at == 'now' ? '' : ' at="' + fields.at + '"';
        	var xml_command = '<start_job job="' + parent.left_frame._job_name + '"'+ at + force +'>' + params + '</start_job>';
        	if( fields.at && scheduler_exec( xml_command, false ) == 1 ) { 
            set_timeout("parent.left_frame.update()",1);
        	}
        } else {
        	Input_dialog.close();
        }
      } else {
        alert( msg );
        parent.left_frame.document.forms.__input_dialog__.elements.at.focus(); 
      }
    }
}


//-----------------------------------------------------------------------------start_order_at
function start_order_at( now ) {
  
    switch( now ) {
      case 0: _popup_menu.close();
              var dialog        = new Input_dialog();
              dialog.width      = 360;
              dialog.close_after_submit = false;
              dialog.submit_fct = "callErrorChecked( 'start_order_at', 2 )";
              dialog.add_title( "Start order $order", {order:'<br/>'+_obj_name+'<br/>'+_obj_title} );
              dialog.add_prompt( get_start_at_prompt() );
              dialog.add_prompt( "" );
              dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'at\',true,event);" /></td></tr></table>', "at", ((parent._scheduler._runtime_settings.start_at_default_is_now) ? "now" : "") );
              dialog.show();
              break;
      case 1: if( !parent._confirm.start_order || confirm(parent.getTranslation('Do you really want to start this order?'))) {
      					_popup_menu.close();
              	var xml_command = '<modify_order at="now" job_chain="' + parent.left_frame._job_chain + '" order="' + parent.left_frame._order_id + '"/>';
              	exec_modify_order(xml_command);
              } else {
              	_popup_menu.close();
              }
              break;
      case 2: var fields        = input_dialog_submit();
              var msg           = mandatory_field( fields.at, parent.getTranslation('Start time') );
              if( msg == '' ) {
              	if( !parent._confirm.start_order || confirm(parent.getTranslation('Do you really want to start this order?'))) {
                	Input_dialog.close();
                	var xml_command = '<modify_order at="' + fields.at + '" job_chain="' + parent.left_frame._job_chain + '" order="' + parent.left_frame._order_id + '"/>';
                	exec_modify_order(xml_command);
                } else {
                	Input_dialog.close();
                }
              } else {
                alert( msg );
                parent.left_frame.document.forms.__input_dialog__.elements.at.focus();
              } 
              break;
    }
}

function start_order( ret )
{
    if( typeof ret != "boolean" ) ret = false;
    var params_element = null;
    var param_names = new Array();
      
    if( !ret ) {
      
      _popup_menu.close(); 
      
      var params      = new Object();
      try { 
          var response       = parent._scheduler.executeSynchron( '<show_order order="' + window.parent.left_frame._order_id + '" job_chain="' + window.parent.left_frame._job_chain + '" what="payload"/>', false );
          var params_element = response.selectSingleNode('//order/payload/params'); 
      }
      catch( x ) { 
          return showError( x );
      }
      if( params_element ) {  
          var param_elements = params_element.selectNodes('param');
          for( var i = 0; i < param_elements.length; i++ ) {  
              params[param_elements[i].getAttribute( "name" )] = xml_encode(param_elements[i].getAttribute( "value" ).replace( /\\\\/g, "\\"));
              param_names.push(param_elements[i].getAttribute( "name" ));
          }
      }
      var dialog        = new Input_dialog();
      dialog.close_after_submit = false;
      dialog.width      = 384;
      dialog.submit_fct = "callErrorChecked( 'start_order', true )";
      dialog.add_title( "Start order $order", {order:'<br/>'+_obj_name+'<br/>'+_obj_title} );
      dialog.add_prompt( get_start_at_prompt() );
      dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'at\',true,event);" /></td></tr></table>', "at", ((parent._scheduler._runtime_settings.start_at_default_is_now) ? "now" : "") );
      dialog.add_params( params, param_names.sort() );
      dialog.show();
    
    } else {
      try {
          var fields        = input_dialog_submit();
          var params        = getParamsXML(fields);
      }
      catch( x ) {
          return showError( x );
      }
      var msg = mandatory_field( fields.at, parent.getTranslation('Start time') );
      if( msg == '' ) {
      	if( !parent._confirm.start_order || confirm(parent.getTranslation('Do you really want to start this order?'))) {
        	Input_dialog.close();
        	var xml_command = '<modify_order at="' + fields.at + '" job_chain="' + parent.left_frame._job_chain + '" order="' + window.parent.left_frame._order_id + '">' + params + '</modify_order>';
        	exec_modify_order(xml_command);
        } else {
        	Input_dialog.close();
        }
      } else {
        alert( msg );
        parent.left_frame.document.forms.__input_dialog__.elements.at.focus();
      }
    }   
}


function resume_order( ret )
{
    if( typeof ret != "boolean" ) ret = false;
    var params_element = null;
    var param_names = new Array();
      
    if( !ret ) {
      
      _popup_menu.close(); 
      
      var params      = new Object();
      try { 
          var response       = parent._scheduler.executeSynchron( '<show_order order="' + window.parent.left_frame._order_id + '" job_chain="' + window.parent.left_frame._job_chain + '" what="payload"/>', false );
          var params_element = response.selectSingleNode('//order/payload/params'); 
      }
      catch( x ) { 
          return showError( x );
      }
      if( params_element ) {  
          var param_elements = params_element.selectNodes('param');
          for( var i = 0; i < param_elements.length; i++ ) {  
              params[param_elements[i].getAttribute( "name" )] = xml_encode(param_elements[i].getAttribute( "value" ).replace( /\\\\/g, "\\"));
              param_names.push(param_elements[i].getAttribute( "name" ));
          }
      }
      var dialog        = new Input_dialog();
      dialog.close_after_submit = false;
      dialog.width      = 384;
      dialog.submit_fct = "callErrorChecked( 'resume_order', true )";
      dialog.add_title( "Resume order $order", {order:'<br/>'+_obj_name+'<br/>'+_obj_title} );
      dialog.add_params( params, param_names.sort() );
      dialog.show();
    
    } else {
      try {
          var fields        = input_dialog_submit();
          var params        = getParamsXML(fields);
      }
      catch( x ) {
          return showError( x );
      }
      
      if( !parent._confirm.resume_order || confirm(parent.getTranslation('Do you really want to resume this order?'))) {
        	Input_dialog.close();
        	var xml_command = '<modify_order job_chain="' + parent.left_frame._job_chain + '" order="' + window.parent.left_frame._order_id + '" suspended="no">' + params + '</modify_order>';
        	exec_modify_order(xml_command);
      } else {
        	Input_dialog.close();
      }
    }   
}

//-----------------------------------------------------------------------------add_order
// Fuer order_menu__onclick()

function add_order( big_chain, order_state, order_end_state, ret )
{    
    if( typeof ret             != "boolean"   ) ret             = false;
    if( typeof order_state     == "undefined" ) order_state     = "";
    if( typeof order_end_state == "undefined" ) order_end_state = "";
    var order_state_prompt     = '<b>'+parent.getTranslation('Select an order state')+'</b>';
    order_state_prompt        += ( order_state     == "" ) ? "" : '<br/>' + parent.getTranslation( '<span class="small">The current order state is $state.</span>', {state:'&quot;'+order_state+'&quot;'} );
    var order_end_state_prompt = '<b>'+parent.getTranslation('Select an order end state')+'</b>';
    order_end_state_prompt    += ( order_end_state == "" ) ? "" : '<br/>' + parent.getTranslation( '<span class="small">The current order state is $state.</span>', {state:'&quot;'+order_end_state+'&quot;'} );
    var params_element         = null;
    var param_names            = new Array();
    var hot                    = 1;
        
    if( !ret ) {
      
      _popup_menu.close();
      
      if( window.parent.left_frame._order_id != "" ) {
          var params   = new Object();
          try {
              var response       = parent._scheduler.executeSynchron( '<show_order order="' + window.parent.left_frame._order_id + '" job_chain="' + window.parent.left_frame._job_chain + '" what="payload"/>', false );
              var params_element = response.selectSingleNode('//order/payload/params');
          }
          catch( x ) {
              return showError( x );
          }
          if( params_element ) {  
              var param_elements = params_element.selectNodes('param');
              for( var i = 0; i < param_elements.length; i++ ) {  
                  params[param_elements[i].getAttribute( "name" )] = xml_encode(param_elements[i].getAttribute( "value" ).replace( /\\\\/g, "\\"));
                  param_names.push(param_elements[i].getAttribute( "name" ));
              }
          }
      } 
      var states        = get_order_states( big_chain );
      var dialog        = new Input_dialog();
      dialog.width      = 384;
      dialog.close_after_submit = false;
      dialog.submit_fct = "callErrorChecked( 'add_order'," + big_chain + ", '" + order_state + "', '" + order_end_state + "', true )";
      dialog.add_title( "Add order to $job_chain", {job_chain:'<br/>'+window.parent.left_frame._job_chain.replace(/^\//,'')} );
      dialog.add_labeled_input( '<b>'+parent.getTranslation("Enter an order id")+'</b>', "id", "" );
      dialog.add_labeled_input( '<b>'+parent.getTranslation("Enter an order title")+'</b>', "title", _obj_title );
      dialog.add_prompt( get_start_at_prompt() );
      dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'at\',true,event);" /></td></tr></table>', "at", ((parent._scheduler._runtime_settings.start_at_default_is_now) ? "now" : "") );
      dialog.add_hidden( "run_time", "" );
      if( typeof states[0] == "object" ) {
      	dialog.add_labeled_select( order_state_prompt, "state", states[0], order_state );
      } else {
        dialog.add_hidden( "state", states[0] );
      }
      if( typeof states[1] == "object" ) {
        dialog.add_labeled_select( order_end_state_prompt, "end_state", states[1], order_end_state );
      } else {
        dialog.add_hidden( "end_state", states[1] );
      }
      dialog.add_params( params, param_names.sort() );
      dialog.show();  
    } else {   
      var fields = input_dialog_submit();
      var msg    = "";
      
      try {
          var fields        = input_dialog_submit();
          var params        = getParamsXML(fields);
      }
      catch( x ) {
      		Input_dialog.close();
          return showError( x );
      }
      fields.id        = ( fields.id != "" ) ? ' id="' + fields.id + '"' : '';
      fields.at        = ' at="' + fields.at + '"';
      fields.end_state = ' end_state="'+fields.end_state+'"';  
      
      if( !parent._confirm.add_order || confirm(parent.getTranslation('Do you really want to add an order?'))) {
        	Input_dialog.close();
        	var xml_command  = '<add_order' + fields.at + fields.id + ' job_chain="' + parent.left_frame._job_chain + '" state="' + fields.state + '"' + fields.end_state + ' title="' + fields.title + '" replace="no">' + params + fields.run_time + '</add_order>';
        	if( scheduler_exec( xml_command, false ) == 1 ) { 
          	set_timeout("parent.left_frame.update()",1);
        	}
      } else {
      	Input_dialog.close();
      }
    }   
}


function getParamsXML(fields)
{
		var params        = "";
    if( fields.param_names.length + fields.count_new_params > 0 ) params += '<params>'; 
    for( var i = 0; i < fields.param_names.length; i++ ) {
    	params += '<param name="' + fields.param_names[i] + '" value="' + xml_encode(fields[fields.param_names[i]]) + '"/>';
    }
    for( var new_param in fields.new_params ) {
    	params += '<param name="' + new_param + '" value="' + xml_encode(fields.new_params[new_param]) + '"/>';
    }
    if( fields.param_names.length + fields.count_new_params > 0 ) params += '</params>';
    return params;
}


//-----------------------------------------------------------------------------delete_orders
// Fuer job_chain_menu__onclick()

function delete_orders( ret )
{   
    if( typeof ret             != "boolean"   ) ret             = false;
    var hot                    = 1;
        
    if( !ret ) {
      
      _popup_menu.close();
      
      var dialog        = new Input_dialog();
      dialog.width      = 384;
      dialog.close_after_submit = false;
      dialog.submit_fct = "callErrorChecked( 'delete_orders', true )";
      dialog.add_title( "Remove temporary orders from $job_chain", {job_chain:'<br/>'+window.parent.left_frame._job_chain.replace(/^\//,'')} );
      //TODO list of temp orders and checkbox for all
      dialog.add_checkbox( "all", parent.getTranslation('all'), false );
      dialog.show();
    } else {
      var fields = input_dialog_submit();
      //TODO plausi 
      Input_dialog.close();
      //TODO command
    }    
}


//-----------------------------------------------------------------------------set_order_state
// Fuer order_menu__onclick()

function set_order_state( order_state, order_end_state, setback, suspended, hot, ret ) 
{   
    if( typeof ret != "boolean" ) ret = false;
    
    if( !ret ) {
      _popup_menu.close();
      var dialog        = new Input_dialog();
      var states        = get_order_states();
      dialog.width      = 240;
      dialog.submit_fct = "callErrorChecked( 'set_order_state', '" + order_state + "', '" + order_end_state + "', " + setback + ", " + suspended + ", " + hot + ", true )";
      dialog.add_title( "Set order state of $order", {order:'<br/>'+_obj_name+'<br/>'+_obj_title} );
      dialog.add_prompt( '<b>Select a new order state</b>' );
      dialog.add_prompt( '<span class="small">The current order state is $state.</span>', {state:'&quot;'+order_state+'&quot;'} );
      dialog.add_prompt( "" );
      dialog.add_select( "new_state", states[0], order_state );
      dialog.add_prompt( "" );
      if( order_end_state != "" ) {
        dialog.add_prompt( '<b>Select a new order end state</b>' );
        dialog.add_prompt( '<span class="small">The current order end state is $state.</span>', {state:'&quot;'+order_end_state+'&quot;'} );
      } else {
        dialog.add_prompt( '<b>Select a new order end state</b>' );
        dialog.add_prompt( '<span class="small">The current order end state is $state.</span>', {state:parent.getTranslation('undefined')} );
      }
      dialog.add_prompt( "" );
      dialog.add_select( "new_end_state", states[1], order_end_state );
      if( setback ) dialog.add_checkbox( "remove_setback", '<span class="small">'+parent.getTranslation('Remove setback')+'</span>', false );
      if( suspended ) dialog.add_checkbox( "resume", '<span class="small">'+parent.getTranslation('Resume order')+'</span>', false );
      dialog.show();
    } else {   
    	var fields = input_dialog_submit();
      var sback = fields.remove_setback ? ' setback="no"' : '';
      //temporary suspended orders which new state is an end state will be resumed to avoid blacklist; https://change.sos-berlin.com/browse/JOC-18
      if( suspended && !hot && !fields.resume ) {  
      	var states  = get_order_states(); 
      	for(var i=0; i < states[0].length; i++ ) {
      		if( states[0][i]['key'] == fields.new_state ) {
      			if( states[0][i]['endnode'] ) { 
      				fields.resume = true;
      			}
      			break;
      		}
      	} 
      }
      var resume = fields.resume ? ' suspended="no"' : '';
      var end_state = ' end_state="'+fields.new_end_state+'"';  
      
      if( !parent._confirm.set_order_state || confirm(parent.getTranslation('Do you really want to change the order state?'))) {
        Input_dialog.close();  
      	var xml_command = '<modify_order job_chain="' + parent.left_frame._job_chain + '" order="' + parent.left_frame._order_id + '"' + sback + resume + ' state="' + fields.new_state + '"' + end_state + '/>'; 
      	exec_modify_order( xml_command );
      } else {
      	Input_dialog.close();
      }  
    }
}


function get_order_states( big_chain )
{    
    if( typeof big_chain == 'undefined' ) big_chain = 0;
    var states            = new Array();
    var end_states        = new Array();
    var job_chain_element = window.parent.left_frame._response.selectSingleNode( ".//job_chain[@path='" + window.parent.left_frame._job_chain + "']" );
    if( !job_chain_element ) {
      var response        = parent._scheduler.executeSynchron( '<show_job_chain job_chain="' + parent.left_frame._job_chain + '"/>', false );
      if( response ) job_chain_element = response.selectSingleNode('//job_chain');
    }
    if( big_chain ) {
      var job_chain_nodes   = job_chain_element.selectNodes( ".//job_chain_node.job_chain|.//job_chain_node[@job_chain]" );
      var job_chain_endnodes   = [];
    } else {
      var job_chain_nodes   = job_chain_element.selectNodes( ".//job_chain_node[@job]|.//file_order_sink" );
      var job_chain_endnodes   = job_chain_element.selectNodes( ".//job_chain_node[not(@job)]|.//job_chain_node.end" );
    }
    end_states.push( {key:'', display:'(none)'} );
    for( var i = 0; i < job_chain_nodes.length; i++ ) {
    		states.push( {key:job_chain_nodes[i].getAttribute('state'), display:job_chain_nodes[i].getAttribute('state'), endnode:false} );
        end_states.push( {key:job_chain_nodes[i].getAttribute('state'), display:job_chain_nodes[i].getAttribute('state'), endnode:false} );
    }
    for( var i = 0; i < job_chain_endnodes.length; i++ ) {
    		states.push( {key:job_chain_endnodes[i].getAttribute('state'), display:job_chain_endnodes[i].getAttribute('state'), endnode:true} );
        end_states.push( {key:job_chain_endnodes[i].getAttribute('state'), display:job_chain_endnodes[i].getAttribute('state'), endnode:true} );
    }
    return new Array(states, end_states); 
}


//-----------------------------------------------------------------------------set_run_time
function set_run_time( caller, hot, ret )
{  
   if( typeof ret != "boolean" ) ret = false;
   if( !ret ) {
      if( caller != 'add_schedule' ) _popup_menu.close();
      var run_time      = get_run_time( caller, hot );
      var dialog        = new Input_dialog();
      dialog.width      = 384;
      dialog.close_after_submit = false;
      dialog.submit_fct = "callErrorChecked( 'set_run_time', '" + caller + "', " + hot + ", true )";
      var has_run_options = ( caller == 'job' ) ? 'false' : 'false'; //z.Zt. werden noch keine run options unterstuetzt
      switch( caller ) {
        case 'order'          : 
        case 'job'            : dialog.add_title( "Set run time of $job", {job:'<br/>'+_obj_name+'<br/>'+_obj_title} );
                                dialog.add_prompt( '<b>Enter a run time</b> or use the $editor', {editor:'<input class="buttonbar" type="button" value=" '+parent.getTranslation('run time editor')+' " onclick="runtime_editor(\'run_time\', ' + has_run_options + ')"/>'} );
                                dialog.add_prompt( "" );
                                break;
        case 'add_schedule'   : dialog.add_title( "Add schedule" );
                                dialog.add_labeled_input( '<b>'+parent.getTranslation('Enter a folder')+'</b>', 'dir', dirname(window.parent.left_frame._schedule) );
                                dialog.add_labeled_input( '<b>'+parent.getTranslation('Enter a schedule name')+'</b>', 'name', basename(window.parent.left_frame._schedule) );
                                dialog.add_prompt( '<b>Enter a run time</b> or use the $editor', {editor:'<input class="buttonbar" type="button" value=" '+parent.getTranslation('run time editor')+' " onclick="runtime_editor(\'schedule\',false)"/>'} );
                                dialog.add_prompt( "" );
                                run_time = ['&lt;schedule/&gt;',''];
                                break;
        case 'add_substitute' : dialog.add_title( "Add substitute for $schedule", {schedule:(window.parent.left_frame._substitute =="/" ? '<br/>'+_obj_title : '<br/>'+window.parent.left_frame._substitute.replace(/^\//,''))} );
                                dialog.add_labeled_input( '<b>'+parent.getTranslation('Enter a folder')+'</b>', 'dir', dirname(window.parent.left_frame._substitute) );
                                dialog.add_labeled_input( '<b>'+parent.getTranslation('Enter a schedule name')+'</b>', 'name', basename(window.parent.left_frame._substitute) );
                                dialog.add_prompt( '<b>Enter a run time</b> or use the $editor', {editor:'<input class="buttonbar" type="button" value=" '+parent.getTranslation('run time editor')+' " onclick="runtime_editor(\'schedule\',false)"/>'} );
                                dialog.add_prompt( "" );
                                break;
        case 'schedule'       : dialog.add_title( "Edit $schedule", {schedule:'<br/>'+_obj_title} );
                                dialog.add_prompt( '<b>Enter a run time</b> or use the $editor', {editor:'<input class="buttonbar" type="button" value=" '+parent.getTranslation('run time editor')+' " onclick="runtime_editor(\'schedule\',false)"/>'} );
                                dialog.add_prompt( "" );
                                break;
      }
      
      var schedules     = get_schedules();
      if(caller == 'order' || caller == 'job') {
        dialog.add_labeled_select( parent.getTranslation("or choose a schedule"), "schedules", schedules, run_time[1], ' onchange="this.form.elements.run_time.value=(this.value!=\'\') ? \'&lt;run_time schedule=&quot;\'+this.value+\'&quot;/&gt;\' : \'&lt;run_time/&gt;\'"' );
      }
      if(!run_time[0]) run_time[0] = "&lt;run_time/&gt;";
      dialog.add_textarea( 'run_time', run_time[0] );
      dialog.show();
    } else {
      var xml_command = '';
      var hot_command = '';
      var fields = input_dialog_submit();
      if( !fields.run_time ) fields.run_time = '<run_time/>'; 
      switch( caller ) {
        case 'order'          : if( !parent._confirm.set_order_run_time || confirm(parent.getTranslation('Do you really want to set the run time?'))) {
        													xml_command = '<modify_order job_chain="' + window.parent.left_frame._job_chain + '" order="' + window.parent.left_frame._order_id + '">' + fields.run_time + '</modify_order>';
                                }
                                Input_dialog.close();
                                break;
        case 'job'            : if( !parent._confirm.set_job_run_time || confirm(parent.getTranslation('Do you really want to set the run time?'))) {
        													xml_command = '<modify_job job="' + window.parent.left_frame._job_name + '">' + fields.run_time + '</modify_job>';
                                }
                                Input_dialog.close();
                                break;
        case 'add_schedule'   : 
        case 'add_substitute' : var msg = mandatory_field( fields.name, parent.getTranslation('Name') );
                                if(msg == '') {
                                	if( !parent._confirm.add_schedule || confirm(parent.getTranslation('Do you really want to add a substituting schedule?'))) {
                                  	var new_run_time = parent._scheduler.loadXML(fields.run_time);
                                  	var scheduler_elem = new_run_time.selectSingleNode('/schedule'); 
                                  	scheduler_elem.setAttribute('name', fields.name);
                                  	hot_command = '<modify_hot_folder folder="'+fields.dir.replace(/\\/g,'/')+'">'+osNewline(new_run_time.xml)+'</modify_hot_folder>';
                                  }
                                  Input_dialog.close(); 
                                } else {
                                  alert( msg );                                                                  
                                  parent.left_frame.document.forms.__input_dialog__.elements.name.focus();
                                  return true;
                                }
                                break;
        case 'schedule'       : if( !parent._confirm.modify_schedule || confirm(parent.getTranslation('Do you really want to modify this schedule?'))) {
                                	var new_run_time = parent._scheduler.loadXML(fields.run_time);
                                	var scheduler_elem = new_run_time.selectSingleNode('/schedule'); 
                                	scheduler_elem.setAttribute('name', basename(parent.left_frame._schedule));
                                	hot_command = '<modify_hot_folder folder="'+dirname(parent.left_frame._schedule)+'">'+osNewline(new_run_time.xml)+'</modify_hot_folder>';
                                }
                                Input_dialog.close();
                                break;
      }
      
      if( hot_command == "" && xml_command != "" ) {
        exec_modify_order(xml_command);
      }
      
      if( hot_command != "" && scheduler_exec( hot_command, false ) == 1 ) {
        switch( caller ) { 
          case 'order'          :
          case 'job'            : //break;
          case 'add_schedule'   :
          case 'add_substitute' :
          case 'schedule'       : set_timeout("update__onclick(false, true, 1000)",3000); break;
        }
      }
    }
}


function get_run_time( caller, hot )
{   
    var run_time         = "";
    var schedule         = "";
    var what             = (hot > 0) ? 'source run_time' : 'run_time';
    parent._source       = undefined;
    
    try {
        switch( caller ) {
          case 'order'          : if( typeof window.parent.left_frame._order_id != 'string' || window.parent.left_frame._order_id == '' ) return ['&lt;run_time/&gt;',''];
                                  var response   = parent._scheduler.executeSynchron( '<show_order order="' + window.parent.left_frame._order_id + '" job_chain="' + window.parent.left_frame._job_chain + '" what="' + what + '"/>', false, false );
                                  run_time       = response.selectSingleNode('//order/run_time');
                                  parent._source = response.selectSingleNode('/spooler/answer/order/source/order');
                                  schedule       = (run_time) ? run_time.getAttribute('schedule') : '';
                                  break;
          case 'job'            : var response   = parent._scheduler.executeSynchron( '<show_job job="' + window.parent.left_frame._job_name + '" what="' + what + '"/>', false, false );
                                  run_time       = response.selectSingleNode('//job/run_time');
                                  parent._source = response.selectSingleNode('/spooler/answer/job/source/job');
                                  schedule       = (run_time) ? run_time.getAttribute('schedule') : '';
                                  break;
          case 'add_schedule'   : return ['&lt;schedule/&gt;',''];
          case 'add_substitute' : if( parent._scheduler._tree_view_enabled ) {
                                    var response = parent._scheduler.executeSynchron( '<show_state what="folders" subsystems="schedule folder"/>', false, false );
                                    run_time     = response.selectSingleNode( "/spooler/answer/state//schedules/schedule[@path = '"+window.parent.left_frame._schedule+"']" );
                                  } else {
                                    var response = parent._scheduler.executeSynchron( '<show_state what="schedules" max_task_history="0" max_orders="0/>', false, false );
                                    run_time     = response.selectSingleNode( "/spooler/answer/state/schedules/schedule[@path = '"+window.parent.left_frame._schedule+"']" );
                                  }
                                  if( !run_time ) return ['&lt;schedule/&gt;',''];
                                  for(var i=0; i<run_time.attributes.length; i++) {
                                    if( run_time.attributes[i].nodeName.search(/^(substitute|valid_from|valid_to|title)$/) == -1 ) {
                                      run_time.removeAttribute(run_time.attributes[i].nodeName);
                                      i--;
                                    }
                                  }
                                  if( window.parent.left_frame._substitute == "/" ) {
                                    var isoDate  = new Date().getISODate();
                                    var restore_attributes = {"substitute":_schedule,"valid_from":isoDate+" 00:00:00","valid_to":isoDate+" 24:00:00"};
                                    for(var entry in restore_attributes) {
                                      run_time.setAttribute(entry, restore_attributes[entry]);
                                    }
                                  }
                                  for(var i=0; i<run_time.childNodes.length; i++) {
		                              	if( run_time.childNodes[i].nodeName=="file_based" || run_time.childNodes[i].nodeName=="schedule.users" || run_time.childNodes[i].nodeName=="replacement" ) {
		                              		run_time.removeChild(run_time.childNodes[i]);
		                              	}
		                              }
		                              break;
          case 'schedule'       : if( parent._scheduler._tree_view_enabled ) {
                                    var response = parent._scheduler.executeSynchron( '<show_state what="folders" subsystems="schedule folder"/>', false, false );
                                    run_time     = response.selectSingleNode( "/spooler/answer/state//schedules/schedule[@path = '"+window.parent.left_frame._schedule+"']" );
                                  } else {
                                    var response = parent._scheduler.executeSynchron( '<show_state what="schedules" max_task_history="0" max_orders="0/>', false, false );
                                    run_time     = response.selectSingleNode( "/spooler/answer/state/schedules/schedule[@path = '"+window.parent.left_frame._schedule+"']" );
                                  }
                                  if( !run_time ) return ['&lt;schedule/&gt;',''];
                                  for(var i=0; i<run_time.attributes.length; i++) {
                                    if( run_time.attributes[i].nodeName.search(/^(substitute|valid_from|valid_to|title)$/) == -1 ) {
                                      run_time.removeAttribute(run_time.attributes[i].nodeName);
                                      i--;
                                    }
                                  }
                                  for(var i=0; i<run_time.childNodes.length; i++) {
		                              	if( run_time.childNodes[i].nodeName=="file_based" || run_time.childNodes[i].nodeName=="schedule.users" || run_time.childNodes[i].nodeName=="replacement" ) {
		                              		run_time.removeChild(run_time.childNodes[i]);
		                              	}
		                              }
		                              break;
        }
        run_time = ( run_time ) ? xml_encode(run_time.xml) : '';
        run_time = run_time.replace(/\n\s*\n/g,"\n").replace(/\t/g,"  ").replace(/\n[ ]{8}/g,"\n");
        return [run_time,schedule];
    }
    catch( x ) {
        showError( x );
        return [run_time,schedule];
    }
}


function get_schedules()
{
    var path
    var schedules = new Array();
    schedules.push( {key:'', display:'(none)'} );
    parent.left_frame._schedules = new Array();
    var schedule_elems = [];
    if(parent._scheduler._tree_view_enabled) {
      schedule_elems = parent.top_frame._state.selectNodes('//schedules/schedule[not(@substitute)]');
    } else {
      schedule_elems = parent.left_frame._response.selectNodes('/spooler/answer/state/schedules/schedule[not(@substitute)]');
    }
    for( var i=0; i < schedule_elems.length; i++ ) {
      path = schedule_elems[i].getAttribute('path');
      schedules.push( {key:path, display:path} );
      parent.left_frame._schedules.push(path);
    }
    return schedules;
}


//--------------------------------------------------------------------------add_schedule
function add_schedule( path )
{
    if( typeof path != 'string' ) path = '';
    _obj_title = '';
    window.parent.left_frame._schedule  = xml_encode(path);
    Input_dialog.close();
    callErrorChecked('set_run_time','add_schedule',1);
}


//--------------------------------------------------------------------------show_calendar

function show_calendar( menu, ret )
{
   if( typeof ret != "boolean" ) ret = false;
   if( !ret ) {
      _popup_menu.close();
      var now           = new Date();
      var date_from     = now.getISODate();
      now.setDate(now.getDate()+7);
      var date_to       = now.getISODate();
      var dialog        = new Input_dialog();
      dialog.width      = 340;
      dialog.submit_fct = "callErrorChecked( 'show_calendar', '" + menu + "',true )";
      dialog.add_title( "Parameterize the start times list" );
      dialog.add_prompt( '<b>Set the period</b>' );
      dialog.add_prompt( '<span class="small">(Timestamps in ISO format yyyy-mm-dd[ hh:mm:ss])</span>' );
      dialog.add_prompt( '' );
      dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td><b>&#160;&#160;&#160;'+parent.getTranslation('...from')+'</b></td><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'from\',false,event);" /></td></tr></table>', "from", date_from, 19 );
      dialog.add_labeled_input( '<table cellspacing="0" cellpadding="0" width="100%"><tr><td><b>&#160;&#160;&#160;'+parent.getTranslation('...to')+'</b></td><td align="right"><img src="icon_calendar.gif" alt="calendar" title="calendar" onclick="Input_dialog.show_calendar(\'before\',false,event);" /></td></tr></table>', "before", date_to, 19 );
      dialog.add_labeled_input( '<b>'+parent.getTranslation('Max. hits')+'</b>', "limit", "100", 10 );
      dialog.add_labeled_select( '<b>'+parent.getTranslation('Output format')+'</b>', "format", [{key:'html', display:'html'},{key:'xml', display:'xml'},{key:'csv', display:'csv'}], "html" );
      switch( menu ) {
        case 'scheduler' : dialog.add_checkbox( "what", '<b>'+parent.getTranslation('Include order start times')+'</b>', true ); break;
        case 'job_chain' :
        case 'order'     : dialog.add_hidden( "what", "1" ); break;
        case 'job'       : dialog.add_hidden( "what", "0" ); break;
      }
      dialog.show();
    } else {
      var fields = input_dialog_submit();
      var from   = check_date(fields.from); 
      if( from   == -1 )                   throw new Error( parent.getTranslation("Period from ($field) is invalid date or before 1970-01-01.",{field:fields.from}) );
      var before = check_date(fields.before); 
      if( before == -1 )                   throw new Error( parent.getTranslation("Period from ($field) is invalid date or before 1970-01-01.",{field:fields.before}) );
      var limit  = fields.limit;
      if( limit  == "" ) { limit = 100; }
      var limit  = parseInt(limit, 10);
      if( !isFinite(limit) || limit <= 0 ) throw new Error( parent.getTranslation("Max. hits ($field) is not a positive number.",{field:fields.limit}) );
      var what   = fields.what;
      if( typeof what != 'boolean' ) what = (what=="1") ? true : false;
      from       = ( from   == "" ) ? "" : " from='"+from+"'";
      before     = ( before == "" ) ? "" : " before='"+before+"'";
      what       = ( !what )        ? "" : " what='orders'";
      limit      = " limit='"+limit+"'";
      var xml_command  = "<show_calendar"+before+from+limit+what+"/>";
      //alert(xml_command);
      _calendar_params = {'xml_command':xml_command,'menu':menu,'format':fields.format,'job':'','order':'','timezone':parent._scheduler._timezone};  //die Variable liest die aufgerufende Seite
      switch( menu ) {
        case 'job_chain' : _calendar_params.job   = window.parent.left_frame._job_chain; break;
        case 'order'     : _calendar_params.job   = window.parent.left_frame._job_chain;
                           _calendar_params.order = window.parent.left_frame._order_id; break;
        case 'job'       : _calendar_params.job   = window.parent.left_frame._job_name; break;
      }
      var window_name  = "scheduler_calendar"; 
      if( parent._scheduler._dependend_windows[ window_name ] ) { 
    	  try {
    		  parent._scheduler._dependend_windows[ window_name ].close();
    	  }
    	  catch(x) {}
      }
      open_url( "scheduler_calendar.html", window_name, true );    
    } 
}


function check_date( datetime )
{
    if(datetime == "") return datetime;
    if(datetime.search( /[^0-9 T:-]/ ) > -1) return -1;
    if(datetime.search( /^\d{4}-\d{2}-\d{2}/ ) == -1) return -1;
    var milliseconds = eval("Date.UTC("+datetime.replace( /^(\d{4})(-)(\d{2})(-)(\d{2}.*)$/,"$1:($3-1):$5" ).replace(/[ T:]/g,",")+")");
    if( milliseconds < 0 ) return -1;
    var date_obj = new Date(milliseconds);
    return date_obj.getUTCFullYear()+"-"+get_2digits(date_obj.getUTCMonth()+1)+"-"+get_2digits(date_obj.getUTCDate())+"T"+get_2digits(date_obj.getUTCHours())+":"+get_2digits(date_obj.getUTCMinutes())+":"+get_2digits(date_obj.getUTCSeconds()); 
}


function get_2digits( num )
{
    return (num < 10) ? "0"+num : num;
}


//--------------------------------------------------------------------------addTimezone
/*function addTimezone(ret)
{
	 if( typeof ret != "boolean" ) ret = false;
   if( !ret ) {
      _popup_menu.close();
      var dialog        = new Input_dialog();
      dialog.width      = 200;
      dialog.submit_fct = "callErrorChecked( 'addTimezone', true )";
      //dialog.add_title( "Add a timezone" );
    var select_opts = [{'key':'','display':'Add time zone'}];
      var availTimezones = parent._scheduler.getTimezones();
      for( i=0; i < availTimezones.length; i++ ) {
      	select_opts.push({'key':availTimezones[i],'display':availTimezones[i]});
      }
      dialog.add_select( "timezone", select_opts );
      dialog.show();
    } else {
    	var fields = input_dialog_submit(); 
    	if( fields.timezone != '' ) {
    		//alert(fields.timezone);
    		var timezonesSelect = parent.top_frame.document.getElementById('timezones');
    		var selectedIdx = timezonesSelect.options.length;
	  		timezonesSelect.options[selectedIdx] = new Option( fields.timezone, fields.timezone );
	  		timezonesSelect.selectedIndex = selectedIdx;
	  		parent.top_frame.setTimezone(fields.timezone);
    	}
    }
} */


//--------------------------------------------------------------------------scheduler_menu__onclick

function scheduler_menu__onclick( elt )
{
    resetError();
    if( typeof window.parent.left_frame._response != 'object' ) throw new Error("Please wait until the window is completly loaded.");
    window.parent.left_frame._job_chain = '';
    window.parent.left_frame._job_name  = '';
    var popup_builder   = new Popup_menu_builder();
    
    var state           = window.parent.top_frame._state.getAttribute( "state" );
    var waiting_errno   = window.parent.top_frame._state.getAttribute( "waiting_errno" );
    var cluster_element = window.parent.top_frame._state.selectSingleNode( "cluster" );
    var within          = parent._scheduler._runtime_settings.terminate_timeout;
    
    var command         = function( cmd ) { return "<modify_spooler cmd='" + cmd + "'/>"; }
    
    popup_builder.add_show_log( parent.getTranslation("Show log")                           , "", "show_log" );
    popup_builder.add_entry   ( parent.getTranslation("Show job dependencies")              , "show_job_illustration()" );
    popup_builder.add_entry   ( parent.getTranslation("Show job chain dependencies")        , "show_job_chain_illustration()" );
    popup_builder.add_entry ( parent.getTranslation("Show start times")                     , "callErrorChecked('show_calendar','scheduler')" );
    popup_builder.add_entry ( parent.getTranslation("Manage filters")                       , "callErrorChecked('show_administration','filter')" );
    popup_builder.add_entry ( parent.getTranslation("Manage log categories")                , "callErrorChecked('show_administration','log_categories')" );
    //popup_builder.add_entry ( parent.getTranslation("Add time zone")                        , "addTimezone()" );
    if( !parent._hide.terminate_jobscheduler || !parent._hide.restart_jobscheduler || !parent._hide.pause_jobscheduler || !!parent._hide.continue_jobscheduler ) {
    	popup_builder.add_bar();
    }
  //popup_builder.add_command ( "Stop"                               , command( "stop" ), state != "stopped"  &&  state != "stopping"  &&  state != "stopping_let_run" );
    if( window.createPopup == undefined ) {
      if( state != "paused" ) {
        popup_builder.add_command ( parent.getTranslation("Pause")+" <span style=\"color:gray\">| "+parent.getTranslation("Continue")+"</span>", command( "pause"), true, parent._hide.pause_jobscheduler, parent._confirm.pause_jobscheduler, 'pause the JobScheduler' );
      } else {
        popup_builder.add_command ( "<span style=\"color:gray\">"+parent.getTranslation("Pause")+" |</span> "+parent.getTranslation("Continue"), command( "continue"), true, parent._hide.continue_jobscheduler, parent._confirm.continue_jobscheduler, 'continue the JobScheduler' );
      }
    } else {
      popup_builder.add_command ( parent.getTranslation("Pause")                              , command( "pause"                         ), state != "paused", parent._hide.pause_jobscheduler, parent._confirm.pause_jobscheduler, 'pause the JobScheduler' );
      popup_builder.add_command ( parent.getTranslation("Continue")                           , command( "continue"                      ), state == "paused", parent._hide.continue_jobscheduler, parent._confirm.continue_jobscheduler, 'continue the JobScheduler' );
    }
    popup_builder.add_command ( parent.getTranslation("Terminate")                        , "<terminate/>"                           , !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler' );
    popup_builder.add_command ( parent.getTranslation("Terminate within ~$secs",{sec:within}), "<terminate timeout='"+parent._scheduler._runtime_settings.terminate_timeout+"'/>", !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler' );
    popup_builder.add_command ( parent.getTranslation("Terminate and restart")            , "<terminate restart='yes'/>"             , !waiting_errno, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler' );
    popup_builder.add_command ( parent.getTranslation("Terminate and restart within ~$secs",{sec:within}), "<terminate restart='yes' timeout='"+parent._scheduler._runtime_settings.terminate_timeout+"'/>", !waiting_errno, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler' );
    
    popup_builder.add_bar();
    popup_builder.add_command ( parent.getTranslation("Abort immediately")                  , command( "abort_immediately"             ), true, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'abort the JobScheduler' );
    popup_builder.add_command ( parent.getTranslation("Abort immediately and restart")      , command( "abort_immediately_and_restart" ), true, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler' );
    
    if( cluster_element && (!parent._hide.terminate_jobscheduler || !parent._hide.restart_jobscheduler) ) {
      popup_builder.add_bar();
      popup_builder.add_command ( parent.getTranslation("Terminate cluster")                , "<terminate all_schedulers='yes'/>"              , !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler cluster' );
      popup_builder.add_command ( parent.getTranslation("Terminate cluster within ~$secs",{sec:within})    , "<terminate timeout='"+parent._scheduler._runtime_settings.terminate_timeout+"' all_schedulers='yes'/>" , !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler cluster' );
      popup_builder.add_command ( parent.getTranslation("Terminate and restart cluster")    , "<terminate restart='yes' all_schedulers='yes'/>", !waiting_errno, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler cluster' );
      popup_builder.add_command ( parent.getTranslation("Terminate and restart cluster within ~$secs",{sec:within})  , "<terminate restart='yes' timeout='"+parent._scheduler._runtime_settings.terminate_timeout+"' all_schedulers='yes'/>", !waiting_errno, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler cluster' );
      if( cluster_element.getAttribute( "exclusive" ) == "yes" ) {
        popup_builder.add_command ( parent.getTranslation("Terminate fail-safe")            , "<terminate continue_exclusive_operation='yes'/>", !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler' ); 
        popup_builder.add_command ( parent.getTranslation("Terminate fail-safe within ~$secs",{sec:within}), "<terminate continue_exclusive_operation='yes' timeout='"+parent._scheduler._runtime_settings.terminate_timeout+"'/>", !waiting_errno, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler' );
      }
    }
    
    if( window.createPopup == undefined ) {
      _popup_menu = popup_builder.show_selectbox_menu( elt );
    } else {
      _popup_menu = popup_builder.show_popup_menu();
    }
}


//--------------------------------------------------------------------------scheduler_extras__onclick

function scheduler_extras__onclick( elt )
{
    resetError();
    if( typeof window.parent.left_frame._response != 'object' ) throw new Error("Please wait until the window is completly loaded.");
    
    var popup_builder   = new Popup_menu_builder();
    
    //popup_builder.add_entry( parent.getTranslation("Documentation") + ' ' + parent.getTranslation("(en)") + '<img src="banner_english.gif" style="position:relative;top:3px;left:2px;"/>'     , "open_url( 'scheduler_home/doc/en/reference/index.xml', 'scheduler_documentation' )" );
    //popup_builder.add_entry( parent.getTranslation("Documentation") + ' ' + parent.getTranslation("(de)") + '<img src="banner_german.gif" style="position:relative;top:3px;left:2px;"/>'      , "open_url( 'scheduler_home/doc/de/reference/index.xml', 'scheduler_documentation' )" );
    popup_builder.add_entry( parent.getTranslation("Documentation") + ' ' + parent.getTranslation("(en)")     , "open_url( 'scheduler_home/doc/en/reference/index.xml', 'scheduler_documentation' )" );
    popup_builder.add_entry( parent.getTranslation("Documentation") + ' ' + parent.getTranslation("(de)")     , "open_url( 'scheduler_home/doc/de/reference/index.xml', 'scheduler_documentation' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("FAQ"), "open_url( 'https://kb.sos-berlin.com/x/JoBB', 'scheduler_wiki' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("Forum"), "open_url( 'https://sourceforge.net/p/jobscheduler/discussion/', 'scheduler_forum' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("Downloads"), "open_url( 'http://sourceforge.net/projects/jobscheduler/files/', 'scheduler_download' )" );
    popup_builder.add_entry( parent.getTranslation("Follow us on Twitter"), "open_url( 'http://twitter.com/#!/job_scheduler', 'scheduler_twitter' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("Release Notes"), "open_url( 'https://kb.sos-berlin.com/x/s4BB', 'scheduler_release_info' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("Home"), "open_url( 'http://www.sos-berlin.com/scheduler', 'scheduler_home' )" );
    //popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("Jira"), "open_url( 'https://change.sos-berlin.com/', 'scheduler_jira' )" );
    popup_builder.add_entry( parent.getTranslation("JobScheduler") + ' ' + parent.getTranslation("OTRS"), "open_url( 'http://www.sos-berlin.com/otrs/customer.pl?Lang=en', 'scheduler_otrs' )" );
    
    popup_builder.add_bar();
    popup_builder.add_entry( parent.getTranslation("Settings")      , "callErrorChecked('scheduler_settings__onclick')", parent.left_frame );
    
    var first_extra_urls = true;
    for(var entry in parent._extra_urls ) {
      if( first_extra_urls ) {
        first_extra_urls = false;
        popup_builder.add_bar();
      }
      popup_builder.add_entry ( entry , "open_url( '"+parent._extra_urls[entry]+"', 'extras_"+entry.replace(/[^a-zA-Z0-9_]/g,"_")+"' )" );
    }
    
    popup_builder.add_bar();
    popup_builder.add_entry( parent.getTranslation("About") + ' ' + parent.getTranslation("JobScheduler"), "parent.left_frame.showInfo()" );
    
    if( window.createPopup == undefined ) {
      _popup_menu = popup_builder.show_selectbox_menu( elt );
    } else {
      _popup_menu = popup_builder.show_popup_menu();
    }
}

//--------------------------------------------------------------------------------job_menu__onclick

function job_menu__onclick( job_name )
{   
    var popup_builder = new Popup_menu_builder();
    parent.left_frame._job_name = xml_encode(job_name);
    var job_element   = parent.left_frame._response.selectSingleNode( './/job[@path="' + parent.left_frame._job_name + '"]' );
    if( !job_element ) {
      var response    = parent._scheduler.executeSynchron( '<show_job job="' + parent.left_frame._job_name + '" max_orders="0" max_task_history="0"/>', false );
      if( response ) job_element = response.selectSingleNode('//job');
    }
    parent.left_frame._job_element      = job_element;
    var enabled       = job_element.getAttribute( "enabled" );
    enabled           = ( !enabled || enabled != "no" );
    var state         = job_element.getAttribute( "state" );
    var initialized   = (state && state != '' && state != 'not_initialized' && state != 'error' && enabled);
    var stopped       = (state == 'stopped' || state == "stopping");
    var order_job     = (job_element.getAttribute( "order" ) == "yes");
    var job_title     = job_element.getAttribute( "title" );
    //if( !job_title ) job_title = job_name.replace(/^\//,'');
    _obj_title        = title_encode(job_title);
    _obj_name         = xml_encode(job_name.replace(/^\//,''));
    var hot           = job_element.selectSingleNode('file_based/@file') ? 1 : 0;
    
    popup_builder.add_show_log( parent.getTranslation("Show log")        , "job=" + encodeComponent(job_name), "show_log_job_" + job_name.replace(/\//g,'_') );
    popup_builder.add_entry   ( parent.getTranslation("Show configuration"), "show_xml2('job', '"+parent.left_frame._job_name+"')", hot );
    
    var has_description = (job_element.getAttribute( "has_description" ) == "yes");
    popup_builder.add_entry   ( parent.getTranslation("Show documentation"), "show_job_desc()", has_description );
    popup_builder.add_entry   ( parent.getTranslation("Show dependencies") , "show_job_illustration()", true );
    popup_builder.add_entry   ( parent.getTranslation("Show start times"), "callErrorChecked('show_calendar','job')" );
    if( !parent._hide.start_job || !parent._hide.set_job_run_time || !parent._hide.stop_job || !parent._hide.unstop_job ) {
    	popup_builder.add_bar();
    }
    if( parent._scheduler._runtime_settings.start_next_period_enabled ) {
    	popup_builder.add_command ( parent.getTranslation("Start task unforced now"), "<start_job job='" + parent.left_frame._job_name + "' force='no'/>", (initialized  && !order_job), parent._hide.start_job, parent._confirm.start_job, 'start this job' );
    }
    popup_builder.add_command ( parent.getTranslation("Start task immediately"), "<start_job job='" + parent.left_frame._job_name + "'/>", (initialized && !order_job), parent._hide.start_job, parent._confirm.start_job, 'start this job' );
    popup_builder.add_entry   ( parent.getTranslation("Start task at") , "callErrorChecked('start_task_at')", (initialized && !order_job), parent._hide.start_job );
    popup_builder.add_entry   ( parent.getTranslation("Start task parametrized")  , "callErrorChecked('start_task')", (initialized && !order_job), parent._hide.start_job );
    popup_builder.add_entry   ( parent.getTranslation("Set run time")  , "callErrorChecked('set_run_time','job'," + hot + ")", initialized, parent._hide.set_job_run_time );
    popup_builder.add_command ( parent.getTranslation("Stop")          , "<modify_job job='" + parent.left_frame._job_name + "' cmd='stop'    />", (!stopped && enabled), parent._hide.stop_job, parent._confirm.stop_job, 'stop this job' );
    popup_builder.add_command ( parent.getTranslation("Unstop")        , "<modify_job job='" + parent.left_frame._job_name + "' cmd='unstop'  />", (stopped && enabled), parent._hide.unstop_job, parent._confirm.unstop_job, 'unstop this job' );
    
    //Makes only sense for API jobs, which runs in multiple process steps
    if( !parent._hide.end_or_continue_or_suspend_tasks_of_api_job ) {
    	popup_builder.add_bar();
    	popup_builder.add_command ( parent.getTranslation("End tasks")     , "<modify_job job='" + parent.left_frame._job_name + "' cmd='end'     />", enabled, parent._hide.end_or_continue_or_suspend_tasks_of_api_job, parent._confirm.end_or_continue_or_suspend_tasks_of_api_job, 'end the tasks' );
    	popup_builder.add_command ( parent.getTranslation("Suspend tasks") , "<modify_job job='" + parent.left_frame._job_name + "' cmd='suspend' />", enabled, parent._hide.end_or_continue_or_suspend_tasks_of_api_job, parent._confirm.end_or_continue_or_suspend_tasks_of_api_job, 'suspend the tasks' );
    	popup_builder.add_command ( parent.getTranslation("Continue tasks"), "<modify_job job='" + parent.left_frame._job_name + "' cmd='continue'/>", enabled, parent._hide.end_or_continue_or_suspend_tasks_of_api_job, parent._confirm.end_or_continue_or_suspend_tasks_of_api_job, 'continue the tasks' );
    }
    //popup_builder.add_bar();
    //popup_builder.add_command ( parent.getTranslation("Delete job")    , "<modify_job job='" + parent.left_frame._job_name + "' cmd='remove'/>", true, parent.getTranslation('Do you really want to delete this job?'), 'job|'+parent.left_frame._job_name );
    
    _popup_menu = popup_builder.show_popup_menu();
}

//-------------------------------------------------------------------------------task_menu__onclick

function task_menu__onclick( task_id )
{
    var popup_builder = new Popup_menu_builder();

    popup_builder.add_show_log( parent.getTranslation("Show log")        , "task=" + task_id, "show_log_task_" + task_id );
    if( !parent._hide.end_task_of_api_job || !parent._hide.kill_running_task ) {
    	popup_builder.add_bar();
    }
    //Makes only sense for API jobs, which runs in multiple process steps
    popup_builder.add_command ( parent.getTranslation("End")             , "<kill_task job='" + _job_name + "' id='" + task_id + "'/>", true, parent._hide.end_task_of_api_job, parent._confirm.end_task_of_api_job, 'end this task' );
    popup_builder.add_command ( parent.getTranslation("Kill immediately"), "<kill_task job='" + _job_name + "' id='" + task_id + "' immediately='yes'/>", true, parent._hide.kill_running_task, parent._confirm.kill_running_task, 'kill this task' );

    _popup_menu = popup_builder.show_popup_menu();
}


//-----------------------------------------------------------------------history_task_menu__onclick

function show_task_log( task_id )
{
    var show_log_command = "task=" + task_id;
    var window_name      = "show_log_task_" + task_id;
    /* solange es nur einen Eintrag im Menue gibt, wird dieser direkt ausgefuehrt
    var popup_builder = new Popup_menu_builder();
    popup_builder.add_show_log( "Show log"        , show_log_command, window_name );
    _popup_menu = popup_builder.show_popup_menu();
    */
    show_log_command = show_log_command.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
    window_name      = window_name.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
    show_log__onclick( show_log_command, window_name );
}


//------------------------------------------------------------------------------history_task_menu__onclick

function history_task_menu__onclick( task_id )
{   
    var popup_builder = new Popup_menu_builder();
    popup_builder.add_entry ( parent.getTranslation("Show log")  , "show_task_log('"+task_id+"')" );
    _popup_menu = popup_builder.show_popup_menu();
}


//-----------------------------------------------------------------------queued_task_menu__onclick

function kill_task_immediately( task_id )
{   
		if( !parent._hide.remove_enqueued_task ) {
			if( !parent._confirm.remove_enqueued_task || confirm(parent.getTranslation('Do you really want to delete this task?')) ) {
    		var xml_command = '<kill_task job="' + _job_name + '" id="' + task_id + '" immediately="yes"/>';
    		if( scheduler_exec( xml_command, false ) == 1 ) { 
        	set_timeout("parent.left_frame.update()",1);
    		}
    	}
    }
}

function queued_task_menu__onclick( task_id )
{   
		if( !parent._hide.remove_enqueued_task ) {
    	var popup_builder = new Popup_menu_builder();
    	popup_builder.add_command ( parent.getTranslation("Delete") , "<kill_task job='" + _job_name + "' id='" + task_id + "' immediately='yes'/>", true, parent._hide.remove_enqueued_task, parent._confirm.remove_enqueued_task, 'delete this task' );
    	_popup_menu = popup_builder.show_popup_menu();
    }
}


//------------------------------------------------------------------------------order_menu__onclick

function order_menu__onclick( job_chain, order_id, menu_caller )
{   
    parent.left_frame._order_id  = xml_encode(order_id);
    parent.left_frame._job_chain = xml_encode(job_chain);
    var job_chain_element = parent.left_frame._response.selectSingleNode( './/job_chain[@path="' + parent.left_frame._job_chain + '"]' );
    var job_chain_nodes   = new Array();
    var order_element     = null;
    var state             = null;
    var end_state         = null;
    var order_title       = null;
    var setback           = null;
    var suspended         = null;
    var occupied_http     = null;
    var hot               = 0;
    //var real_job_chain    = parent.left_frame._job_chain;
    
    if( job_chain_element ) {
      //job_chain_nodes     = job_chain_element.selectNodes( './/job_chain_node' );
      //if( !job_chain_nodes ) job_chain_nodes = new Array();
      order_element       = job_chain_element.selectSingleNode('.//order[@id="' + parent.left_frame._order_id + '"]');
    }
    if( !order_element ) {
      var response        = parent._scheduler.executeSynchron( '<show_order order="' + parent.left_frame._order_id + '" job_chain="' + window.parent.left_frame._job_chain + '"/>', false );
      if( response ) order_element = response.selectSingleNode('//order');
    }
    if( order_element ) {
      state               = order_element.getAttribute('state');
      end_state           = order_element.getAttribute('end_state');
      order_title         = order_element.getAttribute('title');
      setback             = order_element.getAttribute('setback');
      suspended           = order_element.getAttribute('suspended');
      occupied_http       = order_element.getAttribute('occupied_by_http_url');
      hot                 = (order_element.selectSingleNode('file_based/@file')) ? 1 : 0;
      //real_job_chain      = order_element.getAttribute('path').replace(/([^,]+),[^,]+$/,"$1");
      /*
      var job_chain_stack = order_element.selectSingleNode('order.job_chain_stack/order.job_chain_stack.entry');
      if( job_chain_stack ) {
        state             = job_chain_stack.getAttribute('state');
        var big_chain     = job_chain_stack.getAttribute('job_chain');
        job_chain_element = parent.left_frame._response.selectSingleNode( './/job_chain[@path="' + big_chain + '"]' );
        if( job_chain_element ) {
          job_chain_nodes     = job_chain_element.selectNodes( './/job_chain_node.job_chain[@next_state]' );
          if( !job_chain_nodes ) job_chain_nodes = new Array();
        }
      } */
    }
    if( !end_state ) end_state = "";
    //if( !order_title ) order_title = order_id.replace(/^\//,'');
    _obj_title = title_encode(order_title);
    _obj_name  = xml_encode(order_id.replace(/^\//,''));
    if( !occupied_http || occupied_http.search(/^http/) == -1 ) { 
    	occupied_http = ''; 
    } else {
      occupied_http = occupied_http.replace(/[\/]*$/,'') + '/show_log?';
    }
    
    var popup_builder = new Popup_menu_builder();
    if( menu_caller == 'blacklist' ) {
      popup_builder.add_show_log( parent.getTranslation("Show log")         , occupied_http + "job_chain=" + encodeComponent(job_chain) +
                                                   "&order=" + encodeComponent(order_id), "show_log_order_" + job_chain.replace(/\//g,'_') + "__" + order_id );
      popup_builder.add_command ( parent.getTranslation("Delete order")    , "<remove_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "'/>", true, parent._hide.remove_blacklist_order, parent._confirm.remove_blacklist_order, 'delete this order', 'job_chain|'+parent.left_frame._job_chain+'|order|'+parent.left_frame._order_id );
    } else {
      popup_builder.add_show_log( parent.getTranslation("Show log")         , occupied_http + "job_chain=" + encodeComponent(job_chain) +
                                                   "&order=" + encodeComponent(order_id), "show_log_order_" + job_chain.replace(/\//g,'_') + "__" + order_id );
      popup_builder.add_entry   ( parent.getTranslation("Show configuration"), "show_xml2('order','"+job_chain+","+order_id+"')", hot );
      popup_builder.add_entry   ( parent.getTranslation("Show start times") , "callErrorChecked('show_calendar','order')" );
      if( !parent._hide.start_order || !parent._hide.add_order || !parent._hide.set_order_state || !parent._hide.set_order_run_time || !parent._hide.suspend_order ||
      	!parent._hide.resume_order || !parent._hide.reset_order || (!hot && !parent._hide.remove_order) || !parent._hide.remove_setback) {
      	popup_builder.add_bar();
      }
      popup_builder.add_entry   ( parent.getTranslation("Start order now") , "callErrorChecked('start_order_at',1)", (suspended != "yes"), parent._hide.start_order );
      popup_builder.add_entry   ( parent.getTranslation("Start order at")  , "callErrorChecked('start_order_at',0)", (suspended != "yes"), parent._hide.start_order );
      popup_builder.add_entry   ( parent.getTranslation("Start order parametrized"), "callErrorChecked('start_order',0)", (suspended != "yes"), parent._hide.start_order );
      popup_builder.add_entry   ( parent.getTranslation("Add order")       , "callErrorChecked('add_order',0,'" + state + "','" + end_state + "')", true, parent._hide.add_order );
      popup_builder.add_entry   ( parent.getTranslation("Set order state") , "callErrorChecked('set_order_state','" + state + "','" + end_state + "'," + (setback != null) + "," + (suspended == "yes") + "," + hot + ")", (state != null), parent._hide.set_order_state );
      popup_builder.add_entry   ( parent.getTranslation("Set run time")    , "callErrorChecked('set_run_time','order'," + hot + ")", true, parent._hide.set_order_run_time );
      popup_builder.add_command ( parent.getTranslation("Suspend order")   , "<modify_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "' suspended='yes'/>", (suspended != "yes"), parent._hide.suspend_order, parent._confirm.suspend_order, 'suspend this order' );
      popup_builder.add_command ( parent.getTranslation("Resume order")    , "<modify_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "' suspended='no'/>", (suspended == "yes"), parent._hide.resume_order, parent._confirm.resume_order, 'resume this order' );
      popup_builder.add_entry   ( parent.getTranslation("Resume order parametrized"), "callErrorChecked('resume_order',0)", (suspended == "yes"), parent._hide.resume_order );
      popup_builder.add_command ( parent.getTranslation("Reset order")     , "<modify_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "' action='reset'/>", true, parent._hide.reset_order, parent._confirm.reset_order, 'reset this order' );
      if(!hot) {
      	popup_builder.add_command ( parent.getTranslation("Delete order")    , "<remove_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "'/>", true, parent._hide.remove_order, parent._confirm.remove_order, 'delete this order', 'job_chain|'+parent.left_frame._job_chain+'|order|'+parent.left_frame._order_id );
      }
      popup_builder.add_command ( parent.getTranslation("Remove setback")  , "<modify_order job_chain='" + parent.left_frame._job_chain + "' order='" + parent.left_frame._order_id + "' setback='no'/>", (setback != null), parent._hide.remove_setback, parent._confirm.remove_setback, 'remove the setback' );
    }
    _popup_menu = popup_builder.show_popup_menu();
} 


//------------------------------------------------------------------------------get_show_log_command

function get_show_log_command( job_chain, order_id, history_id, end_time )
{
    if( end_time ) {
      var show_log_command = "order=" + encodeComponent(order_id) +"&history_id=" + history_id;
    } else {
    	if(job_chain.substr(0,1) != "/") {
    	  job_chain = "/"+job_chain;
    	}
      var show_log_command = "job_chain=" + encodeComponent(job_chain) + "&order=" + encodeComponent(order_id);
    }
    return show_log_command.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
}


//------------------------------------------------------------------------------show_order_log

function show_order_log( job_chain, order_id, history_id, end_time )
{   
    var window_name  = "show_log_order_" + order_id + "__" + history_id;
    window_name      = window_name.replace( /\\/g, "\\\\" ).replace( /\"/g, "\\&quot;" );
    show_log__onclick( get_show_log_command( job_chain, order_id, history_id, end_time ), window_name );
}


//------------------------------------------------------------------------------get_stdout_stderr

function get_stdout_stderr( job_chain, order_id, history_id, end_time )
{
    _stdout_stderr = {stdout:null,stderr:null};
    var stdout_begin = parent._stdout_begin.replace(/^\s*/,"").replace(/\s*$/,"");
    var stdout_end   = parent._stdout_end.replace(/^\s*/,"").replace(/\s*$/,"");
    var stderr_begin = parent._stderr_begin.replace(/^\s*/,"").replace(/\s*$/,"");
    var stderr_end   = parent._stderr_end.replace(/^\s*/,"").replace(/\s*$/,"");
    var show_log_command;
    var responseText;
    if((stdout_begin && stdout_end) || (stderr_begin && stderr_end)) {
    	if( end_time ) {
    	  show_log_command = get_show_log_command( job_chain, order_id, history_id, end_time );
    	  show_log_command = parent._scheduler._engine_cpp_url+"show_log?"+show_log_command;
    	  responseText = parent._scheduler.executeGet(show_log_command);
    	} else {
    	  show_log_command = '<show_order job_chain="' + xml_encode(job_chain) + '" order="' + xml_encode(order_id) + '" what="log"/>';
    	  var response = parent._scheduler.executeSynchron(show_log_command, false);
    	  responseText = response.xml;
    	}
    	var pattern;
      var result;
      if(stdout_begin && stdout_end) {
      	pattern = new RegExp( stdout_begin + "(.*)[\r\n]+(([\r\n]|.)*)" + stdout_end, "gm" );
      	result = pattern.exec(responseText);
      	if(result && result[2]) {
      		_stdout_stderr.stdout_header = result[1].replace(/\s*<\/span>\s*/, "").replace(/^\s*/,"");
      		_stdout_stderr.stdout = result[2].replace(/<span[^\)]+\)\s*/gm, "").replace(/\s*<\/span>([\s\r\n]+)*/gm, "$1");
        }
      }
      if(stderr_begin && stderr_end) {
      	pattern = new RegExp( stderr_begin + "(.*)[\r\n]+(([\r\n]|.)*)" + stderr_end, "gm" );
      	result = pattern.exec(responseText);
      	if(result && result[2]) {
      		_stdout_stderr.stderr_header = result[1].replace(/\s*<\/span>\s*/, "").replace(/^\s*/,"");
      		_stdout_stderr.stderr = result[2].replace(/<span[^\)]+\)\s*/gm, "").replace(/\s*<\/span>([\s\r\n]+)*/gm, "$1");
        }
      }
    }
}


//------------------------------------------------------------------------------order_history_menu__onclick

function order_history_menu__onclick( job_chain, order_id, history_id, end_time )
{   
    var popup_builder = new Popup_menu_builder();
    popup_builder.add_entry ( parent.getTranslation("Show log")  , "show_order_log('"+job_chain+"', '"+order_id.replace( /\\/g, "\\\\" )+"', '"+history_id+"', '"+end_time+"')" );
    //if( end_time ) {
    try {
    	var stdout_stderr = get_stdout_stderr( job_chain, order_id, history_id, end_time );
    	if( _stdout_stderr.stdout ) {
      	popup_builder.add_entry ( parent.getTranslation("Show stdout"), "open_url('show_stdout_stderr.html#=stdout', '_blank')" );
    	}
    	if( _stdout_stderr.stderr ) {
      	popup_builder.add_entry ( parent.getTranslation("Show stderr"), "open_url('show_stdout_stderr.html#=stderr', '_blank')" );
    	}
    } catch(x) {
    	showError( x );
    }
    //}
    _popup_menu = popup_builder.show_popup_menu();
}

//------------------------------------------------------------------------------job_chain_menu__onclick

function job_chain_menu__onclick( job_chain, orders, big_chain )
{   
    var popup_builder = new Popup_menu_builder();
    big_chain  = ( typeof big_chain == "number" && big_chain > 1 ) ? 1 : 0;
    _obj_title = '';
    parent.left_frame._order_id  = '';
    parent.left_frame._job_chain = xml_encode(job_chain);
    var job_chain_element        = parent.left_frame._response.selectSingleNode( './/job_chain[@path="' + parent.left_frame._job_chain + '"]' );
    if( !job_chain_element ) {
      //var response               = parent._scheduler.executeSynchron( '<show_job_chain job_chain="' + parent.left_frame._job_chain + '" max_order_history="0" max_orders="999" what="job_chain_orders blacklist"/>', false );
      var response               = parent._scheduler.executeSynchron( '<show_job_chain job_chain="' + parent.left_frame._job_chain + '" max_order_history="0"/>', false );
      if( response ) job_chain_element = response.selectSingleNode('//job_chain');
    }
    var hot                      = job_chain_element.selectSingleNode('file_based/@file');
    
    
    popup_builder.add_entry ( parent.getTranslation("Show configuration")  , "show_xml2('job_chain', '"+job_chain+"')", hot );
    popup_builder.add_entry ( parent.getTranslation("Show dependencies")   , "show_job_chain_illustration()", !big_chain );
    popup_builder.add_entry ( parent.getTranslation("Show start times")  , "callErrorChecked('show_calendar','job_chain')", !big_chain );
    
    if(!parent._hide.add_order) {
    	popup_builder.add_bar();
    }
    popup_builder.add_entry ( parent.getTranslation("Add order")           , "callErrorChecked('add_order'," + big_chain + ")", true, parent._hide.add_order );
    
    /*if( big_chain == 0) {
    	var tempOrders               = job_chain_element.selectNodes('//order[not(file_based/@file)]');
      popup_builder.add_entry ( parent.getTranslation("Delete orders")           , "callErrorChecked('delete_orders')", (tempOrders.length > 0) );
    }*/

    	var state             = job_chain_element.getAttribute( "state" );
      var command           = function( cmd ) { return "<job_chain.modify job_chain='"+parent.left_frame._job_chain+"' state='"+cmd+"'/>"; }
      if(!parent._hide.stop_job_chain || !parent._hide.unstop_job_chain) {
      	popup_builder.add_bar();
      }
      popup_builder.add_command ( parent.getTranslation("Stop")            , command('stopped'), state != 'stopped', parent._hide.stop_job_chain, parent._confirm.stop_job_chain, 'stop this job chain'  );
      popup_builder.add_command ( parent.getTranslation("Unstop")          , command('running'), state == 'stopped', parent._hide.unstop_job_chain, parent._confirm.unstop_job_chain, 'unstop this job chain'  );

    //popup_builder.add_command ( parent.getTranslation("Delete job chain")  , "<remove_job_chain job_chain='" + parent.left_frame._job_chain + "'/>", orders==1, parent.getTranslation('Do you really want to delete this job chain?'), 'job_chain|'+parent.left_frame._job_chain );
    
    _popup_menu = popup_builder.show_popup_menu();
}

//--------------------------------------------------------------------------job_chain_node_menu__onclick

function job_chain_node_menu__onclick( state, job_chain )
{   
    var job_chain_elem = null;
    var job_element    = null;
    var job_name       = '';
    var hot            = false;
    var enabled        = true;
    var job_stopped    = false;
    var node_action    = null;
    var response       = parent._scheduler.executeSynchron( '<show_job_chain job_chain="' + job_chain + '" what="job_chain_jobs" max_orders="0" max_order_history="0"/>', false );
    if( response ) {
     job_chain_elem    = response.selectSingleNode('//job_chain');
    }
    if( job_chain_elem ) {
    	var node_elem    = job_chain_elem.selectSingleNode('job_chain_node[@state="'+state+'"]'); 
      job_element      = node_elem.selectSingleNode("job");
      node_action      = node_elem.getAttribute( "action" );
    }
    if( job_element ) {
      	job_name       = job_element.getAttribute('path');
      	hot            = job_element.selectSingleNode('file_based/@file');    
      	enabled        = ( !job_element.getAttribute( "enabled" ) || job_element.getAttribute( "enabled" ) != "no" );
        job_stopped    = (job_element.getAttribute( "state" ) == 'stopped' || job_element.getAttribute( "state" ) == "stopping");
    }
    
    var popup_builder = new Popup_menu_builder();
    
    if(!parent._hide.stop_job_chain_node || !parent._hide.unstop_job_chain_node || !parent._hide.skip_job_chain_node || !parent._hide.unskip_job_chain_node) {
    
    	var command        = function( cmd ) { return "<job_chain_node.modify action='"+cmd+"' job_chain='"+job_chain+"' state='"+state+"'/>"; }
    	var undo_title     = parent.getTranslation("Unstop") + " | " + parent.getTranslation("Unskip");
    	var hide_command   = false;
    	var confirm_command = false;
    	var confirm_msg    = '';
    	switch( node_action ) {
      	case 'stop'       : undo_title = parent.getTranslation("Unstop") + " <span style=\"color:gray\">| " + parent.getTranslation("Unskip") + "</span>"; 
      											hide_command = parent._hide.unstop_job_chain_node;
      											confirm_command = parent._confirm.unstop_job_chain_node;
      											confirm_msg = 'unstop this job chain node';
      											break;
      	case 'next_state' : undo_title = "<span style=\"color:gray\">" + parent.getTranslation("Unstop") + " |</span> " + parent.getTranslation("Unskip");
      											hide_command = parent._hide.unskip_job_chain_node;
      											confirm_command = parent._confirm.unskip_job_chain_node;
      											confirm_msg = 'unskip this job chain node';
      											break;
    	}
    	
    	popup_builder.add_command ( parent.getTranslation("Stop node")    , command('stop'),       node_action != 'stop', parent._hide.stop_job_chain_node, parent._confirm.stop_job_chain_node, 'stop this job chain node' );
    	popup_builder.add_command ( parent.getTranslation("Skip node")    , command('next_state'), node_action != 'next_state', parent._hide.skip_job_chain_node, parent._confirm.skip_job_chain_node, 'skip this job chain node' );
    	if( undo_title ) popup_builder.add_command ( undo_title, command('process'),   node_action != null, hide_command, confirm_command, confirm_msg );
    } 
    if( job_name ) {
      popup_builder.add_bar();
      popup_builder.add_entry   ( parent.getTranslation("Show configuration"), "show_xml2('job', '"+job_name+"')", hot );
      popup_builder.add_command ( parent.getTranslation("Stop job")          , "<modify_job job='" + job_name + "' cmd='stop'    />", (!job_stopped && enabled), parent._hide.stop_job, parent._confirm.stop_job, 'stop this job' );
      popup_builder.add_command ( parent.getTranslation("Unstop job")        , "<modify_job job='" + job_name + "' cmd='unstop'  />", (job_stopped && enabled), parent._hide.unstop_job, parent._confirm.unstop_job, 'unstop this job' );
    }
    _popup_menu = popup_builder.show_popup_menu();
}

//--------------------------------------------------------------------------cluster_member__onclick

function cluster_member__onclick( cluster_member_id )
{
    if( cluster_member_id+"" != "" )
    {
        var popup_builder = new Popup_menu_builder();
        var is_dead = window.parent.left_frame._response.selectSingleNode( 'spooler/answer//cluster_member[ @cluster_member_id="' + cluster_member_id + '" ]' ).getAttribute( "dead" ) == "yes";
        var this_scheduler = (cluster_member_id == window.parent.left_frame._response.selectSingleNode( "spooler/answer//cluster" ).getAttribute( "cluster_member_id" )) ? "this_scheduler='yes'" : "";
        
        if( is_dead )
        {
            popup_builder.add_command( parent.getTranslation("Delete entry")    , "<terminate cluster_member_id='" + xml_encode( cluster_member_id ) + "' delete_dead_entry='yes' />", parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'delete the dead entry' );
        }
        else
        {
            popup_builder.add_command( parent.getTranslation("Terminate")       , "<terminate cluster_member_id='" + xml_encode( cluster_member_id ) + "' " + this_scheduler + " />", true, parent._hide.terminate_jobscheduler, parent._confirm.terminate_jobscheduler, 'terminate the JobScheduler' );
            popup_builder.add_command( parent.getTranslation("Restart")         , "<terminate cluster_member_id='" + xml_encode( cluster_member_id ) + "' restart='yes' " + this_scheduler + " />", true, parent._hide.restart_jobscheduler, parent._confirm.restart_jobscheduler, 'restart the JobScheduler' );
        }
 
        _popup_menu = popup_builder.show_popup_menu();
    }
}


//--------------------------------------------------------------------------schedule_menu__onclick

function schedule_menu__onclick( schedule, substitute, used, hot, title )
{   
    var schedule_title = (title) ? title : schedule.replace(/^\//,'');
    _obj_title  = xml_encode(schedule_title);
    parent.left_frame._schedule   = xml_encode(schedule);
    parent.left_frame._substitute = xml_encode(substitute);
    var popup_builder = new Popup_menu_builder();
    popup_builder.add_entry ( parent.getTranslation("Show configuration") , "show_xml2('schedule', '"+schedule+"')", (hot-1) );
    popup_builder.add_entry ( parent.getTranslation("Add substitute")     , "callErrorChecked('set_run_time','add_substitute',"+(hot-1)+")", true, parent._hide.add_schedule );
    popup_builder.add_entry ( parent.getTranslation("Edit schedule")      , "callErrorChecked('set_run_time','schedule',"+(hot-1)+")", true, parent._hide.modify_schedule );
    //popup_builder.add_bar();
    //popup_builder.add_command ( parent.getTranslation("Delete schedule")  , "<schedule.remove schedule='" + parent.left_frame._schedule + "'/>", used == 1, parent.getTranslation('Do you really want to delete this schedule?'), 'schedule|'+parent.left_frame._schedule );
    _popup_menu = popup_builder.show_popup_menu();
}


//-----------------------------------------------------------------------------------------open_url

function open_url( url, window_name, with_hash, features )
{   
    if( features == undefined  )  features  = "";
    if( with_hash == undefined )  with_hash = false;
    if( parent._scheduler._ie  ) features = _open_url_features;
    if( with_hash && parent._server_settings && url.search(/#/) == -1 ) url += parent.location.hash;  
    
    var my_window = parent.open( url, window_name, '' );
    my_window.focus();
    if( window_name && window_name != '_blank' &&  parent._scheduler )  parent._scheduler._dependend_windows[ window_name ] = my_window;
}


//-----------------------------------------------------------------------------------------map_url

function map_url( host, port, elem )
{ 
  var host_port = host+":"+port;
  if( typeof parent._url_aliase["http://"+host_port+"/"] == "string" ) {
    host_port = parent._url_aliase["http://"+host_port+"/"];
  } else {
    if( typeof parent._url_aliase["http://"+host_port] == "string" ) {
      host_port = parent._url_aliase["http://"+host_port];
    } else {
      if( typeof parent._url_aliase[host_port] == "string" ) {
        host_port = parent._url_aliase[host_port];
      }
    }
  }
  if( host_port.search(/^http:\/\//) == -1 ) host_port = "http://"+host_port;
  if( host_port.search(/\/$/) == -1 ) host_port += "/";
  if( elem ) elem.title = host_port;
  return host_port;
}

//-----------------------------------------------------------------------------open_remote_scheduler

function open_remote_scheduler( host, port )
{ 
  if( typeof port == 'undefined' || port == '' ) {
    host = host.replace(/^http:\/\//,'');
    port = host.replace(/^[^:]+:(.+)$/,"$1");
    host = host.replace(/^([^:]+):.+/,"$1");
  }
  var host_port = map_url( host, port, null ); 
  open_url( host_port, host_port.replace(/[^a-zA-Z0-9_]/g,"_") );
}


//---------------------------------------------------------------------------------------xml_encode

function xml_encode( text )
{
    if( text == null )  return "";
    if( text.toString().search(/&(amp|lt|gt|quot|#039);/) > -1 ) return text;
    return text.toString().replace( /&/g, "&amp;" ).replace( /</g, "&lt;" ).replace( />/g, "&gt;" ).replace( /\"/g, "&quot;" ).replace( /\'/g, "&#039;" );
}


//---------------------------------------------------------------------------------------title_encode

function title_encode( text )
{
    if( text == null )  return "";
    if( text.toString().search(/(www.|https?:\/\/)/) > -1 ) return text;
    //TODO generate links
    return text;
}


//-----------------------------------------------------------------------------------scheduler_init
function scheduler_init()
{
    try {
      Popup_menu_builder.prototype.add_command  = Popup_menu_builder__add_command;
      Popup_menu_builder.prototype.add_show_log = Popup_menu_builder__add_show_log;
    }
    catch( x ) {
      return showError( x );
    }
}


//-----------------------------------------------------------------------------------scheduler_exec
function scheduler_exec( xml, with_modify_datetime, with_add_path, with_all_errors )
{
    try {
      resetError();
      var ret = parent._scheduler.executeSynchron( xml, with_modify_datetime, with_add_path, with_all_errors );
      // https://change.sos-berlin.com/browse/JOC-40
      if( xml.search(/^<modify_order /i) > -1 ) {
      	var error_element = ret.selectSingleNode( "spooler/answer/ERROR" ); 
      	if( error_element && error_element.getAttribute( "code" ).search(/SCHEDULER-379/i) > -1 ) {
      		return 379;
      	}
      }
      return 1;
    }
    catch( x ) {
      showError( x );
      return 0;
    }
}

function exec_modify_order(xml_command) {
		var ret = scheduler_exec( xml_command, false );
    if( ret == 1 ) {
    	set_timeout("parent.left_frame.update()",1);
    }
    else if(ret == 379) {  // https://change.sos-berlin.com/browse/JOC-40
      set_timeout("parent.left_frame.update()",2500);
    }
}


Number.prototype.toHex = function( max_length )
{
    var result = "";
    var hex    = "0123456789ABCDEF";
    var value  = this;
    
    if( max_length == undefined )  max_length = 1

    do
    {
        var digit = value % 16;
        if( digit < 0 )  digit += 15;
        result = hex.substring( digit, digit + 1 ) + result;
        value >>>= 4;
    }
    while( value != 0  ||  result.length < max_length );

    return result;
}


String.prototype.hex2bin = function() {
	
	  var tmp = "";
	  var hex = this;
	  if( hex.search(/[^0123456789abcdefABCDEF]/) > -1 ) { return hex; }
	  if( hex.length % 2 != 0 ) { return hex; }
	  while( hex.length > 1 ) {
        tmp += unescape( "%" + hex.substr( 0, 2 ) );
        hex = hex.slice( 2 );
    }
	  return tmp;
}


String.prototype.ishex = function() {
	
	  var hex = this;
	  if( hex.search(/[^0123456789abcdefABCDEF]/) > -1 ) { return false; }
	  if( hex.length % 2 != 0 ) { return false; }
	  return true;
}

String.prototype.bin2hex = function() {
	
	  var tmp = "";
	  var str = this;  
	  var sLength = this.length;
	  for( var i = 0; i < sLength; i++ ) { 
	      tmp += str.charCodeAt(i).toString(16).replace(/^([\da-f])$/,"0$1");
	  }
	  return tmp;
}

String.prototype.right = function(num) {
	
	  return this.substring(this.length-num); 
}


String.prototype.padLeftShlash = function() {
	  
	  if(this.substr(0,1) != '/') return '/'+this;
	  return this; 
}


Date.prototype.getISODate = function() {
	  
	  return this.getFullYear()+'-'+String('0'+(this.getMonth()+1)).right(2)+'-'+String('0'+this.getDate()).right(2);
}


//-------------------------------------------------------------callback for sort array case insensitive
function case_insensitive(a,b)
{
  var c = String(a).toUpperCase();
  var d = String(b).toUpperCase();
  if (c > d) return 1
  if (c < d) return -1
  return 0;
}

//------------------------------------------------------------------------------set_timeout
function set_timeout( js, delay, debug )
{
    if( typeof debug == 'undefined' ) debug = 4;
    if( parent._scheduler._runtime_settings.debug_level >= debug ) {
        var fname  = (set_timeout.caller) ? set_timeout.caller.toString().match(/^function ([a-zA-Z0-9_]+)/)[1] : 'anonymous';
        parent._scheduler.logger(debug,'DELAYED EXEC ('+(delay/1000)+'s): ' + js + ' called by ' + fname + ' in ' + self.name );
    }
    return window.setTimeout( js, delay );
}


//------------------------------------------------------------------------------basename
function basename( path )
{   
    return path.replace(/\\/g,'/').replace( /.*\//, '' );
}

//------------------------------------------------------------------------------dirname
function dirname( path )
{   
    path = path.replace(/\\/g,'/');
    if( path.search(/\//) == -1 ) {
      return '';
    } else {
      path = path.replace(/\/[^\/]*$/, '');
      return (path=='') ? '/' : path;
    } 
}

//------------------------------------------------------------------------------encodeComponent
function encodeComponent( s )
{
    s = encodeURIComponent(s);
    return s.replace(/#/g,escape('#'));
}


//------------------------------------------------------------------------------osNewline
function osNewline( s )
{   
    return s.replace(/\r/g,""); 
}  

//-------------------------------------------------------------------------------------------------