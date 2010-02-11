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

var Scheduler_Filter                            = function(){}
Scheduler_Filter.debug_level                    = 0;
Scheduler_Filter.scheduler                      = null;
Scheduler_Filter.is_inherited_scheduler         = true;
Scheduler_Filter.div_error                      = null;
Scheduler_Filter.xmlJobs                        = null;                         
Scheduler_Filter.xmlJobChains                   = null;                         
Scheduler_Filter.xmlOrders                      = null;                         
Scheduler_Filter.jobs                           = null;
Scheduler_Filter.job_chains                     = null;
Scheduler_Filter.orders                         = null;
Scheduler_Filter.form                           = null;
Scheduler_Filter.table_jobs                     = null;
Scheduler_Filter.table_job_chains               = null;
Scheduler_Filter.table_orders                   = null;
Scheduler_Filter.display_attribute_jobs         = 'path';
Scheduler_Filter.display_attribute_job_chains   = 'path';
Scheduler_Filter.display_attribute_orders       = 'path';
Scheduler_Filter.tabs                           = {'jobs':'tab_1','job_chains':'tab_2','orders':'tab_3'};

Scheduler_Filter.cookie_name_jobs_general       = 'job_filters';
Scheduler_Filter.cookie_name_jobs_active        = 'job_filter_active';
Scheduler_Filter.cookie_name_jobs_prefix        = 'job_filter_';

Scheduler_Filter.cookie_name_job_chains_general = 'job_chain_filters';
Scheduler_Filter.cookie_name_job_chains_active  = 'job_chain_filter_active';
Scheduler_Filter.cookie_name_job_chains_prefix  = 'job_chain_filter_';

Scheduler_Filter.cookie_name_orders_general     = 'order_filters';
Scheduler_Filter.cookie_name_orders_active      = 'order_filter_active';
Scheduler_Filter.cookie_name_orders_prefix      = 'order_filter_';

Scheduler_Filter.cookie_jobs_general            = '';
Scheduler_Filter.cookie_jobs_insert_id          = 1;

Scheduler_Filter.cookie_job_chains_general      = '';
Scheduler_Filter.cookie_job_chains_insert_id    = 1;

Scheduler_Filter.cookie_orders_general          = '';
Scheduler_Filter.cookie_orders_insert_id        = 1;

Scheduler_Filter.active_jobs_filter             = '';
Scheduler_Filter.active_job_chains_filter       = '';
Scheduler_Filter.active_orders_filter           = '';

Scheduler_Filter.ie                             = (navigator.appVersion.match(/\bMSIE\b/));
Scheduler_Filter.ie8                            = (Scheduler_Filter.ie && navigator.appVersion.match(/\bMSIE\s+(\d+)/)[1] > 7);
Scheduler_Filter.pattern                        = {'jobs':'','job_chains':'','orders':''};

/***/
window.onload = function(){
  
  if(window.name == "" ){ window.name = "scheduler_filter_administration";}
  if(Scheduler_Filter.scheduler == null) {  
    if(window.opener && window.opener._scheduler){
      Scheduler_Filter.scheduler = window.opener._scheduler;
    } 
    else {
      Scheduler_Filter.scheduler              = new Scheduler();
      Scheduler_Filter.is_inherited_scheduler = false;
    }
  }
  Scheduler_Filter.div_error        = document.getElementById('div_error');
  Scheduler_Filter.form             = document.getElementById('sos_form');
  Scheduler_Filter.table_jobs       = document.getElementById('table_filters_jobs');
  Scheduler_Filter.table_job_chains = document.getElementById('table_filters_job_chains');        
  Scheduler_Filter.table_orders     = document.getElementById('table_filters_orders');        
  
  try{
    if(1*Scheduler_Filter.debug_level > 0){
      document.getElementById('debug').innerHTML = document.cookie.replace(/;/g,'<br />');
    }
  }
  catch(x){}
  
  var start_tab = 'jobs';
  if(Scheduler_Filter.scheduler._show_card == 'job_chains' || Scheduler_Filter.scheduler._show_card == 'orders' ){
    start_tab = Scheduler_Filter.scheduler._show_card;
  }
  Scheduler_Filter.tab_control = new SOS_Tab('Scheduler_Filter.tab_control');
  Scheduler_Filter.tab_control.make(Scheduler_Filter.tabs[start_tab]);
    
  if(Scheduler_Filter.init()){
    Scheduler_Filter.set_filter('jobs');
    Scheduler_Filter.set_filter('job_chains');     
    Scheduler_Filter.set_filter('orders');     
    
    Scheduler_Filter.set_focus(start_tab);
  }
  //Scheduler_Filter.remove_all_cookies();
  //Scheduler_Filter.remove_cookie(Scheduler_Filter.cookie_name_jobs_active);
  //Scheduler_Filter.set_cookie(Scheduler_Filter.cookie_name_jobs_active,'1');  
}

/***/
window.onunload = function(){
  try{
    if(!Scheduler_Filter.is_inherited_scheduler && Scheduler_Filter.scheduler){
      Scheduler_Filter.scheduler.close();
    }
  }
  catch(x){}
}    

/***/
Scheduler_Filter.init = function(){   
  try {
    Scheduler_Filter.scheduler.logger(1,'INIT FILTER MANAGEMENT ' + self.name,'scheduler_init_filters');
  
    var response                                = Scheduler_Filter.scheduler.execute( '<show_state what="job_chains job_chain_orders order_source_files blacklist" />', false, true, true ); 
    var state_element                           = response.selectSingleNode( "/spooler/answer/state");  
    if(!Scheduler_Filter.is_inherited_scheduler) {
      Scheduler_Filter.scheduler.setState( state_element );
      Scheduler_Filter.scheduler.addPathAttribute(response);
    }
    Scheduler_Filter.xmlJobs                    = response.selectNodes( "//jobs/job[not(@visible) or @visible='yes']" );
    Scheduler_Filter.xmlJobChains               = response.selectNodes( "//job_chains/job_chain[not(@visible) or @visible='yes']" );
    Scheduler_Filter.xmlOrders                  = response.selectNodes( "//job_chains/job_chain[not(@visible) or @visible='yes']//order_queue/order" );
    Scheduler_Filter.jobs                       = new Array();
    Scheduler_Filter.job_chains                 = new Array();
    Scheduler_Filter.orders                     = new Array();
    
    Scheduler_Filter.cookie_jobs_general        = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_jobs_general);
    Scheduler_Filter.cookie_job_chains_general  = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_job_chains_general);
    Scheduler_Filter.cookie_orders_general      = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_orders_general);
    Scheduler_Filter.active_jobs_filter         = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_jobs_active);
    Scheduler_Filter.active_job_chains_filter   = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_job_chains_active);
    Scheduler_Filter.active_orders_filter       = Scheduler_Filter.get_cookie(Scheduler_Filter.cookie_name_orders_active);
    
    if(Scheduler_Filter.xmlJobs && Scheduler_Filter.xmlJobs.length > 0){
      for( var i = 0; i < Scheduler_Filter.xmlJobs.length; i++ ) {
        Scheduler_Filter.jobs[i]                = Scheduler_Filter.xmlJobs[i].getAttribute(Scheduler_Filter.display_attribute_jobs);
      }
      Scheduler_Filter.jobs.sort(Scheduler_Filter.case_insensitive);
      Scheduler_Filter.fill_table('jobs',Scheduler_Filter.jobs,Scheduler_Filter.table_jobs);
      Scheduler_Filter.fill_filters('jobs',Scheduler_Filter.cookie_jobs_general);
    }
    
    if(Scheduler_Filter.xmlJobChains && Scheduler_Filter.xmlJobChains.length > 0){
      for( var i = 0; i < Scheduler_Filter.xmlJobChains.length; i++ ) {
        Scheduler_Filter.job_chains[i]          = Scheduler_Filter.xmlJobChains[i].getAttribute(Scheduler_Filter.display_attribute_job_chains);
      }
      Scheduler_Filter.job_chains.sort(Scheduler_Filter.case_insensitive);
      Scheduler_Filter.fill_table('job_chains',Scheduler_Filter.job_chains,Scheduler_Filter.table_job_chains);
      Scheduler_Filter.fill_filters('job_chains',Scheduler_Filter.cookie_job_chains_general);
    }
    
    if(Scheduler_Filter.xmlOrders && Scheduler_Filter.xmlOrders.length > 0){
      for( var i = 0; i < Scheduler_Filter.xmlOrders.length; i++ ) {
        var order_path                          = Scheduler_Filter.xmlOrders[i].getAttribute(Scheduler_Filter.display_attribute_orders);
        if(order_path && order_path != '/') {
          Scheduler_Filter.orders[i]            = order_path;
        } else {
          Scheduler_Filter.orders[i]            = Scheduler_Filter.xmlOrders[i].getAttribute('job_chain')+','+Scheduler_Filter.xmlOrders[i].getAttribute('id');
        }
      }
      Scheduler_Filter.orders.sort(Scheduler_Filter.case_insensitive);
      Scheduler_Filter.fill_table('orders',Scheduler_Filter.orders,Scheduler_Filter.table_orders);
      Scheduler_Filter.fill_filters('orders',Scheduler_Filter.cookie_orders_general);
    }
    Scheduler_Filter.scheduler.logger(1,'ELAPSED TIME FOR INIT FILTER MANAGEMENT ' + self.name,'scheduler_init_filters');
    return true;
  }
  catch(x) {
   Scheduler_Filter.show_error(x);
   return false;
  }
}

/***/
Scheduler_Filter.fill_table = function(range,arr,table){
  for(var i=0;i<arr.length;i++){
    var tr                  = document.createElement("tr");
    
    var td_checkbox         = document.createElement("td");
    td_checkbox.style.paddingLeft = "3px";
    if( Scheduler_Filter.ie ) {
      td_checkbox.innerHTML = '<input type="checkbox" name="'+range+'" onclick="Scheduler_Filter.pattern[this.name]=\'\';" />';
    } else {
      var chckbox           = document.createElement("input");
      chckbox.setAttribute("type","checkbox");
      chckbox.setAttribute("name",range);
      chckbox.setAttribute("onclick","Scheduler_Filter.pattern[this.name]='';");
      td_checkbox.appendChild(chckbox);
    }
    
    var td_job_name         = document.createElement("td");
    td_job_name.style.verticalAlign = "middle";
    td_job_name.innerHTML   = Scheduler_Filter.display_path(arr[i]);
    
    tr.appendChild(td_checkbox);
    tr.appendChild(td_job_name);
    tr.sos_path             = arr[i];
    tr.sos_checkbox         = tr.firstChild.firstChild;
    table.tBodies[0].appendChild(tr); 
  }
}

/***/
Scheduler_Filter.normalize_path = function(path){
  return path.replace(/^[\\\\]*\//,'');  
}

/***/
Scheduler_Filter.reg_quote = function(path){
  return path.replace(/([\.\)\(\[\]\^\+\$])/g,'\\$1');
}

/***/
Scheduler_Filter.display_path = function(path){
  var delim = Scheduler_Filter.ie8 ? '&#8203;' : '<wbr />';
  return Scheduler_Filter.normalize_path(path).replace(/\//g,'/'+delim).replace(/_/g,'_'+delim);  
}

/***/
Scheduler_Filter.fill_filters = function(range,cookie_general){
  if(cookie_general.length > 0){
    var el_filters        = Scheduler_Filter.form.elements['filters_'+range];
    var c_arr             = cookie_general.split('|');
    var j                 = 0;//1;
    var cookie_insert_id  = Scheduler_Filter['cookie_'+range+'_insert_id'];
    var active_filter     = Scheduler_Filter['active_'+range+'_filter'];
    
    /*
    for(var i=0;i<c_arr.length;i++){
      var arr = c_arr[i].split('=>');
      if(arr[0] && arr[1]){
        if(1*arr[0] >= cookie_insert_id){
          cookie_insert_id = 1*arr[0]+1;
          Scheduler_Filter['cookie_'+range+'_insert_id'] = cookie_insert_id; 
        }
        
        //alert(arr[0]+' = '+arr[1]);
        var new_option_selected = 1*active_filter == arr[0] ? true : false;  
        var new_option          = new Option(arr[1],arr[0],false,new_option_selected);
        new_option.setAttribute('class','input');
        //el_filters.options[j]   = new_option;
        el_filters.options[el_filters.length]   = new_option;
        j++;
      }         
    }
    */
    var arr_2_sort = new Array();
    for(var i=0;i<c_arr.length;i++){
      var arr = c_arr[i].split('=>');
      if(arr[0] && arr[1]){
        if(1*arr[0] >= cookie_insert_id){
          cookie_insert_id = 1*arr[0]+1;
          Scheduler_Filter['cookie_'+range+'_insert_id'] = cookie_insert_id; 
        }
        arr_2_sort[j] = {
          key   : arr[0],
          title : arr[1]  
        }
        j++;
      }         
    }
    arr_2_sort.sort(Scheduler_Filter.obj_case_insensitive);
    for(var i=0;i<arr_2_sort.length;i++){
      var new_option_selected               = 1*active_filter == arr_2_sort[i].key ? true : false;  
      var new_option                        = new Option(arr_2_sort[i].title,arr_2_sort[i].key,false,new_option_selected);
      new_option.setAttribute('class','input');
      el_filters.options[el_filters.length] = new_option;
    }
    
    
    if(document.all && active_filter.length > 0){
      for(var i=0;i<el_filters.length;i++){
        if(el_filters.options[i].value == active_filter){
          el_filters.value = active_filter;
          break;
          //el_filters.value = active_filter;
        }
      }
    } 
  }
}

/***/
Scheduler_Filter.set_filter = function(range,el_filters){
  
  if(!el_filters) el_filters = Scheduler_Filter.form.elements['filters_'+range];
  
  var val             = '';
  var disabled        = true;
  var cookie_prefix   = '';
  var cookie_val      = '';
  var table           = null;
  if(el_filters.value == '-1'){//new
    Scheduler_Filter.select_all(range,false);
  }
  else{
    val           = el_filters.options[el_filters.selectedIndex].text;
    disabled      = false;
    table         = Scheduler_Filter['table_'+range];
    cookie_prefix = Scheduler_Filter['cookie_name_'+range+'_prefix'];
    cookie_val    = Scheduler_Filter.get_cookie(cookie_prefix+el_filters.value);
    
    if( cookie_val.substr(0,1) == ">" ) {
      cookie_val  = cookie_val.substr(1);
      Scheduler_Filter.pattern[range] = cookie_val;
    } else {
      cookie_val  = "^("+cookie_val+")$";
      Scheduler_Filter.pattern[range] = "";
    }
    Scheduler_Filter.form.elements['filter_'+range].value = Scheduler_Filter.pattern[range];
    var pattern   = new RegExp(cookie_val);
    for(var i=0;i<table.tBodies[0].rows.length;i++){
      var tr      = table.tBodies[0].rows[i];
      tr.sos_checkbox.checked = (pattern.test(Scheduler_Filter.normalize_path(tr.sos_path)) || pattern.test(tr.sos_path)); 
    }
  }
  Scheduler_Filter.form.elements['filter_name_'+range].value = val;
  
  Scheduler_Filter.disable_edit_buttons(range,disabled);
}

/***/
Scheduler_Filter.set_focus = function(range){
  try{
    var el_filter_name  = Scheduler_Filter.form.elements['filter_name_'+range];
    if(el_filter_name){
      el_filter_name.focus(); 
      el_filter_name.value = el_filter_name.value;
    }
  }
  catch(x){}
}

/***/
Scheduler_Filter.disable_edit_buttons = function(range,disabled){
  var btn_store_as  = document.getElementById('btn_store_as_'+range);
  var btn_remove    = document.getElementById('btn_remove_'+range);
  
  //btn_remove.disabled = disabled; 
  btn_store_as.style.display  = (disabled) ? 'none' : '';
  btn_remove.style.display    = (disabled) ? 'none' : '';  
}

/***/
Scheduler_Filter.store_filter = function(range,is_store_as){
  Scheduler_Filter.scheduler.logger(1,'STORE'+(is_store_as? ' AS' : '')+' FILTER ' + self.name);
  try{
    var el_filters    = Scheduler_Filter.form.elements['filters_'+range];
    var filter_name   = Scheduler_Filter.form.elements['filter_name_'+range].value;
    var is_stored     = false;
    var cookie_key    = -1;
    eval( 'var server_filters = _'+range.replace(/s$/,'')+'_filter;' ); 
    if(filter_name.length > 0){
      if(filter_name.search(/(\||=>|;)/g) != -1){
        alert(getTranslation('Using\n\n|\n=>\n;\n\nis not allowed'));
        return false; 
      }
      if((typeof _project_filter[filter_name] != 'undefined') || (typeof server_filters[filter_name] != 'undefined')){
        var custom_file = ( _server_settings ) ? location.hash.substr(1) : 'custom';
        alert(getTranslation('A filter "$filter_name" is already defined in the file $file.',{'filter_name':filter_name,'file':custom_file+'.js'}));
        return false; 
      }
      if(el_filters.value == '-1' || is_store_as){//new
        var option_found  = false;
        var add_new       = true;
        for(var i=0;i<el_filters.options.length;i++){
          var opt = el_filters.options[i];
          if(opt.text == filter_name){
            option_found  = true;
            break;
          } 
        }
        if(option_found){
          add_new = confirm(getTranslation('The same filter name "$filter" was already defined\n\nDo you want nevertheless to store it ?',{filter:filter_name}));
        }
        if(add_new){
          cookie_key            = Scheduler_Filter['cookie_'+range+'_insert_id'];
     
          var new_option        = new Option(filter_name,cookie_key,false,true);
          //el_filters.options[i] = new_option;
          new_option.setAttribute('class','input');
          el_filters.options[el_filters.length] = new_option;
          
          Scheduler_Filter.disable_edit_buttons(range,false);
          Scheduler_Filter.show_msg(range,getTranslation('new filter was stored'));
          
          is_stored   = true;
          Scheduler_Filter['cookie_'+range+'_insert_id']+=1;
        }
      }
      else{
         el_filters.options[el_filters.selectedIndex].text = filter_name;
         Scheduler_Filter.show_msg(range,getTranslation('filter was stored'));
         is_stored  = true;
         cookie_key = el_filters.value; 
      }
      
      if(is_stored){
        var val   = '';
        var j     = 0;
        var table = Scheduler_Filter['table_'+range];
        if( Scheduler_Filter.pattern[range] != '' ) {
          val = ">"+Scheduler_Filter.pattern[range];
        } else {
          for(var i=0;i<table.tBodies[0].rows.length;i++){
            var tr = table.tBodies[0].rows[i];
            if(tr.sos_checkbox && tr.sos_checkbox.checked){
              val+= (j > 0) ? '|'+tr.sos_path : tr.sos_path; 
              j++;
            } 
          }
          val = Scheduler_Filter.reg_quote(val);
        }
        var cookie_prefix = Scheduler_Filter['cookie_name_'+range+'_prefix'];
        Scheduler_Filter.set_cookie(cookie_prefix+String(cookie_key),val);
        Scheduler_Filter.update_general_cookie(range,false,cookie_key,filter_name);
        try {
          window.opener.control_frame.get_filters( range, true );
        } catch(E) {}
      }
    }
    else{
      alert(getTranslation('Filter name must be stated'));  
    }
  }
  catch(x){
    Scheduler_Filter.show_error(x); 
  }
  finally{
    return false; 
  }
}

/***/
Scheduler_Filter.update_general_cookie = function(range,is_remove,cookie_key,filter_name){
  if(!filter_name) filter_name = 'undefined filter name';
  
  var cookie_general_value  = Scheduler_Filter['cookie_'+range+'_general'];
  var cookie_general_name   = Scheduler_Filter['cookie_name_'+range+'_general'];
  if(cookie_general_value.length > 0){
    var c_arr             = cookie_general_value.split('|');
    cookie_general_value  = '';
    var j                 = 1;
    var found             = false;
    for(var i=0;i<c_arr.length;i++){
      var arr = c_arr[i].split('=>');
      if(arr[0] && arr[1]){
        if(1*arr[0] == 1*cookie_key){
          if(!is_remove){
            cookie_general_value+= (cookie_general_value.length > 0) ? '|' : '';
            cookie_general_value+= cookie_key+'=>'+filter_name;
            found = true;
          }
        }
        else{
          cookie_general_value+= (cookie_general_value.length > 0) ? '|' : '';
          cookie_general_value+= arr[0]+'=>'+arr[1];
        }
      }         
    } 
    if(!is_remove && !found){
      cookie_general_value += '|'+cookie_key+'=>'+filter_name;
    }
  }
  else{
    if(!is_remove){
      cookie_general_value = cookie_key+'=>'+filter_name;
    }
  }
  Scheduler_Filter.set_cookie(cookie_general_name,cookie_general_value);
  
  var val = cookie_general_value.replace(/"/g,'\\"');
  Scheduler_Filter['cookie_'+range+'_general'] = val;
  //alert("\\'"+Scheduler_Filter.cookie_jobs_general);
}

/***/
Scheduler_Filter.remove_filter = function(range){
  Scheduler_Filter.scheduler.logger(1,'REMOVE FILTER ' + self.name);
  try{
    var option_value = '-1';
    var option_index = 0;
    var el_filters   = Scheduler_Filter.form.elements['filters_'+range];
    for(var i=0;i<el_filters.options.length;i++){
      var opt = el_filters.options[i];
      if(opt.text == Scheduler_Filter.form.elements['filter_name_'+range].value){
        option_value = opt.value;
        option_index = i;
        break;
      } 
    }
    if(option_value != '-1'){
      if(confirm('Do you really want to remove this filter ?')){
        var cookie_prefix = Scheduler_Filter['cookie_name_'+range+'_prefix'];
        Scheduler_Filter.remove_cookie(cookie_prefix+option_value);
        Scheduler_Filter.update_general_cookie(range,true,option_value);
        
        var active_filter = Scheduler_Filter['active_'+range+'_filter'];
        if(active_filter == option_value){
          var cookie_name_active_filter = Scheduler_Filter['cookie_name_'+range+'_active'];
          Scheduler_Filter.remove_cookie(cookie_name_active_filter);
          Scheduler_Filter['active_'+range+'_filter'] = '';
        }
        
        el_filters.options[option_index]  = null;
        el_filters.selectedIndex          = 0;
        
        var filter_name   = Scheduler_Filter.form.elements['filter_name_'+range];
        try{
          filter_name.focus();
        }
        catch(x){}
        filter_name.value = '';
        
        Scheduler_Filter.select_all(range,false);
        Scheduler_Filter.disable_edit_buttons(range,true);
        Scheduler_Filter.show_msg(range,getTranslation('filter was removed'));
        try {
          window.opener.control_frame.get_filters( range, true );
        } catch(E) {}
      }
    }
  }
  catch(x){
    Scheduler_Filter.show_error(x); 
  }
  finally{
    return false; 
  }
}

/***/
Scheduler_Filter.do_check = function(range){
  try{
    var filter = Scheduler_Filter.form.elements['filter_'+range].value;
    if(filter.length > 0){
      var table   = Scheduler_Filter['table_'+range];
      var pattern = new RegExp(filter);
      Scheduler_Filter.pattern[range] = filter;
      for(var i=0;i<table.tBodies[0].rows.length;i++){
        var tr    = table.tBodies[0].rows[i];
        tr.sos_checkbox.checked = (Scheduler_Filter.normalize_path(tr.sos_path).search(pattern) > -1 || tr.sos_path.search(pattern) > -1);
      }
    }
  }
  catch(x){
    Scheduler_Filter.show_error(x); 
  }
}

/***/
Scheduler_Filter.select_all = function(range,flag){
  Scheduler_Filter.pattern[range] = "";
  var table = Scheduler_Filter['table_'+range];
  for(var i=0;i<table.tBodies[0].rows.length;i++){
    table.tBodies[0].rows[i].sos_checkbox.checked = flag;
  } 
}

/***/
Scheduler_Filter.show_error = function(x){
  try{
    if(Scheduler_Filter.div_error) {
      Scheduler_Filter.div_error.style.display  = "block";
      Scheduler_Filter.div_error.innerHTML      = x.message.replace( /&/g, "&amp;" ).replace( /</g, "&lt;" ).replace( />/g, "&gt;" ).replace( /\"/g, "&quot;" ).replace( /\'/g, "&#039;" ).replace( /\n/g, "<br/>" ).replace( "  ", "\xA0 " );
    } 
    else {
      alert( x.message );
    }
  }
  catch(x){}
  return false;
} 

/** */
Scheduler_Filter.show_msg = function(range,msg){
  if(document.getElementById('span_msg_'+range)){
    document.getElementById('span_msg_'+range).innerHTML = msg;
    window.setTimeout('Scheduler_Filter.reset_msg(\''+range+'\')',4000);
  }
}
/***/
Scheduler_Filter.reset_msg = function(range){
  if(document.getElementById('span_msg_'+range)){
    document.getElementById('span_msg_'+range).innerHTML = '';
  }
}

/***/
Scheduler_Filter.obj_case_insensitive = function(a,b){
return Scheduler_Filter.case_insensitive(a.title,b.title);
}

/***/
Scheduler_Filter.case_insensitive = function(a,b){
  var c = String(a).toUpperCase();
  var d = String(b).toUpperCase();
  if (c > d) return 1
  if (c < d) return -1
  return 0;
}

/***/
Scheduler_Filter.set_cookie = function (name, value) 
{
    try {
      var today   = new Date();
      var expires = new Date();
      var expire  = 1000*60*60*24*365;
      name        = Scheduler_Filter.scheduler._cookie_prefix+name;
      if ( value == null ) {
        expires.setTime(today.getTime());
        document.cookie = name+"=; expires="+expires.toGMTString()+"; path=/";
      } else {
        expires.setTime(today.getTime() + expire);
        document.cookie = name+"="+escape(value)+"; expires="+expires.toGMTString()+"; path=/";
      }
      Scheduler_Filter.scheduler.logger(1,'COOKIE WRITE: ' + name + '=' + value);
    } catch(x) {
      Scheduler_Filter.scheduler.logger(1,'ERROR OCCURS AT COOKIE WRITE: ' + name + '=' + value + '\n' + x.message);
    }      
}

/***/
Scheduler_Filter.get_cookie = function(name) 
{   
    name        = Scheduler_Filter.scheduler._cookie_prefix+name;
    Scheduler_Filter.scheduler.logger(1,'LOOKING FOR COOKIE: ' + name);
    var value   = "";
    var pattern = new RegExp(name+"=([^;]*);");
    if (document.cookie.length > 0 && pattern.test(document.cookie+";") ) {
      var result = pattern.exec(document.cookie+";");
      if( result.length > 0 ) {
        if( RegExp.$1 != "" ) value = unescape(RegExp.$1);
        Scheduler_Filter.scheduler.logger(1,'COOKIE FOUND: ' + name + '=' + value);
      }
    } else {
      Scheduler_Filter.scheduler.logger(1,'COOKIE NOT FOUND (default value is "")');
    }
    return value;
}

/***/
Scheduler_Filter.remove_cookie = function(name){
  name            = Scheduler_Filter.scheduler._cookie_prefix+name;
  var expires     = new Date(1981,01,01);
  document.cookie = name + "=; expires="+expires.toGMTString()+"; path=/"; 
}

/***/
Scheduler_Filter.remove_all_cookies = function(){
  var arr = document.cookie.split(';');
  for(var i=0;i<arr.length;i++){
    var name        = arr[i].split('=')[0];
    var expires     = new Date(1981,01,01);
    document.cookie = name + "=; expires="+expires.toGMTString()+"; path=/"; 
  }
}