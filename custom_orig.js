/*****************************************************************
*   For user-defined settings
*   The following variables are defaults when the site is loaded
*   but can be overwritten via cookies.
******************************************************************/


/*****************************************************************
*  If 'false' then almost all following variables are stored in 
*  cookies, so that these settings are only default values and 
*  will be overwritten via the cookies.
*  If 'true' then only filters are read from cookies whereat the
*  defined correspondent filters in this file have priority.
*/
_disable_cookie_settings = false;


/*****************************************************************
*  Set the interface language. 
*  A language file scheduler_lang_[_sos_lang].js must exist.
*  If this value is empty then no language file is tried to include 
*  and english is used.
*/
_sos_lang            = '';


/*****************************************************************
*  If 'true' then the periodically update is on (default = false).
*/
_update_periodically     = false;


/*****************************************************************
*  Interval between the updates in seconds (default = 5).
*/
_update_seconds          = 5;


/*****************************************************************
*  You can switch between the tabs 'jobs', 'job_chains',
*  'process_classes' and 'last_activities' 
*  (default = 'jobs') as the beginning view.
*/
_show_card               = 'jobs';


/*****************************************************************
*  For some tabs you can set the default view mode (list or tree).
*/
_view.jobs               = 'list';
_view.job_chains         = 'list';
_view.orders             = 'list';
_view.schedules          = 'list';


/*****************************************************************
*  You can specify which checkboxes are on (true) or off (false).
*  Defaults: 'show order jobs' and 'show tasks' are on and the
*  others are off.
*/
_checkbox_states.show_tasks_checkbox            = true;
_checkbox_states.show_job_chain_orders_checkbox = true;
_checkbox_states.show_job_chain_jobs_checkbox   = false;
_checkbox_states.show_order_history_checkbox    = false;
_checkbox_states.show_task_error_checkbox       = false;
_checkbox_states.show_error_checkbox            = false;


/*****************************************************************
*  You can specify the value of some radios.
*  For 'last_activities_radios' are 'all', 'orders' and 'tasks'
*  possible values (Default: 'all').
*/
_radio_states.last_activities_radios = 'all';


/*****************************************************************
*  You can specify the value of some selectboxes.
*  For 'show_jobs_select' are 'all', 'standalone' and 'order'
*  possible values (Default: 'all').
*  For 'jobs_state_select' are 'all', 'running', 'stopped',
*  'running_or_stopped' and 'other' possible values (Default: 'all).
*/
_select_states.show_jobs_select      = 'all';
_select_states.jobs_state_select     = 'all';
       

/*****************************************************************
*  You can set how much orders are shown in the job chains if the
*  checkbox 'show orders' is checked. The default is 10.
*/
_max_orders              = 10;


/*****************************************************************
*  You can set how much orders and tasks are shown in the last
*  activities. The default is 30.
*/
_max_last_activities     = 30;


/*****************************************************************
*  Per default the last 50 entries are shown from the orders history
*  and last 10 from the tasks history respectively. 
*  You can set another value.
*/
_max_order_history       = 50;
_max_task_history        = 10;


/*****************************************************************
*  If you have a cluster with backup or distributed Job Schedulers
*  then you can terminate the cluster with a timeout in seconds.
*/
_terminate_timeout       = 60;


/*****************************************************************
*  In 'start task/order at'-Dialogs are set the start time equals
*  'now' per default. You can omit this. 
*/
_start_at_default_is_now = true;


/*****************************************************************
*  Enables job menu item 'Start task unforced now'. 
*/
_start_next_period_enabled  = false;


/*****************************************************************
*  level for debugging: 0-9
*/
_debug_level             = 0;


/*****************************************************************
*  You can add two items to the popup menu of the extras button:
*  monitor and configuration. These functons are deprecated.
*/
_extra_items.monitor       = false;
_extra_items.configuration = false;


/*****************************************************************
*  You can add arbitrary URLs to the popup menu of the extras
*  button in the form:
*  _extra_urls[ 'display name' ] = 'url';
*/


/*****************************************************************
*  You can set an URL mapping for the remote schedulers if i.e. an
*  Apache proxy configuration is used. Set an alias in the form: 
*  _url_aliase[ 'remote_host:remote_port' ] = 'url';
*/


/*****************************************************************
*  You can set filters for jobs, job chains and order in the form 
*  _job_filter[ 'filter name' ]       = 'regular expression';
*  _job_chain_filter[ 'filter name' ] = 'regular expression';
*  _order_filter[ 'filter name' ]     = 'regular expression';
*  or use 
*  _project_filter[ 'filter name' ]   = 'regular expression';
*  which is effective for jobs, job chains and orders simultaneously
*
*  A selectbox will be displayed with the filter names.
*  To set which selectbox option is selected you can use
*  _active_job_filter                 = 'name of the filter';
*  _active_job_chain_filter           = 'name of the filter';
*  _active_order_filter               = 'name of the filter';
*  or 
*  _active_project_filter             = 'name of the filter';  
*/

