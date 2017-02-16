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

// $Id$

//----------------------------Initialising of global vars which are set in the custom.js file
var _disable_cookie_settings    = false;
var _update_periodically        = false;
var _update_seconds             = 5;
var _update_incl_hot_folders    = false;
var _show_card                  = 'jobs';
var _checkbox_states            = new Object();
var _radio_states               = new Object();
var _select_states              = new Object();
var _view                       = new Object();
var _left_frame_update          = true;
var _max_orders                 = 10;
var _max_last_activities        = 30;
var _max_order_history          = 50;
var _max_task_history           = 10;
var _description_lang           = 'en';
var _terminate_timeout          = 60;
var _task_terminate_timeout     = 10;
var _start_at_default_is_now    = true;
var _start_next_period_enabled  = false;
var _debug_level                = 0;
var _extra_items                = new Object();
_extra_items.monitor            = false;
_extra_items.configuration      = false;
var _extra_urls                 = new Object();
var _url_aliase                 = new Object();
var _sos_lang                   = '';
var _job_filter                 = new Object();
var _job_chain_filter           = new Object();
var _order_filter               = new Object();
var _project_filter             = new Object();
var _active_job_filter          = -1;
var _active_job_chain_filter    = -1;
var _active_order_filter        = -1;
var _active_project_filter      = -1;
var _stdout_begin               = "";
var _stdout_end                 = "";
var _stderr_begin               = "";
var _stderr_end                 = "";
var _display_last_activities_tab = false;
var _timezones									= {};
var _timezone_preselected       = 'local';
var _hide                       = {
	'stop_job_chain':false, 'unstop_job_chain':false, 
	'stop_job_chain_node':false, 'unstop_job_chain_node':false,
  'skip_job_chain_node':false, 'unskip_job_chain_node':false, 
  'stop_job':false, 'unstop_job':false, 'start_job':false, 
  'start_order':false, 'add_order':false, 'reset_order':false, 
  'set_order_state':false, 'suspend_order':false, 
  'resume_order':false, 'remove_order':false,
  'remove_blacklist_order':false, 'remove_setback':false, 
  'kill_running_task':false, 'remove_enqueued_task':false,
  'set_job_run_time':false, 'set_order_run_time':false,
  'end_or_continue_or_suspend_tasks_of_api_job':true, 
  'end_task_of_api_job':false, 'terminate_jobscheduler':false,
  'pause_jobscheduler':false, 'continue_jobscheduler':false,
  'restart_jobscheduler':false, 'add_schedule':false,
  'modify_schedule':false
}
var _confirm                    = {
  'stop_job_chain':false, 'unstop_job_chain':false, 
  'stop_job_chain_node':false, 'unstop_job_chain_node':false, 
  'skip_job_chain_node':false, 'unskip_job_chain_node':false, 
  'stop_job':false, 'unstop_job':false, 'start_job':false, 
  'start_order':false, 'add_order':false, 'reset_order':false, 
  'set_order_state':false, 'suspend_order':false, 
  'resume_order':false, 'remove_order':true, 
  'remove_blacklist_order':true, 'remove_setback':false, 
  'kill_running_task':false, 'remove_enqueued_task':true,
  'end_or_continue_or_suspend_tasks_of_api_job':false, 
  'end_task_of_api_job':false, 'terminate_jobscheduler':false,
  'pause_jobscheduler':false, 'continue_jobscheduler':false,
  'restart_jobscheduler':false, 'add_schedule':false,
  'modify_schedule':false
}

 



//--------------------------------------------------------------------------custom.js per hash  
var _server_settings            = ( location.hash && location.hash.length > 1 && (location.hash.substr(1).replace(/[_a-zA-Z0-9.~()-]/g,'').length == 0));
var base_url                    = ( location.href + "" ).replace( /\/[^\/]*$/, "/" );
var customJsPath                = "";
if (base_url.toLowerCase().indexOf("/jobscheduler/joc/") >= 0) {
   customJsPath = "../engine-cpp/"; 
}
document.writeln('<script type="text/javascript" src="'+customJsPath+'scheduler_data/custom/custom.js"></sc'+'ript>');
if( _server_settings && location.hash.substr(1) != 'custom') {
  document.writeln('<script type="text/javascript" src="'+customJsPath+'scheduler_data/custom/'+encodeURIComponent(location.hash.substr(1))+'.js"></sc'+'ript>');
}


//------------------------------------------------------------------------------getTranslation
var _lang_file_exists           = false;
var _translations               = new Object();
var _jobscheduler_menu          = new Object();

function getTranslation( s, args )
{   
    if(!_lang_file_exists && typeof args != 'object' ) return s;
    if( _lang_file_exists && typeof _translations[s] == 'string' ) s = _translations[s];
    if( typeof args == 'object' )  {
      for( var entry in args ) {
        s = s.replace(new RegExp('\\$'+entry,'g'),args[entry]);
      }
    }
    return s;
}
