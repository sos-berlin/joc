/********************************************************* begin of preamble
**
** Copyright (C) 2003-2015 Software- und Organisations-Service GmbH. 
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

var __current_popup_menu = null;
var _mouse_x = 0;
var _mouse_y = 0;

//-----------------------------------------------------------------------------document.onmousemove

if( typeof window.createPopup == "undefined" )
{   
    document.onmousedown = function( e )
    {
        _mouse_x = e.pageX;
        _mouse_y = e.pageY;
        if(window['SOS_Div']) SOS_Div.moveStart(e); 
    }
} else {
  if(window['SOS_Div']) document.onmousedown = SOS_Div.moveStart;
}

if(window['SOS_Div']) document.onmouseup = SOS_Div.moveEnd;
//------------------------------------------------------------------------------------------mouse_x

function mouse_x( indent )
{   
    if( typeof indent != "number" ) { indent = 0; }
    return (typeof window.event != "undefined") ? document.documentElement.scrollLeft + event.clientX + indent
                                                : _mouse_x + indent;
}

//------------------------------------------------------------------------------------------mouse_y

function mouse_y( indent )
{   
    if( typeof indent != "number" ) { indent = 0; }
    return (typeof window.event != "undefined") ? window.pageYOffset + event.clientY + indent
                                                : _mouse_y + indent;
}

//---------------------------------------------------------------------------------------Popup_menu

function Popup_menu()
{
}

//---------------------------------------------------------------------------------Popup_menu.close

Popup_menu.prototype.close = function( menu_type, element_name )
{
    
    if( typeof menu_type == "undefined" ) { menu_type = "popup"; }
    
    if( window.createPopup == undefined )
    {
        if( menu_type == "popup" ) {
          var div = document.getElementById( "__popup_menu__" );
          if( div )
          {
            div.style.visibility = "hidden";
            div.innerHTML = "";
          }
        } else { 
          var selectbox = document.getElementById( element_name );
          if( selectbox )
          {
            for( var i=2; i < selectbox.length; i++ ) {
              selectbox.options[i] = null;
            }
          }
          //document.getElementById( 'update_periodically_checkbox' ).checked=parent._scheduler._update_periodically;
        }
    }
    else
    {
        this._popup.hide();
    }
    __current_popup_menu = null;
}


//-------------------------------------------------------------------------------Popup_menu_builder

function Popup_menu_builder()
{
    Input_dialog.close();
    this._popup_menu      = new Popup_menu();
    this._html_array      = new Array();
    this._selectbox_array = new Array();
    
    this._html_array.push( "<table cellpadding='0' cellspacing='0' " +
                                 " style='background-color: menu; border: thin outset; padding:4px 0px 6px;'>" );
    //this._html_array.push( "<tr><td style='background-color: menu; line-height: 4px;'>&#160;</td></tr>" );
    
    this._finished = false;
}

//---------------------------------------------------------------------Popup_menu_builder.add_entry

Popup_menu_builder.prototype.add_entry = function( html_entry, call, is_active, is_hidden, is_submenu )
{
    if( is_active  == undefined )  is_active  = true;
    if( is_hidden  == undefined )  is_hidden  = false;
    if( is_submenu == undefined )  is_submenu = false;
    
    if(!is_hidden) {
    	var html =  "<tr>";
    	html += "<td style='";
    	html +=         "font-family: Tahoma, Sans-Serif; font-size: 11px; ";
    	html +=         "color: " + ( is_active? "menutext" : "gray" ) + ";";
    	html +=         "cursor: default; white-space: nowrap; ";
    	html +=         "background-color: menu; ";
    	html +=         "padding-left:10px; padding-right:10px; ";
    	//if( window.createPopup == undefined ) html +=         "line-height:12px; ";
    	html +=         "'";

    	if( call != undefined  &&  is_active )
    	{
        html +=   " onmouseover='this.style.backgroundColor=\"highlight\"; this.style.color=\"highlighttext\";'";
        html +=   " onmouseout='this.style.backgroundColor=\"menu\"; this.style.color=\"menutext\"'";
        html +=   " onclick=\"";
        if( window.createPopup != undefined ) {  
          html += "with( parent ) { ";
          html += call.replace( /\"/g, "\\\"" ).replace( /&#039;/g, "\\'" );
          html += "}";
        } else {
          html += call.replace( /\"/g, "\\\"" ).replace( /&#039;/g, "\\'" );
        }
        
        html +=    "\"";
    	}
    
    	html +=  ">";
    	html +=  html_entry;
    	html += "</td>";
    	html += "</tr>";
    	this._html_array.push( html );
    
    	if( window.createPopup == undefined ) {
      	var opt         = new Option( html_entry, call.replace( /&quot;/g, '"' ) );  
      	if( opt.innerHTML ) {   //im IE leer
        	opt.innerHTML = '&#160;&#160;' + opt.text;
      	} else {
        	opt.text      = '  '+ opt.text;
      	} 
      
      	opt.setAttribute( 'class', 'selectbox_menu' );    
      	opt.disabled    = ( !is_active );
       
      	this._selectbox_array.push( opt );
      }
    }
} 

//-----------------------------------------------------------------------Popup_menu_builder.add_bar

Popup_menu_builder.prototype.add_bar = function()
{
    var html =  "<tr>";
    html += "<td style='background-color:menu;color:gray;padding:2px;'>";
    html +=  "<hr size='1'/>";
    html += "</td>";
    html += "</tr>";
                         
    this._html_array.push( html );
    
    if( window.createPopup == undefined ) {
      if( parent._scheduler._chrome > 0 || parent._scheduler._safari > 0 ) {
        var opt         = new Option( '--------------------------------------------', '' );
      } else {
        var opt         = new Option( '---------------------------------------------------------------------------------------', '' );
      }
      opt.setAttribute( 'class', 'selectbox_menu' );    
      opt.setAttribute( 'style', 'font-size:6px;height:8px;' );    
      opt.disabled    = true;
       
      this._selectbox_array.push( opt );
    }
}

//--------------------------------------------------------------------------Popup_menu_builder.html

Popup_menu_builder.prototype.html = function()
{
    if( !this._finished )
    {
      //this._html_array.push( "<tr><td style='background-color: menu; line-height: 6px;'>&#160;</td></tr>" );
      this._html_array.push( "</table>" );
      this._finished = true;
    }
    
    return this._html_array.join( "" );
}

//-------------------------------------------------------------Popup_menu_builder.create_popup_menu

Popup_menu_builder.prototype.create_popup_menu = function( menu_type, element_name )
{
    if( typeof menu_type == "undefined" ) { menu_type = "popup"; }
    
    if( window.createPopup == undefined )
    {
        if( menu_type == "popup" ) 
        {
          var div = document.getElementById( "__popup_menu__" );
          if( !div )
          {
            var body = document.getElementsByTagName( "body" )[ 0 ];
            div = body.ownerDocument.createElement( "div" );
            div.setAttribute( "id"   , "__popup_menu__" );
            div.setAttribute( "style", "" );
            div.style.position   = "absolute";
            div.style.top        = "0px";
            div.style.left       = "0px";
            div.style.visibility = "hidden";
            div.style.zIndex     = "999";
            div.setAttribute( "onmouseout" , "if(__current_popup_menu) { __current_popup_menu._close_timer = setTimeout( 'try{__current_popup_menu.close();}catch(x){}' , 400 ); }" );
            div.setAttribute( "onmouseover", "if(__current_popup_menu) { clearTimeout( __current_popup_menu._close_timer ); }" );          
            body.appendChild( div );
          }
        
          div.innerHTML = this.html();
        }
        else if( menu_type == "selectbox" )
        {
          var selectbox = document.getElementById( element_name );
          if( selectbox ) {
            this.add_entry('','',false);
            this._finished = true;
          
            for( var i=0; i < this._selectbox_array.length; i++ ) {
              selectbox.options[i+2] = this._selectbox_array[i];
            }
            selectbox.options[this._selectbox_array.length+1].style.fontSize = '0px';
            selectbox.options[this._selectbox_array.length+1].style.height = '3px';
            selectbox.setAttribute( "onblur", "try{ __current_popup_menu.close('selectbox','" + element_name + "'); }catch(x){}" );
            //selectbox.setAttribute( "onclick", "parent._scheduler._update_periodically=document.getElementById( 'update_periodically_checkbox' ).checked;document.getElementById( 'update_periodically_checkbox' ).checked=false;window.clearTimeout( window.parent.left_frame._timer )" );
            //selectbox.setAttribute( "onclick", "window.clearTimeout( window.parent.left_frame._timer )" );
            selectbox.setAttribute( "onchange", "eval(this.value);this.selectedIndex=0;" );
            //alert( $(selectbox).getWidth() );
          }
        }
    }
    else
    {
        if( !this._popup_menu._popup )
        {   
            this._popup_menu._popup = window.createPopup();
            this._popup_menu._popup.document.body.innerHTML = this.html();
        }
    }
    
    return this._popup_menu;
}

//---------------------------------------------------------------Popup_menu_builder.show_popup_menu

Popup_menu_builder.prototype.show_popup_menu = function()
{
    this.create_popup_menu();
    
    __current_popup_menu = this._popup_menu;

    if( window.createPopup == undefined )
    {
        var div              = document.getElementById( "__popup_menu__" );
        var dim              = {width: div.offsetWidth, height: div.offsetHeight, top: mouse_y(-4), left: mouse_x(-4)};
        if( dim.left + dim.width  > window.innerWidth - 12 ) dim.left -= dim.width - 8; 
        //if( dim.top  + dim.height > document.documentElement.scrollTop + window.innerHeight - 16 ) dim.top -= dim.height - 8;
        if( dim.top  + dim.height > window.pageYOffset + window.innerHeight - 16 ) dim.top -= dim.height - 8;
        dim.left             = Math.max( dim.left, 2);
        dim.top              = Math.max( dim.top , 2);
        div.style.left       = dim.left+"px";
        div.style.top        = dim.top+"px";
        div.style.visibility = "visible";
    }
    else    
    {
        this._popup_menu._popup.show( 0, 0, 0, 0 );
        var width  = this._popup_menu._popup.document.body.scrollWidth;
        var height = this._popup_menu._popup.document.body.scrollHeight;
        this._popup_menu._popup.hide();
        if( event.type == 'contextmenu' ) {
          this._popup_menu._popup.show( event.clientX, event.clientY, width, height, document.documentElement );
        } else {
          this._popup_menu._popup.show( (mouse_x()> 120 ? 90-width : 0), 15, width, height, event.srcElement );
        }
    }
    
    return this._popup_menu;
}


Popup_menu_builder.prototype.show_selectbox_menu = function( element_name )
{
    this.create_popup_menu( "selectbox", element_name );
    
    __current_popup_menu = this._popup_menu;

    if( window.createPopup != undefined )
    {
        this._popup_menu._popup.show( 0, 0, 0, 0 );
        var width  = this._popup_menu._popup.document.body.scrollWidth;
        var height = this._popup_menu._popup.document.body.scrollHeight;
        this._popup_menu._popup.hide();
        this._popup_menu._popup.show( (mouse_x()> 120 ? 90-width : 0), 15, width, height, event.srcElement );
    }
    
    return this._popup_menu;
} 


//----------------------------------------------------------------------------------------class Input_dialog

function Input_dialog() 
{    
    Input_dialog.close();
    this._html_array                    = new Array();
    this._html_hiddens                  = new Array();
    this._with_params                   = false;
    this._with_new_params               = false;
    this._with_reload                   = false;
    this.submit_fct                     = "";
    this.close_after_submit             = true;
    this.submit_title                   = 'submit';
    var left_frame_width                = ( typeof parent.left_frame.innerWidth == "undefined" ) ? parent.left_frame.document.body.clientWidth : parent.left_frame.innerWidth;
    var top_frame_width                 = ( typeof parent.top_frame.innerWidth  == "undefined" ) ? parent.top_frame.document.body.clientWidth  : parent.top_frame.innerWidth;
    if( top_frame_width == left_frame_width ) left_frame_width = left_frame_width * 51 / 100; 
    this.width                          = parseInt(left_frame_width-52, 10);
}
    
    
Input_dialog._element                   = null;


Input_dialog.close = function() 
{
    if(window['SOS_Calendar'] && SOS_Calendar.isOpen) SOS_Calendar.close();
    if( !Input_dialog._element ) {
      Input_dialog._element             = parent.left_frame.document.getElementById('__input__');
    }
    Input_dialog._element.innerHTML     = "";
    Input_dialog._element.style.display = "none";
    //var shadowElem1                     = parent.left_frame.document.getElementById('__input_shadow1__');
    //var shadowElem2                     = parent.left_frame.document.getElementById('__input_shadow2__');
    //if( shadowElem1 && shadowElem2 ) {
    //  shadowElem1.style.display         = "none";
    //  shadowElem2.style.display         = "none";
    //}
    //var iframeElem                      = parent.left_frame.document.getElementById('__input_iframe__');
    //if( parent._scheduler._ie > 0 && iframeElem ) { iframeElem.style.display  = "none"; }
}
 

Input_dialog.prototype.show = function() 
{    
    parent.left_frame.focus();
    Input_dialog._element.style.top     = "70px";
    Input_dialog._element.style.zIndex  = "20";
    var _scrollTop = parent.left_frame.document.documentElement.scrollTop + parent.left_frame.document.body.scrollTop; 
    if( _scrollTop > 60 ) {
      Input_dialog._element.style.top   = (10 + _scrollTop) + "px";
    }
    Input_dialog._element.innerHTML     = this.html();
    Input_dialog._element.style.display = "block";
    var input_dialog_form               = parent.left_frame.document.forms['__input_dialog__'];
    if( this._with_new_params ) Input_dialog.add_new_params(this.width,input_dialog_form,false);
    var first_elem = null;
    for( var i=0; i < input_dialog_form.elements.length; i++ ) {
      if( input_dialog_form.elements[i].type ) {
        first_elem = input_dialog_form.elements[i];
        break;
      }
    } 
    if( first_elem ) {
      first_elem.focus();
      if( first_elem.type.search( /^text/ ) > -1 && first_elem.name != 'run_time' ) first_elem.select();
    }
    //Input_dialog.add_shadow();
}


Input_dialog.add_shadow = function()
{   
    var iframeElem                      = parent.left_frame.document.getElementById('__input_iframe__');
    if( parent._scheduler._ie > 0 && iframeElem ) {
      iframeElem.style.top              = Input_dialog._element.offsetTop+'px';
      iframeElem.style.zIndex           = Input_dialog._element.style.zIndex-3;
      iframeElem.style.left             = Input_dialog._element.offsetLeft+'px';  
      iframeElem.style.width            = Input_dialog._element.offsetWidth+'px';
      iframeElem.style.height           = Input_dialog._element.offsetHeight+'px';
      iframeElem.style.display          = "block";
    }
    var shadowElem1                     = parent.left_frame.document.getElementById('__input_shadow1__');
    var shadowElem2                     = parent.left_frame.document.getElementById('__input_shadow2__');
    if( shadowElem1 && shadowElem2 ) {
      shadowElem1.style.top             = (Input_dialog._element.offsetTop+8)+'px';
      shadowElem1.style.zIndex          = Input_dialog._element.style.zIndex-1;
      shadowElem1.style.width           = (Input_dialog._element.offsetWidth-6)+'px';
      shadowElem1.style.height          = (Input_dialog._element.offsetHeight-6)+'px';
      shadowElem1.style.display         = "block";
      shadowElem2.style.top             = (Input_dialog._element.offsetTop+6)+'px';
      shadowElem2.style.zIndex          = Input_dialog._element.style.zIndex-2;
      shadowElem2.style.width           = (Input_dialog._element.offsetWidth-2)+'px';
      shadowElem2.style.height          = (Input_dialog._element.offsetHeight-2)+'px';
      shadowElem2.style.display         = "block";
    }
}


Input_dialog.prototype.add_prompt = function( str, obj ) 
{    
    if( typeof str == "string" ) {
      this._html_array.push( '<tr>' );
      this._html_array.push( '<td>' + parent.getTranslation(str,obj) + '</td>' );
      this._html_array.push( '</tr>' );
    } 
}


Input_dialog.prototype.add_title = function( str, obj ) 
{   
    if( typeof str == "string" ) {
      this._html_array.push( '<tr>' );
      this._html_array.push( '<th class="input_title" style="padding-bottom:6px;">' + parent.getTranslation(str,obj) + '</th>' );
      this._html_array.push( '</tr>' );
    } 
}

 
Input_dialog.prototype.add_input = function( name, val, max_length, with_td, style ) 
{    
    if( typeof max_length   != "number"  ) max_length = 0;
    if( typeof with_td      != "boolean" ) with_td    = true;
    if( typeof style        != "string"  ) style      = 'width:' + this.width + 'px';
    if( max_length > 0 ) max_length = ' maxlength="' + max_length + '"';
    if( with_td ) this._html_array.push( '<tr><td style="padding-bottom:2px;">' );
    this._html_array.push( '<input type="text" name="' + name + '" value="' + val + '"' + max_length + ' style="' + style + '"/>' );
    if( with_td ) this._html_array.push( '</td></tr>' );
}


Input_dialog.prototype.add_labeled_input = function( label, name, val, max_length, evt ) 
{    
    if( typeof val != "string" ) val = "";
    if( typeof max_length != "number" ) max_length = 0;
    if( typeof evt != "string" ) evt = '';
    if( max_length > 0 ) max_length = ' maxlength="' + max_length + '"';
    this._html_array.push( '<tr><td style="padding:0px;">' );
    this._html_array.push( '<table cellspacing="0" cellpadding="0" width="100%" border="0">' );
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td class="param_name" style="width:40%;padding-right:2px;"><span style="position:relative;top:-2px;">' + label + '</span></td>' );
    this._html_array.push( '<td style="width:60%;padding-bottom:2px;">' );
    this._html_array.push( '<input type="text" name="' + name + '" value="' + val + '"' + max_length + ' style="width:' + parseInt((this.width*3/5)-1,10) + 'px" ' + evt + '/>' );
    this._html_array.push( '</td></tr>' );
    this._html_array.push( '</table></td></tr>' );
}


Input_dialog.prototype.add_labeled_input_with_unit = function( label, name, val, max_length, evt, unit ) 
{    
    if( typeof val != "string" ) val = "";
    if( typeof max_length != "number" ) max_length = 0;
    if( typeof evt != "string" ) evt = '';
    var max_length_attr = "";
    if( max_length > 0 ) max_length_attr = ' maxlength="' + max_length + '"';
    this._html_array.push( '<tr><td style="padding:0px;">' );
    this._html_array.push( '<table cellspacing="0" cellpadding="0" width="100%" border="0">' );
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td class="param_name" style="width:40%;padding-right:2px;">' + label + '</td>' );
    this._html_array.push( '<td style="width:60%;padding-bottom:2px;"><input type="text" name="' + name + '" value="' + val + '"' + max_length_attr + ' style="text-align:right;width:' + (max_length+1) + 'em" ' + evt + '/>' );
    this._html_array.push( '<span style="padding:0px 2px;">' + unit + '</span></td></tr>' );
    this._html_array.push( '</table></td></tr>' );
}


Input_dialog.prototype.add_settings_input = function( label, name, val, max_length ) {
    this._html_array.push( '<tr><td style="padding-bottom:2px;">'+label+'</td><td style="padding-bottom:2px;text-align:right;">' );
    this.add_input( name, val, max_length, false, 'text-align:right;width:28px;padding:0px 2px;font-size:12px;' );
    this._html_array.push( '</td></tr>' );
}    

 
Input_dialog.prototype.add_select = function( name, opts, selected_opt, size, width, with_td ) 
{    
    if( typeof opts         != "object"  ) opts         = new Array();
    if( typeof selected_opt != "string"  ) selected_opt = "";
    if( typeof size         != "number"  ) size         = 1;
    if( typeof width        != "number"  ) width        = this.width;
    if( typeof with_td      != "boolean" ) with_td      = true;
    width = (width == 0) ? '' : ' style="width:' + width + 'px"';
    if( with_td ) this._html_array.push( '<tr><td style="padding-bottom:2px;">' );
    this._html_array.push( '<select name="' + name + '" size="' + size + '"' + width +'>' );
    for( var i=0; i<opts.length; i++) {
      var selected = ( selected_opt == opts[i].key ) ? ' selected="true"' : "";
      this._html_array.push( '<option value="' + opts[i].key + '"' + selected + '>' + opts[i].display + '</option>' );
    }
    this._html_array.push( '</select>' );
    if( with_td ) this._html_array.push( '</td></tr>' );
}


Input_dialog.prototype.add_labeled_select = function( label, name, opts, selected_opt, onchange ) 
{    
    if( typeof opts         != "object" ) opts         = new Array();
    if( typeof selected_opt != "string" ) selected_opt = "";
    if( typeof onchange     != "string" ) onchange     = "";
    this._html_array.push( '<tr><td style="padding:0px;">' );
    this._html_array.push( '<table cellspacing="0" cellpadding="0" width="100%" border="0">' );
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td class="param_name" style="width:40%;padding-right:2px;"><span style="position:relative;top:-2px;">' + label + '</span></td>' );
    this._html_array.push( '<td style="width:60%;padding-bottom:2px;"><select name="' + name + '" size="1" style="width:' + parseInt((this.width*3/5)+3,10) + 'px" '+onchange+'>' );
    for( var i=0; i<opts.length; i++) {
      var selected = ( selected_opt == opts[i].key ) ? ' selected="true"' : "";
      this._html_array.push( '<option value="' + opts[i].key + '"' + selected + '>' + opts[i].display + '</option>' );
    }
    this._html_array.push( '</select></td></tr>' );
    this._html_array.push( '</table></td></tr>' );
}


Input_dialog.prototype.add_checkbox = function( name, label, checked ) 
{    
    if( typeof checked      != "boolean" ) checked = false;
    checked = ( checked ) ? ' checked="true"' : "";
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td style="padding-bottom:2px;"><span style="white-space:nowrap;"><input type="checkbox" name="' + name + '" value="1"' + checked + '/>&#160;'+label+'</span></td>' );
    this._html_array.push( '</tr>' );
}


Input_dialog.prototype.add_hidden = function( name, val ) 
{    
    this._html_hiddens.push( '<input type="hidden" name="' + name + '" value="' + val + '"/>' );
}


Input_dialog.prototype.add_textarea = function( name, val, rows ) 
{    
    if( typeof rows == "undefined" ) rows = 16;
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td style="padding-bottom:2px;"><textarea name="' + name + '" style="width:' + this.width + 'px" rows="' + rows + '"/>' + val + '</textarea>' );
    this._html_array.push( '</tr>' );
} 


Input_dialog.prototype.add_params = function( params, param_names ) 
{    
    this.add_hidden( "_param_names", param_names.join(";") );
      
    this._with_params = true;
    var first = true;
    for( var i = 0; i < param_names.length; i++ ) {
        if( param_names[i].search( /^(scheduler_order_(managed_version|managed_id|id|title|job_chain|state)|std_(out|err)_output)$/ ) > -1 ) {
            this.add_hidden( param_names[i], params[param_names[i]] );
            continue;
        } 
        if( first ) {
            first = false;
            this._html_array.push( '<tr>' );
            this._html_array.push( '<td style="padding-top:8px;"><b>'+parent.getTranslation('Change parameters')+'</b></td>' );
            this._html_array.push( '</tr>' );
            this._html_array.push( '<tr><td style="padding:0px;">' );
            this._html_array.push( '<table cellspacing="0" cellpadding="0" width="100%" border="0">' );
            //this._html_array.push( '<colgroup><col width="40%"><col width="*"><col width="1%"></colgroup><tbody>' );
            this._html_array.push( '<colgroup><col width="40%"><col width="60%"></colgroup><tbody>' );  
        }
        if( param_names[i] == "command" ) {
        		var isHex = params[param_names[i]].ishex() ? "true" : "false";
            this._html_array.push( '<tr>' );
            this._html_array.push( '<td class="param_name" colspan="3" style="width:100%;padding-right:2px;"><span style="position:relative;top:-2px;">' + param_names[i] + '</span></td></tr>' );
            this._html_array.push( '<tr>' );
            this._html_array.push( '<td colspan="2" style="width:100%;padding-bottom:2px;">' );
            this._html_array.push( '<textarea sos_value_ishex="'+isHex+'" name="' + param_names[i] + '" style="width:' + (this.width-1) + 'px" rows="4" onfocus="this.select();">' + params[param_names[i]].hex2bin() + '</textarea>' );
            //this._html_array.push( '</td><td onclick="this.parentNode.parentNode.removeChild(this.parentNode);"><div class="delete"></div></td></tr>' );
            this._html_array.push( '</td></tr>' );
        } else {
            this._html_array.push( '<tr>' );
            this._html_array.push( '<td class="param_name" style="padding-right:2px;"><span style="position:relative;top:-2px;">' + param_names[i] + '</span></td>' );
            this._html_array.push( '<td style="padding-bottom:2px;text-align:right;">' );
            this._html_array.push( '<input type="text" name="' + param_names[i] + '" value="' + params[param_names[i]] + '" style="width:' + parseInt((this.width*3/5)-1,10) + 'px"/>' );
            //this._html_array.push( '</td><td onclick="this.parentNode.parentNode.removeChild(this.parentNode);"><div class="delete"></div></td></tr>' );
            this._html_array.push( '</td></tr>' );
        }
    }
    if( !first ) {
      this._html_array.push( '</tbody></table></td></tr>' );
    } else {
      this._with_new_params = true;
    }
    
    this._html_array.push( '<tr id="__input_new_parameters_title__" style="display:none">' );
    this._html_array.push( '<td style="padding-top:8px;"><b>'+(this._with_new_params ? parent.getTranslation('Declare parameters') : parent.getTranslation('Declare new parameters') )+'</b></td>' );
    this._html_array.push( '</tr>' );
    this._html_array.push( '<tr id="__input_new_parameters_tr__" style="display:none">' );
    this._html_array.push( '<td id="__input_new_parameters__" style="padding:0px;"></td>' );
    this._html_array.push( '</tr>' );
}


Input_dialog.add_new_params = function( width, input_form, with_shadow ) 
{    
    if( typeof with_shadow != "boolean" ) with_shadow = true;
    var count_new_params_element = input_form.elements["count_new_params"];
    var count_new_params         = parseInt(count_new_params_element.value,10);
    var input_new_parameters     = parent.left_frame.document.getElementById('__input_new_parameters__');
      
    if( count_new_params == 0 ) {
      var input_new_parameters_title = parent.left_frame.document.getElementById('__input_new_parameters_title__');
      var input_new_parameters_tr    = parent.left_frame.document.getElementById('__input_new_parameters_tr__');
      if( input_new_parameters_title ) input_new_parameters_title.style.display = ( parent._scheduler._ie > 0 ) ? "block" : "table-row";
      if( input_new_parameters_tr    ) input_new_parameters_tr.style.display    = ( parent._scheduler._ie > 0 ) ? "block" : "table-row";
      if( input_new_parameters       ) { 
        var new_param_html = '<table cellspacing="0" cellpadding="0" width="100%" border="0">';
        new_param_html    += '<colgroup><col width="40%"><col width="*"><col width="1%"></colgroup><tbody id="__input_new_parameters_body__">';
        new_param_html    += '<tr><th style="width:40%" >'+parent.getTranslation('name')+'</th><th colspan="2" style="width:*">'+parent.getTranslation('value')+'</th></tr>';
        new_param_html    += '</tbody></table>';
        input_new_parameters.innerHTML = new_param_html;
      }
    }
    
    if(parent.left_frame.document.getElementById('__input_new_parameters_body__')) {
      Input_dialog.add_new_param( width, count_new_params );
    }
    parent.left_frame.focus();
    if(input_form.elements["new_param_name_"+count_new_params]) {
      input_form.elements["new_param_name_"+count_new_params].focus();
    }
         
    count_new_params++;
    count_new_params_element.value = count_new_params;
    if(with_shadow) Input_dialog.add_shadow();
}
  
  
Input_dialog.add_new_param = function( width, idx ) 
{
    var newTR                 = parent.left_frame.document.createElement("tr");
    parent.left_frame.document.getElementById('__input_new_parameters_body__').appendChild(newTR);
    var newTD                 = parent.left_frame.document.createElement("td");
    newTR.appendChild(newTD);
    newTD.setAttribute('style', '');
    newTD.style.width         = "40%";
    newTD.style.paddingRight  = "2px";
    newTD.style.paddingBottom = "2px";
    var newTD2                = parent.left_frame.document.createElement("td");
    newTR.appendChild(newTD2);
    newTD2.setAttribute('style', '');
    newTD2.style.width         = "60%";
    newTD2.style.textAlign = "right";
    var newTD3                 = parent.left_frame.document.createElement("td");
    newTR.appendChild(newTD3);
    newTD3.setAttribute('onclick',"this.parentNode.parentNode.removeChild(this.parentNode);");
    //var newDeleteDiv          = parent.left_frame.document.createElement("div");
    //newTD3.appendChild(newDeleteDiv);
    //newDeleteDiv.className    = "delete";
    if( parent._scheduler._ie > 0 ) {
      newTD.innerHTML  = '<input type="text" name="new_param_name_' + idx + '" value="" style="width:' + parseInt((width*2/5)-4,10) + 'px;" />';
      newTD2.innerHTML = '<input type="text" name="new_param_value_' + idx + '" value="" style="width:' + parseInt((width*3/5)-1,10) + 'px;" />';
    } else {
      var newINPUT = parent.left_frame.document.createElement("input");
      newTD.appendChild(newINPUT);
      newINPUT.setAttribute('type', 'text');
      newINPUT.setAttribute('name', 'new_param_name_' + idx);
      newINPUT.setAttribute('value', '');
      newINPUT.setAttribute('style', '');
      newINPUT.style.width      = parseInt((width*2/5)-4,10) + 'px';
      //newTR.appendChild(newTD);
      var newINPUT2 = parent.left_frame.document.createElement("input");
      newTD2.appendChild(newINPUT2);
      newINPUT2.setAttribute('type', 'text');
      newINPUT2.setAttribute('name', 'new_param_value_' + idx);
      newINPUT2.setAttribute('value', '');
      newINPUT2.setAttribute('style', '');
      newINPUT2.style.width     = parseInt((width*3/5)-1,10) + 'px';
      //newTR.appendChild(newTD2);
    }
}    

 
Input_dialog.prototype.html = function()
{
    var html = '<div class="inner_input"><form name="__input_dialog__" onsubmit="return false;" style="margin:0px;">';
    html    += '<table id="__input_table__" cellspacing="0" cellpadding="0" width="' + this.width + 'px" style="margin:5px;" border="0">';
    this._html_array.push( '<tr>' );
    this._html_array.push( '<td style="white-space:nowrap;text-align:center;padding-top:4px;">' );
    if(this.submit_title) {
      var submit_onclick = (this.submit_fct) ? this.submit_fct+";" : ""; 
      submit_onclick    += (this.close_after_submit) ? "Input_dialog.close();return false;" : "return false;";
      if( this._with_reload ) {
        this._html_array.push( '<input class="buttonbar" type="submit" value=" '+parent.getTranslation(this.submit_title)+' " onclick="if(' + this.submit_fct + ') window.parent.location.reload(true);"/>' );
      } else {
        this._html_array.push( '<input class="buttonbar" type="submit" value=" '+parent.getTranslation(this.submit_title)+' " onclick="' + submit_onclick + '"/>' );
      }
    }
    if( this._with_params ) this._html_array.push( '<input class="buttonbar" type="button" value=" '+parent.getTranslation('new param')+' " onclick="Input_dialog.add_new_params(' + this.width + ',this.form);"/>' );
    this._html_array.push( '<input class="buttonbar" type="button" value=" '+parent.getTranslation('cancel')+' " onclick="Input_dialog.close()"/></td>' );
    this._html_array.push( '</tr>' );
    this._html_array.push( '</table>' );
    this.add_hidden( "count_new_params", 0 );
    html    += this._html_array.join( "" );
    html    += this._html_hiddens.join( "" );
    html    += '</form></div>';
    
    return html;
}


Input_dialog.show_calendar = function(formElement,fullDatetime,evt){
    
    if(window['SOS_Calendar']) {                      
      SOS_Calendar.language      = (parent._scheduler._lang_file_exists ? parent._sos_lang : 'en');
      SOS_Calendar.fullYear      = true;
      SOS_Calendar.returnISO     = true;
      SOS_Calendar.onFillForm    = '';
      SOS_Calendar.showHolidays  = false;
      SOS_Calendar.imgDir        = '';
      SOS_Calendar.indent        = 8;
      SOS_Calendar.bgSaturday    = 'white';
      SOS_Calendar.bgSunday      = 'white';
      SOS_Calendar.bgDayCurrent  = '';
      SOS_Calendar.bgDaySelected = '#F2F4F7';
      SOS_Calendar.borderDays    = '';
      SOS_Calendar.skin          = '.';
      SOS_Calendar.fullDatetime  = fullDatetime;
      SOS_Calendar.show(evt,'__input_dialog__.'+formElement);
    }
}


//-------------------------------------------------------------------------------------------------